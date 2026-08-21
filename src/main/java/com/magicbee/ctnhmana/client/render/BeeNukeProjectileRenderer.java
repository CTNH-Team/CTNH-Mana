package com.magicbee.ctnhmana.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;

import com.magicbee.ctnhmana.common.entity.projectile.BeeNukeProjectile;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * 蜜蜂核弹投掷物渲染器：渲染其携带的蜜蜡物品模型（参考原版 ItemEntityRenderer）。
 */
public class BeeNukeProjectileRenderer extends EntityRenderer<BeeNukeProjectile> {

    public BeeNukeProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BeeNukeProjectile entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(0.6F, 0.6F, 0.6F);
        Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemDisplayContext.GROUND, light,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(BeeNukeProjectile entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
