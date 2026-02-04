package com.moguang.ctnhmana.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;

public class FlowerCakeMachine extends SimpleTieredMachine {
    public FlowerCakeMachine(IMachineBlockEntity holder,Object... args) {
        super(holder, 1, (tiers)->32000, args);
    }
}
