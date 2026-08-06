package com.moguang.ctnhmana.networking.packets;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.moguang.ctnhmana.client.ZenithInvadeClient;
import com.moguang.ctnhmana.common.event.zenith.ZenithInvadeEvent;

import java.util.UUID;

/**
 * 虚境入侵 S2C 同步包。
 * action: 0=开始/刷新，1=结束。
 */
public class ZenithInvadePacket implements IPacket {

    public static final byte ACTION_START = 0;
    public static final byte ACTION_STOP = 1;

    private byte action;
    private UUID id;
    private BlockPos sourcePos;
    private int totalDuration;
    private int remainingTicks;
    private boolean playIntro;

    public ZenithInvadePacket() {}

    public static ZenithInvadePacket start(ZenithInvadeEvent event) {
        ZenithInvadePacket packet = new ZenithInvadePacket();
        packet.action = ACTION_START;
        packet.id = event.id;
        packet.sourcePos = event.sourcePos;
        packet.totalDuration = event.totalDuration;
        packet.remainingTicks = event.remainingTicks;
        packet.playIntro = event.playIntro;
        return packet;
    }

    public static ZenithInvadePacket stop(UUID id) {
        ZenithInvadePacket packet = new ZenithInvadePacket();
        packet.action = ACTION_STOP;
        packet.id = id;
        packet.sourcePos = BlockPos.ZERO;
        packet.totalDuration = 0;
        packet.remainingTicks = 0;
        packet.playIntro = false;
        return packet;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(action);
        buf.writeUUID(id);
        buf.writeBlockPos(sourcePos);
        buf.writeVarInt(totalDuration);
        buf.writeVarInt(remainingTicks);
        buf.writeBoolean(playIntro);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        action = buf.readByte();
        id = buf.readUUID();
        sourcePos = buf.readBlockPos();
        totalDuration = buf.readVarInt();
        remainingTicks = buf.readVarInt();
        playIntro = buf.readBoolean();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        IPacket.super.execute(handler);
        if (action == ACTION_START) {
            ZenithInvadeClient.startOrUpdate(id, sourcePos, totalDuration, remainingTicks, playIntro);
        } else if (action == ACTION_STOP) {
            ZenithInvadeClient.stop(id);
        }
    }
}
