package com.moguang.ctnhmana.client.ponder;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.ponder.Mana.ManaHatch;
import com.moguang.ctnhmana.client.ponder.Mana.MysticSpire;
import com.moguang.ctnhmana.registry.CMMachines;
import com.moguang.ctnhmana.registry.CMMultiblockMachines;

public final class CTNHManaPonderScenes {

    static final MachineDefinition[] MANA_HATCHES = {
            CMMachines.MANA_HATCH,
            CMMachines.ADVANCED_MANA_HATCH,
            CMMachines.GIGA_MANA_HATCH,
            CMMachines.SKY_MANA_HATCH,
            CMMachines.INDUSTRY_MANA_HATCH,
            CMMachines.BM_HATCH,
            CMMachines.BM_HATCH_T2,
            CMMachines.CREATIVE_MANA_HATCH
    };

    private CTNHManaPonderScenes() {}

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(CMMultiblockMachines.MysticSpire.getId())
                .addStoryBoard("mysticspire/scene1", MysticSpire::Scene1, CTNHManaPonderTags.Mana)
                .addStoryBoard("mysticspire/scene2", MysticSpire::Scene2, CTNHManaPonderTags.Mana)
                .addStoryBoard("mysticspire/scene3", MysticSpire::Scene3, CTNHManaPonderTags.Mana);

        for (MachineDefinition manaHatch : MANA_HATCHES) {
            helper.forComponents(manaHatch.getId())
                    .addStoryBoard("manahatch/scene", ManaHatch::Common, CTNHManaPonderTags.Mana);
        }

        CTNHMana.LOGGER.info("Mana Ponder scenes initialized");
    }
}
