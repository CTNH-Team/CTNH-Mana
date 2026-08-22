package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.magicbee.ctnhmana.common.entity.projectile.BeeNukeProjectile;
import com.magicbee.ctnhmana.registry.CMEntities;
import com.magicbee.ctnhmana.registry.CMMobEffects;

import java.util.EnumSet;

/**
 * 巨蜂第三阶段（P3）goal：进入后按「失明攻击 → 飞天核弹」无限循环，直到被杀。
 * <ul>
 * <li>失明攻击子阶段（30 秒）：全程给玩家失明 I（持续刷新），大蜜蜂自身获得发光标记；随后重复「停顿1s → 朝玩家冲刺15格（冲刺前原地生凋零云5秒，冲刺后给2秒缚地）→ 8向滞留药水」。此阶段玩家离 boss 超 30
 * 格会被传送到面前并获 30 秒缚地。</li>
 * <li>飞天子阶段：蜜蜂悬停并保持在玩家上方；飞天 + 16向滞留药水 + 警告；追踪玩家每5秒召2只蜜蜂持续10秒；再16向药水+缚地+警告；3秒后朝玩家抛射蜜蜂核弹（命中 25 半径凋零伤害 +
 * 蜜蜡实心块）；停5秒后切回失明攻击。此阶段不传送玩家。转化为飞天模式时解除玩家失明与大蜜蜂发光标记。</li>
 * </ul>
 */
public class GiantBeePhase3Goal extends Goal {

    /** 失明攻击子阶段总时长（30 秒） */
    private static final int BLIND_COMBO_DURATION = 600;
    /** 失明攻击循环单元：停顿 1 秒（20 tick） */
    private static final int PAUSE_TICKS = 20;
    /** 冲刺距离/速度（15 格） */
    private static final double CHARGE_DISTANCE = 20.0D;
    private static final double CHARGE_SPEED = 1.2D;
    /** 凋零云持续 5 秒（100 tick） */
    private static final int CLOUD_DURATION = 100;
    /** 冲刺后赋予的缚地时长（2 秒） */
    private static final int ROOTED_AFTER_CHARGE = 40;
    /** 飞天阶段：追踪召唤 10 秒，每 5 秒召 2 只 */
    private static final int CHASE_TRACK_TICKS = 200;
    private static final int SUMMON_INTERVAL = 100;
    private static final int SUMMON_MAX = 5;
    private static final int SUMMON_COUNT = 2;
    /** 狂暴时技能 cd 等效加速（tick，1 秒） */
    private static final int RAGE_CD_REDUCE = 20;
    /** 陨石前计时（3 秒） */
    private static final int METEOR_DELAY_TICKS = 60;
    /** 陨石后停顿 5 秒（100 tick） */
    private static final int AFTER_TICKS = 100;
    /** 核弹阶段蜜蜂悬停高度：保持玩家头顶上方该高度（格），初始向上飞约 50 格再保持 40 格以上 */
    private static final double HOVER_HEIGHT = 40.0D;

    private final GiantBee bee;
    private boolean inCombos;      // 失明攻击子阶段
    private int tick;
    private int unitTicks;         // 当前循环单元计时
    private Stage stage;
    private Vec3 chargeDir;
    private boolean chargeWasSet;
    private int summonCooldown;

    private enum Stage {
        PAUSE,
        CHARGE,
        POTIONS
    }

    public GiantBeePhase3Goal(GiantBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.bee.isPhase3() && this.bee.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.bee.isPhase3() && this.bee.isAlive();
    }

    @Override
    public void start() {
        this.inCombos = true;
        this.bee.setP3BlindComboActive(true);
        this.tick = 0;
        this.unitTicks = 0;
        this.stage = Stage.PAUSE;
        this.chargeWasSet = false;
        this.summonCooldown = SUMMON_INTERVAL;
    }

    @Override
    public void stop() {
        this.bee.setP3BlindComboActive(false);
        this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
    }

    @Override
    public void tick() {
        if (this.inCombos) {
            this.tickBlindCombos();
            // 传送仅在黑暗（失明攻击）阶段生效，核弹阶段不再传送玩家
            this.tickTeleportCheck();
        } else {
            this.tickFlyingMeteor();
        }
    }

    /** 玩家与 boss 超过该距离（格）则传送 */
    private static final double TELEPORT_DISTANCE = 30.0D;
    private static final int TELEPORT_ROOT_DURATION = 600;
    /** 传送冷却（tick） */
    private static final int TELEPORT_COOLDOWN = 40;

    private int teleportCooldown;

