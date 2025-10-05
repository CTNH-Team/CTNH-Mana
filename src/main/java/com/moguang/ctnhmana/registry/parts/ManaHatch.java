package com.moguang.ctnhmana.registry.parts;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextTextureWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.api.blockentity.IManaMachineBlockEntity;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.item.ItemBloodOrb;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.DoubleSupplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ManaHatch extends MultiblockPartMachine implements IDistinctPart, IMachineModifyDrops{
    @Getter
    @Persisted
    private final NotifiableItemStackHandler inventory;
    @Getter
    @Persisted
    private final NotifiableFluidTank fluidTank;
//    @Persisted
//    private Level level;
    @Persisted
    private long BT_Max_Mana;
    @Persisted
    private long Max_LP;
    @Persisted
    private long Max_Mana_Power;
    @Persisted
    private long Max_Fluid_Mana;
    @Getter
    @Persisted
    private long BT_Mana=0L;
    @Getter
    @Persisted
    private long LP=0L;
    @Getter
    @Persisted
    private long Mana_Power=0L;
    @Persisted
    protected final IO io=IO.IN;
    @Persisted
    private int MANA_TO_POWER_RATE=20; //默认值为20
    private ISubscription ManaSubs = null;
    @Persisted
    private int LP_CONVERT_SPEED=100;
    @Persisted
    private int BTMANA_CONVERT_SPEED=100;
    //Holder初始化 持久化
    @Persisted
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ManaHatch.class,
            MultiblockPartMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    //宝珠链接
    private SoulNetwork SoulNet;
    @Persisted
    private int LP_TO_POWER_RATE=100; //默认值为100转1
    @Persisted
    private boolean HAVE_ORB=false;
    public ManaHatch(IMachineBlockEntity holder, long max_Mana, long max_LP, long Max_Fluid_Mana, long BT_Max_Mana, int capacity) {
        super(holder);
        fluidTank= new NotifiableFluidTank(this,1,capacity,IO.IN,IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.IN,IO.BOTH);
        this.Max_Mana_Power=max_Mana;
        this.BT_Max_Mana=BT_Max_Mana;
        this.Max_Fluid_Mana=Max_Fluid_Mana;
        this.Max_LP=max_LP;
        this.LP_CONVERT_SPEED=(int)(max_LP*0.01);
        this.BTMANA_CONVERT_SPEED=(int) (BT_Max_Mana*0.01);
        ((IManaMachineBlockEntity) this.holder).setMaxMana(BT_Max_Mana);
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
        var group = new WidgetGroup(0, 0, 34, 34);
        var container = new WidgetGroup(4, 4, 26, 26);
        var speed_progress2=(new ProgressWidget(this.get_MP, 20, 10, 10, 30, new ProgressTexture(CMGuiTextures.PROGRESS_BAR_MANA_EMPTY,CMGuiTextures.PROGRESS_BAR_MANA_FULL).setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
        ).setDynamicHoverTips(mana->{
            return "当前魔力值:%d".formatted((int)(mana*Max_Mana_Power));
        }));
        int index = 0;
        container.addWidgets(
                new SlotWidget(getInventory().storage, index++, 4, 4, true, io.support(IO.IN))
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
    }

    public void updateManaPower()
    {
        if(this.SoulNet!=null)
        {
            var consume=SoulNet.getCurrentEssence();
            if(consume>100000)
            {
                SoulNet.add(new SoulTicket(-100000),100000000);
                Mana_Power=Math.min(Max_Mana_Power,Mana_Power+100000/LP_TO_POWER_RATE);
            }
            else
            {
                SoulNet.setCurrentEssence(0);
                Mana_Power=Math.min(Max_Mana_Power,Mana_Power+consume/LP_TO_POWER_RATE);
            }
        }
        if(Mana_Power<Max_Mana_Power&&((IManaMachineBlockEntity) this.holder).getCurrentMana()>0)
        {
            long consume=((IManaMachineBlockEntity) this.holder).ChangeMana(1000); //1000 mana per tick
            Mana_Power=Math.min(Max_Mana_Power,Mana_Power+consume/MANA_TO_POWER_RATE);
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
            this.SoulNet.clear();
            HAVE_ORB = false;
        }
    }
    }


