package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

/**
 * Placeholder entry for gem-cutting machine recipes.
 * <p>
 * EMI display uses {@link com.moguang.ctnhmana.api.recipe.customlogic.IndustrialGemCuttingLogic#buildRepresentativeRecipes}
 * (real {@code GemRegistry} stacks). Runtime matching stays in
 * {@link com.moguang.ctnhmana.api.recipe.customlogic.IndustrialGemCuttingLogic#createCustomRecipe}.
 * Empty rarity-only gem shells are intentionally not registered.
 */
public class GemCuttingRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // no-op: do not register rarity-only placeholder gem recipes for EMI
    }
}
