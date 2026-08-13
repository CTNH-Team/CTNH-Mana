package com.magicbee.ctnhmana.api.recipe.customlogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;

import com.magicbee.ctnhmana.api.machine.gem.GemSublimatorRules;
import com.magicbee.ctnhmana.registry.CMRecipeTypes;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import org.jetbrains.annotations.Nullable;

/**
 * 宝石携刻机的 EMI / XEI 展示逻辑。
 * <p>
 * <b>重要：</b>真正加工由 {@code GemSublimatorMachine} 的自定义 tick 完成，
 * 不走 GT {@link #createCustomRecipe} 匹配。此处只在注册表加载后
 * 往 {@link CMRecipeTypes#GEM_SUBLIMATOR_RECIPES} 塞「代表性配方」供查表。
 * <p>
 * 展示用物品必须是 {@link GemRegistry#createGemStack} 生成的真实宝石，
 * 禁止只用稀有度 NBT、没有 Gem 绑定的空壳（会显示 Errored gem）。
 */
public class IndustrialGemSublimatorLogic implements GTRecipeType.ICustomRecipeLogic {

    /** 展示配方上的假耗电，仅影响 EMI 显示，不影响机器实际耗电 */
    private static final long EU_PER_TICK = GTValues.VA[GTValues.LV];
    /** 展示用假时长（秒表），机器实际按进度点运行 */
    private static final int DURATION = 20 * 10;

    /**
     * 运行时匹配：恒返回 null。
     * 携刻机不通过 RecipeLogic 匹配库存配方。
     */
    @Override
    public @Nullable GTRecipeDefinition createCustomRecipe(RecipeHandlerGroup holder) {
        return null;
    }

    /**
     * 在 Apoth 动态注册表就绪后构建 EMI 条目：
     * <ol>
     * <li>用一颗代表性宝石（优先 splendor）展示各稀有度升级（含粉耗）</li>
     * <li>跳过 mythic→ancient（首版机器拒绝，避免误导）</li>
     * <li>若干常见精致材料 → 有瑕疵产出示例</li>
     * </ol>
     */
    @Override
    public void buildRepresentativeRecipes() {
        if (GemRegistry.INSTANCE.getValues().isEmpty() || RarityRegistry.INSTANCE.getOrderedRarities().isEmpty()) {
            return;
        }

        // 选一颗「样本宝石」贯穿所有升级档展示；优先 splendor，否则取注册表第一颗
        Gem sample = null;
        for (Gem gem : GemRegistry.INSTANCE.getValues()) {
            if ("core/splendor".equals(gem.getId().getPath())) {
                sample = gem;
                break;
            }
            if (sample == null) {
                sample = gem;
            }
        }
        if (sample == null) {
            return;
        }

        LootRarity max = RarityRegistry.getMaxRarity().get();
        LootRarity rarity = RarityRegistry.getMinRarity().get();
        while (rarity != max) {
            LootRarity next = rarity.next();
            // 样本宝石必须同时支持当前档与下一档，否则跳过
            if (sample.clamp(rarity) == rarity && sample.clamp(next) == next) {
                String path = GemSublimatorRules.rarityPath(rarity);
                // mythic 升级目标为 ancient，首版占位不可用，不进 EMI
                if (!path.isEmpty() && !"mythic".equals(path)) {
                    ItemStack in = GemRegistry.createGemStack(sample, rarity);
                    ItemStack out = GemRegistry.createGemStack(sample, next);
                    int dust = GemSublimatorRules.dustCost(rarity);
                    var recipe = CMRecipeTypes.GEM_SUBLIMATOR_RECIPES
                            .recipeBuilder("xei/upgrade/" + path)
                            .inputItems(in)
                            .inputItems(new ItemStack(Adventure.Items.GEM_DUST.get(), dust))
                            .outputItems(out)
                            .EUt(EU_PER_TICK)
                            .duration(DURATION)
                            .buildRawRecipe();
                    // withPrefix("/") 与镶嵌机一致，避免与 datapack id 冲突
                    CMRecipeTypes.GEM_SUBLIMATOR_RECIPES
                            .addToMainCategory(recipe.withId(recipe.getId().withPrefix("/")));
                }
            }
            rarity = next;
        }

        // 精致 → 有瑕疵：挑几种常见颜色材料作 EMI 示例（完整映射见 GemSublimatorRules）
        addExquisiteExample("ruby", GTMaterials.Ruby);
        addExquisiteExample("sapphire", GTMaterials.Sapphire);
        addExquisiteExample("emerald", GTMaterials.Emerald);
        addExquisiteExample("diamond", GTMaterials.Diamond);
        addExquisiteExample("amethyst", GTMaterials.Amethyst);
        addExquisiteExample("netherite", GTMaterials.Netherite);
    }

    /**
     * 注册一条精致转化展示配方。
     * 材料没有精致形态或映射产物为空时静默跳过。
     */
    private static void addExquisiteExample(String name,
                                            com.gregtechceu.gtceu.api.data.chemical.material.Material material) {
        ItemStack in = ChemicalHelper.get(TagPrefix.gemExquisite, material);
        ItemStack out = GemSublimatorRules.createExquisiteResult(material);
        if (in.isEmpty() || out.isEmpty()) {
            return;
        }
        int dust = GemSublimatorRules.dustCostExquisite();
        var recipe = CMRecipeTypes.GEM_SUBLIMATOR_RECIPES
                .recipeBuilder("xei/exquisite/" + name)
                .inputItems(in)
                .inputItems(new ItemStack(Adventure.Items.GEM_DUST.get(), dust))
                .outputItems(out)
                .EUt(EU_PER_TICK)
                .duration(DURATION)
                .buildRawRecipe();
        CMRecipeTypes.GEM_SUBLIMATOR_RECIPES.addToMainCategory(recipe.withId(recipe.getId().withPrefix("/")));
    }
}
