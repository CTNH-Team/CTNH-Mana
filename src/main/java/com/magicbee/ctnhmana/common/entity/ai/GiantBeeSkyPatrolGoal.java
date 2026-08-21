package com.magicbee.ctnhmana.common.entity.ai;

import net.minecraft.util.Mth;
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
import com.magicbee.ctnhmana.common.entity.projectile.MaliciousThermalilyProjectile;
import com.magicbee.ctnhmana.common.entity.projectile.WitherAconiteProjectile;
import com.magicbee.ctnhmana.registry.CMEntities;
import com.magicbee.ctnhmana.registry.CMMobEffects;

import java.util.EnumSet;
import java.util.List;

/**
 * 巨蜂巡空机制（boss 战）：狂暴开始立即触发，竖直升高到玩家正上方 10 格后保持悬停，
 * 每 0.5 秒向下瞄准玩家发射剧毒药水箭，每 1 秒从正下方丢下 3 级负面滞留药水，
 * 每 5 秒向斜前方抛射一朵凋灵兔葵（落地生成恶意凋零菟葵）；每 10 秒按 50%/50%
 * 触发散射箭雨或滞留药水齐射。目标死亡后不退出，原地悬停等待。
 */
public class GiantBeeSkyPatrolGoal extends Goal {

    /** 巡空高度（玩家头顶上方格数） */
    private static final double PATROL_HEIGHT = 10.0D;
    /** 爬升速度（格/tick） */
    private static final double CLIMB_SPEED = 1.5D;
    /** 悬停 P 控制增益与速度上限 */
    private static final double POSITION_GAIN = 0.5D;
    private static final double MAX_SPEED = 1.0D;
    /** 毒箭：间隔 0.5 秒（10 tick），剧毒 II 5 秒，初速 2.5 */
    private static final int ARROW_INTERVAL = 10;
    private static final int POISON_DURATION = 100;
    private static final float ARROW_SPEED = 2.5F;
    /** 滞留药水：间隔 1 秒（20 tick），效果 3 级 */
    private static final int POTION_INTERVAL = 20;
    private static final int POTION_AMPLIFIER = 2;
    /** 非瞬间效果时长（云持续 = 时长 / 4 = 5 秒） */
    private static final int POTION_EFFECT_DURATION = 400;
    /** 凋灵兔葵：每 5 秒（100 tick）向斜前方抛射一朵 */
    private static final int ACONITE_INTERVAL = 100;
    /** 新技能：每 10 秒（200 tick）50%/50% 触发 */
    private static final int SKILL_INTERVAL = 200;
    /** 散射箭雨：20 发剧毒箭 */
    private static final int SCATTER_ARROWS = 20;
    private static final float SCATTER_SPEED = 1.5F;
    /** 滞留药水齐射：8 个水平方向 */
    private static final int BARRAGE_DIRECTIONS = 8;
    private static final float BARRAGE_SPEED = 0.5F;
    /** 巡空缚地：每 5 秒给玩家 4.5 秒缚地 */
    private static final int ROOT_INTERVAL = 100;
    private static final int ROOT_DURATION = 90;

    private final GiantBee bee;
    private boolean climbing;
    private int arrowCooldown;
    private int potionCooldown;
    private int aconiteCooldown;
    private int skillCooldown;
    private int rootCooldown;

    public GiantBeeSkyPatrolGoal(GiantBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // 巡空模式：狂暴且未进入追缉；转阶段期间停用
        return this.bee.isAngry() && !this.bee.isChasing() && !this.bee.isTransforming() && this.bee.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        // 暂不退出：目标死亡后原地悬停等待
        return this.bee.isAngry() && !this.bee.isChasing() && !this.bee.isTransforming() && this.bee.isAlive();
    }

    @Override
    public void start() {
        this.climbing = true;
        this.arrowCooldown = ARROW_INTERVAL;
        this.potionCooldown = POTION_INTERVAL;
        this.aconiteCooldown = ACONITE_INTERVAL;
        this.skillCooldown = SKILL_INTERVAL;
        this.rootCooldown = ROOT_INTERVAL;
    }

