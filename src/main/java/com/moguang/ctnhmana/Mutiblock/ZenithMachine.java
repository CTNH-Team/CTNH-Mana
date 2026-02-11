package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

public class ZenithMachine extends BaseManaMachine{
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ZenithMachine.class, BaseManaMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    public int base_parallel=0;
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    public ZenithMachine(IMachineBlockEntity holder, int consumption, int base_parallel) {
        super(holder, consumption);
        this.base_parallel+=base_parallel;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.globalmetric.parallel+=base_parallel;
        this.globalmetric.speed+=0.25;
    }
}
