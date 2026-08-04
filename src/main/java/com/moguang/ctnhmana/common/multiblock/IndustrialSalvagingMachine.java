package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.ingredient.item.ItemIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.item.RangedItemIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import net.minecraft.network.chat.Component;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

/**
 * Industrial Salvaging Table controller. Extends {@link ManaMachine} for hatch/mana data hooks.
 */
public class IndustrialSalvagingMachine extends ManaMachine {

    @CN({
            "回收宝石和符文的产出",
            "初始具有64并行，电压每高于LV一级，并行*4",
            "电压达到MV、LuV时，产出最小值各+1(不会超出最大值)",
            "这分明是一个吃豆人，他不应该生成能量吗？"
    })
    @EN({
            "Recycles gems, affixed gear, and runes into materials and spirits",
            "Starts at 64 parallels; x4 for each voltage tier above LV",
            "At MV and LuV, each raises ranged output minimum by 1 (capped by maximum)",
            "That's clearly a Pac-Man. Shouldn't it be generating energy?"
    })
    public static Lang[] industrialSalvagingLang;

    public IndustrialSalvagingMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch();
        if (this.hatch == null) {
            onStructureInvalid();
        }
    }

    public static @Nullable Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group,
                                                     @NotNull GTRecipe recipe) {
        if (!(machine instanceof IndustrialSalvagingMachine imachine)) {
            return RecipeModifier.nullWrongType(IndustrialSalvagingMachine.class, machine);
        }

        int tier = imachine.getTier();
        int bonus = 0;
        if (tier >= GTValues.MV) bonus++;
        if (tier >= GTValues.LuV) bonus++;

        if (bonus > 0) {
            var outputs = recipe.outputs.get(ItemRecipeCapability.CAP);
            if (outputs != null) {
                for (int i = 0; i < outputs.size(); i++) {
                    if (!(outputs.get(i) instanceof RangedItemIngredient ranged)) continue;
                    int max = ranged.getCount();
                    int newMin = Math.min(max, ranged.getMinCount() + bonus);
                    if (newMin != ranged.getMinCount()) {
                        outputs.set(i, ItemIngredient.ranged(ranged.getInner(), newMin, max));
                    }
                }
            }
        }

        int steps = Math.max(0, tier - GTValues.LV);
        int limit = steps >= 13 ? Integer.MAX_VALUE : 64 << (2 * steps);
        int parallel = ParallelLogic.getParallelAmount(group, recipe, limit);
        if (parallel > 1) {
            recipe.multiplyAllContents(parallel);
            recipe.parallels *= parallel;
        }
        return null;
    }
}
