package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.moguang.ctnhmana.common.recipe.builder.botania.ManaInfusionRecipeBuilder;
import com.moguang.ctnhmana.registry.CMItems;
import net.minecraft.data.recipes.FinishedRecipe;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import com.moguang.ctnhmana.registry.multiblock.Botania;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.*;
import static com.moguang.ctnhmana.registry.multiblock.ManaMachine.*;
import static dev.shadowsoffire.apotheosis.ench.Ench.Items.*;
import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModItems.*;

import java.util.Objects;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static net.minecraft.world.item.Items.*;
import static vazkii.botania.common.block.BotaniaBlocks.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import static vazkii.botania.common.item.BotaniaItems.manaQuartz;

import java.util.function.Consumer;
@SuppressWarnings("removal")
public class ManaPoolRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        ManaInfusionRecipeBuilder.builder("mana_diode")
                .input(DIODE.asStack())
                .output(MANA_DIODE.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("gjallar_hornempty")
                .input(Ingredient.of(grassHorn))
                .output(gjallarHornEmpty.getDefaultInstance())
                .mana(20000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("blocks_manasteel1")
                .input(Ingredient.of(IRON_BLOCK))
                .output(ChemicalHelper.get(block,ManaSteel,1))
                .mana(36000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mana_resistor")
                .input(RESISTOR.asStack())
                .output(MANA_RESISTOR.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manasteel1")
                .input(Ingredient.of(IRON_INGOT))
                .output(ChemicalHelper.get(ingot,ManaSteel))
                .mana(4000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mana_transistor")
                .input(TRANSISTOR.asStack())
                .output(MANA_TRANSISTOR.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mana_capacitor")
                .input(CAPACITOR.asStack())
                .output(MANA_CAPACITOR.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mana_inductor")
                .input(INDUCTOR.asStack())
                .output(MANA_INDUCTOR.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("fried_chicken")
                .input(Ingredient.of(COOKED_CHICKEN))
                .output(friedChicken.getDefaultInstance())
                .mana(2000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manaberries")
                .input(Ingredient.of(SWEET_BERRIES))
                .output(ForgeRegistries.ITEMS.getValue(new ResourceLocation("createcafe:mana_berries")).getDefaultInstance())
                .mana(2000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("grassseeds")
                .input(Ingredient.of(GRASS))
                .output(grassSeeds.getDefaultInstance())
                .mana(5000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manabottle")
                .input(Ingredient.of(GLASS_BOTTLE))
                .output(manaBottle.getDefaultInstance())
                .mana(2000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manacookie")
                .input(Ingredient.of(COOKIE))
                .output(manaCookie.getDefaultInstance())
                .mana(25000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manadiamond1")
                .input(Ingredient.of(DIAMOND))
                .output(manaDiamond.getDefaultInstance())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manadiamondblock1")
                .input(Ingredient.of(DIAMOND_BLOCK))
                .output(manaDiamondBlock.asItem().getDefaultInstance())
                .mana(90000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("managlass")
                .input(Ingredient.of(GLASS))
                .output(manaGlass.asItem().getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapearl")
                .input(Ingredient.of(ENDER_PEARL))
                .output(manaPearl.getDefaultInstance())
                .mana(5000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder1")
                .input(Ingredient.of(REDSTONE))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder2")
                .input(Ingredient.of(GLOWSTONE_DUST))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder3")
                .input(Ingredient.of(GUNPOWDER))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder4")
                .input(Ingredient.of(SUGAR))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manaquartz")
                .input(Ingredient.of(QUARTZ))
                .output(manaQuartz.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manastring")
                .input(Ingredient.of(STRING))
                .output(manaString.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mycelseeds1")
                .input(Ingredient.of(BROWN_MUSHROOM))
                .output(mycelSeeds.getDefaultInstance())
                .mana(5000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mycelseeds2")
                .input(Ingredient.of(RED_MUSHROOM))
                .output(mycelSeeds.getDefaultInstance())
                .mana(5000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("nightmarefuel")
                .input(Ingredient.of(COAL))
                .output(nightmareFuel.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("pistonrelay")
                .input(Ingredient.of(PISTON))
                .output(pistonRelay.asItem().getDefaultInstance())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("tinypotato")
                .input(Ingredient.of(POTATO))
                .output(tinyPotato.asItem().getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("podzolseeds")
                .input(Ingredient.of(DEAD_BUSH))
                .output(podzolSeeds.getDefaultInstance())
                .mana(1500)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder5")
                .input(Ingredient.of(Tags.Items.DYES))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
    }
}
