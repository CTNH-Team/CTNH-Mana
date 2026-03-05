package com.moguang.ctnhmana.networking.packets;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;

public class IndexFortunaPacket implements IPacket {

    private int id;

    @Override
    public void decode(FriendlyByteBuf friendlyByteBuf) {
        this.id = friendlyByteBuf.readVarInt();
    }

    public IndexFortunaPacket(int id) {
        this.id = id;
    }

    public IndexFortunaPacket() {}

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeVarInt(this.id);
    }

    @Override
    public void execute(IHandlerContext handler) {
        IPacket.super.execute(handler);
        ServerPlayer player = handler.getPlayer();
        if (player == null) return;
        ItemStack mainHand = player.getMainHandItem();
        if (!(mainHand.getItem() instanceof CaduceusItem)) return;
        CaduceusItem.switchFortuna(mainHand, player);
        player.inventoryMenu.sendAllDataToRemote();
    }
}
