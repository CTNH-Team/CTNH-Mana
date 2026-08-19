package com.magicbee.ctnhmana.common.entity.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

/**
 * 狂暴飞行导航器：配合 {@link RampageNodeEvaluator} 无视一切方块阻挡寻路。
 * 巨蜂与皇家侍从 Bee 共用（它们移动会破坏方块，普通寻路会因阻挡而失败）。
 */
public class RampageFlyingPathNavigation extends FlyingPathNavigation {

    public RampageFlyingPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new RampageNodeEvaluator();
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        // 任何位置都是稳定目的地（方块会被破坏）
        return true;
    }
}
