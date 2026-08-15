package com.magicbee.ctnhmana.common.multiblock;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import net.minecraft.network.chat.Component;

import com.magicbee.ctnhmana.common.item.manamachineupgrade.GTUpgradeItemT2;
import com.magicbee.ctnhmana.utils.CTNHManaUtils;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.api.CrossParallelRecipeLogic;
import tech.vixhentx.mcmod.ctnhlib.api.ICrossParallelRecipeLogicMachine;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

/**
 * 魔力粉碎机（流水线跨配方并行）。
 * <p>
 * 视野分派：{@code upgrade.type == "GT"}（流线之视野/超序之观测）为流水线视野，
 * 其余（含无升级）为非流水线视野，行为与 BaseManaMachine 完全一致。
 * <p>
 * 流水线视野语义：
 * <ul>
 * <li>{@link #recipeModifier} 只做配方级流水线并行（IO 放大 + 缓存原始时长），时间/电压不动；</li>
 * <li>{@link #parallelBudgetModifier} 只做批次累计的“提交检测”（确认并入的配方才计入），不写配方；</li>
 * <li>{@link #modifyRecipeAfterMerge} 在批次定型后统一应用流水线总时长与电压减成；
 * 超频时长/电压加成以“最后计入”方式叠加（k = min(k_i)，不参与原始时长计算）。</li>
 * </ul>
 */
public class ManaMaceratorMachine extends BaseManaMachine implements ICrossParallelRecipeLogicMachine {

    //////////////////////////////////////
    // ******** 批次状态（不持久化） ********//
    //////////////////////////////////////
    /** recipeModifier 缓存：当前配方的原始时长（超频前） */
    protected transient int lastRawDuration;
    /** parallelBudgetModifier 缓存：待提交配方的原始时长与超频倍率 */
    protected transient int lastPendingRawDuration;
    protected transient double lastPendingK;
    /** 已确认并入批次的累计原始时长与最小超频倍率 */
    protected transient long batchRawDuration;
    protected transient double batchMinK = Double.MAX_VALUE;
    /** 已并入配方的 parallels 快照（提交标记） */
    protected transient int batchCommittedParallels;
    /** 非流水线视野：批次总并行预算（= recipeModifier 后的机器最大并行量） */
    protected transient int batchParallelBudget;
    /** 最近一次开工批次的并行数（钩子缓存，供 UI 显示） */
    protected transient int workingParallels = 1;

    public ManaMaceratorMachine(IMachineBlockEntity holder, int consumption) {
        super(holder, consumption);
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new CrossParallelRecipeLogic(this);
    }

    private boolean isGTView() {
        return upgrade != null && "GT".equals(upgrade.getType());
    }

