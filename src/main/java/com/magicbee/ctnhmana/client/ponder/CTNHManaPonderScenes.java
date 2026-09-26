package com.magicbee.ctnhmana.client.ponder;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.client.ponder.mana.IndustrialAltar;
import com.magicbee.ctnhmana.client.ponder.mana.MagicRituals;
import com.magicbee.ctnhmana.client.ponder.mana.MysticSpire;
import com.magicbee.ctnhmana.client.ponder.mana.TerraPlate;
import com.magicbee.ctnhmana.registry.CMMultiblockMachines;
import mythicbotany.register.ModBlocks;
import vazkii.botania.common.block.BotaniaBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

public final class CTNHManaPonderScenes {

    private CTNHManaPonderScenes() {}

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(CMMultiblockMachines.MysticSpire.getId())
                .addStoryBoard("mysticspire/scene1", MysticSpire::Scene1, CTNHManaPonderTags.Mana)
                .addStoryBoard("mysticspire/scene2", MysticSpire::Scene2, CTNHManaPonderTags.Mana)
                .addStoryBoard("mysticspire/scene3", MysticSpire::Scene3, CTNHManaPonderTags.Mana);

        helper.forComponents(CMMultiblockMachines.INDUSTRIAL_ALTAR.getId())
                .addStoryBoard("industrial_altar/common", IndustrialAltar::Common, CTNHManaPonderTags.Mana);

        helper.forComponents(BuiltInRegistries.BLOCK.getKey(BotaniaBlocks.felPumpkin))
                .addStoryBoard("blaze/common", MagicRituals::Blaze, CTNHManaPonderTags.Mana);

        helper.forComponents(BuiltInRegistries.BLOCK.getKey(ModBlocks.centralRuneHolder))
                .addStoryBoard("rune_rituals/common", MagicRituals::RuneRitual, CTNHManaPonderTags.Mana);

        helper.forComponents(BloodMagicItems.DUSK_RITUAL_DIVINER.getId())
                .addStoryBoard("ritual_diviner/common", MagicRituals::RitualDiviner, CTNHManaPonderTags.Mana);

        helper.forComponents(BuiltInRegistries.BLOCK.getKey(BotaniaBlocks.terraPlate))
                .addStoryBoard("terra_plate/common", TerraPlate::common, CTNHManaPonderTags.Mana);

        CTNHMana.LOGGER.info("Mana Ponder scenes initialized");
    }
}
