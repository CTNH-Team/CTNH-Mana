package com.moguang.ctnhmana.client.model;

import net.minecraft.resources.ResourceLocation;

import com.moguang.ctnhmana.CTNHMana;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class StarCakeBlockModel extends GeoModel<GeoAnimatable> {

    public static final ResourceLocation MODEL = CTNHMana.id("geo/moon_flower_cake.json");
    protected static final ResourceLocation TEXTURE = CTNHMana.id("textures/entity/flower_cake.png");

    @Override
    public ResourceLocation getModelResource(GeoAnimatable geoAnimatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable geoAnimatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable geoAnimatable) {
        return null;
    }
}
