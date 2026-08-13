package com.magicbee.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMItems;
import com.magicbee.ctnhmana.registry.CMRecipeTypes;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public class ManaTransformerRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        CMRecipeTypes.MANA_TRANSFORMER_RECIPES.recipeBuilder(CTNHMana.id("crystal_catalyst1"))
                .inputItems(dust, PlatinumGroupSludge, 42)
                .notConsumable(CMItems.CRYSH_CATALYST)
                .outputItems(dust, Palladium, 2)
                .outputItems(dust, Platinum, 2)
                .outputItems(dust, Ruthenium, 2)
                .outputItems(dust, Rhodium, 2)
                .outputItems(dust, Osmium, 2)
                .outputItems(dust, Iridium, 2)
                .EUt(1920)
                .duration(500)
                .circuitMeta(1)
                .save(provider);
    }
}
