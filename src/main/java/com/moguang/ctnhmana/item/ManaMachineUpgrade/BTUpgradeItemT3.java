package com.moguang.ctnhmana.item.ManaMachineUpgrade;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.moguang.ctnhmana.Mutiblock.BaseManaMachine;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.List;

public class BTUpgradeItemT3 extends ManaMachineUpgradeItem {

    public BTUpgradeItemT3(Properties properties) {
        super(properties, "BT", BT_UPDATE_NAME_T3);
    }

    @Override
    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe,
                                                          BaseManaMachine machine) {
        var hatch = machine.getHatch();
        var true_parallel = ParallelLogic.getParallelAmount(machine, recipe, metric.parallel);
        if (recipe.duration >= 100 * 20) {
            var accelerate = Math.min(5, (int) machine.getHatch().Mana / 10000);
            metric.speed += accelerate * 0.5;
            machine.getHatch().consumeManaIfEnough(accelerate * 10000);
        }
        metric.speed += Math.min(2.00, true_parallel * 0.05);
        metric.true_parallel = true_parallel;
        return metric;
    }

    @Override
    public BaseManaMachine.MachineMetric calculateNormalUpgrade(BaseManaMachine.MachineMetric metric,
                                                                BaseManaMachine machine) {
        var hatch = machine.getHatch();

        metric.parallel += Math.min(256, 2 * (hatch.getBTMana() / 50000 + hatch.getmaxBTMana() / 200000));
        metric.speed += Math.min(0.5, (double) hatch.getBTMana() / 100000 * 0.025);
        return metric;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, itemTooltipsAdd(botaniacoreLang_t3, tooltipComponents), isAdvanced); // 调用父类方法以处理原版提示信息
    }

    @CN("§9群蕊之天思")
    public static Lang BT_UPDATE_NAME_T3;
    @CN({
            "偏向于植物魔法与魔力的升级",
            "机器获得：",
            "魔力凝聚仓每存有5W植物魔法魔力，每存有20W植物魔法魔力存储上限，就获得2点并行(最高256)",
            "运行时的每一并行提供§a+5%§r机器工作速度（最高200%）",
            "魔力凝聚仓存有的每10W植物魔法魔力提供§a+2.5%§r机器工作速度（最高50%）",
            "如果配方的原始时间大于100s,则消耗至多5W魔力能量，每消耗1W魔力能量使运行速度增加25%",
            "§o§9与花朵共舞吧，即使是机器也无法遮蔽群星的美丽§r"
    })
    @EN({
            "偏向于植物魔法与魔力的升级",
            "机器获得：",
            "魔力凝聚仓每存有2.5W魔力，每存有20W魔力存储上限，就获得一点并行（最高16)",
            "运行时的每一并行提供§a+2.5%§r机器工作速度（最高25%）",
            "魔力凝聚仓存有的每10W魔力提供§a+2.5%§r机器工作速度（最高25%）",
            "如果配方的原始时间大于100s,则消耗至多5W魔力能量，每消耗1W魔力能量则提供单次的供§a+50%§r运行速度",
            "§o§9与花朵共舞吧，即使是机器也无法遮蔽群星的美丽§r"
    })
    public static Lang[] botaniacoreLang_t3;
}
