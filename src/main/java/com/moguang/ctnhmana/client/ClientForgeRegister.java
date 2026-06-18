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

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeRegister {

    private static final Random SHAKE_RANDOM = new Random();

    // 天顶主题色：紫-粉
    private static final int FLASH_PEAK_COLOR = 0xF5D0FF;
    private static final int FLASH_TAIL_COLOR = 0xB866FF;

    // 闪屏在睁眼之前结束，避免遮挡天空裂缝睁开
    private static final int FLASH_MAX_ALPHA = 70;

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

            // 快速起峰后衰减，持续时间短、不透明度低
            float alpha = elapsed < 2 ? (float) elapsed / 2.0f : 1.0f - flashProgress;
            alpha = Math.max(0, Math.min(1, alpha));
            int alphaInt = (int) (alpha * FLASH_MAX_ALPHA);
            if (alphaInt > 0) {
                int color = lerpColor(FLASH_PEAK_COLOR, FLASH_TAIL_COLOR, alpha);
                guiGraphics.fill(0, 0, screenW, screenH,
                        (alphaInt << 24) | color);
            }
        }

        int shakeStart = ZenithMatrixRender.SHAKE_DELAY;
        int shakeEnd = shakeStart + ZenithMatrixRender.SHAKE_DURATION;
        if (elapsed >= shakeStart && elapsed < shakeEnd) {
            float shakeProgress = (float) (elapsed - shakeStart) / ZenithMatrixRender.SHAKE_DURATION;
            float intensity = (1.0f - shakeProgress) * 4.0f;
            float offsetX = (SHAKE_RANDOM.nextFloat() * 2 - 1) * intensity;
            float offsetY = (SHAKE_RANDOM.nextFloat() * 2 - 1) * intensity;

            // 仅保留屏幕位移，不叠加黑色层，避免遮挡睁眼
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(offsetX, offsetY, 0);
            guiGraphics.pose().popPose();
        }
    }

    private static int lerpColor(int from, int to, float t) {
        int r = (int) (((from >> 16) & 0xFF) * t + ((to >> 16) & 0xFF) * (1.0f - t));
        int g = (int) (((from >> 8) & 0xFF) * t + ((to >> 8) & 0xFF) * (1.0f - t));
        int b = (int) ((from & 0xFF) * t + (to & 0xFF) * (1.0f - t));
        return (r << 16) | (g << 8) | b;
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

        int elapsed = 0;
        boolean forming = ZenithMatrixRender.formationAnimTicks > 0;
        if (forming) {
            elapsed = ZenithMatrixRender.FORMATION_DURATION - ZenithMatrixRender.formationAnimTicks;
        }

        // 天空裂缝睁开动画：闪屏结束后开始，带轻微回弹
        float eyeOpen = 1.0f;
        float skyAlpha = 1.0f;
        if (forming) {
            int eyeOpenStart = ZenithMatrixRender.FLASH_DURATION + 2;
            int eyeOpenDuration = 36;
            if (elapsed < eyeOpenStart) {
                eyeOpen = 0.0f;
            } else if (elapsed < eyeOpenStart + eyeOpenDuration) {
                float p = (float) (elapsed - eyeOpenStart) / eyeOpenDuration;
                eyeOpen = p < 0.85f ? easeOutBack(p / 0.85f) : 1.0f;
            } else {
                eyeOpen = 1.0f;
            }

            int skyFadeStart = ZenithMatrixRender.SHAKE_DELAY + ZenithMatrixRender.SHAKE_DURATION;
            int skyFadeDuration = 30;
            if (elapsed < skyFadeStart) {
                skyAlpha = 0.0f;
            } else if (elapsed < skyFadeStart + skyFadeDuration) {
                float p = (float) (elapsed - skyFadeStart) / skyFadeDuration;
                skyAlpha = p * p * (3.0f - 2.0f * p);
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

            if (galaxyShader.safeGetUniform("EyeOpen") != null) {
                galaxyShader.safeGetUniform("EyeOpen").set(eyeOpen);
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

    private static float easeOutBack(float x) {
        float c1 = 1.70158f;
        float c3 = c1 + 1.0f;
        return 1.0f + c3 * (float) Math.pow(x - 1.0f, 3) + c1 * (float) Math.pow(x - 1.0f, 2);
    }
}
