package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import com.magicbee.ctnhmana.common.entity.RoyalServantBee;

import java.util.EnumSet;

/**
 * 侍从冲锋模式：不顾一切地直线冲向目标（无视阻挡，方块会被碰撞箱破坏机制清开）。
 * 血量低于 {@link RoyalServantBee} 自爆阈值时加速，贴近目标自爆；
 * 撞到目标时每 0.5 秒造成一次近战伤害（攻击力 5）。
 */
public class RoyalServantChargeGoal extends Goal {

    private static final double SELF_DESTRUCT_RANGE = 2.5D;
    private static final int ATTACK_COOLDOWN = 10;

    private final RoyalServantBee bee;
    private int attackCooldown;

    public RoyalServantChargeGoal(RoyalServantBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.bee.getChargeTarget() != null && !this.bee.isDashing();
    }

    @Override
    public boolean canContinueToUse() {
        return this.bee.getChargeTarget() != null && this.bee.isAlive() && !this.bee.isDashing();
    }

    @Override
    public void stop() {
        this.attackCooldown = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.bee.getChargeTarget();
        if (target == null) {
            return;
        }
        // 低血：贴近目标自爆
        if (this.bee.getHealth() < RoyalServantBee.SELF_DESTRUCT_HEALTH &&
                this.bee.distanceToSqr(target) < SELF_DESTRUCT_RANGE * SELF_DESTRUCT_RANGE) {
            this.bee.selfDestruct();
            return;
        }
        // 撞到目标造成近战伤害
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        } else if (this.bee.getBoundingBox().inflate(0.5D).intersects(target.getBoundingBox())) {
            this.bee.doHurtTarget(target);
            this.attackCooldown = ATTACK_COOLDOWN;
        }
        // 直线冲锋（朝目标眼睛位置），并直接转身面朝目标
        this.bee.flyStraightTo(target.getEyePosition(), this.bee.getChargeSpeed());
        this.bee.faceEntity(target);
    }
}
