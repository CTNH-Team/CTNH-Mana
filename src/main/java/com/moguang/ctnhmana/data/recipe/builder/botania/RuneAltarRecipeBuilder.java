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

public class RuneAltarRecipeBuilder {

    // 恢复多输入：使用List存储多个Ingredient
    private final List<Ingredient> inputs = new ArrayList<>();
    private ItemStack output;
    private int mana;
    private ResourceLocation id;
    private int meta = -1;

    private RuneAltarRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    public static RuneAltarRecipeBuilder builder(String name) {
        return new RuneAltarRecipeBuilder(name);
    }

    public RuneAltarRecipeBuilder input(ItemStack itemStack) {
        inputs.add(Ingredient.of(itemStack));
        return this;
    }

    public RuneAltarRecipeBuilder input(TagKey<Item> tagKey) {
        inputs.add(Ingredient.of(tagKey));
        return this;
    }

    public RuneAltarRecipeBuilder input(Ingredient ingredient) {
        inputs.add(ingredient);
        return this;
    }

    public RuneAltarRecipeBuilder input(Item... items) {
        inputs.add(Ingredient.of(items));
        return this;
    }

    public RuneAltarRecipeBuilder input(Ingredient... ingredients) {
        Arrays.stream(ingredients).forEach(ingredient -> inputs.add(ingredient));
        return this;
    }

    public RuneAltarRecipeBuilder output(ItemStack itemStack) {
        this.output = itemStack;
        return this;
    }

    public RuneAltarRecipeBuilder mana(int mana) {
        this.mana = mana;
        return this;
    }

    public RuneAltarRecipeBuilder circuitMeta(int meta) {
        this.meta = meta;
        return this;
    }

    public void toJson(JsonObject json) {
        // 序列化输出物品
        json.add("output", ItemNBTHelper.serializeStack(this.output));
        // 序列化魔力值
        json.addProperty("mana", this.mana);

        // 序列化多输入：遍历inputs生成JSON数组
        JsonArray ingredients = new JsonArray();
        for (Ingredient ingr : inputs) {
            ingredients.add(ingr.toJson());
        }
        json.add("ingredients", ingredients);
    }

    private GTRecipeBuilder mapToGTBuilder() {
        if (this.output == null || this.inputs.isEmpty()) {
            throw new IllegalStateException("参数缺失是凉爽的夏夜");
        }

        ResourceLocation bmId = RuneAltarRecipeBuilder.this.id;
        ResourceLocation gtId = GTCEu.id("manareactor_recipes_rune_altar_" + bmId.getPath());

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
            if (ingredient.getItems()[0].getCount() > 1)
                gtBuilder.inputItems(ingredient.getItems()[0]);
            else gtBuilder.inputItems(ingredient);

        }
        gtBuilder.outputItems(this.output);

        long gtEUt = (long) (128);
        gtBuilder.EUt(gtEUt);
        int gtDuration = (int) Math.max(100, 100 + Math.pow(this.mana / 1000, 2));
        gtBuilder.duration(gtDuration);
        if (meta >= 0)
            gtBuilder.circuitMeta(meta);
        return gtBuilder;
    }

    public FinishedRecipe build() {
        return new FinishedRuneAltarRecipe();
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(build());
        GTRecipeBuilder gtBuilder = this.mapToGTBuilder();
        gtBuilder.save(consumer);
    }

    private class FinishedRuneAltarRecipe implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            // 复用外部类的toJson方法，统一序列化逻辑
            RuneAltarRecipeBuilder.this.toJson(json);
        }

        @Override
        public ResourceLocation getId() {
            // 配方ID路径格式：命名空间 + "rune_altar" + 配方名
            return ResourceLocation.tryBuild(
                    id.getNamespace(),
                    "rune_altar" + "/" + id.getPath());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BotaniaRecipeTypes.RUNE_SERIALIZER;
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
