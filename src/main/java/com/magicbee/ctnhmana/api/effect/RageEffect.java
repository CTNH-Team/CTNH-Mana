package com.magicbee.ctnhmana.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/** 激怒：巨蜂受击积累的怒意标记（等级 0-2 对应 1-3 层），满 3 层时进入狂暴状态 */
public class RageEffect extends MobEffect {

    public RageEffect() {
        super(MobEffectCategory.HARMFUL, 0xC0392B);
    }
}
