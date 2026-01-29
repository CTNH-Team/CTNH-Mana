package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModItems.*;
import java.util.function.Consumer;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static vazkii.botania.common.block.BotaniaBlocks.*;
import static vazkii.botania.common.item.BotaniaItems.*;

@SuppressWarnings("removal")
public class ManaReactorRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        MANA_REACTOR_RECIPES.recipeBuilder("test11")
                .addCondition(new ManaReactorCondition(true,"GT",4))
                .inputItems(runeMana,24)
                .inputFluids(Zenith_essence.getFluid(144))
                .outputItems(HORIZEN_RUNE)
                .duration(200)
                .circuitMeta(5)
                .EUt(114514)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("test22")
                .addCondition(new ManaReactorCondition(true))
                .inputItems(runeFire,24)
                .inputFluids(Zenith_essence.getFluid(144))
                .outputItems(STARLIGHT_RUNE)
                .duration(200)
                .circuitMeta(2)
                .EUt(114514)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("test33")
                .addCondition(new ManaReactorCondition(true))
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:chain")))
                .inputFluids(Zenith_essence.getFluid(144))
                .outputItems(HORIZEN_RUNE)
                .duration(200)
                .circuitMeta(2)
                .EUt(114514)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("gjallarHornempty")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(grassHorn,1)
                .outputItems(gjallarHornEmpty)
                .duration(60)
                .circuitMeta(1)
                .EUt(512)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("blocks_manasteel1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.IRON_BLOCK,1)
                .outputItems(ChemicalHelper.get(block,ManaSteel,1))
                .duration(500)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("blocks_manasteel2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.IRON_INGOT,9)
                .outputItems(ChemicalHelper.get(block,ManaSteel,1))
                .duration(500)
                .circuitMeta(9)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_resistor")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.RESISTOR,1)
                .outputItems(MANA_RESISTOR,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manasteel1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.IRON_INGOT,1)
                .outputItems(ChemicalHelper.get(ingot,ManaSteel),1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manasteel2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.IRON_BLOCK,1)
                .outputItems(ChemicalHelper.get(ingot,ManaSteel),9)
                .duration(500)
                .circuitMeta(9)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_transistor")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.TRANSISTOR,1)
                .outputItems(MANA_TRANSISTOR,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_capacitor")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.CAPACITOR,1)
                .outputItems(MANA_CAPACITOR,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_diode")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.DIODE,1)
                .outputItems(MANA_DIODE,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_inductor")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.INDUCTOR,1)
                .outputItems(MANA_INDUCTOR,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("friedchicken")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.COOKED_CHICKEN,1)
                .outputItems(friedChicken,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(8)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("grassseeds")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GRASS,1)
                .outputItems(grassSeeds,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manabottle")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GLASS_BOTTLE,1)
                .outputItems(manaBottle,1)
                .duration(120)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manacookie")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.COOKIE,1)
                .outputItems(manaCookie,1)
                .duration(100)
                .circuitMeta(1)
                .EUt(128)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manadiamond1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DIAMOND,1)
                .outputItems(manaDiamond,1)
                .duration(100)
                .circuitMeta(1)
                .EUt(128)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manadiamond2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DIAMOND_BLOCK,1)
                .outputItems(manaDiamond,9)
                .duration(1000)
                .circuitMeta(9)
                .EUt(512)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manadiamondblock1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DIAMOND_BLOCK,1)
                .outputItems(BotaniaBlocks.manaDiamondBlock.asItem(),1)
                .duration(1000)
                .circuitMeta(1)
                .EUt(512)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manadiamondblock2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DIAMOND,9)
                .outputItems(BotaniaBlocks.manaDiamondBlock.asItem(),1)
                .duration(1000)
                .circuitMeta(9)
                .EUt(512)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("managlass")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GLASS,1)
                .outputItems(BotaniaBlocks.manaGlass.asItem(),1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("managlass")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.ENDER_PEARL,1)
                .outputItems(manaPearl,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("pixiedust1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.REDSTONE,1)
                .outputItems(manaPowder,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("pixiedust2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GLOWSTONE_DUST,1)
                .outputItems(manaPowder,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("pixiedust3")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GUNPOWDER,1)
                .outputItems(manaPowder,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("pixiedust4")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.SUGAR,1)
                .outputItems(manaPowder,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manaquartz")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.QUARTZ,1)
                .outputItems(BotaniaItems.manaQuartz,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manastring")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.STRING,1)
                .outputItems(manaString,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mycelseeds1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.BROWN_MUSHROOM,1)
                .outputItems(mycelSeeds,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(64)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mycelseeds2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.RED_MUSHROOM,1)
                .outputItems(mycelSeeds,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(64)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("nightmarefuel")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.COAL,1)
                .outputItems(nightmareFuel,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("pistonrelay")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.PISTON,1)
                .outputItems(pistonRelay,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(128)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("tinypotato")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.POTATO,1)
                .outputItems(tinyPotato,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("podzolseeds")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DEAD_BUSH,1)
                .outputItems(podzolSeeds,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manaberries")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.SWEET_BERRIES,1)
                .outputItems(new ResourceLocation("createcafe:mana_berries"),1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);

    }
}
