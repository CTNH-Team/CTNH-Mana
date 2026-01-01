package com.moguang.ctnhmana.mixin.bloodmagic;

import net.minecraft.data.recipes.FinishedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wayoftime.bloodmagic.common.data.GeneratorRecipes;

import java.util.function.Consumer;

@Mixin(GeneratorRecipes.class)
public abstract class BMRecipeCancelMixin {
    @Inject(
            at = @At("HEAD"),
            method = "buildRecipes",
            cancellable = true
    )
    protected void buildRecipes(Consumer<FinishedRecipe> consumer, CallbackInfo ci) {
        ci.cancel();
    }
}
