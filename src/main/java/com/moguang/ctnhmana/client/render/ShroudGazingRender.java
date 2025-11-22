package com.moguang.ctnhmana.client.render;

import com.moguang.ctnhmana.registry.CMMobEffects;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShroudGazingRender {
    // 眼睛区域大小系数（0.2=椭圆宽度为屏幕20%，可调整）
    private static final float EYE_SIZE_RATIO = 0.2f;
    // 眼睛区域柔边系数（越大边缘越柔和，0.05~0.15为宜）
    private static final float EYE_EDGE_SMOOTH = 0.05f;
    // 遮罩基础透明度（可手动调整，0.0~1.0）
    private static final float MASK_BASE_ALPHA = 0.85f;
    public static void renderPurpleTint(PoseStack matrixStack, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        // 只有当玩家有该状态效果时才渲染
        if (player == null || !player.hasEffect(CMMobEffects.ShroudGazing.get())) {
            return;
        }

        // ========== 关键修正1：保存当前渲染状态（避免污染其他渲染） ==========
        matrixStack.pushPose();    // 双重保护，避免矩阵冲突

        // 修复：使用更适合遮罩的混合模式
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
        );
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        // 基础渲染设置（禁用深度测试，确保遮罩在最上层）
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();

        // ========== 2. 修复：渲染顺序反转 → 先遮罩（顶层），后滤镜（底层） ==========
        // 眼睛遮罩Z轴设为-90（最顶层），滤镜设为-89（底层）
        drawPurpleFilter(bufferBuilder, matrixStack, minecraft);
        drawVignetteMask(bufferBuilder, matrixStack, minecraft,0.5f);

//        drawPurpleFilter(bufferBuilder, matrixStack, minecraft);

        // ========== 3. 修复：渲染状态恢复顺序（先pop矩阵，再恢复其他状态） ==========
        matrixStack.popPose();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * 绘制全屏淡紫色滤镜（保留原效果）
     */
    private static void drawPurpleFilter(BufferBuilder bufferBuilder, PoseStack matrixStack, Minecraft minecraft) {
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        float filterAlpha = 0.025f;
        float centerX = screenWidth / 2.0f;
        float centerY = screenHeight / 2.0f;

        // 椭圆（眼睛）尺寸配置
        float eyeWidth = screenWidth * 1f;    // 眼睛宽度（屏幕20%）
        float eyeHeight = screenHeight * 0.14f; // 眼睛高度（宽度的70%，更修长）
        float edgeSmooth = 0.08f;               // 边缘柔边（避免生硬）
        float maskAlpha = 0.5f;
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        // 修复：Z轴改为-89（低于遮罩）
        bufferBuilder.vertex(matrixStack.last().pose(), 0, screenHeight, -89)
                .color(0.6f, 0.0f, 1.0f, filterAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, screenHeight, -89)
                .color(0.6f, 0.0f, 1.0f, filterAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, 0, -89)
                .color(0.6f, 0.0f, 1.0f, filterAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), 0, 0, -89)
                .color(0.6f, 0.0f, 1.0f, filterAlpha).endVertex();
        Tesselator.getInstance().end();


    }

    private static void drawVignetteMask(BufferBuilder bufferBuilder, PoseStack matrixStack, Minecraft minecraft, float vignetteAlpha) {
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        float FOV_SCALE = 0.8f; // 和 FovEventHandler 中保持一致
        int maskWidth = (int) (screenWidth * FOV_SCALE);
        int maskHeight = (int) (screenHeight * FOV_SCALE);
        int maskX = (screenWidth - maskWidth) / 2;
        int maskY = (screenHeight - maskHeight) / 2;

        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        // 顶部遮罩（使用传入的vignetteAlpha）
        bufferBuilder.vertex(matrixStack.last().pose(), 0, maskY, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, maskY, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, 0, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), 0, 0, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();

        // 底部遮罩（同理）
        bufferBuilder.vertex(matrixStack.last().pose(), 0, screenHeight, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, screenHeight, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, maskY + maskHeight, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), 0, maskY + maskHeight, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        // ========== 3. 左侧遮罩（覆盖：maskX+maskWidth到屏幕宽度 + maskY到maskY+maskHeight高度） ==========
        bufferBuilder.vertex(matrixStack.last().pose(), 0, maskY + maskHeight, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), maskX, maskY + maskHeight, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), maskX, maskY, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), 0, maskY, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();

        // ========== 4. 右侧遮罩（覆盖：maskX+maskWidth到屏幕宽度 + maskY到maskY+maskHeight高度） ==========
        bufferBuilder.vertex(matrixStack.last().pose(), maskX + maskWidth, maskY + maskHeight, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, maskY + maskHeight, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, maskY, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), maskX + maskWidth, maskY, -89)
                .color(0.0f, 0.0f, 0.0f, vignetteAlpha).endVertex();



        Tesselator.getInstance().end();
    }
    private static void drawEyeMask(BufferBuilder bufferBuilder, PoseStack matrixStack, Minecraft minecraft) {
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        float centerX = screenWidth / 2.0f;
        float centerY = screenHeight / 2.0f;

        // 椭圆（眼睛）尺寸配置
        float eyeWidth = screenWidth * 0.01f;    // 眼睛宽度（屏幕20%）
        float eyeHeight = screenHeight * 0.014f; // 眼睛高度（宽度的70%，更修长）
        float edgeSmooth = 0.008f;               // 边缘柔边（避免生硬）
        float maskAlpha = 0.05f;                // 遮罩透明度（接近纯黑）

        // ========== 核心：先绘制全屏不透明黑罩（覆盖所有区域） ==========
        bufferBuilder.clear();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        // 全屏黑色遮罩（Z轴-90，顶层）
        bufferBuilder.vertex(matrixStack.last().pose(), 0, 0, -89)
                .color(0.0f, 0.0f, 0.0f, maskAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, 0, -89)
                .color(0.0f, 0.0f, 0.0f, maskAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), screenWidth, screenHeight, -89)
                .color(0.0f, 0.0f, 0.0f, maskAlpha).endVertex();
        bufferBuilder.vertex(matrixStack.last().pose(), 0, screenHeight, -89)
                .color(0.0f, 0.0f, 0.0f, maskAlpha).endVertex();
        Tesselator.getInstance().end();

        // ========== 关键：在黑罩上挖去椭圆（眼睛）区域（透明绘制） ==========
