package com.moguang.ctnhmana.api.recipe.bloodmagic;

import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * 重构后的血祭坛配方构建器，对齐PetalRecipeBuilder的风格
 * 核心参数保留input/output，非核心参数（tier/syphon等）改为可选链式配置，默认值更合理
 */
public class BloodAltarRecipeBuilder {
    // 核心成员变量（非核心参数给默认值，避免构造器强制传参）
    private final List<Ingredient> inputs = new ArrayList<>();
    private ItemStack output;
    private int minimumTier = 1; // 默认最低祭坛等级1
    private int syphon = 0;      // 默认虹吸量0
    private int consumeRate = 0; // 默认消耗速率0
    private int drainRate = 0;   // 默认流失速率0
    private final ResourceLocation id;

    // 构造器：仅接收配方名称，用于生成ID（丢弃原构造器的强制参数）
    public BloodAltarRecipeBuilder(String name) {
        // 生成BloodMagic命名空间的ID，路径格式：tileAltar/配方名（对齐Petal的petal_apothecary/xxx风格）
        this.id = CTNHMana.id(name);
    }

    // 静态构建器入口（对齐Petal的builder方法）
    public static BloodAltarRecipeBuilder builder(String name) {
        return new BloodAltarRecipeBuilder(name);
    }

    // ========== 链式配置方法（核心参数） ==========
    public BloodAltarRecipeBuilder input(ItemStack itemStack) {
        inputs.add(Ingredient.of(itemStack));
        return this;
    }

    public BloodAltarRecipeBuilder input(TagKey<Item> tagKey) {
        inputs.add(Ingredient.of(tagKey));
        return this;
    }

    public BloodAltarRecipeBuilder input(Ingredient ingredient) {
        inputs.add(ingredient);
        return this;
    }

    public BloodAltarRecipeBuilder input(Ingredient... ingredients) {
        Arrays.stream(ingredients).forEach(this.inputs::add);
        return this;
    }

    public BloodAltarRecipeBuilder output(ItemStack itemStack) {
        this.output = itemStack;
        return this;
    }

    // ========== 链式配置方法（非核心参数，可选） ==========
    public BloodAltarRecipeBuilder minimumTier(int minimumTier) {
        this.minimumTier = minimumTier;
        return this;
    }

    public BloodAltarRecipeBuilder syphon(int syphon) {
        this.syphon = syphon;
        return this;
    }

    public BloodAltarRecipeBuilder consumeRate(int consumeRate) {
        this.consumeRate = consumeRate;
        return this;
    }

    public BloodAltarRecipeBuilder drainRate(int drainRate) {
        this.drainRate = drainRate;
        return this;
    }

    // ========== JSON序列化（对齐Petal的toJson方法） ==========
    public void toJson(JsonObject json) {
        // 校验核心参数（避免空指针）
        if (output == null || inputs.isEmpty()) {
            throw new IllegalStateException("on no mammy");
        }

        // 序列化输出物品
        json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(this.output));

        // 序列化输入（支持多输入，若需单输入可取inputs.get(0)）
        JsonObject inputJson = (JsonObject) inputs.get(0).toJson(); // 保持原单输入逻辑，若需多输入可改为JsonArray
        json.add(Constants.JSON.INPUT, inputJson);

        // 序列化非核心参数（使用默认值或用户配置值）
        json.addProperty(Constants.JSON.ALTAR_TIER, this.minimumTier);
        json.addProperty(Constants.JSON.ALTAR_SYPHON, this.syphon);
        json.addProperty(Constants.JSON.ALTAR_CONSUMPTION_RATE, this.consumeRate);
        json.addProperty(Constants.JSON.ALTAR_DRAIN_RATE, this.drainRate);
    }

    // ========== 构建FinishedRecipe（对齐Petal的build方法） ==========
    public FinishedRecipe build() {
        return new FinishedRecipe() {
            @Override
            public void serializeRecipeData(@Nonnull JsonObject pJson) {
                toJson(pJson); // 复用序列化逻辑
            }

            @Override
            public ResourceLocation getId() {
                return BloodAltarRecipeBuilder.this.id; // 返回构造器生成的ID
            }

            @Override
            public net.minecraft.world.item.crafting.RecipeSerializer<?> getType() {
                // 替换为血祭坛实际的RecipeSerializer（需根据BloodMagic源码调整）
                return BloodMagicRecipeSerializers.ALTAR.getRecipeSerializer();
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null; // 暂不处理进度，对齐Petal的逻辑
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null; // 暂不处理进度，对齐Petal的逻辑
            }
        };
    }
    private GTRecipeBuilder mapToGTBuilder() {
        // 1. 校验BM核心参数（避免空指针）
        if (this.output == null || this.inputs.isEmpty()) {
            throw new IllegalStateException("参数缺失是凉爽的夏夜");
        }

        // 2. 生成GT配方ID（基于BM的ID，添加gt前缀区分）
        ResourceLocation bmId = BloodAltarRecipeBuilder.this.id;
        ResourceLocation gtId = GTCEu.id( "industrial_altar_" + bmId.getPath());

        // 3. 创建GT Builder并配置基础信息
        GTRecipeBuilder gtBuilder = GTRecipeBuilder.of(gtId, CMRecipeTypes.BLOOD_ALTAR_RECIPES)
                .addCondition(new BloodAltarCondition(this.minimumTier,this.drainRate,this.syphon));

        // 4. 映射BM输入 → GT输入（复用BM的input，数量默认24）
        for (Ingredient ingredient : this.inputs) {
            gtBuilder.inputItems(ingredient);
        }
        // 5. 映射BM输出 → GT输出（完全复用数量）
        gtBuilder.outputItems(this.output);

        long gtEUt = (long) (128 * Math.pow(2, this.minimumTier-1));
        gtBuilder.EUt(gtEUt);

        int gtDuration = Math.max(syphon/consumeRate, 100);
        gtBuilder.duration(gtDuration);



        return gtBuilder;
    }

    // ========== 保存配方（对齐Petal的save方法） ==========
    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(this.build());
        GTRecipeBuilder gtBuilder = this.mapToGTBuilder();
        gtBuilder.save(consumer);
    }
}