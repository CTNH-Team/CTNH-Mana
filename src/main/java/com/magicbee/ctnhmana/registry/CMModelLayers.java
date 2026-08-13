package com.magicbee.ctnhmana.registry;

import com.magicbee.ctnhmana.client.model.MagicCubeModel;
import com.magicbee.ctnhmana.client.model.ModelDefinition;

public class CMModelLayers {

    public static void init() {};

    public static ModelDefinition MAGIC_CUBE_MODEL = new ModelDefinition("magic_cube", MagicCubeModel::createBodyLayer);
}
