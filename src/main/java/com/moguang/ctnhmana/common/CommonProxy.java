package com.moguang.ctnhmana.common;

import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.moguang.ctnhmana.CMConfig;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.data.CMDatagen;
import com.moguang.ctnhmana.integration.jade.BaseManaMachineStatusProvider;
import com.moguang.ctnhmana.integration.jade.ManaHatchStatusProvider;
import com.moguang.ctnhmana.integration.jade.ThirdEyeStatusProvider;
import com.moguang.ctnhmana.registry.CMCreativeModeTabs;
import com.moguang.ctnhmana.registry.GTMaterialAddon;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.vixhentx.mcmod.ctnhlib.jade.JadePriorityManager;

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
        CMDatagen.init();
        //CMRecipes.init(modEventBus);
        CMConfig.init();
        JadePriorityManager.registerBlockData(
                new ManaHatchStatusProvider(),
                BlockEntity.class,
                900,
                "mana_hatch_status");

        JadePriorityManager.registerBlockComponent(
                new ManaHatchStatusProvider(),
                Block.class,
                900,
                "mana_hatch_status")
        ;
        JadePriorityManager.registerBlockData(
                new BaseManaMachineStatusProvider(),
                BlockEntity.class,
                900,
                "mana_base_machine_status");

        JadePriorityManager.registerBlockComponent(
                new BaseManaMachineStatusProvider(),
                Block.class,
                900,
                "mana_base_machine_status")
        ;
        JadePriorityManager.registerBlockData(
                new ThirdEyeStatusProvider(),
                BlockEntity.class,
                900,
                "mana_pool_status");

        JadePriorityManager.registerBlockComponent(
                new ThirdEyeStatusProvider(),
                Block.class,
                900,
                "mana_pool_status")
        ;
    }

    public void addMaterialFlag(MaterialEvent event) {
        GTMaterialAddon.init();
    }

}
