package com.magicbee.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import com.magicbee.ctnhmana.data.recipe.builder.apotheosis.SalvagingRecipeBuilder;
import com.magicbee.ctnhmana.data.tags.ItemTags;
import com.magicbee.ctnhmana.registry.CMItems;

import java.util.function.Consumer;

import static mythicbotany.register.ModItems.*;
import static vazkii.botania.common.item.BotaniaItems.*;

/**
 * Salvaging recipes for Botania / MythicBotany runes → elemental spirits.
 * Registered for both the Apotheosis Salvaging Table and the industrial machine.
 * <p>
 * Tier mappings follow {@link ItemTags} element tags
 * and Botania's seasonal / sin crafting pairs where tags alone are ambiguous.
 */
public class RuneSalvagingRecipes {

    private static final long EUT = GTValues.VA[GTValues.ULV];
    private static final int DURATION = 20 * 5;

    public static void init(Consumer<FinishedRecipe> provider) {
        tier1(provider);
        tier2(provider);
        tier3(provider);
        tier4(provider);
    }

    /** Tier 1 element runes + mana rune special case. */
    private static void tier1(Consumer<FinishedRecipe> provider) {
        elementTier1(provider, "water", runeWater, CMItems.SPIRIT_WATER);
        elementTier1(provider, "fire", runeFire, CMItems.SPIRIT_FIRE);
        elementTier1(provider, "earth", runeEarth, CMItems.SPIRIT_EARTH);
        elementTier1(provider, "air", runeAir, CMItems.SPIRIT_WIND);

        // Mana rune: 1-3 sparse mana spirit only
        rune("mana_rune")
                .itemInput(runeMana)
                .output(CMItems.WEAK_MANA_SPIRIT, 1, 3)
                .save(provider);
    }

    private static void elementTier1(Consumer<FinishedRecipe> provider, String name, Item rune,
                                     ItemLike spirit) {
        rune(name + "_rune")
                .itemInput(rune)
                .output(spirit, 1, 3)
                .output(CMItems.WEAK_MANA_SPIRIT, 0, 1)
                .save(provider);
    }

    /**
     * Tier 2 seasonal runes.
     * Primary / secondary from project element tags:
     * Spring water>wind, Summer fire>wind, Autumn earth>wind, Winter water>earth.
     */
    private static void tier2(Consumer<FinishedRecipe> provider) {
        seasonal(provider, "spring", runeSpring, CMItems.SPIRIT_WATER, CMItems.SPIRIT_WIND);
        seasonal(provider, "summer", runeSummer, CMItems.SPIRIT_FIRE, CMItems.SPIRIT_WIND);
        seasonal(provider, "autumn", runeAutumn, CMItems.SPIRIT_EARTH, CMItems.SPIRIT_WIND);
        seasonal(provider, "winter", runeWinter, CMItems.SPIRIT_WATER, CMItems.SPIRIT_EARTH);
    }

    private static void seasonal(Consumer<FinishedRecipe> provider, String name, Item rune,
                                 ItemLike primary, ItemLike secondary) {
        rune(name + "_rune")
                .itemInput(rune)
                .output(primary, 1, 6)
                .output(secondary, 0, 3)
                .output(CMItems.GIGA_MANA_SPIRIT, 0, 1)
                .save(provider);
    }

    /**
     * Tier 3 sin runes: 1-3 sin + 1-12 paired element + 0-1 ascending.
     * Paired element from tags / Botania sin recipes.
     */
    private static void tier3(Consumer<FinishedRecipe> provider) {
        sin(provider, "lust", runeLust, CMItems.SPIRIT_WIND);
        sin(provider, "gluttony", runeGluttony, CMItems.SPIRIT_EARTH);
        sin(provider, "greed", runeGreed, CMItems.SPIRIT_WATER);
        sin(provider, "sloth", runeSloth, CMItems.SPIRIT_WATER);
        sin(provider, "wrath", runeWrath, CMItems.SPIRIT_FIRE);
        sin(provider, "envy", runeEnvy, CMItems.SPIRIT_WATER);
        sin(provider, "pride", runePride, CMItems.SPIRIT_FIRE);
    }

    private static void sin(Consumer<FinishedRecipe> provider, String name, Item rune, ItemLike other) {
        rune(name + "_rune")
                .itemInput(rune)
                .output(CMItems.SPIRIT_SIN, 1, 3)
                .output(other, 1, 12)
                .output(CMItems.ASCENDING_MANA_SPIRIT, 0, 1)
                .save(provider);
    }

    /**
     * Tier 4 MythicBotany realm runes: 1-18 primary, 1-9 secondary, 0-1 rainbow (完美).
     */
    private static void tier4(Consumer<FinishedRecipe> provider) {
        realm(provider, "asgard", asgardRune, CMItems.SPIRIT_FIRE, CMItems.SPIRIT_WIND);
        realm(provider, "vanaheim", vanaheimRune, CMItems.SPIRIT_WATER, CMItems.SPIRIT_EARTH);
        realm(provider, "alfheim", alfheimRune, CMItems.SPIRIT_WIND, CMItems.SPIRIT_WATER);
        realm(provider, "midgard", midgardRune, CMItems.SPIRIT_EARTH, CMItems.SPIRIT_WIND);
        realm(provider, "joetunheim", joetunheimRune, CMItems.SPIRIT_EARTH, CMItems.SPIRIT_WATER);
        realm(provider, "muspelheim", muspelheimRune, CMItems.SPIRIT_FIRE, CMItems.SPIRIT_EARTH);
        realm(provider, "niflheim", niflheimRune, CMItems.SPIRIT_WATER, CMItems.SPIRIT_WIND);
        realm(provider, "nidavellir", nidavellirRune, CMItems.SPIRIT_EARTH, CMItems.SPIRIT_FIRE);
        realm(provider, "helheim", helheimRune, CMItems.SPIRIT_SIN, CMItems.SPIRIT_WATER);
    }

    private static void realm(Consumer<FinishedRecipe> provider, String name, Item rune,
                              ItemLike primary, ItemLike secondary) {
        rune(name + "_rune")
                .itemInput(rune)
                .output(primary, 1, 18)
                .output(secondary, 1, 9)
                .output(CMItems.RAINBOW_MANA_SPIRIT, 0, 1)
                .save(provider);
    }

    private static SalvagingRecipeBuilder rune(String id) {
        return SalvagingRecipeBuilder.builder("rune/" + id)
                .requireAdventureModule(false)
                .registerNativeRecipe(true)
                .registerMachineRecipe(true)
                .EUt(EUT)
                .duration(DURATION);
    }
}
