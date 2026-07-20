package com.moguang.ctnhmana.common.item.rune;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import java.util.List;

public class IRuneItem extends ComponentItem {

    public int tier;
    public List<RuneElementType> element;

    public IRuneItem(Properties properties, RuneElementType element, int tier) {
        super(properties);
        this.tier = tier;
        this.element.add(element);
    }

    public IRuneItem(Properties properties, List<RuneElementType> element, int tier)

    {
        super(properties);
        this.tier = tier;
        this.element = element;
    }
}
