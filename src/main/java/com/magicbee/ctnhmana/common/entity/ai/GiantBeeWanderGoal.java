package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.common.entity.GiantBee;

import java.util.EnumSet;

/**
 * 巨蜂漫游：移植自原版 Bee$BeeWanderGoal（去掉蜂巢逻辑）。
 * 悬停 → 面朝方向随机找 24 格内空气点飞过去 → 路径结束停下 → 10% 概率再出发。
 * 抽点位时用 AABB 相交检测直接排除与自身碰撞箱重叠的点位，目标点要求 3×3×5 无碰撞，
 * 路线交给寻路器（不逐节点校验，避免过度拒绝导致无法漫游）。
 */
public class GiantBeeWanderGoal extends Goal {

    /** 漫游半径/垂直范围按巨蜂体型放大（原版蜜蜂为 8/7） */
    private static final int WANDER_RADIUS = 16;
    private static final int WANDER_Y_RANGE = 12;
    /** 净空校验：碰撞箱 2.45×4.2 → 水平 ±1（3 格宽）、垂直 5 格高 */
    private static final int CLEARANCE_RADIUS = 1;
    private static final int CLEARANCE_HEIGHT = 4;

    private final GiantBee bee;

    public GiantBeeWanderGoal(GiantBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.bee.getNavigation().isDone() && this.bee.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.bee.getNavigation().isInProgress();
    }

    @Override
    public void start() {
        Path path = this.findPath();
        if (path != null) {
            this.bee.getNavigation().moveTo(path, 1.0D);
        }
    }

    /** 抽点位：排除与自身碰撞箱重叠或净空不足的点位，寻路成功即接受 */
    private Path findPath() {
        Level level = this.bee.level();
        AABB selfBox = this.bee.getBoundingBox();
        Vec3 viewVector = this.bee.getViewVector(0.0F);
        for (int i = 0; i < 33; i++) {
            Vec3 pos = HoverRandomPos.getPos(this.bee, WANDER_RADIUS, WANDER_Y_RANGE, viewVector.x, viewVector.z,
                    (float) (Math.PI / 2), 3, 1);
            if (pos == null) {
                continue;
            }
            BlockPos target = BlockPos.containing(pos);
            // 抽点位时直接排除与自身碰撞箱重叠的点位
            if (overlapsSelf(target, selfBox)) {
                continue;
            }
            // 目标点自身净空：3×3×5 无碰撞
            if (!isBoxClear(level, target)) {
                continue;
            }
            Path path = this.bee.getNavigation().createPath(target, 1);
            if (path != null) {
                return path;
            }
        }
        Vec3 fallback = AirAndWaterRandomPos.getPos(this.bee, WANDER_RADIUS, 8, -2, viewVector.x, viewVector.z,
                (double) (Math.PI / 2));
        if (fallback != null) {
            BlockPos target = BlockPos.containing(fallback);
            if (!overlapsSelf(target, selfBox) && isBoxClear(level, target)) {
                return this.bee.getNavigation().createPath(target, 1);
            }
        }
        return null;
    }

    /** 候选点的 3×3×5 净空盒是否与自身碰撞箱重叠 */
    private static boolean overlapsSelf(BlockPos target, AABB selfBox) {
        AABB box = new AABB(target.offset(-CLEARANCE_RADIUS, 0, -CLEARANCE_RADIUS),
                target.offset(CLEARANCE_RADIUS, CLEARANCE_HEIGHT - 1, CLEARANCE_RADIUS));
        return box.intersects(selfBox);
    }

    private static boolean isBoxClear(Level level, BlockPos pos) {
        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-CLEARANCE_RADIUS, 0, -CLEARANCE_RADIUS),
                pos.offset(CLEARANCE_RADIUS, CLEARANCE_HEIGHT - 1, CLEARANCE_RADIUS))) {
            BlockState state = level.getBlockState(p);
            if (!state.getCollisionShape(level, p).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
