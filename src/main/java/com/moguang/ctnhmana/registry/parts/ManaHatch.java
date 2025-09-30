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
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.api.blockentity.IManaMachineBlockEntity;
import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

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
    //Holder初始化
    @Persisted
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ManaHatch.class,
            MultiblockPartMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    public int ids=-1;
    public ManaHatch(IMachineBlockEntity holder, long max_Mana, long max_LP, long Max_Fluid_Mana, long BT_Max_Mana, int capacity) {
        super(holder);

        fluidTank= new NotifiableFluidTank(this,1,capacity,IO.BOTH);
        inventory = new NotifiableItemStackHandler(this, 1, IO.IN);
        this.Max_Mana_Power=max_Mana;
        this.BT_Max_Mana=BT_Max_Mana;
        this.Max_Fluid_Mana=Max_Fluid_Mana;
        this.Max_LP=max_LP;
    }
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
        int index = 0;
        container.addWidget(
                new SlotWidget(getInventory().storage, index++, 4, 4, true, io.support(IO.IN))
                        .setBackgroundTexture(GuiTextures.SLOT)
                        .setIngredientIO(IngredientIO.INPUT));
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(container);
        return group;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    //魔力接受单位
//    @Override
//    public Level getManaReceiverLevel() {
//        return this.getLevel();
//    }
//
//    @Override
//    public BlockPos getManaReceiverPos() {
//        return this.getPos();
//    }
//
//    @Override
//    public int getCurrentMana() {
//        return (int)BT_Mana;
//    }
//
//    @Override
//    public boolean isFull() {
//        return BT_Mana>=BT_Max_Mana;
//    }
//
//    @Override
//    public void receiveMana(int i) {
//        BT_Mana+=i;
//        BT_Mana=Math.min(BT_Mana,BT_Max_Mana);
//    }

//    @Override
//    public boolean canReceiveManaFromBursts() {
//        return true;
//    }
    @Override
    public void onLoad() {
        super.onLoad();

        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateManaPower));
        }
    }
    public void updateManaPower()
    {
        if(Mana_Power<Max_Mana_Power)
        {
            Mana_Power=Math.min(Max_Mana_Power,Mana_Power+BT_Mana/MANA_TO_POWER_RATE);
            BT_Mana-=Math.min(BT_Mana,(Max_Mana_Power-Mana_Power)*MANA_TO_POWER_RATE);
        }
    }
//    @Override
//    public Widget createUIWidget() {
//        super.createUIWidget();
//        var group = new WidgetGroup(0, 0, 34, 34);
//        var container = new WidgetGroup(4, 4, 26, 26);
//        var label=(new LabelWidget(-32, 30, Component.translatable("ctnh.compiler.noid")));
//        if(ids!=-1) {
//            label = (new LabelWidget(-32, 30, Component.translatable("ctnh.compiler.id", String.format("%d", ids))));
//        }
//        else
//        {
//            label = (new LabelWidget(-32, 30, Component.translatable("ctnh.compiler.noid")));
//        }
//        int index = 0;
//        container.addWidget(
//                new SlotWidget(getInventory().storage, index++, 4, 4, true, io.support(IO.IN))
//                        .setBackgroundTexture(GuiTextures.SLOT)
//                        .setIngredientIO(IngredientIO.INPUT));
//
//        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
//        group.addWidget(container);
//        group.addWidget(label);
//
//        return group;
//    }
}
