package com.magicbee.ctnhmana.mixin.ars;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;

import com.hollingsworth.arsnouveau.client.container.IAutoFillTerminal;
import com.hollingsworth.arsnouveau.client.container.StoredItemStack;
import com.hollingsworth.arsnouveau.client.jei.CraftingTerminalTransferHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = CraftingTerminalTransferHandler.class, remap = false)
public abstract class MixinEmiLecternRecipeHandler {

    // 复用原类里的 helper，用来创建缺料提示
    @Shadow
    private IRecipeTransferHandlerHelper helper;

    // 如果你想保留 INTERNAL 错误分支，可以 Shadow 这个字段
    @Shadow
    private static IRecipeTransferError ERROR_INSTANCE;

    @Inject(
            method = "transferRecipe",
            at = @At("HEAD"),
            cancellable = true)
    private void relaxedStoragePath(
                                    AbstractContainerMenu container,
                                    CraftingRecipe recipe,
                                    IRecipeSlotsView recipeSlots,
                                    Player player,
                                    boolean maxTransfer,
                                    boolean doTransfer,
                                    CallbackInfoReturnable<IRecipeTransferError> cir) {
        // 非终端容器，交回原方法处理（保持行为一致）
        if (!(container instanceof IAutoFillTerminal term)) {
            return;
        }

        List<IRecipeSlotView> missing = new ArrayList<>();
        List<IRecipeSlotView> views = recipeSlots.getSlotViews();
        List<ItemStack[]> inputs = new ArrayList<>();
        List<StoredItemStack> storedList = term.getStoredItems();
        Inventory inv = player.getInventory();
        for (IRecipeSlotView view : views) {
            if (view.getRole() != RecipeIngredientRole.INPUT &&
                    view.getRole() != RecipeIngredientRole.CATALYST) {
                continue;
            }

            ItemStack[] possibleStacks = view.getIngredients(VanillaTypes.ITEM_STACK)
                    .toArray(ItemStack[]::new);
            if (possibleStacks.length == 0) {
                inputs.add(null);
                continue;
            }

            ItemStack[] resolvedStacks = resolveAvailableStacks(possibleStacks, inv, storedList);
            inputs.add(resolvedStacks.length == 0 ? null : resolvedStacks);
            if (resolvedStacks.length == 0) {
                missing.add(view);
            }
        }
        if (doTransfer) {
            ItemStack[][] stacks = inputs.toArray(new ItemStack[0][]);
            CompoundTag compound = new CompoundTag();
            ListTag list = new ListTag();

            for (int i = 0; i < stacks.length; ++i) {
                if (stacks[i] == null) continue;

                CompoundTag slotTag = new CompoundTag();
                slotTag.putByte("s", (byte) i);
                int k = 0;

                for (int j = 0; j < stacks[i].length && k < 9; ++j) {
                    ItemStack st = stacks[i][j];
                    if (st == null || st.isEmpty()) continue;

                    CompoundTag itemTag = new CompoundTag();
                    st.save(itemTag);
                    slotTag.put("i" + (k++), itemTag);
                }

                slotTag.putByte("l", (byte) Math.min(9, k));
                list.add(slotTag);
            }

            compound.put("i", list);
            term.sendMessage(compound);
        }
        if (!missing.isEmpty()) {
            IRecipeTransferError error = helper.createUserErrorForMissingSlots(
                    Component.translatable("tooltip.ars_nouveau.items_missing"),
                    missing);
            cir.setReturnValue(error);
        } else {
            cir.setReturnValue(null);
        }
        cir.cancel();
    }

    /**
     * 只按物品 ID 匹配，不看 NBT。
     */
    private int findSlotMatchingItemRelaxed(Inventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.items.size(); ++i) {
            ItemStack item = inventory.items.get(i);
            if (!item.isEmpty() && ItemStack.isSameItem(item, stack)) {
                return i;
            }
        }
        return -1;
    }

    private ItemStack[] resolveAvailableStacks(ItemStack[] possibleStacks, Inventory inventory,
                                               List<StoredItemStack> storedList) {
        Map<String, ItemStack> resolvedStacks = new LinkedHashMap<>();

        for (ItemStack candidate : possibleStacks) {
            if (candidate == null || candidate.isEmpty()) {
                continue;
            }

            int playerSlot = findSlotMatchingItemRelaxed(inventory, candidate);
            if (playerSlot != -1) {
                addResolvedStack(resolvedStacks, inventory.items.get(playerSlot));
            }

            ItemStack exactStored = findStoredMatch(storedList, candidate, false);
            if (!exactStored.isEmpty()) {
                addResolvedStack(resolvedStacks, exactStored);
            }
        }

        if (!resolvedStacks.isEmpty()) {
            return resolvedStacks.values().toArray(ItemStack[]::new);
        }

        for (ItemStack candidate : possibleStacks) {
            if (candidate == null || candidate.isEmpty()) {
                continue;
            }

            ItemStack relaxedStored = findStoredMatch(storedList, candidate, true);
            if (!relaxedStored.isEmpty()) {
                addResolvedStack(resolvedStacks, relaxedStored);
            }
        }

        return resolvedStacks.values().toArray(ItemStack[]::new);
    }

    private ItemStack findStoredMatch(List<StoredItemStack> storedList, ItemStack candidate, boolean relaxed) {
        for (StoredItemStack stored : storedList) {
            ItemStack storedStack = stored.getStack();
            if (storedStack.isEmpty() || !ItemStack.isSameItem(storedStack, candidate)) {
                continue;
            }
            if (relaxed || ItemStack.matches(storedStack, candidate)) {
                return storedStack;
            }
        }
        return ItemStack.EMPTY;
    }

    private void addResolvedStack(Map<String, ItemStack> resolvedStacks, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        ItemStack resolved = stack.copy();
        resolved.setCount(1);
        resolvedStacks.putIfAbsent(resolved.getItem().toString() + "|" + resolved.save(new CompoundTag()), resolved);
    }
}
