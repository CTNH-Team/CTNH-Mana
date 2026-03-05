package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import com.moguang.ctnhmana.common.recipe.builder.botania.ElvenTradeRecipeBuilder;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.ADVANCED_MANA_CAPACITOR;
import static com.moguang.ctnhmana.registry.CMItems.ADVANCED_MANA_DIODE;
import static com.moguang.ctnhmana.registry.CMItems.ADVANCED_MANA_INDUCTOR;
import static com.moguang.ctnhmana.registry.CMItems.ADVANCED_MANA_RESISTOR;
import static com.moguang.ctnhmana.registry.CMItems.ADVANCED_MANA_TRANSISTOR;
import static com.moguang.ctnhmana.registry.CMItems.MANA_CAPACITOR;
import static com.moguang.ctnhmana.registry.CMItems.MANA_DIODE;
import static com.moguang.ctnhmana.registry.CMItems.MANA_INDUCTOR;
import static com.moguang.ctnhmana.registry.CMItems.MANA_RESISTOR;
import static com.moguang.ctnhmana.registry.CMItems.MANA_TRANSISTOR;

@SuppressWarnings("removal")
public class ElvenTradeRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        ElvenTradeRecipeBuilder.builder("advanced_mana_resistor")
                .input(MANA_RESISTOR.asStack(), MANA_RESISTOR.asStack())
                .output(ADVANCED_MANA_RESISTOR.asStack())
                .save(provider);

        ElvenTradeRecipeBuilder.builder("advanced_mana_diode")
                .input(MANA_DIODE.asStack(), MANA_DIODE.asStack())
                .output(ADVANCED_MANA_DIODE.asStack())
                .save(provider);
        ElvenTradeRecipeBuilder.builder("advanced_mana_transistor")
                .input(MANA_TRANSISTOR.asStack(), MANA_TRANSISTOR.asStack())
                .output(ADVANCED_MANA_TRANSISTOR.asStack())
                .save(provider);
        ElvenTradeRecipeBuilder.builder("advanced_mana_capacitor")
                .input(MANA_CAPACITOR.asStack(), MANA_CAPACITOR.asStack())
                .output(ADVANCED_MANA_CAPACITOR.asStack())
                .save(provider);
        ElvenTradeRecipeBuilder.builder("advanced_mana_inductor")
                .input(MANA_INDUCTOR.asStack(), MANA_INDUCTOR.asStack())
                .output(ADVANCED_MANA_INDUCTOR.asStack())
                .save(provider);
    }
}
