package com.moguang.ctnhmana.data.recipe.builder.botania;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moguang.ctnhmana.CTNHMana;
import mythicbotany.infuser.InfuserRecipe;
import org.jetbrains.annotations.Nullable;
import org.moddingx.libx.crafting.RecipeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ElfPlateRecipeBuilder {

    // 核心配方属性，初始化默认值（颜色默认白色，魔力消耗默认未设置）
    private final List<Ingredient> inputs = new ArrayList<>();
    private final ResourceLocation id;
    private ItemStack output;
    private String group = "";
    private int manaCost = -1;
    private int fromColor = 16777215; // 白色默认RGB值
    private int toColor = 16777215;   // 白色默认RGB值

    private ElfPlateRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    public static ElfPlateRecipeBuilder builder(String name) {
        return new ElfPlateRecipeBuilder(name);
    }

    public ElfPlateRecipeBuilder input(ItemLike item) {
        this.inputs.add(Ingredient.of(item));
        return this;
    }

    public ElfPlateRecipeBuilder input(ItemLike item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.inputs.add(Ingredient.of(item));
        }
        return this;
    }

    // Tag标签原料（支持物品标签批量匹配）
    public ElfPlateRecipeBuilder input(TagKey<Item> tag) {
        this.inputs.add(Ingredient.of(tag));
        return this;
    }

    // 直接传入Ingredient原料（自定义原料匹配规则）
    public ElfPlateRecipeBuilder input(Ingredient ingredient) {
        this.inputs.add(ingredient);
        return this;
    }

    // 带数量的Ingredient原料（循环添加对应次数）
    public ElfPlateRecipeBuilder input(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.inputs.add(ingredient);
        }
        return this;
    }

    // ===================== 配方属性设置（链式调用，简化命名，贴合你的风格）=====================
    // 设置输出物品（ItemStack，支持自定义数量/NBT）
    public ElfPlateRecipeBuilder output(ItemStack output) {
        this.output = output;
        return this;
    }

    // 重载：直接传入ItemLike，自动生成数量为1的ItemStack（简化调用）
    public ElfPlateRecipeBuilder output(ItemLike output) {
        this.output = new ItemStack(output);
        return this;
    }

    // 设置配方分组（用于配方书分类，非空才会序列化到JSON）
    public ElfPlateRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    // 设置魔力消耗（必须设置，否则build时抛出异常）
    public ElfPlateRecipeBuilder mana(int mana) {
        this.manaCost = mana;
        return this;
    }

    // 设置颜色渐变（精灵台特效颜色，默认白色）
    public ElfPlateRecipeBuilder colors(int fromColor, int toColor) {
        this.fromColor = fromColor;
        this.toColor = toColor;
        return this;
    }

    public void toJson(JsonObject json) {
        // 序列化配方分组（非空才添加）
        if (!this.group.isEmpty()) {
            json.addProperty("group", this.group);
        }
        // 序列化输出物品（支持NBT，使用LibX的RecipeHelper保持兼容性）
        json.add("output", RecipeHelper.serializeItemStack(this.output, true));
        // 序列化原料列表
        JsonArray ingredients = new JsonArray();
        for (Ingredient ingredient : this.inputs) {
            ingredients.add(ingredient.toJson());
        }
        json.add("ingredients", ingredients);
        json.addProperty("mana", this.manaCost);
        json.addProperty("fromColor", this.fromColor);
        json.addProperty("toColor", this.toColor);
    }

    // ===================== 构建FinishedRecipe（匿名内部类，和PetalRecipeBuilder完全一致）=====================
    public FinishedRecipe build() {
        return new FinishedRecipe() {

            @Override
            public void serializeRecipeData(JsonObject pJson) {
                ElfPlateRecipeBuilder.this.toJson(pJson);
            }

            @Override
            public ResourceLocation getId() {
                // 配方ID拼接规则：命名空间/elf_plate/配方名（和petal_apothecary格式完全对齐）
                return ResourceLocation.tryBuild(id.getNamespace(), "elf_plate/" + id.getPath());
            }

            @Override
            public RecipeSerializer<?> getType() {
                return InfuserRecipe.Serializer.INSTANCE;
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null; // 不生成配方进度，和PetalRecipeBuilder保持一致
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null; // 无进度ID
            }
        };
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(this.build());
    }
}
