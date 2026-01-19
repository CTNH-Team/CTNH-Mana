package com.moguang.ctnhmana.Mutiblock;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import com.moguang.ctnhmana.item.ManaFuelStick.IManaFuelStick;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.*;

public class ArcaneHighEnergyCompressionReactorCore extends WorkableMultiblockMachine implements IFancyUIMachine,
        IDisplayUIMachine, IExplosionMachine {
    @Getter
    @Persisted
    private final NotifiableItemStackHandler inventory;
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ArcaneHighEnergyCompressionReactorCore.class,
            WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    @Getter
    @Persisted
    public long maxEU=10000000000L;
    @Getter
    @Persisted
    public long EU=0L;

    public long predicateEU=0L;
    @Persisted
    public int slot_range=2;
    @Persisted
    public long maxHeat=100;
    @Persisted
    public long heat=0;
    @Persisted
    public long stability=100;
    @Persisted
    public long maxStability=100;
    @Persisted
    public int tier=0;
    @Nullable
    protected TickableSubscription tickSubs;
    public EnergyContainerList energyContainer;
    protected ISubscription ManaSubs = null;
    public ArcaneHighEnergyCompressionReactorCore(IMachineBlockEntity holder, int slot_range) {
        super(holder);
        this.slot_range=slot_range;
        inventory=createMachineStorage(slot_range);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        updateEnergyContainer();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTickSubscription));
        }
    }
    public void updateEnergyContainer() {
        List<IEnergyContainer> containers = new ArrayList<>();
        for (IMultiPart part : getParts())
            part.self().holder.self()
                    .getCapability(GTCapability.CAPABILITY_ENERGY_CONTAINER)
                    .ifPresent(container->{
                        this.tier=Math.max(tier,GTUtil.getTierByVoltage(container.getOutputVoltage()));
                        containers.add(container);
                    });
        energyContainer = new EnergyContainerList(containers);
    }
    /// ///////////////////////////////
    /// / tick/ ////
    /// //////////////////////////
    ///
    ///
    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
        if(ManaSubs!=null)
        {
            ManaSubs.unsubscribe();
            ManaSubs=null;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        ManaSubs= inventory.addChangedListener(this::onInventoryChanged);
    }

    private void onInventoryChanged() {
        if(this.isActive())return;
        for(int i=0;i<=slot_range*slot_range-1;i++)
        {
            if(!this.inventory.getStackInSlot(i).isEmpty()&&this.inventory.getStackInSlot(0).getItem() instanceof IManaFuelStick) {
                startRecipeCycle();
                return;
            }
        }

    }

    protected void updateTickSubscription() {
        if (isFormed) {
            tickSubs = subscribeServerTick(tickSubs, this::tick);
        } else if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }
    public void tick()
    {
        if(!isFormed)return;
    }
    /// ///////////////////////////////
    /// / RecipeLogic/ ////
    /// //////////////////////////
    ///
    ///
    @Override
    public boolean onWorking() {
        if(this.getOffsetTimer()%10==0)
        {
            calculateHeat();
            if(stability<0)
            {
                doExplosion(10f);
            }
            predicateEU=calculateEU();
        }
        return super.onWorking();
    }
    @Override
    public void afterWorking()
    {
        super.afterWorking();
        EU+=calculateEU();
        EU=Math.min(EU,maxEU);
        this.heat=0;
        this.stability=maxStability;
    }
    public void startRecipeCycle()
    {
        GTRecipeType recipeType = getRecipeType();
        GTRecipe emptyRecipe = recipeType
                .recipeBuilder(GTCEu.id("empty_recipe_25s"))
                .duration(400)
                .buildRawRecipe();
        recipeLogic.setupRecipe(emptyRecipe);
    }
    public void calculateHeat()
    {
        long[][]heatmap=new long[slot_range][slot_range];
        for(int i=0;i<slot_range;i++)
        {
            for(int j=0;j<slot_range;j++)
            {
                if(!inventory.getStackInSlot(getSlotIndex(i,j)).isEmpty()) {
                    var item=inventory.getStackInSlot(getSlotIndex(i,j));
                    if (item.getItem() instanceof IManaFuelStick) heatmap[i][j]=((IManaFuelStick) item.getItem()).heat;
                    else if (item.is(BotaniaTags.Items.RUNES)) heatmap[i][j]=0;
                }
                else heatmap[i][j]=-1;
            }
        }
        heatmap=calculateRune(heatmap);
        for(int i=0;i<slot_range;i++)
        {
            for(int j=0;j<slot_range;j++)
            {
                if(heat>0) {
                    var item=inventory.getStackInSlot(getSlotIndex(i,j));
                    item.setDamageValue(item.getDamageValue()+5);
                    if(item.getDamageValue()>=item.getMaxDamage())popItem(getSlotIndex(i,j));
                }
                heat=(heatmap[i][j]>=0)?heat+heatmap[i][j]:heat;
            }
        }
    }
    public void popItem(int slot)
    {
        var item=inventory.getStackInSlot(slot);
        inventory.setStackInSlot(slot,ItemStack.EMPTY);
        Map<RecipeCapability<?>, List<Content>> outputContents = new HashMap<>();
        outputContents.put(
                ItemRecipeCapability.CAP,
                List.of(new Content(
                        SizedIngredient.create(item),
                        ChanceLogic.getMaxChancedValue(),
                        ChanceLogic.getMaxChancedValue(),
                        0
                ))
        );
//        ActionResult result = RecipeHelper.handleRecipe(
//                this,           // IRecipeCapabilityHolder
//                this.getRecipeType(),             // GTRecipe (可以为null，因为我们直接传contents)
//                IO.OUT,           // IO方向
//                outputContents,   // 输出内容
//                Collections.emptyMap(), // chanceCaches
//                false,            // isTick
//                true            // simulate (false = 实际执行)
//        );
    }
    public Long calculateEU()
    {
        var now_eu=0L;
        if(this.heat<this.maxHeat*0.5)
        {
            now_eu= (long) (Math.pow(heat,2)*GTValues.VA[GTValues.HV]);
        }
        if(this.heat>=this.maxHeat*0.5&&this.heat<=this.maxHeat)
        {
            now_eu= (long) (Math.pow(heat,2)*GTValues.VA[GTValues.EV]);
        }
        if(this.heat>this.maxHeat)
        {
            now_eu= (long) (Math.pow(this.maxHeat,2)*GTValues.VA[GTValues.EV]*(1+ (double) (this.heat - this.maxHeat) /this.maxHeat)+Math.pow(this.heat-this.maxHeat,3));
        }
        now_eu=Math.min(now_eu,maxEU);
       return now_eu;
    }
    public long[][] calculateRune(long[][]heatmap)
    {
        for(int i=0;i<slot_range;i++)
        {
            for(int j=0;j<slot_range;j++)
            {
                if(heatmap[i][j]==0)
                {
                    continue;
                }
                else
                {
                    continue;
                }
            }
        }
        return heatmap;
    }

    /// ///////////////////////////////
    /// / Slots/ ////
    /// //////////////////////////
    ///
    ///
    protected NotifiableItemStackHandler createMachineStorage(int range) {
        return new NotifiableItemStackHandler(
                this, range*range, IO.NONE, IO.BOTH, slots -> new CustomItemStackHandler(range*range) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
            }
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if(stack.is(BotaniaTags.Items.RUNES)||stack.getItem() instanceof IManaFuelStick)return true;
                return false;
            }

        });
    }

    public int getSlotIndex(int y, int x) {
        if(y*slot_range+x<=slot_range*slot_range)
            return y*slot_range+x;
        return -1;
    }
    public int getSlotLocation(int index) {
        if(index<slot_range*slot_range-1)return 1;
        return -1;
    }
    @Override
    public void addDisplayText(List<Component> textList) {

        if(isFormed) {
            textList.add(AHCCstatusLang[0].translate(EU,maxEU));
            textList.add(AHCCstatusLang[1].translate(heat,maxHeat));
            textList.add(AHCCstatusLang[2].translate(stability,stability));
        }
        if(this.isActive())
        {
            var voltageName = GTValues.VNF[this.tier];
            textList.add(AHCCstatusLang[3].translate(predicateEU,(double)(predicateEU/GTValues.V[this.tier]),voltageName));
        }
    }
    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(198, 208, this, entityPlayer).widget(new FancyMachineUIWidget(this, 198, 208));
    }
    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8 + 20, 182 + 8 + 20);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182 + 20, 182 + 8).setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                        .textSupplier(this.getLevel().isClientSide ? null : this::addDisplayText)
                        .setMaxWidthLimit(200)
                        .clickHandler(this::handleDisplayClick)));
        var size = group.getSize();
        int gridTotalSize = slot_range * 18;
        int startX = (size.width - gridTotalSize) / 2;
        int startY = (size.height - gridTotalSize) / 2;

        int totalSlots = slot_range * slot_range;
        for (int i = 0; i < totalSlots; i++) {
            int row = i / slot_range;
            int col = i % slot_range;
            int slotX = startX + col * 18;
            int slotY = startY + row * 18;
            group.addWidget(
                    new SlotWidget(inventory.storage, i, slotX, slotY, true, true)
                            .setBackground(GuiTextures.SLOT));
        }
        return group;
    }
    @CN(
            {
                    "存储的EU:%d/%d",
                    "积累的热量:%d/%d",
                    "稳定度:%d/%d",
                    "预计发电量:%d EU (%.2fA %s)"
            }
    )
    @EN(
            {
                    "存储的EU:%d",
                    "积累的热量:%d/%d",
                    "稳定度:%d/%d",
                    "预计发电量:%d EU (%.2fA %s) "
            }
    )
    public static Lang[] AHCCstatusLang;
}
