package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.HORIZEN_RUNE;
import static com.moguang.ctnhmana.registry.CMMaterials.Zenith_essence;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.HELL_FORGE_RECIPES;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.WISHING_RECIPES;
import static vazkii.botania.common.item.BotaniaItems.runeFire;

public class WishingWillRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        WISHING_RECIPES.recipeBuilder("test1")
                .inputItems(CustomTags.LV_CIRCUITS,1)
                .outputItems(runeFire,10)
                .duration(3)
                .save(provider);
//        HELL_FORGE_RECIPES.recipeBuilder("testXXX")
//                .addCondition(new HellForgeCondition(10))
//                .inputItems(runeFire,24)
//                .inputFluids(Zenith_essence.getFluid(144))
//                .outputItems(HORIZEN_RUNE)
//                .duration(200)
//                .circuitMeta(19)
//                .EUt(114514)
//                .save(provider);
    }
}
