package com.moguang.ctnhmana.api.recipe.customlogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

/**
 * Fallback for industrial salvaging when no registered GT recipe matches:
 * gems / affix items with a bound {@link LootRarity} produce dust / rarity material by tier.
 */
public class IndustrialSalvagingLogic implements GTRecipeType.ICustomRecipeLogic {

    private static final long EU_PER_TICK = GTValues.VA[GTValues.ULV];
    private static final int DURATION = 20 * 5;
    private static final int AFFIX_MIN = 1;
    private static final int AFFIX_MAX = 4;

    @Override
    public @Nullable GTRecipeDefinition createCustomRecipe(RecipeHandlerGroup holder) {
        var recipeHandlers = holder.getInputHandlerMap().get(ItemRecipeCapability.CAP);
        if (recipeHandlers == null) {
            return null;
        }
        for (var handler : recipeHandlers) {
            for (var content : handler.getContents()) {
                if (!(content instanceof ItemStack stack) || stack.isEmpty()) {
                    continue;
                }
                var recipe = search(stack);
                if (recipe != null) {
                    return recipe;
                }
            }
        }
        return null;
    }

    public @Nullable GTRecipeDefinition search(ItemStack stack) {
        DynamicHolder<LootRarity> rarityHolder = AffixHelper.getRarity(stack);
        if (!rarityHolder.isBound()) {
            return null;
        }
        LootRarity rarity = rarityHolder.get();

        if (stack.getItem() == Adventure.Items.GEM.get()) {
            return buildGemRecipe(stack, rarity);
        }

        if (!AffixHelper.getAffixes(stack).isEmpty()) {
            return buildAffixRecipe(stack, rarity);
        }

        return null;
    }

    private static @Nullable GTRecipeDefinition buildGemRecipe(ItemStack stack, LootRarity rarity) {
        int[] range = gemDustRange(rarity);
        if (range == null) {
            return null;
        }
        ItemStack input = stack.copyWithCount(1);
        ResourceLocation rarityId = RarityRegistry.INSTANCE.getKey(rarity);
        String path = rarityId != null ? rarityId.getPath() : ("ordinal_" + rarity.ordinal());

        return CMRecipeTypes.INDUSTRIAL_SALVAGING_RECIPES
                .recipeBuilder(CTNHMana.id("industrial_salvaging/gem/" + path))
                .inputItems(input)
                .outputItemsRanged(Adventure.Items.GEM_DUST.get(), UniformInt.of(range[0], range[1]))
                .EUt(EU_PER_TICK)
                .duration(DURATION)
                .buildRawRecipe();
    }

    private static GTRecipeDefinition buildAffixRecipe(ItemStack stack, LootRarity rarity) {
        ItemStack input = stack.copyWithCount(1);
        ResourceLocation rarityId = RarityRegistry.INSTANCE.getKey(rarity);
        String path = rarityId != null ? rarityId.getPath() : ("ordinal_" + rarity.ordinal());

        return CMRecipeTypes.INDUSTRIAL_SALVAGING_RECIPES
                .recipeBuilder(CTNHMana.id("industrial_salvaging/affix/" + path))
                .inputItems(input)
                .outputItemsRanged(rarity.getMaterial(), UniformInt.of(AFFIX_MIN, AFFIX_MAX))
                .EUt(EU_PER_TICK)
                .duration(DURATION)
                .buildRawRecipe();
    }

    /** Same ranges as {@code SalvagingRecipes#gemDust}. */
    private static @Nullable int[] gemDustRange(LootRarity rarity) {
        ResourceLocation id = RarityRegistry.INSTANCE.getKey(rarity);
        if (id != null) {
            return switch (id.getPath()) {
                case "common" -> new int[] { 1, 2 };
                case "uncommon" -> new int[] { 1, 3 };
                case "rare" -> new int[] { 2, 4 };
                case "epic" -> new int[] { 2, 5 };
                case "mythic" -> new int[] { 3, 6 };
                case "ancient" -> new int[] { 4, 10 };
                default -> null;
            };
        }
        int ordinal = rarity.ordinal();
        return new int[] { Math.max(1, 1 + ordinal / 2), Math.max(2, 2 + ordinal) };
    }

    @CN("%s宝石")
    @EN("%s Gem")
    public static Lang any_rarity_gem;

    @CN("任意%s词缀物品")
    @EN("Any %s Affixed Item")
    public static Lang any_rarity_affix;

    @CN("按物品稀有度自动生成拆解产出")
    @EN("Outputs scale automatically with the item's rarity")
    public static Lang by_rarity;

    @Override
    public void buildRepresentativeRecipes() {
        for (DynamicHolder<LootRarity> holder : RarityRegistry.INSTANCE.getOrderedRarities()) {
            if (!holder.isBound()) {
                continue;
            }
            LootRarity rarity = holder.get();
            ResourceLocation rarityId = holder.getId();
            String path = rarityId.getPath();
            var rarityName = rarity.toComponent();

            int[] gemRange = gemDustRange(rarity);
            if (gemRange != null) {
                ItemStack gem = new ItemStack(Adventure.Items.GEM.get());
                AffixHelper.setRarity(gem, rarity);
                gem.setHoverName(any_rarity_gem.translate(rarityName));
                var gemRecipe = CMRecipeTypes.INDUSTRIAL_SALVAGING_RECIPES
                        .recipeBuilder(CTNHMana.id("industrial_salvaging/gem/" + path))
                        .inputItems(gem)
                        .outputItemsRanged(Adventure.Items.GEM_DUST.get(),
                                UniformInt.of(gemRange[0], gemRange[1]))
                        .EUt(EU_PER_TICK)
                        .duration(DURATION)
                        .addData("info", true)
                        .buildRawRecipe();
                CMRecipeTypes.INDUSTRIAL_SALVAGING_RECIPES
                        .addToMainCategory(gemRecipe.withId(gemRecipe.getId().withPrefix("/")));
            }

            ItemStack affixItem = new ItemStack(Items.DIAMOND_SWORD);
            AffixHelper.setRarity(affixItem, rarity);
            affixItem.setHoverName(any_rarity_affix.translate(rarityName));
            var affixRecipe = CMRecipeTypes.INDUSTRIAL_SALVAGING_RECIPES
                    .recipeBuilder(CTNHMana.id("industrial_salvaging/affix/" + path))
                    .inputItems(affixItem)
                    .outputItemsRanged(rarity.getMaterial(), UniformInt.of(AFFIX_MIN, AFFIX_MAX))
                    .EUt(EU_PER_TICK)
                    .duration(DURATION)
                    .addData("info", true)
                    .buildRawRecipe();
            CMRecipeTypes.INDUSTRIAL_SALVAGING_RECIPES
                    .addToMainCategory(affixRecipe.withId(affixRecipe.getId().withPrefix("/")));
        }
    }
}
