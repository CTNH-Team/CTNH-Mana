package com.moguang.ctnhmana.data.recipe;

import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import com.moguang.ctnhmana.common.recipe.builder.bloodmagic.TartaricForgeRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.HORIZEN_RUNE;
import static com.moguang.ctnhmana.registry.CMMaterials.Zenith_essence;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.HELL_FORGE_RECIPES;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.MANA_REACTOR_RECIPES;
import static vazkii.botania.common.item.BotaniaItems.*;

public class HellForgeRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        HELL_FORGE_RECIPES.recipeBuilder("testxxx")
                .addCondition(new HellForgeCondition(10))
                .inputItems(runeFire,24)
                .inputFluids(Zenith_essence.getFluid(144))
                .outputItems(HORIZEN_RUNE)
                .duration(200)
                .circuitMeta(19)
                .EUt(114514)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("testxxx")
                .input(runeFire,runeMana,runeAir)
                .output(new ItemStack(runeEnvy))
                .minimumSouls(1000)
                .soulDrain(100)
                .circuitMeta(21)
                .save(provider);
    }
}
