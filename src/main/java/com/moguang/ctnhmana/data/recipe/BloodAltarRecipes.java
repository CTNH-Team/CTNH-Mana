package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.moguang.ctnhmana.api.recipe.bloodmagic.BloodAltarRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.item.BloodMagicJade.EtchingJade;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMItems.HORIZEN_RUNE;
import static com.moguang.ctnhmana.registry.CMMaterials.Zenith_essence;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.BLOOD_ALTAR_RECIPES;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.HELL_FORGE_RECIPES;
import static vazkii.botania.common.item.BotaniaItems.runeFire;

public class BloodAltarRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        BloodAltarRecipeBuilder.builder("test_1")
                .input(new ItemStack(runeFire,1))
                .output(new ItemStack(HORIZEN_RUNE,1))
                .syphon(10000)
                .minimumTier(2)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("blankslate")
                .output(new ItemStack(BloodMagicItems.BLANK_RUNE_ITEM.get(),1))
                .input(new ItemStack(BotaniaBlocks.livingrock.asItem(),1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloodygold")
                .input(ChemicalHelper.get(TagPrefix.ingot,GTMaterials.Gold,1))
                .output(ChemicalHelper.get(TagPrefix.ingot,CMMaterials.COAGULBLOODGOLD,1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloodygold_dust")
                .input(ChemicalHelper.get(TagPrefix.dust,GTMaterials.Gold,1))
                .output(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloodygold_block")
                .input(ChemicalHelper.get(TagPrefix.block,GTMaterials.Gold,1))
                .output(ChemicalHelper.get(TagPrefix.block,CMMaterials.COAGULBLOODGOLD,1))
                .circuitMeta(0)
                .syphon(8000)
                .minimumTier(1)
                .consumeRate(45)
                .drainRate(45)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloody_diode")
                .input(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,2))
                .output(ChemicalHelper.get(TagPrefix.wireFine,CMMaterials.COAGULBLOODGOLD,12))
                .circuitMeta(1)
                .syphon(8000)
                .minimumTier(1)
                .consumeRate(45)
                .drainRate(45)
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("etching_circuit")
                .inputItems(BloodMagicItems.BLANK_RUNE_ITEM.get(),1)
                .circuitMeta(1)
                .outputItems(CMItems.RUNE_CIRCUIT_BOARD,1)
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .addCondition(new BloodAltarCondition(3,100,100*200,"etching"))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_diode")
                .inputItems(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,2))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,CMMaterials.HEMOPLATINUM,12))
                .inputFluids(GTMaterials.Polyethylene.getFluid(144))
                .outputItems(BLOOD_DIODE,16)
                .circuitMeta(2)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_resistor")
                .inputItems(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,2))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,GTMaterials.BlackSteel,12))
                .inputFluids(GTMaterials.Polyethylene.getFluid(144))
                .outputItems(BLOOD_RESISTOR,16)
                .circuitMeta(2)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_capacitor")
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
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_transistor")
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
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_inductor")
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
    }
}
