package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;

public class CMRecipeConditions {

    public static RecipeConditionType<ManaReactorCondition> MANA_REACTOR_CONDITION = GTRegistries.RECIPE_CONDITIONS
            .register(
                    "mana_reactor_condition",
                    new RecipeConditionType<>(ManaReactorCondition::new, ManaReactorCondition.CODEC));
    public static RecipeConditionType<HellForgeCondition> HELL_FORGE_CONDITION = GTRegistries.RECIPE_CONDITIONS
            .register(
                    "hell_forge_condition",
                    new RecipeConditionType<>(HellForgeCondition::new, HellForgeCondition.CODEC));
    public static RecipeConditionType<BloodAltarCondition> BLOOD_ALTAR_CONDITION = GTRegistries.RECIPE_CONDITIONS
            .register(
                    "blood_altar_condition",
                    new RecipeConditionType<>(BloodAltarCondition::new, BloodAltarCondition.CODEC));

    public static void init() {}
}
