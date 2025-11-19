package com.moguang.ctnhmana.item;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class ComplexNotifiableItemStackHandler extends NotifiableItemStackHandler {
    @Persisted
    @DescSynced
    public final ComplexItemStackHandler storage;

    public ComplexNotifiableItemStackHandler(MetaMachine machine, int slots, @NotNull IO handlerIO,@NotNull IO capabilityIO,IntFunction<ComplexItemStackHandler> storageFactory) {
        super(machine, slots, handlerIO,capabilityIO);
        this.storage = (ComplexItemStackHandler)storageFactory.apply(slots);
        this.storage.setOnContentsChanged(this::onContentsChanged);
    }
}
