// package com.moguang.ctnhmana.mixin.ars;
//
// import com.hollingsworth.arsnouveau.client.container.StoredItemStack;
// import com.hollingsworth.arsnouveau.client.jei.CraftingTerminalTransferHandler;
// import net.minecraft.world.item.ItemStack;
// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Overwrite;
// import org.spongepowered.asm.mixin.Shadow;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
//
// @Mixin(value = StoredItemStack.class,remap = false)
// public abstract class StoredItemStackMixin {
// @Shadow
// private ItemStack stack;
// /**
// * @author 80802345
// * @reason killemall
// */
// @Overwrite
// public boolean equals(Object obj) {
// if (this == obj) return true;
// if (!(obj instanceof StoredItemStack other)) return false;
//
// ItemStack a = this.stack;
// ItemStack b = other.getStack();
// if (a == null || b == null) return a == b;
//
// return ItemStack.isSameItem(a, b);
// }
// /**
// * @author 80802345
// * @reason avoid diff
// */
// @Overwrite
// public int hashCode() {
// ItemStack a = this.stack;
// return (a == null) ? 0 : a.getItem().hashCode();
// }
//
//
// }
