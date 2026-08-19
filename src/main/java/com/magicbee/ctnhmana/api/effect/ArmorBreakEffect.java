package com.magicbee.ctnhmana.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * 破甲：每级使目标护甲 -3（修饰符 -3 随默认公式按等级缩放，即 -3 × 等级）；
 * 施加时若目标带有抗性提升，会减少一层（见
 * {@link com.magicbee.ctnhmana.event.ArmorBreakEventHandler}）。
 */
public class ArmorBreakEffect extends MobEffect {

    /** 护甲修饰符 UUID。 */
    private static final String ARMOR_UUID = "a1b2c3d4-5e6f-4a7b-8c9d-0e1f2a3b4c5d";

    public ArmorBreakEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B7D5C);
        this.addAttributeModifier(Attributes.ARMOR, ARMOR_UUID, -3.0D, AttributeModifier.Operation.ADDITION);
    }
}
