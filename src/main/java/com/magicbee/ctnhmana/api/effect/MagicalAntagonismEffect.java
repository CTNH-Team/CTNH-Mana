package com.magicbee.ctnhmana.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import com.magicbee.ctnhmana.networking.packets.AntagonismPacket;

import static com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK;

/**
 * 奥法拮抗：使携带者完全免疫魔法、元素类伤害。
 * 伤害免疫的具体判定见 {@link com.magicbee.ctnhmana.event.MagicalAntagonismEventHandler}。
 * 1.20.1 原版不会把怪物的效果同步给客户端，因此这里周期性推送数据包，
 * 由客户端 {@link com.magicbee.ctnhmana.client.render.AntagonismRender} 在头顶渲染图标。
 */
public class MagicalAntagonismEffect extends MobEffect {

    public MagicalAntagonismEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x6A3AB2);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 2 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return; // 只在服务端推送
        }
        MobEffectInstance instance = entity.getEffect(this);
        if (instance == null) {
            return;
        }
        NETWORK.sendToTrackingChunk(new AntagonismPacket(entity.getId(), this, instance.getDuration()),
                new LevelChunk(entity.level(), new ChunkPos(entity.getOnPos())));
    }
}
