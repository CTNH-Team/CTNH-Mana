package com.magicbee.ctnhmana.api.machine.trait;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

/** Persistent 32-lane circuit mapping owned exclusively by ExtendedCentralControlBus. */
public final class ExtendedControlBusCircuitTrait extends MachineTrait {

    @Persisted
    private final CustomItemStackHandler storage;

    public ExtendedControlBusCircuitTrait(MetaMachine machine, int laneCount) {
        super(machine);
        storage = new CustomItemStackHandler(laneCount);
        storage.setFilter(IntCircuitBehaviour::isIntegratedCircuit);
        storage.setOnContentsChanged(this::onChanged);
    }

    public CustomItemStackHandler getStorage() {
        return storage;
    }
}
