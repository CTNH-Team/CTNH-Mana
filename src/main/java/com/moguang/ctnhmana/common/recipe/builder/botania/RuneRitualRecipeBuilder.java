package com.moguang.ctnhmana.common.recipe.builder.botania;

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
import mythicbotany.rune.SpecialRuneInput;
import mythicbotany.rune.SpecialRuneOutput;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class RuneRitualRecipeBuilder {

    // 核心配置项（对齐原RuneRitualExtension逻辑）
    private final List<RunePosition> runes = new ArrayList<>();
    private final List<Ingredient> inputs = new ArrayList<>();
    private final List<ItemStack> outputs = new ArrayList<>();
    private Ingredient centerRune; // 仪式核心符文/物品
    private int manaCost = 0;      // 魔力消耗，默认0
    private int tickTime = 200;    // 仪式耗时（刻），默认200刻=10秒
    private ResourceLocation id;   // 配方基础ID
    @Nullable
    private SpecialRuneInput specialInput;  // 自定义特殊输入（需自己实现该接口）
    @Nullable
    private SpecialRuneOutput specialOutput; // 自定义特殊输出（需自己实现该接口）

    private RuneRitualRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    // ===================== 静态工厂方法（统一创建入口，对齐Petal风格）=====================
    public static RuneRitualRecipeBuilder builder(String name) {
        return new RuneRitualRecipeBuilder(name);
    }

    // ===================== 链式配置方法 - 核心符文（仪式中心）=====================
    public RuneRitualRecipeBuilder center(Item center) {
        this.centerRune = Ingredient.of(center);
        return this;
    }

    public RuneRitualRecipeBuilder center(TagKey<Item> centerTag) {
        this.centerRune = Ingredient.of(centerTag);
        return this;
    }

    public RuneRitualRecipeBuilder center(Ingredient centerIngredient) {
        this.centerRune = centerIngredient;
        return this;
    }

    // ===================== 链式配置方法 - 符文摆放（核心逻辑，保留原rune/rune2/rune4）=====================
    /**
     * 单符文摆放：指定坐标摆1个符文，默认不消耗
     * 
     * @param rune 符文物品
     * @param x    相对核心X坐标（-5~5）
     * @param z    相对核心Z坐标（-5~5）
     */
    public RuneRitualRecipeBuilder rune(Item rune, int x, int z) {
        return this.rune(Ingredient.of(rune), x, z, false);
    }

    /**
     * 单符文摆放：指定坐标+是否消耗
     */
    public RuneRitualRecipeBuilder rune(Item rune, int x, int z, boolean consume) {
        return this.rune(Ingredient.of(rune), x, z, consume);
    }

    /**
     * 单符文摆放：Tag/Ingredient重载（通用版）
     */
    public RuneRitualRecipeBuilder rune(Ingredient rune, int x, int z, boolean consume) {
        // 坐标范围校验，超出抛异常（保留原逻辑）
        if (x < -5 || x > 5 || z < -5 || z > 5) {
            throw new IllegalStateException(
                    "Rune positions should not be more than 5 blocks away from the central rune holder: (" + x + "," +
                            z + ")");
        }
        this.runes.add(new RunePosition(rune, x, z, consume));
        return this;
    }

    /**
     * 中心对称双摆：(x,z)和(-x,-z)各摆1个，默认不消耗
     */
    public RuneRitualRecipeBuilder rune2(Item rune, int x, int z) {
        return this.rune2(Ingredient.of(rune), x, z, false);
    }

    /**
     * 中心对称双摆：指定是否消耗
     */
    public RuneRitualRecipeBuilder rune2(Item rune, int x, int z, boolean consume) {
        return this.rune2(Ingredient.of(rune), x, z, consume);
    }

    /**
     * 中心对称双摆：Ingredient重载（底层核心）
     */
    public RuneRitualRecipeBuilder rune2(Ingredient rune, int x, int z, boolean consume) {
        this.rune(rune, x, z, consume);
        this.rune(rune, -x, -z, consume);
        return this;
    }

    /**
     * z轴对立双摆：(x,z)和(x,-z)各摆1个，默认不消耗
     */
    public RuneRitualRecipeBuilder runez(Item rune, int x, int z) {
        return this.runez(Ingredient.of(rune), x, z, false);
    }

    /**
     * z轴对立双摆：指定是否消耗
     */
    public RuneRitualRecipeBuilder runez(Item rune, int x, int z, boolean consume) {
        return this.runez(Ingredient.of(rune), x, z, consume);
    }

    /**
     * z轴对立双摆：Ingredient重载（底层核心）
     */
    public RuneRitualRecipeBuilder runez(Ingredient rune, int x, int z, boolean consume) {
        this.rune(rune, x, z, consume);
        this.rune(rune, -x, z, consume);
        return this;
    }

    /**
     * x轴对立双摆：(x,z)和(-x,z)各摆1个，默认不消耗
     */
    public RuneRitualRecipeBuilder runex(Item rune, int x, int z) {
        return this.runex(Ingredient.of(rune), x, z, false);
    }

    /**
     * x轴对立双摆：指定是否消耗
     */
    public RuneRitualRecipeBuilder runex(Item rune, int x, int z, boolean consume) {
        return this.runex(Ingredient.of(rune), x, z, consume);
    }

    /**
     * x轴对立双摆：Ingredient重载（底层核心）
     */
    public RuneRitualRecipeBuilder runex(Ingredient rune, int x, int z, boolean consume) {
        this.rune(rune, x, z, consume);
        this.rune(rune, x, -z, consume);
        return this;
    }

    /**
     * 四向四摆：十字/四角对称摆4个，默认不消耗
     * x/z为0时摆十字，都非0时摆四角（保留原逻辑）
     */
    public RuneRitualRecipeBuilder rune4(Item rune, int x, int z) {
        return this.rune4(Ingredient.of(rune), x, z, false);
    }

    /**
     * 四向四摆：指定是否消耗
     */
    public RuneRitualRecipeBuilder rune4(Item rune, int x, int z, boolean consume) {
        return this.rune4(Ingredient.of(rune), x, z, consume);
    }

    /**
     * 四向四摆：Ingredient重载（底层核心，保留原对称逻辑）
     */
    public RuneRitualRecipeBuilder rune4(Ingredient rune, int x, int z, boolean consume) {
        if (x == 0) {
            this.rune(rune, 0, -z, consume);
            this.rune(rune, 0, z, consume);
            this.rune(rune, -z, 0, consume);
            this.rune(rune, z, 0, consume);
        } else if (z == 0) {
            this.rune(rune, -x, 0, consume);
            this.rune(rune, x, 0, consume);
            this.rune(rune, 0, -x, consume);
            this.rune(rune, 0, x, consume);
        } else {
            this.rune(rune, -x, -z, consume);
            this.rune(rune, -x, z, consume);
            this.rune(rune, x, -z, consume);
            this.rune(rune, x, z, consume);
        }
        return this;
    }

    // ===================== 链式配置方法 - 魔力/耗时/输入/输出 =====================
    /**
     * 设置仪式魔力消耗
     */
    public RuneRitualRecipeBuilder mana(int manaCost) {
        this.manaCost = manaCost;
        return this;
    }

    /**
     * 设置仪式耗时（单位：游戏刻，20刻=1秒）
     */
    public RuneRitualRecipeBuilder time(int tickTime) {
        this.tickTime = tickTime;
        return this;
    }

    /**
     * 添加仪式输入物品（重载：Item/Tag/Ingredient/可变参数，对齐Petal风格）
     */
    public RuneRitualRecipeBuilder input(Item input) {
        this.inputs.add(Ingredient.of(input));
        return this;
    }

    public RuneRitualRecipeBuilder input(TagKey<Item> inputTag) {
        this.inputs.add(Ingredient.of(inputTag));
        return this;
    }

    public RuneRitualRecipeBuilder input(Ingredient ingredient) {
        this.inputs.add(ingredient);
        return this;
    }

    public RuneRitualRecipeBuilder input(Ingredient... ingredients) {
        Arrays.stream(ingredients).forEach(this.inputs::add);
        return this;
    }

    public RuneRitualRecipeBuilder output(Item output) {
        return this.output(new ItemStack(output));
    }

    public RuneRitualRecipeBuilder output(ItemStack outputStack) {
        this.outputs.add(outputStack);
        return this;
    }

    public RuneRitualRecipeBuilder specialInput(@Nullable SpecialRuneInput specialInput) {
        this.specialInput = specialInput;
        return this;
    }

    public RuneRitualRecipeBuilder specialOutput(@Nullable SpecialRuneOutput specialOutput) {
        this.specialOutput = specialOutput;
        return this;
    }

    // ===================== JSON序列化（抽离方法，对齐Petal风格）=====================
    public void toJson(JsonObject json) {
        // 序列化核心符文
        if (this.centerRune == null) {
            throw new IllegalStateException("Rune ritual must have a central rune!");
        }
        json.add("center", this.centerRune.toJson());

        // 序列化符文摆放位置
        JsonArray runesJson = new JsonArray();
        for (RunePosition pos : this.runes) {
            JsonObject posObj = new JsonObject();
            posObj.add("rune", pos.rune.toJson());
            posObj.addProperty("x", pos.x);
            posObj.addProperty("z", pos.z);
            posObj.addProperty("consume", pos.consume);
            runesJson.add(posObj);
        }
        json.add("runes", runesJson);

        // 序列化魔力消耗/耗时
        json.addProperty("mana", this.manaCost);
        json.addProperty("ticks", this.tickTime);

        // 序列化普通输入
        JsonArray inputsJson = new JsonArray();
        this.inputs.forEach(ing -> inputsJson.add(ing.toJson()));
        json.add("inputs", inputsJson);

        // 序列化输出物品
        JsonArray outputsJson = new JsonArray();
        this.outputs.forEach(stack -> outputsJson.add(ItemNBTHelper.serializeStack(stack)));
        json.add("outputs", outputsJson);

        // 序列化特殊输入/输出（原逻辑：存ID字符串）
        if (this.specialInput != null) {
            json.addProperty("special_input", this.specialInput.id.toString());
        }
        if (this.specialOutput != null) {
            json.addProperty("special_output", this.specialOutput.id.toString());
        }
    }

    // ===================== 构建FinishedRecipe（对齐Petal风格）=====================
    public FinishedRecipe build() {
        // 校验核心符文是否设置
        if (this.centerRune == null) {
            throw new IllegalStateException("Rune ritual recipe missing central rune!");
        }
        // 校验配方ID是否有效
        if (this.id == null) {
            throw new IllegalStateException("Rune ritual recipe has invalid ID!");
        }

        return new FinishedRecipe() {

            @Override
            public void serializeRecipeData(JsonObject pJson) {
                // 调用抽离的toJson方法，简化代码
                RuneRitualRecipeBuilder.this.toJson(pJson);
            }

            @Override
            public ResourceLocation getId() {
                return ResourceLocation.tryBuild(id.getNamespace(), "rune_rituals/" + id.getPath());
            }

            @Override
            public RecipeSerializer<?> getType() {
                return mythicbotany.rune.RuneRitualRecipe.Serializer.INSTANCE;
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                // 无进阶成就，返回null（对齐Petal/原逻辑）
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        };
    }

    // ===================== 保存配方（消费FinishedRecipe，对齐Petal风格）=====================
    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(this.build());
    }

    // ===================== 内部静态类：符文位置信息（封装x/z/符文/是否消耗）=====================
    public static class RunePosition {

        public final Ingredient rune;
        public final int x;
        public final int z;
        public final boolean consume;

        public RunePosition(Ingredient rune, int x, int z, boolean consume) {
            this.rune = rune;
            this.x = x;
            this.z = z;
            this.consume = consume;
        }
    }
}
