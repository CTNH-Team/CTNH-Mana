package com.moguang.ctnhmana.utils;

import net.minecraft.network.chat.Component;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

public class CTNHManaUtils {
    public static java.util.List<net.minecraft.network.chat.Component> itemTooltipsAdd(Lang[] langs, List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }
}
