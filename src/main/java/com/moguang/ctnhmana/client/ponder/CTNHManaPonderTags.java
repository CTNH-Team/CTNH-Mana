package com.moguang.ctnhmana.client.ponder;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.api.registration.TagBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMMultiblockMachines;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

public final class CTNHManaPonderTags {

    public static final ResourceLocation Mana = ResourceLocation.tryBuild(CTNHMana.MODID, "mana");

    private CTNHManaPonderTags() {}

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        registerTag(helper, Mana,
                "CTNH Mana Machine", "CTNH魔力机器",
                "CTNH Mana Machine Ponders", "CTNH魔力机器思索")
                .addToIndex()
                .item(Items.AMETHYST_SHARD, true, false)
                .register();

        var manaTag = helper.addToTag(Mana)
                .add(CMMultiblockMachines.MysticSpire.getId());
        for (MachineDefinition manaHatch : CTNHManaPonderScenes.MANA_HATCHES) {
            manaTag.add(manaHatch.getId());
        }

        CTNHMana.LOGGER.info("Mana Ponder tags initialized");
    }

    private static TagBuilder registerTag(PonderTagRegistrationHelper<ResourceLocation> helper,
                                          ResourceLocation id,
                                          String en,
                                          String cn,
                                          String descriptionEn,
                                          String descriptionCn) {
        REGISTRATE.genLang(tagKey(id), en, cn);
        REGISTRATE.genLang(tagDescriptionKey(id), descriptionEn, descriptionCn);
        return helper.registerTag(id);
    }

    private static String tagKey(ResourceLocation id) {
        return id.getNamespace() + ".ponder.tag." + id.getPath();
    }

    private static String tagDescriptionKey(ResourceLocation id) {
        return tagKey(id) + ".description";
    }
}
