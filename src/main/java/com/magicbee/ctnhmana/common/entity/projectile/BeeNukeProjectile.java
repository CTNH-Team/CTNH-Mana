package com.magicbee.ctnhmana.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.magicbee.ctnhmana.common.entity.RoyalServantBee;

/**
 * 蜜蜂核弹投掷物：巨蜂第三阶段朝玩家抛射，命中地面或生物时，
 * 以其落点为中心对半径 25 内的所有非蜜蜂生物造成 100 点凋零伤害，
 * 并生成一块半径 25 的蜜蜡实心块。
 */
public class BeeNukeProjectile extends ThrowableItemProjectile {

    /** 爆炸伤害/实心块半径（格） */
    private static final double RADIUS = 25.0D;
    /** 凋零 AOE 伤害 */
    private static final float AOE_DAMAGE = 100.0F;

    public BeeNukeProjectile(EntityType<? extends BeeNukeProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.HONEYCOMB;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.explodeAt(this.blockPosition());
            this.discard();
        }
    }

    /** 核弹爆炸：25 半径凋零伤害 + 蜜蜡实心块 */
    private void explodeAt(BlockPos center) {
        Level level = this.level();
        // 凋零 AOE：半径 25 内所有非蜜蜂生物
        Vec3 vec = new Vec3(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class,
                new AABB(vec, vec).inflate(RADIUS),
                e -> e.isAlive() && !(e instanceof GiantBee) && !(e instanceof RoyalServantBee))) {
            entity.hurt(entity.damageSources().wither(), AOE_DAMAGE);
        }
        // 生成半径 25 的蜜蜡实心球
        double radiusSq = (RADIUS + 0.5D) * (RADIUS + 0.5D);
        int r = (int) Math.ceil(RADIUS);
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                for (int k = -r; k <= r; k++) {
                    if (i * i + j * j + k * k <= radiusSq) {
                        level.setBlockAndUpdate(center.offset(i, j, k), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
                    }
                }
            }
        }
        // 爆炸音效/粒子
        level.playSound(null, center, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0F, 1.0F);
        level.levelEvent(2001, center, net.minecraft.world.level.block.Block.getId(level.getBlockState(center)));
    }
}
