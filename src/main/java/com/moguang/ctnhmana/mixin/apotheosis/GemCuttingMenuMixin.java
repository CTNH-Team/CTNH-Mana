package com.moguang.ctnhmana.mixin.apotheosis;

import dev.shadowsoffire.apotheosis.adventure.socket.gem.cutting.GemCuttingMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Clears hardcoded gem cutting recipes so upgrades only run on the gem inlay machine.
 */
@Mixin(value = GemCuttingMenu.class, remap = false)
public class GemCuttingMenuMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void ctnhmana$clearNativeRecipes(CallbackInfo ci) {
        List<?> recipes = GemCuttingMenu.RECIPES;
        recipes.clear();
    }
}
