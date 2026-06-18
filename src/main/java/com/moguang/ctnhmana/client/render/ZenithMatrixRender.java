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

public class ZenithMatrixRender extends DynamicRender<IMachineFeature, ZenithMatrixRender> {

    public static Codec<ZenithMatrixRender> CODEC = Codec.unit(ZenithMatrixRender::new);
    public static final DynamicRenderType<IMachineFeature, ZenithMatrixRender> TYPE = new DynamicRenderType<>(
            ZenithMatrixRender.CODEC);

    public static int skyEffectTicks = 0;
    public static int formationAnimTicks = 0;
    public static final int FORMATION_DURATION = 80;
    public static final int SHAKE_DELAY = 20;
    public static final int SHAKE_DURATION = 30;
    public static final int FLASH_DURATION = 15;
    public static final float SKY_EFFECT_RADIUS = 512.0F;
    private static final double SKY_ANCHOR_HEIGHT_OFFSET = 96.0D;
    private static final double SKY_ANCHOR_TOP_PADDING = 32.0D;
    private static BlockPos skyEffectSourcePos;

    // 天顶主题色：紫-粉，与 zenith.fsh 统一
    public static final float[] ZENITH_BEAM_COLOR = { 0.9F, 0.15F, 1.0F, 1.0F };

    public ZenithMatrixRender() {}

    @Override
    public DynamicRenderType<IMachineFeature, ZenithMatrixRender> getType() {
        return TYPE;
    }

    @Override
    public int getViewDistance() {
        return 1024;
    }

    public static void markSkyEffectSource(BlockPos sourcePos) {
        skyEffectTicks = 2;
        skyEffectSourcePos = sourcePos.immutable();
    }

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

    public static boolean hasSkyEffectSource() {
        return skyEffectTicks > 0 && skyEffectSourcePos != null;
    }

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

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(IMachineFeature feature, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {
        var metaMachine = feature.self();

        if (metaMachine instanceof ZenithMatrixMachine machine && machine.isFormed()) {

            var level = machine.getLevel();
            if (level == null) return;
            var eyePos = machine.getZenithEyePos();
            var localEyeX = eyePos.getX() - machine.getPos().getX();
            var localEyeY = eyePos.getY() - machine.getPos().getY();
            var localEyeZ = eyePos.getZ() - machine.getPos().getZ();

            poseStack.pushPose();
            poseStack.translate(localEyeX, localEyeY, localEyeZ);

            float time = level.getGameTime() + partialTick;

            // 主光柱：与天空裂缝同步的脉冲
            float baseRadius = 0.25F;
            float pulse = 1.0F + 0.25F * (float) Math.sin(time * 0.25D);
            float coreRadius = baseRadius * pulse;
            float glowRadius = baseRadius * (1.4F + 0.35F * (float) Math.sin(time * 0.18D + 1.0D));

            // 形成瞬间能量爆发：半径随 formationAnimTicks 放大
            float formationBoost = 1.0F;
            if (formationAnimTicks > 0) {
                float p = 1.0F - (float) formationAnimTicks / FORMATION_DURATION;
                formationBoost = 1.0F + 1.5F * (float) Math.sin(p * Math.PI) * (1.0F - p);
            }

            // 外层淡光晕：宽而柔和
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

            // 内层核心：较亮、较细
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
        return new AABB(feature.self().getPos()).inflate(1024);
    }

    @Override
    public boolean shouldRenderOffScreen(IMachineFeature machine) {
        return true;
    }

    @Override
    public boolean shouldRender(IMachineFeature machine, Vec3 cameraPos) {
        return true;
    }
}
