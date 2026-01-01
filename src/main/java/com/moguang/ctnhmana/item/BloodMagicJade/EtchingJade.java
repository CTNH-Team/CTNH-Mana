package com.moguang.ctnhmana.item.BloodMagicJade;

import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;

public class EtchingJade extends JadeItem{
    public EtchingJade(Properties properties) {
        super(properties, "etching", etching_upgrade);
    }
    @CN("蚀刻强化")
    public static Lang etching_upgrade;
}
