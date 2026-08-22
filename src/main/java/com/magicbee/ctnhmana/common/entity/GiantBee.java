package com.magicbee.ctnhmana.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.common.entity.ai.GiantBeeChaseGoal;
import com.magicbee.ctnhmana.common.entity.ai.GiantBeeFlowerAttractionGoal;
import com.magicbee.ctnhmana.common.entity.ai.GiantBeePhase3Goal;
import com.magicbee.ctnhmana.common.entity.ai.GiantBeePollenGoal;
import com.magicbee.ctnhmana.common.entity.ai.GiantBeeSkyPatrolGoal;
import com.magicbee.ctnhmana.common.entity.ai.GiantBeeWanderGoal;
import com.magicbee.ctnhmana.common.entity.projectile.MaliciousThermalilyProjectile;
import com.magicbee.ctnhmana.registry.CMEntities;
import com.magicbee.ctnhmana.registry.CMItems;
import com.magicbee.ctnhmana.registry.CMMobEffects;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.entity.projectile.EntityMeteor;

import java.util.List;

import javax.annotation.Nullable;

/**
 * 巨蜂：中立飞行 boss 生物。
 * 原版蜜蜂模型放大 {@link #SCALE} 倍渲染；默认中立，满足特定条件后敌对（目前为受到攻击，
 * 后续特定条件通过 {@link #makeAngryAt} 触发）。飞行控制/方块破坏/伤害窗口继承自
 * {@link AbstractRampageBee}。
 */
public class GiantBee extends AbstractRampageBee {

    /** 模型渲染放大倍数，命中箱/眼睛高度与其保持一致 */
    public static final float SCALE = 3.5F;

    /** 敌对持续 tick 数 */
    private static final int ANGER_DURATION = 77777777;
    /** 许愿bee喂食冷却（tick） */
    private static final int FEEDING_COOLDOWN = 6000;
    /** 每秒恢复生命（每 tick 恢复 1 点 = 每秒 20） */
    private static final float REGEN_PER_TICK = 1.0F;
    /** boss 战每秒恢复（未受伤时）/ 受伤后每秒恢复 */
    private static final float BOSS_REGEN_PER_SECOND = 4.0F;
    private static final float BOSS_REGEN_WOUNDED_PER_SECOND = 1.0F;
    /** 低于该伤害完全免疫 */
    private static final float IMMUNE_THRESHOLD = 7.0F;
    /** 超过该伤害削减到该值 */
    private static final float MAX_TAKEN_DAMAGE = 50.0F;
    /** 激怒层数上限 */
    private static final int MAX_RAGE_STACKS = 3;
    /** 激怒持续 tick（10 秒） */
    private static final int RAGE_DURATION = 200;
    /** 激怒 AOE 范围/伤害/中毒时长 */
    private static final double AOE_RANGE = 5.0D;
    private static final float AOE_DAMAGE = 8.0F;
    private static final int POISON_DURATION = 100;

