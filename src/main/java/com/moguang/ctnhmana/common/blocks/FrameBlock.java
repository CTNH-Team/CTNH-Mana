package com.moguang.ctnhmana.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 框架方块，透光（不阻挡光照）。
 */
public class FrameBlock extends Block {
    public FrameBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;
    }
}
