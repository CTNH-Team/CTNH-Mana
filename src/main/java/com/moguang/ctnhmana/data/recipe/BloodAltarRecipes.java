package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.block.FusionCasingBlock;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.hollingsworth.arsnouveau.common.items.FlaskCannon;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.builder.bloodmagic.BloodAltarRecipeBuilder;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import com.gregtechceu.gtceu.common.data.GTBlocks.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.redstone.Redstone;
import org.checkerframework.checker.units.qual.C;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.lens.FlashLens;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static net.minecraft.world.item.Items.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;


public class BloodAltarRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        BloodAltarRecipeBuilder.builder("test_1")//视域符文
                .input(new ItemStack(runeFire,1))
                .output(new ItemStack(HORIZEN_RUNE,1))
                .syphon(10000)
                .minimumTier(2)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("blankslate")//空白石板
                .output(new ItemStack(SLATE.get(),1))
                .input(new ItemStack(BotaniaBlocks.livingrock.asItem(),1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloodygold")//凝血金锭
                .input(ChemicalHelper.get(TagPrefix.ingot,GTMaterials.Gold,1))
                .output(ChemicalHelper.get(TagPrefix.ingot,CMMaterials.COAGULBLOODGOLD,1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloodygold_dust")//凝血金粉
                .input(ChemicalHelper.get(TagPrefix.dust,GTMaterials.Gold,1))
                .output(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloodygold_block")//凝血金块
                .input(ChemicalHelper.get(TagPrefix.block,GTMaterials.Gold,1))
                .output(ChemicalHelper.get(TagPrefix.block,CMMaterials.COAGULBLOODGOLD,1))
                .circuitMeta(0)
                .syphon(8000)
                .minimumTier(1)
                .consumeRate(45)
                .drainRate(45)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloody_diode")//凝血金细线
                .input(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,2))
                .output(ChemicalHelper.get(TagPrefix.wireFine,CMMaterials.COAGULBLOODGOLD,12))
                .circuitMeta(1)
                .syphon(8000)
                .minimumTier(1)
                .consumeRate(45)
                .drainRate(45)
                .save(provider);

        BloodAltarRecipeBuilder.builder("reinforced_slate")//强化石板
                .input(new ItemStack(SLATE.get(),1))
                .output(new ItemStack(REINFORCED_SLATE.get(),1))
                .syphon(2000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(30)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("imbued_slate")//注魔石板
                .input(new ItemStack(REINFORCED_SLATE.get(),1))
                .output(new ItemStack(IMBUED_SLATE.get(),1))
                .syphon(5000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(50)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("demonic_slate")//恶魔石板
                .input(new ItemStack(IMBUED_SLATE.get(),1))
                .output(new ItemStack(DEMONIC_SLATE.get(),1))
                .syphon(15000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("ethereal_slate")//虚空石板
                .input(new ItemStack(DEMONIC_SLATE.get(),1))
                .output(new ItemStack(ETHEREAL_SLATE.get(),1))
                .syphon(40000)
                .minimumTier(5)
                .circuitMeta(1)
                .consumeRate(500)
                .drainRate(10)
                .save(provider);
        BloodAltarRecipeBuilder.builder("end_slate")//终焉石板
                .input(new ItemStack(ETHEREAL_SLATE.get(),1))
                .output(new ItemStack(ENDSLATE.get(),1))
                .syphon(60000)
                .minimumTier(6)
                .circuitMeta(1)
                .consumeRate(300)
                .drainRate(10)
                .save(provider);
        BloodAltarRecipeBuilder.builder("eak_blood_orb")//初级血之宝珠
                .input(ChemicalHelper.get(TagPrefix.ingot,AlfSteel,1))
                .output(new ItemStack(WEAK_BLOOD_ORB.get(),1))
                .syphon(2000)
                .minimumTier(1)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("apprentice_blood_orb")//学徒血之宝珠
                .input(ChemicalHelper.get(TagPrefix.block, Redstone,1))
                .output(new ItemStack(APPRENTICE_BLOOD_ORB.get(),1))
                .syphon(5000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(50)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("magician_blood_orb")//魔法师血之宝珠
                .input(ChemicalHelper.get(TagPrefix.block,CMMaterials.COAGULBLOODGOLD,1))
                .output(new ItemStack(MAGICIAN_BLOOD_ORB.get(),1))
                .syphon(25000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("master_blood_orb")//大师血之宝珠
                .input(new ItemStack(WEAK_BLOOD_SHARD.get(),1))
                .output(new ItemStack(MASTER_BLOOD_ORB.get(),1))
                .syphon(40000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(10)
                .save(provider);
        BloodAltarRecipeBuilder.builder("archmage_blood_orb")//大法师血之宝珠
                .input(new ItemStack(HELLFORGED_BLOCK.get(),1))
                .output(new ItemStack(ARCHMAGE_BLOOD_ORB.get(),1))
                .syphon(80000)
                .minimumTier(5)
                .circuitMeta(1)
                .consumeRate(400)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("enhanced_teleposer_focus")//强化传送聚焦
                .input(new ItemStack(TELEPOSER_FOCUS.get(),1))
                .output(new ItemStack(ENHANCED_TELEPOSER_FOCUS.get(),1))
                .syphon(10000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("teleposer_focus")//传送聚焦
                .input(ChemicalHelper.get(gem,GTMaterials.EnderPearl,1))
                .output(new ItemStack(TELEPOSER_FOCUS.get(),1))
                .syphon(2000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("air_inscription_tool")//风之刻印工具
                .input(new ItemStack(AIR_ESSENCE.get(),1))
                .output(new ItemStack(AIR_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("boss_summoner")//boss召唤器
                .input(new ItemStack(GTItems.SENSOR_HV.get(),1))
                .output(new ItemStack(BOSS_SUMMONER.get(),1))
                .syphon(5000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(50)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bleeding_edge_music")//血刃之音
                .input(new ItemStack(DEMONITE_RAW.get(),1))
                .output(new ItemStack(BLEEDING_EDGE_MUSIC.get(),1))
                .syphon(10000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("weak_activation_crystal")//弱效激活水晶
                .input(new ItemStack(LAVA_CRYSTAL.get(),1))
                .output(new ItemStack(WEAK_ACTIVATION_CRYSTAL.get(),1))
                .syphon(10000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("water_inscription_tool")//水之刻印工具
                .input(new ItemStack(WATER_ESSENCE.get(),1))
                .output(new ItemStack(WATER_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("strong_tau_item")//强效陶符
                .input(new ItemStack(WEAK_TAU_ITEM.get(),1))
                .output(new ItemStack(STRONG_TAU_ITEM.get(),1))
                .syphon(4000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("advanced_boss_summoner")//进阶boss召唤器
                .input(new ItemStack(GTItems.SENSOR_LuV.get(),1))
                .output(new ItemStack(ADVANCED_BOSS_SUMMONER.get(),1))
                .syphon(20000)
                .minimumTier(5)
                .circuitMeta(1)
                .consumeRate(200)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("earth_inscription_tool")//地之刻印工具
                .input(new ItemStack(EARTH_ESSENCE.get(),1))
                .output(new ItemStack(EARTH_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("alchemy_flask")//炼金烧瓶
                .input(new ItemStack(flask,1))
                .output(new ItemStack(ALCHEMY_FLASK.get(),1))
                .syphon(4000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(40)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("dusk_inscription_tool")//黄昏刻印工具
                .input(new ItemStack(RAW_CRYSTAL.get(),1))
                .output(new ItemStack(DUSK_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("dagger_of_sacrifice")//献祭匕首
                .input(new ItemStack(IRON_SWORD,1))
                .output(new ItemStack(DAGGER_OF_SACRIFICE.get(),1))
                .syphon(3000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("life_essence_bucket")//生命源质桶
                .input(new ItemStack(BUCKET,1))
                .output(new ItemStack(LIFE_ESSENCE_BUCKET.get(),1))
                .syphon(1000)
                .minimumTier(1)
                .circuitMeta(1)
                .consumeRate(10)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("fire_inscription_tool")//火之刻印工具
                .input(new ItemStack(FIRE_ESSENCE,1))
                .output(new ItemStack(FIRE_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("soul_snare")//灵魂陷阱
                .input(new ItemStack(STRING,1))
                .output(new ItemStack(SOUL_SNARE.get(),1))
                .syphon(500)
                .minimumTier(1)
                .circuitMeta(1)
                .consumeRate(10)
                .drainRate(20)
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("jade_etching")
                .inputItems(ChemicalHelper.get(gemExquisite,Topaz),2)
                .inputItems(new ItemStack(lensNormal))
                .inputItems(ChemicalHelper.get(lens, NetherStar))
                .inputItems(ChemicalHelper.get(lens, Diamond))
                .inputItems(ChemicalHelper.get(lens, Ruby))
                .inputItems(ChemicalHelper.get(lens, Sapphire))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(),1000))
                .inputFluids(SulfuricAcid,1000)
                .outputItems(ETCHING_JADE)
                .addCondition(new BloodAltarCondition(1,100,100*100*20))
                .EUt(32)
                .duration(100*20)
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("jade_kaguya") //oh my kaguya
                .inputItems(ChemicalHelper.get(gemExquisite,Ruby),2)
                .inputItems(BloodMagicBlocks.SPEED_RUNE.get().asItem())
                .inputItems(BloodMagicBlocks.SPEED_RUNE_2.get().asItem())
                .inputItems(lensSpeed)
                .inputItems(lensEfficiency)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(),1000))
                .inputFluids(DistilledWater,6666)
                .outputItems(EPHEMERAL_JADE)
                .addCondition(new BloodAltarCondition(4,1000,1000*100*20))
                .EUt(1200)
                .duration(100*20)
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("jade_antiblood") //oh my kaguya
                .inputItems(ChemicalHelper.get(gemExquisite,Sapphire),2)
                .inputItems(SUPPRESSION_SIGIL,4)
                .inputItems(REAGENT_SUPPRESSION,4)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(),1000))
                .inputFluids(PCBCoolant,6666)
                .outputItems(SUPPRESSION_JADE)
                .addCondition(new BloodAltarCondition(1,1000,1000*100*20))
                .EUt(32)
                .duration(100*20)
                .save(provider);

    }
}