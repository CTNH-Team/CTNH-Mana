package com.moguang.ctnhmana.api.blockentity;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IZenithMartixBlockEntity extends MetaMachineBlockEntity implements IMachineBlockEntity, IManaged {
    @Getter
    @Setter
    public BlockPos riftPosition;
    @Persisted
    @Getter
    @Setter
    public boolean is_active=false;
    public IZenithMartixBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}
