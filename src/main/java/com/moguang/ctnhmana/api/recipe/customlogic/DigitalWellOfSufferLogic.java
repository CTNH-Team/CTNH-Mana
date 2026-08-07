package com.moguang.ctnhmana.api.recipe.customlogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import dev.shadowsoffire.hostilenetworks.Hostile;
import dev.shadowsoffire.hostilenetworks.item.DataModelItem;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Runtime recipes for Digital Well of Suffer: not-consumable Hostile Networks data model
 * produces Life Essence, scaled by a static model-id tier table (migrated from KJS).
 */
public class DigitalWellOfSufferLogic implements GTRecipeType.ICustomRecipeLogic {

    private static final int DURATION = 50;
    private static final String HN = "hostilenetworks";

    private record Tier(long eut, int essenceMb) {}

    private static final Map<ResourceLocation, Tier> MODEL_TIERS = new LinkedHashMap<>();

    static {
        // LV
        putTier(GTValues.VA[GTValues.LV], 100,
                "chicken", "cod", "cow", "glow_squid", "mooshroom", "pig", "rabbit", "polar_bear", "squid",
                "snow_golem", "sheep");
        // MV
        putTier(GTValues.VA[GTValues.MV], 400,
                "ars_nouveau/wilden_mobs", "blaze", "creeper", "drowned", "ghast", "guardian", "hoglin",
                "magma_cube", "phantom", "skeleton", "slime", "spider", "witch", "zombie", "zombified_piglin",
                "twilightforest/death_tome", "twilightforest/stable_ice_core", "twilightforest/deer",
                "twilightforest/raven");
        // HV
        putTier(GTValues.VA[GTValues.HV], 1600,
                "elder_guardian", "enderman", "evoker", "iron_golem", "shulker", "wither_skeleton", "vindicator",
                "twilightforest/giant", "twilightforest/kobold", "twilightforest/goblin",
                "twilightforest/winter_wolf", "twilightforest/redcap", "twilightforest/helmet_crab",
                "twilightforest/troll", "twilightforest/naga", "twilightforest/minotaur",
                "twilightforest/fire_beetle", "twilightforest/carminite_golem", "twilightforest/towerwood_borer",
                "twilightforest/lich", "twilightforest/yeti", "twilightforest/wraith",
                "twilightforest/skeleton_druid");
        // EV
        putTier(GTValues.VA[GTValues.EV], 6400,
                "artifacts/mimic", "wither", "ender_dragon", "warden",
                "twilightforest/snow_queen", "twilightforest/hydra", "twilightforest/minoshroom",
                "twilightforest/alpha_yeti");
        // IV
        putTier(GTValues.VA[GTValues.IV], 25600, "twilightforest/ur_ghast");
    }

    private static void putTier(long eut, int essenceMb, String... modelPaths) {
        Tier tier = new Tier(eut, essenceMb);
        for (String path : modelPaths) {
            MODEL_TIERS.put(ResourceLocation.fromNamespaceAndPath(HN, path), tier);
        }
    }

    @Override
    public @Nullable GTRecipeDefinition createCustomRecipe(RecipeHandlerGroup holder) {
        var recipeHandlers = holder.getInputHandlerMap().get(ItemRecipeCapability.CAP);
        if (recipeHandlers == null) {
            return null;
        }
        for (var handler : recipeHandlers) {
            for (var content : handler.getContents()) {
                if (!(content instanceof ItemStack stack) || stack.isEmpty()) {
                    continue;
                }
                var recipe = search(stack);
                if (recipe != null) {
                    return recipe;
                }
            }
        }
        return null;
    }

    @Nullable
    public GTRecipeDefinition search(ItemStack stack) {
        if (!stack.is(Hostile.Items.DATA_MODEL.get())) {
            return null;
        }
        var modelHolder = DataModelItem.getStoredModel(stack);
        if (!modelHolder.isBound()) {
            return null;
        }
        ResourceLocation modelId = modelHolder.getId();
        Tier tier = MODEL_TIERS.get(modelId);
        if (tier == null) {
            return null;
        }
        // Runtime: match by item only. Binding the live stack uses StrictNBT, and afterWorking
        // bumps model data each cycle — that would invalidate the next match.
        return buildRuntimeRecipe(modelId, tier);
    }

    private static GTRecipeDefinition buildRuntimeRecipe(ResourceLocation modelId, Tier tier) {
        String path = modelId.getPath().replace('/', '_');
        return CMRecipeTypes.DIGITAL_WELL_OF_SUFFER
                .recipeBuilder(CTNHMana.id("digital_well_of_suffer/" + path))
                .notConsumable(Hostile.Items.DATA_MODEL.get())
                .outputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), tier.essenceMb()))
                .EUt(tier.eut())
                .duration(DURATION)
                .buildRawRecipe();
    }

    @Override
    public void buildRepresentativeRecipes() {
        for (var entry : MODEL_TIERS.entrySet()) {
            ResourceLocation modelId = entry.getKey();
            Tier tier = entry.getValue();
            ItemStack modelStack = new ItemStack(Hostile.Items.DATA_MODEL.get());
            DataModelItem.setStoredModel(modelStack, modelId);
            String path = modelId.getPath().replace('/', '_');
            // XEI: show the concrete model NBT for display only
            var recipe = CMRecipeTypes.DIGITAL_WELL_OF_SUFFER
                    .recipeBuilder(CTNHMana.id("digital_well_of_suffer/" + path))
                    .notConsumable(modelStack)
                    .outputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), tier.essenceMb()))
                    .EUt(tier.eut())
                    .duration(DURATION)
                    .buildRawRecipe();
            CMRecipeTypes.DIGITAL_WELL_OF_SUFFER.addToMainCategory(recipe.withId(recipe.getId().withPrefix("/")));
        }
    }

    @CN("按数据模型等级产出生命精华")
    @EN("Produces Life Essence by Hostile Networks data-model tier")
    public static Lang by_model_tier;
}
