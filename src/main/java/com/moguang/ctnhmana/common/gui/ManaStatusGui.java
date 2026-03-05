package com.moguang.ctnhmana.common.gui;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

import com.moguang.ctnhmana.Mutiblock.BaseManaMachine;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;

import java.util.List;

import static com.moguang.ctnhmana.Mutiblock.BaseManaMachine.BaseManaMachineLang;

public class ManaStatusGui implements IFancyUIProvider {

    protected BaseManaMachine machine;

    @CN("查看机器详细数据")
    public static Lang ManaStatusGuiTooltips;
    @CN("机器状态数据")
    public static Lang ManaStatusProviderTooltips;
    @CN("错误")
    public static Lang ManaStatusGuiError;

    public ManaStatusGui(BaseManaMachine machine) {
        this.machine = machine;
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget fancyMachineUIWidget) {
        WidgetGroup group = new WidgetGroup(0, 0, 190, 125);
        DraggableScrollableWidgetGroup dggroup = new DraggableScrollableWidgetGroup(4, 4, 182, 125);
        dggroup.addWidget((new DraggableScrollableWidgetGroup(4, 4, 182, 117)));
        dggroup.setBackground(new IGuiTexture[] { GuiTextures.DISPLAY });
        // group.addWidget(new LabelWidget(4, 5,ManaStatusProviderTooltips.translate()));
        dggroup.addWidget(new ComponentPanelWidget(4, 5, this::addDisplayText)
                .textSupplier(this.machine.getLevel().isClientSide ? null : this::addDisplayText)
                .setMaxWidthLimit(200)
                .clickHandler(this::handleDisplayClick));
        group.addWidget(dggroup);
        return group;
    }

    public void handleDisplayClick(String componentData, ClickData clickData) {}

    public void addDisplayText(List<Component> textList) {
        if (this.machine == null || !this.machine.isFormed() || this.machine.hatch == null) {
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        textList.add(textList.size(), ManaStatusProviderTooltips.translate());
        textList.add(addEnergyUsageLine(machine.getEnergyContainer()));
        textList.add(addEnergyTierLine(machine.getTier()));
        textList.add(textList.size(), BaseManaMachineLang[0].translate((int) machine.hatch.getMana()));
        if (machine.Zenith_Enhanced != null)
            textList.add(textList.size(), BaseManaMachineLang[8].translate());
        if (!machine.isActive()) {
            textList.add(textList.size(), BaseManaMachineLang[1].translate(machine.consumption));
            textList.add(textList.size(), BaseManaMachineLang[2].translate(machine.getUpdateName()));
            textList.add(textList.size(),
                    BaseManaMachineLang[3].translate(machine.metric.parallel + machine.globalmetric.parallel));
            textList.add(textList.size(),
                    BaseManaMachineLang[4].translate(machine.metric.speed + machine.globalmetric.speed));
            textList.add(textList.size(),
                    BaseManaMachineLang[5].translate(machine.metric.eut + machine.globalmetric.eut));
            textList.add(textList.size(),
                    BaseManaMachineLang[6].translate(machine.metric.input + machine.globalmetric.input));
            textList.add(textList.size(),
                    BaseManaMachineLang[7].translate(machine.metric.output + machine.globalmetric.output));
        } else {
            textList.add(textList.size(), BaseManaMachineLang[1].translate(machine.consumption));
            textList.add(textList.size(), BaseManaMachineLang[2].translate(machine.getUpdateName()));
            textList.add(textList.size(), BaseManaMachineLang[3].translate(machine.recipemetric.parallel));
            textList.add(textList.size(), BaseManaMachineLang[4].translate(machine.recipemetric.speed));
            textList.add(textList.size(), BaseManaMachineLang[5].translate(machine.recipemetric.eut));
            textList.add(textList.size(), BaseManaMachineLang[6].translate(machine.recipemetric.input));
            textList.add(textList.size(), BaseManaMachineLang[7].translate(machine.recipemetric.output));
        }
    }

    @Override
    public IGuiTexture getTabIcon() {
        return CMGuiTextures.MACHINE_STATUS_ICON;
    }

    @Override
    public Component getTitle() {
        return ManaStatusGuiTooltips.translate();
    }

    public Component addEnergyUsageLine(IEnergyContainer energyContainer) {
        if (energyContainer != null && energyContainer.getEnergyCapacity() > 0) {
            long maxVoltage = Math.max(energyContainer.getInputVoltage(), energyContainer.getOutputVoltage());

            String energyFormatted = FormattingUtil.formatNumbers(maxVoltage);
            // wrap in text component to keep it from being formatted
            byte voltageTier = GTUtil.getFloorTierByVoltage(maxVoltage);
            Component voltageName = Component.literal(
                    GTValues.VNF[voltageTier]);

            MutableComponent bodyText = Component.translatable("gtceu.multiblock.max_energy_per_tick",
                    energyFormatted, voltageName).withStyle(ChatFormatting.GRAY);
            Component hoverText = Component.translatable("gtceu.multiblock.max_energy_per_tick_hover")
                    .withStyle(ChatFormatting.GRAY);
            return (bodyText.withStyle(
                    style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))));
        } else return ManaStatusGuiError.translate();
    }

    public Component addEnergyTierLine(int tier) {
        if (tier < GTValues.ULV || tier > GTValues.MAX)
            return ManaStatusGuiError.translate();

        Component voltageName = Component.literal(GTValues.VNF[tier]);
        MutableComponent bodyText = Component.translatable(
                "gtceu.multiblock.max_recipe_tier",
                voltageName).withStyle(ChatFormatting.GRAY);
        Component hoverText = Component.translatable("gtceu.multiblock.max_recipe_tier_hover")
                .withStyle(ChatFormatting.GRAY);
        return (bodyText
                .withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))));
    }
}
