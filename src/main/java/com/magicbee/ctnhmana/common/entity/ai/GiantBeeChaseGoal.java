package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.core.BlockPos;
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
import com.magicbee.ctnhmana.registry.CMBlocks;
import com.magicbee.ctnhmana.registry.CMMobEffects;

import java.util.EnumSet;
import java.util.List;

/**
 * 巨蜂追缉模式（boss 战后半段）：进入时立即向玩家冲锋（带惯性），随后持续追击（速度 0.3）。
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

    /** 进入时冲锋：直接锁定玩家位置冲撞（带惯性） */
    private static final double OPENING_CHARGE_SPEED = 1.4D;
    /** 惯性保留 tick 数 */
    private static final int INERTIA_TICKS = 32;
    /** 追击速度（P 控制，0.3 格/tick） */
    private static final double CHASE_SPEED = 0.3D;
    /** 技能间隔：5 秒 */
    private static final int SKILL_INTERVAL = 100;
    /** 技能 ① 停顿 1 秒（20 tick）后锁定玩家位置高速冲刺 20 格 */
    private static final int SKILL_PAUSE_TICKS = 10;
    private static final double SKILL_CHARGE_DISTANCE = 30.0D;
    private static final double SKILL_CHARGE_SPEED = 5.0D;
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
    private Vec3 chargeDir;
    private int inertiaTicks;
    private int skillCooldown;
    private SkillState skillState = SkillState.NONE;
    private int skillTicks;
    private Vec3 skillTarget;
    private boolean pendingDash;   // 停顿结束后走冲刺（true）还是箭雨（false）
    private int teleportCooldown;
    private int chaseTicks;   // 追缉已持续 tick

    private enum SkillState {
        NONE,
        PAUSING,
        CHARGING
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

    /** 追缉锁定高度偏移（玩家脚底上方格数，避免贴地追踪） */
    private static final double TARGET_HEIGHT_OFFSET = 2.0D;

    /** 追缉瞄准点：玩家位置上方 {@link #TARGET_HEIGHT_OFFSET} 格 */
    private Vec3 aimPoint(LivingEntity target) {
        return new Vec3(target.getX(), target.getY() + TARGET_HEIGHT_OFFSET, target.getZ());
    }

    @Override
    public void start() {
        this.skillCooldown = SKILL_INTERVAL;
        this.skillState = SkillState.NONE;
        this.inertiaTicks = INERTIA_TICKS;
        this.chaseTicks = 0;
        LivingEntity target = this.bee.getTarget();
        if (target != null && target.isAlive()) {
            this.openingCharge = true;
            Vec3 aim = this.aimPoint(target);
            this.chargeDir = aim.subtract(this.bee.position()).normalize();
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
        // 每 5 秒二选一技能
        if (this.skillState == SkillState.NONE && --this.skillCooldown <= 0) {
            this.skillCooldown = SKILL_INTERVAL;
            if (this.bee.getRandom().nextBoolean()) {
                this.startDashSkill(target);
            } else {
                this.startArrowSkill();
            }
        }
        // 技能状态机
        if (this.skillState != SkillState.NONE) {
            this.tickSkillState();
            // 技能期间也持续面朝玩家
            this.bee.faceEntity(target);
            return;
        }
        if (this.openingCharge) {
            // 进入时的冲锋：带惯性直到惯性结束
            this.bee.setDeltaMovement(this.chargeDir.scale(OPENING_CHARGE_SPEED));
            this.inertiaTicks--;
            if (this.inertiaTicks <= 0) {
                this.openingCharge = false;
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

    // ---------- 技能 ① 停顿后向锁定位置冲锋，落点生成恶意凋零菟葵 ----------

    private void startDashSkill(LivingEntity target) {
        this.skillState = SkillState.PAUSING;
        this.skillTicks = SKILL_PAUSE_TICKS;
        this.skillTarget = this.aimPoint(target);
        this.pendingDash = true;
    }

    private void tickSkillState() {
        switch (this.skillState) {
            case PAUSING -> {
                // 停在原地 1 秒（技能发动前蓄力；仅冲刺技能在停顿处生成菟葵）
                this.bee.setDeltaMovement(Vec3.ZERO);
                if (this.pendingDash) {
                    this.spawnRose();
                }
                if (--this.skillTicks <= 0) {
                    if (this.pendingDash) {
                        this.skillState = SkillState.CHARGING;
                        this.skillTicks = (int) Math.ceil(SKILL_CHARGE_DISTANCE / SKILL_CHARGE_SPEED); // 10 tick 冲 20 格
                    } else {
                        // 箭雨/药水技能：停顿结束后发射
                        this.skillState = SkillState.NONE;
                        this.fireArrowSkill();
                    }
                }
            }
            case CHARGING -> {
                // 朝锁定玩家位置高速冲刺 20 格，冲刺时额外破坏 1 格范围清障
                Vec3 to = this.skillTarget.subtract(this.bee.position());
                double dist = to.length();
                this.bee.setDeltaMovement(to.normalize().scale(SKILL_CHARGE_SPEED));
                this.bee.destroyTouchedBlocks(1.0D);
                if (--this.skillTicks <= 0 || dist >= SKILL_CHARGE_DISTANCE) {
                    this.bee.setDeltaMovement(Vec3.ZERO);
                    this.skillState = SkillState.NONE;
                    // 到达：AOE 伤害 + 落点生成菟葵
                    this.spawnRose();
                    this.dealAoE();
                }
            }
            default -> {}
        }
    }

    /** 在冲刺落点（boss 当前位置）生成一棵恶意凋零菟葵（悬浮方块） */
    private void spawnRose() {
        Level level = this.bee.level();
        if (level.isClientSide) {
            return;
        }
        BlockPos pos = this.bee.blockPosition();
        if (level.getBlockState(pos).canBeReplaced()) {
            level.setBlock(pos, CMBlocks.WITHER_ACONITE_TRAP.get().defaultBlockState(), 3);
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

    // ---------- 技能 ② 停顿 1 秒后：10 支剧毒箭 + 失明 III 溅射药水 ----------

    private void startArrowSkill() {
        this.skillState = SkillState.PAUSING;
        this.skillTicks = SKILL_PAUSE_TICKS;
        this.pendingDash = false;
    }

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
