package com.magicbee.ctnhmana.data.recipe.builder.apotheosis;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMRecipeTypes;
import dev.shadowsoffire.apotheosis.adventure.Adventure;

import java.util.function.Consumer;

/**
 * Builder for Apotheosis Gem Cutting rarity-upgrade recipes on
 * {@link CMRecipeTypes#GEM_INLAY_RECIPES}. Expects real gem stacks (with gem id + rarity).
 */
public class GemCuttingRecipeBuilder {

    private final ResourceLocation id;
    private ItemStack gem;
    private ItemStack output;
    private ItemStack material;
    private int materialCount;
    private int dustCount = -1;
    private boolean registerMachineRecipe = true;
    private int meta = -1;
    private long eut = GTValues.VA[GTValues.LV];
    private int duration = 20 * 10;

    private GemCuttingRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    public static GemCuttingRecipeBuilder builder(String name) {
        return new GemCuttingRecipeBuilder(name);
    }

    /** Explicit gem stacks (must include gem id + rarity NBT). */
    public GemCuttingRecipeBuilder gem(ItemStack gemStack) {
        this.gem = gemStack.copyWithCount(1);
        return this;
    }

    public GemCuttingRecipeBuilder output(ItemStack outputStack) {
        this.output = outputStack.copyWithCount(1);
        return this;
    }

    public GemCuttingRecipeBuilder material(ItemLike item, int count) {
        this.material = new ItemStack(item.asItem());
        this.materialCount = count;
        return this;
    }

    public GemCuttingRecipeBuilder material(ItemStack stack, int count) {
        this.material = stack.copyWithCount(1);
        this.materialCount = count;
        return this;
    }

    public GemCuttingRecipeBuilder dustCount(int count) {
        this.dustCount = count;
        return this;
    }

    public GemCuttingRecipeBuilder circuitMeta(int meta) {
        this.meta = meta;
        return this;
    }

    public GemCuttingRecipeBuilder EUt(long eut) {
        this.eut = eut;
        return this;
    }

    public GemCuttingRecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    /** Whether to register the gem-inlay GT recipe. Defaults to {@code true}. */
    public GemCuttingRecipeBuilder registerMachineRecipe(boolean register) {
        this.registerMachineRecipe = register;
        return this;
    }

    private GTRecipeBuilder mapToGTBuilder() {
        if (this.gem == null || this.material == null || this.output == null || this.materialCount <= 0) {
            throw new IllegalStateException("宝石切割配方缺少参数: " + this.id);
        }
        if (this.dustCount < 0) {
            throw new IllegalStateException("宝石切割配方未设置宝石粉消耗: " + this.id);
        }

        ResourceLocation gtId = GTCEu.id("gem_inlay_" + this.id.getPath().replace('/', '_'));
        GTRecipeBuilder gtBuilder = GTRecipeBuilder.of(gtId, CMRecipeTypes.GEM_INLAY_RECIPES)
                .inputItems(this.gem.copyWithCount(2))
                .inputItems(new ItemStack(Adventure.Items.GEM_DUST.get(), this.dustCount))
                .inputItems(this.material.copyWithCount(this.materialCount))
                .outputItems(this.output)
                .EUt(this.eut)
                .duration(this.duration)
                .addData("info", true);

        if (this.meta >= 0) {
            gtBuilder.circuitMeta(this.meta);
        }
        return gtBuilder;
    }

    /** Build a raw GT recipe for XEI representative registration (no FinishedRecipe consumer). */
    public GTRecipeDefinition buildRawRecipe() {
        return mapToGTBuilder().buildRawRecipe();
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        if (this.registerMachineRecipe) {
            mapToGTBuilder().save(consumer);
        }
    }
}
