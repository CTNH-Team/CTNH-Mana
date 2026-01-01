package com.moguang.ctnhmana.common.Mutiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.common.ManaMachine;
import com.moguang.ctnhmana.common.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.common.gui.ManaStatusGui;
import com.moguang.ctnhmana.common.gui.ShroudUi;
import com.moguang.ctnhmana.item.ManaMachineUpgrade.ManaMachineUpgradeItem;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.List;

public class BaseManaMachine extends ManaMachine {
    @Persisted
    public final NotifiableItemStackHandler machineStorage;
    public ManaHatch hatch;
    @Persisted
    public BlockPos hatchPos;
    @Persisted
    public boolean isManaConsumedInstantly = false;
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            BaseManaMachine.class, ManaMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    @Persisted
    public int consumption;
    @Persisted
    public int baseConsumption;
    @Getter
    protected ManaMachineUpgradeItem upgrade;
    public MachineMetric metric=new MachineMetric(); //用于维护每秒刷新的metric
    public MachineMetric recipemetric =new MachineMetric(); //用于维护配recipemodifer中根据配方的metric
    public MachineMetric globalmetric=new MachineMetric(); //用于维护一个全局metric增量，只在成型检查时刷新
    protected ISubscription ManaSubs = null;

    @Nullable
    protected TickableSubscription TickSubs;
    public BaseManaMachine(IMachineBlockEntity holder, int consumption) {
        super(holder);
        this.machineStorage = createMachineStorage();
        this.baseConsumption = consumption;
    }
    //////////////////////////////////////
    // ********   HatchVision  ********//
    /// ///////////////////////////////////
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch(); //获取舱室
        if (this.hatch == null) onStructureInvalid(); //获取不到就别成型
        var tier = getTier();//获取等级
        consumption = (int) Math.pow(2, tier) * baseConsumption; //计算魔力消耗
        checkUpdate(); //检查升级
        this.metric.init(); //重置metric
        this.globalmetric.initGlobal();
        onInventoryChanged();
    }

    @Override
    public void onStructureInvalid() {
        this.hatch = null;
        this.hatchPos =null;
        super.onStructureInvalid();
    }

    public ManaHatch getHatch() {
        for (IMultiPart part : this.getParts()) {
            if (part instanceof ManaHatch hatchs) {
                hatchPos = (hatchs).getPos();
                return hatchs;
            }
        }
        return null;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        //在魔力一次性消耗模式下一次性消耗，否则在onworking每秒消耗
        if (isManaConsumedInstantly && hatch.getBTMana() > consumption * recipe.duration / 20) {
            hatch.consumeMana(consumption * recipe.duration / 20);
            return super.beforeWorking(recipe);
        } else {
            return hatch.consumeManaIfEnough(consumption) && super.beforeWorking(recipe);
        }
    }

    @Override
    public boolean onWorking() {
        if (isManaConsumedInstantly) return super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            if(hatch.consumeManaIfEnough(consumption))super.onWorking();
            else getRecipeLogic().setProgress(0);
        }
        return super.onWorking();
    }
    //////////////////////////////////////
    // ********   Subscriptions&Ticks  ********//
    //////////////////////////////////////
    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTick));
            ManaSubs = machineStorage.addChangedListener(this::onInventoryChanged);
        }
    }
    @Override
    public void onUnload() {
        super.onUnload();
        if (ManaSubs != null) {
            ManaSubs.unsubscribe();
        }
        if(TickSubs!=null)
        {
            TickSubs.unsubscribe();
            TickSubs = null;
        }
    }
    public void updateTick()
    {
        TickSubs=subscribeServerTick(TickSubs, this::updateMetric);
    }
    //每秒维护一次
    public void updateMetric()
    {
        if(!this.isFormed())return;
        if(getOffsetTimer()%20==0) {
            if(this.upgrade!=null) {
                metric = upgrade.calculateNormalUpgrade(new MachineMetric(), this);
                metric.updateType=this.upgrade.type;;
            }
            else
            {
                metric.init();

            }
        }
    }
    public void onInventoryChanged()
    {
        checkUpdate();
    }
    //////////////////////////////////////
    // ********   MachineStorage  ********//
    /// ///////////////////////////////////
    protected NotifiableItemStackHandler createMachineStorage() {
        return new NotifiableItemStackHandler(
                this, 1, IO.NONE, IO.BOTH, slots -> new CustomItemStackHandler(1) {
        }).setFilter(itemStack -> itemStack.getItem() instanceof ManaMachineUpgradeItem);
    }


    //////////////////////////////////////
    // ********   Update  ********//
    //////////////////////////////////////
    ///
    ///
    public class MachineMetric
    {
        public int parallel=1;
        public double speed=1;
        public double eut=1;
        public double input=1;
        public double output=1;
        public String updateType=null;
        public int true_parallel=1;
        public void init()
        {
            parallel=1;
            speed=1;
            eut=1;
            input=1;
            output=1;
            updateType=null;
            true_parallel=1;
        }
        public void plus(MachineMetric metric)
        {
            this.parallel+=metric.parallel;
            this.speed+=metric.speed;
            this.eut+=metric.eut;
            this.input+=metric.input;
            this.output+=metric.output;
        }
        public void initGlobal()
        {
            parallel=0;
            speed=0;
            eut=0;
            input=0;
            output=0;
            updateType=null;
            true_parallel=0;
        }
        public  void Copy(MachineMetric other) {
            this.parallel = other.parallel;
            this.speed = other.speed;
            this.eut = other.eut;
            this.input = other.input;
            this.output = other.output;
            this.updateType = other.updateType;
            this.true_parallel = other.true_parallel;
        }

    }
    //todo
    public void checkWorldData()
    {

    }
    //检查当前的升级和世界效果的状态
    public void checkUpdate() {
        checkWorldData();
        this.upgrade =null;
        metric.updateType=null;
        if (!machineStorage.isEmpty()) {
            var name = machineStorage.getStackInSlot(0).getItem();
            if(name instanceof ManaMachineUpgradeItem mitem)
            {
                metric.updateType=mitem.getType();
                this.upgrade =mitem;
            }
        }

    }

    //////////////////////////////////////
    // ********   RecipeLogic  ********//
    //////////////////////////////////////
    ///
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (machine instanceof BaseManaMachine mmachine) {
            //复制metric 检查metric 计算metric
            mmachine.recipemetric.Copy(mmachine.metric);
            mmachine.recipemetric.plus(mmachine.globalmetric);
            MachineMetric metric= mmachine.recipemetric;
            if(mmachine.upgrade !=null) metric=mmachine.upgrade.calculateUpgrade(metric,recipe,mmachine);
            if(metric.parallel==-1)
            {
                return  ModifierFunction.builder()
                        .parallels(metric.true_parallel)
                        .eutMultiplier(metric.eut)
                        .inputModifier(ContentModifier.multiplier(metric.true_parallel*metric.input))
                        .outputModifier(ContentModifier.multiplier(metric.true_parallel*metric.output))
                        .durationMultiplier(1/metric.speed*Math.min(64,metric.true_parallel))
                        .build();
            }
            return  ModifierFunction.builder()
                    .parallels(metric.true_parallel)
                    .eutMultiplier(metric.true_parallel*metric.eut)
                    .inputModifier(ContentModifier.multiplier(metric.true_parallel*metric.input))
                    .outputModifier(ContentModifier.multiplier(metric.true_parallel*metric.output))
                    .durationMultiplier(1/metric.speed)
                    .build();
        }
        return ModifierFunction.IDENTITY;
    }
    //////////////////////////////////////
    // ********   UI  ********//
    //////////////////////////////////////
    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);
        if (this.getRecipeTypes().length > 0) {
            sideTabs.attachSubTab(new ShroudUi());
            sideTabs.attachSubTab(new ManaStatusGui(this));
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

    @CN("没有升级")
    public static Lang NULL_UPDATE_NAME;
    @CN("Null Pointer Exception发生在魔力凝聚器丢失")
    public static Lang MANAHATCH_NPE_ERROR;
    public MutableComponent getUpdateName()
    {
        if(metric.updateType==null||this.upgrade ==null)
            return NULL_UPDATE_NAME.translate();
        return this.upgrade.getUpdateName().translate();
    }
    @CN({
            "当前魔力能量数：%d",
            "运行时消耗魔力能量:%d/s",
            "当前升级:%s",
            "最大并行数:%d",
            "当前速度倍率:%.2f",
            "当前EU消耗倍率:%.2f",
            "当前输入材料倍率:%.2f",
            "当前输出物品倍率:%.2f",
            "天顶强化已启动",
    })
    @EN({
            "当前魔力能量数：%d",
            "运行时消耗魔力能量:%d",
            "当前升级:%s",
            "最大并行数:%d",
            "当前速度倍率:%.2f",
            "当前EU消耗倍率:%.2f",
            "当前输入材料倍率:%.2f",
            "当前输出产物倍率:%.2f",
            "天顶强化已启动",
    })
    public static  Lang[]  BaseManaMachineLang;
    @CN("当前并行数:%d / %d")
    public static  Lang  BaseManaMachineWorkingParallelLang;
    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if(this.isFormed()) {
            if(hatch!=null)textList.add(textList.size(), BaseManaMachineLang[0].translate((int)hatch.getMana()));
            else{
                textList.add(textList.size(), MANAHATCH_NPE_ERROR.translate());
            }
            if(Zenith_Enhanced!=null)
                textList.add(textList.size(), BaseManaMachineLang[8].translate());
            if(!this.isActive()) {
                textList.add(textList.size(), BaseManaMachineLang[1].translate(this.consumption));
                textList.add(textList.size(), BaseManaMachineLang[2].translate(getUpdateName()));
                textList.add(textList.size(), BaseManaMachineLang[3].translate(metric.parallel+globalmetric.parallel));
//                textList.add(textList.size(), BaseManaMachineLang[4].translate(metric.speed+globalmetric.speed));
//                textList.add(textList.size(), BaseManaMachineLang[5].translate(metric.eut+globalmetric.eut));
//                textList.add(textList.size(), BaseManaMachineLang[6].translate(metric.input+globalmetric.input));
//                textList.add(textList.size(), BaseManaMachineLang[7].translate(metric.output+globalmetric.output));
            }
            else
            {
                textList.add(textList.size(), BaseManaMachineLang[1].translate(this.consumption));
                textList.add(textList.size(), BaseManaMachineLang[2].translate(getUpdateName()));
                textList.add(textList.size(), BaseManaMachineWorkingParallelLang.translate(recipemetric.parallel,metric.parallel+globalmetric.parallel));
//                textList.add(textList.size(), BaseManaMachineLang[4].translate(recipemetric.speed));
//                textList.add(textList.size(), BaseManaMachineLang[5].translate(recipemetric.eut));
//                textList.add(textList.size(), BaseManaMachineLang[6].translate(recipemetric.input));
//                textList.add(textList.size(), BaseManaMachineLang[7].translate(recipemetric.output));
            }
        }

    }
}
