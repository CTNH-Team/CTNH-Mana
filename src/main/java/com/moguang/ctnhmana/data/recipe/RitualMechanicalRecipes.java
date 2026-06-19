package com.moguang.ctnhmana.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.moguang.ctnhmana.Mutiblock.RitualMechanicalMachine;
import com.moguang.ctnhmana.registry.CMMaterials;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.moguang.ctnhmana.registry.CMRecipeTypes.RITUAL_RECIPES;

/**
 * 工业血祭仪式阵（{@link com.moguang.ctnhmana.registry.CMMultiblockMachines#RITUAL_MECHANICAL_ARRAY}）
 * 的 datagen 配方。
 * <p>
 * <b>配方约定：</b>
 * <ul>
 *   <li>类型：{@link com.moguang.ctnhmana.registry.CMRecipeTypes#RITUAL_RECIPES}（{@code blood_ritual}）</li>
 *   <li>触媒：红石粉（象征“红石配方”，无复杂物品条件）</li>
 *   <li>{@code duration}：机器冷却 / 进度条时长（tick）；例如 100 tick = 5 秒</li>
 *   <li>{@link RitualMechanicalMachine#RECIPE_DATA_RITUAL_ID}：血魔法仪式 ID，与
 *       {@link wayoftime.bloodmagic.ritual.RitualRegister} 一致</li>
 *   <li>每完成一条配方 → 控制器调用一次 {@link wayoftime.bloodmagic.ritual.Ritual#performRitual}</li>
 * </ul>
 * <p>
 * 扩展新仪式时复制 builder，修改 {@code ritual_id} 与 {@code duration} 即可。
 */
public class RitualMechanicalRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // 收割仪式：5 秒冷却，单次 performRitual（收割范围内成熟作物）
        RITUAL_RECIPES.recipeBuilder("harvest")
                .inputItems(Items.REDSTONE, 1)
                .duration(100)
                .EUt(1)
                .addData(RitualMechanicalMachine.RECIPE_DATA_RITUAL_ID, "harvest")
                .save(provider);

        // 水源仪式：5 秒冷却，在 5×5 内放置水源
        RITUAL_RECIPES.recipeBuilder("water")
                .inputItems(Items.REDSTONE, 1)
                .duration(100)
                .EUt(1)
                .addData(RitualMechanicalMachine.RECIPE_DATA_RITUAL_ID, "water")
                .save(provider);

        // 速度仪式：3 秒冷却，消耗 2 红石；对范围内生物施加加速效果
        RITUAL_RECIPES.recipeBuilder("speed")
                .inputItems(Items.REDSTONE, 2)
                .duration(60)
                .EUt(1)
                .addData(RitualMechanicalMachine.RECIPE_DATA_RITUAL_ID, "speed")
                .save(provider);

        // 生灵萃取仪式：5 秒冷却，消耗 1 魔力钢粉
        RITUAL_RECIPES.recipeBuilder("extractor")
                .inputItems(ChemicalHelper.get(dust, CMMaterials.ManaSteel), 1)
                .duration(100)
                .EUt(1)
                .addData(RitualMechanicalMachine.RECIPE_DATA_RITUAL_ID, "extractor")
                .save(provider);
    }
}
