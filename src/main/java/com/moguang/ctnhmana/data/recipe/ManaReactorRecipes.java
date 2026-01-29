package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.data.tags.ItemTagLoader;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import com.moguang.ctnhmana.common.recipe.PlantCasingCondition;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.multiblock.Botania;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import dev.latvian.mods.kubejs.KubeJS;
import io.github.lounode.extrabotany.common.block.ExtraBotanyBlock;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.nbt.TagTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.units.qual.C;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.*;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.*;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;

import javax.swing.text.html.HTML;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.*;
import static dev.shadowsoffire.apotheosis.ench.Ench.Items.*;
import static io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.*;
import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModItems.*;
import java.util.function.Consumer;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gregtechceu.gtceu.common.data.GTFluids.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static com.simibubi.create.AllItems.BLAZE_CAKE;
import static net.minecraft.world.item.ItemStack.TooltipPart.*;
import static vazkii.botania.common.block.BotaniaBlocks.*;
import static vazkii.botania.common.item.BotaniaItems.*;
import static wayoftime.bloodmagic.common.fluid.BloodMagicFluids.*;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;


@SuppressWarnings("removal")
public class ManaReactorRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        MANA_REACTOR_RECIPES.recipeBuilder("test11")
                .addCondition(new ManaReactorCondition(true,"GT",4))
                .inputItems(runeMana,24)
                .inputFluids(Zenith_essence.getFluid(144))
                .outputItems(HORIZEN_RUNE)
                .duration(200)
                .circuitMeta(5)
                .EUt(114514)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("test22")
                .addCondition(new ManaReactorCondition(true))
                .inputItems(runeFire,24)
                .inputFluids(Zenith_essence.getFluid(144))
                .outputItems(STARLIGHT_RUNE)
                .duration(200)
                .circuitMeta(2)
                .EUt(114514)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("test33")
                .addCondition(new ManaReactorCondition(true))
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:chain")))
                .inputFluids(Zenith_essence.getFluid(144))
                .outputItems(HORIZEN_RUNE)
                .duration(200)
                .circuitMeta(2)
                .EUt(114514)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("gjallarHornempty")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(grassHorn,1)
                .outputItems(gjallarHornEmpty)
                .duration(60)
                .circuitMeta(1)
                .EUt(512)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("blocks_manasteel1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.IRON_BLOCK,1)
                .inputFluids(Mana,540)
                .outputItems(ChemicalHelper.get(block,ManaSteel,1))
                .duration(500)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("blocks_manasteel2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.IRON_INGOT,9)
                .inputFluids(Mana,540)
                .outputItems(ChemicalHelper.get(block,ManaSteel,1))
                .duration(500)
                .circuitMeta(9)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_resistor")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.RESISTOR,1)
                .outputItems(MANA_RESISTOR,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manasteel1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.IRON_INGOT,1)
                .inputFluids(Mana,60)
                .outputItems(ChemicalHelper.get(ingot,ManaSteel),1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manasteel2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.IRON_BLOCK,1)
                .outputItems(ChemicalHelper.get(ingot,ManaSteel),9)
                .inputFluids(Mana,540)
                .duration(500)
                .circuitMeta(9)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_transistor")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.TRANSISTOR,1)
                .outputItems(MANA_TRANSISTOR,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_capacitor")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.CAPACITOR,1)
                .outputItems(MANA_CAPACITOR,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_diode")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.DIODE,1)
                .outputItems(MANA_DIODE,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_inductor")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.INDUCTOR,1)
                .outputItems(MANA_INDUCTOR,1)
                .duration(60)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("friedchicken")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.COOKED_CHICKEN,1)
                .outputItems(friedChicken,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(8)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("grassseeds")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GRASS,1)
                .outputItems(grassSeeds,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manabottle")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GLASS_BOTTLE,1)
                .outputItems(manaBottle,1)
                .duration(120)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manacookie")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.COOKIE,1)
                .outputItems(manaCookie,1)
                .duration(100)
                .circuitMeta(1)
                .EUt(128)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manadiamond1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DIAMOND,1)
                .outputItems(manaDiamond,1)
                .duration(100)
                .circuitMeta(1)
                .EUt(128)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manadiamond2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DIAMOND_BLOCK,1)
                .outputItems(manaDiamond,9)
                .duration(1000)
                .circuitMeta(9)
                .EUt(512)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manadiamondblock1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DIAMOND_BLOCK,1)
                .outputItems(manaDiamondBlock.asItem(),1)
                .duration(1000)
                .circuitMeta(1)
                .EUt(512)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manadiamondblock2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DIAMOND,9)
                .outputItems(manaDiamondBlock.asItem(),1)
                .duration(1000)
                .circuitMeta(9)
                .EUt(512)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("managlass")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GLASS,1)
                .outputItems(manaGlass.asItem(),1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("managlass")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.ENDER_PEARL,1)
                .outputItems(manaPearl,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manapowder1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.REDSTONE,1)
                .outputItems(manaPowder,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manapowder2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GLOWSTONE_DUST,1)
                .outputItems(manaPowder,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manapowder3")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.GUNPOWDER,1)
                .outputItems(manaPowder,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manapowder4")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.SUGAR,1)
                .outputItems(manaPowder,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manaquartz")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.QUARTZ,1)
                .outputItems(BotaniaItems.manaQuartz,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manastring")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.STRING,1)
                .outputItems(manaString,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mycelseeds1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.BROWN_MUSHROOM,1)
                .outputItems(mycelSeeds,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(64)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mycelseeds2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.RED_MUSHROOM,1)
                .outputItems(mycelSeeds,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(64)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("nightmarefuel")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.COAL,1)
                .outputItems(nightmareFuel,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("pistonrelay")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.PISTON,1)
                .outputItems(pistonRelay.asItem(),1)
                .duration(40)
                .circuitMeta(1)
                .EUt(128)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("tinypotato")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.POTATO,1)
                .outputItems(tinyPotato.asItem(),1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("podzolseeds")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.DEAD_BUSH,1)
                .outputItems(podzolSeeds,1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manaberries")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Items.SWEET_BERRIES,1)
                .outputItems(new ResourceLocation("createcafe:mana_berries"),1)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("manapowder5")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(Tags.Items.DYES,1)
                .outputItems(manaPowder,1)
                .notConsumable(Items.RED_DYE)
                .duration(40)
                .circuitMeta(1)
                .EUt(32)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("zenithessence")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(BlockRegistry.SOURCE_GEM_BLOCK.asItem(),1)
                .inputItems(INFUSED_BREATH,1)
                .inputFluids(Mana.getFluid(10000))
                .inputFluids(Krypton,10000)
                .inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 10000))
                .outputFluids(Zenith_essence.getFluid(2000))
                .duration(200)
                .circuitMeta(1)
                .EUt(IV)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("stable_plus_mana_dust")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(ChemicalHelper.get(dust,Infused_Plus_Mana,10))
                .outputItems(ChemicalHelper.get(dust,Stable_Plus_Mana),15)
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(2000))
                .duration(200)
                .circuitMeta(1)
                .EUt(1440)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("alfsteel1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(elementium)
                .inputItems(dragonstone)
                .inputItems(BotaniaItems.elfQuartz)
                .inputFluids(Mana,10000)
                .inputItems(elfGlass.asItem(),1)
                .chancedOutput(alfsteelIngot.getDefaultInstance(), (int) 5000f,15)
                .duration(1600)
                .circuitMeta(0)
                .EUt(480)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("alfsteel2")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(elementium)
                .inputItems(dragonstone)
                .inputItems(BotaniaItems.elfQuartz)
                .inputFluids(Mana,8000)
                .inputItems(elfGlass.asItem(),1)
                .notConsumable(ELF_CATALYST)
                .chancedOutput(alfsteelIngot.getDefaultInstance(),(int) 7500f,15)
                .duration(800)
                .circuitMeta(1)
                .EUt(1680)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("alfsteel3")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(elementium)
                .inputItems(dragonstone)
                .inputItems(BotaniaItems.elfQuartz)
                .inputFluids(Mana,8000)
                .inputItems(elfGlass.asItem(),1)
                .notConsumable(TERRA_CATALYST)
                .outputItems(AlfSteel)
                .duration(800)
                .circuitMeta(1)
                .EUt(7200)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("terra_catalyst")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(niflheimRune,64)
                .inputItems(gaiaIngot,64)
                .inputItems(AlfSteel,64)
                .inputFluids(Mana,1200*1000)
                .inputItems(runeMana,64)
                .inputItems(ELF_CATALYST)
                .chancedOutput(TERRA_CATALYST.asStack(),(int) 5000f,1)
                .outputItems(ELF_CATALYST)
                .duration(6000)
                .circuitMeta(1)
                .EUt(960)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("pixie_dust9")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(manaPearl)
                .inputFluids(Mana.getFluid(500))
                .notConsumable(ELF_CATALYST)
                .outputItems(pixieDust)
                .duration(100)
                .circuitMeta(1)
                .EUt(480)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("eternal_garden")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(runeSpring)
                .inputItems(runeSummer)
                .inputItems(runeAutumn)
                .inputItems(runeWinter)
                .inputItems(BT_UPDATE_T1)
                .inputItems(BotaniaTags.Items.MYSTICAL_FLOWERS,64)
                .inputFluids(Mana.getFluid(10000))
                .notConsumable(ELF_CATALYST)
                .outputItems(Botania.ETERNAL_GARDEN)
                .duration(400*20)
                .circuitMeta(1)
                .EUt(512)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("dragonstone1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(manaDiamond,2)
                .inputFluids(Mana.getFluid(500))
                .notConsumable(ELF_CATALYST)
                .outputItems(dragonstone)
                .duration(100)
                .circuitMeta(1)
                .EUt(480)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("super_plus_mana_dust")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(ChemicalHelper.get(dust,Stable_Plus_Mana,8))
                .inputFluids(Zenith_essence.getFluid(200))
                .notConsumable(ELF_CATALYST)
                .outputItems(ChemicalHelper.get(dust,Super_Plus_Mana,8))
                .duration(200)
                .circuitMeta(1)
                .EUt(7700)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_macerator")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(ChemicalHelper.get(screw,AlfSteel,64))
                .inputItems(GTMachines.MACERATOR[2],4)
                .inputItems(GTItems.ROBOT_ARM_MV,2)
                .inputItems(GTItems.COMPONENT_GRINDER_DIAMOND,16)
                .inputFluids(Mana.getFluid(1000))
                .outputItems(MANA_MACERATOR)
                .duration(400)
                .circuitMeta(1)
                .EUt(480)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("elfglass1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(manaGlass.asItem(),1)
                .inputFluids(Mana.getFluid(500))
                .notConsumable(ELF_CATALYST)
                .outputItems(elfGlass.asItem(),1)
                .duration(100)
                .circuitMeta(1)
                .EUt(480)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("bifrost_perm")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(elfGlass.asItem(),1)
                .notConsumable(rainbowRod)
                .outputItems(bifrostPerm.asItem())
                .duration(100)
                .circuitMeta(1)
                .EUt(480)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("bt_update_t1")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(HORIZEN_RUNE,32)
                .inputItems(TWIST_RUNE,32)
                .inputItems(STARLIGHT_RUNE,32)
                .inputItems(PROLIFERATION_RUNE,64)
                .inputItems(GTItems.STEM_CELLS,128)
                .inputItems(ChemicalHelper.get(dustTiny,Ultra_Mana),64)
                .inputFluids(Zenith_essence.getFluid(10000))
                .outputItems(BT_UPDATE_T1)
                .duration(40*20)
                .circuitMeta(1)
                .EUt(UV)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("elementium_quartz")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(BotaniaItems.elfQuartz,2)
                .inputFluids(Mana.getFluid(10))
                .outputItems(elementiumQuartz)
                .duration(20)
                .circuitMeta(3)
                .EUt(480)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("life_essence")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(runeFire)
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty:eyebulb")))
                .inputItems(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("biomesoplenty:blood_bucket")),4)
                .inputFluids(Mana.getFluid(1000))
                .outputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 4000))
                .duration(2*20)
                .circuitMeta(1)
                .EUt(480)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("twist_mana")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(ChemicalHelper.get(dust,Uranium235),1)
                .inputItems(ChemicalHelper.get(dust,Thorium),1)
                .inputItems(ChemicalHelper.get(dust,Unknown_Super_Mana),1)
                .inputItems(ChemicalHelper.get(dust,Plutonium241),1)
                .inputFluids(Radon,200)
                .notConsumable(TERRA_CATALYST)
                .outputItems(ChemicalHelper.get(dust,Twist_Mana),3)
                .duration(100)
                .circuitMeta(1)
                .EUt(12120)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("plus_mana_dust")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(ChemicalHelper.get(dust,Fused_demon_mixed),10)
                .inputFluids(Mana.getFluid(10000))
                .outputItems(ChemicalHelper.get(dust,Plus_Mana),8)
                .duration(5*20)
                .circuitMeta(1)
                .EUt(1000)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("zenith_star")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(GTItems.QUANTUM_STAR)
                .inputItems(runeMana)
                .inputItems(vanaheimRune,1)
                .inputItems(ChemicalHelper.get(ingot,Ultra_Mana),4)
                .inputFluids(Mana.getFluid(360*1000))
                .inputFluids(Zenith_essence.getFluid(90*1000))
                .outputItems(ZENITH_STAR)
                .duration(5*20)
                .circuitMeta(1)
                .EUt(1000)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("mana_circuit_board")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(STARLIGHT_RUNE)
                .inputItems(ChemicalHelper.get(plate,ReinforcedEpoxyResin),32)
                .inputItems(ChemicalHelper.get(foil,Ultra_Mana),32)
                .inputItems(vanaheimRune,1)
                .inputItems(ChemicalHelper.get(ingot,Ultra_Mana),4)
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(16*1000))
                .inputFluids(Zenith_essence.getFluid(4*1000))
                .outputItems(MANA_CIRCUIT_BOARD,16)
                .duration(5*20)
                .circuitMeta(1)
                .EUt(1000)
                .save(provider);
        MANA_REACTOR_RECIPES.recipeBuilder("infused_plus_mana_dust")
                .addCondition(new ManaReactorCondition(false))
                .inputItems(midgardRune)
                .inputItems(runeMana)
                .inputItems(ChemicalHelper.get(dust,Unstable_Plus_Mana),1)
                .inputFluids(MANA_STABLE_COOLDOWN.getFluid(16*1000))
                .inputFluids(Zenith_essence.getFluid(4*1000))
                .outputItems(ChemicalHelper.get(dust,Infused_Plus_Mana),1)
                .duration(3*20)
                .circuitMeta(1)
                .EUt(1145141919810L)
                .save(provider);
    }
}
