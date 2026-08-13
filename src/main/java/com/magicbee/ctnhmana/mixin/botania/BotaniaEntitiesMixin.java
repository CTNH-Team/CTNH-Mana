package com.magicbee.ctnhmana.mixin.botania;

import com.magicbee.ctnhmana.common.entity.DeltaSpark;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.common.entity.BotaniaEntities;

import static com.magicbee.ctnhmana.registry.CMEntities.DELTA_SPARK;
import static com.magicbee.ctnhmana.registry.CMEntities.OMEGA_SPARK;

@Mixin(value = BotaniaEntities.class, remap = false)
public class BotaniaEntitiesMixin {

    @Inject(method = "registerWandHudCaps", at = @At("TAIL"))
    private static void addCustomWandHud(BotaniaEntities.ECapConsumer<WandHUD> consumer, CallbackInfo ci) {
        // 这里写新增的consumer调用，逻辑和上面一致
        consumer.accept(entity -> new DeltaSpark.WandHud((DeltaSpark) entity), DELTA_SPARK.get());
        consumer.accept(entity -> new DeltaSpark.WandHud((DeltaSpark) entity), OMEGA_SPARK.get());
    }
}
