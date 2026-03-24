package com.moguang.ctnhmana.common.blockentity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

public class SatoriRoseBlockEntity extends GeneratingFlowerBlockEntity {
    public SatoriRoseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getMaxMana() {
        return 82400;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return null;
    }
}