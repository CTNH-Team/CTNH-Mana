package com.moguang.ctnhmana.client.ponder;

import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMMultiblockMachines;
import tech.vixhentx.mcmod.ctnhlib.client.ponder.CTNHPonderTagHelper;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

public final class CTNHManaPonderTags {

    public static final ResourceLocation Mana = ResourceLocation.tryBuild(CTNHMana.MODID, "mana");

    private CTNHManaPonderTags() {}

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        CTNHPonderTagHelper.registerTag(REGISTRATE, helper, Mana,
                "CTNH Mana Machine", "CTNH魔力机器",
                "CTNH Mana Machine Ponders", "CTNH魔力机器思索")
                .addToIndex()
                .item(Items.AMETHYST_SHARD, true, false)
                .register();

        helper.addToTag(Mana)
                .add(CMMultiblockMachines.MysticSpire.getId())
                .add(ResourceLocation.fromNamespaceAndPath("botania", "fel_pumpkin"))
                .add(ResourceLocation.fromNamespaceAndPath("mythicbotany", "central_rune_holder"))
                .add(ResourceLocation.fromNamespaceAndPath("bloodmagic", "ritualdivinerdusk"));

        CTNHMana.LOGGER.info("Mana Ponder tags initialized");
    }
}
