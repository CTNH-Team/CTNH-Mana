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
import com.moguang.ctnhmana.registry.CMGuiTextures;
import com.moguang.ctnhmana.registry.CMMaterials;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BandOfManaItem;
import wayoftime.bloodmagic.common.item.ItemBloodOrb;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.DoubleSupplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BloodManaHatch extends MultiblockPartMachine implements IDistinctPart, IMachineModifyDrops{
    @Getter
    @Persisted
    private final NotifiableItemStackHandler inventory;
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
    private int Blood_Mana=1000000;
    @Persisted
    private int Soul_Mana=100000;
    @Nullable
    protected TickableSubscription ConvertSubs;

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
    public BloodManaHatch(IMachineBlockEntity holder, long max_Mana, long max_LP, long Max_Fluid_Mana, int capacity) {
        super(holder);
        fluidTank= new NotifiableFluidTank(this,1,capacity,IO.NONE,IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE,IO.BOTH);
        this.Max_Mana_Power=max_Mana;
        this.BT_Max_Mana=BT_Max_Mana;
        this.Max_Fluid_Mana=Max_Fluid_Mana;
        this.Max_LP=max_LP;
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
                new SlotWidget(getInventory().storage, index++, 4, 4, true, io.support(IO.IN))
                        .setBackgroundTexture(GuiTextures.SLOT)
                        .setIngredientIO(IngredientIO.INPUT));
        container.addWidgets(
                new SlotWidget(getInventory().storage, index++, 4, 4, true, io.support(IO.IN))
                        .setBackgroundTexture(GuiTextures.SLOT)
                        .setIngredientIO(IngredientIO.INPUT));
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(speed_progress2);
        group.addWidget(container);
        return group;
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



    //////////////////////////////////////
    // ********   Subscriptions&Ticks  ********//
    //////////////////////////////////////
    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            onInventoryChanged();
            ManaSubs= inventory.addChangedListener(this::onInventoryChanged);
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

    public void updateManaPower()
    {
        ConvertSubs = subscribeServerTick(ConvertSubs, this::ConvertMana);
    }
    public void ConvertMana()
    {
        if(this.SoulNet!=null)
        {
            var consume=(int)(SoulNet.getCurrentEssence()*0.01);
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
                return;
            }
            if(!fluidTank.isEmpty()&&fluidTank.getFluidInTank(0).containsFluid(CMMaterials.Mana.getFluid(1))) {
                var consume = Math.min(fluidTank.getFluidInTank(0).getAmount(), Max_Fluid_Mana);
                Mana_Power = Math.min(Max_Mana_Power, consume+Mana_Power/5);
                fluidTank.getFluidInTank(0).setAmount((int) (fluidTank.getFluidInTank(0).getAmount()-consume));
            }

        }
    }
    public void onInventoryChanged()
    {
        if(!inventory.isEmpty())
        {
            var item=inventory.getStackInSlot(0);
            if(item.getItem() instanceof ItemBloodOrb&&((ItemBloodOrb)item.getItem()).getBinding(item)!=null)
            {
                this.SoulNet=NetworkHelper.getSoulNetwork(((ItemBloodOrb) item.getItem()).getBinding(item));
                HAVE_ORB=true;
            }
            else setSoulNetInvalid();

        }
        else setSoulNetInvalid();
    }
    public void setSoulNetInvalid() {
        if(this.SoulNet!=null) {
            this.SoulNet=null;
            HAVE_ORB = false;
        }
    }


}


