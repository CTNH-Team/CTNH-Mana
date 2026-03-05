package com.moguang.ctnhmana.utils;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.ArrayList;
import java.util.List;

public class CTNHManaUtils {

    public static java.util.List<net.minecraft.network.chat.Component> itemTooltipsAdd(Lang[] langs,
                                                                                       List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }

    public static ItemStack insertItemToOutput(NotifiableItemStackHandler handler, ItemStack stack, boolean simulate) {
        if (handler == null || stack.isEmpty()) {
            return stack;
        }
        if (!stack.isStackable()) {
            return insertToEmpty(handler, stack, simulate);
        }

        IntList emptySlots = new IntArrayList();
        int slots = handler.getSlots();

        for (int i = 0; i < slots; i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (slotStack.isEmpty()) {
                emptySlots.add(i);
            } else if (ItemHandlerHelper.canItemStacksStack(stack, slotStack)) {
                stack = handler.insertItemInternal(i, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }

        for (int slot : emptySlots) {
            stack = handler.insertItemInternal(slot, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    /**
     * Only inerts to empty slots. Perfect for not stackable items
     */
    public static ItemStack insertToEmpty(NotifiableItemStackHandler handler, ItemStack stack, boolean simulate) {
        if (handler == null || stack.isEmpty()) {
            return stack;
        }
        int slots = handler.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (slotStack.isEmpty()) {
                stack = handler.insertItemInternal(i, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }

    public static List<Component> addManaMachineTooltips(Lang[] langs, int consumption) {
        List<Component> list = new ArrayList<>();
        int y = 0;
        for (Lang lang : langs) {
            if (y != 3) list.add(lang.translate());
            if (y == 3) list.add(lang.translate(consumption));
            y += 1;
        }
        return list;
    }

    public static List<Component> addMachineTooltips(Lang[] langs) {
        List<Component> list = new ArrayList<>();
        int y = 0;
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }
}
