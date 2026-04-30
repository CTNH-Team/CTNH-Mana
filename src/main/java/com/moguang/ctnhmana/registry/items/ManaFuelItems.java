package com.moguang.ctnhmana.registry.items;

import com.moguang.ctnhmana.item.ManaFuelStick.IManaFuelStick;
import com.moguang.ctnhmana.registry.CMTags;
import com.tterrag.registrate.util.entry.ItemEntry;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

public final class ManaFuelItems {

    private ManaFuelItems() {}

    public static ItemEntry<IManaFuelStick> registerSparkStick() {
        return REGISTRATE
                .item("spark_stick", properties -> new IManaFuelStick(properties, 5, 1, 1000))
                .cnlang("火花级魔力燃料棒")
                .tag(CMTags.MANA_FUEL_STACK)
                .register();
    }
}
