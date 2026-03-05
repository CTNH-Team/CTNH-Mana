package com.moguang.ctnhmana.api.networks;

public enum BotaniaExtendEffectType {

    SPARK_MANA_FLOW(3),
    SPARK_MANA_FLOW_REVERSE(3),
    SPARK_NET_INDICATOR(2);

    public final int argCount;

    private BotaniaExtendEffectType(int argCount) {
        this.argCount = argCount;
    }
}
