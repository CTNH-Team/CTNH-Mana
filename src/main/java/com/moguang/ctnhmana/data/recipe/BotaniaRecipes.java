package com.moguang.ctnhmana.data.recipe;


import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.moguang.ctnhmana.common.recipe.builder.botania.ManaInfusionRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.PetalRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.RuneAltarRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.TerraPlateRecipeBuilder;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.data.recipe.utils.BotaniaIngredients.*;
import static mythicbotany.register.ModItems.helheimRune;


public class BotaniaRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        PetalRecipeBuilder.builder("demon_flytrap")
                .input(LIGHTBLUE, LIGHTBLUE, GREEN, GREEN, BROWN)
                .input(runeGreed, runeEnvy, gaiaSpirit)
                .output(CMBlocks.DEMON_FLYTRAP.asStack())
                .reagent(Items.GRASS.getDefaultInstance())
                .save(provider);
        PetalRecipeBuilder.builder("blood_antiaris")
                .input(RED, RED, GREEN, GRAY)
                .input(runeSloth, runeFire, runeWrath, gaiaSpirit)
                .output(new ItemStack(BotaniaFlowerBlocks.pureDaisy.asItem(),1))
                .reagent(Items.GRASS.getDefaultInstance())
                .save(provider);
        ManaInfusionRecipeBuilder.builder("test")
                .input(runeSloth)
                .output(new ItemStack(BotaniaItems.spark))
                .mana(3000)
                .save(provider);
        RuneAltarRecipeBuilder.builder("testx")
                .input(runeSloth,runeAir,runeGreed)
                .output(new ItemStack(BotaniaItems.runeEarth,1))
                .mana(99999)
                .save(provider);
        TerraPlateRecipeBuilder.builder("koishi_pain")
                .input(runeLust)
                .input(helheimRune,BotaniaItems.redString,BotaniaItems.thirdEye,BotaniaFlowerBlocks.rosaArcana.asItem(), BloodMagicItems.LIFE_ESSENCE_BUCKET.get())
                .input(ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Stone,1))
                .input(CustomTags.KNIVES)
                .output(new ItemStack(CMItems.KOISHI_EYE,1))
                .mana(5145140)
                .circuitMeta(10)
                .save(provider);

    }
}
