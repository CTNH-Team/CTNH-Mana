package com.magicbee.ctnhmana.client.ponder;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.client.ponder.mana.MagicRituals;
import com.magicbee.ctnhmana.client.ponder.mana.MysticSpire;
import com.magicbee.ctnhmana.registry.CMMultiblockMachines;

public final class CTNHManaPonderScenes {

    private CTNHManaPonderScenes() {}

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(CMMultiblockMachines.MysticSpire.getId())
                .addStoryBoard("mysticspire/scene1", MysticSpire::Scene1, CTNHManaPonderTags.Mana)
                .addStoryBoard("mysticspire/scene2", MysticSpire::Scene2, CTNHManaPonderTags.Mana)
                .addStoryBoard("mysticspire/scene3", MysticSpire::Scene3, CTNHManaPonderTags.Mana);

        helper.forComponents(ResourceLocation.fromNamespaceAndPath("botania", "fel_pumpkin"))
                .addStoryBoard("blaze/common", MagicRituals::Blaze, CTNHManaPonderTags.Mana);

        helper.forComponents(ResourceLocation.fromNamespaceAndPath("mythicbotany", "central_rune_holder"))
                .addStoryBoard("rune_rituals/common", MagicRituals::RuneRitual, CTNHManaPonderTags.Mana);

        helper.forComponents(ResourceLocation.fromNamespaceAndPath("bloodmagic", "ritualdivinerdusk"))
                .addStoryBoard("ritual_diviner/common", MagicRituals::RitualDiviner, CTNHManaPonderTags.Mana);

        CTNHMana.LOGGER.info("Mana Ponder scenes initialized");
    }
}
