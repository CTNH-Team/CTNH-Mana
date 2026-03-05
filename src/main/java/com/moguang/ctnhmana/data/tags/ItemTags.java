package com.moguang.ctnhmana.data.tags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.registry.CMTags;
import com.tterrag.registrate.providers.RegistrateTagsProvider;

import java.util.Objects;

import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMTags.*;
import static vazkii.botania.common.item.BotaniaItems.*;

public class ItemTags {

    public static void init(RegistrateTagsProvider<Item> provider) {
        create(provider, CMTags.ELEMENT_WATER, runeWater, runeSpring, runeWinter, runeSloth, runeEnvy, runeMana,
                STARLIGHT_RUNE.get(), QUASAR_RUNE.get(), PROLIFERATION_RUNE.get());
        create(provider, CMTags.ELEMENT_FIRE, runeFire, runeSummer, runeLust, runeWrath, runeMana, HORIZEN_RUNE.get(),
                STARLIGHT_RUNE.get(), TWIST_RUNE.get(), QUASAR_RUNE.get());
        create(provider, CMTags.ELEMENT_EARTH, runeEarth, runeAutumn, runeWinter, runeGluttony, runeGreed, runeMana,
                HORIZEN_RUNE.get(), STARLIGHT_RUNE.get(), TWIST_RUNE.get(), QUASAR_RUNE.get(),
                PROLIFERATION_RUNE.get());
        create(provider, CMTags.ELEMENT_WIND, runeAir, runeSpring, runeSummer, runeAutumn, runePride, runeMana,
                HORIZEN_RUNE.get(), QUASAR_RUNE.get(), PROLIFERATION_RUNE.get());
        create(provider, CMTags.ELEMENT_SIN, runeMana, runeLust, runeGluttony, runeGreed, runeSloth, runeWrath,
                runeEnvy, runePride, TWIST_RUNE.get(), QUASAR_RUNE.get());
        create(provider, TIER1_RUNES, runeWater, runeFire, runeEarth, runeAir);
        create(provider, TIER2_RUNES, runeSpring, runeSummer, runeAutumn, runeWinter, runeMana);
        create(provider, TIER3_RUNES, runeLust, runeGluttony, runeGreed, runeSloth, runeWrath, runeEnvy, runePride);
    }

    public static void create(RegistrateTagsProvider<Item> provider, TagKey<Item> tagKey, Item... rls) {
        var builder = provider.addTag(tagKey);
        for (Item item : rls) {
            builder.addOptional(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
        }
    }
}
