package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.Mutiblock.parts.CMPartsAbility;
import com.moguang.ctnhmana.common.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.common.Mutiblock.parts.ManaHatches.BloodManaHatch;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import static com.gregtechceu.gtceu.api.GTValues.UHV;
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.registerSimpleMachines;
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
                    holder -> new ManaHatch(holder,10000,10000,10000,100000,1000))
            .cnLangValue("魔力凝聚仓")
            .rotationState(RotationState.ALL)
            .tooltips(manahatchtooltip_1.translate())
            .tooltips(
                    manahatchtootip_base[0].translate(),
                    manahatchtootip_base[1].translate(),
                    manahatchtootip_base[2].translate(),
                    manahatchtootip_base[3].translate(),
                    manahatchtootip_base[4].translate(10000),
                    manahatchtootip_base[5].translate(100000),
                    manahatchtootip_base[6].translate(1000)
                    )
            .abilities(CMPartsAbility.MANAHATCH)
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tier(UHV)
            .register();
    public static final MachineDefinition BM_HATCH = REGISTRATE
            .manamachine("bloodmanahatch",
                    holder -> new BloodManaHatch(holder,666666,6666666,100,666666,100,0.001))
            .cnLangValue("染血魔力凝聚仓")
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
            .overlayTieredHullModel(CTNHMana.id("block/machine/part/manahatch"))
            .tier(UHV)
            .register();
}
