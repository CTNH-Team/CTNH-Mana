package com.magicbee.ctnhmana.common.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.common.entity.ai.RoyalServantChargeGoal;
import com.magicbee.ctnhmana.common.entity.ai.RoyalServantDashGoal;

import java.util.UUID;

/**
 * 超级无敌皇家侍从Bee：巨蜂（boss）狂暴时召唤的护卫，模型与普通蜜蜂一致。
 * 每只存活的侍从为绑定的 boss 提供 25% 伤害减免；冲锋 AI 无视阻挡直线冲向玩家；
 * 由 boss 发动冲刺技能（停顿 0.25 秒 → 冲刺 5 格 → 速度 II 1 秒）；
 * 血量低于 10 时获得速度 III 并加速冲锋，贴近目标自爆造成 15 点范围伤害。
 */
public class RoyalServantBee extends AbstractRampageBee {

    /** 每秒伤害上限 */
    private static final float MAX_TAKEN_PER_SECOND = 24.0F;
    /** 自爆触发血量 */
    public static final float SELF_DESTRUCT_HEALTH = 20.0F;
    /** 自爆距离（到目标脚底） */
    public static final double SELF_DESTRUCT_RANGE = 2.5D;
    /** 自爆 AOE 范围/伤害 */
    private static final double EXPLODE_RANGE = 4.0D;
    private static final float EXPLODE_DAMAGE = 15.0F;
    /** 冲刺技能：停顿 0.25 秒（5 tick） */
    private static final int DASH_PAUSE_TICKS = 5;
    /** 冲刺 5 格（1 格/tick，5 tick 完成） */
    private static final int DASH_TICKS = 5;
    private static final float DASH_SPEED = 1.0F;
    /** 冲刺后获得速度 II 持续 1 秒 */
    private static final int DASH_SPEED_DURATION = 20;
    private static final int DASH_SPEED_AMPLIFIER = 1;
    /** 低血时获得速度 III */
    private static final int LOW_HP_SPEED_AMPLIFIER = 2;
    /** 目标选择范围 */
    private static final double CHARGE_TARGET_RANGE = 48.0D;
    /** 查找绑定 boss 的范围 */
    private static final int BOSS_SEARCH_RANGE = 128;
    /** 冲锋速度（格/tick），速度效果按原版公式加成 */
    private static final double CHARGE_SPEED = 0.4D;

    /** 绑定的 boss UUID（只能由 boss 召唤） */
    private UUID boundBossId;
    private GiantBee cachedBoss;

    /** 冲刺技能状态机 */
    private DashState dashState = DashState.NONE;
    private int dashTicks;
    private Vec3 dashDir;

    private enum DashState {
        NONE,
        PAUSING,
        DASHING
    }

