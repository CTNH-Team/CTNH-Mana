package com.moguang.ctnhmana.data.lang;

import net.minecraftforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Map;

import static com.gregtechceu.gtceu.api.GTValues.*;


public class ChineseLangHandler {
    public static void init(RegistrateCNLangProvider provider){

    }

    public static void replace(@NotNull RegistrateCNLangProvider provider, @NotNull String key,
                               @NotNull String value) {
        try {
            // the regular lang mappings
            Field field = LanguageProvider.class.getDeclaredField("data");
            field.setAccessible(true);
            // noinspection unchecked
            Map<String, String> map = (Map<String, String>) field.get(provider);
            map.put(key, value);

            // upside-down lang mappings
//            Field upsideDownField = RegistrateLangProvider.class.getDeclaredField("upsideDown");
//            upsideDownField.setAccessible(true);
//            // noinspection unchecked
//            map = (Map<String, String>) field.get(upsideDownField.get(provider));
//
//            Method toUpsideDown = RegistrateLangProvider.class.getDeclaredMethod("toUpsideDown",
//                    String.class);
//            toUpsideDown.setAccessible(true);
//
//            map.put(key, (String) toUpsideDown.invoke(provider, value));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error replacing entry in datagen.", e);
        }
    }
}
