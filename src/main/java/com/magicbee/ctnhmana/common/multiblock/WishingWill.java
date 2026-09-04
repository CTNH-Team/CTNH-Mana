package com.magicbee.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.ingredient.item.ItemIngredient;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemHandlerHelper;

import org.jetbrains.annotations.NotNull;

public class WishingWill extends RecipeMultiblockMachine {

    /** 巨型假输入：许愿投币池，9 槽 */
    @Persisted
    public final NotifiableItemStackHandler machineStorage;
    /** 巨型假输出：64 槽，配方产物落入此处等待抛出 */
    @Persisted
    protected final NotifiableItemStackHandler dummyOutputStorage;
    protected TickableSubscription poolSubs = null;

    public WishingWill(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.machineStorage = attachTrait(
                new NotifiableItemStackHandler(this, 9, IO.IN, IO.IN));
        dummyOutputStorage = attachTrait(
                new NotifiableItemStackHandler(this, 64, IO.OUT, IO.OUT));
    }

    /** 水池位置：控制器正前方 4 格、低 1 格（控制器 (24,-57,75) -> 水池 (24,-58,79)） */
    private BlockPos getPoolPos() {
        return MachineUtils.getOffset(this, 0, -1, -4);
    }

    /** 吸收水池里的掉落物（什么都吸）放入假输入，放不下的留在原地 */
    protected void vacuumPoolItems() {
        var level = this.getLevel();
        if (level == null || level.isClientSide) return;
        var pos = getPoolPos();
        AABB area = new AABB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                pos.getX() + 1, pos.getY() + 3, pos.getZ() + 1);
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, area)) {
            if (item.getItem().isEmpty()) continue;
            ItemStack rest = ItemHandlerHelper.insertItemStacked(machineStorage, item.getItem(), false);
            if (rest.isEmpty()) {
                item.remove(Entity.RemovalReason.KILLED);
            } else {
                item.getItem().setCount(rest.getCount());
            }
        }
    }

    /** 把假输入里配不上任何配方的物品抛回水池，避免垃圾卡死配方 */
    protected void ejectUnusableItems() {
        var group = getRecipeLogic().getLastGroup();
        if (group == null) return;
        for (int slot = 0; slot < machineStorage.getSlots(); slot++) {
            ItemStack stack = machineStorage.getStackInSlot(slot);
            if (stack.isEmpty()) continue;
            if (!hasRecipeFor(stack, group)) {
                ejectStack(machineStorage.extractItem(slot, stack.getCount(), false));
            }
        }
    }

    /** 配方库中是否存在以该物品为输入的配方 */
    private boolean hasRecipeFor(ItemStack stack, RecipeHandlerGroup group) {
        var iterator = getRecipeType().getRecipeIterator(group, recipe -> true);
        if (iterator == null) return false;
        while (iterator.hasNext()) {
            var recipe = iterator.next();
            for (ItemIngredient ingredient : recipe.getInputContents(ItemRecipeCapability.CAP)) {
                if (ingredient.test(stack)) return true;
            }
        }
        return false;
    }

    /** 把假输出里的产物按随机动量从水池抛出 */
    protected void ejectOutputs() {
        for (int slot = 0; slot < dummyOutputStorage.getSlots(); slot++) {
            ItemStack stack = dummyOutputStorage.getStackInSlot(slot);
            if (stack.isEmpty()) continue;
            ejectStack(dummyOutputStorage.extractItem(slot, stack.getCount(), false));
        }
    }

    private void ejectStack(@NotNull ItemStack stack) {
        if (stack.isEmpty()) return;
        var level = this.getLevel();
        if (level == null || level.isClientSide) return;
        var pos = getPoolPos();
        ItemEntity itemEntity = new ItemEntity(level,
                pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack);
        itemEntity.setDeltaMovement(
                (level.random.nextDouble() - 0.5) * 0.6,
                0.5 + level.random.nextDouble() * 1.5,
                (level.random.nextDouble() - 0.5) * 0.6);
        itemEntity.setPickUpDelay(10); // 设置拾取延迟
        level.addFreshEntity(itemEntity);
    }

    /** 池子主循环：每 2 tick 抛出产物；空闲时每 20 tick 吸物并吐回无用物品 */
    protected void poolTick() {
        if (!isStructureOperational()) return;
        if (getOffsetTimer() % 2 == 0) {
            ejectOutputs();
        }
        if (recipeLogic.isIdle() && getOffsetTimer() % 20 == 0) {
            vacuumPoolItems();
            ejectUnusableItems();
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        updatePoolSubscription();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        updatePoolSubscription();
    }

    @Override
    protected void onStructureRevalidationChanged(boolean pending) {
        super.onStructureRevalidationChanged(pending);
        updatePoolSubscription();
    }

    private void updatePoolSubscription() {
        if (isStructureOperational()) {
            poolSubs = subscribeServerTick(poolSubs, this::poolTick);
        } else {
            unsubscribePoolTick();
        }
    }

    private void unsubscribePoolTick() {
        if (poolSubs != null) {
            poolSubs.unsubscribe();
            poolSubs = null;
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        unsubscribePoolTick();
    }
}
