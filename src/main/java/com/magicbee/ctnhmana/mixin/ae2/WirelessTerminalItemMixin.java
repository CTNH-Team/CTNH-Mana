package com.magicbee.ctnhmana.mixin.ae2;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import appeng.items.tools.powered.WirelessTerminalItem;
import com.magicbee.ctnhmana.common.event.zenith.ZenithInvadeMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 虚境入侵期间禁止打开无线终端，并提示自定义断连文案。
 */
@Mixin(value = WirelessTerminalItem.class, remap = false)
public class WirelessTerminalItemMixin {

    @Inject(method = "checkPreconditions", at = @At("HEAD"), cancellable = true)
    private void ctnhmana$jamOpen(ItemStack item, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!ZenithInvadeMessages.isJammingWireless(player.level())) {
            return;
        }
        ZenithInvadeMessages.notifyWirelessJammed(player);
        cir.setReturnValue(false);
    }
}
