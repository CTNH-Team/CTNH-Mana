package com.magicbee.ctnhmana.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * 现实解离：攻击力 +等级%、护甲每级 -3、非自然回血每级 -15%（见
 * {@link com.magicbee.ctnhmana.event.RealityDissociationEventHandler}）。
 * 当效果剩余 1 秒时，降低 1 级并重置为 3 秒；1 级时直接移除。
 */
public class RealityDissociationEffect extends MobEffect {

    /** 攻击力修饰符 UUID（MULTIPLY，每级 +1% = 等级%）。 */
    private static final String ATTACK_UUID = "ab9c1d2e-3f4a-4b5c-8d6e-7f0a1b2c3d4e";
    /** 护甲修饰符 UUID（ADDITION，每级 -3）。 */
    private static final String ARMOR_UUID = "bca2d3e4-5f6b-4c7d-9e8f-0a1b2c3d4e5f";
    /** 每个等级的攻击力加成（百分比分子）。 */
    private static final double ATTACK_BONUS_PER_LEVEL = 0.01D;
    /** 每个等级的护甲减益。 */
    private static final double ARMOR_REDUCTION_PER_LEVEL = -3.0D;
    /** 剩余 1 秒（20 tick）时触发降级重置。 */
    private static final int RESET_AT_TICKS = 20;
    /** 重置后的持续时长（3 秒）。 */
    private static final int RESET_DURATION = 3 * 20;

    public RealityDissociationEffect() {
        super(MobEffectCategory.HARMFUL, 0x5B4B7A);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_UUID,
                ATTACK_BONUS_PER_LEVEL, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR, ARMOR_UUID,
                ARMOR_REDUCTION_PER_LEVEL, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        if (modifier.getId().toString().equals(ARMOR_UUID)) {
            return ARMOR_REDUCTION_PER_LEVEL * (amplifier + 1); // 护甲每级 -3
        }
        return ATTACK_BONUS_PER_LEVEL * (amplifier + 1); // 攻击力 +等级%
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration <= RESET_AT_TICKS; // 剩余 ≤1 秒时逐 tick 检查降级
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return; // 只在服务端执行
        }
        MobEffectInstance instance = entity.getEffect(this);
        if (instance == null || instance.getDuration() > RESET_AT_TICKS) {
            return;
        }
        // 降低 1 级并重置为 3 秒；1 级时直接移除
        entity.removeEffect(this);
        if (instance.getAmplifier() > 0) {
            entity.addEffect(new MobEffectInstance(this, RESET_DURATION, instance.getAmplifier() - 1, false, true));
        }
    }
}
