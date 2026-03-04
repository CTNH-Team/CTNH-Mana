package com.moguang.ctnhmana.mixin.ars;

import com.hollingsworth.arsnouveau.api.item.inv.ExtractedStack;
import com.hollingsworth.arsnouveau.api.item.inv.FilterableItemHandler;
import com.hollingsworth.arsnouveau.api.item.inv.InventoryManager;
import com.hollingsworth.arsnouveau.api.item.inv.MultiExtractedReference;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = InventoryManager.class,remap = false)
public abstract class InventoryManagerMixin {
    @Inject(
            method = "extractAllFromHandler(Lcom/hollingsworth/arsnouveau/api/item/inv/FilterableItemHandler;Lnet/minecraft/world/item/ItemStack;I)Lcom/hollingsworth/arsnouveau/api/item/inv/MultiExtractedReference;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void arsngt$relaxedExtractAllFromHandler(FilterableItemHandler filterableItemHandler,
                                                     ItemStack desiredStack,
                                                     int count,
                                                     CallbackInfoReturnable<MultiExtractedReference> cir) {

        ItemStack merged = ItemStack.EMPTY;
        int remaining = Math.min(desiredStack.getMaxStackSize(), count);
        List<ExtractedStack> extractedStacks = new ArrayList<>();
        IItemHandler itemHandler = filterableItemHandler.getHandler();

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.extractItem(i, remaining, true);
            if (stack.isEmpty()) {
                continue;
            }
            if (!ItemStack.isSameItem(stack, desiredStack)) {
                continue;
            }

            int toExtract = Math.min(stack.getCount(), remaining);
            remaining -= toExtract;

            if (merged.isEmpty()) {
                merged = stack.copy();
                merged.setCount(toExtract);
            } else {
                merged.grow(toExtract);
            }

            extractedStacks.add(ExtractedStack.from(filterableItemHandler.getHandler(), i, toExtract));

            if (remaining <= 0) {
                break;
            }
        }

        MultiExtractedReference result = new MultiExtractedReference(merged, extractedStacks);

        cir.setReturnValue(result);
        cir.cancel();
    }
}