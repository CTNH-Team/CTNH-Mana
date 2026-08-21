package com.magicbee.ctnhmana;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = CTNHMana.MODID)
public class CMConfig {

    public static CMConfig INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = Configuration.registerConfig(CMConfig.class, ConfigFormats.yaml()).getConfigInstance();
            }
        }
    }

    @Configurable
    @Configurable.Comment({
            "Player non-armor damage reduction clamp. Multiplicative reductions from various mods are",
            "collapsed so their TOTAL cannot exceed this cap (as a fraction). Only affects players.",
            "Default: 0.5 (50%)" })
    public DamageClamp damageClamp = new DamageClamp();

    public static class DamageClamp {

        @Configurable
        @Configurable.Comment({ "Enable the damage reduction clamp for players", "Default: true" })
        public boolean enabled = true;

        @Configurable
        @Configurable.Comment({ "Maximum effective non-armor reduction allowed (0.0 - 1.0)", "Default: 0.5" })
        @Configurable.DecimalRange(min = 0.0, max = 1.0)
        public float cap = 0.5F;

        @Configurable
        @Configurable.Comment({ "Sigmoid steepness. Higher = sharper transition near the midpoint", "Default: 6" })
        @Configurable.DecimalRange(min = 0.1, max = 30.0)
        public float steepness = 6.0F;

        @Configurable
        @Configurable.Comment({ "Sigmoid midpoint (fraction of cumulative reduction where the curve turns)",
                "Default: 0.5" })
        @Configurable.DecimalRange(min = 0.0, max = 1.0)
        public float midpoint = 0.5F;

        @Configurable
        @Configurable.Comment({ "Log clamped damage changes at debug level", "Default: false" })
        public boolean debugLog = false;

        /** 计算出累计减度 s 实际可生效的减免上限（已乘以 cap）。 */
        public float applyCap(float s) {
            if (!enabled) {
                return s; // 关闭时原样保留，不做钳制
            }
            float sigmoid = 1.0F / (1.0F + (float) Math.exp(-steepness * (s - midpoint)));
            return Math.max(0.0F, Math.min(cap, cap * sigmoid));
        }
    }
}
