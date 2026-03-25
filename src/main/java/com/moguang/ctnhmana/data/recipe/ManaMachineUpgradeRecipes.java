package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GCYMMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.builder.bloodmagic.BloodAltarRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.RuneAltarRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.TerraPlateRecipeBuilder;
import com.moguang.ctnhmana.registry.*;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.register.ModItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot;

public class ManaMachineUpgradeRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        TerraPlateRecipeBuilder.builder("bt_update_t1")
                .input(CMItems.MAGIC_CORE.asStack())
                .input(BotaniaItems.manaDiamond)
                .input(BotaniaItems.manaDiamond)
                .input(ItemTags.FLOWERS)
                .input(BotaniaTags.Items.MYSTICAL_FLOWERS)
                .input(CustomTags.LV_CIRCUITS)
                .output(CMItems.SKY_FLOWER_SPEECH.asStack())
                .mana(100000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("bt_update_t2")
                .input(CMItems.SKY_FLOWER_SPEECH.asStack())
                .input(ModItems.alfheimRune)
                .input(ModItems.alfsteelIngot)
                .input(ModItems.alfsteelTemplate)
                .input(CustomTags.EV_CIRCUITS)
                .output(CMItems.CLEAR_SKY_FLOWER_WISH.asStack())
                .mana(500000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("bt_update_t3")
                .input(CMItems.CLEAR_SKY_FLOWER_WISH.asStack())
                .input(ExtraBotanyItems.orichalcos)
                .input(ExtraBotanyItems.dasRheingold)
                .input(ExtraBotanyItems.theOrigin)
                .input(CustomTags.ZPM_CIRCUITS)
                .output(CMItems.AZURE_SKY_FLOWER_DANCE.asStack())
                .mana(2000000)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bm_update_t1")
                .input(CMItems.MAGIC_CORE.asStack())
                .circuitMeta(11)
                .output(CMItems.TWISTED_BLOOD_FORGING.asStack())
                .minimumTier(1)
                .syphon(1000000)
                .drainRate(100)
                .consumeRate(100)
                .save(provider);
        CMRecipeTypes.HELL_FORGE_RECIPES.recipeBuilder("bm_update_t2")
                .inputItems(CMItems.TWISTED_BLOOD_FORGING.asStack())
                .inputItems(CustomTags.LuV_CIRCUITS)
                .inputItems(CMItems.ENDSLATE)
                .inputItems(CMItems.WILL_RESISTOR)
                .inputItems(ChemicalHelper.get(ingot, CMMaterials.PRIMOVOLITHEST))
                .inputItems(BloodMagicItems.REAGENT_SUPPRESSION)
                .outputItems(CMItems.TWISTED_SOUL_FORGING)
                .addCondition(new HellForgeCondition(42))
                .EUt(8192)
                .duration(44 * 20)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("gt_update_t1")
                .inputItems(CMItems.MAGIC_CORE.asStack())
                .inputItems(GTItems.ELECTRIC_PUMP_LV)
                .inputItems(GTItems.ELECTRIC_PUMP_MV)
                .inputItems(GTItems.ELECTRIC_PUMP_HV)
                .inputItems(CustomTags.HV_CIRCUITS)
                .inputItems(BotaniaItems.thirdEye)
                .inputFluids(CMMaterials.Mana.getFluid(1000))
                .outputItems(CMItems.PIPELINE_VISION)
                .EUt(GTValues.VA[GTValues.HV])
                .duration(10 * 20)
                .save(provider);
        GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder("gt_update_t2")
                .inputItems(CMItems.PIPELINE_VISION)
                .inputItems(GTItems.ELECTRIC_PUMP_HV)
                .inputItems(GTItems.ELECTRIC_PUMP_EV)
                .inputItems(GTItems.ELECTRIC_PUMP_IV)
                .inputItems(CMBlocks.AURA_CONVERGENCE_CASING.get().asItem())
                .inputItems(GCYMMachines.PARALLEL_HATCH[5].asStack())
                .inputItems(CustomTags.ZPM_CIRCUITS)
                .inputFluids(CMMaterials.Zenith_essence.getFluid(1000))
                .outputItems(CMItems.PIPELINE_FARSIGHT)
                .EUt(GTValues.VA[GTValues.IV])
                .duration(10 * 20)
                .save(provider);
        VanillaRecipeHelper.addShapedRecipe(
                provider, "range_1", new ItemStack(CMItems.UPGRADE_RUNE_RANGE_1.get(), 4),
                "AAA",
                "CBC",
                "ACA",
                'A', BotaniaItems.spark.asItem(),
                'B', CMItems.MAGIC_CORE.asStack().getItem(),
                'C', BotaniaItems.manaSteel.asItem());

        VanillaRecipeHelper.addShapedRecipe(
                provider, "range_2", new ItemStack(CMItems.UPGRADE_RUNE_RANGE_2.get(), 2),
                "AAA",
                "CBC",
                "ACA",
                'A', CMItems.UPGRADE_RUNE_RANGE_2.asItem(),
                'B', CMItems.MAGIC_CORE.asStack().getItem(),
                'C', ModItems.alfsteelIngot.asItem());
        VanillaRecipeHelper.addShapedRecipe(
                provider, "range_3", new ItemStack(CMItems.UPGRADE_RUNE_RANGE_3.get(), 1),
                "AAA",
                "CBC",
                "ACA",
                'A', CMItems.UPGRADE_RUNE_RANGE_2.asItem(),
                'B', CMItems.MAGIC_CORE.asStack().getItem(),
                'C', ExtraBotanyItems.orichalcos.asItem());
        VanillaRecipeHelper.addShapedRecipe(
                provider, "speed_1", new ItemStack(CMItems.UPGRADE_RUNE_SPEED_1.get(), 4),
                "AAA",
                "CBC",
                "ACA",
                'A', BotaniaItems.runeFire.asItem(),
                'B', CMItems.MAGIC_CORE.asStack().getItem(),
                'C', BotaniaItems.manaSteel.asItem());
        VanillaRecipeHelper.addShapedRecipe(
                provider, "speed_2", new ItemStack(CMItems.UPGRADE_RUNE_SPEED_2.get(), 2),
                "AAA",
                "CBC",
                "ACA",
                'A', CMItems.UPGRADE_RUNE_SPEED_1.get(),
                'B', CMItems.MAGIC_CORE.asStack().getItem(),
                'C', ModItems.alfsteelIngot.asItem());
        VanillaRecipeHelper.addShapedRecipe(
                provider, "speed_3", new ItemStack(CMItems.UPGRADE_RUNE_SPEED_3.get(), 1),
                "AAA",
                "CBC",
                "ACA",
                'A', CMItems.UPGRADE_RUNE_SPEED_2.get(),
                'B', CustomTags.IV_CIRCUITS,
                'C', ExtraBotanyItems.orichalcos.asItem());
        VanillaRecipeHelper.addShapedRecipe(
                provider, "capacity_1", new ItemStack(CMItems.UPGRADE_RUNE_TRANSLOCATION_1.get(), 1),
                "AAA",
                "CBC",
                "DCD",
                'A', BotaniaBlocks.fabulousPool.asItem(),
                'B', CMItems.MAGIC_CORE.asStack().getItem(),
                'C', BotaniaItems.manaSteel.asItem(),
                'D', CMBlocks.LIVING_ROCK_CASING.get().asItem());
        VanillaRecipeHelper.addShapedRecipe(
                provider, "capacity_2", new ItemStack(CMItems.UPGRADE_RUNE_TRANSLOCATION_2.get(), 1),
                "AAA",
                "CBC",
                "DCD",
                'A', CMItems.UPGRADE_RUNE_TRANSLOCATION_1.get(),
                'B', CMItems.MAGIC_CORE.asStack().getItem(),
                'C', ModItems.alfsteelIngot.asItem(),
                'D', CMBlocks.PURE_LOGIC_CASING.get().asItem());
        VanillaRecipeHelper.addShapedRecipe(
                provider, "capacity_3", new ItemStack(CMItems.UPGRADE_RUNE_TRANSLOCATION_3.get(), 1),
                "AAA",
                "CBC",
                "DCD",
                'A', CMItems.UPGRADE_RUNE_TRANSLOCATION_2.get(),
                'B', CustomTags.IV_CIRCUITS,
                'C', ExtraBotanyItems.orichalcos.asItem(),
                'D', CMBlocks.PURE_MAGIC_CALCULATE_CORE.get().asItem());
        RuneAltarRecipeBuilder.builder("alpha_upgrade")
                .input(CMItems.UPGRADE_RUNE_RANGE_3.asItem())
                .input(CMItems.UPGRADE_RUNE_TRANSLOCATION_3.asStack())
                .input(CMItems.UPGRADE_RUNE_SPEED_3.asStack())
                .input(CMMultiblockMachines.MysticSpire.asStack())
                .input(CMItems.BROKEN_RUNE.asStack())
                .output(CMItems.UPGRADE_RUNE_ALPHA.asStack())
                .mana(7777777)
                .save(provider);
    }
}
