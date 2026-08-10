package com.moguang.ctnhmana.common.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.WidgetUtils;
import com.gregtechceu.gtceu.api.gui.editor.EditableMachineUI;
import com.gregtechceu.gtceu.api.gui.editor.EditableUI;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.WorkLogic;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.api.machine.gem.GemSublimatorRules;
import com.moguang.ctnhmana.api.machine.gem.GemSublimatorRules.MaterialBoost;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemInstance;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.function.BiFunction;

/**
 * 宝石携刻机（单方块，ULV–UV；注册 id 仍为 gem_sublimator）。
 * <p>
 * 设计要点（参考 Fisher 的自定义 tick 进度，而非标准 GT 配方逻辑）：
 * <ul>
 *   <li>Apoth 宝石：原地改稀有度，主槽物品类型不变、不消耗</li>
 *   <li>GT 精致宝石：消耗 1 个，向输出槽产出对应有瑕疵(rare) Apoth 宝石</li>
 *   <li>进度条显示 progress/maxProgress（进度点），不是秒</li>
 *   <li>宝石粉在「本轮开始」时一次性扣除；粉不足则不启动</li>
 *   <li>无电也可缓慢涨进度；通电按电压加速并扣 EU</li>
 * </ul>
 */
public class GemSublimatorMachine extends WorkableTieredMachine implements IFancyUIMachine, IMachineLife {

    public static final int GEM_SLOT = 0;
    public static final int DUST_SLOT = 0;
    /** 与 {@link GemSublimatorRules#MATERIAL_SLOTS} 长度一致 */
    public static final int MATERIAL_SLOTS = 5;

    /** 主槽：Apoth 宝石 或 GT 精致宝石 */
    @Persisted
    protected final NotifiableItemStackHandler gemInventory;
    /** 宝石粉槽 */
    @Persisted
    protected final NotifiableItemStackHandler dustInventory;
    /** 下方 5 格珍宝材料（uncommon→ancient） */
    @Persisted
    protected final NotifiableItemStackHandler materialInventory;
    /** 精致转化产物输出；Apoth 升级不经过此槽 */
    @Persisted
    protected final NotifiableItemStackHandler outputInventory;
    /** 电池/蓄能器充电槽 */
    @Getter
    @Persisted
    protected final CustomItemStackHandler chargerInventory;

    /** 当前进度点（同步到客户端供 UI 显示） */
    @Getter
    @Persisted
    @DescSynced
    private int progress;

    /** 本轮目标进度上限 */
    @Getter
    @Persisted
    @DescSynced
    private int maxProgress;

    /**
     * 本轮是否已经扣过宝石粉。
     * true 后即使粉被拿走也继续跑完本轮；完成后随 {@link #resetRun()} 清零。
     */
    @Persisted
    private boolean dustConsumed;

    /**
     * 本轮锁定模式：
     * <ul>
     *   <li>0 = 空闲</li>
     *   <li>1 = Apoth 稀有度升级</li>
     *   <li>2 = 精致 → 有瑕疵转化</li>
     * </ul>
     */
    @Persisted
    private int runMode;

    @Nullable
    protected TickableSubscription batterySubs;

