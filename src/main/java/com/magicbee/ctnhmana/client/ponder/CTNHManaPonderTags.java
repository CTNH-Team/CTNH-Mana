package com.magicbee.ctnhmana.client.ponder;

import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMMultiblockMachines;
import mythicbotany.register.ModBlocks;
import tech.vixhentx.mcmod.ctnhlib.client.ponder.CTNHPonderTagHelper;
import vazkii.botania.common.block.BotaniaBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import static com.magicbee.ctnhmana.CTNHMana.REGISTRATE;

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
                .add(CMMultiblockMachines.INDUSTRIAL_ALTAR.getId())
                .add(BuiltInRegistries.BLOCK.getKey(BotaniaBlocks.felPumpkin))
                .add(BuiltInRegistries.BLOCK.getKey(ModBlocks.centralRuneHolder))
                .add(BloodMagicItems.DUSK_RITUAL_DIVINER.getId())
                .add(BuiltInRegistries.BLOCK.getKey(BotaniaBlocks.terraPlate));

        CTNHMana.LOGGER.info("Mana Ponder tags initialized");
    }
}
