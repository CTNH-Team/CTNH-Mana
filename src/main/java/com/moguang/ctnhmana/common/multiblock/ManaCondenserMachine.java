package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.moguang.ctnhmana.registry.CMMaterials;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

public class ManaCondenserMachine extends RecipeElectricMultiblockMachine {

    public BlockPos poolPos = MachineUtils.getOffset(this, 0, 4, 0);
    public int parallel = 1;
    public int basicMana = 1000;
    public boolean reverse = false;

    public ManaCondenserMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        // if (machine instanceof ManaCondenserMachine mmachine) {
        // mmachine.parallel = ParallelLogic.getParallelAmount(mmachine, recipe, Integer.MAX_VALUE);
        // return CMRecipeModifiers.accurateParallel(machine, recipe, Integer.MAX_VALUE);
        // }
        return null;
    }

    @Override
    public boolean onWorking() {
        if (getOffsetTimer() % 5 == 0) {
            if (getLevel().getBlockEntity(poolPos) instanceof ManaPoolBlockEntity manaPoolBlockEntity) {
                if (reverse) {
                    manaPoolBlockEntity.receiveMana(basicMana * parallel);
                } else {
                    manaPoolBlockEntity.receiveMana(-basicMana * parallel);
                }
            }
        }
        return super.onWorking();
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        if (getLevel().getBlockEntity(poolPos) instanceof ManaPoolBlockEntity manaPoolBlockEntity) {
            if (recipe.data.get("mode") != null && recipe.data.getString("mode").equals("reverse")) {
                reverse = true;
                if (manaPoolBlockEntity.getAvailableSpaceForMana() < basicMana * parallel * 10) {
                    return Component.empty();
                }
                if (!MachineUtils.canInputFluid(CMMaterials.Mana.getFluid(basicMana * parallel / 5), this)) {
                    return Component.empty();
                }
                return super.beforeWorking(recipe);
            } else {
                reverse = false;
                if (manaPoolBlockEntity.getCurrentMana() < basicMana * parallel * 10) {
                    return Component.empty();
                }
                if (!MachineUtils.canOutputFluid(CMMaterials.Mana.getFluid(basicMana * parallel / 5), this)) {
                    return Component.empty();
                }
                return super.beforeWorking(recipe);
            }
        }
        return Component.empty();
    }
}
