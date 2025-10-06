package com.moguang.ctnhmana.common.Mutiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.common.ManaMachine;
import com.moguang.ctnhmana.common.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.common.gui.BaseManaMachineGui;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseManaMachine extends ManaMachine {
    @Persisted
    public final NotifiableItemStackHandler machineStorage;
    @Persisted public ManaHatch hatch;
    @Persisted private boolean isManaConsumedInstantly=false;
    @Persisted protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            BaseManaMachine.class, ManaMachine.MANAGED_FIELD_HOLDER);
    @Persisted public int consumption;
    @Persisted private int baseconsumption;
    public BaseManaMachine(IMachineBlockEntity holder, int consumption) {
        super(holder);
        this.machineStorage = createMachineStorage();
        this.baseconsumption=consumption;
    }
    //////////////////////////////////////
    // ********   HatchVision  ********//
    //////////////////////////////////////
    ///
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch=getHatch();
        if(this.hatch==null)onStructureInvalid();
        var tier = getTier();
        consumption=(int)Math.pow(2,tier)*baseconsumption;
    }
    @Override
    public void onStructureInvalid()
    {
        this.hatch=null;
    }
    public ManaHatch getHatch() {
        for (IMultiPart part : getParts())
        {
            if(part instanceof ManaHatch hatchs)
                return hatchs;
        }
        return null;
    }
    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe)
    {
        if(isManaConsumedInstantly&&hatch.getBT_Mana()>consumption*recipe.duration/20)
        {
            hatch.consumeMana(consumption*recipe.duration/20);
            return super.beforeWorking(recipe);
        }
        else
        {
            return hatch.consumeManaIfEnough(consumption)&&super.beforeWorking(recipe);
        }
    }
    @Override
    public boolean onWorking()
    {
        if(isManaConsumedInstantly)return super.onWorking();
        if(getOffsetTimer() % 20 == 0&&hatch.consumeManaIfEnough(consumption))
        {
            super.onWorking();
        }
        else getRecipeLogic().setProgress(0);
        return super.onWorking();
    }

    //////////////////////////////////////
    // ********   MachineStorage  ********//
    //////////////////////////////////////
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
                checkUpdate(this.getStackInSlot(0).getItem());
            }
        }).setFilter(itemStack ->itemStack.getTag().contains("mana_update_t1"));
    }
    public void checkUpdate(Item update)
    {
        var name=update.toString();
    }
    //////////////////////////////////////
    // ********   RecipeLogic  ********//
    //////////////////////////////////////
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (machine instanceof BaseManaMachine mmachine) {

        }
        return ModifierFunction.IDENTITY;
        }
    //////////////////////////////////////
    // ********   Update  ********//
    //////////////////////////////////////
    public class Update
    {
        public int parallel=1;
        public double speed=1;
        public double consumption=1;
        public double input=1;
        public double output=1;
        public String updateType=null;
    }
    //////////////////////////////////////
    // ********   UI  ********//
    //////////////////////////////////////
    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);

        if (this.getRecipeTypes().length > 0) {
            sideTabs.attachSubTab(new BaseManaMachineGui(this));
        }
        var directionalConfigurator = CombinedDirectionalFancyConfigurator.of(self(), self());
        if (directionalConfigurator != null)
            sideTabs.attachSubTab(directionalConfigurator);
    }
    @Override
    public @NotNull Widget createUIWidget() {
        var widget = super.createUIWidget();
        if (widget instanceof WidgetGroup group) {
            var size = group.getSize();
            for(int i = 0; i < 1 ;i++){
                group.addWidget(
                        new SlotWidget(machineStorage.storage, i, size.width - 30 - 18*i, size.height - 30, true, true)
                                .setBackground(GuiTextures.SLOT));
            }
        }

        return widget;
    }
}
