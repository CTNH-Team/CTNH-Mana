package com.moguang.ctnhmana.data.recipe;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RecipeRemoval {

    public static List<String> removePaths = new ArrayList<>();

    public static void init(Consumer<ResourceLocation> registry) {
        removePaths.add("gtceu:laser_engraver/engrave_psionic_medulla_exquisite_gem_to_flawless_gem");
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
        for (String path : removePaths) {
            registry.accept(ResourceLocation.parse(path));
        }
    }
}