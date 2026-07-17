package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.ctnhlang.Key;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import com.moguang.ctnhmana.registry.CMItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.client.render.ColorData;
import tech.vixhentx.mcmod.ctnhlib.client.render.highlight.HighlightHandler;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.item.ItemBloodOrb;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import java.util.List;
import java.util.Objects;

public class DemonWillMachine extends RecipeElectricMultiblockMachine {

    private static final double UI_WIDTH_SCALE = 1.5;
    private static final double UI_HEIGHT_SCALE = 1.5;

    private static final EnumDemonWillType[] TYPED_DEMON_WILLS = {
            EnumDemonWillType.VENGEFUL,
            EnumDemonWillType.CORROSIVE,
            EnumDemonWillType.STEADFAST,
            EnumDemonWillType.DESTRUCTIVE
    };

    public static final int BASE_GENERATION_EU = 256;
    public static final double ORB_LP_CAP_FRACTION = 0.01;
    public static final double ORB_LP_BASE_FRACTION = 0.001;
    public static final double ORB_LP_PER_RUNE_FRACTION = 0.001;
    /** Each speed rune level adds this much to recipe duration (×(1 + level × this)). */
    public static final double SPEED_DURATION_PER_RUNE = 0.1;
    /** Typed will gained per displacement rune level (tooltip: level×2 uses level = rune level × this). */
    public static final double DISPLACEMENT_PER_RUNE_MULTIPLIER = 2.0;

    @Persisted
    public final NotifiableItemStackHandler machineStorage;
    public boolean isBoosted = false;
    public double diversity = 1;
    public double difference = 0;
    public static final int BASE_MAX_WILL_TRANSFER = 400;

    public int Sacrifice_rune = 0;
    public int Speed_rune = 0;
    public int Capacity_rune = 0;
    public int Augmented_rune = 0;
    public int Orb_rune = 0;
    public int Charging_rune = 0;
    public int Displacement_rune = 0;
    private long displacementAppliedTick = -1;
    @Persisted
    @DescSynced
    public BlockPos pos1 = BlockPos.ZERO;
    @Persisted
    @DescSynced
    public BlockPos pos2 = BlockPos.ZERO;
    @Persisted
    public int orb_capacity = 0;
    @Persisted
    @DescSynced
    public double energy_cache = 0;
    private static final int[][] RUNE_OFFSETS = new int[][] {
            { 0, 34, -5 }, { 1, 34, -5 }, { 2, 34, -5 }, { -1, 34, -5 }, { -2, 34, -5 },
            { 3, 34, -4 }, { -3, 34, -4 }, { 5, 34, -2 }, { -5, 34, -2 }, { 6, 34, -1 },
            { 6, 34, 0 }, { 6, 34, 1 }, { 6, 34, 2 }, { 6, 34, 3 }, { -6, 34, -1 },
            { -6, 34, 0 }, { -6, 34, 1 }, { -6, 34, 2 }, { -6, 34, 3 }, { 5, 34, 4 },
            { -5, 34, 4 }, { -3, 34, 6 }, { 3, 34, 6 }, { 0, 34, 7 }, { -1, 34, 7 },
            { -2, 34, 7 }, { 1, 34, 7 }, { 2, 34, 7 }, { 0, 32, -8 }, { 1, 32, -8 },
            { 2, 32, -8 }, { -1, 32, -8 }, { -2, 32, -8 }, { 3, 32, -7 }, { 4, 32, -7 },
            { 5, 32, -7 }, { -3, 32, -7 }, { -4, 32, -7 }, { -5, 32, -7 }, { -8, 32, -4 },
            { -8, 32, -3 }, { -8, 32, -2 }, { 8, 32, -4 }, { 8, 32, -3 }, { 8, 32, -2 },
            { -9, 32, -1 }, { -9, 32, 0 }, { -9, 32, 1 }, { -9, 32, 2 }, { -9, 32, 3 },
            { 9, 32, -1 }, { 9, 32, 0 }, { 9, 32, 1 }, { 9, 32, 2 }, { 9, 32, 3 },
            { -8, 32, 4 }, { -8, 32, 5 }, { -8, 32, 6 }, { 8, 32, 4 }, { 8, 32, 5 },
            { 8, 32, 6 }, { 5, 32, 9 }, { 4, 32, 9 }, { 3, 32, 9 }, { -5, 32, 9 },
            { -4, 32, 9 }, { -3, 32, 9 }, { -2, 32, 10 }, { -1, 32, 10 }, { 0, 32, 10 },
            { 1, 32, 10 }, { 2, 32, 10 }
    };

