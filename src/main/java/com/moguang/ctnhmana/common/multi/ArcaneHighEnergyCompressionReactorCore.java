package com.moguang.ctnhmana.common.multi;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
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
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.utils.DummyMachineBlockEntity;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.InfiniteEnergyContainer;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.common.parts.RedstoneSignalBroadcastHatch;
import com.moguang.ctnhmana.common.item.manafuelstick.IManaFuelStick;
import com.moguang.ctnhmana.common.item.rune.RuneElementType;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import com.moguang.ctnhmana.registry.CMTags;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.*;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.moguang.ctnhmana.data.lang.AHCCRuneLang.*;
import static com.moguang.ctnhmana.registry.CMGuiTextures.AHCC_BACKGROUND;
import static com.moguang.ctnhmana.registry.CMMaterials.Livingrock;
import static mythicbotany.register.ModItems.*;

public class ArcaneHighEnergyCompressionReactorCore extends WorkableMultiblockMachine implements IFancyUIMachine,
                                                    IDisplayUIMachine, IExplosionMachine, IChannelMachine,
                                                    ICentralStorageMachine {

    @Getter
    @Persisted
    protected final NotifiableItemStackHandler inventory;
    @Getter
    @Persisted
    public long maxEU = Integer.MAX_VALUE;
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
    public long stability = 150;
    @Persisted
    public long used_stability = 0;
    @Persisted
    public long maxStability = 150;
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
            if ((!this.inventory.getStackInSlot(i).isEmpty() &&
                    this.inventory.getStackInSlot(i).getItem() instanceof IManaFuelStick) || this.cooldown) {
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
                explosion();
                // doExplosion(10f);
            }
            predicateEU = calculateEU();
        }
        return super.onWorking();
    }

    public void explosion() {
        this.EU = 0;
        this.heat = 0;
    }

    // 强冷：输入魔力冷却剂，降低热量并提高稳定度
    public boolean applyManaCoolantEffect() {
        if (!MachineUtils.inputFluid(CMMaterials.MANA_STABLE_COOLDOWN.getFluid(10000), this)) {
            return false;
        }
        var winter_count = Math.min(getRuneCount(BotaniaItems.runeWinter), 5);
        heat = (long) Math.max(0, heat * Math.min(1, 0.7 + 0.025 * winter_count));
        used_stability = Math.max(0, used_stability - 10 - 2 * winter_count);
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
        resetSignal();
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

    // 返回原始的无stack物品
    @NotNull
    private static ItemStack normalizeStackForTwistCollapseMatch(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        Item item = stack.getItem();
        ItemStack out = new ItemStack(item, 1);
        return out;
    }

    /**
     * Test-only helper: try executing TwistCollapse once immediately.
     * This method does not alter current AHCC disintegration behavior.
     */
    public ItemStack testTryTwistCollapseRecipeOnce(ItemStack runeInput) {
        ItemStack crash = new ItemStack(ChemicalHelper.get(dust, Livingrock).getItemHolder(), 1);
        if (runeInput.isEmpty()) return crash;

        ItemStack matchInput = normalizeStackForTwistCollapseMatch(runeInput);
        if (matchInput.isEmpty()) return crash;

        DummyMachineBlockEntity be = new DummyMachineBlockEntity(
                GTValues.LV, CMRecipeTypes.TwistCollapse, GTMachineUtils.defaultTankSizeFunction,
                Collections.emptyList());
        var inputHandler = new NotifiableItemStackHandler(be.getMetaMachine(), 1, IO.IN, IO.IN,
                slots -> new CustomItemStackHandler(matchInput));
        var outputHandler = new NotifiableItemStackHandler(be.getMetaMachine(), 2, IO.OUT);
        RecipeHandlerList dummyInputs = RecipeHandlerList.of(IO.IN,
                new InfiniteEnergyContainer(be.getMetaMachine(), GTValues.V[GTValues.LV], GTValues.V[GTValues.LV], 1,
                        GTValues.V[GTValues.LV], 1),
                inputHandler);
        RecipeHandlerList dummyOutputs = RecipeHandlerList.of(IO.OUT, outputHandler);
        be.getMetaMachine().reinitializeHandlers(List.of(dummyInputs, dummyOutputs));

        Iterator<GTRecipe> recipes = CMRecipeTypes.TwistCollapse.searchRecipe(be.metaMachine,
                recipe -> RecipeHelper.matchContents(be.metaMachine, recipe).isSuccess());
        if (!recipes.hasNext()) return crash;

        GTRecipe recipe = recipes.next();
        if (!RecipeHelper
                .handleRecipeIO(be.metaMachine, recipe, IO.IN, be.getMetaMachine().recipeLogic.getChanceCaches())
                .isSuccess()) {
            return crash;
        }

        for (Content output : recipe.getOutputContents(ItemRecipeCapability.CAP)) {
            ItemStack[] outputs = ItemRecipeCapability.CAP.of(output.content).getItems();
            if (outputs.length > 0 && !outputs[0].isEmpty()) {
                return outputs[0].copy();
            }
        }
        return crash;
    }

    public ItemStack disintegration(ItemStack stack) {
        if (stack.getItem() instanceof IManaFuelStick stick) {
            var random = Math.random();
            if (random <= 1 - (double) stability / maxStability) return (stick.disintegration != null) ?
                    testTryTwistCollapseRecipeOnce(stack) : new ItemStack(BotaniaBlocks.livingrock.asItem());
            else return stack;
        } else if (stack.is(BotaniaTags.Items.RUNES)) {
            // 符文产物分流：基础20%破碎，稳定度<25%后每下降1%再+2%
            double brokenChance = getRuneBrokenChance(this.stability);
            if (Math.random() <= brokenChance) {
                return testTryTwistCollapseRecipeOnce(stack);
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
            now_eu = (long) (Math.pow(heat, 2) * GTValues.VA[GTValues.HV]);
        }
        if (this.heat >= this.maxHeat * 0.5 && this.heat <= this.maxHeat) {
            now_eu = (long) (Math.pow(heat, 2) * GTValues.VA[GTValues.EV]);
        }
        if (this.heat > this.maxHeat) {
            now_eu = (long) (Math.pow(this.maxHeat, 2) * GTValues.VA[GTValues.EV] *
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
                // 地图尚未建立时回退到注术单元原始值，避免显示0造成误导。
                if (heatMap == null) previewHeat = stick.heat;
                if (stabilityMap == null) previewStability = stick.stability;

                tooltips.add(AHCCBarLang[0].translate(previewHeat));
                tooltips.add(AHCCBarLang[1].translate(previewStability));
                return tooltips;
            }

            if (stack.is(BotaniaTags.Items.RUNES)) {
                int slot = slotReference.getSlotIndex();
                long previewStability = getPreviewStabilityBySlot(slot);
                tooltips.add(AHCCBarLang[1].translate(previewStability));
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
        if (this.heat >= maxHeat) channelSignal.put(0, 15);
        if (stability > 0.1 * maxStability)
            channelSignal.put(1, (int) (this.maxStability / Math.max(this.stability, 1)) - 1);
        else channelSignal.put(1, 15);

        if (!hatchList.isEmpty()) {
            for (RedstoneSignalBroadcastHatch hatch : hatchList) {
                hatch.setRedstoneSignalOutput(getChannelSignal(hatch.channel));
            }
        }
    }

    public void resetSignal() {
        channelSignal.put(0, 0);
        channelSignal.put(1, 0);
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
                        // 水符文：相邻注术单元热量-1，且相邻注术单元占用-2稳定度
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
                        // 火符文：相邻注术单元热量x1.2，符文自身占用1稳定度
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
                        // 春符文：稳定度>50%时，占用2稳定度并使所有注术单元热量+1；
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
                        // 夏符文：每有1火元素，相邻注术单元热量+5%；符文自身占用1稳定度,额外获得等同于火元素数量的稳定度占用
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
                        // 秋符文：自身占用2稳定度；每有1地元素，自身每相邻一个注术单元，热量上限+2
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
                        // 所有注术单元热量+10%；自身周围1格半径（含对角）燃料热量-25%
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
                        // 使左侧相邻注术单元占用稳定度x2；
                        // 若左侧注术单元带罪孽元素则热量x2，否则热量x1.5
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
                        // 对角注术单元热量+5%，并增加1稳定占用
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
                        // 每有1罪孽元素，使正四格注术单元产热+10%；
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
                        // 使正四格注术单元产热增加100%，每有一个符文（含自身）该值减少20%
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
                        // 1) 自身长十字范围（整行整列）所有注术单元产热+10%
                        // 2) 若火元素占比最多：
                        // - 所有带火元素的注术单元产热+10%
                        // - 自身长十字范围注术单元获得额外档位加成（取最高档）：
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
                        // 全部注术单元获得产热与占用修正，档位由(水+地符文数量)决定
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
        // 统一约束：
        // 1) 能降低燃料稳定占用的符文，不能把占用降到0以下
        // 2) 若燃料原始占用本身<0，则不做任何调整
        for (int i = 0; i < slot_range; i++) {
            for (int j = 0; j < slot_range; j++) {
                var stack = inventory.getStackInSlot(getSlotIndex(i, j));
                if (!stack.isEmpty() && stack.getItem() instanceof IManaFuelStick stick &&
                        stick.stability >= 0 && stabilitymap[i][j] < 0) {
                    stabilitymap[i][j] = 0;
                }
            }
        }
        return List.of(heatmap, stabilitymap);
    }

    @CN({
            "§l将一切咒术与秘法交融凝缩§r,在无尽的§9解析与推演§r之中,我们终铸造那§d超越世界§r(§5Zenith§r)的§b§l奥术新星§r",
            "§e§l此乃奇迹§r",
            "!§c本机器还在测试中§r,最终机制还不完善,随时可能更改,如遇bug,请联系魔力beeeeeeeeeeeee!",
            "允许使用§9§l激光仓§r，§6§l变电仓§r，§c§l红石信号广播仓§r，§d§l中央存储控制总线§r",
            "本机器具有极为特殊的运行机制,请§c§l认真阅读以下机制§r,如有不理解，请致电魔力beeeeeeeeeeeee",
            "本机器UI内部具有放置§6§l注术单元§r和§b§l符文§r的槽位，每个注魔单元可以为本机器提供§c热量§r(名字待定),当机器UI内部放置任意注术单元时,机器开始执行循环",
            "机器具有两种执行模式,并进行循环,在§6§l产热模式§r下,机器将工作§e20s§r,并且每§e0.5s§r执行一次机器热流程,获取来自注术单元的热并且减少§a稳定度§r,在执行完毕后,切换为§b§l维度稳定模式§r",
            "在§b§l维度稳定模式§r下,机器将执行维度稳定,§e10s§r内不进行任何操作,在执行完毕后,切换为§6产热模式§r",
            "在产热模式下,注术单元将给机器注入§c热量§r,当注术单元枯竭时,其将会被弹出到输出IO中，如果机器的§a稳定度§r较低,那么注术单元有可能发生崩解(具体崩解产物请查阅配方类型：§5扭曲崩解§r)",
            "在产热模式下,符文具有一定特效：当将符文放入UI内部槽位时，符文的效果将会显现。符文本身不产生任何§c热量§r，而是给机器提供增益并占用一定§a稳定度§r，当配方执行完毕后，如果机器稳定度较低，符文可能发生崩解并被弹出到输出IO中，崩解会产出§5扭曲符文§r",
            "在产热模式下，§c热量§r会逐渐积累，并且在配方执行完毕后一次性全部转化为电量，热量越高，产生的电量越高，同时当热量大于上限的§650%§r和§c100%§r时，热量对电量的转化比会大幅度上升（由于公式未定，请先咨询魔力beeeee对应公式）",
            "在产热模式下，§a稳定度§r是空间稳定的重要指标，当其低于§c0§r时将会造成重大灾难（但由于魔力beeeeee还没写爆炸，所以现在我们只会把存储热降为零，享受这不会爆炸的时光吧）,符文和注术单元会占用稳定度，与此同时热量占上限超过§650%§r,§c100%§r时，稳定度会随每§e0.5s§r降低，当热量超过§c100%§r时，稳定度会迅速降低",
            "在产热模式执行完毕时，§c热量§r被转化为电量，弹出损坏符文，§a稳定度§r将会回复到满值，随后进入§b维度稳定§r（冷却）模式",
            "在产热模式下,可以输入10000mb魔力稳定剂进行强冷，强冷会立即使稳定值+10,同时立即扣除一部分热量,每一轮至多强冷一次，冬之符文等符文可以强化强冷的效果",
            "具有§e§l两个红石信号频道§r，使用§c§l红石信号广播仓§r来调频",
            "§9§l频道0§r（§c热量§r）：§e每10tick§r刷新（仅在§6产热§r且§7非冷却§r时）；§c热量§r§7≤§70§r→§70§r；§70§r§7<§r§c热量§r§7<§r上限§650%§r→§b2§r；§650%§r§7≤§r§c热量§r§7<§r上限§6100%§r→§65§r；§c热量§r§7≥§r上限→§c10§r",
            "§d§l频道1§r（§a稳定度§r）：同上刷新节奏；若§a稳定度§r§7>§r上限§610%§r，则输出§7max(§70§r,⌊§a上限§r/max(§a当前稳定度§r,1)⌋§7-1§r)；否则输出§c12§r",
            "放置§c红石信号广播仓§r并§e选择频道§r后，仓室输出的§e红石强度§r即为对应频道当前值（§9频道0§r反映§c热量§r阈值档，§d频道1§r反映§a稳定度§r危急程度）",
            "中央存储控制总线可以精确控制主UI的槽位，请查阅它的tooltip",
            "这个机器仍然非常不完善，所以一定要去多咨询魔力beeeeee反馈bug"
    })
    @EN({
            "§lAll curses and secrets arc blended and distilled§r; across endless §9parsing and deduction§r, we at last forge the arcane nova that transcends the world—§5Zenith§r.",
            "§e§lThis we call a miracle.§r",
            "!§cWork in progress§r — mechanics unfinished; changes likely; bugs → Mana bee.",
            "Allows §9§lLaser Hatch§r, §6§lEnergy Hatch§r, §c§lRedstone Signal Control Hatch§r, §d§lCentral Control Bus§r.",
            "Highly unusual rules — §c§lread below§r; call Mana bee if unclear.",
            "UI slots for §6§linfusion cells§r and §brunes§r; each cell supplies §cHeat§r (name TBD); any cell starts the cycle.",
            "Two phases: in §6§lHeating§r, runs §e20s§r, §cHeat§r logic every §e0.5s§r and consumes §aStability§r; then §b§lDimensional Stabilization§r.",
            "In §b§lDimensional Stabilization§r, §e10s§r idle; then back to §6Heating§r.",
            "Heating: cells add §cHeat§r; depleted cells export; low §aStability§r may §5TwistCollapse§r (see §5TwistCollapse§r recipes).",
            "Runes: no §cHeat§r, buffs and §aStability§r cost; low stability may break runes into §5Twisted Runes§r.",
            "§cHeat§r banks then converts to EU; higher §cHeat§r → more EU; above §650%§r / §c100%§r cap, conversion spikes (formula TBD — ask Mana bee).",
            "§aStability§r anchors space; below §c0§r is disaster (currently only clears stored §cHeat§r). Above §650%§r / §c100%§r cap, §aStability§r drains every §e0.5s§r; over §c100%§r cap it drops faster.",
            "在产热模式下,可以输入10000mb魔力稳定剂进行强冷，强冷会立即使稳定值+10,同时立即扣除一部分热量,每一轮至多强冷一次，冬之符文等符文可以强化强冷的效果",
            "Heating ends: EU credited, broken runes popped, §aStability§r full, then §bcooldown§r (stabilization).",
            "§e§lTwo redstone channels§r — tune with §c§lRedstone Broadcast Hatch§r.",
            "§9§lCh0§r (§cHeat§r): refreshes every §e10 ticks§r while §6heating§r & §7not cooling§r: §cHeat§r§7≤§70§r→§70§r; §70§r§7<§r§cHeat§r§7<§r §650%§r cap→§b2§r; §650%§r§7≤§r§cHeat§r§7<§r§6100%§r cap→§65§r; §cHeat§r§7≥§r cap→§c10§r.",
            "§d§lCh1§r (§aStability§r): same cadence; if §aStability§r§7>§r §610%§r cap → §7max(§70§r,⌊§amax§r/max(§acurrent§r,1)⌋§7-1§r); else §c12§r.",
            "Place §cRedstone Signal Control Hatch§r and §epick channel§r; output §estrength§r equals that channel (§9Ch0§r = §cHeat§r tier, §dCh1§r = §aStability§r stress)."
    })
    public static Lang[] AHCC_TOOLTIPS;
}
