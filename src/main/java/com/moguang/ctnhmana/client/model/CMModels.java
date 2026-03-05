package com.moguang.ctnhmana.client.model;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

import java.util.Map;

public class CMModels {

    public static final Map<String, ? extends GeoModel<GeoAnimatable>> MODELS = Map.of(
            "star_cake_model", new StarCakeBlockModel());
}
