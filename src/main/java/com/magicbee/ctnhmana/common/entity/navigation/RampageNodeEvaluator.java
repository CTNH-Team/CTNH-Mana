package com.magicbee.ctnhmana.common.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * 狂暴寻路节点评估器：所有位置一律视为可通行（{@link BlockPathTypes#OPEN}）。
 * 巨蜂/侍从移动时会摧毁碰撞箱方块，不需要被实心方块阻挡而寻路失败。
 */
public class RampageNodeEvaluator extends WalkNodeEvaluator {

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter level, int x, int y, int z, Mob mob) {
        return BlockPathTypes.OPEN;
    }
}
