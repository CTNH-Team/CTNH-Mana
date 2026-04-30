package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.moguang.ctnhmana.Mutiblock.parts.RedstoneSignalBroadcastHatch;
import com.moguang.ctnhmana.item.ManaFuelStick.IManaFuelStick;
import com.moguang.ctnhmana.item.Rune.RuneElementType;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMTags;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.*;

import static com.moguang.ctnhmana.data.lang.AHCCRuneLang.*;
import static com.moguang.ctnhmana.item.Rune.RuneElementType.*;
import static com.moguang.ctnhmana.registry.CMGuiTextures.AHCC_BACKGROUND;
import static mythicbotany.register.ModItems.*;

public class ArcaneHighEnergyCompressionReactorCore extends WorkableMultiblockMachine implements IFancyUIMachine,
                                                    IDisplayUIMachine, IExplosionMachine, IChannelMachine,
                                                    ICentralStorageMachine {

    @Getter
    @Persisted
    protected final NotifiableItemStackHandler inventory;
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ArcaneHighEnergyCompressionReactorCore.class,
            WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Getter
    @Persisted
    public long maxEU = 10000000000L;
    @Getter
    @Persisted
    public long EU = 0L;
    public long predicateEU = 0L;
    @Persisted
    public int slot_range = 2;
    @Persisted
    public long baseMaxHeat = 1600;
    @Persisted
    public long maxHeat = 1600;
    @Persisted
    public long heat = 0;
    @Persisted
    public long stability = 100;
    @Persisted
    public long used_stability = 0;
    @Persisted
    public long maxStability = 100;
    @Persisted
    public double stabilityPressure = 0;
    @Persisted
    public int tier = 0;
    @Nullable
    protected TickableSubscription tickSubs;
    public EnergyContainerList energyContainer;
    protected ISubscription ManaSubs = null;
    public Map<RuneElementType, Integer> elementMap = new HashMap<>();
    public long[][] heatMap;
    public long[][] stabilityMap;
    public Map<Integer, Integer> channelSignal = new HashMap<>();
    public List<RedstoneSignalBroadcastHatch> hatchList = new ArrayList<>();
    @Persisted
    private boolean shouldChecked = false; // 该参数用于在afterworking后立即检查是否需要进行下一轮运行，如果为否则tick逻辑不会试图检查是否运行
    @Persisted
    public boolean cooldown = false; // 冷却时间
    @Persisted
    public int workingCheckTicks = 10; // onWorking检测一次的时间变量
    @Persisted
    public int cooldownDurationTicks = 200; // 冷却时间
    @Persisted
    public int recipeDurationTicks = 400; // 配方执行时间

    public ArcaneHighEnergyCompressionReactorCore(IMachineBlockEntity holder, int slot_range) {
        super(holder);
        this.slot_range = slot_range;
        inventory = createMachineStorage(slot_range);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        updateEnergyContainer();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTickSubscription));
        }
        hatchList = broadcastSelf();
    }

    @Override
    public void onStructureInvalid() {
        hatchList = null;
        if (this.isActive()) {
            doExplosion(10F);
        }
        super.onStructureInvalid();
    }

    public void updateEnergyContainer() {
        List<IEnergyContainer> containers = new ArrayList<>();
        for (IMultiPart part : getParts())
            part.self().holder.self()
                    .getCapability(GTCapability.CAPABILITY_ENERGY_CONTAINER)
                    .ifPresent(container -> {
                        this.tier = Math.max(tier, GTUtil.getTierByVoltage(container.getOutputVoltage()));
                        containers.add(container);
                    });
        energyContainer = new EnergyContainerList(containers);
    }
    /// ///////////////////////////////
    /// / tick/ ////
    /// //////////////////////////
    ///
    ///

    @Override
    public void onLoad() {
        super.onLoad();
        ManaSubs = inventory.addChangedListener(this::onInventoryChanged);
        elementMap.put(RuneElementType.EARTH, 0);
        elementMap.put(RuneElementType.FIRE, 0);
        elementMap.put(RuneElementType.WATER, 0);
        elementMap.put(RuneElementType.WIND, 0);
        elementMap.put(RuneElementType.SIN, 0);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
        if (ManaSubs != null) {
            ManaSubs.unsubscribe();
            ManaSubs = null;
        }
    }

    private void onInventoryChanged() {
        rebuildAllMaps();
        if (this.isActive()) return;
        for (int i = 0; i <= slot_range * slot_range - 1; i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty() &&
                    this.inventory.getStackInSlot(i).getItem() instanceof IManaFuelStick) {
                startRecipeCycle();
                return;
            }
        }
    }

    protected void updateTickSubscription() {
        if (isFormed) {
            tickSubs = subscribeServerTick(tickSubs, this::tick);
        } else if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    public void tick() {
        if (!isFormed) return;
        if (!isActive() && shouldChecked) {
            onInventoryChanged();
            shouldChecked = false; // 只需要检查一次，非常省性能
        }
        var consume = Math.min(EU, energyContainer.getEnergyCapacity() - energyContainer.getEnergyStored());
        EU -= consume;
        energyContainer.addEnergy(consume);
    }

    /// ///////////////////////////////
    /// / RecipeLogic/ ////
    /// //////////////////////////
    ///
    ///
    @Override
    public boolean onWorking() {
        if (workingCheckTicks > 0 && this.getOffsetTimer() % workingCheckTicks == 0 && !this.cooldown) {
            rebuildAllMaps();
            calculateHeat();
            applyManaCoolantEffect();
            calculateStability();
            changeSignal();
            if (stability < 0) {
                // doExplosion(10f);
            }
            predicateEU = calculateEU();
        }
        return super.onWorking();
    }

    // 强冷：输入魔力冷却剂，降低热量并提高稳定度
    public boolean applyManaCoolantEffect() {
        if (!MachineUtils.inputFluid(CMMaterials.MANA_STABLE_COOLDOWN.getFluid(10000), this)) {
            return false;
        }
        var winter_count = Math.min(getRuneCount(BotaniaItems.runeWinter), 5);
        heat = (long) Math.max(0, heat * Math.min(1, 0.7 + 0.025 * winter_count));
        stability = Math.min(maxStability, stability + Math.round(maxStability * 0.05));
        return true;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        EU += calculateEU();
        EU = Math.min(EU, maxEU);
        double runeBreakChance = getRuneBreakChance(this.stability);
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty() && inventory.getStackInSlot(i).is(BotaniaTags.Items.RUNES)) {
                if (Math.random() <= runeBreakChance) popItem(i);
            }
        }
        this.stability = maxStability;
        changeSignal();
        shouldChecked = true; // 需要检查是否需要立即进入下一轮
        stabilityPressure = 0;
        used_stability = 0;
        reverseCooldown();
    }

    public void startRecipeCycle() {
        if (!isWorkingEnabled() && !this.cooldown) return;
        GTRecipeType recipeType = getRecipeType();
        rebuildAllMaps();// 每次循环开始前重算一次符文状态
        // 在循环开始前分别计算三类时间参数
        workingCheckTicks = calculateWorkingCheckTicks();
        cooldownDurationTicks = calculateCooldownDurationTicks();
        recipeDurationTicks = calculateRecipeDurationTicks();
        int time;
        if (this.cooldown) {
            time = cooldownDurationTicks;
        } else {
            time = recipeDurationTicks;
        }
        GTRecipe emptyRecipe = recipeType
                .recipeBuilder(GTCEu.id("empty_recipe_25s"))
                .duration(time)
                .buildRawRecipe();
        stabilityPressure = 0;
        used_stability = 0;
        this.heat = 0;
        recipeLogic.setupRecipe(emptyRecipe);
        recipeLogic.markLastRecipeDirty();
    }

    public int calculateWorkingCheckTicks() {
        workingCheckTicks = 10;
        return Math.max(1, workingCheckTicks);
    }

    public int calculateCooldownDurationTicks() {
        cooldownDurationTicks = 200;
        int windRuneCount = getRuneCount(BotaniaItems.runeAir);
        int slothRuneCount = getRuneCount(BotaniaItems.runeSloth);
        int waterRuneCount = getRuneCount(BotaniaItems.runeWater);
        int reduction = windRuneCount * 20;
        int increase = slothRuneCount > 0 ? 20 : 0;
        if (slothRuneCount > 0 && waterRuneCount >= 10) {
            increase += 20;
        }
        return Math.max(20, cooldownDurationTicks - reduction + increase);
    }

    public int calculateRecipeDurationTicks() {
        recipeDurationTicks = 400;
        int slothRuneCount = getRuneCount(BotaniaItems.runeSloth);
        int waterRuneCount = getRuneCount(BotaniaItems.runeWater);
        int increase = slothRuneCount > 0 ? 20 : 0;
        if (slothRuneCount > 0 && waterRuneCount >= 10) {
            increase += 20;
        }
        return Math.max(1, recipeDurationTicks + increase);
    }

    public int getRuneCount(Item runeItem) {
        int count = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            var stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem().equals(runeItem)) {
                count++;
            }
        }
        return count;
    }

    public void rebuildAllMaps() {
        // 重建所有图
        maxHeat = baseMaxHeat;
        calculateTotalElementMap();
        long[][] heatmap = new long[slot_range][slot_range];
        long[][] stabilitymap = new long[slot_range][slot_range];
        for (int i = 0; i < slot_range; i++) {
            for (int j = 0; j < slot_range; j++) {
                var stack = inventory.getStackInSlot(getSlotIndex(i, j));
                if (!stack.isEmpty()) {
                    if (stack.getItem() instanceof IManaFuelStick stick) {
                        heatmap[i][j] = stick.heat;
                        stabilitymap[i][j] = stick.stability;
                    } else if (stack.is(BotaniaTags.Items.RUNES)) {
                        heatmap[i][j] = 0;
                        stabilitymap[i][j] = 0;
                    } else {
                        heatmap[i][j] = -1;
                        stabilitymap[i][j] = 0;
                    }
                } else {
                    heatmap[i][j] = -1;
                    stabilitymap[i][j] = 0;
                }
            }
        }
        var maps = calculateRune(heatmap, stabilitymap);
        heatMap = maps.get(0);
        stabilityMap = maps.get(1);
    }

    public void reverseCooldown() {
        this.cooldown = !this.cooldown;
    }

    public void calculateStability() {
        if (this.heat > this.maxHeat * 0.5) stabilityPressure += 0.5;
        if (this.heat > this.maxHeat)
            stabilityPressure += (long) (5 * Math.pow((double) (this.heat / this.maxHeat), 4));
        this.stability = (long) (this.maxStability - this.stabilityPressure - this.used_stability);
    }

    public void calculateHeat() {
        if (heatMap == null || stabilityMap == null ||
                heatMap.length != slot_range || stabilityMap.length != slot_range) {
            rebuildAllMaps();
        }
        long[][] heatmap = heatMap;
        long[][] stabilitymap = stabilityMap;
        used_stability = 0;
        for (int i = 0; i < slot_range; i++) {
            for (int j = 0; j < slot_range; j++) {
                if (heatmap[i][j] > 0) {
                    var item = inventory.getStackInSlot(getSlotIndex(i, j));
                    if (item.getItem() instanceof IManaFuelStick) {
                        var consume = Math.min(item.getMaxDamage() - item.getDamageValue(), heatmap[i][j]);
                        item.setDamageValue(item.getDamageValue() + (int) consume);
                        heatmap[i][j] = consume;
                        if (item.getDamageValue() >= item.getMaxDamage()) {
                            popItem(getSlotIndex(i, j));
                            stabilitymap[i][j] = 0;
                        }
                    }
                }
                heat = (heatmap[i][j] >= 0) ? heat + heatmap[i][j] : heat;
                used_stability += stabilitymap[i][j];
            }
        }
    }

    public void calculateTotalElementMap() {
        for (RuneElementType type : elementMap.keySet()) {
            elementMap.put(type, 0);
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            var stack = inventory.getStackInSlot(i);
            if (stack.isEmpty() || !stack.is(BotaniaTags.Items.RUNES)) continue;
            calculateElementMap(stack);
        }
        // 魔力符文：提供额外1所有元素类型（每个魔力符文各+1）
        int manaRuneCount = getRuneCount(BotaniaItems.runeMana);
        if (manaRuneCount > 0) {
            for (RuneElementType type : elementMap.keySet()) {
                elementMap.put(type, elementMap.get(type) + manaRuneCount);
            }
        }
        // 矮人国度符文：每个额外提供5地元素
        int nidavellirRuneCount = getRuneCount(nidavellirRune);
        if (nidavellirRuneCount > 0) {
            elementMap.put(RuneElementType.EARTH,
                    elementMap.getOrDefault(RuneElementType.EARTH, 0) + nidavellirRuneCount * 5);
        }
    }

    public void calculateElementMap(ItemStack stack) {
        if (stack.is(CMTags.ELEMENT_EARTH)) {
            elementMap.put(RuneElementType.EARTH, elementMap.get(RuneElementType.EARTH) + 1);
        }
        if (stack.is(CMTags.ELEMENT_FIRE)) {
            elementMap.put(RuneElementType.FIRE, elementMap.get(RuneElementType.FIRE) + 1);
        }
        if (stack.is(CMTags.ELEMENT_WATER)) {
            elementMap.put(RuneElementType.WATER, elementMap.get(RuneElementType.WATER) + 1);
        }
        if (stack.is(CMTags.ELEMENT_WIND)) {
            elementMap.put(RuneElementType.WIND, elementMap.get(RuneElementType.WIND) + 1);
        }
        if (stack.is(CMTags.ELEMENT_SIN)) {
            elementMap.put(RuneElementType.SIN, elementMap.get(RuneElementType.SIN) + 1);
        }
    }

    public void popItem(int slot) {
        var item = disintegration(inventory.getStackInSlot(slot));
        if (item.isEmpty()) return;
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
        List<IRecipeHandler<?>> handlers = this.getCapabilitiesFlat(IO.OUT, ItemRecipeCapability.CAP);
        if (handlers.isEmpty()) return;

        for (IRecipeHandler<?> handler : handlers) {
            if (handler instanceof NotifiableItemStackHandler handler1 && !item.isEmpty()) {
                item = CTNHManaUtils.insertItemToOutput(handler1, item, false);
            }
        }
    }

    public ItemStack disintegration(ItemStack stack) {
        if (stack.getItem() instanceof IManaFuelStick stick) {
            var random = Math.random();
            if (random <= 1 - (double) stability / maxStability) return (stick.disintegration != null) ?
                    new ItemStack(stick.disintegration) : new ItemStack(BotaniaBlocks.livingrock.asItem());
            else return stack;
        } else if (stack.is(BotaniaTags.Items.RUNES)) {
            // 符文产物分流：基础20%破碎，稳定度<25%后每下降1%再+2%
            double brokenChance = getRuneBrokenChance(this.stability);
            if (Math.random() <= brokenChance) {
                return new ItemStack(CMItems.BROKEN_RUNE);
            }
            return new ItemStack(CMItems.EMPTY_RUNE);
        }
        return stack;
    }

    public double getRuneBreakChance(long currentStability) {
        if (maxStability <= 0) return 1.0;
        double stabilityRatio = Math.max(0.0, Math.min(1.0, (double) currentStability / maxStability));
        if (stabilityRatio >= 0.5) return 0.0;
        // 初始1%，并且相对50%线每下降1%，额外+2%
        double belowHalfPercent = (0.5 - stabilityRatio) * 100.0;
        return Math.min(1.0, 0.01 + belowHalfPercent * 0.02);
    }

    public double getRuneBrokenChance(long currentStability) {
        if (maxStability <= 0) return 1.0;
        double stabilityRatio = Math.max(0.0, Math.min(1.0, (double) currentStability / maxStability));
        // 基础20%
        double chance = 0.20;
        if (stabilityRatio < 0.25) {
            // 低于25%后，每下降1%，破碎概率+2%
            double belowQuarterPercent = (0.25 - stabilityRatio) * 100.0;
            chance += belowQuarterPercent * 0.02;
        }
        return Math.min(1.0, chance);
    }

    public Long calculateEU() {
        var now_eu = 0L;
        if (this.heat < this.maxHeat * 0.5) {
            now_eu = (long) (Math.pow(heat, 1.5) * GTValues.VA[GTValues.HV]);
        }
        if (this.heat >= this.maxHeat * 0.5 && this.heat <= this.maxHeat) {
            now_eu = (long) (Math.pow(heat, 1.5) * GTValues.VA[GTValues.EV]);
        }
        if (this.heat > this.maxHeat) {
            now_eu = (long) (Math.pow(this.maxHeat, 1.5) * GTValues.VA[GTValues.EV] *
                    (1 + (double) (this.heat - this.maxHeat) / this.maxHeat) + Math.pow(this.heat - this.maxHeat, 3));
        }
        now_eu = Math.min(now_eu, maxEU);
        return now_eu;
    }

    /// ///////////////////////////////
    /// / Slots/ ////
    /// //////////////////////////
    protected NotifiableItemStackHandler createMachineStorage(int range) {
        return new NotifiableItemStackHandler(
                this, range * range, IO.NONE, IO.IN, slots -> new CustomItemStackHandler(range * range) {

                    @Override
                    public int getSlotLimit(int slot) {
                        return 1;
                    }

                    @Override
                    public void onContentsChanged(int slot) {
                        super.onContentsChanged(slot);
                    }

                    @Override
                    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                        if (stack.is(BotaniaTags.Items.RUNES) || stack.getItem() instanceof IManaFuelStick) return true;
                        return false;
                    }
                });
    }

    public int getSlotIndex(int y, int x) {
        if (y * slot_range + x <= slot_range * slot_range)
            return y * slot_range + x;
        return -1;
    }

    public int getSlotLocation(int index) {
        if (index < slot_range * slot_range - 1) return 1;
        return -1;
    }

    public long getPreviewHeatBySlot(int slot) {
        if (slot < 0 || slot >= slot_range * slot_range || heatMap == null) return 0;
        int row = slot / slot_range;
        int col = slot % slot_range;
        if (!isValidLocation(row, col)) return 0;
        return heatMap[row][col];
    }

    public long getPreviewStabilityBySlot(int slot) {
        if (slot < 0 || slot >= slot_range * slot_range || stabilityMap == null) return 0;
        int row = slot / slot_range;
        int col = slot % slot_range;
        if (!isValidLocation(row, col)) return 0;
        return stabilityMap[row][col];
    }

    /// ///////////////////////////////
    /// / UI/ ////
    /// //////////////////////////
    @Override
    public void addDisplayText(List<Component> textList) {
        if (isFormed) {
            textList.add(AHCCstatusLang[0].translate(EU, maxEU));
            if (this.cooldown) {
                textList.add(Component.literal("维度稳定模式"));
            } else {
                textList.add(AHCCstatusLang[1].translate(heat, maxHeat));
                textList.add(AHCCstatusLang[2].translate(stability, maxStability));
            }
        }
        if (this.isActive() && !this.cooldown) {
            var voltageName = GTValues.VNF[this.tier];
            textList.add(AHCCstatusLang[3].translate(predicateEU, (double) (predicateEU / GTValues.V[this.tier]),
                    voltageName));
        }
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        int extra = Math.max(0, slot_range - 3) * 18;
        int uiWidth = 198 + extra;
        int uiHeight = 208 + extra;
        return new ModularUI(uiWidth, uiHeight, this, entityPlayer)
                .widget(new FancyMachineUIWidget(this, uiWidth, uiHeight));
    }

    @Override
    public Widget createUIWidget() {
        int extra = Math.max(0, slot_range - 3) * 18;
        int panelWidth = 202 + extra;
        int panelHeight = 190 + extra;
        var group = new WidgetGroup(0, 0, panelWidth + 8, panelHeight + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, panelWidth, panelHeight).setBackground(AHCC_BACKGROUND)
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                        .textSupplier(this.getLevel().isClientSide ? null : this::addDisplayText)
                        .setMaxWidthLimit(200)
                        .clickHandler(this::handleDisplayClick)));
        var size = group.getSize();
        int gridTotalSize = slot_range * 18;
        int startX = (size.width - gridTotalSize) / 2;
        int startY = (size.height - gridTotalSize) / 2;

        int totalSlots = slot_range * slot_range;
        for (int i = 0; i < totalSlots; i++) {
            int row = i / slot_range;
            int col = i % slot_range;
            int slotX = startX + col * 18 - 2;
            int slotY = startY + row * 18 - 2;
            group.addWidget(
                    new AHCCFuelPreviewSlotWidget(inventory.storage, i, slotX, slotY, true, true)
                            .setBackground(GuiTextures.SLOT));
        }
        return group;
    }

    @CN({
            "存储的EU:%d/%d",
            "积累的热量:%d/%d",
            "稳定度:%d/%d",
            "预计发电量:%d EU (%.2fA %s)",
            "当前状态：§b维度稳定模式",
            "当前状态：维度压缩模式"
    })
    @EN({
            "存储的EU:%d",
            "积累的热量:%d/%d",
            "稳定度:%d/%d",
            "预计发电量:%d EU (%.2fA %s) ",
            "当前状态：§b维度稳定模式",
            "当前状态：维度压缩模式"
    })
    public static Lang[] AHCCstatusLang;
    @CN({
            "提供热量:%d",
            "稳定度占用:%d"
    })
    @EN({
            "提供热量:%d",
            "稳定度占用:%d"
    })
    public static Lang[] AHCCBarLang;

    public class AHCCFuelPreviewSlotWidget extends SlotWidget {

        public AHCCFuelPreviewSlotWidget() {
            super();
        }

        public AHCCFuelPreviewSlotWidget(CustomItemStackHandler itemHandler, int slotIndex, int xPosition,
                                         int yPosition, boolean canTakeItems, boolean canPutItems) {
            super(itemHandler, slotIndex, xPosition, yPosition, canTakeItems, canPutItems);
        }

        @Override
        public List<Component> getTooltipTexts() {
            List<Component> tooltips = super.getTooltipTexts();
            if (slotReference == null) return tooltips;
            var stack = slotReference.getItem();
            if (stack.getItem() instanceof IManaFuelStick stick) {
                int slot = slotReference.getSlotIndex();
                long previewHeat = getPreviewHeatBySlot(slot);
                long previewStability = getPreviewStabilityBySlot(slot);
                // 地图尚未建立时回退到燃料棒原始值，避免显示0造成误导。
                if (heatMap == null) previewHeat = stick.heat;
                if (stabilityMap == null) previewStability = stick.stability;

                tooltips.add(AHCCBarLang[0].translate(previewHeat));
                tooltips.add(AHCCBarLang[1].translate(previewStability));
                return tooltips;
            }

            if (stack.is(BotaniaTags.Items.RUNES)) {
                Lang[] runeLang = getAHCCRuneLang(stack);
                if (runeLang != null) {
                    for (Lang lang : runeLang) {
                        tooltips.add(lang.translate());
                    }
                }
            }
            return tooltips;
        }
    }

    @Nullable
    private Lang[] getAHCCRuneLang(ItemStack stack) {
        if (stack.getItem().equals(BotaniaItems.runeWater)) return ahccRuneWater;
        if (stack.getItem().equals(BotaniaItems.runeFire)) return ahccRuneFire;
        if (stack.getItem().equals(BotaniaItems.runeEarth)) return ahccRuneEarth;
        if (stack.getItem().equals(BotaniaItems.runeAir)) return ahccRuneAir;
        if (stack.getItem().equals(BotaniaItems.runeSpring)) return ahccRuneSpring;
        if (stack.getItem().equals(BotaniaItems.runeSummer)) return ahccRuneSummer;
        if (stack.getItem().equals(BotaniaItems.runeAutumn)) return ahccRuneAutumn;
        if (stack.getItem().equals(BotaniaItems.runeWinter)) return ahccRuneWinter;
        if (stack.getItem().equals(BotaniaItems.runeMana)) return ahccRuneMana;

        if (stack.getItem().equals(BotaniaItems.runeLust)) return ahccRuneLust;
        if (stack.getItem().equals(BotaniaItems.runeGluttony)) return ahccRuneGluttony;
        if (stack.getItem().equals(BotaniaItems.runeGreed)) return ahccRuneGreed;
        if (stack.getItem().equals(BotaniaItems.runeSloth)) return ahccRuneSloth;
        if (stack.getItem().equals(BotaniaItems.runeWrath)) return ahccRuneWrath;
        if (stack.getItem().equals(BotaniaItems.runeEnvy)) return ahccRuneEnvy;
        if (stack.getItem().equals(BotaniaItems.runePride)) return ahccRunePride;

        if (stack.getItem().equals(asgardRune)) return ahccRuneAsgard;
        if (stack.getItem().equals(vanaheimRune)) return ahccRuneVanaheim;
        if (stack.getItem().equals(alfheimRune)) return ahccRuneAlfheim;
        if (stack.getItem().equals(midgardRune)) return ahccRuneMidgard;
        if (stack.getItem().equals(joetunheimRune)) return ahccRuneJotunheim;
        if (stack.getItem().equals(muspelheimRune)) return ahccRuneMuspelheim;
        if (stack.getItem().equals(niflheimRune)) return ahccRuneNiflheim;
        if (stack.getItem().equals(nidavellirRune)) return ahccRuneNidavellir;
        if (stack.getItem().equals(helheimRune)) return ahccRuneHelheim;
        return null;
    }

    public boolean isValidLocation(int i, int j) {
        if (i >= 0 && j >= 0 && i < slot_range && j < slot_range) return true;
        return false;
    }

    /// ///////////////////////////////
    /// / Redstone/ ////
    /// //////////////////////////
    ///
    ///
    public int getChannelSignal(int channel) {
        return channelSignal.getOrDefault(channel, 0);
    }

    public List<RedstoneSignalBroadcastHatch> broadcastSelf() {
        List<RedstoneSignalBroadcastHatch> hatches = new ArrayList<>();
        for (IMultiPart part : this.getParts()) {
            if (part instanceof RedstoneSignalBroadcastHatch hatch) {
                hatch.setRedstoneSignalOutput((getChannelSignal(hatch.channel)));
                hatches.add(hatch);
            }
        }
        return hatches;
    }

    public void changeSignal() {
        if (this.heat <= 0) channelSignal.put(0, 0);
        if (this.heat < this.maxHeat * 0.5 && this.heat > 0) channelSignal.put(0, 2);
        if (this.heat >= this.maxHeat * 0.5 && this.heat < this.maxHeat) channelSignal.put(0, 5);
        if (this.heat >= maxHeat) channelSignal.put(0, 10);
        if (stability > 0.1 * maxStability)
            channelSignal.put(1, (int) (this.maxStability / Math.max(this.stability, 1)) - 1);
        else channelSignal.put(1, 12);

        if (!hatchList.isEmpty()) {
            for (RedstoneSignalBroadcastHatch hatch : hatchList) {
                hatch.setRedstoneSignalOutput(getChannelSignal(hatch.channel));
            }
        }
    }

    /// ///////////////////////////////
    /// / Rune/ ////
    /// //////////////////////////
    ///
    ///
    // 符文运算
    public List<long[][]> calculateRune(long[][] heatmap, long[][] stabilitymap) {
        int winterApplied = 0;
        boolean muspelheimBonusTriggered = false;
        boolean nidavellirTerminalTriggered = false;
        for (int i = 0; i < slot_range; i++) {
            for (int j = 0; j < slot_range; j++) {
                if (heatmap[i][j] == 0) {
                    var stack = inventory.getStackInSlot(getSlotIndex(i, j));
                    if (stack.getItem().equals(BotaniaItems.runeWater)) {
                        // 水符文：相邻燃料棒热量-1，且相邻燃料棒占用-2稳定度
                        if (isValidLocation(i - 1, j) && heatmap[i - 1][j] > 0) {
                            heatmap[i - 1][j] -= 1;
                            stabilitymap[i - 1][j] -= 2;
                        }
                        if (isValidLocation(i + 1, j) && heatmap[i + 1][j] > 0) {
                            heatmap[i + 1][j] -= 1;
                            stabilitymap[i + 1][j] -= 2;
                        }
                        if (isValidLocation(i, j - 1) && heatmap[i][j - 1] > 0) {
                            heatmap[i][j - 1] -= 1;
                            stabilitymap[i][j - 1] -= 2;
                        }
                        if (isValidLocation(i, j + 1) && heatmap[i][j + 1] > 0) {
                            heatmap[i][j + 1] -= 1;
                            stabilitymap[i][j + 1] -= 2;
                        }
                    }
                    if (stack.getItem().equals(BotaniaItems.runeFire)) {
                        // 火符文：相邻燃料棒热量x1.2，符文自身占用1稳定度
                        if (isValidLocation(i - 1, j) && heatmap[i - 1][j] > 0) heatmap[i - 1][j] *= 1.2;
                        if (isValidLocation(i + 1, j) && heatmap[i + 1][j] > 0) heatmap[i + 1][j] *= 1.2;
                        if (isValidLocation(i, j - 1) && heatmap[i][j - 1] > 0) heatmap[i][j - 1] *= 1.2;
                        if (isValidLocation(i, j + 1) && heatmap[i][j + 1] > 0) heatmap[i][j + 1] *= 1.2;
                        stabilitymap[i][j] += 1;
                    }
                    if (stack.getItem().equals(BotaniaItems.runeEarth)) {
                        // 地符文：热量上限+2x地元素总数，符文自身占用1稳定度
                        int earthElements = elementMap.getOrDefault(RuneElementType.EARTH, 0);
                        maxHeat += 5L * earthElements;
                        stabilitymap[i][j] += 1;
                    }
                    if (stack.getItem().equals(BotaniaItems.runeAir)) {
                        // 风符文：符文自身占用1稳定度；冷却减时效果在循环开始时统一结算
                        stabilitymap[i][j] += 1;
                    }
                    if (stack.getItem().equals(BotaniaItems.runeSpring)) {
                        // 春符文：稳定度>50%时，占用2稳定度并使所有燃料棒热量+1；
                        // 稳定度<=50%时，改为提供1稳定度
                        if (this.stability > this.maxStability * 0.5) {
                            stabilitymap[i][j] += 2;
                            for (int y = 0; y < slot_range; y++) {
                                for (int x = 0; x < slot_range; x++) {
                                    if (heatmap[y][x] > 0) {
                                        heatmap[y][x] += 1;
                                    }
                                }
                            }
                        } else {
                            stabilitymap[i][j] -= 1;
                        }
                    }
                    if (stack.getItem().equals(BotaniaItems.runeSummer)) {
                        // 夏符文：每有1火元素，相邻燃料棒热量+5%；符文自身占用1稳定度,额外获得等同于火元素数量的稳定度占用
                        int fireElements = elementMap.getOrDefault(RuneElementType.FIRE, 0);
                        double multiplier = 1.0 + fireElements * 0.05;
                        if (isValidLocation(i - 1, j) && heatmap[i - 1][j] > 0) heatmap[i - 1][j] *= multiplier;
                        if (isValidLocation(i + 1, j) && heatmap[i + 1][j] > 0) heatmap[i + 1][j] *= multiplier;
                        if (isValidLocation(i, j - 1) && heatmap[i][j - 1] > 0) heatmap[i][j - 1] *= multiplier;
                        if (isValidLocation(i, j + 1) && heatmap[i][j + 1] > 0) heatmap[i][j + 1] *= multiplier;
                        stabilitymap[i][j] += 1;
                        //
                        stabilitymap[i][j] += fireElements;
                    }
                    if (stack.getItem().equals(BotaniaItems.runeAutumn)) {
                        // 秋符文：自身占用2稳定度；每有1地元素，自身每相邻一个燃料棒，热量上限+2
                        int earthElements = elementMap.getOrDefault(RuneElementType.EARTH, 0);
                        int adjacentFuelCount = 0;
                        if (isValidLocation(i - 1, j) && heatmap[i - 1][j] > 0) adjacentFuelCount++;
                        if (isValidLocation(i + 1, j) && heatmap[i + 1][j] > 0) adjacentFuelCount++;
                        if (isValidLocation(i, j - 1) && heatmap[i][j - 1] > 0) adjacentFuelCount++;
                        if (isValidLocation(i, j + 1) && heatmap[i][j + 1] > 0) adjacentFuelCount++;
                        maxHeat += (long) earthElements * adjacentFuelCount;
                        stabilitymap[i][j] += 2;
                    }
                    if (stack.getItem().equals(BotaniaItems.runeWinter)) {
                        //
                    }
                    if (stack.getItem().equals(BotaniaItems.runeMana)) {
                        // 魔力符文：自身占用2稳定度
                        stabilitymap[i][j] += 2;
                    }
                    if (stack.getItem().equals(BotaniaItems.runeLust)) {
                        // 色欲符文：占用等同于罪孽元素数量的稳定度；
                        // 所有燃料棒热量+10%；自身周围1格半径（含对角）燃料热量-25%
                        int sinElements = elementMap.getOrDefault(RuneElementType.SIN, 0);
                        stabilitymap[i][j] += sinElements;
                        for (int y = 0; y < slot_range; y++) {
                            for (int x = 0; x < slot_range; x++) {
                                if (heatmap[y][x] > 0) {
                                    heatmap[y][x] *= 1.1;
                                }
                            }
                        }
                        for (int dy = -1; dy <= 1; dy++) {
                            for (int dx = -1; dx <= 1; dx++) {
                                if (dy == 0 && dx == 0) continue;
                                int ny = i + dy;
                                int nx = j + dx;
                                if (isValidLocation(ny, nx) && heatmap[ny][nx] > 0) {
                                    heatmap[ny][nx] *= 0.75;
                                }
                            }
                        }
                    }
                    if (stack.getItem().equals(BotaniaItems.runeGluttony)) {
                        // 暴食符文：提供 地元素*罪孽元素*3 的热量上限，
                        // 并占用等同于罪孽元素数量的稳定度
                        int earthElements = elementMap.getOrDefault(RuneElementType.EARTH, 0);
                        int sinElements = elementMap.getOrDefault(RuneElementType.SIN, 0);
                        maxHeat += (long) earthElements * sinElements * 2;
                        stabilitymap[i][j] += sinElements;
                    }
                    if (stack.getItem().equals(BotaniaItems.runeGreed)) {
                        // 贪婪符文：自身占用等同于罪孽元素数量；
                        // 使左侧相邻燃料棒占用稳定度x2；
                        // 若左侧燃料棒带罪孽元素则热量x2，否则热量x1.5
                        int sinElements = elementMap.getOrDefault(RuneElementType.SIN, 0);
                        stabilitymap[i][j] += sinElements;
                        if (isValidLocation(i, j - 1) && heatmap[i][j - 1] > 0) {
                            var leftFuelStack = inventory.getStackInSlot(getSlotIndex(i, j - 1));
                            stabilitymap[i][j - 1] *= 2;
                            heatmap[i][j - 1] *= leftFuelStack.is(CMTags.ELEMENT_SIN) ? 2.0 : 1.5;
                        }
                    }
                    if (stack.getItem().equals(BotaniaItems.runeSloth)) {
                        // 懒惰符文：自身占用改为每5点罪孽元素+2稳定度占用
                        int sinElements = elementMap.getOrDefault(RuneElementType.SIN, 0);
                        stabilitymap[i][j] += (sinElements / 5) * 2;
                    }
                    if (stack.getItem().equals(BotaniaItems.runeWrath)) {
                        // 愤怒符文：自身占用等同于罪孽元素数量的稳定度；
                        // 当稳定度<50%时，(罪孽元素+火元素)每有1，
                        // 对角燃料棒热量+5%，并增加1稳定占用
                        int sinElements = elementMap.getOrDefault(RuneElementType.SIN, 0);
                        int fireElements = elementMap.getOrDefault(RuneElementType.FIRE, 0);
                        int wrathPower = sinElements + fireElements;
                        stabilitymap[i][j] += sinElements;
                        if (this.stability < this.maxStability * 0.5 && wrathPower > 0) {
                            double multiplier = 1.0 + wrathPower * 0.05;
                            int[][] diagonalOffsets = new int[][] { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
                            for (int[] offset : diagonalOffsets) {
                                int ny = i + offset[0];
                                int nx = j + offset[1];
                                if (isValidLocation(ny, nx) && heatmap[ny][nx] > 0) {
                                    heatmap[ny][nx] *= multiplier;
                                    stabilitymap[ny][nx] += wrathPower;
                                }
                            }
                        }
                    }
                    if (stack.getItem().equals(BotaniaItems.runeEnvy)) {
                        // 嫉妒符文：占用等同于罪孽元素数量的稳定度；
                        // 每有1罪孽元素，使正四格燃料棒产热+10%；
                        // 若存在其他嫉妒符文，或存在产热高于正四格目标的槽位，则该效果不生效
                        int sinElements = elementMap.getOrDefault(RuneElementType.SIN, 0);
                        stabilitymap[i][j] += sinElements;
                        if (sinElements <= 0 || getRuneCount(BotaniaItems.runeEnvy) > 1) {
                            continue;
                        }

                        int[][] cardinalOffsets = new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
                        List<int[]> targets = new ArrayList<>();
                        long targetMaxHeat = Long.MIN_VALUE;
                        for (int[] offset : cardinalOffsets) {
                            int ny = i + offset[0];
                            int nx = j + offset[1];
                            if (isValidLocation(ny, nx) && heatmap[ny][nx] > 0) {
                                targets.add(new int[] { ny, nx });
                                targetMaxHeat = Math.max(targetMaxHeat, heatmap[ny][nx]);
                            }
                        }
                        if (targets.isEmpty()) {
                            continue;
                        }

                        boolean hasHigherHeatElsewhere = false;
                        for (int y = 0; y < slot_range; y++) {
                            for (int x = 0; x < slot_range; x++) {
                                if (y == i && x == j) continue;
                                boolean isTarget = false;
                                for (int[] target : targets) {
                                    if (target[0] == y && target[1] == x) {
                                        isTarget = true;
                                        break;
                                    }
                                }
                                if (isTarget) continue;
                                if (heatmap[y][x] > targetMaxHeat) {
                                    hasHigherHeatElsewhere = true;
                                    break;
                                }
                            }
                            if (hasHigherHeatElsewhere) break;
                        }
                        if (hasHigherHeatElsewhere) {
                            continue;
                        }

                        double multiplier = 1.0 + sinElements * 0.1;
                        for (int[] target : targets) {
                            heatmap[target[0]][target[1]] *= multiplier;
                        }
                    }
                    if (stack.getItem().equals(BotaniaItems.runePride)) {
                        // 傲慢符文：自身占用等同于罪孽元素数量的稳定度；
                        // 使正四格燃料棒产热增加100%，每有一个符文（含自身）该值减少20%
                        int sinElements = elementMap.getOrDefault(RuneElementType.SIN, 0);
                        stabilitymap[i][j] += sinElements;

                        int runeCount = 0;
                        for (int slotIndex = 0; slotIndex < inventory.getSize(); slotIndex++) {
                            var runeStack = inventory.getStackInSlot(slotIndex);
                            if (!runeStack.isEmpty() && runeStack.is(BotaniaTags.Items.RUNES)) {
                                runeCount++;
                            }
                        }

                        // 基础+100%（x2.0），每个符文-20%（倍率-0.2），最低0.1倍率
                        double multiplier = Math.max(0.1, 2.0 - runeCount * 0.2);
                        if (isValidLocation(i - 1, j) && heatmap[i - 1][j] > 0) heatmap[i - 1][j] *= multiplier;
                        if (isValidLocation(i + 1, j) && heatmap[i + 1][j] > 0) heatmap[i + 1][j] *= multiplier;
                        if (isValidLocation(i, j - 1) && heatmap[i][j - 1] > 0) heatmap[i][j - 1] *= multiplier;
                        if (isValidLocation(i, j + 1) && heatmap[i][j + 1] > 0) heatmap[i][j + 1] *= multiplier;
                    }
                    if (stack.getItem().equals(asgardRune)) {
                        // 阿斯加德符文（神域）：预留效果（暂未实现）
                    }
                    if (stack.getItem().equals(vanaheimRune)) {
                        // 华纳海姆符文（华纳神族/丰饶）：
                        // 增加 水元素*地元素*2 的热量上限
                        // 当(水元素+地元素)达到阈值时，额外乘算：
                        // 20+ *2
                        int waterElements = elementMap.getOrDefault(RuneElementType.WATER, 0);
                        int earthElements = elementMap.getOrDefault(RuneElementType.EARTH, 0);
                        int waterEarthSum = waterElements + earthElements;
                        long bonusMaxHeat = (long) waterElements * earthElements * 2;
                        if (waterEarthSum >= 20) {
                            bonusMaxHeat *= 2;
                        }
                        maxHeat += bonusMaxHeat;
                        stabilitymap[i][j] += 2;
                    }
                    if (stack.getItem().equals(alfheimRune)) {
                        // 亚尔夫海姆符文（精灵之国）：预留效果（暂未实现）
                    }
                    if (stack.getItem().equals(midgardRune)) {
                        // 米德加德符文（人类世界/中庭）：预留效果（暂未实现）
                    }
                    if (stack.getItem().equals(joetunheimRune)) {
                        // 约顿海姆符文（巨人国度）：预留效果（暂未实现）
                    }
                    if (stack.getItem().equals(muspelheimRune)) {
                        // 穆斯贝尔海姆符文（火焰国度）：
                        // 1) 自身长十字范围（整行整列）所有燃料棒产热+10%
                        // 2) 若火元素占比最多：
                        // - 所有带火元素的燃料棒产热+10%
                        // - 自身长十字范围燃料棒获得额外档位加成（取最高档）：
                        // 10+火元素:+10%，20+火元素:+30%，25+火元素:+50%
                        // 3) 触发占比效果时自身占用5稳定度，否则占用2稳定度
                        // 4) 额外档位（福袋）本轮最多触发一次
                        int fireElements = elementMap.getOrDefault(RuneElementType.FIRE, 0);
                        int earthElements = elementMap.getOrDefault(RuneElementType.EARTH, 0);
                        int waterElements = elementMap.getOrDefault(RuneElementType.WATER, 0);
                        int windElements = elementMap.getOrDefault(RuneElementType.WIND, 0);
                        int sinElements = elementMap.getOrDefault(RuneElementType.SIN, 0);

                        int totalElements = fireElements + earthElements + waterElements + windElements + sinElements;
                        boolean fireDominant = totalElements > 0 &&
                                fireElements > earthElements &&
                                fireElements > waterElements &&
                                fireElements > windElements &&
                                fireElements > sinElements;

                        stabilitymap[i][j] += fireDominant ? 10 : 2;

                        // 长十字：整列
                        for (int y = 0; y < slot_range; y++) {
                            if (y != i && heatmap[y][j] > 0) {
                                heatmap[y][j] *= 1.1;
                            }
                        }
                        // 长十字：整行
                        for (int x = 0; x < slot_range; x++) {
                            if (x != j && heatmap[i][x] > 0) {
                                heatmap[i][x] *= 1.1;
                            }
                        }

                        // 自身会使所有火元素燃料额外占用1稳定度
                        for (int y = 0; y < slot_range; y++) {
                            for (int x = 0; x < slot_range; x++) {
                                var fuelStack = inventory.getStackInSlot(getSlotIndex(y, x));
                                if (heatmap[y][x] > 0 && fuelStack.is(CMTags.ELEMENT_FIRE)) {
                                    stabilitymap[y][x] += 1;
                                }
                            }
                        }

                        if (fireDominant) {
                            for (int y = 0; y < slot_range; y++) {
                                for (int x = 0; x < slot_range; x++) {
                                    var fuelStack = inventory.getStackInSlot(getSlotIndex(y, x));
                                    if (heatmap[y][x] > 0 && fuelStack.is(CMTags.ELEMENT_FIRE)) {
                                        heatmap[y][x] *= 1.1;
                                    }
                                }
                            }

                            double extraMultiplier = 1.0;
                            if (fireElements >= 25) {
                                extraMultiplier = 1.5;
                            } else if (fireElements >= 20) {
                                extraMultiplier = 1.3;
                            } else if (fireElements >= 10) {
                                extraMultiplier = 1.1;
                            }

                            if (extraMultiplier > 1.0 && !muspelheimBonusTriggered) {
                                // 长十字额外档位（福袋）本轮仅触发一次
                                for (int y = 0; y < slot_range; y++) {
                                    if (y != i && heatmap[y][j] > 0) {
                                        heatmap[y][j] *= extraMultiplier;
                                    }
                                }
                                for (int x = 0; x < slot_range; x++) {
                                    if (x != j && heatmap[i][x] > 0) {
                                        heatmap[i][x] *= extraMultiplier;
                                    }
                                }
                                muspelheimBonusTriggered = true;
                            }
                        }
                    }
                    if (stack.getItem().equals(niflheimRune)) {
                        // 尼福尔海姆符文（雾之国/寒雾与冰）：预留效果（暂未实现）
                    }
                    if (stack.getItem().equals(nidavellirRune)) {
                        // 尼达维勒符文（矮人国度/锻造）：
                        // 自身额外提供5地元素（已在元素统计阶段结算）
                        // 若地元素最多，且当前热量<上限50%，触发一次地系终端：
                        // 全部燃料棒获得产热与占用修正，档位由(水+地符文数量)决定
                        int selfStabilityCost = 2;
                        int earthElements = elementMap.getOrDefault(RuneElementType.EARTH, 0);
                        int fireElements = elementMap.getOrDefault(RuneElementType.FIRE, 0);
                        int waterElements = elementMap.getOrDefault(RuneElementType.WATER, 0);
                        int windElements = elementMap.getOrDefault(RuneElementType.WIND, 0);
                        int sinElements = elementMap.getOrDefault(RuneElementType.SIN, 0);
                        int totalElements = earthElements + fireElements + waterElements + windElements + sinElements;
                        boolean earthDominant = totalElements > 0 &&
                                earthElements > fireElements &&
                                earthElements > waterElements &&
                                earthElements > windElements &&
                                earthElements > sinElements;

                        if (!nidavellirTerminalTriggered && earthDominant && this.heat < this.maxHeat * 0.5) {
                            int earthWaterRuneSum = getRuneCount(BotaniaItems.runeEarth) +
                                    getRuneCount(BotaniaItems.runeWater);
                            double heatMultiplier = 1.1;
                            int stabilityReduction = 1;
                            if (earthWaterRuneSum >= 75) {
                                heatMultiplier = 1.5;
                                stabilityReduction = 2;
                            } else if (earthWaterRuneSum >= 50) {
                                heatMultiplier = 1.3;
                            } else if (earthWaterRuneSum >= 20) {
                                heatMultiplier = 1.2;
                            }

                            for (int y = 0; y < slot_range; y++) {
                                for (int x = 0; x < slot_range; x++) {
                                    var fuelStack = inventory.getStackInSlot(getSlotIndex(y, x));
                                    if (heatmap[y][x] > 0 && fuelStack.getItem() instanceof IManaFuelStick) {
                                        heatmap[y][x] *= heatMultiplier;
                                        stabilitymap[y][x] -= stabilityReduction;
                                    }
                                }
                            }
                            nidavellirTerminalTriggered = true;
                            selfStabilityCost = 5;
                        }
                        stabilitymap[i][j] += selfStabilityCost;
                    }
                    if (stack.getItem().equals(helheimRune)) {
                        // 赫尔海姆符文（冥界/死亡）：预留效果（暂未实现）
                    }
                } else {
                    continue;
                }
            }
        }
        return List.of(heatmap, stabilitymap);
    }
}
