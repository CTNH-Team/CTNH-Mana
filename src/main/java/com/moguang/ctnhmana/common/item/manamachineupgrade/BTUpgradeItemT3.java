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

public class BTUpgradeItemT3 extends ManaMachineUpgradeItem {

    public BTUpgradeItemT3(Properties properties) {
        super(properties, "BT", BT_UPDATE_NAME_T3);
    }

    @Override
    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe,
                                                          BaseManaMachine machine, RecipeHandlerGroup group) {
        var hatch = machine.getHatch();
        var true_parallel = ParallelLogic.getParallelAmount(group, recipe, metric.parallel);
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
        super.appendHoverText(stack, level, itemTooltipsAdd(botaniacoreLang_t3, tooltipComponents), isAdvanced); // 调用父类方法以处理原版提示信??
    }

    @CN("§9群蕊之天思")
    @EN("§9Garden of Celestial Thought")
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
            "Favors Botania mana and machine throughput",
            "The machine gains:",
            "For every 50k Botania mana in the condenser, and for every 200k max storage, gain +2 parallelism (max 256)",
            "Each active parallel grants §a+5%§r processing speed (capped at +200%)",
            "Every 100k Botania mana stored grants §a+2.5%§r speed (capped at +50%)",
            "If base recipe time exceeds 100s, spend up to 50k Mana Energy: +25% speed per 10k Mana Energy consumed",
            "§o§9Dance with the flowers — even machines cannot hide the beauty of the stars§r"
    })
    public static Lang[] botaniacoreLang_t3;
}
