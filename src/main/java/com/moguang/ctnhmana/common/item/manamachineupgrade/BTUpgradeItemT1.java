package com.moguang.ctnhmana.common.item.manamachineupgrade;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.common.multiblock.BaseManaMachine;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

public class BTUpgradeItemT1 extends ManaMachineUpgradeItem {

    public BTUpgradeItemT1(Properties properties) {
        super(properties, "BT", BT_UPDATE_NAME);
    }

    @Override
    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe,
                                                          BaseManaMachine machine, RecipeHandlerGroup group) {
        var hatch = machine.getHatch();
        var true_parallel = ParallelLogic.getParallelAmount(group, recipe, metric.parallel);
        metric.speed += Math.min(0.16, true_parallel * 0.01);
        metric.true_parallel = true_parallel;
        return metric;
    }

    @Override
    public BaseManaMachine.MachineMetric calculateNormalUpgrade(BaseManaMachine.MachineMetric metric,
                                                                BaseManaMachine machine) {
        var hatch = machine.getHatch();

        metric.parallel += Math.min(16, (hatch.getBTMana() / 50000 + hatch.getmaxBTMana() / 200000));
        metric.speed += Math.min(0.1, (double) hatch.getBTMana() / 100000 * 0.02);
        return metric;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, itemTooltipsAdd(botaniacoreLang_t1, tooltipComponents), isAdvanced); // 调用父类方法以处理原版提示信??
    }

    @CN("§9孤蕊之独舞")
    @EN("§9Solo Bloom Waltz")
    public static Lang BT_UPDATE_NAME;
    @CN({
            "偏向于植物魔法与魔力的升级",
            "机器获得：",
            "魔力凝聚仓每存有5W植物魔法魔力，每存有20W植物魔法魔力存储上限，就获得一点并行(最高16)",
            "运行时的每一并行提供§a+1%§r机器工作速度(最高16%)",
            "魔力凝聚仓存有的每10W植物魔法魔力提供§a+2%§r机器工作速度(最高10%)",
            "§o§9与花朵共舞吧，即使是机器也无法遮蔽群星的美丽§r"
    })
    @EN({
            "偏向于植物魔法与魔力的升级",
            "机器获得：",
            "魔力凝聚仓每存有5W植物魔法魔力，每存有20W植物魔法魔力存储上限，就获得一点并行(最高16)",
            "运行时的每一并行提供§a+1%§r机器工作速度(最高16%)",
            "魔力凝聚仓存有的每10W植物魔法魔力提供§a+2%§r机器工作速度(最高10%)",
            "§o§9与花朵共舞吧，即使是机器也无法遮蔽群星的美丽§r"
    })
    public static Lang[] botaniacoreLang_t1;
}
