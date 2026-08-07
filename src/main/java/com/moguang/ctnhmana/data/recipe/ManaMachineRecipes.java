package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.moguang.ctnhmana.data.recipe.builder.botania.ElfPlateRecipeBuilder;
import com.moguang.ctnhmana.data.recipe.builder.botania.TerraPlateRecipeBuilder;
import com.moguang.ctnhmana.registry.*;
import mythicbotany.register.ModItems;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.moguang.ctnhmana.registry.CMBlocks.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMachines.BROADCAST_HATCH;
import static com.moguang.ctnhmana.registry.CMMachines.CENTRALCONTROL_BUS;
import static com.moguang.ctnhmana.registry.CMMachines.EXTENDED_CENTRALCONTROL_BUS;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.*;
import static com.moguang.ctnhmana.registry.multiblock.BloodMagic.*;
import static com.moguang.ctnhmana.registry.multiblock.Botania.*;
import static com.moguang.ctnhmana.registry.multiblock.ManaMachine.*;
import static com.moguang.ctnhmana.registry.multiblock.Misc.*;
import static vazkii.botania.common.block.BotaniaBlocks.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import static wayoftime.bloodmagic.common.block.BloodMagicBlocks.*;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.HELLFORGED_BLOCK;

public class ManaMachineRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("gaia_reactor")// 盖亚反应器
                .inputItems(gaiaIngot, 4)
                .inputItems(CASING_STEEL_SOLID.asItem(), 4)
                .inputItems(EMITTER_MV, 2)
                .inputItems(ChemicalHelper.get(screw, TerraSteel))
                .inputItems(manaDiamond)
                .inputFluids(Polyethylene.getFluid(288))
                .outputItems(GAIA_REACTOR)
                .EUt(120)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("industrial_petal_apothecary")// 工业花药台
                .inputItems(LIVING_ROCK_CASING.asItem(), 8)
                .inputItems(PURE_MAGIC_CALCULATE_CORE.asItem(), 1)
                .inputItems(corporeaIndex.asItem(), 1)
                .inputItems(ELEMENTIUM_FRAME.asItem(), 4)
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(ELECTRIC_MOTOR_LV)
                .outputItems(INDUSTRIAL_PETAL_APOTHECARY)
                .EUt(30)
                .duration(200)
                .save(provider);
        VanillaRecipeHelper.addShapedRecipe(// 工业狱火锻炉
                provider, "hell_forge",
                HELLFORGE.asStack(1),
                "AEA",
                "BCB",
                "ADA",
                'A', HELLFORGED_INGOT.get(),
                'B', ETHEREAL_SLATE.get(),
                'C', SOUL_LOCKING_CASING.asStack(),
                'D', SOUL_FORGE_ITEM.get(),
                'E', CustomTags.LuV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(// 魔力卷板机
                provider, "mana_bender",
                MANA_BENDER.asStack(1),
                "AEA",
                "BCB",
                "DFG",
                'A', LIVING_ROCK_CASING.asItem(),
                'B', ELECTRIC_MOTOR_MV.asItem(),
                'C', MAGIC_CORE.get(),
                'D', ELECTRIC_PISTON_MV.asItem(),
                'E', TERRA_STEEL_CASING.asItem(),
                'F', ROBOT_ARM_MV.asItem(),
                'G', CustomTags.EV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(provider, "ritual_rune_air",
                new ItemStack(AIR_RITUAL_STONE.get().asItem(), 1),
                "AB",
                'A', BLANK_RITUAL_STONE.get().asItem(),
                'B', AIR_INSCRIPTION_TOOL.get());
        VanillaRecipeHelper.addShapedRecipe(provider, "ritual_rune_water",
                new ItemStack(WATER_RITUAL_STONE.get().asItem(), 1),
                "AB",
                'A', BLANK_RITUAL_STONE.get().asItem(),
                'B', WATER_INSCRIPTION_TOOL.get());
        VanillaRecipeHelper.addShapedRecipe(provider, "ritual_rune_fire",
                new ItemStack(FIRE_RITUAL_STONE.get().asItem(), 1),
                "AB",
                'A', BLANK_RITUAL_STONE.get().asItem(),
                'B', FIRE_INSCRIPTION_TOOL.get());
        VanillaRecipeHelper.addShapedRecipe(provider, "ritual_rune_earth",
                new ItemStack(EARTH_RITUAL_STONE.get().asItem(), 1),
                "AB",
                'A', BLANK_RITUAL_STONE.get().asItem(),
                'B', EARTH_INSCRIPTION_TOOL.get());
        VanillaRecipeHelper.addShapedRecipe(provider, "ritual_rune_dusk",
                new ItemStack(DUSK_RITUAL_STONE.get().asItem(), 1),
                "AB",
                'A', BLANK_RITUAL_STONE.get().asItem(),
                'B', DUSK_INSCRIPTION_TOOL.get());

        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_wiremill")// 魔力线材轧机
                .inputItems(ELECTRIC_PISTON_MV, 1)
                .inputItems(MAGIC_CORE.get())
                .inputItems(ChemicalHelper.get(wireGtQuadruple, TerraSteel), 4)
                .inputItems(CustomTags.EV_CIRCUITS, 4)
                .inputFluids(Mana.getFluid(1000))
                .outputItems(MANA_WIREMILL)
                .EUt(120)
                .duration(400)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_lathe")// 魔力车床
                .inputItems(ELECTRIC_PISTON_MV, 1)
                .inputItems(MAGIC_CORE.get())
                .inputItems(LATHE[2], 2)
                .inputItems(CustomTags.EV_CIRCUITS, 4)
                .inputFluids(Mana.getFluid(1000))
                .outputItems(MANA_LATHE)
                .EUt(120)
                .duration(400)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_mixer")// 魔力搅拌机
                .inputItems(ELECTRIC_PISTON_MV, 1)
                .inputItems(MAGIC_CORE.get())
                .inputItems(MIXER[4], 2)
                .inputItems(CustomTags.LuV_CIRCUITS, 4)
                .inputFluids(Mana.getFluid(1000))
                .outputItems(MANA_MIXER)
                .EUt(120)
                .duration(400)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_assembler")// 魔力组装机
                .inputItems(ChemicalHelper.get(gear, AlfSteel), 16)
                .inputItems(MAGIC_CORE.get())
                .inputItems(ROBOT_ARM_EV, 2)
                .inputItems(CIRCUIT_ASSEMBLER[4], 4)
                .inputItems(CustomTags.LuV_CIRCUITS, 6)
                .inputItems(MAGIC_CORE.asStack(4))
                .inputItems(ORICHALCOS_FRAME.asStack(8))
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(1000))
                .outputItems(MANA_ASSEMBLER)
                .EUt(2048)
                .duration(100)
                .save(provider);
        VanillaRecipeHelper.addShapedRecipe(// 魔力反应器
                provider, "mana_reactor",
                MANA_REACTOR.asStack(1),
                "AAA",
                "BCB",
                "DED",
                'A', LIVING_ROCK_CASING.asItem(),
                'B', CustomTags.EV_CIRCUITS,
                'C', MAGIC_CORE.get(),
                'D', gaiaSpreader.asItem(),
                'E', fabulousPool.asItem());
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_condenser")// 魔力凝聚器
                .inputItems(corporeaSpark, 2)
                .inputItems(spark, 2)
                .inputItems(CIRCUIT_ASSEMBLER[4], 4)
                .inputItems(CASING_TITANIUM_STABLE.asItem(), 4)
                .inputItems(runeWinter, 16)
                .inputFluids(Mana.getFluid(4000))
                .outputItems(MANA_CONDENSER.asStack())
                .EUt(1920)
                .duration(200)
                .save(provider);
        TerraPlateRecipeBuilder.builder("twist_reactor_mk1")// 扭曲聚变反应堆mk1
                .input(TWISTED_FUSION_CASING.asItem())
                .input(TWISTED_FUSION_CASING.asItem())
                .input(TWISTED_FUSION_CASING.asItem())
                .input(FIELD_GENERATOR_IV.get())
                .input(ChemicalHelper.get(wireGtHex, IndiumTinBariumTitaniumCuprate).getItem())
                .input(ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT_WAFER.get())
                .input(SUPERCONDUCTING_COIL.asItem())
                .output(TWISTED_FUSION_MK1.asStack())
                .mana(7777777)
                .save(provider);
        ElfPlateRecipeBuilder.builder("twist_reactor_mk2")
                .input(TWISTED_FUSION_MK1.getItem())
                .input(DIMENSION_TWISTED_COIL.asItem())
                .input(SUPERNORMAL_MAGIC_CALCULATE_CORE.asItem())
                .input(TWIST_RUNE.asItem())
                .input(STARLIGHT_RUNE.asItem())
                .output(TWISTED_FUSION_MK2.asStack())
                .mana(77777777)
                .save(provider);
        TerraPlateRecipeBuilder.builder("twist_reactor_mk3")
                .input(TWISTED_FUSION_MK2.getItem())
                .input(REALITY_TWISTED_COIL.asItem())
                .input(SUPERNORMAL_MAGIC_CALCULATE_CORE.asItem())
                .input(TWIST_RUNE.asItem())
                .input(STARLIGHT_RUNE.asItem())
                .input(PROLIFERATION_RUNE.asItem())
                .input(HORIZEN_RUNE.asItem())
                .input(QUASAR_RUNE.asItem())
                .output(TWISTED_FUSION_MK3.asStack())
                .mana(777777777)
                .save(provider);

        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("meteor_capturer")// 坠星操纵者
                .inputItems(CustomTags.LuV_CIRCUITS, 2)
                .inputItems(ChemicalHelper.get(frameGt, TungstenSteel).getItem(), 2)
                .inputItems(ChemicalHelper.get(plate, TungstenSteel).getItem(), 4)
                .inputItems(FIELD_GENERATOR_EV.asStack(1))
                .inputItems(ENDSLATE.get())
                .inputItems(BLOODSTONE_BRICK.get().asItem(), 4)
                .inputFluids(Epoxy.getFluid(576))
                .outputItems(METEOR_CAPTURER.asStack())
                .EUt(7680)
                .duration(100)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("industral_blood_altar")// 工业血之祭坛
                .inputItems(ChemicalHelper.get(gear, COAGULBLOODGOLD), 16)
                .inputItems(BLANK_RUNE.get().asItem(), 16)
                .inputItems(BLOOD_ALTAR.get().asItem(), 1)
                .inputItems(SLATE.get())
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 10000))
                .outputItems(INDUSTRIAL_ALTAR.getItem())
                .EUt(240)
                .duration(100)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("industrial_salvaging")// 工业拆解台
                .inputItems(LIVING_ROCK_CASING.asItem(), 8)
                .inputItems(dev.shadowsoffire.apotheosis.adventure.Adventure.Items.SALVAGING_TABLE.get())
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .inputItems(ROBOT_ARM_MV.asStack(2))
                .outputItems(INDUSTRIAL_SALVAGING.getItem())
                .EUt(120)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("gem_inlay")// 宝石镶嵌机
                .inputItems(LIVING_ROCK_CASING.asItem(), 8)
                .inputItems(dev.shadowsoffire.apotheosis.adventure.Adventure.Items.GEM_CUTTING_TABLE.get())
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .inputItems(ROBOT_ARM_MV.asStack(2))
                .outputItems(GEM_INLAY.getItem())
                .EUt(120)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("ritual_mechanical_array")// 工业血祭仪式阵
                .inputItems(RITUAL_MECHANICAL_BLOCK.get().asItem(), 8)
                .inputItems(BLOOD_RITUAL_MECHANICAL_BLOCK.get().asItem(), 8)
                .inputItems(MASTER_RITUAL_STONE.get().asItem(), 1)
                .inputItems(BLANK_RITUAL_STONE.get().asItem(), 4)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 4000))
                .outputItems(RITUAL_MECHANICAL_ARRAY.getItem())
                .EUt(480)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder("demon_will_generator")// 恶魔意志发电机
                .inputItems(CustomTags.ZPM_CIRCUITS, 8)
                .inputItems(HELLFORGED_BLOCK.get().asItem(), 2)
                .inputItems(ChemicalHelper.get(screw, PRIMOVOLITHEST), 16)
                .inputItems(DEMON_WILL_GAUGE, 16)
                .inputItems(REAGENT_SUPPRESSION.get())
                .inputItems(REAGENT_TELEPOSITION.get(), 1)
                .inputItems(TWISTED_SOUL_FORGING.asItem())
                .inputItems(CORROSIVE_CORE)
                .inputItems(STEADFAST_CORE)
                .inputItems(DESTRUCTIVE_CORE)
                .inputItems(VENGEFUL_CORE)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 4000))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), 4000))
                .outputItems(DEMON_WILL_GENERATOR.getItem())
                .EUt(7680)
                .duration(100)
                .save(provider);
        GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder("beams")// 戴森光束
                .inputItems(ZENITH_STAR, 32)
                .inputItems(CustomTags.UV_CIRCUITS, 32)
                .inputItems(ChemicalHelper.get(frameGt, Ultra_Mana), 64)
                .inputItems(SUPERCONDUCTING_COIL.asItem(), 32)
                .inputItems(MANA_CIRCUIT_BOARD, 64)
                .inputItems(ZENITH_EYE.asItem(), 8)
                .inputItems(ChemicalHelper.get(wireGtQuadruple, Ultra_Mana), 64)
                .inputItems(ChemicalHelper.get(block, EnderPearl), 64)
                .inputItems(STARLIGHT_RUNE, 8)
                .inputItems(TWIST_RUNE, 8)
                .inputItems(HORIZEN_RUNE, 8)
                .inputItems(QUASAR_RUNE)
                .inputItems(HIGH_POWER_CASING.asItem(), 32)
                .inputFluids(Zenith_essence.getFluid(14400))
                .inputFluids(Mana_Radiation_Mixture.getFluid(144000))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 144000))
                .inputFluids(NaquadahEnriched.getFluid(14400))
                .outputItems(NICOLL_DYSON_BEAMS)
                .stationResearch(
                        b -> b.researchStack(ZENITH_STAR.get().getDefaultInstance()).CWUt(32).EUt(VA[ZPM]))
                .EUt(524288)
                .duration(1000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("mysticspire")// 神秘尖塔
                .input(corporeaBrickStairs.asItem())
                .input(corporeaBlock.asItem())
                .input(corporeaBrick.asItem())
                .input(corporeaIndex.asItem())
                .input(corporeaSpark)
                .input(MAGIC_CORE.asStack())
                .input(CustomTags.HV_CIRCUITS)
                .output(MysticSpire.asStack())
                .mana(500000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("magic_core")
                .inputItems(ChemicalHelper.get(gear, Steel), 4)
                .inputItems(ChemicalHelper.get(gear, ManaSteel), 4)
                .inputItems(FIELD_GENERATOR_LV)
                .inputItems(manaDetector.asItem())
                .inputItems(runeMana)
                .outputItems(MAGIC_CORE.asStack())
                .EUt(32)
                .duration(20)
                .save(provider);
        VanillaRecipeHelper.addShapedRecipe(// 魔力操纵者
                provider, "mana_transformer",
                MANA_FORCE_TRANSFORMER.asStack(1),
                "AAA",
                "BCB",
                "DED",
                'A', PURE_LOGIC_CASING.asItem(),
                'B', CustomTags.EV_CIRCUITS,
                'C', MAGIC_CORE.get(),
                'D', MysticSpire.asStack(),
                'E', SKY_FLOWER_SPEECH.get());
        GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder("eternal_wos")
                .inputItems(CORROSIVE_CORE)
                .inputItems(DESTRUCTIVE_CORE)
                .inputItems(VENGEFUL_CORE)
                .inputItems(STEADFAST_CORE)
                .inputItems(CMMachines.DIGITAL_WELL_OF_SUFFER[IV].asStack())
                .inputItems(ChemicalHelper.get(plateDouble, HEMOPLATINUM), 6)
                .inputItems(ChemicalHelper.get(plateDouble, COAGULBLOODGOLD), 6)
                .inputItems(CustomTags.ZPM_CIRCUITS, 4)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 66666))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), 6666))
                .inputFluids(SodiumPotassium, 66666)
                .outputItems(ETERNAL_WELL_OF_SUFFER.asStack())
                .EUt(32768)
                .duration(1000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("redstone_signal_broadcast_hatch")// 红石信号广播仓
                .inputItems(MACHINE_CASING_EV.asStack(1))
                .inputItems(new ItemStack(Items.REDSTONE_TORCH, 1))
                .inputItems(COVER_MACHINE_CONTROLLER.asStack(1))
                .inputItems(ChemicalHelper.get(screw, StainlessSteel), 2)
                .inputItems(COVER_ACTIVITY_DETECTOR.asStack(1))
                .outputItems(BROADCAST_HATCH.asStack(1))
                .EUt(VA[EV])
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("centralcontrol_bus")// 中央存储控制总线
                .inputItems(MACHINE_CASING_EV.asStack(1))
                .inputItems(CustomTags.EV_CIRCUITS, 1)
                .inputItems(ITEM_IMPORT_BUS[EV].asStack(1))
                .inputItems(ITEM_EXPORT_BUS[EV].asStack(1))
                .inputItems(ROBOT_ARM_EV.asStack(1))
                .inputItems(ChemicalHelper.get(plate, Lead), 1)
                .inputItems(ChemicalHelper.get(plate, Bismuth), 1)
                .outputItems(CENTRALCONTROL_BUS.asStack(1))
                .EUt(VA[EV])
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("extended_centralcontrol_bus")// 拓展中央存储控制总线
                .inputItems(CENTRALCONTROL_BUS.asStack(1))
                .inputItems(MACHINE_CASING_IV.asStack(1))
                .inputItems(CustomTags.IV_CIRCUITS, 2)
                .inputItems(ITEM_IMPORT_BUS[IV].asStack(1))
                .inputItems(ITEM_EXPORT_BUS[IV].asStack(1))
                .inputItems(ROBOT_ARM_IV.asStack(2))
                .inputItems(COVER_MACHINE_CONTROLLER.asStack(1))
                .outputItems(EXTENDED_CENTRALCONTROL_BUS.asStack(1))
                .EUt(VA[IV])
                .duration(400)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_fuel_infuser")
                .inputItems(ELECTRIC_PUMP_IV.asStack(4))
                .inputItems(FLUID_CELL_LARGE_STAINLESS_STEEL.asStack(4))
                .inputItems(UNFADING_GARDEN_CASING.asStack())
                .inputItems(MANA_SOC.asStack(4))
                .inputItems(CustomTags.LuV_CIRCUITS, 4)
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(1440))
                .outputItems(MANA_FUEL_INFUSER.asStack())
                .EUt(VA[EV])
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder("ahcc_assembly_line")
                .inputItems(MANA_REACTOR.asStack(4))
                .inputItems(GAIA_REACTOR.asStack(4))
                .inputItems(MACHINE_CASING_LuV.asStack(1))
                .inputItems(TERRA_STEEL_FRAME.asStack(16))
                .inputItems(CustomTags.ZPM_CIRCUITS, 32)
                .inputItems(CustomTags.LuV_CIRCUITS, 64)
                .inputItems(MAGIC_CORE.asStack(16))
                .inputItems(WILL_WAFER.asStack(16))
                .inputItems(MANA_SOC.asStack(64))
                .inputItems(PURE_MAGIC_CALCULATE_CORE.asStack(16))
                .inputItems(ARCANE_SHIELDING_COATED_GLASS.asStack(16))
                .inputItems(ChemicalHelper.get(gear, Orichalcos), 16)
                .inputItems(FIELD_GENERATOR_IV.asStack(16))
                .inputItems(ChemicalHelper.get(plate, NiobiumTitanium), 64)
                .inputItems(ChemicalHelper.get(wireGtOctal, Aerialite), 32)
                .inputItems(ModItems.alfheimRune, 64)
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(14400))
                .inputFluids(Mana.getFluid(14400))
                .inputFluids(Zenith_essence.getFluid(14400))
                .inputFluids(SolderingAlloy.getFluid(14400))
                .outputItems(AHCC.asStack())
                .EUt(VA[IV])
                .duration((int) (1000 * SECONDS))
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("zenith_martix")
                .inputItems(ZENITH_EYE.asItem(), 2)
                .inputItems(ChemicalHelper.get(gemChipped, Psionic_Medulla), 4)
                .inputItems(TWIST_RUNE)
                .inputItems(HORIZEN_RUNE)
                .inputItems(STARLIGHT_RUNE)
                .inputItems(PROLIFERATION_RUNE)
                .inputItems(CustomTags.UV_CIRCUITS, 4)
                .inputFluids(Shroud_Zenith_essence, 1440)
                .EUt(32768)
                .duration(20 * 1000)
                .save(provider);
    }
}
