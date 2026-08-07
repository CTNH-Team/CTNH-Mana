package com.moguang.ctnhmana.utils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentListMap;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.item.ItemIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

public class CTNHManaUtils {

    /** Blood Magic Life Essence fluid ingredient (mB). */
    public static FluidIngredient lifeEssence(int amount) {
        return FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), amount);
    }

    /** Blood Magic Doubt fluid ingredient (mB). */
    public static FluidIngredient doubt(int amount) {
        return FluidIngredient.of(BloodMagicFluids.DOUBT_FLUID.get(), amount);
    }

    public static List<Component> itemTooltipsAdd(Lang[] langs,
                                                  List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }

    public static ItemStack insertItemToOutput(NotifiableItemStackHandler handler, ItemStack stack, boolean simulate) {
        if (handler == null || stack.isEmpty()) {
            return stack;
        }
        if (!stack.isStackable()) {
            return insertToEmpty(handler, stack, simulate);
        }

        IntList emptySlots = new IntArrayList();
        int slots = handler.getSlots();

        for (int i = 0; i < slots; i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (slotStack.isEmpty()) {
                emptySlots.add(i);
            } else if (ItemHandlerHelper.canItemStacksStack(stack, slotStack)) {
                stack = handler.insertItemInternal(i, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }

        for (int slot : emptySlots) {
            stack = handler.insertItemInternal(slot, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    private static ItemStack insertToEmpty(NotifiableItemStackHandler handler, ItemStack stack, boolean simulate) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (handler.getStackInSlot(i).isEmpty()) {
                return handler.insertItemInternal(i, stack, simulate);
            }
        }
        return stack;
    }

    public static List<Component> addMachineTooltips(Lang[] langs) {
        List<Component> list = new ArrayList<>();
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }

    // 加电压不加时间超频（默认不缩放 notConsumable）
    public static void applyParallel(GTRecipe recipe, int pa) {
        multiplyInputs(recipe, pa);
        recipe.multiplyOutputs(pa);
        recipe.parallels *= pa;
        recipe.multiplyEUt(pa);
    }

    // 加时间不加电压超频（默认不缩放 notConsumable）
    public static void applyParallelWithoutEU(GTRecipe recipe, int pa) {
        multiplyInputs(recipe, pa);
        recipe.multiplyOutputs(pa);
        recipe.parallels *= pa;
        recipe.multiplyDuration(pa);
    }

    public static void multiplyInputs(GTRecipe recipe, int multiplier) {
        multiplyInputs(recipe, multiplier, false);
    }

    public static void multiplyInputs(GTRecipe recipe, int multiplier, boolean scaleNonConsumables) {
        multiplyContentMap(recipe.inputs, multiplier, scaleNonConsumables);
    }

    public static void multiplyTickInputs(GTRecipe recipe, int multiplier) {
        multiplyTickInputs(recipe, multiplier, false);
    }

    public static void multiplyTickInputs(GTRecipe recipe, int multiplier, boolean scaleNonConsumables) {
        multiplyContentMap(recipe.tickInputs, multiplier, scaleNonConsumables);
    }

    public static void multiplyAllContents(GTRecipe recipe, int multiplier) {
        multiplyAllContents(recipe, multiplier, false);
    }

    public static void multiplyAllContents(GTRecipe recipe, int multiplier, boolean scaleNonConsumables) {
        multiplyInputs(recipe, multiplier, scaleNonConsumables);
        recipe.multiplyOutputs(multiplier);
        multiplyTickInputs(recipe, multiplier, scaleNonConsumables);
        recipe.multiplyTickOutputs(multiplier);
    }

    public static int getMaxParallelByInput(RecipeHandlerGroup holder, GTRecipe recipe, int limit, boolean tick) {
        return getMaxParallelByInput(holder, recipe, limit, tick, false);
    }

    public static int getMaxParallelByInput(RecipeHandlerGroup holder, GTRecipe recipe, int limit, boolean tick,
                                            boolean includeNonConsumables) {
        IntSupplier action = () -> {
            int minimum = Integer.MAX_VALUE;
            var map = tick ? recipe.tickInputs : recipe.inputs;
            for (RecipeCapability<?> cap : map.keySet()) {
                if (!cap.doMatchInRecipe()) continue;
                int capParallel = cap.getMaxParallelByInput(holder, recipe, limit, tick);
                if (capParallel == 0) return 0;
                minimum = Math.min(minimum, capParallel);
            }
            return minimum == Integer.MAX_VALUE ? limit : minimum;
        };
        if (includeNonConsumables) {
            return action.getAsInt();
        }
        return withNonConsumablesRemoved(recipe, action);
    }

    /**
     * Mana parallel helper. Defaults to ignoring notConsumable (chance==0) inputs.
     */
    public static int getParallelAmount(RecipeHandlerGroup group, GTRecipe recipe, int parallelLimit) {
        return getParallelAmount(group, recipe, parallelLimit, true, false);
    }

    public static int getParallelAmount(RecipeHandlerGroup group, GTRecipe recipe, int parallelLimit,
                                        boolean includeTick) {
        return getParallelAmount(group, recipe, parallelLimit, includeTick, false);
    }

    /**
     * @param includeNonConsumables if {@code false} (default), chance==0 catalysts do not limit parallel
     */
    public static int getParallelAmount(RecipeHandlerGroup group, GTRecipe recipe, int parallelLimit,
                                        boolean includeTick, boolean includeNonConsumables) {
        if (includeNonConsumables) {
            return ParallelLogic.getParallelAmount(group, recipe, parallelLimit, includeTick);
        }
        return withNonConsumablesRemoved(recipe,
                () -> ParallelLogic.getParallelAmount(group, recipe, parallelLimit, includeTick));
    }

    /**
     * Parallel with fake output capacity of {@link Integer#MAX_VALUE}: output tanks do not limit parallels.
     */
    public static int getParallelAmountWithFakeOutputCapacity(RecipeHandlerGroup group, GTRecipe recipe,
                                                              int parallelLimit) {
        return getParallelAmountWithFakeOutputCapacity(group, recipe, parallelLimit, true, false);
    }

    public static int getParallelAmountWithFakeOutputCapacity(RecipeHandlerGroup group, GTRecipe recipe,
                                                              int parallelLimit, boolean includeTick,
                                                              boolean includeNonConsumables) {
        var previous = group.getOutputVoid();
        try {
            // Treat all output capacities as MAX_VALUE for this parallel pass
            group.setOutputVoid(cap -> true);
            return getParallelAmount(group, recipe, parallelLimit, includeTick, includeNonConsumables);
        } finally {
            group.setOutputVoid(previous);
        }
    }

    private static void multiplyContentMap(ContentListMap map, int multiplier, boolean scaleNonConsumables) {
        if (multiplier == 1) return;
        map.forEachEntry(new ContentListMap.EntryConsumer() {

            @Override
            public <T> void accept(RecipeCapability<T> cap, List<T> list) {
                list.replaceAll(content -> {
                    if (!scaleNonConsumables && isNonConsumable(cap, content)) {
                        return content;
                    }
                    return cap.copyWithMultiplier(content, multiplier);
                });
            }
        });
    }

    private static <T> boolean isNonConsumable(RecipeCapability<T> cap, T content) {
        if (cap == ItemRecipeCapability.CAP) {
            return ((ItemIngredient) content).getChance() == 0;
        }
        if (cap == FluidRecipeCapability.CAP) {
            return ((FluidIngredient) content).getChance() == 0;
        }
        return false;
    }

    private static int withNonConsumablesRemoved(GTRecipe recipe, IntSupplier action) {
        var heldItems = stripNonConsumables(recipe.inputs.get(ItemRecipeCapability.CAP));
        var heldFluids = stripNonConsumables(recipe.inputs.get(FluidRecipeCapability.CAP));
        var heldTickItems = stripNonConsumables(recipe.tickInputs.get(ItemRecipeCapability.CAP));
        var heldTickFluids = stripNonConsumables(recipe.tickInputs.get(FluidRecipeCapability.CAP));
        try {
            return action.getAsInt();
        } finally {
            restore(recipe.inputs.get(ItemRecipeCapability.CAP), heldItems);
            restore(recipe.inputs.get(FluidRecipeCapability.CAP), heldFluids);
            restore(recipe.tickInputs.get(ItemRecipeCapability.CAP), heldTickItems);
            restore(recipe.tickInputs.get(FluidRecipeCapability.CAP), heldTickFluids);
        }
    }

    private static <T> List<T> stripNonConsumables(List<T> inputs) {
        if (inputs == null || inputs.isEmpty()) return null;
        List<T> held = null;
        for (var it = inputs.iterator(); it.hasNext();) {
            T ing = it.next();
            boolean nc = false;
            if (ing instanceof ItemIngredient item) {
                nc = item.getChance() == 0;
            } else if (ing instanceof FluidIngredient fluid) {
                nc = fluid.getChance() == 0;
            }
            if (nc) {
                if (held == null) held = new ArrayList<>();
                held.add(ing);
                it.remove();
            }
        }
        return held;
    }

    private static <T> void restore(List<T> inputs, List<T> held) {
        if (held != null && inputs != null) {
            inputs.addAll(held);
        }
    }
}
