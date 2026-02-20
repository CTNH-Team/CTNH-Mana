package com.moguang.ctnhmana.networking.packets;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import com.moguang.ctnhmana.client.render.particle.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * S2C：服务端发此包后，客户端在指定实体位置生成指令对象粒子。
 * 不包含注册逻辑，需在 channel 中自行 registerMessage + 用 PacketDistributor 发送。
 */
public class IndexTargetParticlePacket implements IPacket {
    private int entityId;

    public IndexTargetParticlePacket(int entityId) {
        this.entityId = entityId;
    }


    public IndexTargetParticlePacket() {
    }
    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.entityId);
    }
    @Override
    public void decode(FriendlyByteBuf friendlyByteBuf) {
        this.entityId= friendlyByteBuf.readInt();
    }
    @Override
    public void execute(IHandlerContext handler) {
        IPacket.super.execute(handler);
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        Entity entity = level.getEntity(this.entityId);
        if (!(entity instanceof LivingEntity living)) return;
        double x = living.getX();
        double y = living.getY() + living.getBbHeight() * 0.5D + 1.0D;
        double z = living.getZ();
        level.addParticle(
                ParticleRegistry.INDEX_TARGET.get(),
                x, y, z,
                0.0D, 0.0D, 0.0D
        );
    }
}