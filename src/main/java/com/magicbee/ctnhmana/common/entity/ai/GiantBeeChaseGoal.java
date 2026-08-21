package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.magicbee.ctnhmana.registry.CMMobEffects;

import java.util.EnumSet;
import java.util.List;

/**
 * 巨蜂追缉模式（boss 战后半段）：进入时立即向玩家头顶直线冲刺，随后持续追击（速度 0.3）。
 * 每 5 秒二选一发动技能：①停顿 0.5 秒后锁定玩家位置冲刺，落点生成恶意凋零菟葵；
 * ②向面朝方向发射 10 支剧毒箭并扔一瓶失明 III 溅射药水。
 * 玩家距离 ≥25 格时：传送玩家到自身面前并赋予 30 秒缚地。
 * 追缉持续 45 秒后：回复 25 点生命并恢复巡空模式。
 */
public class GiantBeeChaseGoal extends Goal {

    /** 追缉持续时间（tick，45 秒） */
    private static final int CHASE_DURATION = 600;
    /** 追缉结束时恢复的生命 */
    private static final float RESTORE_HEALTH = 25.0F;
    /** 追缉开始时召唤 4 只皇家蜜蜂的上限 */
    private static final int MAX_CHASE_SERVANTS = 5;

    /** 进入时冲锋：直接锁定玩家位置冲撞 */
    private static final double OPENING_CHARGE_SPEED = 1.4D;
    /** 追击速度（P 控制，0.3 格/tick） */
    private static final double CHASE_SPEED = 0.3D;
    /** 技能间隔：5 秒 */
    private static final int SKILL_INTERVAL = 100;
    /** 技能 ① 停顿 1 秒后锁定玩家位置高速冲刺（仿皇家蜜蜂：固定方向直线冲） */
    private static final int SKILL_PAUSE_TICKS = 10;
    private static final double SKILL_CHARGE_SPEED = 1.5D;
    /** 冲刺固定 tick 数（皇家蜜蜂式，不看距离） */
    private static final int SKILL_CHARGE_TICKS = 15;
    /** 冲刺时的破坏半径（碰撞箱外扩格数，需 ≥ 单 tick 位移以免撞墙受阻） */
    private static final double SKILL_CHARGE_BREAK = 2.D;
    /** 冲刺到达后的 AOE 伤害（范围 4 格，凋零伤害） */
    private static final double SKILL_AOE_RANGE = 4.0D;
    private static final float SKILL_AOE_DAMAGE = 15.0F;
    /** 技能 ② 10 支剧毒箭 + 失明 III 溅射药水 */
    private static final int ARROW_COUNT = 10;
    private static final float ARROW_SPEED = 1.8F;
    private static final int POISON_DURATION = 100;
    private static final int BLIND_DURATION = 600;
    private static final int BLIND_AMPLIFIER = 2;
    /** 玩家距离 ≥25 格：传送到自己面前并缚地 30 秒 */
    private static final double TELEPORT_DISTANCE = 25.0D;
    private static final int ROOTED_DURATION = 600;
    /** 失明溅射药水 */
    private static final float POTION_SPEED = 0.8F;

    private final GiantBee bee;
    private boolean openingCharge;   // 进入时的冲锋
    private Vec3 openingAim;         // 冲锋锁定点（玩家头顶+2）
    private int skillCooldown;
    private SkillState skillState = SkillState.NONE;
    private int skillTicks;
    private Vec3 skillTarget;
    private Vec3 skillDir;   // 冲刺锁定方向（开始冲刺时固定，仿皇家蜜蜂）
    private int comboRemaining;   // 连招剩余轮数（停顿-冲锋-丢箭，重复 3 次）
    private int teleportCooldown;
    private int chaseTicks;   // 追缉已持续 tick

    private enum SkillState {
        NONE,
        PAUSING,
        CHARGING,
        FIRING
    }

    public GiantBeeChaseGoal(GiantBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // 转阶段期间不移动/不转向玩家
        return this.bee.isChasing() && !this.bee.isTransforming() && this.bee.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.bee.isChasing() && !this.bee.isTransforming() && this.bee.isAlive();
    }

    /** 追缉锁定高度偏移：朝玩家头顶上方格数，保证飞行单位冲锋不贴地 */
    private static final double TARGET_HEIGHT_OFFSET = 2.0D;

    /** 追缉瞄准点：玩家头顶上方 {@link #TARGET_HEIGHT_OFFSET} 格（飞行单位冲锋，保持高度） */
    private Vec3 aimPoint(LivingEntity target) {
        return new Vec3(target.getX(), target.getY() + target.getBbHeight() + TARGET_HEIGHT_OFFSET, target.getZ());
    }

