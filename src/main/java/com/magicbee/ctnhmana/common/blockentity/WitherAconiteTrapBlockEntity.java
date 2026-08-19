package com.magicbee.ctnhmana.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import com.magicbee.ctnhmana.common.entity.RoyalServantBee;
import com.magicbee.ctnhmana.registry.CMMobEffects;

/**
 * 恶意凋零菟葵方块实体：每 2 秒在周围生成一朵凋零雾（每秒 5 凋灵伤害 + 凋灵 II），
 * 存在 30 秒后爆炸：对半径 5 格内生物造成 15 点凋灵伤害并移除自身。
 */
public class WitherAconiteTrapBlockEntity extends BlockEntity {

    /** 凋零雾生成间隔（tick，2 秒） */
    private static final int FOG_INTERVAL = 40;
    /** 雾半径/持续（3 秒） */
    private static final float FOG_RADIUS = 3.5F;
    private static final int FOG_DURATION = 60;
    /** 凋灵 II 效果 */
    private static final int WITHER_DURATION = 60;
    private static final int WITHER_AMPLIFIER = 1;
    /** 30 秒后爆炸 */
    private static final int EXPLODE_AFTER = 600;
    /** 爆炸范围（半径）/凋零伤害 */
    private static final double EXPLODE_RANGE = 5.0D;
    private static final float EXPLODE_DAMAGE = 15.0F;
    /** 为周围蜜蜂回复：每 1 秒回复 5 点生命（5/s） */
    private static final int HEAL_INTERVAL = 20;
    private static final float HEAL_PER_TICK_BATCH = 5.0F;
    private static final double HEAL_RANGE = 8.0D;

    private int age;

    public WitherAconiteTrapBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, WitherAconiteTrapBlockEntity entity) {
        entity.tickServer();
    }

    private void tickServer() {
        this.age++;
        if (this.age % FOG_INTERVAL == 0) {
            this.spawnWitherFog();
        }
        if (this.age % HEAL_INTERVAL == 0) {
            this.healNearbyBees();
        }
        if (this.age >= EXPLODE_AFTER) {
            this.explode();
        }
    }

    /** 为周围（8 格内）的皇家侍从 Bee 回复 5 点生命 */
    private void healNearbyBees() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        for (RoyalServantBee bee : this.level.getEntitiesOfClass(RoyalServantBee.class,
                AABB.ofSize(this.worldPosition.getCenter(), HEAL_RANGE * 2.0D, HEAL_RANGE * 2.0D,
                        HEAL_RANGE * 2.0D),
                RoyalServantBee::isAlive)) {
            bee.heal(HEAL_PER_TICK_BATCH);
        }
    }

    /** 生成一朵凋零雾：每秒 5 凋灵伤害 + 凋灵 II 3 秒 */
    private void spawnWitherFog() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        AreaEffectCloud cloud = new AreaEffectCloud(this.level, this.worldPosition.getX() + 0.5D,
                this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D);
        cloud.setRadius(FOG_RADIUS);
        cloud.setDuration(FOG_DURATION);
        cloud.setWaitTime(0);
        cloud.setParticle(ParticleTypes.SMOKE);
        cloud.addEffect(new MobEffectInstance(CMMobEffects.WITHER_CLOUD.get(), FOG_DURATION, 0));
        cloud.addEffect(new MobEffectInstance(MobEffects.WITHER, WITHER_DURATION, WITHER_AMPLIFIER));
        this.level.addFreshEntity(cloud);
    }

    /** 30 秒到点爆炸：范围凋零伤害 + 爆炸特效，然后方块消失 */
    private void explode() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        BlockPos pos = this.worldPosition;
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class,
                AABB.ofSize(pos.getCenter(), EXPLODE_RANGE * 2.0D, EXPLODE_RANGE * 2.0D, EXPLODE_RANGE * 2.0D),
                LivingEntity::isAlive)) {
            entity.hurt(entity.damageSources().wither(), EXPLODE_DAMAGE);
        }
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.getX() + 0.5D, pos.getY() + 0.5D,
                    pos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
        this.level.removeBlock(pos, false);
    }
}
