package com.moguang.ctnhmana.common.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.texture.*;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.common.ManaMachine;
import com.moguang.ctnhmana.registry.CMMachines;
import net.minecraft.network.chat.Component;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
public class BaseManaMachineGui implements IFancyUIProvider {

    @CN("当前升级")
    public static  Lang  TitleLang;

    protected ManaMachine machine;
    public BaseManaMachineGui(IRecipeLogicMachine machine)
    {
        if(machine instanceof ManaMachine mmachine)
            this.machine=mmachine;
    }
    public Widget createListPage(int x,int y,int num,int xf,int yf)
    {
        var group=new DraggableScrollableWidgetGroup(xf,yf,120,40)
                .setYBarStyle(GuiTextures.SLIDER_BACKGROUND, new ColorRectTexture(-1))
                .setYScrollBarWidth(10)  // 设置垂直滚动条宽度// 配置样式
                .setScrollable(true);
        for(int i=1;i<=num;i++)
            group.addWidget(new TextTextureWidget(x,y*i-y,100,y,"这个是用来测试的"));
        return group;
    }
    @Override
    public Widget createMainPage(FancyMachineUIWidget fancyMachineUIWidget) {
        var group=new DraggableScrollableWidgetGroup(0,0,200,200);
        var levelgroup=new DraggableScrollableWidgetGroup(0,10,200,100)
                .setYBarStyle(GuiTextures.SLIDER_BACKGROUND, new ColorRectTexture(-1))
                .setYScrollBarWidth(8)  // 设置垂直滚动条宽度// 配置样式
                .setScrollable(true)
                  ;
        var test_text=new TextTextureWidget(20,100,10,10,"你好");
        var listpage=createListPage(0,20,4,20,20);
        var checkwidget=new SwitchWidget(20,0,160,20,((clickData, state) ->
        {
            if(state)
            {
                levelgroup.addWidget(listpage);
            }
            else levelgroup.removeWidget(listpage);
        })
        )
                .setTexture(new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON,
                                new TextTexture("gtceu.creative.activity.off")),
                        new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON,
                                new TextTexture("gtceu.creative.activity.on")))
                .setHoverTooltips("你好我在这里")
                .setActive(true)
                ;
        levelgroup.addWidget(checkwidget);
//       levelgroup.addWidget(test_text);
        group.addWidget(levelgroup);
        return group;
    }

    @Override
    public IGuiTexture getTabIcon() {
        return new ItemStackTexture(CMMachines.MANA_HATCH.asStack());
    }

    @Override
    public Component getTitle() {
        return TitleLang.translate();
    }
}