//        bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
//
//        // 步骤1：椭圆中心顶点（完全透明，作为三角形扇的中心）
//        bufferBuilder.vertex(matrixStack.last().pose(), centerX, centerY, -90)
//                .color(0.0f, 0.0f, 0.0f, 0.0f).endVertex();
//
//        // 步骤2：绘制椭圆边缘顶点（形成透明的椭圆区域）
//        int segments = 128; // 分段数越高，椭圆越平滑
//        for (int i = 0; i <= segments; i++) {
//            float angle = (float) (i * 2 * Math.PI / segments);
//
//            // 计算椭圆边缘坐标（精准贴合眼睛形状）
//            float x = centerX + (float) (eyeWidth * Math.cos(angle));
//            float y = centerY + (float) (eyeHeight * Math.sin(angle));
//
//            // 计算边缘柔边（从完全透明到遮罩透明度的过渡）
//            float dx = (x - centerX) / eyeWidth;
//            float dy = (y - centerY) / eyeHeight;
//            float distance = (float) Math.sqrt(dx * dx + dy * dy);
//
//            float alpha;
//            if (distance < 1.0f - edgeSmooth) {
//                alpha = 0.0f; // 椭圆内部：完全透明（可视区域）
//            } else if (distance > 1.0f + edgeSmooth) {
//                alpha = maskAlpha; // 椭圆外部：恢复遮罩透明度（不挖空）
//            } else {
//                // 边缘柔边：线性插值实现平滑过渡
//                alpha = maskAlpha * ((distance - (1.0f - edgeSmooth)) / (2 * edgeSmooth));
//            }
//
//            // 绘制椭圆边缘顶点（透明挖空）
//            bufferBuilder.vertex(matrixStack.last().pose(), x, y, -90)
//                    .color(0.0f, 0.0f, 0.0f, alpha).endVertex();
//        }

        // 提交绘制（完成椭圆挖空）
        Tesselator.getInstance().end();
    }



}
