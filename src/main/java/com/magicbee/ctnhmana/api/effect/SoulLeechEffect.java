package com.magicbee.ctnhmana.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;

/**
 * 灵魂汲取：周期性造成魔法伤害。
 */
public class SoulLeechEffect extends MobEffect {

    public SoulLeechEffect() {
        super(MobEffectCategory.HARMFUL, 0x5A2D82);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().isClientSide()) return;
        float damage = 4.4F + amplifier;
        if (livingEntity.getHealth() < 4.4F + amplifier || livingEntity.isDeadOrDying()) {
            var health = livingEntity.getMaxHealth() / 15;
            var chunk = WorldDemonWillHandler.getWillChunk(livingEntity.level(), livingEntity.getOnPos());
            if (chunk.getCurrentWill().getWill(EnumDemonWillType.DEFAULT) >= 110) return;
            chunk.getCurrentWill().addWill(EnumDemonWillType.DEFAULT, health,
                    110);
            return;
        }
        livingEntity.hurt(livingEntity.damageSources().magic(), damage);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int interval = 4 * 20;
        return duration % interval == 0;
    }
}
