package com.moguang.ctnhmana.common.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.moguang.ctnhmana.common.parts.ExtendedCentralControlBus;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;

/**
 * 多路编程电路网格中的单个通道格：只读显示 + 左键选中 + {@link GuiTextures#SELECT_BOX} 高亮。
 * <p>
 * GT 原版没有此类（只有单槽 {@link com.gregtechceu.gtceu.api.gui.widget.GhostCircuitSlotWidget}）；
 * 选中框绘制参考 AE 配置槽 {@code AEItemConfigSlotWidget}。
 */
public class SelectableCircuitSlotWidget extends SlotWidget {

    private final ExtendedCentralControlBus bus;
    private final int circuitSlot;
    private final IntConsumer onSelected;

    public SelectableCircuitSlotWidget(IItemHandlerModifiable handler, int circuitSlot, int x, int y, int size,
                                       ExtendedCentralControlBus bus, IntConsumer onSelected) {
        super(handler, circuitSlot, x, y, false, false);
        setSize(size, size);
        this.bus = bus;
        this.circuitSlot = circuitSlot;
        this.onSelected = onSelected;
        setBackground(new GuiTextureGroup(GuiTextures.SLOT, GuiTextures.INT_CIRCUIT_OVERLAY));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY) && button == 0) {
            onSelected.accept(circuitSlot);
            return true;
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        if (bus.selectedCircuitSlot == circuitSlot) {
            var pos = getPosition();
            GuiTextures.SELECT_BOX.draw(graphics, mouseX, mouseY, pos.x, pos.y, getSize().width, getSize().height);
        }
    }
}
