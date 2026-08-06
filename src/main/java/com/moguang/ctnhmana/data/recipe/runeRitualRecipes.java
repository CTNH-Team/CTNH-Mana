package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.data.recipe.builder.botania.RuneRitualRecipeBuilder;
import com.moguang.ctnhmana.registry.CMBlocks;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.kvasir.WanderingTraderRuneInput;
import vazkii.botania.common.block.BotaniaBlocks;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

import java.util.function.Consumer;

import static com.github.L_Ender.cataclysm.init.ModItems.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static mythicbotany.register.ModBlocks.*;
import static mythicbotany.register.ModItems.*;
import static net.minecraft.world.item.Items.*;
import static twilightforest.init.TFBlocks.*;
import static vazkii.botania.common.block.BotaniaFlowerBlocks.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;

@SuppressWarnings("removal")
public class runeRitualRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        RuneRitualRecipeBuilder.builder("yuriko")
                .center(pureDaisy.asItem())
                .rune2(runeSummer, 2, 2)
                .rune2(runeSummer, 3, 1)
                .rune2(runeSummer, 1, -3)
                .rune2(runeAutumn, 3, -1)
                .rune2(runeAutumn, 1, 3)
                .rune2(runeAutumn, 2, -2)
                .rune4(ChemicalHelper.get(ingot, YURIKO).getItem(), 3, 0, true)
                .input(terraSword)
                .input(manaDiamond)
                .input(whitePetal)
                .specialInput(WanderingTraderRuneInput.INSTANCE)
                .output(YURIKO_RING.asItem())
                .mana(79631)
                .save(provider);
        RuneRitualRecipeBuilder.builder("mjoellnir")
                .center(fimbultyrTablet)
                .rune4(runeWrath, 0, 5)
                .rune4(runePride, 4, 4)
                .runez(runeAir, -2, 3)
                .runez(runeAir, -3, 2)
                .runez(nidavellirRune, 2, 0)
                .runez(runeEarth, -2, -3)
                .runez(runeEarth, -3, -2)
                .rune(asgardRune, 0, 2)
                .rune(joetunheimRune, 0, -2)
                .input(GOLD_INGOT)
                .input(POLISHED_ANDESITE)
                .input(goldenSeeds)
                .input(redString)
                .input(tinyPlanet)
                .output(mjoellnir.asItem())
                .mana(500000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("twist_rune")
                .center(ENDSLATE.asItem())
                .rune4(SPEED_RUNE_2_ITEM.get(), 0, 5, true)
                .rune4(SPEED_RUNE_ITEM.get(), 3, 3, true)
                .rune4(SACRIFICE_RUNE_ITEM.get(), 0, 3, true)
                .rune4(SACRIFICE_RUNE_2_ITEM.get(), 4, 2, true)
                .rune4(ChemicalHelper.get(block, PRIMOVOLITHEST).getItem(), 2, 2, true)
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .output(TWIST_RUNE.asItem())
                .mana(500000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("sacrificial_dagger")
                .center(ChemicalHelper.get(gemChipped, GTMaterials.Glass).getItem())
                .rune(enderDagger, -1, 2, true)
                .rune(WILDEN_SPIKE.get(), 3, 0, true)
                .rune(ToolHelper.get(GTToolType.KNIFE, GTMaterials.RoseGold).getItem(), 1, 2, true)
                .rune(ToolHelper.get(GTToolType.KNIFE, GTMaterials.RoseGold).getItem(), -1, -2, true)
                .rune(ATHAME.get(), 1, -2, true)
                .rune(DIAMOND_SWORD, -3, 0, true)
                .input(runeGreed)
                .input(runeWrath)
                .input(redString)
                .specialInput(WanderingTraderRuneInput.INSTANCE)
                .output(SACRIFICIAL_DAGGER.get())
                .mana(1000000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("altar")
                .center(thermalily.asItem())
                .rune(runeGreed, -3, 1, true)
                .rune(runeEnvy, -2, 2, true)
                .rune(runeLust, -1, 1, true)
                .rune(ChemicalHelper.get(block, GTMaterials.Redstone).getItem(), 2, 1, true)
                .rune(runeWrath, 2, 2, true)
                .rune(ChemicalHelper.get(block, GTMaterials.Redstone).getItem(), -2, 1, true)
                .rune(runeSloth, 1, 1, true)
                .rune(runeGluttony, -2, 0, true)
                .rune(runeMana, 3, 1, true)
                .rune(runePride, 2, 0, true)
                .rune(ChemicalHelper.get(plate, GTMaterials.BlackSteel).getItem(), -1, -1, true)
                .rune(ChemicalHelper.get(plate, GTMaterials.RedSteel).getItem(), 0, -1, true)
                .rune(ChemicalHelper.get(plate, GTMaterials.BlackSteel).getItem(), 1, -1, true)
                .rune(ChemicalHelper.get(plateDense, GTMaterials.Obsidian).getItem(), -2, -2, true)
                .rune(ChemicalHelper.get(plateDense, GTMaterials.Obsidian).getItem(), 0, -2, true)
                .rune(ChemicalHelper.get(plateDense, GTMaterials.Obsidian).getItem(), 1, -2, true)
                .rune(ChemicalHelper.get(plateDense, GTMaterials.Obsidian).getItem(), 2, -2, true)
                .rune(ChemicalHelper.get(plateDense, GTMaterials.Obsidian).getItem(), -1, -2, true)
                .input(redString)
                .input(superLavaPendant)
                .input(ExtraBotanyItems.shadowium)
                .output(BloodMagicBlocks.BLOOD_ALTAR.get().asItem())
                .mana(666666)
                .save(provider);
        RuneRitualRecipeBuilder.builder("mana_collector")
                .center(ExtraBotanyItems.orichalcos.asItem())
                .rune4(REAGENT_MAGNETISM.get(), 4, 4, true)
                .rune4(MAGIC_CORE.asItem(), 2, 2, true)
                .rune4(pixieDust, 2, 3, true)
                .rune4(pixieDust, 3, 2, true)
                .runex(lensLight, 0, -5, true)
                .runez(spark, 5, 0, true)
                .rune4(BotaniaBlocks.dreamwoodLogGlimmering.asItem(), 0, 4, true)
                .rune4(BotaniaBlocks.dreamwoodLogGlimmering.asItem(), 1, 4, true)
                .rune4(BotaniaBlocks.dreamwoodLogGlimmering.asItem(), 4, 1, true)
                .input(niflheimRune)
                .input(alfheimRune)
                .input(vanaheimRune)
                .output(manaCollector.asItem())
                .mana(1000000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("horizon_rune")
                .center(BROKEN_RUNE.asItem())
                .rune4(ChemicalHelper.get(ingot, Plus_Mana).getItem(), -2, -3)
                .rune4(QUANTUM_EYE.asItem(), 0, 4, true)
                .rune4(QUANTUM_EYE.asItem(), 1, 4, true)
                .rune4(QUANTUM_EYE.asItem(), 4, 1, true)
                .rune(muspelheimRune, -3, -2)
                .rune(muspelheimRune, -4, -2)
                .rune(runeLust, -3, 2)
                .rune(runeLust, -4, 2)
                .rune(runePride, 3, 2)
                .rune(runePride, 4, 2)
                .rune(niflheimRune, 3, -2)
                .rune(niflheimRune, 4, -2)
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .output(HORIZEN_RUNE.asItem())
                .mana(500000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("starlight_rune")
                .center(BROKEN_RUNE.asItem())
                .runex(QUANTUM_STAR.asItem(), 0, -4)
                .runez(QUANTUM_STAR.asItem(), -3, -0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")), -1, 0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")), -5, 0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:moon_cobblestone")), -2, 0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:moon_cobblestone")), -4, 0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("deep_aether:sterling_aercloud")), 1, -4)
                .runez(HOLLOW_OAK_SAPLING.get().asItem(), 1, 4)
                .rune(HOLLOW_OAK_SAPLING.get().asItem(), 0, 3)
                .rune(HOLLOW_OAK_SAPLING.get().asItem(), 0, 5)
                .rune(ForgeRegistries.ITEMS.getValue(new ResourceLocation("deep_aether:sterling_aercloud")), 0, -3)
                .rune(ForgeRegistries.ITEMS.getValue(new ResourceLocation("deep_aether:sterling_aercloud")), 0, -5)
                .rune4(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")), -2, 1)
                .rune4(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")), -3, 2)
                .rune4(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")), -4, 1)
                .rune4(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:moon_cobblestone")), -3, 1)
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .output(STARLIGHT_RUNE.asItem())
                .mana(500000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("starlight_rune_from_proliferation")
                .center(PROLIFERATION_RUNE.asItem())
                .rune(NETHER_STAR.asItem(), 0, -1, true)
                .rune(NETHER_STAR.asItem(), 1, 0, true)
                .rune(NETHER_STAR.asItem(), 0, 1, true)
                .rune(NETHER_STAR.asItem(), -1, 0, true)
                .input(STARLIGHT_RUNE.asItem())
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .output(new ItemStack(STARLIGHT_RUNE.asItem(), 2))
                .mana(250000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("twist_rune_from_proliferation")
                .center(PROLIFERATION_RUNE.asItem())
                .rune(STEADFAST_CORE.get(), 0, -1, true)
                .rune(VENGEFUL_CORE.get(), 1, 0, true)
                .rune(CORROSIVE_CORE.get(), 0, 1, true)
                .rune(DESTRUCTIVE_CORE.get(), -1, 0, true)
                .input(TWIST_RUNE.asItem())
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .output(new ItemStack(TWIST_RUNE.asItem(), 2))
                .mana(250000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("horizen_rune_from_proliferation")
                .center(PROLIFERATION_RUNE.asItem())
                .rune(QUANTUM_EYE.asItem(), 0, -1, true)
                .rune(QUANTUM_EYE.asItem(), 1, 0, true)
                .rune(QUANTUM_EYE.asItem(), 0, 1, true)
                .rune(QUANTUM_EYE.asItem(), -1, 0, true)
                .input(HORIZEN_RUNE.asItem())
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .output(new ItemStack(HORIZEN_RUNE.asItem(), 2))
                .mana(250000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("kvasirblood")// 克瓦希尔之血
                .center(fimbultyrTablet.asItem())
                .rune2(runeFire, 1, -3)
                .rune2(runeFire, 3, -1)
                .rune2(helheimRune, 2, -2)
                .rune2(runeSummer, 3, 1)
                .rune2(runeSummer, 1, 3)
                .rune2(midgardRune, 2, 2)
                .input(terraSword)
                .input(ChemicalHelper.get(nugget, AlfSteel).getItem())
                .input(vial)
                .specialInput(WanderingTraderRuneInput.INSTANCE)
                .output(kvasirBlood.asItem())
                .mana(10000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("andwari_ring1")// 安瓦尔之戒
                .center(runeMana.asItem())
                .runex(runeGreed, 0, -3)
                .rune4(runeGreed, 2, -2)
                .runez(alfheimRune, 3, 0)
                .input(manaweaveCloth)
                .input(cursedAndwariRing)
                .output(andwariRing.asItem())
                .mana(0)
                .save(provider);
        RuneRitualRecipeBuilder.builder("andwari_ring2")// 安瓦尔之戒
                .center(runeMana.asItem())
                .runex(runeGreed, 0, -3)
                .rune4(runeGreed, 2, -2)
                .runez(alfheimRune, 3, 0)
                .input(manaweaveCloth)
                .input(andwariRing)
                .output(andwariRing.asItem())
                .mana(0)
                .save(provider);
        RuneRitualRecipeBuilder.builder("brunmeflower")
                .center(ORICHALCOS_SPIRIT.asItem())
                .rune2(FLAME_EYE.get(), -1, 1)
                .rune2(endoflame.asItem(), 2, 0)
                .rune(endoflame.asItem(), 0, -2)
                .rune(endoflame.asItem(), 0, -3)
                .rune(endoflame.asItem(), 0, -4)
                .rune2(thermalily.asItem(), 3, 1)
                .rune2(thermalily.asItem(), 4, 2)
                .rune2(mythicbotany.register.ModItems.muspelheimRune.asItem(), 2, 1)
                .rune2(mythicbotany.register.ModItems.muspelheimRune.asItem(), 3, 2)
                .rune2(exoblaze.asItem(), 4, 3)
                .rune2(runeFire, 1, -2)
                .rune2(runeFire, 2, -2)
                .rune2(runeFire, 3, -2)
                .rune2(runeFire, 4, -2)
                .input(Tags.Items.SEEDS)
                .output(CMBlocks.ANATTA_LOTUS.asStack())
                .mana(3000)
                .save(provider);
    }
}
