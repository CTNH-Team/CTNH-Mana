package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.moguang.ctnhmana.common.recipe.builder.botania.RuneRitualRecipeBuilder;
import com.moguang.ctnhmana.registry.CMItems;
import mythicbotany.data.recipes.extension.RuneRitualExtension;
import mythicbotany.kvasir.WanderingTraderRuneInput;
import mythicbotany.register.ModItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.recipe.RecipeProviderBase;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot;
import static com.moguang.ctnhmana.registry.CMMaterials.YURIKO;


public class runeRitualRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        RuneRitualRecipeBuilder.builder("yuriko")
                .center(BotaniaFlowerBlocks.pureDaisy.asItem())
                .rune2(BotaniaItems.runeSummer, 2, 2)
                .rune2(BotaniaItems.runeSummer, 3, 1)
                .rune2(BotaniaItems.runeSummer, 1, -3)
                .rune2(BotaniaItems.runeAutumn, 3, -1)
                .rune2(BotaniaItems.runeAutumn, 1, 3)
                .rune2(BotaniaItems.runeAutumn, 2, -2)
                .rune4(ChemicalHelper.get(ingot,YURIKO).getItem(), 3, 0)
                .input(BotaniaItems.terraSword)
                .input(BotaniaItems.manaDiamond)
                .input(BotaniaItems.whitePetal)
                .specialInput(WanderingTraderRuneInput.INSTANCE)
                .output(CMItems.YURIKO_RING.asItem())
                .mana(79631)
                .save(provider);
    }

}
