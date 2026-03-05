package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.common.recipe.builder.botania.ManaInfusionRecipeBuilder;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static com.moguang.ctnhmana.registry.multiblock.ManaMachine.*;
import static dev.shadowsoffire.apotheosis.ench.Ench.Items.*;
import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModItems.*;
import static net.minecraft.world.item.Items.*;
import static vazkii.botania.common.block.BotaniaBlocks.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import static vazkii.botania.common.item.BotaniaItems.manaQuartz;

@SuppressWarnings("removal")
public class ManaPoolRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        ManaInfusionRecipeBuilder.builder("mana_diode")// 魔力二极管
                .input(DIODE.asStack())
                .output(MANA_DIODE.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("gjallar_hornempty")// 加拉尔号角（空）
                .input(Ingredient.of(grassHorn))
                .output(gjallarHornEmpty.getDefaultInstance())
                .mana(20000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("blocks_manasteel1")// 魔力钢块
                .input(Ingredient.of(IRON_BLOCK))
                .output(ChemicalHelper.get(block, ManaSteel, 1))
                .mana(36000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mana_resistor")// 魔力电阻
                .input(RESISTOR.asStack())
                .output(MANA_RESISTOR.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manasteel1")// 魔力钢锭
                .input(Ingredient.of(IRON_INGOT))
                .output(ChemicalHelper.get(ingot, ManaSteel))
                .mana(4000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mana_transistor")// 魔力晶体管
                .input(TRANSISTOR.asStack())
                .output(MANA_TRANSISTOR.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mana_capacitor")// 魔力电容
                .input(CAPACITOR.asStack())
                .output(MANA_CAPACITOR.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mana_inductor")// 魔力电感
                .input(INDUCTOR.asStack())
                .output(MANA_INDUCTOR.asStack())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("fried_chicken")// 炸鸡
                .input(Ingredient.of(COOKED_CHICKEN))
                .output(friedChicken.getDefaultInstance())
                .mana(2000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manaberries")// 魔力浆果
                .input(Ingredient.of(SWEET_BERRIES))
                .output(ForgeRegistries.ITEMS.getValue(new ResourceLocation("createcafe:mana_berries"))
                        .getDefaultInstance())
                .mana(2000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("grassseeds")// 草籽
                .input(Ingredient.of(GRASS))
                .output(grassSeeds.getDefaultInstance())
                .mana(5000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manabottle")// 魔力瓶
                .input(Ingredient.of(GLASS_BOTTLE))
                .output(manaBottle.getDefaultInstance())
                .mana(2000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manacookie")// 魔力曲奇
                .input(Ingredient.of(COOKIE))
                .output(manaCookie.getDefaultInstance())
                .mana(25000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manadiamond1")// 魔力钻石
                .input(Ingredient.of(DIAMOND))
                .output(manaDiamond.getDefaultInstance())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manadiamondblock1")// 魔力钻石块
                .input(Ingredient.of(DIAMOND_BLOCK))
                .output(manaDiamondBlock.asItem().getDefaultInstance())
                .mana(90000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("managlass")// 魔力玻璃
                .input(Ingredient.of(GLASS))
                .output(manaGlass.asItem().getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapearl")// 魔力珍珠
                .input(Ingredient.of(ENDER_PEARL))
                .output(manaPearl.getDefaultInstance())
                .mana(5000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder1")// 魔力粉
                .input(Ingredient.of(REDSTONE))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder2")// 魔力粉
                .input(Ingredient.of(GLOWSTONE_DUST))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder3")// 魔力粉
                .input(Ingredient.of(GUNPOWDER))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder4")// 魔力粉
                .input(Ingredient.of(SUGAR))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manaquartz")// 魔力石英
                .input(Ingredient.of(QUARTZ))
                .output(manaQuartz.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manastring")// 魔力线
                .input(Ingredient.of(STRING))
                .output(manaString.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mycelseeds1")// 菌丝籽
                .input(Ingredient.of(BROWN_MUSHROOM))
                .output(mycelSeeds.getDefaultInstance())
                .mana(5000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("mycelseeds2")// 菌丝籽
                .input(Ingredient.of(RED_MUSHROOM))
                .output(mycelSeeds.getDefaultInstance())
                .mana(5000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("nightmarefuel")// 噩梦燃料
                .input(Ingredient.of(COAL))
                .output(nightmareFuel.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("pistonrelay")// 活塞中继器
                .input(Ingredient.of(PISTON))
                .output(pistonRelay.asItem().getDefaultInstance())
                .mana(10000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("tinypotato")// 小土豆
                .input(Ingredient.of(POTATO))
                .output(tinyPotato.asItem().getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("podzolseeds")// 灰化土籽
                .input(Ingredient.of(DEAD_BUSH))
                .output(podzolSeeds.getDefaultInstance())
                .mana(1500)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("manapowder5")// 魔力粉
                .input(Ingredient.of(Tags.Items.DYES))
                .output(manaPowder.getDefaultInstance())
                .mana(1000)
                .circuitMeta(1)
                .save(provider);
        ManaInfusionRecipeBuilder.builder("fluid_mana")// 魔力流体
                .input(Items.BUCKET)
                .output(Mana.getBucket())
                .mana(250000)
                .allowReactor(false)
                .save(provider);
    }
}
