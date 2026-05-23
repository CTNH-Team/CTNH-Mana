package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.InfusionCellCastingCondition;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.items.CMFuelItems;
import mythicbotany.register.ModItems;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.foil;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.moguang.ctnhmana.registry.CMItems.BROKEN_RUNE;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static com.moguang.ctnhmana.registry.items.CMFuelItems.*;

public class TwistCollapseRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        TwistCollapse.recipeBuilder("broken")
                .inputItems(BotaniaTags.Items.RUNES)
                .outputItems(BROKEN_RUNE)
                .hideDuration(true)
                .duration(1)
                .save(provider);

        // AHCC 注术单元崩解：TwistCollapse 匹配后产出对应崩解态（见 ArcaneHighEnergyCompressionReactorCore#testTryTwistCollapseRecipeOnce）
        TwistCollapse.recipeBuilder("mana_spark_stick_disintegration")
                .inputItems(SPARK_STICK.asStack(1))
                .outputItems(SPARK_STICK_DISINTEGRATED.asStack(1))
                .hideDuration(true)
                .duration(1)
                .save(provider);
        TwistCollapse.recipeBuilder("mana_advanced_spark_stick_disintegration")
                .inputItems(ADVANCED_SPARK_STICK.asStack(1))
                .outputItems(ADVANCED_SPARK_STICK_DISINTEGRATED.asStack(1))
                .hideDuration(true)
                .duration(1)
                .save(provider);
        TwistCollapse.recipeBuilder("mana_terra_stick_disintegration")
                .inputItems(TERRA_STICK.asStack(1))
                .outputItems(TERRA_STICK_DISINTEGRATED.asStack(1))
                .hideDuration(true)
                .duration(1)
                .save(provider);
        TwistCollapse.recipeBuilder("mana_mixed_will_stick_disintegration")
                .inputItems(MIXED_WILL_STICK.asStack(1))
                .outputItems(MIXED_WILL_STICK_DISINTEGRATED.asStack(1))
                .hideDuration(true)
                .duration(1)
                .save(provider);
        TwistCollapse.recipeBuilder("mana_uranium_nucleon_fluctuation_disintegration")
                .inputItems(URANIUM_NUCLEON_FLUCTUATION_INFUSION_CELL.asStack(1))
                .outputItems(URANIUM_NUCLEON_FLUCTUATION_INFUSION_CELL_CLEAVED.asStack(1))
                .hideDuration(true)
                .duration(1)
                .save(provider);
        // 火花燃料棒
        MANA_FUEL_INFUSER_RECIPES.recipeBuilder("spark_recipes")
                .inputItems(ChemicalHelper.get(TagPrefix.foil, CMMaterials.ManaSteel), 644)
                .inputItems(GTItems.FLUID_CELL_LARGE_STAINLESS_STEEL)
                .inputItems(BotaniaItems.spark, 32)
                .inputItems(BotaniaItems.runeFire, 32)
                .outputItems(SPARK_STICK)
                .addCondition(new InfusionCellCastingCondition(500 * 10))
                .duration(100)
                .EUt(320)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("spark_recycle")
                .inputItems(SPARK_STICK_DISINTEGRATED)
                .circuitMeta(16)
                .outputItems(ModItems.muspelheimRune, 4)
                .outputItems(BotaniaItems.corporeaSpark, 8)
                .outputItems(CMFuelItems.SPARK_SUBSTRATE, 4)
                .chancedOutput(dust, CMMaterials.AlfSteel, 4, 1111, 0)
                .chancedOutput(dust, CMMaterials.TerraSteel, 4, 2222, 0)
                .chancedOutput(dust, CMMaterials.Elementium, 4, 6666, 0)
                .outputFluids(CMMaterials.Mana.getFluid(1000))
                .outputFluids(CMMaterials.Twisted_Aggregate_Matrix.getFluid(288))
                .duration(100)
                .EUt(320)
                .save(provider);
        // 进阶燃料棒
        MANA_FUEL_INFUSER_RECIPES.recipeBuilder("ex_spark_recipes")
                .inputItems(ChemicalHelper.get(TagPrefix.foil, CMMaterials.Aerialite), 64)
                .inputItems(GTItems.FLUID_CELL_LARGE_TITANIUM)
                .inputItems(CMFuelItems.SPARK_SUBSTRATE, 4)
                .inputItems(ModItems.muspelheimRune, 16)
                .outputItems(ADVANCED_SPARK_STICK)
                .addCondition(new InfusionCellCastingCondition(1000 * 10))
                .duration(100)
                .EUt(640)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("ex_spark_recycle")
                .inputItems(ADVANCED_SPARK_STICK_DISINTEGRATED)
                .circuitMeta(16)
                .outputItems(ModItems.muspelheimRune, 4)
                .outputItems(BotaniaItems.corporeaSpark, 8)
                .outputItems(CMFuelItems.SPARK_SUBSTRATE, 16)
                .chancedOutput(dust, CMMaterials.SHADOWIUM, 4, 2500, 0)
                .chancedOutput(dust, CMMaterials.Photonium, 4, 2500, 0)
                .chancedOutput(new ItemStack(CMFuelItems.SPARK_SUBSTRATE, 16), 1000, 0)
                .outputFluids(CMMaterials.Mana.getFluid(2000))
                .outputFluids(CMMaterials.Twisted_Aggregate_Matrix.getFluid(144 * 4))
                .duration(100)
                .EUt(1280)
                .save(provider);
        // 泰拉燃料棒
        MANA_FUEL_INFUSER_RECIPES.recipeBuilder("terra_recipes")
                .inputItems(ChemicalHelper.get(TagPrefix.foil, CMMaterials.TerraSteel), 64)
                .inputItems(GTItems.FLUID_CELL_LARGE_TITANIUM)
                .inputItems(CMFuelItems.SPARK_SUBSTRATE, 8)
                .inputItems(BotaniaItems.lifeEssence, 64)
                .outputItems(TERRA_STICK)
                .addCondition(new InfusionCellCastingCondition(2000 * 10))
                .duration(100)
                .EUt(640)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("terra_recycle")
                .inputItems(TERRA_STICK_DISINTEGRATED)
                .circuitMeta(16)
                .outputItems(CMFuelItems.SPARK_SUBSTRATE, 16)
                .outputItems(TERRA_SUBSTRATE, 8)
                .outputFluids(CMMaterials.Mana.getFluid(4000))
                .outputFluids(CMMaterials.Twisted_Aggregate_Matrix.getFluid(144 * 6))
                .duration(100)
                .EUt(1280)
                .save(provider);
        // 裂晶燃料棒
        MANA_FUEL_INFUSER_RECIPES.recipeBuilder("mixin_will_fuel")
                .inputItems(ChemicalHelper.get(TagPrefix.foil, CMMaterials.DEMON), 64)
                .inputItems(GTItems.FLUID_CELL_LARGE_TITANIUM)
                .inputItems(ChemicalHelper.get(TagPrefix.dust, CMMaterials.PRIMOVOLITHEST), 4)
                .inputItems(BloodMagicItems.RAW_CRYSTAL, 32)
                .inputItems(CMItems.TWIST_RUNE)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), 3792))
                .outputItems(MIXED_WILL_STICK)
                .addCondition(new InfusionCellCastingCondition(44444))
                .duration(666)
                .EUt(6666)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("will_recycle")
                .inputItems(MIXED_WILL_STICK)
                .addCondition(new HellForgeCondition(4))
                .circuitMeta(16)
                .chancedOutput(new ItemStack(BloodMagicItems.STEADFAST_CRYSTAL.get(), 32), 2500, 0)
                .chancedOutput(new ItemStack(BloodMagicItems.CORROSIVE_CRYSTAL.get(), 32), 2500, 0)
                .chancedOutput(new ItemStack(BloodMagicItems.VENGEFUL_CRYSTAL.get(), 32), 2500, 0)
                .chancedOutput(new ItemStack(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get(), 32), 2500, 0)
                .outputFluids(CMMaterials.Twisted_Aggregate_Matrix.getFluid(144 * 8))
                .outputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), 6666))
                .duration(100)
                .EUt(6666)
                .save(provider);

        MANA_FUEL_INFUSER_RECIPES.recipeBuilder("u_fuel_recipe")
                .inputItems(ModItems.muspelheimRune, 8)
                .inputItems(ChemicalHelper.get(dust, Uranium235), 256)
                .inputItems(ChemicalHelper.get(dust, Europium), 8)
                .inputFluids(UraniumHexafluoride.getFluid(324))
                .inputItems(ChemicalHelper.get(dust, Europium), 4)
                .inputItems(CMItems.CORROSIVE_CORE.asStack())
                .inputItems(ChemicalHelper.get(foil, CMMaterials.HEMOPLATINUM), 64)
                .inputItems(CMFuelItems.TERRA_SUBSTRATE.asStack())
                .outputItems(URANIUM_NUCLEON_FLUCTUATION_INFUSION_CELL)
                .addCondition(new InfusionCellCastingCondition(14400))
                .duration(400)
                .EUt(4096)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("uranium_nucleon_fluctuation_cleaved_recycle")
                .inputItems(URANIUM_NUCLEON_FLUCTUATION_INFUSION_CELL_CLEAVED)
                .circuitMeta(16)
                .outputFluids(CMMaterials.Mana.getFluid(8000))
                .outputFluids(CMMaterials.Twisted_Aggregate_Matrix.getFluid(144 * 16))
                .chancedOutput(dust, Uranium235, 32, 1500, 0)
                .chancedOutput(dust, Uranium238, 32, 2200, 0)
                .chancedOutput(dust, Neptunium, 32, 1200, 0)
                .chancedOutput(dust, Americium, 32, 800, 0)
                .chancedOutput(dust, Plutonium239, 32, 1400, 0)
                .chancedOutput(dust, Plutonium241, 32, 900, 0)
                .chancedOutput(dust, Thorium, 32, 2500, 0)
                .chancedOutput(dust, Protactinium, 32, 1100, 0)
                .duration(200)
                .EUt(1920)
                .save(provider);
    }
}
