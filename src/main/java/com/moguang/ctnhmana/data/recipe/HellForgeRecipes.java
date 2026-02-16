package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
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
import vazkii.botania.common.item.BotaniaItems;
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
                    .minimumSouls(2).soulDrain(1)
                    .input(Ingredient.of(SHAPED_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(shapedStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("deforester_charge" + suffixArray[i])
                    .minimumSouls(2).soulDrain(1)
                    .input(Ingredient.of(DEFORESTER_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(deforesterStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("vein_charge" + suffixArray[i])
                    .minimumSouls(2).soulDrain(1)
                    .input(Ingredient.of(VEINMINE_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i])
                    .output(veinStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("fungal_charge" + suffixArray[i])
                    .minimumSouls(2).soulDrain(1)
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
                    .minimumSouls(8).soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.DEFORESTER_CHARGE_2.get()),ingredientArray2[i])
                    .output(deforester2Stack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("vein_charge_2" + suffixArray2[i])
                    .minimumSouls(8).soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.VEINMINE_CHARGE_2.get()),ingredientArray2[i])
                    .output(vein2Stack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("fungal_charge_2" + suffixArray2[i])
                    .minimumSouls(8).soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.FUNGAL_CHARGE_2.get()),ingredientArray2[i])
                    .output(fungal2Stack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("shaped_charge_deep" + suffixArray2[i])
                    .minimumSouls(8).soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get()),ingredientArray2[i])
                    .output(shapedChargeDeepStack)
                    .save(provider);
            TartaricForgeRecipeBuilder.builder("aug_shaped_charge" + suffixArray2[i])
                    .minimumSouls(8).soulDrain(4)
                    .input(Ingredient.of(BloodMagicBlocks.AUG_SHAPED_CHARGE.get()),ingredientArray2[i])
                    .output(augShapedStack)
                    .save(provider);
        }
        TartaricForgeRecipeBuilder.builder("output_routing_node")//输出路由节点
                .input(new ItemStack(REDSTONE,1))
                .input(new ItemStack(IRON_INGOT,1))
                .input(ROUTING_NODE_BLOCK_ITEM.get())
                .input(new ItemStack(GLOWSTONE_DUST,1))
                .output(new ItemStack(OUTPUT_ROUTING_NODE_BLOCK_ITEM.get()))
                .minimumSouls(120).soulDrain(60)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("steadfast_crystal_block_item")//坚韧晶体块
                .input(STEADFAST_CRYSTAL.get(),STEADFAST_CRYSTAL.get(),STEADFAST_CRYSTAL.get(),STEADFAST_CRYSTAL.get())
                .output(new ItemStack(STEADFAST_CRYSTAL_BLOCK_ITEM.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("deforester_charge_item")//伐木充能
                .input(OAK_PLANKS,COBBLESTONE,COAL,DARK_OAK_LOG)
                .output(new ItemStack(DEFORESTER_CHARGE_ITEM.get(),8))
                .minimumSouls(20).soulDrain(10)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("throwing_dagger_syringe")//飞刀注射器
                .input(GLASS,ANDESITE)
                .output(new ItemStack(THROWING_DAGGER_SYRINGE.get(),8))
                .minimumSouls(20).soulDrain(10)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("vengeful_crystal_catalyst")
                .input(SULFUR.get(),TAU_OIL.get(),NETHER_WART,MELON_SEEDS)
                .output(new ItemStack(VENGEFUL_CRYSTAL_CATALYST.get(),8))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("amethyst_throwing_dagger")//紫水晶飞刀
                .input(new ItemStack(AMETHYST_SHARD,1).getItem(),new ItemStack(COPPER_INGOT,1).getItem(),new ItemStack(COPPER_INGOT,1).getItem())
                .output(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("bloodmagic:amethystthrowingdagger")),16))
                .minimumSouls(64).soulDrain(32)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("hellforged_parts")//狱火锻零件
                .input(HELLFORGED_PARTS.get(),NETHERITE_SCRAP,DIAMOND,RAW_CRYSTAL.get())
                .output(new ItemStack(HELLFORGED_PARTS.get(),2))
                .minimumSouls(2000).soulDrain(1000)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("destructive_crystal_block_item")//破坏晶体块
                .input(DESTRUCTIVE_CRYSTAL.get(),DESTRUCTIVE_CRYSTAL.get(),DESTRUCTIVE_CRYSTAL.get(),DESTRUCTIVE_CRYSTAL.get())
                .output(new ItemStack(DESTRUCTIVE_CRYSTAL_BLOCK_ITEM.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("destructive_crystal_catalyst")//破坏晶体催化剂
                .input(NETHER_WART,BEETROOT,TAU_OIL.get(),SULFUR.get())
                .output(new ItemStack(DESTRUCTIVE_CRYSTAL_CATALYST.get(),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("lesser_gem")//次级宝石
                .input(PETTY_GEM.get(),LAPIS_BLOCK,REDSTONE_BLOCK,DIAMOND)
                .output(new ItemStack(LESSER_GEM.get(),1))
                .minimumSouls(120).soulDrain(60)
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
        TartaricForgeRecipeBuilder.builder("miners_key")//矿工之钥
                .input(COPPER_INGOT,HELLFORGED_INGOT.get(),IMBUED_SLATE.get())
                .output(new ItemStack(DUNGEON_MINE_KEY.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("corrosive_crystal_catalyst")//侵蚀晶体催化剂
                .input(NETHER_WART,WHEAT_SEEDS,TAU_OIL.get(),SULFUR.get())
                .output(new ItemStack(CORROSIVE_CRYSTAL_CATALYST.get(),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("demon_pylon_item")//恶魔塔
                .input(DEMON_CRUCIBLE_ITEM.get(),REAGENT_TELEPOSITION.get(),REAGENT_SUPPRESSION.get(),DEMON_CRYSTALLIZER_ITEM.get())
                .output(new ItemStack(DEMON_PYLON_ITEM.get(),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("sentient_scythe")//感知镰刀
                .input(IRON_HOE,PETTY_GEM.get())
                .output(new ItemStack(SENTIENT_SCYTHE.get(),1))
                .minimumSouls(2).soulDrain(1)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("demon_crucible_item")//恶魔坩埚
                .input(REAGENT_SIGHT.get(),REAGENT_VOID.get(),CAULDRON,REINFORCED_SLATE.get())
                .output(new ItemStack(DEMON_CRUCIBLE_ITEM.get(),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("sentient_axe")
                .input(IRON_AXE,PETTY_GEM.get())
                .output(new ItemStack(SENTIENT_AXE.get(),1))
                .minimumSouls(2).soulDrain(1)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("steadfast_crystal_catalyst")
                .input(NETHER_WART,PUMPKIN_SEEDS,TAU_OIL.get(),SULFUR.get())
                .output(new ItemStack(STEADFAST_CRYSTAL_CATALYST.get(),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("veinmine_charge_2_item")
                .input(SAND,COPPER_BLOCK,COAL,SANDSTONE)
                .output(new ItemStack(VEINMINE_CHARGE_2_ITEM.get(),1))
                .minimumSouls(160).soulDrain(80)
                .circuitMeta(2)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("corrosive_crystal_catalyst_greater")
                .input(COMMON_GEM.get(),DEMONIC_SLATE.get(),WEAK_BLOOD_SHARD.get(),RAW_CRYSTAL.get())
                .output(new ItemStack(GREATER_GEM.get(),1))
                .minimumSouls(2000).soulDrain(1000)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("aug_shaped_charge_item")
                .input(SAND,COPPER_BLOCK,COAL,BRICK)
                .output(new ItemStack(AUG_SHAPED_CHARGE_ITEM.get(),6))
                .minimumSouls(160).soulDrain(80)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("resonator")
                .input(RAW_CRYSTAL.get(),COPPER_BLOCK,ANDESITE)
                .output(new ItemStack(RESONATOR.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("node_router")
                .input(LAPIS_LAZULI,LAPIS_LAZULI,REINFORCED_SLATE.get(),STICK)
                .output(new ItemStack(NODE_ROUTER.get(),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("vengeful_crystal_block_item")
                .input(VENGEFUL_CRYSTAL.get(),VENGEFUL_CRYSTAL.get(),VENGEFUL_CRYSTAL.get(),VENGEFUL_CRYSTAL.get())
                .output(new ItemStack(VENGEFUL_CRYSTAL_BLOCK_ITEM.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("raw_crystal_block_item")
                .input(RAW_CRYSTAL.get(),RAW_CRYSTAL.get(),RAW_CRYSTAL.get(),RAW_CRYSTAL.get())
                .circuitMeta(22)
                .output(new ItemStack(RAW_CRYSTAL_BLOCK_ITEM.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("petty_gem")
                .input(REDSTONE,GLASS,GOLD_INGOT,LAPIS_LAZULI)
                .output(new ItemStack(PETTY_GEM.get(),1))
                .minimumSouls(2).soulDrain(1)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("rune_winter")
                .input(runeWater,runeEarth,SNOW_BLOCK,PACKED_ICE)
                .output(runeWinter.getDefaultInstance())
                .minimumSouls(400).soulDrain(200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("veinmine_charge_item")
                .input(SANDSTONE,COBBLESTONE,SAND,COAL)
                .output(new ItemStack(VEINMINE_CHARGE_ITEM.get(),8))
                .minimumSouls(20).soulDrain(10)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("field_restriction_casing")
                .input(Ingredient.of(new ItemStack(CASING_FORCE_FILED.get(),8)),Ingredient.of(ChemicalHelper.get(gear,GTMaterials.TungstenSteel)),Ingredient.of(ENDSLATE.get()))
                .output(new ItemStack(FIELD_RESTRICTION_CASING.get(),4))
                .minimumSouls(4000).soulDrain(2000)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("demon_crystallizer")
                .input(SOUL_FORGE_ITEM.get(),REAGENT_MAGNETISM.get(),REAGENT_BLOOD_LIGHT.get(),BotaniaBlocks.manaGlass.asItem())
                .output(new ItemStack(DEMON_CRYSTALLIZER_ITEM.get()))
                .minimumSouls(1000).soulDrain(500)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("input_routing_node_block_item")
                .input(GOLD_INGOT,ROUTING_NODE_BLOCK_ITEM.get(),REDSTONE,GLOWSTONE)
                .output(new ItemStack(INPUT_ROUTING_NODE_BLOCK_ITEM.get(),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("fused_demon_mixed_mana_dust")
                .input(Ingredient.of(ChemicalHelper.get(dust,Fused_Lp_Mixed_Mana)),Ingredient.of(ETHEREAL_SLATE.get()))
                .output(ChemicalHelper.get(dust,Fused_demon_mixed,12))
                .minimumSouls(4000).soulDrain(2000)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("common_gem")
                .input(GOLD_BLOCK,DIAMOND,IMBUED_SLATE.get(),LESSER_GEM.get())
                .output(new ItemStack(COMMON_GEM.get(),1))
                .minimumSouls(480).soulDrain(240)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("sentient_pickaxe")
                .input(PETTY_GEM.get(),IRON_PICKAXE)
                .output(new ItemStack(SENTIENT_PICKAXE.get(),1))
                .minimumSouls(2).soulDrain(1)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("deforester_charge_2_item")
                .input(DARK_OAK_LOG,CHARCOAL,COPPER_BLOCK,OAK_PLANKS)
                .output(new ItemStack(DEFORESTER_CHARGE_2_ITEM.get(),8))
                .minimumSouls(160).soulDrain(80)
                .circuitMeta(2)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("sanguine_reverter")
                .input(IMBUED_SLATE.get(),ANDESITE,IRON_INGOT,SHEARS)
                .output(new ItemStack(SANGUINE_REVERTER.get()))
                .minimumSouls(700).soulDrain(350)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("shaped_charge_item_ev")
                .input(SAND,COPPER_BLOCK,CHARCOAL,ANDESITE)
                .output(new ItemStack(SHAPED_CHARGE_ITEM.get(),4))
                .minimumSouls(160).soulDrain(80)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("corrosive_crystal_block_item")
                .input(CORROSIVE_CRYSTAL.get(),CORROSIVE_CRYSTAL.get(),CORROSIVE_CRYSTAL.get(),CORROSIVE_CRYSTAL.get())
                .output(new ItemStack(CORROSIVE_CRYSTAL_BLOCK_ITEM.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("sentient_sword")
                .input(IRON_SWORD,PETTY_GEM.get())
                .output(new ItemStack(SENTIENT_SWORD.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("demon_will_gauge")
                .input(RAW_CRYSTAL.get(),GLASS,GOLD_INGOT,REDSTONE)
                .output(new ItemStack(DEMON_WILL_GAUGE.get(),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("rune_spring")
                .input(runeWater,runeFire,WHEAT_SEEDS,CHERRY_SAPLING)
                .output(runeSpring.getDefaultInstance())
                .minimumSouls(400).soulDrain(200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("fungal_charge_item")
                .input(BROWN_MUSHROOM,CHARCOAL,COBBLESTONE)
                .output(new ItemStack(FUNGAL_CHARGE_ITEM.get(),8))
                .minimumSouls(20).soulDrain(10)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("prismarine_crystalline_resonator")
                .input(RAW_CRYSTAL.get(),TAU_OIL.get(),AMETHYST_SHARD,ChemicalHelper.get(ingot,GTMaterials.Vanadium).getItem())
                .output(new ItemStack(PRIMITIVE_CRYSTALLINE_RESONATOR.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("raw_crystal1")
                .input(Ingredient.of(new ItemStack(AMETHYST_SHARD,8)),Ingredient.of(ENDSLATE.get()),Ingredient.of(CORROSIVE_CRYSTAL.get()))
                .output(new ItemStack(RAW_CRYSTAL.get(),1))
                .minimumSouls(2000).soulDrain(1000)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("sentient_shovel")
                .input(IRON_SHOVEL,PETTY_GEM.get())
                .output(new ItemStack(SENTIENT_SHOVEL.get(),1))
                .minimumSouls(2).soulDrain(1)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("throwing_dagger")
                .input(IRON_INGOT,IRON_INGOT,STRING)
                .output(new ItemStack(THROWING_DAGGER.get(),16))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("rune_summer")
                .input(runeEarth,runeAir,BLUE_ICE,ChemicalHelper.get(dust,GTMaterials.Ice).getItem())
                .output(runeSummer.getDefaultInstance())
                .minimumSouls(400).soulDrain(200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("fungal_charge_2_item")
                .input(CHARCOAL,COPPER_BLOCK,BROWN_MUSHROOM)
                .output(new ItemStack(FUNGAL_CHARGE_2_ITEM.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(2)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("rune_autumn1")
                .input(APPLE,HONEY_BOTTLE,runeAir,runeFire)
                .output(runeAutumn.getDefaultInstance())
                .minimumSouls(400).soulDrain(200)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("master_routing_node_block_item")
                .input(IRON_BLOCK,DIAMOND,IMBUED_SLATE.get())
                .output(new ItemStack(MASTER_ROUTING_NODE_BLOCK_ITEM.get(),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("dungeons_simple_key")
                .input(IRON_INGOT,IRON_INGOT,REDSTONE_BLOCK,IMBUED_SLATE.get())
                .output(new ItemStack(DUNGEON_SIMPLE_KEY.get(),1))
                .minimumSouls(600).soulDrain(300)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("master_core")
                .input(IRON_INGOT,IRON_INGOT)
                .input(Tags.Items.GLASS)
                .input(LAPIS_BLOCK)
                .output(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("bloodmagic:mastercore")),1))
                .minimumSouls(800).soulDrain(400)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("shaped_charge_item")
                .input(COBBLESTONE,SAND,CHARCOAL,ANDESITE)
                .output(new ItemStack(SHAPED_CHARGE_ITEM.get(),8))
                .minimumSouls(20).soulDrain(10)
                .circuitMeta(1)
                .save(provider);
        TartaricForgeRecipeBuilder.builder("hellforged_resonator")
                .input(AMETHYST_SHARD,HELLFORGED_INGOT.get(),RAW_CRYSTAL.get(),GOLD_INGOT)
                .output(new ItemStack(HELLFORGED_RESONATOR.get(),1))
                .minimumSouls(2400).soulDrain(1200)
                .circuitMeta(1)
                .save(provider);

        //四个核心的工作台合成
        VanillaRecipeHelper.addShapedRecipe(provider, true, "steadfast_core", STEADFAST_CORE.asStack(),
                "ABA",
                "CDC",
                "AEA",
                'A', STEADFAST_CRYSTAL.get(),
                'B', lifeEssence,
                'C', REAGENT_VOID.get(),
                'D', runeEnvy,
                'E', lensWarp);
        VanillaRecipeHelper.addShapedRecipe(provider, true, "vengeful_core", VENGEFUL_CORE.asStack(),
                "ABA",
                "CDC",
                "AEA",
                'A', VENGEFUL_CRYSTAL.get(),
                'B', lifeEssence,
                'C', REAGENT_SIGHT.get(),
                'D', runeWrath,
                'E', lensDamage);
        VanillaRecipeHelper.addShapedRecipe(provider, true, "corrosive_core", CORROSIVE_CORE.asStack(),
                "ABA",
                "CDC",
                "AEA",
                'A', CORROSIVE_CRYSTAL.get(),
                'B', lifeEssence,
                'C', REAGENT_GROWTH.get(),
                'D', runeGreed,
                'E', lensMine);
        VanillaRecipeHelper.addShapedRecipe(provider, true, "destructive_core", DESTRUCTIVE_CORE.asStack(),
                "ABA",
                "CDC",
                "AEA",
                'A', DESTRUCTIVE_CRYSTAL.get(),
                'B', lifeEssence,
                'C', REAGENT_HOLDING.get(),
                'D', runeSloth,
                'E', lensInfluence);
    }
}