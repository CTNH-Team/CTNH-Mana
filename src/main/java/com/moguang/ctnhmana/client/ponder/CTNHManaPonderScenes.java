package com.moguang.ctnhmana.client.ponder;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.ponder.Mana.MysticSpire;
import com.moguang.ctnhmana.registry.CMMultiblockMachines;

public final class CTNHManaPonderScenes {

    private CTNHManaPonderScenes() {
    }

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(CMMultiblockMachines.MysticSpire.getId())
                .addStoryBoard("mysticspire/scene1", MysticSpire::Scene1, CTNHManaPonderTags.Mana)
                .addStoryBoard("mysticspire/scene2", MysticSpire::Scene2, CTNHManaPonderTags.Mana)
                .addStoryBoard("mysticspire/scene3", MysticSpire::Scene3, CTNHManaPonderTags.Mana);

        CTNHMana.LOGGER.info("Mana Ponder scenes initialized");
    }
}
