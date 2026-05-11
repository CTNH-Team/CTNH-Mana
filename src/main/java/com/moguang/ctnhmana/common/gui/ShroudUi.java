package com.moguang.ctnhmana.common.gui;

import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;

import com.lowdragmc.lowdraglib.gui.texture.*;
import com.lowdragmc.lowdraglib.gui.widget.*;

import net.minecraft.network.chat.Component;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import com.moguang.ctnhmana.registry.CMMachines;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.awt.*;

public class ShroudUi implements IFancyUIProvider {

    @CN("当前升级")
    @EN("Current upgrades")
    public static Lang TitleLangShroud;
    public boolean a = false;
    private final int x_position = (int) ((275 - 92) / 2 * 1.5);
    private final int y_position = (int) ((275 - 168) / 2 * 1.5);
    private final int width = (int) (275 * 1.5);
    private final int height = (int) (168 * 1.5);
    private final int circle_length = (int) (92 * 1.5);

    @Override
    public Widget createMainPage(FancyMachineUIWidget fancyMachineUIWidget) {
        var group = new DraggableScrollableWidgetGroup(0, 0, 800, 400);
        var centergroup = new WidgetGroup(0, 0, 800, 400);
        var configgroup = new WidgetGroup(width, 0, 800 - width, 200);
        var buffgroup = new WidgetGroup(width, 300, 800 - width, 200);
        buffgroup.setBackground(CMGuiTextures.PATERN_BACKGROUND);
        centergroup.setBackground(CMGuiTextures.SHROUND_BACKGROUND);
        var center_ring = new ImageWidget((275 - 92) / 2 / 2 * 3, (275 - 92) / 2 / 2 * 3, circle_length, circle_length,
                CMGuiTextures.SHROUND_RING);
        var quadrant_neutral_A = new ArcButtonWidget(0, y_position, width, height, CMGuiTextures.QUADRANT_NEUTRAL,
                clickData -> {
                    a = true;
                });
        var left_texture = CMGuiTextures.QUADRANT_NEUTRAL.copy();
        var neutral_90 = CMGuiTextures.QUADRANT_NEUTRAL.copy();
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
        var delve_window = new ImageWidget(100, 20, 800 - 100 - width, 140,
                CMGuiTextures.DELVE_WINDOW.setColor(Color.GREEN.getRGB()));

        configgroup.addWidgets(delve_window);
        centergroup.addWidgets(center_ring, quadrant_neutral_A, quadrant_neutral_B, quadrant_neutral_C,
                quadrant_neutral_D, configgroup);
        group.addWidgets(centergroup);
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
