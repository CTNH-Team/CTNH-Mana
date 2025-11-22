package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;

public class CMRecipeConditions {
    public static RecipeConditionType<ManaReactorCondition> MANA_REACTOR_CONDITION = GTRegistries.RECIPE_CONDITIONS.register(
            "zenith_condition", new RecipeConditionType<>(ManaReactorCondition::new, ManaReactorCondition.CODEC));
    public static void init() {}
}
