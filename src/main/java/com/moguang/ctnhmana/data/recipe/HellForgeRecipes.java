package com.moguang.ctnhmana.data.recipe;

import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.HORIZEN_RUNE;
import static com.moguang.ctnhmana.registry.CMMaterials.Zenith_essence;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.HELL_FORGE_RECIPES;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.MANA_REACTOR_RECIPES;
import static vazkii.botania.common.item.BotaniaItems.runeFire;
import static vazkii.botania.common.item.BotaniaItems.runeMana;

public class HellForgeRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        HELL_FORGE_RECIPES.recipeBuilder("testXXX")
                .addCondition(new HellForgeCondition(10))
                .inputItems(runeFire,24)
                .inputFluids(Zenith_essence.getFluid(144))
                .outputItems(HORIZEN_RUNE)
                .duration(200)
                .circuitMeta(19)
                .EUt(114514)
                .save(provider);
    }
}