    @Override
    public void start() {
        this.skillCooldown = SKILL_INTERVAL;
        this.skillState = SkillState.NONE;
        this.chaseTicks = 0;
        // 二阶段追缉开始：苦难护盾不足 3 则提升到 3，并立即召唤 4 只皇家蜜蜂（二阶段专属）
        if (this.bee.isPhase2()) {
            this.bee.ensurePainShield(2); // amplifier 2 = 3 级
            this.bee.summonServantCount(MAX_CHASE_SERVANTS, 4);
        }
        LivingEntity target = this.bee.getTarget();
        if (target != null && target.isAlive()) {
            this.openingCharge = true;
            // 直接锁定玩家头顶+2 的位置，不再用固定方向，避免方向漂移
            this.openingAim = this.aimPoint(target);
        } else {
            this.openingCharge = false;
        }
    }

    @Override
    public void stop() {
        this.skillState = SkillState.NONE;
    }

    @Override
    public void tick() {
        LivingEntity target = this.bee.getTarget();
        if (target == null || !target.isAlive()) {
            this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
            return;
        }
        // 玩家距离 ≥25 格：传送到面前并缚地 30 秒
        if (this.teleportCooldown > 0) {
            this.teleportCooldown--;
        }
        if (target instanceof Player player && player.distanceTo(this.bee) >= TELEPORT_DISTANCE &&
                this.teleportCooldown <= 0) {
            this.teleportPlayer(player);
            this.teleportCooldown = 40; // 2 秒冷却
        }
        // 近身近战攻击
        if (this.bee.distanceToSqr(target) < 4.0D) {
            this.bee.doHurtTarget(target);
        }
        // 追缉 45 秒后：恢复 25 血并切回巡空
        if (++this.chaseTicks >= CHASE_DURATION) {
            this.bee.exitChaseMode();
            return;
        }
        // 每 5 秒技能：二阶段为连招 3 次（停顿-冲锋-丢箭），一阶段为单次二选一（停顿-冲锋 或 停顿-箭）
        if (this.skillState == SkillState.NONE && --this.skillCooldown <= 0) {
            this.skillCooldown = SKILL_INTERVAL;
            this.startCombo(target);
        }
        // 技能状态机
        if (this.skillState != SkillState.NONE) {
            this.tickSkillState();
            // 技能期间也持续面朝玩家
            this.bee.faceEntity(target);
            return;
        }
        if (this.openingCharge) {
            // 进入时的冲锋：直接朝锁定点直线飞冲，无减速/惯性，到位即止
            Vec3 to = this.openingAim.subtract(this.bee.position());
            double dist = to.length();
            if (dist >= 2.0D && Double.isFinite(dist) && dist > 0.01D) {
                this.bee.setDeltaMovement(to.scale(OPENING_CHARGE_SPEED / dist));
                this.bee.destroyTouchedBlocks(1.5D); // 顺路清障，避免撞墙回弹
            } else {
                this.openingCharge = false; // 已到位
                this.bee.setDeltaMovement(Vec3.ZERO);
            }
        } else {
            // 持续追击玩家上方 2 格（P 控制，避免贴地）
            Vec3 aim = this.aimPoint(target);
            Vec3 to = aim.subtract(this.bee.position());
            this.bee.setDeltaMovement(Math.copySign(Math.min(Math.abs(to.x), CHASE_SPEED), to.x),
                    Math.copySign(Math.min(Math.abs(to.y), CHASE_SPEED), to.y),
                    Math.copySign(Math.min(Math.abs(to.z), CHASE_SPEED), to.z));
        }
        this.bee.faceEntity(target);
    }

    // ---------- 玩家传送 ----------

    /** 传送到 boss 面前（上方 1 格）并赋予 30 秒缚地 */
    private void teleportPlayer(Player player) {
        if (this.bee.level().isClientSide) {
            return;
        }
        Vec3 front = this.bee.getLookAngle().normalize();
        Vec3 pos = this.bee.position().add(front.x * 2.0D, 1.0D, front.z * 2.0D);
        player.teleportTo(pos.x, pos.y, pos.z);
        player.addEffect(new MobEffectInstance(CMMobEffects.ROOTED.get(), ROOTED_DURATION, 0, false, true));
    }

    // ---------- 技能 ① 停顿后向锁定位置冲锋（不放菟葵） ----------

    /** 发起技能：二阶段 = 停顿-冲锋-丢箭连招 3 次；一阶段 = 二选一单次（停顿-冲锋 或 停顿-箭） */
    private void startCombo(LivingEntity target) {
        this.skillTarget = this.aimPoint(target);
        this.comboRemaining = this.bee.isPhase2() ? 3 : 1;
        // 一阶段：随机决定本轮是冲锋还是箭；二阶段每轮固定冲锋+丢箭
        if (!this.bee.isPhase2() && this.bee.getRandom().nextBoolean()) {
            this.comboRemaining = 0; // 标记仅箭
            this.skillState = SkillState.PAUSING;
            this.skillTicks = SKILL_PAUSE_TICKS;
            return;
        }
        this.startComboPause();
    }

