package com.moguang.ctnhmana.item.ManaMachineUpgrade;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.moguang.ctnhmana.Mutiblock.BaseManaMachine;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatches.BloodManaHatch;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.List;

public class BMUpgradeItemT2 extends ManaMachineUpgradeItem {

    public BMUpgradeItemT2(Properties properties) {
        super(properties, "BM", BM_UPDATE_NAME_T2);
    }

    public double BASE_CONSUPTION = 0.5;
    public double BASE_MIN = 10;

    @Override
    public BaseManaMachine.MachineMetric calculateNormalUpgrade(BaseManaMachine.MachineMetric metric,
                                                                BaseManaMachine machine) {
        boolean stead = false;
        if (machine.getHatch() instanceof BloodManaHatch hatch) {
            if (hatch.getSoulNet() != null && hatch.getOrb() != null) {
                metric.parallel += hatch.getOrb().getTier() * 2;
                metric.parallel += hatch.getSoulNet().getCurrentEssence() / 250000;
                metric.parallel = Math.min(metric.parallel, 64);
            }
            if (hatch.rawWill >= 20) {
                metric.parallel *= 3;
            }
            if (hatch.steadfastWill >= BASE_MIN) {
                stead = true;
            }
            if (hatch.destructiveWill >= BASE_MIN) {
                metric.speed += 2.5;
                if (!stead) metric.output -= 0.1;

            }
            if (hatch.vengefulWill >= BASE_MIN) {
                metric.eut *= 0.6;
                if (!stead) metric.speed -= 0.15;
            }
            if (hatch.corrosiveWill >= BASE_MIN) {
                if (!stead) metric.parallel *= 0.75;
                metric.speed += (metric.speed - 1) * 1.5;
            }
        }

        return metric;
    }

    @Override
    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe,
                                                          BaseManaMachine machine) {
        var tier = machine.getTier();
        int consume = 0;
        if (machine.getHatch() instanceof BloodManaHatch hatch) {
            if (hatch.rawWill >= 20) {
                hatch.rawWill -= tier * 2 * BASE_CONSUPTION;
                consume += 1;
            }
            if (hatch.steadfastWill >= BASE_MIN) {
                hatch.steadfastWill -= (tier - 1) * BASE_CONSUPTION;
                consume += 1;
            }
            if (hatch.destructiveWill >= BASE_MIN) {
                hatch.destructiveWill -= (tier - 1) * BASE_CONSUPTION;
                consume += 1;
            }
            if (hatch.vengefulWill >= BASE_MIN) {
                hatch.vengefulWill -= (tier - 1) * BASE_CONSUPTION;
                consume += 1;
            }
            if (hatch.corrosiveWill >= BASE_MIN) {
                hatch.corrosiveWill -= (tier - 1) * BASE_CONSUPTION;
                consume += 1;
            }

        }
        if (consume >= 5) metric.parallel = Integer.MAX_VALUE;
        metric.true_parallel = ParallelLogic.getParallelAmount(machine, recipe, metric.parallel);
        return metric;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, itemTooltipsAdd(bmcoreLang_t2, tooltipComponents), isAdvanced); // 调用父类方法以处理原版提示信息
    }

    @CN({
            "偏向于生命源质和恶魔意志的升级",
            "机器获得：",
            "宝珠的每一等级提供§a+2§r最大并行,灵魂网络的每 250000LP提供§a+2§r最大并行（最多64）",
            "如果机器中的普通恶魔意志至少拥有20，则在运行时消耗电压等级点恶魔意志，使最大并行翻三倍",
            "如果机器中的特殊恶魔意志至少拥有10，则在运行时消耗0.5*（机器电压-1）点恶魔意志，获得以下效果:",
            "破坏意志：运行速度§a+250%§r，最终产物§c-20%§r",
            "复仇意志：消耗电压§a-40%§r，运行速度§c-15%§r",
            "侵蚀意志：使运行速度增幅§a+50%§r，最大并行§c-25%§r",
            "坚韧意志：§a消除以上所有意志的负面效果§r",
            "如果触发了每一种意志的效果，则运行时最大并行改为§4无限",
            "§c警告：极为不稳定的意志消耗模式§r",
            "§c禁忌不会成为通向真理的阻碍，我们将利用每一种被他人称作疯狂的可能性§r"
    })
    @EN({
            "偏向于生命源质和恶魔意志的升级",
            "机器获得：",
            "Orb tier gives §a+2§r max parallel each, and every 250000 LP in Soul Network gives §a+2§r max parallel (up to 64)",
            "If raw demon will is at least 20, consume voltage-tier amount of raw will during operation to triple max parallel",
            "如果机器中的每种特殊恶魔意志至少拥有10，则在运行时消耗0.5*（机器电压-1）点恶魔意志，获得以下效果:",
            "Destructive Will: Speed §a+250%§r, Final Output §c-20%§r",
            "Vengeful Will: EU Consumption §a-40%§r, Speed §c-15%§r",
            "Corrosive Will: Final Output §a+20%§r, Max Parallel §c-25%§r",
            "Steadfast Will: removes all negative effects above",
            "如果触发了每一种意志的效果，则运行时最大并行改为§4无限",
            "§c警告：不稳定的意志消耗模式§r",
            "§c禁忌不会成为通向真理的阻碍，我们将利用每一种被他人称作疯狂的可能性§r"
    })
    public static Lang[] bmcoreLang_t2;

    @CN("§4魂痕之铸造")
    public static Lang BM_UPDATE_NAME_T2;
}
