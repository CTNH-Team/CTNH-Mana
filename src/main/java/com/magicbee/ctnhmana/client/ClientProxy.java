package com.magicbee.ctnhmana.client;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;

import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.client.fx.SparkFlowClientTracker;
import com.magicbee.ctnhmana.client.ponder.CTNHManaPonderPlugin;
import com.magicbee.ctnhmana.client.render.*;
import com.magicbee.ctnhmana.client.render.particle.IconParticle;
import com.magicbee.ctnhmana.common.CommonProxy;
import com.magicbee.ctnhmana.common.item.equipment.SaberWandItem;
import com.magicbee.ctnhmana.registry.CMItems;
import com.magicbee.ctnhmana.registry.CMModelLayers;
import com.magicbee.ctnhmana.registry.CMParticleTypes;
import com.mojang.blaze3d.vertex.*;
import lombok.Getter;

import java.io.IOException;

@SuppressWarnings("removal")
public class ClientProxy extends CommonProxy {

    @Getter
    private static ShaderInstance zenithShader;
    @Getter
    private static ShaderInstance zenithBeamShader;

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
        // 原版火花流动提示的客户端本地续画
        MinecraftForge.EVENT_BUS.register(SparkFlowClientTracker.class);
    }

    @SubscribeEvent
    public void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        new ResourceLocation(CTNHMana.MODID, "zenith"),
                        DefaultVertexFormat.POSITION_TEX),
                shaderInstance -> zenithShader = shaderInstance);
        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        new ResourceLocation(CTNHMana.MODID, "zenith_beam"),
                        DefaultVertexFormat.POSITION_TEX),
                shaderInstance -> zenithBeamShader = shaderInstance);
    }

    @SubscribeEvent
    public void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CMParticleTypes.INDEX_TARGET.get(), IconParticle.Provider::new);
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CMModelLayers.init();
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    CMItems.SABER_WAND.get(), // 目标物品
                    new ResourceLocation(CTNHMana.MODID, "wand_status"),
                    (stack, level, entity, seed) -> {
                        if (!SaberWandItem.getBindMode(stack)) return 1.0f;
                        return 0f;
                    });
            ItemProperties.register(
                    CMItems.CADUCEUS.get(),
                    new ResourceLocation(CTNHMana.MODID, "tool_type"),
                    (stack, level, entity, seed) -> {
                        if (stack.getTag().contains("caduceus_type_index")) {
                            return stack.getTag().getFloat("caduceus_type_index") / 12f;
                        }
                        return 0f;
                    });
            PonderIndex.addPlugin(new CTNHManaPonderPlugin());
        });
    }
}
