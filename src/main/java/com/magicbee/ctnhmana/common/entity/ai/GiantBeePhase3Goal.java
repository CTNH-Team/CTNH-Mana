package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.magicbee.ctnhmana.registry.CMMobEffects;

import java.util.EnumSet;

/**
 * 巨蜂第三阶段（P3）goal：进入后按「失明攻击 → 飞天陨石」无限循环，直到被杀。
 * <ul>
 * <li>失明攻击子阶段（30 秒）：全程给玩家失明 II（持续刷新），随后重复「停顿1s → 朝玩家冲刺15格（冲刺前原地生凋零云5秒，冲刺后给2秒缚地）→ 8向滞留药水」。</li>
 * <li>飞天子阶段：飞天 + 16向滞留药水 + 警告；追踪玩家每5秒召2只蜜蜂持续10秒；再飞天+16向药水+缚地+警告；3秒后召唤蜂蜜陨石砸玩家；停5秒后切回失明攻击。转化为陨石模式时解除玩家失明。</li>
 * <li>通用：三阶段全程玩家离 boss 超过 30 格时，传动到 boss 面前并获得 30 秒缚地。</li>
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
    /** 陨石前计时（3 秒） */
    private static final int METEOR_DELAY_TICKS = 60;
    /** 陨石后停顿 5 秒（100 tick） */
    private static final int AFTER_TICKS = 100;

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
        } else {
            this.tickFlyingMeteor();
        }
        // 三阶段通用：玩家离 boss 超过 30 格，传送到面前并给 30 秒缚地
        this.tickTeleportCheck();
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
        // 失明攻击子阶段全程：给玩家失明 II（amplifier 1），持续刷新
        Player p = this.bee.level().getNearestPlayer(this.bee, 64.0D);
        if (p != null) {
            p.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 1));
        }
        this.tick++;
        if (this.tick >= BLIND_COMBO_DURATION) {
            // 30 秒结束：切飞天陨石子阶段，并解除玩家失明
            this.inCombos = false;
            this.bee.setP3BlindComboActive(false);
            this.tick = 0;
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
    private int subStage;   // 0=第一次飞天 1=追踪召唤 2=第二次飞天+锚 3=陨石前 4=等陨石 5=停顿
    private BlockPos meteorLandPos;   // 本次陨石落点（落地时触发 AOE）

    private void startFlyingPhase() {
        this.subStageTick = 0;
        this.subStage = 0;
        // 第一次飞天：向上 + 16 向滞留药水 + 警告
        this.bee.setDeltaMovement(0.0D, 1.5D, 0.0D);
        this.bee.throwPotionDirections(16);
        this.bee.warnPlayer("究极蜜蜂陨石已经部署");
    }

    private void tickFlyingMeteor() {
        Player p = this.bee.level().getNearestPlayer(this.bee, 64.0D);
        this.subStageTick++;
        switch (this.subStage) {
            case 0 -> {
                // 短暂飞天（保持上升约 40 tick 到位）
                if (this.subStageTick >= 40) {
                    this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
                    this.subStage = 1;
                    this.subStageTick = 0;
                    this.summonCooldown = SUMMON_INTERVAL;
                }
            }
            case 1 -> {
                // 追踪玩家每 5 秒召 2 只，持续 10 秒
                LivingEntity target = this.bee.getTarget();
                if (target != null && target.isAlive()) {
                    this.bee.faceEntity(target);
                    Vec3 aim = new Vec3(target.getX(), target.getY() + target.getBbHeight() + 2.0D, target.getZ());
                    Vec3 to = aim.subtract(this.bee.position());
                    this.bee.setDeltaMovement(Mth.clamp(to.x, -0.4D, 0.4D), Mth.clamp(to.y, -0.4D, 0.4D),
                            Mth.clamp(to.z, -0.4D, 0.4D));
                    if (--this.summonCooldown <= 0) {
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
                // 第二次飞天：向上 + 16 向药水 + 停原地 + 警告 + 缚地失明
                this.bee.throwPotionDirections(16);
                this.bee.warnPlayer("警告：究极蜜蜂陨石即将发射！");
                if (p != null) {
                    p.addEffect(new MobEffectInstance(CMMobEffects.ROOTED.get(), 200, 0, false, true));
                }
                this.subStage = 3;
                this.subStageTick = 0;
            }
            case 3 -> {
                // 停原地，3 秒后砸陨石
                this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
                if (this.subStageTick >= METEOR_DELAY_TICKS) {
                    BlockPos targetPos = p != null ? p.blockPosition() : this.bee.blockPosition();
                    this.bee.summonHoneyMeteorAt(targetPos);
                    this.meteorLandPos = targetPos;
                    this.subStage = 4;
                    this.subStageTick = 0;
                }
            }
            case 4 -> {
                // 等待陨石落地（短暂），落地瞬间对非蜜蜂生物造成 10 半径 100 凋零伤害
                this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
                if (this.subStageTick >= 20) {
                    if (this.meteorLandPos != null) {
                        this.bee.meteorImpactDamage(this.meteorLandPos);
                    }
                    this.subStage = 5;
                    this.subStageTick = 0;
                }
            }
            case 5 -> {
                // 停顿 5 秒
                this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
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
