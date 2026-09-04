package com.magicbee.ctnhmana.integration.jade;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/** Registers Jade information for non-GT machines and Botania objects. */
@WailaPlugin
public final class CTNHManaJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ThirdEyeStatusProvider(), BlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ThirdEyeStatusProvider(), Block.class);
    }
}
