package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.moguang.ctnhmana.api.recipe.bloodmagic.BloodAltarRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.HORIZEN_RUNE;
import static com.moguang.ctnhmana.registry.CMMaterials.Zenith_essence;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.HELL_FORGE_RECIPES;
import static vazkii.botania.common.item.BotaniaItems.runeFire;

public class BloodAltarRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        BloodAltarRecipeBuilder.builder("test_1")
                .input(new ItemStack(runeFire,1))
                .output(new ItemStack(HORIZEN_RUNE,1))
                .syphon(10000)
                .minimumTier(2)
                .consumeRate(20)
                .drainRate(20)

                .save(provider);
    }
}
