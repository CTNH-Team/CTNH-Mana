package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.moguang.ctnhmana.api.recipe.customlogic.IndustrialGemCuttingLogic;
import com.moguang.ctnhmana.data.recipe.builder.apotheosis.GemCuttingRecipeBuilder;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.cutting.GemCuttingMenu;
import dev.shadowsoffire.placebo.reload.DynamicHolder;

import java.util.function.Consumer;

/**
 * Apotheosis Gem Cutting Table rarity upgrades → gem inlay machine GT recipes.
 * Native hardcoded recipes are cleared via mixin; original JEI category is hidden.
 */
public class GemCuttingRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // common → uncommon
        upgrade(provider, "common",
                Adventure.Items.COMMON_MATERIAL.get(), GemCuttingMenu.STD_MAT_COST,
                Adventure.Items.UNCOMMON_MATERIAL.get(), GemCuttingMenu.NEXT_MAT_COST,
                null, 0);

        // uncommon → rare
        upgrade(provider, "uncommon",
                Adventure.Items.UNCOMMON_MATERIAL.get(), GemCuttingMenu.STD_MAT_COST,
                Adventure.Items.RARE_MATERIAL.get(), GemCuttingMenu.NEXT_MAT_COST,
                Adventure.Items.COMMON_MATERIAL.get(), GemCuttingMenu.PREV_MAT_COST);

        // rare → epic
        upgrade(provider, "rare",
                Adventure.Items.RARE_MATERIAL.get(), GemCuttingMenu.STD_MAT_COST,
                Adventure.Items.EPIC_MATERIAL.get(), GemCuttingMenu.NEXT_MAT_COST,
                Adventure.Items.UNCOMMON_MATERIAL.get(), GemCuttingMenu.PREV_MAT_COST);

        // epic → mythic
        upgrade(provider, "epic",
                Adventure.Items.EPIC_MATERIAL.get(), GemCuttingMenu.STD_MAT_COST,
                Adventure.Items.MYTHIC_MATERIAL.get(), GemCuttingMenu.NEXT_MAT_COST,
                Adventure.Items.RARE_MATERIAL.get(), GemCuttingMenu.PREV_MAT_COST);

        // mythic → ancient (ancient material unavailable → no next-mat variant)
        upgrade(provider, "mythic",
                Adventure.Items.MYTHIC_MATERIAL.get(), GemCuttingMenu.STD_MAT_COST,
                null, 0,
                Adventure.Items.EPIC_MATERIAL.get(), GemCuttingMenu.PREV_MAT_COST);
    }

    private static void upgrade(Consumer<FinishedRecipe> provider, String rarityPath,
                                Item stdMat, int stdCost,
                                Item nextMat, int nextCost,
                                Item prevMat, int prevCost) {
        DynamicHolder<LootRarity> rarityHolder = RarityRegistry.INSTANCE.holder(Apotheosis.loc(rarityPath));
        if (!rarityHolder.isBound()) {
            return;
        }
        LootRarity rarity = rarityHolder.get();
        if (rarity == RarityRegistry.getMaxRarity().get()) {
            return;
        }
        LootRarity next = rarity.next();

        ItemStack gem = representativeGem(rarity);
        ItemStack out = representativeGem(next);
        int dust = GemCuttingMenu.getDustCost(rarity);

        GemCuttingRecipeBuilder.builder(rarityPath + "/std")
                .gem(gem)
                .material(new ItemStack(stdMat), stdCost)
                .dustCount(dust)
                .output(out)
                .circuitMeta(1)
                .save(provider);

        if (nextMat != null) {
            GemCuttingRecipeBuilder.builder(rarityPath + "/next")
                    .gem(gem)
                    .material(new ItemStack(nextMat), nextCost)
                    .dustCount(dust)
                    .output(out)
                    .circuitMeta(2)
                    .save(provider);
        }

        if (prevMat != null) {
            GemCuttingRecipeBuilder.builder(rarityPath + "/prev")
                    .gem(gem)
                    .material(new ItemStack(prevMat), prevCost)
                    .dustCount(dust)
                    .output(out)
                    .circuitMeta(3)
                    .save(provider);
        }
    }

    private static ItemStack representativeGem(LootRarity rarity) {
        ItemStack gem = new ItemStack(Adventure.Items.GEM.get());
        AffixHelper.setRarity(gem, rarity);
        gem.setHoverName(IndustrialGemCuttingLogic.any_rarity_gem.translate(rarity.toComponent()));
        return gem;
    }
}
