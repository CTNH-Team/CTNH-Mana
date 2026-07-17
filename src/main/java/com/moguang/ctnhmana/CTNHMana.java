package com.moguang.ctnhmana;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import com.moguang.ctnhmana.client.ClientProxy;
import com.moguang.ctnhmana.common.CommonProxy;
import com.moguang.ctnhmana.registry.CMRegistrate;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod(CTNHMana.MODID)
public class CTNHMana {

    public static final String MODID = "ctnhmana";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String CUSTOM_TAG_SOURCE = "CTNH Custom Tags";
    public static final CMRegistrate REGISTRATE = CMRegistrate.create();

    @SuppressWarnings("removal")
    public CTNHMana() {
        DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.tryParse(MODID + ":" + name);
    }
}
