package com.moguang.ctnhmana.common.Mutiblock.parts.ManaHatches;

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
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.api.blockentity.IManaMachineBlockEntity;
import com.moguang.ctnhmana.common.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import com.moguang.ctnhmana.registry.CMMaterials;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BandOfManaItem;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.ItemBloodOrb;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleSupplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BloodManaHatch extends ManaHatch implements IDistinctPart, IMachineModifyDrops{
    @Getter
    @Persisted
    private final NotifiableItemStackHandler blood_inventory;
    @Getter
    @Persisted
    private final NotifiableItemStackHandler soul_inventory=createMachineStorage();
    @Getter
    @Persisted
    private final NotifiableFluidTank fluidTank;
    //    @Persisted
//    private Level level;
    @Persisted
    public long BT_Max_Mana;
    @Persisted
    public long Max_LP;
    @Persisted
    public long Max_Mana_Power;
    @Persisted
    public long Max_Fluid_Mana;
    @Getter
    @Persisted
    public long LP=0L;
    @Setter
    @Getter
    @Persisted
    public long Mana_Power=0L;
    @Persisted
    protected final IO io=IO.IN;
    @Persisted
    private int MANA_TO_POWER_RATE=20; //默认值为20
    private ISubscription ManaSubs = null;
    @Persisted
    private int LP_CONVERT_SPEED=100000;
    @Persisted
    private int BTMANA_CONVERT_SPEED=100;
    @Persisted
    private int FLUIDMANA_CONVERT_SPEED=100;
    @Persisted
    private int Blood_Mana;
    @Persisted
    public int Soul_Mana=0;
    @Persisted
    public int Max_Soul_Mana;
    @Nullable
    protected TickableSubscription ConvertSubs;
    @Persisted
    private int timer=0;

    //Holder初始化 持久化

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(BloodManaHatch.class,
            MultiblockPartMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    //宝珠链接
    private SoulNetwork SoulNet;
    @Persisted
    private int LP_TO_POWER_RATE=10; //默认值为10转1
    @Persisted
    private boolean HAVE_ORB=false;
    @Persisted
    private double MAX_BLOOD_CONVERT_RATE;
    public BloodManaHatch(IMachineBlockEntity holder, long max_Mana, long max_LP, double MAX_BLOOD_CONVERT_RATE, int capacity,int max_blood,int max_soul_mana) {
        super(holder,1000000,1000000,0,0,320000);
        fluidTank= new NotifiableFluidTank(this,1,capacity,IO.NONE,IO.BOTH);
        blood_inventory = new NotifiableItemStackHandler(this, 1, IO.NONE,IO.BOTH);
        this.MAX_BLOOD_CONVERT_RATE=MAX_BLOOD_CONVERT_RATE;
        this.Blood_Mana=max_blood;
        this.Max_Soul_Mana=max_soul_mana;
    }
    public DoubleSupplier get_MP = () ->(double)this.Mana_Power/Max_Mana_Power;

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

    protected NotifiableItemStackHandler createMachineStorage() {
        return new NotifiableItemStackHandler(
                this, 1, IO.NONE, IO.BOTH, slots -> new CustomItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
            }
        }).setFilter(itemStack -> itemStack.getItem() instanceof ItemBloodOrb);
    }

    @Override
    public Widget createUIWidget() {
        var group = new DraggableScrollableWidgetGroup(0, 0, 176, 124);
        var container = new WidgetGroup(176/2-13, 124/2-26, 26, 26);
        var container2=new WidgetGroup(176/2-13, 124/2+26, 26, 26);
        var speed_progress2=(new ProgressWidget(this.get_MP, 176-4-5-18, 124/2-26, 24, 80, new ProgressTexture(CMGuiTextures.PROGRESS_BAR_MANA_EMPTY,CMGuiTextures.PROGRESS_BAR_MANA_FULL).setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
        ).setDynamicHoverTips(mana->{
            return "当前魔力值:%d".formatted((int)(mana*Max_Mana_Power));
        }));
        int index = 0;
        container.addWidgets(
                new SlotWidget(getBlood_inventory().storage, index++, 4, 4, true, io.support(IO.IN))
                        .setBackgroundTexture(GuiTextures.SLOT)
                        .setIngredientIO(IngredientIO.INPUT));
        container.addWidgets(
                new SlotWidget(getSoul_inventory().storage, index++, 4, 4, true, io.support(IO.IN))
                        .setBackgroundTexture(GuiTextures.SLOT)
                        .setIngredientIO(IngredientIO.INPUT));
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(speed_progress2);
        group.addWidget(container);
        return group;
    }
    //////////////////////////////////////
    // ********   Subscriptions&Ticks  ********//
    //////////////////////////////////////
    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            ((IManaMachineBlockEntity) this.holder).setMaxMana(BT_Max_Mana);
            onInventoryChanged();
            ManaSubs= blood_inventory.addChangedListener(this::onInventoryChanged);
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
    @Override
    public void ConvertMana()
    {
        timer+=1;
        if(timer>=20&&Soul_Mana<Max_Soul_Mana)
        {
            var willChunk1 = WorldDemonWillHandler.getWillChunk(Objects.requireNonNull(getLevel()),getPos());
            var will=willChunk1.getCurrentWill().getWill(EnumDemonWillType.valueOf("default"));
        }
        if(Mana_Power<Max_Mana_Power) {
            if (this.SoulNet != null) {
                var consume = SoulNet.getCurrentEssence();
                if (consume > LP_CONVERT_SPEED) {
                    SoulNet.add(new SoulTicket(-LP_CONVERT_SPEED), 100000000);
                    Mana_Power = Math.min(Max_Mana_Power, Mana_Power + LP_CONVERT_SPEED / LP_TO_POWER_RATE);
                } else {
                    SoulNet.setCurrentEssence(0);
                    Mana_Power = Math.min(Max_Mana_Power, Mana_Power + consume / LP_TO_POWER_RATE);
                }
            }
            if(!fluidTank.isEmpty()&&fluidTank.getFluidInTank(0).containsFluid(new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(),1000))) {
                var consume = Math.min(fluidTank.getFluidInTank(0).getAmount(), (long)(fluidTank.getFluidInTank(0).getAmount()*0.001));
                Mana_Power = Math.min(Max_Mana_Power, (long)(consume/MAX_BLOOD_CONVERT_RATE)+Mana_Power);
                fluidTank.getFluidInTank(0).setAmount((int) (fluidTank.getFluidInTank(0).getAmount()-consume));
            }
        }
    }
    @Override
    public void onInventoryChanged()
    {
        if(!blood_inventory.isEmpty())
        {
            var item=blood_inventory.getStackInSlot(0);
            if(item.getItem() instanceof ItemBloodOrb&&((ItemBloodOrb)item.getItem()).getBinding(item)!=null)
            {
                this.SoulNet=NetworkHelper.getSoulNetwork(((ItemBloodOrb) item.getItem()).getBinding(item));
                HAVE_ORB=true;
            }
            else setSoulNetInvalid();

        }
        else setSoulNetInvalid();
    }
    @Override
    public void setSoulNetInvalid() {
        if(this.SoulNet!=null) {
            this.SoulNet=null;
            HAVE_ORB = false;
        }
    }





}


