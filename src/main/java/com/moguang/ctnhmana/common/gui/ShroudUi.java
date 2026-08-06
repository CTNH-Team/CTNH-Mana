package com.moguang.ctnhmana.common.gui;

import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;

import com.lowdragmc.lowdraglib.gui.texture.*;
import com.lowdragmc.lowdraglib.gui.widget.*;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.common.multiblock.BaseManaMachine;
import com.moguang.ctnhmana.data.ManaData;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import com.moguang.ctnhmana.registry.CMMachines;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.awt.*;

public class ShroudUi implements IFancyUIProvider {

    private static final int UI_WIDTH = 800;
    private static final int UI_HEIGHT = 400;

    @CN("当前升级")
    @EN("Current upgrades")
    public static Lang TitleLangShroud;
    @CN("§5虚境的奥秘等待揭秘......§r")
    @EN("§5The secrets of the Zenith realm await revelation......§r")
    public static Lang ZenithLockedHintLang;

    public boolean a = false;
    private final BaseManaMachine machine;
    private final int y_position = (int) ((275 - 168) / 2 * 1.5);
    private final int width = (int) (275 * 1.5);
    private final int height = (int) (168 * 1.5);
    private final int circle_length = (int) (92 * 1.5);

    public ShroudUi(BaseManaMachine machine) {
        this.machine = machine;
    }

    private boolean isZenithOpen() {
        if (machine.getLevel() instanceof ServerLevel serverLevel) {
            return ManaData.getOrCreate(serverLevel).isZenithOpen();
        }
        return machine.isZenithOpen;
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget fancyMachineUIWidget) {
        var group = new DraggableScrollableWidgetGroup(0, 0, UI_WIDTH, UI_HEIGHT);
        if (!isZenithOpen()) {
            group.addWidget(createLockedView());
            return group;
        }
        group.addWidget(createUnlockedView());
        return group;
    }

    private Widget createLockedView() {
        var centergroup = new WidgetGroup(0, 0, UI_WIDTH, UI_HEIGHT);
        centergroup.setBackground(CMGuiTextures.SHROUND_BACKGROUND);
        MutableComponent hint = ZenithLockedHintLang.translate().copy();
        if (!hint.getString().contains("§")) {
            hint = hint.withStyle(ChatFormatting.LIGHT_PURPLE);
        }
        int labelHeight = 12;
        int y = (UI_HEIGHT - labelHeight) / 2;
        centergroup.addWidget(new TextTextureWidget(0, y, UI_WIDTH, labelHeight)
                .setText(hint)
                .textureStyle(texture -> texture.setType(TextTexture.TextType.NORMAL).setDropShadow(false)));
        return centergroup;
    }

    private Widget createUnlockedView() {
        var centergroup = new WidgetGroup(0, 0, UI_WIDTH, UI_HEIGHT);
        var configgroup = new WidgetGroup(width, 0, UI_WIDTH - width, 200);
        var buffgroup = new WidgetGroup(width, 300, UI_WIDTH - width, 200);
        buffgroup.setBackground(CMGuiTextures.PATERN_BACKGROUND);
        centergroup.setBackground(CMGuiTextures.SHROUND_BACKGROUND);
        var center_ring = new ImageWidget((275 - 92) / 2 / 2 * 3, (275 - 92) / 2 / 2 * 3, circle_length, circle_length,
                CMGuiTextures.SHROUND_RING);
        var quadrant_neutral_A = new ArcButtonWidget(0, y_position, width, height, CMGuiTextures.QUADRANT_NEUTRAL,
                clickData -> {
                    a = true;
                });
        var left_texture = CMGuiTextures.QUADRANT_NEUTRAL.copy();
        quadrant_neutral_A.setHoverTexture(CMGuiTextures.QUADRANT_NEUTRAL_SELECTED);
        var quadrant_neutral_B = new ArcButtonWidget(0, y_position, width, height, left_texture.rotate(45),
                clickData -> {
                    a = true;
                });

        quadrant_neutral_B.setStartAngle(45);
        quadrant_neutral_B.setEndAngle(135);
        quadrant_neutral_B.setHoverTexture(CMGuiTextures.QUADRANT_NEUTRAL_SELECTED.copy().rotate(45));

        var quadrant_neutral_C = new ArcButtonWidget(0, y_position, width, height,
                CMGuiTextures.QUADRANT_NEUTRAL.copy().rotate(90), clickData -> {
                    a = true;
                });
        quadrant_neutral_C.setStartAngle(135);
        quadrant_neutral_C.setEndAngle(135 + 90);
        quadrant_neutral_C.setHoverTexture(CMGuiTextures.QUADRANT_NEUTRAL_SELECTED.copy().rotate(90));

        var quadrant_neutral_D = new ArcButtonWidget(0, y_position, width, height,
                CMGuiTextures.QUADRANT_NEUTRAL.copy().rotate(180 - 45), clickData -> {
                    a = true;
                });
        quadrant_neutral_D.setStartAngle(135 + 90);
        quadrant_neutral_D.setEndAngle(135 + 180);
        quadrant_neutral_D.setHoverTexture(CMGuiTextures.QUADRANT_NEUTRAL_SELECTED.copy().rotate(180 - 45));
        var delve_window = new ImageWidget(100, 20, UI_WIDTH - 100 - width, 140,
                CMGuiTextures.DELVE_WINDOW.setColor(Color.GREEN.getRGB()));

        configgroup.addWidgets(delve_window);
        centergroup.addWidgets(center_ring, quadrant_neutral_A, quadrant_neutral_B, quadrant_neutral_C,
                quadrant_neutral_D, configgroup);
        var group = new WidgetGroup(0, 0, UI_WIDTH, UI_HEIGHT);
        group.addWidget(centergroup);
        return group;
    }

    @Override
    public IGuiTexture getTabIcon() {
        return new ItemStackTexture(CMMachines.MANA_HATCH.asStack());
    }

    @Override
    public Component getTitle() {
        return TitleLangShroud.translate();
    }
}
