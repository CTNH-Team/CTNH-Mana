package com.moguang.ctnhmana.data.recipe;
import com.github.L_Ender.cataclysm.items.CMBlockItem;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.models.GTMachineModels;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.moguang.ctnhmana.common.recipe.builder.PetalRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.TerraPlateRecipeBuilder;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.multiblock.Botania;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.mjoellnir.Mjoellnir;
import net.minecraft.ResourceLocationException;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.checkerframework.checker.units.qual.C;
import vazkii.botania.common.block.BotaniaBlock;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlock;
import vazkii.botania.common.crafting.recipe.PhantomInkRecipe;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import javax.sound.sampled.LineEvent;
import javax.swing.text.html.HTML;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.*;
import static io.github.lounode.extrabotany.common.item.ExtraBotanyItems.*;
import static mythicbotany.register.ModItems.*;
import static com.moguang.ctnhmana.data.recipe.utils.BotaniaIngredients.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static mythicbotany.register.ModBlocks.*;
import static wayoftime.bloodmagic.common.item.BloodMagicItems.*;

@SuppressWarnings("removal")
public class TerraPlateRecipe {
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
