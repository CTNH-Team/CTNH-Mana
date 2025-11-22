package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import com.moguang.ctnhmana.common.recipe.PlantCasingCondition;

public class CMRecipeConditions {
    public static RecipeConditionType<ManaReactorCondition> MANA_REACTOR_CONDITION = GTRegistries.RECIPE_CONDITIONS.register(
            "mana_reactor_condition", new RecipeConditionType<>(ManaReactorCondition::new, ManaReactorCondition.CODEC));
    public static RecipeConditionType<PlantCasingCondition> PLANT_CASING = GTRegistries.RECIPE_CONDITIONS.register(
            "plant_casing_condition", new RecipeConditionType<>(PlantCasingCondition::new, PlantCasingCondition.CODEC));
    public static void init() {}
}
