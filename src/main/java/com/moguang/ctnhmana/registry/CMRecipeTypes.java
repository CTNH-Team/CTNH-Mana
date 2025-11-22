package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

public class CMRecipeTypes {
    public static final GTRecipeType MANA_REACTOR_RECIPES =
            REGISTRATE.recipeType(GTCEu.id("mana_reactor"), GTRecipeTypes.ELECTRIC)
                    .cnlang("魔力反应").setMaxIOSize(9, 9, 6, 6)
                    .setMaxTooltips(5)
                    .setSound(GTSoundEntries.COOLING);
    public static void init() {}
}
