package com.moguang.ctnhmana.api.recipe.bloodmagic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.recipe.HellForgeCondition;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TartaricForgeRecipeBuilder {
        private final List<Ingredient> inputs = new ArrayList<>();
        private ItemStack output;
        private double minimumSouls = 0.0; // 默认最低灵魂量0.0
        private double soulDrain = 0.0;    // 默认灵魂消耗0.0
        private final ResourceLocation id;
        private int meta = -1; // 电路元数据，对齐BloodAltar的设计

        // 私有构造器：仅接收配方名称，用于生成ID（对齐BloodAltar的设计）
        private TartaricForgeRecipeBuilder(String name) {
            this.id = CTNHMana.id("tartaric_forge/" + name);
        }

        public static TartaricForgeRecipeBuilder builder(String name) {
            return new TartaricForgeRecipeBuilder(name);
        }

        public TartaricForgeRecipeBuilder input(ItemStack itemStack) {
            inputs.add(Ingredient.of(itemStack));
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

        public TartaricForgeRecipeBuilder input(Ingredient... ingredients) {
            Arrays.stream(ingredients).forEach(this.inputs::add);
            return this;
        }

        public TartaricForgeRecipeBuilder output(ItemStack itemStack) {
            this.output = itemStack;
            return this;
        }

        public TartaricForgeRecipeBuilder minimumSouls(double minimumSouls) {
            if (minimumSouls < 0) {
                throw new IllegalArgumentException("minimumSouls cannot be negative!");
            }
            this.minimumSouls = minimumSouls;
            return this;
        }

        public TartaricForgeRecipeBuilder soulDrain(double soulDrain) {
            if (soulDrain < 0) {
                throw new IllegalArgumentException("soulDrain cannot be negative!");
            }
            this.soulDrain = soulDrain;
            return this;
        }
        public TartaricForgeRecipeBuilder circuitMeta(int meta) {
            this.meta = meta;
            return this;
        }

        public void toJson(JsonObject json) {
            if (output == null || output.isEmpty()) {
                throw new IllegalStateException("Tartaric Forge recipe output cannot be null or empty!");
            }
            if (inputs.isEmpty()) {
                throw new IllegalStateException("Tartaric Forge recipe must have at least one input!");
            }

            json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(this.output));

            JsonArray inputArray = new JsonArray();
            for (Ingredient ingredient : this.inputs) {
                inputArray.add(ingredient.toJson());
            }
            json.add("input", inputArray);
            json.addProperty("minimumSouls", this.minimumSouls);
            json.addProperty("soulDrain", this.soulDrain);
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
                    return null;
                }

                @Nullable
                @Override
                public ResourceLocation getAdvancementId() {
                    return null;
                }
            };
        }


        private GTRecipeBuilder mapToGTBuilder() {

            if (this.output == null || this.output.isEmpty() || this.inputs.isEmpty()) {
                throw new IllegalStateException("Tartaric Forge recipe missing required input/output!");
            }

            ResourceLocation bmId = TartaricForgeRecipeBuilder.this.id;
            ResourceLocation gtId = GTCEu.id("industrial_tartaric_forge_" + bmId.getPath());
            GTRecipeBuilder gtBuilder = GTRecipeBuilder.of(gtId, CMRecipeTypes.HELL_FORGE_RECIPES)
                    .addCondition(new HellForgeCondition("default", (int) this.soulDrain));

            for (Ingredient ingredient : this.inputs) {
                gtBuilder.inputItems(ingredient);
            }

            gtBuilder.outputItems(this.output);


            long gtEUt = GTValues.VA[GTValues.EV];
            gtBuilder.EUt(gtEUt);

            int gtDuration = 200;
            gtBuilder.duration(gtDuration);


            if (meta >= 0) {
                gtBuilder.circuitMeta(meta);
            }

            return gtBuilder;
        }

        public void save(Consumer<FinishedRecipe> consumer) {
            consumer.accept(this.build());
            GTRecipeBuilder gtBuilder = this.mapToGTBuilder();
            gtBuilder.save(consumer);
        }
    }

