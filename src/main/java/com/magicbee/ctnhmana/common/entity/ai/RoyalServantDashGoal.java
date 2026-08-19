package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;

import com.magicbee.ctnhmana.common.entity.RoyalServantBee;

import java.util.EnumSet;

/**
 * 侍从冲刺技能占位 goal：状态机（停顿 → 冲刺 → 速度 X）由实体
 * 推进，本 goal 只在技能期间持有 MOVE 标志，
 * 阻止冲锋 goal 干扰冲刺。
 */
public class RoyalServantDashGoal extends Goal {

    private final RoyalServantBee bee;

    public RoyalServantDashGoal(RoyalServantBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.bee.isDashing();
    }

    @Override
    public boolean canContinueToUse() {
        return this.bee.isDashing();
    }
}
