package com.moguang.ctnhmana.client.render;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.moguang.ctnhmana.Mutiblock.ZenithMatrixMachine;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;

/**
 * 天顶矩阵的动态渲染器，负责：
 * 1. 在天顶之眼位置渲染向上的信标光柱。
 * 2. 管理天空裂缝（zenith sky rift）的客户端状态（位置、形成动画计时）。
 *
 * 该类继承自 GTCEu 的 DynamicRender，会在多方块机器被加载时由 DynamicRenderManager 调用。
 */
public class ZenithMatrixRender extends DynamicRender<IMachineFeature, ZenithMatrixRender> {

    /** 动态渲染器序列化/反序列化用的 Codec。 */
    public static Codec<ZenithMatrixRender> CODEC = Codec.unit(ZenithMatrixRender::new);
    /** 注册到 DynamicRenderManager 的渲染类型。 */
    public static final DynamicRenderType<IMachineFeature, ZenithMatrixRender> TYPE = new DynamicRenderType<>(
            ZenithMatrixRender.CODEC);

    /**
     * 天空裂缝效果的剩余存活刻数。
     * 每次 clientTick 中调用 markSkyEffectSource 时会被重置为 2，
     * 因此只要机器处于成形状态，该值会保持大于 0，天空效果持续渲染。
     */
    public static int skyEffectTicks = 0;
    /** 形成动画剩余刻数。从 FORMATION_DURATION 递减到 0，控制闪屏、震动、睁眼等一次性效果。 */
    public static int formationAnimTicks = 0;

    /** 形成动画总时长：80 刻 = 4 秒（20 TPS）。 */
    public static final int FORMATION_DURATION = 80;
    /** 屏幕震动相对于形成动画开始刻的延迟。 */
    public static final int SHAKE_DELAY = 20;
    /** 屏幕震动持续刻数。 */
    public static final int SHAKE_DURATION = 30;
    /** 屏幕闪光持续刻数。 */
    public static final int FLASH_DURATION = 15;

    /** 天空裂缝在 Y 轴上方平面的渲染半径。 */
    public static final float SKY_EFFECT_RADIUS = 512.0F;
    /** 天空锚点相对于天顶之眼方块的最小高度偏移。 */
    private static final double SKY_ANCHOR_HEIGHT_OFFSET = 96.0D;
    /** 天空锚点在世界建筑高度之上的额外内边距。 */
    private static final double SKY_ANCHOR_TOP_PADDING = 32.0D;

    /** 当前天空效果的世界源点位置（天顶之眼坐标），用于渲染天空层时定位。 */
    private static BlockPos skyEffectSourcePos;

    /** 天顶主题色：紫-粉，与 shaders/core/zenith.fsh 中的 ZENITH_BRIGHT / ZENITH_CORE 保持一致。 */
    public static final float[] ZENITH_BEAM_COLOR = { 0.9F, 0.15F, 1.0F, 1.0F };

    public ZenithMatrixRender() {}

    @Override
    public DynamicRenderType<IMachineFeature, ZenithMatrixRender> getType() {
        return TYPE;
    }

    @Override
    public int getViewDistance() {
        // 光柱与天空效果都可能很高/很远，需要更大的渲染距离。
        return 1024;
    }

    /**
     * 标记天空效果应从哪个世界坐标开始渲染。
     * 由 ZenithMatrixMachine.clientTick 在机器成形后每刻调用。
     *
     * @param sourcePos 天顶之眼方块位置
     */
    public static void markSkyEffectSource(BlockPos sourcePos) {
        // 使用 immutable() 避免外部修改影响静态源点。
        skyEffectTicks = 2;
        skyEffectSourcePos = sourcePos.immutable();
    }

    /**
     * 客户端每刻更新一次效果状态。
     * 递减 skyEffectTicks 与 formationAnimTicks，并在天空效果到期后清空源点。
     */
    public static void tickClientEffects() {
        if (skyEffectTicks > 0) {
            skyEffectTicks--;
        }
        if (skyEffectTicks <= 0) {
            skyEffectSourcePos = null;
        }
        if (formationAnimTicks > 0) {
            formationAnimTicks--;
        }
    }

    /** 当前是否存在有效的天空效果源点。 */
    public static boolean hasSkyEffectSource() {
        return skyEffectTicks > 0 && skyEffectSourcePos != null;
    }

