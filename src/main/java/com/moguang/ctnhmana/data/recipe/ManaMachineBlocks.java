package com.moguang.ctnhmana.data.recipe;

import appeng.core.definitions.AEItems;
import appeng.items.AEBaseItem;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.moguang.ctnhmana.common.recipe.builder.botania.ManaInfusionRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.TerraPlateRecipeBuilder;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMElements;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.multiblock.BloodMagic;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import vazkii.botania.common.block.BotaniaBlockFlammability;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.moguang.ctnhmana.registry.CMBlocks.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.MANA_REACTOR_RECIPES;

public class ManaMachineBlocks {
    public static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("pure_block") //纯净机械方块
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(BotaniaBlocks.livingrock.asItem())
                .inputItems(ChemicalHelper.get(TagPrefix.gearSmall,CMMaterials.ManaSteel),2)
                .circuitMeta(1)
                .EUt(32)
                .duration(120)
                .outputItems(CMBlocks.LIVING_ROCK_CASING.asItem()) //纯净机械方块
                .save(provider);
        ManaInfusionRecipeBuilder.builder("advanced_glass")    //强化魔力玻璃
                .input(GTBlocks.CASING_TEMPERED_GLASS.asStack())
                .output(CMBlocks.ENHANCED_MANA_GLASS.asStack()) //强化魔力玻璃
                .circuitMeta(1)
                .mana(15000)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("unwither_casing")  //不凋花园方块
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(GTBlocks.CASING_STAINLESS_CLEAN.asStack())
                .inputItems(CMBlocks.ENHANCED_MANA_GLASS.asStack())
                .inputItems(ChemicalHelper.get(plate,CMMaterials.Photonium),2)
                .inputFluids(CMMaterials.MANA_STABLE_COOLDOWN.getFluid(100))
                .outputItems(CMBlocks.UNFADING_GARDEN_CASING,2) //不凋花园方块
                .circuitMeta(7)
                .EUt(128)
                .duration(300)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("pipe_element")    //源质钢管道机械方块
                .inputItems(ChemicalHelper.get(TagPrefix.rod,CMMaterials.Elementium),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gearSmall,CMMaterials.Elementium),4)
                .inputItems(ELEMENTIUM_FRAME.asStack())
                .circuitMeta(5)
                .EUt(32)
                .duration(400)
                .outputItems(CMBlocks.ELEMENTIUM_PIPE_CASING.asItem(),2) //源质钢管道机械方块
                .save(provider);
        VanillaRecipeHelper.addShapedRecipe( //源质钢管道机械方块
                provider, "pip_element_one",
                CMBlocks.ELEMENTIUM_PIPE_CASING.asStack(2), //源质钢管道机械方块
                "ABA",
                "BCB",
                "ABA",
                'A', ChemicalHelper.get(TagPrefix.rod,CMMaterials.Elementium),
                'B', (ChemicalHelper.get(TagPrefix.gearSmall,CMMaterials.Elementium)),
                'C', ELEMENTIUM_FRAME.asStack()
        );
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("elementium_gear_box") //源质钢齿轮箱机械方块
                .inputItems(ChemicalHelper.get(plate,CMMaterials.Elementium),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear,CMMaterials.Elementium),2)
                .inputItems(ELEMENTIUM_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.ELEMENTAL_CASING_GEARBOX.asItem(),2) //源质钢齿轮箱机械方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("manasteel_gear_box") //魔力钢齿轮箱方块
                .inputItems(ChemicalHelper.get(plate,CMMaterials.ManaSteel),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear,CMMaterials.ManaSteel),2)
                .inputItems(CMBlocks.MANA_STEEL_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.CASING_MANASTEEL_GEARBOX.asItem(),2) //魔力钢齿轮箱方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("orichalcos_gear_box") //奥利哈钢齿轮箱机械方块
                .inputItems(ChemicalHelper.get(plate,CMMaterials.Orichalcos),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear,CMMaterials.Orichalcos),2)
                .inputItems(CMBlocks.ORICHALCOS_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.Orichalcos.asItem(),2) //奥利哈钢齿轮箱机械方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("elf_steel_gear_box") //精灵钢齿轮箱机械方块
                .inputItems(ChemicalHelper.get(plate,CMMaterials.AlfSteel),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear,CMMaterials.AlfSteel),2)
                .inputItems(CMBlocks.ALFSTEEL_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.ELF_STEEL_CASING_GEARBOX.asItem(),2) //精灵钢齿轮箱机械方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("force_fileding_block") //力场领域机械方块
                .inputItems(GTItems.FIELD_GENERATOR_EV)
                .inputItems(CMBlocks.CASING_BLOOD.asStack(2))
                .inputItems(CMItems.BLOODY_CHIP,4)
                .inputItems(CMItems.BLOOD_CAPACITOR,4)
                .EUt(GTValues.VA[GTValues.EV])
                .duration(200)
                .circuitMeta(7)
                .outputItems(CMBlocks.CASING_FORCE_FILED.asStack(2)) //力场领域机械方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("pure_logic_casing") //纯净魔力逻辑传输方块
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(GTItems.FIELD_GENERATOR_LV)
                .inputItems(CMBlocks.LIVING_ROCK_CASING,4)
                .inputItems(BotaniaItems.runeMana.asItem(),1)
                .outputItems(CMBlocks.PURE_LOGIC_CASING.asStack(4)) //纯净魔力逻辑传输方块
                .duration(200)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_shatter_core") //魔力粉碎核心
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(GTItems.COMPONENT_GRINDER_DIAMOND)
                .inputItems(ChemicalHelper.get(TagPrefix.cableGtSingle, CMMaterials.ManaSteel),4)
                .inputItems(BotaniaItems.runeSummer.asItem(),2)
                .inputItems(BotaniaItems.runeMana.asItem(),2)
                .outputItems(CMBlocks.MANA_SHATTER_CORE.asItem()) //魔力粉碎核心
                .duration(200)
                .circuitMeta(2)
                .EUt(32)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_forge_core") //魔力锻造核心
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(Blocks.ANVIL.asItem())
                .inputItems(ChemicalHelper.get(TagPrefix.cableGtSingle, CMMaterials.ManaSteel),4)
                .inputItems(BotaniaItems.runeAutumn.asItem(),2)
                .inputItems(BotaniaItems.runeMana.asItem(),2)
                .outputItems(CMBlocks.MANA_FORGE_CORE.asItem()) //魔力粉碎核心
                .duration(200)
                .circuitMeta(2)
                .EUt(32)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_refinment_core") //魔力细核心
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(BotaniaItems.manaMirror.asItem())
                .inputItems(ChemicalHelper.get(TagPrefix.cableGtSingle, CMMaterials.ManaSteel),4)
                .inputItems(BotaniaItems.runeSpring.asItem(),2)
                .inputItems(BotaniaItems.runeMana.asItem(),2)
                .outputItems(CMBlocks.MANA_REFINEMENT_CORE.asItem()) //魔力粉碎核心
                .duration(200)
                .circuitMeta(2)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("zenith_gearbox") //天顶齿轮箱
                .inputItems(CMBlocks.CASING_MANASTEEL_GEARBOX)
                .inputItems(ChemicalHelper.get(TagPrefix.gem,CMMaterials.Psionic_Medulla))
                .inputFluids(CMMaterials.Zenith_essence.getFluid(1000))
                .outputItems(ZENITH_CASING_GEARBOX)
                .duration(1000)
                .circuitMeta(1)
                .EUt(GTValues.VA[GTValues.IV])
                .save(provider);
        TerraPlateRecipeBuilder.builder("pure_logic_core") //纯净魔力核心
                .input(CustomTags.HV_CIRCUITS)
                .input(BotaniaItems.runeMana)
                .input(PURE_LOGIC_CASING.asStack())
                .input(AEItems.CALCULATION_PROCESSOR.stack())
                .input(CMItems.MANA_WAFER.asStack())
                .output(PURE_MAGIC_CALCULATE_CORE.asStack())
                .mana(500000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("bloodlogic_casing") //血逻辑方块
                .inputItems(PURE_LOGIC_CASING.asStack(6))
                .inputItems(GTItems.FIELD_GENERATOR_HV)
                .inputItems(CustomTags.EV_CIRCUITS)
                .inputItems(CMItems.BLOOD_CAPACITOR,6)
                .inputItems(BotaniaItems.runeWrath.asItem(),3)
                .inputItems(ChemicalHelper.get(TagPrefix.gear,CMMaterials.COAGULBLOODGOLD),3)
                .outputItems(CASING_BLOODLOGIC.asItem(),6)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 66666))
                .duration(666*2)
                .EUt(GTValues.VA[GTValues.HV])
                .save(provider);
        TerraPlateRecipeBuilder.builder("mana_steel_casing1") // 魔力钢机械方块
                .input(ChemicalHelper.get(plate,ManaSteel))
                .input(ChemicalHelper.get(plate,ManaSteel))
                .input(ChemicalHelper.get(plate,ManaSteel))
                .input(ChemicalHelper.get(plate,ManaSteel))
                .input(ChemicalHelper.get(plate,ManaSteel))
                .input(ChemicalHelper.get(plate,ManaSteel))
                .input(ChemicalHelper.get(frameGt,ManaSteel))
                .output(MANA_STEEL_CASING.asStack())
                .mana(500000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_steel_casing2") // 魔力钢机械方块
                .inputItems(ChemicalHelper.get(plate,ManaSteel),6)
                .inputItems(ChemicalHelper.get(frameGt,ManaSteel),1)
                .inputFluids(Mana.getFluid(1000))
                .circuitMeta(6)
                .outputItems(MANA_STEEL_CASING.asItem(),1)
                .EUt(512)
                .save(provider);
        TerraPlateRecipeBuilder.builder("mana_steel_frame1") // 魔力钢框架
                .input(ChemicalHelper.get(rod,ManaSteel))
                .input(ChemicalHelper.get(rod,ManaSteel))
                .input(ChemicalHelper.get(rod,ManaSteel))
                .input(ChemicalHelper.get(rod,ManaSteel))
                .output(ChemicalHelper.get(frameGt,ManaSteel))
                .mana(5000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("mana_steel_frame2") // 魔力钢框架
                .inputItems(ChemicalHelper.get(rod,ManaSteel),4)
                .inputFluids(Mana.getFluid(100))
                .circuitMeta(4)
                .outputItems(ChemicalHelper.get(frameGt,ManaSteel),1)
                .EUt(1024)
                .save(provider);
        TerraPlateRecipeBuilder.builder("elementium_casing1") // 源质钢机械方块
                .input(ChemicalHelper.get(plate,Elementium))
                .input(ChemicalHelper.get(plate,Elementium))
                .input(ChemicalHelper.get(plate,Elementium))
                .input(ChemicalHelper.get(plate,Elementium))
                .input(ChemicalHelper.get(plate,Elementium))
                .input(ChemicalHelper.get(plate,Elementium))
                .input(ChemicalHelper.get(frameGt,Elementium))
                .output(ELEMENTIUM_CASING.asStack())
                .mana(700000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("elementium_casing2") //  源质钢机械方块
                .inputItems(ChemicalHelper.get(plate,Elementium),6)
                .inputItems(ChemicalHelper.get(frameGt,Elementium),1)
                .inputFluids(Mana.getFluid(2000))
                .circuitMeta(6)
                .outputItems(ELEMENTIUM_CASING.asItem(),1)
                .EUt(1024)
                .save(provider);
        TerraPlateRecipeBuilder.builder("elementium_frame1") // 源质钢框架
                .input(ChemicalHelper.get(rod,Elementium))
                .input(ChemicalHelper.get(rod,Elementium))
                .input(ChemicalHelper.get(rod,Elementium))
                .input(ChemicalHelper.get(rod,Elementium))
                .output(ChemicalHelper.get(frameGt,Elementium))
                .mana(6000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("elementium_frame2") // 源质钢框架
                .inputItems(ChemicalHelper.get(rod,Elementium),4)
                .inputFluids(Mana.getFluid(250))
                .circuitMeta(4)
                .outputItems(ChemicalHelper.get(frameGt,Elementium),1)
                .EUt(512)
                .save(provider);
        TerraPlateRecipeBuilder.builder("alfsteel_casing1") // 精灵钢机械方块
                .input(ChemicalHelper.get(plate,AlfSteel))
                .input(ChemicalHelper.get(plate,AlfSteel))
                .input(ChemicalHelper.get(plate,AlfSteel))
                .input(ChemicalHelper.get(plate,AlfSteel))
                .input(ChemicalHelper.get(plate,AlfSteel))
                .input(ChemicalHelper.get(plate,AlfSteel))
                .input(ChemicalHelper.get(frameGt,AlfSteel))
                .output(ALF_STEEL_CASING.asStack())
                .mana(1000000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("alfsteel_casing2") //   精灵钢机械方块
                .inputItems(ChemicalHelper.get(plate,AlfSteel),6)
                .inputItems(ChemicalHelper.get(frameGt,AlfSteel),1)
                .inputFluids(Mana.getFluid(5000))
                .circuitMeta(6)
                .outputItems(ALF_STEEL_CASING.asItem(),1)
                .EUt(1920)
                .save(provider);
        TerraPlateRecipeBuilder.builder("alfsteel_frame1") // 精灵钢框架
                .input(ChemicalHelper.get(rod,AlfSteel))
                .input(ChemicalHelper.get(rod,AlfSteel))
                .input(ChemicalHelper.get(rod,AlfSteel))
                .input(ChemicalHelper.get(rod,AlfSteel))
                .output(ChemicalHelper.get(frameGt,AlfSteel))
                .mana(5000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("alfsteel_frame2") // 精灵钢框架
                .inputItems(ChemicalHelper.get(rod,AlfSteel),4)
                .inputFluids(Mana.getFluid(1000))
                .circuitMeta(4)
                .outputItems(ChemicalHelper.get(frameGt,AlfSteel),1)
                .EUt(1920)
                .save(provider);
        TerraPlateRecipeBuilder.builder("terra_steel_casing1") // 泰拉钢机械方块
                .input(ChemicalHelper.get(plate,TerraSteel))
                .input(ChemicalHelper.get(plate,TerraSteel))
                .input(ChemicalHelper.get(plate,TerraSteel))
                .input(ChemicalHelper.get(plate,TerraSteel))
                .input(ChemicalHelper.get(plate,TerraSteel))
                .input(ChemicalHelper.get(plate,TerraSteel))
                .input(ChemicalHelper.get(frameGt,TerraSteel))
                .output(TERRA_STEEL_CASING.asStack())
                .mana(750000)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("terra_steel_casing2") //   泰拉钢机械方块
                .inputItems(ChemicalHelper.get(plate,TerraSteel),6)
                .inputItems(ChemicalHelper.get(frameGt,TerraSteel),1)
                .inputFluids(Mana.getFluid(3000))
                .circuitMeta(6)
                .outputItems(TERRA_STEEL_CASING.asItem(),1)
                .EUt(1920)
                .save(provider);
        TerraPlateRecipeBuilder.builder("terra_steel_frame1") // 泰拉钢框架
                .input(ChemicalHelper.get(rod,TerraSteel))
                .input(ChemicalHelper.get(rod,TerraSteel))
                .input(ChemicalHelper.get(rod,TerraSteel))
                .input(ChemicalHelper.get(rod,TerraSteel))
                .output(ChemicalHelper.get(frameGt,TerraSteel))
                .mana(500)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("terra_steel_frame2") // 泰拉钢框架
                .inputItems(ChemicalHelper.get(rod,TerraSteel),4)
                .inputFluids(Mana.getFluid(500))
                .circuitMeta(4)
                .outputItems(ChemicalHelper.get(frameGt,TerraSteel),1)
                .EUt(1920)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("zenith_casing_block") //   天顶强化机械方块
                .inputItems(ChemicalHelper.get(plate,Plus_Mana),2)
                .inputItems(ChemicalHelper.get(frameGt,Plus_Mana),1)
                .inputItems(CASING_BLOOD.asItem(),2)
                .inputFluids(Zenith_essence.getFluid(200))
                .circuitMeta(6)
                .outputItems(ZENITH_CASING_BLOCK.asItem(),1)
                .EUt(1920)
                .save(provider);
    }
}
