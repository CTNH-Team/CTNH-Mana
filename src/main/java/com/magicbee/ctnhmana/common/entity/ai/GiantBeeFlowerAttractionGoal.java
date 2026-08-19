package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

import com.magicbee.ctnhmana.common.entity.GiantBee;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.mana.spark.ManaSpark;

import java.util.EnumSet;

/** 巨蜂中立行为：被拿三眼系列物品的玩家（优先）/产魔花/功能花/火花吸引，靠近后悬停；没有目标时交还给漫游 goal */
public class GiantBeeFlowerAttractionGoal extends Goal {

    /** 重新扫描间隔（tick） */
    private static final int SCAN_INTERVAL = 40;
    private static final double RANGE = 16.0D;
    private static final double HOVER_DISTANCE = 3.0D;

    private final GiantBee bee;
    private BlockPos target;
    private int scanTimer;

    public GiantBeeFlowerAttractionGoal(GiantBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // 授粉后短暂休息，不被花吸引，让巨蜂先飞开漫游
        if (this.bee.attractionCooldown > 0) {
            return false;
        }
        if (this.scanTimer-- > 0) {
            return this.target != null;
        }
        this.scanTimer = SCAN_INTERVAL;
        this.scanTarget();
        return this.target != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.scanTimer-- > 0) {
            return this.target != null;
        }
        this.scanTimer = SCAN_INTERVAL;
        this.scanTarget();
        return this.target != null;
    }

    @Override
    public void start() {
        this.scanTarget();
    }

    @Override
    public void stop() {
        this.bee.getNavigation().stop();
        this.target = null;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }
        double x = this.target.getX() + 0.5D;
        double y = this.target.getY() + 1.0D;
        double z = this.target.getZ() + 0.5D;
        if (this.bee.distanceToSqr(x, y, z) < HOVER_DISTANCE * HOVER_DISTANCE) {
            // 靠近后悬停
            this.bee.getNavigation().stop();
        } else {
            this.bee.getNavigation().moveTo(x, y, z, 1.0D);
        }
    }

    /** 扫描附近拿三眼物品的玩家（优先）/产魔花/功能花/火花，选最近的目标 */
    private void scanTarget() {
        Level level = this.bee.level();
        BlockPos center = this.bee.blockPosition();
        BlockPos eyePlayer = findNearestEyeHolder(level, center);
        if (eyePlayer != null) {
            // 拿着第三只眼系列的玩家优先吸引
            this.target = eyePlayer;
            return;
        }
        BlockPos nearest = findNearestFlower(level, center, (int) RANGE);
        BlockPos nearestSpark = findNearestSpark(level, center);
        if (nearestSpark != null && (nearest == null ||
                nearestSpark.distToCenterSqr(center.getX() + 0.5D, center.getY(), center.getZ() + 0.5D) < nearest
                        .distToCenterSqr(center.getX() + 0.5D, center.getY(), center.getZ() + 0.5D))) {
            nearest = nearestSpark;
        }
        this.target = nearest;
    }

    /** 找范围内主手/副手拿着第三只眼系列物品的玩家 */
    private static BlockPos findNearestEyeHolder(Level level, BlockPos center) {
        BlockPos nearest = null;
        double best = Double.MAX_VALUE;
        AABB area = new AABB(center).inflate(RANGE);
        for (Player player : level.getEntitiesOfClass(Player.class, area, p -> {
            ItemStack mainHand = p.getMainHandItem();
            ItemStack offHand = p.getOffhandItem();
            return GiantBee.isThirdEyeItem(mainHand) || GiantBee.isThirdEyeItem(offHand);
        })) {
            BlockPos pos = player.blockPosition();
            double dist = pos.distToCenterSqr(center.getX() + 0.5D, center.getY(), center.getZ() + 0.5D);
            if (dist < best) {
                best = dist;
                nearest = pos;
            }
        }
        return nearest;
    }

    /** 按区块遍历方块实体，避免逐格扫描 */
    private static BlockPos findNearestFlower(Level level, BlockPos center, int radius) {
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
                    if (blockEntity instanceof GeneratingFlowerBlockEntity ||
                            blockEntity instanceof FunctionalFlowerBlockEntity) {
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

    private static BlockPos findNearestSpark(Level level, BlockPos center) {
        BlockPos nearest = null;
        double best = Double.MAX_VALUE;
        AABB area = new AABB(center).inflate(RANGE);
        for (Entity entity : level.getEntitiesOfClass(Entity.class, area, e -> e instanceof ManaSpark)) {
            BlockPos pos = entity.blockPosition();
            double dist = pos.distToCenterSqr(center.getX() + 0.5D, center.getY(), center.getZ() + 0.5D);
            if (dist < best) {
                best = dist;
                nearest = pos;
            }
        }
        return nearest;
    }
}
