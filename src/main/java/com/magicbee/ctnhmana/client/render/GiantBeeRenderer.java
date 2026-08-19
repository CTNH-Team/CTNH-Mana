package com.magicbee.ctnhmana.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import com.magicbee.ctnhmana.client.model.GiantBeeModel;
import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.mojang.blaze3d.vertex.PoseStack;

public class GiantBeeRenderer extends MobRenderer<GiantBee, GiantBeeModel> {

    private static final ResourceLocation BEE_TEXTURE = ResourceLocation
            .withDefaultNamespace("textures/entity/bee/bee.png");
    private static final ResourceLocation BEE_ANGRY_TEXTURE = ResourceLocation
            .withDefaultNamespace("textures/entity/bee/bee_angry.png");

    public GiantBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new GiantBeeModel(GiantBeeModel.createBodyLayer().bakeRoot()), 1.5F);
    }

    @Override
    protected void scale(GiantBee bee, PoseStack poseStack, float partialTick) {
        poseStack.scale(GiantBee.SCALE, GiantBee.SCALE, GiantBee.SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(GiantBee bee) {
        return bee.isAngry() ? BEE_ANGRY_TEXTURE : BEE_TEXTURE;
    }
}
