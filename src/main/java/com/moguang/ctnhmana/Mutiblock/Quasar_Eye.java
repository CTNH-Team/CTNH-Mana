package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import com.ctnhlang.*;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

@Domain("multiblock.quasar_eye")
public class Quasar_Eye extends WorkableElectricMultiblockMachine implements ITieredMachine {

    private double rune_energy = 0;
    private int energy_tier = 0;
    private int active = 0;
    public static final String RUNE_ENERGY = "rune_energy";
    public static final String ET = "energy_tier";
    public static final String ACTIVE = "active";
    public long power = 0;
    private static int[] based_output = { 0, 67108864, 134217728, 268435456 };

    public Quasar_Eye(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        rune_energy = 0;
        energy_tier = 0;
        active = 0;
    }

    public double energy_caculate(double rune, int energy_tier) {
        if (rune <= 50) {
            return 0.5;
        }
        return Math.min((Math.log(((rune) / 50))) + 1, 1 + energy_tier * 1);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (!MachineUtils.inputFluid(CMMaterials.Mana.getFluid(recipe.data.getInt("consumption")), this)) {
            if (recipe.data.getInt("active") > active) return false;
        }

        if (MachineUtils.inputItem(CMItems.TWIST_RUNE.asStack(1), this)) {
            rune_energy += 32;
        }
        if (MachineUtils.inputItem(CMItems.HORIZEN_RUNE.asStack(1), this)) {
            rune_energy += 32;
        }
        if (MachineUtils.inputItem(CMItems.STARLIGHT_RUNE.asStack(1), this)) {
            rune_energy += 32;
        }
        if (MachineUtils.inputItem(CMItems.PROLIFERATION_RUNE.asStack(1), this)) {
            rune_energy += 16;
        }
        if (MachineUtils.inputItem(CMItems.QUASAR_RUNE.asStack(1), this)) {
            rune_energy += 512;
        }
        if (active < recipe.data.getInt("active")) {
            active = recipe.data.getInt("active");
        }
        energy_tier = recipe.data.getInt("tier");
        return super.beforeWorking(recipe);
    }

