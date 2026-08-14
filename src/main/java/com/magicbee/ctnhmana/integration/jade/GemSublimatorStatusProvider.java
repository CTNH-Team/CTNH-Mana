package com.magicbee.ctnhmana.integration.jade;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
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
import com.magicbee.ctnhmana.common.machine.GemSublimatorMachine;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

/**
 * 宝石刻格机专用 JADE：显示自定义进度点 {@code progress/maxProgress}，
 * 而不是 GT {@code WorkLogicMachineProvider} 的 tick/秒格式。
 * <p>
 * 机器侧 {@link GemSublimatorMachine} 不覆盖 {@code IWorkLogicMachine#getProgress()}，
 * 保持默认 0，避免通用 workable JADE 误显示。
 */
public class GemSublimatorStatusProvider extends CapabilityBlockProvider<GemSublimatorMachine> {

    /** 青玉色进度条，与魔力类深蓝条区分 */
    private static final int BAR_COLOR = 0xFF1ABC9C;

    @CN("刻格进度：%s / %s")
    @EN("Engraving: %s / %s")
    public static Lang jadeProgress;

    public GemSublimatorStatusProvider() {
        super(GTCEu.id("gem_sublimator_status_provider"));
    }

    @Override
    protected @Nullable GemSublimatorMachine getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        if (MetaMachine.getMachine(level, pos) instanceof GemSublimatorMachine machine) {
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, GemSublimatorMachine machine) {
        data.putInt("Progress", machine.getProgress());
        data.putInt("MaxProgress", machine.getMaxProgress());
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        int current = capData.getInt("Progress");
        int max = capData.getInt("MaxProgress");
        if (max <= 0) {
            return;
        }

        var helper = tooltip.getElementHelper();
        Component text = jadeProgress.translate(
                String.valueOf(current),
                String.valueOf(max));
        tooltip.add(
                helper.progress(
                        getProgress(current, max),
                        text,
                        helper.progressStyle().color(BAR_COLOR, BAR_COLOR).textColor(-1),
                        Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555),
                        true));
    }
}
