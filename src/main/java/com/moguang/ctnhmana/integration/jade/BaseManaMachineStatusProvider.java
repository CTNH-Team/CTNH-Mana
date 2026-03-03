package com.moguang.ctnhmana.integration.jade;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.moguang.ctnhmana.Mutiblock.BaseManaMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static com.moguang.ctnhmana.Mutiblock.BaseManaMachine.BaseManaMachineLang;

public class BaseManaMachineStatusProvider extends CapabilityBlockProvider<BaseManaMachine> {
    public BaseManaMachineStatusProvider() {
        super(GTCEu.id("manamachine_status_provider"));
    }
    @Override
    protected @Nullable BaseManaMachine getCapability(Level level, BlockPos pos, @Nullable Direction direction) {
        if(BaseManaMachine.getMachine(level,pos) instanceof BaseManaMachine machine)return (BaseManaMachine)BaseManaMachine.getMachine(level,pos);
        return null;
    }

    @Override
    protected void write(CompoundTag data, BaseManaMachine machine) {
        String upgrade = "NONE";
        if (machine.isFormed() && machine.getUpgrade() != null) {
            upgrade = machine.getUpgrade().getUpdateName().translate().getString();
        }
        data.putString("upgrade", upgrade);
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor blockAccessor, BlockEntity blockEntity, IPluginConfig iPluginConfig) {
        if (!capData.contains("upgrade")) return;
        var upgrade = capData.getString("upgrade");
        if ("NONE".equals(upgrade)) return;

        var helper = tooltip.getElementHelper();
        tooltip.add(helper.text(BaseManaMachineLang[2].translate(upgrade)));
    }
}
