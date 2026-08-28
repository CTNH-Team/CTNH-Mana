package com.magicbee.ctnhmana.mixin.botania.client;

import net.minecraft.client.Minecraft;

import com.magicbee.ctnhmana.CMConfig;
import com.magicbee.ctnhmana.client.fx.SparkFlowClientTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;

/**
 * 记录服务端发来的火花流动提示，交给 {@link SparkFlowClientTracker} 在 TTL 内本地续画。
 *
 * <p>
 * 收到提示后接管该效果并 {@code ci.cancel()}：原版那一发粒子带 0.98 阻力、寿命 28~40 tick 随机，落点是
 * 距离的 0.86~1.11 倍，在尖塔那种长连线上会明显冲过火花中心。本地续画用匀速、寿命等于飞行时间的粒子，
 * 落点精确。粒子总开关关闭时不接管，保持原版行为。
 * </p>
 */
@Mixin(value = BotaniaEffectPacket.Handler.class, remap = false)
public class BotaniaEffectPacketHandlerMixin {

    @Inject(method = "handle", at = @At("HEAD"), cancellable = true, remap = false)
    private static void ctnhmana$recordSparkFlow(BotaniaEffectPacket packet, CallbackInfo ci) {
        if (packet.type() != EffectType.SPARK_MANA_FLOW) {
            return;
        }
        CMConfig.SparkParticles config = CMConfig.spark();
        if (!config.enabled || config.particlesPerConnection() <= 0) {
            return;
        }
        int[] args = packet.args();
        if (args.length < 3) {
            return;
        }
        int fromId = args[0];
        int toId = args[1];
        int color = args[2];
        // handle() 运行在网络线程，写表必须回到主线程
        Minecraft.getInstance().execute(() -> SparkFlowClientTracker.onFlowHint(fromId, toId, color));
        // 由本地续画接管，落点见类注释
        ci.cancel();
    }
}
