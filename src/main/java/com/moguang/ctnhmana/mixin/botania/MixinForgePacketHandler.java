package com.moguang.ctnhmana.mixin.botania;

import com.moguang.ctnhmana.api.networks.BotaniaEffectPacketExtend;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.forge.network.ForgePacketHandler;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(value = ForgePacketHandler.class, remap = false)
public abstract class MixinForgePacketHandler {
    @Shadow @Final public static SimpleChannel CHANNEL;

    @Shadow private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeClientBoundHandler(Consumer<T> consumer)
    {
        return null;
    };
    @Inject(
            method = "init()V", // 目标方法：ForgePacketHandler的init()
            at = @At("TAIL"),   // 注入位置：方法最后一行执行后
            remap = false       // 无需重映射
    )
    private static void injectRegisterCustomPacket(CallbackInfo ci) {
    int newi=11;
        CHANNEL.registerMessage(newi, BotaniaEffectPacketExtend.class, BotaniaEffectPacketExtend::encode, BotaniaEffectPacketExtend::decode,
                makeClientBoundHandler(BotaniaEffectPacketExtend.Handler::handle));
    }
}