    @Override
    public void tick() {
        LivingEntity target = this.bee.getTarget();
        if (target == null || !target.isAlive()) {
            // 无目标：原地悬停
            this.bee.setDeltaMovement(0.0D, 0.0D, 0.0D);
            return;
        }
        double targetY = target.getY() + target.getBbHeight() + PATROL_HEIGHT;
        if (this.climbing) {
            // 进入巡空立即竖直升高，到位前不攻击（面朝玩家，保证面向直线即玩家方向）
            this.bee.setDeltaMovement(0.0D, CLIMB_SPEED, 0.0D);
            this.bee.faceEntity(target);
            if (this.bee.getY() >= targetY - 0.5D) {
                this.climbing = false;
            }
            return;
        }
        // 保持玩家正上方 10 格（分量 P 控制，接近时速度归零稳定悬停）
        double dx = target.getX() - this.bee.getX();
        double dy = targetY - this.bee.getY();
        double dz = target.getZ() - this.bee.getZ();
        this.bee.setDeltaMovement(
                Mth.clamp(dx * POSITION_GAIN, -MAX_SPEED, MAX_SPEED),
                Mth.clamp(dy * POSITION_GAIN, -MAX_SPEED, MAX_SPEED),
                Mth.clamp(dz * POSITION_GAIN, -MAX_SPEED, MAX_SPEED));
        // 面向玩家（天空直线方向即玩家方向）
        this.bee.faceEntity(target);
        // 每 0.5 秒向下瞄准玩家发射剧毒药水箭
        if (--this.arrowCooldown <= 0) {
            this.arrowCooldown = ARROW_INTERVAL;
            this.shootPoisonArrow();
        }
        // 每 1 秒从正下方丢下 3 级负面滞留药水（二阶段 25% 概率改为恶意热爆花）
        if (--this.potionCooldown <= 0) {
            this.potionCooldown = POTION_INTERVAL;
            if (this.bee.isPhase2() && this.bee.getRandom().nextFloat() < 0.25F) {
                this.throwThermalily();
            } else {
                this.throwLingeringPotion();
            }
        }
        // 每 5 秒向斜前方抛射凋灵兔葵（二阶段：扇形前方 3 朵，30° 角）
        if (--this.aconiteCooldown <= 0) {
            this.aconiteCooldown = ACONITE_INTERVAL;
            if (this.bee.isPhase2()) {
                this.throwWitherAconiteFan();
            } else {
                this.throwWitherAconite();
            }
        }
        // 每 10 秒触发技能：非二阶段 50%/50% 单选；二阶段同时全部发动
        if (--this.skillCooldown <= 0) {
            this.skillCooldown = SKILL_INTERVAL;
            if (this.bee.isPhase2()) {
                this.scatterPoisonArrows();
                this.throwPotionBarrage();
            } else if (this.bee.getRandom().nextBoolean()) {
                this.scatterPoisonArrows();
            } else {
                this.throwPotionBarrage();
            }
        }
        // 每 5 秒给玩家 2 秒缚地
        if (--this.rootCooldown <= 0) {
            this.rootCooldown = ROOT_INTERVAL;
            if (target instanceof Player player && !player.isCreative()) {
                player.addEffect(new MobEffectInstance(CMMobEffects.ROOTED.get(), ROOT_DURATION, 0, false, true));
            }
        }
    }

