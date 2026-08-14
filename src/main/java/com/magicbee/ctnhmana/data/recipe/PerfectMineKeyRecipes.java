package com.magicbee.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.magicbee.ctnhmana.registry.CMItems.*;

public class PerfectMineKeyRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("perfect_mine_key")// 完善的工头钥匙
                .inputItems(BROKEN_MINE_KEY.asStack(), 30)
                .inputItems(CustomTags.LV_CIRCUITS)
                .outputItems(PERFECT_MINE_KEY.asStack())
                .EUt(30)
                .duration(600)
                .save(provider);
    }
}
