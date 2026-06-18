package com.moguang.ctnhmana.common.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.config.ConfigHolder;

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

import com.moguang.ctnhmana.Mutiblock.parts.ExtendedCentralControlBus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

/**
 * 拓展总线 Fancy UI 侧栏「编程电路」页。
 * <p>
 * 布局与 0~32 按钮矩阵移植自 {@link com.gregtechceu.gtceu.api.gui.widget.GhostCircuitSlotWidget#createConfigurator()} /
 * {@link com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator}；
 * 因本机器有 32 路通道，额外用 {@link SelectableCircuitSlotWidget} 网格选择当前编辑通道。
 */
public class ExtendedCentralControlBusCircuitUi implements IFancyUIProvider {

    @CN({
            "§6编程电路§r（先点击上方通道槽位，再点击下方编号）",
            "通道 %d：目标槽位 %s",
            "当前编辑：通道 %d"
    })
    @EN({
            "§6Programmed Circuits§r (select a channel above, then pick 0~32 below)",
            "Channel %d: target slot %s",
            "Editing channel %d"
    })
    public static Lang[] extendedCentralControlBusUiLang;
    @CN("编程电路")
    @EN("Circuits")
    public static Lang extendedCentralControlBusCircuitTabLang;

    private final ExtendedCentralControlBus bus;

    public ExtendedCentralControlBusCircuitUi(ExtendedCentralControlBus bus) {
        this.bus = bus;
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget fancyMachineUIWidget) {
        var root = new WidgetGroup(0, 0, ExtendedCentralControlBus.UI_WIDTH, ExtendedCentralControlBus.UI_HEIGHT - 8);
        var scroll = new DraggableScrollableWidgetGroup(6, 6,
                ExtendedCentralControlBus.UI_WIDTH - 12, ExtendedCentralControlBus.UI_HEIGHT - 20);
        scroll.addWidget(new ExtendedCentralControlBusCircuitPanel(bus));
        root.addWidget(scroll);
        return root;
    }

    @Override
    public IGuiTexture getTabIcon() {
        return new GuiTextureGroup(GuiTextures.SLOT, GuiTextures.INT_CIRCUIT_OVERLAY);
    }

    @Override
    public Component getTitle() {
        return extendedCentralControlBusCircuitTabLang.translate();
    }

    /**
     * 编程电路页内容：32 路网格 + 0~32 配置区；所有 {@link #writeClientAction} 在此统一处理。
     */
    public static final class ExtendedCentralControlBusCircuitPanel extends WidgetGroup {

        private static final int SELECT_SLOT = 0;
        private static final int SET_CONFIG = 1;

        /** 上方通道网格：稍大槽位便于点选 */
        public static final int SLOT = 20;
        public static final int GAP = 6;
        public static final int STEP = SLOT + GAP;
        public static final int COLS = 8;
        private static final int PANEL_W = COLS * STEP + 12;

        /** 下方 0~32 配置区：与 GT {@code createConfigurator} 相同（18px、无间隙） */
        private static final int CFG_SLOT = 18;
        private static final int CFG_STEP = 18;
        private static final int CFG_PANEL_W = 174;
        private static final int CFG_PANEL_H = 132;
        private static final int CFG_BTN_LEFT = 5;
        private static final int CFG_ROW0_Y = 48;
        private static final int CFG_LAST_ROW_COLS = 6;

        private final ExtendedCentralControlBus bus;
        private SlotWidget previewSlot;

        public ExtendedCentralControlBusCircuitPanel(ExtendedCentralControlBus bus) {
            super(0, 0, PANEL_W, computePanelHeight());
            this.bus = bus;
            IItemHandlerModifiable handler = bus.getCircuitInventory().storage;

            addWidget(new LabelWidget(4, 4, extendedCentralControlBusUiLang[0].translate().getString()));

            int gridTop = 22;
            int gridLeft = (PANEL_W - COLS * STEP) / 2;
            for (int i = 0; i < ExtendedCentralControlBus.CIRCUIT_SLOT_COUNT; i++) {
                int x = gridLeft + (i % COLS) * STEP;
                int y = gridTop + (i / COLS) * STEP;
                int circuitSlot = i;
                addWidget(new SelectableCircuitSlotWidget(handler, circuitSlot, x, y, SLOT, bus, this::onSlotSelected)
                        .setHoverTooltips(extendedCentralControlBusUiLang[1].translate(circuitSlot,
                                formatTarget(bus.getTargetSlot(circuitSlot)))));
            }

            int configY = gridTop + (ExtendedCentralControlBus.CIRCUIT_SLOT_COUNT / COLS) * STEP + 14;
            addWidget(new LabelWidget(4, configY,
                    () -> extendedCentralControlBusUiLang[2].translate(bus.selectedCircuitSlot).getString()));

            addWidget(buildConfigurator(configY + 16));
            refreshPreviewSlot();
        }

