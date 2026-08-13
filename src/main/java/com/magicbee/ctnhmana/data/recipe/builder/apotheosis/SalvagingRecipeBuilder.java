package com.magicbee.ctnhmana.data.recipe.builder.apotheosis;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMRecipeTypes;
import dev.shadowsoffire.apotheosis.adventure.affix.salvaging.SalvagingRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

/**
 * Datagen builder for Apotheosis Salvaging Table recipes ({@code apotheosis:salvaging}).
 * Optionally mirrors into {@link CMRecipeTypes#INDUSTRIAL_SALVAGING_RECIPES}.
 * <p>
 * NBT/type inputs ({@code affix_item}/{@code gem}) only register the native salvaging recipe;
 * machine mirroring is skipped until GT can match them reliably.
 */
public class SalvagingRecipeBuilder {

    private final ResourceLocation id;
    private JsonObject inputJson;
    /** GT machine input; null for NBT-only native inputs that are not mirrored. */
    @Nullable
    private Ingredient machineInput;
    private final List<OutputEntry> outputs = new ArrayList<>();
    private boolean requireAdventureModule = true;
    /** Whether to register the native {@code apotheosis:salvaging} recipe. Defaults to true. */
    private boolean registerNativeRecipe = true;
    /** Whether to also register an industrial salvaging GT recipe. Defaults to true. */
    private boolean registerMachineRecipe = true;
    private int meta = -1;
    private long eut = GTValues.VA[GTValues.ULV];
    private int duration = 20 * 5;

    private SalvagingRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    public static SalvagingRecipeBuilder builder(String name) {
        return new SalvagingRecipeBuilder(name);
    }

    /** Native affix-item input. Does not register a machine recipe. */
    public SalvagingRecipeBuilder affixInput(String rarity) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "apotheosis:affix_item");
        json.addProperty("rarity", rarity);
        this.inputJson = json;
        this.machineInput = null;
        this.registerMachineRecipe = false;
        return this;
    }

    /** Native gem input. Does not register a machine recipe. */
    public SalvagingRecipeBuilder gemInput(String rarity) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "apotheosis:gem");
        json.addProperty("rarity", rarity);
        this.inputJson = json;
        this.machineInput = null;
        this.registerMachineRecipe = false;
        return this;
    }

    public SalvagingRecipeBuilder itemInput(ItemLike item) {
        Ingredient ingredient = Ingredient.of(item);
        this.inputJson = ingredient.toJson().getAsJsonObject();
        this.machineInput = ingredient;
        return this;
    }

    public SalvagingRecipeBuilder itemInput(Ingredient ingredient) {
        var json = ingredient.toJson();
        if (!json.isJsonObject()) {
            throw new IllegalArgumentException("Salvaging input ingredient must serialize to a JsonObject");
        }
        this.inputJson = json.getAsJsonObject();
        this.machineInput = ingredient;
        return this;
    }

    public SalvagingRecipeBuilder output(ItemLike item, int minCount, int maxCount) {
        if (maxCount < minCount) {
            throw new IllegalArgumentException("maxCount must be >= minCount");
        }
        this.outputs.add(new OutputEntry(item.asItem(), minCount, maxCount));
        return this;
    }

    public SalvagingRecipeBuilder requireAdventureModule(boolean require) {
        this.requireAdventureModule = require;
        return this;
    }

    /** Whether to register the native salvaging recipe. Defaults to {@code true}. */
    public SalvagingRecipeBuilder registerNativeRecipe(boolean register) {
        this.registerNativeRecipe = register;
        return this;
    }

    /**
     * Whether to also register the industrial salvaging GT recipe. Defaults to {@code true}.
     * Affix/gem inputs force this to {@code false}.
     */
    public SalvagingRecipeBuilder registerMachineRecipe(boolean register) {
        this.registerMachineRecipe = register;
        return this;
    }

    public SalvagingRecipeBuilder circuitMeta(int meta) {
        this.meta = meta;
        return this;
    }

    public SalvagingRecipeBuilder EUt(long eut) {
        this.eut = eut;
        return this;
    }

    public SalvagingRecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public void toJson(JsonObject json) {
        if (this.inputJson == null || this.outputs.isEmpty()) {
            throw new IllegalStateException("Salvaging recipe missing input or outputs: " + this.id);
        }

        if (this.requireAdventureModule) {
            JsonArray conditions = new JsonArray();
            JsonObject condition = new JsonObject();
            condition.addProperty("type", "apotheosis:module");
            condition.addProperty("module", "adventure");
            conditions.add(condition);
            json.add("conditions", conditions);
        }

        json.add("input", this.inputJson);

        JsonArray outputsJson = new JsonArray();
        for (OutputEntry entry : this.outputs) {
            JsonObject output = new JsonObject();
            output.addProperty("min_count", entry.minCount);
            output.addProperty("max_count", entry.maxCount);
            JsonObject stack = new JsonObject();
            stack.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(entry.item)).toString());
            output.add("stack", stack);
            outputsJson.add(output);
        }
        json.add("outputs", outputsJson);
    }

    private GTRecipeBuilder mapToGTBuilder() {
        if (this.machineInput == null || this.outputs.isEmpty()) {
            throw new IllegalStateException("机器配方缺少可序列化输入或产出: " + this.id);
        }

        ResourceLocation gtId = GTCEu.id("industrial_salvaging_" + this.id.getPath());
        GTRecipeBuilder gtBuilder = GTRecipeBuilder.of(gtId, CMRecipeTypes.INDUSTRIAL_SALVAGING_RECIPES)
                .inputItems(this.machineInput);

        for (OutputEntry entry : this.outputs) {
            gtBuilder.outputItemsRanged(new ItemStack(entry.item), UniformInt.of(entry.minCount, entry.maxCount));
        }

        gtBuilder.EUt(this.eut).duration(this.duration);
        if (this.meta >= 0) {
            gtBuilder.circuitMeta(this.meta);
        }
        return gtBuilder;
    }

    public FinishedRecipe build() {
        return new FinishedSalvagingRecipe();
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        if (this.registerNativeRecipe) {
            consumer.accept(build());
        }
        if (this.registerMachineRecipe && this.machineInput != null) {
            mapToGTBuilder().save(consumer);
        }
    }

    private class FinishedSalvagingRecipe implements FinishedRecipe {

        @Override
        public void serializeRecipeData(@Nonnull JsonObject json) {
            SalvagingRecipeBuilder.this.toJson(json);
        }

        @Override
        public ResourceLocation getId() {
            return ResourceLocation.tryBuild(id.getNamespace(), "salvaging/" + id.getPath());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return SalvagingRecipe.Serializer.INSTANCE;
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
    }

    private record OutputEntry(Item item, int minCount, int maxCount) {}
}
