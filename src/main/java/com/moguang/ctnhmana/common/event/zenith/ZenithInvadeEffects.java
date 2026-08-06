package com.moguang.ctnhmana.common.event.zenith;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.menu.me.common.MEStorageMenu;
import com.moguang.ctnhmana.registry.CMMobEffects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 虚境入侵效果：
 * <ul>
 *   <li>常驻：AE 终端干扰</li>
 *   <li>每 20s 随机 1 个轻微事件</li>
 *   <li>每 44s 随机 1 个中度事件</li>
 *   <li>每 77s 随机 1 个重度事件</li>
 * </ul>
 */
public final class ZenithInvadeEffects {

    public static final int LIGHT_INTERVAL_TICKS = 20 * 20;
    public static final int MEDIUM_INTERVAL_TICKS = 44 * 20;
    public static final int HEAVY_INTERVAL_TICKS = 77 * 20;

    public static final int LIGHT_GAZE_TICKS = 10 * 20;
    public static final int MEDIUM_GAZE_TICKS = 20 * 20;
    public static final int DEBUFF_DURATION_TICKS = 15 * 20;
    public static final int JUMP_BOOST_TICKS = 2 * 20;
    /** 跳跃提升 20 → amplifier 19 */
    public static final int JUMP_BOOST_AMPLIFIER = 19;
    public static final int MACHINE_RADIUS = 5;

    private static final List<MobEffect> INVADE_DEBUFFS = List.of(
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.BLINDNESS,
            MobEffects.HUNGER,
            MobEffects.WEAKNESS,
            MobEffects.POISON,
            MobEffects.WITHER,
            MobEffects.GLOWING,
            MobEffects.LEVITATION,
            MobEffects.UNLUCK,
            MobEffects.BAD_OMEN,
            MobEffects.DARKNESS);

    private ZenithInvadeEffects() {}

    /** 维度内存在活跃入侵时每 tick 调用一次。 */
    public static void onDimensionTick(ServerLevel level) {
        jamAeTerminals(level);

        long time = level.getGameTime();
        if (time <= 0) return;

        if (time % LIGHT_INTERVAL_TICKS == 0) {
            for (ServerPlayer player : level.players()) {
                triggerLightEvent(level, player);
            }
        }
        if (time % MEDIUM_INTERVAL_TICKS == 0) {
            for (ServerPlayer player : level.players()) {
                triggerMediumEvent(level, player);
            }
        }
        if (time % HEAVY_INTERVAL_TICKS == 0) {
            for (ServerPlayer player : level.players()) {
                triggerHeavyEvent(level, player);
            }
        }
    }

    // ================= 轻微：每 20s 随机其一 =================

    private static void triggerLightEvent(ServerLevel level, ServerPlayer player) {
        int pick = level.getRandom().nextInt(3);
        switch (pick) {
            case 0 -> applyRandomDebuffs(player, level.getRandom());
            case 1 -> playExplosionSound(level, player);
            default -> applyShroudGaze(player, LIGHT_GAZE_TICKS);
        }
    }

    private static void applyRandomDebuffs(ServerPlayer player, RandomSource random) {
        List<MobEffect> pool = new ArrayList<>(INVADE_DEBUFFS);
        Collections.shuffle(pool, new java.util.Random(random.nextLong()));
        int count = Math.min(3, pool.size());
        for (int i = 0; i < count; i++) {
            player.addEffect(new MobEffectInstance(pool.get(i), DEBUFF_DURATION_TICKS, random.nextInt(2)));
        }
    }

