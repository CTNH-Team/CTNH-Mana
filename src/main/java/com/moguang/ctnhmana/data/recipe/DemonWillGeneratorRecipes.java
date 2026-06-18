package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;

import java.util.function.Consumer;

public class DemonWillGeneratorRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        CMRecipeTypes.DEMON_WILL_GENERATOR_RECIPE.recipeBuilder("generator")
                .circuitMeta(0)
                .EUt(-1)
                .duration(66)
                .save(provider);

        CMRecipeTypes.QUASAR_EYE.recipeBuilder("generator1")
                .circuitMeta(0)
                .inputFluids(CMMaterials.Mana.getFluid(100000))
                .EUt(-33554432 * 2)
                .duration(200)
                .addData("consumption", 1000000)
                .addData("tier", 1)
                .addData("active", 1)
                .save(provider);
    }
}
