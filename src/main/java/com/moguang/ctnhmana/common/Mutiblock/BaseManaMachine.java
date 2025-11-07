package com.moguang.ctnhmana.common.Mutiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.common.ManaMachine;
import com.moguang.ctnhmana.common.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.common.gui.BaseManaMachineGui;
import com.moguang.ctnhmana.common.gui.ShroudUi;
import com.moguang.ctnhmana.registry.CMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
    public BlockPos hatchpos;
    @Persisted
    private boolean isManaConsumedInstantly = false;
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            BaseManaMachine.class, ManaMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    @Persisted
    public int consumption;
    @Persisted
    private int baseconsumption;
    private MachineMetric metric=new MachineMetric();
    private MachineMetric pre_metric=new MachineMetric();
//    public  int parallel=1;
//    public  double speed=1;
//    public  double EU_consumption=1;
//    public  double input=1;
//    public  double output=1;
//    public  String updateType=null;
    public BaseManaMachine(IMachineBlockEntity holder, int consumption) {
        super(holder);
        this.machineStorage = createMachineStorage();
        this.baseconsumption = consumption;
    }
    //////////////////////////////////////
    // ********   HatchVision  ********//

    /// ///////////////////////////////////
    @Override
    public void onStructureFormed() {

        super.onStructureFormed();
        this.hatch = getHatch();
        if (this.hatch == null) onStructureInvalid();
        var tier = getTier();
        consumption = (int) Math.pow(2, tier) * baseconsumption;
        checkUpdate();


    }

    @Override
    public void onStructureInvalid() {
        this.hatch = null;
        this.hatchpos=null;
        super.onStructureInvalid();


    }

    public ManaHatch getHatch() {
        for (IMultiPart part : this.getParts()) {
            if (part instanceof ManaHatch hatchs) {
                hatchpos = (hatchs).getPos();
                return hatchs;
            }
        }
        return null;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        pre_metric.Copy(metric);
        if(RecipeHelper.getRecipeEUtTier(recipe)>=this.tier) //如果运行同等级配方速度-0.25;
            pre_metric.speed-=0.25;
        //在魔力一次性消耗模式下一次性消耗，否则在onworking美妙消耗
        if (isManaConsumedInstantly && hatch.getBT_Mana() > consumption * recipe.duration / 20) {
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

    @Override
    public void onLoad() {
        super.onLoad();
        if(this.isFormed()) {
            checkUpdate();
        }
    }

    //////////////////////////////////////
    // ********   MachineStorage  ********//

    /// ///////////////////////////////////
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
                checkUpdate();
            }
        });//有问题.setFilter(itemStack -> itemStack.getTag().contains("mana_update_t1"));
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
        public String updateType="None";
        public int true_parallel=1;
        public void init()
        {
            parallel=1;
            speed=1;
            eut=1;
            input=1;
            output=1;
            updateType="None";
            true_parallel=1;
        }
        public  void Copy(MachineMetric other) {
            this.parallel = other.parallel;
            this.speed = other.speed;
            this.eut = other.eut;
            this.input = other.input;
            this.output = other.output;
            this.updateType = other.updateType; // String不可变，浅拷贝安全
            this.true_parallel = other.true_parallel;
        }

    }
    public void checkWorldData()
    {

    }
    //检查当前的升级和世界效果的状态
    public void checkUpdate() {
        metric=new MachineMetric();
        if (!machineStorage.isEmpty()) {
            var name = machineStorage.getStackInSlot(0).getItem();
            if(name.equals(CMItems.FLOWER_UPDATE.get()))metric.updateType="BT";
            if(name.equals(CMItems.GT_UPDATE.get()))metric.updateType="GT";
        }

    }
    //计算植物魔法升级
    public MachineMetric caculateBTupdate(MachineMetric metric,GTRecipe recipe)
    {
        metric.parallel+=(hatch.getBT_Mana()/50000+hatch.getBT_Max_Mana()/200000);
        var true_parallel= ParallelLogic.getParallelAmount(this,recipe,metric.parallel);
        metric.speed+=Math.min(0.1,true_parallel*0.01-0.01);
        metric.speed+=Math.min(0.25, (double) hatch.getBT_Mana() /10000000);
        metric.true_parallel=true_parallel;
        return metric;
    }
    //计算格雷升级
    public MachineMetric caculateGTupdate(MachineMetric metric,GTRecipe recipe)
    {
        metric.parallel+=64;
        var true_parallel= ParallelLogic.getParallelAmount(this,recipe,metric.parallel);
        metric.speed+=Math.min(0.4,true_parallel*00.1-0.01);
        if(!hatch.getInventory().isEmpty())metric.speed-=0.1;
        if(hatch.getBT_Mana()>=100000)metric.speed-=0.1;
        return metric;
    }
    //////////////////////////////////////
    // ********   RecipeLogic  ********//
    //////////////////////////////////////
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (machine instanceof BaseManaMachine mmachine) {
            var metric= mmachine.pre_metric;
            switch (metric.updateType)
            {
                case "BT":metric=mmachine.caculateBTupdate(metric,recipe);
                case "GT":metric=mmachine.caculateGTupdate(metric,recipe);

                case "None":metric.speed*=0.1;

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
    @CN("§9繁蕊之簇拥")
    public static Lang BT_UPDATE_NAME;
    @CN("§4赤痕之翘曲")
    public static Lang BM_UPDATE_NAME;
    @CN("§5流线之视野")
    public static Lang ARS_UPDATE_NAME;
    @CN("§9繁蕊之簇拥")
    public static Lang GT_UPDATE_NAME;
    @CN("没有升级")
    public static Lang NULL_UPDATE_NAME;
    @CN("Null Pointer Exception发生在魔力凝聚器丢失")
    public static Lang MANAHATCH_NPE_ERROR;
    public MutableComponent getUpdateName()
    {
        if(metric.updateType==null)
            return NULL_UPDATE_NAME.translate();
        switch (metric.updateType)
        {
            case "BT":return BT_UPDATE_NAME.translate();
            case "BM":return BM_UPDATE_NAME.translate();
            case "GT":return GT_UPDATE_NAME.translate();
            case "None":return  NULL_UPDATE_NAME.translate();
        };
        return NULL_UPDATE_NAME.translate();
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
    })
    public static  Lang[]  BaseManaMachineLang;
    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if(this.isFormed()) {
            if(hatch!=null)textList.add(textList.size(), BaseManaMachineLang[0].translate((int)hatch.getMana_Power()));
            else{
                textList.add(textList.size(), MANAHATCH_NPE_ERROR.translate());
            }
            textList.add(textList.size(), BaseManaMachineLang[1].translate(this.consumption));
            textList.add(textList.size(), BaseManaMachineLang[2].translate(getUpdateName()));
            textList.add(textList.size(), BaseManaMachineLang[3].translate(metric.parallel));
            textList.add(textList.size(), BaseManaMachineLang[4].translate(metric.speed));
            textList.add(textList.size(), BaseManaMachineLang[5].translate(metric.eut));
            textList.add(textList.size(), BaseManaMachineLang[6].translate(metric.input));
            textList.add(textList.size(), BaseManaMachineLang[7].translate(metric.output));
        }

    }
}