    private void tickTeleportCheck() {
        if (this.teleportCooldown > 0) {
            this.teleportCooldown--;
        }
        if (this.teleportCooldown > 0 || this.bee.level().isClientSide) {
            return;
        }
        Player closest = this.bee.level().getNearestPlayer(this.bee, 128.0D);
        if (closest != null && closest.distanceTo(this.bee) > TELEPORT_DISTANCE) {
            Vec3 front = this.bee.getLookAngle().normalize();
            Vec3 pos = this.bee.position().add(front.x * 2.0D, 1.0D, front.z * 2.0D);
            closest.teleportTo(pos.x, pos.y, pos.z);
            closest.addEffect(new MobEffectInstance(CMMobEffects.ROOTED.get(),
                    TELEPORT_ROOT_DURATION, 0, false, true));
            this.teleportCooldown = TELEPORT_COOLDOWN;
        }
    }

    // ---------- 失明攻击子阶段 ----------

    private void tickBlindCombos() {
        LivingEntity target = this.bee.getTarget();
        if (target == null || !target.isAlive()) {
            this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
            return;
        }
        // 失明攻击子阶段全程：给玩家失明I（amplifier 0），持续刷新
        Player p = this.bee.level().getNearestPlayer(this.bee, 64.0D);
        if (p != null) {
            p.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0));
        }
        // 失明攻击子阶段全程：大蜜蜂自身获得发光标记（宝箱显形描边，随阶段结束消失）
        this.bee.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 0, false, false));
        this.tick++;
        if (this.tick >= BLIND_COMBO_DURATION) {
            // 30 秒结束：切飞天陨石子阶段，并解除玩家失明与大蜜蜂发光标记
            this.inCombos = false;
            this.bee.setP3BlindComboActive(false);
            this.tick = 0;
            this.bee.removeEffect(MobEffects.GLOWING);
            if (p != null) {
                p.removeEffect(MobEffects.BLINDNESS);
            }
            this.startFlyingPhase();
            return;
        }
        // 朝玩家上方瞄准（头顶+2）
        Vec3 aim = new Vec3(target.getX(), target.getY() + target.getBbHeight() + 2.0D, target.getZ());
        this.bee.faceEntity(target);
        switch (this.stage) {
            case PAUSE -> {
                // 停顿 1 秒
                this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
                if (++this.unitTicks >= PAUSE_TICKS) {
                    // 冲刺前原地生成凋零云 5 秒
                    this.bee.spawnWitherCloudAt(this.bee.blockPosition(), CLOUD_DURATION);
                    // 锁定冲刺方向
                    Vec3 to = aim.subtract(this.bee.position());
                    this.chargeDir = to.normalize();
                    this.chargeWasSet = true;
                    this.stage = Stage.CHARGE;
                    this.unitTicks = 0;
                }
            }
            case CHARGE -> {
                // 朝锁定方向冲刺 15 格
                if (this.chargeWasSet) {
                    this.bee.setDeltaMovement(this.chargeDir.scale(CHARGE_SPEED));
                    this.bee.destroyTouchedBlocks(1.5D);
                    this.unitTicks++;
                    if (this.unitTicks >= Math.ceil(CHARGE_DISTANCE / CHARGE_SPEED)) {
                        this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
                        // 冲刺后给玩家 2 秒缚地
                        Player targetPlayer = this.bee.level().getNearestPlayer(this.bee, 64.0D);
                        if (targetPlayer != null) {
                            targetPlayer.addEffect(new MobEffectInstance(CMMobEffects.ROOTED.get(),
                                    ROOTED_AFTER_CHARGE, 0, false, true));
                        }
                        this.stage = Stage.POTIONS;
                        this.unitTicks = 0;
                        this.chargeWasSet = false;
                    }
                } else {
                    // 安全后退到 PAUSE
                    this.stage = Stage.PAUSE;
                    this.unitTicks = 0;
                }
            }
            case POTIONS -> {
                // 8 方向滞留药水
                this.bee.throwPotionDirections(8);
                this.stage = Stage.PAUSE;
                this.unitTicks = 0;
            }
            default -> this.stage = Stage.PAUSE;
        }
    }

    // ---------- 飞天陨石子阶段 ----------

    private int subStageTick;
    private int subStage;   // 0=第一次飞天 1=追踪召唤 2=第二次飞天+锚 3=核弹前 4=核弹后缓冲 5=停顿

    private void startFlyingPhase() {
        this.subStageTick = 0;
        this.subStage = 0;
        // 第一次飞天：快速向上攀升（约 50 格），随后切换悬停保持在玩家 40 格以上
        this.bee.setDeltaMovement(0.0D, 2.0D, 0.0D);
        this.bee.throwPotionDirections(16);
        this.bee.warnPlayer("究极蜜蜂核弹已经部署");
    }

    private void tickFlyingMeteor() {
        Player p = this.bee.level().getNearestPlayer(this.bee, 64.0D);
        this.subStageTick++;
        // 整个核弹阶段：蜜蜂悬停并保持在玩家上方（头顶 HOVER_HEIGHT 格），跟随玩家移动
        // 垂直方向用更快速度快速攀升，确保保持 40 格以上高度
        if (p != null) {
            Vec3 hover = new Vec3(p.getX(), p.getY() + p.getBbHeight() + HOVER_HEIGHT, p.getZ());
            Vec3 to = hover.subtract(this.bee.position());
            this.bee.setDeltaMovement(Mth.clamp(to.x, -0.4D, 0.4D), Mth.clamp(to.y, -0.8D, 0.8D),
                    Mth.clamp(to.z, -0.4D, 0.4D));
            this.bee.faceEntity(p);
        }
        switch (this.subStage) {
            case 0 -> {
                // 就位阶段：40 tick 后进入追踪召唤（悬停已由上方 hover 接管）
                if (this.subStageTick >= 40) {
                    this.subStage = 1;
                    this.subStageTick = 0;
                    this.summonCooldown = SUMMON_INTERVAL;
                }
            }
            case 1 -> {
                // 追踪玩家期间每 5 秒召 2 只，持续 10 秒（悬停由上方 hover 统一处理）
                LivingEntity target = this.bee.getTarget();
                if (target != null && target.isAlive()) {
                    this.summonCooldown -= this.bee.isRageMode() ? RAGE_CD_REDUCE : 1;
                    if (this.summonCooldown <= 0) {
                        this.summonCooldown = SUMMON_INTERVAL;
                        this.bee.summonServantCount(SUMMON_MAX, SUMMON_COUNT);
                    }
                }
                if (this.subStageTick >= CHASE_TRACK_TICKS) {
                    this.subStage = 2;
                    this.subStageTick = 0;
                }
            }
            case 2 -> {
                // 第二次信号：16 向药水 + 警告 + 缚地（悬停跟随玩家）
                this.bee.throwPotionDirections(16);
                this.bee.warnPlayer("警告：究极蜜蜂核弹即将发射！");
                if (p != null) {
                    p.addEffect(new MobEffectInstance(CMMobEffects.ROOTED.get(), 200, 0, false, true));
                }
                this.subStage = 3;
                this.subStageTick = 0;
            }
            case 3 -> {
                // 3 秒后朝玩家抛射蜜蜂核弹（替换原陨石大招），期间持续悬停
                if (this.subStageTick >= METEOR_DELAY_TICKS) {
                    Level level = this.bee.level();
                    if (!level.isClientSide) {
                        BeeNukeProjectile nuke = new BeeNukeProjectile(CMEntities.BEE_NUKE_PROJECTILE.get(), level);
                        nuke.setOwner(this.bee);
                        LivingEntity target = this.bee.getTarget();
                        Vec3 aimDir;
                        if (target != null && target.isAlive()) {
                            aimDir = new Vec3(target.getX(), target.getY() + target.getBbHeight() + 2.0D,
                                    target.getZ()).subtract(this.bee.position()).normalize();
                        } else {
                            aimDir = this.bee.getLookAngle().normalize();
                        }
                        nuke.moveTo(this.bee.getX(), this.bee.getY() + this.bee.getBbHeight() * 0.5D,
                                this.bee.getZ());
                        nuke.shoot(aimDir.x, aimDir.y, aimDir.z, 1.6F, 0.2F);
                        level.addFreshEntity(nuke);
                    }
                    this.subStage = 4;
                    this.subStageTick = 0;
                }
            }
            case 4 -> {
                // 核弹已抛出，短暂缓冲后进入停顿
                if (this.subStageTick >= 20) {
                    this.subStage = 5;
                    this.subStageTick = 0;
                }
            }
            case 5 -> {
                // 停顿 5 秒（持续悬停），然后切回失明攻击
                if (this.subStageTick >= AFTER_TICKS) {
                    // 循环计数 +1（解除锁血），切回失明攻击
                    this.bee.incrementP3Cycle();
                    this.inCombos = true;
                    this.bee.setP3BlindComboActive(true);
                    this.tick = 0;
                    this.unitTicks = 0;
                    this.stage = Stage.PAUSE;
                    this.chargeWasSet = false;
                }
            }
            default -> {}
        }
    }
}
