package com.moguang.ctnhmana.mixin.botania;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;

@Mixin(value = ManaPoolBlockEntity.class, remap = false)
public abstract class ManaPoolBlockEntityMixin extends BlockEntity {

    @Shadow
    private int manaCap;

    public ManaPoolBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "initManaCapAndNetwork", at = @At(value = "HEAD"))
    public void CTNH$initManaCapAndNetwork(CallbackInfo ci) {
        if (manaCap == -1) {
            var cap=((ManaPoolBlock) getBlockState().getBlock()).variant;
            if(cap== ManaPoolBlock.Variant.DILUTED)manaCap=10000;
            if(cap== ManaPoolBlock.Variant.DEFAULT)manaCap=1500000;
            if(cap==ManaPoolBlock.Variant.FABULOUS)manaCap=10000000;
//            manaCap = ((ManaPoolBlock) getBlockState().getBlock()).variant == ManaPoolBlock.Variant.DILUTED ? 100000 :
//                    10000000;;

        }


    }
}