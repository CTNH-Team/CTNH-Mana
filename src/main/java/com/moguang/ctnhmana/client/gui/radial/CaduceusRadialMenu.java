package com.moguang.ctnhmana.client.gui.radial;

import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;
import com.moguang.ctnhmana.networking.packets.CaduceusPacket;
import com.moguang.ctnhmana.registry.CMItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * Caduceus 径向菜单：仅主图标，无副图标。
 * 提供槽位列表与选择后更新 NBT + 发包同步。
 */
public final class CaduceusRadialMenu {

    private static final java.util.Map<GTToolType, ItemStack> ICON_CACHE = new java.util.HashMap<>();

    /** 为每种工具类型选一个代表图标（仅用于菜单显示） */
    private static ItemStack iconFor(GTToolType type) {
        return ICON_CACHE.computeIfAbsent(type, t -> {
            switch (t.name) {
                case "sword":
                    return new ItemStack(Items.DIAMOND_SWORD);
                case "pickaxe":
                    return new ItemStack(Items.DIAMOND_PICKAXE);
                case "shovel":
                    return new ItemStack(Items.DIAMOND_SHOVEL);
                case "axe":
                    return new ItemStack(Items.DIAMOND_AXE);
                case "hoe":
                    return new ItemStack(Items.DIAMOND_HOE);
                case "wrench":
                    return new ItemStack(Items.STICK);
                case "scythe":
                case "saw":
                case "file":
                case "screwdriver":
                case "wire_cutter":
                case "knife":
                case "plunger":
                    return new ItemStack(CMItems.CADUCEUS.get());
                default:
                    return new ItemStack(CMItems.CADUCEUS.get());
            }
        });
    }

    /**
     * 创建 Caduceus 的径向菜单（仅主图标）。
     * 选择后会更新 stack 的 NBT 并发送 CaduceusPacket 到服务端。
     */
    public static RadialMenu<ItemStack> create(ItemStack caduceusStack) {
        List<RadialMenuSlot<ItemStack>> slotList = new ArrayList<>();
        for (GTToolType type : CaduceusItem.CYCLE_TYPES) {
            slotList.add(new RadialMenuSlot<>(
                iconFor(type),
                Component.translatable("item.gtceu.tool." + type.name)
            ));
        }

        IntConsumer onSelect = slot -> {
            if (slot < 0 || slot >= CaduceusItem.CYCLE_TYPES.size()) return;
            GTToolType selected = CaduceusItem.CYCLE_TYPES.get(slot);
            CaduceusItem.setCurrentType(caduceusStack, selected);
            com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK.sendToServer(
                new CaduceusPacket(selected.name)
            );
        };

        RadialMenu.DrawCallback<ItemStack> draw = (icon, graphics, x, y, size) -> {
            if (icon != null && !icon.isEmpty()) {
                graphics.renderItem(icon, x, y);
                graphics.renderItemDecorations(
                    net.minecraft.client.Minecraft.getInstance().font,
                    icon,
                    x,
                    y
                );
            }
        };

        return new RadialMenu<>(onSelect, slotList, draw);
    }
}
