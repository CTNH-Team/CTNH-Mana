package com.magicbee.ctnhmana.mixin.bloodmagic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.will.DemonWillHolder;

/**
 * 修复上游 {@link DemonWillHolder#addWill(EnumDemonWillType, double, double)} 的上限失效 bug：
 * 原实现先算出 added = min(max - current, amount) 用于返回值，却把 amount 全量写入存储，
 * 导致 max 上限形同虚设（恶魔坩埚的 100 上限、恶魔意志发电机的意志转移上限全部失效）。
 * 这里在返回前把超出上限的部分回退，使实际存储净增量严格等于返回的 added。
 */
@Mixin(value = DemonWillHolder.class, remap = false)
public abstract class DemonWillHolderMixin {

    @Inject(method = "addWill(Lwayoftime/bloodmagic/api/compat/EnumDemonWillType;DD)D",
            at = @At("RETURN"),
            cancellable = true)
    private void ctnh$enforceWillCap(EnumDemonWillType type, double amount, double max,
                                     CallbackInfoReturnable<Double> cir) {
        double overflow = amount - cir.getReturnValue();
        if (overflow > 0) {
            ((DemonWillHolder) (Object) this).drainWill(type, overflow);
        }
    }
}
