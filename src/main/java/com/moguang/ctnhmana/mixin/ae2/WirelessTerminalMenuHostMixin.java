package com.moguang.ctnhmana.mixin.ae2;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import appeng.helpers.WirelessTerminalMenuHost;
import com.moguang.ctnhmana.common.event.zenith.ZenithInvadeMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 虚境入侵期间强制无线终端 rangeCheck 失败，关闭已打开界面并显示自定义断连文案。
 * {@link appeng.helpers.WirelessCraftingTerminalMenuHost} 继承此类，一并生效。
 * <p>
 * 注意：{@code getPlayer}/{@code isClientSide} 在父类 {@code ItemMenuHost} 上，
 * 对子类目标 {@code @Shadow} 会解析失败，故用转型调用。
 */
@Mixin(value = WirelessTerminalMenuHost.class, remap = false)
public abstract class WirelessTerminalMenuHostMixin {

    @Inject(method = "checkWirelessRange", at = @At("HEAD"), cancellable = true)
    private void ctnhmana$jamRange(AbstractContainerMenu menu, CallbackInfoReturnable<Boolean> cir) {
        WirelessTerminalMenuHost self = (WirelessTerminalMenuHost) (Object) this;
        if (self.isClientSide()) {
            return;
        }
        Player player = self.getPlayer();
        if (!ZenithInvadeMessages.isJammingWireless(player.level())) {
            return;
        }
        ZenithInvadeMessages.notifyWirelessJammed(player);
        cir.setReturnValue(false);
    }
}
