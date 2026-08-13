package com.magicbee.ctnhmana.networking.packets;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.magicbee.ctnhmana.registry.CMParticleTypes;

public class IndexTargetBlockPacket implements IPacket {

    private int[] blockPos;

    public IndexTargetBlockPacket(int[] blockPos) {
        this.blockPos = blockPos;
    }

    public IndexTargetBlockPacket() {}

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeVarIntArray(this.blockPos);
    }

    @Override
    public void decode(FriendlyByteBuf friendlyByteBuf) {
        this.blockPos = friendlyByteBuf.readVarIntArray();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        IPacket.super.execute(handler);
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        level.addParticle(
                CMParticleTypes.INDEX_TARGET.get(),
                blockPos[0] + 0.5, blockPos[1] + 0.5, blockPos[2],
                100.0D, 0.0D, 0.0D);
    }
}
