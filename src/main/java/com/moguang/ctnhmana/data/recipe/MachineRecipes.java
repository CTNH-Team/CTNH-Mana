package com.moguang.ctnhmana.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.frameGt;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.gear;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.moguang.ctnhmana.registry.CMBlocks.CASING_MANASTEEL_GEARBOX;
import static com.moguang.ctnhmana.registry.CMMaterials.ManaSteel;

public class MachineRecipes {
    public static void init(Consumer<FinishedRecipe> provider){
        ASSEMBLER_RECIPES.recipeBuilder("manasteel_gearbox_casing")//魔力钢齿轮箱机壳
                .inputItems(plate, ManaSteel, 4)
                .inputItems(gear, ManaSteel, 2)
                .inputItems(frameGt, ManaSteel)
                .circuitMeta(4)
                .outputItems(CASING_MANASTEEL_GEARBOX.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16).save(provider);
        //产物：魔力钢齿轮箱机壳
        VanillaRecipeHelper.addShapedRecipe(provider, true,
                "casing_manasteel_gearbox",
                CASING_MANASTEEL_GEARBOX.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft),
                "PhP",
                "GFG",
                "PwP",
                'P', new MaterialEntry(TagPrefix.plate, ManaSteel),
                'F', new MaterialEntry(frameGt, ManaSteel),
                'G', new MaterialEntry(gear, ManaSteel));
    }
}
