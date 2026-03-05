package com.moguang.ctnhmana.common.blockentity.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.client.model.machine.MachineRenderState;

import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.mana.ManaReceiver;

public class MysticSpireBlockEntity extends IManaMachineBlockEntity
                                    implements IMachineBlockEntity, IManaged, ManaReceiver {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MysticSpireBlockEntity.class, MetaMachineBlockEntity.MANAGED_FIELD_HOLDER);
    @Persisted
    @DescSynced
    @RequireRerender
    private MachineRenderState renderState;

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private final long offset;

    public MysticSpireBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.renderState = this.getDefinition().defaultRenderState();
        this.offset = (long) GTValues.RNG.nextInt(20);
    }
}
