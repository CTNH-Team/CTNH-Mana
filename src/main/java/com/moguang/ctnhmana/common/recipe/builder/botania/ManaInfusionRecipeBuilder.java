package com.moguang.ctnhmana.common.recipe.builder.botania;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.common.recipe.builder.bloodmagic.BloodAltarRecipeBuilder;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.helper.ItemNBTHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public  class ManaInfusionRecipeBuilder {
    // 静态常量：催化物（对应原 FinishedRecipe 的 CONJURATION/ALCHEMY）
    public static final StateIngredient CONJURATION_CATALYST = StateIngredientHelper.of(BotaniaBlocks.conjurationCatalyst);
    public static final StateIngredient ALCHEMY_CATALYST = StateIngredientHelper.of(BotaniaBlocks.alchemyCatalyst);
    private final List<Ingredient> inputs = new ArrayList<>();
    private final ResourceLocation id;
    private ItemStack output;
    private int mana;
    private int meta=-1;
    @Nullable
    private StateIngredient catalyst;
    @Nullable
    private String group;

    // 构造方法（对齐 PetalRecipeBuilder）
    public ManaInfusionRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    // 静态创建方法（对齐 PetalRecipeBuilder 的 builder 方法）
    public static ManaInfusionRecipeBuilder builder(String name) {
        return new ManaInfusionRecipeBuilder(name);
    }

    public ManaInfusionRecipeBuilder input(ItemStack itemStack) {
        inputs.add(Ingredient.of(itemStack));
        return this;
    }
    public ManaInfusionRecipeBuilder input(TagKey<Item> tagKey) {
        inputs.add(Ingredient.of(tagKey));
        return this;
    }
    public ManaInfusionRecipeBuilder input(Ingredient ingredient) {
        inputs.add(ingredient);
        return this;
    }
    public ManaInfusionRecipeBuilder input(Ingredient... ingredients) {
        Arrays.stream(ingredients).forEach(ingredient -> inputs.add(ingredient));
        return this;
    }

    // 输出物品（对齐 PetalRecipeBuilder）
    public ManaInfusionRecipeBuilder output(ItemStack itemStack) {
        this.output = itemStack;
        return this;
    }

    // 魔力消耗
    public ManaInfusionRecipeBuilder mana(int mana) {
        this.mana = mana;
        return this;
    }

    // 催化物
    public ManaInfusionRecipeBuilder catalyst(@Nullable StateIngredient catalyst) {
        this.catalyst = catalyst;
        return this;
    }

    // 配方分组
    public ManaInfusionRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }
    public ManaInfusionRecipeBuilder circuitMeta(int meta)
    {
        this.meta=meta;
        return this;
    }
    private GTRecipeBuilder mapToGTBuilder() {
        if (this.output == null || this.inputs.isEmpty()) {
            throw new IllegalStateException("参数缺失是凉爽的夏夜");
        }

        ResourceLocation bmId = ManaInfusionRecipeBuilder.this.id;
        ResourceLocation gtId = GTCEu.id( "botania_recipes" + bmId.getPath());

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
            if(ingredient.getItems()[0].getCount()>1)
                gtBuilder.inputItems(ingredient.getItems()[0]);
            else gtBuilder.inputItems(ingredient);

//            gtBuilder.inputItems(ingredient.getItems()[0]);
        }
        gtBuilder.outputItems(this.output);
        long gtEUt=(long)(128);
        gtBuilder.EUt(gtEUt);
        int gtDuration = Math.max(20,this.mana/5000);
        gtBuilder.duration(gtDuration);
        if(meta>=0)
            gtBuilder.circuitMeta(meta);

        return gtBuilder;
    }
    public void toJson(JsonObject json) {
        JsonArray ingredients = new JsonArray();
        List<Ingredient> inputs = this.inputs;
        int len = inputs.size();

        json.add("input",inputs.get(0).toJson());
        json.add("output", ItemNBTHelper.serializeStack(this.output));
        json.addProperty("mana", this.mana);
        if (this.group != null && !this.group.isEmpty()) {
            json.addProperty("group", this.group);
        }
        if(this.group==null)
        {
            json.addProperty("group", "");
        }
        if (this.catalyst != null) {
            json.add("catalyst", this.catalyst.serialize());
        }
    }
    public FinishedRecipe build() {
        return new FinishedRecipe() {

            @Override
            public void serializeRecipeData(JsonObject pJson) {
                toJson(pJson);
            }

            @Override
            public ResourceLocation getId() {
                return ResourceLocation.tryBuild(id.getNamespace(), "mana_infusion/"  + id.getPath());
            }

            @Override
            public RecipeSerializer<?> getType() {
                return BotaniaRecipeTypes.MANA_INFUSION_SERIALIZER;
            }

            @org.jetbrains.annotations.Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @org.jetbrains.annotations.Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        };
    }

    // 保存配方（对齐 PetalRecipeBuilder 的 save 方法）
    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(build());
        GTRecipeBuilder gtBuilder = this.mapToGTBuilder();
        gtBuilder.save(consumer);
    }
}
