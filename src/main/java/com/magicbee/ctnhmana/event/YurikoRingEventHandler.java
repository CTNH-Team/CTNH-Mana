package com.magicbee.ctnhmana.event;

import com.gregtechceu.gtceu.common.data.GTDamageTypes;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.common.item.equipment.YurikoRingItem;
import com.magicbee.ctnhmana.registry.CMItems;
import vazkii.botania.common.handler.EquipmentHandler;

import java.util.List;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class YurikoRingEventHandler {

    @SubscribeEvent()
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource damageSource = event.getSource();
        LivingEntity target = event.getEntity();
        float originalDamage = event.getAmount();
        double yuriko_chance = 1.0;
        if (damageSource.getEntity() instanceof Player player) {
            var item = EquipmentHandler.findOrEmpty(CMItems.YURIKO_RING.asItem(), player);
            if (item.getItem() instanceof YurikoRingItem) {
                if (Math.random() <= yuriko_chance) {
                    event.setAmount((float) (originalDamage * 1.5));
                    target.hurt(GTDamageTypes.ELECTRIC.source(target.level()), (float) (originalDamage * 0.25));
                    target.level().levelEvent(null, 2008, target.blockPosition(), 0);
                    List<LivingEntity> entityList = target.level().getNearbyEntities(
                            LivingEntity.class,
                            TargetingConditions.forCombat().range(3),
                            target,
                            target.getBoundingBox().inflate(3));
                    for (LivingEntity entity : entityList) {
                        if (entity instanceof Player player1) {
                            player1.heal((float) (originalDamage * 0.1));
                            player1.level().levelEvent(null, 1018, target.blockPosition(), 0);
                        } else {
                            // forge文档真不如Fabric一根吧
                            entity.hurt(GTDamageTypes.ELECTRIC.source(target.level()), (float) (originalDamage * 0.25));
                            entity.level().levelEvent(null, 2008, target.blockPosition(), 0);
                        }
                    }
                } else {
                    event.setAmount((float) ((float) originalDamage * 0.75));
                }
            }
        }
        if (target instanceof Player player) {
            var item = EquipmentHandler.findOrEmpty(CMItems.YURIKO_RING.asItem(), player);
            if (item.getItem() instanceof YurikoRingItem) {
                event.setAmount(event.getAmount() * 1.25F);
            }
        }
    }

    @SubscribeEvent()
    public static void LivingDeathEvent(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();
        if (killer instanceof Player player) {
            var item = EquipmentHandler.findOrEmpty(CMItems.YURIKO_RING.asItem(), player);
            if (item.getItem() instanceof YurikoRingItem) {
                MobEffectInstance resistance = new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE,
                        200,
                        0,                        // 等级：5（注意：MC中等级从0开始，V对应4）
                        false,                    // 是否显示粒子效果（true=显示，false=隐藏）
                        true                     // 是否显示图标（true=显示在屏幕右侧）
                );
                ((Player) killer).addEffect(resistance);
                killer.hurt(GTDamageTypes.ELECTRIC.source(killer.level()), (float) (2.22));
            }
        }
    }
}
