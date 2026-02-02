package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

public class ZenithMachine extends BaseManaMachine{
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ZenithMachine.class, BaseManaMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    public ZenithMachine(IMachineBlockEntity holder, int consumption, int base_parallel) {
        super(holder, consumption);
        this.globalmetric.parallel+=base_parallel;
    }
}
