package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.common.block.BotaniaFlowerBlocks;

import java.util.function.Consumer;

public class EternalGardenSpecialRecipes {

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

    @CN("任意食物")
    @EN("Any food")
    public static Lang eternal_food_lang;
    @CN("任意燃料")
    @EN("Any fuel")
    public static Lang eternal_coal_lang;
    @CN("实际产出与食物饱食度相关")
    @EN("Actual output scales with food saturation")
    public static Lang eternalFoodRecipeLang;
    @CN("实际产出与机器热值相关")
    @EN("Actual output scales with machine heating value")
    public static Lang eternalCoalRecipeLang;
}
