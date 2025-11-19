package com.moguang.ctnhmana.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShourdGazingRender {
    public static void renderPurpleTint(PoseStack matrixStack, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        // 只有当玩家有该状态效果时才渲染
        if (player == null) {
            return;
        }

        // 渲染设置
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        // 紫色半透明（RGBA：红0.6，绿0，蓝1.0，透明度0.3）
        RenderSystem.setShaderColor(0.6f, 0.0f, 1.0f, 0.3f);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        // 获取屏幕尺寸
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        // 绘制全屏矩形（紫色滤镜）
        bufferbuilder.vertex(matrixStack.last().pose(), 0, screenHeight, -90).color(0.6f, 0.0f, 1.0f, 0.3f).endVertex();
        bufferbuilder.vertex(matrixStack.last().pose(), screenWidth, screenHeight, -90).color(0.6f, 0.0f, 1.0f, 0.3f).endVertex();
        bufferbuilder.vertex(matrixStack.last().pose(), screenWidth, 0, -90).color(0.6f, 0.0f, 1.0f, 0.3f).endVertex();
        bufferbuilder.vertex(matrixStack.last().pose(), 0, 0, -90).color(0.6f, 0.0f, 1.0f, 0.3f).endVertex();

        tesselator.end();
        // 恢复渲染状态
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
