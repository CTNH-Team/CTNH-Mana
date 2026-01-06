package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
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
                    .setMaxTooltips(7)
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

    public static final GTRecipeType DEMON_WILL_GENERATOR_RECIPE =
            REGISTRATE.recipeType(GTCEu.id("demon_will_generator"), GTRecipeTypes.GENERATOR)
            .cnlang("恶魔意志发电").setEUIO(IO.OUT).setMaxIOSize(1, 2, 1, 2)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CRYSTALLIZATION, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType ETERNAL_GARDEN = REGISTRATE.recipeType(GTCEu.id("eternal_garden"),ELECTRIC)
            .cnlang("永恒花园").setMaxIOSize(6, 6, 3, 3)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE,  ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType MANA_CONDENSER_RECIPES = REGISTRATE.recipeType(GTCEu.id("mana_condenser"), MULTIBLOCK)
            .cnlang("魔力凝集").setEUIO(IO.IN).setMaxIOSize(1, 0, 2, 2)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_COMPRESS, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType BEAMS = REGISTRATE.recipeType(GTCEu.id("beams"), GTRecipeTypes.ELECTRIC)
            .cnlang("戴森光束").setEUIO(IO.IN).setMaxIOSize(9, 2, 1, 2)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CRYSTALLIZATION, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .addDataInfo(data -> LocalizationUtils.format("ctnh.multiblock.nicoll_dyson_beams.info.mana_required",String.format("%.2f",data.getFloat("required_mana")/1000000)))
            .addDataInfo(data -> LocalizationUtils.format("ctnh.multiblock.nicoll_dyson_beams.info.mana_consumption",String.format("%.2f",data.getFloat("mana")/1000000)));

    public static final GTRecipeType QUASAR_CREATE=REGISTRATE.recipeType(GTCEu.id("quasar_create"),GENERATOR)
            .cnlang("类星体创生").setMaxIOSize(1, 3, 1, 21)
            .setEUIO(IO.OUT)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE,  ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType QUASAR_EYE = REGISTRATE.recipeType(GTCEu.id("quasar_eye"), GTRecipeTypes.ELECTRIC)
            .cnlang("§5类星体§r§1之§c眼")
            .setEUIO(IO.OUT)
            .setMaxIOSize(1, 0, 2, 1)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE)
            .addDataInfo(data -> LocalizationUtils.format("ctnh.recipe.quasar_eye.info.0",String.format("%.1f",data.getFloat("consumption"))))
            .addDataInfo(data -> LocalizationUtils.format("ctnh.recipe.quasar_eye.info.1",String.format("%d",data.getInt("tier"))))
            .addDataInfo(data -> LocalizationUtils.format("ctnh.recipe.quasar_eye.info.2",String.format("%d",data.getInt("active"))));

    public static final GTRecipeType TWISTED_FUSION = REGISTRATE.recipeType(GTCEu.id("twisted_fusion"),ELECTRIC)
            .cnlang("扭曲聚变反应堆").setMaxIOSize(0, 0, 2, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    public static final GTRecipeType DIGITAL_WELL_OF_SUFFER = REGISTRATE.recipeType(GTCEu.id("digital_well_of_suffer"), ELECTRIC)
            .cnlang("数字化苦难之井")
            .setEUIO(IO.IN)
            .setMaxIOSize(1,0,0,1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    public static final GTRecipeType GAIA_REACTOR_RECIPES = REGISTRATE.recipeType(GTCEu.id("gaia_reactor"), MULTIBLOCK)
            .cnlang("盖亚反应").setMaxIOSize(2, 24, 2, 2)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CUT);

    public static final GTRecipeType METEOR_CAPTURER_RECIPES = REGISTRATE.recipeType(GTCEu.id("meteor_capturer"), MULTIBLOCK)
            .cnlang("集成式坠星位标").setMaxIOSize(1, 24, 1, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);
    public static void init() {}
}
