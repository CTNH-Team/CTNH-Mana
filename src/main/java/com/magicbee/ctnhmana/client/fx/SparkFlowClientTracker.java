package com.magicbee.ctnhmana.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.magicbee.ctnhmana.CMConfig;
import vazkii.botania.common.helper.VecHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 原版火花流动粒子的客户端本地续画。
 *
 * <p>
 * 服务端每 {@code botaniaHintIntervalTicks} tick 只发一条「这条边正在流动」的提示（见
 * {@code ManaSparkEntityMixin}），本类把提示按 (from, to) 记入表并刷新 TTL，然后每客户端 tick 本地生成粒子。
 * 流动停止后最多多画 {@code clientFlowTtlTicks} tick 即自动消失。
 * </p>
 */
public final class SparkFlowClientTracker {

    private static final Map<Long, Flow> FLOWS = new HashMap<>();

    private SparkFlowClientTracker() {}

    /** 收到服务端流动提示（已在客户端主线程）。 */
    public static void onFlowHint(int fromId, int toId, int color) {
        CMConfig.SparkParticles config = CMConfig.spark();
        if (!config.enabled || config.particlesPerConnection() <= 0) {
            return;
        }
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        long key = ((long) fromId << 32) | (toId & 0xFFFFFFFFL);
        Flow flow = FLOWS.get(key);
        if (flow == null) {
            flow = new Flow(fromId, toId);
            FLOWS.put(key, flow);
        }
        flow.color = color;
        flow.expireAt = level.getGameTime() + config.flowTtl();
    }

    public static void clear() {
        FLOWS.clear();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            clear();
            return;
        }
        if (FLOWS.isEmpty() || minecraft.isPaused()) {
            return;
        }
        CMConfig.SparkParticles config = CMConfig.spark();
        int perConnection = config.particlesPerConnection();
        if (!config.enabled || perConnection <= 0) {
            clear();
            return;
        }

        int budget = config.maxParticlesPerTick();
        long now = level.getGameTime();
        Iterator<Flow> iterator = FLOWS.values().iterator();
        while (iterator.hasNext()) {
            Flow flow = iterator.next();
            if (now > flow.expireAt) {
                iterator.remove();
                continue;
            }
            Entity from = level.getEntity(flow.fromId);
            Entity to = level.getEntity(flow.toId);
            if (from == null || to == null || !from.isAlive() || !to.isAlive()) {
                iterator.remove();
                continue;
            }
            for (int i = 0; i < perConnection && budget > 0; i++) {
                SparkFlowParticles.spawn(level, VecHelper.fromEntityCenter(from), VecHelper.fromEntityCenter(to),
                        flow.color);
                budget--;
            }
        }
    }

    private static final class Flow {

        private final int fromId;
        private final int toId;
        private int color;
        private long expireAt;

        private Flow(int fromId, int toId) {
            this.fromId = fromId;
            this.toId = toId;
        }
    }
}
