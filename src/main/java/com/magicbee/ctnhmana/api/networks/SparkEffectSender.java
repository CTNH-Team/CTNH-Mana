package com.magicbee.ctnhmana.api.networks;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.PacketDistributor;

import vazkii.botania.forge.network.ForgePacketHandler;
import vazkii.botania.network.BotaniaPacket;

/**
 * 按「粒子出生点」的半径发送火花粒子包。
 *
 * <p>
 * 客户端的 wisp 粒子类型 {@code overrideLimiter == false}，`addAlwaysVisibleParticle` 走 {@code force = false}
 * 分支，因此相机 32 格之外的火花粒子本来就会被 100% 丢弃；而 Botania 默认用 {@code TRACKING_ENTITY_AND_SELF}
 * 发到 64~80 格。改成按出生点做半径裁剪属于零视觉损失的纯削减。
 * </p>
 */
public final class SparkEffectSender {

    private SparkEffectSender() {}

    public static void sendNearSpawn(ServerLevel level, double x, double y, double z, BotaniaPacket packet,
                                     double radius) {
        ForgePacketHandler.CHANNEL.send(
                PacketDistributor.NEAR.with(
                        () -> new PacketDistributor.TargetPoint(x, y, z, radius, level.dimension())),
                packet);
    }
}
