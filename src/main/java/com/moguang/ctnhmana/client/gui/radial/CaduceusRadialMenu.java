package com.moguang.ctnhmana.client.gui.radial;

import com.gregtechceu.gtceu.api.item.tool.GTToolType;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;
import com.moguang.ctnhmana.networking.packets.CaduceusPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

@SuppressWarnings("removal")
public final class CaduceusRadialMenu {

    private static ResourceLocation textureFor(GTToolType type) {
        return new ResourceLocation(CTNHMana.MODID, "textures/item/caduceus/caduceus_" + type.name + ".png");
    }

    /**
     * 创建 Caduceus 的径向菜单，图标为 item/caduceus 各形态贴图（直接 blit 资源）。
     * 选择后根据主手栈更新 NBT 并发送 CaduceusPacket。
     */
    public static RadialMenu<ResourceLocation> create() {
        List<RadialMenuSlot<ResourceLocation>> slotList = new ArrayList<>();
        for (GTToolType type : CaduceusItem.CYCLE_TYPES) {
            slotList.add(new RadialMenuSlot<>(
                    textureFor(type),
                    Component.translatable("item.gtceu.tool." + type.name)));
        }

        IntConsumer onSelect = slot -> {
            if (slot < 0 || slot >= CaduceusItem.CYCLE_TYPES.size()) return;
            GTToolType selectedType = CaduceusItem.CYCLE_TYPES.get(slot);
            com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK.sendToServer(
                    new CaduceusPacket(selectedType.name));
        };

        RadialMenu.DrawCallback<ResourceLocation> draw = (texture, graphics, x, y, size) -> {
            if (texture != null) {
                graphics.blit(texture, x, y, 0, 0, size, size, size, size);
            }
        };

        return new RadialMenu<>(onSelect, slotList, draw);
    }
}
