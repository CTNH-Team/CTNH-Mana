package com.moguang.ctnhmana.data;


import com.moguang.ctnhmana.data.lang.ChineseLangHandler;
import com.moguang.ctnhmana.data.lang.EnglishLangHandler;
import com.moguang.ctnhmana.data.tags.FluidTypeTags;
import com.tterrag.registrate.providers.ProviderType;
import org.moddingx.libx.annotation.registration.Reg;
import tech.vixhentx.mcmod.ctnhlib.registrate.lang.RegistrateCNLangProvider;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static tech.vixhentx.mcmod.ctnhlib.registrate.data.ProviderTypes.CNLANG;


public class CMDatagen {

    public static void init() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, EnglishLangHandler::init);
        REGISTRATE.addDataGenerator(CNLANG, ChineseLangHandler::init);
        REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, FluidTypeTags::init);
    }
}
