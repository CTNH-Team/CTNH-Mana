package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;

import com.moguang.ctnhmana.data.recipe.builder.apotheosis.SalvagingRecipeBuilder;
import dev.shadowsoffire.apotheosis.adventure.Adventure;

import java.util.function.Consumer;

/**
 * Apotheosis Salvaging Table recipes, migrated from {@code apotheosis:salvaging/*}.
 */
public class SalvagingRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        affixMaterials(provider);
        gemDust(provider);
        horseArmor(provider);
        RuneSalvagingRecipes.init(provider);
    }

    private static void affixMaterials(Consumer<FinishedRecipe> provider) {
        SalvagingRecipeBuilder.builder("common_material")
                .affixInput("apotheosis:common")
                .output(Adventure.Items.COMMON_MATERIAL.get(), 1, 4)
                .save(provider);

        SalvagingRecipeBuilder.builder("uncommon_material")
                .affixInput("apotheosis:uncommon")
                .output(Adventure.Items.UNCOMMON_MATERIAL.get(), 1, 4)
                .save(provider);

        SalvagingRecipeBuilder.builder("rare_material")
                .affixInput("apotheosis:rare")
                .output(Adventure.Items.RARE_MATERIAL.get(), 1, 4)
                .save(provider);

        SalvagingRecipeBuilder.builder("epic_material")
                .affixInput("apotheosis:epic")
                .output(Adventure.Items.EPIC_MATERIAL.get(), 1, 4)
                .save(provider);

        SalvagingRecipeBuilder.builder("mythic_material")
                .affixInput("apotheosis:mythic")
                .output(Adventure.Items.MYTHIC_MATERIAL.get(), 1, 4)
                .save(provider);
    }

    private static void gemDust(Consumer<FinishedRecipe> provider) {
        SalvagingRecipeBuilder.builder("common_gem_dust")
                .gemInput("apotheosis:common")
                .output(Adventure.Items.GEM_DUST.get(), 1, 2)
                .save(provider);

        SalvagingRecipeBuilder.builder("uncommon_gem_dust")
                .gemInput("apotheosis:uncommon")
                .output(Adventure.Items.GEM_DUST.get(), 1, 3)
                .save(provider);

        SalvagingRecipeBuilder.builder("rare_gem_dust")
                .gemInput("apotheosis:rare")
                .output(Adventure.Items.GEM_DUST.get(), 2, 4)
                .save(provider);

        SalvagingRecipeBuilder.builder("epic_gem_dust")
                .gemInput("apotheosis:epic")
                .output(Adventure.Items.GEM_DUST.get(), 2, 5)
                .save(provider);

        SalvagingRecipeBuilder.builder("mythic_gem_dust")
                .gemInput("apotheosis:mythic")
                .output(Adventure.Items.GEM_DUST.get(), 3, 6)
                .save(provider);

        SalvagingRecipeBuilder.builder("ancient_gem_dust")
                .gemInput("apotheosis:ancient")
                .output(Adventure.Items.GEM_DUST.get(), 4, 10)
                .save(provider);
    }

    private static void horseArmor(Consumer<FinishedRecipe> provider) {
        SalvagingRecipeBuilder.builder("leather_horse_armor")
                .itemInput(Items.LEATHER_HORSE_ARMOR)
                .output(Items.LEATHER, 3, 5)
                .save(provider);

        SalvagingRecipeBuilder.builder("iron_horse_armor")
                .itemInput(Items.IRON_HORSE_ARMOR)
                .output(Items.LEATHER, 1, 2)
                .output(Items.IRON_INGOT, 2, 4)
                .save(provider);

        SalvagingRecipeBuilder.builder("golden_horse_armor")
                .itemInput(Items.GOLDEN_HORSE_ARMOR)
                .output(Items.LEATHER, 1, 2)
                .output(Items.GOLD_INGOT, 2, 4)
                .save(provider);

        SalvagingRecipeBuilder.builder("diamond_horse_armor")
                .itemInput(Items.DIAMOND_HORSE_ARMOR)
                .output(Items.LEATHER, 1, 2)
                .output(Items.DIAMOND, 2, 4)
                .save(provider);
    }
}
