package com.magicbee.ctnhmana.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.magicbee.ctnhmana.registry.CMItems;

/**
 * 恶意热爆花投掷物：巨蜂二阶段向八方向抛射，落地爆炸。
 * 爆炸破坏落点周围（半径 3）可破坏方块并造成 10 点调灵范围伤害。
 */
public class MaliciousThermalilyProjectile extends ThrowableItemProjectile {

    /** 爆炸半径（破坏方块）/ AOE 伤害半径与伤害 */
    private static final double EXPLODE_RADIUS = 3.0D;
    private static final float AOE_DAMAGE = 10.0F;

    public MaliciousThermalilyProjectile(EntityType<? extends MaliciousThermalilyProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return CMItems.MALICIOUS_THERMALILY.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.explode();
            this.discard();
        }
    }

    /** 落地爆炸：破坏周围方块 + 10 点范围伤害 */
    private void explode() {
        Level level = this.level();
        BlockPos center = this.blockPosition();
        // 破坏落点周围可破坏方块
        int radius = (int) Math.ceil(EXPLODE_RADIUS);
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius))) {
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || !state.getFluidState().isEmpty()) {
                continue;
            }
            if (state.getDestroySpeed(level, pos) < 0.0F || !state.canEntityDestroy(level, pos, this)) {
                continue;
            }
            Block.dropResources(state, level, pos, null, this, ItemStack.EMPTY);
            level.levelEvent(2001, pos, Block.getId(state));
            level.removeBlock(pos, false);
        }
        // 10 点范围伤害（调灵伤害，不炸自己）
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(EXPLODE_RADIUS),
                e -> e.isAlive() && e.getId() != this.getId())) {
            entity.hurt(entity.damageSources().explosion(this, this.getOwner()), AOE_DAMAGE);
        }
        // 爆炸音效 + 粒子
        level.levelEvent(2001, center, Block.getId(level.getBlockState(center)));
        level.playSound(null, center, net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE,
                net.minecraft.sounds.SoundSource.HOSTILE, 1.0F, 1.0F);
    }
}
