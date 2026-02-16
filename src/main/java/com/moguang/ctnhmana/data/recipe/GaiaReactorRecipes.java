package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.moguang.ctnhmana.data.recipe.utils.BotaniaIngredients;
import net.minecraft.data.recipes.FinishedRecipe;
import static com.moguang.ctnhmana.data.recipe.utils.BotaniaIngredients.gaiaSpirit;
import java.util.function.Consumer;

import net.minecraft.world.item.Item;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.item.material.RuneItem;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import static mythicbotany.register.ModItems.*;
public class GaiaReactorRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        GAIA_REACTOR_RECIPES.recipeBuilder("gaia_1")
                .inputItems(terrasteel)
                .outputFluids(Mana.getFluid(1000))
                .outputItems(lifeEssence,4)
                .outputItems(manaDiamond,5)
                .outputItems(manaPearl,5)
                .outputItems(ChemicalHelper.get(ingot,ManaSteel),10)
                .chancedOutput(runeAir.getDefaultInstance(),(int)2500f,1)
                .chancedOutput(runeEarth.getDefaultInstance(),(int)2500f,1)
                .chancedOutput(runeFire.getDefaultInstance(),(int)2500f,1)
                .chancedOutput(runeWater.getDefaultInstance(),(int)2500f,1)
                .chancedOutput(runeSpring.getDefaultInstance(),(int)1000f,1)
                .chancedOutput(runeSummer.getDefaultInstance(),(int)1000f,1)
                .chancedOutput(runeAutumn.getDefaultInstance(),(int)1000f,1)
                .chancedOutput(runeWinter.getDefaultInstance(),(int)1000f,1)
                .EUt(192000/400)
                .duration(400)
                .save(provider);
        GAIA_REACTOR_RECIPES.recipeBuilder("gaia_2")
                .inputItems(gaiaIngot)
                .outputFluids(Mana.getFluid(10000))
                .outputItems(lifeEssence,16)
                .outputItems(manaDiamond,8)
                .outputItems(manaPearl,8)
                .outputItems(dragonstone,4)
                .outputItems(ChemicalHelper.get(ingot,ManaSteel),12)
                .outputItems(ChemicalHelper.get(ingot,TerraSteel),2)
                .chancedOutput(runeAir.getDefaultInstance(),(int)5000f,1)
                .chancedOutput(runeFire.getDefaultInstance(),(int)5000f,1)
                .chancedOutput(runeEarth.getDefaultInstance(),(int)5000f,1)
                .chancedOutput(runeWater.getDefaultInstance(),(int)5000f,1)
                .chancedOutput(runeSpring.getDefaultInstance(),(int)3000f,1)
                .chancedOutput(runeSummer.getDefaultInstance(),(int)3000f,1)
                .chancedOutput(runeAutumn.getDefaultInstance(),(int)3000f,1)
                .chancedOutput(runeWinter.getDefaultInstance(),(int)3000f,1)
                .chancedOutput(runeMana.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(runeLust.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(runeGluttony.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(runeGreed.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(runeSloth.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(runePride.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(runeWrath.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(runeEnvy.getDefaultInstance(),(int)2000f,1)
                .EUt(1536000/800)
                .duration(800)
                .save(provider);
        GAIA_REACTOR_RECIPES.recipeBuilder("gaia_3")
                .inputItems(ChemicalHelper.get(ingot,AlfSteel))
                .outputFluids(Mana.getFluid(45000))
                .outputItems(ChemicalHelper.get(ingot,Elementium),4)
                .outputItems(runeMana,2)
                .chancedOutput(asgardRune.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(vanaheimRune.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(alfheimRune.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(midgardRune.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(joetunheimRune.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(muspelheimRune.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(niflheimRune.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(nidavellirRune.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(helheimRune.getDefaultInstance(),(int)2000f,1)
                .chancedOutput(ChemicalHelper.get(ingot,AlfSteel),(int)2000f,1)
                .EUt(9126000/1200)
                .duration(1200)
                .save(provider);
}}
