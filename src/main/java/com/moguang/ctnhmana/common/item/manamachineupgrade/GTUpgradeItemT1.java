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

public class GTUpgradeItemT1 extends ManaMachineUpgradeItem {

    public GTUpgradeItemT1(Properties properties) {
        super(properties, "GT", GT_UPDATE_NAME);
    }

    @Override
    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe,
                                                          BaseManaMachine machine, RecipeHandlerGroup group) {
        if (metric.parallel > 1) {
            var num = metric.parallel - 1;
            metric.parallel = -1;
            metric.speed += num * 0.05;
            metric.eut -= num * 0.025;
        }
        var true_patch_parallel = ParallelLogic.getParallelAmount(group, recipe, 512, false);
        metric.speed += Math.min(5.00, true_patch_parallel * 0.05);
        metric.eut -= Math.min(0.5, 0.025 * (int) (recipe.duration * true_patch_parallel / 4000));
        metric.eut = Math.max(0.5, metric.eut);
        metric.true_parallel = true_patch_parallel;
        return metric;
    }

    public BaseManaMachine.MachineMetric calculateNormalUpgrade(BaseManaMachine.MachineMetric metric,
                                                                BaseManaMachine machine) {
        metric.parallel += 1;
        return metric;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, itemTooltipsAdd(gtcoreLang_t1, tooltipComponents), isAdvanced); // 调用父类方法以处理原版提示信�?
    }

    @CN("§5流线之视�?)
    @EN("§5Streamlined Horizon")
    public static Lang GT_UPDATE_NAME;
    @CN({
            "偏向于大规模工业流水线的升级",
            "机器获得�?,
            "机器的并行§a+1§r，在运行前§c清除机器所有的并行§r",
            "每清除一点并行，获得以下加成:",
            "最终机器速度§a+5%r，§a-2.5%§r能量消�?,
            "将机器的并行转化为至�?12§b魔力批处理§r:",
            "运行多倍配方不再提升电压消耗，而是提升等量时间",
            "每有1魔力批处理，机器速度获得§a+5%§r，最多§a+500%§r",
            "经过批处理后的配方每�?00s，§a-2.5%§r能量消耗，最�?50%",
            "魔力批处理大�?4的部分不再提升配方的时间",
            "§o§5格雷员工的意志铸就了机器的灵能，每一个机器都如黑洞一般地吞噬着所有的输入§r"
    })
    @EN({
            "偏向于大规模工业流水线的升级",
            "机器获得�?,
            "机器的并行§a+1§r，在运行前§c清除机器所有的并行§r",
            "每清除一点并行，获得以下加成:",
            "最终机器速度§a+5%，§a-2.5%§r能量消�?,
            "将机器的并行转化为至�?56§b魔力批处理§r:",
            "运行多倍配方不再提升电压消耗，而是提升等量时间",
            "每有1魔力批处理，机器速度获得§a+2%§r，最�?222%",
            "经过批处理后的配方每�?00s，§a-2.5%§r能量消耗，最�?50%",
            "魔力批处理大�?4的部分不再提升配方的时间",
            "§o§5格雷员工的意志铸就了机器的灵能，每一个机器都如黑洞一般地吞噬着所有的输入§r"
    })
    public static Lang[] gtcoreLang_t1;
}