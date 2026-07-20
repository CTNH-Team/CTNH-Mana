package com.moguang.ctnhmana.client.model;

import net.minecraft.resources.ResourceLocation;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.item.FlowerCakeItem;
import software.bernie.geckolib.model.GeoModel;

public class StarCakeItemModel extends GeoModel<FlowerCakeItem> {

    protected static final ResourceLocation TEXTURE = CTNHMana.id("textures/item/flower_cake_item.png");

    @Override
    public ResourceLocation getModelResource(FlowerCakeItem flowerCakeItem) {
        return null;
    }

    @Override
    public ResourceLocation getTextureResource(FlowerCakeItem flowerCakeItem) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FlowerCakeItem flowerCakeItem) {
        return null;
    }
}
