package com.moguang.ctnhmana.data.tags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.registry.CMTags;
import com.tterrag.registrate.providers.RegistrateTagsProvider;

import java.util.Objects;

import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMTags.*;
import static mythicbotany.register.ModItems.*;
import static vazkii.botania.common.item.BotaniaItems.*;

public class ItemTags {

    public static void init(RegistrateTagsProvider<Item> provider) {
        create(provider, CMTags.ELEMENT_WATER, runeWater, runeSpring, runeWinter, runeSloth, runeMana,
                STARLIGHT_RUNE.get(), QUASAR_RUNE.get(), PROLIFERATION_RUNE.get(),
                vanaheimRune, alfheimRune, joetunheimRune, niflheimRune, helheimRune);
        create(provider, CMTags.ELEMENT_FIRE, runeFire, runeSummer, runeWrath, runeMana, HORIZEN_RUNE.get(),
                STARLIGHT_RUNE.get(), TWIST_RUNE.get(), QUASAR_RUNE.get(),
                asgardRune, muspelheimRune, nidavellirRune);
        create(provider, CMTags.ELEMENT_EARTH, runeEarth, runeAutumn, runeWinter, runeGluttony, runeMana,
                HORIZEN_RUNE.get(), STARLIGHT_RUNE.get(), TWIST_RUNE.get(), QUASAR_RUNE.get(),
                PROLIFERATION_RUNE.get(),
                vanaheimRune, midgardRune, joetunheimRune, muspelheimRune, nidavellirRune);
        create(provider, CMTags.ELEMENT_WIND, runeAir, runeSpring, runeSummer, runeAutumn, runeMana,
                HORIZEN_RUNE.get(), QUASAR_RUNE.get(), PROLIFERATION_RUNE.get(),
                asgardRune, alfheimRune, midgardRune, niflheimRune);
        create(provider, CMTags.ELEMENT_SIN, runeMana, runeLust, runeGluttony, runeGreed, runeSloth, runeWrath,
                runeEnvy, runePride, TWIST_RUNE.get(), QUASAR_RUNE.get(), helheimRune);
        create(provider, TIER1_RUNES, runeWater, runeFire, runeEarth, runeAir);
        create(provider, TIER2_RUNES, runeSpring, runeSummer, runeAutumn, runeWinter, runeMana);
        create(provider, TIER3_RUNES, runeLust, runeGluttony, runeGreed, runeSloth, runeWrath, runeEnvy, runePride);
        create(provider, TIER4_RUNES, asgardRune, vanaheimRune, alfheimRune, midgardRune, joetunheimRune,
                muspelheimRune, niflheimRune, nidavellirRune, helheimRune);
        create(provider, TIER5_RUNES, HORIZEN_RUNE.get(), STARLIGHT_RUNE.get(), TWIST_RUNE.get(), QUASAR_RUNE.get(),
                PROLIFERATION_RUNE.get());
    }

    public static void create(RegistrateTagsProvider<Item> provider, TagKey<Item> tagKey, Item... rls) {
        var builder = provider.addTag(tagKey);
        for (Item item : rls) {
            builder.addOptional(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
        }
    }
}
