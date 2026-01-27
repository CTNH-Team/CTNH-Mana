package com.moguang.ctnhmana.common;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.Mutiblock.ZENITH_MATRIX;
import com.moguang.ctnhmana.data.ManaData;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

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
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    @Setter
    @Getter
    @Nullable
    public ZENITH_MATRIX Zenith_Enhanced=null;
    public Map<String, Integer> ManaLevel = new HashMap<>();
    public List<String> LevelName= Arrays.asList("BT","BM","ARS","GT"); //Waiting for NANE CHANGING
    public ManaData Manadata;

    protected void SyncManaData() {
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            var manadata=ManaData.getOrCreate(serverLevel);
            ManaLevel=manadata.get();
        }
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        SyncManaData();
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
        WidgetGroup widget = new WidgetGroup(0, 0, 190, 125);
        widget.addWidget((new DraggableScrollableWidgetGroup(4, 4, 182, 117)).setBackground(this.getScreenTexture()).addWidget(new LabelWidget(4, 5, this.self().getBlockState().getBlock().getDescriptionId())).addWidget((new ComponentPanelWidget(4, 17, this::addDisplayText)).textSupplier(this.getLevel().isClientSide ? null : this::addDisplayText).setMaxWidthLimit(200).clickHandler(this::handleDisplayClick)));
        widget.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});

        var button_refresh=(new ButtonWidget(10, 60, 15, 15, new GuiTextureGroup(new IGuiTexture[]{ResourceBorderTexture.BUTTON_COMMON, new TextTexture("-")}), clickData ->
        {
            SyncManaData();
        }
        ).setHoverTooltips("刷新机器数据状态")
        );
        if(widget != null)
        {
            ((WidgetGroup) widget).addWidget(button_refresh);
        }
    return widget;
    }




    }