    /** 向下瞄准玩家方向发射剧毒 II 药水箭（玩家不可拾取） */
    private void shootPoisonArrow() {
        Level level = this.bee.level();
        Arrow arrow = new Arrow(level, this.bee);
        arrow.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, 1));
        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        LivingEntity target = this.bee.getTarget();
        if (target != null && target.isAlive()) {
            // 直接以自身指向玩家的向量发射，不依赖当前朝向
            Vec3 aim = target.getEyePosition().subtract(this.bee.getEyePosition());
            arrow.shoot(aim.x, aim.y, aim.z, ARROW_SPEED, 1.0F);
        } else {
            arrow.shootFromRotation(this.bee, this.bee.getXRot(), this.bee.getYRot(), 0.0F, ARROW_SPEED, 1.0F);
        }
        level.addFreshEntity(arrow);
    }

    /** 从正下方竖直丢下一瓶 3 级负面滞留药水（剧毒/缓慢/瞬间伤害随机） */
    private void throwLingeringPotion() {
        Level level = this.bee.level();
        if (level.isClientSide) {
            return;
        }
        ThrownPotion potion = new ThrownPotion(level, this.bee);
        potion.setItem(this.randomLingeringPotion());
        potion.moveTo(this.bee.getX(), this.bee.getY() - 1.0D, this.bee.getZ());
        potion.shoot(0.0D, -1.0D, 0.0D, 0.6F, 0.0F);
        level.addFreshEntity(potion);
    }

    /** 向目标方向斜抛一朵凋灵兔葵（含水平分量，非竖直下投） */
    private void throwWitherAconite() {
        Level level = this.bee.level();
        if (level.isClientSide) {
            return;
        }
        WitherAconiteProjectile aconite = new WitherAconiteProjectile(CMEntities.WITHER_ACONITE_PROJECTILE.get(),
                level);
        aconite.setOwner(this.bee);
        aconite.moveTo(this.bee.getX(), this.bee.getY() - 1.0D, this.bee.getZ());
        LivingEntity target = this.bee.getTarget();
        if (target != null && target.isAlive()) {
            // 朝目标方向的水平分量抛射
            double dx = target.getX() - this.bee.getX();
            double dz = target.getZ() - this.bee.getZ();
            double length = Math.sqrt(dx * dx + dz * dz);
            if (length > 0.01D) {
                aconite.shoot(dx / length * 0.5D, 0.0D, dz / length * 0.5D, 0.7F, 0.0F);
            } else {
                aconite.shoot(0.0D, -1.0D, 0.0D, 0.6F, 0.0F);
            }
        } else {
            aconite.shoot(0.0D, -1.0D, 0.0D, 0.6F, 0.0F);
        }
        level.addFreshEntity(aconite);
    }

    /** 朝目标方向竖直下丢一朵恶意热爆花（着陆爆炸） */
    private void throwThermalily() {
        Level level = this.bee.level();
        if (level.isClientSide) {
            return;
        }
        MaliciousThermalilyProjectile proj = new MaliciousThermalilyProjectile(
                CMEntities.MALICIOUS_THERMALILY_PROJECTILE.get(), level);
        proj.setOwner(this.bee);
        proj.moveTo(this.bee.getX(), this.bee.getY() - 1.0D, this.bee.getZ());
        proj.shoot(0.0D, -1.0D, 0.0D, 0.6F, 0.0F);
        level.addFreshEntity(proj);
    }

    /** 二阶段：朝目标方向扇形抛射 3 朵凋灵兔葵（两侧 ±15°，共 30° 角） */
    private void throwWitherAconiteFan() {
        Level level = this.bee.level();
        if (level.isClientSide) {
            return;
        }
        LivingEntity target = this.bee.getTarget();
        double baseYaw = this.bee.getYRot();
        if (target != null && target.isAlive()) {
            // 面向目标
            Vec3 to = new Vec3(target.getX() - this.bee.getX(), 0.0D, target.getZ() - this.bee.getZ());
            baseYaw = (float) (Math.atan2(-to.x, to.z) * (180.0D / Math.PI));
        }
        for (int i = 0; i < 3; i++) {
            double yaw = baseYaw + (i - 1) * 15.0D; // -15°, 0°, +15°（30° 扇区）
            Vec3 dir = new Vec3(-Math.sin(yaw * Math.PI / 180.0D), 0.0D, Math.cos(yaw * Math.PI / 180.0D));
            WitherAconiteProjectile aconite = new WitherAconiteProjectile(CMEntities.WITHER_ACONITE_PROJECTILE.get(),
                    level);
            aconite.setOwner(this.bee);
            aconite.moveTo(this.bee.getX(), this.bee.getY() - 1.0D, this.bee.getZ());
            aconite.shoot(dir.x, dir.y, dir.z, 0.7F, 0.0F);
            level.addFreshEntity(aconite);
        }
    }

    /** 技能：向各个方向（下半球随机）抛射 20 发剧毒箭 */
    private void scatterPoisonArrows() {
        Level level = this.bee.level();
        if (level.isClientSide) {
            return;
        }
        for (int i = 0; i < SCATTER_ARROWS; i++) {
            double yaw = this.bee.getRandom().nextDouble() * Math.PI * 2.0D;
            double pitch = this.bee.getRandom().nextDouble() * (Math.PI / 2.0D); // 0~90° 向下
            Vec3 dir = new Vec3(Math.cos(pitch) * Math.cos(yaw), -Math.sin(pitch), Math.cos(pitch) * Math.sin(yaw));
            Arrow arrow = new Arrow(level, this.bee);
            arrow.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, 1));
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            arrow.shoot(dir.x, dir.y, dir.z, SCATTER_SPEED, 1.0F);
            level.addFreshEntity(arrow);
        }
    }

    /** 技能：向前后左右共 8 个位点（每 45°）抛射一瓶滞留药水 */
    private void throwPotionBarrage() {
        Level level = this.bee.level();
        if (level.isClientSide) {
            return;
        }
        for (int i = 0; i < BARRAGE_DIRECTIONS; i++) {
            double yaw = i * (360.0D / BARRAGE_DIRECTIONS);
            Vec3 dir = new Vec3(-Math.sin(yaw * Math.PI / 180.0D), 0.0D, Math.cos(yaw * Math.PI / 180.0D));
            ThrownPotion potion = new ThrownPotion(level, this.bee);
            potion.setItem(this.randomLingeringPotion());
            potion.moveTo(this.bee.getX(), this.bee.getY() - 1.0D, this.bee.getZ());
            potion.shoot(dir.x, dir.y, dir.z, BARRAGE_SPEED, 0.0F);
            level.addFreshEntity(potion);
        }
    }

    /** 随机一瓶 3 级负面滞留药水：剧毒 / 缓慢 / 瞬间伤害 */
    private ItemStack randomLingeringPotion() {
        ItemStack stack = new ItemStack(Items.LINGERING_POTION);
        MobEffectInstance effect = switch (this.bee.getRandom().nextInt(3)) {
            case 0 -> new MobEffectInstance(MobEffects.POISON, POTION_EFFECT_DURATION, POTION_AMPLIFIER);
            case 1 -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, POTION_EFFECT_DURATION, POTION_AMPLIFIER);
            default -> new MobEffectInstance(MobEffects.HARM, 1, POTION_AMPLIFIER);
        };
        PotionUtils.setCustomEffects(stack, List.of(effect));
        return stack;
    }
}
