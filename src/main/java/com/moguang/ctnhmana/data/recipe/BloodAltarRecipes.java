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
import com.moguang.ctnhmana.item.BloodMagicJade.EtchingJade;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import com.gregtechceu.gtceu.common.data.GTBlocks.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.redstone.Redstone;
import org.moddingx.libx.creativetab.CreativeTabX;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.lens.FlashLens;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMItems.HORIZEN_RUNE;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static net.minecraft.world.item.Items.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import static vazkii.botania.common.item.BotaniaItems.runeFire;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.REINFORCED_SLATE;

public class BloodAltarRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        BloodAltarRecipeBuilder.builder("test_1")
                .input(new ItemStack(runeFire,1))
                .output(new ItemStack(HORIZEN_RUNE,1))
                .syphon(10000)
                .minimumTier(2)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("blankslate")
                .output(new ItemStack(SLATE.get(),1))
                .input(new ItemStack(BotaniaBlocks.livingrock.asItem(),1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloodygold")
                .input(ChemicalHelper.get(TagPrefix.ingot,GTMaterials.Gold,1))
                .output(ChemicalHelper.get(TagPrefix.ingot,CMMaterials.COAGULBLOODGOLD,1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloodygold_dust")
                .input(ChemicalHelper.get(TagPrefix.dust,GTMaterials.Gold,1))
                .output(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,1))
                .circuitMeta(0)
                .syphon(1000)
                .minimumTier(1)
                .consumeRate(5)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloodygold_block")
                .input(ChemicalHelper.get(TagPrefix.block,GTMaterials.Gold,1))
                .output(ChemicalHelper.get(TagPrefix.block,CMMaterials.COAGULBLOODGOLD,1))
                .circuitMeta(0)
                .syphon(8000)
                .minimumTier(1)
                .consumeRate(45)
                .drainRate(45)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bloody_diode")
                .input(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,2))
                .output(ChemicalHelper.get(TagPrefix.wireFine,CMMaterials.COAGULBLOODGOLD,12))
                .circuitMeta(1)
                .syphon(8000)
                .minimumTier(1)
                .consumeRate(45)
                .drainRate(45)
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("etching_circuit")
                .inputItems(BloodMagicItems.BLANK_RUNE_ITEM.get(),1)
                .circuitMeta(1)
                .outputItems(CMItems.RUNE_CIRCUIT_BOARD,1)
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .addCondition(new BloodAltarCondition(3,100,100*200,"etching"))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_diode")
                .inputItems(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,2))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,CMMaterials.HEMOPLATINUM,12))
                .inputFluids(GTMaterials.Polyethylene.getFluid(144))
                .outputItems(BLOOD_DIODE,16)
                .circuitMeta(2)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_resistor")
                .inputItems(ChemicalHelper.get(TagPrefix.dust,CMMaterials.COAGULBLOODGOLD,2))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,GTMaterials.BlackSteel,12))
                .inputFluids(GTMaterials.Polyethylene.getFluid(144))
                .outputItems(BLOOD_RESISTOR,16)
                .circuitMeta(2)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_capacitor")
                .inputItems(ChemicalHelper.get(TagPrefix.foil,GTMaterials.Electrum,32))
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.COAGULBLOODGOLD,8))
                .inputItems(BloodMagicItems.REAGENT_LAVA)
                .inputFluids(GTMaterials.Polyethylene.getFluid(144*8))
                .outputItems(BLOOD_CAPACITOR,48)
                .circuitMeta(1)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_transistor")
                .inputItems(ChemicalHelper.get(TagPrefix.foil,CMMaterials.Aerialite,32))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,GTMaterials.BlackSteel,64))
                .inputItems(BloodMagicItems.REAGENT_WATER)
                .inputFluids(GTMaterials.Polyethylene.getFluid(144*8))
                .outputItems(BLOOD_TRANSISTOR,48)
                .circuitMeta(1)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BLOOD_ALTAR_RECIPES.recipeBuilder("bloody_inductor")
                .inputItems(ChemicalHelper.get(TagPrefix.ring,GTMaterials.NickelZincFerrite,1))
                .inputItems(ChemicalHelper.get(TagPrefix.dust,GTMaterials.Sulfur,4))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,GTMaterials.BlackSteel,8))
                .inputItems(ChemicalHelper.get(TagPrefix.wireFine,CMMaterials.HEMOPLATINUM,12))
                .inputFluids(GTMaterials.Polyethylene.getFluid(144*8))
                .outputItems(BLOOD_INDUCTOR,32)
                .circuitMeta(1)
                .duration(450)
                .EUt(GTValues.VA[GTValues.MV])
                .addCondition(new BloodAltarCondition(2,40,900))
                .save(provider);
        BloodAltarRecipeBuilder.builder("reinforced_slate")
                .input(new ItemStack(SLATE.get(),1))
                .output(new ItemStack(REINFORCED_SLATE.get(),1))
                .syphon(2000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(30)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("imbued_slate")
                .input(new ItemStack(REINFORCED_SLATE.get(),1))
                .output(new ItemStack(IMBUED_SLATE.get(),1))
                .syphon(5000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(50)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("demonic_slate")
                .input(new ItemStack(IMBUED_SLATE.get(),1))
                .output(new ItemStack(DEMONIC_SLATE.get(),1))
                .syphon(15000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("ethereal_slate")
                .input(new ItemStack(DEMONIC_SLATE.get(),1))
                .output(new ItemStack(ETHEREAL_SLATE.get(),1))
                .syphon(40000)
                .minimumTier(5)
                .circuitMeta(1)
                .consumeRate(500)
                .drainRate(10)
                .save(provider);
        BloodAltarRecipeBuilder.builder("end_slate")
                .input(new ItemStack(ETHEREAL_SLATE.get(),1))
                .output(new ItemStack(ENDSLATE.get(),1))
                .syphon(60000)
                .minimumTier(6)
                .circuitMeta(1)
                .consumeRate(300)
                .drainRate(10)
                .save(provider);
        BloodAltarRecipeBuilder.builder("eak_blood_orb")
                .input(ChemicalHelper.get(TagPrefix.ingot,AlfSteel,1))
                .output(new ItemStack(WEAK_BLOOD_ORB.get(),1))
                .syphon(2000)
                .minimumTier(1)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("apprentice_blood_orb")
                .input(ChemicalHelper.get(TagPrefix.block, Redstone,1))
                .output(new ItemStack(APPRENTICE_BLOOD_ORB.get(),1))
                .syphon(5000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(50)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("magician_blood_orb")
                .input(ChemicalHelper.get(TagPrefix.block,CMMaterials.COAGULBLOODGOLD,1))
                .output(new ItemStack(MAGICIAN_BLOOD_ORB.get(),1))
                .syphon(25000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("master_blood_orb")
                .input(new ItemStack(WEAK_BLOOD_SHARD.get(),1))
                .output(new ItemStack(MASTER_BLOOD_ORB.get(),1))
                .syphon(40000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(10)
                .save(provider);
        BloodAltarRecipeBuilder.builder("archmage_blood_orb")
                .input(new ItemStack(HELLFORGED_BLOCK.get(),1))
                .output(new ItemStack(ARCHMAGE_BLOOD_ORB.get(),1))
                .syphon(80000)
                .minimumTier(5)
                .circuitMeta(1)
                .consumeRate(400)
                .drainRate(5)
                .save(provider);
        BloodAltarRecipeBuilder.builder("enhanced_teleposer_focus")
                .input(new ItemStack(TELEPOSER_FOCUS.get(),1))
                .output(new ItemStack(ENHANCED_TELEPOSER_FOCUS.get(),1))
                .syphon(10000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("teleposer_focus")
                .input(ChemicalHelper.get(gem,GTMaterials.EnderPearl,1))
                .output(new ItemStack(TELEPOSER_FOCUS.get(),1))
                .syphon(2000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("air_inscription_tool")
                .input(new ItemStack(AIR_ESSENCE.get(),1))
                .output(new ItemStack(AIR_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("boss_summoner")
                .input(new ItemStack(GTItems.SENSOR_HV.get(),1))
                .output(new ItemStack(BOSS_SUMMONER.get(),1))
                .syphon(5000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(50)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("bleeding_edge_music")
                .input(new ItemStack(DEMONITE_RAW.get(),1))
                .output(new ItemStack(BLEEDING_EDGE_MUSIC.get(),1))
                .syphon(10000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("weak_activation_crystal")
                .input(new ItemStack(LAVA_CRYSTAL.get(),1))
                .output(new ItemStack(WEAK_ACTIVATION_CRYSTAL.get(),1))
                .syphon(10000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("water_inscription_tool")
                .input(new ItemStack(WATER_ESSENCE.get(),1))
                .output(new ItemStack(WATER_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("strong_tau_item")
                .input(new ItemStack(WEAK_TAU_ITEM.get(),1))
                .output(new ItemStack(STRONG_TAU_ITEM.get(),1))
                .syphon(4000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(30)
                .save(provider);
        BloodAltarRecipeBuilder.builder("advanced_boss_summoner")
                .input(new ItemStack(GTItems.SENSOR_LuV.get(),1))
                .output(new ItemStack(ADVANCED_BOSS_SUMMONER.get(),1))
                .syphon(20000)
                .minimumTier(5)
                .circuitMeta(1)
                .consumeRate(200)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("earth_inscription_tool")
                .input(new ItemStack(EARTH_ESSENCE.get(),1))
                .output(new ItemStack(EARTH_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("alchemy_flask")
                .input(new ItemStack(flask,1))
                .output(new ItemStack(ALCHEMY_FLASK.get(),1))
                .syphon(4000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(40)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("dusk_inscription_tool")
                .input(new ItemStack(RAW_CRYSTAL.get(),1))
                .output(new ItemStack(DUSK_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("dagger_of_sacrifice")
                .input(new ItemStack(IRON_SWORD,1))
                .output(new ItemStack(DAGGER_OF_SACRIFICE.get(),1))
                .syphon(3000)
                .minimumTier(2)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("life_essence_bucket")
                .input(new ItemStack(BUCKET,1))
                .output(new ItemStack(LIFE_ESSENCE_BUCKET.get(),1))
                .syphon(1000)
                .minimumTier(1)
                .circuitMeta(1)
                .consumeRate(10)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("fire_inscription_tool")
                .input(new ItemStack(FIRE_ESSENCE,1))
                .output(new ItemStack(FIRE_INSCRIPTION_TOOL.get(),1))
                .syphon(2000)
                .minimumTier(3)
                .circuitMeta(1)
                .consumeRate(20)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("soul_snare")
                .input(new ItemStack(STRING,1))
                .output(new ItemStack(SOUL_SNARE.get(),1))
                .syphon(500)
                .minimumTier(1)
                .circuitMeta(1)
                .consumeRate(10)
                .drainRate(20)
                .save(provider);
        BloodAltarRecipeBuilder.builder("casing_blood")
                .input(new ItemStack(GTBlocks.CASING_STEEL_SOLID,1))
                .output(new ItemStack(CMBlocks.CASING_BLOOD.get(),1))
                .syphon(10000)
                .minimumTier(4)
                .circuitMeta(1)
                .consumeRate(100)
                .drainRate(20)
                .save(provider);

    }
}
