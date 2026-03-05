package com.moguang.ctnhmana.integration.jade;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.moguang.ctnhmana.Mutiblock.ManaMachine;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.overlay.DisplayHelper;

public class ManaMachineManaStatusProvider extends CapabilityBlockProvider<ManaMachine> {

    public ManaMachineManaStatusProvider() {
        super(GTCEu.id("manamachine_mana_status_provider"));
    }

    @Override
    protected @Nullable ManaMachine getCapability(Level level, BlockPos pos, @Nullable Direction direction) {
        if (ManaMachine.getMachine(level, pos) instanceof ManaMachine machine) {
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, ManaMachine machine) {
        long mana = 0;
        long maxMana = 0;
        if (machine.isFormed() && machine.hatch != null) {
            mana = machine.hatch.getMana();
            maxMana = machine.hatch.getMaxMana();
        }
        data.putLong("mana", mana);
        data.putLong("max_mana", maxMana);
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player,
                              BlockAccessor accessor, BlockEntity blockEntity, IPluginConfig config) {
        if (!capData.contains("max_mana") || !capData.contains("mana")) return;
        long mana = capData.getLong("mana");
        long maxMana = capData.getLong("max_mana");
        if (maxMana <= 0) return;

        float progress = getProgress(mana, maxMana);
        var helper = tooltip.getElementHelper();

        tooltip.add(
                helper.progress(
                        progress,
                        Component.translatable(
                                "ctnhmana.jade.manahatch.manaprogress",
                                DisplayHelper.dfCommas.format(mana),
                                DisplayHelper.dfCommas.format(maxMana)),
                        helper.progressStyle().color(0x00008B, 0x00008B).textColor(-1),
                        Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555),
                        true));
    }
}
