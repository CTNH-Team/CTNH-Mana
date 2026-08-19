package com.magicbee.ctnhmana.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import com.magicbee.ctnhmana.client.model.RoyalServantBeeModel;
import com.magicbee.ctnhmana.common.entity.RoyalServantBee;

/**
 * 皇家侍从Bee渲染器：模型与普通蜜蜂一致（{@link RoyalServantBeeModel}），无缩放。
 */
public class RoyalServantBeeRenderer extends MobRenderer<RoyalServantBee, RoyalServantBeeModel> {

    private static final ResourceLocation BEE_TEXTURE = ResourceLocation
            .withDefaultNamespace("textures/entity/bee/bee.png");

    public RoyalServantBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new RoyalServantBeeModel(RoyalServantBeeModel.createBodyLayer().bakeRoot()), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(RoyalServantBee bee) {
        return BEE_TEXTURE;
    }
}
