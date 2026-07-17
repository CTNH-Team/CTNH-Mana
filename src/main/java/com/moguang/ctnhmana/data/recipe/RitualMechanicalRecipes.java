package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.common.multiblock.RitualMechanicalMachine;
import com.moguang.ctnhmana.registry.CMMaterials;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.IV;
import static com.gregtechceu.gtceu.api.GTValues.V;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.RITUAL_RECIPES;
import static dev.shadowsoffire.apotheosis.ench.Ench.Items.INFUSED_BREATH;

/**
 * 工业血祭仪式阵（{@link com.moguang.ctnhmana.registry.CMMultiblockMachines#RITUAL_MECHANICAL_ARRAY}）
 * 的 datagen 配方。
 * <p>
 * 每条配方对应一次血魔法 {@link wayoftime.bloodmagic.ritual.Ritual#performRitual} 调用；
 * {@code duration} 为机器冷却，触媒物品各仪式互不相同。
 */
public class RitualMechanicalRecipes {

    /** Ender IO 经验之汁，中文社区常称「液态经验」 */
    private static final FluidIngredient LIQUID_EXPERIENCE = FluidIngredient
            .of(ForgeRegistries.FLUIDS.getValue(ResourceLocation.parse("enderio:xp_juice")), 1000);

    public static void init(Consumer<FinishedRecipe> provider) {
        // ── 原版 Blood Magic 仪式 ──────────────────────────────────────
        ritual(provider, "water", "water", Items.WATER_BUCKET, 100);
        ritual(provider, "speed", "speed", Items.SUGAR, 100);
        ritual(provider, "animal_growth", "animal_growth", Items.CARROT, 100);
        ritual(provider, "armour_evolve", "armour_evolve", ChemicalHelper.get(dust, CMMaterials.DEMON), 200);
        ritual(provider, "condor", "condor", Items.FEATHER, 100);
        ritual(provider, "crafting", "crafting", Items.CRAFTING_TABLE, 100);
        ritual(provider, "crushing", "crushing", Items.COBBLESTONE, 100);
        ritual(provider, "crystal_harvest", "crystal_harvest", Items.QUARTZ, 100);
        ritual(provider, "crystal_split", "crystal_split", Items.AMETHYST_SHARD, 100);
        ritual(provider, "ellipsoid", "ellipsoid", Items.GLASS, 100);
        ritual(provider, "feathered_knife", "feathered_knife", Items.FLINT, 200);
        ritual(provider, "felling", "felling", Items.IRON_AXE, 100);
        ritual(provider, "forsaken_soul", "forsaken_soul", Items.SOUL_SAND, 200);
        ritual(provider, "full_stomach", "full_stomach", Items.BREAD, 100);
        ritual(provider, "geode", "geode", Items.CALCITE, 200);
        ritual(provider, "green_grove", "green_grove", Items.BONE_MEAL, 100);
        ritual(provider, "grounding", "grounding", ChemicalHelper.get(dust, CMMaterials.TerraSteel), 100);
        ritual(provider, "jumping", "jumping", Items.SLIME_BALL, 100);
        ritual(provider, "lava", "lava", Items.MAGMA_CREAM, 100);
        ritual(provider, "downgrade", "downgrade", Items.ROTTEN_FLESH, 100);
        ritual(provider, "magnetism", "magnetism", Items.IRON_INGOT, 100);
        ritual(provider, "meteor", "meteor", Items.FIRE_CHARGE, 400);
        ritual(provider, "placer", "placer", Items.DISPENSER, 100);
        ritual(provider, "regeneration", "regeneration", Items.GOLDEN_APPLE, 200);
        ritual(provider, "sphere", "sphere", Items.SNOWBALL, 100);
        ritual(provider, "upgrade_remove", "upgrade_remove", Items.SHEARS, 100);
        ritual(provider, "well_of_suffering", "well_of_suffering", Items.IRON_BARS, 200);
        ritual(provider, "yawning_void", "yawning_void", Items.ENDER_EYE, 200);
        ritual(provider, "zephyr", "zephyr", Items.PHANTOM_MEMBRANE, 100);

        // ── CTNH 扩展仪式 ──────────────────────────────────────────────
        ritual(provider, "extractor", "extractor", ChemicalHelper.get(dust, CMMaterials.ManaSteel), 100,
                new ItemStack(Items.REDSTONE, 1));
        // 虚境之视：先合成灌注龙息，配方完成后执行仪式（需主人在线）
        RITUAL_RECIPES.recipeBuilder("shroudsight")
                .inputItems(Items.DRAGON_BREATH, 1)
                .inputFluids(LIQUID_EXPERIENCE)
                .inputFluids(CMMaterials.Zenith_essence.getFluid(20))
                .outputItems(INFUSED_BREATH.get(), 1)
                .duration(600)
                .EUt(V[IV])
                .addData(RitualMechanicalMachine.RECIPE_DATA_RITUAL_ID, "shroudsight")
                .save(provider);
        ritual(provider, "bosssummon", "bosssummon", Items.WITHER_SKELETON_SKULL, 400);
        ritual(provider, "dragoncloud", "dragoncloud", Items.DRAGON_BREATH, 300);
        ritual(provider, "manacharger", "manacharger", BotaniaItems.manaPearl, 100);
    }

    private static void ritual(Consumer<FinishedRecipe> provider, String recipeId, String ritualId,
                               ItemLike input, int duration) {
        ritual(provider, recipeId, ritualId, new ItemStack(input), duration, null);
    }

    private static void ritual(Consumer<FinishedRecipe> provider, String recipeId, String ritualId,
                               ItemStack input, int duration) {
        ritual(provider, recipeId, ritualId, input, duration, null);
    }

    private static void ritual(Consumer<FinishedRecipe> provider, String recipeId, String ritualId,
                               ItemStack input, int duration, @Nullable ItemStack output) {
        var builder = RITUAL_RECIPES.recipeBuilder(recipeId)
                .inputItems(input, 1)
                .duration(duration)
                .EUt(1)
                .addData(RitualMechanicalMachine.RECIPE_DATA_RITUAL_ID, ritualId);
        if (output != null) {
            builder.outputItems(output);
        }
        builder.save(provider);
    }
}
