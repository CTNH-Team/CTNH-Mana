package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.capability.IParallelHatch;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.common.parts.ManaHatch;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.ArrayList;
import java.util.List;

import static com.moguang.ctnhmana.common.multiblock.BaseManaMachine.failureManaLang_NoEnoughMana;

public class NicollDysonBeams extends RecipeElectricMultiblockMachine implements IExplosionMachine, ITieredMachine {

    @Persisted
    public int SLOT_COUNT = 4;
    @Persisted
    public int twist_power = 0;
    @Persisted
    public int starlight_power = 0;
    @Persisted
    public int horizen_power = 0;
    @Persisted
    public double max_mana = 1000000;
    @Persisted
    public double mana = 0;
    @Persisted
    public int overload = 0;
    @Persisted
    public int overload_crash = 20;
    @Persisted
    public int broken = 0;
    public int twist_seat = 0;
    public int starlight_seat = 0;
    public static final String MAX_MANA = "max_mana";
    public static final String MANA = "mana";
    public static final String OVERLOAD = "overload";
    public int quasar_power = 0;
    public int mana_parallel = 1;
    public List<String> AvailableRune = List.of("twist_rune", "starlight_rune", "horizen_rune", "quasar_rune");
    public ManaHatch hatch;
    @Persisted
    public final NotifiableItemStackHandler machineStorage;
    @Nullable
    protected TickableSubscription TickSubs;