    public GemSublimatorMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
        this.gemInventory = createGemHandler();
        this.dustInventory = createDustHandler();
        this.materialInventory = createMaterialHandler();
        this.outputInventory = createOutputHandler();
        this.chargerInventory = createChargerItemHandler();
    }

    /** 仅允许可充放电的电池类物品（含可选 FE 兼容）。 */
    protected CustomItemStackHandler createChargerItemHandler() {
        var handler = new CustomItemStackHandler();
        handler.setFilter(item -> GTCapabilityHelper.getElectricItem(item) != null ||
                (ConfigHolder.INSTANCE.compat.energy.nativeEUToFE &&
                        GTCapabilityHelper.getForgeEnergyItem(item) != null));
        return handler;
    }

    protected NotifiableItemStackHandler createGemHandler() {
        var handler = new NotifiableItemStackHandler(this, 1, IO.BOTH, IO.BOTH);
        handler.setFilter(stack -> GemSublimatorRules.isApothGem(stack) || GemSublimatorRules.isExquisiteGem(stack));
        return handler;
    }

    protected NotifiableItemStackHandler createDustHandler() {
        var handler = new NotifiableItemStackHandler(this, 1, IO.BOTH, IO.IN);
        handler.setFilter(stack -> stack.is(Adventure.Items.GEM_DUST.get()));
        return handler;
    }

    /**
     * 材料槽过滤器只校验「是某种珍宝材料」。
     * 具体槽位是否匹配在 {@link #applyMaterialBoosts()} 里按 index 再判一次；
     * 错位放的材料不会被消耗也不会加速。
     */
    protected NotifiableItemStackHandler createMaterialHandler() {
        var handler = new NotifiableItemStackHandler(this, MATERIAL_SLOTS, IO.BOTH, IO.IN);
        handler.setFilter(stack -> {
            for (int i = 0; i < MATERIAL_SLOTS; i++) {
                if (GemSublimatorRules.isMaterialForSlot(stack, i)) {
                    return true;
                }
            }
            return false;
        });
        return handler;
    }

    protected NotifiableItemStackHandler createOutputHandler() {
        return new NotifiableItemStackHandler(this, 1, IO.BOTH, IO.OUT);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (isRemote()) return;
        chargerInventory.setOnContentsChanged(this::updateBatterySubscription);
        updateBatterySubscription();
        // WorkLogic 负责周期性调用 serverRunningTick
        getWorkLogic().updateTickSubscription();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (batterySubs != null) {
            batterySubs.unsubscribe();
            batterySubs = null;
        }
    }

    /** 破坏方块时清空全部自定义库存，避免物品丢失。 */
    @Override
    public void onMachineRemoved() {
        clearInventory(chargerInventory);
        clearInventory(gemInventory.storage);
        clearInventory(dustInventory.storage);
        clearInventory(materialInventory.storage);
        clearInventory(outputInventory.storage);
    }

    /**
     * 始终保持 tick 订阅：主槽/粉/材料变化后也能立刻响应，
     * 不必像 Fisher 那样依赖各类 listener 手动唤醒。
     */
    @Override
    public boolean keepSubscribing() {
        return true;
    }

    @Override
    public boolean shouldWeatherOrTerrainExplosion() {
        return false;
    }

    /** 电池槽有可充放电对象时挂上充电 tick。 */
    protected void updateBatterySubscription() {
        if (energyContainer.dischargeOrRechargeEnergyContainers(chargerInventory, 0, true)) {
            batterySubs = subscribeServerTick(batterySubs, this::chargeBattery);
        } else if (batterySubs != null) {
            batterySubs.unsubscribe();
            batterySubs = null;
        }
    }

    protected void chargeBattery() {
        if (!energyContainer.dischargeOrRechargeEnergyContainers(chargerInventory, 0, false)) {
            updateBatterySubscription();
        }
    }

    /**
     * 服务端主循环（每 tick）：
     * <ol>
     *   <li>软锤暂停 → idle</li>
     *   <li>解析主槽能否开跑 / 目标进度</li>
     *   <li>中途换宝石或模式变化 → 重置本轮</li>
     *   <li>未扣粉则检查粉（精致模式还要检查输出空位）并一次性扣粉</li>
     *   <li>珍宝材料瞬间加速</li>
     *   <li>通电加速，否则走无电慢涨</li>
     *   <li>满进度结算</li>
     * </ol>
     */
    @Override
    public void serverRunningTick() {
        if (!isWorkingEnabled()) {
            setStatus(WorkLogic.Status.IDLE);
            return;
        }

        ItemStack gem = gemInventory.getStackInSlot(0);
        RunContext ctx = resolveRun(gem);
        if (ctx == null) {
            // 无效输入或已满级 / 电压不够 / 完美占位拒绝
            resetRun();
            setStatus(WorkLogic.Status.IDLE);
            return;
        }

        // 目标变了（例如玩家中途换成另一档宝石）则丢弃旧进度，重新锁定
        if (maxProgress != ctx.maxProgress || runMode != ctx.mode) {
            if (runMode != 0 && (runMode != ctx.mode || maxProgress != ctx.maxProgress)) {
                resetRun();
            }
            maxProgress = ctx.maxProgress;
            runMode = ctx.mode;
        }

        // —— 开跑扣粉（每轮仅一次）——
        if (!dustConsumed) {
            // 精致转化：输出槽塞不下则不扣粉、不启动，避免卡死浪费粉
            if (ctx.mode == 2) {
                var material = GemSublimatorRules.getExquisiteMaterial(gem);
                ItemStack preview = material == null ? ItemStack.EMPTY :
                        GemSublimatorRules.createExquisiteResult(material);
                if (preview.isEmpty() || !outputInventory.storage.insertItem(0, preview, true).isEmpty()) {
                    setStatus(WorkLogic.Status.IDLE);
                    return;
                }
            }
            int need = ctx.dustCost;
            ItemStack dust = dustInventory.getStackInSlot(0);
            if (dust.getCount() < need) {
                setStatus(WorkLogic.Status.IDLE);
                return;
            }
            dust.shrink(need);
            dustInventory.setStackInSlot(0, dust);
            dustConsumed = true;
        }

        // 材料加速优先；材料有自己的上限，不受无电 1000 / LV 通电 2500 等「渠道硬顶」回拉
        applyMaterialBoosts();

        // 「不能超过 XX」只限制对应增速渠道，且不得把其它渠道已推上去的进度钳回去
        long euCost = GemSublimatorRules.energyPerTick(getTier());
        boolean canPower = euCost > 0 && energyContainer.getEnergyStored() >= euCost;
        if (canPower) {
            int poweredGain = GemSublimatorRules.tickProgressGain(getTier(), true, progress);
            if (poweredGain > 0 && tryDrainEnergy()) {
                int before = progress;
                int after = progress + poweredGain;
                progress = GemSublimatorRules.clampChannelGain(before, after,
                        GemSublimatorRules.poweredHardCap(getTier()), maxProgress);
            }
            // 已通电但本档增益为 0（如 LV≥2500）：本 tick 不再走无电自然涨
        } else if (GemSublimatorRules.isNaturalThrottled(progress)) {
            // 无电且已过 1000 阻遏：按档位保底爬升（碎裂1s / 开裂2s / 瑕疵5s / 其后60s / 精致2s）
            LootRarity current = null;
            if (ctx.mode() == 1 && GemSublimatorRules.isApothGem(gem)) {
                current = GemInstance.unsocketed(gem).rarity().get();
            }
            int crawlInterval = GemSublimatorRules.unpoweredCrawlInterval(ctx.mode(), current);
            if (crawlInterval > 0 && getOffsetTimer() % crawlInterval == 0) {
                progress = GemSublimatorRules.applyNaturalCrawlGain(progress, maxProgress);
            }
        } else if (getOffsetTimer() % GemSublimatorRules.UNPOWERED_INTERVAL == 0) {
            // 无电未过阻遏：每 5 tick +1
            progress = GemSublimatorRules.applyNaturalGain(progress, maxProgress);
        }

        setStatus(WorkLogic.Status.WORKING);

        if (progress >= maxProgress) {
            completeRun(ctx);
        }
    }

    /**
     * 尝试按当前档 VA 扣 1A 电。
     * ULV 的 energyPerTick 为 0，永远走无电路径。
     */
    private boolean tryDrainEnergy() {
        long eu = GemSublimatorRules.energyPerTick(getTier());
        if (eu <= 0) {
            return false;
        }
        if (energyContainer.getEnergyStored() < eu) {
            return false;
        }
        return energyContainer.removeEnergy(eu) >= eu;
    }

    /**
     * 扫描 5 个材料槽：槽位物品与规则表匹配且尚未顶满该材料上限时，
     * 消耗 1 个并立刻加进度（再钳到本轮 maxProgress）。
     */
    private void applyMaterialBoosts() {
        for (int i = 0; i < MATERIAL_SLOTS; i++) {
            ItemStack stack = materialInventory.getStackInSlot(i);
            if (!GemSublimatorRules.isMaterialForSlot(stack, i)) {
                continue;
            }
            MaterialBoost boost = GemSublimatorRules.MATERIAL_SLOTS[i];
            int before = progress;
            int after = boost.apply(progress);
            if (after != before) {
                stack.shrink(1);
                materialInventory.setStackInSlot(i, stack);
                progress = Math.min(after, maxProgress);
            }
        }
    }

    /**
     * 进度满时结算。
     * <ul>
     *   <li>模式 1：原地 {@link AffixHelper#setRarity}，不清空主槽</li>
     *   <li>模式 2：扣 1 精致，向输出槽写入 Apoth 宝石；输出再次模拟失败则保留进度不结算</li>
     * </ul>
     * 成功后 {@link #resetRun()}，若主槽仍可升下一档，下一 tick 会自动开新一轮。
     */
    private void completeRun(RunContext ctx) {
        ItemStack gem = gemInventory.getStackInSlot(0);
        if (ctx.mode == 1) {
            AffixHelper.setRarity(gem, ctx.targetRarity);
            gemInventory.setStackInSlot(0, gem);
        } else if (ctx.mode == 2) {
            var material = GemSublimatorRules.getExquisiteMaterial(gem);
            ItemStack out = material == null ? ItemStack.EMPTY : GemSublimatorRules.createExquisiteResult(material);
            if (out.isEmpty()) {
                return;
            }
            ItemStack remainder = outputInventory.storage.insertItem(0, out, true);
            if (!remainder.isEmpty()) {
                return;
            }
            gem.shrink(1);
            gemInventory.setStackInSlot(0, gem);
            outputInventory.storage.insertItem(0, out, false);
        }
        resetRun();
    }

    /** 清空本轮进度与扣粉标记，准备下一轮或进入 idle。 */
    private void resetRun() {
        progress = 0;
        maxProgress = 0;
        dustConsumed = false;
        runMode = 0;
    }

    /**
     * 根据主槽内容解析本轮能否运行。
     *
     * @return null 表示不可启动（空槽、满级、电压不够、完美占位、映射失败等）
     */
    @Nullable
    private RunContext resolveRun(ItemStack gem) {
        // —— Apoth 宝石稀有度升级 ——
        if (GemSublimatorRules.isApothGem(gem)) {
            GemInstance inst = GemInstance.unsocketed(gem);
            if (!inst.isValidUnsocketed() || inst.isMaxRarity()) {
                return null;
            }
            LootRarity current = inst.rarity().get();
            LootRarity next = RarityRegistry.next(inst.rarity()).get();
            // next==current 表示已是全局最高稀有度；canTarget 负责电压上限与 ancient 占位
            if (next == current || !GemSublimatorRules.canTargetRarity(next, getTier())) {
                return null;
            }
            int max = GemSublimatorRules.maxProgressForUpgrade(current);
            if (max <= 0) {
                return null;
            }
            return new RunContext(1, max, GemSublimatorRules.dustCost(current), next);
        }
        // —— GT 精致 → Apoth 有瑕疵 ——
        if (GemSublimatorRules.isExquisiteGem(gem)) {
            LootRarity rare = GemSublimatorRules.rarityByPath("rare");
            if (rare == null || !GemSublimatorRules.canTargetRarity(rare, getTier())) {
                return null;
            }
            if (GemSublimatorRules.createExquisiteResult(GemSublimatorRules.getExquisiteMaterial(gem)).isEmpty()) {
                return null;
            }
            return new RunContext(2, GemSublimatorRules.PROGRESS_EXQUISITE_TO_RARE,
                    GemSublimatorRules.dustCostExquisite(), rare);
        }
        return null;
    }

    /**
     * 单轮运行上下文（不持久化，每 tick 由主槽重算）。
     *
     * @param mode         1=Apoth 升级，2=精致转化
     * @param maxProgress  本轮目标进度
     * @param dustCost     开跑一次性粉耗
     * @param targetRarity 完成后的目标稀有度（精致模式固定 rare）
     */
    private record RunContext(int mode, int maxProgress, int dustCost, LootRarity targetRarity) {}

    /** UI 进度条用的 0~1 比例。 */
    public double getProgressPercent() {
        return maxProgress <= 0 ? 0 : (double) progress / (double) maxProgress;
    }

    //////////////////////////////////////
    // ********** GUI ***********//
    //////////////////////////////////////

    @CN("进度：%s/%s")
    @EN("Progress: %s/%s")
    public static Lang progressHover;

    @CN("能量：%s / %s EU")
    @EN("Energy: %s / %s EU")
    public static Lang energyHover;

    @CN({
            "§b吸收天地之精华§r",
            "将宝石升级到更高等级，并提供格雷宝石转化为神话宝石的配方",
            "启动不需要电量；不通电时进度缓慢增加，通电时进度会额外增加",
    })
    @EN({
            "§bAbsorb the Essence of Heaven and Earth§r",
            "Upgrade gems to higher rarities, and convert GregTech exquisite gems into Apotheosis gems",
            "No power required to start; progress grows slowly when unpowered, and faster when powered",
    })
    public static Lang[] gemSublimatorTooltip;

    @CN("最多可以把宝石升级到%s品质")
    @EN("Can upgrade gems up to %s quality")
    public static Lang gemSublimatorMaxQualityTooltip;

    @CN("通电时，进度会增加%s")
    @EN("When powered, progress increases: %s")
    public static Lang gemSublimatorPoweredGainTooltip;

    @CN("在下方UI给予稀有度材料以加速进度；按住CTRL查询相应数据")
    @EN("Place rarity materials in the lower UI to boost progress; hold CTRL for details")
    public static Lang gemSublimatorCtrlHintTooltip;

    /**
     * 通电增速说明，按电压档：0=ULV … 5=IV，6=LuV+。
     */
    @CN({
            "无法通电加速",
            "每tick +1，最多至2500",
            "每tick +4，达到5000后减速为 +1",
            "每tick +16，达到10000后减速为 +4",
            "每tick +64，达到25000后减速为 +8",
            "每tick +64，达到50000后减速为 +16",
            "每tick +64",
    })
    @EN({
            "no powered acceleration",
            "+1/tick, up to 2500",
            "+4/tick, then +1 after 5000",
            "+16/tick, then +4 after 10000",
            "+64/tick, then +8 after 25000",
            "+64/tick, then +16 after 50000",
            "+64/tick",
    })
    public static Lang[] gemSublimatorPoweredGainByTier;

    @CN({
            "珍宝材料加速数据：",
            "陈旧布匹：+50进度，最多加速至1000",
            "发光水晶碎片：+250进度，最多加速至5000",
            "玄奥沙：+2000进度，最多加速至10000",
            "神铸珍珠：+5000进度，无上限",
            "无限之体现：+100000进度，无上限",
    })
    @EN({
            "Treasure material boosts:",
            "Timeworn Fabric: +50 progress, up to 1000",
            "Luminous Crystal Shard: +250 progress, up to 5000",
            "Arcane Sands: +2000 progress, up to 10000",
            "Godforged Pearl: +5000 progress, uncapped",
            "Manifestation of Infinity: +100000 progress, uncapped",
    })
    public static Lang[] gemSublimatorCtrlTooltip;

    /** 本档机器「最多升到 XX 品质」行。 */
    public static Component maxQualityTooltipLine(int tier) {
        LootRarity max = GemSublimatorRules.maxTargetRarity(tier);
        Component name = max != null ? max.toComponent() : Component.literal("?");
        return gemSublimatorMaxQualityTooltip.translate(name);
    }

    /** 本档机器「通电时进度增加 XXX」行。 */
    public static Component poweredGainTooltipLine(int tier) {
        int idx = switch (tier) {
            case GTValues.ULV -> 0;
            case GTValues.LV -> 1;
            case GTValues.MV -> 2;
            case GTValues.HV -> 3;
            case GTValues.EV -> 4;
            case GTValues.IV -> 5;
            default -> 6;
        };
        return gemSublimatorPoweredGainTooltip.translate(gemSublimatorPoweredGainByTier[idx].translate());
    }

    /**
     * 可编辑 UI：左侧能量条+电池，右侧功能区（主槽/粉/进度/输出/材料）。
     * 布局仿 Fisher，便于与 GT 单方块机器 UI 习惯一致。
     */
    public static BiFunction<ResourceLocation, Integer, EditableMachineUI> EDITABLE_UI_CREATOR = Util
            .memoize((path, tier) -> new EditableMachineUI("misc", path, () -> {
                var template = createTemplate().createDefault();
                var energyBar = createSublimatorEnergyBar().createDefault();
                var batterySlot = createBatterySlot().createDefault();
                var energyGroup = new WidgetGroup(0, 0, energyBar.getSize().width, energyBar.getSize().height + 20);
                batterySlot.setSelfPosition(
                        new Position((energyBar.getSize().width - 18) / 2, energyBar.getSize().height + 1));
                energyGroup.addWidget(energyBar);
                energyGroup.addWidget(batterySlot);
                var group = new WidgetGroup(0, 0,
                        Math.max(energyGroup.getSize().width + template.getSize().width + 4 + 16, 186),
                        Math.max(template.getSize().height + 8, energyGroup.getSize().height + 8));
                var size = group.getSize();
                energyGroup.setSelfPosition(new Position(3, (size.height - energyGroup.getSize().height) / 2));
                template.setSelfPosition(new Position(
                        (size.width - energyGroup.getSize().width - 4 - template.getSize().width) / 2 + 2 +
                                energyGroup.getSize().width + 2,
                        (size.height - template.getSize().height) / 2));
                group.addWidget(energyGroup);
                group.addWidget(template);
                return group;
            }, (template, machine) -> {
                if (machine instanceof GemSublimatorMachine sublimator) {
                    createTemplate().setupUI(template, sublimator);
                    createSublimatorEnergyBar().setupUI(template, sublimator);
                    createBatterySlot().setupUI(template, sublimator);
                }
            }));

    protected static EditableUI<SlotWidget, GemSublimatorMachine> createBatterySlot() {
        return new EditableUI<>("battery_slot", SlotWidget.class, () -> {
            var slotWidget = new SlotWidget();
            slotWidget.setBackground(GuiTextures.SLOT, GuiTextures.CHARGER_OVERLAY);
            return slotWidget;
        }, (slotWidget, machine) -> {
            slotWidget.setHandlerSlot(machine.chargerInventory, 0);
            slotWidget.setCanPutItems(true);
            slotWidget.setCanTakeItems(true);
        });
    }

    /** 左侧能量条：hover 显示当前电量 / 容量（EU）。 */
    protected static EditableUI<ProgressWidget, GemSublimatorMachine> createSublimatorEnergyBar() {
        return new EditableUI<>("energy_container", ProgressWidget.class, () -> {
            var progressBar = new ProgressWidget(ProgressWidget.JEIProgress, 0, 0, 18, 60,
                    new ProgressTexture(IGuiTexture.EMPTY, GuiTextures.ENERGY_BAR_BASE));
            progressBar.setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP);
            progressBar.setBackground(GuiTextures.ENERGY_BAR_BACKGROUND);
            return progressBar;
        }, (progressBar, machine) -> {
            progressBar.setProgressSupplier(() -> {
                long cap = machine.energyContainer.getEnergyCapacity();
                return cap <= 0 ? 0 : machine.energyContainer.getEnergyStored() * 1d / cap;
            });
            progressBar.setDynamicHoverTips(p -> energyHover.translate(
                    FormattingUtil.formatNumbers(machine.energyContainer.getEnergyStored()),
                    FormattingUtil.formatNumbers(machine.energyContainer.getEnergyCapacity())).getString());
        });
    }

    /**
     * 功能区模板：
     * <pre>
     * [宝石] [粉] [====进度====] [输出]
     * [材0][材1][材2][材3][材4]
     * </pre>
     * 进度条 hover 显示「进度：当前/上限」，不是 JEI 秒数。
     */
    protected static EditableUI<WidgetGroup, GemSublimatorMachine> createTemplate() {
        return new EditableUI<>("functional_container", WidgetGroup.class, () -> {
            // 输出槽右缘约 106，原宽 98 会裁切；加宽并留右内边距
            int templateWidth = Math.max(4 + MATERIAL_SLOTS * 18 + 4, 4 + 40 + 44 + 18 + 6);
            WidgetGroup main = new WidgetGroup(0, 0, templateWidth, 18 * 3 + 24);

            SlotWidget gemSlot = new SlotWidget();
            gemSlot.initTemplate();
            gemSlot.setSelfPosition(new Position(4, 4));
            gemSlot.setBackground(GuiTextures.SLOT);
            gemSlot.setId("gem_slot");
            main.addWidget(gemSlot);

            SlotWidget dustSlot = new SlotWidget();
            dustSlot.initTemplate();
            dustSlot.setSelfPosition(new Position(4 + 18 + 4, 4));
            dustSlot.setBackground(GuiTextures.SLOT, GuiTextures.DUST_OVERLAY);
            dustSlot.setId("dust_slot");
            main.addWidget(dustSlot);

            ProgressWidget bar = new ProgressWidget(ProgressWidget.JEIProgress, 4 + 40, 4, 40, 18,
                    new ProgressTexture(IGuiTexture.EMPTY, GuiTextures.PROGRESS_BAR_ARROW.getSubTexture(0, 0.5, 1, 0.5)));
            bar.setFillDirection(ProgressTexture.FillDirection.LEFT_TO_RIGHT);
            bar.setBackground(GuiTextures.PROGRESS_BAR_ARROW.getSubTexture(0, 0, 1, 0.5));
            bar.setId("progress_bar");
            main.addWidget(bar);

            SlotWidget outSlot = new SlotWidget();
            outSlot.initTemplate();
            outSlot.setSelfPosition(new Position(4 + 40 + 44, 4));
            outSlot.setBackground(GuiTextures.SLOT);
            outSlot.setId("out_slot");
            main.addWidget(outSlot);

            for (int i = 0; i < MATERIAL_SLOTS; i++) {
                SlotWidget mat = new SlotWidget();
                mat.initTemplate();
                mat.setSelfPosition(new Position(4 + i * 18, 4 + 18 + 8));
                mat.setBackground(GuiTextures.SLOT);
                mat.setId("mat_slot_" + i);
                main.addWidget(mat);
            }

            main.setBackground(GuiTextures.BACKGROUND_INVERSE);
            return main;
        }, (group, machine) -> {
            // 按 widget id 绑定真实库存；模板与实例分离便于 Editable UI
            WidgetUtils.widgetByIdForEach(group, "^gem_slot$", SlotWidget.class, slot -> {
                slot.setHandlerSlot(machine.gemInventory.storage, 0);
                slot.setCanTakeItems(true);
                slot.setCanPutItems(true);
            });
            WidgetUtils.widgetByIdForEach(group, "^dust_slot$", SlotWidget.class, slot -> {
                slot.setHandlerSlot(machine.dustInventory.storage, 0);
                slot.setCanTakeItems(true);
                slot.setCanPutItems(true);
            });
            WidgetUtils.widgetByIdForEach(group, "^out_slot$", SlotWidget.class, slot -> {
                slot.setHandlerSlot(machine.outputInventory.storage, 0);
                slot.setCanTakeItems(true);
                slot.setCanPutItems(false);
            });
            WidgetUtils.widgetByIdForEach(group, "^progress_bar$", ProgressWidget.class, bar -> {
                bar.setProgressSupplier(machine::getProgressPercent);
                bar.setDynamicHoverTips(p -> progressHover.translate(
                        String.valueOf(machine.getProgress()),
                        String.valueOf(machine.getMaxProgress())).getString());
            });
            WidgetUtils.widgetByIdForEach(group, "^mat_slot_[0-9]+$", SlotWidget.class, slot -> {
                int index = WidgetUtils.widgetIdIndex(slot);
                if (index >= 0 && index < MATERIAL_SLOTS) {
                    slot.setHandlerSlot(machine.materialInventory.storage, index);
                    slot.setCanTakeItems(true);
                    slot.setCanPutItems(true);
                }
            });
        });
    }
}