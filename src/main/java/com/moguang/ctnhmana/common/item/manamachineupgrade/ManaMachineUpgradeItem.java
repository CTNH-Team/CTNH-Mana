package com.moguang.ctnhmana.common.item.manamachineupgrade;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;

import com.moguang.ctnhmana.common.multi.BaseManaMachine;
import lombok.Getter;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

public class ManaMachineUpgradeItem extends ComponentItem {

    @Getter
    public String type;
    @Getter
    public Lang updateName;

    public ManaMachineUpgradeItem(Properties properties, String type, Lang updateName) {
        super(properties);
        this.type = type;
        this.updateName = updateName;
    }

    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe,
                                                          BaseManaMachine machine) {
        return metric;
    }

    public BaseManaMachine.MachineMetric calculateNormalUpgrade(BaseManaMachine.MachineMetric metric,
                                                                BaseManaMachine machine) {
        return metric;
    }

    public static List<Component> itemTooltipsAdd(Lang[] langs, List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }
}
