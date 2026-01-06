package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMBlocks.*;
import static com.moguang.ctnhmana.registry.CMMaterials.ManaSteel;

public class ManaRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(
                provider, "rune_carrier_block_recipe_one",
                RUNE_CARRIER_BLOCK.asStack(),
                        "ABA",
                                "CDC",
                                "AEA",
                'A', GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.plateDouble, GTMaterials.Electrum).asStack(),
                'B', BotaniaItems.manaDiamond,
                'C', BotaniaItems.manaPearl,
                'D', BotaniaBlocks.livingrockPolished.asItem(),
                'E', GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.plate, ManaSteel).asStack()
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "rune_stone_perfect_recipe_one",
                RUNE_STONE_PERFECT.asStack(),
                        "ABA",
                                "CDC",
                                "ABA",
                'A', GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.rod, GTMaterials.Electrum).asStack(),
                'B', BotaniaItems.manaPearl,
                'D', BotaniaItems.manaDiamond,
                'C', GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.plateDouble, GTMaterials.Electrum).asStack()
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "fire_rune_stone_one",
                FIRE_RUNE_STONE.asStack(),
                        "ABA",
                                "ACA",
                                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeFire    // 火符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "water_rune_stone_one",
                WATER_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeWater  // 水符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "earth_rune_stone_one",
                EARTH_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeEarth  // 土符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "wind_rune_stone_one",
                WIND_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeAir  // 风符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "spring_rune_stone_one",
                SPRING_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeSpring  // 春符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "summer_rune_stone_one",
                SUMMER_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeSummer  // 夏符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "autumn_rune_stone_one",
                AUTUMN_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeAutumn  // 秋符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "winter_rune_stone_one",
                WINTER_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeWinter  // 冬符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "mana_rune_stone_one",
                MANA_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeMana  // 魔力符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "lust_rune_stone_one",
                SIN_LUST_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeLust  // 欲望符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "gluttony_rune_stone_one",
                SIN_GLUTTONY_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeGluttony  // 暴食符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "greed_rune_stone_one",
                SIN_GREED_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeGreed  // 贪婪符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "sloth_rune_stone_one",
                SIN_SLOTH_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeSloth  // 懒惰符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "wrath_rune_stone_one",
                SIN_WRATH_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeWrath  // 暴怒符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "envy_rune_stone_one",
                SIN_ENVY_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runeEnvy  // 嫉妒符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "pride_rune_stone_one",
                SIN_PRIDE_RUNE_STONE.asStack(),
                "ABA",
                "ACA",
                "ADA",
                'A', BotaniaItems.manaPowder,
                'B', BotaniaItems.lensNormal,
                'D', RUNE_CARRIER_BLOCK.asStack(),
                'C', BotaniaItems.runePride  // 傲慢符文
        );
        VanillaRecipeHelper.addShapedRecipe(
                provider, "fabric_one",
                FABRIC.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', BotaniaItems.manaString,
                'B', BotaniaItems.manaweaveCloth,
                'C', BotaniaItems.spellCloth
        );

// 符文载体方块配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("rune_carrier_block_recipe_two")//符文载体
                .circuitMeta(20)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Electrum,4)
                .inputItems(BotaniaItems.manaDiamond)
                .inputItems(BotaniaItems.manaPearl,2)
                .inputItems(BotaniaBlocks.livingrockPolished.asItem(),1)
                .inputItems(TagPrefix.plate, ManaSteel,1)
                .outputItems(RUNE_CARRIER_BLOCK.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);
// 完美符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("rune_stone_perfect_recipe_two")
                .circuitMeta(20)
                .inputItems(TagPrefix.rod, GTMaterials.Electrum, 4)
                .inputItems(BotaniaItems.manaPearl, 2)
                .inputItems(BotaniaItems.manaDiamond)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Electrum)
                .outputItems(RUNE_STONE_PERFECT.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 火符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("fire_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeFire)
                .outputItems(FIRE_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 水符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("water_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeWater)
                .outputItems(WATER_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 土符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("earth_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeEarth)
                .outputItems(EARTH_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 风符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("wind_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeAir)
                .outputItems(WIND_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 春符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("spring_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeSpring)
                .outputItems(SPRING_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 夏符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("summer_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeSummer)
                .outputItems(SUMMER_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 秋符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("autumn_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeAutumn)
                .outputItems(AUTUMN_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 冬符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("winter_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeWinter)
                .outputItems(WINTER_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 魔力符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeMana)
                .outputItems(MANA_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 欲望符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("lust_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeLust)
                .outputItems(SIN_LUST_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 暴食符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("gluttony_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeGluttony)
                .outputItems(SIN_GLUTTONY_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 贪婪符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("greed_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeGreed)
                .outputItems(SIN_GREED_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 懒惰符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("sloth_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeSloth)
                .outputItems(SIN_SLOTH_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 暴怒符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("wrath_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeWrath)
                .outputItems(SIN_WRATH_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 嫉妒符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("envy_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runeEnvy)
                .outputItems(SIN_ENVY_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

// 傲慢符文石配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("pride_rune_stone_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaPowder, 6)
                .inputItems(BotaniaItems.lensNormal)
                .inputItems(RUNE_CARRIER_BLOCK.asStack())
                .inputItems(BotaniaItems.runePride)
                .outputItems(SIN_PRIDE_RUNE_STONE.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);
// 魔力丝绸方块配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("fabric_two")
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaString, 4)
                .inputItems(BotaniaItems.manaweaveCloth, 2)
                .inputItems(BotaniaItems.spellCloth)
                .outputItems(FABRIC.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);

    }
}
