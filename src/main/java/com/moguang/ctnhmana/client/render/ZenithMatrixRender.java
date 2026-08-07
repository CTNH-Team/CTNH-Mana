package com.moguang.ctnhmana.client.render;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.moguang.ctnhmana.client.ClientProxy;
import com.moguang.ctnhmana.common.multiblock.ZenithMatrixMachine;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.Codec;

/**
 * 天顶矩阵的动态渲染器，负责：
 * 1. 在天顶之眼位置渲染向上的自定义 shader 光柱。
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

    /** 光柱侧面的细分段数。 */
    private static final int BEAM_SIDES = 48;
    /** 光柱高度：从眼睛直达天空锚点。 */
    private static final float BEAM_HEIGHT = 320.0F;

    /**
     * 天空裂缝效果的剩余存活刻数。
     * 每次 clientTick 中调用 markSkyEffectSource 时会被重置为 2，
     * 因此只要机器处于成形状态，该值会保持大于 0，天空效果持续渲染。
     */
    public static int skyEffectTicks = 0;
    /** 形成动画剩余刻数。从 FORMATION_DURATION 递减到 0，控制闪屏、震动、睁眼等一次性效果。 */
    public static int formationAnimTicks = 0;
    /**
     * 开扉过程中的屏幕震动强度（0~1），由机器客户端同步。
     * 与形成动画震动叠加使用。
     */
    public static float doorOpenShakeIntensity = 0.0F;
    /**
     * 开扉进度（0~1），由机器 clientTick 写入；渲染用 {@link #doorOpenPurpleDisplay}，每 5 秒采样一次。
     */
    public static float doorOpenProgress = 0.0F;
    /**
     * 实际用于屏幕泛紫的进度采样值（0~1），每 {@link #DOOR_PURPLE_SAMPLE_INTERVAL} 刻更新一次。
     */
    public static float doorOpenPurpleDisplay = 0.0F;
    /** 开扉泛紫采样间隔：5 秒。 */
    public static final int DOOR_PURPLE_SAMPLE_INTERVAL = 5 * 20;
    private static int doorPurpleSampleTicks = 0;

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
    /** 是否由虚境入侵限时事件驱动天空特效 */
    private static boolean timedSkyEffectActive = false;

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
     * 由 ZenithMatrixMachine.clientTick 在机器成形后每刻调用，
     * 或由虚境入侵客户端镜像维持心跳。
     *
     * @param sourcePos 天顶之眼方块位置
     */
    public static void markSkyEffectSource(BlockPos sourcePos) {
        // 使用 immutable() 避免外部修改影响静态源点。
        skyEffectTicks = 2;
        skyEffectSourcePos = sourcePos.immutable();
    }

    /**
     * 启动限时天空特效（虚境入侵）。
     *
     * @param sourcePos     锚点
     * @param durationTicks 剩余时长
     * @param playIntro     是否播放形成开场动画
     */
    public static void beginTimedSkyEffect(BlockPos sourcePos, int durationTicks, boolean playIntro) {
        timedSkyEffectActive = true;
        // durationTicks 由 ZenithInvadeClient 镜像维护；此处只做心跳续命 + 可选开场
        skyEffectSourcePos = sourcePos.immutable();
        skyEffectTicks = Math.max(skyEffectTicks, 2);
        if (playIntro && durationTicks > 0) {
            formationAnimTicks = FORMATION_DURATION;
        }
    }

    /** 清除限时天空特效标记（事件全部结束时调用）。 */
    public static void clearTimedSkyEffect() {
        timedSkyEffectActive = false;
        // 留给机器成形心跳续命；若机器未成形则下几 tick 自然清空
        skyEffectTicks = Math.min(skyEffectTicks, 2);
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
            timedSkyEffectActive = false;
        }
        if (formationAnimTicks > 0) {
            formationAnimTicks--;
        }
        // 开门震动由机器侧写入，此处自然衰减以免残留
        if (doorOpenShakeIntensity > 0 && formationAnimTicks <= 0) {
            doorOpenShakeIntensity *= 0.85F;
            if (doorOpenShakeIntensity < 0.01F) {
                doorOpenShakeIntensity = 0;
            }
        }
        // 开扉进度：配方结束后衰减；渲染采样每 5 秒刷新一次，避免每 tick 改紫度
        if (doorOpenProgress > 0 && doorOpenShakeIntensity < 0.01F && formationAnimTicks <= 0) {
            doorOpenProgress *= 0.9F;
            if (doorOpenProgress < 0.01F) {
                doorOpenProgress = 0;
            }
        }
        doorPurpleSampleTicks++;
        if (doorOpenProgress > 0.01F && doorOpenPurpleDisplay <= 0.01F) {
            // 开扉刚开始：立刻采一次样，之后每 5 秒再改
            doorOpenPurpleDisplay = doorOpenProgress;
            doorPurpleSampleTicks = 0;
        } else if (doorPurpleSampleTicks >= DOOR_PURPLE_SAMPLE_INTERVAL) {
            doorPurpleSampleTicks = 0;
            doorOpenPurpleDisplay = doorOpenProgress;
        } else if (doorOpenProgress <= 0.01F && doorOpenPurpleDisplay > 0) {
            // 结束后尽快清显示采样，避免残留满强度紫幕
            doorOpenPurpleDisplay *= 0.85F;
            if (doorOpenPurpleDisplay < 0.01F) {
                doorOpenPurpleDisplay = 0;
            }
        }
    }

    /** 当前是否存在有效的天空效果源点。 */
    public static boolean hasSkyEffectSource() {
        return skyEffectTicks > 0 && skyEffectSourcePos != null;
    }

    public static boolean isTimedSkyEffectActive() {
        return timedSkyEffectActive && hasSkyEffectSource();
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
     * 使用自定义 GLSL shader（zenith_beam）绘制一个圆柱形能量柱，
     * 在片段着色器中生成螺旋、噪声流动与边缘发光效果。
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

            ShaderInstance beamShader = ClientProxy.getZenithBeamShader();
            if (beamShader == null) return;

            // 将坐标系原点从机器控制器移动到天顶之眼方块中心。
            var eyePos = machine.getZenithEyePos();
            var localEyeX = eyePos.getX() - machine.getPos().getX();
            var localEyeY = eyePos.getY() - machine.getPos().getY();
            var localEyeZ = eyePos.getZ() - machine.getPos().getZ();

            poseStack.pushPose();
            poseStack.translate(localEyeX + 0.5D, localEyeY, localEyeZ + 0.5D);

            // 连续时间，包含 partialTick 保证动画流畅。
            float time = level.getGameTime() + partialTick;

            // ========== 光柱动态参数 ==========
            float baseRadius = 0.4F;
            // 亚空间能量束：半径稳定，不呼吸抖动。
            // 形成瞬间的能量爆发。
            float formationBoost = 1.0F;
            if (formationAnimTicks > 0) {
                float p = 1.0F - (float) formationAnimTicks / FORMATION_DURATION;
                formationBoost = 1.0F + 2.0F * Mth.sin(p * Mth.PI) * (1.0F - p);
            }
            float radius = baseRadius * formationBoost;

            // ========== 设置自定义 shader ==========
            RenderSystem.setShader(() -> beamShader);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(515);

            // 传递 Uniform：Time 驱动 shader 内部动画。
            if (beamShader.safeGetUniform("Time") != null) {
                beamShader.safeGetUniform("Time").set(time * 0.05f);
            }
            // 传递 Uniform：BeamColor 使用天顶主题色。
            if (beamShader.safeGetUniform("BeamColor") != null) {
                beamShader.safeGetUniform("BeamColor").set(
                        ZENITH_BEAM_COLOR[0], ZENITH_BEAM_COLOR[1], ZENITH_BEAM_COLOR[2]);
            }
            // 传递 Uniform：BeamAlpha 控制整体不透明度。
            if (beamShader.safeGetUniform("BeamAlpha") != null) {
                beamShader.safeGetUniform("BeamAlpha").set(1.0f);
            }

            // ========== 构建圆柱网格 ==========
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.getBuilder();
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            try {
                modelViewStack.mulPoseMatrix(poseStack.last().pose());
                RenderSystem.applyModelViewMatrix();

                for (int i = 0; i < BEAM_SIDES; i++) {
                    float a0 = (float) i / BEAM_SIDES * Mth.TWO_PI;
                    float a1 = (float) (i + 1) / BEAM_SIDES * Mth.TWO_PI;
                    float x0 = Mth.cos(a0) * radius;
                    float z0 = Mth.sin(a0) * radius;
                    float x1 = Mth.cos(a1) * radius;
                    float z1 = Mth.sin(a1) * radius;
                    float u0 = (float) i / BEAM_SIDES;
                    float u1 = (float) (i + 1) / BEAM_SIDES;

                    // 顶点保持在光柱局部空间，避免 shader 动画随相机矩阵变化。
                    builder.vertex(x0, 0, z0).uv(u0, 0).endVertex();
                    builder.vertex(x0, BEAM_HEIGHT, z0).uv(u0, 1).endVertex();
                    builder.vertex(x1, BEAM_HEIGHT, z1).uv(u1, 1).endVertex();
                    builder.vertex(x1, 0, z1).uv(u1, 0).endVertex();
                }

                tesselator.end();
            } finally {
                modelViewStack.popPose();
                RenderSystem.applyModelViewMatrix();
            }

            // 恢复渲染状态。
            RenderSystem.disableDepthTest();// 恢复渲染状态。
            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();

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
