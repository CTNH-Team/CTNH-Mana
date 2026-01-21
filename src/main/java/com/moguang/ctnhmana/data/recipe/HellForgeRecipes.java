package com.moguang.ctnhmana.data.recipe;

import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import com.moguang.ctnhmana.common.recipe.builder.bloodmagic.TartaricForgeRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.extensions.IForgeTagAppender;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentData;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.core.AnointmentRegistrar;

import static com.moguang.ctnhmana.registry.CMMaterials.tagPrefixIgnore;
import static wayoftime.bloodmagic.anointment.Anointment.*;
import java.util.function.Consumer;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;
import static com.moguang.ctnhmana.registry.CMItems.HORIZEN_RUNE;
import static com.moguang.ctnhmana.registry.CMMaterials.Zenith_essence;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.HELL_FORGE_RECIPES;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.MANA_REACTOR_RECIPES;
import static vazkii.botania.common.item.BotaniaItems.*;

public class HellForgeRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        HELL_FORGE_RECIPES.recipeBuilder("testxxx")//工业锻造独有配方
                .addCondition(new HellForgeCondition(10))
                .inputItems(runeFire,24)
                .inputFluids(Zenith_essence.getFluid(144))
                .outputItems(HORIZEN_RUNE)
                .duration(200)
                .circuitMeta(19)
                .EUt(114514)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("testxxy")
                .input(runeFire,runeMana,runeAir)
                .output(new ItemStack(runeEnvy))
                .minimumSouls(1000)
                .soulDrain(100)
                .circuitMeta(21)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("output_routing_node")
                .input(new ItemStack(Items.REDSTONE,1))
                .input(new ItemStack(Items.IRON_INGOT,1))
                .input(ROUTING_NODE_BLOCK_ITEM.get())
                .input(new ItemStack(Items.GLOWSTONE_DUST,1))
                .output(new ItemStack(OUTPUT_ROUTING_NODE_BLOCK_ITEM.get()))
                .minimumSouls(200)
                .soulDrain(60)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("steadfast_crystal_block_item")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(STEADFAST_CRYSTAL)
                .inputItems(STEADFAST_CRYSTAL)
                .inputItems(STEADFAST_CRYSTAL)
                .inputItems(STEADFAST_CRYSTAL)
                .outputItems(STEADFAST_CRYSTAL_BLOCK_ITEM,1)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("deforester_charge_item")
                .addCondition(new HellForgeCondition(10))
                .inputItems(Items.OAK_PLANKS,1)
                .inputItems(Items.COBBLESTONE,1)
                .inputItems(Items.COAL,1)
                .inputItems(Items.DARK_OAK_LOG,1)
                .outputItems(DEFORESTER_CHARGE_ITEM,8)
                .duration(200)
                .EUt(200)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("throwing_dagger_syringe")
                .addCondition(new HellForgeCondition(10))
                .inputItems(Items.GLASS,1)
                .inputItems(Items.ANDESITE,1)
                .outputItems(THROWING_DAGGER_SYRINGE,8)
                .duration(200)
                .EUt(200)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("vengeful_crystal_catalyst")
                .addCondition(new HellForgeCondition(400))
                .inputItems(SULFUR,1)
                .inputItems(TAU_OIL,1)
                .inputItems(Items.NETHER_WART,1)
                .inputItems(Items.MELON_SEEDS,1)
                .outputItems(VENGEFUL_CRYSTAL_CATALYST,8)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
    }
}
