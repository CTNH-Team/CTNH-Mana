package com.moguang.ctnhmana.client.render;

import com.moguang.ctnhmana.client.model.StarCakeItemModel;
import com.moguang.ctnhmana.item.FlowerCakeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class StarCakeItemRender extends GeoItemRenderer<FlowerCakeItem> {
    public StarCakeItemRender(GeoModel<?> model) {
        super((GeoModel<FlowerCakeItem>) model);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        if (transformType == ItemDisplayContext.GUI) {
            poseStack.translate(0.0, -0.5, 0.0);
        }
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
