package com.moguang.ctnhmana.api.recipe.customlogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.world.item.ItemStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.api.machine.gem.GemSublimatorRules;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import com.moguang.ctnhmana.registry.CMTags;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

/**
 * 宝石携刻「展示」EMI 配方类型逻辑。
 * <p>
 * 输入使用 {@link CMTags#APOTHEOSIS_GEMS}（Tag），因此玩家对<strong>任意</strong>神话宝石
 * 在 EMI 中查询「用途」时，都能看到「Tag + 宝石粉 → XX品质的宝石」。
 * <p>
 * 不参与机器运行时匹配；真实加工仍由 {@code GemSublimatorMachine} 完成。
 */
public class IndustrialGemSublimatorGenericLogic implements GTRecipeType.ICustomRecipeLogic {

    private static final long EU_PER_TICK = GTValues.VA[GTValues.LV];
    private static final int DURATION = 20 * 10;

    @CN("%s品质的宝石")
    @EN("%s Quality Gem")
    public static Lang qualityGem;

    @Override
    public @Nullable GTRecipeDefinition createCustomRecipe(RecipeHandlerGroup holder) {
        return null;
    }

    /**
     * 为每个可升级的「当前稀有度 → 下一稀有度」注册一条：
     * {@code #forge:apotheosis_gems + N 宝石粉 → 「XX品质的宝石」}。
     * <p>
     * 跳过 mythic→ancient。产出仍用真实 {@link GemRegistry#createGemStack} 绑定稀有度 NBT，
     * 再用 hover 名改成通用「XX品质的宝石」，避免 EMI 只显示某一具体宝石种类。
     */
    @Override
    public void buildRepresentativeRecipes() {
        if (GemRegistry.INSTANCE.getValues().isEmpty() || RarityRegistry.INSTANCE.getOrderedRarities().isEmpty()) {
            return;
        }

        Gem sample = pickSampleGem();
        if (sample == null) {
            return;
        }

        LootRarity max = RarityRegistry.getMaxRarity().get();
        LootRarity rarity = RarityRegistry.getMinRarity().get();
        while (rarity != max) {
            LootRarity next = rarity.next();
            String fromPath = GemSublimatorRules.rarityPath(rarity);
            String toPath = GemSublimatorRules.rarityPath(next);
            if (!fromPath.isEmpty() && !"mythic".equals(fromPath) &&
                    sample.clamp(next) == next) {
                ItemStack out = GemRegistry.createGemStack(sample, next);
                // 展示名：有瑕疵品质的宝石 / Rare Quality Gem（稀有度名取自 Apoth 组件）
                out.setHoverName(qualityGem.translate(next.toComponent()));
                int dust = GemSublimatorRules.dustCost(rarity);
                var recipe = CMRecipeTypes.GEM_SUBLIMATOR_GENERIC_RECIPES
                        .recipeBuilder("xei/tag_upgrade/" + fromPath + "_to_" + toPath)
                        .inputItems(CMTags.APOTHEOSIS_GEMS, 1)
                        .inputItems(new ItemStack(Adventure.Items.GEM_DUST.get(), dust))
                        .outputItems(out)
                        .EUt(EU_PER_TICK)
                        .duration(DURATION)
                        .buildRawRecipe();
                CMRecipeTypes.GEM_SUBLIMATOR_GENERIC_RECIPES
                        .addToMainCategory(recipe.withId(recipe.getId().withPrefix("/")));
            }
            rarity = next;
        }
    }

    @Nullable
    private static Gem pickSampleGem() {
        Gem fallback = null;
        for (Gem gem : GemRegistry.INSTANCE.getValues()) {
            if ("core/splendor".equals(gem.getId().getPath())) {
                return gem;
            }
            if (fallback == null) {
                fallback = gem;
            }
        }
        return fallback;
    }
}
