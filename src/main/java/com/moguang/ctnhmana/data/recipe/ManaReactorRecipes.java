package com.moguang.ctnhmana.data.recipe;

import com.moguang.ctnhmana.common.recipe.ManaReactorCondition;
import com.moguang.ctnhmana.common.recipe.PlantCasingCondition;
import net.minecraft.data.recipes.FinishedRecipe;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES;
import static com.moguang.ctnhmana.registry.CMItems.*;
import static com.moguang.ctnhmana.registry.CMMaterials.*;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.*;
import static vazkii.botania.common.item.BotaniaItems.*;


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
                .outputItems(HORIZEN_RUNE)
                .duration(200)
                .circuitMeta(2)
                .EUt(114514)
                .save(provider);
    }
}
