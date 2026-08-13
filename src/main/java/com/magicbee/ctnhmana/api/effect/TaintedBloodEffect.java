package com.magicbee.ctnhmana.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.List;

/**
 * 污血：造成伤害大幅提升，并激活紧闭的第三只眼效果。
 * 期间受到致死伤害时将生命保留至 1。
 */
public class TaintedBloodEffect extends MobEffect {

    public TaintedBloodEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x8B0000);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!(livingEntity instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        // 紧闭的第三只眼：削弱周围生物的索敌
        List<LivingEntity> entityList = player.level().getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.forCombat().range(8),
                player,
                player.getBoundingBox().inflate(8));
        for (LivingEntity entity : entityList) {
            if (entity instanceof Mob mob) {
                var followRange = mob.getAttribute(Attributes.FOLLOW_RANGE);
                if (followRange != null) {
                    followRange.setBaseValue(1);
                }
                if (mob.getTarget() == player) {
                    mob.setTarget(null);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