        private static int computePanelHeight() {
            int gridTop = 22;
            int gridH = (ExtendedCentralControlBus.CIRCUIT_SLOT_COUNT / COLS) * STEP;
            return gridTop + gridH + 14 + 14 + 16 + CFG_PANEL_H + 10;
        }

        private WidgetGroup buildConfigurator(int y) {
            int configX = (PANEL_W - CFG_PANEL_W) / 2;
            var group = new WidgetGroup(configX, y, CFG_PANEL_W, CFG_PANEL_H);
            var handler = bus.getCircuitInventory().storage;
            boolean ghost = ConfigHolder.INSTANCE.machines.ghostCircuit;
            int previewX = (CFG_PANEL_W - CFG_SLOT) / 2;

            group.addWidget(new LabelWidget(9, 8, "Programmed Circuit Configuration"));
            previewSlot = new SlotWidget(handler, bus.selectedCircuitSlot, previewX, 20, !ghost, !ghost);
            previewSlot.setBackground(new GuiTextureGroup(GuiTextures.SLOT, GuiTextures.INT_CIRCUIT_OVERLAY));
            group.addWidget(previewSlot);
            if (ghost) {
                group.addWidget(new ButtonWidget(previewX, 20, CFG_SLOT, CFG_SLOT, IGuiTexture.EMPTY,
                        clickData -> onCircuitSet(-1)));
            }

            int idx = 0;
            for (int row = 0; row <= 2; row++) {
                for (int col = 0; col <= 8; col++) {
                    group.addWidget(configButton(CFG_BTN_LEFT + CFG_STEP * col, CFG_ROW0_Y + CFG_STEP * row, idx++));
                }
            }
            int lastRowLeft = (CFG_PANEL_W - CFG_LAST_ROW_COLS * CFG_SLOT) / 2;
            for (int col = 0; col < CFG_LAST_ROW_COLS; col++) {
                group.addWidget(configButton(lastRowLeft + CFG_STEP * col, CFG_ROW0_Y + CFG_STEP * 3, col + 27));
            }
            group.setBackground(GuiTextures.BACKGROUND);
            return group;
        }

        private ButtonWidget configButton(int x, int y, int configuration) {
            var button = new ButtonWidget(x, y, CFG_SLOT, CFG_SLOT,
                    new GuiTextureGroup(GuiTextures.SLOT,
                            new ItemStackTexture(IntCircuitBehaviour.stack(configuration)).scale(16f / 18)),
                    clickData -> onCircuitSet(configuration));
            button.setHoverTooltips(String.valueOf(configuration));
            return button;
        }

        private void refreshPreviewSlot() {
            previewSlot.setHandlerSlot(bus.getCircuitInventory().storage, bus.selectedCircuitSlot);
        }

        private void onSlotSelected(int circuitSlot) {
            bus.selectedCircuitSlot = circuitSlot;
            refreshPreviewSlot();
            writeClientAction(SELECT_SLOT, buf -> buf.writeVarInt(circuitSlot));
        }

        private void onCircuitSet(int configuration) {
            int slot = bus.selectedCircuitSlot;
            if (slot < 0 || slot >= ExtendedCentralControlBus.CIRCUIT_SLOT_COUNT) {
                return;
            }
            applyCircuit(slot, configuration);
            writeClientAction(SET_CONFIG, buf -> {
                buf.writeVarInt(slot);
                buf.writeVarInt(configuration);
            });
        }

        private void applyCircuit(int slot, int configuration) {
            IItemHandlerModifiable handler = bus.getCircuitInventory().storage;
            if (configuration < 0) {
                handler.setStackInSlot(slot, ItemStack.EMPTY);
            } else {
                handler.setStackInSlot(slot, IntCircuitBehaviour.stack(configuration));
            }
        }

        @Override
        public void handleClientAction(int id, FriendlyByteBuf buffer) {
            if (id == SELECT_SLOT) {
                bus.selectedCircuitSlot = buffer.readVarInt();
                refreshPreviewSlot();
            } else if (id == SET_CONFIG) {
                applyCircuit(buffer.readVarInt(), buffer.readVarInt());
            }
        }

        private static String formatTarget(int target) {
            return target < 0 ? "-" : String.valueOf(target);
        }
    }
}