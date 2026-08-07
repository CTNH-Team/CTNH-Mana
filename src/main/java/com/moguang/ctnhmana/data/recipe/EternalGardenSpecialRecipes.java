package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.common.block.BotaniaFlowerBlocks;

import java.util.function.Consumer;

public class EternalGardenSpecialRecipes {

    public static final int RECIPE_INFO_LINES = 3;

    public static void init(Consumer<FinishedRecipe> provider) {
        var food = Items.BREAD.getDefaultInstance();
        food.setHoverName(eternal_food_lang.translate());
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder("eat")// 魔力
                .notConsumable(BotaniaFlowerBlocks.gourmaryllis.asItem())
                .inputItems(food)
                .EUt(320)
                .duration(20)
                .outputFluids(CMMaterials.Mana.getFluid(1))
                .addData("type", "eat")
                .save(provider);
        var flame = Items.COAL.getDefaultInstance();
        flame.setHoverName(eternal_coal_lang.translate());
        CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder("endo_flame")// 魔力
                .notConsumable(BotaniaFlowerBlocks.endoflame.asItem())
                .inputItems(flame)
                .EUt(320)
                .duration(20)
                .outputFluids(CMMaterials.Mana.getFluid(1))
                .addData("type", "fire")
                .save(provider);
    }

    /** JEI/EMI recipe tooltip line for recipes with special Eternal Garden runtime rules. */
    public static String recipeTypeInfo(CompoundTag data, int line) {
        if (line < 0 || line >= RECIPE_INFO_LINES) {
            return "";
        }
        Lang[] langs = switch (data.getString("type")) {
            case "water" -> waterRecipeLang;
            case "eat" -> eternalFoodRecipeLang;
            case "fire" -> eternalCoalRecipeLang;
            case "boom" -> boomRecipeLang;
            case "wither" -> witherRecipeLang;
            case "lighting" -> data.getBoolean("light") ? lightingStormRecipeLang : lightingRecipeLang;
            case "blame", "flame" -> blameRecipeLang;
            case "fly" -> flyRecipeLang;
            default -> null;
        };
        if (langs == null || line >= langs.length) {
            return "";
        }
        return langs[line].translate().getString();
    }

    @CN("任意食物")
    @EN("Any food")
    public static Lang eternal_food_lang;
    @CN("任意燃料")
    @EN("Any fuel")
    public static Lang eternal_coal_lang;

    @CN({
            "水绣球：最大并行8",
            "产出×并行×电压超频(每高一级×1.1)",
            "耗电×并行"
    })
    @EN({
            "Hydroangeas: max parallel 8",
            "output × parallel × voltage OC (×1.1/tier)",
            "EU × parallel"
    })
    public static Lang[] waterRecipeLang;

    @CN({
            "彼方兰：实际产出与食物饱食度相关",
            "最大并行=8+(机器等级-配方等级)×4"
    })
    @EN({
            "Gourmaryllis: output scales with food nutrition",
            "max parallel = 8+(machine−recipe tier)×4"
    })
    public static Lang[] eternalFoodRecipeLang;

    @CN({
            "炎修花：燃料热值叠加热度并放大产出",
            "可消耗极寒之凛冰降温；最大并行8"
    })
    @EN({
            "Endoflame: fuel burn time raises heat and boosts output",
            "may consume Cryotheum to cool; max parallel 8"
    })
    public static Lang[] eternalCoalRecipeLang;

    @CN({
            "热爆：最大并行=2^机器等级×32",
            "按可用输入缩放全部内容"
    })
    @EN({
            "Thermal boom: max parallel = 2^machine tier × 32",
            "scales all contents by available inputs"
    })
    public static Lang[] boomRecipeLang;

    @CN({
            "凋零菟葵：运行时对周围生物施加凋零伤害",
            "最大并行4；产出×并行×电压超频"
    })
    @EN({
            "Wither Aconite: withers nearby living entities while running",
            "max parallel 4; output × parallel × voltage OC"
    })
    public static Lang[] witherRecipeLang;

    @CN({
            "雷卡兰：雷暴时产出×300×电压超频",
            "晴天/无雨时无额外倍率"
    })
    @EN({
            "Reikarlily: ×300×voltage OC output while raining",
            "no weather bonus when dry"
    })
    public static Lang[] lightingRecipeLang;

    @CN({
            "雷卡兰(电路12)：雷暴时召唤闪电",
            "产出×100000×电压超频"
    })
    @EN({
            "Reikarlily (circuit 12): summons lightning in thunderstorms",
            "output ×100000×voltage OC"
    })
    public static Lang[] lightingStormRecipeLang;

    @CN({
            "炽玫瑰：运行时点燃周围实体",
            "最大并行=8+max(等级-3,0)×4",
            "产出×并行×电压超频"
    })
    @EN({
            "Thermalily: ignites nearby entities while running",
            "max parallel = 8+max(tier−3,0)×4",
            "output × parallel × voltage OC"
    })
    public static Lang[] blameRecipeLang;

    @CN({
            "勿落草：范围内漂浮生物越多产出越高",
            "(并造成魔法伤害)；EU×倍率"
    })
    @EN({
            "Shulk-Me-Not: more levitating mobs nearby → higher output",
            "(and magic damage); EU × multiplier"
    })
    public static Lang[] flyRecipeLang;
}
