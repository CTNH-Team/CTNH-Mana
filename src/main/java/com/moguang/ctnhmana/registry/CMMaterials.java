package com.moguang.ctnhmana.registry;


import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.data.materials.BotaniaMaterials;
import mythicbotany.register.ModItems;
import net.minecraft.resources.ResourceLocation;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_PLATE;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.registry.CMElements.*;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.Prefix;

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
        TagPrefix.ingot.setIgnored(ManaSteel, () -> BotaniaItems.manaSteel);
        TagPrefix.nugget.setIgnored(ManaSteel, () -> BotaniaItems.manasteelNugget);
        TagPrefix.block.setIgnored(ManaSteel, () -> BotaniaBlocks.manasteelBlock);
        TagPrefix.ingot.setIgnored(TerraSteel, () -> BotaniaItems.terrasteel);
        TagPrefix.nugget.setIgnored(TerraSteel, () -> BotaniaItems.terrasteelNugget);
        TagPrefix.block.setIgnored(TerraSteel, () -> BotaniaBlocks.terrasteelBlock);
        TagPrefix.ingot.setIgnored(Elementium, () -> BotaniaItems.elementium);
        TagPrefix.nugget.setIgnored(Elementium, () -> BotaniaItems.elementiumNugget);
        TagPrefix.block.setIgnored(Elementium, () -> BotaniaBlocks.elementiumBlock);
        TagPrefix.ingot.setIgnored(AlfSteel, () -> ModItems.alfsteelIngot);
        TagPrefix.nugget.setIgnored(AlfSteel, () -> ModItems.alfsteelNugget);
        TagPrefix.block.setIgnored(AlfSteel, () -> mythicbotany.register.ModBlocks.alfsteelBlock);

    }
    public static final Material Mana =  REGISTRATE.material(GTCEu.id("mana"))
            .cnlang("液态魔力")
            .liquid()
            .color(0x43e7ed)
            .buildAndRegister();
    public static final Material Zenith_essence=REGISTRATE.material(GTCEu.id("zenith_essence"))
            .cnlang("§5天顶源质§r")
            .liquid()
            .color(0x7D26CD)
            .secondaryColor(0x836FFF)
            .buildAndRegister();
    public static final Material Psionic_Medulla=REGISTRATE.material(GTCEu.id("psionic_medulla"))
            .cnlang("§5灵界髓质§r")
            .liquid()
            .gem()
            .dust()
            .color(0x7D26CD)
            .secondaryColor(0x836FFF)
            .buildAndRegister();
    public static final Material Ultra_Mana=REGISTRATE.material(GTCEu.id("ultra_mana"))
            .cnlang("究极魔力")
            .liquid()
            .ingot()
            .dust()
            .element(ULTRA_MANA)
            .color(0x7D26CD)
            .blastTemp(7200, BlastProperty.GasTier.HIGHEST, 122222, 1000)
            .cableProperties(GTValues.V[GTValues.ZPM], 8, 1, false)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .buildAndRegister();


}

