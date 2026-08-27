package com.magicbee.ctnhmana.mixin.botania.client;

import net.minecraft.client.Minecraft;

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
 * 不取消原逻辑：Botania 自己画的那一个粒子正好当作首帧。若客户端缺少本 mixin（版本不一致等），
 * 也只是退化为「每秒一个光点」的稀疏效果，不会报错。
 * </p>
 */
@Mixin(value = BotaniaEffectPacket.Handler.class, remap = false)
public class BotaniaEffectPacketHandlerMixin {

    @Inject(method = "handle", at = @At("HEAD"), remap = false)
    private static void ctnhmana$recordSparkFlow(BotaniaEffectPacket packet, CallbackInfo ci) {
        if (packet.type() != EffectType.SPARK_MANA_FLOW) {
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
    }
}
