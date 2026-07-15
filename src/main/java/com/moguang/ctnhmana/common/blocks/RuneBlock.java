package com.moguang.ctnhmana.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.moguang.ctnhmana.registry.CMBlocks;
import org.jetbrains.annotations.Nullable;

public class RuneBlock extends Block {

    public RuneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                    @Nullable BlockState queryState, @Nullable BlockPos queryPos) {
        return CMBlocks.RUNE_CARRIER_BLOCK.getDefaultState();
    }
}
