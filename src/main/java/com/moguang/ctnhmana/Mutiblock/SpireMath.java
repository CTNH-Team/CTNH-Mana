package com.moguang.ctnhmana.Mutiblock;

/**
 * 尖塔相关安全算术：避免 int/long 乘加溢出导致负数或错误广播。
 */
public final class SpireMath {

    private SpireMath() {}

    /** long × double，结果钳在 [1, maxResult]（用于升级符文连乘 speed/maxMana）。 */
    public static long mulLongDoubleCap(long base, double mult, long maxResult) {
        if (mult <= 1.0 || base <= 0) return Math.min(base, maxResult);
        double prod = (double) base * mult;
        if (Double.isInfinite(prod) || Double.isNaN(prod) || prod >= (double) maxResult) {
            return maxResult;
        }
        long asLong = (long) prod;
        return Math.min(Math.max(1L, asLong), maxResult);
    }

    /**
     * (inputVoltage * speed / baseSpeed) * 4，溢出时返回保守上限。
     * 天顶尖塔调用时应传入<strong>奥法档</strong>{@code speed}，勿传入已对魔力乘过天顶 ×4 的值（EU ×4 仅此一处）。
     */
    public static long euSpeedScaled(long inputVoltage, int speed, int baseSpeed) {
        if (baseSpeed <= 0 || speed <= 0 || inputVoltage <= 0) return 0L;
        try {
            long q = Math.multiplyExact(inputVoltage, (long) speed);
            q /= baseSpeed;
            return Math.multiplyExact(q, 4L);
        } catch (ArithmeticException e) {
            return Long.MAX_VALUE / 8;
        }
    }

    public static int multiplyIntPositiveCap(int a, int b) {
        long p = (long) a * (long) b;
        if (p > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (p < 1) return 1;
        return (int) p;
    }

    /** a + b，结果不超过 max；溢出时返回 max。 */
    public static long addCapToMax(long a, long b, long max) {
        try {
            long s = Math.addExact(a, b);
            return Math.min(s, max);
        } catch (ArithmeticException e) {
            return max;
        }
    }

    public static long nonNegative(long v) {
        return Math.max(0L, v);
    }
}
