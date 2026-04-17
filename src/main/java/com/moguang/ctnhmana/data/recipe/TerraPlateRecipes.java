package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.common.recipe.builder.ElfPlateRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.TerraPlateRecipeBuilder;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMachines;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import com.simibubi.create.AllItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModItems.helheimRune;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;

@SuppressWarnings("removal")
public class TerraPlateRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        TerraPlateRecipeBuilder.builder("terrasteel123")// 泰拉钢
                .input(new ItemStack(BotaniaItems.manaDiamond, 1))
                .input(new ItemStack(BotaniaItems.manaPearl, 1))
                .input(new ItemStack(BotaniaItems.manaSteel, 1))
                .input(new ItemStack(BotaniaItems.runeMana, 1))
                .output(new ItemStack(BotaniaItems.terrasteel, 1))
                .mana(500000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("life_essence_bucket")// 生命精华桶
                .input(new ItemStack(BotaniaItems.runeFire, 1))
                .input(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty:eyebulb")))
                .input(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty:blood_bucket")))
                .output(new ItemStack(LIFE_ESSENCE_BUCKET.get(), 1))
                .mana(50000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("the_universe")// 宇宙之忆
                .input(new ItemStack(theEnd, 1))
                .input(new ItemStack(theChaos, 1))
                .input(new ItemStack(theOrigin, 1))
                .input(new ItemStack(TWIST_RUNE,1))
                .input(new ItemStack(STARLIGHT_RUNE,1))
                .input(new ItemStack(PROLIFERATION_RUNE,1))
                .input(new ItemStack(HORIZEN_RUNE,1))
                .output(new ItemStack(theUniverse, 1))
                .mana(4000000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("aerialite")// 风之石
                .input(new ItemStack(Items.PHANTOM_MEMBRANE, 1))
                .input(new ItemStack(BotaniaItems.enderAirBottle, 1))
                .input(new ItemStack(BotaniaItems.dragonstone, 1))
                .output(new ItemStack(aerialite, 1))
                .mana(4000000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("rhein_hammer")// 莱茵之锤
                .input(new ItemStack(photoniumHammer, 1))
                .input(new ItemStack(shadowiumHammer, 1))
                .input(new ItemStack(elementiumHammer, 1))
                .input(new ItemStack(terrasteelHammer, 1))
                .input(new ItemStack(gaiaHammer, 1))
                .input(new ItemStack(aerialiteHammer, 1))
                .input(new ItemStack(orichalcosHammer, 1))
                .input(new ItemStack(dasRheingold, 1))
                .input(new ItemStack(manasteelHammer, 1))
                .input(new ItemStack(theUniverse, 1))
                .output(new ItemStack(rheinHammer, 1))
                .allowReactor(false)
                .mana(4000000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("koishi_pain")// 古明地眼
                .input(BotaniaItems.runeLust)
                .input(helheimRune, BotaniaItems.redString, BotaniaItems.thirdEye,
                        BotaniaFlowerBlocks.rosaArcana.asItem(), BloodMagicItems.LIFE_ESSENCE_BUCKET.get())
                .input(ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Stone, 1))
                .input(CustomTags.KNIVES)
                .output(new ItemStack(CMItems.KOISHI_EYE, 1))
                .mana(5145140)
                .circuitMeta(10)
                .save(provider);
        ElfPlateRecipeBuilder.builder("saber_alf")
                .input(BotaniaItems.dreamwoodWand)
                .input(CustomTags.HV_CIRCUITS)
                .input(MAGIC_CORE.asStack().getItem())
                .output(SABER_WAND.asStack().getItem())
                .mana(1000000)
                .save(provider);
        ElfPlateRecipeBuilder.builder("star_cake")
                .output(CMMachines.STAR_FLOWER_CAKE.asStack())
                .input(Blocks.CAKE)
                .input(BotaniaItems.flightTiara)
                .input(BotaniaItems.runeGluttony)
                .input(BotaniaFlowerBlocks.gourmaryllis)
                .input(AllItems.BAR_OF_CHOCOLATE)
                .mana(90000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("mixin_will_steel")
                .input(photonium)
                .input(shadowium)
                .input(BloodMagicItems.RAW_CRYSTAL.get())
                .input(BloodMagicItems.CORROSIVE_CRYSTAL.get())
                .input(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get())
                .input(BloodMagicItems.VENGEFUL_CRYSTAL.get())
                .input(BloodMagicItems.STEADFAST_CRYSTAL.get())
                .output(ChemicalHelper.get(TagPrefix.ingot, CMMaterials.PRIMOVOLITHEST))
                .mana(77778)
                .save(provider);
        // ElfPlateRecipeBuilder.builder("oriculos")
        // .input(photonium)
        // .input(shadowium)
        // .input(ModItems.alfsteelIngot)
        // .input(BotaniaItems.manaSteel)
        // .input(BotaniaItems.terrasteel)
        // .input(BotaniaItems.gaiaIngot)
        // .input(BotaniaItems.elementium)
        // .input(theOrigin)
        // .output(orichalcos)
        // .mana(3000000)
        // .save(provider);
        // CMRecipeTypes.MANA_REACTOR_RECIPES.recipeBuilder("oriculos_reactor")
        // .inputItems(photonium)
        // .inputItems(shadowium)
        // .inputItems(ModItems.alfsteelIngot)
        // .inputItems(BotaniaItems.manaSteel)
        // .inputItems(BotaniaItems.terrasteel)
        // .inputItems(BotaniaItems.gaiaIngot)
        // .inputItems(BotaniaItems.elementium)
        // .notConsumable(TERRA_CATALYST)
        // .outputItems(orichalcos)
        // .EUt(GTValues.VA[GTValues.EV])
        // .duration(400)
        // .save(provider);
        TerraPlateRecipeBuilder.builder("mana_rune")
                .input(BotaniaBlocks.livingrock.asItem())
                .input(BotaniaItems.manaDiamond.asItem())
                .input(ChemicalHelper.get(TagPrefix.dust, CMMaterials.Elementium))
                .output(BotaniaItems.runeMana.asItem().getDefaultInstance())
                .circuitMeta(3)
                .mana(10000)
                .save(provider);
        CMRecipeTypes.MANA_FORGE_RECIPES.recipeBuilder("break_oriculos")
                .inputItems(orichalcos)
                .outputItems(UNIMBUED_SPIRIT.get(), 4)
                .EUt(128)
                .duration(4000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("build_orichalcos")
                .input(ORICHALCOS_SPIRIT.asItem())
                .input(BotaniaItems.manaSteel.asItem())
                .input(ChemicalHelper.get(TagPrefix.dust, CMMaterials.Fused_Mana))
                .output(orichalcos.asItem().getDefaultInstance())
                .mana(100000)
                .circuitMeta(17)
                .save(provider);
        TerraPlateRecipeBuilder.builder("the_orgin")
                .input(ChemicalHelper.get(TagPrefix.ingot,CMMaterials.ManaSteel))
                .input(ChemicalHelper.get(TagPrefix.ingot,CMMaterials.AlfSteel))
                .input(ChemicalHelper.get(TagPrefix.ingot,CMMaterials.Elementium))
                .input(BotaniaItems.gaiaIngot)
                .input(photonium)
                .input(aerialite)
                .input(BotaniaItems.runeMana)
                .input(BotaniaFlowerBlocks.pureDaisy.asItem())
                .input(BotaniaBlocks.livingrock.asItem())
                .output(theOrigin.asItem().getDefaultInstance())
                .mana(123456)
                .save(provider);
    }
}