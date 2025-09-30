package com.moguang.ctnhmana.common;

import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.moguang.ctnhmana.CMConfig;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMCreativeModeTabs;
import com.moguang.ctnhmana.registry.GTMaterialAddon;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
public class CommonProxy {
    public CommonProxy() {
        init();
        IEventBus modEventBus = FMLJavaModLoadingContext
                .get().getModEventBus();
        modEventBus.addListener(this::addMaterialFlag);
    }
    @SuppressWarnings("removal")
    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CMCreativeModeTabs.init();
        CTNHMana.REGISTRATE.registerRegistrate();

        //CMRecipes.init(modEventBus);
        CMConfig.init();
    }

    public void addMaterialFlag(MaterialEvent event) {
        GTMaterialAddon.init();
    }

}
