package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.render.StarCakeMachineBERProvider;
import com.moguang.ctnhmana.common.DigitalWosMachine;
import com.moguang.ctnhmana.common.blockentity.machine.FlowerCakeBlockEntity;
import com.moguang.ctnhmana.common.item.FlowerCakeItem;
import com.moguang.ctnhmana.common.machine.FlowerCakeBlock;
import com.moguang.ctnhmana.common.machine.FlowerCakeMachine;
import com.moguang.ctnhmana.common.machine.GemSublimatorMachine;
import com.moguang.ctnhmana.common.parts.CMPartsAbility;
import com.moguang.ctnhmana.common.parts.CentralControlBus;
import com.moguang.ctnhmana.common.parts.ExtendedCentralControlBus;
import com.moguang.ctnhmana.common.parts.ManaHatch;
import com.moguang.ctnhmana.common.parts.ManaHatches.BloodManaHatch;
import com.moguang.ctnhmana.common.parts.ManaHatches.CreativeManaHatch;
import com.moguang.ctnhmana.common.parts.ManaHatches.SparkManaHatch;
import com.moguang.ctnhmana.common.parts.RedstoneSignalBroadcastHatch;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.registrate.builders.CTNHMachineBuilder;

import java.util.Locale;
import java.util.function.BiFunction;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties.IS_FORMED;
import static com.gregtechceu.gtceu.common.data.GTMachines.CREATIVE_TOOLTIPS;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.common.DigitalWosMachine.digitalWosTooltip;
import static com.moguang.ctnhmana.common.machine.GemSublimatorMachine.gemSublimatorCtrlHintTooltip;
import static com.moguang.ctnhmana.common.machine.GemSublimatorMachine.gemSublimatorCtrlTooltip;
import static com.moguang.ctnhmana.common.machine.GemSublimatorMachine.gemSublimatorTooltip;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.*;

public class CMMachines {

    static {
        REGISTRATE.creativeModeTab(() -> CMCreativeModeTabs.MACHINE);
    }

    public static void init() {}

    @CN({
            "§b制作组物品§r",
            "§b我永恒的灵魂，注视着你的心。纵使黑夜孤寂，白昼如焚。",
            "需要输入900000魔力来激活，食用后获得半个小时的创造飞行",
            "可连续食用！"
    })
    @EN({
            "§bDeveloper Item§r",
            "§bMy eternal soul gazes upon your heart. Though the night is lonely, the day burns like fire.",
            "Requires 900,000 mana to activate. Grants 30 minutes of creative flight upon consumption.",
            "Can be consumed repeatedly!"
    })
    public static Lang[] flowercakelang;

    public static final MachineDefinition STAR_FLOWER_CAKE = REGISTRATE.machine(
            "flower_cake",
            "献给月亮的花束",
            MachineDefinition::new,
            be -> new FlowerCakeMachine(be),
            (block, properties) -> new FlowerCakeBlock(block, properties) {

                @Override
                public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
                    return 1.0f;
                }

                @Override
                public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
                    return 0;
                }

