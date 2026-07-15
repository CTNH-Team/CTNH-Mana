package com.moguang.ctnhmana.common.item.bloodmagicjade;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.network.chat.Component;

import lombok.Getter;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

public class JadeItem extends ComponentItem {

    @Getter
    public String type;
    @Getter
    public Lang upgradeName;

    public JadeItem(Properties properties, String type, Lang upgradeName) {
        super(properties);
        this.type = type;
        this.upgradeName = upgradeName;
    }

    public static List<Component> itemTooltipsAdd(Lang[] langs, List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }
}
