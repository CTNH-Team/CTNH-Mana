package com.moguang.ctnhmana.data.recipe.builder.botania;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moguang.ctnhmana.CTNHMana;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ElvenTradeRecipeBuilder {

    private final ResourceLocation id;
    private final List<Ingredient> inputs = new ArrayList<>();
    private final List<ItemStack> outputs = new ArrayList<>();

    private ElvenTradeRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    public static ElvenTradeRecipeBuilder builder(String name) {
        return new ElvenTradeRecipeBuilder(name);
    }

    /* ------------ 输入 ------------ */

    public ElvenTradeRecipeBuilder input(Ingredient ingredient) {
        this.inputs.add(ingredient);
        return this;
    }

    public ElvenTradeRecipeBuilder input(Ingredient... ingredients) {
        this.inputs.addAll(Arrays.asList(ingredients));
        return this;
    }

    public ElvenTradeRecipeBuilder input(ItemStack stack) {
        this.inputs.add(Ingredient.of(stack));
        return this;
    }

    public ElvenTradeRecipeBuilder input(ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            this.inputs.add(Ingredient.of(stack));
        }
        return this;
    }

    public ElvenTradeRecipeBuilder input(Item item) {
        this.inputs.add(Ingredient.of(item));
        return this;
    }

    public ElvenTradeRecipeBuilder input(Item... items) {
        this.inputs.add(Ingredient.of(items));
        return this;
    }

    /* ------------ 输出 ------------ */

    public ElvenTradeRecipeBuilder output(ItemStack stack) {
        this.outputs.add(stack);
        return this;
    }

    public ElvenTradeRecipeBuilder output(ItemStack... stacks) {
        Collections.addAll(this.outputs, stacks);
        return this;
    }

    public ElvenTradeRecipeBuilder output(Item item) {
        this.outputs.add(new ItemStack(item));
        return this;
    }

    public ElvenTradeRecipeBuilder output(Item... items) {
        for (Item item : items) {
            this.outputs.add(new ItemStack(item));
        }
        return this;
    }

    /* ------------ 构建 ------------ */

    private FinishedRecipe buildInternal() {
        if (inputs.isEmpty() || outputs.isEmpty()) {
            throw new IllegalStateException("ElvenTradeRecipeBuilder 缺少输入或输出（inputs / outputs 为空）");
        }

        return new FinishedRecipe() {

            @Override
            public void serializeRecipeData(JsonObject json) {
                JsonArray in = new JsonArray();
                for (Ingredient ingredient : inputs) {
                    in.add(ingredient.toJson());
                }

                JsonArray out = new JsonArray();
                for (ItemStack stack : outputs) {
                    out.add(ItemNBTHelper.serializeStack(stack));
                }

                json.add("ingredients", in);
                json.add("output", out);
            }

            @Override
            public ResourceLocation getId() {
                return ResourceLocation.tryBuild(id.getNamespace(), "elven_trade/" + id.getPath());
            }

            @Override
            public RecipeSerializer<?> getType() {
                return BotaniaRecipeTypes.ELVEN_TRADE_SERIALIZER;
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
        };
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(buildInternal());
    }
}
