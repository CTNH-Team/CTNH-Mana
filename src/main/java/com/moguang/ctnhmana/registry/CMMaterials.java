package com.moguang.ctnhmana.registry;


import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
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
    
    // 自定义图标集
    public static final MaterialIconSet HEMOPLATINUM_ICON_SET = new MaterialIconSet("hemoplatinum", MaterialIconSet.METALLIC);
    public static final MaterialIconSet COAGULBLOODGOLD_ICON_SET = new MaterialIconSet("coagulbloodgold", MaterialIconSet.METALLIC);
    public static final MaterialIconSet ULTRA_MANA_ICON_SET=new MaterialIconSet("ultramana",MaterialIconSet.METALLIC);

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

    public static final Material Mana=REGISTRATE.material(GTCEu.id("mana"))
            .cnlang("魔力")
            .liquid()
            .element(MANA)
            .color(0x43e7ed)
            .buildAndRegister();
    public static final Material Zenith_essence=REGISTRATE.material(GTCEu.id("zenith_essence"))
            .cnlang("§5天顶源质§r")
            .liquid()
            .color(0x7D26CD)
            .secondaryColor(0x836FFF)
            .element(ZENITH)
            .buildAndRegister();
    public static final Material Mana_Radiation_Mixture=REGISTRATE.material(GTCEu.id("mana_radiation_mixture"))
            .cnlang("混合辐射魔力激发")
            .liquid()
            .plasma()
            .color(0x12C924)
            .element(MANA_RADIATION_MIXTURE)
            .buildAndRegister();
    public static final Material Eve_Beam=REGISTRATE.material(GTCEu.id("eve_beam"))
            .cnlang("EVE高能粒子")
            .liquid()
            .plasma()
            .color(0x1237C9)
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
            .iconSet(ULTRA_MANA_ICON_SET)
            .color(0x7D26CD)
            .blastTemp(7200, BlastProperty.GasTier.HIGHEST, 77777, 77777)
            .cableProperties(GTValues.V[GTValues.UIV], 777777, 0, true)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .buildAndRegister();
    public static final Material Fused_Mana=REGISTRATE.material(GTCEu.id("fused_mana"))
            .cnlang("蕴魔")
            .liquid()
            .ore()
            .color(0xBC91CC)
            .dust()
            .ingot()
            .buildAndRegister();
    public static final Material Fused_Mixed_Mana=REGISTRATE.material(GTCEu.id("fused_mixed_mana"))
            .cnlang("分选蕴魔")
            .liquid()
            .color(0xB784C9)
            .dust()
            .ingot()
            .element(MANA_MIXED_2)
            .buildAndRegister();
    public static final Material Stable_Plus_Mana=REGISTRATE.material(GTCEu.id("stable_plus_mana"))
            .cnlang("临界态究极魔力")
            .dust()
            .liquid()
            .ingot()
            .color(0xC949C9)
            .buildAndRegister();
    public static final Material Unknown_Super_Mana=REGISTRATE.material(GTCEu.id("unknown_super_mana"))
            .cnlang("混沌态临界魔力")
            .dust()
            .liquid()
            .ingot()
            .color(0xF02DFA)
            .element(MANA_UNKNOWN_SUPER)
            .buildAndRegister();
    public static final Material Remain_Mana=REGISTRATE.material(GTCEu.id("remain_mana"))
            .cnlang("魔力残留物")
            .dust()
            .color(0xC576C9)
            .buildAndRegister();
    public static final Material Super_Plus_Mana=REGISTRATE.material(GTCEu.id("super_plus_mana"))
            .cnlang("临界富集魔力")
            .dust()
            .liquid()
            .ingot()
            .color(0x801B85)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .element(MANA_PLUS3)
            .buildAndRegister();
    public static final Material Quaser_Mana=REGISTRATE.material(GTCEu.id("quaser_mana"))
            .cnlang("类星体魔力")
            .dust()
            .liquid()
            .ingot()
            .color(0xA00FA8)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .buildAndRegister();
    public static final Material Infused_Plus_Mana=REGISTRATE.material(GTCEu.id("infused_plus_mana"))
            .cnlang("不稳定注魔临界魔力")
            .dust()
            .liquid()
            .ingot()
            .color(0x810887)
            .buildAndRegister();
    public static final Material Unstable_Plus_Mana=REGISTRATE.material(GTCEu.id("unstable_plus_mana"))
            .cnlang("不稳定超富集魔力")
            .dust()
            .liquid()
            .ingot()
            .color(0xE71FF2)
            .buildAndRegister();
    public static final Material Twist_Mana=REGISTRATE.material(GTCEu.id("twist_mana"))
            .cnlang("扭曲放射态临界魔力")
            .dust()
            .liquid()
            .ingot()
            .element(TWIST_MANA)
            .color(0xE73EF0)
            .buildAndRegister();
    public static final Material Fused_demon_mixed=REGISTRATE.material(GTCEu.id("fused_demon_mixed"))
            .cnlang("恶魔意志筛选蕴魔")
            .dust()
            .liquid()
            .ingot()
            .element(MANA_LP_MIXED)
            .buildAndRegister();

    public static final Material Twist_Power_Mana=REGISTRATE.material(GTCEu.id("twist_power_mana"))
            .cnlang("极端扭曲放射态临界魔力")
            .dust()
            .liquid()
            .ingot()
            .element(TWIST_POWER_MANA)
            .color(0x9E07A6)
            .buildAndRegister();
    public static final Material Plus_Mana=REGISTRATE.material(GTCEu.id("plus_mana"))
            .cnlang("富集魔力")
            .dust()
            .liquid()
            .ingot()
            .element(MANA_PLUS2)
            .color(0xED85F2)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .buildAndRegister();
    public static final Material Fused_Lp_Mixed_Mana=REGISTRATE.material(GTCEu.id("fused_lp_mixed_mana"))
            .cnlang("源质提纯恶魔")
            .dust()
            .liquid()
            .ingot()
            .element(FUSED_LP_MIXED_MANA)
            .color(0xC354C9)
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
            .iconSet(HEMOPLATINUM_ICON_SET)
            .blastTemp(3600, BlastProperty.GasTier.HIGHEST, 444, 4444)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING,GENERATE_FINE_WIRE)
            .buildAndRegister();
    public  static final Material COAGULBLOODGOLD=REGISTRATE.material(GTCEu.id("coagulbloodgold"))
            .cnlang("§4凝血金")
            .ingot()
            .liquid()
            .color(0XD4A017)
            .secondaryColor(0X990000)
            .element(CMElements.COAGULBLOODGOLD)
            .iconSet(COAGULBLOODGOLD_ICON_SET)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING,GENERATE_FINE_WIRE)
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
    public  static final Material YURIKO=REGISTRATE.material(GTCEu.id("yuriko"))
            .cnlang("绮璃")
            .liquid()
            .ingot()
            .color(0XFFD1DC)
            .element(Yuriko)
            .blastTemp(5400, BlastProperty.GasTier.HIGHEST, 4444, 444)
            .cableProperties(GTValues.V[GTValues.LuV], 44, 4, false)
            .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
            .buildAndRegister();



}

