package com.moguang.ctnhmana.registry;


import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.moguang.ctnhmana.data.materials.BotaniaMaterials;
import mythicbotany.register.ModItems;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

public class CMMaterials {

    public static Material ManaSteel;
    public static Material TerraSteel;
    public static Material Elementium;
    public static Material AlfSteel;

    public static void init() {
        BotaniaMaterials.init();
    }
    public static void register() {

    }
    public static void tagPrefixIgnore() {
        TagPrefix.ingot.setIgnored(ManaSteel, BotaniaItems.manaSteel);
        TagPrefix.nugget.setIgnored(ManaSteel, BotaniaItems.manasteelNugget);
        TagPrefix.block.setIgnored(ManaSteel, BotaniaBlocks.manasteelBlock);
        TagPrefix.ingot.setIgnored(TerraSteel, BotaniaItems.terrasteel);
        TagPrefix.nugget.setIgnored(TerraSteel, BotaniaItems.terrasteelNugget);
        TagPrefix.block.setIgnored(TerraSteel, BotaniaBlocks.terrasteelBlock);
        TagPrefix.ingot.setIgnored(Elementium, BotaniaItems.elementium);
        TagPrefix.nugget.setIgnored(Elementium, BotaniaItems.elementiumNugget);
        TagPrefix.block.setIgnored(Elementium, BotaniaBlocks.elementiumBlock);
        TagPrefix.ingot.setIgnored(AlfSteel, ModItems.alfsteelIngot);
        TagPrefix.nugget.setIgnored(AlfSteel, ModItems.alfsteelNugget);

    }
    public static final Material Mana = new Material.Builder(GTCEu.id("mana"))

            .liquid()
            .color(0x43e7ed)
            .buildAndRegister();
}

