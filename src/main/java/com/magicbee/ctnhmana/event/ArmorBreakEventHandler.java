package com.magicbee.ctnhmana.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.magicbee.ctnhmana.common.entity.RoyalServantBee;
import com.magicbee.ctnhmana.registry.CMMobEffects;

/**
 * 破甲效果处理：施加破甲时，若目标带有抗性提升，则减少一层。
 * 巨蜂/皇家侍从 Bee 造成伤害时，25% 概率对目标施加 1 级破甲 10 秒；
 * 已有破甲则层数 +1 并重置为 10 秒。
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorBreakEventHandler {

    /** 破甲时长（tick，10 秒） */
    private static final int ARMOR_BREAK_DURATION = 200;
    /** 施加概率 */
    private static final float ARMOR_BREAK_CHANCE = 0.25F;

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) {
            return;
        }
        // 自己人不触发
        if (target instanceof GiantBee || target instanceof RoyalServantBee) {
            return;
        }
        DamageSource source = event.getSource();
        LivingEntity attacker = null;
        if (source.getEntity() instanceof GiantBee || source.getEntity() instanceof RoyalServantBee) {
            attacker = (LivingEntity) source.getEntity();
        } else if (source.getDirectEntity() instanceof AbstractArrow arrow && arrow.getOwner() instanceof GiantBee) {
            attacker = (LivingEntity) arrow.getOwner();
        }
        if (attacker == null || !target.isAlive()) {
            return;
        }
        if (target.getRandom().nextFloat() >= ARMOR_BREAK_CHANCE) {
            return;
        }
        // 已有破甲：层数 +1 并重置时长；否则施加 1 级
        int amplifier = target.hasEffect(CMMobEffects.ARMOR_BREAK.get()) ?
                target.getEffect(CMMobEffects.ARMOR_BREAK.get()).getAmplifier() + 1 : 0;
        target.addEffect(new MobEffectInstance(CMMobEffects.ARMOR_BREAK.get(), ARMOR_BREAK_DURATION, amplifier,
                false, true));
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return; // 只在服务端判定
        }
        if (event.getEffectInstance().getEffect() != CMMobEffects.ARMOR_BREAK.get()) {
            return;
        }
        MobEffectInstance resistance = entity.getEffect(MobEffects.DAMAGE_RESISTANCE);
        if (resistance == null) {
            return;
        }
        // 减少一层抗性提升，1 层时直接移除
        entity.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        if (resistance.getAmplifier() > 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,
                    resistance.getDuration(), resistance.getAmplifier() - 1, false, true));
        }
    }
}
