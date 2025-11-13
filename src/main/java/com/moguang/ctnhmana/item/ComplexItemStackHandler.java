package com.moguang.ctnhmana.item;

import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stack.StackUpgradeConfig;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ComplexItemStackHandler extends InventoryHandler implements IContentChangeAware, ITagSerializable<CompoundTag> {
    protected ComplexItemStackHandler(int numberOfInventorySlots, IStorageWrapper storageWrapper, CompoundTag contentsNbt, Runnable saveHandler, int baseSlotLimit, StackUpgradeConfig stackUpgradeConfig) {
        super(numberOfInventorySlots, storageWrapper, contentsNbt, saveHandler, baseSlotLimit, stackUpgradeConfig);
    }
    @Getter
    @Setter
    protected @NotNull Runnable onContentsChanged = () -> {};
    @Getter
    @Setter
    protected Predicate<ItemStack> filter = stack -> true;
    @Override
    protected boolean isAllowed(ItemStack itemStack) {
        return false;
    }

}
