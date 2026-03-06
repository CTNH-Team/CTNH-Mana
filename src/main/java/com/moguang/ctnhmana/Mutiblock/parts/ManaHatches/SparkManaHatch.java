package com.moguang.ctnhmana.Mutiblock.parts.ManaHatches;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import com.moguang.ctnhmana.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.common.blockentity.machine.IManaMachineBlockEntity;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import com.moguang.ctnhmana.registry.CMItems;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.entity.ManaSparkEntity;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BandOfManaItem;

import java.util.List;

public class SparkManaHatch extends ManaHatch implements IDropSaveMachine {

    @Getter
    @Persisted
    private final NotifiableItemStackHandler inventory;
    @Getter
    @Persisted
    private final NotifiableFluidTank fluidTank;
    @Nullable
    protected TickableSubscription ConvertSubs;
    // Holder初始化 持久化
    private ISubscription ManaSubs = null;
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(SparkManaHatch.class,
            ManaHatch.MANAGED_FIELD_HOLDER);
    private static final double SEARCH_RANGE = 8.0D;
    public AABB searchArea;
    public List<ManaSparkEntity> sparks;
    public int sparkConvertSpeed = 15000;
    @Persisted
    public DyeColor network = DyeColor.WHITE;

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public SparkManaHatch(IMachineBlockEntity holder, long maxMana, long maxLP, int maxBTMana, int capacity,
                          int BTMANA_CONVERT_RATE, int LP_CONVERT_RATE, int FLUID_MANA_CONVERT_RATE) {
        super(holder, maxMana, maxLP, maxBTMana, capacity, BTMANA_CONVERT_RATE, LP_CONVERT_RATE,
                FLUID_MANA_CONVERT_RATE);
        fluidTank = new NotifiableFluidTank(this, 1, capacity, IO.NONE, IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
        var centerPos = this.getPos();
        this.sparkConvertSpeed = 15000;
    }

    public SparkManaHatch(IMachineBlockEntity holder, long maxMana, long maxLP, int maxBTMana, int capacity,
                          int BTMANA_CONVERT_RATE, int LP_CONVERT_RATE, int FLUID_MANA_CONVERT_RATE,
                          int sparkConvertSpeed) {
        super(holder, maxMana, maxLP, maxBTMana, capacity, BTMANA_CONVERT_RATE, LP_CONVERT_RATE,
                FLUID_MANA_CONVERT_RATE);
        fluidTank = new NotifiableFluidTank(this, 1, capacity, IO.NONE, IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
        var centerPos = this.getPos();
        this.sparkConvertSpeed = sparkConvertSpeed;
    }

    public SparkManaHatch(IMachineBlockEntity holder, long maxMana, long maxLP, int maxBTMana, int capacity) {
        super(holder, maxMana, maxLP, maxBTMana, capacity);
        fluidTank = new NotifiableFluidTank(this, 1, capacity, IO.NONE, IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
    }

    public SparkManaHatch(IMachineBlockEntity holder, long maxMana, long maxLP, int maxBTMana, int capacity,
                          int sparkConvertSpeed) {
        super(holder, maxMana, maxLP, maxBTMana, capacity);
        fluidTank = new NotifiableFluidTank(this, 1, capacity, IO.NONE, IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
        this.sparkConvertSpeed = sparkConvertSpeed;
    }

    @Override
    public void onDrops(List<ItemStack> drops) {
        // 实现 IDropSaveMachine 时，破坏时数据会写入掉落物 NBT，不再把槽位内容单独扔到地上，避免重复/丢失
        if (!saveBreak()) {
            clearInventory(getInventory().storage);
        }
    }

    @Override
    public Widget createUIWidget() {
        var group = new DraggableScrollableWidgetGroup(0, 0, 176, 124);
        var container = new WidgetGroup(176 / 2 - 13, 124 / 2 - 26, 26, 26);
        var speed_progress2 = (new ProgressWidget(this.get_MP, 176 - 4 - 5 - 18, 124 / 2 - 52, 24, 112,
                new ProgressTexture(CMGuiTextures.PROGRESS_BAR_BT_MANA_HATCH_EMPTY,
                        CMGuiTextures.PROGRESS_BAR_BT_MANA_HATCH_DYNAMIC)
                        .setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP))
                .setDynamicHoverTips(mana -> {
                    return "当前魔力值:%d".formatted((int) (mana * maxMana));
                }));
        int index = 0;
        container.addWidgets(
                new SlotWidget(this.getInventory().storage, index++, 4, 4, true, io.support(IO.IN))
                        .setBackgroundTexture(CMGuiTextures.SLOT_RING)
                        .setIngredientIO(IngredientIO.INPUT));
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidgets(speed_progress2);
        group.addWidget(container);
        return group;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        var centerPos = this.getPos();
        this.searchArea = new AABB(
                centerPos.getX() - SEARCH_RANGE,
                centerPos.getY() - SEARCH_RANGE,
                centerPos.getZ() - SEARCH_RANGE,
                centerPos.getX() + SEARCH_RANGE + 1,
                centerPos.getY() + SEARCH_RANGE + 1,
                centerPos.getZ() + SEARCH_RANGE + 1);
        if (getLevel() instanceof ServerLevel serverLevel) {
            searchSpark();
            ((IManaMachineBlockEntity) this.holder).setMaxMana(maxBTMana);
            onInventoryChanged();
            ManaSubs = inventory.addChangedListener(this::onInventoryChanged);
            serverLevel.getServer().tell(new TickTask(0, this::updateManaPower));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (ManaSubs != null) {
            ManaSubs.unsubscribe();
        }
        if (ConvertSubs != null) {
            ConvertSubs.unsubscribe();
            ConvertSubs = null;
        }
    }

    private void updateManaPower() {
        ConvertSubs = subscribeServerTick(ConvertSubs, this::ConvertMana);
    }

    @Override
    public void ConvertMana() {
        if (getOffsetTimer() % 20 == 0) ConvertSparkMana();
        if (getOffsetTimer() % 200 == 0) searchSpark();
        TransferRingMana();
        if (Mana < maxMana) {
            ConvertBTMana();
            ConvertFluidMana();

        }
    }

    public void searchSpark() {
        var level = this.getLevel();
        sparks = level.getEntitiesOfClass(ManaSparkEntity.class, searchArea);
    }

    public void ConvertSparkMana() {
        var current_btmana = ((IManaMachineBlockEntity) this.holder).getCurrentMana();
        if (current_btmana >= this.maxBTMana || sparks == null) return;
        for (ManaSparkEntity spark : sparks) {
            if (spark.isAlive() && spark.getNetwork().equals(this.network)) {
                var receiver = spark.getAttachedManaReceiver();
                if (receiver != null) {
                    var consume = Math.min(Math.min(sparkConvertSpeed, maxBTMana - current_btmana),
                            receiver.getCurrentMana());
                    receiver.receiveMana(-consume);
                    ((IManaMachineBlockEntity) this.holder).receiveMana(consume);
                }
            }
        }
    }

    @Override
    public void TransferRingMana() {
        if (!((IManaMachineBlockEntity) this.holder).isFull() && !inventory.isEmpty()) {
            // 把魔力戒指里的魔力转化为植物魔法魔力
            // 每tick转化容量的1%魔力
            var item = inventory.getStackInSlot(0);
            if (item.getItem() instanceof BandOfManaItem ManaRing) {
                var p = ItemNBTHelper.getInt(item, "mana", 0);
                if (p >= 20) {
                    int consume = (int) Math.min(((IManaMachineBlockEntity) this.holder).getMaxBTMana() * 0.01, p);
                    // Mana = Math.min(maxMana, consume / MANA_TO_POWER_RATE + Mana);
                    ((IManaMachineBlockEntity) this.holder).receiveMana(consume);
                    ItemNBTHelper.setInt(item, "mana", p - (int) consume);
                }
            }
            if (item.getItem().equals(CMItems.UNIMBUED_SPIRIT.get()) && this.Mana >= 10000 * item.getCount()) {
                this.Mana -= 10000 * item.getCount();
                inventory.setStackInSlot(0, new ItemStack(CMItems.ORICHALCOS_SPIRIT.get(), item.getCount()));
            }
        }
    }
}
