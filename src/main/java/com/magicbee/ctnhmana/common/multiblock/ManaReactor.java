package com.magicbee.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

public class ManaReactor extends BaseManaMachine {

    public ManaReactor(IMachineBlockEntity holder, int consumption) {
        super(holder, consumption);
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        return true;
    }
}
