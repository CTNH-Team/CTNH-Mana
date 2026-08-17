package com.magicbee.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import com.magicbee.ctnhmana.registry.CMItems;
import com.magicbee.ctnhmana.registry.CMMaterials;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.magicbee.ctnhmana.registry.CMBlocks.*;
import static com.magicbee.ctnhmana.registry.CMItems.ELF_CATALYST;
import static com.magicbee.ctnhmana.registry.CMMaterials.*;
import static com.magicbee.ctnhmana.registry.CMRecipeTypes.MANA_TRANSFORMER_RECIPES;
import static vazkii.botania.common.item.BotaniaItems.dragonstone;

public class ManaRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(// 产物：符文载体
                provider, "rune_carrier_block_recipe_one",
                RUNE_CARRIER_BLOCK.asStack(),
                "ABA",
                "CDC",
                "AEA",
                'A', GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.plateDouble, GTMaterials.Electrum).asStack(),
                'B', BotaniaItems.manaDiamond,
                'C', BotaniaItems.manaPearl,
                'D', BotaniaBlocks.livingrockPolished.asItem(),
                'E', GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.plate, ManaSteel).asStack());

        VanillaRecipeHelper.addShapedRecipe(// 产物：完美的符文石
                provider, "rune_stone_perfect_recipe_one",
                RUNE_STONE_PERFECT.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.rod, GTMaterials.Electrum).asStack(),
                'B', BotaniaItems.manaPearl,
                'D', BotaniaItems.manaDiamond,
                'C', GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.plateDouble, GTMaterials.Electrum).asStack());
        VanillaRecipeHelper.addShapedRecipe(// 产物：火之符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：水之符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：地之符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：风之符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：春之符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：夏之符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：秋之符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：冬之符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：魔力符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：欲望符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：暴食符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：贪婪符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：懒惰符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：暴怒符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：嫉妒符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：傲慢符文石
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
        VanillaRecipeHelper.addShapedRecipe(// 产物：魔力丝绸方块
                provider, "fabric_one",
                FABRIC.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', BotaniaItems.manaString,
                'B', BotaniaItems.manaweaveCloth,
                'C', BotaniaItems.spellCloth);

        // 符文载体方块配方
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("rune_carrier_block_recipe_two")// 符文载体
                .circuitMeta(20)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Electrum, 4)
                .inputItems(BotaniaItems.manaDiamond)
                .inputItems(BotaniaItems.manaPearl, 2)
                .inputItems(BotaniaBlocks.livingrockPolished.asItem(), 1)
                .inputItems(TagPrefix.plate, ManaSteel, 1)
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
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("fabric_two")// 魔力丝绸方块
                .circuitMeta(20)
                .inputItems(BotaniaItems.manaString, 4)
                .inputItems(BotaniaItems.manaweaveCloth, 2)
                .inputItems(BotaniaItems.spellCloth)
                .outputItems(FABRIC.asStack())
                .EUt(GTValues.VA[GTValues.ULV])
                .duration(20)
                .save(provider);
        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder("broken_rune")
                .inputItems(BotaniaItems.runeMana, 32)
                .inputFluids(CMMaterials.Mana.getFluid(10000))
                .chancedOutput(CMItems.BROKEN_RUNE.asStack(), 2000, 1000)
                .EUt(GTValues.VA[GTValues.EV])
                .duration(100 * 20)
                .save(provider);
        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder("zenith_shroud")
                .inputItems(ChemicalHelper.get(TagPrefix.gem, CMMaterials.Psionic_Medulla))
                .inputFluids(CMMaterials.Zenith_essence.getFluid(1000))
                .outputFluids(CMMaterials.Shroud_Zenith_essence.getFluid(800))
                .EUt(GTValues.VA[GTValues.IV])
                .duration(50 * 20)
                .save(provider);
        VanillaRecipeHelper.addShapedRecipe(// 魔力池
                provider, "mana_pool",
                new ItemStack(BotaniaBlocks.manaPool.asItem()),
                "AAA",
                "ABA",
                "AAA",
                'A', BotaniaBlocks.livingrock,
                'B', BotaniaItems.manaTablet);
        VanillaRecipeHelper.addShapedRecipe(
                provider, "advanced_stone",
                new ItemStack(Blocks.REINFORCED_DEEPSLATE, 8),
                "AAA",
                "ABA",
                "AAA",
                'A', Blocks.DEEPSLATE,
                'B', Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        VanillaRecipeHelper.addShapedRecipe(
                provider, "path", new ItemStack(BloodMagicBlocks.OBSIDIAN_PATH.get(), 32),
                "AAA",
                "ABA",
                "AAA",
                'A', ChemicalHelper.get(TagPrefix.plateDense, GTMaterials.Obsidian),
                'B', ChemicalHelper.get(TagPrefix.gear, CMMaterials.COAGULBLOODGOLD));
        GTRecipeTypes.LARGE_CHEMICAL_RECIPES.recipeBuilder("elf")
                .inputFluids(GTMaterials.Helium, 1000)
                .inputFluids(GTMaterials.Neon, 1000)
                .inputFluids(GTMaterials.CarbonDioxide, 2000)
                .inputFluids(CMMaterials.Mana, 1000)
                .outputFluids(CMMaterials.ELF_FUEL.getFluid(2000))
                .EUt(120)
                .duration(1000)
                .save(provider);
        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder("empty_rune")
                .inputItems(BotaniaBlocks.livingrock.asItem(), 16)
                .inputFluids(CMMaterials.Mana, 100)
                .outputItems(CMItems.EMPTY_RUNE, 4)
                .duration(200)
                .EUt(8)
                .save(provider);
        VanillaRecipeHelper.addShapedRecipe(
                provider, "caduceus", new ItemStack(CMItems.CADUCEUS),
                "ACA",
                "ABA",
                "ACA",
                'A', CMItems.INDEX_CLOTH,
                'C', GTItems.FIELD_GENERATOR_IV,
                'B', new ItemStack(GTMultiMachines.ACTIVE_TRANSFORMER.getItem()));
        VanillaRecipeHelper.addShapedRecipe(
                provider, "index_cloth", new ItemStack(CMItems.INDEX_CLOTH),
                "AAA",
                "ABA",
                "ACA",
                'A', BotaniaItems.manaString,
                'C', GTItems.FIELD_GENERATOR_IV,
                'B', BotaniaItems.spellCloth);
        MANA_TRANSFORMER_RECIPES.recipeBuilder("fused_mixed_mana")// 分选蕴魔粉
                .inputItems(ChemicalHelper.get(dust, Fused_Mana), 16)
                .notConsumable(ELF_CATALYST)
                .outputItems(ChemicalHelper.get(dust, Fused_Mixed_Mana), 16)
                .outputItems(ChemicalHelper.get(dust, ManaSteel), 4)
                .outputItems(ChemicalHelper.get(dust, Elementium), 4)
                .outputItems(dragonstone)
                .outputFluids(Mana.getFluid(100))
                .EUt(320)
                .save(provider);
    }
}
