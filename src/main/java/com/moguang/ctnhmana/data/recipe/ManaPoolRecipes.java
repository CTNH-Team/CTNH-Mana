package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.moguang.ctnhmana.common.recipe.builder.botania.ManaInfusionRecipeBuilder;
import com.moguang.ctnhmana.registry.CMItems;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class ManaPoolRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        ManaInfusionRecipeBuilder.builder("exchange_test")
                .input(GTItems.DIODE.asStack())
                .output(CMItems.MANA_DIODE.asStack())
                .mana(10000)
                .circuitMeta(2)
                .save(provider);
    }
}
