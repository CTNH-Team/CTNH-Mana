package com.moguang.ctnhmana.common.recipe.builder.botania;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TerraPlateRecipeBuilder {
    // 多输入：使用List存储多个Ingredient
    private final List<Ingredient> inputs = new ArrayList<>();
    private ItemStack output;
    private int mana;
    private ResourceLocation id;
    private int meta=-1;
    private boolean isManaReactorAllowed=true;
    private TerraPlateRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    public static TerraPlateRecipeBuilder builder(String name) {
        return new TerraPlateRecipeBuilder(name);
    }

    public TerraPlateRecipeBuilder input(ItemStack itemStack) {
        inputs.add(Ingredient.of(itemStack));
        return this;
    }
    public TerraPlateRecipeBuilder input(Item item) {
        inputs.add(Ingredient.of(new ItemStack(item,1)));
        return this;
    }

    public TerraPlateRecipeBuilder input(TagKey<Item> tagKey) {
        inputs.add(Ingredient.of(tagKey));
        return this;
    }

    public TerraPlateRecipeBuilder input(Ingredient ingredient) {
        inputs.add(ingredient);
        return this;
    }
    public TerraPlateRecipeBuilder input(Item... items) {
        Arrays.stream(items).forEach(item -> inputs.add(Ingredient.of(new ItemStack(item,1))));
        return this;
    }


    public TerraPlateRecipeBuilder input(Ingredient... ingredients) {
        Arrays.stream(ingredients).forEach(ingredient -> inputs.add(ingredient));
        return this;
    }

    public TerraPlateRecipeBuilder output(ItemStack itemStack) {
        this.output = itemStack;
        return this;
    }

    public TerraPlateRecipeBuilder mana(int mana) {
        this.mana = mana;
        return this;
    }
    public TerraPlateRecipeBuilder circuitMeta(int meta)
    {
        this.meta=meta;
        return this;
    }
    public TerraPlateRecipeBuilder allowReactor(boolean isallowed)
    {
        this.isManaReactorAllowed=isallowed;
        return this;
    }
    private GTRecipeBuilder mapToGTBuilder() {
        if (this.output == null || this.inputs.isEmpty()) {
            throw new IllegalStateException("参数缺失是凉爽的夏夜");
        }

        ResourceLocation bmId = TerraPlateRecipeBuilder.this.id;
        ResourceLocation gtId = GTCEu.id( "manareactor_recipes_terra_plate_" + bmId.getPath());

        GTRecipeBuilder gtBuilder = GTRecipeBuilder.of(gtId, CMRecipeTypes.MANA_REACTOR_RECIPES);
        List<Ingredient> pre_inputs = new ArrayList<>();
        for (Ingredient currentIng : this.inputs) {
            // 前置校验1：跳过空配料，避免后续空指针/数组越界
            if (currentIng == null || currentIng == Ingredient.EMPTY) {
                continue;
            }
            ItemStack[] currentStacks = currentIng.getItems();
            if (currentStacks == null || currentStacks.length == 0 || currentStacks[0].isEmpty()) {
                continue;
            }
            ItemStack currentFirstStack = currentStacks[0]; // 取第一个代表栈作为匹配依据
            boolean isMatched = false; // 标记是否匹配到已有Ingredient

            for (Ingredient existIng : pre_inputs) {
                ItemStack[] existStacks = existIng.getItems();
                if (existStacks == null || existStacks.length == 0 || existStacks[0].isEmpty()) {
                    continue;
                }
                if (existIng.test(currentFirstStack)) {
                    ItemStack existFirstStack = existStacks[0];
                    int maxStack = existFirstStack.getMaxStackSize();
                    if (existFirstStack.getCount() < maxStack) {
                        existFirstStack.setCount(existFirstStack.getCount() + 1);
                    }
                    isMatched = true;
                    break; // 找到匹配，立即跳出内层循环，避免重复计数
                }
            }

            if (!isMatched) {
                pre_inputs.add(currentIng);
            }
        }

        for (Ingredient ingredient : pre_inputs) {
            gtBuilder.inputItems(ingredient);
        }
        gtBuilder.outputItems(this.output);

        long gtEUt=(long)(512);
        gtBuilder.EUt(gtEUt);
        int gtDuration = Math.max(20,this.mana/2000);
        gtBuilder.duration(gtDuration);
        if(meta>=0)
            gtBuilder.circuitMeta(meta);
        return gtBuilder;
    }

    public void toJson(JsonObject json) {
        json.addProperty("mana", this.mana);
        JsonArray ingredients = new JsonArray();
        for (Ingredient ingr : inputs) {
            ingredients.add(ingr.toJson());
        }
        json.add("ingredients", ingredients);
        json.add("result", ItemNBTHelper.serializeStack(this.output));
    }

    public FinishedRecipe build() {
        return new FinishedTerraPlateRecipe();

    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(build());
        if(isManaReactorAllowed) {
            GTRecipeBuilder gtBuilder = this.mapToGTBuilder();
            gtBuilder.save(consumer);
        }
    }

    private class FinishedTerraPlateRecipe implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            TerraPlateRecipeBuilder.this.toJson(json);
        }

        @Override
        public ResourceLocation getId() {
            return ResourceLocation.tryBuild(id.getNamespace(), "terra_plate/"  + id.getPath());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BotaniaRecipeTypes.TERRA_PLATE_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null; // 保持原有逻辑，不序列化进阶
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null; // 保持原有逻辑，无进阶ID
        }
    }
}
