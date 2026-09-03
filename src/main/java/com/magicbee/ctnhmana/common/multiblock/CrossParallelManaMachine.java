package com.magicbee.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import net.minecraft.network.chat.Component;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
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
 * 其余（含无升级）为非流水线视野。
 * <p>
 * 两视野统一语义：
 * <ul>
 * <li>每个配方在<b>第一个 recipeModifier</b>锁定其并行数（读实时库存），只做结构性 IO/EU 缩放，
 * 增益倍率一律延后到批次定型；</li>
 * <li>{@link #parallelBudgetModifier} 只做批次累计的“提交检测”（确认并入的配方才计入）
 * 并强制两重预算：总并行预算（非 GT）= 升级并行帽；EU 预算 = 输入容量 Σ(电压×电流)；</li>
 * <li>{@link #modifyRecipeAfterMerge} 在批次定型统一应用增益并<b>最后超频</b>
 * （超频基准 = 批次总 EU/t，硬顶为机器超频电压，不会超压）。</li>
 * </ul>
 * 非流水线视野：并行消耗额外 EU（EU/t = Σ(EU_i×pa_i)），时长 = max(原始时长)/speed。
 * 流水线视野：并行按批处理模式增长时间而非 EU（EU/t = Σ base EU_i），时长 = Σ原始时长/speed×min(64, paTotal)。
 */
public class CrossParallelManaMachine extends BaseManaMachine implements ICrossParallelRecipeLogicMachine {

    //////////////////////////////////////
    // ******** 批次状态（不持久化） ********//
    //////////////////////////////////////
    /** recipeModifier 缓存：当前配方的原始时长（超频前） */
    protected transient int lastRawDuration;
    /** parallelBudgetModifier 缓存：待提交配方的原始时长 */
    protected transient int lastPendingRawDuration;
    /** 待提交配方的 EU/t 贡献（GT：base EU；非 GT：EU_i×pa_i） */
    protected transient long lastPendingEUt;
    /** 已确认并入批次的累计原始时长（GT 用） */
    protected transient long batchRawDuration;
    /** 已并入配方的 EU/t 累计（容量预算 + 超频基准依据） */
    protected transient long batchCommittedEUt;
    /** 已并入配方的 parallels 快照（提交标记） */
    protected transient int batchCommittedParallels;
    /** 非流水线视野：批次总并行预算（= 升级并行帽） */
    protected transient int batchParallelBudget;
    /** 最近一次开工批次的并行数（钩子缓存，供 UI 显示） */
    protected transient int workingParallels = 1;

    public CrossParallelManaMachine(IMachineBlockEntity holder, int consumption) {
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
        if (machine instanceof CrossParallelManaMachine mm) {
            if (mm.isGTView()) return gtRecipeModifier(mm, group, recipe);
            return nonGTRecipeModifier(mm, group, recipe); // 非流水线视野：增益全部延后
        }
        return RecipeModifier.nullWrongType(CrossParallelManaMachine.class, machine);
    }

    /**
     * 流水线视野：只做配方级流水线并行——算并行数 pa、IO 放大、缓存原始时长/EU；时间/电压一律不动。
     * 不调用升级项的 calculateUpgrade（其 speed/eut 计算基于单配方 pa，与批次语义冲突）。
     */
    private static Component gtRecipeModifier(CrossParallelManaMachine mm, RecipeHandlerGroup group, GTRecipe recipe) {
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

    /**
     * 非流水线视野：在第一个修改器锁定并行数 pa（读实时库存，帽 = 升级并行帽），
     * 并用剩余电压预算二次限制并行（已并入配方占用的电压不重复计算），
     * 只做结构性 IO/EU×pa 缩放，不改时长、不施加任何升级增益。
     */
    private static Component nonGTRecipeModifier(CrossParallelManaMachine mm, RecipeHandlerGroup group,
                                                 GTRecipe recipe) {
        mm.recipemetric.Copy(mm.metric);
        mm.recipemetric.plus(mm.globalmetric);
        int cap = mm.upgrade != null ? mm.upgrade.getMaxParallelCap(mm.recipemetric, mm) :
                Math.max(1, mm.recipemetric.parallel);
        int pa = Math.max(1, CTNHManaUtils.getParallelAmount(group, recipe, cap, true));
        // 已占用电压 = 合并配方的 EU/t（tickInputs 已累加，等于 Σ 各配方贡献，无提交滞后问题）
        long occupied = 0;
        if (mm.getRecipeLogic() instanceof CrossParallelRecipeLogic logic && logic.mergedRecipe != null) {
            occupied = RecipeHelper.getRealEUt(logic.mergedRecipe);
        }
        long remaining = capacityOf(mm) - occupied;
        long recipeEUt = RecipeHelper.getRealEUt(recipe); // applyParallel 前 = base EU
        if (remaining > 0 && recipeEUt > 0) {
            pa = Math.min(pa, (int) Math.max(1, Math.min(Integer.MAX_VALUE, remaining / recipeEUt)));
        }
        CTNHManaUtils.applyParallel(recipe, pa); // 结构性：输入/输出/EU×pa、parallels=pa
        mm.lastRawDuration = recipe.duration; // = 原始时长
        return null;
    }

    //////////////////////////////////////
    // ******** ParallelBudgetModifier ********//
    //////////////////////////////////////
    public static Component parallelBudgetModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (!(machine instanceof CrossParallelManaMachine mm)) return null;
        if (!(mm.getRecipeLogic() instanceof CrossParallelRecipeLogic logic)) return null;
        if (!mm.isGTView()) return nonGTBudget(mm, logic, recipe);
        return gtBudget(mm, logic, recipe);
    }

    /** 输入容量 Σ(电压×电流)，与 GTCEu EURecipeCapability 同口径；无能量舱时不限。 */
    private static long capacityOf(CrossParallelManaMachine mm) {
        long capacity = 0;
        for (var handler : mm.getCapabilitiesFlat(IO.IN, EURecipeCapability.CAP)) {
            if (handler instanceof IEnergyContainer container) {
                capacity += container.getInputVoltage() * container.getInputAmperage();
            }
        }
        return capacity > 0 ? capacity : Long.MAX_VALUE;
    }

    /**
     * 非流水线视野：批次累计的“提交检测”（确认并入的配方才计入 EU 与并行），
     * 并强制总并行预算（= 升级并行帽）与 EU 预算（Σ(EU_i×pa_i) ≤ 输入容量）。
     */
    private static Component nonGTBudget(CrossParallelManaMachine mm, CrossParallelRecipeLogic logic, GTRecipe recipe) {
        if (logic.mergedRecipe == null) {
            // 新轮次：重置批次状态
            mm.batchParallelBudget = Math.max(1,
                    mm.upgrade != null ? mm.upgrade.getMaxParallelCap(mm.recipemetric, mm) :
                            Math.max(1, mm.recipemetric.parallel));
            mm.batchCommittedParallels = 0;
            mm.batchCommittedEUt = 0;
        } else {
            int committed = logic.mergedRecipe.parallels;
            if (committed != mm.batchCommittedParallels) {
                // 上一配方已确认并入：提交其 EU/t 贡献
                mm.batchCommittedEUt += mm.lastPendingEUt;
                mm.batchCommittedParallels = committed;
            }
        }
        long candidateEUt = RecipeHelper.getRealEUt(recipe); // 非 GT：EU_i×pa_i（applyParallel 已缩放）
        if (logic.mergedRecipe != null) {
            int used = logic.mergedRecipe.parallels;
            int incoming = Math.max(1, recipe.parallels);
            if (used + incoming > mm.batchParallelBudget) {
                return RecipeModifier.DEFAULT_FAILURE; // 超并行预算：本配方不并入
            }
        }
        if (mm.batchCommittedEUt + candidateEUt > capacityOf(mm)) {
            return RecipeModifier.DEFAULT_FAILURE; // 超电压预算：本配方不并入
        }
        mm.lastPendingEUt = candidateEUt;
        return null;
    }

    /**
     * 流水线视野：批次累计的“提交检测”——只有确认并入的配方才计入累计，
     * 并强制 EU 预算（Σ base EU_i ≤ 输入容量）。最终时长计算在
     * {@link #modifyRecipeAfterMerge} 统一完成。
     */
    private static Component gtBudget(CrossParallelManaMachine mm, CrossParallelRecipeLogic logic, GTRecipe recipe) {
        if (logic.mergedRecipe == null) {
            // 新轮次：重置批次状态
            mm.batchCommittedParallels = 0;
            mm.batchRawDuration = 0;
            mm.batchCommittedEUt = 0;
        } else {
            int committed = logic.mergedRecipe.parallels;
            if (committed != mm.batchCommittedParallels) {
                // 上一配方已确认并入：提交其原始时长与 EU/t 贡献
                mm.batchRawDuration += mm.lastPendingRawDuration;
                mm.batchCommittedEUt += mm.lastPendingEUt;
                mm.batchCommittedParallels = committed;
            }
        }
        long candidateEUt = RecipeHelper.getRealEUt(recipe); // GT：base EU（不乘 pa）
        if (mm.batchCommittedEUt + candidateEUt > capacityOf(mm)) {
            return RecipeModifier.DEFAULT_FAILURE; // 超电压预算：本配方不并入
        }
        // 缓存当前配方的待提交数据
        mm.lastPendingRawDuration = mm.lastRawDuration;
        mm.lastPendingEUt = candidateEUt;
        return null;
    }

    //////////////////////////////////////
    // ******** 批次定型 ********//
    //////////////////////////////////////
    /**
     * 批次全部确认并入后统一定型：施加全部增益，然后对最终配方计算超频。
     * 超频基准 = 批次总 EU/t，以机器超频电压为硬顶，超频后不会超压。
     */
    @Override
    public @Nullable Component modifyRecipeAfterMerge(GTRecipe recipe, RecipeHandlerGroup group) {
        return isGTView() ? gtModifyRecipeAfterMerge(recipe, group) : nonGTModifyRecipeAfterMerge(recipe, group);
    }

    /** 流水线视野：统一施加流水线速度与 EU 减成，最后超频。 */
    private Component gtModifyRecipeAfterMerge(GTRecipe recipe, RecipeHandlerGroup group) {
        if (recipe.parallels != batchCommittedParallels) {
            // 最后一个配方也已并入：提交它
            batchRawDuration += lastPendingRawDuration;
            batchCommittedEUt += lastPendingEUt;
            batchCommittedParallels = recipe.parallels;
        }
        if (batchRawDuration <= 0) return null;
        workingParallels = Math.max(1, recipe.parallels);

        boolean t2 = upgrade instanceof GTUpgradeItemT2;
        int paTotal = workingParallels;
        double num = Math.max(0, recipemetric.parallel - 1); // 清除的并行数
        // 流水线 speed（沿用 GT 升级公式，量纲搬批次总量）
        double speed = t2 ? recipemetric.speed * Math.pow(1.05, num) + Math.min(20.00, paTotal * 0.05) :
                recipemetric.speed + num * 0.05 + Math.min(5.00, paTotal * 0.05);
        // 总时长 = Σ原始时长 × (1/speed) × min(64, paTotal)
        long finalDuration = Math.max(1, Math.round(batchRawDuration / speed * Math.min(64, paTotal)));
        recipe.duration = (int) finalDuration;
        // 电压减成（基于批次总“批处理时长”）
        double eutMult = t2 ?
                Math.max(0.2,
                        recipemetric.eut - num * 0.05 -
                                Math.min(1.0, 0.025 * (int) (batchRawDuration * paTotal / 2000))) :
                Math.max(0.5, recipemetric.eut - num * 0.025 -
                        Math.min(0.5, 0.025 * (int) (batchRawDuration * paTotal / 4000)));
        recipe.multiplyEUt(eutMult);
        // 回写批次最终倍率到 recipemetric，供侧边栏（ManaStatusGui）显示正确值；
        // 下一轮配方处理时 gtRecipeModifier 会重新 Copy+plus 覆盖，不影响计算。
        recipemetric.speed = speed;
        recipemetric.eut = eutMult;
        recipemetric.parallel = workingParallels;
        // 批次级超频（基准 = Σbase EU × eutMult；硬顶为超频电压，不会超压）
        OverclockingLogic.NON_PERFECT_OVERCLOCK.getModifier(this, group, recipe, getOverclockVoltage());
        return null;
    }

    /** 非流水线视野：统一施加升级增益（含 pa 依赖加成），最后超频。 */
    private Component nonGTModifyRecipeAfterMerge(GTRecipe recipe, RecipeHandlerGroup group) {
        if (recipe.parallels != batchCommittedParallels) {
            // 最后一个配方也已并入：提交它
            batchCommittedEUt += lastPendingEUt;
            batchCommittedParallels = recipe.parallels;
        }
        workingParallels = Math.max(1, recipe.parallels);
        int batchParallel = workingParallels;

        // 批次级增益：以批次总并行统一计算（BM 意志 / BT 魔力每批次消耗一次）
        MachineMetric m = new MachineMetric();
        m.Copy(recipemetric);
        if (upgrade != null) {
            m = upgrade.calculateBatchUpgrade(m, recipe, batchParallel, this, group);
        }
        if (m.input != 1.0) {
            CTNHManaUtils.multiplyInputs(recipe, Math.max(1, (int) Math.round(m.input)));
        }
        if (m.output != 1.0) {
            recipe.multiplyOutputs(Math.max(1, (int) Math.round(m.output)));
        }
        if (m.eut != 1.0) {
            recipe.multiplyEUt(m.eut);
        }
        // 时长 = max(原始时长) / speed；recipe.duration 此刻 = mergeRecipe 取的最大原始时长
        recipe.duration = (int) Math.max(1, Math.round(recipe.duration / Math.max(0.01, m.speed)));
        recipe.parallels = batchParallel;
        // 批次级超频（基准 = Σ(EU_i×pa_i) × eut；硬顶为超频电压，不会超压）
        OverclockingLogic.NON_PERFECT_OVERCLOCK.getModifier(this, group, recipe, getOverclockVoltage());
        return null;
    }

    //////////////////////////////////////
    // ******** BATCH_MODE 视野包装 ********//
    //////////////////////////////////////
    /** 流水线视野跳过 BATCH_MODE（其时长放大与流水线语义冲突），非流水线视野原样执行。 */
    public static Component batchModeViewAware(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof CrossParallelManaMachine mm && mm.isGTView()) return null;
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

    @CN({
            "§a启用跨配方并行§r,一个批次最多合并§a64§r个不同配方",
            "所有配方的升级加成§a统一按批次总并行计算§r,批次并行上限§a等同于并行上限§r",
            "批次时长取§a其中最长的配方时长§r,并行只提升电压消耗而不增长时间",
            "§5启用流水线升级时§r,所有配方都会统一计入流水线的加成之中",
            "§5流水线视野下§r,并行改为§5增长时间而非电压§r,批次时长为各配方时长之和",
            "§e批次总电压不会超过能量仓输入容量§r,超出预算的配方会降并行或不并入",
            "§e超频在批次合并完毕后对最终配方统一计算§r"
    })
    @EN({
            "§aEnables cross-recipe parallelism§r: up to §a64§r different recipes merge into one batch",
            "All upgrade bonuses are §acomputed once from the batch total parallel§r, and the batch parallel limit§a equals the parallel limit§r",
            "Batch duration is §athe longest recipe duration in the batch§r; parallelism raises EU/t instead of duration",
            "§5When a pipeline upgrade is installed§r, all recipes are uniformly included in the pipeline bonus",
            "§5In pipeline view§r, parallelism §5raises duration instead of EU/t§r, and batch duration is the sum of recipe durations",
            "§eBatch total EU/t never exceeds the energy hatch input capacity§r; over-budget recipes are scaled down or left out",
            "§eOverclocking is applied once to the final recipe after the batch is merged§r"
    })
    public static Lang[] crossParallelLang;
}
