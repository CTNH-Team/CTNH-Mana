package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.moguang.ctnhmana.registry.parts.CMPartsAbility;
import com.moguang.ctnhmana.registry.parts.ManaHatch;

import static com.gregtechceu.gtceu.api.GTValues.UHV;
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.registerSimpleMachines;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.OVERLAY_ITEM_HATCH;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

public class CMMachines {
    static {
        REGISTRATE.creativeModeTab(() -> CMCreativeModeTabs.MACHINE);
    }


    public static void init() {

    }

    public static final MachineDefinition STERILE_CLEANROOM_MAINTENANCE_HATCH = REGISTRATE
            .manamachine("manahatch",
                    holder -> new ManaHatch(holder,100000,10000,10000,1000000,1000))
            .rotationState(RotationState.ALL)
            .abilities(CMPartsAbility.MANAHATCH)
            .workableTieredHullModel(GTCEu.id("block/machines/digital_well_of_suffer"))
            .tier(UHV)
            .register();
}
