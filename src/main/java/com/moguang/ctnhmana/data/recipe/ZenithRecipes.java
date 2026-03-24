package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GCYMMachines;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.moguang.ctnhmana.common.recipe.ZenithCondition;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import com.moguang.ctnhmana.registry.multiblock.ZenithMachine;
import mythicbotany.register.ModItems;
import net.minecraft.data.recipes.FinishedRecipe;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

public class ZenithRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        CMRecipeTypes.ZENITH_CIRCUIT.recipeBuilder("zenith_soc_a")
                .inputItems(CMItems.MANA_WAFER,7)
                .inputItems(CMItems.WILL_WAFER,7)
                .inputItems(CMItems.BLOODY_WAFER,7)
                .inputItems(CMItems.UMLHPIC_WAFER,7)
                .inputItems(CMItems.HORIZEN_RUNE,7)
                .inputItems(CMItems.ORICHALCOS_SPIRIT,7)
                .inputFluids(CMMaterials.Shroud_Zenith_essence,777)
                .EUt(8192)
                .duration(777)
                .outputItems(CMItems.ZENITH_WAFER,7)
                .save(provider);
        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder("zenith_soc_a_1")
                .inputItems(CMItems.ZENITH_WAFER)
                .outputItems(CMItems.ZENITH_SOC,7)
                .inputFluids(CMMaterials.Zenith_essence,100)
                .EUt(8192)
                .duration(1000)
                .save(provider);
        CMRecipeTypes.ZENITH_CIRCUIT.recipeBuilder("zenith_soc_b")
                .inputItems(CMItems.ZENITH_WAFER)
                .inputItems(ChemicalHelper.get(TagPrefix.ingot,CMMaterials.Ultra_Mana),7)
                .inputFluids(CMMaterials.Mana_Radiation_Mixture,777)
                .inputFluids(CMMaterials.Shroud_Zenith_essence,777)
                .addCondition(new ZenithCondition(true))
                .EUt(7777)
                .duration(777)
                .outputItems(CMItems.ZENITH_WAFER,7)
                .save(provider);
        CMRecipeTypes.ZENITH_CIRCUIT.recipeBuilder("mana_hv")
                .inputItems(CMItems.ZENITH_SOC,1)
                .inputItems(ChemicalHelper.get(TagPrefix.screw,CMMaterials.ManaSteel),7)
                .inputItems(BotaniaItems.runeMana,7)
                .inputFluids(CMMaterials.Zenith_essence,42)
                .inputFluids(CMMaterials.TerraSteel,777)
                .outputItems(CMItems.MANA_ELECTRONIC_CIRCUIT,7)
                .EUt(7777)
                .duration(77)
                .save(provider);
        CMRecipeTypes.ZENITH_CIRCUIT.recipeBuilder("mana_ev")
                .inputItems(CMItems.ZENITH_SOC,1)
                .inputItems(ChemicalHelper.get(TagPrefix.screw,CMMaterials.TerraSteel),7)
                .inputItems(ModItems.niflheimRune,7)
                .inputFluids(CMMaterials.Zenith_essence,42)
                .inputFluids(CMMaterials.AlfSteel,77)
                .outputItems(CMItems.MANA_INTEGRATED_CIRCUIT,7)
                .EUt(7777)
                .duration(77)
                .save(provider);
        CMRecipeTypes.ZENITH_CIRCUIT.recipeBuilder("mana_iv")
                .inputItems(CMItems.ZENITH_SOC,7)
                .inputItems(ChemicalHelper.get(TagPrefix.screw,CMMaterials.COAGULBLOODGOLD),7)
                .inputItems(BloodMagicItems.RAW_CRYSTAL,7)
                .inputItems(CMItems.ENDSLATE,1)
                .inputFluids(CMMaterials.Zenith_essence,42)
                .inputFluids(CMMaterials.HEMOPLATINUM,777)
                .outputItems(CMItems.WILL_CRYSTAL_PROCESSOR,42)
                .EUt(7777)
                .duration(77)
                .save(provider);
        CMRecipeTypes.ZENITH_CIRCUIT.recipeBuilder("mana_luv")
                .inputItems(CMItems.ZENITH_SOC,7)
                .inputItems(ChemicalHelper.get(TagPrefix.screw,CMMaterials.COAGULBLOODGOLD),7)
                .inputItems(BloodMagicItems.BLANK_RUNE_ITEM,7)
                .inputFluids(CMMaterials.Zenith_essence,420)
                .inputFluids(CMMaterials.HEMOPLATINUM,777)
                .outputItems(CMItems.BLOODY_NANO_PROCESSOR_MAINFRAME,7)
                .EUt(7777)
                .duration(777)
                .save(provider);
        CMRecipeTypes.ZENITH_CIRCUIT.recipeBuilder("mana_zpm")
                .inputItems(CMItems.ZENITH_SOC,7)
                .inputItems(ChemicalHelper.get(TagPrefix.screw,CMMaterials.HEMOPLATINUM),7)
                .inputItems(BloodMagicItems.RAW_CRYSTAL,7)
                .inputItems(CMItems.ENDSLATE,7)
                .inputFluids(CMMaterials.Zenith_essence,420)
                .inputFluids(CMMaterials.PRIMOVOLITHEST,777)
                .outputItems(CMItems.MIXIN_WILL_PROCESSOR_MAINFRAME,7)
                .EUt(7777)
                .duration(777)
                .save(provider);
        GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder("zenith_dis")//天顶蒸馏
                .inputItems(CMBlocks.ZENITH_CASING_BLOCK.get(),7)
                .inputItems(GTMultiMachines.DISTILLATION_TOWER.asStack(),7)
                .inputItems(CMItems.PIPELINE_FARSIGHT)
                .inputItems(ChemicalHelper.get(TagPrefix.screw,CMMaterials.Aerialite),7)
                .inputItems(CMItems.HORIZEN_RUNE,7)
                .inputItems(CustomTags.ZPM_CIRCUITS,7)
                .inputFluids(CMMaterials.Shroud_Zenith_essence,777)
                .inputFluids(CMMaterials.Zenith_essence,777)
                .inputFluids(CMMaterials.MANA_STABLE_COOLDOWN,777)
                .inputFluids(CMMaterials.AlfSteel,777)
                .outputItems(ZenithMachine.ZENITH_DISTILLATION.asStack())
                .EUt(7777)
                .duration(777)
                .save(provider);
        GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder("zenith_ass")//天顶电组
                .inputItems(CMBlocks.ZENITH_CASING_BLOCK.get(),7)
                .inputItems(GCYMMachines.LARGE_CIRCUIT_ASSEMBLER.asStack(),7)
                .inputItems(CMItems.AZURE_SKY_FLOWER_DANCE)
                .inputItems(ChemicalHelper.get(TagPrefix.screw,CMMaterials.DEMON),7)
                .inputItems(CMItems.STARLIGHT_RUNE,7)
                .inputItems(CustomTags.ZPM_CIRCUITS,7)
                .inputItems(CustomTags.LuV_CIRCUITS,7)
                .inputItems(CMItems.UMLHPIC_WAFER,7)
                .inputFluids(CMMaterials.Shroud_Zenith_essence,777)
                .inputFluids(CMMaterials.Zenith_essence,777)
                .inputFluids(CMMaterials.MANA_STABLE_COOLDOWN,777)
                .inputFluids(CMMaterials.AlfSteel,777)
                .outputItems(ZenithMachine.ZENITH_CIRCUIT_ASSEMBLER.asStack())
                .EUt(7777)
                .duration(7777)
                .save(provider);


    }
}