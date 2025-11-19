package com.moguang.ctnhmana.client.render;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.moguang.ctnhmana.common.Mutiblock.ZENITH_MATRIX;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3d;

import static com.hollingsworth.arsnouveau.client.ClientInfo.partialTicks;

public class ZenithMatrixBlockEntityRender extends DynamicRender<IMachineFeature, ZenithMatrixBlockEntityRender> {
    public static Codec<ZenithMatrixBlockEntityRender> CODEC = Codec.unit(ZenithMatrixBlockEntityRender::new);
    public static final DynamicRenderType<IMachineFeature, ZenithMatrixBlockEntityRender> TYPE = new DynamicRenderType<>(ZenithMatrixBlockEntityRender.CODEC);
    public ZenithMatrixBlockEntityRender() {
    }

    @Override
    public DynamicRenderType<IMachineFeature, ZenithMatrixBlockEntityRender> getType() {
        return TYPE;
    }
    @Override
    public int getViewDistance() {
        return 1024;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(IMachineFeature feature, float gameTime, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        var metaMachine = feature.self();
        float[] color = {1.0F, 0.2F, 0.8F, 1.0F};
        if (metaMachine instanceof ZENITH_MATRIX machine && machine.isFormed()) {
            var locate =machine.getPos();
            BeaconRenderer.renderBeaconBeam(
                    poseStack,
                    buffer,
                    BeaconRenderer.BEAM_LOCATION,
                    partialTicks,
                    1F,  // 纹理缩放（值越小纹理重复越密集）
                    machine.getLevel().getGameTime(),
                    locate.getY() +1,  // 起始Y坐标（方块顶部）
                    200,  // 光束高度
                    color,
                    0.25F,  // 主体半径
                    0.25F   // 光晕半径
            );

           // EnderDragonRenderer.renderCrystalBeams(locate.getX(), locate.getY()+4, locate.getZ(), machine.getLevel().getGameTime() + gameTime, 2000, poseStack, buffer, 15);

        }
    }
    public boolean shouldRender(MultiblockControllerMachine machine, Vec3 cameraPos) {
        return true;
    }

}
