package com.moguang.ctnhmana.data.recipe;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.moguang.ctnhmana.common.recipe.builder.botania.TerraPlateRecipeBuilder;
import com.moguang.ctnhmana.registry.CMItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
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
        TerraPlateRecipeBuilder.builder("terrasteel123")//泰拉钢
                .input(new ItemStack(BotaniaItems.manaDiamond,1))
                .input(new ItemStack(BotaniaItems.manaPearl,1))
                .input(new ItemStack(BotaniaItems.manaSteel,1))
                .input(new ItemStack(BotaniaItems.runeMana,1))
                .output(new ItemStack(BotaniaItems.terrasteel,1))
                .mana(500000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("life_essence_bucket")//生命精华桶
                .input(new ItemStack(BotaniaItems.runeFire,1))
                .input(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty:eyebulb")))
                .input(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty:blood_bucket")))
                .output(new ItemStack(LIFE_ESSENCE_BUCKET.get(),1))
                .mana(50000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("the_universe")//宇宙之忆
                .input(new ItemStack(theEnd,1))
                .input(new ItemStack(theChaos,1))
                .input(new ItemStack(theOrigin,1))
                .output(new ItemStack(theUniverse,1))
                .mana(4000000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("aerialite")//风之石
                .input(new ItemStack(Items.PHANTOM_MEMBRANE,1))
                .input(new ItemStack(BotaniaItems.enderAirBottle,1))
                .input(new ItemStack(BotaniaItems.dragonstone,1))
                .output(new ItemStack(aerialite,1))
                .mana(4000000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("rhein_hammer")//莱茵之锤
                .input(new ItemStack(photoniumHammer,1))
                .input(new ItemStack(shadowiumHammer,1))
                .input(new ItemStack(elementiumHammer,1))
                .input(new ItemStack(terrasteelHammer,1))
                .input(new ItemStack(gaiaHammer,1))
                .input(new ItemStack(aerialiteHammer,1))
                .input(new ItemStack(orichalcosHammer,1))
                .input(new ItemStack(dasRheingold,1))
                .input(new ItemStack(manasteelHammer,1))
                .input(new ItemStack(theUniverse,1))
                .output(new ItemStack(rheinHammer,1))
                .mana(4000000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("koishi_pain")//古明地眼
                .input(BotaniaItems.runeLust)
                .input(helheimRune,BotaniaItems.redString,BotaniaItems.thirdEye, BotaniaFlowerBlocks.rosaArcana.asItem(), BloodMagicItems.LIFE_ESSENCE_BUCKET.get())
                .input(ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Stone,1))
                .input(CustomTags.KNIVES)
                .output(new ItemStack(CMItems.KOISHI_EYE,1))
                .mana(5145140)
                .circuitMeta(10)
                .save(provider);
        TerraPlateRecipeBuilder.builder("mana_electronic_circuit")//魔力电子电路
                .input(MANA_RESISTOR.asStack())
                .input(MANA_CAPACITOR.asStack())
                .input(MANA_DIODE.asStack())
                .input(ELECTRONIC_CIRCUIT_MV.asStack())
                .input(MANA_SOC.asStack())
                .output(MANA_ELECTRONIC_CIRCUIT.asStack())
                .mana(100000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("mana_integrated_circuit")//魔力集成电路
                .input(ADVANCED_MANA_RESISTOR.asStack())
                .input(ADVANCED_MANA_CAPACITOR.asStack())
                .input(ADVANCED_MANA_DIODE.asStack())
                .input(ELECTRONIC_CIRCUIT_MV.asStack())
                .input(MANA_SOC.asStack())
                .output(MANA_INTEGRATED_CIRCUIT.asStack())
                .mana(250000)
                .save(provider);
    }

}
