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
 * 物理拮抗效果处理：携带者在受到物理、爆炸、弹射物、环境等伤害时完全免疫。
 * 与奥法拮抗（{@link MagicalAntagonismEventHandler}）合并覆盖所有普通伤害类型，
 * 仅 /kill、虚空、世界边界、generic 这类极特殊伤害不在免疫范围内。
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhysicalAntagonismEventHandler {

    /** 无法用原版 damage type tag 覆盖的近战/接触/环境物理伤害 id（1.20.1）。 */
    private static final Set<String> PHYSICAL_DAMAGE = Set.of(
            "mob_attack", "mob_attack_no_aggro", "player_attack",
            "sting", "thorns", "cactus", "sweet_berry_bush",
            "cramming", "in_wall", "fly_into_wall",
            "falling_block", "anvil", "falling_stalactite", "stalagmite",
            "fall", "drown", "dry_out", "starve");

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return; // 只在服务端判定
        }
        if (!entity.hasEffect(CMMobEffects.PHYSICAL_ANTAGONISM.get())) {
            return;
        }
        if (isPhysicalDamage(event.getSource())) {
            event.setCanceled(true);
        }
    }

    private static boolean isPhysicalDamage(DamageSource source) {
        // 爆炸与弹射物（箭、三叉戟、火球等）走原版 tag 判定
        if (source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypeTags.IS_PROJECTILE)) {
            return true;
        }
        return source.typeHolder().unwrapKey()
                .map(key -> PHYSICAL_DAMAGE.contains(key.location().getPath()))
                .orElse(false);
    }
}
