package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.moguang.ctnhmana.api.recipe.bloodmagic.BloodAltarRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.item.BloodMagicJade.EtchingJade;
import com.moguang.ctnhmana.registry.CMItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMItems.HORIZEN_RUNE;
import static com.moguang.ctnhmana.registry.CMMaterials.Zenith_essence;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.BLOOD_ALTAR_RECIPES;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.HELL_FORGE_RECIPES;
import static vazkii.botania.common.item.BotaniaItems.runeFire;

public class BloodAltarRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        BloodAltarRecipeBuilder.builder("test_1")
                .input(new ItemStack(runeFire,1))
                .output(new ItemStack(HORIZEN_RUNE,1))
                .syphon(10000)
                .minimumTier(2)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("blankslate")
                .output(new ItemStack(BloodMagicItems.BLANK_RUNE_ITEM.get(),1))
                .input(new ItemStack(BotaniaBlocks.livingrock.asItem(),1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("etching_circuit")
                .inputItems(BloodMagicItems.BLANK_RUNE_ITEM.get(),1)
                .circuitMeta(1)
                .outputItems(CMItems.RUNE_CIRCUIT_BOARD,1)
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .addCondition(new BloodAltarCondition(3,100,100*200,"etching"))
                .save(provider);
    }
}
