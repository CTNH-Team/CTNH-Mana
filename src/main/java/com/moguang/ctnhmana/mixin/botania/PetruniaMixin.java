package com.moguang.ctnhmana.mixin.botania;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import mythicbotany.functionalflora.Petrunia;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Petrunia.class, remap = false)
public class PetruniaMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void ctnhmana$setMaxMana(BlockEntityType<?> type, BlockPos pos, BlockState state, CallbackInfo ci) {
        ((FunctionalFlowerBaseAccessor) this).ctnhmana$setMaxMana(30000);
    }
}
