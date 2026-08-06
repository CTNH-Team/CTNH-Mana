package com.moguang.ctnhmana.mixin.botania;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import mythicbotany.config.MythicConfig;
import mythicbotany.functionalflora.WitherAconite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 凋零兔葵数值下调：每颗下界之星产出 60 万 mana，容量 1200，传输 400。
 */
@Mixin(value = WitherAconite.class, remap = false)
public class WitherAconiteMixin {

    private static final int CTNH_MANA_PER_STAR = 600_000;
    private static final int CTNH_MAX_MANA = 1200;
    private static final int CTNH_MAX_TRANSFER = 400;

    @Shadow
    @Final
    @Mutable
    public static int DEFAULT_MANA_PER_STAR;


    @Inject(method = "<init>", at = @At("TAIL"))
    private void ctnhmana$adjustCapaAndTransfer(BlockEntityType<?> type, BlockPos pos, BlockState state,
                                                CallbackInfo ci) {
        FunctionalFlowerBaseAccessor accessor = (FunctionalFlowerBaseAccessor) this;
        accessor.ctnhmana$setMaxMana(CTNH_MAX_MANA);
        accessor.ctnhmana$setMaxTransfer(CTNH_MAX_TRANSFER);
        // 配置可能在花类 clinit 之后才加载，构造时再写一次保证生效
        MythicConfig.flowers.witherAconiteMana = CTNH_MANA_PER_STAR;
    }
}