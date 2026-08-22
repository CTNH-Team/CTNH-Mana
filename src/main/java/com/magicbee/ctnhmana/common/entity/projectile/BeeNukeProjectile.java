package com.magicbee.ctnhmana.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
 * 蜜蜂核弹投掷物：巨蜂第三阶段朝玩家抛射，命中地面或生物时分两阶段生效。
 * 第一阶段：进行 40 半径的爆炸（特效 + 25 点爆炸伤害）；
 * 第二阶段：生成半径 25 的蜂蜜块（不取代基岩），并对半径 25 内所有非蜜蜂生物造成 75 点凋零伤害。
 */
public class BeeNukeProjectile extends ThrowableItemProjectile {

    /** 第一阶段爆炸半径/伤害（格 / 点爆炸伤害） */
    private static final double EXPLODE_RADIUS = 40.0D;
    private static final float EXPLODE_DAMAGE = 25.0F;
    /** 第二阶段蜂蜜块/凋零半径与伤害（格 / 点凋零伤害） */
    private static final double RADIUS = 25.0D;
    private static final float AOE_DAMAGE = 75.0F;

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

    /** 核弹爆炸：第一阶段 40 半径爆炸（特效 + 25 爆炸伤害），第二阶段生成蜂蜜块 + 75 凋零伤害 */
    private void explodeAt(BlockPos center) {
        Level level = this.level();
        Vec3 vec = new Vec3(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D);

        // 第一步：落点先来一次带特效的大爆炸（粒子 + 音效），并对 40 半径造成 25 点爆炸伤害
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, center.getX() + 0.5D, center.getY() + 0.5D,
                    center.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, center.getX() + 0.5D, center.getY() + 0.5D,
                    center.getZ() + 0.5D, 8, 1.5D, 1.5D, 1.5D, 0.15D);
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, center.getX() + 0.5D, center.getY() + 0.5D,
                    center.getZ() + 0.5D, 40, 3.0D, 3.0D, 3.0D, 0.4D);
        }
        level.playSound(null, center, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.2F, 0.9F);
        level.levelEvent(2001, center, net.minecraft.world.level.block.Block.getId(level.getBlockState(center)));
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class,
                new AABB(vec, vec).inflate(EXPLODE_RADIUS),
                e -> e.isAlive() && !(e instanceof GiantBee) && !(e instanceof RoyalServantBee))) {
            entity.hurt(entity.damageSources().explosion(this, this.getOwner()), EXPLODE_DAMAGE);
        }

        // 第二步：生成半径 25 的蜂蜜实心球（不取代基岩），并对半径 25 内非蜜蜂生物造成 75 点凋零伤害
        double radiusSq = (RADIUS + 0.5D) * (RADIUS + 0.5D);
        int r = (int) Math.ceil(RADIUS);
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                for (int k = -r; k <= r; k++) {
                    if (i * i + j * j + k * k > radiusSq) {
                        continue;
                    }
                    BlockPos target = center.offset(i, j, k);
                    // 不取代基岩（及不可破坏方块）
                    if (level.getBlockState(target).getDestroySpeed(level, target) < 0.0F ||
                            level.getBlockState(target).is(Blocks.BEDROCK)) {
                        continue;
                    }
                    level.setBlockAndUpdate(target, Blocks.HONEY_BLOCK.defaultBlockState());
                }
            }
        }
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class,
                new AABB(vec, vec).inflate(RADIUS),
                e -> e.isAlive() && !(e instanceof GiantBee) && !(e instanceof RoyalServantBee))) {
            entity.hurt(entity.damageSources().wither(), AOE_DAMAGE);
        }
    }
}
