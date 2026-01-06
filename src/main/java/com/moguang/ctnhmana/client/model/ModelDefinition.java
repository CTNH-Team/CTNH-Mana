package com.moguang.ctnhmana.client.model;

import com.moguang.ctnhmana.CTNHMana;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public class ModelDefinition {
    public ModelDefinition(String name, Supplier<LayerDefinition> createBodyLayer) {
        this.name = name;
        this.LAYER_LOCATION = new ModelLayerLocation(CTNHMana.id(name),"main");
        this.createBodyLayer = createBodyLayer;
    }
    public String name;
    public ModelLayerLocation LAYER_LOCATION ;
    public Supplier<LayerDefinition> createBodyLayer;
}