    public RoyalServantBee(EntityType<? extends RoyalServantBee> type, Level level) {
        super(type, level);
        this.autoDashCooldown = AUTO_DASH_INTERVAL;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 77.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RoyalServantDashGoal(this));
        this.goalSelector.addGoal(2, new RoyalServantChargeGoal(this));
    }

    @Override
    public void checkDespawn() {
        // boss 召唤物不自然消失
    }

    // ---------- 绑定 boss ----------

    public void setBoundBoss(GiantBee boss) {
        this.boundBossId = boss.getUUID();
    }

    public boolean isBoundTo(GiantBee boss) {
        return this.boundBossId != null && this.boundBossId.equals(boss.getUUID());
    }

    /** 按 UUID 查找绑定的 boss（缓存，失效时才重新搜索） */
    public GiantBee getBoundBoss() {
        if (this.boundBossId == null || this.level().isClientSide) {
            return null;
        }
        if (this.cachedBoss == null || !this.cachedBoss.isAlive() || this.cachedBoss.level() != this.level()) {
            this.cachedBoss = this.level().getEntitiesOfClass(GiantBee.class,
                    this.getBoundingBox().inflate(BOSS_SEARCH_RANGE), e -> e.getUUID().equals(this.boundBossId))
                    .stream().findFirst().orElse(null);
        }
        return this.cachedBoss;
    }

    // ---------- 目标与冲锋 ----------

    /** 冲锋目标：boss 的目标，否则最近的玩家 */
    public LivingEntity getChargeTarget() {
        GiantBee boss = this.getBoundBoss();
        if (boss != null && boss.getTarget() != null && boss.getTarget().isAlive()) {
            return boss.getTarget();
        }
        return this.level().getNearestPlayer(this, CHARGE_TARGET_RANGE);
    }

    /**
     * 冲锋速度：固定 0.4 格/tick。
     * 速度效果（速度 II/III）按原版公式（每级 +20% 基础）加成。
     */
    public double getChargeSpeed() {
        double speed = CHARGE_SPEED;
        MobEffectInstance effect = this.getEffect(MobEffects.MOVEMENT_SPEED);
        if (effect != null) {
            speed *= 1.0D + 0.2D * (effect.getAmplifier() + 1);
        }
        return speed;
    }

    /** 免疫凋零/中毒/缓慢效果 */
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (effect.getEffect() == MobEffects.WITHER || effect.getEffect() == MobEffects.POISON ||
                effect.getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide) {
            // 自己人（boss/其他侍从）不互伤
            if (source.getEntity() instanceof GiantBee || source.getEntity() instanceof RoyalServantBee) {
                return super.hurt(source, amount);
            }
            // 免疫凋灵伤害（凋灵雾/恶意兔葵爆炸）
            if (source.is(DamageTypes.WITHER)) {
                return false;
            }
            // 每秒至多受到 19 伤害（kill 伤害除外）
            if (!source.is(DamageTypes.GENERIC_KILL)) {
                amount = this.clampDamagePerSecond(amount, MAX_TAKEN_PER_SECOND);
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void customServerAiStep() {
        this.tickDash();
        // 有目标（追缉状态）时每秒破坏碰撞箱碰到的方块
        if (this.getChargeTarget() != null) {
            this.tickDestroyTouchedBlocks();
        }
        // 血量低于 10：持续保持速度 III 直到自爆
        if (this.getHealth() < SELF_DESTRUCT_HEALTH) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, LOW_HP_SPEED_AMPLIFIER, false, false));
        }
        // 每 5 秒主动向目标发起一次冲锋技能
        if (this.dashState == DashState.NONE && --this.autoDashCooldown <= 0) {
            this.autoDashCooldown = AUTO_DASH_INTERVAL;
            this.triggerDash();
        }
    }

    /** 每 5 秒主动发动一次冲刺技能（tick） */
    private static final int AUTO_DASH_INTERVAL = 100;
    private int autoDashCooldown;

    /** boss 命令的额外连续冲锋次数（追缉停顿时连发 2 次用） */
    private int pendingDashCommands;

    /** 令其在当前冲锋结束后再连续发动 extra 次冲锋（boss 用） */
    public void queueExtraDashes(int extra) {
        this.pendingDashCommands += extra;
    }

    // ---------- 冲刺技能（由 boss 发动） ----------

    /** 由绑定的 boss 调用：原地停顿 0.25 秒后向目标方向冲刺 5 格 */
    public void triggerDash() {
        if (this.level().isClientSide) {
            return;
        }
        LivingEntity target = this.getChargeTarget();
        if (target == null) {
            return;
        }
        Vec3 targetPos = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
        this.dashDir = targetPos.subtract(this.position()).normalize();
        this.dashState = DashState.PAUSING;
        this.dashTicks = DASH_PAUSE_TICKS;
    }

    public boolean isDashing() {
        return this.dashState != DashState.NONE;
    }

    private void tickDash() {
        switch (this.dashState) {
            case PAUSING -> {
                // 原地停顿 0.25 秒（直接面朝目标）
                this.setDeltaMovement(Vec3.ZERO);
                LivingEntity target = this.getChargeTarget();
                if (target != null) {
                    this.faceEntity(target);
                }
                if (--this.dashTicks <= 0) {
                    this.dashState = DashState.DASHING;
                    this.dashTicks = DASH_TICKS;
                }
            }
            case DASHING -> {
                // 向锁定方向冲刺 5 格（面朝冲刺方向），冲刺时额外破坏 1 格范围清障
                this.setDeltaMovement(this.dashDir.scale(DASH_SPEED));
                this.destroyTouchedBlocks(1.0D);
                this.setYRot((float) (Mth.atan2(this.dashDir.z, this.dashDir.x) * (180.0D / Math.PI)) - 90.0F);
                this.setYBodyRot(this.getYRot());
                this.setYHeadRot(this.getYRot());
                if (--this.dashTicks <= 0) {
                    this.dashState = DashState.NONE;
                    this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, DASH_SPEED_DURATION,
                            DASH_SPEED_AMPLIFIER, false, false));
                    // 若还被命令额外冲锋，立即连发下一次
                    if (this.pendingDashCommands > 0) {
                        this.pendingDashCommands--;
                        this.triggerDash();
                    }
                }
            }
            default -> {}
        }
    }

    // ---------- 自爆 ----------

    /** 低血近身自爆：15 点范围伤害（不伤自己人），然后死亡 */
    public void selfDestruct() {
        if (this.level().isClientSide) {
            return;
        }
        for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(EXPLODE_RANGE),
                e -> e.isAlive() && e != this && !(e instanceof GiantBee) && !(e instanceof RoyalServantBee))) {
            target.hurt(this.damageSources().mobAttack(this), EXPLODE_DAMAGE);
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 1,
                    0.0D, 0.0D, 0.0D, 0.0D);
            this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0F,
                    1.0F);
        }
        this.kill();
    }
}