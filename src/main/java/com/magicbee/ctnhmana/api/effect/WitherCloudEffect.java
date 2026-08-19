package com.magicbee.ctnhmana.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * 凋灵云效果：云内生物每 20 tick（1 秒）受到 5 点凋灵伤害（凋灵伤害类型）。
 * 由凋灵兔葵投掷物落地生成的凋灵云施加，与普通凋灵效果叠加。
 */
public class WitherCloudEffect extends MobEffect {

    public WitherCloudEffect() {
        super(MobEffectCategory.HARMFUL, 0x2B2B2B);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.tickCount % 20 == 0) {
            entity.hurt(entity.damageSources().wither(), 5.0F);
        }
    }
}
