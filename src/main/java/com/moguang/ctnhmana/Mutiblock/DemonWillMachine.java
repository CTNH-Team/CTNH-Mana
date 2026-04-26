package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.Domain;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.Key;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;

import java.util.List;
import java.util.Objects;

@Domain(value = "ctnh", category = "multiblock.demon_will_generator")
public class DemonWillMachine extends WorkableElectricMultiblockMachine {

    @Persisted
    public final NotifiableItemStackHandler machineStorage;
    public boolean isBoosted = false;
    public double diversity = 1;
    public double difference = 0;
    public int Sacrifice_rune = 0;
    public int Speed_rune = 0;
    public int Capacity_rune = 0;
    public int Augmented_rune = 0;
    public BlockPos pos1 = BlockPos.ZERO;
    public BlockPos pos2 = BlockPos.ZERO;
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
    public int MAX_WILL = 400;
    public List<String> enableTypes = List.of("vengeful_core", "corrosive_core", "steadfast_core", "destructive_core");
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            DemonWillMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    public DemonWillMachine(IMachineBlockEntity holder) {
        super(holder);
        machineStorage = createMachineStorage((byte) 1);
    }

    @Override
    public @NotNull Widget createUIWidget() {
        var widget = super.createUIWidget();
        if (widget instanceof WidgetGroup group) {
            var size = group.getSize();
            group.addWidget(
                    new SlotWidget(machineStorage.storage, 0, size.width - 30, size.height - 30, true, true)
                            .setBackground(GuiTextures.SLOT));
        }
        return widget;
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
                        resetMode();
                        super.onContentsChanged(slot);
                    }
                }).setFilter(itemStack -> enableTypes.contains(itemStack.getItem().toString()));
    }

    public ItemStack getMachineStorageItem() {
        return machineStorage.getStackInSlot(0);
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
        if (will1 == will2) {
            return 0;
        }
        return Math.abs(will1 - will2);
    }

    public double getRecipeDifference(double baseDifference) {
        var runeAdjustedDifference = baseDifference * Math.pow(1.2, Augmented_rune) + Capacity_rune * 2;
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
                willChunk1.getCurrentWill().addWill(type1, Math.abs(difference) * 0.04, MAX_WILL);
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
                willChunk2.getCurrentWill().addWill(type1, Math.abs(difference) * 0.04, MAX_WILL);
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

    public void resetMode() {
        if (getMachineStorageItem() == null) {
            type = EnumDemonWillType.DEFAULT;
        } else {
            switch (getMachineStorageItem().getItem().toString()) {
                case "vengeful_core" -> type = EnumDemonWillType.VENGEFUL;
                case "corrosive_core" -> type = EnumDemonWillType.CORROSIVE;
                case "steadfast_core" -> type = EnumDemonWillType.STEADFAST;
                case "destructive_core" -> type = EnumDemonWillType.DESTRUCTIVE;
            }
        }
    }

    public void calculateRune() {
        Speed_rune = 0;
        Augmented_rune = 0;
        Capacity_rune = 0;
        Sacrifice_rune = 0;
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
        return 2 + 0.25 * Sacrifice_rune;
    }

    public GTRecipe getBloodRecipe() {
        return GTRecipeBuilder.ofRaw().inputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), 1000))
                .buildRawRecipe();
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe) {
        if (machine instanceof DemonWillMachine dmachine) {
            var difference = dmachine.getRecipeDifference(dmachine.difference);
            var diversity = dmachine.diversity;
            var modifierFunction = ModifierFunction.builder().durationMultiplier(1 + dmachine.Speed_rune * 0.2);
            if (dmachine.isBoosted) {
                modifierFunction
                        .eutMultiplier(diversity * dmachine.difference_caculate(difference) * dmachine.getBoostRate());
            } else {
                modifierFunction.eutMultiplier(diversity * dmachine.difference_caculate(difference));
            }
            return modifierFunction.build();
        }
        return ModifierFunction.IDENTITY;
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            if (!type.equals(EnumDemonWillType.DEFAULT)) {
                double random = Math.random();
                if (random < 0.05)
                    getMachineStorageItem().shrink(1);
            }
        }
        long totalContinuousRunningTime = recipeLogic.getTotalContinuousRunningTime();
        // check boost fluid
        if ((totalContinuousRunningTime == 1 || totalContinuousRunningTime % 20 == 0)) {
            var boosterRecipe = getBloodRecipe();
            this.isBoosted = RecipeHelper.matchRecipe(this, boosterRecipe).isSuccess() &&
                    RecipeHelper.handleRecipeIO(this, boosterRecipe, IO.IN, this.recipeLogic.getChanceCaches())
                            .isSuccess();
        }
        return value;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        difference = getTotalWillDifference();
        if (type == EnumDemonWillType.DEFAULT) {
            calculateDiversity();
        } else {
            diversity = 0.8;
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public void onStructureFormed() {
        pos1 = MachineUtils.getOffset(this, 10, 0, -10);
        pos2 = MachineUtils.getOffset(this, -10, 0, 10);
        resetMode();
        calculateRune();
        super.onStructureFormed();
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        var outputEnergy = (isBoosted ? getBoostRate() : 1) * diversity * getRecipeDifference(difference) * 128;
        var voltageName = GTValues.VNF[GTUtil.getTierByVoltage((long) outputEnergy)];
        textList.add(Component.translatable("ctnh.multiblock.photovoltaic_power_station.info.2",
                FormattingUtil.formatNumbers(outputEnergy), voltageName));
        switch (type) {
            case DEFAULT -> textList.add(INFO_SPECIALTY_DEFAULT.translate());
            case VENGEFUL -> textList.add(INFO_SPECIALTY_VENGEFUL.translate());
            case CORROSIVE -> textList.add(INFO_SPECIALTY_CORROSIVE.translate());
            case STEADFAST -> textList.add(INFO_SPECIALTY_STEADFAST.translate());
            case DESTRUCTIVE -> textList.add(INFO_SPECIALTY_DESTRUCTIVE.translate());
        }
        textList.add(INFO_DIFFERENCE.translate(String.format("%.1f", difference)));
        if (isBoosted) {
            textList.add(INFO_BOOSTED.translate());
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
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
    public static Lang INFO_SPECIALTY_DEFAULT;
    @Key("info.vengeful")
    @CN("专精强化：复仇")
    public static Lang INFO_SPECIALTY_VENGEFUL;
    @Key("info.corrosive")
    @CN("专精强化：腐蚀")
    public static Lang INFO_SPECIALTY_CORROSIVE;
    @Key("info.steadfast")
    @CN("专精强化：坚韧")
    public static Lang INFO_SPECIALTY_STEADFAST;
    @Key("info.destructive")
    @CN("专精强化：破坏")
    public static Lang INFO_SPECIALTY_DESTRUCTIVE;
    @Key("info.1")
    @CN("浓度差异：%s")
    public static Lang INFO_DIFFERENCE;
    @Key("info.boosted")
    @CN("§4血祭模式开启，生命源质强化中")
    public static Lang INFO_BOOSTED;

    @Key("tooltip")
    @CN({
            "驾驭恶魔之力",
            "允许使用激光仓，变电仓",
            "利用机器两侧区块的恶魔意志浓度差发电，浓度差越大发电量呈指数增长。",
            "计算基于机器两侧恶魔合金块处的意志浓度。",
            "两侧区块内各类恶魔意志的多样性会影响发电效率。",
            "在机器内放入意志核心可切换为专精模式，仅针对某种意志类型。",
            "机器内的符文块可替换以提供不同强化：\n§4牺牲符文与自我牺牲符文§r----提升困惑强化模式下的发电倍率（2+0.25*符文等级）§r\n§3速度符文§r----增加配方时长（每级+20%）§r\n§e增容符文§r----每个符文使恶魔意志浓度差+2§r\n§c超容符文§r----每个符文使恶魔意志浓度差额外*1.2（乘算）§r\n==============================",
            "输入§4困惑液§r可激活强化模式，每秒消耗1000mb疑惑液。",
            "按住shift查看详细的计算公式"
    })
    @EN({
            "驾驭恶魔之力",
            "允许使用激光仓，变电仓",
            "利用机器两侧区块的恶魔意志浓度差发电，浓度差越大发电量呈指数增长。",
            "计算基于机器两侧恶魔合金块处的意志浓度。",
            "两侧区块内各类恶魔意志的多样性会影响发电效率。",
            "在机器内放入意志核心可切换为专精模式，仅针对某种意志类型。",
            "机器内的符文块可替换以提供不同强化：\n§4牺牲符文与自我牺牲符文§r----提升困惑强化模式下的发电倍率（2+0.25*符文等级）§r\n§3速度符文§r----增加配方时长（每级+20%）§r\n§e增容符文§r----每个符文使恶魔意志浓度差+2§r\n§c超容符文§r----每个符文使恶魔意志浓度差额外*1.2（乘算）§r\n==============================",
            "输入§4困惑液§r可激活强化模式，每秒消耗1000mb疑惑液。",
            "按住左Ctrl查看详细的计算公式"
    })
    public static Lang[] TOOLTIPS;
    @CN({
            "基础发电公式：最终发电量=浓度差*log2(浓度差)*256*多样性",
            "启用专精模式时，使基础浓度差额外*2，且只消耗对应专精的意志",
            "启用生命源质强化模式时，使最终发电量*(2+0.25*牺牲符文等级)",
            "多样性会影响发电效率，其按照辛普森多样性指数来计算，当只有一种意志时为0.2,最多为1,当启用专精强化时固定为0.8",
            "每次恶魔意志迁移时，消耗一方8%的恶魔意志，并且使另一方获得消耗量一半的恶魔意志，当恶魔意志量<10时，将会一次性消耗所有恶魔意志并且不发生意志迁移"
    })
    @EN({
            "基础发电公式：先计算有效浓度差=difference*log(difference)，最终发电量=浓度差^2*256*多样性",
            "启用专精模式时，使基础浓度差额外*2，且只消耗对应专精的意志",
            "启用生命源质强化模式时，使最终发电量*(2+0.25*牺牲符文等级)",
            "多样性会影响发电效率，其按照辛普森多样性指数来计算，当只有一种意志时为0.2,最多为1,当启用专精强化时固定为0.8",
            "每次恶魔意志迁移时，消耗一方8%的恶魔意志，并且使另一方获得消耗量一半的恶魔意志，当恶魔意志量<10时，将会一次性消耗所有恶魔意志并且不发生意志迁移"
    })
    public static Lang[] SHIFT_TOOLTIPS;
}
