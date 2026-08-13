package com.magicbee.ctnhmana.common.event.zenith;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.networking.packets.ZenithInvadePacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK;

/**
 * 虚境入侵事件的服务端管理器（SavedData）。
 * 负责创建 / 持久化 / 每 tick 推进，并通过 S2C 同步到客户端。
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ZenithInvadeManager extends SavedData {

    private static final String DATA_NAME = "ctnhmana_zenith_invade";

    private final ServerLevel level;
    private final List<ZenithInvadeEvent> activeEvents = new ArrayList<>();

    public ZenithInvadeManager(ServerLevel level) {
        this.level = level;
    }

    public ZenithInvadeManager(ServerLevel level, CompoundTag tag) {
        this.level = level;
        ListTag list = tag.getList("events", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            activeEvents.add(ZenithInvadeEvent.load(list.getCompound(i)));
        }
    }

    public static ZenithInvadeManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                tag -> new ZenithInvadeManager(level, tag),
                () -> new ZenithInvadeManager(level),
                DATA_NAME);
    }

    public boolean hasActive() {
        return !activeEvents.isEmpty();
    }

    /**
     * 在指定锚点启动一次虚境入侵。
     *
     * @return 新建的事件实例
     */
    public ZenithInvadeEvent start(BlockPos sourcePos, int durationTicks, boolean playIntro) {
        ZenithInvadeEvent event = new ZenithInvadeEvent(sourcePos, durationTicks, playIntro);
        activeEvents.add(event);
        setDirty();
        broadcastStart(event);
        ZenithInvadeMessages.broadcastInvadeStart(level);
        return event;
    }

    public List<ZenithInvadeEvent> getActiveEvents() {
        return activeEvents;
    }

    /** 服务端每 tick 推进所有事件 */
    public void serverTick() {
        if (activeEvents.isEmpty()) return;

        // 维度级机制只跑一次，不跟单个事件重复叠加
        ZenithInvadeEffects.onDimensionTick(level);

        Iterator<ZenithInvadeEvent> it = activeEvents.iterator();
        boolean dirty = false;
        while (it.hasNext()) {
            ZenithInvadeEvent event = it.next();
            if (!event.tick()) {
                broadcastStop(event);
                it.remove();
                dirty = true;
            }
        }
        if (dirty) setDirty();
        // 周期性标记脏数据，保证剩余时间可持久化
        if (level.getGameTime() % 200 == 0) {
            setDirty();
        }
    }

    private void broadcastStart(ZenithInvadeEvent event) {
        var packet = ZenithInvadePacket.start(event);
        for (ServerPlayer player : level.players()) {
            NETWORK.sendToPlayer(packet, player);
        }
    }

    private void broadcastStop(ZenithInvadeEvent event) {
        var packet = ZenithInvadePacket.stop(event.id);
        for (ServerPlayer player : level.players()) {
            NETWORK.sendToPlayer(packet, player);
        }
    }

    /** 向刚进入维度的玩家同步当前全部活跃事件 */
    public void syncToPlayer(ServerPlayer player) {
        for (ZenithInvadeEvent event : activeEvents) {
            NETWORK.sendToPlayer(ZenithInvadePacket.start(event), player);
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (ZenithInvadeEvent event : activeEvents) {
            list.add(event.save());
        }
        tag.put("events", list);
        return tag;
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.level instanceof ServerLevel serverLevel)) return;
        get(serverLevel).serverTick();
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel serverLevel)) return;
        get(serverLevel).syncToPlayer(player);
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel serverLevel)) return;
        get(serverLevel).syncToPlayer(player);
    }
}
