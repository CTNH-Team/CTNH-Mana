package com.moguang.ctnhmana.mixin.ars;


import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.container.IAutoFillTerminal;
import com.hollingsworth.arsnouveau.client.container.StoredItemStack;
import com.hollingsworth.arsnouveau.client.jei.CraftingTerminalTransferHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = CraftingTerminalTransferHandler.class,remap = false)
public abstract class MixinEmiLecternRecipeHandler {
    // 复用原类里的 helper，用来创建缺料提示
    @Shadow private IRecipeTransferHandlerHelper helper;

    // 如果你想保留 INTERNAL 错误分支，可以 Shadow 这个字段
    @Shadow
    private static IRecipeTransferError ERROR_INSTANCE;


    @Inject(
            method = "transferRecipe",
            at = @At("HEAD"),
            cancellable = true
    )
    private void relaxedStoragePath(
            AbstractContainerMenu container,
            CraftingRecipe recipe,
            IRecipeSlotsView recipeSlots,
            Player player,
            boolean maxTransfer,
            boolean doTransfer,
            CallbackInfoReturnable<IRecipeTransferError> cir
    ) {
        // 非终端容器，交回原方法处理（保持行为一致）
        if (!(container instanceof IAutoFillTerminal term)) {
            return;
        }

        List<IRecipeSlotView> missing = new ArrayList<>();
        List<IRecipeSlotView> views = recipeSlots.getSlotViews();
        List<ItemStack[]> inputs = new ArrayList<>();
        List<StoredItemStack> storedList = term.getStoredItems();
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

            inputs.add(possibleStacks);
            boolean found = false;
            Inventory inv = player.getInventory();
            for (ItemStack candidate : possibleStacks) {
                if (candidate != null &&
                        findSlotMatchingItemRelaxed(inv, candidate) != -1) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                outer:
                for (ItemStack candidate : possibleStacks) {
                    if (candidate == null || candidate.isEmpty()) continue;
                    for (StoredItemStack stored : storedList) {
                        if (ItemStack.isSameItem(stored.getStack(), candidate)) {
                            found = true;
                            break outer;
                        }
                    }
                }
            }
            if (!found) {
                missing.add(view);
            }
        }
        if (doTransfer) {
            ItemStack[][] stacks = inputs.toArray(new ItemStack[0][]);
            CompoundTag compound = new CompoundTag();
            ListTag list = new ListTag();
            Inventory inv = player.getInventory();

            for (int i = 0; i < stacks.length; ++i) {
                if (stacks[i] == null) continue;

                CompoundTag slotTag = new CompoundTag();
                slotTag.putByte("s", (byte) i);
                int k = 0;

                for (int j = 0; j < stacks[i].length && k < 9; ++j) {
                    ItemStack st = stacks[i][j];
                    if (st == null || st.isEmpty()) continue;

                    boolean ok = false;

                    for (StoredItemStack stored : storedList) {
                        if (ItemStack.isSameItem(stored.getStack(), st)) {
                            ok = true;
                            break;
                        }
                    }
                    if (!ok && findSlotMatchingItemRelaxed(inv, st) != -1) {
                        ok = true;
                    }
                    if (ok) {
                        CompoundTag itemTag = new CompoundTag();
                        st.save(itemTag);
                        slotTag.put("i" + (k++), itemTag);
                    }
                }

                slotTag.putByte("l", (byte) Math.min(9, k));
                list.add(slotTag);
            }

            compound.put("i", list);
            term.sendMessage(compound);
        }
        if (!missing.isEmpty()) {
            IRecipeTransferError error =
                    helper.createUserErrorForMissingSlots(
                            Component.translatable("tooltip.ars_nouveau.items_missing"),
                            missing
                    );
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
}