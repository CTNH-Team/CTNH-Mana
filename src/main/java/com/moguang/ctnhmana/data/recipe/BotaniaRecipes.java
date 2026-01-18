package com.moguang.ctnhmana.data.recipe;


import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.moguang.ctnhmana.common.recipe.builder.PetalRecipeBuilder;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.multiblock.Botania;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.mjoellnir.Mjoellnir;
import net.minecraft.ResourceLocationException;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.checkerframework.checker.units.qual.C;
import vazkii.botania.common.block.BotaniaBlock;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlock;
import vazkii.botania.common.crafting.recipe.PhantomInkRecipe;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import javax.sound.sampled.LineEvent;
import javax.swing.text.html.HTML;
import java.util.function.Consumer;
import static io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.*;
import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModItems.*;
import static com.moguang.ctnhmana.data.recipe.utils.BotaniaIngredients.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static mythicbotany.register.ModBlocks.*;

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
                .output(CMBlocks.BLOOD_ANTIARIS.asStack())
                .reagent(Items.GRASS.getDefaultInstance())
                .save(provider);
        PetalRecipeBuilder.builder("pure_daisy")
                .input(WHITE, WHITE, YELLOW)
                .input(new ItemStack(Items.DANDELION))
                .output(new ItemStack(BotaniaFlowerBlocks.pureDaisy.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("entropinnyum")
                .input(BLACK,BLACK,RED,RED,GREEN)
                .input(ChemicalHelper.get(TagPrefix.block,AlfSteel,1))
                .input(ChemicalHelper.get(TagPrefix.block,Ultra_Mana,1))
                .output(new ItemStack(BotaniaFlowerBlocks.entropinnyum.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("dandelifeon")
                .input(PURPLE,PURPLE,GREEN,LIME)
                .input(runeWater,runeFire,runeAir,runeEarth,gaiaSpirit,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.dandelifeon.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("arcana")
                .input(PURPLE,PURPLE,PINK,LIME)
                .input(new ItemStack(BotaniaItems.runeMana))
                .input(new ItemStack(Items.EXPERIENCE_BOTTLE))
                .input(ChemicalHelper.get(TagPrefix.ingot,TerraSteel,1))
                .output(new ItemStack(BotaniaFlowerBlocks.rosaArcana.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("spectrolus")
                .input(WHITE,PURPLE,YELLOW,RED,BLACK,BLUE,ORANGE)
                .input(runeAir,runeWinter,pixieDust)
                .input(new ItemStack(Items.WHITE_WOOL))
                .output(new ItemStack(BotaniaFlowerBlocks.spectrolus.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("gourmaryllis")
                .input(LIGHTGRAY,LIGHTGRAY,YELLOW,YELLOW,RED,RED)
                .input(runeFire,gaiaSpirit,runeSummer)
                .output(new ItemStack(BotaniaFlowerBlocks.gourmaryllis.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("kekimurus")
                .input(WHITE,WHITE,ORANGE,ORANGE,BROWN,BROWN)
                .input(runeGluttony,pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.kekimurus.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("hydroangeas")
                .input(LIGHTBLUE,LIGHTBLUE,BLUE,BLUE)
                .output(new ItemStack(BotaniaFlowerBlocks.hydroangeas.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("narslimmus")
                .input(LIME,LIME,BLACK,BLACK,GREEN,GREEN)
                .input(runeSummer,runeWater)
                .output(new ItemStack(BotaniaFlowerBlocks.narslimmus.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("rafflowsia")
                .input(PURPLE,PURPLE,GREEN,GREEN,BLACK,BLACK)
                .input(runeEarth,runePride,pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.rafflowsia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("thermalily")
                .input(RED,RED,ORANGE,ORANGE)
                .input(runeFire,runeEarth)
                .output(new ItemStack(BotaniaFlowerBlocks.thermalily.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("munchdew")
                .input(LIME,LIME,RED,RED,GREEN)
                .input(runeGluttony)
                .output(new ItemStack(BotaniaFlowerBlocks.munchdew.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("endoflame")
                .input(LIGHTGRAY,BROWN,BROWN,RED)
                .input(runeFire)
                .output(new ItemStack(BotaniaFlowerBlocks.endoflame.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("shulk_me_not")
                .input(PURPLE,PURPLE,PINK,PINK,LIGHTGRAY,LIGHTGRAY)
                .input(gaiaSpirit,runeEarth,runeEnvy)
                .input(ChemicalHelper.get(TagPrefix.ingot,AlfSteel,1))
                .input(ChemicalHelper.get(TagPrefix.ingot,Elementium,1))
                .input(ChemicalHelper.get(TagPrefix.ingot,TerraSteel,1))
                .output(new ItemStack(BotaniaFlowerBlocks.shulkMeNot.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("labellia")
                .input(YELLOW,YELLOW,BLACK,WHITE,BLUE)
                .input(pixieDust,redstoneRoot,runeAutumn)
                .output(new ItemStack(BotaniaFlowerBlocks.labellia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("hyacidus")
                .input(PURPLE,PURPLE,MAGENTA,MAGENTA,GREEN)
                .input(runeWater,redstoneRoot,runeAutumn)
                .output(new ItemStack(BotaniaFlowerBlocks.hyacidus.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("jaded_amaranthus")
                .input(LIME,GRAY,PURPLE)
                .input(runeSpring,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.jadedAmaranthus.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("tigerseye")
                .input(BROWN,ORANGE,LIME,YELLOW)
                .input(runeAutumn)
                .output(new ItemStack(BotaniaFlowerBlocks.tigerseye.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bellethorn")
                .input(RED,RED,CYAN,CYAN)
                .input(redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.bellethorn.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("loonium")
                .input(GREEN,GREEN,GREEN,GREEN,GRAY)
                .input(redstoneRoot,runeEnvy,runeGluttony,runeSloth,pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.loonium.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("dreadthorn")
                .input(BLACK,BLACK,BLACK,CYAN,CYAN)
                .input(redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.dreadthorn.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("spectranthemum")
                .input(WHITE,WHITE,LIGHTGRAY,LIGHTGRAY,CYAN,CYAN)
                .input(runeWater,runeEnvy,pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.spectranthemum.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("hopperhock")
                .input(GRAY,GRAY,LIGHTGRAY,LIGHTGRAY)
                .input(runeAir,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.hopperhock.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("clayconia")
                .input(GRAY,GRAY,LIGHTGRAY,LIGHTGRAY,CYAN)
                .input(runeEarth)
                .output(new ItemStack(BotaniaFlowerBlocks.clayconia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("orechid")
                .input(GRAY,GRAY,RED,RED,GREEN,GREEN,YELLOW,YELLOW)
                .input(runeGreed,runePride,redstoneRoot,pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.orechid.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("heiseidream")
                .input(MAGENTA,MAGENTA,PURPLE,PURPLE,PINK,PINK)
                .input(runeWrath,pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.heiseiDream.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bubbell")
                .input(BLUE,BLUE,CYAN,CYAN,LIGHTBLUE,LIGHTBLUE)
                .input(runeWater,runeSummer,pixieDust)
                .output(new ItemStack(BotaniaFlowerBlocks.bubbell.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("solegnolia")
                .input(BROWN,BROWN,RED)
                .input(redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.solegnolia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("daffomill")
                .input(WHITE,WHITE,BROWN,YELLOW)
                .input(runeAir,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.daffomill.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("medumone")
                .input(GRAY,GRAY,BROWN,BROWN)
                .input(runeEarth,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.medumone.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("rannuncarpus")
                .input(ORANGE,ORANGE,YELLOW,YELLOW)
                .input(runeEarth,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.rannuncarpus.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("tangleberrie")
                .input(CYAN,CYAN,GRAY,GRAY,LIGHTGRAY)
                .input(runeEarth,runeAir)
                .output(new ItemStack(BotaniaFlowerBlocks.tangleberrie.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("fallenkanade")
                .input(WHITE,WHITE,ORANGE,ORANGE,YELLOW,YELLOW)
                .input(runeSpring)
                .output(new ItemStack(BotaniaFlowerBlocks.fallenKanade.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("agricarnation")
                .input(LIME,LIME,GREEN,YELLOW)
                .input(runeSpring,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.agricarnation.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bergamute")
                .input(ORANGE,GREEN)
                .input(redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.bergamute.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("pollidisiac")
                .input(RED,RED,PINK,PINK,ORANGE,ORANGE)
                .input(runeLust,runeFire)
                .output(new ItemStack(BotaniaFlowerBlocks.pollidisiac.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("orechidignem")
                .input(RED,RED,PINK,PINK,WHITE,WHITE)
                .input(runePride,runeGreed,pixieDust,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.orechidIgnem.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("exoflame")
                .input(RED,GRAY,RED,LIGHTGRAY)
                .input(runeFire,runeSummer)
                .output(new ItemStack(BotaniaFlowerBlocks.exoflame.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("marimorphosis")
                .input(YELLOW,GREEN,RED,GRAY)
                .input(runeFire,runeEarth,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.marimorphosis.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("vinculotus")
                .input(BLACK,BLACK,PURPLE,PURPLE,GREEN)
                .input(runeSloth,runeLust,runeWater,redstoneRoot)
                .output(new ItemStack(BotaniaFlowerBlocks.vinculotus.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("jiyuulia")
                .input(PINK,PINK,PURPLE,PURPLE)
                .input(runeAir,runeWater)
                .output(new ItemStack(BotaniaFlowerBlocks.jiyuulia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("witheraconite")
                .input(BLACK,BLACK)
                .input(runePride)
                .input(new ItemStack(Items.WITHER_ROSE))
                .input(ChemicalHelper.get(TagPrefix.ingot,AlfSteel,1))
                .input(ChemicalHelper.get(TagPrefix.ingot,Elementium,1))
                .input(ChemicalHelper.get(TagPrefix.ingot,TerraSteel,1))
                .input(ChemicalHelper.get(TagPrefix.ingot,ManaSteel,1))
                .input(new ItemStack(BotaniaItems.gaiaIngot))
                .input(new ItemStack(Items.NETHER_STAR))
                .input(new ItemStack(alfsteelSword))
                .output(new ItemStack(witherAconite.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("aquapanthus")
                .input(BLUE,BLUE,GREEN,GREEN,CYAN,LIGHTBLUE)
                .input(runeWater)
                .output(new ItemStack(aquapanthus.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("exoblaze")
                .input(LIGHTGRAY,GRAY,YELLOW,YELLOW)
                .input(runeFire)
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(exoblaze.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("raindeletia")
                .input(WHITE,MAGENTA,BLUE,LIGHTBLUE)
                .input(runeWater,runeSpring)
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(raindeletia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("hellebore")
                .input(CYAN,PURPLE,RED,RED)
                .input(runeFire)
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(hellebore.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("feysythia")
                .input(YELLOW,YELLOW,GREEN,GREEN)
                .input(runeFire,runeAutumn)
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(feysythia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("petrunia")
                .input(GREEN,GREEN,RED,RED,RED)
                .input(runeFire,runeMana)
                .input(ChemicalHelper.get(TagPrefix.ingot,AlfSteel,1))
                .input(ChemicalHelper.get(TagPrefix.ingot,Elementium,1))
                .input(ChemicalHelper.get(TagPrefix.ingot,TerraSteel,1))
                .input(ChemicalHelper.get(TagPrefix.ingot,ManaSteel,1))
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(petrunia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("reikarlily")
                .input(LIGHTBLUE,LIGHTBLUE,BLUE,BLUE,CYAN)
                .input(runeSloth,runeEnvy,runePride,gaiaSpirit)
                .input(ChemicalHelper.get(TagPrefix.ingot,TerraSteel,1))
                .input(ChemicalHelper.get(TagPrefix.ingot,AlfSteel,1))
                .input(new ItemStack(BotaniaItems.gaiaIngot))
                .input(new ItemStack(Items.BLAZE_POWDER))
                .output(new ItemStack(reikarlily.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("tinkle")
                .input(YELLOW,YELLOW,LIME,GREEN)
                .input(runeEarth,runeWater)
                .input(new ItemStack(spiritFragment))
                .input(new ItemStack(spiritFragment))
                .output(new ItemStack(tinkle.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bellflower")
                .input(YELLOW,YELLOW,LIME)
                .input(runeAir)
                .input(new ItemStack(spiritFragment,1))
                .output(new ItemStack(bellflower.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("resoncund")
                .input(ORANGE,MAGENTA,MAGENTA)
                .input(runeLust,runeGluttony)
                .output(new ItemStack(resoncund.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("omniviolet")
                .input(PURPLE,PURPLE,BLUE,BLUE)
                .input(runeLust,runeMana,runeSpring)
                .output(new ItemStack(omniviolet.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("stonesia")
                .input(GRAY,GRAY,BLACK)
                .input(gaiaSpirit,runeGluttony,runeAutumn)
                .output(new ItemStack(stonesia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("twinstar")
                .input(YELLOW,YELLOW,YELLOW,ORANGE,ORANGE,ORANGE)
                .input(pixieDust,pixieDust)
                .output(new ItemStack(twinstar.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("sunshinelily")
                .input(YELLOW,YELLOW,BLUE,ORANGE)
                .output(new ItemStack(sunshineLily.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("edelweiss")
                .input(WHITE,WHITE,WHITE,LIGHTBLUE,LIGHTBLUE,LIGHTBLUE)
                .input(runeWinter,runeMana)
                .input(ChemicalHelper.get(TagPrefix.ingot,TerraSteel,1))
                .input(new ItemStack(BotaniaItems.gaiaIngot))
                .output(new ItemStack(edelweiss.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("bloodenchantress")
                .input(RED,RED,RED,RED)
                .input(runeSummer,runeFire,runeWrath)
                .output(new ItemStack(bloodEnchantress.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("moonlightlily")
                .input(BLACK,BLACK,GRAY, PURPLE)
                .output(new ItemStack(moonlightLily.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("tradeorchid")
                .input(LIME,BROWN,YELLOW,LIME,GREEN)
                .input(runeLust,runeGreed,redstoneRoot)
                .output(new ItemStack(tradeOrchid.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("woodienia")
                .input(BROWN,BROWN,BROWN,GRAY)
                .input(runeGluttony,redstoneRoot)
                .input(new ItemStack(elementiumQuartz))
                .output(new ItemStack(woodienia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("annoyingflower")
                .input(WHITE,WHITE,GREEN,PINK,PINK)
                .input(runeMana)
                .input(new ItemStack(spiritFragment))
                .output(new ItemStack(annoyingflower.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("serenitian")
                .input(PURPLE,PURPLE,BLUE,BLUE)
                .input(runeSloth,runeMana,runeGreed,gaiaSpirit)
                .input(new ItemStack(Items.WITHER_ROSE))
                .output(new ItemStack(serenitian.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("necrofleur")
                .input(LIGHTGRAY,LIGHTGRAY,RED,PINK)
                .input(runeWrath,pixieDust)
                .output(new ItemStack(necrofleur.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("manalink")
                .input(CYAN,CYAN,CYAN,LIGHTBLUE,LIGHTBLUE)
                .input(runeLust,runeSloth,gaiaSpirit)
                .output(new ItemStack(manalink.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("enchanter")
                .input(PURPLE,PURPLE,LIME,LIME,MAGENTA)
                .input(runeGluttony,runePride,runeGreed,gaiaSpirit)
                .output(new ItemStack(enchanter.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("mirrowtunia")
                .input(CYAN,CYAN,BLUE,LIGHTBLUE)
                .input(runeAir,runeWrath,runePride,pixieDust)
                .output(new ItemStack(mirrowtunia.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
        PetalRecipeBuilder.builder("manastar")
                .input(GREEN,RED,CYAN,LIGHTBLUE)
                .output(new ItemStack(BotaniaFlowerBlocks.manastar.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);
    }
}