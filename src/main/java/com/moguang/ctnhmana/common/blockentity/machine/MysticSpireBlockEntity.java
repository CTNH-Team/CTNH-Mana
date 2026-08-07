package com.moguang.ctnhmana.common.blockentity.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.moguang.ctnhmana.common.multiblock.SpireBigMath;
import vazkii.botania.api.mana.ManaReceiver;

import java.math.BigInteger;

/**
 * 奥法尖塔魔力池：{@link #mysticTrueManaStr} 为真实储量（BigInteger），{@link #mysticTrueManaCapStr} 为真实容量；
 * {@link #BTMana} 仅为与 Botania / 火花交互用的 int 窗口，{@link #getCurrentMana()} 只读该 int，不直接扫 BigInteger。
 */
public class MysticSpireBlockEntity extends ManaMachineBlockEntity
                                    implements IMachineBlockEntity, IManaged, ManaReceiver {

    /** 真实储量（十进制字符串） */
    @Persisted
    private String mysticTrueManaStr = "0";

    /** 真实容量上限（十进制字符串）；可与 {@link #maxBTMana} 相同或更大（深层储量） */
    @Persisted
    private String mysticTrueManaCapStr = "0";

    public MysticSpireBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public BigInteger getTrueManaBig() {
        return SpireBigMath.parsePersisted(mysticTrueManaStr);
    }

    private void setTrueManaBig(BigInteger v) {
        mysticTrueManaStr = SpireBigMath.toPersistString(SpireBigMath.nonNegative(v));
    }

    /** 真实容量；未持久化有效值时退化为当前 {@link #maxBTMana} */
    public BigInteger getTrueManaCapBig() {
        if (mysticTrueManaCapStr == null || mysticTrueManaCapStr.isEmpty()) {
            return BigInteger.valueOf(Math.max(0, maxBTMana));
        }
        BigInteger c = SpireBigMath.parsePersisted(mysticTrueManaCapStr);
        return c.signum() <= 0 ? BigInteger.valueOf(Math.max(0, maxBTMana)) : c;
    }

    private void setTrueManaCapBig(BigInteger cap) {
        mysticTrueManaCapStr = SpireBigMath.toPersistString(SpireBigMath.nonNegative(cap));
    }

    /**
     * 将真实储量投影到 {@link #BTMana}：对外交互窗口为 min(真实, {@link #maxBTMana})，且落在 int 范围内。
     * Botania / 火花逻辑应在本方法执行后再读 {@link #getCurrentMana()}。
     */
    public void syncMysticManaCacheFromTrue() {
        BigInteger interactionCap = BigInteger.valueOf(Math.max(0, maxBTMana));
        BigInteger vis = SpireBigMath.min(getTrueManaBig(), interactionCap);
        this.BTMana = SpireBigMath.clampToIntNonNegative(vis);
    }

    /**
     * 设置真实容量（可与 int 档 {@link #maxBTMana} 不同，用于深层储量等）。
     */
    public void setTrueManaCapacityBig(BigInteger cap) {
        setTrueManaCapBig(cap);
        if (getTrueManaBig().compareTo(getTrueManaCapBig()) > 0) {
            setTrueManaBig(getTrueManaCapBig());
        }
        syncMysticManaCacheFromTrue();
        setChanged();
    }

    public void migrateLegacyMysticManaIfNeeded() {
        if (mysticTrueManaCapStr == null || mysticTrueManaCapStr.isEmpty()) {
            setTrueManaCapBig(BigInteger.valueOf(Math.max(0, maxBTMana)));
        }
        if (SpireBigMath.parsePersisted(mysticTrueManaStr).signum() == 0 && BTMana != 0) {
            setTrueManaBig(BigInteger.valueOf(BTMana));
        }
        syncMysticManaCacheFromTrue();
    }

    /** 真实剩余可注入空间（容量 − 当前储量） */
    public BigInteger getTrueManaRoomBig() {
        return SpireBigMath.subtractNonNegative(getTrueManaCapBig(), getTrueManaBig());
    }

    /**
     * 本 tick 可从真实储量向外输送的 int 上限（min(真实储量, speedLimit)），
     * 不依赖 {@link #getCurrentMana()}，避免真实储量远超 int 窗口时误判为 0。
     */
    public int mysticOutboundTickCap(int speedLimit) {
        if (speedLimit <= 0) return 0;
        BigInteger t = getTrueManaBig();
        if (t.signum() <= 0) return 0;
        return SpireBigMath.clampToIntNonNegative(SpireBigMath.min(t, BigInteger.valueOf(speedLimit)));
    }

    /**
     * 本 tick 真实剩余空间允许注入的 int 上限（min(剩余空间, speedLimit)）。
     * 用于替代 {@code maxBTMana - getCurrentMana()} 的 int 减法（池条顶满时真实侧仍可有空位）。
     */
    public int mysticInboundTickBudget(int speedLimit) {
        if (speedLimit <= 0) return 0;
        BigInteger room = getTrueManaRoomBig();
        if (room.signum() <= 0) return 0;
        return SpireBigMath.clampToIntNonNegative(SpireBigMath.min(room, BigInteger.valueOf(speedLimit)));
    }

    /** 德尔塔火花扣魔力：只改真实储量，再刷新 int 缓存 */
    public void mysticDrainMana(int amount) {
        if (amount <= 0) return;
        BigInteger t = getTrueManaBig();
        BigInteger take = BigInteger.valueOf(amount).min(t);
        setTrueManaBig(t.subtract(take));
        syncMysticManaCacheFromTrue();
        setChanged();
    }

    /**
     * Botania {@link ManaReceiver}：只返回 int 缓存 {@link #BTMana}，不在这里做 BigInteger 计算。
     * 调用前应已通过 {@link #syncMysticManaCacheFromTrue()}（每 tick / 火花 tick 入口）刷新。
     */
    @Override
    public int getCurrentMana() {
        return Math.max(0, BTMana);
    }

    @Override
    public boolean isFull() {
        return getTrueManaBig().compareTo(getTrueManaCapBig()) >= 0;
    }

    @Override
    public void receiveMana(int i) {
        if (i == 0) return;
        BigInteger cap = getTrueManaCapBig();
        BigInteger t = getTrueManaBig();
        if (i > 0) {
            setTrueManaBig(SpireBigMath.addCapToMax(t, BigInteger.valueOf(i), cap));
        } else {
            setTrueManaBig(SpireBigMath.subtractNonNegative(t, BigInteger.valueOf(-(long) i)));
        }
        syncMysticManaCacheFromTrue();
        setChanged();
    }

    /**
     * 仅设置 Botania 交互用 int 档 {@link #maxBTMana}（传送/池条窗口），
     * <strong>不</strong>修改 {@link #mysticTrueManaCapStr}；真实容量由 {@link #setTrueManaCapacityBig} 单独维护。
     */
    @Override
    public void setMaxMana(int i) {
        super.setMaxMana(i);
        syncMysticManaCacheFromTrue();
        setChanged();
    }

    @Override
    public long sendMana(long mana) {
        if (mana <= 0) return 0;
        BigInteger t = getTrueManaBig();
        long take = Math.min(mana, SpireBigMath.clampToLong(t));
        setTrueManaBig(SpireBigMath.subtractNonNegative(t, BigInteger.valueOf(take)));
        syncMysticManaCacheFromTrue();
        setChanged();
        return take;
    }
}
