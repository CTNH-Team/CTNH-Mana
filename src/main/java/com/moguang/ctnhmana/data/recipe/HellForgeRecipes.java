package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import com.moguang.ctnhmana.common.recipe.builder.bloodmagic.TartaricForgeRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.extensions.IForgeTagAppender;
import vazkii.botania.common.block.BotaniaBlocks;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentData;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.core.AnointmentRegistrar;
import net.minecraftforge.registries.ForgeRegistries;

import static com.moguang.ctnhmana.registry.CMBlocks.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.Material.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static net.minecraft.world.item.Items.*;
import static wayoftime.bloodmagic.anointment.Anointment.*;
import java.util.function.Consumer;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;
import static com.moguang.ctnhmana.registry.CMItems.HORIZEN_RUNE;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static vazkii.botania.common.item.BotaniaItems.*;
@SuppressWarnings("removal")
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
        Ingredient[] secondIngredientArray = new Ingredient[] { Ingredient.of(FURNACE),
                Ingredient.of(Tags.Items.DUSTS_REDSTONE), Ingredient.of(COBWEB),
                Ingredient.of(Blocks.NETHERRACK) };
        Ingredient[] thirdIngredientArray = new Ingredient[] { Ingredient.of(CHARCOAL, COAL),
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
                    .input(Ingredient.of(SHAPED_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(shapedStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("deforester_charge" + suffixArray[i])
                    .minimumSouls(60)
                    .soulDrain(1)
                    .input(Ingredient.of(DEFORESTER_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(deforesterStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("vein_charge" + suffixArray[i])
                    .minimumSouls(60)
                    .soulDrain(1)
                    .input(Ingredient.of(VEINMINE_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(veinStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("fungal_charge" + suffixArray[i])
                    .minimumSouls(60)
                    .soulDrain(1)
                    .input(Ingredient.of(FUNGAL_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(fungalStack)
                    .save(provider);
        }
        String[] suffixArray2 = new String[] { "_smelting_l", "_fortune_1_l", "_fortune_2_l", "_silk_touch_l",
                "_voiding" };
        AnointmentHolder[] holderArray2 = new AnointmentHolder[] { smeltingHolder, fortune1Holder, fortune2Holder,
                silkHolder, voidHolder };
        Ingredient[] ingredientArray2 = new Ingredient[] { Ingredient.of(SMELTING_ANOINTMENT_L.get()),
                Ingredient.of(FORTUNE_ANOINTMENT_L.get()),
                Ingredient.of(FORTUNE_ANOINTMENT_2.get()),
                Ingredient.of(SILK_TOUCH_ANOINTMENT_L.get()),
                Ingredient.of(VOIDING_ANOINTMENT_L.get()) };
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
                .input(new ItemStack(REDSTONE,1))
                .input(new ItemStack(IRON_INGOT,1))
                .input(ROUTING_NODE_BLOCK_ITEM.get())
                .input(new ItemStack(GLOWSTONE_DUST,1))
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
                .inputItems(OAK_PLANKS,1)
                .inputItems(COBBLESTONE,1)
                .inputItems(COAL,1)
                .inputItems(DARK_OAK_LOG,1)
                .outputItems(DEFORESTER_CHARGE_ITEM,8)
                .duration(200)
                .EUt(200)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("throwing_dagger_syringe")
                .addCondition(new HellForgeCondition(10))
                .inputItems(GLASS,1)
                .inputItems(ANDESITE,1)
                .outputItems(THROWING_DAGGER_SYRINGE,8)
                .duration(200)
                .EUt(200)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("vengeful_crystal_catalyst")
                .addCondition(new HellForgeCondition(400))
                .inputItems(SULFUR,1)
                .inputItems(TAU_OIL,1)
                .inputItems(NETHER_WART,1)
                .inputItems(MELON_SEEDS,1)
                .outputItems(VENGEFUL_CRYSTAL_CATALYST,8)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("vengeful_crystal_catalyst")
                .addCondition(new HellForgeCondition(400))
                .inputItems(SULFUR,1)
                .inputItems(TAU_OIL,1)
                .inputItems(NETHER_WART,1)
                .inputItems(MELON_SEEDS,1)
                .outputItems(VENGEFUL_CRYSTAL_CATALYST,8)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("amethyst_throwing_dagger")
                .addCondition(new HellForgeCondition(32))
                .inputItems(AMETHYST_SHARD,1)
                .inputItems(COPPER_INGOT,2)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("bloodmagic:amethystthrowingdagger")),16)
                .duration(200)
                .EUt(128000/200)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("hellforged_parts")
                .addCondition(new HellForgeCondition(1000))
                .inputItems(HELLFORGED_PARTS)
                .inputItems(NETHERITE_SCRAP,1)
                .inputItems(DIAMOND,1)
                .inputItems(RAW_CRYSTAL.get())
                .outputItems(HELLFORGED_PARTS,2)
                .duration(200)
                .EUt(4000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("destructive_crystal_block_item")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(DESTRUCTIVE_CRYSTAL)
                .inputItems(DESTRUCTIVE_CRYSTAL)
                .inputItems(DESTRUCTIVE_CRYSTAL)
                .inputItems(DESTRUCTIVE_CRYSTAL)
                .outputItems(DESTRUCTIVE_CRYSTAL_BLOCK_ITEM,1)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("destructive_crystal_catalyst")
                .addCondition(new HellForgeCondition(400))
                .inputItems(NETHER_WART)
                .inputItems(BEETROOT)
                .inputItems(TAU_OIL)
                .inputItems(SULFUR)
                .outputItems(DESTRUCTIVE_CRYSTAL_CATALYST,1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("lesser_gem")
                .addCondition(new HellForgeCondition(60))
                .inputItems(PETTY_GEM)
                .inputItems(LAPIS_BLOCK)
                .inputItems(REDSTONE_BLOCK)
                .inputItems(DIAMOND)
                .outputItems(LESSER_GEM,1)
                .duration(200)
                .EUt(1200)
                .circuitMeta(1)
                .save(provider);
//todo     HELL_FORGE_RECIPES.recipeBuilder("wither_soul_vial")
//                .addCondition(new HellForgeCondition(450))//恶魔意志消耗量
//                .inputItems(ChemicalHelper.get(block, GTMaterials.NetherStar))//下界之星块
//                .inputItems(REAGENT_BLOOD_LIGHT)//血光试剂
//                .inputItems(REAGENT_BINDING)//束缚试剂
//                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("enderio:empty_soul_vial")))//空灵魂瓶
//                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("enderio:filled_soul_vial"," {BlockEntityTag:{EntityStorage:{Entity:{id:\"minecraft:wither\"}}}}")))//凋灵灵魂瓶
//                .duration(200)
//                .EUt(8000)
//                .circuitMeta(1)
//                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("miners_key")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(COPPER_INGOT)
                .inputItems(HELLFORGED_INGOT)
                .inputItems(IMBUED_SLATE)
                .outputItems(DUNGEON_MINE_KEY,1)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("corrosive_crystal_catalyst")
                .addCondition(new HellForgeCondition(400))
                .inputItems(NETHER_WART)
                .inputItems(WHEAT_SEEDS)
                .inputItems(TAU_OIL)
                .inputItems(SULFUR)
                .outputItems(CORROSIVE_CRYSTAL_CATALYST,1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("demon_pylon_item")
                .addCondition(new HellForgeCondition(400))
                .inputItems(DEMON_CRUCIBLE_ITEM)
                .inputItems(REAGENT_TELEPOSITION)
                .inputItems(REAGENT_SUPPRESSION)
                .inputItems(DEMON_CRYSTALLIZER_ITEM)
                .outputItems(DEMON_PYLON_ITEM,1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("sentient_scythe")
                .addCondition(new HellForgeCondition(0))
                .inputItems(IRON_HOE)
                .inputItems(PETTY_GEM)
                .outputItems(SENTIENT_SCYTHE,1)
                .duration(200)
                .EUt(30)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("demon_crucible_item")
                .addCondition(new HellForgeCondition(400))
                .inputItems(REAGENT_SIGHT)
                .inputItems(REAGENT_VOID)
                .inputItems(CAULDRON)
                .inputItems(REINFORCED_SLATE)
                .outputItems(DEMON_CRUCIBLE_ITEM,1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("sentient_axe")
                .addCondition(new HellForgeCondition(0))
                .inputItems(IRON_AXE)
                .inputItems(PETTY_GEM)
                .outputItems(SENTIENT_AXE,1)
                .duration(200)
                .EUt(30)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("steadfast_crystal_catalyst")
                .addCondition(new HellForgeCondition(400))
                .inputItems(NETHER_WART)
                .inputItems(PUMPKIN_SEEDS)
                .inputItems(TAU_OIL)
                .inputItems(SULFUR)
                .outputItems(STEADFAST_CRYSTAL_CATALYST,1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("veinmine_charge_2_item")
                .addCondition(new HellForgeCondition(80))
                .inputItems(SAND)
                .inputItems(COPPER_BLOCK)
                .inputItems(COAL)
                .inputItems(SANDSTONE)
                .outputItems(VEINMINE_CHARGE_2_ITEM,1)
                .duration(200)
                .EUt(1600)
                .circuitMeta(2)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("corrosive_crystal_catalyst")
                .addCondition(new HellForgeCondition(1000))
                .inputItems(COMMON_GEM)
                .inputItems(DEMONIC_SLATE)
                .inputItems(WEAK_BLOOD_SHARD)
                .inputItems(RAW_CRYSTAL.get())
                .outputItems(GREATER_GEM,1)
                .duration(200)
                .EUt(20000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("aug_shaped_charge_item")
                .addCondition(new HellForgeCondition(80))
                .inputItems(SAND)
                .inputItems(COPPER_BLOCK)
                .inputItems(COAL)
                .inputItems(BRICK)
                .outputItems(AUG_SHAPED_CHARGE_ITEM,6)
                .duration(200)
                .EUt(1600)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("resonator")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(RAW_CRYSTAL)
                .inputItems(COPPER_BLOCK)
                .inputItems(ANDESITE)
                .outputItems(RESONATOR,1)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("node_router")
                .addCondition(new HellForgeCondition(400))
                .inputItems(LAPIS_LAZULI)
                .inputItems(LAPIS_LAZULI)
                .inputItems(REINFORCED_SLATE)
                .inputItems(STICK)
                .outputItems(NODE_ROUTER,1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("vengeful_crystal_block_item")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(VENGEFUL_CRYSTAL)
                .inputItems(VENGEFUL_CRYSTAL)
                .inputItems(VENGEFUL_CRYSTAL)
                .inputItems(VENGEFUL_CRYSTAL)
                .outputItems(VENGEFUL_CRYSTAL_BLOCK_ITEM,1)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("raw_crystal_block_item")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(RAW_CRYSTAL)
                .inputItems(RAW_CRYSTAL)
                .inputItems(RAW_CRYSTAL)
                .inputItems(RAW_CRYSTAL)
                .outputItems(RAW_CRYSTAL_BLOCK_ITEM,1)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("petty_gem")
                .addCondition(new HellForgeCondition(1))
                .inputItems(REDSTONE)
                .inputItems(GLASS)
                .inputItems(GOLD_INGOT)
                .inputItems(LAPIS_LAZULI)
                .outputItems(PETTY_GEM,1)
                .duration(200)
                .EUt(20)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("rune_winter")
                .addCondition(new HellForgeCondition(200))
                .inputItems(runeWater)
                .inputItems(runeEarth)
                .inputItems(SNOW_BLOCK)
                .inputItems(PACKED_ICE)
                .outputItems(runeWinter)
                .duration(200)
                .EUt(4000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("veinmine_charge_item")
                .addCondition(new HellForgeCondition(10))
                .inputItems(SANDSTONE)
                .inputItems(COBBLESTONE)
                .inputItems(SAND)
                .inputItems(COAL)
                .outputItems(VEINMINE_CHARGE_ITEM,8)
                .duration(200)
                .EUt(400)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("field_restriction_casing")
                .addCondition(new HellForgeCondition(2000))
                .inputItems(CASING_FORCE_FILED.asItem(),8)
                .inputItems(ChemicalHelper.get(gear,GTMaterials.TungstenSteel),8)
                .inputItems(ENDSLATE)
                .outputItems(FIELD_RESTRICTION_CASING.asItem(),4)
                .duration(200)
                .EUt(10000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("demon_crystallizer")
                .addCondition(new HellForgeCondition(500))
                .inputItems(SOUL_FORGE_ITEM)
                .inputItems(REAGENT_MAGNETISM)
                .inputItems(REAGENT_BLOOD_LIGHT)
                .inputItems(BotaniaBlocks.manaGlass.asItem())
                .outputItems(DEMON_CRYSTALLIZER_ITEM)
                .duration(200)
                .EUt(10000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("input_routing_node_block_item")
                .addCondition(new HellForgeCondition(400))
                .inputItems(GOLD_INGOT)
                .inputItems(ROUTING_NODE_BLOCK_ITEM)
                .inputItems(REDSTONE)
                .inputItems(GLOWSTONE)
                .outputItems(INPUT_ROUTING_NODE_BLOCK_ITEM,1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("fused_demon_mixed_mana_dust")
                .addCondition(new HellForgeCondition(2000))
                .inputItems(ChemicalHelper.get(dust,Fused_Lp_Mixed_Mana),16)
                .notConsumable(ETHEREAL_SLATE)
                .outputItems(ChemicalHelper.get(dust,Fused_demon_mixed),12)
                .duration(200)
                .EUt(1200)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("common_gem")
                .addCondition(new HellForgeCondition(240))
                .inputItems(GOLD_BLOCK)
                .inputItems(DIAMOND)
                .inputItems(IMBUED_SLATE)
                .inputItems(LESSER_GEM)
                .outputItems(COMMON_GEM,1)
                .duration(200)
                .EUt(4800)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("sentient_pickaxe")
                .addCondition(new HellForgeCondition(0))
                .inputItems(PETTY_GEM)
                .inputItems(IRON_PICKAXE)
                .outputItems(SENTIENT_PICKAXE,1)
                .duration(200)
                .EUt(20)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("deforester_charge_2_item")
                .addCondition(new HellForgeCondition(80))
                .inputItems(DARK_OAK_LOG)
                .inputItems(CHARCOAL)
                .inputItems(COPPER_BLOCK)
                .inputItems(OAK_PLANKS)
                .outputItems(DEFORESTER_CHARGE_2_ITEM,8)
                .duration(200)
                .EUt(1600)
                .circuitMeta(2)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("sanguine_reverter")
                .addCondition(new HellForgeCondition(350))
                .inputItems(IMBUED_SLATE)
                .inputItems(ANDESITE)
                .inputItems(IRON_INGOT)
                .inputItems(SHEARS)
                .outputItems(SANGUINE_REVERTER)
                .duration(200)
                .EUt(7000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("shaped_charge_item")
                .addCondition(new HellForgeCondition(80))
                .inputItems(SAND)
                .inputItems(COPPER_BLOCK)
                .inputItems(CHARCOAL)
                .inputItems(ANDESITE)
                .outputItems(SHAPED_CHARGE_ITEM,4)
                .duration(200)
                .EUt(1600)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("corrosive_crystal_block_item")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(CORROSIVE_CRYSTAL)
                .inputItems(CORROSIVE_CRYSTAL)
                .inputItems(CORROSIVE_CRYSTAL)
                .inputItems(CORROSIVE_CRYSTAL)
                .outputItems(CORROSIVE_CRYSTAL_BLOCK_ITEM,1)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("sentient_sword")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(IRON_SWORD)
                .inputItems(PETTY_GEM)
                .outputItems(SENTIENT_SWORD,1)
                .duration(200)
                .EUt(30)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("demon_will_gauge")
                .addCondition(new HellForgeCondition(400))
                .inputItems(RAW_CRYSTAL)
                .inputItems(GLASS)
                .inputItems(GOLD_INGOT)
                .inputItems(REDSTONE)
                .outputItems(DEMON_WILL_GAUGE,1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("rune_spring")
                .addCondition(new HellForgeCondition(200))
                .inputItems(runeWater)
                .inputItems(runeFire)
                .inputItems(WHEAT_SEEDS)
                .inputItems(CHERRY_SAPLING)
                .outputItems(runeSpring,1)
                .duration(200)
                .EUt(4000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("fungal_charge_item")
                .addCondition(new HellForgeCondition(10))
                .inputItems(BROWN_MUSHROOM)
                .inputItems(CHARCOAL)
                .inputItems(COBBLESTONE)
                .outputItems(FUNGAL_CHARGE_ITEM,8)
                .duration(200)
                .EUt(200)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("prismarine_crystalline_resonator")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(RAW_CRYSTAL)
                .inputItems(TAU_OIL)
                .inputItems(AMETHYST_SHARD)
                .inputItems(ChemicalHelper.get(ingot,GTMaterials.Vanadium))
                .outputItems(PRIMITIVE_CRYSTALLINE_RESONATOR,1)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("raw_crystal1")
                .addCondition(new HellForgeCondition(1000))
                .inputItems(AMETHYST_SHARD,8)
                .notConsumable(ENDSLATE)
                .inputItems(CORROSIVE_CRYSTAL)
                .outputItems(RAW_CRYSTAL,1)
                .duration(200)
                .EUt(10000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("sentient_shovel")
                .addCondition(new HellForgeCondition(0))
                .inputItems(IRON_SHOVEL)
                .inputItems(PETTY_GEM)
                .outputItems(SENTIENT_SHOVEL,1)
                .duration(200)
                .EUt(30)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("corrosive_crystal_block_item")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(IRON_INGOT)
                .inputItems(IRON_INGOT)
                .inputItems(STRING)
                .outputItems(THROWING_DAGGER,16)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("rune_summer")
                .addCondition(new HellForgeCondition(200))
                .inputItems(runeEarth)
                .inputItems(runeAir)
                .inputItems(BLUE_ICE)
                .inputItems(ChemicalHelper.get(dust,GTMaterials.Ice))
                .outputItems(runeSummer,1)
                .duration(200)
                .EUt(4000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("fungal_charge_2_item")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(CHARCOAL)
                .inputItems(COPPER_BLOCK)
                .inputItems(BROWN_MUSHROOM)
                .outputItems(FUNGAL_CHARGE_2_ITEM,1)
                .duration(200)
                .EUt(1600)
                .circuitMeta(2)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("rune_autumn1")
                .addCondition(new HellForgeCondition(200))
                .inputItems(APPLE)
                .inputItems(HONEY_BOTTLE)
                .inputItems(runeAir)
                .inputItems(runeFire)
                .outputItems(runeAutumn,1)
                .duration(200)
                .EUt(4000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("master_routing_node_block_item")
                .addCondition(new HellForgeCondition(400))
                .inputItems(IRON_BLOCK)
                .inputItems(DIAMOND)
                .inputItems(IMBUED_SLATE)
                .outputItems(MASTER_ROUTING_NODE_BLOCK_ITEM,1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("dungeons_simple_key")
                .addCondition(new HellForgeCondition(300))
                .inputItems(IRON_INGOT)
                .inputItems(IRON_INGOT)
                .inputItems(REDSTONE_BLOCK)
                .inputItems(IMBUED_SLATE)
                .outputItems(DUNGEON_SIMPLE_KEY,1)
                .duration(200)
                .EUt(6000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("master_core")
                .addCondition(new HellForgeCondition(400))
                .inputItems(IRON_INGOT)
                .inputItems(IRON_INGOT)
                .inputItems(Tags.Items.GLASS)
                .inputItems(LAPIS_BLOCK)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("bloodmagic:mastercore")),1)
                .duration(200)
                .EUt(8000)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("shaped_charge_item")
                .addCondition(new HellForgeCondition(10))
                .inputItems(COBBLESTONE)
                .inputItems(SAND)
                .inputItems(CHARCOAL)
                .inputItems(ANDESITE)
                .outputItems(SHAPED_CHARGE_ITEM,8)
                .duration(200)
                .EUt(200)
                .circuitMeta(1)
                .save(provider);
        HELL_FORGE_RECIPES.recipeBuilder("hellforged_resonator")
                .addCondition(new HellForgeCondition(1200))
                .inputItems(AMETHYST_SHARD)
                .inputItems(HELLFORGED_INGOT)
                .inputItems(RAW_CRYSTAL)
                .inputItems(GOLD_INGOT)
                .outputItems(HELLFORGED_RESONATOR,1)
                .duration(200)
                .EUt(24000)
                .circuitMeta(1)
                .save(provider);
    }
}
