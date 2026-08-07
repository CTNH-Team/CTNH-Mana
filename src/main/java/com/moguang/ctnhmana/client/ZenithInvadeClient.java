package com.moguang.ctnhmana.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.moguang.ctnhmana.client.render.ZenithMatrixRender;
import com.moguang.ctnhmana.common.event.zenith.ZenithInvadeEvent;
import com.moguang.ctnhmana.registry.sounds.CMSoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 虚境入侵事件的客户端镜像。
 * 驱动天空裂缝 / 开场动画 / shroud_whisper 循环；后续客户端机制也可挂在此处。
 */
@OnlyIn(Dist.CLIENT)
public final class ZenithInvadeClient {

    private static final Map<UUID, ZenithInvadeEvent> ACTIVE = new LinkedHashMap<>();

    /** 入侵期间循环播放的低语；结束时停止。 */
    @Nullable
    private static InvadeWhisperSound whisperSound;

    private ZenithInvadeClient() {}

    public static void startOrUpdate(UUID id, BlockPos sourcePos, int totalDuration, int remainingTicks,
                                     boolean playIntro) {
        ZenithInvadeEvent event = new ZenithInvadeEvent(id, sourcePos, totalDuration, remainingTicks, playIntro);
        ACTIVE.put(id, event);
        // 开场动画仅在接近起点时触发，避免重登重复闪屏
        boolean shouldPlayIntro = playIntro && event.getElapsedTicks() < 2;
        ZenithMatrixRender.beginTimedSkyEffect(sourcePos, remainingTicks, shouldPlayIntro);
        ensureWhisperPlaying();
    }

    public static void stop(UUID id) {
        ACTIVE.remove(id);
        if (ACTIVE.isEmpty()) {
            ZenithMatrixRender.clearTimedSkyEffect();
            stopWhisper();
        } else {
            // 仍有其他入侵事件时，刷新为最新一个的锚点
            ZenithInvadeEvent last = null;
            for (ZenithInvadeEvent e : ACTIVE.values()) {
                last = e;
            }
            if (last != null) {
                ZenithMatrixRender.beginTimedSkyEffect(last.sourcePos, last.remainingTicks, false);
            }
        }
    }

    /** 客户端每 tick 推进镜像倒计时，并维持天空特效 / 低语心跳 */
    public static void tick() {
        if (ACTIVE.isEmpty()) {
            stopWhisper();
            return;
        }
        Iterator<Map.Entry<UUID, ZenithInvadeEvent>> it = ACTIVE.entrySet().iterator();
        ZenithInvadeEvent primary = null;
        while (it.hasNext()) {
            ZenithInvadeEvent event = it.next().getValue();
            if (!event.tick()) {
                it.remove();
                continue;
            }
            primary = event;
        }
        if (primary != null) {
            ZenithMatrixRender.markSkyEffectSource(primary.sourcePos);
            ensureWhisperPlaying();
        } else {
            ZenithMatrixRender.clearTimedSkyEffect();
            stopWhisper();
        }
    }

    public static boolean hasActive() {
        return !ACTIVE.isEmpty();
    }

    public static ZenithInvadeEvent getPrimary() {
        if (ACTIVE.isEmpty()) return null;
        ZenithInvadeEvent last = null;
        for (ZenithInvadeEvent e : ACTIVE.values()) {
            last = e;
        }
        return last;
    }

    private static void ensureWhisperPlaying() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        SoundManager soundManager = mc.getSoundManager();
        if (whisperSound != null && soundManager.isActive(whisperSound)) {
            return;
        }
        whisperSound = new InvadeWhisperSound();
        soundManager.play(whisperSound);
    }

    private static void stopWhisper() {
        if (whisperSound == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.getSoundManager().isActive(whisperSound)) {
            mc.getSoundManager().stop(whisperSound);
        }
        whisperSound = null;
    }

    /** 虚境入侵专用循环低语；入侵结束时自行 stop。 */
    private static final class InvadeWhisperSound extends AbstractTickableSoundInstance {

        private InvadeWhisperSound() {
            super(CMSoundEvent.SHROUD_WHISPER_EFFECT.get(), SoundSource.AMBIENT,
                    SoundInstance.createUnseededRandom());
            this.looping = true;
            this.relative = true;
            this.volume = 0.55F;
            this.delay = 0;
        }

        @Override
        public void tick() {
            if (!hasActive()) {
                stop();
            }
        }
    }
}
