package com.moguang.ctnhmana.client.gui.radial;

import net.minecraft.network.chat.Component;


public record RadialMenuSlot<T>(T primaryIcon, Component slotName) {
}