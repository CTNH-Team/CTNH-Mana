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
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
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
        ItemStack stack = new ItemStack(BloodMagicBlocks.DEFORESTER_CHARGE.get());
        AnointmentHolder smeltingHolder = new AnointmentHolder();
        smeltingHolder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_SMELTING.get(), new AnointmentData(1, 1, 1));

        AnointmentHolder fortune1Holder = new AnointmentHolder();
        fortune1Holder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_FORTUNE.get(), new AnointmentData(1, 1, 1));

        AnointmentHolder fortune2Holder = new AnointmentHolder();
        fortune2Holder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_FORTUNE.get(), new AnointmentData(2, 1, 1));

        AnointmentHolder silkHolder = new AnointmentHolder();
        silkHolder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_SILK_TOUCH.get(), new AnointmentData(1, 1, 1));

        AnointmentHolder voidHolder = new AnointmentHolder();
        voidHolder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_VOIDING.get(), new AnointmentData(1, 1, 1));

//		smeltingHolder.toItemStack(stack);

        String[] suffixArray = new String[] { "_smelting", "_fortune_1", "_silk_touch", "_voiding" };
        AnointmentHolder[] holderArray = new AnointmentHolder[] { smeltingHolder, fortune1Holder, silkHolder,
                voidHolder };
        Ingredient[] firstIngredientArray = new Ingredient[] { Ingredient.of(Tags.Items.CROPS_NETHER_WART),
                Ingredient.of(Tags.Items.CROPS_NETHER_WART), Ingredient.of(Tags.Items.CROPS_NETHER_WART),
                Ingredient.of(Tags.Items.CROPS_NETHER_WART) };
        Ingredient[] secondIngredientArray = new Ingredient[] { Ingredient.of(Items.FURNACE),
                Ingredient.of(Tags.Items.DUSTS_REDSTONE), Ingredient.of(Items.COBWEB),
                Ingredient.of(Blocks.NETHERRACK) };
        Ingredient[] thirdIngredientArray = new Ingredient[] { Ingredient.of(Items.CHARCOAL, Items.COAL),
                Ingredient.of(BloodMagicTags.DUST_COAL), Ingredient.of(Tags.Items.NUGGETS_GOLD),
                Ingredient.of(Blocks.COBBLED_DEEPSLATE) };
        for (int i = 0; i < suffixArray.length; i++)
        {
            ItemStack deforesterStack = new ItemStack(BloodMagicBlocks.DEFORESTER_CHARGE.get());
            ItemStack fungalStack = new ItemStack(BloodMagicBlocks.FUNGAL_CHARGE.get());
            ItemStack shapedStack = new ItemStack(BloodMagicBlocks.SHAPED_CHARGE.get());
            ItemStack veinStack = new ItemStack(BloodMagicBlocks.VEINMINE_CHARGE.get());
            AnointmentHolder holder = holderArray[i];
            holder.toItemStack(deforesterStack);
            holder.toItemStack(fungalStack);
            holder.toItemStack(shapedStack);
            holder.toItemStack(veinStack);
            TartaricForgeRecipeBuilder.builder("shaped_charge" + suffixArray[i])
                    .minimumSouls(60)
                    .soulDrain(1)
                    .input(Ingredient.of(BloodMagicItems.SHAPED_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(shapedStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("deforester_charge" + suffixArray[i])
                    .minimumSouls(60)
                    .soulDrain(1)
                    .input(Ingredient.of(BloodMagicItems.DEFORESTER_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(deforesterStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("vein_charge" + suffixArray[i])
                    .minimumSouls(60)
                    .soulDrain(1)
                    .input(Ingredient.of(BloodMagicItems.VEINMINE_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(veinStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("fungal_charge" + suffixArray[i])
                    .minimumSouls(60)
                    .soulDrain(1)
                    .input(Ingredient.of(BloodMagicItems.FUNGAL_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(fungalStack)
                    .save(provider);
        }
        String[] suffixArray2 = new String[] { "_smelting_l", "_fortune_1_l", "_fortune_2_l", "_silk_touch_l",
                "_voiding" };
        AnointmentHolder[] holderArray2 = new AnointmentHolder[] { smeltingHolder, fortune1Holder, fortune2Holder,
                silkHolder, voidHolder };
        Ingredient[] ingredientArray2 = new Ingredient[] { Ingredient.of(BloodMagicItems.SMELTING_ANOINTMENT_L.get()),
                Ingredient.of(BloodMagicItems.FORTUNE_ANOINTMENT_L.get()),
                Ingredient.of(BloodMagicItems.FORTUNE_ANOINTMENT_2.get()),
                Ingredient.of(BloodMagicItems.SILK_TOUCH_ANOINTMENT_L.get()),
                Ingredient.of(BloodMagicItems.VOIDING_ANOINTMENT_L.get()) };
        for (int i = 0; i < suffixArray2.length; i++)
        {
            ItemStack deforester2Stack = new ItemStack(BloodMagicBlocks.DEFORESTER_CHARGE_2.get());
            ItemStack vein2Stack = new ItemStack(BloodMagicBlocks.VEINMINE_CHARGE_2.get());
            ItemStack fungal2Stack = new ItemStack(BloodMagicBlocks.FUNGAL_CHARGE_2.get());
            ItemStack shapedChargeDeepStack = new ItemStack(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get());
            ItemStack augShapedStack = new ItemStack(BloodMagicBlocks.AUG_SHAPED_CHARGE.get());
            AnointmentHolder holder = holderArray2[i];
            holder.toItemStack(deforester2Stack);
            holder.toItemStack(vein2Stack);
            holder.toItemStack(fungal2Stack);
            holder.toItemStack(shapedChargeDeepStack);
            holder.toItemStack(augShapedStack);
            TartaricForgeRecipeBuilder.builder("deforester_charge_2" + suffixArray2[i])
                    .minimumSouls(300)
                    .soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.DEFORESTER_CHARGE_2.get()),ingredientArray2[i])
                    .output(deforester2Stack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("deforester_charge_2" + suffixArray2[i])
                    .minimumSouls(300)
                    .soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.DEFORESTER_CHARGE_2.get()),ingredientArray2[i])
                    .output(deforester2Stack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("vein_charge_2" + suffixArray2[i])
                    .minimumSouls(300)
                    .soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.VEINMINE_CHARGE_2.get()),ingredientArray2[i])
                    .output(vein2Stack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("fungal_charge_2" + suffixArray2[i])
                    .minimumSouls(300)
                    .soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.FUNGAL_CHARGE_2.get()),ingredientArray2[i])
                    .output(fungal2Stack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("shaped_charge_deep" + suffixArray2[i])
                    .minimumSouls(300)
                    .soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get()),ingredientArray2[i])
                    .output(shapedChargeDeepStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("aug_shaped_charge" + suffixArray2[i])
                    .minimumSouls(300)
                    .soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.AUG_SHAPED_CHARGE.get()),ingredientArray2[i])
                    .output(augShapedStack)
                    .save(provider);
        }
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
