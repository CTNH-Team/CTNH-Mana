package com.moguang.ctnhmana.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.item.equipment.TaintedBloodWeepingEye;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMobEffects;
import vazkii.botania.common.handler.EquipmentHandler;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import java.util.List;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TaintedBloodWeepingEyeEventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        if (target instanceof Player player) {
            ItemStack eye = EquipmentHandler.findOrEmpty(CMItems.TAINTED_BLOOD_EYE.asItem(), player);
            if (eye.getItem() instanceof TaintedBloodWeepingEye) {
                event.setAmount(event.getAmount() * 1.25F);
            }
        }

        if (event.getSource().getEntity() instanceof Player attacker &&
                attacker.hasEffect(CMMobEffects.TAINTED_BLOOD.get())) {
            // 造成伤害 +514% → 最终为原伤害的 6.14 倍
            event.setAmount(event.getAmount() * 6.14F);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide()) {
            return;
        }

        ItemStack eye = EquipmentHandler.findOrEmpty(CMItems.TAINTED_BLOOD_EYE.asItem(), player);
        if (!(eye.getItem() instanceof TaintedBloodWeepingEye)) {
            return;
        }

        // 污血期间：致死伤害将生命保留至 1
        if (player.hasEffect(CMMobEffects.TAINTED_BLOOD.get())) {
            event.setCanceled(true);
            player.setHealth(1.0F);
            player.invulnerableTime = 20;
            return;
        }

        if (TaintedBloodWeepingEye.isOnCooldown(eye)) {
            return;
        }

        event.setCanceled(true);
        triggerRevive(player, eye);
    }

    private static void triggerRevive(Player player, ItemStack eye) {
        SoulNetwork network = NetworkHelper.getSoulNetwork(player);
        int available = network.getCurrentEssence();
        int want = Math.min(TaintedBloodWeepingEye.MAX_CONSUME_LP, available);
        int consume = want > 0 ? network.syphon(new SoulTicket(want)) : 0;

        int units = consume / TaintedBloodWeepingEye.LP_PER_UNIT;
        float healAmount = units * TaintedBloodWeepingEye.HEAL_PER_UNIT;
        player.setHealth(Math.max(1.0F, healAmount));
        player.invulnerableTime = 20;

        if (units > 0) {
            List<LivingEntity> enemies = player.level().getNearbyEntities(
                    LivingEntity.class,
                    TargetingConditions.forCombat().range(TaintedBloodWeepingEye.AOE_RADIUS),
                    player,
                    player.getBoundingBox().inflate(TaintedBloodWeepingEye.AOE_RADIUS));
            MobEffectInstance slow = new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    TaintedBloodWeepingEye.SLOW_DURATION,
                    TaintedBloodWeepingEye.SLOW_AMPLIFIER,
                    false,
                    true);
            float aoeDamage = TaintedBloodWeepingEye.AOE_DAMAGE * units;
            for (LivingEntity enemy : enemies) {
                enemy.hurt(player.damageSources().magic(), aoeDamage);
                enemy.addEffect(new MobEffectInstance(slow));
            }
        }

        player.addEffect(new MobEffectInstance(
                CMMobEffects.TAINTED_BLOOD.get(),
                TaintedBloodWeepingEye.TAINTED_BLOOD_DURATION,
                0,
                false,
                true));

        TaintedBloodWeepingEye.startCooldown(eye);

        player.sendSystemMessage(
                TaintedBloodWeepingEye.taintedBloodHoverLang[(int) (Math.random() *
                        TaintedBloodWeepingEye.taintedBloodHoverLang.length)].translate());
    }
}
