package com.moguang.ctnhmana.registry;

import com.moguang.ctnhmana.client.model.MagicCubeModel;
import com.moguang.ctnhmana.client.model.ModelDefinition;

public class CMModelLayers {
    public static void init(){};
    public static ModelDefinition MAGIC_CUBE_MODEL = new ModelDefinition("magic_cube", MagicCubeModel::createBodyLayer);
}
