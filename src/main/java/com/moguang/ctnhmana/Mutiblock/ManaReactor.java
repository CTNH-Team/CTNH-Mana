package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.moguang.ctnhmana.common.recipe.ZenithCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaReactor extends BaseManaMachine {

    public ManaReactor(IMachineBlockEntity holder, int consumption) {
        super(holder, consumption);
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        return true;
    }

    @Override
    protected @Nullable GTRecipe getRealRecipe(GTRecipe recipe) {
        SyncManaData();
        List<ZenithCondition> conditions = recipe.conditions.stream()
                .filter(ZenithCondition.class::isInstance)
                .map(ZenithCondition.class::cast)
                .toList();
        if (conditions.isEmpty()) return super.getRealRecipe(recipe);
        var condition = conditions.get(0);
        if (!condition.getZenithType().equals("Blank")) {
            var type = condition.getType();
            var tier = condition.getTier();
            if (tier > 0 && tier - ManaLevel.get(type) > 0) {
                var speed_up = 0.2 * (tier - ManaLevel.get(type));
            }
        }

        var newRecipe = recipe.copy();
        return super.getRealRecipe(newRecipe);
    }
}
