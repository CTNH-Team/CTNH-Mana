package com.magicbee.ctnhmana.api.recipe.customlogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.magicbee.ctnhmana.data.recipe.builder.apotheosis.GemCuttingRecipeBuilder;
import com.magicbee.ctnhmana.registry.CMRecipeTypes;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemInstance;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.cutting.GemCuttingMenu;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Runtime fallback for gem inlay: matches Apotheosis {@link GemCuttingMenu.RarityUpgrade}
 * against concrete gem NBT stacks. Also builds XEI representative recipes (per gem × rarity)
 * via {@link GemCuttingRecipeBuilder} once registries are loaded.
 */
public class IndustrialGemCuttingLogic implements GTRecipeType.ICustomRecipeLogic {

    private static final long EU_PER_TICK = GTValues.VA[GTValues.LV];
    private static final int DURATION = 20 * 10;

    @Override
    public @Nullable GTRecipeDefinition createCustomRecipe(RecipeHandlerGroup holder) {
        var recipeHandlers = holder.getInputHandlerMap().get(ItemRecipeCapability.CAP);
        if (recipeHandlers == null) {
            return null;
        }

        List<ItemStack> gems = new ArrayList<>();
        ItemStack dust = ItemStack.EMPTY;
        List<ItemStack> materials = new ArrayList<>();

        for (var handler : recipeHandlers) {
            for (var content : handler.getContents()) {
                if (!(content instanceof ItemStack stack) || stack.isEmpty()) {
                    continue;
                }
                if (stack.getItem() == Adventure.Items.GEM_DUST.get()) {
                    dust = stack;
                    continue;
                }
                GemInstance gem = GemInstance.unsocketed(stack);
                if (gem.isValidUnsocketed()) {
                    gems.add(stack);
                    continue;
                }
                if (RarityRegistry.isMaterial(stack.getItem())) {
                    materials.add(stack);
                }
            }
        }

        if (gems.size() < 2 || dust.isEmpty() || materials.isEmpty()) {
            return null;
        }

        for (int i = 0; i < gems.size(); i++) {
            ItemStack main = gems.get(i);
            GemInstance mainGem = GemInstance.unsocketed(main);
            if (!mainGem.isValidUnsocketed() || mainGem.isMaxRarity()) {
                continue;
            }
            for (int j = 0; j < gems.size(); j++) {
                if (i == j) continue;
                ItemStack bot = gems.get(j);
                GemInstance botGem = GemInstance.unsocketed(bot);
                if (!botGem.isValidUnsocketed() || botGem.gem() != mainGem.gem() ||
                        botGem.rarity() != mainGem.rarity()) {
                    continue;
                }

                int dustCost = GemCuttingMenu.getDustCost(mainGem.rarity().get());
                if (dust.getCount() < dustCost) {
                    continue;
                }

                DynamicHolder<LootRarity> gemRarity = mainGem.rarity();
                for (ItemStack mat : materials) {
                    DynamicHolder<LootRarity> matRarity = RarityRegistry.getMaterialRarity(mat.getItem());
                    int matCost;
                    String variant;
                    if (matRarity == gemRarity) {
                        matCost = GemCuttingMenu.STD_MAT_COST;
                        variant = "std";
                    } else if (matRarity == RarityRegistry.next(gemRarity)) {
                        matCost = GemCuttingMenu.NEXT_MAT_COST;
                        variant = "next";
                    } else if (matRarity == RarityRegistry.prev(gemRarity)) {
                        matCost = GemCuttingMenu.PREV_MAT_COST;
                        variant = "prev";
                    } else {
                        continue;
                    }
                    if (mat.getCount() < matCost) {
                        continue;
                    }

                    ItemStack out = main.copyWithCount(1);
                    AffixHelper.setRarity(out, RarityRegistry.next(gemRarity).get());

                    ResourceLocation rarityId = gemRarity.getId();
                    String path = rarityId != null ? rarityId.getPath() : ("ordinal_" + gemRarity.get().ordinal());
                    ResourceLocation gemId = mainGem.gem().getId();
                    String gemPath = gemId != null ? gemId.getPath().replace('/', '_') : "gem";

                    return GemCuttingRecipeBuilder.builder("dynamic/" + gemPath + "/" + path + "/" + variant)
                            .gem(main)
                            .output(out)
                            .material(mat, matCost)
                            .dustCount(dustCost)
                            .EUt(EU_PER_TICK)
                            .duration(DURATION)
                            .buildRawRecipe();
                }
            }
        }
        return null;
    }

