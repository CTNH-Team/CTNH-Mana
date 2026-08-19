package com.magicbee.ctnhmana.api.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.mixin.minecraft.EntityAccessor;

/**
 * 缚地：每 0.5 秒解除目标的飞行状态（鞘翅滑翔/创造模式飞行）并施加向下的大动量，将其压回地面。
 */
public class RootedEffect extends MobEffect {

    /** 每 0.5 秒触发一次。 */
    private static final int TICK_INTERVAL = 10;
    /** 每次施加的向下动量。 */
    private static final double DOWNWARD_MOMENTUM = 1.5D;
    /** 原版 Entity.FLAG_FALL_FLYING（protected，清除经 mixin EntityAccessor 暴露的 setSharedFlag）。 */
    private static final int FALL_FLYING_FLAG = 7;

    public RootedEffect() {
        super(MobEffectCategory.HARMFUL, 0x6B4F2A);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % TICK_INTERVAL == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return; // 只在服务端执行
        }
        // 解除飞行状态：鞘翅滑翔与创造模式飞行
        if (entity.isFallFlying()) {
            ((EntityAccessor) entity).ctnhmana$setSharedFlag(FALL_FLYING_FLAG, false);
        }
        if (entity instanceof Player player && player.getAbilities().flying) {
            player.getAbilities().flying = false;
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.onUpdateAbilities();
            }
        }
        // 施加向下的大动量
        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x, motion.y - DOWNWARD_MOMENTUM, motion.z);
    }
}
