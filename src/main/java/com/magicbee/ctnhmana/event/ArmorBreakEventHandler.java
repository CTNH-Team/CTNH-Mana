package com.magicbee.ctnhmana.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMMobEffects;

/**
 * 破甲效果处理：施加破甲时，若目标带有抗性提升，则减少一层。
 * 巨蜂/皇家侍从 Bee 造成伤害时不再主动施加破甲。
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorBreakEventHandler {

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
