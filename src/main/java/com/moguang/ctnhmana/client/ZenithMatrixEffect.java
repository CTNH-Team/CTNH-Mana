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

/**
 * 客户端 Forge 事件监听器，负责渲染天顶矩阵相关的屏幕效果与天空层特效。
 *
 * 包含：
 * 1. 形成瞬间的屏幕闪光与震动。
 * 2. 天空裂缝（zenith sky rift）渲染。
 * 3. FOV 脉冲（与屏幕震动同步）。
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ZenithMatrixEffect {

    /** 屏幕震动随机数生成器。 */
    private static final Random SHAKE_RANDOM = new Random();

    // ================= 天顶主题色：紫-粉 =================
    /** 闪光峰值颜色：较亮的粉紫色。 */
    private static final int FLASH_PEAK_COLOR = 0xF5D0FF;
    /** 闪光衰减后的颜色：较深的紫色。 */
    private static final int FLASH_TAIL_COLOR = 0xB866FF;

    /**
     * 闪屏最大不透明度（0-255）。
     * 取值较低且闪光在睁眼之前结束，避免 GUI 层遮挡天空裂缝睁开动画。
     */
    private static final int FLASH_MAX_ALPHA = 70;

    /**
     * GUI 渲染后事件：先渲染“虚境凝视”的紫色 tint，再叠加形成特效（闪光/震动）。
     */
    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiOverlayEvent.Post event) {
        ShroudGazingRender.renderPurpleTint(event.getGuiGraphics().pose(), event.getPartialTick());
        renderFormationEffects(event.getGuiGraphics(), event.getPartialTick());
    }

    /**
     * 渲染形成动画期间的一次性屏幕效果：闪光 + 震动。
     *
     * @param guiGraphics GuiGraphics 实例
     * @param partialTick 当前帧插值
     */
    private static void renderFormationEffects(GuiGraphics guiGraphics, float partialTick) {
        int animTicks = ZenithMatrixRender.formationAnimTicks;
        // 没有正在播放的形成动画时直接返回。
        if (animTicks <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();

        // elapsed：从形成动画开始到现在经过的刻数，0 ~ FORMATION_DURATION。
        int elapsed = ZenithMatrixRender.FORMATION_DURATION - animTicks;

        // ========== 屏幕闪光 ==========
        if (elapsed < ZenithMatrixRender.FLASH_DURATION) {
            float flashProgress = (float) elapsed / ZenithMatrixRender.FLASH_DURATION;

            // 快速起峰：前 2 刻从 0 升到 1；之后随 flashProgress 衰减。
            float alpha = elapsed < 2 ? (float) elapsed / 2.0f : 1.0f - flashProgress;
            alpha = Math.max(0, Math.min(1, alpha));
            int alphaInt = (int) (alpha * FLASH_MAX_ALPHA);
            if (alphaInt > 0) {
                // 颜色随 alpha 从峰值色过渡到尾色，使闪光更有能量感。
                int color = lerpColor(FLASH_PEAK_COLOR, FLASH_TAIL_COLOR, alpha);
                guiGraphics.fill(0, 0, screenW, screenH,
                        (alphaInt << 24) | color);
            }
        }

        // ========== 屏幕震动 ==========
        int shakeStart = ZenithMatrixRender.SHAKE_DELAY;
        int shakeEnd = shakeStart + ZenithMatrixRender.SHAKE_DURATION;
        if (elapsed >= shakeStart && elapsed < shakeEnd) {
            float shakeProgress = (float) (elapsed - shakeStart) / ZenithMatrixRender.SHAKE_DURATION;
            // 震动强度随时间衰减，最大 4 像素偏移。
            float intensity = (1.0f - shakeProgress) * 4.0f;
            float offsetX = (SHAKE_RANDOM.nextFloat() * 2 - 1) * intensity;
            float offsetY = (SHAKE_RANDOM.nextFloat() * 2 - 1) * intensity;

            // 仅保留屏幕位移，不叠加黑色层，避免遮挡天空裂缝睁眼。
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(offsetX, offsetY, 0);
            guiGraphics.pose().popPose();
        }
    }

    /**
     * 在两个 RGB 颜色之间做线性插值。
     *
     * @param from 起始颜色
     * @param to   目标颜色
     * @param t    插值系数，越接近 1 越偏向 from
     * @return 插值后的 RGB 整数
     */
    private static int lerpColor(int from, int to, float t) {
        int r = (int) (((from >> 16) & 0xFF) * t + ((to >> 16) & 0xFF) * (1.0f - t));
        int g = (int) (((from >> 8) & 0xFF) * t + ((to >> 8) & 0xFF) * (1.0f - t));
        int b = (int) ((from & 0xFF) * t + (to & 0xFF) * (1.0f - t));
        return (r << 16) | (g << 8) | b;
    }

    /**
     * FOV 修改事件：
     * 1. 玩家拥有“虚境凝视”效果时缩小 FOV。
     * 2. 形成动画震动期间加入正弦脉冲，增强冲击感。
     */
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
                // 随 shakeProgress 衰减的正弦波动，幅度最大 0.05。
                float fovPulse = (float) Math.sin(elapsed * 0.8) * (1.0f - shakeProgress) * 0.05f;
                event.setNewFovModifier(event.getNewFovModifier() + fovPulse);
            }
        }
    }

    /** 客户端每刻结束时更新效果计时器。 */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ZenithMatrixRender.tickClientEffects();
        }
    }

    /** 世界渲染阶段事件：在天空渲染之后绘制天顶裂缝。 */
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            if (ZenithMatrixRender.hasSkyEffectSource()) {
                renderZenithSkyEffect(event);
            }
        }
    }

    /**
     * 渲染天空裂缝。
     * 使用注册的 zenith shader，在世界坐标上方绘制一个巨大的水平面，
     * 通过 UV 展开表现“睁开的眼睛”。
     *
     * @param event RenderLevelStageEvent
     */
    @SuppressWarnings("removal")
    private static void renderZenithSkyEffect(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) return;

        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        float partialTick = event.getPartialTick();
        int ticks = (int) level.getGameTime();

        // 计算天空锚点相对于相机的偏移。
        Vec3 anchorOffset = ZenithMatrixRender.getSkyEffectAnchorOffset(
                camera.getPosition(),
                level.getMaxBuildHeight());
        if (anchorOffset == null) return;

        // 计算从形成动画开始到现在经过的刻数。
        int elapsed = 0;
        boolean forming = ZenithMatrixRender.formationAnimTicks > 0;
        if (forming) {
            elapsed = ZenithMatrixRender.FORMATION_DURATION - ZenithMatrixRender.formationAnimTicks;
        }

        // ========== 天空裂缝睁开动画 ==========
        // eyeOpen：控制 shader 中眼睛的开合，0 完全闭合，1 完全睁开。
        // skyAlpha：整个天空层的透明度，与 shake 同步渐入。
        float eyeOpen = 1.0f;
        float skyAlpha = 1.0f;
        if (forming) {
            // 睁眼在闪光结束后才开始，避免被闪屏遮住。
            int eyeOpenStart = ZenithMatrixRender.FLASH_DURATION + 2;
            int eyeOpenDuration = 36;
            if (elapsed < eyeOpenStart) {
                eyeOpen = 0.0f;
            } else if (elapsed < eyeOpenStart + eyeOpenDuration) {
                float p = (float) (elapsed - eyeOpenStart) / eyeOpenDuration;
                // 前 85% 使用 easeOutBack（带轻微回弹），后 15% 保持完全睁开。
                eyeOpen = p < 0.85f ? easeOutBack(p / 0.85f) : 1.0f;
            } else {
                eyeOpen = 1.0f;
            }

            // 天空层整体 alpha：在震动结束后开始渐入，使用 smoothstep 曲线。
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

            // 激活自定义 zenith shader。
            RenderSystem.setShader(ClientProxy::getZenithShader);

            // 通过 shaderColor 的 alpha 通道控制天空层整体透明度。
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, skyAlpha);

            // 传递 Uniform：Time 控制 shader 内部所有随时间变化的动画。
            if (galaxyShader.safeGetUniform("Time") != null) {
                galaxyShader.safeGetUniform("Time").set((ticks + partialTick) * 0.05f);
            }

            // 传递 Uniform：EyeOpen 控制眼睛睁开程度。
            if (galaxyShader.safeGetUniform("EyeOpen") != null) {
                galaxyShader.safeGetUniform("EyeOpen").set(eyeOpen);
            }

            // 禁用背面剔除与深度写入，确保天空层不被地形裁剪。
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);

            // 绘制一个巨大的水平四边形作为天空层画布。
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            bufferbuilder.vertex(skyMatrix, centerX - radius, centerY, centerZ - radius).uv(-1.0F, -1.0F).endVertex();
            bufferbuilder.vertex(skyMatrix, centerX + radius, centerY, centerZ - radius).uv(1.0F, -1.0F).endVertex();
            bufferbuilder.vertex(skyMatrix, centerX + radius, centerY, centerZ + radius).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(skyMatrix, centerX - radius, centerY, centerZ + radius).uv(-1.0F, 1.0F).endVertex();

            tesselator.end();

            // 恢复渲染状态。
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableCull();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    /**
     * ease-out-back 缓动：在接近 1 时略微超过目标值再回弹，产生“弹开”感。
     *
     * @param x 输入进度，0 ~ 1
     * @return 缓动后的进度
     */
    private static float easeOutBack(float x) {
        float c1 = 1.70158f;
        float c3 = c1 + 1.0f;
        return 1.0f + c3 * (float) Math.pow(x - 1.0f, 3) + c1 * (float) Math.pow(x - 1.0f, 2);
    }
}
