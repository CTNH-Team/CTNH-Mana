package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.common.ManaMachine;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatch;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.failureManaLang_NoEnoughMana;

public class ManaForceTransformer extends ManaMachine implements IExplosionMachine{
    @Persisted
    public BlockPos hatchPos;
    public ManaHatch hatch;
    @Persisted
    public int baseconsumption=1;
    @Persisted
    public int consumption=4;
    public ManaForceTransformer(IMachineBlockEntity holder,int consumption) {
        super(holder);
        this.baseconsumption=consumption;
    }
    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe)
    {
        var mana=this.hatch.getMana();
        this.hatch.setMana(0);
        if(mana<100000) {
            RecipeLogic.putFailureReason(this,recipe,failureManaLang_NoEnoughMana.translate());
            return false;
        }
        return super.beforeWorking(recipe);
    }
    @Override
    public boolean onWorking() {
        if (getOffsetTimer() % 20 == 0) {
            if(hatch.consumeManaIfEnough(consumption))super.onWorking();
            else getRecipeLogic().setProgress(0);
        }
        return super.onWorking();
    }
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch(); //获取舱室
        if (this.hatch == null) onStructureInvalid(); //获取不到就别成型
        var tier = getTier();//获取等级
        this.consumption= (int) (baseconsumption*Math.pow(2,tier-1));
    }
    public ManaHatch getHatch() {
        for (IMultiPart part : this.getParts()) {
            if (part instanceof ManaHatch hatchs) {
                hatchPos = (hatchs).getPos();
                return hatchs;
            }
        }
        return null;
    }
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (machine instanceof ManaForceTransformer fmachine) {
            var mana = fmachine.hatch.getMana();
            int parallel = (int) (mana / 100000);
            var output = mana / 200000;
            var true_parallel = ParallelLogic.getParallelAmount(fmachine, recipe, parallel);
            ContentModifier modifer = new ContentModifier(true_parallel, output);
            return ModifierFunction.builder()
                    .parallels(true_parallel)
                    .inputModifier(ContentModifier.multiplier(true_parallel))
                    .outputModifier(modifer)
                    .eutModifier(ContentModifier.multiplier(true_parallel))
                    .build();
        }
        return ModifierFunction.IDENTITY;
    }
    @CN({
            "§9没有不科学，只有未被探及的科学§r",
            "§b没有不魔法，只有未被诠释的魔法§r",
            "运行前消耗魔力凝聚仓所有的魔力能量，每消耗10W魔力能力就§a+1§r最大并行，每消耗20W魔力能量就§a+1§r最终产出",
            "如果消耗的魔力能量小于10W，则§c吞噬所有的输入§r",
            "运行时每秒消耗4魔力能量，电压每高于LV一级，消耗量就翻倍",
    })
    @EN({
            "没有不科学，只有未被探及的科学",
            "没有不魔法，只有未被诠释的魔法",
            "运行前消耗魔力凝聚仓所有的魔力能量，每消耗10W魔力能力就§a+1§r最大并行，每消耗20W魔力能力就§a+1§r最终产出",
            "如果消耗的魔力能量小于10W，则§c吞噬所有的输入§r",
            "运行时每秒消耗4魔力能量，电压每高于LV一级，消耗量就翻倍",
    })
    public static Lang[] MFT_Lang;
}

