package com.moguang.ctnhmana.data;


import com.moguang.ctnhmana.data.lang.ChineseLangHandler;
import com.moguang.ctnhmana.data.lang.EnglishLangHandler;
import com.moguang.ctnhmana.data.lang.RegistrateCNLangProvider;
import com.tterrag.registrate.providers.ProviderType;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;


public class CMDatagen {
    public static final ProviderType<RegistrateCNLangProvider> CNLANG = ProviderType.register("ctnhbio_cn_lang", (p, e) -> new RegistrateCNLangProvider(p, e.getGenerator().getPackOutput()));

    public static void init() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, EnglishLangHandler::init);
        REGISTRATE.addDataGenerator(CNLANG, ChineseLangHandler::init);

    }
}
