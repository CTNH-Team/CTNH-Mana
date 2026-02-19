package com.moguang.ctnhmana.networking.packets;

import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class CaduceusPacket implements IPacket {
    protected String toolTypeName;
    public CaduceusPacket()
    {

    }

    public CaduceusPacket(String toolTypeName) {
        this.toolTypeName = toolTypeName;
    }
    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(this.toolTypeName);
    }

    @Override
    public void decode(FriendlyByteBuf friendlyByteBuf) {
        this.toolTypeName= friendlyByteBuf.readUtf().toString();
    }

    @Override
    public void execute(IHandlerContext handler) {
        IPacket.super.execute(handler);
        ServerPlayer player = handler.getPlayer();
        if (player == null) return;

        ItemStack mainHand = player.getMainHandItem();
        if (!(mainHand.getItem() instanceof CaduceusItem)) return;

        GTToolType type = GTToolType.getTypes().get(this.toolTypeName);
        if (type == null || !CaduceusItem.CYCLE_TYPES.contains(type)) {
            CTNHMana.LOGGER.warn("Invalid Caduceus tool type from client: {}", this.toolTypeName);
            return;
        }

        CaduceusItem.setCurrentType(mainHand, type);
        player.inventoryMenu.sendAllDataToRemote();
    }
}