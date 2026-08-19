package com.magicbee.ctnhmana.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.magicbee.ctnhmana.registry.CMBlocks;
import mythicbotany.register.ModBlocks;

/**
 * 凋灵兔葵投掷物（来源：神话魔力学）：由巨蜂巡空时抛下，落地后在落点无条件生成
 * 悬浮的恶意凋零菟葵方块（该花会生成凋零雾，30 秒后爆炸）。
 */
public class WitherAconiteProjectile extends ThrowableItemProjectile {

    public WitherAconiteProjectile(EntityType<? extends WitherAconiteProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModBlocks.witherAconite.asItem();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.placeTrapBlock(result);
            this.discard();
        }
    }

    /** 落点无条件生成悬浮的恶意凋零菟葵；位置被实心方块占据时向上找空位 */
    private void placeTrapBlock(BlockHitResult result) {
        Level level = this.level();
        BlockPos pos = result.getBlockPos().relative(result.getDirection());
        for (int i = 0; i < 4; i++) {
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || state.canBeReplaced()) {
                level.setBlock(pos, CMBlocks.WITHER_ACONITE_TRAP.get().defaultBlockState(), 3);
                return;
            }
            pos = pos.above();
        }
    }
}
