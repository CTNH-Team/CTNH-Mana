package com.moguang.ctnhmana.common;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.data.ManaData;
import net.minecraft.server.level.ServerLevel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManaMachine extends WorkableElectricMultiblockMachine{
    public ManaMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ManaMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    public Map<String, Integer> ManaLevel = new HashMap<>();
    public List<String> LevelName= Arrays.asList("BT","BM","Ars","GT"); //Waiting for NANE CHANGING
    public ManaData Manadata;

    private void SyncManaData() {
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            var manadata=ManaData.getOrCreate(serverLevel);
            ManaLevel=manadata.get();
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        SyncManaData();
        //SO MUCH BUGS
        //只是一个测试
    }

    @Override
    public Widget createUIWidget() {
    var widget=super.createUIWidget();
        var button_refresh=(new ButtonWidget(10, 60, 15, 15, new GuiTextureGroup(new IGuiTexture[]{ResourceBorderTexture.BUTTON_COMMON, new TextTexture("-")}), clickData ->
        {
            SyncManaData();
        }
        ).setHoverTooltips("刷新机器数据状态")
        );
        if(widget instanceof WidgetGroup group)
        {
            ((WidgetGroup) widget).addWidget(button_refresh);
        }
    return widget;
    }




    }
