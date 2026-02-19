package com.moguang.ctnhmana.event;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class IndexEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide) return; // 只服务端逻辑时可加

        long tick = player.level().getGameTime();
        if (tick % 100 == 0)
        {
            var tags=player.getPersistentData();
            if(tags.contains("karma")&&player.getEffect(CMMobEffects.Karma.get())==null)
            {
                MobEffectInstance karmaEffect = new MobEffectInstance(
                    CMMobEffects.Karma.get(),
                    20*10*60,
                    tags.getInt("karma")+1,
                    false,
                    true
            );
                MobEffectInstance darkEffect = new MobEffectInstance(
                        MobEffects.DARKNESS,
                        20*7,
                        1,
                        false,
                        true
                );
                player.addEffect(karmaEffect);
                player.addEffect(darkEffect);

            }
            if(tags.contains("karma_fortuna")&&player.getEffect(CMMobEffects.KarmaFortuna.get())==null)
            {
                MobEffectInstance karmaEffectFortuna = new MobEffectInstance(
                        CMMobEffects.KarmaFortuna.get(),
                        20*10*60,
                        tags.getInt("karma_fortuna"),
                        false,
                        true
                );
                MobEffectInstance darkEffect = new MobEffectInstance(
                        MobEffects.DARKNESS,
                        20*7,
                        1,
                        false,
                        true
                );
                player.addEffect(karmaEffectFortuna);
                player.addEffect(darkEffect);
            }
        }

    }
}