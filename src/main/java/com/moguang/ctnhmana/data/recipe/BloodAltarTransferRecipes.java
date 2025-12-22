package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeSerializer;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;

import javax.annotation.Nullable;
import java.util.function.Consumer;


public class BloodAltarTransferRecipes {
    public static void convertAllRecipesOfType(@NotNull RecipeType<?> recipeType,
                                               @NotNull GTRecipeType targetRecipeType,
                                               @NotNull net.minecraft.world.item.crafting.RecipeManager recipeManager,
                                               @NotNull Consumer<GTRecipe> consumer) {
        // 获取该类型的所有配方
        var recipes = recipeManager.getAllRecipesFor(recipeType);

        for (Recipe<?> recipe : recipes) {
            var gtRecipe=recipe;
            if (gtRecipe != null) {
                consumer.accept((GTRecipe) gtRecipe);
            }
        }
    }
    public static void convertAllBMRecipesOfType(@NotNull RecipeType<?> recipeType,
                                               @NotNull GTRecipeType targetRecipeType,
                                               @NotNull net.minecraft.world.item.crafting.RecipeManager recipeManager,
                                               @NotNull Consumer<GTRecipe> consumer) {
        // 获取该类型的所有配方
        var recipes = recipeManager.getAllRecipesFor(BloodMagicRecipeType.ALTAR.get());

        for (Recipe<?> recipe : recipes) {
            var gtRecipe=recipe;
            if (gtRecipe != null) {
                consumer.accept((GTRecipe) gtRecipe);
            }
        }
    }
    @Nullable
    public static GTRecipe convertToGTRecipe(@NotNull ResourceLocation id,
                                             @NotNull Recipe<?> recipe,
                                             @NotNull GTRecipeType targetRecipeType) {
        try {
            // 使用目标配方类型的recipeBuilder创建构建器
            var builder = targetRecipeType.recipeBuilder(id);

            // 添加输入：遍历原配方的所有ingredients
            for (var ingredient : recipe.getIngredients()) {
                if (!ingredient.isEmpty()) {
                    builder.inputItems(ingredient);
                }
            }

            // 添加输出：获取原配方的输出物品
            RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
            ItemStack resultItem = recipe.getResultItem(registryAccess);
            if (!resultItem.isEmpty()) {
                builder.outputItems(resultItem);
            }

            // 如果是SmeltingRecipe（原版熔炉配方），保留烹饪时间
            if (recipe instanceof SmeltingRecipe smeltingRecipe) {
                builder.duration(smeltingRecipe.getCookingTime());
            } else {
                // 对于其他类型的配方，使用默认时间（200 ticks，与原版熔炉相同）
                builder.duration(200);
            }

            // 构建并返回GT配方
            return GTRecipeSerializer.SERIALIZER.fromJson(id, builder.build().serializeRecipe());

        } catch (Exception e) {
            GTCEu.LOGGER.error("Failed to convert recipe {} to GT recipe: {}", id, e.getMessage());
            return null;
        }
    }


}
