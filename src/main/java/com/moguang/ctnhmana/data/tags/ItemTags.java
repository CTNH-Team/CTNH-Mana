package com.moguang.ctnhmana.data.tags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.registry.CMTags;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.shadowsoffire.apotheosis.adventure.Adventure;

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
        create(provider, SPIRE_UPDATE, UPGRADE_RUNE_SPEED_1.get(), UPGRADE_RUNE_SPEED_2.get(),
                UPGRADE_RUNE_SPEED_3.get(),
                UPGRADE_RUNE_RANGE_1.get(), UPGRADE_RUNE_RANGE_2.get(), UPGRADE_RUNE_RANGE_3.get(),
                UPGRADE_RUNE_ALPHA.get(),
                UPGRADE_RUNE_TRANSLOCATION_1.get(), UPGRADE_RUNE_TRANSLOCATION_2.get(),
                UPGRADE_RUNE_TRANSLOCATION_3.get(),
                UPGRADE_RUNE_OMEGA.get());
        // 神话宝石仅一种 Item，NBT 区分具体种类；Tag 供携刻机通用 EMI 配方查表
        create(provider, APOTHEOSIS_GEMS, Adventure.Items.GEM.get());
    }

    public static void create(RegistrateTagsProvider<Item> provider, TagKey<Item> tagKey, Item... rls) {
        var builder = provider.addTag(tagKey);
        for (Item item : rls) {
            builder.addOptional(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
        }
    }
}
