package com.moguang.ctnhmana.client;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;

import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
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
import com.mojang.blaze3d.vertex.*;
import lombok.Getter;

import java.io.IOException;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy extends CommonProxy {

    @Getter
    private static ShaderInstance zenithShader;

    public ClientProxy() {
        super();
        init();
    }

    public static void init() {
        DynamicRenderManager.register(CTNHMana.id("zenith_laser"), ZenithMatrixRender.TYPE);
        DynamicRenderManager.register(CTNHMana.id("eternal_garden"), EternalGardenRender.TYPE);
        DynamicRenderManager.register(CTNHMana.id("mana_condenser"), ManaCondenserRender.TYPE);
        DynamicRenderManager.register(CTNHMana.id("mana_reactor"), ManaReactorRender.TYPE);
        DynamicRenderManager.register(CTNHMana.id("demon_will_generator"), DemonWillRender.TYPE);
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        new ResourceLocation(CTNHMana.MODID, "zenith"),
                        DefaultVertexFormat.POSITION),
                shaderInstance -> zenithShader = shaderInstance);
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
    }
}
