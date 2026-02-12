package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.builder.bloodmagic.BloodAltarRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.ManaInfusionRecipeBuilder;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.BLOOD_ALTAR_RECIPES;

public class ManaCircuitRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        ManaInfusionRecipeBuilder.builder("simple_mana_soc")//魔力SOC晶圆
                .input(GTItems.SIMPLE_SYSTEM_ON_CHIP_WAFER.asStack())
                .circuitMeta(10)
                .output(MANA_WAFER.asStack())
                .mana(100000)
                .save(provider);
        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder("mana_soc")//魔力SOC
                .inputItems(MANA_WAFER.asStack())
                .inputFluids(CMMaterials.Mana.getFluid(500))
                .outputItems(MANA_SOC,6)
                .EUt(32)
                .duration(100*20)
                .save(provider);

        BLOOD_ALTAR_RECIPES.recipeBuilder("etching_circuit")//符石电路基板
                .inputItems(BloodMagicItems.BLANK_RUNE_ITEM.get(),1)
                .circuitMeta(1)
                .outputItems(CMItems.RUNE_CIRCUIT_BOARD,1)
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .addCondition(new BloodAltarCondition(3,100,100*200,"etching"))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_diode")//血染逻辑二极管
                .inputItems(ChemicalHelper.get(TagPrefix.dust, CMMaterials.COAGULBLOODGOLD,2))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,CMMaterials.HEMOPLATINUM,12))
                .inputFluids(GTMaterials.Polyethylene.getFluid(144))
                .outputItems(BLOOD_DIODE,16)
                .circuitMeta(2)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloody_resistor")//血染逻辑电阻
                .input(GTItems.SMD_RESISTOR.asStack())
                .output(BLOOD_RESISTOR.asStack())
                .minimumTier(3)
                .circuitMeta(2)
                .consumeRate(1000)
                .syphon(5000)
                .drainRate(1000)
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_capacitor")//血染逻辑电容
                .inputItems(ChemicalHelper.get(TagPrefix.foil,GTMaterials.Electrum,32))
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.COAGULBLOODGOLD,8))
                .inputItems(BloodMagicItems.REAGENT_LAVA)
                .inputFluids(GTMaterials.Polyethylene.getFluid(144*8))
                .outputItems(BLOOD_CAPACITOR,48)
                .circuitMeta(1)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_transistor")//鲜血结晶管
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.Aerialite,32))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,GTMaterials.BlackSteel,64))
                .inputItems(BloodMagicItems.REAGENT_WATER)
                .inputFluids(GTMaterials.Polyethylene.getFluid(144*8))
                .outputItems(BLOOD_TRANSISTOR,48)
                .circuitMeta(1)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_inductor")//血级电感
                .inputItems(ChemicalHelper.get(TagPrefix.ring,GTMaterials.NickelZincFerrite,1))
                .inputItems(ChemicalHelper.get(TagPrefix.dust,GTMaterials.Sulfur,4))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,GTMaterials.BlackSteel,8))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,CMMaterials.HEMOPLATINUM,12))
                .inputFluids(GTMaterials.Polyethylene.getFluid(144*8))
                .outputItems(BLOOD_INDUCTOR,32)
                .circuitMeta(1)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloody_wafer")//血染逻辑晶圆
                .input(MANA_WAFER.asStack())
                .output(BLOODY_WAFER.asStack())
                .circuitMeta(16)
                .minimumTier(6)
                .syphon(1000000)
                .drainRate(10000)
                .consumeRate(10000)
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_wafer_2")//血染逻辑晶圆
                .inputItems(GTItems.PHOSPHORUS_WAFER.asItem())
                .outputItems(BLOODY_WAFER.asStack())
                .addCondition(new BloodAltarCondition(2,100,5000,"etching"))
                .EUt(GTValues.VA[GTValues.HV])
                .duration(5000/100*20)
                .circuitMeta(6)
                .save(provider);
        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder("bloody_circuit")//血染逻辑芯片
                .inputItems(BLOODY_WAFER.asStack())
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(),1000))
                .outputItems(BLOODY_CHIP,6)
                .EUt(32)
                .duration(444*20)
                .save(provider);
        CMRecipeTypes.HELL_FORGE_RECIPES.recipeBuilder("will_resistor")//意志阻遏电阻
                .outputItems(WILL_RESISTOR,6)
                .inputItems(GTItems.ADVANCED_SMD_RESISTOR,6)
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.DEMON),6)
                .inputItems(BloodMagicItems.REINFORCED_SLATE)
                .addCondition(new HellForgeCondition(66))
                .EUt(6666)
                .duration(66*6)
                .save(provider);
        CMRecipeTypes.HELL_FORGE_RECIPES.recipeBuilder("will_transistor")//晶化意志管
                .outputItems(WILL_TRANSISTOR,6)
                .inputItems(GTItems.ADVANCED_SMD_TRANSISTOR,6)
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.DEMON),6)
                .inputItems(BloodMagicItems.REINFORCED_SLATE)
                .inputItems(BloodMagicItems.RAW_CRYSTAL,1)
                .addCondition(new HellForgeCondition(66))
                .EUt(6666)
                .duration(66*6)
                .save(provider);
        CMRecipeTypes.HELL_FORGE_RECIPES.recipeBuilder("will_inductor")//意志悖论电感
                .outputItems(WILL_INDUCTOR,6)
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR,6)
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.PRIMOVOLITHEST),4)
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.DEMON),6)
                .inputItems(BloodMagicItems.DEMONIC_SLATE)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), 666))
                .addCondition(new HellForgeCondition("steadfast",66))
                .EUt(8192)
                .duration(66*6)
                .save(provider);
        CMRecipeTypes.HELL_FORGE_RECIPES.recipeBuilder("will_capator")//疑虑增生电容
                .outputItems(WILL_CAPACITOR,66)
                .inputItems(Items.NETHER_STAR,6)
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.PRIMOVOLITHEST),66)
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.DEMON),66)
                .inputItems(BloodMagicItems.DEMONIC_SLATE)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), 6666))
                .addCondition(new HellForgeCondition("vengeful",6666))
                .EUt(6666)
                .duration(6666)
                .save(provider);
        CMRecipeTypes.HELL_FORGE_RECIPES.recipeBuilder("will_diode")//意志注入二极管
                .outputItems(WILL_DIODE,6)
                .inputItems(MANA_DIODE,6)
                .inputItems(BloodMagicItems.RAW_CRYSTAL,1)
                .inputItems(BloodMagicItems.CORROSIVE_CRYSTAL,1)
                .inputItems(BloodMagicItems.DESTRUCTIVE_CRYSTAL,1)
                .inputItems(BloodMagicItems.VENGEFUL_CRYSTAL,1)
                .inputItems(BloodMagicItems.STEADFAST_CRYSTAL,1)
                .addCondition(new HellForgeCondition(66))
                .EUt(6666)
                .duration(666)
                .save(provider);

    }
}
