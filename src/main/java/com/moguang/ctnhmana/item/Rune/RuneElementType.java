package com.moguang.ctnhmana.item.Rune;

import net.minecraft.util.StringRepresentable;

import lombok.Getter;

public enum RuneElementType implements StringRepresentable {

    FIRE("fire"),
    WATER("water"),
    EARTH("earth"),
    WIND("wind"),
    SIN("sin");

    @Getter
    private final String serializedName;

    RuneElementType(String name) {
        this.serializedName = name;
    }
}
