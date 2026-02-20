package com.moguang.ctnhmana.api.effect;

import com.moguang.ctnhmana.networking.packets.IndexTargetParticlePacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import static com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK;

/**
 * 指令对象效果。服务端在 tick 时向追踪该实体的玩家发粒子包，由客户端渲染。
 */
public class IndexTargetEffect extends MobEffect {
    public IndexTargetEffect() {
        super(MobEffectCategory.NEUTRAL, 0x9900FF);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 2 == 0;
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) return;
        NETWORK.sendToTrackingChunk(new IndexTargetParticlePacket(entity.getId()),new LevelChunk(entity.level(),new ChunkPos(entity.getOnPos())));
    }
}