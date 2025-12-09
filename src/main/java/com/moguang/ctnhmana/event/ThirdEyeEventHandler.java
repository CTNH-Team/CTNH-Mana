package com.moguang.ctnhmana.event;

import com.moguang.ctnhmana.CTNHMana;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.bauble.ThirdEyeItem;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ThirdEyeEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource damageSource = event.getSource();
        LivingEntity target = event.getEntity();
        float originalDamage = event.getAmount();
        if (damageSource.getEntity() instanceof Player player) {
            ItemStack item= EquipmentHandler.findOrEmpty(BotaniaItems.thirdEye,player);
            if(item.getItem() instanceof ThirdEyeItem)
            {
                event.setAmount((float) (originalDamage * 1.5));
                // 触发暴击粒子效果（原版视觉反馈）
                target.level().levelEvent(null, 2008, target.blockPosition(), 0);
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(LivingEvent.LivingTickEvent event) {
        if ((event.getEntity() instanceof Player player))
        {
            Level level=player.level();
            var time = Objects.requireNonNull(level).getDayTime() % 40;
            if(time==0)return;
            ItemStack item=EquipmentHandler.findOrEmpty(BotaniaItems.thirdEye,player);
            if(item.getItem() instanceof ThirdEyeItem)
            {
                List<LivingEntity> entityList=player.level().getNearbyEntities(
                        LivingEntity.class,
                        TargetingConditions.forCombat().range(8),
                        player,
                        player.getBoundingBox().inflate(8));
                for(LivingEntity entity:entityList)
                {
                    if (entity instanceof NeutralMob mob)
                    {
                        mob.setTarget(player);

                    }
                }

            }
            return;
        }

    }
}
