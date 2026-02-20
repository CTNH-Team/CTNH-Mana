package com.moguang.ctnhmana.networking.packets;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import com.moguang.ctnhmana.client.render.particle.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class IndexTargetBlockPacket implements IPacket{
    private int[] blockPos;

    public IndexTargetBlockPacket(int[] blockPos) {
        this.blockPos =blockPos;
    }


    public IndexTargetBlockPacket() {
    }
    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeVarIntArray(this.blockPos);
    }
    @Override
    public void decode(FriendlyByteBuf friendlyByteBuf) {
        this.blockPos = friendlyByteBuf.readVarIntArray();
    }
    @Override
    public void execute(IHandlerContext handler) {
        IPacket.super.execute(handler);
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        level.addParticle(
                ParticleRegistry.INDEX_TARGET.get(),
                blockPos[0]+0.5, blockPos[1]+0.5, blockPos[2],
                100.0D, 0.0D, 0.0D
        );
    }
}