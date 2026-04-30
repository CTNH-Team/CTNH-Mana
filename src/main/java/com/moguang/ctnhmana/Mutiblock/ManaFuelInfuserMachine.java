package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.moguang.ctnhmana.item.ManaFuelStick.IManaFuelStick;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaFuelInfuserMachine extends ManaMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ManaFuelInfuserMachine.class, ManaMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Nullable
    protected TickableSubscription tickSubs;
    public ItemStack lastInputItem = ItemStack.EMPTY;
    public int lastInputItemCount = 0;
    public FluidStack lastInputFluid = FluidStack.EMPTY;
    public int lastInputFluidAmount = 0;

    public ManaFuelInfuserMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTick));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch(); // 获取舱室
        if (this.hatch == null) onStructureInvalid(); // 获取不到就别成型
    }

    public void updateTick() {
        tickSubs = subscribeServerTick(tickSubs, this::tickReadInput);
    }

    public void tickReadInput() {
        lastInputItem = ItemStack.EMPTY;
        lastInputItemCount = 0;
        lastInputFluid = FluidStack.EMPTY;
        lastInputFluidAmount = 0;

        // 遍历全部输入槽，筛选魔力燃料并执行恢复/转移
        processAllInputFuels();

        // 读取输入流体（只记录第一个输入流体）
        MachineUtils.applyContents(this, content -> {
            if (lastInputFluid.isEmpty() && content instanceof FluidStack stack && !stack.isEmpty()) {
                lastInputFluid = stack.copy();
                lastInputFluidAmount = stack.getAmount();
            }
        }, FluidRecipeCapability.CAP, IO.IN);
    }

    private void processAllInputFuels() {
        List<IRecipeHandler<?>> inputHandlers = this.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP);
        for (IRecipeHandler<?> inputHandler : inputHandlers) {
            if (!(inputHandler instanceof NotifiableItemStackHandler itemHandler)) continue;
            for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                ItemStack stack = itemHandler.getStackInSlot(slot);
                if (stack.isEmpty()) continue;

                if (lastInputItem.isEmpty()) {
                    lastInputItem = stack.copy();
                    lastInputItemCount = stack.getCount();
                }

                if (!(stack.getItem() instanceof IManaFuelStick fuel)) continue;

                recoverFuelDurabilityByMana(fuel, stack);
                if (stack.getDamageValue() == 0) {
                    moveFuelToOutput(itemHandler, slot, stack);
                } else {
                    itemHandler.setStackInSlot(slot, stack);
                }
            }
        }
    }

    /**
     * 按 10:1 转化率恢复耐久（10 点魔力恢复 1 点耐久）。
     */
    private void recoverFuelDurabilityByMana(IManaFuelStick fuel, ItemStack stack) {
        if (stack.getDamageValue() <= 0 || this.hatch == null) return;
        int needRepair = stack.getDamageValue();
        int maxRepairByMana = (int) (this.hatch.getMana() / 10);
        int repair = Math.min(needRepair, maxRepairByMana);
        if (repair <= 0) return;

        int manaCost = repair * 10;
        if (!this.hatch.consumeManaIfEnough(manaCost)) return;
        stack.setDamageValue(Math.max(0, stack.getDamageValue() - repair));
    }

    /**
     * 耐久已满（damage=0）的燃料尝试移动到输出仓。
     */
    private void moveFuelToOutput(NotifiableItemStackHandler inputHandler, int slot, ItemStack stack) {
        List<IRecipeHandler<?>> outputHandlers = this.getCapabilitiesFlat(IO.OUT, ItemRecipeCapability.CAP);
        ItemStack remain = stack.copy();
        for (IRecipeHandler<?> outputHandler : outputHandlers) {
            if (outputHandler instanceof NotifiableItemStackHandler outItemHandler && !remain.isEmpty()) {
                remain = CTNHManaUtils.insertItemToOutput(outItemHandler, remain, false);
            }
        }

        int moved = stack.getCount() - remain.getCount();
        if (moved <= 0) return;
        stack.shrink(moved);
        inputHandler.setStackInSlot(slot, stack.isEmpty() ? ItemStack.EMPTY : stack);
    }
}
