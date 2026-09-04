package com.magicbee.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

public class ZenithMachine extends BaseManaMultiBlockMachine {

    @Persisted
    public int base_parallel = 0;

    public ZenithMachine(IMachineBlockEntity holder, int consumption, int base_parallel) {
        super(holder, consumption);
        this.base_parallel += base_parallel;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.globalmetric.parallel += base_parallel;
        this.globalmetric.speed = 0.77;
    }
}
