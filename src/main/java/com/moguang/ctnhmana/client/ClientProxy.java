package com.moguang.ctnhmana.client;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;

import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.ponder.CTNHManaPonderPlugin;
import com.moguang.ctnhmana.client.render.*;
import com.moguang.ctnhmana.client.render.particle.IconParticle;
import com.moguang.ctnhmana.common.CommonProxy;
import com.moguang.ctnhmana.registry.CMModelLayers;
import com.moguang.ctnhmana.registry.CMParticleTypes;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        init();
    }

    public static void init() {
        DynamicRenderManager.register(CTNHMana.id("zenith_laser"), ZenithMatrixBlockEntityRender.TYPE);
        DynamicRenderManager.register(CTNHMana.id("eternal_garden"), EternalGardenRender.TYPE);
        DynamicRenderManager.register(CTNHMana.id("mana_condenser"), ManaCondenserRender.TYPE);
        DynamicRenderManager.register(CTNHMana.id("mana_reactor"), ManaReactorRender.TYPE);
        DynamicRenderManager.register(CTNHMana.id("demon_will_generator"), DemonWillRender.TYPE);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PonderIndex.addPlugin(new CTNHManaPonderPlugin()));
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CMParticleTypes.INDEX_TARGET.get(), IconParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CMModelLayers.init();
        // var models = REGISTRATE.getModels();
    }

    @SubscribeEvent
    public void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // event.registerEntityRenderer(CBEntities.BASIC_MOB.get(),
        // BasicLivingMachineEntityRenderer::new
        // );
    }
}
