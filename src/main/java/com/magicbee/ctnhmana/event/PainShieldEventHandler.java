package com.magicbee.ctnhmana.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMMobEffects;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWillGem;

import java.util.Collections;
import java.util.List;

/**
 * 苦难护盾效果处理：百分比减伤；被持有意志魂石（Curios 饰品栏）的玩家攻击时，
 * 消耗 25 普通意志降低 1 级效果（1 级直接移除），触发后 6 秒冷却。
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PainShieldEventHandler {

    /** 1 级减伤 75%，每级 +5%，至多 95%。 */
    private static final float BASE_REDUCTION = 0.75F;
    private static final float REDUCTION_PER_LEVEL = 0.05F;
    private static final float MAX_REDUCTION = 0.95F;
    /** 每次触发消耗的意志与冷却时长（6 秒）。 */
    private static final double WILL_COST = 25.0D;
    private static final int COOLDOWN_TICKS = 6 * 20;
    private static final String COOLDOWN_KEY = "ctnhmana_pain_shield_cooldown";

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return; // 只在服务端判定
        }
        MobEffectInstance instance = entity.getEffect(CMMobEffects.PAIN_SHIELD.get());
        if (instance == null) {
            return;
        }
        float reduction = Math.min(BASE_REDUCTION + REDUCTION_PER_LEVEL * instance.getAmplifier(), MAX_REDUCTION);
        event.setAmount(event.getAmount() * (1.0F - reduction));
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getSource().getDirectEntity() instanceof Player player)) {
            return; // 只有玩家攻击会消耗意志
        }
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) {
            return; // 只在服务端判定
        }
        MobEffectInstance instance = target.getEffect(CMMobEffects.PAIN_SHIELD.get());
        if (instance == null) {
            return;
        }
        long gameTime = target.level().getGameTime();
        if (target.getPersistentData().getLong(COOLDOWN_KEY) > gameTime) {
            return; // 冷却中
        }
        // 只检索 Curios 饰品栏中的魂石
        List<SlotResult> gems = CuriosApi.getCuriosInventory(player)
                .resolve()
                .map(handler -> handler.findCurios(stack -> stack.getItem() instanceof IDemonWillGem))
                .orElse(Collections.emptyList());
        for (SlotResult result : gems) {
            ItemStack stack = result.stack();
            IDemonWillGem gem = (IDemonWillGem) stack.getItem();
            if (gem.getWill(EnumDemonWillType.DEFAULT, stack) <= WILL_COST) {
                continue;
            }
            gem.drainWill(EnumDemonWillType.DEFAULT, stack, WILL_COST, false);
            // 降低 1 级，1 级时直接移除（1.20.1 的 update 只升不降，需先移除再添加）
            target.removeEffect(CMMobEffects.PAIN_SHIELD.get());
            if (instance.getAmplifier() > 0) {
                target.addEffect(new MobEffectInstance(CMMobEffects.PAIN_SHIELD.get(),
                        instance.getDuration(), instance.getAmplifier() - 1, false, true));
            }
            target.getPersistentData().putLong(COOLDOWN_KEY, gameTime + COOLDOWN_TICKS);
            return;
        }
    }
}
