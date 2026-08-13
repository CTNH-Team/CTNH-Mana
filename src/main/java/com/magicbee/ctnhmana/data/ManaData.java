package com.magicbee.ctnhmana.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class ManaData extends SavedData {

    private static final String DATA_NAME = "ctnhmana_manadata";
    /** 虚境之门开启状态的 tag 键 */
    public static final String TAG_ZENITH_OPEN = "isZenithOpen";

    private final ServerLevel serverLevel;
    public Map<String, Integer> ManaLevel = new HashMap<>();
    public List<String> LevelName = Arrays.asList("BT", "BM", "ARS", "GT"); // Waiting for NANE CHANGING

    /** 通用状态标签：布尔等标志统一存于此，避免散落的内置字段 */
    private final CompoundTag tags = new CompoundTag();

    public static ManaData getOrCreate(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(tag -> new ManaData(serverLevel, tag),
                () -> new ManaData(serverLevel), DATA_NAME);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        var ManaList = new ListTag();

        ManaLevel.forEach((key, value) -> {
            var tag = new CompoundTag();
            tag.putInt(key, value);
            ManaList.add(tag);
        });
        nbt.put("ManaInfo", ManaList);
        // 将 tags 中的标志合并写入根 NBT（保持旧键兼容）
        nbt.putBoolean(TAG_ZENITH_OPEN, tags.getBoolean(TAG_ZENITH_OPEN));
        return nbt;
    }

    /** 从 tag 读取虚境之门是否开启 */
    public boolean isZenithOpen() {
        return tags.getBoolean(TAG_ZENITH_OPEN);
    }

    /** 以 tag 形式写入虚境之门开启状态 */
    public void setZenithOpen(boolean open) {
        tags.putBoolean(TAG_ZENITH_OPEN, open);
        setDirty();
    }

    /** 通用布尔 tag 读取 */
    public boolean getBooleanTag(String key) {
        return tags.getBoolean(key);
    }

    /** 通用布尔 tag 写入 */
    public void setBooleanTag(String key, boolean value) {
        tags.putBoolean(key, value);
        setDirty();
    }

    public void ChangeLevel(String id, int level) {
        ManaLevel.put(id, level);
        setDirty();
    }

    public Map<String, Integer> get() {
        return ManaLevel;
    }

    public ManaData(ServerLevel serverLevel, CompoundTag tag) {
        this.serverLevel = serverLevel;
        var list = tag.getList("ManaInfo", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag compoundTag = list.getCompound(i);
            String key = compoundTag.getAllKeys().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "I HATE STREAM AND LAMABDA WHY JAVA HAVE ALL THESE THINGS"));
            ManaLevel.put(key, compoundTag.getInt(key));
        }
        // 从 tag 读取虚境之门状态
        tags.putBoolean(TAG_ZENITH_OPEN, tag.getBoolean(TAG_ZENITH_OPEN));
    }

    public ManaData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        LevelName.forEach((code) -> ChangeLevel(code, 0));
        tags.putBoolean(TAG_ZENITH_OPEN, false);
    }
}
