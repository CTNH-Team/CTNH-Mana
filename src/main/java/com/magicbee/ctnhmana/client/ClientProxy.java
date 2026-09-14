package com.magicbee.ctnhmana.client;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;

import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.magicbee.ctnhmana.CTNHMana;
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

    /**
     * 给究极魔力锭的烘焙结果套一层紫色迷雾包装（{@link UltraManaMistModel}）。
     *
     * <p>物品本体由 GTCEu 在客户端动态资源包里生成为 {@code {"parent": "gtceu:item/material_sets/ultramana/ingot"}}，
     * 自定义模型 loader 写在父模型上不会被 Forge 采用，因此这里在烘焙完成后按物品 id 替换掉整个 baked model。
     */
    @SubscribeEvent
    public void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation location = new ModelResourceLocation(CTNHMana.id("ultra_mana_ingot"), "inventory");
        BakedModel original = event.getModels().get(location);
        if (original == null) {
            CTNHMana.LOGGER.warn("未找到 {} 的烘焙模型，究极魔力锭的紫色迷雾未启用", location);
            return;
        }
        event.getModels().put(location, new UltraManaMistModel(original));
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
