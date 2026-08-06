package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import com.moguang.ctnhmana.common.event.zenith.ZenithGlitchText;
import com.moguang.ctnhmana.common.parts.ManaHatch;
import com.moguang.ctnhmana.data.ManaData;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManaMachine extends RecipeElectricMultiblockMachine {

    public ManaMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    public ManaHatch hatch;
    @Persisted
    public BlockPos hatchPos;
    @Setter
    @Getter
    @Nullable
    public ZenithMatrixMachine Zenith_Enhanced = null;
    public Map<String, Integer> ManaLevel = new HashMap<>();
    public List<String> LevelName = Arrays.asList("BT", "BM", "ARS", "GT"); // Waiting for NANE CHANGING
    public ManaData Manadata;
    @DescSynced
    public boolean isZenithOpen = false;

    protected void SyncManaData() {
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            var manadata = ManaData.getOrCreate(serverLevel);
            ManaLevel = manadata.get();
            isZenithOpen = manadata.isZenithOpen();
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        SyncManaData();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        SyncManaData();
    }

    @Override
    public void onStructureInvalid() {
        this.hatch = null;
        this.hatchPos = null;
        super.onStructureInvalid();
    }

    @Nullable
    public ManaHatch getHatch() {
        for (IMultiPart part : this.getParts()) {
            if (part instanceof ManaHatch hatchs) {
                hatchPos = (hatchs).getPos();
                return hatchs;
            }
        }
        return null;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        buildDisplayText(textList);
        applyZenithUiGlitch(textList);
    }

    /** 子类覆盖此方法追加 UI 文案；乱码统一在 {@link #addDisplayText} 末尾处理。 */
    protected void buildDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
    }

    protected final void applyZenithUiGlitch(List<Component> textList) {
        ZenithGlitchText.scrambleIfInvading(getLevel(), textList);
    }

    // @Override
    // public Widget createUIWidget() {
    // WidgetGroup widget = new WidgetGroup(0, 0, 190, 125);
    // widget.addWidget((new DraggableScrollableWidgetGroup(4, 4, 182,
    // 117)).setBackground(this.getScreenTexture()).addWidget(new LabelWidget(4, 5,
    // this.self().getBlockState().getBlock().getDescriptionId())).addWidget((new ComponentPanelWidget(4, 17,
    // this::addDisplayText)).textSupplier(this.getLevel().isClientSide ? null :
    // this::addDisplayText).setMaxWidthLimit(200).clickHandler(this::handleDisplayClick)));
    // widget.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
    //
    // var button_refresh=(new ButtonWidget(10, 60, 15, 15, new GuiTextureGroup(new
    // IGuiTexture[]{ResourceBorderTexture.BUTTON_COMMON, new TextTexture("-")}), clickData ->
    // {
    // SyncManaData();
    // }
    // ).setHoverTooltips("刷新机器数据状态")
    // );
    // if(widget != null)
    // {
    // ((WidgetGroup) widget).addWidget(button_refresh);
    // }
    // return widget;
    // }
}
