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

import static com.moguang.ctnhmana.registry.CMBlocks.*;
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
                .inputItems(ChemicalHelper.get(TagPrefix.plate,CMMaterials.Photonium),2)
                .inputFluids(CMMaterials.MANA_STABLE_COOLDOWN.getFluid(100))
                .outputItems(CMBlocks.UNFADING_GARDEN_CASING,2) //不凋花园方块
                .circuitMeta(7)
                .EUt(128)
                .duration(300)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("pipe_element")    //源质钢管道机械方块
                .inputItems(ChemicalHelper.get(TagPrefix.rod,CMMaterials.Elementium),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gearSmall,CMMaterials.Elementium),4)
                .inputItems(CMBlocks.ELEMENTAL_FRAME.asStack())
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
                'C', CMBlocks.ELEMENTAL_FRAME.asStack()
        );
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("elementium_gear_box") //源质钢齿轮箱机械方块
                .inputItems(ChemicalHelper.get(TagPrefix.plate,CMMaterials.Elementium),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear,CMMaterials.Elementium),2)
                .inputItems(CMBlocks.ELEMENTAL_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.ELEMENTAL_CASING_GEARBOX.asItem(),2) //源质钢齿轮箱机械方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("manasteel_gear_box") //魔力钢齿轮箱方块
                .inputItems(ChemicalHelper.get(TagPrefix.plate,CMMaterials.ManaSteel),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear,CMMaterials.ManaSteel),2)
                .inputItems(CMBlocks.MANA_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.CASING_MANASTEEL_GEARBOX.asItem(),2) //魔力钢齿轮箱方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("orichalcos_gear_box") //奥利哈钢齿轮箱机械方块
                .inputItems(ChemicalHelper.get(TagPrefix.plate,CMMaterials.Orichalcos),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear,CMMaterials.Orichalcos),2)
                .inputItems(CMBlocks.ORICHALCOS_FRAME.asStack())
                .EUt(32)
                .duration(400)
                .circuitMeta(6)
                .outputItems(CMBlocks.Orichalcos.asItem(),2) //奥利哈钢齿轮箱机械方块
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("elf_steel_gear_box") //精灵钢齿轮箱机械方块
                .inputItems(ChemicalHelper.get(TagPrefix.plate,CMMaterials.AlfSteel),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear,CMMaterials.AlfSteel),2)
                .inputItems(CMBlocks.ELF_FRAME.asStack())
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


    }
}
