package com.moguang.ctnhmana.data.recipe.builder.botania;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.ULV;
import static com.gregtechceu.gtceu.api.GTValues.VA;

public class PetalRecipeBuilder {

    private final List<Ingredient> inputs = new ArrayList<>();
    private ItemStack output;
    private Ingredient reagent;
    private ResourceLocation id;

    public PetalRecipeBuilder(String name) {
        id = CTNHMana.id(name);
    }

    public PetalRecipeBuilder input(ItemStack itemStack) {
        inputs.add(Ingredient.of(itemStack));
        return this;
    }

    public PetalRecipeBuilder input(TagKey<Item> tagKey) {
        inputs.add(Ingredient.of(tagKey));
        return this;
    }

    public PetalRecipeBuilder input(Ingredient ingredient) {
        inputs.add(ingredient);
        return this;
    }

    public PetalRecipeBuilder input(Ingredient... ingredients) {
        Arrays.stream(ingredients).forEach(ingredient -> inputs.add(ingredient));
        return this;
    }

    public PetalRecipeBuilder output(ItemStack itemStack) {
        output = itemStack;
        return this;
    }

    public PetalRecipeBuilder reagent(ItemStack itemStack) {
        reagent = Ingredient.of(itemStack);
        return this;
    }

    public PetalRecipeBuilder reagent(TagKey<Item> tagKey) {
        reagent = Ingredient.of(tagKey);
        return this;
    }

    public static PetalRecipeBuilder builder(String name) {
        return new PetalRecipeBuilder(name);
    }

    private GTRecipeBuilder mapToGTBuilder() {
        if (this.output == null || this.inputs.isEmpty() || this.reagent == null) {
            throw new IllegalStateException("参数缺失是凉爽的夏夜");
        }

        ResourceLocation bmId = PetalRecipeBuilder.this.id;
        ResourceLocation gtId = GTCEu.id("industrial_petal_apothecary_" + bmId.getPath());

        GTRecipeBuilder gtBuilder = GTRecipeBuilder.of(gtId, CMRecipeTypes.INDUSTRIAL_PETAL_APOTHECARY_RECIPES);

        List<Ingredient> pre_inputs = new ArrayList<>();
        List<Ingredient> gtInputs = new ArrayList<>(this.inputs);
        gtInputs.add(this.reagent);
        for (Ingredient currentIng : gtInputs) {
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
            if (ingredient.getItems()[0].getCount() > 1)
                gtBuilder.inputItems(ingredient.getItems()[0]);
            else gtBuilder.inputItems(ingredient);
        }
        gtBuilder.outputItems(this.output);
        gtBuilder.EUt(VA[ULV]);
        gtBuilder.duration(200);
        return gtBuilder;
    }

    public void toJson(JsonObject json) {
        json.add("output", ItemNBTHelper.serializeStack(this.output));
        JsonArray ingredients = new JsonArray();
        List<Ingredient> inputs = this.inputs;
        int len = inputs.size();

        for (int var5 = 0; var5 < len; ++var5) {
            Ingredient ingr = inputs.get(var5);
            ingredients.add(ingr.toJson());
        }

        json.add("reagent", this.reagent.toJson());
        json.add("ingredients", ingredients);
    }

    public FinishedRecipe build() {
        return new FinishedPetalRecipe();
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(build());
        GTRecipeBuilder gtBuilder = this.mapToGTBuilder();
        gtBuilder.save(consumer);
    }

    private class FinishedPetalRecipe implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            PetalRecipeBuilder.this.toJson(json);
        }

        @Override
        public ResourceLocation getId() {
            return ResourceLocation.tryBuild(id.getNamespace(), "petal_apothecary" + "/" + id.getPath());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BotaniaRecipeTypes.PETAL_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
