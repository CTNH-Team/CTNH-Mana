package com.moguang.ctnhmana.data.recipe.builder.apotheosis;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.cutting.GemCuttingMenu;

import java.util.function.Consumer;

/**
 * Datagen builder for Apotheosis Gem Cutting Table rarity-upgrade recipes,
 * mirrored into {@link CMRecipeTypes#GEM_INLAY_RECIPES} for the gem inlay machine.
 * <p>
 * Native gem cutting has no RecipeSerializer (hardcoded in {@link GemCuttingMenu});
 * this builder only registers GT recipes.
 */
public class GemCuttingRecipeBuilder {

    private final ResourceLocation id;
    private ItemStack gem;
    private ItemStack material;
    private int materialCount;
    private ItemStack output;
    private int dustCount = -1;
    private long eut = GTValues.VA[GTValues.LV];
    private int duration = 20 * 10;
    private int meta = -1;

    private GemCuttingRecipeBuilder(String name) {
        this.id = CTNHMana.id(name);
    }

    public static GemCuttingRecipeBuilder builder(String name) {
        return new GemCuttingRecipeBuilder(name);
    }

    /** Main gem and sacrificial duplicate (same stack, count 2 in GT). */
    public GemCuttingRecipeBuilder gem(ItemStack gemStack) {
        this.gem = gemStack.copyWithCount(1);
        return this;
    }

    public GemCuttingRecipeBuilder material(ItemStack materialStack, int count) {
        this.material = materialStack.copyWithCount(1);
        this.materialCount = count;
        return this;
    }

    public GemCuttingRecipeBuilder output(ItemStack outputStack) {
        this.output = outputStack.copy();
        return this;
    }

    /** Dust cost from {@link GemCuttingMenu#getDustCost}. Required. */
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
                .duration(this.duration);

        if (this.meta >= 0) {
            gtBuilder.circuitMeta(this.meta);
        }
        return gtBuilder;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        mapToGTBuilder().save(consumer);
    }
}
