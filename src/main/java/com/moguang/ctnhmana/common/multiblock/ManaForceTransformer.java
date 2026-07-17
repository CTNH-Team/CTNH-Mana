package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.network.chat.Component;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import static com.moguang.ctnhmana.common.multiblock.BaseManaMachine.failureManaLang_NoEnoughMana;

public class ManaForceTransformer extends ManaMachine implements IExplosionMachine {

    @Persisted
    public int baseconsumption = 1;
    @Persisted
    public int consumption = 4;
    @Persisted
    public long consume_mana = 0;

    public ManaForceTransformer(IMachineBlockEntity holder, int consumption) {
        super(holder);
        this.baseconsumption = consumption;
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        var mana = this.hatch.getMana();
        this.hatch.setMana(0);
        if (mana < 100000) {
            return failureManaLang_NoEnoughMana.translate();
        }
        this.consume_mana = mana;
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        this.consume_mana = 0;
    }

    @Override
    public boolean onWorking() {
        if (getOffsetTimer() % 20 == 0) {
            if (hatch.consumeManaIfEnough(consumption)) super.onWorking();
            else getRecipeLogic().setProgress(0);
        }
        return super.onWorking();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch(); // 获取舱室
        if (this.hatch == null) onStructureInvalid(); // 获取不到就别成型
        var tier = getTier();// 获取等级
        this.consumption = (int) (baseconsumption * Math.pow(2, tier - 1));
        this.consume_mana = 0;
    }

    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof ManaForceTransformer fmachine) {
            var mana = fmachine.hatch.getMana();
            int parallel = 64 * (int) ((fmachine.consume_mana) / 100000);
            var true_parallel = ParallelLogic.getParallelAmount(group, recipe, parallel);
            recipe.multiplyAllContents(true_parallel);
            recipe.multiplyEUt(true_parallel);
            recipe.parallels = true_parallel;
            return null;
        }
        return null;
    }

    @CN({
            "§9没有不科学，只有未被探及的科学§r",
            "§b没有不魔法，只有未被诠释的魔法§r",
            "运行前消耗魔力凝聚仓所有的魔力能量，并且获得64最大并行,每额外消耗10W魔力能量就§a*2§r最大并行",
            "如果消耗的魔力能量小于10W，则§c吞噬所有的输入§r",
            "运行时每秒消耗4魔力能量，电压每高于LV一级，消耗量就翻倍",
    })
    @EN({
            "§9Nothing is unscientific—only science not yet charted§r",
            "§bNothing is unmagical—only magic not yet spoken§r",
            "Before each run: §edrains all§r mana from the condenser hatch; §abase§r max parallels §e64§r; each additional §e100k§r mana §adoubles§r max parallels (§a×2§r)",
            "If drained mana totals §cunder 100k§r, the recipe §cfails§r (§7mana is still emptied§r)",
            "While running: §e4§r mana/s; §edoubles§r per voltage tier above §eLV§r",
    })
    public static Lang[] MFT_Lang;
}