                @Override
                public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
                    return Shapes.empty();
                }

                @Override
                public RenderShape getRenderShape(BlockState state) {
                    return RenderShape.ENTITYBLOCK_ANIMATED;
                }
            },
            (b, p) -> new FlowerCakeItem(b, p),
            (beType, pos, state) -> new FlowerCakeBlockEntity(beType, pos, state))
            .recipeModifier(RecipeModifier.NO_MODIFIER)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(CTNHManaUtils.addMachineTooltips(flowercakelang))
            .hasBER(false)
            .rotationState(RotationState.NON_Y_AXIS)
            .onBlockEntityRegister(beType -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    StarCakeMachineBERProvider.registerRenderer(beType, "star_cake_model");
                }
            })
            .simpleModel(GTCEu.id("block/machine/template/part/hatch_machine"))
            .register();
    public static final MachineDefinition MANA_HATCH = REGISTRATE
            .manamachine("manahatch",
                    holder -> new ManaHatch(holder, 10000, 10000, 100000, 6400))
            .cnLangValue("原型·魔力凝聚仓")
            .rotationState(RotationState.ALL)
            .modelProperty(IS_FORMED, false)
            .tooltips(manahatchtooltip_1.translate())
            .tooltips(
                    manahatchtootip_base[0].translate(),
                    manahatchtootip_base[1].translate(),
                    manahatchtootip_base[2].translate(),
                    manahatchtootip_base[3].translate(),
                    manahatchtootip_base[4].translate(10000),
                    manahatchtootip_base[5].translate(100000),
                    manahatchtootip_base[6].translate(8000))
            .tooltips(manahatchctrltooltip_1.translate())
            .tooltipBuilder((stack, tooltip) -> {
                if (GTUtil.isCtrlDown()) {
                    tooltip.add(Component.empty());
                    tooltip.add(manaHatchDataLang[0].translate());
                    tooltip.add(manaHatchDataLang[7].translate("凝聚魔力能量"));
                    tooltip.add(manaHatchDataLang[2].translate("20"));
                    tooltip.add(manaHatchDataLang[3].translate("每tick至多0.1%"));
                    tooltip.add(manaHatchDataLang[4].translate("100"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化效率"));
                }
            })
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel("manahatch")
            .tier(HV)
            .register();
    public static final MachineDefinition ADVANCED_MANA_HATCH = REGISTRATE
            .manamachine("elf_manahatch",
                    holder -> new SparkManaHatch(holder, 40000, 100000, 500000, 32000, 7500))
            .cnLangValue("精灵·魔力凝聚仓")
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .tooltips(manahatchtooltip_1.translate())
            .tooltips(
                    sparkmanahatchtootip_base[0].translate(),
                    sparkmanahatchtootip_base[1].translate(),
                    sparkmanahatchtootip_base[2].translate(),
                    sparkmanahatchtootip_base[3].translate(),
                    sparkmanahatchtootip_base[4].translate(7500),
                    sparkmanahatchtootip_base[5].translate(40000),
                    sparkmanahatchtootip_base[6].translate(500000),
                    sparkmanahatchtootip_base[7].translate(32000))
            .tooltips(manahatchctrltooltip_1.translate())
            .tooltipBuilder((stack, tooltip) -> {
                if (GTUtil.isCtrlDown()) {
                    tooltip.add(Component.empty());
                    tooltip.add(manaHatchDataLang[0].translate());
                    tooltip.add(manaHatchDataLang[7].translate("凝聚魔力能量"));
                    tooltip.add(manaHatchDataLang[2].translate("20"));
                    tooltip.add(manaHatchDataLang[3].translate("每tick至多1%"));
                    tooltip.add(manaHatchDataLang[7].translate("网路LP转化"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化效率"));
                }
            })
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tier(EV)
            .register();
    public static final MachineDefinition GIGA_MANA_HATCH = REGISTRATE
            .manamachine("giga_manahatch",
                    holder -> new SparkManaHatch(holder, 160000, 100000, 5000000, 128000, 20000))
            .cnLangValue("盖亚·魔力凝聚仓")
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .tooltips(manahatchtooltip_1.translate())
            .tooltips(
                    sparkmanahatchtootip_base[0].translate(),
                    sparkmanahatchtootip_base[1].translate(),
                    sparkmanahatchtootip_base[2].translate(),
                    sparkmanahatchtootip_base[3].translate(),
                    sparkmanahatchtootip_base[4].translate(20000),
                    sparkmanahatchtootip_base[5].translate(160000),
                    sparkmanahatchtootip_base[6].translate(5000000),
                    sparkmanahatchtootip_base[7].translate(128000))
            .tooltips(manahatchctrltooltip_1.translate())
            .tooltipBuilder((stack, tooltip) -> {
                if (GTUtil.isCtrlDown()) {
                    tooltip.add(Component.empty());
                    tooltip.add(manaHatchDataLang[0].translate());
                    tooltip.add(manaHatchDataLang[1].translate("1"));
                    tooltip.add(manaHatchDataLang[2].translate("20"));
                    tooltip.add(manaHatchDataLang[3].translate("每tick至多1%"));
                    tooltip.add(manaHatchDataLang[7].translate("网路LP转化"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化效率"));
                }
            })
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tier(LuV)
            .register();
    @CN("植物魔法mana转化为魔力能量时，具有双倍转化率(10mana转化1魔力能量)")
    @EN("When converting Botania mana to Mana Energy, conversion is doubled (10 Botania mana → 1 Mana Energy).")
    public static Lang SkyhatchLang;
    public static final MachineDefinition SKY_MANA_HATCH = REGISTRATE
            .manamachine("sky_manahatch",
                    holder -> new SparkManaHatch(holder, 1280000, 100000, 100000000, 1280000, 40000))
            .cnLangValue("天域·魔力凝聚仓")
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .tooltips(manahatchtooltip_1.translate())
            .tooltips(
                    sparkmanahatchtootip_base[0].translate(),
                    sparkmanahatchtootip_base[1].translate(),
                    sparkmanahatchtootip_base[2].translate(),
                    sparkmanahatchtootip_base[3].translate(),
                    sparkmanahatchtootip_base[4].translate(40000),
                    sparkmanahatchtootip_base[5].translate(1280000),
                    sparkmanahatchtootip_base[6].translate(100000000),
                    sparkmanahatchtootip_base[7].translate(1280000),
                    SkyhatchLang.translate())
            .tooltips(manahatchctrltooltip_1.translate())
            .tooltipBuilder((stack, tooltip) -> {
                if (GTUtil.isCtrlDown()) {
                    tooltip.add(Component.empty());
                    tooltip.add(manaHatchDataLang[0].translate());
                    tooltip.add(manaHatchDataLang[1].translate("1"));
                    tooltip.add(manaHatchDataLang[2].translate("10"));
                    tooltip.add(manaHatchDataLang[3].translate("每tick至多1%"));
                    tooltip.add(manaHatchDataLang[7].translate("网路LP转化"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化效率"));
                }
            })
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tier(UHV)
            .register();

    public static final MachineDefinition INDUSTRY_MANA_HATCH = REGISTRATE
            .manamachine("industry_manahatch",
                    holder -> new ManaHatch(holder, 320000, 10000, 200000, Integer.MAX_VALUE - 1))
            .cnLangValue("规模化工级·魔力凝聚仓")
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .tooltips(manahatchtooltip_1.translate())
            .tooltips(
                    manahatchtootip_base[0].translate(),
                    manahatchtootip_base[1].translate(),
                    manahatchtootip_base[2].translate(),
                    manahatchtootip_base[3].translate(),
                    manahatchtootip_base[4].translate(320000),
                    manahatchtootip_base[5].translate(100000),
                    manahatchtootip_base[6].translate(Integer.MAX_VALUE - 1))
            .tooltips(manahatchctrltooltip_1.translate())
            .tooltipBuilder((stack, tooltip) -> {
                if (GTUtil.isCtrlDown()) {
                    tooltip.add(Component.empty());
                    tooltip.add(manaHatchDataLang[0].translate());
                    tooltip.add(manaHatchDataLang[1].translate("1"));
                    tooltip.add(manaHatchDataLang[2].translate("20"));
                    tooltip.add(manaHatchDataLang[3].translate("每tick至多0.1%"));
                    tooltip.add(manaHatchDataLang[4].translate("100"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化"));
                    tooltip.add(manaHatchDataLang[7].translate("液态LP转化效率"));
                }
            })
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tier(UHV)
            .register();
    public static final MachineDefinition BM_HATCH = REGISTRATE
            .manamachine("bloodmanahatch",
                    holder -> new BloodManaHatch(holder, 66666, 6666666, 100, 666666, 666, 0.01))
            .cnLangValue("血染魔力凝聚仓")
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .tooltips(bloodmanahatchtooltip_1.translate())
            .tooltips(
                    bloodmanahatchtootip_base[0].translate(),
                    bloodmanahatchtootip_base[1].translate(),
                    bloodmanahatchtootip_base[2].translate(),
                    bloodmanahatchtootip_base[3].translate(),
                    bloodmanahatchtootip_base[4].translate(),
                    bloodmanahatchtootip_base[5].translate(666),
                    bloodmanahatchtootip_base[6].translate(66666),
                    bloodmanahatchtootip_base[7].translate(66666))
            .tooltips(manahatchctrltooltip_1.translate())
            .tooltipBuilder((stack, tooltip) -> {
                if (GTUtil.isCtrlDown()) {
                    tooltip.add(Component.empty());
                    tooltip.add(manaHatchDataLang[0].translate());
                    tooltip.add(manaHatchDataLang[1].translate("1"));
                    tooltip.add(manaHatchDataLang[7].translate("植物魔法魔力转化"));
                    tooltip.add(manaHatchDataLang[7].translate("戒指魔力转移效率"));
                    tooltip.add(manaHatchDataLang[4].translate("100"));
                    tooltip.add(manaHatchDataLang[5].translate("100"));
                    tooltip.add(manaHatchDataLang[6].translate("1%"));
                }
            })
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/bloodmanahatch"))
            .tier(UHV)
            .register();
    public static final MachineDefinition BM_HATCH_T2 = REGISTRATE
            .manamachine("soulmanahatch",
                    holder -> new BloodManaHatch(holder, 444444, 66666666, 100, 6666666, 6666, 0.1))
            .cnLangValue("铸魂魔力凝聚仓")
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .tooltips(bloodmanahatchtooltip_1.translate())
            .tooltips(
                    bloodmanahatchtootip_base[0].translate(),
                    bloodmanahatchtootip_base[1].translate(),
                    bloodmanahatchtootip_base[2].translate(),
                    bloodmanahatchtootip_base[3].translate(),
                    bloodmanahatchtootip_base[4].translate(),
                    bloodmanahatchtootip_base[5].translate(6666),
                    bloodmanahatchtootip_base[6].translate(444444),
                    bloodmanahatchtootip_base[7].translate(666666))
            .tooltips(manahatchctrltooltip_1.translate())
            .tooltipBuilder((stack, tooltip) -> {
                if (GTUtil.isCtrlDown()) {
                    tooltip.add(Component.empty());
                    tooltip.add(manaHatchDataLang[0].translate());
                    tooltip.add(manaHatchDataLang[1].translate("1"));
                    tooltip.add(manaHatchDataLang[7].translate("植物魔法魔力转化"));
                    tooltip.add(manaHatchDataLang[7].translate("戒指魔力转移效率"));
                    tooltip.add(manaHatchDataLang[4].translate("100"));
                    tooltip.add(manaHatchDataLang[5].translate("100"));
                    tooltip.add(manaHatchDataLang[6].translate("10%"));
                }
            })
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/bloodmanahatch"))
            .tier(ZPM)
            .register();
    public static final MachineDefinition CREATIVE_MANA_HATCH = REGISTRATE
            .manamachine("creative_manahatch", CreativeManaHatch::new)
            .cnLangValue("创造模式魔力凝聚仓")
            .langValue("Creative Mana Hatch")
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tooltipBuilder(CREATIVE_TOOLTIPS)
            .tier(MAX)
            .register();

    public static final MachineDefinition[] DIGITAL_WELL_OF_SUFFER = registerTieredMachines(
            "digital_well_of_suffer",
            (holder, tier) -> new DigitalWosMachine(holder, tier, (tiers) -> tiers * 32000),
            (tier, builder) -> builder
                    .langValue("%s Digital Well of Suffer".formatted(VNF[tier]))
                    .recipeType(CMRecipeTypes.DIGITAL_WELL_OF_SUFFER)
                    .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("digital_well_of_suffer"),
                            CMRecipeTypes.DIGITAL_WELL_OF_SUFFER))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeModifier(DigitalWosMachine::recipeModifier)
                    .workableTieredHullModel(CTNHMana.id("block/machine/digital_well_of_suffer"))
                    .tooltips(CTNHManaUtils.addMachineTooltips((digitalWosTooltip)))
                    .register(),
            GTValues.tiersBetween(LV, UV));
    /**
     * 宝石携刻机 ULV–UV（注册 id 仍为 gem_sublimator）。
     * recipeType 仅供 EMI 分类；实际加工由 {@link GemSublimatorMachine} 自定义 tick 驱动。
     * 外观暂复用高压釜 hull 模型。
     */
    public static final MachineDefinition[] GEM_SUBLIMATOR = registerTieredMachines(
            "gem_sublimator",
            GemSublimatorMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Gem Engraver".formatted(VNF[tier]))
                    .cnLangValue("%s宝石携刻机".formatted(VNF[tier]))
                    // 具体示例 + Tag 通用两条分类都挂上，查机器/查任意宝石都能在 EMI 看到
                    .recipeType(CMRecipeTypes.GEM_SUBLIMATOR_RECIPES)
                    .recipeType(CMRecipeTypes.GEM_SUBLIMATOR_GENERIC_RECIPES)
                    .editableUI(GemSublimatorMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("gem_sublimator"), tier))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .workableTieredHullModel(GTCEu.id("block/machines/autoclave"))
                    .tooltips(CTNHManaUtils.addMachineTooltips(gemSublimatorTooltip))
                    .tooltipBuilder((stack, tooltip) -> {
                        // 按本档电压写入「最高品质」与「通电增速」
                        tooltip.add(GemSublimatorMachine.maxQualityTooltipLine(tier));
                        tooltip.add(GemSublimatorMachine.poweredGainTooltipLine(tier));
                        tooltip.add(gemSublimatorCtrlHintTooltip.translate());
                        if (GTUtil.isCtrlDown()) {
                            tooltip.add(Component.empty());
                            for (Lang line : gemSublimatorCtrlTooltip) {
                                tooltip.add(line.translate());
                            }
                        }
                    })
                    .register(),
            GTValues.tiersBetween(ULV, UV));
    public static final MachineDefinition BROADCAST_HATCH = REGISTRATE
            .machine("redstone_signal_broadcast_hatch", RedstoneSignalBroadcastHatch::new)
            .cnLangValue("红石信号广播仓")
            .tier(EV)
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .abilities(CMPartsAbility.SIGNALHATCH)
            .tooltips(RedstoneSignalBroadcastHatch.broadcasthatchLang[1].translate(),
                    RedstoneSignalBroadcastHatch.broadcasthatchLang[2].translate())
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/bloodmanahatch"))
            .register();
    public static final MachineDefinition CENTRALCONTROL_BUS = REGISTRATE
            .machine("centralcontrol_bus", holder -> new CentralControlBus(holder, 4))
            .cnLangValue("中央存储控制总线")
            .tier(EV)
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .abilities(CMPartsAbility.SIGNALHATCH)
            .tooltips(CTNHManaUtils.addMachineTooltips(centralControlBusLang))
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/bloodmanahatch"))
            .register();
    public static final MachineDefinition EXTENDED_CENTRALCONTROL_BUS = REGISTRATE
            .machine("extended_centralcontrol_bus", holder -> new ExtendedCentralControlBus(holder, IV))
            .cnLangValue("拓展中央存储控制总线")
            .tier(IV)
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .abilities(CMPartsAbility.ExtendedCentralControlBus)
            .tooltips(CTNHManaUtils.addMachineTooltips(ExtendedCentralControlBus.extendedCentralControlBusLang))
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/bloodmanahatch"))
            .register();

    public static MachineDefinition[] registerTieredMachines(String name,
                                                             BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
                                                             BiFunction<Integer, CTNHMachineBuilder<MachineDefinition>, MachineDefinition> builder,
                                                             int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            var register = REGISTRATE
                    .machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                            holder -> factory.apply(holder, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }
}
