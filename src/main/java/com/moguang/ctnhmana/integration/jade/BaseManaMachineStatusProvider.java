package com.moguang.ctnhmana.integration.jade;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.moguang.ctnhmana.Mutiblock.BaseManaMachine;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.overlay.DisplayHelper;

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

        long mana=0;
        long maxMana=0;
        String upgrade="NONE";
        if(machine.isFormed()&&machine.hatch!=null)
        {
            mana=machine.hatch.getMana();
            maxMana=machine.hatch.getMaxMana();
            if(machine.getUpgrade()!=null)
            {
                upgrade=machine.getUpgrade().getUpdateName().translate().getString();
            }
        }
        data.putLong("mana",mana);
        data.putLong("max_mana",maxMana);
        data.putString("upgrade",upgrade);

    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor blockAccessor, BlockEntity blockEntity, IPluginConfig iPluginConfig) {
        if(!capData.contains("max_mana")||!capData.contains("mana"))return;
        var mana=capData.getLong("mana");
        var maxmana=capData.getLong("max_mana");

        var progress_mana=getProgress(mana,maxmana);
        var helper = tooltip.getElementHelper();
        var upgrade=capData.getString("upgrade");
        if(maxmana>0)
        tooltip.add(
                helper.progress(
                        progress_mana,
                        Component.translatable("ctnhmana.jade.manahatch.manaprogress", DisplayHelper.dfCommas.format(mana), DisplayHelper.dfCommas.format(maxmana)),
                        helper.progressStyle().color(0x00008B, 0x00008B).textColor(-1),
                        Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555),
                        true));
        if(!upgrade.equals("NONE")) tooltip.add(helper.text(BaseManaMachineLang[2].translate(upgrade)));
    }
}
