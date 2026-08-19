package com.magicbee.ctnhmana.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.common.entity.navigation.RampageFlyingPathNavigation;

/**
 * 狂暴蜜蜂基类：巨蜂与皇家侍从 Bee 的共享实现。
 * 包含蜜蜂式飞行控制、无视阻挡的寻路（配合 {@link RampageFlyingPathNavigation}）、
 * 碰撞箱方块破坏、每秒伤害窗口限制。
 */
public abstract class AbstractRampageBee extends PathfinderMob implements FlyingAnimal {

    /** 飞行动画状态（与原版蜜蜂一致，供模型读取） */
    public float rollAmount;
    public float lerpHeadAngle;

    /** 每秒伤害窗口起点 tick（-1 表示窗口未开始） */
    private int damageWindowStartTick = -1;
    /** 当前伤害窗口（1 秒）内已受到的伤害 */
    private float damageThisSecond;

    /** 当前 1 秒窗口内是否受到过伤害（供回血等机制判断） */
    protected boolean tookDamageThisSecond() {
        return this.damageThisSecond > 0.0F;
    }

    /** 直接转身面朝目标（即时 yRot/yBodyRot/yHeadRot 对齐，不经平滑缓冲，保证移动时已面向） */
    public void faceEntity(LivingEntity target) {
        if (target == null) {
            return;
        }
        Vec3 to = target.getEyePosition().subtract(this.getEyePosition());
        float yaw = (float) (Mth.atan2(to.z, to.x) * (180.0D / Math.PI)) - 90.0F;
        this.setYRot(yaw);
        this.setYBodyRot(yaw);
        this.setYHeadRot(yaw);
        float pitch = (float) (Mth.atan2(to.y, Math.sqrt(to.x * to.x + to.z * to.z)) * (180.0D / Math.PI));
        this.setXRot(pitch);
    }

    public AbstractRampageBee(EntityType<? extends AbstractRampageBee> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isFlying()) {
                this.moveRelative(this.getSpeed(), travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.6F));
            } else {
                super.travel(travelVector);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.updateRollAmount();
    }

    /** 更新飞行姿态动画参数（与原版蜜蜂相同） */
    private void updateRollAmount() {
        this.rollAmount = Mth.lerp(0.9F, this.rollAmount, this.onGround() ? 0.0F : 1.0F);
        this.lerpHeadAngle = Mth.lerp(0.2F, this.lerpHeadAngle, this.getYHeadRot());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        RampageFlyingPathNavigation navigation = new RampageFlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    /**
     * 每秒伤害窗口限制：同一 20 tick（1 秒）窗口内累计受到的伤害不超过 maxPerSecond，
     * 超出的部分被丢弃。返回削减后的伤害。
     */
    protected float clampDamagePerSecond(float amount, float maxPerSecond) {
        if (this.damageWindowStartTick < 0 || this.tickCount - this.damageWindowStartTick >= 20) {
            this.damageWindowStartTick = this.tickCount;
            this.damageThisSecond = 0.0F;
        }
        float budget = Math.max(0.0F, maxPerSecond - this.damageThisSecond);
        float capped = Math.min(amount, budget);
        this.damageThisSecond += capped;
        return capped;
    }

    /** 无视阻挡直线飞向目标（路径上的方块会被碰撞箱破坏机制清开） */
    public void flyStraightTo(Vec3 target, double speed) {
        Vec3 to = target.subtract(this.position());
        double length = to.length();
        if (length < 1.0E-4D) {
            return;
        }
        this.setDeltaMovement(to.scale(speed / length));
        this.getLookControl().setLookAt(target.x, target.y, target.z, 30.0F, 30.0F);
    }

    /** 破坏冷却：每秒（20 tick）触发一次破坏 */
    private int breakCooldown;

    /** 每 tick 调用：每秒破坏一次碰撞箱往外 1 格范围的可破坏方块（追缉形态） */
    protected void tickDestroyTouchedBlocks() {
        if (this.breakCooldown > 0) {
            this.breakCooldown--;
            return;
        }
        this.breakCooldown = 20;
        this.destroyTouchedBlocks(1.0D);
    }

    /** 立即破坏一次碰撞箱本身碰到的可破坏方块（用于冲刺等爆发） */
    protected void destroyTouchedBlocks() {
        this.destroyTouchedBlocks(0.0D);
    }

    /**
     * 摧毁碰撞箱（可选向外扩展 radius 格）碰到的可破坏方块并生成掉落物。
     */
    public void destroyTouchedBlocks(double inflate) {
        Level level = this.level();
        BlockPos.betweenClosedStream(this.getBoundingBox().inflate(inflate)).forEach(pos -> {
            BlockState state = level.getBlockState(pos);
            // 空气与液体不处理
            if (state.isAir() || !state.getFluidState().isEmpty()) {
                return;
            }
            // 硬度小于基岩才破坏（基岩等不可破坏方块硬度为 -1），且尊重其他模组的保护事件
            if (state.getDestroySpeed(level, pos) < 0.0F || !state.canEntityDestroy(level, pos, this)) {
                return;
            }
            BlockEntity blockEntity = level.getBlockEntity(pos);
            Block.dropResources(state, level, pos, blockEntity, this, ItemStack.EMPTY);
            level.levelEvent(2001, pos, Block.getId(state)); // 方块破坏粒子+音效
            level.removeBlock(pos, false);
        });
    }
}
