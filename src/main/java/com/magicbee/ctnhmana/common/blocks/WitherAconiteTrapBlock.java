package com.magicbee.ctnhmana.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import com.magicbee.ctnhmana.common.blockentity.WitherAconiteTrapBlockEntity;
import com.magicbee.ctnhmana.registry.CMBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 恶意凋零菟葵方块（凋灵兔葵投掷物落地生成）：悬浮于空中（无支撑不掉落），
 * 挖掉不掉落任何物品；方块实体负责周期生成凋零雾，存在 30 秒后爆炸。
 */
public class WitherAconiteTrapBlock extends Block implements EntityBlock {

    public WitherAconiteTrapBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // 悬浮：不受下方方块支撑约束
        return true;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // 挖掉不掉落
        return List.of();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WitherAconiteTrapBlockEntity(CMBlockEntities.WITHER_ACONITE_TRAP.get(), pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }
        return (lvl, pos, st, be) -> {
            if (be instanceof WitherAconiteTrapBlockEntity trap) {
                WitherAconiteTrapBlockEntity.serverTick(lvl, pos, st, trap);
            }
        };
    }
}
