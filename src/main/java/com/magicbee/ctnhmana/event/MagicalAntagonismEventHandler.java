package com.magicbee.ctnhmana.event;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMMobEffects;

import java.util.Set;

/**
 * 奥法拮抗效果处理：携带者在受到魔法、元素类伤害时完全免疫。
 * 与物理拮抗（{@link PhysicalAntagonismEventHandler}）合并覆盖所有普通伤害类型，
 * 仅 /kill、虚空、世界边界、generic 这类极特殊伤害不在免疫范围内。
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagicalAntagonismEventHandler {

    /** 1.20.1 没有魔法伤害 tag，按伤害 id 白名单判定（魔法 + 元素类）。 */
    private static final Set<String> MAGICAL_DAMAGE = Set.of(
            "magic", "indirect_magic", "wither", "dragon_breath",
            "sonic_boom", "wither_skull", "shulker_bullet",
            // 元素类伤害
            "in_fire", "on_fire", "lava", "hot_floor", "campfire",
            "lightning_bolt", "freeze");

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return; // 只在服务端判定
        }
        if (!entity.hasEffect(CMMobEffects.MAGICAL_ANTAGONISM.get())) {
            return;
        }
        if (isMagicalDamage(event.getSource())) {
            event.setCanceled(true);
        }
    }

    private static boolean isMagicalDamage(DamageSource source) {
        // 原版 witch_resistant_to = 魔法类伤害集合（magic/indirect_magic/sonic_boom/thorns），
        // 同时覆盖其它模组按此 tag 标记的魔法伤害
        if (source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            return true;
        }
        return source.typeHolder().unwrapKey()
                .map(key -> MAGICAL_DAMAGE.contains(key.location().getPath()))
                .orElse(false);
    }
}