    private void startComboPause() {
        this.skillState = SkillState.PAUSING;
        this.skillTicks = SKILL_PAUSE_TICKS;
        this.bee.commandServantsToDash(2); // 停顿期间皇家蜜蜂连冲 2 次
    }

    private void tickSkillState() {
        switch (this.skillState) {
            case PAUSING -> {
                // 停在原地蓄力，同时面朝目标
                this.bee.setDeltaMovement(Vec3.ZERO);
                this.bee.faceEntity(this.bee.getTarget());
                if (--this.skillTicks <= 0) {
                    if (this.comboRemaining <= 0) {
                        // 一阶段"仅箭"：停顿结束丢箭
                        this.skillState = SkillState.NONE;
                        this.fireArrowSkill();
                    } else {
                        // 开始冲刺：按锁定方向直线冲（仿皇家蜜蜂，方向固定不变）
                        Vec3 to = this.skillTarget.subtract(this.bee.position());
                        if (to.lengthSqr() < 0.0001D) {
                            // 目标过近，退化为原地 AOE
                            this.skillState = SkillState.NONE;
                            this.dealAoE();
                        } else {
                            this.skillDir = to.normalize();
                            this.skillState = SkillState.CHARGING;
                            this.skillTicks = SKILL_CHARGE_TICKS; // 冲固定 tick
                        }
                    }
                }
            }
            case CHARGING -> {
                // 沿锁定方向直线高速冲刺，每 tick 破坏清障（仿皇家蜜蜂冲刺）
                this.bee.destroyTouchedBlocks(SKILL_CHARGE_BREAK);
                this.bee.setDeltaMovement(this.skillDir.scale(SKILL_CHARGE_SPEED));
                if (--this.skillTicks <= 0) {
                    // 冲刺结束：停步 + AOE
                    this.bee.setDeltaMovement(Vec3.ZERO);
                    this.dealAoE();
                    // 二阶段：本轮冲锋后丢箭，并进入下一轮连招；一阶段：结束
                    if (this.bee.isPhase2()) {
                        this.fireArrowSkill();
                        this.comboRemaining--;
                        if (this.comboRemaining > 0) {
                            this.startComboPause();
                        } else {
                            this.skillState = SkillState.NONE;
                        }
                    } else {
                        this.skillState = SkillState.NONE;
                    }
                }
            }
            default -> {}
        }
    }

    /** 冲刺到达后对周围造成凋零范围伤害 */
    private void dealAoE() {
        Level level = this.bee.level();
        if (level.isClientSide) {
            return;
        }
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class,
                this.bee.getBoundingBox().inflate(SKILL_AOE_RANGE),
                e -> e.isAlive() && e != this.bee)) {
            entity.hurt(entity.damageSources().wither(), SKILL_AOE_DAMAGE);
        }
    }

    // ---------- 技能 ② 停顿结束后：10 支剧毒箭 + 失明 III 溅射药水 ----------

    /** 停顿结束后发射箭雨 + 失明药水 */
    private void fireArrowSkill() {
        Level level = this.bee.level();
        if (level.isClientSide) {
            return;
        }
        // 朝面朝方向发射 10 支剧毒箭（沿 look 方向小散射）
        Vec3 look = this.bee.getLookAngle().normalize();
        for (int i = 0; i < ARROW_COUNT; i++) {
            double spreadX = (this.bee.getRandom().nextDouble() - 0.5D) * 0.4D;
            double spreadY = (this.bee.getRandom().nextDouble() - 0.5D) * 0.4D;
            Arrow arrow = new Arrow(level, this.bee);
            arrow.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, 1));
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            arrow.shoot(look.x + spreadX, look.y + spreadY, look.z, ARROW_SPEED, 1.0F);
            level.addFreshEntity(arrow);
        }
        // 扔一瓶失明 III 溅射药水
        ThrownPotion potion = new ThrownPotion(level, this.bee);
        ItemStack stack = new ItemStack(Items.SPLASH_POTION);
        PotionUtils.setCustomEffects(stack,
                List.of(new MobEffectInstance(MobEffects.BLINDNESS, BLIND_DURATION, BLIND_AMPLIFIER)));
        potion.setItem(stack);
        potion.shoot(look.x, look.y, look.z, POTION_SPEED, 0.0F);
        level.addFreshEntity(potion);
    }
}