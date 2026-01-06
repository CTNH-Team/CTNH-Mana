package com.moguang.ctnhmana.registry;


import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.data.materials.BotaniaMaterials;
import io.github.lounode.extrabotany.common.block.ExtraBotanyBlocks;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import io.github.lounode.extrabotany.common.item.equipment.tool.hammer.OrichalcosHammer;
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
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

public class CMMaterials {

    public static Material ManaSteel;
    public static Material TerraSteel;
    public static Material Elementium;
    public static Material AlfSteel;
    public static Material Orichalcos;
    public static Material Photonium;
    public static Material Aerialite;
    public static Material DEMON;
    public static Material SHADOWIUM;

    public static Material Livingrock;

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
        TagPrefix.ingot.setIgnored(Orichalcos, () -> ExtraBotanyItems.orichalcos);
        TagPrefix.nugget.setIgnored(Orichalcos, () -> ExtraBotanyItems.orichalcosNugget);
        TagPrefix.block.setIgnored(Orichalcos, () -> ExtraBotanyBlocks.orichalcosBlock);
        TagPrefix.ingot.setIgnored(Photonium, () -> ExtraBotanyItems.photonium);
        TagPrefix.nugget.setIgnored(Photonium, () -> ExtraBotanyItems.photoniumNugget);
        TagPrefix.block.setIgnored(Photonium, () -> ExtraBotanyBlocks.photoniumBlock);
        TagPrefix.ingot.setIgnored(Aerialite, () -> ExtraBotanyItems.aerialite);
        TagPrefix.nugget.setIgnored(Aerialite, () -> ExtraBotanyItems.aerialiteNugget);
        TagPrefix.block.setIgnored(Aerialite, () -> ExtraBotanyBlocks.aerialiteBlock);
        TagPrefix.ingot.setIgnored(DEMON,()-> BloodMagicItems.HELLFORGED_INGOT.get());
        TagPrefix.block.setIgnored(DEMON,()-> BloodMagicBlocks.HELLFORGED_BLOCK.get());

        TagPrefix.block.setIgnored(Livingrock, () -> BotaniaBlocks.livingrock);
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
            .element(ZENITH)
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
            .blastTemp(7200, BlastProperty.GasTier.HIGHEST, 77777, 77777)
            .cableProperties(GTValues.V[GTValues.UIV], 777777, 0, true)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .buildAndRegister();
    public static final Material Shroud_Zenith_essence=REGISTRATE.material(GTCEu.id("shroud_zenith_essence"))
            .cnlang("§5虚境化的天顶源质§r")
            .liquid()
            .color(0x7D26CD)
            .secondaryColor(0x836FFF)
            .element(ACTIVE_ZENITH)
            .buildAndRegister();
    public static final Material MANA_STABLE_COOLDOWN = REGISTRATE.material(GTCEu.id("mana_stable_cooldown"))
            .cnlang("魔力稳定剂")
            .liquid()
            .color(0x28358A)
            .buildAndRegister();
    public static final Material ELF_FUEL = REGISTRATE.material(GTCEu.id("elf_fuel"))
            .cnlang("精灵稳定燃料")
            .liquid()
            .color(0x28358A)
            .buildAndRegister()
            .setFormula("ArNeC2O4Ma", true);
    public  static final Material HEMOPLATINUM=REGISTRATE.material(GTCEu.id("hemoplatinum"))
            .cnlang("§4血铂")
            .liquid()
            .ingot()
            .color(0XD8D8DA)
            .secondaryColor(0X990000)
            .element(CMElements.HEMOPLATINUM)
            .blastTemp(3600, BlastProperty.GasTier.HIGHEST, 444, 4444)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .buildAndRegister();
    public  static final Material COAGULBLOODGOLD=REGISTRATE.material(GTCEu.id("coagulbloodgold"))
            .cnlang("§4凝血金")
            .ingot()
            .liquid()
            .color(0XD4A017)
            .secondaryColor(0X990000)
            .element(CMElements.COAGULBLOODGOLD)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .buildAndRegister();
    public  static final Material PRIMOVOLITHEST=REGISTRATE.material(GTCEu.id("primovolithest"))
            .cnlang("混元意志钢")
            .liquid()
            .ingot()
            .color(0X2C2C2E)
            .element(CMElements.PRIMOVOLITHEST)
            .blastTemp(5400, BlastProperty.GasTier.HIGHEST, 4444, 444)
            .cableProperties(GTValues.V[GTValues.LuV], 44, 4, false)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .buildAndRegister();


}

