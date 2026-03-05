package com.moguang.ctnhmana.common.gui;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.texture.AnimationTexture;
import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;

import net.createmod.catnip.annotations.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.Getter;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_TEX_COLOR;

@LDLRegister(name = "animation_texture_Y", group = "texture")
public class AnimationTextureY extends AnimationTexture {

    @Configurable(name = "ldlib.gui.editor.name.resource")
    public ResourceLocation imageLocation;

    @Configurable(tips = "ldlib.gui.editor.tips.cell_size")
    @NumberRange(range = { 1, Integer.MAX_VALUE })
    @Getter
    protected int cellSize;

    @Configurable(tips = "ldlib.gui.editor.tips.cell_from")
    @NumberRange(range = { 0, Integer.MAX_VALUE })
    @Getter
    protected int from;

    @Configurable(tips = "ldlib.gui.editor.tips.cell_to")
    @NumberRange(range = { 0, Integer.MAX_VALUE })
    @Getter
    protected int to;

    @Configurable(tips = "ldlib.gui.editor.tips.cell_animation")
    @NumberRange(range = { 0, Integer.MAX_VALUE })
    @Getter
    protected int animation;

    @Configurable
    @NumberColor
    @Getter
    protected int color = -1;

    protected int currentFrame;

    protected int currentTime;
    private long lastTick;

    public AnimationTextureY() {
        this("ldlib:textures/gui/particles.png");
        setCellSize(8).setAnimation(32, 44).setAnimation(1);
    }

    @SuppressWarnings("removal")
    public AnimationTextureY(String imageLocation) {
        this.imageLocation = new ResourceLocation(imageLocation);
    }

    public AnimationTextureY(ResourceLocation imageLocation) {
        this.imageLocation = imageLocation;
    }

    public AnimationTexture copy() {
        return new AnimationTexture(imageLocation).setCellSize(cellSize).setAnimation(from, to).setAnimation(animation)
                .setColor(color);
    }

    @SuppressWarnings("removal")
    public AnimationTexture setTexture(String imageLocation) {
        this.imageLocation = new ResourceLocation(imageLocation);
        return this;
    }

    public AnimationTexture setCellSize(int cellSize) {
        this.cellSize = cellSize;
        return this;
    }

    public AnimationTexture setAnimation(int from, int to) {
        this.currentFrame = from;
        this.from = from;
        this.to = to;
        return this;
    }

    @Override
    @Environment(Environment.EnvType.CLIENT)
    public void updateTick() {
        if (Minecraft.getInstance().level != null) {
            long tick = Minecraft.getInstance().level.getGameTime();
            if (tick == lastTick) return;
            lastTick = tick;
        }
        if (currentTime >= animation) {
            currentTime = 0;
            currentFrame += 1;
        } else {
            currentTime++;
        }
        if (currentFrame > to) {
            currentFrame = from;
        } else if (currentFrame < from) {
            currentFrame = from;
        }
    }

    public AnimationTexture setAnimation(int animation) {
        this.animation = animation;
        return this;
    }

    @Override
    public AnimationTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    @Environment(Environment.EnvType.CLIENT)
    protected void drawInternal(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
        updateTick();
        float cell = 1f / this.cellSize;
        int X = currentFrame % cellSize;
        int Y = currentFrame % cellSize;

        float imageU = 0.0f;
        float imageV = Y * cell;

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, imageLocation);
        var matrix4f = graphics.pose().last().pose();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, POSITION_TEX_COLOR);
        bufferbuilder.vertex(matrix4f, x, y + height, 0).uv(imageU, imageV + cell).color(color).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y + height, 0).uv(1.0f, imageV + cell).color(color).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y, 0).uv(1.0f, imageV).color(color).endVertex();
        bufferbuilder.vertex(matrix4f, x, y, 0).uv(imageU, imageV).color(color).endVertex();
        tessellator.end();
    }

    @Environment(Environment.EnvType.CLIENT)
    protected void drawGuides(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
        // 没有人会在意那个难用的内部图形界面，不是吗
        float cell = 1f / this.cellSize;
        int X = from % cellSize;
        int Y = from % cellSize;

        float imageU = 0 * cell;
        float imageV = Y * cell;

        new ColorBorderTexture(-1, 0xff00ff00).draw(graphics, 0, 0,
                x + width * imageU, y + height * imageV,
                (int) (width * (cell)), (int) (height * (cell)));

        X = to % cellSize;
        Y = to / cellSize;

        imageU = 0 * cell;
        imageV = Y * cell;

        new ColorBorderTexture(-1, 0xffff0000).draw(graphics, 0, 0,
                x + width * imageU, y + height * imageV,
                (int) (width * (cell)), (int) (height * (cell)));
    }
}
