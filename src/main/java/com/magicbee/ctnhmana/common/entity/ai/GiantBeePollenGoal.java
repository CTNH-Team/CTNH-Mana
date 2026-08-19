package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

import com.magicbee.ctnhmana.common.entity.GiantBee;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;

import java.util.EnumSet;

/**
 * 巨蜂授粉循环：每 2 分钟找最近的产魔花飞过去（没找到 10 秒后重试），
 * 到达或 30 秒后先在花上方悬停一会，再撒下花粉，3×3×3 内产魔花 +500 mana。
 */
public class GiantBeePollenGoal extends Goal {

    /** 授粉周期：2 分钟 */
    public static final int POLLEN_INTERVAL = 2400;
    /** 周围没有产魔花时重试间隔：10 秒 */
    private static final int NO_FLOWER_RETRY = 200;
    /** 飞行寻花超时：30 秒 */
    private static final int SEEK_TIMEOUT = 600;
    /** 到达后悬停时间（tick），悬停完再撒粉，过渡更平滑 */
    private static final int HOVER_TICKS = 30;
    /** 授粉后短暂不被花吸引（tick），让巨蜂先飞开漫游 */
    private static final int ATTRACTION_REST = 200;
    private static final double SEEK_RANGE = 48.0D;
    private static final double ARRIVE_DISTANCE = 2.5D;
    private static final int MANA_PER_FLOWER = 500;

    private final GiantBee bee;
    private BlockPos targetFlower;
    private int seekTicks;
    private int hoverTicks;
    private boolean done;

    public GiantBeePollenGoal(GiantBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.bee.pollenCooldown <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.done;
    }

    @Override
    public void start() {
        this.done = false;
        this.seekTicks = 0;
        this.hoverTicks = 0;
        BlockPos nearest = findNearestGeneratingFlower(this.bee.level(), this.bee.blockPosition(), (int) SEEK_RANGE);
        if (nearest == null) {
            // 周围没有产魔花，10 秒后重试
            this.bee.pollenCooldown = NO_FLOWER_RETRY;
            this.done = true;
            return;
        }
        this.targetFlower = nearest;
    }

    @Override
    public void stop() {
        this.bee.getNavigation().stop();
        this.targetFlower = null;
    }

    @Override
    public void tick() {
        if (this.done || this.targetFlower == null) {
            return;
        }
        double x = this.targetFlower.getX() + 0.5D;
        double y = this.targetFlower.getY() + 1.0D;
        double z = this.targetFlower.getZ() + 0.5D;
        if (this.hoverTicks > 0) {
            // 到达后悬停：原地不动，悬停结束再撒粉
            this.hoverTicks--;
            if (this.hoverTicks <= 0) {
                this.burstPollen();
            }
            return;
        }
        this.seekTicks++;
        this.bee.getNavigation().moveTo(x, y, z, 1.0D);
        if (this.bee.distanceToSqr(x, y, z) <= ARRIVE_DISTANCE * ARRIVE_DISTANCE || this.seekTicks >= SEEK_TIMEOUT) {
            this.bee.getNavigation().stop();
            this.hoverTicks = HOVER_TICKS;
        }
    }

    /** 撒花粉：植物催生粒子 + 3×3×3 内产魔花 +500 mana */
    private void burstPollen() {
        Level level = this.bee.level();
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.bee.getX(), this.bee.getY() - 1.0D,
                    this.bee.getZ(), 24, 2.0D, 0.5D, 2.0D, 0.1D);
        }
        if (this.targetFlower != null) {
            BlockPos.betweenClosed(this.targetFlower.offset(-1, -1, -1), this.targetFlower.offset(1, 1, 1))
                    .forEach(pos -> {
                        if (level.getBlockEntity(pos) instanceof GeneratingFlowerBlockEntity flower) {
                            flower.addMana(MANA_PER_FLOWER);
                        }
                    });
        }
        this.bee.pollenCooldown = POLLEN_INTERVAL;
        this.bee.attractionCooldown = ATTRACTION_REST;
        this.done = true;
    }

    /** 按区块遍历方块实体，找最近的产魔花 */
    private static BlockPos findNearestGeneratingFlower(Level level, BlockPos center, int radius) {
        BlockPos nearest = null;
        double best = Double.MAX_VALUE;
        int minX = center.getX() - radius;
        int maxX = center.getX() + radius;
        int minZ = center.getZ() - radius;
        int maxZ = center.getZ() + radius;
        for (int chunkX = minX >> 4; chunkX <= maxX >> 4; chunkX++) {
            for (int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; chunkZ++) {
                LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    if (blockEntity instanceof GeneratingFlowerBlockEntity) {
                        BlockPos pos = blockEntity.getBlockPos();
                        if (Math.abs(pos.getX() - center.getX()) <= radius &&
                                Math.abs(pos.getZ() - center.getZ()) <= radius &&
                                Math.abs(pos.getY() - center.getY()) <= radius * 2) {
                            double dist = pos.distToCenterSqr(center.getX() + 0.5D, center.getY(),
                                    center.getZ() + 0.5D);
                            if (dist < best) {
                                best = dist;
                                nearest = pos;
                            }
                        }
                    }
                }
            }
        }
        return nearest;
    }
}
