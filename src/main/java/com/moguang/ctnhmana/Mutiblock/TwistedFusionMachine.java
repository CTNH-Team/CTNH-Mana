package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import org.jetbrains.annotations.Nullable;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.failureManaLang_NoEnoughTwistLevel;

public class TwistedFusionMachine extends WorkableElectricMultiblockMachine {

    public int mks = 0;

    public TwistedFusionMachine(IMachineBlockEntity holder, int mk) {
        super(holder);
        this.mks = mk;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        var startEU = recipe.data.getLong("eu_to_start");
        if (startEU >= 160000000 && startEU <= 320000000 && mks < 2) {
            RecipeLogic.putFailureReason(this, recipe, failureManaLang_NoEnoughTwistLevel.translate(2));
            return false;
        }
        if (startEU > 320000000 && mks < 3) {
            RecipeLogic.putFailureReason(this, recipe, failureManaLang_NoEnoughTwistLevel.translate(3));
            return false;
        }
        return super.beforeWorking(recipe);
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (machine instanceof TwistedFusionMachine zmachine) {
            if (recipe.getType().equals(GTRecipeTypes.FUSION_RECIPES)) {
                var startEU = recipe.data.getLong("eu_to_start");
                ModifierFunction modifierFunction = ModifierFunction.IDENTITY;
                if (zmachine.mks > 5 && zmachine.mks < Integer.MAX_VALUE - 1) {
                    int pa = ParallelLogic.getParallelAmount(machine, recipe, 1024);

                    return ModifierFunction.builder()
                            .parallels(pa)
                            .durationMultiplier(0.25 * pa * Math.pow(0.99, pa))
                            .eutMultiplier(0.25 * pa * Math.pow(0.99, pa))
                            .inputModifier(ContentModifier.multiplier(pa))
                            .outputModifier(ContentModifier.multiplier(pa))
                            .build();
                } else if (zmachine.mks >= Integer.MAX_VALUE - 1) {
                    int pa = ParallelLogic.getParallelAmount(machine, recipe, Integer.MAX_VALUE - 10);
                    return ModifierFunction.builder()
                            .parallels(pa)
                            .durationMultiplier(0.0001 * pa * Math.pow(0.9, pa))
                            .eutMultiplier(0.0001 * pa * Math.pow(0.9, pa))
                            .inputModifier(ContentModifier.multiplier(pa))
                            .outputModifier(ContentModifier.multiplier(pa))
                            .build();
                } else {
                    return ModifierFunction.builder()
                            .durationMultiplier(1 / (1 + 0.5 * ((TwistedFusionMachine) machine).mks))
                            .build();
                }
                // else if (startEU <= 160000000) {
                // modifierFunction = CTNHRecipeModifiers.accurateParallel(zmachine, recipe, zmachine.mks*16+16);
                // } else if (startEU <= 320000000) {
                // modifierFunction = CTNHRecipeModifiers.accurateParallel(zmachine, recipe, zmachine.mks*4+4);
                // } else if (startEU <= 480000000) {
                // modifierFunction = CTNHRecipeModifiers.accurateParallel(zmachine, recipe, zmachine.mks+1);
                // }

            }
        }
        return ModifierFunction.IDENTITY;
    }
}