    //////////////////////////////////////
    // ******** RecipeModifier ********//
    //////////////////////////////////////
    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof ManaMaceratorMachine mm) {
            if (mm.isGTView()) return gtRecipeModifier(mm, group, recipe);
            return BaseManaMachine.recipeModifier(machine, group, recipe); // 非流水线视野：完全沿用现有逻辑
        }
        return RecipeModifier.nullWrongType(ManaMaceratorMachine.class, machine);
    }

    /**
     * 流水线视野：只做配方级流水线并行——算批处理数 pa、IO 放大、缓存原始时长；时间/电压一律不动。
     * 不调用升级项的 calculateUpgrade（其 speed/eut 计算基于单配方 pa，与批次语义冲突）。
     */
    private static Component gtRecipeModifier(ManaMaceratorMachine mm, RecipeHandlerGroup group, GTRecipe recipe) {
        mm.recipemetric.Copy(mm.metric);
        mm.recipemetric.plus(mm.globalmetric);
        boolean t2 = mm.upgrade instanceof GTUpgradeItemT2;
        int limit = t2 ? 1024 : 512;
        int pa = Math.max(1, CTNHManaUtils.getParallelAmount(group, recipe, limit, false));
        CTNHManaUtils.multiplyInputs(recipe, Math.max(1, (int) Math.round(pa * mm.recipemetric.input)));
        recipe.multiplyOutputs(Math.max(1, (int) Math.round(pa * mm.recipemetric.output)));
        recipe.parallels = pa;
        mm.lastRawDuration = recipe.duration; // 原始时长，超频修改器尚未运行
        return null;
    }

    //////////////////////////////////////
    // ******** ParallelBudgetModifier ********//
    //////////////////////////////////////
    public static Component parallelBudgetModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (!(machine instanceof ManaMaceratorMachine mm)) return null;
        if (!(mm.getRecipeLogic() instanceof CrossParallelRecipeLogic logic)) return null;
        if (!mm.isGTView()) return nonGTBudget(mm, logic, recipe);
        return gtBudget(mm, logic, recipe);
    }

    /**
     * 非流水线视野：批次总并行预算 = recipeModifier 后的机器最大并行量（metric.parallel 系）。
     * 合并中总并行超限的配方不并入（输入不扣，安全）。
     */
    private static Component nonGTBudget(ManaMaceratorMachine mm, CrossParallelRecipeLogic logic, GTRecipe recipe) {
        if (logic.mergedRecipe == null) {
            mm.batchParallelBudget = Math.max(1, mm.recipemetric.parallel); // 委托的 recipeModifier 已刷新 recipemetric
            return null;
        }
        int used = logic.mergedRecipe.parallels;
        int incoming = Math.max(1, recipe.parallels);
        if (used + incoming > mm.batchParallelBudget) {
            return RecipeModifier.DEFAULT_FAILURE; // 超预算：本配方不并入
        }
        return null;
    }

    /**
     * 流水线视野：批次累计的“提交检测”——只有确认并入的配方才计入累计，
     * 避免中途 checkRecipe/beforeWorking 失败的配方污染批次量。最终计算在
     * {@link #modifyRecipeAfterMerge} 统一完成。
     */
    private static Component gtBudget(ManaMaceratorMachine mm, CrossParallelRecipeLogic logic, GTRecipe recipe) {
        if (logic.mergedRecipe == null) {
            // 新轮次：重置批次状态
            mm.batchCommittedParallels = 0;
            mm.batchRawDuration = 0;
            mm.batchMinK = Double.MAX_VALUE;
        } else {
            int committed = logic.mergedRecipe.parallels;
            if (committed != mm.batchCommittedParallels) {
                // 上一配方已确认并入：提交其原始时长与超频倍率
                mm.batchRawDuration += mm.lastPendingRawDuration;
                mm.batchMinK = Math.min(mm.batchMinK, mm.lastPendingK);
                mm.batchCommittedParallels = committed;
            }
        }
        // 缓存当前配方的待提交数据（超频修改器已运行，此处 duration 为超频后有效时长）
        double k = (double) recipe.duration / Math.max(1, mm.lastRawDuration);
        mm.lastPendingRawDuration = mm.lastRawDuration;
        mm.lastPendingK = k;
        return null;
    }

    //////////////////////////////////////
    // ******** 批次定型 ********//
    //////////////////////////////////////
    /**
     * 流水线视野：批次全部确认并入后，统一应用最终总时长与电压减成。
     * 超频时长加成以 k = min(k_i) 最后计入（不参与原始时长/速度/减成计算）；
     * 超频电压加成保留（multiplyEUt 作用于已超频的批次内容之上）。
     */
    @Override
    public @Nullable Component modifyRecipeAfterMerge(GTRecipe recipe, RecipeHandlerGroup group) {
        if (!isGTView()) return null;
        if (recipe.parallels != batchCommittedParallels) {
            // 最后一个配方也已并入：提交它
            batchRawDuration += lastPendingRawDuration;
            batchMinK = Math.min(batchMinK, lastPendingK);
            batchCommittedParallels = recipe.parallels;
        }
        if (batchRawDuration <= 0) return null;
        workingParallels = Math.max(1, recipe.parallels);

        boolean t2 = upgrade instanceof GTUpgradeItemT2;
        int paTotal = workingParallels;
        double num = Math.max(0, recipemetric.parallel - 1); // 清除的并行数
        double k = batchMinK == Double.MAX_VALUE ? 1.0 : batchMinK; // 超频整体倍率（保守取最小）
        // 流水线 speed（沿用 GT 升级公式，量纲搬批次总量）
        double speed = t2
                ? recipemetric.speed * Math.pow(1.05, num) + Math.min(20.00, paTotal * 0.05)
                : recipemetric.speed + num * 0.05 + Math.min(5.00, paTotal * 0.05);
        // 总时长 = Σ原始时长 × (1/speed) × min(64, paTotal) × 超频倍率
        long finalDuration = Math.max(1, Math.round(batchRawDuration / speed * Math.min(64, paTotal) * k));
        recipe.duration = (int) finalDuration;
        // 电压减成（基于批次总“批处理时长”）
        double eutMult = t2
                ? Math.max(0.2, recipemetric.eut - num * 0.05 - Math.min(1.0, 0.025 * (int) (batchRawDuration * paTotal / 2000)))
                : Math.max(0.5, recipemetric.eut - num * 0.025 - Math.min(0.5, 0.025 * (int) (batchRawDuration * paTotal / 4000)));
        recipe.multiplyEUt(eutMult);
        // 回写批次最终倍率到 recipemetric，供侧边栏（ManaStatusGui）显示正确值；
        // 下一轮配方处理时 gtRecipeModifier 会重新 Copy+plus 覆盖，不影响计算。
        recipemetric.speed = speed;
        recipemetric.eut = eutMult;
        recipemetric.parallel = workingParallels;
        return null;
    }

    //////////////////////////////////////
    // ******** BATCH_MODE 视野包装 ********//
    //////////////////////////////////////
    /** 流水线视野跳过 BATCH_MODE（其时长放大与流水线语义冲突），非流水线视野原样执行。 */
    public static Component batchModeViewAware(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof ManaMaceratorMachine mm && mm.isGTView()) return null;
        return GTRecipeModifiers.BATCH_MODE.apply(machine, group, recipe);
    }

    //////////////////////////////////////
    // ******** UI ********//
    //////////////////////////////////////
    @Override
    protected void buildDisplayText(List<Component> textList) {
        super.buildDisplayText(textList);
        // 流水线视野 + 工作状态：并行数行换为批次总并行口径（super 活动分支最后一行即该行）
        if (this.isFormed() && this.isActive() && isGTView() && !textList.isEmpty()) {
            textList.remove(textList.size() - 1);
            int limit = upgrade instanceof GTUpgradeItemT2 ? 1024 : 512;
            textList.add(BaseManaMachineWorkingParallelLang.translate(workingParallels, limit));
        }
    }
    @CN(
            {
                    "§a启用跨配方并行§r,允许一次性并行不同的配方,每个配方分别计算升级加成,批次并行上限§a等同于并行上限§r",
                    "§5启用流水线升级时§r,所有配方都会统一计入流水线的加成之中"
            }
    )
    @EN({
            "§aEnables cross-recipe parallelism§r, allowing multiple different recipes to run at once. Each recipe gets its own upgrade bonus, and the batch parallel limit§a equals the parallel limit§r",
            "§5When a pipeline upgrade is installed§r, all recipes are uniformly included in the pipeline bonus"
    })
    public static Lang[] manaMaceratorLang;
}