package com.moguang.ctnhmana.data.recipe;

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
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;


public class runeRitualRecipes extends RecipeProviderBase implements RuneRitualExtension{
    public runeRitualRecipes(DatagenContext ctx) {
        super(ctx);
    }

    @Override
    protected void setup() {
        this.runeRitual(ModItems.fimbultyrTablet).rune2(ModItems.midgardRune, 2, 2).rune2(ModItems.helheimRune, -2, 2).rune2(BotaniaItems.runeSummer, 1, 3).rune2(BotaniaItems.runeSummer, 3, 1).rune2(BotaniaItems.runeFire, -1, 3).rune2(BotaniaItems.runeFire, -3, 1).input(BotaniaItems.enderDagger).input(ModItems.alfsteelNugget).input(BotaniaItems.vial).special(WanderingTraderRuneInput.INSTANCE).output(ModItems.kvasirBlood).mana(20000).build();
    }
}
