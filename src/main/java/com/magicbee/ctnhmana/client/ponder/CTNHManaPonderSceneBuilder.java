package com.magicbee.ctnhmana.client.ponder;

import com.gregtechceu.gtceu.GTCEu;

import net.createmod.ponder.api.scene.SceneBuilder;

import com.magicbee.ctnhmana.CTNHMana;
import tech.vixhentx.mcmod.ctnhlib.client.ponder.CTNHPonderSceneBuilder;

import static com.magicbee.ctnhmana.CTNHMana.REGISTRATE;

public class CTNHManaPonderSceneBuilder extends CTNHPonderSceneBuilder {

    public CTNHManaPonderSceneBuilder(SceneBuilder builder) {
        super(builder, CTNHMana.MODID, CTNHManaPonderSceneBuilder::registerLang);
    }

    private static void registerLang(String key, String en, String cn) {
        if (GTCEu.isDataGen()) {
            REGISTRATE.genLang(key, en, cn);
        }
    }
}
