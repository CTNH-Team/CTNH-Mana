package com.moguang.ctnhmana.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.render.ShroudGazingRender;
import com.moguang.ctnhmana.client.render.ZenithMatrixRender;
import com.moguang.ctnhmana.registry.CMMobEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.joml.Matrix4f;
import java.util.Random;
import java.io.IOException;
@SuppressWarnings("removal")


@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeRegister {

    private static final Random SHAKE_RANDOM = new Random();

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiOverlayEvent.Post event) {
        ShroudGazingRender.renderPurpleTint(event.getGuiGraphics().pose(), event.getPartialTick());
        renderFormationEffects(event.getGuiGraphics(), event.getPartialTick());
    }

    private static void renderFormationEffects(GuiGraphics guiGraphics, float partialTick) {
        int animTicks = ZenithMatrixRender.formationAnimTicks;
        if (animTicks <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();

        int elapsed = ZenithMatrixRender.FORMATION_DURATION - animTicks;

        if (elapsed < ZenithMatrixRender.FLASH_DURATION) {
            float flashProgress = (float) elapsed / ZenithMatrixRender.FLASH_DURATION;

            float alpha = elapsed < 3 ? (float) elapsed / 3.0f : 1.0f - (flashProgress - 0.3f / 0.7f);
            alpha = Math.max(0, Math.min(1, 1.0f - flashProgress));
            int alphaInt = (int) (alpha * 180);
            if (alphaInt > 0) {
                guiGraphics.fill(0, 0, screenW, screenH,
                        (alphaInt << 24) | 0xFFFFFF);
            }
        }

        int shakeStart = ZenithMatrixRender.SHAKE_DELAY;
        int shakeEnd = shakeStart + ZenithMatrixRender.SHAKE_DURATION;
        if (elapsed >= shakeStart && elapsed < shakeEnd) {
            float shakeProgress = (float) (elapsed - shakeStart) / ZenithMatrixRender.SHAKE_DURATION;
            float intensity = (1.0f - shakeProgress) * 4.0f; // 随时间衰减，最大4像素偏移
            float offsetX = (SHAKE_RANDOM.nextFloat() * 2 - 1) * intensity;
            float offsetY = (SHAKE_RANDOM.nextFloat() * 2 - 1) * intensity;

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(offsetX, offsetY, 0);

            int shakeAlpha = (int) ((1.0f - shakeProgress) * 40);
            if (shakeAlpha > 0) {
                guiGraphics.fill(0, 0, screenW, screenH,
                        (shakeAlpha << 24) | 0x000000);
            }
            guiGraphics.pose().popPose();
        }
    }

    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player != null && player.hasEffect(CMMobEffects.ShroudGazing.get())) {
            event.setNewFovModifier((float) (event.getNewFovModifier() * 0.8));
        }

        int animTicks = ZenithMatrixRender.formationAnimTicks;
        if (animTicks > 0) {
            int elapsed = ZenithMatrixRender.FORMATION_DURATION - animTicks;
            int shakeStart = ZenithMatrixRender.SHAKE_DELAY;
            int shakeEnd = shakeStart + ZenithMatrixRender.SHAKE_DURATION;
            if (elapsed >= shakeStart && elapsed < shakeEnd) {
                float shakeProgress = (float) (elapsed - shakeStart) / ZenithMatrixRender.SHAKE_DURATION;
                float fovPulse = (float) Math.sin(elapsed * 0.8) * (1.0f - shakeProgress) * 0.05f;
                event.setNewFovModifier(event.getNewFovModifier() + fovPulse);
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ZenithMatrixRender.tickClientEffects();
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            if (ZenithMatrixRender.hasSkyEffectSource()) {
                renderZenithSkyEffect(event);
            }
        }
    }

    @SuppressWarnings("removal")
    private static void renderZenithSkyEffect(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) return;

        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        float partialTick = event.getPartialTick();
        int ticks = (int) level.getGameTime();
        Vec3 anchorOffset = ZenithMatrixRender.getSkyEffectAnchorOffset(
                camera.getPosition(),
                level.getMaxBuildHeight());
        if (anchorOffset == null) return;

        float skyAlpha = 1.0f;
        int animTicks = ZenithMatrixRender.formationAnimTicks;
        if (animTicks > 0) {
            int elapsed = ZenithMatrixRender.FORMATION_DURATION - animTicks;

            int skyFadeStart = ZenithMatrixRender.SHAKE_DELAY + ZenithMatrixRender.SHAKE_DURATION;
            int skyFadeDuration = 30;
            if (elapsed < skyFadeStart) {
                skyAlpha = 0.0f;
            } else if (elapsed < skyFadeStart + skyFadeDuration) {
                skyAlpha = (float) (elapsed - skyFadeStart) / skyFadeDuration; // 渐入
            }
        }

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);

        ShaderInstance galaxyShader = ClientProxy.getZenithShader();
        if (galaxyShader != null) {
            float radius = ZenithMatrixRender.SKY_EFFECT_RADIUS;
            float centerX = (float) anchorOffset.x;
            float centerY = (float) anchorOffset.y;
            float centerZ = (float) anchorOffset.z;
            Matrix4f skyMatrix = poseStack.last().pose();

            RenderSystem.setShader(ClientProxy::getZenithShader);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, skyAlpha);

            if (galaxyShader.safeGetUniform("Time") != null) {
                galaxyShader.safeGetUniform("Time").set((ticks + partialTick) * 0.05f);
            }

            RenderSystem.disableCull();
            RenderSystem.depthMask(false);

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            bufferbuilder.vertex(skyMatrix, centerX - radius, centerY, centerZ - radius).uv(-1.0F, -1.0F).endVertex();
            bufferbuilder.vertex(skyMatrix, centerX + radius, centerY, centerZ - radius).uv(1.0F, -1.0F).endVertex();
            bufferbuilder.vertex(skyMatrix, centerX + radius, centerY, centerZ + radius).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(skyMatrix, centerX - radius, centerY, centerZ + radius).uv(-1.0F, 1.0F).endVertex();

            tesselator.end();

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableCull();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}