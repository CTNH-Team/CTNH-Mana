package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;

import net.minecraft.data.recipes.FinishedRecipe;

import com.moguang.ctnhmana.registry.CMRecipeTypes;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;

import java.util.function.Consumer;

public class EternalWosRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        CMRecipeTypes.DIGITAL_WELL_OF_SUFFER.recipeBuilder("confused")
                .inputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), 1000))
                .outputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000))
                .duration(100)
                .EUt(320)
                .save(provider);
    }
}
