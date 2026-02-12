package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.moguang.ctnhmana.common.recipe.builder.botania.RuneAltarRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import java.util.function.Consumer;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.moguang.ctnhmana.data.recipe.utils.BotaniaIngredients.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.simibubi.create.AllItems.*;
import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModItems.*;
import static net.minecraft.world.item.Items.*;
import static twilightforest.init.TFBlocks.*;
import static twilightforest.init.TFItems.*;
import static twilightforest.init.TFItems.CICADA;

@SuppressWarnings("removal")
public class RuneAltarRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        RuneAltarRecipeBuilder.builder("asgard_rune")//阿斯加德符文
                .input(runeAir,runePride,runeAutumn)
                .input(new ItemStack(BotaniaItems.rainbowRod))
                .input(new ItemStack(Items.NETHERITE_INGOT))
                .mana(20000)
                .output(new ItemStack(asgardRune,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("helheim_rune")//赫尔海姆符文
                .input(runeEnvy,runeFire,runeAutumn)
                .input(new ItemStack(Items.GOLD_INGOT))
                .input(Tags.Items.HEADS)
                .mana(20000)
                .output(new ItemStack(helheimRune,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("muspelheim_rune")//穆斯贝尔海姆符文
                .input(runeWrath,runeSummer,runeFire)
                .input(MAGMA_BLOCK)
                .input(NETHER_BRICK)
                .mana(20000)
                .output(new ItemStack(muspelheimRune,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("earth_rune")//大地符文
                .input(new ItemStack(SANDSTONE))
                .input(new ItemStack(BlockRegistry.FLOURISHING_LOG.asItem(),1))
                .input(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:andesite_alloy_ingot")))
                .input(new ItemStack(GRANITE))
                .input(new ItemStack(CLAY_BALL))
                .input(ChemicalHelper.get(dust,Calcite))
                .mana(2000)
                .output(new ItemStack(BotaniaItems.runeEarth,2))
                .save(provider);
        RuneAltarRecipeBuilder.builder("joetunheim_rune")//约顿海姆符文
                .input(new ItemStack(BLACKSTONE))
                .input(new ItemStack(BRICK))
                .input(runeGluttony,runeEarth,runeAutumn)
                .mana(20000)
                .output(new ItemStack(joetunheimRune,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("autumn_rune")//秋之符文
                .input(runeFire,runeAir)
                .input(new ItemStack(WHEAT))
                .input(new ItemStack(GLASS))
                .input(new ItemStack(DEAD_BUSH))
                .mana(5000)
                .output(new ItemStack(BotaniaItems.runeAutumn,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("vanaheim_rune")//华纳海姆符文
                .input(runeSpring,runeEarth,runePride)
                .input(ChemicalHelper.get(ingot,TerraSteel))
                .input(new ItemStack(BotaniaBlocks.alfPortal.asItem()))
                .mana(20000)
                .output(new ItemStack(vanaheimRune,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("winter_rune")//冬之符文
                .input(runeWater,runeEarth)
                .input(ChemicalHelper.get(dust,Ice))
                .input(new ItemStack(Blocks.SNOW_BLOCK.asItem()))
                .input(WHITE_WOOL)
                .input(ARCTIC_FUR.get())
                .mana(5000)
                .output(new ItemStack(BotaniaItems.runeWinter,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("water_rune")//水之符文
                .input(SUGAR_CANE)
                .input(LILY_PAD)
                .input(FISHING_ROD)
                .input(KELP)
                .input(new ItemStack(BlockRegistry.CASCADING_LOG.asItem(),2))
                .input(OAK_BOAT)
                .mana(2000)
                .output(new ItemStack(BotaniaItems.runeWater,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("fire_rune")//火之符文
                .input(BLAZE_CAKE.get())
                .input(GUNPOWDER)
                .input(Blocks.MAGMA_BLOCK.asItem())
                .input(BLAZE_POWDER)
                .input(new ItemStack(BlockRegistry.BLAZING_LOG.asItem(),2))
                .input(CRIMSON_FUNGUS)
                .mana(2000)
                .output(new ItemStack(BotaniaItems.runeFire,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("summer_rune")//夏之符文
                .input(runeEarth,runeWater)
                .input(LILAC)
                .input(PROPELLER.get())
                .input(CICADA.get())
                .input(MELON_SLICE)
                .mana(5000)
                .output(new ItemStack(BotaniaItems.runeSummer,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("alfheim_rune")//精灵国符文
                .input(runeAir,runeLust,runeSummer)
                .input(BotaniaItems.elementium)
                .input(JUNGLE_LEAVES)
                .mana(20000)
                .output(new ItemStack(alfheimRune,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("mana_rune")//魔力符文
                .input(BotaniaItems.manaBottle)
                .input(BotaniaItems.manaCookie)
                .input(BotaniaItems.manaQuartz)
                .input(BotaniaItems.manaPowder)
                .input(BotaniaBlocks.manaGlass.asItem())
                .input(BotaniaItems.manaweaveCloth)
                .input(JUNGLE_LEAVES)
                .mana(10000)
                .output(new ItemStack(BotaniaItems.runeMana,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("spring_rune")//春之符文
                .input(runeWater,runeFire)
                .input(Blocks.MOSS_BLOCK.asItem())
                .input(ROOT_BLOCK.get().asItem())
                .input(CHERRY_SAPLING)
                .input(HOLLOW_OAK_SAPLING.get().asItem())
                .mana(5000)
                .output(new ItemStack(BotaniaItems.runeSpring,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("midgard_rune")//米德加德符文
                .input(runeGreed,runeSpring,runeEarth)
                .input(Blocks.GRASS_BLOCK.asItem())
                .input(BotaniaItems.manaSteel)
                .mana(20000)
                .output(new ItemStack(midgardRune,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("air_rune")//风之符文
                .input(new ItemStack(BlockRegistry.VEXING_LOG.asItem(),1))
                .input(PROPELLER.get())
                .input(FEATHER)
                .input(STRING)
                .input(DANDELION)
                .input(ForgeRegistries.ITEMS.getValue(new ResourceLocation("create:white_sail")))
                .mana(2000)
                .output(new ItemStack(BotaniaItems.runeAir,2))
                .save(provider);
        RuneAltarRecipeBuilder.builder("niflheim_rune")//尼福尔海姆符文
                .input(runeWater,runeWinter,runeWrath)
                .input(Blocks.BLUE_ICE.asItem())
                .input(IRON_INGOT)
                .mana(20000)
                .output(new ItemStack(niflheimRune,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("nidavellir_rune")//尼达维符文
                .input(runeEarth,runeWinter,runeSloth)
                .input(Blocks.IRON_BLOCK.asItem())
                .input(COPPER_INGOT)
                .mana(20000)
                .output(new ItemStack(nidavellirRune,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("envy_rune")//嫉妒符文
                .input(runeWater,runeWinter)
                .input(BotaniaItems.manaDiamond)
                .input(BotaniaItems.manaDiamond)
                .mana(10000)
                .output(new ItemStack(BotaniaItems.runeEnvy,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("gilded_potato")//镀金土豆
                .input(POTATO)
                .input(GOLD_NUGGET)
                .mana(1000)
                .output(new ItemStack(gildedPotato,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("greed_rune")//贪婪符文
                .input(runeWater,runeSpring)
                .input(BotaniaItems.manaDiamond)
                .input(BotaniaItems.manaDiamond)
                .mana(10000)
                .output(new ItemStack(BotaniaItems.runeGreed,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("gluttony_rune")//暴食符文
                .input(runeFire,runeWinter)
                .input(BotaniaItems.manaDiamond)
                .input(BotaniaItems.manaDiamond)
                .mana(10000)
                .output(new ItemStack(BotaniaItems.runeGluttony,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("player_head")//玩家头颅
                .input(SKELETON_SKULL)
                .input(GOLDEN_APPLE)
                .input(NAME_TAG)
                .input(BotaniaItems.pixieDust)
                .input(PRISMARINE_CRYSTALS)
                .mana(30000)
                .output(new ItemStack(PLAYER_HEAD,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("lust_rune")//色欲符文
                .input(runeAir,runeSummer)
                .input(BotaniaItems.manaDiamond)
                .input(BotaniaItems.manaDiamond)
                .mana(10000)
                .output(new ItemStack(BotaniaItems.runeLust,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("orichalcos_hammer")//奥利哈钢之锤
                .input(orichalcos)
                .input(theChaos)
                .input(theEnd)
                .input(theOrigin)
                .input(gildedPotatoMashed)
                .mana(200000)
                .output(new ItemStack(orichalcosHammer,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("orichalcos")//奥利哈钢
                .input(gaiaSpirit,gaiaSpirit,gaiaSpirit,gaiaSpirit)
                .input(heroMedal)
                .input(new ItemStack(BotaniaItems.gaiaIngot))
                .input(new ItemStack(BotaniaItems.gaiaIngot))
                .input(gildedPotatoMashed)
                .mana(100000)
                .output(new ItemStack(orichalcos,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("pride_rune")//傲慢符文
                .input(runeFire,runeSummer)
                .input(BotaniaItems.manaDiamond)
                .input(BotaniaItems.manaDiamond)
                .mana(10000)
                .output(new ItemStack(BotaniaItems.runePride,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("photonium")//光子锭
                .input(spiritFragment,spiritFragment,spiritFragment)
                .input(BotaniaItems.elementium)
                .input(gildedPotatoMashed)
                .mana(1500)
                .output(new ItemStack(photonium,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("shadowium")//暗影锭
                .input(nightmareFuel,nightmareFuel,nightmareFuel)
                .input(BotaniaItems.elementium)
                .input(gildedPotatoMashed)
                .mana(1500)
                .output(new ItemStack(shadowium,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("sloth_rune")//懒惰符文
                .input(runeAir,runeAutumn)
                .input(BotaniaItems.manaDiamond)
                .input(BotaniaItems.manaDiamond)
                .mana(10000)
                .output(new ItemStack(BotaniaItems.runeSloth,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("wrath_rune")//愤怒符文
                .input(runeEarth,runeWinter)
                .input(BotaniaItems.manaDiamond)
                .input(BotaniaItems.manaDiamond)
                .mana(10000)
                .output(new ItemStack(BotaniaItems.runeWrath,1))
                .save(provider);
        RuneAltarRecipeBuilder.builder("zadkiel")//扎德基尔
                .input(POWDER_SNOW_BUCKET)
                .input(SNOW_BLOCK)
                .input(TOTEM_OF_UNDYING)
                .input(PACKED_ICE)
                .input(ICE)
                .input(BLUE_ICE)
                .mana(100000)
                .output(new ItemStack(zadkiel,1))
                .save(provider);
    }
}
