package com.moguang.ctnhmana.data.recipe;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.*;
import static com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static dev.shadowsoffire.apotheosis.ench.Ench.Items.*;
import static net.minecraft.world.item.Items.*;
import static vazkii.botania.common.block.BotaniaBlocks.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import vazkii.botania.common.lib.BotaniaTags;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.*;
import static com.moguang.ctnhmana.registry.multiblock.ManaMachine.MANA_MACERATOR;
import static dev.shadowsoffire.apotheosis.ench.Ench.Items.*;
import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModItems.*;
import java.util.function.Consumer;

import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;


@SuppressWarnings("removal")
public class MeteorCapturerRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        METEOR_CAPTURER_RECIPES.recipeBuilder("iron")
                .inputItems(IRON_BLOCK,64)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*128))
                .outputItems(ChemicalHelper.get(ore,Iron),256)
                .outputItems(ChemicalHelper.get(ore,Hematite),64)
                .outputItems(ChemicalHelper.get(ore,YellowLimonite),64)
                .outputItems(ChemicalHelper.get(ore,Goethite),64)
                .outputItems(ChemicalHelper.get(ore,Magnetite),64)
                .outputItems(ChemicalHelper.get(ore,Pyrite),64)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("naquadah_uranium_plutonium")
                .inputItems(ChemicalHelper.get(block,GTMaterials.Naquadria))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*4096))
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_naquadah_oxide_mixture_ore")),2048)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_uranium_ore")),1024)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_plutonium_ore")),1024)
                .duration(800)
                .EUt(98304000/800)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("oilsands")//油砂矿
                .inputItems(Oil.getBucket())
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*256))
                .outputItems(ChemicalHelper.get(ore,Oilsands),1024)
                .duration(800)
                .EUt(6144000/800)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("ancient_debris_gold")
                .inputItems(ChemicalHelper.get(block,NetherStar),144)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*384))
                .outputItems(ANCIENT_DEBRIS,1024)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:nether_gold_ore")),2048)
                .duration(400)
                .EUt(4888800/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("zinc_copper_tin_iron_andesite")
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:andesite_alloy_block")))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*128))
                .outputItems(ANDESITE,64)
                .outputItems(ChemicalHelper.get(ore,Zinc),64)
                .outputItems(ChemicalHelper.get(ore,Copper),64)
                .outputItems(ChemicalHelper.get(ore,Tin),64)
                .outputItems(ChemicalHelper.get(ore,Iron),256)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("palladium_platinum_cooperite_iridium_osmium_copper")
                .inputItems(GTMachines.ASSEMBLER[6])
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*1024))
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_palladium_ore_ore")),64)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_platinum_ore_ore")),256)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_cooperite_ore")),64)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_iridium_ore")),64)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_osmium_ore")),64)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_copper_ore")),64)
                .duration(400)
                .EUt(12888000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("ilmenite_bauxite_rutile_titanium")
                .inputItems(EMITTER_IV)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*1024))
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_ilmenite_ore")),512)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_bauxite_ore")),256)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_rutile_ore")),128)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mars_stone_titanium_ore")),64)
                .duration(400)
                .EUt(3072000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("cooper_chalcopyrite_tetrahedrite_bornite_chalcocite")
                .inputItems(ChemicalHelper.get(block,Copper))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*128))
                .outputItems(ChemicalHelper.get(ore,Copper),256)
                .outputItems(ChemicalHelper.get(ore,Chalcopyrite),64)
                .outputItems(ChemicalHelper.get(ore,Tetrahedrite),64)
                .outputItems(ChemicalHelper.get(ore,Bornite),64)
                .outputItems(ChemicalHelper.get(ore,Chalcocite),64)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("emerald_green_sapphire_malachite_olivine_monazite")
                .inputItems(ChemicalHelper.get(gemExquisite,GreenSapphire))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*192))
                .outputItems(ChemicalHelper.get(ore,Emerald),64)
                .outputItems(ChemicalHelper.get(ore,GreenSapphire),64)
                .outputItems(ChemicalHelper.get(ore,Malachite),64)
                .outputItems(ChemicalHelper.get(ore,Olivine),64)
                .outputItems(ChemicalHelper.get(ore,Monazite),64)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("coal_graphite")
                .inputItems(ChemicalHelper.get(block,Coke),64)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*192))
                .outputItems(ChemicalHelper.get(ore,Coal),5120)
                .outputItems(ChemicalHelper.get(ore,Graphite),5120)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("palladium_platinum_cooperite_pentlandite_tetrahedrite")
                .inputItems(EMITTER_HV)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*1024))
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:moon_stone_palladium_ore_ore")),64)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:moon_stone_platinum_ore_ore")),256)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:moon_stone_cooperite_ore")),128)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:moon_stone_pentlandite_ore")),64)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:moon_stone_tetrahedrite_ore")),64)
                .duration(400)
                .EUt(3072000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("galena_sphalerite")
                .inputItems(ChemicalHelper.get(block,Indium),9)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*1024))
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:netherrack_galena_ore")),2560)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:netherrack_sphalerite_ore")),2560)
                .duration(400)
                .EUt(12288000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("zenith_star")
                .inputItems(ZENITH_STAR)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*1920))
                .outputItems(INFUSED_BREATH,45)
                .outputItems(SOURCE_GEM_BLOCK.asItem(),64)
                .chancedOutput(SOURCE_GEM_BLOCK.asItem().getDefaultInstance(),(int) 8000f,32)
                .chancedOutput(INFUSED_BREATH.get().getDefaultInstance(),(int) 8000f,32)
                .chancedOutput(ChemicalHelper.get(ingot,AlfSteel),(int) 6000f,2)
                .chancedOutput(ChemicalHelper.get(ingot,ManaSteel),(int) 6000f,16)
                .chancedOutput(runeMana.asItem().getDefaultInstance(),(int) 8000f,2)
                .chancedOutput(STARLIGHT_RUNE.asItem().getDefaultInstance(),(int)8000f,1)
                .chancedOutput(ChemicalHelper.get(ingot,Elementium),(int) 6000f,2)
                .chancedOutput(ChemicalHelper.get(ingot,Super_Plus_Mana),(int) 6000f,1)
                .chancedOutput(ChemicalHelper.get(ingot,Plus_Mana),(int) 6000f,1)
                .chancedOutput(ChemicalHelper.get(ingot,Ultra_Mana),(int) 6000f,1)
                .chancedOutput(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:moon_stone_dust")).getDefaultInstance(),(int) 4000f,64)
                .chancedOutput(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:moon_stone_dust")).getDefaultInstance(),(int) 4000f,64)
                .chancedOutput(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mar_stone_dust")).getDefaultInstance(),(int) 4000f,64)
                .chancedOutput(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:mar_stone_dust")).getDefaultInstance(),(int) 4000f,64)
                .chancedOutput(BotaniaItems.elfQuartz.getDefaultInstance(),(int) 6000f,64)
                .chancedOutput(dragonstone.getDefaultInstance(),(int) 6000f,64)
                .chancedOutput(elfGlass.asItem().getDefaultInstance(),(int) 6000f,64)
                .duration(400)
                .EUt(104857600L/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("scheelite_tungstate")
                .inputItems(ChemicalHelper.get(block,Tungsten),32)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*1024))
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:endstone_scheelite_ore")),512)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:endstone_tungstate_ore")),512)
                .duration(400)
                .EUt(3072000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("aluminum_silicon_sulfur_phosphorus")
                .inputItems(EMITTER_MV)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*256))
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:endstone_aluminium_ore")),256)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:endstone_bauxite_ore")),256)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:endstone_cryolite_ore")),256)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:endstone_ilmenite_ore")),64)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("press")
                .inputItems(EMITTER_EV)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*256))
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ae2omnicells:omni_link_print_press")),1)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ae2omnicells:complex_link_print_press")),1)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ae2omnicells:multidimensional_expansion_print_press")),1)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("fused_mana")//融合魔力矿
                .inputItems(ChemicalHelper.get(block,Plus_Mana),9)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*192))
                .outputItems(ChemicalHelper.get(ore,Fused_Mana),2560)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("ruby_almandine_cinnabar_garnet_red_realgar_pyrope")
                .inputItems(ChemicalHelper.get(gemExquisite,Ruby))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*192))
                .outputItems(ChemicalHelper.get(ore,Ruby),64)
                .outputItems(ChemicalHelper.get(ore,Almandine),64)
                .outputItems(ChemicalHelper.get(ore,Cinnabar),64)
                .outputItems(ChemicalHelper.get(ore,GarnetRed),64)
                .outputItems(ChemicalHelper.get(ore,Realgar),64)
                .outputItems(ChemicalHelper.get(ore,Pyrope),64)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("naquadah_enriched_naquadria_oxide_mixture")
                .inputItems(EMITTER_ZPM)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*1024))
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:naquadah_oxide_mixture_ore")),512)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:enriched_naquadah_oxide_mixture_ore")),128)
                .outputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gtceu:naquadria_oxide_mixture_ore")),32)
                .duration(400)
                .EUt(80000000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("blue_topaz_diamond_lazurite_lapis_sapphire_sodalite_apatite")
                .inputItems(ChemicalHelper.get(gemExquisite,Sapphire))
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*192))
                .outputItems(ChemicalHelper.get(ore,BlueTopaz),64)
                .outputItems(ChemicalHelper.get(ore,Diamond),64)
                .outputItems(ChemicalHelper.get(ore,Lazurite),64)
                .outputItems(ChemicalHelper.get(ore,Lapis),64)
                .outputItems(ChemicalHelper.get(ore,Sapphire),64)
                .outputItems(ChemicalHelper.get(ore,Sodalite),64)
                .outputItems(ChemicalHelper.get(ore,Apatite),64)
                .duration(400)
                .EUt(768000/400)
                .save(provider);
        METEOR_CAPTURER_RECIPES.recipeBuilder("tantalite")//钽铁矿
                .inputItems(EMITTER_LuV)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1000*512))
                .outputItems(ChemicalHelper.get(ore,Tantalite),384)
                .duration(400)
                .EUt(100000)
                .save(provider);
    }
}
