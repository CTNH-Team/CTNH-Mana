package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.Mutiblock.ICentralStorageMachine;
import com.moguang.ctnhmana.Mutiblock.parts.CentralControlBus;
import com.moguang.ctnhmana.Mutiblock.parts.RedstoneSignalBroadcastHatch;
import com.moguang.ctnhmana.common.DigitalWosMachine;
import com.moguang.ctnhmana.Mutiblock.parts.CMPartsAbility;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatches.BloodManaHatch;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatches.SparkManaHatch;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties.IS_FORMED;
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.registerSimpleMachines;
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.registerTieredMachines;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.*;

public class CMMachines {
    static {
        REGISTRATE.creativeModeTab(() -> CMCreativeModeTabs.MACHINE);
    }


    public static void init() {

    }
    public static final MachineDefinition MANA_HATCH = REGISTRATE
            .manamachine("manahatch",
                    holder -> new ManaHatch(holder,10000,10000,100000,6400))
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
                    manahatchtootip_base[6].translate(8000)
                    )
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel("manahatch")
            .tier(HV)
            .register();
    public static final MachineDefinition ADVANCED_MANA_HATCH = REGISTRATE
            .manamachine("elf_manahatch",
                    holder -> new SparkManaHatch(holder,40000,100000,500000,32000,7500))
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
                    sparkmanahatchtootip_base[7].translate(32000)
            )
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tier(EV)
            .register();
    public static final MachineDefinition GIGA_MANA_HATCH = REGISTRATE
            .manamachine("giga_manahatch",
                    holder -> new SparkManaHatch(holder,160000,100000,5000000,128000,20000))
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
                    sparkmanahatchtootip_base[6].translate(2500000),
                    sparkmanahatchtootip_base[7].translate(128000)
            )
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tier(UHV)
            .register();
    public static final MachineDefinition INDUSTRY_MANA_HATCH = REGISTRATE
            .manamachine("industry_manahatch",
                    holder -> new ManaHatch(holder,320000,10000,200000,Integer.MAX_VALUE-1))
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
                    manahatchtootip_base[6].translate(Integer.MAX_VALUE-1)
            )
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tier(UHV)
            .register();
    public static final MachineDefinition BM_HATCH = REGISTRATE
            .manamachine("bloodmanahatch",
                    holder -> new BloodManaHatch(holder,666666,6666666,100,666666,100,0.001))
            .cnLangValue("染血魔力凝聚仓")
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .tooltips(bloodmanahatchtooltip_1.translate())
            .tooltips(
                    bloodmanahatchtootip_base[0].translate(),
                    bloodmanahatchtootip_base[1].translate(),
                    bloodmanahatchtootip_base[2].translate(),
                    bloodmanahatchtootip_base[3].translate(),
                    bloodmanahatchtootip_base[4].translate(),
                    bloodmanahatchtootip_base[5].translate(100),
                    bloodmanahatchtootip_base[6].translate(666666),
                    bloodmanahatchtootip_base[7].translate(666666)
            )
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/bloodmanahatch"))
            .tier(UHV)
            .register();

    public static final MachineDefinition[] DIGITAL_WELL_OF_SUFFER = registerTieredMachines("digital_well_of_suffer",
            (holder, tier) -> new DigitalWosMachine(holder,tier,(tiers) -> tiers * 32000),
            (tier,builder) -> builder
                    .langValue("%s Digital Well of Suffer".formatted(VNF[tier]))
                    .recipeType(CMRecipeTypes.DIGITAL_WELL_OF_SUFFER)
                    .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("digital_well_of_suffer"),CMRecipeTypes.DIGITAL_WELL_OF_SUFFER))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeModifier(DigitalWosMachine::recipeModifier)
                    .workableTieredHullModel(GTCEu.id("block/machines/digital_well_of_suffer"))
                    .tooltips(Component.translatable("ctnh.dwof.tooltip").withStyle(ChatFormatting.YELLOW))
                    .register(),
            GTValues.tiersBetween(LV,UV));
    public static final MachineDefinition BROADCAST_HATCH=REGISTRATE
            .machine("redstone_signal_broadcast_hatch", RedstoneSignalBroadcastHatch::new)
            .cnLangValue("红石信号广播仓")
            .tier(EV)
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .abilities(CMPartsAbility.SIGNALHATCH)
            .tooltips(RedstoneSignalBroadcastHatch.broadcasthatchLang[1].translate(),RedstoneSignalBroadcastHatch.broadcasthatchLang[2].translate())
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/bloodmanahatch"))
            .register();
    public static final MachineDefinition CENTRALCONTROL_BUS=REGISTRATE
            .machine("centralcontrol_bus", holder->new CentralControlBus(holder,4))
            .cnLangValue("中央存储控制总线")
            .tier(EV)
            .modelProperty(IS_FORMED, false)
            .rotationState(RotationState.ALL)
            .abilities(CMPartsAbility.SIGNALHATCH)
            .tooltips(CTNHManaUtils.addMachineTooltips(CentralControlBus.centralControlBusLang))
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/bloodmanahatch"))
            .register();

}
