package com.moguang.ctnhmana.common.recipe.builder.bloodmagic;

import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.common.recipe.builder.bloodmagic.BloodAltarRecipeBuilder;
import com.moguang.ctnhmana.common.recipe.builder.botania.TerraPlateRecipeBuilder;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TartaricForgeRecipeBuilder {
    // 核心配方属性
    private final List<Ingredient> inputs = new ArrayList<>();
    private ItemStack output;
    private double minimumSouls;
    private double soulDrain;
    private ResourceLocation id;
    private int meta=-1;
    public TartaricForgeRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }
    public static TartaricForgeRecipeBuilder builder(String name) {
        return new TartaricForgeRecipeBuilder(name);
    }
    public TartaricForgeRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }
    public TartaricForgeRecipeBuilder input(ItemStack itemStack) {
        inputs.add(Ingredient.of(itemStack));
        return this;
    }
    public TartaricForgeRecipeBuilder input(Item item) {
        inputs.add(Ingredient.of(new ItemStack(item,1)));
        return this;
    }

    public TartaricForgeRecipeBuilder input(TagKey<Item> tagKey) {
        inputs.add(Ingredient.of(tagKey));
        return this;
    }

    public TartaricForgeRecipeBuilder input(Ingredient ingredient) {
        inputs.add(ingredient);
        return this;
    }
    public TartaricForgeRecipeBuilder input(Item... items) {
        Arrays.stream(items).forEach(item -> inputs.add(Ingredient.of(new ItemStack(item,1))));
        return this;
    }


    public TartaricForgeRecipeBuilder input(Ingredient... ingredients) {
        Arrays.stream(ingredients).forEach(ingredient -> inputs.add(ingredient));
        return this;
    }
    public TartaricForgeRecipeBuilder output(ItemStack output) {
        this.output = output;
        return this;
    }
    public TartaricForgeRecipeBuilder minimumSouls(double minimumSouls) {
        this.minimumSouls = minimumSouls;
        return this;
    }

    public TartaricForgeRecipeBuilder soulDrain(double soulDrain) {
        this.soulDrain = soulDrain;
        return this;
    }
    public TartaricForgeRecipeBuilder circuitMeta(int meta)
    {
        this.meta=meta;
        return this;
    }

    public void toJson(JsonObject json) {
        for (int i = 0; i < Math.min(inputs.size(), 4); i++) {
            json.add(Constants.JSON.INPUT + i, inputs.get(i).toJson());
        }

        json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(output));
        // 序列化灵魂相关属性（转为float保持原有精度）
        json.addProperty(Constants.JSON.TARTARIC_MINIMUM, (float) minimumSouls);
        json.addProperty(Constants.JSON.TARTARIC_DRAIN, (float) soulDrain);
    }
    private GTRecipeBuilder mapToGTBuilder() {
        if (this.output == null || this.inputs.isEmpty()) {
            throw new IllegalStateException("参数缺失是凉爽的夏夜");
        }

        ResourceLocation bmId = TartaricForgeRecipeBuilder.this.id;
        ResourceLocation gtId = GTCEu.id( "hell_forge_" + bmId.getPath());

        GTRecipeBuilder gtBuilder = GTRecipeBuilder.of(gtId, CMRecipeTypes.HELL_FORGE_RECIPES)
                .addCondition(new HellForgeCondition(soulDrain));
        for (Ingredient ingredient : this.inputs) {
            gtBuilder.inputItems(ingredient);
        }
        gtBuilder.outputItems(this.output);

        long gtEUt = (long) (8192);
        gtBuilder.EUt(gtEUt);
        int gtDuration = (int) Math.max(soulDrain, 100);
        gtBuilder.duration(gtDuration);
        if(meta>=0)
            gtBuilder.circuitMeta(meta);
        return gtBuilder;
    }

    public FinishedRecipe build() {
        return new FinishedRecipe() {
            @Override
            public void serializeRecipeData(@Nonnull JsonObject pJson) {
                toJson(pJson); // 复用序列化逻辑
            }

            @Override
            public ResourceLocation getId() {
                return TartaricForgeRecipeBuilder.this.id; // 返回构造器生成的ID
            }

            @Override
            public net.minecraft.world.item.crafting.RecipeSerializer<?> getType() {
                return BloodMagicRecipeSerializers.TARTARIC.getRecipeSerializer();
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
    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(this.build());
        GTRecipeBuilder gtBuilder = this.mapToGTBuilder();
        gtBuilder.save(consumer);
    }

}