package com.magicbee.ctnhmana.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMMobEffects;

import java.util.List;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulLeechEventHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Level level = victim.level();
        if (level.isClientSide()) return;

        MobEffectInstance soulLeech = victim.getEffect(CMMobEffects.SOUL_LEECH.get());
        if (soulLeech == null) return;

        int amplifier = soulLeech.getAmplifier();
        float burstDamage = 2.0F + amplifier;
        double radius = 3.0D + amplifier * 1.5D;

        // 死亡触发：周围生物受到魔法伤害
        List<LivingEntity> nearby = level.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(victim.blockPosition()).inflate(radius),
                e -> e != victim && e.isAlive());
        for (LivingEntity target : nearby) {
            target.hurt(level.damageSources().magic(), burstDamage);
        }

        // 施加汲取者奖励：击杀者（若是生物）回复生命
        if (event.getSource().getEntity() instanceof LivingEntity killer && killer.isAlive()) {
            killer.heal(2.0F + amplifier * 2.0F);
        }

        // 视觉反馈
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.SOUL,
                    victim.getX(), victim.getY() + 0.5D, victim.getZ(),
                    24 + amplifier * 8,
                    0.5D, 0.35D, 0.5D,
                    0.02D);
            serverLevel.sendParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    victim.getX(), victim.getY() + 0.5D, victim.getZ(),
                    8 + amplifier * 4,
                    0.25D, 0.25D, 0.25D,
                    0.01D);
        }
    }
}
