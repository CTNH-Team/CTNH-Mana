package com.moguang.ctnhmana.networking.packets;

import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class IndexFortunaPacket implements IPacket {
    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {

    }
    public IndexFortunaPacket() {
    }
    @Override
    public void decode(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void execute(IHandlerContext handler) {
        IPacket.super.execute(handler);
        ServerPlayer player = handler.getPlayer();
        if (player == null) return;
        ItemStack mainHand = player.getMainHandItem();
        if (!(mainHand.getItem() instanceof CaduceusItem)) return;
        CaduceusItem.switchFortuna(mainHand,player);
        player.inventoryMenu.sendAllDataToRemote();
    }
}