    private static void playExplosionSound(ServerLevel level, ServerPlayer player) {
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE,
                SoundSource.AMBIENT, 1.0F, 0.75F + level.random.nextFloat() * 0.2F);
    }

    private static void applyShroudGaze(ServerPlayer player, int durationTicks) {
        player.addEffect(new MobEffectInstance(CMMobEffects.ShroudGazing.get(), durationTicks));
    }

    // ================= 中度：每 44s 随机其一 =================

    private static void triggerMediumEvent(ServerLevel level, ServerPlayer player) {
        int pick = level.getRandom().nextInt(5);
        switch (pick) {
            case 0 -> interruptNearbyMachines(level, player);
            case 1 -> safeExplosionAtFeet(level, player);
            case 2 -> dropMainHand(player);
            case 3 -> applyJumpBoostSpike(player);
            default -> dragonBreathAndGaze(level, player);
        }
    }

    private static void applyJumpBoostSpike(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, JUMP_BOOST_TICKS, JUMP_BOOST_AMPLIFIER));
    }

    /** 5 格内正在运行的 GT 机器 progress 归零，并播放 ARC（跳电感）。 */
    private static void interruptNearbyMachines(ServerLevel level, ServerPlayer player) {
        BlockPos center = player.blockPosition();
        boolean any = false;
        for (int dx = -MACHINE_RADIUS; dx <= MACHINE_RADIUS; dx++) {
            for (int dy = -MACHINE_RADIUS; dy <= MACHINE_RADIUS; dy++) {
                for (int dz = -MACHINE_RADIUS; dz <= MACHINE_RADIUS; dz++) {
                    if (dx * dx + dy * dy + dz * dz > MACHINE_RADIUS * MACHINE_RADIUS) continue;
                    BlockPos pos = center.offset(dx, dy, dz);
                    BlockEntity be = level.getBlockEntity(pos);
                    if (!(be instanceof IMachineBlockEntity machineHolder)) continue;
                    MetaMachine machine = machineHolder.getMetaMachine();
                    if (!(machine instanceof IRecipeLogicMachine recipeMachine)) continue;
                    RecipeLogic logic = recipeMachine.getRecipeLogic();
                    if (!logic.isWorking() || logic.getProgress() <= 0) continue;
                    logic.setProgress(0);
                    any = true;
                    GTSoundEntries.ARC.playOnServer(level, pos, 1.0F, 0.9F + level.random.nextFloat() * 0.2F);
                }
            }
        }
        if (!any) {
            // 附近无机器时仍给一点反馈
            GTSoundEntries.ARC.playOnServer(level, center, 0.6F, 1.0F);
        }
    }

    private static void safeExplosionAtFeet(ServerLevel level, ServerPlayer player) {
        level.explode(null, player.getX(), player.getY(), player.getZ(),
                2.5F, false, Level.ExplosionInteraction.NONE);
    }

    private static void dropMainHand(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;
        player.drop(stack.copy(), false, true);
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        player.inventoryMenu.broadcastChanges();
    }

    private static void dragonBreathAndGaze(ServerLevel level, ServerPlayer player) {
        AreaEffectCloud cloud = new AreaEffectCloud(EntityType.AREA_EFFECT_CLOUD, level);
        cloud.setPos(player.getX(), player.getY(), player.getZ());
        cloud.setParticle(ParticleTypes.DRAGON_BREATH);
        cloud.setRadius(2.5F);
        cloud.setDuration(12 * 20);
        cloud.setRadiusOnUse(-0.05F);
        cloud.setWaitTime(10);
        cloud.setRadiusPerTick(-0.02F);
        cloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 0, false, false));
        level.addFreshEntity(cloud);
        applyShroudGaze(player, MEDIUM_GAZE_TICKS);
    }

    // ================= 重度：每 77s 随机其一 =================

    private static void triggerHeavyEvent(ServerLevel level, ServerPlayer player) {
        int pick = level.getRandom().nextInt(4);
        switch (pick) {
            case 0 -> spawnInvisibleCreepers(level, player);
            case 1 -> summonWardenAtHead(level, player);
            case 2 -> drainNearbyMachinePower(level, player);
            default -> setHealthToOne(level, player);
        }
    }

    /**
     * 5 格内有电的 GT 机器：电能归零、进度归零，并播放跳电特效。
     */
    private static void drainNearbyMachinePower(ServerLevel level, ServerPlayer player) {
        BlockPos center = player.blockPosition();
        boolean any = false;
        for (int dx = -MACHINE_RADIUS; dx <= MACHINE_RADIUS; dx++) {
            for (int dy = -MACHINE_RADIUS; dy <= MACHINE_RADIUS; dy++) {
                for (int dz = -MACHINE_RADIUS; dz <= MACHINE_RADIUS; dz++) {
                    if (dx * dx + dy * dy + dz * dz > MACHINE_RADIUS * MACHINE_RADIUS) continue;
                    BlockPos pos = center.offset(dx, dy, dz);
                    BlockEntity be = level.getBlockEntity(pos);
                    if (!(be instanceof IMachineBlockEntity machineHolder)) continue;
                    MetaMachine machine = machineHolder.getMetaMachine();
                    if (!drainMachineEnergy(level, pos, machine)) continue;
                    if (machine instanceof IRecipeLogicMachine recipeMachine) {
                        recipeMachine.getRecipeLogic().setProgress(0);
                    }
                    playTripEffect(level, pos);
                    any = true;
                }
            }
        }
        if (!any) {
            playTripEffect(level, center);
        }
    }

    /** @return 是否实际抽空了电能 */
    private static boolean drainMachineEnergy(ServerLevel level, BlockPos pos, MetaMachine machine) {
        boolean drained = false;
        IEnergyContainer cap = GTCapabilityHelper.getEnergyContainer(level, pos, null);
        if (cap != null) {
            long stored = cap.getEnergyStored();
            if (stored > 0) {
                cap.removeEnergy(stored);
                drained = true;
            }
        }
        for (MachineTrait trait : machine.getTraits()) {
            if (!(trait instanceof IEnergyContainer container)) continue;
            long stored = container.getEnergyStored();
            if (stored <= 0) continue;
            container.removeEnergy(stored);
            drained = true;
        }
        return drained;
    }

    private static void playTripEffect(ServerLevel level, BlockPos pos) {
        GTSoundEntries.ARC.playOnServer(level, pos, 1.0F, 0.85F + level.random.nextFloat() * 0.25F);
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                28, 0.45, 0.45, 0.45, 0.08);
        level.sendParticles(ParticleTypes.FLASH,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                1, 0.0, 0.0, 0.0, 0.0);
    }

    private static void spawnInvisibleCreepers(ServerLevel level, ServerPlayer player) {
        RandomSource random = level.getRandom();
        int count = 1 + random.nextInt(3);
        BlockPos base = player.blockPosition();
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double dist = 1.5 + random.nextDouble() * (MACHINE_RADIUS - 1.5);
            double x = player.getX() + Math.cos(angle) * dist;
            double z = player.getZ() + Math.sin(angle) * dist;
            double y = base.getY();
            Creeper creeper = EntityType.CREEPER.create(level);
            if (creeper == null) continue;
            creeper.moveTo(x, y, z, random.nextFloat() * 360.0F, 0.0F);
            creeper.setInvisible(true);
            creeper.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 60, 0, false, false));
            creeper.setPersistenceRequired();
            level.addFreshEntity(creeper);
        }
    }

    private static void summonWardenAtHead(ServerLevel level, ServerPlayer player) {
        Warden warden = EntityType.WARDEN.create(level);
        if (warden == null) return;
        warden.moveTo(player.getX(), player.getEyeY(), player.getZ(), player.getYRot(), 0.0F);
        warden.setPersistenceRequired();
        level.addFreshEntity(warden);
    }

    private static void setHealthToOne(ServerLevel level, ServerPlayer player) {
        float health = player.getHealth();
        if (health <= 1.0F) return;
        float damage = health - 1.0F;
        player.hurt(level.damageSources().magic(), damage);
        if (player.getHealth() > 1.0F) {
            player.setHealth(1.0F);
        }
    }

    // ================= 常驻 =================

    /**
     * 关闭玩家已打开的 AE 存储终端菜单（有线 / 无线共用 {@link MEStorageMenu}）。
     * 无线打开拦截另由 mixin 负责。
     */
    private static void jamAeTerminals(ServerLevel level) {
        boolean notify = level.getGameTime() % 20 == 0;
        for (ServerPlayer player : level.players()) {
            AbstractContainerMenu menu = player.containerMenu;
            if (menu instanceof MEStorageMenu) {
                player.closeContainer();
                if (notify) {
                    ZenithInvadeMessages.notifyWirelessJammed(player);
                }
            }
        }
    }
}
