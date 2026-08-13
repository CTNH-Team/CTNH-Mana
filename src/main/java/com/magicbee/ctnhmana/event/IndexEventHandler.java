package com.magicbee.ctnhmana.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.networking.packets.IndexTargetBlockPacket;
import com.magicbee.ctnhmana.registry.CMMobEffects;

import static com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK;
import static com.magicbee.ctnhmana.common.item.caduceus.CaduceusItem.playIndexMusic;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class IndexEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide) {

            return; // 只服务端逻辑时可加
        }

        long tick = player.level().getGameTime();
        if (tick % 100 == 0) {
            var tags = player.getPersistentData();
            if (tags.contains("index_target_block") && player.level().getGameTime() % 100 == 0) {
                var posArray = tags.getIntArray("index_target_block");
                NETWORK.sendToTrackingChunk(new IndexTargetBlockPacket(posArray),
                        new LevelChunk(player.level(), new ChunkPos(player.getOnPos())));
            }
            if (tags.contains("karma") && player.getEffect(CMMobEffects.Karma.get()) == null) {
                MobEffectInstance karmaEffect = new MobEffectInstance(
                        CMMobEffects.Karma.get(),
                        20 * 10 * 60,
                        tags.getInt("karma") + 1,
                        false,
                        true);
                MobEffectInstance darkEffect = new MobEffectInstance(
                        MobEffects.DARKNESS,
                        20 * 7,
                        1,
                        false,
                        true);
                player.addEffect(karmaEffect);
                player.addEffect(darkEffect);

            }
            if (tags.contains("karma_fortuna") && player.getEffect(CMMobEffects.KarmaFortuna.get()) == null) {
                MobEffectInstance karmaEffectFortuna = new MobEffectInstance(
                        CMMobEffects.KarmaFortuna.get(),
                        20 * 10 * 60,
                        tags.getInt("karma_fortuna"),
                        false,
                        true);
                MobEffectInstance darkEffect = new MobEffectInstance(
                        MobEffects.DARKNESS,
                        20 * 7,
                        1,
                        false,
                        true);
                player.addEffect(karmaEffectFortuna);
                player.addEffect(darkEffect);
            }
        }
    }

    @SubscribeEvent
    public static void LivingDeathEvent(LivingDeathEvent event) {
        var killer = event.getSource().getEntity();
        var entity = event.getEntity();
        if (!entity.level().isClientSide() && entity.hasEffect(CMMobEffects.indextarget.get()) &&
                (killer instanceof Player player) && killer.getPersistentData().contains("index_target") &&
                player.getPersistentData().getString("index_target").equals(entity.getStringUUID())) {
            player.getPersistentData().remove("index_target");
            if (!player.hasEffect(CMMobEffects.Bladeunleashed.get())) {
                player.addEffect(new MobEffectInstance(
                        CMMobEffects.Bladeunleashed.get(),
                        20 * 30 * 60,
                        0,
                        true,
                        true));
            } else {
                var amplifier = player.getEffect(CMMobEffects.Bladeunleashed.get()).getAmplifier();
                player.addEffect(new MobEffectInstance(
                        CMMobEffects.Bladeunleashed.get(),
                        20 * 30 * 60,
                        Math.min(3, amplifier + 1),
                        true,
                        true));
            }

        }
        if (entity.level().isClientSide() && entity instanceof Player player) {
            playIndexMusic(player);
        }
    }
}
