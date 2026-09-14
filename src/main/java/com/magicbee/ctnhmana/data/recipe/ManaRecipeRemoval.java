package com.magicbee.ctnhmana.data.recipe;

import tech.vixhentx.mcmod.ctnhlib.data.recipe.RecipeRemovalHelper;
import tech.vixhentx.mcmod.ctnhlib.data.recipe.RecipeRemovalHelper.RemoveFilter;

import java.util.List;

/**
 * Mana-owned recipe-removal rules registered through CTNH-Lib.
 */
public final class ManaRecipeRemoval {

    private static final List<String> REMOVED_RECIPE_IDS = List.of(
            "gtceu:laser_engraver/engrave_psionic_medulla_exquisite_gem_to_flawless_gem",
            "gtceu:laser_engraver/engrave_psionic_medulla_flawless_gem_to_gem",
            "bloodmagic:sacrificial_dagger",
            "botania:mana_pool",
            "mythicbotany:mana_collector",
            "bloodmagic:path/path_obsidian",
            "bloodmagic:blood_altar",
            "bloodmagic:blood_rune_speed",
            "bloodmagic:blood_rune_blank",
            "bloodmagic:blood_rune_acceleration",
            "bloodmagic:largebloodstonebrick",
            "bloodmagic:blood_rune_sacrifice",
            "bloodmagic:blood_rune_self_sacrifice",
            "bloodmagic:blood_rune_displacement",
            "bloodmagic:blood_rune_capacity",
            "mythicbotany:wither_aconite_petal_apothecary",
            "extrabotany:the_origin",
            "apotheosis:salvaging/common_material",
            "apotheosis:salvaging/uncommon_material",
            "apotheosis:salvaging/rare_material",
            "apotheosis:salvaging/epic_material",
            "apotheosis:salvaging/mythic_material",
            "apotheosis:salvaging/common_gem_dust",
            "apotheosis:salvaging/uncommon_gem_dust",
            "apotheosis:salvaging/rare_gem_dust",
            "apotheosis:salvaging/epic_gem_dust",
            "apotheosis:salvaging/mythic_gem_dust",
            "apotheosis:salvaging/ancient_gem_dust",
            "apotheosis:salvaging/leather_horse_armor",
            "apotheosis:salvaging/iron_horse_armor",
            "apotheosis:salvaging/golden_horse_armor",
            "extrabotany:pleiades_combat_maid_headgear",
            "extrabotany:pleiades_combat_maid_skirt",
            "extrabotany:pleiades_combat_maid_suit",
            "extrabotany:pleiades_combat_maid_boots",
            "apotheosis:salvaging/diamond_horse_armor");

    private ManaRecipeRemoval() {}

    public static void init() {
        REMOVED_RECIPE_IDS.forEach(recipeId -> RecipeRemovalHelper.remove(new RemoveFilter().id(recipeId)));
    }
}