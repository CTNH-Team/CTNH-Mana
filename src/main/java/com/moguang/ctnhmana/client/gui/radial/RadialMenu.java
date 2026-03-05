package com.moguang.ctnhmana.client.gui.radial;

import net.minecraft.client.gui.GuiGraphics;

import java.util.List;
import java.util.function.IntConsumer;

/**
 * 径向菜单数据模型（仅主图标，无副图标）
 * 提供槽位列表、选择回调和绘制回调。
 *
 * @param <T> 主图标类型
 */
public class RadialMenu<T> {

    private final IntConsumer setSelectedSlot;
    private final List<RadialMenuSlot<T>> slots;
    private final DrawCallback<T> drawCallback;

    /**
     * @param setSelectedSlot 选择槽位时的回调（需在服务端同步时自行发包）
     * @param slots           槽位列表
     * @param drawCallback    绘制主图标的回调
     */
    public RadialMenu(IntConsumer setSelectedSlot, List<RadialMenuSlot<T>> slots, DrawCallback<T> drawCallback) {
        this.setSelectedSlot = setSelectedSlot;
        this.slots = slots;
        this.drawCallback = drawCallback;
    }

    public List<RadialMenuSlot<T>> getSlots() {
        return slots;
    }

    public void setCurrentSlot(int slot) {
        setSelectedSlot.accept(slot);
    }

    public void drawIcon(T icon, GuiGraphics graphics, int x, int y, int size) {
        drawCallback.draw(icon, graphics, x, y, size);
    }

    /**
     * 主图标绘制回调
     */
    @FunctionalInterface
    public interface DrawCallback<T> {

        void draw(T icon, GuiGraphics graphics, int x, int y, int size);
    }
}
