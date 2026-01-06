package com.moguang.ctnhmana.common.blockentity.machine;

import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ZenithEyeBlockEntity extends BlockEntity implements IManaged {
    public ZenithEyeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return null;
    }

    @Override
    public IManagedStorage getSyncStorage() {
        return null;
    }

    @Override
    public void onChanged() {

    }
}
