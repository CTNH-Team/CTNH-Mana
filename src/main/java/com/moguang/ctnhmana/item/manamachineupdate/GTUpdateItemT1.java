package com.moguang.ctnhmana.item.manamachineupdate;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.moguang.ctnhmana.common.Mutiblock.BaseManaMachine;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.List;

public class GTUpdateItemT1 extends ManaMachineUpdateItem{
    public GTUpdateItemT1(Properties properties) {
        super(properties, "GT", GT_UPDATE_NAME);
    }

    @Override
    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe, BaseManaMachine machine)
    {
        var hatch=machine.getHatch();
        var true_parallel= ParallelLogic.getParallelAmount(machine,recipe,metric.parallel);
        metric.speed+=Math.min(0.4,true_parallel*0.1-0.01);
        if(!hatch.getInventory().isEmpty())metric.speed-=0.1;
        if(hatch.getBT_Mana()>=100000)metric.speed-=0.1;
        return metric;
    }
    public BaseManaMachine.MachineMetric calculateNormalUpgrade(BaseManaMachine.MachineMetric metric,BaseManaMachine machine)
    {
        var hatch=machine.getHatch();
        metric.parallel+=64;
        return metric;
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, itemTooltipsAdd(gtcoreLang_t1,tooltipComponents), isAdvanced); // 调用父类方法以处理原版提示信息
    }
    @CN("§5流线之视野")
    public static Lang GT_UPDATE_NAME;
    @CN(
            {
                    "偏向于大规模工业流水线的升级",
                    "机器获得：",
                    "任何时候都具有64并行",
                    "运行时的每一并行提供§a+1%§r机器工作速度（最高40%）",
                    "魔力凝聚仓存有§n物品§r时，§c-10%§r机器工作速度",
                    "魔力凝聚仓存有超过10W魔力时，§c-10%§r机器工作速度",
                    "§o§5真正至臻完美的流水线不应该容许任何非常态输入，只有gt流体和超级并行才是格雷员工的标配§r"
            }
    )
    @EN(
            {
                    "偏向于大规模工业流水线的升级",
                    "机器获得：",
                    "任何时候都具有64并行",
                    "运行时的每一并行提供§a+1%§r机器工作速度（最高40%）",
                    "魔力凝聚仓存有§n物品§r时，§c-10%§r机器工作速度",
                    "魔力凝聚仓存有超过10W魔力时，§c-10%§r机器工作速度",
                    "§o§5真正至臻完美的流水线不应该容许任何非常态输入，只有gt流体和超级并行才是格雷员工的标配§r"
            }
    )
    public static Lang[] gtcoreLang_t1;
}
