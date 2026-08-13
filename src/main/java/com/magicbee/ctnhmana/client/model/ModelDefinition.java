package com.magicbee.ctnhmana.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import com.magicbee.ctnhmana.CTNHMana;

import java.util.function.Supplier;

public class ModelDefinition {

    public ModelDefinition(String name, Supplier<LayerDefinition> createBodyLayer) {
        this.name = name;
        this.LAYER_LOCATION = new ModelLayerLocation(CTNHMana.id(name), "main");
        this.createBodyLayer = createBodyLayer;
    }

    public String name;
    public ModelLayerLocation LAYER_LOCATION;
    public Supplier<LayerDefinition> createBodyLayer;
}
