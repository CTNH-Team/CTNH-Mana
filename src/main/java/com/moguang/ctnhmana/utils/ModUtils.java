package com.moguang.ctnhmana.utils;

import net.minecraft.resources.ResourceLocation;
import vazkii.botania.common.lib.ResourceLocationHelper;
import wayoftime.bloodmagic.BloodMagic;

public class ModUtils {
    public static ResourceLocation BotaniaRL(String path) {
        return ResourceLocationHelper.prefix(path);
    }
    public static ResourceLocation BloodMagicRL(String path) {
        return BloodMagic.rl(path);
    }
}