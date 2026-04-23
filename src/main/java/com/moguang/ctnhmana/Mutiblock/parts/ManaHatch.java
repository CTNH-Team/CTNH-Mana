package com.moguang.ctnhmana.Mutiblock.parts;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import com.moguang.ctnhmana.common.blockentity.machine.IManaMachineBlockEntity;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import io.github.lounode.extrabotany.common.item.relic.MasterBandOfManaItem;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BandOfManaItem;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.item.ItemBloodOrb;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import java.util.List;
import java.util.function.DoubleSupplier;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ManaHatch extends MultiblockPartMachine implements IDistinctPart, IMachineModifyDrops {

    @Getter
    @Persisted
    public NotifiableItemStackHandler inventory;
    @Getter
    @Persisted
    protected final NotifiableFluidTank fluidTank;
    // @Persisted
    // private Level level;
    @Persisted
    public int maxBTMana;
    @Persisted
    @Getter
    public long maxLP;
    @Persisted
    @Getter
    public long maxMana;
    @Persisted
    public long maxFluidMana;
    @Setter
    @Getter
    @Persisted
    public long Mana = 0L;
    @Getter
    @Persisted
    public long LP = 0L;
    @Persisted
    protected final IO io = IO.IN;
    @Persisted
    public int BTMANA_CONVERT_RATE = 20; // 默认值为20
    @Persisted
    public int LP_CONVERT_RATE = 100; // 默认值为100转1
    @Persisted
    public int FLUID_MANA_CONVERT_RATE = 1;
    protected ISubscription ManaSubs = null;
    @Persisted
    public int LP_CONVERT_SPEED = 1000;
    @Persisted
    public int BTMANA_CONVERT_SPEED = 100;
    @Persisted
    public int FLUID_MANA_CONVERT_SPEED = 100;
    @Nullable
    protected TickableSubscription ConvertSubs;
    // Holder初始化 持久化

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ManaHatch.class,
            MultiblockPartMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    // 宝珠链接
    @Nullable
    @Getter
    public SoulNetwork SoulNet;
    @Getter
    @Nullable
    public BloodOrb orb;
    @Persisted
    @Getter
    public boolean HAVE_ORB = false;
    public String bar_type;

    public ManaHatch(IMachineBlockEntity holder, long maxMana, long maxLP, int maxBTMana, int capacity,
                     int BTMANA_CONVERT_RATE, int LP_CONVERT_RATE, int FLUID_MANA_CONVERT_RATE) {
        super(holder);
        fluidTank = new NotifiableFluidTank(this, 1, capacity, IO.NONE, IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
        this.maxMana = maxMana;
        this.maxBTMana = maxBTMana;
        this.maxFluidMana = maxFluidMana;
        this.maxLP = maxLP;
        this.BTMANA_CONVERT_RATE = BTMANA_CONVERT_RATE;
        this.LP_CONVERT_RATE = LP_CONVERT_RATE;
        this.FLUID_MANA_CONVERT_RATE = FLUID_MANA_CONVERT_RATE;
        this.LP_CONVERT_SPEED = (int) (maxLP * 0.01);
        this.BTMANA_CONVERT_SPEED = (int) (maxBTMana * 0.01);
        this.FLUID_MANA_CONVERT_SPEED = (int) (capacity * 0.01);
        ((IManaMachineBlockEntity) this.holder).setMaxMana(maxBTMana);
    }

    public ManaHatch(IMachineBlockEntity holder, long maxMana, long maxLP, int maxBTMana, int capacity) {
        super(holder);
        fluidTank = new NotifiableFluidTank(this, 1, capacity, IO.NONE, IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
        this.maxMana = maxMana;
        this.maxBTMana = maxBTMana;
        this.maxLP = maxLP;
        this.LP_CONVERT_SPEED = (int) (maxLP * 0.01);
        this.BTMANA_CONVERT_SPEED = (int) (maxBTMana * 0.01);
        this.FLUID_MANA_CONVERT_SPEED = (int) (capacity * 0.01);
        ((IManaMachineBlockEntity) this.holder).setMaxMana(maxBTMana);
    }

    public ManaHatch(IMachineBlockEntity holder, long maxMana, long maxLP, int maxBTMana, int capacity,
                     String bar_type) {
        super(holder);
        fluidTank = new NotifiableFluidTank(this, 1, capacity, IO.NONE, IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
        this.maxMana = maxMana;
        this.maxBTMana = maxBTMana;
        this.maxLP = maxLP;
        this.LP_CONVERT_SPEED = (int) (maxLP * 0.01);
        this.BTMANA_CONVERT_SPEED = (int) (maxBTMana * 0.01);
        this.FLUID_MANA_CONVERT_SPEED = (int) (capacity * 0.01);
        ((IManaMachineBlockEntity) this.holder).setMaxMana(maxBTMana);
        this.bar_type = bar_type;
    }

    public DoubleSupplier get_MP = () -> (double) this.Mana / maxMana;

    @Override
    public void onDrops(List<ItemStack> drops) {
        clearInventory(getInventory().storage);
    }

    @Override
    public boolean isDistinct() {
        return getInventory().isDistinct();
    }

    @Override
    public void setDistinct(boolean isDistinct) {
        getInventory().setDistinct(isDistinct);
    }

    public ManaHatch getself() {
        return this;
    }

    @Override
    public Widget createUIWidget() {
        var group = new DraggableScrollableWidgetGroup(0, 0, 176, 124).setBackground(CMGuiTextures.BT_BACKGROUND);
        var container = new WidgetGroup(176 / 2 - 13, 124 / 2 - 26, 26, 26);
        var speed_progress2 = (new ProgressWidget(this.get_MP, 176 - 24 - 4 - 4, 6, 24, 112,
                new ProgressTexture(CMGuiTextures.PROGRESS_BAR_MANA_HATCH_EMPTY,
                        CMGuiTextures.PROGRESS_BAR_MANA_HATCH_DYNAMIC)
                        .setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP))
                .setDynamicHoverTips(mana -> {
                    return "当前魔力值:%d".formatted((int) (mana * maxMana));
                }));
        if (bar_type != null && bar_type.equals("BT"))
            speed_progress2 = (new ProgressWidget(this.get_MP, 176 - 24 - 4 - 4, 6, 24, 112,
                    new ProgressTexture(CMGuiTextures.PROGRESS_BAR_BT_MANA_HATCH_EMPTY,
                            CMGuiTextures.PROGRESS_BAR_BT_MANA_HATCH_DYNAMIC)
                            .setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP))
                    .setDynamicHoverTips(mana -> {
                        return "当前魔力值:%d".formatted((int) (mana * maxMana));
                    }));
        if (bar_type != null && bar_type.equals("BM"))
            speed_progress2 = (new ProgressWidget(this.get_MP, 176 - 24 - 4 - 4, 6, 24, 112,
                    new ProgressTexture(CMGuiTextures.PROGRESS_BAR_BM_MANA_HATCH_EMPTY,
                            CMGuiTextures.PROGRESS_BAR_BM_MANA_HATCH_DYNAMIC)
                            .setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP))
                    .setDynamicHoverTips(mana -> {
                        return "当前魔力值:%d".formatted((int) (mana * maxMana));
                    }));

        int index = 0;
        container.addWidgets(
                new SlotWidget(getInventory().storage, index++, 4, 4, true, io.support(IO.IN))
                        .setBackgroundTexture(GuiTextures.SLOT)
                        .setIngredientIO(IngredientIO.INPUT));
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidgets(speed_progress2);
        group.addWidget(container);
        return group;
    }

    //////////////////////////////////////
    // ******** Visit ********//
    //////////////////////////////////////
    public boolean isFull() {
        return Mana >= maxMana;
    }

    public boolean isEmpty() {
        return Mana == 0;
    }

    public void consumeMana(long consume) {
        Mana = Math.max(0, Mana - consume);
    }

    public boolean consumeManaIfEnough(long consume) {
        if (Mana >= consume) {
            Mana = Math.max(0, Mana - consume);
            return true;
        }
        return false;
    }

    public int getmaxBTMana() {
        return ((IManaMachineBlockEntity) this.holder).getMaxBTMana();
    }

    public int getBTMana() {
        return ((IManaMachineBlockEntity) this.holder).getCurrentMana();
    }

    //////////////////////////////////////
    // ******** Subscriptions&Ticks ********//
    //////////////////////////////////////
    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
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

    public void ConvertMana() {
        TransferRingMana();
        if (Mana < maxMana) {
            ConvertLP();
            ConvertBTMana();
            ConvertFluidMana();

        }
    }

    public void ConvertLP() {
        // 转化LP到Mana
        // 只有LP是无转化限制，会损失转化的
        if (this.SoulNet != null && SoulNet.getCurrentEssence() >= 100) {
            var consume = SoulNet.getCurrentEssence();
            if (consume > LP_CONVERT_SPEED) {
                SoulNet.add(new SoulTicket(-LP_CONVERT_SPEED), 100000000);
                Mana = Math.min(maxMana, Mana + LP_CONVERT_SPEED / LP_CONVERT_RATE);
            } else {
                SoulNet.setCurrentEssence(1);
                Mana = Math.min(maxMana, Mana + consume / LP_CONVERT_RATE);
            }
        }
    }

    public void ConvertBTMana() {
        // 转化植物魔法Mana到Mana
        if (((IManaMachineBlockEntity) this.holder).getCurrentMana() > 0) {
            long consume = ((IManaMachineBlockEntity) this.holder)
                    .sendMana(Math.min((maxMana - Mana) * BTMANA_CONVERT_RATE, BTMANA_CONVERT_SPEED));
            Mana = Math.min(maxMana, Mana + consume / BTMANA_CONVERT_RATE);
        }
    }

    public void ConvertFluidMana() {
        if (!fluidTank.isEmpty() && fluidTank.getFluidInTank(0).containsFluid(CMMaterials.Mana.getFluid(1))) {
            var consume = Math.min((maxMana - Mana) * FLUID_MANA_CONVERT_RATE,
                    Math.min(fluidTank.getFluidInTank(0).getAmount(), FLUID_MANA_CONVERT_SPEED));
            Mana = Math.min(maxMana, consume / FLUID_MANA_CONVERT_RATE + Mana);
            fluidTank.getFluidInTank(0).setAmount((int) (fluidTank.getFluidInTank(0).getAmount() - consume));
        }
    }

    public void TransferRingMana() {
        if (!this.inventory.isEmpty()) {
            var item = this.inventory.getStackInSlot(0);
            if (item.getItem().equals(CMItems.UNIMBUED_SPIRIT.get()) && this.Mana >= 10000 * item.getCount()) {
                this.Mana -= 10000 * item.getCount();
                this.inventory.setStackInSlot(0, new ItemStack(CMItems.ORICHALCOS_SPIRIT.get(), item.getCount()));
            }
        }
        if (!((IManaMachineBlockEntity) this.holder).isFull() && !inventory.isEmpty()) {
            // 把魔力戒指里的魔力转化为植物魔法魔力
            // 每tick转化容量的0.1%魔力
            var item = inventory.getStackInSlot(0);
            if (item.getItem() instanceof BandOfManaItem ManaRing) {
                var p = ItemNBTHelper.getInt(item, "mana", 0);
                if (p >= 20) {
                    int consume = (int) Math.min(((IManaMachineBlockEntity) this.holder).getMaxBTMana() * 0.001, p);
                    // Mana = Math.min(maxMana, consume / MANA_TO_POWER_RATE + Mana);
                    ((IManaMachineBlockEntity) this.holder).receiveMana(consume);
                    ItemNBTHelper.setInt(item, "mana", p - (int) consume);
                }
            }
            if (item.getItem() instanceof MasterBandOfManaItem mRing) {
                var p = ItemNBTHelper.getLong(item, "mana", 0);
                if (p >= 20) {
                    int consume = (int) Math.min(((IManaMachineBlockEntity) this.holder).getMaxBTMana() * 0.001, p);
                    // Mana = Math.min(maxMana, consume / MANA_TO_POWER_RATE + Mana);
                    ((IManaMachineBlockEntity) this.holder).receiveMana(consume);
                    ItemNBTHelper.setLong(item, "mana", p - (long) consume);
                }
            }

        }
    }

    public void onInventoryChanged() {
        if (!inventory.isEmpty()) {
            var item = inventory.getStackInSlot(0);
            if (item.getItem() instanceof ItemBloodOrb orb &&
                    ((ItemBloodOrb) item.getItem()).getBinding(item) != null) {
                this.orb = orb.getOrb(item);
                this.SoulNet = NetworkHelper.getSoulNetwork(((ItemBloodOrb) item.getItem()).getBinding(item));
                HAVE_ORB = true;
            } else setSoulNetInvalid();
        } else setSoulNetInvalid();
    }

    public void setSoulNetInvalid() {
        if (this.SoulNet != null) {
            this.SoulNet = null;
            HAVE_ORB = false;
        }
    }

    @Override
    public boolean canShared() {
        return false;
    }
}
