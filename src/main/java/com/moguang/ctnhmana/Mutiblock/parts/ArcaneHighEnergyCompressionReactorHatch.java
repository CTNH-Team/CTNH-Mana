package com.moguang.ctnhmana.Mutiblock.parts;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.item.soul.ItemSoulGem;

import java.util.List;

public class ArcaneHighEnergyCompressionReactorHatch extends MultiblockPartMachine implements IDistinctPart, IMachineModifyDrops {
    @Getter
    @Persisted
    private final NotifiableItemStackHandler inventory;
    public ArcaneHighEnergyCompressionReactorHatch(IMachineBlockEntity holder,int slot_range)
    {
        super(holder);
        inventory=createMachineStorage(slot_range);
    }
    protected NotifiableItemStackHandler createMachineStorage(int range) {
        return new NotifiableItemStackHandler(
                this, range*range, IO.NONE, IO.BOTH, slots -> new CustomItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
            }
        });
    }
    @Override
    public void onDrops(List<ItemStack> drops) {
        clearInventory(getInventory().storage);
    }



    @Override
    public boolean isDistinct() {
        return getInventory().isDistinct();
    }

    @Override
    public void setDistinct(boolean isDistinct) {
        getInventory().setDistinct(isDistinct);
    }
}