    @Override
    public boolean onWorking() {
        if (getOffsetTimer() % 100 == 0) {
            if (rune_energy > 0) rune_energy -= Math.max((rune_energy / 50) * Math.log((rune_energy / 50) + 1), 0);
        }
        return super.onWorking();
    }

    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        textList.add(textList.size(), INFO_MANA_MODEL.translate(String.format("%d", energy_tier)));
        textList.add(textList.size(), INFO_RUNE_ENERGY.translate(String.format("%.2f", rune_energy)));
        textList.add(textList.size(),
                INFO_MANA_PRODUCTION.translate(String.format("%.2f", energy_caculate(rune_energy, energy_tier))));
        textList.add(textList.size(), INFO_RUNE_CONSUMPTION
                .translate(String.format("%.2f", (rune_energy / 50) * Math.log(rune_energy / 50 + 1))));
        textList.add(textList.size(),
                INFO_QUASAR_PARALLEL.translate(String.format("%.2f", energy_caculate(rune_energy, energy_tier) * 5)));
        textList.add(textList.size(), INFO_CONSUMPTION_PARALLEL
                .translate(String.format("%.2f", (1 - 0.05 * Math.max((rune_energy - 50) / 50, 10)))));
        textList.add(textList.size(), INFO_ACCUMULATED.translate((double) power / 100000000 + "E EU"));
    }

    // lang: info (display in UI)
    @Key("info.mana_model")
    @CN("当前魔力燃料等级:%d")
    @EN("Current mana fuel tier: %d")
    public static Lang INFO_MANA_MODEL;
    @Key("info.rune_energy")
    @CN("符文能量：%.2f")
    @EN("Rune energy: %.2f")
    public static Lang INFO_RUNE_ENERGY;
    @Key("info.mana_production")
    @CN("当前发电效率:%.2f")
    @EN("Current generation efficiency: %.2f")
    public static Lang INFO_MANA_PRODUCTION;
    @Key("info.rune_consumption")
    @CN("当前消耗符文能量速率:%.2f /100tick")
    @EN("Rune energy consumption rate: %.2f / 100 ticks")
    public static Lang INFO_RUNE_CONSUMPTION;
    @Key("info.quasar_parallel")
    @CN("时间并行:%.2f")
    @EN("Time parallelism: %.2f")
    public static Lang INFO_QUASAR_PARALLEL;
    @Key("info.consumption_parallel")
    @CN("能源消耗率:%.2f")
    @EN("Energy consumption factor: %.2f")
    public static Lang INFO_CONSUMPTION_PARALLEL;
    @Key("info.0")
    @CN("积累的能量:%s")
    @EN("Accumulated energy: %s")
    public static Lang INFO_ACCUMULATED;

    // lang: tooltips (multiblock description)
    @Key("ctnh.multiblock.quasar_eye.tooltip")
    @CN({
            "§9魔力§r的§c终极奥秘§r，能够创造§5类星体§r的装置如今握在§6你§r手中",
            "机器激活需要§r消耗初始魔力燃料§r，具体数值请查阅JEI",
            "在高能量等级下激活低等级配方时§b可免除激活消耗§r",
            "§5符文能量§r决定产出强度。投入§b五级符文§r可放大符文能量并提升产出。使用§5类星体符文§r可大量生成符文能量",
            "符文能量获取逻辑：§5每次配方周期前§r，每种可消耗符文类型§c最多消耗一个§r",
            "§c警告§r：符文能量越高，§c消耗速率§r越快。符文能量低于50时效率§c减半§r！",
            "能量效率公式：log((符文能量)/50)+1。最大效率：(1+能量等级)",
            "具有时间并行。消耗与时长均乘以并行系数(效率*5)",
            "燃料消耗公式：1-0.05*Math.max((符文能量-50)/50,0.75)",
            "发电模式下，将1%的EU产出积累进类星体之眼。每25点符文能量额外+1%积累",
            "创造模式下，释放全部储存EU。高级燃料可放大产出。每1000E EU产生额外气体副产物。储存EU<1E时创造模式禁用",
            "§b好消息§r：这台机器不会爆炸。§c但无法保证以后版本！§r"
    })
    @EN({
            "§9魔力§r的§c终极奥秘§r，能够创造§5类星体§r的装置如今握在§6你§r手中",
            "机器激活需要§r消耗初始魔力燃料§r，具体数值请查阅JEI",
            "在高能量等级下激活低等级配方时§b可免除激活消耗§r",
            "§5符文能量§r决定产出强度。投入§b五级符文§r可放大符文能量并提升产出。使用§5类星体符文§r可大量生成符文能量",
            "符文能量获取逻辑：§5每次配方周期前§r，每种可消耗符文类型§c最多消耗一个§r",
            "§c警告§r：符文能量越高，§c消耗速率§r越快。符文能量低于50时效率§c减半§r！",
            "能量效率公式：log((符文能量)/50)+1。最大效率：(1+能量等级)",
            "具有时间并行。消耗与时长均乘以并行系数(效率*5)",
            "燃料消耗公式：1-0.05*Math.max((符文能量-50)/50,0.75)",
            "发电模式下，将1%的EU产出积累进类星体之眼。每25点符文能量额外+1%积累",
            "创造模式下，释放全部储存EU。高级燃料可放大产出。每1000E EU产生额外气体副产物。储存EU<1E时创造模式禁用",
            "§b好消息§r：这台机器不会爆炸。§c但无法保证以后版本！§r"
    })
    public static Lang[] TOOLTIPS;

    /** 类星体之眼配方在 JEI 中显示的 dataInfo，由 LangProcessor 注册 ctnh.recipe.quasar_eye.info.0/1/2 */
    @Category("recipe")
    public static class RecipeLang {

        @CN("激活消耗：%.1f")
        @EN("Activation cost: %.1f")
        public static Lang RECIPE_INFO_0;

        @CN("能量等级：%d")
        @EN("Energy tier: %d")
        public static Lang RECIPE_INFO_1;

        @CN("激活等级：%d")
        @EN("Activation tier: %d")
        public static Lang RECIPE_INFO_2;
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe) {
        if (machine instanceof Quasar_Eye qmachine) {
            if (recipe.recipeType.equals(CMRecipeTypes.QUASAR_CREATE)) {

                var true_eut = Math.min(qmachine.power / 200, Long.MAX_VALUE);
                if (true_eut < 10000000) return ModifierFunction.NULL;
                var outputmuti = true_eut / 50000000L;
                qmachine.power = 0;
                return ModifierFunction.builder()
                        .eutMultiplier(true_eut)
                        .outputModifier(ContentModifier.multiplier(outputmuti))
                        .build();
            }
            var EUt = RecipeHelper.getRealEUtWithIO(recipe).voltage();
            var tier = recipe.data.getInt("tier");
            var power = (long) (qmachine.energy_caculate(qmachine.rune_energy, tier) *
                    RecipeHelper.getRealEUtWithIO(recipe).voltage() *
                    (qmachine.energy_caculate(qmachine.rune_energy, tier) * 5) * recipe.duration * 0.2 *
                    (qmachine.rune_energy / 25));
            qmachine.power += power / 200;
            return ModifierFunction.builder()
                    .eutMultiplier(qmachine.energy_caculate(qmachine.rune_energy, tier))
                    .durationMultiplier(qmachine.energy_caculate(qmachine.rune_energy, tier) * 5)
                    .inputModifier(ContentModifier.multiplier(qmachine.energy_caculate(qmachine.rune_energy, tier) * 5 *
                            (1 - 0.05 * Math.max((qmachine.rune_energy - 50) / 50, 10))))
                    .outputModifier(ContentModifier.multiplier(qmachine.energy_caculate(qmachine.rune_energy, tier) *
                            5 * (1 - 0.05 * Math.max((qmachine.rune_energy - 50) / 50, 10))))
                    .build();
        }
        return ModifierFunction.NULL;
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        if (!forDrop) {
            tag.putInt(ACTIVE, active);
            tag.putInt(ET, energy_tier);
            tag.putDouble(RUNE_ENERGY, rune_energy);
        }
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        active = tag.contains(ACTIVE) ? tag.getInt(ACTIVE) : 0;
        energy_tier = tag.contains(ET) ? tag.getInt(ET) : 0;
        rune_energy = tag.contains(RUNE_ENERGY) ? tag.getDouble(RUNE_ENERGY) : 0;
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }
}
