package com.moguang.ctnhmana.common.gui;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.texture.*;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.latvian.mods.rhino.annotations.JSFunction;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import lombok.Getter;
import lombok.Setter;
import net.createmod.catnip.annotations.Environment;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

@LDLRegister(name = "arc_button", group = "widget.basic") // 修改注册名以便区分
@RemapPrefixForJS("kjs$")
public class ArcButtonWidget extends Widget implements IConfigurableWidget {

    @Configurable(name = "ldlib.gui.editor.name.clicked_texture")
    protected IGuiTexture clickedTexture;

    // 弧线核心参数（可在编辑器中配置）
    @Configurable(name = "arc.inner_radius", tips = "内半径（弧形空心部分）")
    @Setter @Getter
    protected int innerRadius = 92/2;

    @Configurable(name = "arc.outer_radius", tips = "外半径（弧形外边界）")
    @Setter @Getter
    protected int outerRadius = 275/2;

    @Configurable(name = "arc.start_angle", tips = "起始角度（度，0为右向，顺时针递增）")
    @Setter @Getter
    protected double startAngle = 360-45; // 单位：度（内部转换为弧度）

    @Configurable(name = "arc.end_angle", tips = "结束角度（度）")
    @Setter @Getter
    protected double endAngle = 45; // 单位：度（内部转换为弧度）

    protected Consumer<ClickData> onPressCallback;
    @Getter
    protected boolean isClicked = false;

    public ArcButtonWidget() {
        this(0, 0, 40, 40, new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON, new TextTexture("ArcBtn")), null);
    }

    @Override
    public void initTemplate() {
        setHoverBorderTexture(1, -1);
    }

    public ArcButtonWidget(int xPosition, int yPosition, int width, int height, IGuiTexture buttonTexture, Consumer<ClickData> onPressed) {
        super(xPosition, yPosition, width, height);
        this.onPressCallback = onPressed;
        setBackground(buttonTexture);
    }

    public ArcButtonWidget(int xPosition, int yPosition, int width, int height, Consumer<ClickData> onPressed) {
        super(xPosition, yPosition, width, height);
        this.onPressCallback = onPressed;
    }

    public ArcButtonWidget setOnPressCallback(Consumer<ClickData> onPressCallback) {
        this.onPressCallback = onPressCallback;
        return this;
    }

    public ArcButtonWidget setButtonTexture(IGuiTexture... buttonTexture) {
        super.setBackground(buttonTexture);
        return this;
    }

    @HideFromJS
    public ArcButtonWidget setHoverTexture(IGuiTexture... hoverTexture) {
        super.setHoverTexture(hoverTexture);
        return this;
    }

    public ArcButtonWidget setClickedTexture(IGuiTexture... clickedTexture) {
        this.clickedTexture = clickedTexture.length > 1 ? new GuiTextureGroup(clickedTexture) : clickedTexture[0];
        return this;
    }

    public ArcButtonWidget kjs$setHoverTexture(IGuiTexture... hoverTexture) {
        super.setHoverTexture(hoverTexture);
        return this;
    }

    public ArcButtonWidget setHoverBorderTexture(int border, int color) {
        super.setHoverTexture(new ColorBorderTexture(border, color));
        return this;
    }

    /**
     * 核心修改：重写鼠标碰撞检测，判断是否在弧线区域内
     */
    @Override
    public boolean isMouseOverElement(double mouseX, double mouseY) {
        // 1. 计算弧线圆心（默认使用控件中心作为圆心）
        Position center = getPosition().add(getSize().width / 2, getSize().height / 2);
        double centerX = center.x;
        double centerY = center.y;

        // 2. 计算鼠标相对于圆心的偏移量
        double dx = mouseX - centerX;
        double dy = mouseY - centerY;

        // 3. 计算鼠标到圆心的距离（平方，避免开方提高性能）
        double distanceSquared = dx * dx + dy * dy;
        // 若距离不在 [内半径², 外半径²] 范围内，直接返回false
        if (distanceSquared < innerRadius * innerRadius || distanceSquared > outerRadius * outerRadius) {
            return false;
        }

        // 4. 计算鼠标相对于圆心的角度（弧度），并转换为[0, 2π)范围
        double angle = Math.atan2(dy, dx); // 范围：[-π, π]
        if (angle < 0) {
            angle += 2 * Math.PI; // 转换为 [0, 2π)
        }

        // 5. 将配置的角度（度）转换为弧度
        double start = Math.toRadians(startAngle);
        double end = Math.toRadians(endAngle);

        // 6. 处理跨0度的弧线（如从350度到10度）
        boolean inAngleRange;
        if (start <= end) {
            inAngleRange = angle >= start && angle <= end;
        } else {
            inAngleRange = angle >= start || angle <= end;
        }

        return inAngleRange;
    }

    @Override
    @Environment(Environment.EnvType.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 只有当鼠标在弧线区域内时才响应点击
        if (isMouseOverElement(mouseX, mouseY)) {
            isClicked = true;
            ClickData clickData = new ClickData();
            writeClientAction(1, clickData::writeToBuf);
            if (onPressCallback != null) {
                onPressCallback.accept(clickData);
            }
            playButtonClickSound();
            return true;
        }
        return false;
    }

    @Override
    @Environment(Environment.EnvType.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isClicked = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            ClickData clickData = ClickData.readFromBuf(buffer);
            if (onPressCallback != null) {
                onPressCallback.accept(clickData);
            }
        }
    }

    @Environment(Environment.EnvType.CLIENT)
    protected void drawBackgroundTexture(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        var isHovered = isMouseOverElement(mouseX, mouseY); // 使用弧线检测判断是否hover
        if (!isHovered || drawBackgroundWhenHover) {
            if (isClicked && clickedTexture != null) {
                Position pos = getPosition();
                Size size = getSize();
                clickedTexture.draw(graphics, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
            } else if (backgroundTexture != null) {
                Position pos = getPosition();
                Size size = getSize();
                backgroundTexture.draw(graphics, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
            }
        }
        if (hoverTexture != null && isHovered && isActive()) {
            Position pos = getPosition();
            Size size = getSize();
            hoverTexture.draw(graphics, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }
    }



}