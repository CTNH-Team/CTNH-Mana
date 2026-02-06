package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.tool.GTToolItem;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTToolTiers;
import com.gregtechceu.gtceu.common.data.item.GTToolActions;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.moguang.ctnhmana.common.recipe.builder.botania.RuneRitualRecipeBuilder;
import com.moguang.ctnhmana.registry.CMItems;
import mythicbotany.data.recipes.extension.RuneRitualExtension;
import mythicbotany.kvasir.WanderingTraderRuneInput;
import mythicbotany.mjoellnir.ItemMjoellnir;
import mythicbotany.register.ModItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.recipe.RecipeProviderBase;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import static com.github.L_Ender.cataclysm.init.ModItems.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static mythicbotany.register.ModItems.*;
import java.util.function.Consumer;
import static mythicbotany.register.ModBlocks.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static net.minecraft.world.item.Items.*;
import static twilightforest.init.TFBlocks.*;
import static twilightforest.init.TFItems.*;
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
                .rune4(ChemicalHelper.get(ingot,YURIKO).getItem(), 3, 0,true)
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
                .rune4(runePride,4,4)
                .runez(runeAir, -2, 3)
                .runez(runeAir, -3, 2)
                .runez(nidavellirRune,2,0)
                .runez(runeEarth, -2, -3)
                .runez(runeEarth, -3, -2)
                .rune(asgardRune,0,2)
                .rune(joetunheimRune,0,-2)
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
                .rune4(SPEED_RUNE_2_ITEM.get(), 0, 5,true)
                .rune4(SPEED_RUNE_ITEM.get(), 3, 3,true)
                .rune4(SACRIFICE_RUNE_ITEM.get(), 0, 3,true)
                .rune4(SACRIFICE_RUNE_2_ITEM.get(), 4, 2,true)
                .rune4(ChemicalHelper.get(block,PRIMOVOLITHEST).getItem(), 2, 2,true)
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .output(TWIST_RUNE.asItem())
                .mana(500000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("sacrificial_dagger")
                .center(ChemicalHelper.get(gemChipped, GTMaterials.Glass).getItem())
                .rune(enderDagger,-1,2,true)
                .rune(WILDEN_SPIKE.get(), 3, 0,true)
                .rune(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:rose_gold_knife")), 1, 2,true)
                .rune(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:rose_gold_butchery_knives")), -1, -2,true)
                .rune(ATHAME.get(),1,-2,true)
                .rune(DIAMOND_SWORD,-3,0,true)
                .input(runeGreed)
                .input(runeWrath)
                .input(redString)
                .specialInput(WanderingTraderRuneInput.INSTANCE)
                .output(SACRIFICIAL_DAGGER.get())
                .mana(1000000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("mana_collector")
                .center(gaiaIngot.asItem())
                .rune4(REAGENT_MAGNETISM.get(), 4, 4,true)
                .rune4(dragonstone, 2, 2,true)
                .rune4(pixieDust, 2, 3,true)
                .rune4(pixieDust, 3, 2,true)
                .runex(lensLight,0,-5,true)
                .runez(spark,5,0,true)
                .rune4(BotaniaBlocks.dreamwoodLogGlimmering.asItem(),0,4,true)
                .rune4(BotaniaBlocks.dreamwoodLogGlimmering.asItem(),1,4,true)
                .rune4(BotaniaBlocks.dreamwoodLogGlimmering.asItem(),4,1,true)
                .input(niflheimRune)
                .input(alfheimRune)
                .input(vanaheimRune)
                .output(manaCollector.asItem())
                .mana(1000000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("horizon_rune")
                .center(midgardRune.asItem())
                .rune4(ChemicalHelper.get(ingot,Plus_Mana).getItem(), -2, -3)
                .rune4(QUANTUM_EYE.asItem(),0,4,true)
                .rune4(QUANTUM_EYE.asItem(),1,4,true)
                .rune4(QUANTUM_EYE.asItem(),4,1,true)
                .rune(muspelheimRune,-3,-2)
                .rune(muspelheimRune,-4,-2)
                .rune(runeLust,-3,2)
                .rune(runeLust,-4,2)
                .rune(runePride,3,2)
                .rune(runePride,4,2)
                .rune(niflheimRune,3,-2)
                .rune(niflheimRune,4,-2)
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .output(HORIZEN_RUNE.asItem())
                .mana(500000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("starlight_rune")
                .center(QUANTUM_STAR.asItem())
                .runex(QUANTUM_STAR.asItem(), 0, -4)
                .runez(QUANTUM_STAR.asItem(), -3, -0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")),-1,0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")),-5,0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:moon_cobblestone")),-2,0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:moon_cobblestone")),-4,0)
                .runez(ForgeRegistries.ITEMS.getValue(new ResourceLocation("deep_aether:sterling_aercloud")),1,-4)
                .runez(HOLLOW_OAK_SAPLING.get().asItem(), 1,4)
                .rune(HOLLOW_OAK_SAPLING.get().asItem(),0,3)
                .rune(HOLLOW_OAK_SAPLING.get().asItem(),0,5)
                .rune(ForgeRegistries.ITEMS.getValue(new ResourceLocation("deep_aether:sterling_aercloud")),0,-3)
                .rune(ForgeRegistries.ITEMS.getValue(new ResourceLocation("deep_aether:sterling_aercloud")),0,-5)
                .rune4(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")),-2,1)
                .rune4(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")),-3,2)
                .rune4(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_stone")),-4,1)
                .rune4(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:moon_cobblestone")),-3,1)
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .input(Zenith_essence.getBucket())
                .output(STARLIGHT_RUNE.asItem())
                .mana(500000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("kvasirblood")
                .center(fimbultyrTablet.asItem())
                .rune2(runeFire, 1, -3)
                .rune2(runeFire, 3, -1)
                .rune2(helheimRune, 2, -2)
                .rune2(runeSummer, 3, 1)
                .rune2(runeSummer, 1, 3)
                .rune2(midgardRune, 2, 2)
                .input(terraSword)
                .input(ChemicalHelper.get(nugget,AlfSteel).getItem())
                .input(vial)
                .specialInput(WanderingTraderRuneInput.INSTANCE)
                .output(kvasirBlood.asItem())
                .mana(10000)
                .save(provider);
        RuneRitualRecipeBuilder.builder("andwari_ring1")
                .center(runeMana.asItem())
                .runex(runeGreed, 0, -3)
                .rune4(runeGreed, 2, -2)
                .runez(alfheimRune, 3,0)
                .input(manaweaveCloth)
                .input(cursedAndwariRing)
                .output(andwariRing.asItem())
                .mana(0)
                .save(provider);
        RuneRitualRecipeBuilder.builder("andwari_ring2")
                .center(runeMana.asItem())
                .runex(runeGreed, 0, -3)
                .rune4(runeGreed, 2, -2)
                .runez(alfheimRune, 3, 0)
                .input(manaweaveCloth)
                .input(andwariRing)
                .output(andwariRing.asItem())
                .mana(0)
                .save(provider);
    }

}
