package com.moguang.ctnhmana.data;

import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.saveddata.SavedData;

import java.lang.reflect.Array;
import java.util.*;

public class ManaData extends SavedData {
    private static final String ManaData = "ManaSavedData";
    private final ServerLevel serverLevel;
    public Map<String, Integer> ManaLevel = new HashMap<>();
    public List<String> LevelName= Arrays.asList("BT","BM","ARS","GT"); //Waiting for NANE CHANGING
    public static ManaData getOrCreate(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(tag -> new ManaData(serverLevel, tag),
                () -> new ManaData(serverLevel), "ctnhmana_manadata");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        var ManaList = new ListTag();

        ManaLevel.forEach((key, value) ->
                {
                    var tag=new CompoundTag();
                    tag.putInt(key,value);
                    ManaList.add(tag);
                }
        );
        nbt.put("ManaInfo",ManaList);
        return nbt;
    }
    public void ChangeLevel(String id,int level)
    {
        ManaLevel.put(id,level);
        setDirty();
    }
    public Map<String,Integer>get()
    {
        return ManaLevel;
    }
    public ManaData(ServerLevel serverLevel,CompoundTag tag) {
        this.serverLevel = serverLevel;
        var list = tag.getList("ManaInfo", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag compoundTag = list.getCompound(i);
            String key=compoundTag.getAllKeys().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("I HATE STREAM AND LAMABDA WHY JAVA HAVE ALL THESE THINGS"));
            ManaLevel.put(key,compoundTag.getInt(key));
        }

    }
    public ManaData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        LevelName.forEach((code)->ChangeLevel(code,0));
    }

}
