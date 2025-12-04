package com.moguang.ctnhmana.common.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.common.ManaMachine;

public class Zenith_Machine extends BaseManaMachine{
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            Zenith_Machine.class, BaseManaMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    public Zenith_Machine(IMachineBlockEntity holder, int consumption,int base_parallel) {
        super(holder, consumption);
        this.globalmetric.parallel+=base_parallel;
    }
}
