package com.moguang.ctnhmana.common.blockentity.flower;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.registry.CMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

public class TulpenmanieBlockEntity extends GeneratingFlowerBlockEntity {
    public TulpenmanieBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Persisted
    public int burstMana=670000;
    public int getRange() {
        return 5;
    }
    @Override
    public int getMaxMana() {
        return 1000000;
    }

    @Override
    public int getColor() {
        return 0XFFD700;
    }
    @Override
    public void tickFlower() {
        super.tickFlower();
        if(burstMana<=0)
        {
            if(this.level.isClientSide()) {
                for (int i = 0; i < 25; i++) {
                    double offsetX = Math.random() * 0.5;
                    double offsetY = Math.random() * 0.5 + 0.5;
                    double offsetZ = Math.random() * 0.5;
                    level.addParticle(
                            ParticleTypes.BUBBLE,
                            this.getBlockPos().getX() + 0.5 + offsetX,
                            this.getBlockPos().getY() + 0.5 + offsetY,
                            this.getBlockPos().getZ() + 0.5 + offsetZ,
                            offsetX * 0.2,
                            offsetY * 0.2,
                            offsetZ * 0.2
                    );
                }
            }
            else
            {
                level.removeBlockEntity(this.getBlockPos()); // 从世界中移除旧BlockEntity
                this.setRemoved(); // 终止BlockEntity的所有逻辑
                level.setBlock(this.getBlockPos(), CMBlocks.Tulpenmanie.getDefaultState(),3);
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
            if(level.getBlockEntity(adjacentPos) instanceof ManaPoolBlockEntity me&&me.getCurrentMana()>=100)
            {
                me.receiveMana(-100);
                this.addMana(100);
                this.burstMana-=25;
            }
        }
        this.addMana(this.getMana()/1000);
        this.burstMana-=this.getMana()/1000;
    }
    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
    }
}