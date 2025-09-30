package com.moguang.ctnhmana;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
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

}
