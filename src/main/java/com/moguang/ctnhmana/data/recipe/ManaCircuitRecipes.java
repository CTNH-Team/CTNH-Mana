package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.common.recipe.builder.bloodmagic.BloodAltarRecipeBuilder;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import net.minecraft.data.recipes.FinishedRecipe;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.BLOOD_ALTAR_RECIPES;

public class ManaCircuitRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        BLOOD_ALTAR_RECIPES.recipeBuilder("etching_circuit")
                .inputItems(BloodMagicItems.BLANK_RUNE_ITEM.get(),1)
                .circuitMeta(1)
                .outputItems(CMItems.RUNE_CIRCUIT_BOARD,1)
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .addCondition(new BloodAltarCondition(3,100,100*200,"etching"))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_diode")
                .inputItems(ChemicalHelper.get(TagPrefix.dust, CMMaterials.COAGULBLOODGOLD,2))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,CMMaterials.HEMOPLATINUM,12))
                .inputFluids(GTMaterials.Polyethylene.getFluid(144))
                .outputItems(BLOOD_DIODE,16)
                .circuitMeta(2)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloody_resistor")
                .input(GTItems.SMD_RESISTOR.asStack())
                .output(BLOOD_RESISTOR.asStack())
                .minimumTier(3)
                .circuitMeta(2)
                .consumeRate(1000)
                .syphon(5000)
                .drainRate(1000)
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
