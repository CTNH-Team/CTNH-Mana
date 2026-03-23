package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import com.moguang.ctnhmana.common.recipe.builder.PetalRecipeBuilder;
import com.moguang.ctnhmana.registry.CMBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

import static com.moguang.ctnhmana.data.recipe.utils.BotaniaIngredients.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.MANA_FORGE_RECIPES;
import static io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.*;
import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModBlocks.*;
import static mythicbotany.register.ModItems.*;

public class BotaniaRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        PetalRecipeBuilder.builder("demon_flytrap")// 恶魔捕蝇草
                .input(LIGHTBLUE, LIGHTBLUE, GREEN, GREEN, BROWN)
                .input(runeGreed, runeEnvy, gaiaSpirit)
                .output(CMBlocks.DEMON_FLYTRAP.asStack())
                .reagent(Items.GRASS.getDefaultInstance())
                .save(provider);
        PetalRecipeBuilder.builder("blood_antiaris")// 见血封喉
                .input(RED, RED, GREEN, GRAY)
                .input(runeSloth, runeFire, runeWrath, gaiaSpirit)
                .output(CMBlocks.BLOOD_ANTIARIS.asStack())
                .reagent(Items.GRASS.getDefaultInstance())
                .save(provider);
        PetalRecipeBuilder.builder("pure_daisy")// 纯白雏菊
                .input(WHITE, WHITE, YELLOW)
                .input(new ItemStack(Items.DANDELION))
                .output(new ItemStack(BotaniaFlowerBlocks.pureDaisy.asItem(), 1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("entropinnyum")// 熵律草
                .input(BLACK, BLACK, RED, RED, GREEN)
                .input(ChemicalHelper.get(TagPrefix.block, AlfSteel, 1))
                .input(ChemicalHelper.get(TagPrefix.block, Ultra_Mana, 1))
                .output(new ItemStack(BotaniaFlowerBlocks.entropinnyum.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("dandelifeon")// 生命游戏
                .input(PURPLE, PURPLE, GREEN, LIME)
                .input(runeWater, runeFire, runeAir, runeEarth, gaiaSpirit, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.dandelifeon.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("arcana")// 奥秘玫瑰
                .input(PURPLE, PURPLE, PINK, LIME)
                .input(new ItemStack(BotaniaItems.runeMana))
                .input(new ItemStack(Items.EXPERIENCE_BOTTLE))
                .input(ChemicalHelper.get(TagPrefix.ingot, TerraSteel, 1))
                .output(new ItemStack(BotaniaFlowerBlocks.rosaArcana.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("spectrolus")// 七彩莲
                .input(WHITE, PURPLE, YELLOW, RED, BLACK, BLUE, ORANGE)
                .input(runeAir, runeWinter, pixieDust)
                .input(new ItemStack(Items.WHITE_WOOL))
                .output(new ItemStack(BotaniaFlowerBlocks.spectrolus.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("gourmaryllis")// 美食家莲
                .input(LIGHTGRAY, LIGHTGRAY, YELLOW, YELLOW, RED, RED)
                .input(runeFire, runeSummer)
                .output(new ItemStack(BotaniaFlowerBlocks.gourmaryllis.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("kekimurus")// 巧克力莲
                .input(WHITE, WHITE, ORANGE, ORANGE, BROWN, BROWN)
                .input(runeGluttony, pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.kekimurus.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("hydroangeas")// 水绣球
                .input(LIGHTBLUE, LIGHTBLUE, BLUE, BLUE)
                .output(new ItemStack(BotaniaFlowerBlocks.hydroangeas.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("narslimmus")// 纳斯拉姆斯
                .input(LIME, LIME, BLACK, BLACK, GREEN, GREEN)
                .input(runeSummer, runeWater)
                .output(new ItemStack(BotaniaFlowerBlocks.narslimmus.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("rafflowsia")// 凋零莲
                .input(PURPLE, PURPLE, GREEN, GREEN, BLACK, BLACK)
                .input(runeEarth, runePride, pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.rafflowsia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("thermalily")// 火莲
                .input(RED, RED, ORANGE, ORANGE)
                .input(runeFire, runeEarth)
                .output(new ItemStack(BotaniaFlowerBlocks.thermalily.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("munchdew")// 咀嚼露
                .input(LIME, LIME, RED, RED, GREEN)
                .input(runeGluttony)
                .output(new ItemStack(BotaniaFlowerBlocks.munchdew.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("endoflame")// 末影火花
                .input(LIGHTGRAY, BROWN, BROWN, RED)
                .input(runeFire)
                .output(new ItemStack(BotaniaFlowerBlocks.endoflame.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("shulk_me_not")// 喵喵莲
                .input(PURPLE, PURPLE, PINK, PINK, LIGHTGRAY, LIGHTGRAY)
                .input(gaiaSpirit, runeEarth, runeEnvy)
                .input(ChemicalHelper.get(TagPrefix.ingot, Orichalcos, 1))
                .input(ChemicalHelper.get(TagPrefix.ingot, Elementium, 1))
                .input(ChemicalHelper.get(TagPrefix.ingot, TerraSteel, 1))
                .output(new ItemStack(BotaniaFlowerBlocks.shulkMeNot.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("labellia")// 标签莲
                .input(YELLOW, YELLOW, BLACK, WHITE, BLUE)
                .input(pixieDust, redstoneRoot, runeAutumn)
                .output(new ItemStack(BotaniaFlowerBlocks.labellia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("hyacidus")// 腐化莲
                .input(PURPLE, PURPLE, MAGENTA, MAGENTA, GREEN)
                .input(runeWater, redstoneRoot, runeAutumn)
                .output(new ItemStack(BotaniaFlowerBlocks.hyacidus.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("jaded_amaranthus")// 翡翠苋
                .input(LIME, GRAY, PURPLE)
                .input(runeSpring, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.jadedAmaranthus.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("tigerseye")// 虎眼莲
                .input(BROWN, ORANGE, LIME, YELLOW)
                .input(runeAutumn)
                .output(new ItemStack(BotaniaFlowerBlocks.tigerseye.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bellethorn")// 美杜莎莲
                .input(RED, RED, CYAN, CYAN)
                .input(redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.bellethorn.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("loonium")// 天界莲
                .input(GREEN, GREEN, GREEN, GREEN, GRAY)
                .input(redstoneRoot, runeEnvy, runeGluttony, runeSloth, pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.loonium.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("dreadthorn")// 恐惧荆棘
                .input(BLACK, BLACK, BLACK, CYAN, CYAN)
                .input(redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.dreadthorn.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("spectranthemum")// 幽灵菊
                .input(WHITE, WHITE, LIGHTGRAY, LIGHTGRAY, CYAN, CYAN)
                .input(runeWater, runeEnvy, pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.spectranthemum.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("hopperhock")// 漏斗莲
                .input(GRAY, GRAY, LIGHTGRAY, LIGHTGRAY)
                .input(runeAir, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.hopperhock.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("clayconia")// 黏土莲
                .input(GRAY, GRAY, LIGHTGRAY, LIGHTGRAY, CYAN)
                .input(runeEarth)
                .output(new ItemStack(BotaniaFlowerBlocks.clayconia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("orechid")// 矿脉莲
                .input(GRAY, GRAY, RED, RED, GREEN, GREEN, YELLOW, YELLOW)
                .input(runeGreed, runePride, redstoneRoot, pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.orechid.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("heiseidream")// 平成之梦
                .input(MAGENTA, MAGENTA, PURPLE, PURPLE, PINK, PINK)
                .input(runeWrath, pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.heiseiDream.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bubbell")// 气泡莲
                .input(BLUE, BLUE, CYAN, CYAN, LIGHTBLUE, LIGHTBLUE)
                .input(runeWater, runeSummer, pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.bubbell.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("solegnolia")// 日光莲
                .input(BROWN, BROWN, RED)
                .input(redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.solegnolia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("daffomill")// 雏菊磨坊
                .input(WHITE, WHITE, BROWN, YELLOW)
                .input(runeAir, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.daffomill.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("medumone")// 梅杜磨
                .input(GRAY, GRAY, BROWN, BROWN)
                .input(runeEarth, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.medumone.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("rannuncarpus")// 毛茛莲
                .input(ORANGE, ORANGE, YELLOW, YELLOW)
                .input(runeEarth, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.rannuncarpus.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("tangleberrie")// 缠结浆果
                .input(CYAN, CYAN, GRAY, GRAY, LIGHTGRAY)
                .input(runeEarth, runeAir)
                .output(new ItemStack(BotaniaFlowerBlocks.tangleberrie.asItem(), 1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("fallenkanade")// 神乐莲
                .input(WHITE, WHITE, ORANGE, ORANGE, YELLOW, YELLOW)
                .input(runeSpring)
                .output(new ItemStack(BotaniaFlowerBlocks.fallenKanade.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("agricarnation")// 农业康乃馨
                .input(LIME, LIME, GREEN, YELLOW)
                .input(runeSpring, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.agricarnation.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bergamute")// 佛手柑莲
                .input(ORANGE, GREEN)
                .input(redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.bergamute.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("pollidisiac")// 花粉莲
                .input(RED, RED, PINK, PINK, ORANGE, ORANGE)
                .input(runeLust, runeFire)
                .output(new ItemStack(BotaniaFlowerBlocks.pollidisiac.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("orechidignem")// 地狱矿脉莲
                .input(RED, RED, PINK, PINK, WHITE, WHITE)
                .input(runePride, runeGreed, pixieDust, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.orechidIgnem.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("exoflame")// 外焰莲
                .input(RED, GRAY, RED, LIGHTGRAY)
                .input(runeFire, runeSummer)
                .output(new ItemStack(BotaniaFlowerBlocks.exoflame.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("marimorphosis")// 岩石莲
                .input(YELLOW, GREEN, RED, GRAY)
                .input(runeFire, runeEarth, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.marimorphosis.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("vinculotus")// 束缚莲
                .input(BLACK, BLACK, PURPLE, PURPLE, GREEN)
                .input(runeSloth, runeLust, runeWater, redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.vinculotus.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("jiyuulia")// 基尤莲
                .input(PINK, PINK, PURPLE, PURPLE)
                .input(runeAir, runeWater)
                .output(new ItemStack(BotaniaFlowerBlocks.jiyuulia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("witheraconite")// 乌头莲
                .input(BLACK, BLACK)
                .input(runePride)
                .input(new ItemStack(Items.WITHER_ROSE))
                .input(ChemicalHelper.get(TagPrefix.ingot, AlfSteel, 1))
                .input(ChemicalHelper.get(TagPrefix.ingot, Elementium, 1))
                .input(ChemicalHelper.get(TagPrefix.ingot, TerraSteel, 1))
                .input(ChemicalHelper.get(TagPrefix.ingot, ManaSteel, 1))
                .input(new ItemStack(ChemicalHelper.get(TagPrefix.ingot,PRIMOVOLITHEST).getItem()))
                .input(new ItemStack(Items.NETHER_STAR))
                .input(new ItemStack(alfsteelSword))
                .output(new ItemStack(witherAconite.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("aquapanthus")// 水莲
                .input(BLUE, BLUE, GREEN, GREEN, CYAN, LIGHTBLUE)
                .input(runeWater)
                .output(new ItemStack(aquapanthus.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("exoblaze")// 烈焰莲
                .input(LIGHTGRAY, GRAY, YELLOW, YELLOW)
                .input(runeFire)
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(exoblaze.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("raindeletia")// 雨莲
                .input(WHITE, MAGENTA, BLUE, LIGHTBLUE)
                .input(runeWater, runeSpring)
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(raindeletia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("hellebore")// 铁筷子莲
                .input(CYAN, PURPLE, RED, RED)
                .input(runeFire)
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(hellebore.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("feysythia")// 仙女莲
                .input(YELLOW, YELLOW, GREEN, GREEN)
                .input(runeFire, runeAutumn)
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(feysythia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("petrunia")// 矮牵牛莲
                .input(GREEN, GREEN, RED, RED, RED)
                .input(runeFire, runeMana)
                .input(ChemicalHelper.get(TagPrefix.ingot, AlfSteel, 1))
                .input(ChemicalHelper.get(TagPrefix.ingot, Elementium, 1))
                .input(ChemicalHelper.get(TagPrefix.ingot, TerraSteel, 1))
                .input(ChemicalHelper.get(TagPrefix.ingot, ManaSteel, 1))
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(petrunia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("reikarlily")// 蕾卡尔莲
                .input(LIGHTBLUE, LIGHTBLUE, BLUE, BLUE, CYAN)
                .input(runeSloth, runeEnvy, runePride, gaiaSpirit)
                .input(ChemicalHelper.get(TagPrefix.ingot, TerraSteel, 1))
                .input(ChemicalHelper.get(TagPrefix.ingot, AlfSteel, 1))
                .input(new ItemStack(BotaniaItems.gaiaIngot))
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(reikarlily.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("tinkle")// 叮当莲
                .input(YELLOW, YELLOW, LIME, GREEN)
                .input(runeEarth, runeWater)
                .input(new ItemStack(spiritFragment))
                .input(new ItemStack(spiritFragment))
                .output(new ItemStack(tinkle.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bellflower")// 风铃莲
                .input(YELLOW, YELLOW, LIME)
                .input(runeAir)
                .input(new ItemStack(spiritFragment, 1))
                .output(new ItemStack(bellflower.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("resoncund")// 共鸣莲
                .input(ORANGE, MAGENTA, MAGENTA)
                .input(runeLust, runeGluttony)
                .output(new ItemStack(resoncund.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("omniviolet")// 全紫莲
                .input(PURPLE, PURPLE, BLUE, BLUE)
                .input(runeLust, runeMana, runeSpring)
                .output(new ItemStack(omniviolet.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("stonesia")// 石莲
                .input(GRAY, GRAY, BLACK)
                .input(gaiaSpirit, runeGluttony, runeAutumn)
                .output(new ItemStack(stonesia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("twinstar")// 双星莲
                .input(YELLOW, YELLOW, YELLOW, ORANGE, ORANGE, ORANGE)
                .input(pixieDust, pixieDust)
                .output(new ItemStack(twinstar.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("sunshinelily")// 日光百合
                .input(YELLOW, YELLOW, BLUE, ORANGE)
                .output(new ItemStack(sunshineLily.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("edelweiss")// 雪绒花
                .input(WHITE, WHITE, WHITE, LIGHTBLUE, LIGHTBLUE, LIGHTBLUE)
                .input(runeWinter, runeMana)
                .input(ChemicalHelper.get(TagPrefix.ingot, TerraSteel, 1))
                .input(new ItemStack(BotaniaItems.gaiaIngot))
                .output(new ItemStack(edelweiss.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bloodenchantress")// 血附魔莲
                .input(RED, RED, RED, RED)
                .input(runeSummer, runeFire, runeWrath)
                .output(new ItemStack(bloodEnchantress.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("moonlightlily")// 月光百合
                .input(BLACK, BLACK, GRAY, PURPLE)
                .output(new ItemStack(moonlightLily.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("tradeorchid")// 交易兰
                .input(LIME, BROWN, YELLOW, LIME, GREEN)
                .input(runeLust, runeGreed, redstoneRoot)
                .output(new ItemStack(tradeOrchid.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("woodienia")// 木莲
                .input(BROWN, BROWN, BROWN, GRAY)
                .input(runeGluttony, redstoneRoot)
                .input(new ItemStack(elementiumQuartz))
                .output(new ItemStack(woodienia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("annoyingflower")// 烦人莲
                .input(WHITE, WHITE, GREEN, PINK, PINK)
                .input(runeMana)
                .input(new ItemStack(spiritFragment))
                .output(new ItemStack(annoyingflower.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("serenitian")// 宁静莲
                .input(PURPLE, PURPLE, BLUE, BLUE)
                .input(runeSloth, runeMana, runeGreed, gaiaSpirit)
                .input(new ItemStack(Items.WITHER_ROSE))
                .output(new ItemStack(serenitian.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("necrofleur")// 死灵莲
                .input(LIGHTGRAY, LIGHTGRAY, RED, PINK)
                .input(runeWrath, pixieDust)
                .output(new ItemStack(necrofleur.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("manalink")// 魔力链接
                .input(CYAN, CYAN, CYAN, LIGHTBLUE, LIGHTBLUE)
                .input(runeLust, runeSloth, gaiaSpirit)
                .output(new ItemStack(manalink.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("enchanter")// 附魔台
                .input(PURPLE, PURPLE, LIME, LIME, MAGENTA)
                .input(runeGluttony, runePride, runeGreed, gaiaSpirit)
                .output(new ItemStack(enchanter.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("mirrowtunia")// 镜花莲
                .input(CYAN, CYAN, BLUE, LIGHTBLUE)
                .input(runeAir, runeWrath, runePride, pixieDust)
                .output(new ItemStack(mirrowtunia.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("manastar")// 魔力之星
                .input(GREEN, RED, CYAN, LIGHTBLUE)
                .output(new ItemStack(BotaniaFlowerBlocks.manastar.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("semper_augustus")// 奥古斯都
                .input(RED, RED, WHITE, WHITE)
                .input(new ItemStack(orichalcos))
                .input(new ItemStack(BotaniaItems.gaiaIngot))
                .output(new ItemStack(CMBlocks.SEMPER_AUGUSTUS.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("black_vein")// 金盏花
                .input(BLACK, BLACK, RED, WHITE)
                .input(new ItemStack(Blocks.GOLD_BLOCK.asItem()))
                .input(GTItems.ELECTRIC_PUMP_LV.asStack())
                .output(new ItemStack(CMBlocks.BLACKVEIN_MARIGOLD.asItem(), 1))
                .reagent(Tags.Items.SEEDS)
                .save(provider);
        MANA_FORGE_RECIPES.recipeBuilder("potato")
                .inputItems(gildedPotato)
                .outputItems(gildedPotatoMashed)
                .duration(1000)
                .EUt(4)
                .save(provider);
        MANA_FORGE_RECIPES.recipeBuilder("spirit")
                .inputItems(spiritFuel)
                .outputItems(spiritFragment)
                .duration(2000)
                .EUt(4)
                .save(provider);
    }
}