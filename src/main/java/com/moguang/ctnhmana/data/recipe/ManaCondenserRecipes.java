package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import com.moguang.ctnhmana.registry.CMMaterials;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMRecipeTypes.MANA_CONDENSER_RECIPES;

public class ManaCondenserRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        MANA_CONDENSER_RECIPES.recipeBuilder("mana")// 魔力
                .circuitMeta(0)
                .outputFluids(CMMaterials.Mana.getFluid(200))
                .EUt(480)
                .duration(50)
                .save(provider);
        // MANA_CONDENSER_RECIPES.recipeBuilder("mana_helium")
        // .circuitMeta(1)
        // .inputFluids(GTMaterials.Helium.getFluid(100))
        // .outputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100))
        // .outputFluids(CTNHMaterials.Mana.getFluid(200))
        // .EUt(200)
        // .duration(50)
        // .save(provider);
        // MANA_CONDENSER_RECIPES.recipeBuilder("mana_steam")
        // .circuitMeta(2)
        // .inputFluids(FluidIngredient.of(Fluids.WATER, 4000))
        // .outputFluids(GTMaterials.Steam.getFluid(4000))
        // .inputFluids(CTNHMaterials.Mana.getFluid(200))
        // .EUt(1920)
        // .duration(50)
        // .addData("mode", "reverse")
        // .save(provider);
        // MANA_CONDENSER_RECIPES.recipeBuilder("mana_de")
        // .circuitMeta(2)
        // .inputFluids(GTMaterials.Deuterium.getFluid(40))
        // .outputFluids(CTNHMaterials.HotDeuterium.getFluid(40))
        // .inputFluids(CTNHMaterials.Mana.getFluid(200))
        // .EUt(1920)
        // .duration(50)
        // .addData("mode", "reverse")
        // .save(provider);
        // MANA_CONDENSER_RECIPES.recipeBuilder("mana_na")
        // .circuitMeta(2)
        // .inputFluids(GTMaterials.Sodium.getFluid(20))
        // .outputFluids(CTNHMaterials.HotSodium.getFluid(20))
        // .inputFluids(CTNHMaterials.Mana.getFluid(200))
        // .EUt(1920)
        // .duration(50)
        // .addData("mode", "reverse")
        // .save(provider);
        // MANA_CONDENSER_RECIPES.recipeBuilder("mana_nak")
        // .circuitMeta(2)
        // .inputFluids(GTMaterials.SodiumPotassium.getFluid(20))
        // .outputFluids(CTNHMaterials.HotSodiumPotassium.getFluid(20))
        // .inputFluids(CTNHMaterials.Mana.getFluid(200))
        // .EUt(1920)
        // .duration(50)
        // .addData("mode", "reverse")
        // .save(provider);
    }
}
