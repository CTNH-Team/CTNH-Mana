package com.moguang.ctnhmana.integration.jade;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.moguang.ctnhmana.common.multiblock.EternalWosMachine;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.overlay.DisplayHelper;

public class EternalWosStatusProvider extends CapabilityBlockProvider<EternalWosMachine> {

    public EternalWosStatusProvider() {
        super(GTCEu.id("eternal_wos_status_provider"));
    }

    @Override
    protected @Nullable EternalWosMachine getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        if (EternalWosMachine.getMachine(level, pos) instanceof EternalWosMachine machine) {
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, EternalWosMachine machine) {
        boolean producingWill = machine.isFormed() &&
                machine.isSoulInfusionActive() &&
                machine.getRecipeLogic().isWorking() &&
                machine.getSoulWillPerRecipe() > 0;
        data.putBoolean("producing_will", producingWill);
        if (producingWill) {
            data.putDouble("will_output", machine.getSoulWillOutput());
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor blockAccessor,
                              BlockEntity blockEntity, IPluginConfig config) {
        if (!capData.getBoolean("producing_will")) {
            return;
        }
        var helper = tooltip.getElementHelper();
        tooltip.add(helper.text(Component.translatable(
                "ctnhmana.jade.eternal_wos.will_output",
                DisplayHelper.dfCommas.format(capData.getDouble("will_output")))));
    }
}
