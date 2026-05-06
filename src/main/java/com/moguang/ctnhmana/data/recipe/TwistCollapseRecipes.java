package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import vazkii.botania.common.lib.BotaniaTags;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.BROKEN_RUNE;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.TwistCollapse;
import static com.moguang.ctnhmana.registry.items.ManaFuelItems.SPARK_STICK;
import static com.moguang.ctnhmana.registry.items.ManaFuelItems.SPARK_STICK_DISINTEGRATED;

public class TwistCollapseRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        TwistCollapse.recipeBuilder("any_rune_to_twist_rune")
                .inputItems(BotaniaTags.Items.RUNES)
                .outputItems(BROKEN_RUNE)
                .hideDuration(true)
                .duration(1)
                .save(provider);

        // AHCC 燃料棒崩解：TwistCollapse 匹配后产出对应崩解态（见 ArcaneHighEnergyCompressionReactorCore#testTryTwistCollapseRecipeOnce）
        TwistCollapse.recipeBuilder("mana_spark_stick_disintegration")
                .inputItems(SPARK_STICK.asStack(1))
                .outputItems(SPARK_STICK_DISINTEGRATED.asStack(1))
                .hideDuration(true)
                .duration(1)
                .save(provider);
        TwistCollapse.recipeBuilder("mana_advanced_spark_stick_disintegration")
                .inputItems(SPARK_STICK.asStack(1))
                .outputItems(SPARK_STICK_DISINTEGRATED.asStack(1))
                .hideDuration(true)
                .duration(1)
                .save(provider);
        TwistCollapse.recipeBuilder("mana_terra_stick_disintegration")
                .inputItems(SPARK_STICK.asStack(1))
                .outputItems(SPARK_STICK_DISINTEGRATED.asStack(1))
                .hideDuration(true)
                .duration(1)
                .save(provider);
        TwistCollapse.recipeBuilder("mana_mixed_will_stick_disintegration")
                .inputItems(SPARK_STICK.asStack(1))
                .outputItems(SPARK_STICK_DISINTEGRATED.asStack(1))
                .hideDuration(true)
                .duration(1)
                .save(provider);
    }
}
