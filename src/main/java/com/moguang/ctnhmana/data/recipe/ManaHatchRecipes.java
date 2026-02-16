package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMachines;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.multiblock.Botania;
import mythicbotany.register.ModItems;
import net.minecraft.data.recipes.FinishedRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

public class ManaHatchRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("base_mana_hatch")//魔力凝聚者
                .inputItems(CMBlocks.LIVING_ROCK_CASING)
                .inputItems(CMBlocks.MANA_STEEL_CASING.asStack())
                .inputItems(BotaniaBlocks.manaPool.asItem())
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.ManaSteel),4)
                .inputItems(GTMachines.FLUID_IMPORT_HATCH[2].asStack())
                .inputItems(BotaniaItems.runeMana)
                .outputItems(CMMachines.MANA_HATCH.asStack())
                .EUt(32)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("elf_mana_hatch")//精灵魔力凝聚者
                .inputItems(CMBlocks.ELEMENTIUM_CASING.asStack())
                .inputItems(BotaniaBlocks.fabulousPool.asItem())
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.Elementium),4)
                .inputItems(GTMachines.FLUID_IMPORT_HATCH[2].asStack())
                .inputItems(BotaniaItems.spark)
                .inputItems(ModItems.alfheimRune)
                .outputItems(CMMachines.ADVANCED_MANA_HATCH.asStack())
                .EUt(120)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("giga_mana_hatch")//千兆魔力凝聚者
                .inputItems(CMBlocks.TERRA_STEEL_CASING.asStack())
                .inputItems(BotaniaBlocks.fabulousPool.asItem())
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.TerraSteel),4)
                .inputItems(GTMachines.FLUID_IMPORT_HATCH[4].asStack())
                .inputItems(BotaniaItems.corporeaSpark)
                .inputItems(ModItems.vanaheimRune)
                .inputItems(ModItems.asgardRune)
                .outputItems(CMMachines.GIGA_MANA_HATCH.asStack())
                .EUt(320)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("sky_mana_hatch")//天际魔力凝聚者
                .inputItems(CMBlocks.ELF_STEEL_CASING_GEARBOX.asStack())
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.Orichalcos),4)
                .inputItems(GTMachines.FLUID_IMPORT_HATCH[5].asStack())
                .inputItems(BotaniaItems.corporeaSpark)
                .inputItems(CMItems.STARLIGHT_RUNE)
                .inputItems(CustomTags.IV_CIRCUITS,2)
                .inputFluids(CMMaterials.Zenith_essence.getFluid(144))
                .outputItems(CMMachines.SKY_MANA_HATCH.asStack())
                .EUt(8192)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("chemical_mana_hatch")//工业魔力凝聚者
                .inputItems(CMBlocks.PURE_MAGIC_CALCULATE_CORE.asStack())
                .inputItems(CMBlocks.PURE_LOGIC_CASING.asStack())
                .inputItems(ChemicalHelper.get(TagPrefix.gear, GTMaterials.TungstenSteel),4)
                .inputItems(GTAEMachines.FLUID_IMPORT_HATCH_ME.asStack())
                .inputItems(BotaniaItems.corporeaSpark)
                .inputItems(CustomTags.IV_CIRCUITS,2)
                .inputFluids(GTMaterials.Polyethylene.getFluid(144))
                .outputItems(CMMachines.INDUSTRY_MANA_HATCH.asStack())
                .EUt(8192/4)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("bloody_mana_hatch")//凝血魔力凝聚者
                .inputItems(CMBlocks.CASING_BLOOD.asStack())
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.COAGULBLOODGOLD),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.HEMOPLATINUM),4)
                .inputItems(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())
                .outputItems(CMMachines.BM_HATCH.asStack())
                .EUt(8192/4/4)
                .duration(200)
                .save(provider);
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder("soul_mana_hatch")//灵魂魔力凝聚者
                .inputItems(CMMachines.BM_HATCH.asStack())
                .inputItems(CMBlocks.SOUL_LOCKING_CASING.asStack())
                .inputItems(CMBlocks.CASING_BLOODLOGIC.asStack())
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.COAGULBLOODGOLD),4)
                .inputItems(ChemicalHelper.get(TagPrefix.gear, CMMaterials.PRIMOVOLITHEST),4)
                .inputItems(BloodMagicItems.MASTER_BLOOD_ORB.get())
                .inputItems(CMItems.ENDSLATE)
                .outputItems(CMMachines.BM_HATCH_T2.asStack())
                .EUt(8192/4)
                .duration(200)
                .save(provider);

    }
}