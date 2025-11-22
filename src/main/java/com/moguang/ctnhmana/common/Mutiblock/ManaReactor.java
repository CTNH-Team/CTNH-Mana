package com.moguang.ctnhmana.common.Mutiblock;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaReactor extends BaseManaMachine{

    public ManaReactor(IMachineBlockEntity holder, int consumption) {
        super(holder, consumption);
    }
    @Override
    public boolean alwaysTryModifyRecipe() {
        return true;
    }

    @Override
    protected @Nullable GTRecipe getRealRecipe(GTRecipe recipe) {
        List<ManaReactorCondition> conditions = recipe.conditions.stream().filter(ManaReactorCondition.class::isInstance)
                .map(ManaReactorCondition.class::cast)
                .toList();
        var newRecipe = recipe.copy();
        return super.getRealRecipe(newRecipe);
    }
}