    /**
     * 计算天空效果锚点相对于相机的偏移。
     * 锚点取“世界最大建筑高度 + 内边距”与“天顶之眼 Y + 高度偏移”中的较大值，
     * 确保天空层始终出现在玩家头顶上方。
     *
     * @param cameraPos      当前相机位置
     * @param maxBuildHeight 世界最大建筑高度
     * @return 锚点相对于相机的偏移向量；无有效源点时返回 null
     */
    public static Vec3 getSkyEffectAnchorOffset(Vec3 cameraPos, int maxBuildHeight) {
        if (!hasSkyEffectSource()) return null;

        double anchorY = Math.max(
                maxBuildHeight + SKY_ANCHOR_TOP_PADDING,
                skyEffectSourcePos.getY() + SKY_ANCHOR_HEIGHT_OFFSET);
        Vec3 anchorPos = new Vec3(
                skyEffectSourcePos.getX() + 0.5D,
                anchorY,
                skyEffectSourcePos.getZ() + 0.5D);
        return anchorPos.subtract(cameraPos);
    }

    /**
     * 实际渲染光柱。
     * 这里绘制两层信标光束：外层宽光晕 + 内层亮核心，并加入时间脉动与形成爆发效果。
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(IMachineFeature feature, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {
        var metaMachine = feature.self();

        // 仅对成形的天顶矩阵生效。
        if (metaMachine instanceof ZenithMatrixMachine machine && machine.isFormed()) {

            var level = machine.getLevel();
            if (level == null) return;

            // 将坐标系原点从机器控制器移动到天顶之眼方块。
            var eyePos = machine.getZenithEyePos();
            var localEyeX = eyePos.getX() - machine.getPos().getX();
            var localEyeY = eyePos.getY() - machine.getPos().getY();
            var localEyeZ = eyePos.getZ() - machine.getPos().getZ();

            poseStack.pushPose();
            poseStack.translate(localEyeX, localEyeY, localEyeZ);

            // 连续时间，包含 partialTick 保证动画流畅。
            float time = level.getGameTime() + partialTick;

            // ========== 光柱半径计算 ==========
            // 基础半径。
            float baseRadius = 0.25F;
            // 核心半径：以 sin 波做呼吸式脉动，幅度 25%。
            float pulse = 1.0F + 0.25F * (float) Math.sin(time * 0.25D);
            float coreRadius = baseRadius * pulse;
            // 外层光晕：更宽、相位略有偏移，产生“光圈荡漾”感。
            float glowRadius = baseRadius * (1.4F + 0.35F * (float) Math.sin(time * 0.18D + 1.0D));

            // 形成瞬间的能量爆发：在 formationAnimTicks 期间额外放大半径，形成“睁开时能量喷涌”的视觉。
            float formationBoost = 1.0F;
            if (formationAnimTicks > 0) {
                // p: 0 -> 1，表示形成动画进度。
                float p = 1.0F - (float) formationAnimTicks / FORMATION_DURATION;
                // sin(p * π) 在 0 与 1 处为 0，中间达到峰值；(1-p) 让爆发后段逐渐回落。
                formationBoost = 1.0F + 1.5F * (float) Math.sin(p * Math.PI) * (1.0F - p);
            }

            // ========== 外层淡光晕 ==========
            // 宽而柔和，X/Z 半径相同，呈现基础辉光。
            BeaconRenderer.renderBeaconBeam(
                    poseStack,
                    buffer,
                    BeaconRenderer.BEAM_LOCATION,
                    partialTick,
                    1F,
                    time,
                    0,
                    320,
                    ZENITH_BEAM_COLOR,
                    glowRadius * formationBoost,
                    glowRadius * 0.6F * formationBoost);

            // ========== 内层核心 ==========
            // 颜色比外层略暗一点（G 通道降低），半径更小，显得“实心”。
            float[] coreColor = { ZENITH_BEAM_COLOR[0], ZENITH_BEAM_COLOR[1] * 0.85F, ZENITH_BEAM_COLOR[2], 1.0F };
            BeaconRenderer.renderBeaconBeam(
                    poseStack,
                    buffer,
                    BeaconRenderer.BEAM_LOCATION,
                    partialTick,
                    1F,
                    time,
                    0,
                    320,
                    coreColor,
                    coreRadius * formationBoost,
                    coreRadius * 0.5F * formationBoost);

            poseStack.popPose();
        }
    }

    @Override
    public AABB getRenderBoundingBox(IMachineFeature feature) {
        // 光柱与天空效果覆盖范围很大，因此渲染包围盒也取得很大。
        return new AABB(feature.self().getPos()).inflate(1024);
    }

    @Override
    public boolean shouldRenderOffScreen(IMachineFeature machine) {
        // 即使不在屏幕内也可能因为光柱很高而需要渲染。
        return true;
    }

    @Override
    public boolean shouldRender(IMachineFeature machine, Vec3 cameraPos) {
        // 始终尝试渲染，由 viewDistance 与视锥进一步裁剪。
        return true;
    }
}
