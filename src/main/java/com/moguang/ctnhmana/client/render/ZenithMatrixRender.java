package com.moguang.ctnhmana.client.render;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
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

    public ZenithMatrixRender() {}

    @Override
    public DynamicRenderType<IMachineFeature, ZenithMatrixRender> getType() {
        return TYPE;
    }

    @Override
    public int getViewDistance() {
        return 1024;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(IMachineFeature feature, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {
        var metaMachine = feature.self();
        float[] color = { 1.0F, 0.2F, 0.8F, 1.0F };

        if (metaMachine instanceof ZenithMatrixMachine machine && machine.isFormed()) {

            var level = machine.getLevel();
            var pos = machine.getPos();

            BeaconRenderer.renderBeaconBeam(
                    poseStack,
                    buffer,
                    BeaconRenderer.BEAM_LOCATION,
                    partialTick,
                    1F,
                    level.getGameTime(),
                    pos.getY() + 1,
                    320,
                    color,
                    0.25F,
                    0.25F);
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
