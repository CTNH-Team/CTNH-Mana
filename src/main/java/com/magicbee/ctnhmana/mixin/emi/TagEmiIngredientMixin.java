package com.magicbee.ctnhmana.mixin.emi;

import dev.emi.emi.api.stack.TagEmiIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = TagEmiIngredient.class, remap = false)
public abstract class TagEmiIngredientMixin {

    @Redirect(
              method = "render",
              at = @At(
                       value = "INVOKE",
                       target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    private Object ctnhmana$useLastStack(List<?> list, int index) {
        return list.get(list.size() - 1);
    }
}
