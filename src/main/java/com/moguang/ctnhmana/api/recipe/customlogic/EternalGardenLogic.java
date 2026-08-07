package com.moguang.ctnhmana.api.recipe.customlogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BotaniaFlowerBlocks;

import java.util.List;

/**
 * Runtime fallback for Eternal Garden: any food with Gourmaryllis, or any fuel with Endoflame.
 */
public class EternalGardenLogic implements GTRecipeType.ICustomRecipeLogic {

    @Override
    public @Nullable GTRecipeDefinition createCustomRecipe(RecipeHandlerGroup holder) {
        var recipeHandlers = holder.getInputHandlerMap().get(ItemRecipeCapability.CAP);
        if (recipeHandlers == null) {
            return null;
        }

        boolean hasEndoflame = false;
        boolean hasGourmaryllis = false;
        for (var handler : recipeHandlers) {
            for (var content : handler.getContents()) {
                if (!(content instanceof ItemStack stack) || stack.isEmpty()) {
                    continue;
                }
                if (stack.getItem().equals(BotaniaFlowerBlocks.endoflame.asItem())) {
                    hasEndoflame = true;
                }
                if (stack.getItem().equals(BotaniaFlowerBlocks.gourmaryllis.asItem())) {
                    hasGourmaryllis = true;
                }
            }
        }

        if (hasEndoflame) {
            var burn = searchBurnRecipe(recipeHandlers);
            if (burn != null) {
                return burn;
            }
        }
        if (hasGourmaryllis) {
            return searchFoodRecipe(recipeHandlers);
        }
        return null;
    }

    @Nullable
    private static GTRecipeDefinition searchFoodRecipe(List<? extends IRecipeHandler<?>> recipeHandlers) {
        for (var handler : recipeHandlers) {
            for (var content : handler.getContents()) {
                if (!(content instanceof ItemStack stack) || stack.isEmpty()) {
                    continue;
                }
                if (stack.getItem().equals(BotaniaFlowerBlocks.gourmaryllis.asItem())) {
                    continue;
                }
                var properties = stack.getFoodProperties(null);
                if (properties == null) {
                    continue;
                }
                var num = Math.pow(properties.getNutrition(), 3);
                return CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("eternal_garden/eats"))
                        .notConsumable(BotaniaFlowerBlocks.gourmaryllis.asItem())
                        .inputItems(stack.copyWithCount(1))
                        .outputFluids(CMMaterials.Mana.getFluid((int) (num)))
                        .duration((int) (num))
                        .EUt(GTValues.HV, 1)
                        .addData("type", "eat")
                        .buildRawRecipe();
            }
        }
        return null;
    }

    @Nullable
    private static GTRecipeDefinition searchBurnRecipe(List<? extends IRecipeHandler<?>> recipeHandlers) {
        for (var handler : recipeHandlers) {
            for (var content : handler.getContents()) {
                if (!(content instanceof ItemStack stack) || stack.isEmpty()) {
                    continue;
                }
                if (stack.getItem().equals(BotaniaFlowerBlocks.endoflame.asItem())) {
                    continue;
                }
                int burns = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
                if (burns <= 0) {
                    continue;
                }
                return CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("eternal_garden/burns"))
                        .notConsumable(BotaniaFlowerBlocks.endoflame.asItem())
                        .inputItems(stack.copyWithCount(1))
                        .outputFluids(CMMaterials.Mana.getFluid(1))
                        .duration(100)
                        .EUt(GTValues.HV, 1)
                        .addData("temp", burns)
                        .addData("type", "fire")
                        .buildRawRecipe();
            }
        }
        return null;
    }
}
