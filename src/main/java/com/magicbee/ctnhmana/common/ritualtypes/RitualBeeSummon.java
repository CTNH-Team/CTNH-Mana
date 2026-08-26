package com.magicbee.ctnhmana.common.ritualtypes;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.magicbee.ctnhmana.registry.CMEntities;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;

import java.util.List;
import java.util.function.Consumer;

/**
 * 唤蜂仪式：激活后消耗 114514 LP 召唤一只大蜜蜂。
 * 同一个维度内只允许存在一只大蜜蜂；若已有则不再召唤（也不扣 LP）。
 */
@RitualRegister("beesummon")
public class RitualBeeSummon extends Ritual {

    /** 召唤一次消耗的 LP */
    private static final int SUMMON_COST = 114514;
    /** 检查范围半径（格） */
    private static final double CHECK_RANGE = 256.0D;

    public RitualBeeSummon() {
        super("ritualbeesummon", 0, SUMMON_COST, "ritual.ctnhmana.ritualbeesummon");
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        Level level = masterRitualStone.getWorldObj();
        if (level.isClientSide) {
            return;
        }
        // 仅首次刷新（激活后立即）执行一次召唤
        if (masterRitualStone.getRunningTime() != 0) {
            return;
        }
        // 同维度已存在大蜜蜂则不再召唤
        BlockPos pos = masterRitualStone.getMasterBlockPos();
        AABB area = new AABB(pos.getX() - CHECK_RANGE, level.getMinBuildHeight(), pos.getZ() - CHECK_RANGE,
                pos.getX() + CHECK_RANGE, level.getMaxBuildHeight(), pos.getZ() + CHECK_RANGE);
        List<GiantBee> existing = level.getEntitiesOfClass(GiantBee.class, area, LivingEntity::isAlive);
        if (!existing.isEmpty()) {
            return;
        }
        // LP 不足：反噬，不召唤
        if (masterRitualStone.getOwnerNetwork() == null ||
                masterRitualStone.getOwnerNetwork().getCurrentEssence() < SUMMON_COST) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }
        // 消耗 LP 并生成大蜜蜂
        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(SUMMON_COST));
        GiantBee bee = CMEntities.GIANT_BEE.get().create(level);
        if (bee == null) {
            return;
        }
        bee.moveTo(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0.0F, 0.0F);
        bee.setPersistenceRequired();
        level.addFreshEntity(bee);
        level.playSound(null, pos, SoundEvents.BEE_POLLINATE, SoundSource.HOSTILE, 1.0F, 1.0F);
    }

    @Override
    public int getRefreshCost() {
        return 0; // 一次性召唤，不在每次刷新时扣 LP
    }

    @Override
    public int getRefreshTime() {
        return 20;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> consumer) {
        this.addSquareRunes(consumer, 3, -3, EnumRuneType.EARTH);
        this.addSquareRunes(consumer, 2, -2, EnumRuneType.WATER);
        this.addCornerRunes(consumer, 1, -2, EnumRuneType.AIR);
        this.addParallelRunes(consumer, 1, -1, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 3, -2, EnumRuneType.WATER);
        this.addSmaalSquareRunes(consumer, 3, 0, -1, EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer, -3, 0, -1, EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer, 0, -3, -1, EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer, 0, 3, -1, EnumRuneType.DUSK);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualBeeSummon();
    }

    protected final void addSquareRunes(Consumer<RitualComponent> components, int size, int y, EnumRuneType rune) {
        if (size < 1) {
            throw new IllegalArgumentException("正方形大小必须为正整数");
        }
        addCornerRunes(components, size, y, rune);
        for (int offset = 1; offset < size; offset++) {
            addRune(components, size, y, offset, rune);
            addRune(components, size, y, -offset, rune);
            addRune(components, -size, y, offset, rune);
            addRune(components, -size, y, -offset, rune);
            addRune(components, offset, y, size, rune);
            addRune(components, -offset, y, size, rune);
            addRune(components, offset, y, -size, rune);
            addRune(components, -offset, y, -size, rune);
        }
        addParallelRunes(components, size, y, rune);
    }

    protected final void addSmaalSquareRunes(Consumer<RitualComponent> components, int x, int z, int y,
                                             EnumRuneType rune) {
        addRune(components, x + 1, y, z, rune);
        addRune(components, x - 1, y, z, rune);
        addRune(components, x, y, z + 1, rune);
        addRune(components, x, y, z - 1, rune);
        addRune(components, x + 1, y, z + 1, rune);
        addRune(components, x + 1, y, z - 1, rune);
        addRune(components, x - 1, y, z + 1, rune);
        addRune(components, x - 1, y, z - 1, rune);
    }
}
