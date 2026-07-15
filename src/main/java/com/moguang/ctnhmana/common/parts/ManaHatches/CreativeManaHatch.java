package com.moguang.ctnhmana.common.parts.ManaHatches;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.moguang.ctnhmana.common.parts.ManaHatch;

public class CreativeManaHatch extends ManaHatch {

    private static final long CREATIVE_MANA = 1_000_000L;

    public CreativeManaHatch(IMachineBlockEntity holder) {
        super(holder, CREATIVE_MANA, 0, 0, 1);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        refillMana();
    }

    @Override
    public void ConvertMana() {
        refillMana();
    }

    @Override
    public void consumeMana(long consume) {}

    @Override
    public boolean consumeManaIfEnough(long consume) {
        return true;
    }

    private void refillMana() {
        Mana = maxMana;
    }
}
