package com.magicbee.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.network.chat.Component;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.magicbee.ctnhmana.utils.CTNHManaUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

public class TwistedFusionMachine extends RecipeElectricMultiblockMachine {

    public int mks = 0;

    public TwistedFusionMachine(IMachineBlockEntity holder, int mk) {
        super(holder);
        this.mks = mk;
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) {
            return failureManaLang_NoEnoughTwistLevel.translate(mks);
        }
        var startEU = recipe.data.getLong("eu_to_start");
        if (startEU >= 160000000 && startEU <= 320000000 && mks < 2) {
            return failureManaLang_NoEnoughTwistLevel.translate(2);
        }
        if (startEU > 320000000 && mks < 3) {
            return failureManaLang_NoEnoughTwistLevel.translate(3);
        }
        return super.beforeWorking(recipe);
    }

    public static @Nullable Component recipeModifier(@NotNull MetaMachine machine, RecipeHandlerGroup group,
                                                     @NotNull GTRecipe recipe) {
        if (!(machine instanceof TwistedFusionMachine zmachine)) {
            return RecipeModifier.nullWrongType(TwistedFusionMachine.class, machine);
        }
        if (!recipe.recipeType.equals(GTRecipeTypes.FUSION_RECIPES)) {
            return null;
        }

        if (zmachine.mks > 5 && zmachine.mks < Integer.MAX_VALUE - 1) {
            int pa = CTNHManaUtils.getParallelAmount(group, recipe, 1024);
            if (pa <= 1) return null;
            // 加电压并行，再叠旧折扣：总 EU = eut * pa * 0.25 * 0.99^pa，时长同旧公式
            CTNHManaUtils.applyParallel(recipe, pa);
            double scale = 0.25 * Math.pow(0.99, pa);
            recipe.multiplyEUt(scale);
            recipe.multiplyDuration(0.25 * pa * Math.pow(0.99, pa));
            return null;
        }
        if (zmachine.mks >= Integer.MAX_VALUE - 1) {
            int pa = CTNHManaUtils.getParallelAmount(group, recipe, Integer.MAX_VALUE - 10);
            if (pa <= 1) return null;
            CTNHManaUtils.applyParallel(recipe, pa);
            double scale = 0.0001 * Math.pow(0.9, pa);
            recipe.multiplyEUt(scale);
            recipe.multiplyDuration(0.0001 * pa * Math.pow(0.9, pa));
            return null;
        }

        recipe.multiplyDuration(1.0 / (1 + 0.5 * zmachine.mks));
        return null;
    }

    @CN("等级不足，至少需要扭曲等级:%d")
    @EN("Insufficient twist tier — need at least twist tier %d")
    public static Lang failureManaLang_NoEnoughTwistLevel;
}
