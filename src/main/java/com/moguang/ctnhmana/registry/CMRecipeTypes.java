package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

public class CMRecipeTypes {
    public static final GTRecipeType MANA_REACTOR_RECIPES =
            REGISTRATE.recipeType(GTCEu.id("mana_reactor"), GTRecipeTypes.ELECTRIC)
                    .cnlang("魔力反应").setMaxIOSize(6, 6, 3, 3)
                    .setEUIO(IO.IN)
                    .setMaxTooltips(5)
                    .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE,  ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                    .setSound(GTSoundEntries.CHEMICAL);
    public static void init() {}
}
