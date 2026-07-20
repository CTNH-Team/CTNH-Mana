package com.moguang.ctnhmana.common;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import dev.shadowsoffire.hostilenetworks.Hostile;
import dev.shadowsoffire.hostilenetworks.item.DataModelItem;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.Nullable;

public class DigitalWosMachine extends SimpleTieredMachine {

    public double multiplier = 0;

    public DigitalWosMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction,
                             Object... args) {
        super(holder, tier, tankScalingFunction, args);
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        if (!importItems.isEmpty()) {
            ItemStack stack = (ItemStack) importItems.getContents().get(0);
            if (stack.is(Hostile.Items.DATA_MODEL.get())) {
                var count = DataModelItem.getData(stack);
                if (count < 6) multiplier = 0;
                else if (count < 48) multiplier = 1;
                else if (count < 300) multiplier = 1.5;
                else if (count < 900) multiplier = 2;
                else multiplier = 3;
            }
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        if (!importItems.isEmpty()) {
            ItemStack stack = (ItemStack) importItems.getContents().get(0);
            if (stack.is(Hostile.Items.DATA_MODEL.get())) {
                var count = DataModelItem.getData(stack);
                if (count < 54) {
                    DataModelItem.setData(stack, count + 1);
                }
            }
        }
        super.afterWorking();
    }

    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof DigitalWosMachine dmachine) {
            recipe.outputs.multiply((int) dmachine.multiplier);
            recipe.tickOutputs.multiply((int) dmachine.multiplier);
        }
        return null;
    }
}
