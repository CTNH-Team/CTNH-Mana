package com.moguang.ctnhmana.common.recipe.builder.bloodmagic;

import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
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


public class BloodAltarRecipeBuilder {
    private final List<Ingredient> inputs = new ArrayList<>();
    private ItemStack output;
    private int minimumTier = 1; // 默认最低祭坛等级1
    private int syphon = 0;      // 默认虹吸量0
    private int consumeRate = 0; // 默认消耗速率0
    private int drainRate = 0;   // 默认流失速率0
    private final ResourceLocation id;
    private int meta=-1;
    public BloodAltarRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

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
    public BloodAltarRecipeBuilder circuitMeta(int meta)
    {
        this.meta=meta;
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

        JsonObject inputJson = (JsonObject) inputs.get(0).toJson(); // 保持原单输入逻辑，若需多输入可改为JsonArray
        json.add(Constants.JSON.INPUT, inputJson);

        // 序列化非核心参数（使用默认值或用户配置值）
        json.addProperty(Constants.JSON.ALTAR_TIER, this.minimumTier-1);
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
        if (this.output == null || this.inputs.isEmpty()) {
            throw new IllegalStateException("参数缺失是凉爽的夏夜");
        }

        ResourceLocation bmId = BloodAltarRecipeBuilder.this.id;
        ResourceLocation gtId = GTCEu.id( "industrial_altar_" + bmId.getPath());

        GTRecipeBuilder gtBuilder = GTRecipeBuilder.of(gtId, CMRecipeTypes.BLOOD_ALTAR_RECIPES)
                .addCondition(new BloodAltarCondition(this.minimumTier,this.drainRate,this.syphon));

        for (Ingredient ingredient : this.inputs) {
            gtBuilder.inputItems(ingredient);
        }
        gtBuilder.outputItems(this.output);

        long gtEUt = (long) (128 * Math.pow(2, this.minimumTier-1));
        gtBuilder.EUt(gtEUt);
        int gtDuration = Math.max(syphon/consumeRate, 100);
        gtBuilder.duration(gtDuration);
        if(meta>=0)
            gtBuilder.circuitMeta(meta);
        return gtBuilder;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(this.build());
        GTRecipeBuilder gtBuilder = this.mapToGTBuilder();
        gtBuilder.save(consumer);
    }
}