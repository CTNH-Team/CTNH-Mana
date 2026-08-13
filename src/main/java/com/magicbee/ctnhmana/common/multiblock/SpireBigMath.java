package com.magicbee.ctnhmana.common.multiblock;

import com.magicbee.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * 尖塔 BigInteger 储量与 GT long / Botania int API 之间的安全算术。
 */
public final class SpireBigMath {

    private SpireBigMath() {}

    /**
     * 尖塔真实魔力容量连乘时的绝对上限（远大于 int，防止符文组合爆炸失控）。
     */
    public static final BigInteger TRUE_MANA_ABS_CEILING = BigInteger.ONE.shiftLeft(8192);

    /**
     * 与 {@link SpireMath#mulLongDoubleCap} 同类，但作用于 BigInteger，
     * 容量不再受 int 限制。
     */
    public static BigInteger mulBigDoubleCap(BigInteger base, double mult, BigInteger maxResult) {
        if (mult <= 1.0 || base.signum() <= 0) {
            return min(base, maxResult);
        }
        BigDecimal prod = new BigDecimal(base).multiply(BigDecimal.valueOf(mult));
        BigInteger rounded = prod.setScale(0, RoundingMode.DOWN).toBigInteger();
        return min(rounded, maxResult);
    }

    /**
     * Botania 魔力池「柱条」用的 int 上限：真实容量 ≥ {@link Integer#MAX_VALUE} 时柱条顶格为 {@link Integer#MAX_VALUE}。
     */
    public static int interactionManaBarCap(BigInteger trueManaCapacity) {
        if (trueManaCapacity == null || trueManaCapacity.signum() <= 0) {
            return 0;
        }
        BigInteger maxIntBi = BigInteger.valueOf(Integer.MAX_VALUE);
        if (trueManaCapacity.compareTo(maxIntBi) >= 0) {
            return Integer.MAX_VALUE;
        }
        return trueManaCapacity.intValueExact();
    }

    /** 将持久化十进制字符串还原为 {@link BigInteger}；非法或空视为 0。 */
    public static BigInteger parsePersisted(@Nullable String s) {
        if (s == null || s.isEmpty()) return BigInteger.ZERO;
        try {
            return new BigInteger(s);
        } catch (NumberFormatException e) {
            return BigInteger.ZERO;
        }
    }

    /** 写入存档 / 同步字段用的十进制字符串 */
    public static String toPersistString(BigInteger v) {
        if (v == null || v.signum() == 0) return "0";
        return v.toString();
    }

    public static BigInteger nonNegative(BigInteger v) {
        if (v == null || v.signum() < 0) return BigInteger.ZERO;
        return v;
    }

    public static BigInteger min(BigInteger a, BigInteger b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    public static BigInteger min(BigInteger a, BigInteger b, BigInteger c) {
        return min(min(a, b), c);
    }

    /** a - b，结果不小于 0 */
    public static BigInteger subtractNonNegative(BigInteger a, BigInteger b) {
        BigInteger d = a.subtract(b);
        return d.signum() < 0 ? BigInteger.ZERO : d;
    }

    /** stored + add，不超过 cap */
    public static BigInteger addCapToMax(BigInteger stored, BigInteger add, BigInteger cap) {
        if (add.signum() <= 0) return nonNegative(stored);
        BigInteger sum = stored.add(add);
        return sum.compareTo(cap) >= 0 ? cap : sum;
    }

    /**
     * (energyCapacity * maxMana / baseMaxMana) * 4。
     * 天顶尖塔应传入与 {@link MysticSpireBlockEntity#getTrueManaCapBig()}
     * 一致的奥法档真实容量（{@link BigInteger}），勿用钳到 int 的 {@code MysticSpire#maxMana}。
     */
    public static BigInteger euCapacityScaled(long energyCapacity, BigInteger maxMana, BigInteger baseMaxMana) {
        if (baseMaxMana == null || baseMaxMana.signum() <= 0) return BigInteger.ZERO;
        if (maxMana == null || maxMana.signum() <= 0 || energyCapacity <= 0) return BigInteger.ZERO;
        BigInteger ec = BigInteger.valueOf(energyCapacity);
        return ec.multiply(maxMana).divide(baseMaxMana).multiply(BigInteger.valueOf(4L));
    }

    /** 与变电站 CEUtil 一致：用于写入 GT IEnergyContainer（long API） */
    public static long clampToLong(BigInteger v) {
        if (v == null || v.signum() <= 0) {
            return 0L;
        } else if (v.bitLength() > 63) {
            return Long.MAX_VALUE;
        } else {
            long r = v.longValue();
            return r < 0L ? Long.MAX_VALUE : r;
        }
    }

    /** Botania / Java int 上限内非负钳制 */
    public static int clampToIntNonNegative(BigInteger v) {
        if (v == null || v.signum() <= 0) {
            return 0;
        }
        if (v.bitLength() > 31) {
            return Integer.MAX_VALUE;
        }
        long x = v.longValue();
        if (x > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (int) x;
    }
}
