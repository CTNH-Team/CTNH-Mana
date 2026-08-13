package com.magicbee.ctnhmana.common;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import dev.shadowsoffire.hostilenetworks.Hostile;
import dev.shadowsoffire.hostilenetworks.item.DataModelItem;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

public class DigitalWosMachine extends SimpleTieredMachine {

    public double multiplier = 0;

    public DigitalWosMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction,
                             Object... args) {
        super(holder, tier, tankScalingFunction, args);
    }

    /** Resolve LP multiplier from the current data-model data count. */
    public double resolveMultiplier() {
        if (importItems.isEmpty()) {
            return 0;
        }
        ItemStack stack = (ItemStack) importItems.getContents().get(0);
        if (!stack.is(Hostile.Items.DATA_MODEL.get())) {
            return 0;
        }
        var count = DataModelItem.getData(stack);
        if (count < 6) return 0;
        if (count < 48) return 1;
        if (count < 300) return 1.5;
        if (count < 900) return 2;
        return 3;
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        multiplier = resolveMultiplier();
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
            // modifyRecipe runs BEFORE beforeWorking — must resolve multiplier here, not use the stale field
            double mult = dmachine.resolveMultiplier() * Math.pow(1.2, dmachine.getTier() - 3);

            recipe.outputs.multiply(mult);
            recipe.tickOutputs.multiply(mult);
        }
        return null;
    }

    @CN({
            "§4你能听到它们在机器中的哀嚎吗？",
            "根据数据模型等级提升产出,并升级数据模型",
            "模型最高只能升级到进阶等级",
            "电压每高于HV一级，最终产量*1.2",
    })
    @EN({
            "你能听到它们在机器中的哀嚎吗？",
            "根据数据模型等级提升产出,并升级数据模型",
            "模型最高只能升级到进阶等级",
            "电压每高于HV一级，最终产量*1.2",
    })
    public static Lang[] digitalWosTooltip;
}
