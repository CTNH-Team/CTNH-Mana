package com.moguang.ctnhmana.api.recipe.customlogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemInstance;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.cutting.GemCuttingMenu;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Runtime fallback for gem inlay: matches Apotheosis {@link GemCuttingMenu.RarityUpgrade}
 * against concrete gem NBT stacks in the machine inventory.
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
                    if (matRarity == gemRarity) {
                        matCost = GemCuttingMenu.STD_MAT_COST;
                    } else if (matRarity == RarityRegistry.next(gemRarity)) {
                        matCost = GemCuttingMenu.NEXT_MAT_COST;
                    } else if (matRarity == RarityRegistry.prev(gemRarity)) {
                        matCost = GemCuttingMenu.PREV_MAT_COST;
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
                    ResourceLocation matId = matRarity.getId();
                    String matPath = matId != null ? matId.getPath() : "mat";

                    return CMRecipeTypes.GEM_INLAY_RECIPES
                            .recipeBuilder(CTNHMana.id("gem_inlay/dynamic/" + path + "/" + matPath))
                            .inputItems(main.copyWithCount(1))
                            .inputItems(bot.copyWithCount(1))
                            .inputItems(new ItemStack(Adventure.Items.GEM_DUST.get(), dustCost))
                            .inputItems(mat.copyWithCount(matCost))
                            .outputItems(out)
                            .EUt(EU_PER_TICK)
                            .duration(DURATION)
                            .buildRawRecipe();
                }
            }
        }
        return null;
    }

    @CN("%s宝石")
    @EN("%s Gem")
    public static Lang any_rarity_gem;

    @CN("按宝石切割台规则升级稀有度")
    @EN("Upgrades gem rarity using Gem Cutting Table rules")
    public static Lang by_cutting;

    @Override
    public void buildRepresentativeRecipes() {
        // Representative recipes are registered via GemCuttingRecipes datagen builder.
    }
}
