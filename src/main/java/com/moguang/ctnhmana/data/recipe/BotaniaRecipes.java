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
import mythicbotany.mjoellnir.Mjoellnir;
import net.minecraft.ResourceLocationException;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import vazkii.botania.common.block.BotaniaBlock;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlock;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import javax.sound.sampled.LineEvent;
import javax.swing.text.html.HTML;
import java.util.function.Consumer;

import static com.moguang.ctnhmana.data.recipe.utils.BotaniaIngredients.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;

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
                .input(new ItemStack(BotaniaItems.runeEarth))
                .input(new ItemStack(BotaniaItems.runeFire))
                .output(new ItemStack(BotaniaFlowerBlocks.thermalily.asItem(),1))
                .reagent(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .save(provider);

    }
}
