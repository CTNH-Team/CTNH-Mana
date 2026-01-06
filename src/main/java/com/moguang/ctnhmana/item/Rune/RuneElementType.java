package com.moguang.ctnhmana.item.Rune;

import lombok.Getter;
import net.minecraft.util.StringRepresentable;

public enum RuneElementType implements StringRepresentable {

    FIRE("fire"),
    WATER("water"),
    EARTH("earth"),
    WOOD("wood"),
    SIN("sin")
    ;
    @Getter
    private final String serializedName;

    RuneElementType(String name)
    {
        this.serializedName=name;
    }

}