    private static final EntityDataAccessor<Boolean> DATA_ANGRY = SynchedEntityData.defineId(GiantBee.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_CHASING = SynchedEntityData.defineId(GiantBee.class,
            EntityDataSerializers.BOOLEAN);
    /** 狂暴（被无尽剑激怒）：任何阶段 75% 独立减伤 + 近战×2 + 技能 cd 1 秒 */
    private static final EntityDataAccessor<Boolean> DATA_RAGE = SynchedEntityData.defineId(GiantBee.class,
            EntityDataSerializers.BOOLEAN);

    /** 侍从生效范围/每只存活侍从提供的伤害减免 */
    private static final double SERVANT_BUFF_RANGE = 32.0D;
    private static final float SERVANT_DAMAGE_REDUCTION_PER_BEE = 0.25F;
    /** 巡空模式召唤：每 15 秒尝试（300 tick）/单次 2 只 / 上限 4 */
    private static final int SERVANT_WAVE_INTERVAL_SKY = 300;
    private static final int SERVANT_WAVE_COUNT_SKY = 2;
    private static final int MAX_SERVANTS_SKY = 4;
    /** 追缉模式召唤：每 14 秒尝试（280 tick）/单次 3 只 / 上限 5 */
    private static final int SERVANT_WAVE_INTERVAL_CHASE = 280;
    private static final int SERVANT_WAVE_COUNT_CHASE = 3;
    private static final int MAX_SERVANTS_CHASE = 5;
    /** 召唤波概率基数：100% - 25% × 现存侍从数 */
    private static final double SERVANT_WAVE_CHANCE_PER_BEE = 0.25D;
    /** 蜜蜂守卫提供的伤害减免上限（任何阶段） */
    private static final float MAX_SERVANT_DAMAGE_REDUCTION = 0.95F;
    /** 追缉模式自带减伤（与蜜蜂减伤乘算） */
    private static final float CHASE_DAMAGE_REDUCTION = 0.5F;
    /** 狂暴减伤（独立乘区，任何阶段生效） */
    private static final float RAGE_DAMAGE_REDUCTION = 0.75F;
    /** 狂暴时技能 cd 等效加速（tick，1 秒） */
    private static final int RAGE_CD_REDUCE = 20;
    /** 狂暴近战翻倍：施加的力量效果 amplifier（基础攻击 12 → 24） */
    private static final int RAGE_BOOST_AMPLIFIER = 3;
    /** 巡空→追缉切换条件：至少 30 秒且累计受伤害至少 60；或 2 分钟未切换 */
    private static final int CHASE_SWITCH_MIN_TICKS = 600;
    private static final float CHASE_SWITCH_DAMAGE_THRESHOLD = 120.0F;
    private static final int CHASE_SWITCH_FORCE_TICKS = 2400;

    // ---------- 二阶段转阶段 ----------

    /** 血量 ≤ 该值触发阶段转化（第一阶段到 400 血进入转阶段） */
    private static final float PHASE2_TRIGGER_HEALTH = 400.0F;
    /** 转化前停顿 tick（锁血，1 秒） */
    private static final int PHASE2_PAUSE_TICKS = 20;
    /** 恢复期每秒回血 */
    private static final float PHASE2_REGEN_PER_SECOND = 20.0F;
    /** 恢复期八方向抛射恶意热爆花间隔（tick，4 秒） */
    private static final int THERMALILY_INTERVAL = 80;
    /** 恢复期 0.25 秒抛射面朝方向药水箭 */
    private static final int ARROW_INTERVAL_P2 = 5;
    /** 转阶段每 10 秒召唤 3 只皇家蜜蜂 */
    private static final int TRANSFORM_SUMMON_INTERVAL = 200;
    private static final int TRANSFORM_SUMMON_COUNT = 3;
    private static final int TRANSFORM_MAX_SERVANTS = 5;
    // ---------- 第三阶段 ----------
    /** 血量 ≤ 该值进入第三阶段 */
    private static final float PHASE3_TRIGGER_HEALTH = 400.0F;
    /** 第三阶段锁血下限：循环未完整走完一遍时，非 kill 伤害不使血量低于该值 */
    private static final float P3_LOCK_HEALTH = 200.0F;
    /** 阶段变量 */
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(GiantBee.class,
            EntityDataSerializers.INT);

    private int angerTimer;
    /** 授粉循环冷却（tick），0 表示可以授粉（由 GiantBeePollenGoal 读取/重置） */
    public int pollenCooldown;
    /** 授粉后短暂不被花吸引的冷却（tick），由 GiantBeePollenGoal 设置 */
    public int attractionCooldown;
    /** 许愿bee喂食冷却（tick） */
    private int feedingCooldown;
    /** 最后伤害者（激怒满层时锁定进入狂暴） */
    private LivingEntity lastDamager;
    /** 召唤侍从波倒计时（tick） */
    private int servantWaveTimer;
    /** 巡空模式累计 tick（切换追缉用，回血不参与伤害累计） */
    private int patrolTicks;
    /** 巡空模式累计受到伤害（回血不会扣减） */
    private float patrolDamageTaken;
    /** 转阶段：转化倒计时（tick）/ 恢复期倒计时/ 八方向抛射冷却/ 药水箭冷却 */
    private int transformTicks;
    private boolean phase2Recovering;
    private boolean phase2Transforming;   // 转阶段整体（停顿+恢复期），期间不移动/不转向玩家
    private int thermalilyCooldown;
    private int p2ArrowCooldown;
    private int transformSummonCooldown;
    /** 第三阶段：是否进行中 / 完整循环计数 / 是否处于失明攻击子阶段（该阶段 50% 减伤） */
    private boolean p3Active;
    private int p3CycleCount;
    private boolean p3BlindComboActive;

    /** 狂暴时显示的 BOSS 血量条 */
    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(this.getDisplayName(),
            BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(false);

    public GiantBee(EntityType<? extends GiantBee> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        // 第三阶段（最高优先级，isPhase3 触发时接管）
        this.goalSelector.addGoal(0, new GiantBeePhase3Goal(this));
        // 巡空机制优先级最高（boss 战）
        this.goalSelector.addGoal(0, new GiantBeeSkyPatrolGoal(this));
        // 追缉模式（巡空后切换，优先级最高，一定触发则替换巡空）
        this.goalSelector.addGoal(0, new GiantBeeChaseGoal(this));
        this.goalSelector.addGoal(1, new GiantBeePollenGoal(this));
        this.goalSelector.addGoal(2, new GiantBeeFlowerAttractionGoal(this));
        this.goalSelector.addGoal(3, new GiantBeeWanderGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1333.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    // ---------- 中立 / 敌对 ----------

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ANGRY, false);
        this.entityData.define(DATA_CHASING, false);
        this.entityData.define(DATA_RAGE, false);
        this.entityData.define(DATA_PHASE, 1);
    }

    // ---------- 阶段 ----------

    /** 当前阶段（1 = 一阶段，2 = 真正的二阶段） */
    public int getPhase() {
        return this.entityData.get(DATA_PHASE);
    }

    /** 是否已进入真正的二阶段 */
    public boolean isPhase2() {
        return this.getPhase() >= 2;
    }

    /** 是否处于二阶段转化中（停顿+恢复期）：禁移动、禁转向玩家、高速自转 */
    public boolean isTransforming() {
        return this.phase2Transforming;
    }

    /** 设置阶段变量（转阶段后半段设置 2） */
    public void setPhase(int phase) {
        this.entityData.set(DATA_PHASE, phase);
    }

    /** 是否已进入第三阶段 */
    public boolean isPhase3() {
        return this.getPhase() >= 3;
    }

    /** P3 是否进行中 */
    public boolean isP3Active() {
        return this.p3Active;
    }

    /** P3 是否处于失明攻击子阶段（该阶段 50% 减伤） */
    public boolean isP3BlindComboActive() {
        return this.p3BlindComboActive;
    }

    /** P3 是否尚未完整走完一轮循环（据此锁血 ≥200） */
    public boolean isP3Locked() {
        return this.p3Active && this.p3CycleCount < 1;
    }

    /** 供 P3 goal 切换失明攻击子阶段标志（决定 50% 减伤是否生效） */
    public void setP3BlindComboActive(boolean active) {
        this.p3BlindComboActive = active;
    }

    /** 供 P3 goal 在飞天子阶段结束时增加完整循环计数（解除锁血） */
    public void incrementP3Cycle() {
        this.p3CycleCount++;
    }

    public boolean isAngry() {
        return this.entityData.get(DATA_ANGRY);
    }

    public void setAngry(boolean angry) {
        this.entityData.set(DATA_ANGRY, angry);
    }

    // ---------- 狂暴（无尽剑触发） ----------

    /** 是否处于狂暴状态（被无尽剑激怒，永久）：75% 独立减伤 + 近战×2 + 技能 cd 1 秒 */
    public boolean isRageMode() {
        return this.entityData.get(DATA_RAGE);
    }

    public void setRageMode(boolean rage) {
        this.entityData.set(DATA_RAGE, rage);
    }

    // ---------- 模式：巡空 / 追缉 ----------

    /** 是否处于追缉模式（战斗后半段：追击、50% 减伤、冲刺技能） */
    public boolean isChasing() {
        return this.entityData.get(DATA_CHASING);
    }

    /** 切换为追缉模式：禁用巡空、启用追缉，解除二阶段巡空的永久缚地 */
    public void enterChaseMode() {
        if (this.level().isClientSide) {
            return;
        }
        // 二阶段巡空的永久缚地在进入追缉时解除
        Player nearby = this.level().getNearestPlayer(this, 64.0D);
        if (nearby != null) {
            nearby.removeEffect(CMMobEffects.ROOTED.get());
        }
        this.entityData.set(DATA_CHASING, true);
    }

    /** 退出追缉模式：恢复 50 血、复位巡空累计计时/伤害，回到巡空 */
    public void exitChaseMode() {
        if (this.level().isClientSide) {
            return;
        }
        this.heal(50.0F);
        this.patrolTicks = 0;
        this.patrolDamageTaken = 0.0F;
        this.entityData.set(DATA_CHASING, false);
    }

    /**
     * 特定敌对条件的入口：条件满足时调用此方法使巨蜂敌对并锁定目标。
     * 目前默认触发条件为受到攻击；后续特定条件（持有指定物品/结构开启等）在此触发。
     */
    public void makeAngryAt(LivingEntity target) {
        if (this.level().isClientSide) {
            return;
        }
        if (!this.isAngry()) {
            // 首次被激怒进入 boss 状态：全服广播
            if (this.level().getServer() != null) {
                this.level().getServer().getPlayerList().broadcastSystemMessage(
                        Component.literal("你激怒了不该激怒的蜜蜂！"), false);
            }
        }
        this.setAngry(true);
        this.setTarget(target);
        this.angerTimer = 0;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide) {
            // 无尽剑（avaritia:infinity）伤害：完全免疫，并触发狂暴
            if (this.isInfinitySwordDamage(source)) {
                if (!this.isRageMode()) {
                    this.setRageMode(true);
                    this.setAngry(true);
                    this.angerTimer = 0;
                    // 触发狂暴时必须锁定攻击目标，否则 AI goal 空转不转头
                    if (this.getTarget() == null || !this.getTarget().isAlive()) {
                        if (source.getEntity() instanceof LivingEntity attacker) {
                            this.setTarget(attacker);
                        } else {
                            this.setTarget(this.level().getNearestPlayer(this, 64.0D));
                        }
                    }
                    // 广播作弊提示
                    if (this.level().getServer() != null) {
                        this.level().getServer().getPlayerList().broadcastSystemMessage(
                                Component.literal("你竟然作弊！大蜜蜂不会饶恕你！"), false);
                    }
                }
                return false;
            }
            // 其他巨蜂的 AOE 反伤会互相连锁无限递归：巨蜂之间的伤害不触发本机制
            if (source.getEntity() instanceof GiantBee) {
                return super.hurt(source, amount);
            }
            // 转阶段恢复期：锁血（不受伤害，除非 kill）
            if (this.phase2Recovering && !source.is(DamageTypes.GENERIC_KILL)) {
                return false;
            }
            if (source.getEntity() instanceof LivingEntity attacker) {
                this.lastDamager = attacker;
            }
            // kill 伤害（/kill 指令）不受免疫与伤害上限限制
            boolean isKillDamage = source.is(DamageTypes.GENERIC_KILL);
            // 低于 7 的伤害完全免疫
            if (amount < IMMUNE_THRESHOLD && !isKillDamage && !this.isAngry()) {
                return false;
            }
            // 激怒判定基于原始伤害
            if (amount > MAX_TAKEN_DAMAGE && !this.isAngry()) {
                // 超过 50：直接获得 3 层激怒
                this.gainRage(MAX_RAGE_STACKS);
            } else if (!this.isAngry()) {
                // 7-50：1 层激怒 + 范围 AOE 伤害 + 中毒
                this.gainRage(1);
                this.rageAoE();
            }
            // BOSS 伤害限制：自带减伤与蜜蜂减伤按区间乘算（追缉 50% × 蜜蜂 25%/只，蜜蜂区段上限 95%），再限制单次/每秒不超过 50（kill 除外）
            if (!isKillDamage) {
                amount = this.applyServantDamageReduction(amount);
                if (this.isChasing()) {
                    amount *= (1.0F - CHASE_DAMAGE_REDUCTION);
                }
                // 第三阶段失明攻击子阶段：额外 50% 减伤
                if (this.isP3BlindComboActive()) {
                    amount *= 0.5F;
                }
                // 狂暴（无尽剑触发）：任何阶段 75% 独立减伤乘区
                if (this.isRageMode()) {
                    amount *= (1.0F - RAGE_DAMAGE_REDUCTION);
                }
                amount = Math.min(amount, MAX_TAKEN_DAMAGE);
                float actualTaken = this.clampDamagePerSecond(amount, MAX_TAKEN_DAMAGE);
                // 巡空模式（狂暴且未切换追缉）实际受到的伤害计入切换累计值（回血不扣减）
                if (this.isAngry() && !this.isChasing()) {
                    this.patrolDamageTaken += actualTaken;
                }
                amount = actualTaken;
                // 第三阶段锁血：未完整走完一轮循环时，非 kill 伤害不使血量低于 P3_LOCK_HEALTH
                if (this.isP3Locked() && this.getHealth() - amount < P3_LOCK_HEALTH) {
                    amount = Math.max(0.0F, this.getHealth() - P3_LOCK_HEALTH);
                }
            }
        }
        return super.hurt(source, amount);
    }

    /** 是否为无尽贪婪（Re-Avaritia）无尽剑造成的伤害（伤害类型 avaritia:infinity） */
    private boolean isInfinitySwordDamage(DamageSource source) {
        return source.typeHolder().unwrapKey()
                .map(key -> key.location().toString().equals("avaritia:infinity"))
                .orElse(false);
    }

    /**
     * 免疫无尽剑左键秒杀：无尽剑直接 setHealth(health - Float.MAX_VALUE) / setHealth(0) 绕过 hurt。
     * 这里拦截单次扣血超过伤害上限（50）的设置（正常战斗扣血经 hurt 钳制 ≤50；回血为增加不受影响）。
     */
    @Override
    public void setHealth(float pHealth) {
        // 免疫无尽剑左键秒杀：拦截单次扣血超过伤害上限（50）的设置。
        // 正常战斗扣血经 hurt 钳制 ≤50；回血为增加（drop 为负）不受影响；无限剑 setHealth(h-∞)/setHealth(0) 为大量扣血被忽略。
        if (!this.level().isClientSide) {
            float drop = this.getHealth() - pHealth;
            if (drop > MAX_TAKEN_DAMAGE) {
                return;
            }
        }
        super.setHealth(pHealth);
    }

    /**
     * 每只存活侍从提供 25% 伤害减免（线性叠加，范围 {@link #SERVANT_BUFF_RANGE} 内）。
     * 即使满 4/5 只也一样，蜜蜂减伤上限 95% 由调用处钳制。
     */
    private float applyServantDamageReduction(float amount) {
        int servants = this.countServants();
        if (servants <= 0) {
            return amount;
        }
        float reduction = Math.min(SERVANT_DAMAGE_REDUCTION_PER_BEE * servants, MAX_SERVANT_DAMAGE_REDUCTION);
        return amount * (1.0F - reduction);
    }

    // ---------- 皇家侍从 Bee ----------

    /** 范围内存活且绑定自己的侍从数量 */
    private int countServants() {
        return this.level().getEntitiesOfClass(RoyalServantBee.class, this.getBoundingBox().inflate(SERVANT_BUFF_RANGE),
                e -> e.isAlive() && e.isBoundTo(this)).size();
    }

    /** 召唤一只皇家侍从 Bee（生成在 boss 上方并绑定自己） */
    public void summonServantBee() {
        if (this.level().isClientSide) {
            return;
        }
        RoyalServantBee servant = CMEntities.ROYAL_SERVANT_BEE.get().create(this.level());
        if (servant != null) {
            servant.moveTo(this.getX(), this.getY() + this.getBbHeight() + 1.0D, this.getZ(), this.getYRot(), 0.0F);
            servant.setBoundBoss(this);
            // 黑暗阶段（P3 失明攻击）：召唤的护卫不给苦难护盾，改给持久发光
            boolean darkStage = this.isP3BlindComboActive();
            if (this.isChasing() && this.isPhase2() && !darkStage) {
                // 追缉模式下召唤的护卫获得 2 级苦难护盾（amplifier 1，持续 600 秒）
                servant.addEffect(new MobEffectInstance(CMMobEffects.PAIN_SHIELD.get(), 12000, 0, false, false));
            }
            if (darkStage) {
                servant.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120000, 0, false, false));
            }
            this.level().addFreshEntity(servant);
        }
    }

    /** 命令范围内所有存活侍从发动冲刺技能（供 boss 技能系统调用） */
    public void commandServantsToDash() {
        this.level().getEntitiesOfClass(RoyalServantBee.class, this.getBoundingBox().inflate(SERVANT_BUFF_RANGE),
                e -> e.isAlive() && e.isBoundTo(this)).forEach(RoyalServantBee::triggerDash);
    }

    /** 命令范围内所有存活侍从发动 total 次连续冲锋（先触发一次，其余排队连发） */
    public void commandServantsToDash(int total) {
        this.level().getEntitiesOfClass(RoyalServantBee.class, this.getBoundingBox().inflate(SERVANT_BUFF_RANGE),
                e -> e.isAlive() && e.isBoundTo(this)).forEach(servant -> {
                    servant.queueExtraDashes(total - 1);
                    servant.triggerDash();
                });
    }

    /** 确保苦难护盾等级至少为 targetAmplifier（amplifier；若已有更高则不动） */
    public void ensurePainShield(int targetAmplifier) {
        if (this.level().isClientSide) {
            return;
        }
        MobEffectInstance shield = this.getEffect(CMMobEffects.PAIN_SHIELD.get());
        if (shield == null || shield.getAmplifier() < targetAmplifier) {
            this.addEffect(new MobEffectInstance(CMMobEffects.PAIN_SHIELD.get(), 12000, targetAmplifier, false, false));
        }
    }

    /** 获得激怒层数（buff 持续 10 秒）；满 3 层锁定最后的伤害者进入狂暴 */
    private void gainRage(int amount) {
        int current = this.hasEffect(CMMobEffects.RAGE.get()) ?
                this.getEffect(CMMobEffects.RAGE.get()).getAmplifier() + 1 : 0;
        int stacks = Math.min(MAX_RAGE_STACKS, current + amount);
        this.addEffect(new MobEffectInstance(CMMobEffects.RAGE.get(), RAGE_DURATION, stacks - 1, false, false));
        if (stacks >= MAX_RAGE_STACKS && this.lastDamager != null) {
            this.makeAngryAt(this.lastDamager);
            this.removeEffect(CMMobEffects.RAGE.get());
        }
    }

    /** 对周围生物造成一次 AOE 伤害并附加中毒 */
    private void rageAoE() {
        if (this.level().isClientSide) {
            return;
        }
        for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(AOE_RANGE),
                e -> e != this && !(e instanceof GiantBee) && !(e instanceof RoyalServantBee) && e.isAlive())) {
            target.hurt(this.damageSources().mobAttack(this), AOE_DAMAGE);
            target.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, 0));
        }
    }

    @Override
    protected void customServerAiStep() {
        // 转阶段期间：强制停止移动（防止其它 goal 施加位移）
        if (this.phase2Transforming) {
            this.getNavigation().stop();
            this.setDeltaMovement(Vec3.ZERO);
        }
        // 回血：非 boss 战每秒 20；boss 战每秒 4，若上一秒受到过伤害降为每秒 1
        if (!this.isAngry()) {
            this.heal(REGEN_PER_TICK);
        } else {
            this.heal(this.tookDamageThisSecond() ? BOSS_REGEN_WOUNDED_PER_SECOND / 20.0F :
                    BOSS_REGEN_PER_SECOND / 20.0F);
        }
        if (this.pollenCooldown > 0) {
            this.pollenCooldown--;
        }
        if (this.attractionCooldown > 0) {
            this.attractionCooldown--;
        }
        if (this.feedingCooldown > 0) {
            this.feedingCooldown--;
        }
        // 转阶段是独立逻辑：既非巡空也非追缉，完全接管，不执行任何战斗/召唤/计时。
        // 血量 ≤ 阈值自动触发进入转阶段。
        if (!this.phase2Transforming && !this.isPhase2() && this.isAngry() &&
                this.getHealth() <= PHASE2_TRIGGER_HEALTH) {
            this.startPhase2Transition();
        }
        // 第三阶段触发：已进入二阶段且血量 ≤400 时进入 P3
        if (!this.p3Active && this.isPhase2() && this.getHealth() <= PHASE3_TRIGGER_HEALTH) {
            this.p3Active = true;
            this.p3CycleCount = 0;
            this.p3BlindComboActive = false;
            this.setPhase(3);
            // 进入三阶段：解除二阶段巡空的永久缚地
            Player nearby = this.level().getNearestPlayer(this, 64.0D);
            if (nearby != null) {
                nearby.removeEffect(CMMobEffects.ROOTED.get());
            }
        }
        if (this.phase2Transforming) {
            this.tickPhaseTransition();
            return;
        }
        // BOSS 血量条仅在狂暴时显示
        this.bossEvent.setVisible(this.isAngry());
        if (this.isAngry()) {
            // 狂暴（无尽剑触发）：永久保持，近战伤害×2（力量效果），技能 cd 等效同比减速
            if (this.isRageMode()) {
                this.angerTimer = 0; // 狂暴不会因时间消退
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40,
                        RAGE_BOOST_AMPLIFIER, false, false));
            }
            // 任何模式：每秒破坏一次碰撞箱往外 1 格范围的可破坏方块
            this.tickDestroyTouchedBlocks();
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            this.angerTimer++;
            if (this.angerTimer >= ANGER_DURATION) {
                this.setAngry(false);
                this.setTarget(null);
            }
            // 巡空模式：累计在线时长，满足条件切换追缉
            if (!this.isChasing()) {
                this.patrolTicks++;
                // 二阶段：巡空时永久给玩家缚地（每 tick 刷新）
                if (this.isPhase2()) {
                    Player nearby = this.level().getNearestPlayer(this, 64.0D);
                    if (nearby != null) {
                        nearby.addEffect(new MobEffectInstance(CMMobEffects.ROOTED.get(), 100, 0, false, true));
                    }
                }
                if ((this.patrolTicks >= CHASE_SWITCH_MIN_TICKS &&
                        this.patrolDamageTaken >= CHASE_SWITCH_DAMAGE_THRESHOLD) ||
                        this.patrolTicks >= CHASE_SWITCH_FORCE_TICKS) {
                    this.enterChaseMode();
                }
            }
            // 每 15 秒（巡空）/14 秒（追缉）尝试召唤一波对应数量的侍从并命令现存侍从冲刺；狂暴时等效 1 秒
            this.servantWaveTimer -= this.isRageMode() ? RAGE_CD_REDUCE : 1;
            if (this.servantWaveTimer <= 0) {
                this.servantWaveTimer = this.isChasing() ? SERVANT_WAVE_INTERVAL_CHASE : SERVANT_WAVE_INTERVAL_SKY;
                this.summonServantWave();
            }
        }
    }

    /**
     * 召唤波：概率 = 100% - 25% × 现存侍从数。
     * 巡空单次 2 只（上限 4）；追缉单次 3 只（上限 5）。
     * 成功则召唤并与自己绑定，同时命令现存侍从发动冲刺技能。
     */
    private void summonServantWave() {
        int max = this.isChasing() ? MAX_SERVANTS_CHASE : MAX_SERVANTS_SKY;
        int count = this.isChasing() ? SERVANT_WAVE_COUNT_CHASE : SERVANT_WAVE_COUNT_SKY;
        // 二阶段：巡空/追缉召唤均额外 +1 只
        if (this.isPhase2()) {
            count++;
        }
        int servants = this.countServants();
        if (servants >= max) {
            return;
        }
        double chance = 1.0D - SERVANT_WAVE_CHANCE_PER_BEE * servants;
        if (this.getRandom().nextDouble() >= chance) {
            return;
        }
        int toSummon = Math.min(count, max - servants);
        for (int i = 0; i < toSummon; i++) {
            this.summonServantBee();
        }
        this.commandServantsToDash();
    }

    // ---------- 二阶段转阶段 ----------

    /**
     * 转阶段：独立逻辑，进入时立即切追缉标记、锁血停顿（customServerAiStep 提前 return，不再跑巡空/追缉/召唤）。
     */
    private void startPhase2Transition() {
        this.enterChaseMode();
        this.phase2Transforming = true;
        this.transformTicks = PHASE2_PAUSE_TICKS;
    }

    /**
     * 转阶段状态机（仅在 transforming 时被调）：
     * 停顿 1 秒（锁血）→ 恢复期（每秒回 20 + 每 4 秒八方向热爆花 + 0.25s 药水箭）→ 回满血 → 阶段2。
     * 全程停在原地并高速自转（掉头式旋转，不转向玩家）。
     */
    private void tickPhaseTransition() {
        // 转阶段：停在原地 + 高速自转（不转向玩家，掉头式旋转）
        this.setDeltaMovement(Vec3.ZERO);
        this.setYRot(this.getYRot() + 8.0F);
        this.setYBodyRot(this.getYRot());
        this.setYHeadRot(this.getYRot());
        // 转化停顿：锁血（hurt 中已拦截），倒计时
        if (this.transformTicks > 0 && !this.phase2Recovering) {
            this.transformTicks--;
            if (this.transformTicks <= 0) {
                this.phase2Recovering = true;
                this.thermalilyCooldown = THERMALILY_INTERVAL;
                this.p2ArrowCooldown = ARROW_INTERVAL_P2;
            }
            return;
        }
        // 恢复期：每秒回 20 + 每 4 秒八方向热爆花 + 0.25s 药水箭 + 每 10 秒召唤 3 只皇家蜜蜂
        if (this.phase2Recovering) {
            this.heal(PHASE2_REGEN_PER_SECOND / 20.0F);
            if (--this.thermalilyCooldown <= 0) {
                this.thermalilyCooldown = THERMALILY_INTERVAL;
                this.throwThermalilyBarrage();
            }
            if (--this.p2ArrowCooldown <= 0) {
                this.p2ArrowCooldown = ARROW_INTERVAL_P2;
                this.shootPhase2PoisonArrow();
            }
            if (--this.transformSummonCooldown <= 0) {
                this.transformSummonCooldown = TRANSFORM_SUMMON_INTERVAL;
                this.summonServantCount(TRANSFORM_MAX_SERVANTS, TRANSFORM_SUMMON_COUNT);
            }
            // 回满血：进入真正的二阶段（5 级苦难护盾 + 切回巡空模式）
            if (this.getHealth() >= this.getMaxHealth()) {
                this.phase2Recovering = false;
                this.phase2Transforming = false;
                this.transformTicks = 0;
                this.setPhase(2);
                // 进入二阶段：全服广播
                if (this.level().getServer() != null) {
                    this.level().getServer().getPlayerList().broadcastSystemMessage(
                            Component.literal("你彻底激怒了究极魔力蜜蜂！"), false);
                }
                this.addEffect(new MobEffectInstance(CMMobEffects.PAIN_SHIELD.get(), 12000, 4, false, false));
                this.exitChaseMode();
            }
        }
    }

    /** 召唤指定数量的皇家蜜蜂并命令现存侍从冲刺（上限 max） */
    public void summonServantCount(int max, int count) {
        int servants = this.countServants();
        if (servants >= max) {
            return;
        }
        int toSummon = Math.min(count, max - servants);
        for (int i = 0; i < toSummon; i++) {
            this.summonServantBee();
        }
        this.commandServantsToDash();
    }

    /** 恢复期：八方向抛射恶意热爆花（水平 8 个 45° 方向） */
    private void throwThermalilyBarrage() {
        Level level = this.level();
        if (level.isClientSide) {
            return;
        }
        for (int i = 0; i < 8; i++) {
            double yaw = i * (360.0D / 8.0D);
            Vec3 dir = new Vec3(-Math.sin(yaw * Math.PI / 180.0D), 0.0D, Math.cos(yaw * Math.PI / 180.0D));
            MaliciousThermalilyProjectile proj = new MaliciousThermalilyProjectile(
                    CMEntities.MALICIOUS_THERMALILY_PROJECTILE.get(), level);
            proj.setOwner(this);
            proj.moveTo(this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ());
            proj.shoot(dir.x, dir.y, dir.z, 0.9F, 0.2F);
            level.addFreshEntity(proj);
        }
    }

    /** 恢复期：0.25 秒向面朝方向发射一支剧毒药水箭 */
    private void shootPhase2PoisonArrow() {
        Level level = this.level();
        if (level.isClientSide) {
            return;
        }
        Vec3 look = this.getLookAngle().normalize();
        Arrow arrow = new Arrow(level, this);
        arrow.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        arrow.shoot(look.x, look.y, look.z, 2.0F, 1.0F);
        level.addFreshEntity(arrow);
    }

    // ---------- 第三阶段辅助 ----------

    /** 原地生成一朵凋零云（持续 {@code duration} tick，5 秒用 100），仿恶意菟葵凋零云 */
    public void spawnWitherCloudAt(BlockPos pos, int duration) {
        if (this.level().isClientSide) {
            return;
        }
        AreaEffectCloud cloud = new AreaEffectCloud(this.level(), pos.getX() + 0.5D, pos.getY() + 0.5D,
                pos.getZ() + 0.5D);
        cloud.setRadius(3.5F);
        cloud.setDuration(duration);
        cloud.setWaitTime(0);
        cloud.setParticle(ParticleTypes.SMOKE);
        cloud.addEffect(new MobEffectInstance(CMMobEffects.WITHER_CLOUD.get(), duration, 0));
        cloud.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, 1));
        this.level().addFreshEntity(cloud);
    }

    /** 朝 n 个水平方向各丢一瓶滞留药水（8 或 16 向） */
    public void throwPotionDirections(int count) {
        Level level = this.level();
        if (level.isClientSide) {
            return;
        }
        for (int i = 0; i < count; i++) {
            double yaw = i * (360.0D / count);
            Vec3 dir = new Vec3(-Math.sin(yaw * Math.PI / 180.0D), 0.0D, Math.cos(yaw * Math.PI / 180.0D));
            ThrownPotion potion = new ThrownPotion(level, this);
            potion.setItem(this.randomLingeringPotion());
            potion.moveTo(this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ());
            potion.shoot(dir.x, dir.y, dir.z, 0.5F, 0.0F);
            level.addFreshEntity(potion);
        }
    }

    /** 随机一瓶 3 级负面滞留药水（剧毒 / 缓慢 / 瞬间伤害） */
    private ItemStack randomLingeringPotion() {
        ItemStack stack = new ItemStack(Items.LINGERING_POTION);
        MobEffectInstance effect = switch (this.getRandom().nextInt(3)) {
            case 0 -> new MobEffectInstance(MobEffects.POISON, 400, 2);
            case 1 -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 2);
            default -> new MobEffectInstance(MobEffects.HARM, 1, 2);
        };
        PotionUtils.setCustomEffects(stack, List.of(effect));
        return stack;
    }

    /** 从高空朝目标位置召唤一颗血魔法蜂蜜陨石 */
    public void summonHoneyMeteorAt(BlockPos target) {
        Level level = this.level();
        if (level.isClientSide) {
            return;
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        EntityMeteor meteor = new EntityMeteor(serverLevel, target.getX() + 0.5D, serverLevel.getHeight(),
                target.getZ() + 0.5D);
        meteor.setDeltaMovement(0.0D, -0.5D, 0.0D);
        meteor.setContainedStack(new ItemStack(Blocks.HONEYCOMB_BLOCK));
        serverLevel.addFreshEntity(meteor);
    }

    /** 陨石落地冲击：以落点为中心 10 半径，对所有非蜜蜂生物造成 100 点凋零伤害 */
    public void meteorImpactDamage(BlockPos center) {
        Level level = this.level();
        if (level.isClientSide) {
            return;
        }
        Vec3 vec = new Vec3(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class,
                new AABB(vec, vec).inflate(10.0D),
                e -> e.isAlive() && !(e instanceof GiantBee) && !(e instanceof RoyalServantBee))) {
            entity.hurt(entity.damageSources().wither(), 100.0F);
        }
    }

    /** 对最近的玩家显示文本警告（Exp 或操作栏） */
    public void warnPlayer(String message) {
        Player player = this.level().getNearestPlayer(this, 64.0D);
        if (player != null) {
            player.displayClientMessage(Component.literal(message), true);
        }
    }

    // ---------- 喂食 ----------

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isThirdEyeItem(stack)) {
            if (this.feedingCooldown > 0) {
                if (!player.level().isClientSide) {
                    player.displayClientMessage(
                            Component.literal("许愿bee还在冷却中，剩余 " + (this.feedingCooldown / 20) + " 秒"), true);
                }
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
            if (!player.level().isClientSide) {
                stack.shrink(1);
                this.spawnAtLocation(new ItemStack(CMItems.WISH_BEE.get()));
                this.feedingCooldown = FEEDING_COOLDOWN;
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        // 其他物品：动物驯服失败粒子（黑烟），物品返还（不消耗），不进冷却
        if (!player.level().isClientSide && player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 1.0D, this.getZ(), 12, 0.5D, 0.5D,
                    0.5D, 0.05D);
        }
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    /** 第三只眼 / 紧闭的第三只眼 / 污血泣眼 */
    public static boolean isThirdEyeItem(ItemStack stack) {
        Item item = stack.getItem();
        return item == CMItems.KOISHI_EYE.get() || item == CMItems.TAINTED_BLOOD_EYE.get() ||
                item == BotaniaItems.thirdEye;
    }

    // ---------- boss 特性 ----------

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void die(DamageSource source) {
        this.bossEvent.setVisible(false);
        super.die(source);
    }

    @Override
    public void checkDespawn() {
        // boss 不自然消失
    }

    @Override
    public float getEyeHeight(Pose pose) {
        return 0.6F * SCALE;
    }
}
