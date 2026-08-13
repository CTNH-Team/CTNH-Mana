package com.magicbee.ctnhmana.client.render;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;

import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.magicbee.ctnhmana.common.multiblock.MachineUtils;
import com.magicbee.ctnhmana.common.multiblock.ManaCondenserMachine;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;

import java.util.List;

public class ManaCondenserRender extends DynamicRender<IMachineFeature, ManaCondenserRender> {

    public static Codec<ManaCondenserRender> CODEC = Codec.unit(ManaCondenserRender::new);
    public static final DynamicRenderType<IMachineFeature, ManaCondenserRender> TYPE = new DynamicRenderType<>(
            ManaCondenserRender.CODEC);

    public ManaCondenserRender() {}

    @Override
    public DynamicRenderType<IMachineFeature, ManaCondenserRender> getType() {
        return TYPE;
    }

    @Override
    public boolean shouldRenderOffScreen(IMachineFeature machine) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 48;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(IMachineFeature feature, float gameTime, PoseStack poseStack, MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {
        var metaMachine = feature.self();
        if (metaMachine instanceof ManaCondenserMachine machine && machine.isFormed() &&
                (machine.isActive() || machine.getLevel() instanceof TrackedDummyWorld)) {
            List<BlockPos> target = List.of(
                    MachineUtils.getOffset(machine, 0, 11, 13),
                    MachineUtils.getOffset(machine, 0, 11, -13),
                    MachineUtils.getOffset(machine, 8, 11, 8),
                    MachineUtils.getOffset(machine, 8, 11, -8),
                    MachineUtils.getOffset(machine, -8, 11, 8),
                    MachineUtils.getOffset(machine, -8, 11, -8),
                    MachineUtils.getOffset(machine, 13, 11, 0),
                    MachineUtils.getOffset(machine, -13, 11, 0),
                    MachineUtils.getOffset(machine, 5, 8, 5),
                    MachineUtils.getOffset(machine, 5, 8, -5),
                    MachineUtils.getOffset(machine, -5, 8, 5),
                    MachineUtils.getOffset(machine, -5, 8, -5));
            BlockPos core = MachineUtils.getOffset(machine, 0, 5, 0);
            boolean reverse = false;
            int r = reverse ? 255 : 0;
            int g = reverse ? 50 : 255;
            int b = reverse ? 50 : 255;
            if (machine.getRecipeLogic().getLastRecipe().data.get("mode") != null &&
                    machine.getRecipeLogic().getLastRecipe().data.getString("mode").equals("reverse")) {
                reverse = true;
            }

            for (BlockPos pos : target) {
                float x = pos.getX() - core.getX();
                float y = pos.getY() - core.getY();
                float z = pos.getZ() - core.getZ();
                if (reverse) {
                    renderCustomBeam(poseStack, buffer, -x, -y, -z, machine.getLevel().getGameTime() + gameTime,
                            0xF000F0, r, g, b);
                } else {
                    renderCustomBeam(poseStack, buffer, x, y, z, machine.getLevel().getGameTime() + gameTime, 0xF000F0,
                            r, g, b);
                }

            }
        }
    }

    private void renderCustomBeam(PoseStack poseStack, MultiBufferSource buffer, float x, float y, float z,
                                  float gameTime, int packedLight, int r, int g, int b) {
        float f = Mth.sqrt(x * x + z * z);
        float f1 = Mth.sqrt(x * x + y * y + z * z);

        poseStack.pushPose();
        poseStack.translate(0.5F, 5.5F, 0.5F);// 改这
        poseStack.mulPose(Axis.YP.rotation((float) (-Math.atan2(z, x)) - ((float) Math.PI / 2F)));
        poseStack.mulPose(Axis.XP.rotation((float) (-Math.atan2(f, y)) - ((float) Math.PI / 2F)));

        VertexConsumer vertexconsumer = buffer
                .getBuffer(RenderType.entitySmoothCutout(EnderDragonRenderer.CRYSTAL_BEAM_LOCATION));

        float time = gameTime * 0.01F;
        float f3 = f1 / 32.0F - time;

        float f4 = 0.0F;
        float f5 = 0.75F;

        for (int j = 1; j <= 8; ++j) {
            float f7 = Mth.sin(j * ((float) Math.PI * 2F) / 8.0F) * 0.75F; // 可通过乘数调整粗细
            float f8 = Mth.cos(j * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
            float f9 = (float) j / 8.0F;

            vertexconsumer.vertex(poseStack.last().pose(), f4 * 0.2F, f5 * 0.2F, 0.0F).color(0, 0, 0, 255).uv(f9, -time)
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight)
                    .normal(poseStack.last().normal(), 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(poseStack.last().pose(), f4, f5, f1).color(r, g, b, 255).uv(f9, f3)
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight)
                    .normal(poseStack.last().normal(), 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(poseStack.last().pose(), f7, f8, f1).color(r, g, b, 255).uv(f9, f3)
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight)
                    .normal(poseStack.last().normal(), 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(poseStack.last().pose(), f7 * 0.2F, f8 * 0.2F, 0.0F).color(0, 0, 0, 255).uv(f9, -time)
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight)
                    .normal(poseStack.last().normal(), 0.0F, -1.0F, 0.0F).endVertex();

            f4 = f7;
            f5 = f8;
        }
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(IMachineFeature feature) {
        return new AABB(feature.self().getPos()).inflate(15);
    }
}