    private BlockPos[] getRunes() {
        BlockPos[] runes = new BlockPos[RUNE_OFFSETS.length];
        for (int i = 0; i < RUNE_OFFSETS.length; i++) {
            int[] offset = RUNE_OFFSETS[i];
            runes[i] = MachineUtils.getOffset(this, offset[0], offset[1], offset[2]);
        }
        return runes;
    }

    public EnumDemonWillType type = EnumDemonWillType.DEFAULT;
    @Nullable
    public SoulNetwork soulNetwork;
    public boolean soulNetworkLinked = false;
    public int lastLpPerTick = 0;

    public DemonWillMachine(IMachineBlockEntity holder) {
        super(holder);
        machineStorage = createMachineStorage((byte) 1);
    }

    private static int scw(int v) {
        return (int) Math.round(v * UI_WIDTH_SCALE);
    }

    private static int sch(int v) {
        return (int) Math.round(v * UI_HEIGHT_SCALE);
    }

    @Override
    public @NotNull Widget createUIWidget() {
        int groupW = scw(182 + 8);
        int groupH = sch(117 + 8);
        var group = new WidgetGroup(0, 0, groupW, groupH);

        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, scw(182), sch(117))
                .setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                        .textSupplier(this.getLevel().isClientSide ? null : this::addDisplayText)
                        .setMaxWidthLimit(scw(200))
                        .clickHandler(this::handleDisplayClick)));

        int rowY = groupH - sch(30);
        group.addWidget(
                new SlotWidget(machineStorage.storage, 0, groupW - scw(30), rowY, true, true)
                        .setBackground(CMGuiTextures.SLOT_ORB));

        int buttonW = 20;
        int buttonH = 20;
        int anchorX = (groupW - buttonW) / 2;
        var anchorHighlightButton = new SwitchWidget(anchorX, rowY, buttonW, buttonH, (clickData, pressed) -> {
            highlightAnchorBlocks();
        })
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_ANIMATION_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_ANIMATION_ON))
                .setSupplier(() -> false)
                .setHoverTooltips(INFO_ANCHOR_HIGHLIGHT.translate());
        group.addWidget(anchorHighlightButton);

        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    /** Highlights hellforged anchor blocks at {@link #pos1} and {@link #pos2} (client only). */
    public void highlightAnchorBlocks() {
        var level = getLevel();
        if (level == null || !level.isClientSide() || !isFormed()) {
            return;
        }
        var dim = level.dimension();
        long until = System.currentTimeMillis() + 20_000L;
        if (!pos1.equals(BlockPos.ZERO)) {
            HighlightHandler.highlight(pos1, dim, until, ColorData.RED);
        }
        if (!pos2.equals(BlockPos.ZERO)) {
            HighlightHandler.highlight(pos2, dim, until, ColorData.RED);
        }
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        int w = scw(198);
        int h = sch(208);
        return new ModularUI(w, h, this, entityPlayer).widget(new FancyMachineUIWidget(this, w, h));
    }

    protected NotifiableItemStackHandler createMachineStorage(byte value) {
        return new NotifiableItemStackHandler(
                this, 1, IO.NONE, IO.BOTH, slots -> new CustomItemStackHandler(1) {

                    @Override
                    public int getSlotLimit(int slot) {
                        return value;
                    }

                    @Override
                    public void onContentsChanged(int slot) {
                        updateMachineStorage();
                        super.onContentsChanged(slot);
                    }
                }).setFilter(DemonWillMachine::isAllowedMachineStorageItem);
    }

    public static boolean isWillCore(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        var item = stack.getItem();
        return item == CMItems.VENGEFUL_CORE.get() || item == CMItems.CORROSIVE_CORE.get() ||
                item == CMItems.STEADFAST_CORE.get() || item == CMItems.DESTRUCTIVE_CORE.get();
    }

    public static boolean isAllowedMachineStorageItem(ItemStack stack) {
        return stack.isEmpty() || stack.getItem() instanceof ItemBloodOrb || isWillCore(stack);
    }

    public ItemStack getMachineStorageItem() {
        return machineStorage.getStackInSlot(0);
    }

    /** Updates specialty mode and soul network link from the controller storage slot. */
    public void updateMachineStorage() {
        soulNetworkLinked = false;
        soulNetwork = null;
        type = EnumDemonWillType.DEFAULT;

        var stack = getMachineStorageItem();
        if (stack.isEmpty()) {
            return;
        }
        if (stack.getItem() instanceof ItemBloodOrb bloodOrb) {
            var binding = bloodOrb.getBinding(stack);
            BloodOrb orb = bloodOrb.getOrb(stack);
            if (binding != null) {
                soulNetwork = NetworkHelper.getSoulNetwork(binding);
                orb_capacity = orb.getCapacity();
                soulNetworkLinked = soulNetwork != null;
            }
            return;
        }
        if (isWillCore(stack)) {
            type = resolveCoreType(stack);
        }
    }

    private static EnumDemonWillType resolveCoreType(ItemStack stack) {
        if (stack.is(CMItems.VENGEFUL_CORE.get())) {
            return EnumDemonWillType.VENGEFUL;
        }
        if (stack.is(CMItems.CORROSIVE_CORE.get())) {
            return EnumDemonWillType.CORROSIVE;
        }
        if (stack.is(CMItems.STEADFAST_CORE.get())) {
            return EnumDemonWillType.STEADFAST;
        }
        if (stack.is(CMItems.DESTRUCTIVE_CORE.get())) {
            return EnumDemonWillType.DESTRUCTIVE;
        }
        return EnumDemonWillType.DEFAULT;
    }

    public double getTotalWillDifference() {
        double difference = 0;
        if (type == EnumDemonWillType.DEFAULT) {
            for (EnumDemonWillType type1 : EnumDemonWillType.values()) {
                var newDifference = getWillDifference(pos1, pos2, type1);
                if (newDifference == 0) {
                    continue;
                }
                difference += newDifference;
                adjustWillChunk(pos1, pos2, type1);
            }
        } else {
            difference += getWillDifference(pos1, pos2, type);
            adjustWillChunk(pos1, pos2, type);
        }
        return difference;
    }

    public double getWillDifference(BlockPos pos1, BlockPos pos2, EnumDemonWillType type1) {
        var will1 = WorldDemonWillHandler.getCurrentWill(Objects.requireNonNull(getLevel()), pos1, type1);
        var will2 = WorldDemonWillHandler.getCurrentWill(Objects.requireNonNull(getLevel()), pos2, type1);
        if (will1 == will2 || Math.max(will1, will2) < 5) {
            return 0;
        }
        return Math.abs(will1 - will2);
    }

    public double getRecipeDifference(double baseDifference) {
        if (baseDifference <= 5) return 0;
        var runeAdjustedDifference = (baseDifference + Capacity_rune) * (1 + 0.1 * Augmented_rune);
        if (type == EnumDemonWillType.DEFAULT) {
            return runeAdjustedDifference;
        }
        return runeAdjustedDifference * 2;
    }

    public double getHigherWill(BlockPos pos1, BlockPos pos2, EnumDemonWillType type1) {
        var will1 = WorldDemonWillHandler.getCurrentWill(Objects.requireNonNull(getLevel()), pos1, type1);
        var will2 = WorldDemonWillHandler.getCurrentWill(Objects.requireNonNull(getLevel()), pos2, type1);
        return Math.max(will1, will2);
    }

    public void adjustWillChunk(BlockPos pos1, BlockPos pos2, EnumDemonWillType type1) {
        var willChunk1 = WorldDemonWillHandler.getWillChunk(Objects.requireNonNull(getLevel()), pos1);
        var willChunk2 = WorldDemonWillHandler.getWillChunk(Objects.requireNonNull(getLevel()), pos2);
        var difference = getWillDifference(pos1, pos2, type1);
        var high_will = getHigherWill(pos1, pos2, type1);
        if (willChunk1.getCurrentWill().getWill(type1) < willChunk2.getCurrentWill().getWill(type1)) {
            if (Math.abs(high_will) >= 9999) {
                willChunk2.getCurrentWill().clearWill();
                difference = 0;
            }
            if (Math.abs(high_will) < 10) {
                willChunk2.getCurrentWill().drainWill(type1, Math.abs(difference));
            } else {
                willChunk1.getCurrentWill().addWill(type1, Math.abs(difference) * 0.04, getMaxWillTransfer());
                willChunk2.getCurrentWill().drainWill(type1, Math.abs(difference) * 0.08);
            }
        } else {
            if (Math.abs(high_will) >= 9999) {
                willChunk1.getCurrentWill().clearWill();
                difference = 0;
            }
            if (Math.abs(high_will) < 10) {
                willChunk1.getCurrentWill().drainWill(type1, Math.abs(difference));
            } else {
                willChunk1.getCurrentWill().drainWill(type1, Math.abs(difference) * 0.08);
                willChunk2.getCurrentWill().addWill(type1, Math.abs(difference) * 0.04, getMaxWillTransfer());
            }
        }
    }

    public void calculateDiversity() {
        double total1 = 0;
        double total2 = 0;
        double diversity1 = 1.2;
        double diversity2 = 1.2;
        for (EnumDemonWillType type1 : EnumDemonWillType.values()) {
            total1 += WorldDemonWillHandler.getCurrentWill(Objects.requireNonNull(getLevel()), pos1, type1);
            total2 += WorldDemonWillHandler.getCurrentWill(Objects.requireNonNull(getLevel()), pos2, type1);
        }
        for (EnumDemonWillType type1 : EnumDemonWillType.values()) {
            if (total1 == 0) {
                diversity1 = 0.2;
            } else {
                var will = WorldDemonWillHandler.getCurrentWill(Objects.requireNonNull(getLevel()), pos1, type1);
                diversity1 -= Math.pow(will / total1, 2);
            }
            if (total2 == 0) {
                diversity2 = 0.2;
            } else {
                var will = WorldDemonWillHandler.getCurrentWill(Objects.requireNonNull(getLevel()), pos2, type1);
                diversity2 -= Math.pow(will / total2, 2);
            }
        }
        diversity = diversity1 * diversity2;
    }

    public int getMaxWillTransfer() {
        return BASE_MAX_WILL_TRANSFER;
    }

    public void calculateRune() {
        Speed_rune = 0;
        Augmented_rune = 0;
        Capacity_rune = 0;
        Sacrifice_rune = 0;
        Orb_rune = 0;
        Charging_rune = 0;
        Displacement_rune = 0;
        for (var rune : getRunes()) {
            var runeBlock = Objects.requireNonNull(getLevel()).getBlockState(rune).getBlock();
            if (runeBlock.equals(BloodMagicBlocks.SPEED_RUNE.get())) {
                Speed_rune++;
            } else if (runeBlock.equals(BloodMagicBlocks.SPEED_RUNE_2.get())) {
                Speed_rune += 2;
            } else if (runeBlock.equals(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get())) {
                Augmented_rune++;
            } else if (runeBlock.equals(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE_2.get())) {
                Augmented_rune += 2;
            } else if (runeBlock.equals(BloodMagicBlocks.CAPACITY_RUNE.get())) {
                Capacity_rune++;
            } else if (runeBlock.equals(BloodMagicBlocks.CAPACITY_RUNE_2.get())) {
                Capacity_rune += 2;
            } else if (runeBlock.equals(BloodMagicBlocks.ORB_RUNE.get())) {
                Orb_rune++;
            } else if (runeBlock.equals(BloodMagicBlocks.ORB_RUNE_2.get())) {
                Orb_rune += 2;
            } else if (runeBlock.equals(BloodMagicBlocks.CHARGING_RUNE.get())) {
                Charging_rune++;
            } else if (runeBlock.equals(BloodMagicBlocks.CHARGING_RUNE_2.get())) {
                Charging_rune += 2;
            } else if (runeBlock.equals(BloodMagicBlocks.DISPLACEMENT_RUNE.get())) {
                Displacement_rune++;
            } else if (runeBlock.equals(BloodMagicBlocks.DISPLACEMENT_RUNE_2.get())) {
                Displacement_rune += 2;
            } else if (runeBlock.equals(BloodMagicBlocks.SACRIFICE_RUNE.get()) ||
                    runeBlock.equals(BloodMagicBlocks.SELF_SACRIFICE_RUNE.get())) {
                        Sacrifice_rune++;
                    } else
                if (runeBlock.equals(BloodMagicBlocks.SACRIFICE_RUNE_2.get()) ||
                        runeBlock.equals(BloodMagicBlocks.SELF_SACRIFICE_RUNE_2.get())) {
                            Sacrifice_rune += 2;
                        }
        }
    }

    public double difference_caculate(double difference) {
        var num = 0.0;
        num = difference * Math.log(difference + 1);
        return num;
    }

    public double getBoostRate() {
        return 2 + 0.2 * Sacrifice_rune;
    }

    /** Converts raw (DEFAULT) will into each typed will on both sampling chunks; once per tick. */
    public void applyDisplacementRunes() {
        if (Displacement_rune <= 0) {
            return;
        }
        var level = getLevel();
        if (level == null || level.isClientSide()) {
            return;
        }
        var tick = getOffsetTimer();
        if (tick == displacementAppliedTick) {
            return;
        }
        displacementAppliedTick = tick;
        var perType = Displacement_rune * DISPLACEMENT_PER_RUNE_MULTIPLIER;
        convertDefaultWillAtChunk(level, pos1, perType);
        convertDefaultWillAtChunk(level, pos2, perType);
    }

    private static void convertDefaultWillAtChunk(
                                                  Level level, BlockPos pos, double perType) {
        var will = WorldDemonWillHandler.getWillChunk(level, pos).getCurrentWill();
        var available = will.getWill(EnumDemonWillType.DEFAULT);
        var units = Math.min(perType, Math.floor(available / TYPED_DEMON_WILLS.length));
        if (units <= 0) {
            return;
        }
        var drain = units * TYPED_DEMON_WILLS.length;
        will.drainWill(EnumDemonWillType.DEFAULT, drain);
        for (var willType : TYPED_DEMON_WILLS) {
            will.addWill(willType, units, DemonWillMachine.BASE_MAX_WILL_TRANSFER);
        }
    }

    /**
     * Net EU/t output. When soul network is linked, a fraction of gross generation is converted to LP
     * (capped at {@link #ORB_LP_CAP_FRACTION}) and deducted from output.
     *
     * @param depositLp if true and on server, adds LP to the linked soul network this cycle
     */
    public double computeGenerationEutMultiplier(boolean depositLp) {
        var effectiveDifference = getRecipeDifference(difference);
        var multiplier = diversity * difference_caculate(effectiveDifference);
        if (isBoosted) {
            multiplier *= getBoostRate();
        }
        var gross = multiplier * BASE_GENERATION_EU;

        if (!soulNetworkLinked) {
            return gross;
        }

        var lpFraction = Math.min(ORB_LP_CAP_FRACTION, ORB_LP_PER_RUNE_FRACTION * Orb_rune);
        if (depositLp && soulNetwork != null && Orb_rune >= 1) {
            var level = getLevel();
            if (level != null && !level.isClientSide() && lpFraction > 0) {
                var lp = (int) (gross * lpFraction);
                if (lp > 0) {
                    soulNetwork.add(new SoulTicket(lp), this.orb_capacity);
                    lastLpPerTick = lp;
                }
            }
        }

        return gross * (1 - lpFraction);
    }

    public GTRecipe getBloodRecipe() {
        return GTRecipeBuilder.ofRaw()
                .inputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(),
                        (int) (1000 * (1 + 0.2 * Sacrifice_rune))))
                .buildRawRecipe()
                .toRuntime();
    }

    public static @Nullable Component recipeModifier(@NotNull MetaMachine machine, RecipeHandlerGroup group,
                                                     @NotNull GTRecipe recipe) {
        if (!(machine instanceof DemonWillMachine dmachine)) {
            return RecipeModifier.nullWrongType(DemonWillMachine.class, machine);
        }
        dmachine.applyDisplacementRunes();
        var eu = dmachine.computeGenerationEutMultiplier(false);
        dmachine.energy_cache = eu;
        recipe.multiplyDuration(1 + dmachine.Speed_rune * SPEED_DURATION_PER_RUNE);
        recipe.multiplyEUt(eu);
        return null;
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            if (!soulNetworkLinked && !type.equals(EnumDemonWillType.DEFAULT)) {
                var stack = getMachineStorageItem();
                if (isWillCore(stack) && Math.random() < 0.05) {
                    stack.shrink(1);
                }
            }
            var boosterRecipe = getBloodRecipe();
            var group = getRecipeLogic().getLastGroup();
            this.isBoosted = RecipeHelper.matchRecipe(group, boosterRecipe).isSuccess() &&
                    RecipeHelper.handleRecipeIO(group, boosterRecipe, IO.IN).isSuccess();
        }
        return value;
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        difference = getTotalWillDifference();
        if (type == EnumDemonWillType.DEFAULT) {
            calculateDiversity();
        } else {
            diversity = 0.8;
        }
        computeGenerationEutMultiplier(true);
        return super.beforeWorking(recipe);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateMachineStorage();
    }

    @Override
    public void onStructureFormed() {
        pos1 = MachineUtils.getOffset(this, 10, 40, -9);
        pos2 = MachineUtils.getOffset(this, -10, 40, 11);
        updateMachineStorage();
        calculateRune();
        super.onStructureFormed();
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        var outputEnergy = energy_cache;
        var voltageName = GTValues.VNF[GTUtil.getTierByVoltage((long) outputEnergy)];
        textList.add(INFO_POWER.translate(FormattingUtil.formatNumbers((long) outputEnergy), voltageName));
        textList.add(INFO_RUNE_LEVELS.translate(
                Speed_rune, Capacity_rune, Augmented_rune, Sacrifice_rune, Orb_rune, Charging_rune,
                Displacement_rune));
        switch (type) {
            case DEFAULT -> textList.add(INFO_SPECIALTY_DEFAULT.translate());
            case VENGEFUL -> textList.add(INFO_SPECIALTY_VENGEFUL.translate());
            case CORROSIVE -> textList.add(INFO_SPECIALTY_CORROSIVE.translate());
            case STEADFAST -> textList.add(INFO_SPECIALTY_STEADFAST.translate());
            case DESTRUCTIVE -> textList.add(INFO_SPECIALTY_DESTRUCTIVE.translate());
        }
        textList.add(INFO_DIFFERENCE.translate(String.format("%.1f", difference)));
        if (soulNetworkLinked) {
            textList.add(INFO_SOUL_NETWORK_LINKED.translate());
            if (lastLpPerTick > 0) {
                var lpPercent = Math.min(ORB_LP_CAP_FRACTION,
                        ORB_LP_BASE_FRACTION + ORB_LP_PER_RUNE_FRACTION * Orb_rune) * 100;
                textList.add(INFO_LP_CONVERSION.translate(
                        String.format("%.2f", lpPercent),
                        FormattingUtil.formatNumbers(lastLpPerTick)));
            }
        }
        if (isBoosted) {
            textList.add(INFO_BOOSTED.translate());
        }
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        return capability != EURecipeCapability.CAP;
    }

    // lang: info (display in UI)
    @Key("info.default")
    @CN("专精强化：无")
    @EN("Specialty boost: None")
    public static Lang INFO_SPECIALTY_DEFAULT;
    @Key("info.vengeful")
    @CN("专精强化：复仇")
    @EN("Specialty boost: Vengeful")
    public static Lang INFO_SPECIALTY_VENGEFUL;
    @Key("info.corrosive")
    @CN("专精强化：腐蚀")
    @EN("Specialty boost: Corrosive")
    public static Lang INFO_SPECIALTY_CORROSIVE;
    @Key("info.steadfast")
    @CN("专精强化：坚韧")
    @EN("Specialty boost: Steadfast")
    public static Lang INFO_SPECIALTY_STEADFAST;
    @Key("info.destructive")
    @CN("专精强化：破坏")
    @EN("Specialty boost: Destructive")
    public static Lang INFO_SPECIALTY_DESTRUCTIVE;
    @Key("info.1")
    @CN("浓度差异：%s")
    @EN("Concentration difference: %s")
    public static Lang INFO_DIFFERENCE;
    @Key("info.boosted")
    @CN("§4困惑强化开启§r（困惑液消耗中）")
    @EN("§4Doubt boost active§r (consuming doubt fluid)")
    public static Lang INFO_BOOSTED;
    @Key("info.power")
    @CN("当前发电量：%s EU/t（%s）")
    @EN("Output: %s EU/t (%s)")
    public static Lang INFO_POWER;
    @Key("info.rune_levels")
    @CN("符文等级：速度 %d | 增容 %d | 超容 %d | 牺牲 %d | 宝珠 %d | 充能 %d | 转位 %d")
    @EN("Rune levels — Speed %d | Capacity %d | Augmented %d | Sacrifice %d | Orb %d | Charging %d | Displacement %d")
    public static Lang INFO_RUNE_LEVELS;
    @Key("info.soul_network_linked")
    @CN("§a灵魂网络已链接")
    @EN("§aSoul network linked")
    public static Lang INFO_SOUL_NETWORK_LINKED;
    @Key("info.lp_conversion")
    @CN("宝珠转化：§a%.2f%%§r EU/t → LP，上周期 +%s LP")
    @EN("Orb conversion: §a%.2f%%§r EU/t → LP, last cycle +%s LP")
    public static Lang INFO_LP_CONVERSION;
    @Key("info.anchor_highlight")
    @CN("显示锚定的恶魔合金")
    @EN("Show anchored demon alloy")
    public static Lang INFO_ANCHOR_HIGHLIGHT;

    @Key("ctnh.multiblock.demon_will_generator.tooltip")
    @CN({
            "驾驭恶魔之力",
            "允许使用激光仓、变电仓",
            "利用机器两侧区块的恶魔意志浓度差发电；有效浓度差越大，发电量增长越快（含 ln 项）",
            "警告：§4当两侧意志浓度差小于5时，将无视所有符文加成，直接将该意志浓度差计算为0§r",
            "计算基于机器两侧恶魔合金所在区块的意志浓度，可以点击UI「显示锚定的恶魔合金」来高亮它们正下方的位点，定位对应的区块（注意：检测的是恶魔合金块所在位置的区块，请注意可能发生的结构跨多个区块问题）",
            "两侧区块内各类恶魔意志的多样性会影响发电效率",
            "控制器槽位可放入 §4血魔法宝珠§r（须已绑定）或意志核心；宝珠链接其主人的灵魂网络，核心开启专精（每秒 5% 概率消耗 1 个）",
            "结构符文可替换（T2 符文=2 级）：\n§4牺牲/自我牺牲符文§r——每级使困惑强化模式的困惑液消耗量和发电量+20%§r\n§3速度符文§r——每级使配方时长+10%§r\n§e增容符文§r——每级使有效浓度差+1§r\n§c超容符文§r——每级使有效浓度差+10%§r\n§d宝珠符文§r——已链接灵魂网络时，将实际发电量的一部分转化为灵魂网络内的LP，转化率=0.1%*宝珠符文数\n§b转位符文§r——在配方开始前每周期将两侧区块空白意志转为复仇/腐蚀/坚韧/破坏各 §a等级*2§r 点\n§7充能符文§r——仅统计，暂无效果\n==============================",
            "输入 §4困惑液§r 开启困惑强化；约每秒消耗 §a1000×(1+0.2×牺牲等级) mb§r",
            "按住 Shift 查看详细公式；控制器界面显示各符文等级统计"
    })
    @EN({
            "Harness demon will",
            "Supports laser and transformer hatches",
            "Generates power from demon will difference between the two chunk sides; higher effective difference scales output faster (includes ln term)",
            "Per will type: §4counts toward raw difference only if max(both sides) ≥ 5 and the sides differ; otherwise that type is 0§r",
            "Uses will concentration in the chunks at the hellforged blocks on each side of the structure",
            "Diversity of will types on both sides affects generation efficiency",
            "Controller slot: §4blood orb§r (must be bound) or will core; orb links the owner's soul network, core enables specialty (5%/s chance to consume 1 per second)",
            "Replaceable structure runes (tier II counts as 2 levels):\n§4Sacrifice / Self-Sacrifice§r — each level adds §a+20%§r doubt-fluid use and output while doubt boost is active\n§3Speed§r — each level adds §a+10%§r recipe duration\n§eCapacity§r — each level adds §a+1§r to effective concentration difference\n§cAugmented Capacity§r — each level adds §a+10%§r effective concentration difference\n§dOrb§r — when soul network is linked, converts §a1%§r of actual output into LP at §a0.1%×orb rune levels§r conversion rate\n§bDisplacement§r — each cycle before the recipe starts, converts raw will on both sides into §alevel×2§r of each typed will (Vengeful / Corrosive / Steadfast / Destructive)\n§7Charging§r — counted only, no effect yet\n==============================",
            "Supply §4doubt fluid§r for doubt boost; consumes about §a1000×(1+0.2×sacrifice level) mB§r per second",
            "Hold Shift for detailed formulas; controller UI shows rune level totals"
    })
    public static Lang[] TOOLTIPS;
    @Key("ctnh.multiblock.demon_will_generator.shift_tooltip")
    @CN({
            "真实浓度差（单类型）= 两侧不等且 max(两侧)≥5 时取 |差值|，否则为 0",
            "有效浓度差 = (真实浓度差 + 增容等级) × (1 + 0.1×超容等级)；专精模式再 ×2",
            "EU/t = 256 × 多样性 × 有效浓度差 × ln(有效浓度差+1) × [困惑强化倍率]",
            "困惑强化倍率 = 2 + 0.2×牺牲等级（需持续输入困惑液）",
            "多样性：两侧各按 1.2−Σ(占比²) 计算后相乘；无意志侧取 0.2；专精固定 0.8；两侧均衡时最高约 1.44",
            "意志迁移：高侧扣除差值4%，低侧获得差值4%；高侧该类型意志<10 时只清空不迁移；单侧≥999 时清空该侧全部意志",
            "专精模式仅处理对应意志；默认模式累加所有类型的浓度差",
            "已链接灵魂网络时：每周期 LP = 毛发电 × min(1%, 0.1%+0.1%×宝珠等级)；实际 EU/t = 毛发电 × (1 − 转化比例)",
            "转位：每侧区块消耗 4×min(等级×2, floor(空白/4)) 空白意志，四种类型意志各 +min(等级×2, floor(空白/4))"
    })
    @EN({
            "Raw difference (per type) = |side1−side2| only if sides differ and max(both sides) ≥ 5; otherwise 0",
            "Effective difference = raw difference × (1 + 0.1×capacity levels) × (1 + 0.1×augmented levels); ×2 in specialty mode",
            "EU/t = 256 × diversity × effective difference × ln(effective difference+1) × [doubt boost]",
            "Doubt boost = 2 + 0.2×sacrifice levels (requires continuous doubt fluid input)",
            "Diversity: per side 1.2−Σ(ratio²), then multiply both sides; 0.2 if a side has no will; 0.8 in specialty; up to ~1.44 when both sides are balanced",
            "Will migration: high side −4% of difference, low side +4%; if high side <10 for that type, drain only (no transfer); if either side ≥999, clear all will on that side",
            "Specialty mode processes one will type only; default mode sums concentration differences of all types",
            "With linked soul network: LP per cycle = gross EU/t × min(1%, 0.1%+0.1%×orb levels); actual EU/t = gross × (1 − conversion fraction)",
            "Displacement: per side drains 4×min(level×2, floor(raw/4)) raw will; each typed will gains +min(level×2, floor(raw/4))"
    })
    public static Lang[] SHIFT_TOOLTIPS;
}
