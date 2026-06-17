package com.moguang.ctnhmana.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.entity.player.Player;
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
import com.mojang.math.Axis;
import org.joml.Matrix4f;

import java.util.Random;

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
            if (ZenithMatrixRender.skyEffectTicks > 0) {
                ZenithMatrixRender.skyEffectTicks--;
            }
            if (ZenithMatrixRender.formationAnimTicks > 0) {
                ZenithMatrixRender.formationAnimTicks--;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            if (ZenithMatrixRender.skyEffectTicks > 0) {
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
            poseStack.pushPose();

            float timeOfDay = level.getTimeOfDay(partialTick);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(timeOfDay * 360.0F));
            Matrix4f skyMatrix = poseStack.last().pose();

            RenderSystem.setShader(ClientProxy::getZenithShader);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, skyAlpha);

            if (galaxyShader.safeGetUniform("GameTime") != null) {
                galaxyShader.safeGetUniform("GameTime").set((ticks + partialTick) * 0.01f);
            }
            if (galaxyShader.safeGetUniform("CameraYawPitch") != null) {
                galaxyShader.safeGetUniform("CameraYawPitch").set(
                        camera.getYRot(), camera.getXRot());
            }

            RenderSystem.disableCull();
            RenderSystem.depthMask(false);

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

            float radius = 512.0F;
            int stacks = 48;
            int slices = 96;

            for (int i = 0; i < stacks; i++) {
                float v0 = (float) i / stacks;
                float v1 = (float) (i + 1) / stacks;
                float theta0 = (float) Math.PI * v0;
                float theta1 = (float) Math.PI * v1;
                float y0 = (float) Math.cos(theta0) * radius;
                float y1 = (float) Math.cos(theta1) * radius;
                float r0 = (float) Math.sin(theta0) * radius;
                float r1 = (float) Math.sin(theta1) * radius;

                for (int j = 0; j < slices; j++) {
                    float u0 = (float) j / slices;
                    float u1 = (float) (j + 1) / slices;
                    float phi0 = (float) (u0 * Math.PI * 2.0);
                    float phi1 = (float) (u1 * Math.PI * 2.0);

                    float x00 = (float) Math.cos(phi0) * r0, z00 = (float) Math.sin(phi0) * r0;
                    float x01 = (float) Math.cos(phi1) * r0, z01 = (float) Math.sin(phi1) * r0;
                    float x10 = (float) Math.cos(phi0) * r1, z10 = (float) Math.sin(phi0) * r1;
                    float x11 = (float) Math.cos(phi1) * r1, z11 = (float) Math.sin(phi1) * r1;

                    bufferbuilder.vertex(skyMatrix, x00, y0, z00).endVertex();
                    bufferbuilder.vertex(skyMatrix, x10, y1, z10).endVertex();
                    bufferbuilder.vertex(skyMatrix, x11, y1, z11).endVertex();
                    bufferbuilder.vertex(skyMatrix, x01, y0, z01).endVertex();
                }
            }

            tesselator.end();

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableCull();
            poseStack.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}
