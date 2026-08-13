package com.magicbee.ctnhmana.data.recipe;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RecipeRemoval {

    public static List<String> removePaths = new ArrayList<>();

    public static void init(Consumer<ResourceLocation> registry) {
        removePaths.add("gtceu:laser_engraver/engrave_psionic_medulla_exquisite_gem_to_flawless_gem");
        removePaths.add("gtceu:laser_engraver/engrave_psionic_medulla_flawless_gem_to_gem");
        removePaths.add("bloodmagic:sacrificial_dagger");
        removePaths.add("botania:mana_pool");
        removePaths.add("mythicbotany:mana_collector");
        removePaths.add("bloodmagic:path/path_obsidian");
        removePaths.add("bloodmagic:blood_altar");
        removePaths.add("bloodmagic:blood_rune_speed");
        removePaths.add("bloodmagic:blood_rune_blank");
        removePaths.add("bloodmagic:blood_rune_acceleration");
        removePaths.add("bloodmagic:largebloodstonebrick");
        removePaths.add("bloodmagic:blood_rune_sacrifice");
        removePaths.add("bloodmagic:blood_rune_self_sacrifice");
        removePaths.add("bloodmagic:blood_rune_displacement");
        removePaths.add("bloodmagic:blood_rune_capacity");
        removePaths.add("mythicbotany:wither_aconite_petal_apothecary");
        removePaths.add("extrabotany:the_origin");

        // Apotheosis salvaging table — re-registered under ctnhmana:salvaging/*
        removePaths.add("apotheosis:salvaging/common_material");
        removePaths.add("apotheosis:salvaging/uncommon_material");
        removePaths.add("apotheosis:salvaging/rare_material");
        removePaths.add("apotheosis:salvaging/epic_material");
        removePaths.add("apotheosis:salvaging/mythic_material");
        removePaths.add("apotheosis:salvaging/common_gem_dust");
        removePaths.add("apotheosis:salvaging/uncommon_gem_dust");
        removePaths.add("apotheosis:salvaging/rare_gem_dust");
        removePaths.add("apotheosis:salvaging/epic_gem_dust");
        removePaths.add("apotheosis:salvaging/mythic_gem_dust");
        removePaths.add("apotheosis:salvaging/ancient_gem_dust");
        removePaths.add("apotheosis:salvaging/leather_horse_armor");
        removePaths.add("apotheosis:salvaging/iron_horse_armor");
        removePaths.add("apotheosis:salvaging/golden_horse_armor");
        removePaths.add("apotheosis:salvaging/diamond_horse_armor");

        for (String path : removePaths) {
            registry.accept(ResourceLocation.parse(path));
        }
    }
}
