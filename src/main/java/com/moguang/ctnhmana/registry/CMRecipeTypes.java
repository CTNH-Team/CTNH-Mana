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
                    .cnlang("魔力反应").setMaxIOSize(9, 9, 6, 6)
                    .setEUIO(IO.IN)
                    .setMaxTooltips(5)
                    .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE,  ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                    .setSound(GTSoundEntries.CHEMICAL);
    public static final GTRecipeType MANA_TRANSFORMER_RECIPES =
            REGISTRATE.recipeType(GTCEu.id("mana_transformer"), GTRecipeTypes.ELECTRIC)
                    .cnlang("魔力操纵").setMaxIOSize(9, 9, 6, 6)
                    .setEUIO(IO.IN)
                    .setMaxTooltips(5)
                    .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE,  ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                    .setSound(GTSoundEntries.CHEMICAL);
    public static final GTRecipeType HELL_FORGE_RECIPES =
            REGISTRATE.recipeType(GTCEu.id("hell_forge"), GTRecipeTypes.ELECTRIC)
                    .cnlang("狱火铸造").setMaxIOSize(6, 6, 3, 3)
                    .setEUIO(IO.IN)
                    .setMaxTooltips(5)
                    .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE,  ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                    .setSound(GTSoundEntries.CHEMICAL);
    public static final GTRecipeType BLOOD_ALTAR_RECIPES =
            REGISTRATE.recipeType(GTCEu.id("blood_altar"), GTRecipeTypes.ELECTRIC)
                    .cnlang("工业血祭").setMaxIOSize(6, 6, 6, 6)
                    .setEUIO(IO.IN)
                    .setMaxTooltips(6)
                    .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE,  ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                    .setSound(GTSoundEntries.CHEMICAL);
    public static final GTRecipeType WISHING_RECIPES =
            REGISTRATE.recipeType(GTCEu.id("genshin_wishing"), GTRecipeTypes.MULTIBLOCK)
                    .cnlang("§c许§6☆§e愿§a☆§9池§d☆§c幸§6☆§e运§a☆§9抽§d☆§c奖").setMaxIOSize(1, 18, 0, 0)
                    .setEUIO(IO.IN)
                    .setMaxTooltips(5)
                    .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE,  ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                    .setSound(GTSoundEntries.CHEMICAL);
    public static void init() {}
}
