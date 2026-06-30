package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.METEOR_RITUAL_GUIDE;

/**
 * 坠星位标 EMI 参考配方（{@link com.moguang.ctnhmana.registry.CMRecipeTypes#METEOR_RITUAL_GUIDE}）。
 * <p>
 * 仅用于 JEI/EMI 展示，工业血祭仪式阵不会处理此类型。产量为按血魔法原版配方的估算值。
 * 原版已有的矿物/物品用 {@link Items}；仅 GT 独占矿物用 {@link ChemicalHelper}。
 */
public class MeteorRitualGuideRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // bloodmagic:meteor/iron — syphon 1_000_000
        METEOR_RITUAL_GUIDE.recipeBuilder("iron")
                .inputItems(Items.IRON_BLOCK, 1)
                .outputItems(Items.IRON_ORE, 400)
                .outputItems(Items.COPPER_ORE, 150)
                .outputItems(ChemicalHelper.get(ore, Tin), 100)
                .outputItems(Items.REDSTONE_ORE, 80)
                .outputItems(ChemicalHelper.get(ore, Lead), 60)
                .outputItems(Items.LAPIS_ORE, 50)
                .outputItems(ChemicalHelper.get(ore, Silver), 50)
                .outputItems(Items.GOLD_ORE, 30)
                .addData("meteor_lp", 1_000_000)
                .hideDuration(true)
                .duration(400)
                .EUt(1)
                .save(provider);

        // bloodmagic:meteor/diamond — syphon 1_000_000
        METEOR_RITUAL_GUIDE.recipeBuilder("diamond")
                .inputItems(Items.DIAMOND, 1)
                .outputItems(Items.DIAMOND_ORE, 64)
                .outputItems(ChemicalHelper.get(ore, Ruby), 80)
                .outputItems(ChemicalHelper.get(ore, Sapphire), 80)
                .outputItems(Items.EMERALD_ORE, 60)
                .outputItems(ChemicalHelper.get(ore, Cinnabar), 120)
                .addData("meteor_lp", 1_000_000)
                .hideDuration(true)
                .duration(400)
                .EUt(1)
                .save(provider);

        // bloodmagic:meteor/stone — syphon 1_000_000
        METEOR_RITUAL_GUIDE.recipeBuilder("stone")
                .inputItems(Items.STONE, 1)
                .outputItems(Items.COAL_ORE, 200)
                .outputItems(ChemicalHelper.get(ore, Apatite), 80)
                .outputItems(Items.IRON_ORE, 60)
                .addData("meteor_lp", 1_000_000)
                .hideDuration(true)
                .duration(400)
                .EUt(1)
                .save(provider);

        // bloodmagic:meteor/nether — syphon 1_000_000
        METEOR_RITUAL_GUIDE.recipeBuilder("nether")
                .inputItems(Items.GLOWSTONE_DUST, 1)
                .outputItems(Items.GLOWSTONE, 120)
                .outputItems(Items.NETHER_QUARTZ_ORE, 180)
                .outputItems(Items.NETHER_GOLD_ORE, 70)
                .outputItems(Items.ANCIENT_DEBRIS, 40)
                .addData("meteor_lp", 1_000_000)
                .hideDuration(true)
                .duration(400)
                .EUt(1)
                .save(provider);

        // bloodmagic:meteor/ice_fire — syphon 250_000
        METEOR_RITUAL_GUIDE.recipeBuilder("ice_fire")
                .inputItems(Items.BONE, 1)
                .outputItems(Items.COPPER_ORE, 120)
                .outputItems(ChemicalHelper.get(ore, Silver), 120)
                .addData("meteor_lp", 250_000)
                .hideDuration(true)
                .duration(400)
                .EUt(1)
                .save(provider);

        // bloodmagic:meteor/mekanism — syphon 500_000，标定物 forge:alloys/advanced
        METEOR_RITUAL_GUIDE.recipeBuilder("mekanism")
                .inputItems(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("forge", "alloys/advanced")), 1)
                .outputItems(ChemicalHelper.get(ore, Osmium), 100)
                .outputItems(Items.COPPER_ORE, 100)
                .outputItems(ChemicalHelper.get(ore, Tin), 80)
                .outputItems(ChemicalHelper.get(ore, Lead), 60)
                .outputItems(ChemicalHelper.get(ore, Uranium238), 80)
                .outputItems(Items.GLOWSTONE, 50)
                .addData("meteor_lp", 500_000)
                .hideDuration(true)
                .duration(400)
                .EUt(1)
                .save(provider);
    }
}
