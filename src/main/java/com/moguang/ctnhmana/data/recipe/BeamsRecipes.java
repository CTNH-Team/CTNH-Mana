package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.PLASMA;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static dev.shadowsoffire.apotheosis.ench.Ench.Items.*;
import static net.minecraft.world.item.Items.*;
import static vazkii.botania.common.block.BotaniaBlocks.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;

@SuppressWarnings("removal")
public class BeamsRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        BEAMS.recipeBuilder("slate")// 空白石板
                .addData("required_mana", 500000)
                .addData("mana", 100000)
                .circuitMeta(1)
                .inputItems(livingrock.asItem(), 1024)
                .outputItems(SLATE, 1024)
                .EUt(1)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("reinforced_slate")// 强化石板
                .addData("required_mana", 500000)
                .addData("mana", 100000)
                .circuitMeta(1)
                .inputItems(SLATE.get(), 1024)
                .outputItems(REINFORCED_SLATE, 1024)
                .EUt(1)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("demonic_slate")// 恶魔石板
                .addData("required_mana", 1000000)
                .addData("mana", 500000)
                .circuitMeta(1)
                .inputItems(IMBUED_SLATE.get(), 512)
                .outputItems(DEMONIC_SLATE, 512)
                .EUt(19600)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("imbued_slate")// 注魔石板
                .addData("required_mana", 750000)
                .addData("mana", 400000)
                .circuitMeta(1)
                .inputItems(REINFORCED_SLATE.get(), 1024)
                .outputItems(IMBUED_SLATE, 1024)
                .EUt(1960)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("weak_blood_shard")// 弱血碎片
                .addData("required_mana", 2000000)
                .addData("mana", 1000000)
                .circuitMeta(1)
                .inputItems(STRONG_TAU_ITEM.get(), 256)
                .outputItems(WEAK_BLOOD_SHARD, 512)
                .EUt(10000)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("raw_crystal")// 原晶
                .addData("required_mana", 5000000)
                .addData("mana", 2000000)
                .circuitMeta(1)
                .inputItems(AMETHYST_SHARD, 1280)
                .notConsumable(ENDSLATE)
                .outputItems(RAW_CRYSTAL, 640)
                .EUt(100000)
                .duration(20)
                .save(provider);
        BEAMS.recipeBuilder("mana_data")
                .addData("required_mana", 0)
                .addData("mana", 0)
                .circuitMeta(24)
                .inputFluids(PCBCoolant.getFluid(100))
                .EUt(1)
                .duration(100)
                .save(provider);
        BEAMS.recipeBuilder("zenith_essence")// 顶点精华
                .addData("required_mana", 5000000)
                .addData("mana", 1000000)
                .circuitMeta(1)
                .inputItems(INFUSED_BREATH, 100)
                .inputFluids(Mana.getFluid(10000))
                .inputItems(SOURCE_GEM_BLOCK.asItem(), 100)
                .outputFluids(Zenith_essence.getFluid(300 * 1000))
                .EUt(66666)
                .duration(400)
                .save(provider);
        BEAMS.recipeBuilder("umlhpic_wafer")// 魔力超高压集成电路晶圆
                .addData("required_mana", 10000000)
                .addData("mana", 5000000)
                .circuitMeta(1)
                .inputItems(ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT_WAFER, 320)
                .inputItems(TWIST_RUNE, 10)
                .inputItems(ChemicalHelper.get(dust, Ultra_Mana), 100)
                .inputFluids(Zenith_essence.getFluid(30 * 1000))
                .outputItems(UMLHPIC_WAFER, 320)
                .EUt(261424)
                .duration(400)
                .save(provider);
        BEAMS.recipeBuilder("naquadah_l")// 镎矿液
                .addData("required_mana", 1000000)
                .addData("mana", 500000)
                .circuitMeta(1)
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:naquadah_oxide_mixture_dust")),
                        1280)
                .outputFluids(Naquadah.getFluid(172800))
                .EUt(100000)
                .duration(180)
                .save(provider);
        BEAMS.recipeBuilder("enriched_naquadah_l")// 富集镎矿液
                .addData("required_mana", 3000000)
                .addData("mana", 2000000)
                .circuitMeta(1)
                .inputItems(ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation("gtceu:enriched_naquadah_oxide_mixture_dust")), 1280)
                .outputFluids(NaquadahEnriched.getFluid(172800))
                .EUt(400000)
                .duration(180)
                .save(provider);
        BEAMS.recipeBuilder("nqquadria")// 超频金液
                .addData("required_mana", 5000000)
                .addData("mana", 3000000)
                .circuitMeta(1)
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:naquadria_oxide_mixture_dust")),
                        1280)
                .outputFluids(Naquadria.getFluid(115200))
                .EUt(1000000)
                .duration(400)
                .save(provider);
        BEAMS.recipeBuilder("mana_circuit_board")// 魔力电路板
                .addData("required_mana", 5000000)
                .addData("mana", 1000000)
                .circuitMeta(1)
                .inputItems(ChemicalHelper.get(plate, ReinforcedEpoxyResin), 256)
                .inputItems(ChemicalHelper.get(foil, Ultra_Mana), 256)
                .inputFluids(Zenith_essence.getFluid(16 * 1000))
                .outputItems(MANA_CIRCUIT_BOARD, 256)
                .EUt(40000)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("ultra_mana1")// 超魔力锭
                .addData("required_mana", 5000000)
                .addData("mana", 4000000)
                .circuitMeta(1)
                .inputItems(ChemicalHelper.get(ingot, Plus_Mana), 64)
                .inputFluids(Mana_Radiation_Mixture.getFluid(10000))
                .outputItems(ChemicalHelper.get(ingot, Ultra_Mana), 64)
                .EUt(10000)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("super_plus_mana")// 超级加魔力锭
                .addData("required_mana", 2000000)
                .addData("mana", 1000000)
                .circuitMeta(2)
                .inputItems(ChemicalHelper.get(ingot, Plus_Mana), 64)
                .outputItems(ChemicalHelper.get(ingot, Super_Plus_Mana), 64)
                .EUt(1)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("eve_beam_plasma")// 夏娃光束等离子体
                .addData("required_mana", 5000000)
                .addData("mana", 2000000)
                .circuitMeta(1)
                .inputItems(TWIST_RUNE, 8)
                .inputFluids(Mana_Radiation_Mixture.getFluid(1000000))
                .outputFluids(Eve_Beam.getFluid(PLASMA, 100 * 1000))
                .EUt(200000)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("ultra_mana2")// 超魔力锭
                .addData("required_mana", 10000000)
                .addData("mana", 5000000)
                .circuitMeta(1)
                .inputItems(ChemicalHelper.get(ingot, Plus_Mana), 128)
                .inputFluids(Eve_Beam.getFluid(PLASMA, 10 * 1000))
                .outputItems(ChemicalHelper.get(ingot, Ultra_Mana), 128)
                .EUt(1000000)
                .duration(500)
                .save(provider);
        BEAMS.recipeBuilder("naquadria_ingot")// 超频金锭
                .addData("required_mana", 5000000)
                .addData("mana", 1000000)
                .circuitMeta(1)
                .inputItems(ChemicalHelper.get(ingot, Naquadah), 128)
                .inputFluids(Eve_Beam.getFluid(PLASMA, 1000))
                .outputItems(ChemicalHelper.get(ingot, Naquadria), 64)
                .EUt(1000000)
                .duration(500)
                .save(provider);
        BEAMS.recipeBuilder("rune_mana")// 魔力符文
                .addData("required_mana", 500000)
                .addData("mana", 100000)
                .circuitMeta(1)
                .inputItems(placeholder, 1024)
                .outputItems(runeMana, 1024)
                .EUt(1)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("ethereal_slate")// 虚空石板
                .addData("required_mana", 1250000)
                .addData("mana", 500000)
                .circuitMeta(1)
                .inputItems(DEMONIC_SLATE, 256)
                .outputItems(ETHEREAL_SLATE, 256)
                .EUt(7680)
                .duration(400)
                .save(provider);
        BEAMS.recipeBuilder("ethereal_slate")// 终焉石板
                .addData("required_mana", 2500000)
                .addData("mana", 1000000)
                .circuitMeta(1)
                .inputItems(ETHEREAL_SLATE, 128)
                .outputItems(ENDSLATE, 128)
                .EUt(1000000)
                .duration(1000)
                .save(provider);
        BEAMS.recipeBuilder("manasteel")// 魔力钢锭
                .addData("required_mana", 100000)
                .addData("mana", 10000)
                .circuitMeta(1)
                .inputItems(IRON_INGOT, 512)
                .outputItems(ChemicalHelper.get(ingot, ManaSteel), 512)
                .EUt(1)
                .duration(50)
                .save(provider);
        BEAMS.recipeBuilder("elementium")// 源质钢锭
                .addData("required_mana", 500000)
                .addData("mana", 100000)
                .circuitMeta(2)
                .inputItems(IRON_INGOT, 512)
                .outputItems(ChemicalHelper.get(ingot, Elementium), 512)
                .EUt(1)
                .duration(1000)
                .save(provider);
        BEAMS.recipeBuilder("alfsteel")// 精灵钢锭
                .addData("required_mana", 1000000)
                .addData("mana", 500000)
                .circuitMeta(3)
                .inputItems(IRON_INGOT, 512)
                .outputItems(ChemicalHelper.get(ingot, AlfSteel), 512)
                .EUt(4)
                .duration(200)
                .save(provider);
        BEAMS.recipeBuilder("mana_radiation_mixture")// 魔力辐射混合物
                .addData("required_mana", 5000000)
                .addData("mana", 5000000)
                .circuitMeta(1)
                .inputItems(ChemicalHelper.get(dust, Thorium), 300)
                .inputItems(ChemicalHelper.get(dust, Uranium235), 300)
                .inputItems(ChemicalHelper.get(dust, AlfSteel), 300)
                .inputItems(ChemicalHelper.get(dust, Naquadah), 300)
                .inputFluids(Zenith_essence.getFluid(30 * 1000))
                .outputFluids(Mana_Radiation_Mixture.getFluid(400 * 1000))
                .EUt(55555)
                .duration(400)
                .save(provider);
    }
}
