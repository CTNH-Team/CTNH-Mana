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

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.Mutiblock.IndustrialAltarMachine;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.overlay.DisplayHelper;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

public class BloodAltarStatusProvider extends CapabilityBlockProvider<IndustrialAltarMachine> {

    public BloodAltarStatusProvider() {
        super(GTCEu.id("bloodaltar_status_provider"));
    }

    @Override
    protected @Nullable IndustrialAltarMachine getCapability(Level level, BlockPos pos, @Nullable Direction direction) {
        if (IndustrialAltarMachine.getMachine(level, pos) instanceof IndustrialAltarMachine machine)
            return (IndustrialAltarMachine) IndustrialAltarMachine.getMachine(level, pos);
        return null;
    }

    @Override
    protected void write(CompoundTag data, IndustrialAltarMachine machine) {
        int lp = 0;
        int max_lp = 0;
        String upgrade = "None";
        int consume = 0;
        if (machine.isFormed() && machine.altar != null) {
            lp = machine.altar.getCurrentBlood();
            max_lp = machine.altar.getCapacity();
        }
        if (machine.isFormed() && machine.isActive()) {
            consume = machine.consumption_lp;
            if (machine.getUpgrade().equals("suppression")) consume *= 0.5;
        }
        data.putInt("lp", lp);
        data.putInt("max_lp", max_lp);
        data.putInt("consume", consume);
        data.putString("upgrade", upgrade);
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor blockAccessor,
                              BlockEntity blockEntity, IPluginConfig iPluginConfig) {
        if (!capData.contains("lp") || !capData.contains("max_lp")) return;
        var lp = capData.getInt("lp");
        var consume = capData.getInt("consume");
        var max_lp = capData.getInt("max_lp");
        var progress_mana = getProgress(lp, max_lp);
        var helper = tooltip.getElementHelper();
        var upgrade = capData.getString("upgrade");
        if (max_lp > 0)
            tooltip.add(
                    helper.progress(
                            progress_mana,
                            Component.translatable("ctnhmana.jade.manahatch.manaprogress",
                                    DisplayHelper.dfCommas.format(lp), DisplayHelper.dfCommas.format(max_lp)),
                            helper.progressStyle().color(0XB0000, 0X8B0000).textColor(-1),
                            Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555),
                            true));
        if (consume > 0) tooltip.add(helper.text(Lpconsumelang.translate(consume)));
    }

    @CN("§4LP消耗速度:%d/t")
    @EN("§4LP consumption: %d/t")
    public static Lang Lpconsumelang;
}
