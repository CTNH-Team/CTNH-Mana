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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BandOfManaItem;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.ItemBloodOrb;
import wayoftime.bloodmagic.common.item.soul.ItemSoulGem;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.demonaura.WillChunk;
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
    private final NotifiableItemStackHandler soul_inventory;
    @Getter
    @Persisted
    private final NotifiableFluidTank fluidTank;
    //    @Persisted
//    private Level level;;
    @Persisted
    public long maxMana;
    @Getter
    @Persisted
    public long LP=0L;
    @Persisted
    protected final IO io=IO.IN;
    @Persisted
    private int MANA_TO_POWER_RATE=20; //默认值为20
    private ISubscription ManaSubs = null;
    @Persisted
    private double FLUID_LP_CONVERT_SPEED=0.001;
    @Persisted
    private int Blood_Mana;
    @Persisted
    public int DemonWill=0;
    @Persisted
    public int maxDemonWill=100;
    @Nullable
    protected TickableSubscription ConvertSubs;
    @Persisted
    private int timer=0;
    @Persisted
    double rawWill = 0;
    @Persisted
    double steadfastWill = 0;
    @Persisted
    double corrosiveWill=0;
    @Persisted
    double destructiveWill = 0;
    @Persisted
    double vengefulWill = 0;
    WillChunk willChunk=null;
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
    public BloodManaHatch(IMachineBlockEntity holder, long max_Mana, long max_LP, int LP_CONVERT_RATE, int capacity,int maxDemonWill,double FLUID_LP_CONVERT_SPEED) {
        super(holder,max_Mana,max_LP,0,0,capacity);
        fluidTank= new NotifiableFluidTank(this,1,capacity,IO.NONE,IO.BOTH);
        blood_inventory =createMachineStorageOrb();
        soul_inventory=createMachineStorageGem();
        this.LP_CONVERT_RATE=LP_CONVERT_RATE;
        this.maxDemonWill=maxDemonWill;
        this.FLUID_LP_CONVERT_SPEED=FLUID_LP_CONVERT_SPEED;
    }
    public DoubleSupplier get_MP = () ->(double)this.Mana/maxMana;

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

    protected NotifiableItemStackHandler createMachineStorageGem() {
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
        }).setFilter(itemStack -> itemStack.getItem() instanceof ItemSoulGem);
    }
    protected NotifiableItemStackHandler createMachineStorageOrb() {
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
        var speed_progress2=(new ProgressWidget(this.get_MP, 176-4-5-18, 124/2-26, 24, 80, new ProgressTexture(CMGuiTextures.PROGRESS_BAR_MANA_HATCH_EMPTY,CMGuiTextures.PROGRESS_BAR_MANA_HATCH_DYNAMIC).setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
        ).setDynamicHoverTips(mana->{
            return "当前魔力值:%d".formatted((int)(mana*maxMana));
        }));
        int index = 0;
        container.addWidgets(
                new SlotWidget(getBlood_inventory().storage, index++, 4, 4, true, io.support(IO.IN))
                        .setBackgroundTexture(GuiTextures.SLOT)
                        .setIngredientIO(IngredientIO.INPUT));
        index=0;
        container.addWidgets(
                new SlotWidget(getSoul_inventory().storage, index++, 4, 20, true, io.support(IO.IN))
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
        this.willChunk = WorldDemonWillHandler.getWillChunk(Objects.requireNonNull(getLevel()),getPos());
        if (getLevel() instanceof ServerLevel serverLevel) {
            ((IManaMachineBlockEntity) this.holder).setMaxMana(maxBTMana);
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
    private void updateManaPower()
    {
        ConvertSubs = subscribeServerTick(ConvertSubs, this::ConvertMana);
    }
    @Override
    public void ConvertMana()
    {
        if(getOffsetTimer()%20==0&&DemonWill<maxDemonWill)
        {
            ConvertGemsWill();
            if(willChunk!=null) {
                for (EnumDemonWillType type1 : EnumDemonWillType.values()) {
                    ConvertWill(type1);
                }
            }
        }
        if(Mana<maxMana) {
            ConvertLP();
            ConvertFluidLP();
        }
    }
    public void ConvertGemsWill()
    {
        if(!soul_inventory.isEmpty())
        {
            var item=getSoul_inventory().getStackInSlot(0);
            if(item.getItem() instanceof ItemSoulGem gem &&gem.getWill(gem.getCurrentType(item),item)>0)
            {
                var type=gem.getCurrentType(item);
                var current_will=gem.getWill(type,item);
                double consume=0;
                switch (type)
                {
                    case DEFAULT ->
                    {
                        consume=Math.min(current_will,maxDemonWill-rawWill);
                        rawWill+=consume;

                    }
                    case CORROSIVE ->
                    {
                        consume=Math.min(current_will,maxDemonWill-corrosiveWill);
                        corrosiveWill+=consume;
                    }
                    case DESTRUCTIVE ->
                    {
                        consume=Math.min(current_will,maxDemonWill-destructiveWill);
                        destructiveWill+=consume;
                    }
                    case VENGEFUL ->
                    {
                        consume=Math.min(current_will,maxDemonWill-vengefulWill);
                        vengefulWill+=consume;
                    }
                    case STEADFAST ->
                    {
                        consume=Math.min(current_will,maxDemonWill-steadfastWill);
                        steadfastWill+=consume;
                    }
                }
                gem.drainWill(type,item,consume,true);

            }
        }
    }
    public void ConvertFluidLP()
    {
        if(!fluidTank.isEmpty()&&fluidTank.getFluidInTank(0).containsFluid(new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(),(int)(1/FLUID_LP_CONVERT_SPEED)))) {
            var consume = Math.min(fluidTank.getFluidInTank(0).getAmount(), (long)(fluidTank.getFluidInTank(0).getAmount()*FLUID_LP_CONVERT_SPEED));
            Mana = Math.min(maxMana, (long)(consume/LP_CONVERT_RATE)+Mana);
            fluidTank.getFluidInTank(0).setAmount((int) (fluidTank.getFluidInTank(0).getAmount()-consume));
        }
    }
    public void ConvertWill(EnumDemonWillType TYPE)
    {
        switch (TYPE)
        {
            case DEFAULT ->
            {
                var will = willChunk.getCurrentWill().getWill(TYPE);
                var consume=Math.min(will,maxDemonWill-rawWill);
                rawWill+=consume;
                WorldDemonWillHandler.drainWill(this.getLevel(), this.getPos(), TYPE, consume, true);
            }
            case CORROSIVE ->
            {
                var will = willChunk.getCurrentWill().getWill(TYPE);
                var consume=Math.min(will,maxDemonWill-corrosiveWill);
                corrosiveWill+=consume;
                WorldDemonWillHandler.drainWill(this.getLevel(), this.getPos(), TYPE, consume, true);
            }
            case DESTRUCTIVE ->
            {
                var will = willChunk.getCurrentWill().getWill(TYPE);
                var consume=Math.min(will,maxDemonWill-destructiveWill);
                destructiveWill+=consume;
                WorldDemonWillHandler.drainWill(this.getLevel(), this.getPos(), TYPE, consume, true);
            }
            case VENGEFUL ->
            {
                var will = willChunk.getCurrentWill().getWill(TYPE);
                var consume=Math.min(will,maxDemonWill-vengefulWill);
                vengefulWill+=consume;
                WorldDemonWillHandler.drainWill(this.getLevel(), this.getPos(), TYPE, consume, true);
            }
            case STEADFAST ->
            {
                var will = willChunk.getCurrentWill().getWill(TYPE);
                var consume=Math.min(will,maxDemonWill-steadfastWill);
                steadfastWill+=consume;
                WorldDemonWillHandler.drainWill(this.getLevel(), this.getPos(), TYPE, consume, true);
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


