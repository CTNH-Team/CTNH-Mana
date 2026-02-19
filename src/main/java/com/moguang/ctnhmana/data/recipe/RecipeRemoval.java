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
        for (String path : removePaths) {
            registry.accept(ResourceLocation.parse(path));
        }
    }
}