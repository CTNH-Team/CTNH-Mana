package com.magicbee.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.magicbee.ctnhmana.api.recipe.condition.HellForgeCondition;
import com.magicbee.ctnhmana.data.recipe.builder.bloodmagic.BloodAltarRecipeBuilder;
import com.magicbee.ctnhmana.data.recipe.builder.botania.ManaInfusionRecipeBuilder;
import com.magicbee.ctnhmana.data.recipe.builder.botania.TerraPlateRecipeBuilder;
import com.magicbee.ctnhmana.registry.CMBlocks;
import com.magicbee.ctnhmana.registry.CMItems;
import com.magicbee.ctnhmana.registry.CMMaterials;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.register.ModItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.core.recipe.IngredientBloodOrb;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.magicbee.ctnhmana.registry.CMBlocks.*;
import static com.magicbee.ctnhmana.registry.CMMaterials.*;
import static com.magicbee.ctnhmana.registry.CMRecipeTypes.HELL_FORGE_RECIPES;
import static com.magicbee.ctnhmana.registry.CMRecipeTypes.MANA_REACTOR_RECIPES;

public class ManaMachineBlockRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("pure_block") // 纯净机械方块
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(BotaniaBlocks.livingrock.asItem(), 2)
                .inputItems(ChemicalHelper.get(TagPrefix.gearSmall, CMMaterials.ManaSteel), 4)
                .circuitMeta(1)
                .EUt(32)
                .duration(120)
                .outputItems(CMBlocks.LIVING_ROCK_CASING.asItem(), 2) // 纯净机械方块
                .save(provider);
        ManaInfusionRecipeBuilder.builder("advanced_glass")    // 强化魔力玻璃
                .input(GTBlocks.CASING_TEMPERED_GLASS.asStack())
                .output(CMBlocks.ENHANCED_MANA_GLASS.asStack()) // 强化魔力玻璃
                .circuitMeta(1)
                .mana(15000)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("unwither_casing")  // 不凋花园方块
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(GTBlocks.CASING_STAINLESS_CLEAN.asStack(), 2)
                .inputItems(CMBlocks.ENHANCED_MANA_GLASS.asStack(), 2)
                .inputItems(ChemicalHelper.get(plate, CMMaterials.Photonium), 2)
                .inputFluids(CMMaterials.MANA_STABLE_COOLDOWN.getFluid(100))
                .outputItems(CMBlocks.UNFADING_GARDEN_CASING.asItem(), 4) // 不凋花园方块
                .circuitMeta(7)
                .EUt(128)
                .duration(300)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("pipe_element")    // 源质钢管道机械方块
                .inputItems(ChemicalHelper.get(TagPrefix.rod, CMMaterials.Elementium), 4)
                .inputItems(ChemicalHelper.get(TagPrefix.gearSmall, CMMaterials.Elementium), 4)
                .inputItems(ELEMENTIUM_FRAME.asStack())
                .circuitMeta(5)
                .EUt(32)
                .duration(400)
                .outputItems(CMBlocks.ELEMENTIUM_PIPE_CASING.asItem(), 2) // 源质钢管道机械方块
                .save(provider);
        VanillaRecipeHelper.addShapedRecipe( // 源质钢管道机械方块
                provider, "pip_element_one",
                CMBlocks.ELEMENTIUM_PIPE_CASING.asStack(2), // 源质钢管道机械方块
                "ABA",
                "BCB",
                "ABA",
                'A', ChemicalHelper.get(TagPrefix.rod, CMMaterials.Elementium),
                'B', (ChemicalHelper.get(TagPrefix.gearSmall, CMMaterials.Elementium)),
                'C', ELEMENTIUM_FRAME.asStack());

        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("elementium_gear_box") // 源质钢齿轮箱机械方块
                .inputItems(ChemicalHelper.get(plate, CMMaterials.Elementium), 4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.Elementium), 2)
                .inputItems(ELEMENTIUM_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.ELEMENTAL_CASING_GEARBOX.asItem(), 2) // 源质钢齿轮箱机械方块
                .save(provider);
        ASSEMBLER_RECIPES.recipeBuilder("manasteel_gearbox_casing")// 魔力钢齿轮箱机壳
                .inputItems(plate, ManaSteel, 4)
                .inputItems(gear, ManaSteel, 2)
                .inputItems(CMBlocks.MANA_STEEL_FRAME.asStack())
                .circuitMeta(4)
                .outputItems(CASING_MANASTEEL_GEARBOX.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16).save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("manasteel_gear_box") // 魔力钢齿轮箱方块
                .inputItems(ChemicalHelper.get(plate, CMMaterials.ManaSteel), 4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.ManaSteel), 2)
                .inputItems(CMBlocks.MANA_STEEL_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.CASING_MANASTEEL_GEARBOX.asItem(), 2) // 魔力钢齿轮箱方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("orichalcos_gear_box") // 奥利哈钢齿轮箱机械方块
                .inputItems(ChemicalHelper.get(plate, CMMaterials.Orichalcos), 4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.Orichalcos), 2)
                .inputItems(CMBlocks.ORICHALCOS_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.ORICHALCOS_STEEL_CASING_GEARBOX.asItem(), 2) // 奥利哈钢齿轮箱机械方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("elf_steel_gear_box") // 精灵钢齿轮箱机械方块
                .inputItems(ChemicalHelper.get(plate, CMMaterials.AlfSteel), 4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.AlfSteel), 2)
                .inputItems(CMBlocks.ALFSTEEL_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.ELF_STEEL_CASING_GEARBOX.asItem(), 2) // 精灵钢齿轮箱机械方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("force_fileding_block") // 力场领域机械方块
                .inputItems(GTItems.FIELD_GENERATOR_EV)
                .inputItems(CMBlocks.CASING_BLOOD.asStack(2))
                .inputItems(CMItems.BLOODY_CHIP, 4)
                .inputItems(CMItems.BLOOD_CAPACITOR, 4)
                .EUt(GTValues.VA[GTValues.EV])
                .duration(200)
                .circuitMeta(7)
                .outputItems(CMBlocks.CASING_FORCE_FILED.asStack(2)) // 力场领域机械方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("pure_logic_casing") // 纯净魔力逻辑传输方块
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(GTItems.FIELD_GENERATOR_LV)
                .inputItems(LIVING_ROCK_CASING.asStack(), 4)
                .inputItems(BotaniaItems.runeMana.asItem(), 1)
                .outputItems(CMBlocks.PURE_LOGIC_CASING.asStack(4)) // 纯净魔力逻辑传输方块
                .duration(200)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_shatter_core") // 魔力粉碎核心
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(GTItems.COMPONENT_GRINDER_DIAMOND)
                .inputItems(ChemicalHelper.get(TagPrefix.cableGtSingle, CMMaterials.ManaSteel), 2)
                .inputItems(BotaniaItems.runeSummer.asItem(), 1)
                .inputItems(BotaniaItems.runeMana.asItem(), 1)
                .inputItems(MANA_STEEL_CASING.asStack())
                .outputItems(CMBlocks.MANA_SHATTER_CORE.asItem()) // 魔力粉碎核心
                .duration(200)
                .circuitMeta(2)
                .EUt(32)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_forge_core") // 魔力锻造核心
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(Blocks.ANVIL.asItem())
                .inputItems(ChemicalHelper.get(TagPrefix.cableGtSingle, CMMaterials.ManaSteel), 2)
                .inputItems(BotaniaItems.runeAutumn.asItem(), 1)
                .inputItems(BotaniaItems.runeMana.asItem(), 1)
                .inputItems(MANA_STEEL_CASING.asStack())
                .outputItems(CMBlocks.MANA_FORGE_CORE.asItem()) // 魔力锻造核心
                .duration(200)
                .circuitMeta(2)
                .EUt(32)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_refinment_core") // 魔力精炼核心
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(BotaniaItems.manaMirror.asItem())
                .inputItems(ChemicalHelper.get(TagPrefix.cableGtSingle, CMMaterials.ManaSteel), 2)
                .inputItems(BotaniaItems.runeSpring.asItem(), 1)
                .inputItems(BotaniaItems.runeMana.asItem(), 1)
                .inputItems(MANA_STEEL_CASING.asStack())
                .outputItems(CMBlocks.MANA_REFINEMENT_CORE.asItem())
                .duration(200)
                .circuitMeta(2)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("zenith_gearbox") // 天顶齿轮箱
                .inputItems(CASING_MANASTEEL_GEARBOX.asStack())
                .inputItems(ChemicalHelper.get(TagPrefix.gem, CMMaterials.Psionic_Medulla))
                .inputFluids(CMMaterials.Zenith_essence.getFluid(1000))
                .outputItems(ZENITH_CASING_GEARBOX.asItem())
                .duration(1000)
                .circuitMeta(1)
                .EUt(GTValues.VA[GTValues.IV])
                .save(provider);
        TerraPlateRecipeBuilder.builder("pure_logic_core") // 纯净魔力核心
                .input(CustomTags.HV_CIRCUITS)
                .input(BotaniaItems.runeMana)
                .input(PURE_LOGIC_CASING.asStack())
                .input(AEItems.CALCULATION_PROCESSOR.stack())
                .input(CMItems.MANA_WAFER.asStack())
                .output(PURE_MAGIC_CALCULATE_CORE.asStack())
                .mana(500000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("bloodlogic_casing") // 血逻辑方块
                .inputItems(PURE_LOGIC_CASING.asStack(6))
                .inputItems(GTItems.FIELD_GENERATOR_HV)
                .inputItems(CustomTags.EV_CIRCUITS)
                .inputItems(CMItems.BLOOD_RESISTOR, 66)
                .inputItems(BotaniaItems.runeWrath.asItem(), 3)
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.COAGULBLOODGOLD), 3)
                .outputItems(CASING_BLOODLOGIC.asItem(), 6)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 6666))
                .duration(666 * 2)
                .EUt(GTValues.VA[GTValues.HV])
                .save(provider);
        TerraPlateRecipeBuilder.builder("mana_steel_casing1") // 魔力钢机械方块
                .input(ChemicalHelper.get(plate, ManaSteel))
                .input(ChemicalHelper.get(plate, ManaSteel))
                .input(ChemicalHelper.get(plate, ManaSteel))
                .input(ChemicalHelper.get(plate, ManaSteel))
                .input(ChemicalHelper.get(plate, ManaSteel))
                .input(ChemicalHelper.get(plate, ManaSteel))
                .input(MANA_STEEL_FRAME.asStack())
                .circuitMeta(21)
                .output(MANA_STEEL_CASING.asStack())
                .mana(500000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_steel_casing2") // 魔力钢机械方块
                .inputItems(ChemicalHelper.get(plate, ManaSteel), 6)
                .inputItems(MANA_STEEL_FRAME.asStack())
                .inputFluids(Mana.getFluid(1000))
                .circuitMeta(6)
                .outputItems(MANA_STEEL_CASING.asItem(), 1)
                .EUt(512)
                .save(provider);
        TerraPlateRecipeBuilder.builder("mana_steel_frame1") // 魔力钢框架
                .input(ChemicalHelper.get(rod, ManaSteel))
                .input(ChemicalHelper.get(rod, ManaSteel))
                .input(ChemicalHelper.get(rod, ManaSteel))
                .input(ChemicalHelper.get(rod, ManaSteel))
                .output(MANA_STEEL_FRAME.asStack())
                .mana(5000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("elementium_casing1") // 源质钢机械方块
                .input(ChemicalHelper.get(plate, Elementium))
                .input(ChemicalHelper.get(plate, Elementium))
                .input(ChemicalHelper.get(plate, Elementium))
                .input(ChemicalHelper.get(plate, Elementium))
                .input(ChemicalHelper.get(plate, Elementium))
                .input(ChemicalHelper.get(plate, Elementium))
                .input(ELEMENTIUM_FRAME.asStack())
                .output(ELEMENTIUM_CASING.asStack())
                .mana(70000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("elementium_frame1") // 源质钢框架
                .input(ChemicalHelper.get(rod, Elementium))
                .input(ChemicalHelper.get(rod, Elementium))
                .input(ChemicalHelper.get(rod, Elementium))
                .input(ChemicalHelper.get(rod, Elementium))
                .output(ELEMENTIUM_FRAME.asStack())
                .mana(6000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("orichalcos_frame1") // 奥利哈框架
                .input(ChemicalHelper.get(rod, Orichalcos))
                .input(ChemicalHelper.get(rod, Orichalcos))
                .input(ChemicalHelper.get(rod, Orichalcos))
                .input(ChemicalHelper.get(rod, Orichalcos))
                .output(ORICHALCOS_FRAME.asStack())
                .mana(12000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("alfsteel_casing1") // 精灵钢机械方块
                .input(ChemicalHelper.get(plate, AlfSteel))
                .input(ChemicalHelper.get(plate, AlfSteel))
                .input(ChemicalHelper.get(plate, AlfSteel))
                .input(ChemicalHelper.get(plate, AlfSteel))
                .input(ChemicalHelper.get(plate, AlfSteel))
                .input(ChemicalHelper.get(plate, AlfSteel))
                .input(ALFSTEEL_FRAME.asStack())
                .output(ALF_STEEL_CASING.asStack())
                .mana(100000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("alfsteel_casing2") // 精灵钢机械方块
                .inputItems(ChemicalHelper.get(plate, AlfSteel), 6)
                .inputItems(ALFSTEEL_FRAME.asStack())
                .inputFluids(Mana.getFluid(5000))
                .circuitMeta(6)
                .outputItems(ALF_STEEL_CASING.asItem(), 1)
                .EUt(1920)
                .save(provider);
        TerraPlateRecipeBuilder.builder("alfsteel_frame1") // 精灵钢框架
                .input(ChemicalHelper.get(rod, AlfSteel))
                .input(ChemicalHelper.get(rod, AlfSteel))
                .input(ChemicalHelper.get(rod, AlfSteel))
                .input(ChemicalHelper.get(rod, AlfSteel))
                .output(ALFSTEEL_FRAME.asStack())
                .mana(5000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("terra_steel_casing1") // 泰拉钢机械方块
                .input(ChemicalHelper.get(plate, TerraSteel))
                .input(ChemicalHelper.get(plate, TerraSteel))
                .input(ChemicalHelper.get(plate, TerraSteel))
                .input(ChemicalHelper.get(plate, TerraSteel))
                .input(ChemicalHelper.get(plate, TerraSteel))
                .input(ChemicalHelper.get(plate, TerraSteel))
                .input(TERRA_STEEL_FRAME.asStack())
                .output(TERRA_STEEL_CASING.asStack())
                .mana(75000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("terra_steel_casing2") // 泰拉钢机械方块
                .inputItems(ChemicalHelper.get(plate, TerraSteel), 6)
                .inputItems(TERRA_STEEL_FRAME.asStack())
                .inputFluids(Mana.getFluid(3000))
                .circuitMeta(6)
                .outputItems(TERRA_STEEL_CASING.asItem(), 1)
                .EUt(1920)
                .save(provider);
        TerraPlateRecipeBuilder.builder("terra_steel_frame1") // 泰拉钢框架
                .input(ChemicalHelper.get(rod, TerraSteel))
                .input(ChemicalHelper.get(rod, TerraSteel))
                .input(ChemicalHelper.get(rod, TerraSteel))
                .input(ChemicalHelper.get(rod, TerraSteel))
                .output(TERRA_STEEL_FRAME.asStack())
                .mana(500)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("terra_steel_frame2") // 泰拉钢框架
                .inputItems(ChemicalHelper.get(rod, TerraSteel), 4)
                .inputFluids(Mana.getFluid(500))
                .circuitMeta(4)
                .outputItems(TERRA_STEEL_FRAME.asStack(), 1)
                .EUt(1920)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("zenith_casing_block") // 天顶强化机械方块
                .inputItems(ChemicalHelper.get(plate, Plus_Mana), 2)
                .inputItems(ChemicalHelper.get(frameGt, Plus_Mana), 1)
                .inputItems(CASING_BLOOD.asItem(), 2)
                .inputFluids(Zenith_essence.getFluid(200))
                .circuitMeta(6)
                .outputItems(ZENITH_CASING_BLOCK.asItem(), 1)
                .EUt(1920)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("arcane_glass_a") // 奥能屏蔽覆层玻璃
                .inputItems(ENHANCED_MANA_GLASS.asStack(8))
                .inputItems(ModItems.niflheimRune, 8)
                .inputItems(GTBlocks.CASING_LAMINATED_GLASS.asStack(8))
                .inputItems(ORICHALCOS_FRAME.asStack())
                .outputItems(ARCANE_CONSTRAINT_COATED_GLASS.asItem(), 16)
                .EUt(1920)
                .duration(500)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("arcane_glass_b") // 奥能约束覆层玻璃
                .inputItems(ARCANE_CONSTRAINT_COATED_GLASS.asStack())
                .inputItems(ChemicalHelper.get(plateDense, GTMaterials.Lead), 2)
                .inputItems(ChemicalHelper.get(plate, GTMaterials.RhodiumPlatedPalladium), 2)
                .inputItems(ELEMENTAL_RADIATION_SUPPRESSION_BLOCK.asStack())
                .outputItems(ARCANE_SHIELDING_COATED_GLASS.asItem())
                .EUt(1920)
                .duration(2000)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("elemental_radio") // 元素辐射抑制方块
                .inputItems(ChemicalHelper.get(block, GTMaterials.Lead), 1)
                .inputItems(ELF_STEEL_CASING_GEARBOX.asStack())
                .inputItems(ChemicalHelper.get(plateDouble, SHADOWIUM), 2)
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(1000))
                .outputItems(ELEMENTAL_RADIATION_SUPPRESSION_BLOCK.asStack(), 2)
                .EUt(1920)
                .duration(100)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("super_logic_core") // 超因果奥术运算核心
                .inputItems(CMItems.ENDSLATE.get())
                .inputItems(ZENITH_CASING_BLOCK.asStack())
                .inputItems(FIELD_RESTRICTION_CASING.asStack())
                .inputItems(ExtraBotanyItems.theOrigin)
                .inputItems(CMItems.BROKEN_RUNE.get())
                .inputFluids(Shroud_Zenith_essence.getFluid(1000))
                .inputItems(CustomTags.ZPM_CIRCUITS)
                .outputItems(SUPERNORMAL_MAGIC_CALCULATE_CORE.asItem())
                .EUt(777)
                .duration(777 * 20)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("twsit_tier_1") // 物质扭曲线圈
                .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
                .inputItems(CMItems.TWIST_RUNE.get())
                .inputItems(GTItems.FIELD_GENERATOR_LuV)
                .inputItems(ZENITH_CASING_GEARBOX.asStack())
                .inputFluids(Ultra_Mana.getFluid(77))
                .inputItems(ExtraBotanyItems.theChaos)
                .outputItems(MATERIAL_TWISTED_COIL.asItem())
                .EUt(7777)
                .duration(7 * 20)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("twsit_tier_2") // 维度扭曲线圈
                .inputItems(MATERIAL_TWISTED_COIL.asItem())
                .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
                .inputItems(CMItems.TWIST_RUNE.get(), 2)
                .inputItems(GTItems.FIELD_GENERATOR_ZPM)
                .inputItems(ZENITH_CASING_GEARBOX.asStack())
                .inputItems(GTMultiMachines.FUSION_REACTOR[GTValues.LuV].asStack())
                .inputItems(ExtraBotanyItems.theOrigin)
                .inputFluids(Twist_Power_Mana.getFluid(777))
                .outputItems(DIMENSION_TWISTED_COIL.asItem())
                .EUt(77777)
                .duration(77 * 20)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("twsit_tier_3") // 现实扭曲线圈
                .inputItems(DIMENSION_TWISTED_COIL.asItem())
                .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
                .inputItems(CMItems.TWIST_RUNE.get(), 3)
                .inputItems(GTItems.FIELD_GENERATOR_ZPM)
                .inputItems(ZENITH_CASING_GEARBOX.asStack())
                .inputItems(ExtraBotanyItems.theEnd)
                .inputItems(GTMultiMachines.FUSION_REACTOR[GTValues.ZPM].asStack())
                .inputFluids(Twist_Power_Mana.getFluid(7777))
                .outputItems(CMBlocks.REALITY_TWISTED_COIL.asItem())
                .EUt(77777)
                .duration(777 * 20)
                .save(provider);
        TerraPlateRecipeBuilder.builder("the_end") // 终末扭曲线圈
                .input(REALITY_TWISTED_COIL.asItem())
                .input(MATERIAL_TWISTED_COIL.asItem())
                .input(DIMENSION_TWISTED_COIL.asItem())
                .input(ExtraBotanyItems.theUniverse)
                .input(CMItems.QUASAR_RUNE.asStack())
                .input(GTMultiMachines.FUSION_REACTOR[GTValues.UV].asStack())
                .input(CMItems.ENDSLATE.asStack())
                .input(ChemicalHelper.get(block, Ultra_Mana))
                .input(CustomTags.UHV_CIRCUITS)
                .output(TERMINAL_TWISTED_COIL.asStack())
                .mana(Integer.MAX_VALUE)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("black_stone_casing") // 黑石锢魂外壳
                .inputItems(Blocks.OBSIDIAN.asItem(), 16)
                .inputItems(ChemicalHelper.get(plate, DEMON), 32)
                .inputItems(BloodMagicItems.REAGENT_BLOOD_LIGHT)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 6666))
                .outputItems(SOUL_LOCKING_CASING.asItem(), 16)
                .EUt(666)
                .duration(66 * 20)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("acane_accelerate_block") // 魔流束加速管道方块
                .inputItems(BloodMagicBlocks.SPEED_RUNE.get().asItem(), 2)
                .inputItems(GTItems.ELECTRIC_PUMP_IV)
                .inputItems(CustomTags.IV_CIRCUITS)
                .inputItems(ChemicalHelper.get(gear, GTMaterials.Ruridit))
                .inputItems(ChemicalHelper.get(screw, CMMaterials.Orichalcos), 6)
                .outputItems(ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK.asItem(), 2)
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(100))
                .EUt(666)
                .duration(100)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("aura_casing") // 立场汇聚机械方块
                .inputItems(GTItems.FIELD_GENERATOR_IV)
                .inputItems(GTItems.EMITTER_IV)
                .inputItems(ChemicalHelper.get(frameGt, PRIMOVOLITHEST))
                .inputItems(CASING_FORCE_FILED.asStack())
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(777))
                .outputItems(AURA_CONVERGENCE_CASING.asItem(), 1)
                .EUt(6666)
                .duration(777)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_fusion") // 魔力聚变外壳
                .inputItems(ChemicalHelper.get(plate, ManaSteel), 64)
                .inputItems(ORICHALCOS_FRAME.asItem(), 2)
                .inputItems(ChemicalHelper.get(frameGt, GTMaterials.Ruridit), 6)
                .inputItems(ALF_STEEL_CASING.asItem(), 8)
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(1600))
                .outputItems(MANA_FUSION_CASING.asItem(), 8)
                .EUt(4321)
                .duration(660)
                .save(provider);
        TerraPlateRecipeBuilder.builder("zenith_eye") // 天顶之眼
                .input(BotaniaItems.thirdEye)
                .input(ZENITH_CASING_BLOCK.asStack())
                .input(ZENITH_CASING_GEARBOX.asStack())
                .input(FIELD_RESTRICTION_CASING.asStack())
                .input(ELEMENTAL_CASING_GEARBOX.asStack())
                .input(ELF_STEEL_CASING_GEARBOX.asStack())
                .input(CMBlocks.ORICHALCOS_STEEL_CASING_GEARBOX.asStack())
                .input(CMItems.HORIZEN_RUNE.asStack())
                .output(ZENITH_EYE.asStack())
                .mana(7777777)
                .save(provider);
        BloodAltarRecipeBuilder.builder("casing_blood")// 血染机械方块
                .input(new ItemStack(GTBlocks.CASING_STEEL_SOLID, 1))
                .output(new ItemStack(CMBlocks.CASING_BLOOD.get(), 1))
                .syphon(10000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);
        ASSEMBLER_RECIPES.recipeBuilder("shroud_coil")
                .inputItems(ChemicalHelper.get(foil, Ultra_Mana), 8)
                .inputItems(ChemicalHelper.get(cableGtDouble, PRIMOVOLITHEST), 8)
                .inputItems(ChemicalHelper.get(gem, Psionic_Medulla))
                .inputItems(CMItems.TWIST_RUNE)
                .inputFluids(Shroud_Zenith_essence, 1000)
                .outputItems(SHROUD_MANA_COIL.asItem())
                .EUt(32768)
                .duration(1000)
                .save(provider);
        VanillaRecipeHelper.addShapedRecipe(
                provider, "rune_block_1",
                new ItemStack(BloodMagicBlocks.BLANK_RUNE.get(), 2), // 空白符文
                "ABA",
                "BCB",
                "ABA",
                'A', ChemicalHelper.get(plate, GTMaterials.BlackSteel),
                'B', BloodMagicItems.SLATE.get(),
                'C', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get()));
        VanillaRecipeHelper.addShapedRecipe(
                provider, "speed_rune",
                new ItemStack(BloodMagicBlocks.SPEED_RUNE.get(), 2), // 加速符文
                "ABA",
                "BCB",
                "ABA",
                'A', ChemicalHelper.get(plate, GTMaterials.BlueSteel),
                'C', BloodMagicItems.REINFORCED_SLATE.get(),
                'B', BotaniaItems.runeAir.asItem());
        VanillaRecipeHelper.addShapedRecipe(
                provider, "accelerate_rune",
                new ItemStack(BloodMagicBlocks.ACCELERATION_RUNE.get(), 1), // 促速符文
                "ADA",
                "BCB",
                "AEA",
                'A', CASING_BLOODLOGIC.get(),
                'C', BloodMagicBlocks.SPEED_RUNE.get(),
                'B', BotaniaItems.runeGreed.asItem(),
                'D', BloodMagicItems.DEMONIC_SLATE.get(),
                'E', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get()));
        VanillaRecipeHelper.addShapedRecipe(
                provider, "sacrifice_rune",
                new ItemStack(BloodMagicBlocks.SACRIFICE_RUNE.get(), 1), // 献祭符文
                "ADA",
                "BCB",
                "AEA",
                'A', ChemicalHelper.get(plate, COAGULBLOODGOLD),
                'C', BloodMagicBlocks.BLANK_RUNE.get(),
                'B', BotaniaItems.runeLust,
                'D', BloodMagicItems.REINFORCED_SLATE.get(),
                'E', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get()));
        VanillaRecipeHelper.addShapedRecipe(
                provider, "self_sacrifice_rune",
                new ItemStack(BloodMagicBlocks.SELF_SACRIFICE_RUNE.get(), 1), // 自我献祭符文
                "ADA",
                "BCB",
                "AEA",
                'A', ChemicalHelper.get(plate, GTMaterials.Gold),
                'C', BloodMagicBlocks.BLANK_RUNE.get(),
                'B', BotaniaItems.runeEnvy,
                'D', BloodMagicItems.REINFORCED_SLATE.get(),
                'E', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get()));
        VanillaRecipeHelper.addShapedRecipe(
                provider, "displacement_rune",
                new ItemStack(BloodMagicBlocks.DISPLACEMENT_RUNE.get(), 1), // 转位符文
                "ADA",
                "BCB",
                "AEA",
                'A', ChemicalHelper.get(plate, GTMaterials.BlueSteel),
                'C', BloodMagicBlocks.BLANK_RUNE.get(),
                'B', BotaniaItems.runeWinter,
                'D', BloodMagicItems.REINFORCED_SLATE.get(),
                'E', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get()));
        VanillaRecipeHelper.addShapedRecipe(
                provider, "capacity_rune",
                new ItemStack(BloodMagicBlocks.CAPACITY_RUNE.get(), 2), // 容量符文
                "ADA",
                "BCB",
                "AEA",
                'C', ChemicalHelper.get(plate, GTMaterials.BlueAlloy),
                'A', BloodMagicBlocks.BLANK_RUNE.get(),
                'B', BotaniaItems.runeWater,
                'D', BloodMagicItems.REINFORCED_SLATE.get(),
                'E', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get()));
        VanillaRecipeHelper.addShapedRecipe(
                provider, "blood_steel",
                new ItemStack(BloodMagicBlocks.BLOODSTONE.get(), 2), // 大血石砖
                "AAA",
                "ABA",
                "AAA",
                'A', ChemicalHelper.get(plate, HEMOPLATINUM),
                'B', BloodMagicItems.WEAK_BLOOD_SHARD.get());
        ASSEMBLER_RECIPES.recipeBuilder("compressed")
                .outputItems(MANA_COMPRESSED_CORE.asItem(), 2)
                .inputItems(ChemicalHelper.get(plate, HEMOPLATINUM), 16)
                .inputItems(CMItems.BLOOD_INDUCTOR, 4)
                .inputItems(BloodMagicItems.REAGENT_VOID)
                .inputItems(ZENITH_WILL_MECHANICAL_BLOCK.asItem(), 2)
                .EUt(1000)
                .duration(100)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("zenith_will")
                .inputItems(ChemicalHelper.get(plate, HEMOPLATINUM), 8)
                .inputItems(ChemicalHelper.get(plate, PRIMOVOLITHEST), 8)
                .addCondition(new HellForgeCondition(100))
                .inputItems(CASING_BLOODLOGIC.asItem(), 2)
                .outputItems(ZENITH_WILL_MECHANICAL_BLOCK.asItem())
                .EUt(1000)
                .duration(100)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("arcane_can")// 奥能覆层
                .inputItems(ChemicalHelper.get(screw, Photonium), 16)
                .inputItems(ChemicalHelper.get(screw, Aerialite), 8)
                .inputItems(ChemicalHelper.get(plate, Orichalcos), 2)
                .inputItems(UNFADING_GARDEN_CASING.asItem())
                .outputItems(ARCANE_REACTOR_BLOCK.asItem())
                .duration(200)
                .EUt(1000)
                .save(provider);
        ASSEMBLER_RECIPES.recipeBuilder("arcane_laser")// 奥能激光
                .inputItems(ARCANE_REACTOR_BLOCK.asItem())
                .inputItems(GTBlocks.LASER_PIPES[0].asItem(), 4)
                .inputItems(GTItems.LAPOTRON_CRYSTAL)
                .inputItems(ItemsRegistry.SOURCE_GEM.asItem(), 16)
                .inputItems(ExtraBotanyItems.lensPush, 4)
                .outputItems(ARCANE_LASER_CONDUIT_BLOCK.asItem())
                .duration(200)
                .EUt(8192)
                .save(provider);
        ASSEMBLER_RECIPES.recipeBuilder("arcane_laser_tower")// 奥能激光塔
                .inputItems(ARCANE_REACTOR_BLOCK.asItem())
                .inputItems(GTBlocks.LASER_PIPES[0].asItem(), 4)
                .inputItems(GTItems.LAPOTRON_CRYSTAL)
                .inputItems(ItemsRegistry.SOURCE_GEM.asItem(), 16)
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_IV.asItem())
                .inputItems(ExtraBotanyItems.lensPotion, 4)
                .outputItems(ARCANE_ENERGY_LASER_TOWER.asItem())
                .duration(200)
                .EUt(8192)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("aura_frozen_coil") // 立场冰封线圈
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(1444))
                .inputItems(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.asStack(1))
                .inputItems(BotaniaItems.runeWinter, 1)
                .inputItems(CustomTags.IV_CIRCUITS, 1)
                .inputItems(ChemicalHelper.get(plate, ManaSteel), 4)
                .circuitMeta(1)
                .outputItems(AURA_FROZEN_COIL.asStack(2))
                .EUt(GTValues.VA[GTValues.IV])
                .duration(400)
                .save(provider);
        ASSEMBLER_RECIPES.recipeBuilder("blood_ritual_mechanical_block")
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000))
                .inputItems(CASING_BLOOD.asItem(), 2)
                .inputItems(ChemicalHelper.get(gear, HEMOPLATINUM), 16)
                .outputItems(BLOOD_RITUAL_MECHANICAL_BLOCK.asStack())
                .duration(200)
                .EUt(8192 / 4 / 4)
                .save(provider);
        ASSEMBLER_RECIPES.recipeBuilder("ritual_colum_block")
                .inputItems(BLOOD_RITUAL_MECHANICAL_BLOCK.asStack())
                .inputItems(CMItems.RUNE_CIRCUIT_BOARD.asItem(), 4)
                .inputItems(BotaniaItems.runeEnvy, 7)
                .inputItems(BloodMagicBlocks.MASTER_RITUAL_STONE.get().asItem())
                .outputItems(RITUAL_COLUM_BLOCK.asStack())
                .duration(200)
                .EUt(8192 / 4 / 4)
                .save(provider);
        ASSEMBLER_RECIPES.recipeBuilder("ritual_mechine_block")
                .inputItems(RITUAL_COLUM_BLOCK.asStack())
                .inputItems(CustomTags.IV_CIRCUITS)
                .inputItems(ChemicalHelper.get(plate, DEMON), 4)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), 1000))
                .outputItems(RITUAL_MECHANICAL_BLOCK.asStack())
                .duration(200)
                .EUt(8192 / 4 / 4)
                .save(provider);
    }
}
