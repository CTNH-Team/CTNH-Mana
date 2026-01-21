package com.moguang.ctnhmana.Mutiblock;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.capability.recipe.*;
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
import com.gregtechceu.gtceu.utils.GTTransferUtils;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.Mutiblock.parts.RedstoneSignalBroadcastHatch;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import com.moguang.ctnhmana.item.ManaFuelStick.IManaFuelStick;
import com.moguang.ctnhmana.item.Rune.RuneElementType;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMTags;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
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
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.*;

import static com.moguang.ctnhmana.item.Rune.RuneElementType.*;

public class ArcaneHighEnergyCompressionReactorCore extends WorkableMultiblockMachine implements IFancyUIMachine,
        IDisplayUIMachine, IExplosionMachine,IChannelMachine,ICentralStorageMachine{
    @Getter
    @Persisted
    protected final NotifiableItemStackHandler inventory;
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
    public long used_stability=0;
    @Persisted
    public long maxStability=100;
    @Persisted
    public double stablility_pressrue=0;
    @Persisted
    public int tier=0;
    @Nullable
    protected TickableSubscription tickSubs;
    public EnergyContainerList energyContainer;
    protected ISubscription ManaSubs = null;
    public Map<RuneElementType,Integer>elementMap=new HashMap<>();
    public Map<Integer,Integer>channelSignal=new HashMap<>();
    public List<RedstoneSignalBroadcastHatch>hatchList=new ArrayList<>();
    @Persisted
    public double presure=1.0;
    @Persisted
    private boolean shouldChecked=false; //该参数用于在afterworking后立即检查是否需要进行下一轮运行，如果为否则tick逻辑不会试图检查是否运行
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
        hatchList=broadcastSelf();
    }
    @Override
    public void onStructureInvalid() {
        hatchList=null;
        if(this.isActive())
        {
            doExplosion(10F);
        }

        super.onStructureInvalid();
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
    public void onLoad() {
        super.onLoad();
        ManaSubs= inventory.addChangedListener(this::onInventoryChanged);
        elementMap.put(RuneElementType.EARTH, 0);
        elementMap.put(RuneElementType.FIRE, 0);
        elementMap.put(RuneElementType.WATER, 0);
        elementMap.put(RuneElementType.WIND, 0);
        elementMap.put(RuneElementType.SIN, 0);
    }
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
    private void onInventoryChanged() {
        if(this.isActive())return;
        for(int i=0;i<=slot_range*slot_range-1;i++)
        {
            if(!this.inventory.getStackInSlot(i).isEmpty()&&this.inventory.getStackInSlot(i).getItem() instanceof IManaFuelStick) {
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
        if(!isActive()&&shouldChecked)
        {
            onInventoryChanged();
            shouldChecked=false; //只需要检查一次，非常省性能
        }

        var consume=Math.min(EU,energyContainer.getEnergyCapacity()-energyContainer.getEnergyStored());
        EU-=consume;
        energyContainer.addEnergy(consume);
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
            calculateStability();
            changeSignal();
            if(stability<0)
            {
//                doExplosion(10f);
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
        for(int i=0;i<inventory.getSize();i++)
        {
            if(!inventory.getStackInSlot(i).isEmpty()&&inventory.getStackInSlot(i).is(BotaniaTags.Items.RUNES))
            {
                var random=Math.random()+1;
                if(random<=1- (double) stability /maxStability)popItem(i);
            }
        }
        changeSignal();
        shouldChecked=true; //需要检查是否需要立即进入下一轮
        stablility_pressrue=0;
        used_stability=0;
    }
    public void startRecipeCycle()
    {
        GTRecipeType recipeType = getRecipeType();
        GTRecipe emptyRecipe = recipeType
                .recipeBuilder(GTCEu.id("empty_recipe_25s"))
                .duration(400)
                .buildRawRecipe();
        stablility_pressrue=0;
        used_stability=0;
        this.heat=0;
        recipeLogic.setupRecipe(emptyRecipe);
        recipeLogic.markLastRecipeDirty();
    }
    public void calculateStability()
    {
        if(this.heat>this.maxHeat*0.5)stablility_pressrue+=0.5;
        if(this.heat>this.maxHeat*1.0)stablility_pressrue+= (long) (5*Math.pow((double)(this.heat/this.maxHeat),4));
        this.stability= (long) (this.maxStability-this.stablility_pressrue-this.used_stability);
    }
    public void calculateHeat()
    {
        long[][]heatmap=new long[slot_range][slot_range];
        long[][]stabilitymap=new long[slot_range][slot_range];
        for(int i=0;i<slot_range;i++)
        {
            for(int j=0;j<slot_range;j++)
            {
                if(!inventory.getStackInSlot(getSlotIndex(i,j)).isEmpty()) {
                    var item=inventory.getStackInSlot(getSlotIndex(i,j));
                    calculateElementMap(item);
                    if (item.getItem() instanceof IManaFuelStick) {
                        heatmap[i][j] = ((IManaFuelStick) item.getItem()).heat;
                        stabilitymap[i][j]=((IManaFuelStick) item.getItem()).stability;
                    }
                    if (item.is(BotaniaTags.Items.RUNES)) {
                        heatmap[i][j] = 0;
                        stabilitymap[i][j]=0;
                    }
                }
                else
                {
                    heatmap[i][j]=-1;
                    stabilitymap[i][j]=0;
                }
            }
        }
        used_stability=0;
        var maps=calculateRune(heatmap,stabilitymap);
        heatmap=maps.get(0);
        stabilitymap=maps.get(1);
        for(int i=0;i<slot_range;i++)
        {
            for(int j=0;j<slot_range;j++)
            {
                if(heatmap[i][j]>0) {
                    var item=inventory.getStackInSlot(getSlotIndex(i,j));
                    var consume=Math.min(item.getMaxDamage()-item.getDamageValue(),heatmap[i][j]);
                    item.setDamageValue(item.getDamageValue()+(int) consume);
                    heatmap[i][j]=consume;
                    if(item.getDamageValue()>=item.getMaxDamage()) {
                        popItem(getSlotIndex(i, j));
                        stabilitymap[i][j]=0;
                    }
                }
                heat=(heatmap[i][j]>=0)?heat+heatmap[i][j]:heat;
                used_stability+=stabilitymap[i][j];
            }
        }
    }
    public void calculateElementMap(ItemStack stack)
    {
        for(RuneElementType i:elementMap.keySet())
        {
            elementMap.put(i,0);
        }
        if (stack.is(CMTags.ELEMENT_EARTH)) {
            elementMap.put(RuneElementType.EARTH, elementMap.get(RuneElementType.EARTH) + 1);
        }
        if (stack.is(CMTags.ELEMENT_FIRE)) {
            elementMap.put(RuneElementType.FIRE, elementMap.get(RuneElementType.FIRE) + 1);
        }
        if (stack.is(CMTags.ELEMENT_WATER)) {
            elementMap.put(RuneElementType.WATER, elementMap.get(RuneElementType.WATER) + 1);
        }
        if (stack.is(CMTags.ELEMENT_WIND)) {
            elementMap.put(RuneElementType.WIND, elementMap.get(RuneElementType.WIND) + 1);
        }
        if (stack.is(CMTags.ELEMENT_SIN)) {
            elementMap.put(RuneElementType.SIN, elementMap.get(RuneElementType.SIN) + 1);
        }

    }
    public void popItem(int slot) {
        var item = disintegration(inventory.getStackInSlot(slot));
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
        List<IRecipeHandler<?>> handlers = this.getCapabilitiesFlat(IO.OUT, ItemRecipeCapability.CAP);
        if(handlers.isEmpty())return;

        for(IRecipeHandler<?>handler:handlers)
        {
            if(handler instanceof NotifiableItemStackHandler handler1&&!item.isEmpty())
            {
               item= CTNHManaUtils.insertItemToOutput(handler1,item,false);
            }
        }
    }
    public ItemStack disintegration(ItemStack stack)
    {
        if(stack.getItem() instanceof IManaFuelStick stick)
        {
            var random=Math.random();
            if(random<=1- (double) stability /maxStability)return (stick.disintegration!=null)?new ItemStack(BotaniaBlocks.livingrock.asItem()):new ItemStack(stick.disintegration);
            else return stack;
        }
        else if (stack.is(BotaniaTags.Items.RUNES)&&this.isActive())
        {
            return new ItemStack(CMItems.BROKEN_RUNE);
        }
        return stack;
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

    /// ///////////////////////////////
    /// / Slots/ ////
    /// //////////////////////////
    protected NotifiableItemStackHandler createMachineStorage(int range) {
        return new NotifiableItemStackHandler(
                this, range*range, IO.NONE, IO.IN, slots -> new CustomItemStackHandler(range*range) {
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
            textList.add(AHCCstatusLang[2].translate(stability,maxStability));
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

    public boolean isValidLocation(int i,int j)
    {
        if(i>=0&&j>=0&&i<slot_range&&j<slot_range)return true;
        return false;
    }
    /// ///////////////////////////////
    /// / Redstone/ ////
    /// //////////////////////////
    ///
    ///
    public int getChannelSignal(int channel)
    {
        return channelSignal.getOrDefault(channel, 0);
    }
    public List<RedstoneSignalBroadcastHatch> broadcastSelf()
    {
        List<RedstoneSignalBroadcastHatch>hatches=new ArrayList<>();
        for (IMultiPart part : this.getParts()) {
            if (part instanceof RedstoneSignalBroadcastHatch hatch) {
                hatch.setRedstoneSignalOutput((getChannelSignal(hatch.channel)));
                hatches.add(hatch);
            }
        }
        return hatches;
    }
    public void changeSignal()
    {
        if(this.heat<=0)channelSignal.put(0,0);
        if(this.heat<this.maxHeat*0.5&&this.heat>0)channelSignal.put(0,2);
        if(this.heat>=this.maxHeat*0.5&&this.heat<this.maxHeat)channelSignal.put(0,5);
        if(this.heat>=maxHeat)channelSignal.put(0,10);
        if(stability>0.1*maxStability)channelSignal.put(1, (int) (this.maxStability/Math.max(this.stability,1))-1);
        else channelSignal.put(1,12);

        if(!hatchList.isEmpty())
        {
            for(RedstoneSignalBroadcastHatch hatch:hatchList)
            {
                hatch.setRedstoneSignalOutput(getChannelSignal(hatch.channel));
            }
        }

    }
    /// ///////////////////////////////
    /// / Rune/ ////
    /// //////////////////////////
    ///
    ///
    //符文运算
    public List<long[][]> calculateRune(long[][]heatmap,long[][]stabilitymap)
    {
        for(int i=0;i<slot_range;i++)
        {
            for(int j=0;j<slot_range;j++)
            {
                if(heatmap[i][j]==0)
                {
                    var stack=inventory.getStackInSlot(getSlotIndex(i,j));
                    if(stack.getItem().equals(BotaniaItems.runeWater))
                    {
                        if (isValidLocation(i - 1, j) && heatmap[i - 1][j] > 0)
                        {
                            heatmap[i - 1][j] -=1;
                            stabilitymap[i - 1][j]-=2;
                        }
                        if (isValidLocation(i + 1, j) && heatmap[i + 1][j] > 0)
                        {
                            heatmap[i + 1][j] -=1;
                            stabilitymap[i + 1][j] -= 2;
                        }
                        if (isValidLocation(i, j - 1) && heatmap[i][j - 1] > 0)
                        {
                            heatmap[i][j - 1] -=1;
                            stabilitymap[i][j - 1] -= 2;
                        }
                        if (isValidLocation(i, j + 1) && heatmap[i][j + 1] > 0)
                        {
                            heatmap[i][j + 1] -=1;
                            stabilitymap[i][j + 1] -= 2;
                        }
                    }
                    if(stack.getItem().equals(BotaniaItems.runeFire))
                    {
                            if (isValidLocation(i - 1, j) && heatmap[i - 1][j] > 0) heatmap[i - 1][j] += 2;
                            if (isValidLocation(i + 1, j) && heatmap[i + 1][j] > 0) heatmap[i + 1][j] += 2;
                            if (isValidLocation(i, j - 1) && heatmap[i][j - 1] > 0) heatmap[i][j - 1] += 2;
                            if (isValidLocation(i, j + 1) && heatmap[i][j + 1] > 0) heatmap[i][j + 1] += 2;
                            stabilitymap[i][j]+=1;
                    }
                    if(stack.getItem().equals(BotaniaItems.runeEarth))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeAir))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeSpring))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeSummer))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeAutumn))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeWinter))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeMana))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeLust))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeGluttony))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeGreed))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeSloth))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeWrath))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runeEnvy))
                    {

                    }
                    if(stack.getItem().equals(BotaniaItems.runePride))
                    {

                    }
                }
                else
                {
                    continue;
                }
            }
        }
        return List.of(heatmap,stabilitymap);
    }
}
