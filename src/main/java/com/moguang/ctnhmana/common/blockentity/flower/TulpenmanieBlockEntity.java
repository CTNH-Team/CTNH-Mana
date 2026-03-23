package com.moguang.ctnhmana.common.blockentity.flower;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.moguang.ctnhmana.registry.CMBlocks;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

public class TulpenmanieBlockEntity extends GeneratingFlowerBlockEntity {

    public TulpenmanieBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Persisted
    public int burstMana = 670000;

    public int getRange() {
        return 5;
    }

    @Override
    public int getMaxMana() {
        return 999999;
    }

    @Override
    public int getColor() {
        return 0XFFD700;
    }

    @Override
    public void tickFlower() {
        super.tickFlower();
        if (burstMana <= 0) {
            if (this.level.isClientSide()) {

            } else {
                level.removeBlockEntity(this.getBlockPos()); // 从世界中移除旧BlockEntity
                this.setRemoved(); // 终止BlockEntity的所有逻辑
                level.setBlock(this.getBlockPos(), CMBlocks.Tulpenmanie.getDefaultState(), 3);
            }

        }
        if (this.level.isClientSide()) return;

        Direction[] horizontalDirections = {
                Direction.SOUTH,  // 前（南）
                Direction.NORTH,  // 后（北）
                Direction.WEST,   // 左（西）
                Direction.EAST    // 右（东）
        };
        for (Direction dir : horizontalDirections) {
            BlockPos adjacentPos = this.getBlockPos().relative(dir);
            if (!level.isInWorldBounds(adjacentPos)) {
                continue;
            }
            if (level.getBlockEntity(adjacentPos) instanceof ManaPoolBlockEntity me && me.getCurrentMana() >= 100) {
                me.receiveMana(-500);
                this.addMana(500);
                this.burstMana -= 50;
            }
        }
        this.addMana(this.getMana() / 1000 * 2);
        this.burstMana -= this.getMana() / 1000;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
    }
}
