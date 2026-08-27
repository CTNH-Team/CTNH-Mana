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

    @Configurable
    @Configurable.Comment({
            "Spark mana-flow particle controls. Server keys shape what the server sends,",
            "client keys shape what your own game draws locally." })
    public SparkParticles sparkParticles = new SparkParticles();

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

    public static class SparkParticles {

        @Configurable
        @Configurable.Comment({ "Master switch for spark mana-flow particles (no packets and no particles when off)",
                "Default: true" })
        public boolean enabled = true;

        @Configurable
        @Configurable.Comment({ "SERVER: send one flow hint every N ticks for vanilla Botania sparks (1 = vanilla)",
                "Clients keep drawing the connection locally in between. Default: 20" })
        public int botaniaHintIntervalTicks = 20;

        @Configurable
        @Configurable.Comment({ "SERVER: only send flow hints to players within this radius of the particle origin",
                "Clients discard these particles past 32 blocks anyway; 34 keeps a small margin. Default: 34" })
        @Configurable.DecimalRange(min = 1.0, max = 128.0)
        public float botaniaHintRadius = 34.0F;

        @Configurable
        @Configurable.Comment({ "SERVER: refresh the spire spark connection table at most every N ticks",
                "Default: 10" })
        public int spireSyncIntervalTicks = 10;

        @Configurable
        @Configurable.Comment({ "SERVER: maximum connections synced per spire spark", "Default: 64" })
        public int spireMaxSyncedTargets = 64;

        @Configurable
        @Configurable.Comment({ "CLIENT: particles drawn per active connection per tick (0 = draw nothing)",
                "Default: 1" })
        public int clientParticlesPerConnection = 1;

        @Configurable
        @Configurable.Comment({ "CLIENT: keep drawing a vanilla spark connection for this many ticks after a hint",
                "Should stay above botaniaHintIntervalTicks. Default: 25" })
        public int clientFlowTtlTicks = 25;

        @Configurable
        @Configurable.Comment({ "CLIENT: hard cap on spark flow particles per client tick", "Default: 96" })
        public int clientMaxParticlesPerTick = 96;

        public int hintInterval() {
            return Math.max(1, botaniaHintIntervalTicks);
        }

        public double hintRadius() {
            return Math.max(1.0D, botaniaHintRadius);
        }

        public int syncInterval() {
            return Math.max(1, spireSyncIntervalTicks);
        }

        public int maxSyncedTargets() {
            return Math.max(1, spireMaxSyncedTargets);
        }

        public int particlesPerConnection() {
            return Math.max(0, clientParticlesPerConnection);
        }

        public int flowTtl() {
            return Math.max(1, clientFlowTtlTicks);
        }

        public int maxParticlesPerTick() {
            return Math.max(0, clientMaxParticlesPerTick);
        }
    }

    private static final SparkParticles SPARK_FALLBACK = new SparkParticles();

    /** 配置可能尚未加载（例如 mixin 早于 {@link #init()} 执行），此时回退到默认值而不是抛 NPE。 */
    public static SparkParticles spark() {
        CMConfig instance = INSTANCE;
        return instance == null || instance.sparkParticles == null ? SPARK_FALLBACK : instance.sparkParticles;
    }
}
