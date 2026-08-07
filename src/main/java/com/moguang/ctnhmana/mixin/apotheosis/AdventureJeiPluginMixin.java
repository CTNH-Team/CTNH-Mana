package com.moguang.ctnhmana.mixin.apotheosis;

import dev.shadowsoffire.apotheosis.adventure.compat.AdventureJEIPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Hide original Apotheosis Gem Cutting JEI category; recipes live on the gem inlay machine.
 */
@Mixin(value = AdventureJEIPlugin.class, remap = false)
public class AdventureJeiPluginMixin {

    @Redirect(
              method = "registerCategories",
              at = @At(
                       value = "INVOKE",
                       target = "Lmezz/jei/api/registration/IRecipeCategoryRegistration;addRecipeCategories([Lmezz/jei/api/recipe/category/IRecipeCategory;)V",
                       ordinal = 2))
    private void ctnhmana$skipGemCuttingCategory(IRecipeCategoryRegistration reg,
                                                 mezz.jei.api.recipe.category.IRecipeCategory<?>... categories) {
        // skip GemCuttingCategory
    }

    @Redirect(
              method = "registerRecipes",
              at = @At(
                       value = "INVOKE",
                       target = "Lmezz/jei/api/registration/IRecipeRegistration;addRecipes(Lmezz/jei/api/recipe/RecipeType;Ljava/util/List;)V",
                       ordinal = 2))
    private <T> void ctnhmana$skipGemCuttingRecipes(IRecipeRegistration reg, RecipeType<T> recipeType,
                                                    List<T> recipes) {
        // skip GEM_CUTTING recipes
    }

    @Redirect(
              method = "registerRecipeCatalysts",
              at = @At(
                       value = "INVOKE",
                       target = "Lmezz/jei/api/registration/IRecipeCatalystRegistration;addRecipeCatalyst(Lnet/minecraft/world/item/ItemStack;Lmezz/jei/api/recipe/RecipeType;)V",
                       ordinal = 2))
    private void ctnhmana$skipGemCuttingCatalyst(IRecipeCatalystRegistration reg,
                                                 net.minecraft.world.item.ItemStack stack, RecipeType<?> type) {
        // skip gem cutting table catalyst
    }
}
