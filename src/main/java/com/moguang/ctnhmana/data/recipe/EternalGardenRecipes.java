package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.unification.material.MaterialRegistryManager;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.fml.ModList;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import vazkii.botania.common.block.BotaniaFlowerBlocks;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTItems.QUANTUM_STAR;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.reikarlily;
import static mythicbotany.register.ModBlocks.witherAconite;
import static net.minecraft.world.item.Items.NETHER_STAR;

/**
 * Registered Eternal Garden flower recipes. Dynamic food/fuel recipes stay in
 * {@link EternalGardenSpecialRecipes} + {@link com.moguang.ctnhmana.api.recipe.customlogic.EternalGardenLogic}.
 * <p>
 * {@code type} data must match {@link com.moguang.ctnhmana.common.multiblock.EternalGarden#recipeModifier}.
 */
public class EternalGardenRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // 水绣球
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("hydroangeas_water"))
                .inputItems(BotaniaFlowerBlocks.hydroangeas.asItem())
                .inputFluids(Water.getFluid(1000))
                .outputFluids(CMMaterials.Mana.getFluid(300))
                .addData("type", "water")
                .circuitMeta(1)
                .EUt(256)
                .duration(20)
                .save(provider);
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("hydroangeas_distilled_water"))
                .inputItems(BotaniaFlowerBlocks.hydroangeas.asItem())
                .inputFluids(DistilledWater.getFluid(1000))
                .outputFluids(CMMaterials.Mana.getFluid(600))
                .addData("type", "water")
                .circuitMeta(2)
                .EUt(12800 / 25)
                .duration(25)
                .save(provider);

        // 勿落草
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("shulk_me_not"))
                .notConsumable(BotaniaFlowerBlocks.shulkMeNot.asItem())
                .outputFluids(CMMaterials.Mana.getFluid(4))
                .addData("type", "fly")
                .circuitMeta(1)
                .EUt(256)
                .duration(1)
                .save(provider);

        // 炽玫瑰（recipeModifier: blame）— pyrotheum 配方仅在加载 CTNH-Core 时注册
        if (ModList.get().isLoaded("ctnhcore")) {
            Material pyrotheum = MaterialRegistryManager.getInstance().getMaterial("ctnhcore:pyrotheum");
            if (pyrotheum != null) {
                CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("thermalily_pyrotheum"))
                        .notConsumable(BotaniaFlowerBlocks.thermalily.asItem())
                        .inputFluids(pyrotheum.getFluid(1000))
                        .outputFluids(CMMaterials.Mana.getFluid(2000))
                        .addData("type", "blame")
                        .circuitMeta(1)
                        .EUt(256)
                        .duration(50)
                        .save(provider);
            }
        }
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("thermalily_blaze"))
                .notConsumable(BotaniaFlowerBlocks.thermalily.asItem())
                .inputFluids(Blaze.getFluid(1000))
                .outputFluids(CMMaterials.Mana.getFluid(1000))
                .addData("type", "blame")
                .circuitMeta(2)
                .EUt(256)
                .duration(50)
                .save(provider);
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("thermalily_lava"))
                .notConsumable(BotaniaFlowerBlocks.thermalily.asItem())
                .inputFluids(Lava.getFluid(1000))
                .outputFluids(CMMaterials.Mana.getFluid(500))
                .addData("type", "blame")
                .circuitMeta(3)
                .EUt(256)
                .duration(50)
                .save(provider);

        // 凋零菟葵
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("wither_aconite_nether_star"))
                .notConsumable(witherAconite.asItem())
                .inputItems(NETHER_STAR)
                .outputFluids(CMMaterials.Mana.getFluid(100000))
                .addData("type", "wither")
                .circuitMeta(1)
                .EUt(256)
                .duration(2000)
                .save(provider);
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("wither_aconite_quantum_star"))
                .notConsumable(witherAconite.asItem())
                .inputItems(QUANTUM_STAR)
                .outputFluids(CMMaterials.Mana.getFluid(400000))
                .addData("type", "wither")
                .circuitMeta(2)
                .EUt(256)
                .duration(2500)
                .save(provider);

        // 雷卡兰
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("reikarlily_12"))
                .notConsumable(reikarlily.asItem())
                .outputFluids(CMMaterials.Mana.getFluid(1))
                .addData("type", "lighting")
                .addData("light", true)
                .circuitMeta(12)
                .EUt(256)
                .duration(1000)
                .save(provider);
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder(CTNHMana.id("reikarlily_24"))
                .notConsumable(reikarlily.asItem())
                .outputFluids(CMMaterials.Mana.getFluid(1))
                .addData("type", "lighting")
                .addData("light", false)
                .circuitMeta(24)
                .EUt(256)
                .duration(100)
                .save(provider);
    }
}
