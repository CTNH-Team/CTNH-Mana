package com.moguang.ctnhmana.data.recipe;
import com.moguang.ctnhmana.common.recipe.builder.botania.TerraPlateRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;

@SuppressWarnings("removal")
public class TerraPlateRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        TerraPlateRecipeBuilder.builder("terrasteel123")
                .input(new ItemStack(BotaniaItems.manaDiamond,1))
                .input(new ItemStack(BotaniaItems.manaPearl,1))
                .input(new ItemStack(BotaniaItems.manaSteel,1))
                .input(new ItemStack(BotaniaItems.runeMana,1))
                .output(new ItemStack(BotaniaItems.terrasteel,1))
                .mana(500000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("life_essence_bucket")
                .input(new ItemStack(BotaniaItems.runeFire,1))
                .input(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty:eyebulb")))
                .input(ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty:blood_bucket")))
                .output(new ItemStack(LIFE_ESSENCE_BUCKET.get(),1))
                .mana(50000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("the_universe")
                .input(new ItemStack(theEnd,1))
                .input(new ItemStack(theChaos,1))
                .input(new ItemStack(theOrigin,1))
                .output(new ItemStack(theUniverse,1))
                .mana(4000000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("aerialite")
                .input(new ItemStack(Items.PHANTOM_MEMBRANE,1))
                .input(new ItemStack(BotaniaItems.enderAirBottle,1))
                .input(new ItemStack(BotaniaItems.dragonstone,1))
                .output(new ItemStack(aerialite,1))
                .mana(4000000)
                .save(provider);
        TerraPlateRecipeBuilder.builder("rhein_hammer")
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
    }

}
