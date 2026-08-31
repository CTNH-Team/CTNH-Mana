package com.magicbee.ctnhmana.common.item.manamachineupgrade;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.network.chat.Component;

import com.magicbee.ctnhmana.common.multiblock.BaseManaMachine;
import lombok.Getter;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

public class ManaMachineUpgradeItem extends ComponentItem {

    @Getter
    public String type;
    @Getter
    public Lang updateName;

    public ManaMachineUpgradeItem(Properties properties, String type, Lang updateName) {
        super(properties);
        this.type = type;
        this.updateName = updateName;
    }

    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe,
                                                          BaseManaMachine machine, RecipeHandlerGroup group) {
        return metric;
    }

    public BaseManaMachine.MachineMetric calculateNormalUpgrade(BaseManaMachine.MachineMetric metric,
                                                                BaseManaMachine machine) {
        return metric;
    }

    /**
     * 跨并专用：返回本升级允许的最大并行帽（纯查询，无副作用）。
     * 在首个 recipeModifier 调用，用于计算当前配方的并行数。
     */
    public int getMaxParallelCap(BaseManaMachine.MachineMetric metric, BaseManaMachine machine) {
        return Math.max(1, metric.parallel);
    }

    /**
     * 跨并专用：批次定型统一计算本批次增益，并处理一次性副作用（如 BM 意志消耗）。
     *
     * @param metric        基础 metric（每秒刷新的 metric + globalmetric）
     * @param recipe        合并后的配方（duration 为最大原始时长，供时长相关加成判断）
     * @param batchParallel 批次总并行（Σ 各配方并行）
     */
    public BaseManaMachine.MachineMetric calculateBatchUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe,
                                                               int batchParallel, BaseManaMachine machine,
                                                               RecipeHandlerGroup group) {
        metric.true_parallel = batchParallel;
        return metric;
    }

    public static List<Component> itemTooltipsAdd(Lang[] langs, List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }
}
