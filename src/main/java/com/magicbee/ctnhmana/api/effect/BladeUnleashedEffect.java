package com.magicbee.ctnhmana.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class BladeUnleashedEffect extends MobEffect {

    public BladeUnleashedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x9900FF);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map,
                                         int amplifier) {
        super.removeAttributeModifiers(entity, map, amplifier);
        if (!(entity instanceof Player player) || player.level().isClientSide()) {
            return;
        }
    }
}
