package com.magicbee.ctnhmana.api.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;

import static com.magicbee.ctnhmana.registry.sounds.CMSoundEvent.SHROUD_WHISPER_EFFECT;

public class ShroudGazeEffect extends MobEffect {

    public ShroudGazeEffect(MobEffectCategory category, int color) {
        super(category, 0x9900FF);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 100 == 0; // 每秒检查一次
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplified) {
        if (entity.level().isClientSide() && entity instanceof Player player) {
            playMusicIfNeeded(player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playMusicIfNeeded(Player player) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != player) return;
        SoundInstance Gazing = new GazingMusic(player);
        SoundManager soundManager = mc.getSoundManager();
        boolean isPlaying = soundManager.isActive(Gazing);
        if (!isPlaying) {
            mc.getSoundManager().play(Gazing);
        }
    }

    private static class GazingMusic extends AbstractTickableSoundInstance {

        Player player = null;

        private GazingMusic(Player player) {
            super(SHROUD_WHISPER_EFFECT.get(), SoundSource.MUSIC, SoundInstance.createUnseededRandom());
            this.player = player;
            this.looping = true;
        }

        public void tick() {}
    }
}
