package com.magicbee.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import com.magicbee.ctnhmana.api.recipe.customlogic.IndustrialGemCuttingLogic;

import java.util.function.Consumer;

/**
 * Placeholder entry for gem-cutting machine recipes.
 * <p>
 * EMI display uses {@link IndustrialGemCuttingLogic#buildRepresentativeRecipes}
 * (real {@code GemRegistry} stacks). Runtime matching stays in
 * {@link IndustrialGemCuttingLogic#createCustomRecipe}.
 * Empty rarity-only gem shells are intentionally not registered.
 */
public class GemCuttingRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // no-op: do not register rarity-only placeholder gem recipes for EMI
    }
}
