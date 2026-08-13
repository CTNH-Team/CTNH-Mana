package com.magicbee.ctnhmana.common.event.zenith;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

/**
 * 虚境入侵（zenithinvade）事件实例。
 * 服务端权威存储；客户端仅用于表现与后续扩展钩子。
 */
public class ZenithInvadeEvent {

    /** 默认持续：10 分钟 */
    public static final int DEFAULT_DURATION_TICKS = 10 * 60 * 20;

    /** 事件唯一 ID，便于后续机制引用 / 同步 */
    public final UUID id;
    /** 特效锚点（通常为天顶之眼） */
    public final BlockPos sourcePos;
    /** 总时长（tick） */
    public final int totalDuration;
    /** 剩余时长（tick） */
    public int remainingTicks;
    /** 是否播放开场形成动画（闪光 / 睁眼等） */
    public final boolean playIntro;

    public ZenithInvadeEvent(BlockPos sourcePos, int durationTicks, boolean playIntro) {
        this(UUID.randomUUID(), sourcePos.immutable(), durationTicks, durationTicks, playIntro);
    }

    public ZenithInvadeEvent(UUID id, BlockPos sourcePos, int totalDuration, int remainingTicks, boolean playIntro) {
        this.id = id;
        this.sourcePos = sourcePos.immutable();
        this.totalDuration = totalDuration;
        this.remainingTicks = remainingTicks;
        this.playIntro = playIntro;
    }

    /** 已播放刻数 */
    public int getElapsedTicks() {
        return Math.max(0, totalDuration - remainingTicks);
    }

    /** 进度 0~1 */
    public float getProgress() {
        if (totalDuration <= 0) return 1.0f;
        return 1.0f - (float) remainingTicks / totalDuration;
    }

    public boolean isFinished() {
        return remainingTicks <= 0;
    }

    /** 每 tick 递减，返回是否仍存活 */
    public boolean tick() {
        if (remainingTicks > 0) {
            remainingTicks--;
        }
        return remainingTicks > 0;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("id", id);
        tag.putInt("x", sourcePos.getX());
        tag.putInt("y", sourcePos.getY());
        tag.putInt("z", sourcePos.getZ());
        tag.putInt("total", totalDuration);
        tag.putInt("remain", remainingTicks);
        tag.putBoolean("intro", playIntro);
        return tag;
    }

    public static ZenithInvadeEvent load(CompoundTag tag) {
        return new ZenithInvadeEvent(
                tag.getUUID("id"),
                new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")),
                tag.getInt("total"),
                tag.getInt("remain"),
                tag.getBoolean("intro"));
    }
}
