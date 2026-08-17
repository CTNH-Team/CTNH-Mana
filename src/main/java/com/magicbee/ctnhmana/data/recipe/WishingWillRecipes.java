package com.magicbee.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.data.recipe.CustomTags.CIRCUITS;
import static com.magicbee.ctnhmana.registry.CMRecipeTypes.WISHING_RECIPES;

public class WishingWillRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // 占位配方：投任意电路换火焰符文，后续替换为 gacha 概率产出
        WISHING_RECIPES.recipeBuilder("placeholder_wish")
                .inputItems(CIRCUITS, 1)
                .outputItems(BotaniaItems.runeFire, 10)
                .duration(200)
                .EUt(0)
                .save(provider);
    }
}
