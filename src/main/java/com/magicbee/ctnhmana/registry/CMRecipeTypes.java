package com.magicbee.ctnhmana.registry;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.api.recipe.customlogic.DigitalWellOfSufferLogic;
import com.magicbee.ctnhmana.api.recipe.customlogic.EternalGardenLogic;
import com.magicbee.ctnhmana.api.recipe.customlogic.IndustrialGemCuttingLogic;
import com.magicbee.ctnhmana.api.recipe.customlogic.IndustrialGemSublimatorGenericLogic;
import com.magicbee.ctnhmana.api.recipe.customlogic.IndustrialGemSublimatorLogic;
import com.magicbee.ctnhmana.api.recipe.customlogic.IndustrialSalvagingLogic;
import com.magicbee.ctnhmana.common.multiblock.QuasarEye;
import com.magicbee.ctnhmana.common.multiblock.RitualMechanicalMachine;
import com.magicbee.ctnhmana.data.recipe.EternalGardenSpecialRecipes;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.magicbee.ctnhmana.CTNHMana.REGISTRATE;

public class CMRecipeTypes {

    public static final GTRecipeType MANA_REACTOR_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("mana_reactor"), GTRecipeTypes.ELECTRIC)
            .cnlang("魔力反应").setMaxIOSize(9, 9, 6, 6)
            .setEUIO(IO.IN)
            .setMaxTooltips(5)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_MANA, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BT_BACKGROUND));
    public static final GTRecipeType MANA_TRANSFORMER_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("mana_transformer"), GTRecipeTypes.ELECTRIC)
            .cnlang("魔力操纵").setMaxIOSize(9, 9, 6, 6)
            .setEUIO(IO.IN)
            .setMaxTooltips(5)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_MANA, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BT_BACKGROUND));
    public static final GTRecipeType HELL_FORGE_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("hell_forge"), GTRecipeTypes.ELECTRIC)
            .cnlang("狱火铸造").setMaxIOSize(6, 6, 3, 3)
            .setEUIO(IO.IN)
            .setMaxTooltips(5)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_BLOOD, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BM_BACKGROUND));
    public static final GTRecipeType BLOOD_ALTAR_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("blood_altar"), GTRecipeTypes.ELECTRIC)
            .cnlang("工业血祭").setMaxIOSize(9, 9, 3, 3)
            .setEUIO(IO.IN)
            .setMaxTooltips(7)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_BLOOD, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BM_BACKGROUND));
    public static final GTRecipeType WISHING_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("genshin_wishing"), GTRecipeTypes.MULTIBLOCK)
            .cnlang("§c许§6☆§e愿§a☆§9池§d☆§c幸§6☆§e运§a☆§9抽§d☆§c奖").setMaxIOSize(1, 18, 0, 0)
            .setEUIO(IO.IN)
            .setMaxTooltips(5)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType DEMON_WILL_GENERATOR_RECIPE = REGISTRATE
            .recipeType(CTNHMana.id("demon_will_generator"), GTRecipeTypes.GENERATOR)
            .cnlang("恶魔意志发电").setEUIO(IO.OUT).setMaxIOSize(1, 2, 1, 2)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CRYSTALLIZATION, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType ETERNAL_GARDEN = REGISTRATE.recipeType(CTNHMana.id("eternal_garden"), ELECTRIC)
            .cnlang("永恒花园").setMaxIOSize(6, 6, 3, 3)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .addDataInfo(data -> EternalGardenSpecialRecipes.recipeTypeInfo(data, 0))
            .addDataInfo(data -> EternalGardenSpecialRecipes.recipeTypeInfo(data, 1))
            .addDataInfo(data -> EternalGardenSpecialRecipes.recipeTypeInfo(data, 2))
            .setSound(GTSoundEntries.CHEMICAL)
            .addCustomRecipeLogic(new EternalGardenLogic());

    public static final GTRecipeType MANA_CONDENSER_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("mana_condenser"), MULTIBLOCK)
            .cnlang("魔力凝集").setEUIO(IO.IN).setMaxIOSize(1, 0, 2, 2)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_COMPRESS, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType BEAMS = REGISTRATE.recipeType(CTNHMana.id("beams"), GTRecipeTypes.ELECTRIC)
            .cnlang("戴森光束").setEUIO(IO.IN).setMaxIOSize(9, 2, 1, 2)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CRYSTALLIZATION, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .addDataInfo(data -> LocalizationUtils.format("ctnh.multiblock.nicoll_dyson_beams.info.mana_required",
                    String.format("%.2f", data.getFloat("required_mana") / 1000000)))
            .addDataInfo(data -> LocalizationUtils.format("ctnh.multiblock.nicoll_dyson_beams.info.mana_consumption",
                    String.format("%.2f", data.getFloat("mana") / 1000000)));

    public static final GTRecipeType QUASAR_CREATE = REGISTRATE.recipeType(CTNHMana.id("quasar_create"), GENERATOR)
            .cnlang("类星体创生").setMaxIOSize(1, 3, 1, 21)
            .setEUIO(IO.OUT)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType QUASAR_EYE = REGISTRATE
            .recipeType(CTNHMana.id("quasar_eye"), GTRecipeTypes.ELECTRIC)
            .cnlang("§5类星体§r§1之§c眼")
            .setEUIO(IO.OUT)
            .setMaxIOSize(1, 0, 2, 1)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE)
            .addDataInfo(data -> QuasarEye.RecipeLang.RECIPE_INFO_0
                    .translate(String.format("%.1f", data.getFloat("consumption"))).getString())
            .addDataInfo(data -> QuasarEye.RecipeLang.RECIPE_INFO_1.translate(data.getInt("tier")).getString())
            .addDataInfo(data -> QuasarEye.RecipeLang.RECIPE_INFO_2.translate(data.getInt("active")).getString());

    public static final GTRecipeType TWISTED_FUSION = REGISTRATE.recipeType(CTNHMana.id("twisted_fusion"), ELECTRIC)
            .cnlang("扭曲聚变").setMaxIOSize(0, 0, 3, 3)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    public static final GTRecipeType DIGITAL_WELL_OF_SUFFER = REGISTRATE
            .recipeType(CTNHMana.id("digital_well_of_suffer"), ELECTRIC)
            .cnlang("数字化苦难之井")
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 1)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_BLOOD, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH)
            .addDataInfo(data -> DigitalWellOfSufferLogic.by_model_tier.translate().getString())
            .addCustomRecipeLogic(new DigitalWellOfSufferLogic());

    public static final GTRecipeType GAIA_REACTOR_RECIPES = REGISTRATE.recipeType(CTNHMana.id("gaia_reactor"), ELECTRIC)
            .cnlang("盖亚反应").setMaxIOSize(2, 24, 2, 2)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_MANA, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CUT)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BT_BACKGROUND));
    public static final GTRecipeType INDUSTRIAL_PETAL_APOTHECARY_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("industrial_petal_apothecary"), ELECTRIC)
            .cnlang("工业花药").setMaxIOSize(16, 1, 0, 0)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_MANA, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CUT)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BT_BACKGROUND));
    public static final GTRecipeType INDUSTRIAL_SALVAGING_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("industrial_salvaging"), ELECTRIC)
            .cnlang("工业拆解").setMaxIOSize(1, 6, 0, 0)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_MANA, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BT_BACKGROUND))
            .addDataInfo(data -> data.contains("info") ?
                    IndustrialSalvagingLogic.by_rarity.translate().getString() : "")
            .addCustomRecipeLogic(new IndustrialSalvagingLogic());
    public static final GTRecipeType GEM_INLAY_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("gem_inlay"), ELECTRIC)
            .cnlang("宝石镶嵌").setMaxIOSize(4, 1, 0, 0)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_MANA, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CUT)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BT_BACKGROUND))
            .addCustomRecipeLogic(new IndustrialGemCuttingLogic());
    /**
     * 宝石刻格配方类型：仅用于 EMI 展示分类（具体宝石示例）。
     * 运行时匹配在 {@link IndustrialGemSublimatorLogic#createCustomRecipe} 中恒为 null，
     * 代表性配方由 {@link IndustrialGemSublimatorLogic#buildRepresentativeRecipes} 注入。
     */
    public static final GTRecipeType GEM_SUBLIMATOR_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("gem_sublimator"), ELECTRIC)
            .cnlang("宝石刻格")
            .lang("Gem Engraving")
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_MANA, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BT_BACKGROUND))
            .addCustomRecipeLogic(new IndustrialGemSublimatorLogic());
    /**
     * 宝石刻格「展示」EMI 分类：{@code #forge:apotheosis_gems + 粉 → XX品质的宝石}。
     * 输入为 Tag，与具体宝石 NBT 解耦，任意神话宝石查表都能看到。
     * 机器侧与 {@link #GEM_SUBLIMATOR_RECIPES} 一并注册，仅作 EMI 展示，不参与运行时匹配。
     */
    public static final GTRecipeType GEM_SUBLIMATOR_GENERIC_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("gem_sublimator_generic"), DUMMY)
            .cnlang("宝石刻格（展示）")
            .lang("Gem Engraving (Display)")
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_MANA, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BT_BACKGROUND))
            .addCustomRecipeLogic(new IndustrialGemSublimatorGenericLogic());
    public static final GTRecipeType MANA_FORGE_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("mana_forge"), ELECTRIC)
            .cnlang("注魔锻造").setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_MANA, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FORGE_HAMMER)
            .setUiBuilder((recipe, widgetGroup) -> widgetGroup.setBackground(CMGuiTextures.BT_BACKGROUND));
    public static final GTRecipeType MANA_FUEL_INFUSER_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("mana_fuel_infuser"), ELECTRIC)
            .cnlang("注术单元铸造")
            .setMaxIOSize(6, 1, 3, 0)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType METEOR_CAPTURER_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("meteor_capturer"), ELECTRIC)
            .cnlang("集成式坠星位标").setMaxIOSize(1, 24, 1, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);
    public static final GTRecipeType ZENITH_CIRCUIT = REGISTRATE
            .recipeType(CTNHMana.id("zenith_assembler"), ELECTRIC)
            .cnlang("天域组合").setMaxIOSize(6, 1, 3, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER);
    public static final GTRecipeType ANTIPHASE_ETCHING = REGISTRATE
            .recipeType(CTNHMana.id("antiphase_etching"), ELECTRIC)
            .cnlang("反相蚀刻").setMaxIOSize(1, 1, 1, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);
    public static final GTRecipeType TwistCollapse = REGISTRATE
            .recipeType(CTNHMana.id("twist_collapse"), DUMMY)
            .cnlang("扭曲崩解").setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);

    /**
     * 坠星位标参考配方（{@code ctnhmana:meteor_ritual_guide}），仅 EMI/JEI 展示，机器不处理。
     */
    @CN("需要消耗的LP：%d")
    @EN("LP cost: %d")
    public static Lang lp_cost;
    public static final GTRecipeType METEOR_RITUAL_GUIDE = REGISTRATE
            .recipeType(CTNHMana.id("meteor_ritual_guide"), DUMMY)
            .cnlang("坠星位标").setMaxIOSize(1, 16, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_BLOOD, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .setMaxTooltips(4)
            .addDataInfo(data -> lp_cost.translate(data.getInt("meteor_lp")).getString());

    /**
     * 工业血祭仪式阵配方类型（{@code ctnhmana:blood_ritual}）。
     * <p>
     * 基于 DUMMY，仅需极低 EU；配方 {@code duration} 为冷却，{@code ritual_id} 数据字段指定
     * 配方完成后执行的血魔法仪式。详见 {@link RitualMechanicalMachine}。
     */
    public static final GTRecipeType RITUAL_RECIPES = REGISTRATE
            .recipeType(CTNHMana.id("blood_ritual"), ELECTRIC)
            .cnlang("血祭仪式").setMaxIOSize(1, 1, 2, 0)
            .setEUIO(IO.IN)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_BLOOD, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .setMaxTooltips(5)
            .addDataInfo(RitualMechanicalMachine::formatRitualRecipeTip)
            .addDataInfo(RitualMechanicalMachine::formatRitualLpTip);
    public static final GTRecipeType DOOR_OF_SHROUD = REGISTRATE
            .recipeType(CTNHMana.id("shroud_door"), ELECTRIC)
            .cnlang("打开虚境之门扉").setMaxIOSize(6, 0, 6, 0)
            .setEUIO(IO.IN)
            .setProgressBar(CMGuiTextures.PROGRESS_BAR_BLOOD, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .setMaxTooltips(4);

    public static void init() {}
}
