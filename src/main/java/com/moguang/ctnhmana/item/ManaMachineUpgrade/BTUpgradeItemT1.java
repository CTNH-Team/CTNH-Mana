package com.moguang.ctnhmana.item.ManaMachineUpgrade;

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

public class BTUpgradeItemT1 extends ManaMachineUpgradeItem {
    public BTUpgradeItemT1(Properties properties) {
        super(properties,"BT",BT_UPDATE_NAME);
    }
    @Override
    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe, BaseManaMachine machine)
    {
        var hatch=machine.getHatch();
        var true_parallel= ParallelLogic.getParallelAmount(machine,recipe,metric.parallel);
        metric.speed+=Math.min(0.25,true_parallel*0.025-0.025);
        metric.true_parallel=true_parallel;
        return metric;
    }
    @Override
    public BaseManaMachine.MachineMetric calculateNormalUpgrade(BaseManaMachine.MachineMetric metric,BaseManaMachine machine)
    {
        var hatch=machine.getHatch();

        metric.parallel+=Math.min(16,(hatch.getBTMana()/50000+hatch.getmaxBTMana()/200000));
        metric.speed+=Math.min(0.25, (double) hatch.getBTMana() /10000000);
        return metric;
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, itemTooltipsAdd(botaniacoreLang_t1,tooltipComponents), isAdvanced); // 调用父类方法以处理原版提示信息
    }

    @CN("§9繁蕊之簇拥")
    public static Lang BT_UPDATE_NAME;
    @CN(
            {
                    "偏向于植物魔法与魔力的升级",
                    "机器获得：",
                    "魔力凝聚仓每存有5W魔力，每存有20W魔力存储上限，就获得一点并行（最高16)",
                    "运行时的每一并行提供§a+2.5%§r机器工作速度（最高25%）",
                    "魔力凝聚仓存有的每10W魔力提供§a+2.5%§r机器工作速度（最高25%）",
                    "§o§9与花朵共舞吧，即使是机器也无法遮蔽群星的美丽§r"
            }
    )
    @EN(
            {
                    "偏向于植物魔法与魔力的升级",
                    "机器获得：",
                    "魔力凝聚仓每存有2.5W魔力，每存有20W魔力存储上限，就获得一点并行（最高16)",
                    "运行时的每一并行提供§a+2.5%§r机器工作速度（最高25%）",
                    "魔力凝聚仓存有的每10W魔力提供§a+2.5%§r机器工作速度（最高25%）",
                    "§o§9与花朵共舞吧，即使是机器也无法遮蔽群星的美丽§r"
            }
    )
    public static Lang[] botaniacoreLang_t1;

}