    public NicollDysonBeams(IMachineBlockEntity holder) {
        super(holder);
        this.machineStorage = createMachineStorage((byte) 64);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch(); // 获取舱室
        if (this.hatch == null) onStructureInvalid(); // 获取不到就别成型
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTick));
        }
        var tier = getTier();// 获取等级
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        if (TickSubs != null) {
            TickSubs.unsubscribe();
            TickSubs = null;
        }
    }

    public ManaHatch getHatch() {
        for (IMultiPart part : this.getParts()) {
            if (part instanceof ManaHatch hatchs) {
                return hatchs;
            }
        }
        return null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (TickSubs != null) {
            TickSubs.unsubscribe();
            TickSubs = null;
        }
    }

    public void updateTick() {
        TickSubs = subscribeServerTick(TickSubs, this::tick);
    }

    public void tick() {
        if (this.getOffsetTimer() % 20 == 0 && this.hatch != null) {
            if (hatch.getMana() > 100000 && this.mana < max_mana) {
                var consume = Math.min(hatch.getMana(), 100000 * mana_parallel);
                if (hatch.consumeManaIfEnough(consume)) {
                    mana += (1 + 0.05 * horizen_power) * consume;
                    mana = Math.min(mana, max_mana);
                }
            }
        }
    }

    public int caculate() {
        return 0;
    }

    public double consume_twist() {
        if (twist_power <= 3) return 0;
        else {
            return Math.max((twist_power - 3) / 3, 1) * 0.01 + (Math.max(starlight_power - twist_power, 0) * 0.01) +
                    (Math.max((100 - mana / 100000) * 0.0005, 0));
        }
    }

    public double consume_starlight() {
        if (starlight_power <= 3) return 0;
        else {
            return Math.max((starlight_power - 3) / 3, 1) * 0.01 + (Math.max(twist_power - starlight_power, 0) * 0.01) +
                    (mana / 100000 * 0.0005);
        }
    }

    public boolean check_overload() {
        var tier = getTier();
        var crash_power = -((twist_power / 3) + ((mana / 100000) * (Math.max(twist_power / (horizen_power + 1), 1)))) +
                starlight_power * 4 + 5 + tier;
        if (crash_power < 0) {
            return true;
        }
        if (quasar_power > 0) {
            return true;
        }
        return false;
    }

    public void consumeItem(int i) {
        machineStorage.extractItem(i, 1, false);
    }

    public void rune_consume() {
        var random = Math.random();
        for (int i = 0; i < 4; i++) {
            if (machineStorage.getStackInSlot(i).getItem().equals(CMItems.TWIST_RUNE.get())) {
                if (random <= consume_twist()) consumeItem(i);
            } else if (machineStorage.getStackInSlot(i).getItem().equals(CMItems.STARLIGHT_RUNE.get())) {
                if (random <= consume_starlight()) consumeItem(i);
            } else if (machineStorage.getStackInSlot(i).getItem().equals(CMItems.QUASAR_RUNE.get())) {
                if (random <= 0.00001) consumeItem(i);
            } else if (machineStorage.getStackInSlot(i).getItem().equals(CMItems.HORIZEN_RUNE.get())) {
                if (random <= 0.0025 * (horizen_power)) consumeItem(i);
            }
        }
    }

    @Override
    public boolean onWorking() {
        int timer = (int) ((int) 20 * (1 - Math.min(0.01 * twist_power, 0.9)));
        if (getOffsetTimer() % Math.max(timer, 5) == 0) {
            var tier = getTier();
            if (check_overload()) {
                overload += 1;
                if (overload >= overload_crash / 2) {
                    broken = 1;
                }
            } else {
                if (broken <= overload_crash / 2) {
                    overload -= 1;
                    overload = Math.max(overload, 0);
                }
            }
            if (overload >= overload_crash) {
                doExplosion(100f);
                return false;
            }
        }
        return super.onWorking();
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        if (broken == 2) {
            doExplosion(100000f);
            return failureManaLang_BeamCrash.translate();
        }
        if (mana >= recipe.data.getInt("required_mana")) {
            max_mana = 1000000 * (1 + 0.125 * horizen_power);
            rune_consume();
            mana -= recipe.data.getInt("mana");
            return null;
        }
        return failureManaLang_NoEnoughMana.translate();
    }

    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, @NotNull GTRecipe recipe) {
        int pa = 1;
        if (machine instanceof IMultiController controller) {
            if (controller.isFormed()) {
                int parallels = (Integer) controller.getParallelHatch()
                        .map(IParallelHatch::getCurrentParallel)
                        .orElse(0);
                if (parallels > 0) {
                    pa = parallels;
                }

            }
        }
        if (machine instanceof NicollDysonBeams xmachine) {
            var tier = xmachine.getTier();
            xmachine.mana_parallel = pa;
            if (xmachine.quasar_power > 0) {
                recipe.multiplyDuration(1 - Math.min(0.01 * xmachine.twist_power, 0.9));
                CTNHManaUtils.multiplyInputs(recipe, 10);
                recipe.multiplyOutputs(10);
                recipe.multiplyEUt((1 - 0.01 * xmachine.starlight_power) * 10);
                return null;
            }
            recipe.multiplyDuration(1 - Math.min(0.01 * xmachine.twist_power, 0.9));
            recipe.multiplyEUt(1 - 0.01 * xmachine.starlight_power);
            return null;
        }
        return null;
    }

    @Override
    public @NotNull Widget createUIWidget() {
        var widget = super.createUIWidget();
        if (widget instanceof WidgetGroup group) {
            var size = group.getSize();
            for (int i = 0; i < SLOT_COUNT; i++) {
                group.addWidget(
                        new SlotWidget(machineStorage.storage, i, size.width - 30 - 18 * i, size.height - 30, true,
                                true)
                                .setBackground(GuiTextures.SLOT));
            }
        }

        return widget;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        if (overload > 0 && overload < overload_crash / 2) {
            textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.overload"));
            textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.overload_1",
                    FormattingUtil.formatNumbers(overload), overload_crash));
        }
        if (overload_crash / 2 <= overload) {
            textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.overload_2"));
            textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.overload_1",
                    FormattingUtil.formatNumbers(overload), overload_crash));
        }
        if (broken > 0) textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.crash"));
        textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.max_mana",
                String.format("%.4f", max_mana / 1000000)));
        textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.mana",
                String.format("%.4f", mana / 1000000)));
        textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.twist_consumption",
                String.format("%.2f", consume_twist())));
        textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.stable",
                String.format("%.2f",
                        -((twist_power / 3) + ((mana / 100000) * (Math.max(twist_power / (horizen_power + 1), 1)))) +
                                starlight_power * 4 + 5 + tier)));
        textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.starlight_consumption",
                String.format("%.2f", consume_starlight())));
        textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.time",
                String.format("%.2f", 1 - Math.min(0.01 * twist_power, 0.9))));
        textList.add(Component.translatable("ctnh.multiblock.nicoll_dyson_beams.info.eut_consumption",
                String.format("%.2f", Math.max(1 - 0.003 * starlight_power, 0.25))));
    }

    @Override
    public boolean keepSubscribing() {
        return true;
    }

    public List<ItemStack> getMachineStorageItem() {
        var ItemList = new ArrayList<ItemStack>();
        for (int i = 0; i < 4; i++) {
            ItemList.add(machineStorage.getStackInSlot(i));
        }
        return ItemList;
    }

    public void updateMachineCount(List<ItemStack> itemlist) {
        twist_power = 0;
        starlight_power = 0;
        horizen_power = 0;
        quasar_power = 0;
        for (ItemStack itemStack : itemlist) {
            switch (itemStack.getItem().toString()) {
                case "twist_rune" -> twist_power = twist_power + itemStack.getCount();
                case "starlight_rune" -> starlight_power = starlight_power + itemStack.getCount();
                case "horizen_rune" -> horizen_power = horizen_power + itemStack.getCount();
                case "quasar_rune" -> quasar_power = itemStack.getCount();
            }
        }
    }

    protected NotifiableItemStackHandler createMachineStorage(byte value) {
        return new NotifiableItemStackHandler(
                this, 5, IO.NONE, IO.BOTH, slots -> new CustomItemStackHandler(SLOT_COUNT) {

                    @Override
                    public int getSlotLimit(int slot) {
                        return value;
                    }

                    @Override
                    public void onContentsChanged(int slot) {
                        var Machine = getMachineStorageItem();
                        updateMachineCount(Machine);
                        super.onContentsChanged(slot);
                    }
                }).setFilter(itemStack -> AvailableRune.contains(itemStack.getItem().toString()));
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        if (!forDrop) {
            tag.putDouble(MAX_MANA, max_mana);
            tag.putDouble(MANA, mana);
            tag.putInt(OVERLOAD, overload);
        }
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        max_mana = tag.contains(MAX_MANA) ? tag.getDouble(MAX_MANA) : 0;
        mana = tag.contains(MANA) ? tag.getDouble(MANA) : 0;
        overload = tag.contains(OVERLOAD) ? tag.getInt(OVERLOAD) : 0;
    }

    @CN("END COLOR :XD")
    @EN("END COLOR :XD")
    public static Lang failureManaLang_BeamCrash;
}
