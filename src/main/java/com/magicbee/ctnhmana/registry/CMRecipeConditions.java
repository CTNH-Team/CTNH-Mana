package com.magicbee.ctnhmana.registry;

import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import com.magicbee.ctnhmana.api.recipe.condition.BloodAltarCondition;
import com.magicbee.ctnhmana.api.recipe.condition.HellForgeCondition;
import com.magicbee.ctnhmana.api.recipe.condition.InfusionCellCastingCondition;
import com.magicbee.ctnhmana.api.recipe.condition.ZenithCondition;

public class CMRecipeConditions {

    /** 注术单元铸造：参数为所需凝聚仓魔力能量（long） */
    public static RecipeConditionType<InfusionCellCastingCondition> INFUSION_CELL_CASTING_CONDITION = GTRegistries.RECIPE_CONDITIONS
            .register(
                    "infusion_cell_casting_condition",
                    new RecipeConditionType<>(InfusionCellCastingCondition::new, InfusionCellCastingCondition.CODEC));

    public static RecipeConditionType<ZenithCondition> MANA_REACTOR_CONDITION = GTRegistries.RECIPE_CONDITIONS
            .register(
                    "mana_reactor_condition",
                    new RecipeConditionType<>(ZenithCondition::new, ZenithCondition.CODEC));
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
