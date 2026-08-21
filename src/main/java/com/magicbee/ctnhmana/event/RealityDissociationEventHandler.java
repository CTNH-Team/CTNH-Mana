package com.magicbee.ctnhmana.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.magicbee.ctnhmana.common.entity.RoyalServantBee;
import com.magicbee.ctnhmana.registry.CMMobEffects;

/**
 * 现实解离效果处理：
 * <ul>
 * <li>非自然回血量每级减少 10%。</li>
 * <li>大蜜蜂/皇家蜜蜂造成伤害时，给目标施加 3 秒现实解离；若已有则重置时长并提高 1 级。</li>
 * </ul>
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RealityDissociationEventHandler {

    /** 每级非自然回血减少的比例。 */
    private static final float HEAL_REDUCTION_PER_LEVEL = 0.10F;
    /** 现实解离持续时长（3 秒）。 */
    private static final int DISSOCIATION_DURATION = 3 * 20;

    /**
     * 给目标施加一次现实解离：无则 0 级，有则等级 +1；无论何种情况都重置为 3 秒。
     *
     * @param target     被施加者
     * @param bonusLevel 额外叠加的等级（自爆用 +1，普通伤害 0）
     */
    public static void applyDissociation(LivingEntity target, int bonusLevel) {
        if (target.level().isClientSide) {
            return;
        }
        MobEffectInstance existing = target.getEffect(CMMobEffects.REALITY_DISSOCIATION.get());
        int newAmplifier = (existing != null ? existing.getAmplifier() + 1 : 0) + bonusLevel;
        target.addEffect(new MobEffectInstance(CMMobEffects.REALITY_DISSOCIATION.get(),
                DISSOCIATION_DURATION, newAmplifier, false, true));
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        Entity source = event.getSource().getEntity();
        // 大蜜蜂或皇家蜜蜂造成的伤害：施加 3 秒现实解离
        if (source instanceof GiantBee) {
            applyDissociation(event.getEntity(), 0);
        } else if (source instanceof RoyalServantBee) {
            applyDissociation(event.getEntity(), 0);
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return; // 只在服务端判定
        }
        MobEffectInstance effect = entity.getEffect(CMMobEffects.REALITY_DISSOCIATION.get());
        if (effect != null) {
            // 每级减少 10% 回血，最多减到 0
            float multiply = 1.0F - HEAL_REDUCTION_PER_LEVEL * (effect.getAmplifier() + 1);
            event.setAmount(event.getAmount() * Math.max(multiply, 0.0F));
        }
    }
}
