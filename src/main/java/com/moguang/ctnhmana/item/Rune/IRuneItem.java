package com.moguang.ctnhmana.item.Rune;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import net.minecraft.world.item.Item;

import java.util.List;

public class IRuneItem extends ComponentItem {
    public int tier;
    public List<RuneElementType> element;
    public IRuneItem(Properties properties, RuneElementType element, int tier)
    {
        super(properties);
        this.tier=tier;
        this.element.add(element);
    }
    public IRuneItem(Properties properties,List<RuneElementType> element,int tier)

    {
        super(properties);
        this.tier=tier;
        this.element=element;
    }
}
