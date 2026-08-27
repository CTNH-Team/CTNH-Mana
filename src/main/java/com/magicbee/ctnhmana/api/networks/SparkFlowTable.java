package com.magicbee.ctnhmana.api.networks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.Collection;

/**
 * 火花「活跃连线表」的紧凑编解码。
 *
 * <p>
 * 服务端把本同步窗口内真正发生过魔力流动的目标汇总成一张表，通过实体数据（只在内容变化时下发、
 * 且自动按追踪范围裁剪）同步给客户端；客户端据此本地生成粒子。这取代了原先「每 tick 每目标一个
 * 网络包」的做法，稳态下不再有粒子相关的网络流量。
 * </p>
 */
public final class SparkFlowTable {

    /** 火花 → 方块（魔力流出）。 */
    public static final int TYPE_BLOCK_OUT = 0;
    /** 方块 → 火花（魔力流入）。 */
    public static final int TYPE_BLOCK_IN = 1;
    /** 火花 → 其他实体（普通火花 / 对端尖塔火花）。 */
    public static final int TYPE_ENTITY_OUT = 2;
    /** 其他实体 → 本火花。 */
    public static final int TYPE_ENTITY_IN = 3;

    /** 每条连线占用的 int 数量：type + 3 个载荷位。 */
    public static final int STRIDE = 4;

    private static final String TAG_ENTRIES = "flow";
    private static final String TAG_ANIMATION = "anim";

    private SparkFlowTable() {}

    /**
     * 把本窗口累积的连线集合编码为可同步的 NBT。
     *
     * @param animation  尖塔 UI 的粒子开关状态；关闭时客户端不画
     * @param maxTargets 连线数量上限，防止实体数据包过大
     */
    public static CompoundTag encode(boolean animation, Collection<BlockPos> blockOut, Collection<BlockPos> blockIn,
                                     Collection<Integer> entityOut, Collection<Integer> entityIn, int maxTargets) {
        int limit = Math.max(0, maxTargets);
        int count = Math.min(limit, blockOut.size() + blockIn.size() + entityOut.size() + entityIn.size());
        int[] entries = new int[count * STRIDE];
        int index = 0;
        index = writeBlocks(entries, index, TYPE_BLOCK_OUT, blockOut);
        index = writeBlocks(entries, index, TYPE_BLOCK_IN, blockIn);
        index = writeEntities(entries, index, TYPE_ENTITY_OUT, entityOut);
        writeEntities(entries, index, TYPE_ENTITY_IN, entityIn);

        CompoundTag tag = new CompoundTag();
        if (entries.length > 0) {
            tag.putIntArray(TAG_ENTRIES, entries);
            tag.putBoolean(TAG_ANIMATION, animation);
        }
        return tag;
    }

    private static int writeBlocks(int[] entries, int index, int type, Collection<BlockPos> positions) {
        for (BlockPos pos : positions) {
            if (index + STRIDE > entries.length) {
                return index;
            }
            entries[index] = type;
            entries[index + 1] = pos.getX();
            entries[index + 2] = pos.getY();
            entries[index + 3] = pos.getZ();
            index += STRIDE;
        }
        return index;
    }

    private static int writeEntities(int[] entries, int index, int type, Collection<Integer> entityIds) {
        for (Integer entityId : entityIds) {
            if (index + STRIDE > entries.length || entityId == null) {
                return index;
            }
            entries[index] = type;
            entries[index + 1] = entityId;
            entries[index + 2] = 0;
            entries[index + 3] = 0;
            index += STRIDE;
        }
        return index;
    }

    /** 表为空（没有任何活跃连线）时返回 true。 */
    public static boolean isEmpty(CompoundTag tag) {
        return tag == null || !tag.contains(TAG_ENTRIES);
    }

    public static boolean animation(CompoundTag tag) {
        return tag != null && tag.getBoolean(TAG_ANIMATION);
    }

    public static int[] entries(CompoundTag tag) {
        return tag == null ? new int[0] : tag.getIntArray(TAG_ENTRIES);
    }

    public static boolean isInbound(int type) {
        return type == TYPE_BLOCK_IN || type == TYPE_ENTITY_IN;
    }

    public static boolean isEntity(int type) {
        return type == TYPE_ENTITY_OUT || type == TYPE_ENTITY_IN;
    }
}
