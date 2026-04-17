package com.moguang.ctnhmana.mixin.emi;

import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.TagEmiIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value= TagEmiIngredient.class,remap=false)
public abstract class TagEmiIngredientMixin {
        @Redirect(
                method = "render",
                at = @At(
                        value = "INVOKE",
                        target = "Ljava/util/List;get(I)Ljava/lang/Object;"
                )
        )
        private Object ctnhmana$useLastStack(List<?> list, int index) {
            return list.get(list.size() - 1);
        }

}