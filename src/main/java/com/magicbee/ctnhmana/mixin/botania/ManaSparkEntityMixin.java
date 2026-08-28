package com.magicbee.ctnhmana.mixin.botania;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.CMConfig;
import com.magicbee.ctnhmana.api.networks.SparkEffectSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.entity.ManaSparkEntity;
import vazkii.botania.network.BotaniaPacket;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

/**
 * 把原版火花的「每 tick 每条传输边一个粒子包」压成「每 N tick 一条流动提示」，并按粒子出生点做半径裁剪。
 *
 * <p>
 * 客户端收到提示后由 {@code SparkFlowClientTracker} 在 TTL 内本地续画，因此观感基本不变。语义变化仅此一处：
 * 该包从「画一个光点」变成「这条边正在流动」。把 {@code botaniaHintIntervalTicks} 设为 1 即可回退原版行为。
 * </p>
 *
 * <p>
 * 版本敏感点：注入的是 {@code ManaSparkEntity#particlesTowards(Entity)} 内唯一的 {@code sendToTracking} 调用
 * （Botania 1.20.1-450 只有这一个粒子发送方法，两处调用点分别是喂玩家魔力物品与 transfers 循环）。Botania 升级后
 * 若该私有方法改名或改结构，此 mixin 会在启动期直接报错（配置为 required），这是刻意选择——静默失效会让带宽问题
 * 悄悄回归。
 * </p>
 */
@Mixin(value = ManaSparkEntity.class, remap = false)
public abstract class ManaSparkEntityMixin {

    @Redirect(
              method = "particlesTowards(Lnet/minecraft/world/entity/Entity;)V",
              at = @At(value = "INVOKE",
                       target = "Lvazkii/botania/xplat/XplatAbstractions;sendToTracking" +
                               "(Lnet/minecraft/world/entity/Entity;Lvazkii/botania/network/BotaniaPacket;)V"),
              remap = false)
    private void ctnhmana$throttleFlowHint(XplatAbstractions xplat, Entity anchor, BotaniaPacket packet) {
        CMConfig.SparkParticles config = CMConfig.spark();
        if (!config.enabled) {
            return;
        }
        Entity self = (Entity) (Object) this;
        int interval = config.hintInterval();
        // 用实体 id 错开相位，避免所有火花在同一 tick 一起发包形成峰值
        if (interval > 1 && (self.tickCount + self.getId()) % interval != 0) {
            return;
        }
        if (!(self.level() instanceof ServerLevel serverLevel)) {
            xplat.sendToTracking(anchor, packet);
            return;
        }
        Vec3 spawn = ctnhmana$spawnPos(serverLevel, packet, anchor);
        SparkEffectSender.sendNearSpawn(serverLevel, spawn.x, spawn.y, spawn.z, packet, config.hintRadius());
    }

    /** 粒子出生点为 {@code args[0]} 对应实体的中心（正向为火花自身，particlesFrom 为对端）。 */
    @Unique
    private static Vec3 ctnhmana$spawnPos(ServerLevel level, BotaniaPacket packet, Entity anchor) {
        Entity source = anchor;
        if (packet instanceof BotaniaEffectPacket effect) {
            int[] args = effect.args();
            if (args.length > 0) {
                Entity resolved = level.getEntity(args[0]);
                if (resolved != null) {
                    source = resolved;
                }
            }
        }
        return source.position().add(0.0D, source.getBbHeight() / 2.0D, 0.0D);
    }
}
