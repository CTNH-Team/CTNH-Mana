package com.moguang.ctnhmana.common.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moguang.ctnhmana.CTNHMana;
import mythicbotany.data.recipes.extension.InfuserExtension;
import mythicbotany.infuser.InfuserRecipe;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import org.moddingx.libx.crafting.RecipeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 精灵台配方建造器
 * 贴合PetalRecipeBuilder编码风格，支持魔力消耗、颜色渐变、多原料/带数量原料、配方分组
 */
public class ElfPlateRecipeBuilder {
    // 核心配方属性，初始化默认值（颜色默认白色，魔力消耗默认未设置）
    private final List<Ingredient> inputs = new ArrayList<>();
    private final ResourceLocation id;
    private ItemStack output;
    private String group = "";
    private int manaCost = -1;
    private int fromColor = 16777215; // 白色默认RGB值
    private int toColor = 16777215;   // 白色默认RGB值

    /**
     * 私有构造器，禁止直接实例化，通过静态builder工厂方法调用
     * @param name 配方基础名称，用于生成配方ID
     */
    private ElfPlateRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    // ===================== 静态工厂方法（和PetalRecipeBuilder完全一致）=====================
    public static ElfPlateRecipeBuilder builder(String name) {
        return new ElfPlateRecipeBuilder(name);
    }

    // ===================== 原料添加方法（链式调用，支持多类型原料/带数量）=====================
    // 单物品原料（ItemLike，适配物品/方块实例）
    public ElfPlateRecipeBuilder input(ItemLike item) {
        this.inputs.add(Ingredient.of(item));
        return this;
    }

    // 带数量的物品原料（循环添加对应次数，满足多份原料需求）
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

    // ===================== JSON序列化（抽离方法，解耦逻辑，和PetalRecipeBuilder一致）=====================
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
        // 序列化精灵台核心属性：魔力消耗、颜色渐变
        json.addProperty("mana", this.manaCost);
        json.addProperty("fromColor", this.fromColor);
        json.addProperty("toColor", this.toColor);
    }

    // ===================== 构建FinishedRecipe（匿名内部类，和PetalRecipeBuilder完全一致）=====================
    public FinishedRecipe build() {
        // 核心校验：魔力消耗未设置，抛出明确异常
        if (this.manaCost < 0) {
            throw new IllegalStateException("No mana cost set for elf plate recipe: " + this.id);
        }
        // 额外校验：输出物品未设置/空，抛出异常（避免运行时空指针）
        if (this.output == null || this.output.isEmpty()) {
            throw new IllegalStateException("No output set for elf plate recipe: " + this.id);
        }

        return new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject pJson) {
                ElfPlateRecipeBuilder.this.toJson(pJson);
            }

            @Override
            public ResourceLocation getId() {
                // 配方ID拼接规则：命名空间/elf_plate/配方名（和petal_apothecary格式完全对齐）
                return ResourceLocation.tryBuild(id.getNamespace(), "elf_plate/"  + id.getPath());
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

    // ===================== 保存配方（对接DataGenerator，一键保存，和PetalRecipeBuilder一致）=====================
    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(this.build());
    }
}