    /**
     * XEI display only: real gem stacks from {@link GemRegistry} (not rarity-only shells).
     * Runtime matching remains in {@link #createCustomRecipe}.
     */
    @Override
    public void buildRepresentativeRecipes() {
        if (GemRegistry.INSTANCE.getValues().isEmpty() || RarityRegistry.INSTANCE.getOrderedRarities().isEmpty()) {
            return;
        }

        LootRarity max = RarityRegistry.getMaxRarity().get();
        for (Gem gem : GemRegistry.INSTANCE.getValues()) {
            LootRarity rarity = RarityRegistry.getMinRarity().get();
            while (rarity != max) {
                if (gem.clamp(rarity) == rarity) {
                    ItemStack in = GemRegistry.createGemStack(gem, rarity);
                    ItemStack out = GemRegistry.createGemStack(gem, rarity.next());
                    int dust = GemCuttingMenu.getDustCost(rarity);
                    ResourceLocation gemId = gem.getId();
                    ResourceLocation rarityId = RarityRegistry.INSTANCE.getKey(rarity);
                    String gemPath = gemId != null ? gemId.getPath().replace('/', '_') : "gem";
                    String rarityPath = rarityId != null ? rarityId.getPath() : ("ord_" + rarity.ordinal());

                    // std material (cost 3)
                    var std = GemCuttingRecipeBuilder.builder("xei/" + gemPath + "/" + rarityPath + "/std")
                            .gem(in)
                            .output(out)
                            .material(rarity.getMaterial(), GemCuttingMenu.STD_MAT_COST)
                            .dustCount(dust)
                            .circuitMeta(1)
                            .EUt(EU_PER_TICK)
                            .duration(DURATION)
                            .buildRawRecipe();
                    CMRecipeTypes.GEM_INLAY_RECIPES.addToMainCategory(std.withId(std.getId().withPrefix("/")));

                    // next material (cost 1), skip when next is ancient (material unavailable)
                    LootRarity next = rarity.next();
                    ResourceLocation nextId = RarityRegistry.INSTANCE.getKey(next);
                    if (nextId == null || !"ancient".equals(nextId.getPath())) {
                        var nextRecipe = GemCuttingRecipeBuilder
                                .builder("xei/" + gemPath + "/" + rarityPath + "/next")
                                .gem(in)
                                .output(out)
                                .material(next.getMaterial(), GemCuttingMenu.NEXT_MAT_COST)
                                .dustCount(dust)
                                .circuitMeta(2)
                                .EUt(EU_PER_TICK)
                                .duration(DURATION)
                                .buildRawRecipe();
                        CMRecipeTypes.GEM_INLAY_RECIPES
                                .addToMainCategory(nextRecipe.withId(nextRecipe.getId().withPrefix("/")));
                    }

                    // prev material (cost 9)
                    if (rarity != RarityRegistry.getMinRarity().get()) {
                        var prevRecipe = GemCuttingRecipeBuilder
                                .builder("xei/" + gemPath + "/" + rarityPath + "/prev")
                                .gem(in)
                                .output(out)
                                .material(rarity.prev().getMaterial(), GemCuttingMenu.PREV_MAT_COST)
                                .dustCount(dust)
                                .circuitMeta(3)
                                .EUt(EU_PER_TICK)
                                .duration(DURATION)
                                .buildRawRecipe();
                        CMRecipeTypes.GEM_INLAY_RECIPES
                                .addToMainCategory(prevRecipe.withId(prevRecipe.getId().withPrefix("/")));
                    }
                }
                rarity = rarity.next();
            }
        }
    }
}
