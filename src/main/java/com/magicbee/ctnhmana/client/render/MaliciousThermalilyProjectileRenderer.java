package com.magicbee.ctnhmana.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.common.entity.projectile.MaliciousThermalilyProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * 恶意热爆花投掷物渲染器：用 entropinnyum_boom 贴图渲染为 billboard 方块状实体。
 */
public class MaliciousThermalilyProjectileRenderer extends EntityRenderer<MaliciousThermalilyProjectile> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CTNHMana.MODID,
            "block/entropinnyum_boom");

    public MaliciousThermalilyProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MaliciousThermalilyProjectile entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(0.5F, 0.5F, 0.5F);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(TEXTURE);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        float half = 0.5F;
        int packedOverlay = OverlayTexture.pack(OverlayTexture.u(0.0F), OverlayTexture.v(false));
        // billboard 四边形（渲染方块贴图）
        consumer.vertex(poseStack.last().pose(), -half, -half, 0).color(1F, 1F, 1F, 1F)
                .uv(u0, v1).overlayCoords(packedOverlay).uv2(light)
                .normal(poseStack.last().normal(), 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(poseStack.last().pose(), half, -half, 0).color(1F, 1F, 1F, 1F)
                .uv(u1, v1).overlayCoords(packedOverlay).uv2(light)
                .normal(poseStack.last().normal(), 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(poseStack.last().pose(), half, half, 0).color(1F, 1F, 1F, 1F)
                .uv(u1, v0).overlayCoords(packedOverlay).uv2(light)
                .normal(poseStack.last().normal(), 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(poseStack.last().pose(), -half, half, 0).color(1F, 1F, 1F, 1F)
                .uv(u0, v0).overlayCoords(packedOverlay).uv2(light)
                .normal(poseStack.last().normal(), 0.0F, 0.0F, 1.0F).endVertex();
        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(MaliciousThermalilyProjectile entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
