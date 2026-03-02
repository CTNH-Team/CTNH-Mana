package com.moguang.ctnhmana.client.gui.radial;

import com.hollingsworth.arsnouveau.client.registry.ModKeyBindings;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.List;

/**
 * 简化的径向菜单界面（仅主图标，无副图标）
 */
public class RadialMenuScreen<T> extends Screen {

    private static final float PRECISION = 5.0f;
    private static final int MAX_SLOTS = 20;
    private static final float OPEN_ANIMATION_LENGTH = 0.25f;

    private final RadialMenu<T> menu;
    private final List<RadialMenuSlot<T>> slots;
    private int selectedItem = -1;
    private float totalTime;
    private float prevTick;
    private float extraTick;

    public RadialMenuScreen(RadialMenu<T> menu) {
        super(Component.literal(""));
        this.menu = menu;
        this.slots = menu.getSlots();
    }

    @Override
    public void tick() {
        if (totalTime < OPEN_ANIMATION_LENGTH) {
            extraTick++;
        }
            int openRadialKey = 78;
            boolean radialKeyIsDown = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), openRadialKey);
            if (!radialKeyIsDown) {
                if (this.selectedItem != -1) {
                    menu.setCurrentSlot(selectedItem);
                }
                minecraft.player.closeContainer();
            }

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        float currTick = partialTicks;
        totalTime += (currTick + extraTick - prevTick) / 20f;
        extraTick = 0;
        prevTick = currTick;

        float openAnimation = Mth.clamp(totalTime / OPEN_ANIMATION_LENGTH, 0, 1);
        openAnimation = (float) (1 - Math.pow(1 - openAnimation, 3));

        float radiusIn = Math.max(0.1f, 45 * openAnimation);
        float radiusOut = radiusIn * 2;
        float itemRadius = (radiusIn + radiusOut) * 0.5f;

        int centerX = width / 2;
        int centerY = height / 2;
        int n = Math.min(MAX_SLOTS, slots.size());

        double mouseAngleDeg = Math.toDegrees(Math.atan2(mouseY - centerY, mouseX - centerX));
        double mouseDist = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));

        float slot0 = (((0 - 0.5f) / (float) n) + 0.25f) * 360;
        if (mouseAngleDeg < slot0) mouseAngleDeg += 360;

        selectedItem = -1;
        for (int i = 0; i < n; i++) {
            float left = (((i - 0.5f) / (float) n) + 0.25f) * 360;
            float right = (((i + 0.5f) / (float) n) + 0.25f) * 360;
            if (mouseAngleDeg >= left && mouseAngleDeg < right && mouseDist >= radiusIn && mouseDist < radiusOut) {
                selectedItem = i;
                break;
            }
        }

        // 绘制扇形
        PoseStack ps = graphics.pose();
        ps.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < n; i++) {
            float left = (((i - 0.5f) / (float) n) + 0.25f) * 360;
            float right = (((i + 0.5f) / (float) n) + 0.25f) * 360;
            if (selectedItem == i) {
                drawSlice(buf, ps, centerX, centerY, 10, radiusIn, radiusOut, left, right, 63, 161, 191, 60);
            } else {
                drawSlice(buf, ps, centerX, centerY, 10, radiusIn, radiusOut, left, right, 0, 0, 0, 64);
            }
        }

        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
        ps.popPose();

        // 选中项名称
        if (selectedItem >= 0 && selectedItem < slots.size()) {
            graphics.drawCenteredString(font, slots.get(selectedItem).slotName(), width / 2, (height - font.lineHeight) / 2, 0xFFFFFF);
        }

        // 主图标（48x48 材质）
        for (int i = 0; i < n; i++) {
            float angle = ((i / (float) n) - 0.25f) * 2 * (float) Math.PI;
            if (n % 2 != 0) angle += Math.PI / n;
            int x = (int) (centerX - 24 + itemRadius * Math.cos(angle));
            int y = (int) (centerY - 24 + itemRadius * Math.sin(angle));

            T icon = slots.get(i).primaryIcon();
            if (icon != null) {
                menu.drawIcon(icon, graphics, x, y, 48);
            }
        }
    }

    private static void drawSlice(BufferBuilder buf, PoseStack ps, float cx, float cy, float z,
                                  float rIn, float rOut, float startDeg, float endDeg,
                                  int r, int g, int b, int a) {
        float angle = endDeg - startDeg;
        int sections = Math.max(1, Mth.ceil(angle / PRECISION));
        startDeg = (float) Math.toRadians(startDeg);
        endDeg = (float) Math.toRadians(endDeg);
        angle = endDeg - startDeg;

        var mat = ps.last().pose();
        for (int i = 0; i < sections; i++) {
            float a1 = startDeg + (i / (float) sections) * angle;
            float a2 = startDeg + ((i + 1) / (float) sections) * angle;

            float x1i = cx + rIn * (float) Math.cos(a1);
            float y1i = cy + rIn * (float) Math.sin(a1);
            float x1o = cx + rOut * (float) Math.cos(a1);
            float y1o = cy + rOut * (float) Math.sin(a1);
            float x2o = cx + rOut * (float) Math.cos(a2);
            float y2o = cy + rOut * (float) Math.sin(a2);
            float x2i = cx + rIn * (float) Math.cos(a2);
            float y2i = cy + rIn * (float) Math.sin(a2);

            buf.vertex(mat, x1o, y1o, z).color(r / 255f, g / 255f, b / 255f, a / 255f).endVertex();
            buf.vertex(mat, x1i, y1i, z).color(r / 255f, g / 255f, b / 255f, a / 255f).endVertex();
            buf.vertex(mat, x2i, y2i, z).color(r / 255f, g / 255f, b / 255f, a / 255f).endVertex();
            buf.vertex(mat, x2o, y2o, z).color(r / 255f, g / 255f, b / 255f, a / 255f).endVertex();
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        int num = key - 48;
        if (num >= 1 && num <= 9 && num <= slots.size()) {
            selectedItem = num - 1;
            confirmSelection();
            return true;
        }
        return super.keyPressed(key, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (selectedItem >= 0) {
            confirmSelection();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void confirmSelection() {
        if (selectedItem >= 0 && selectedItem < slots.size()) {
            menu.setCurrentSlot(selectedItem);
        }
        if (minecraft != null) {
            minecraft.setScreen(null);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
/*
Note: This code has been modified from David Quintana's solution.
Below is the required copyright notice.
Copyright (c) 2015, David Quintana <gigaherz@gmail.com>
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the author nor the
      names of the contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/