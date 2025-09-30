package com.moguang.ctnhmana.data.lang.utils;


import net.minecraftforge.common.data.LanguageProvider;

public class EntityPropertyLangUtil {
    LanguageProvider provider;
    public EntityPropertyLangUtil(LanguageProvider provider,String inText, String outText){
        this.provider = provider;

    }
    //example: text = "Name %s %s" verb = "contains" , the result will be "Name contains %s" or "Name : %s"

}
