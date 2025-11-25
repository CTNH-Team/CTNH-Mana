package com.moguang.ctnhmana.integration.jade;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.moguang.ctnhmana.common.Mutiblock.parts.ManaHatch;
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

public class ManaHatchStatusProvider extends CapabilityBlockProvider<ManaHatch> {
    public ManaHatchStatusProvider() {
        super(GTCEu.id("manahatch_status_provider"));
    }

    @Override
    protected @Nullable ManaHatch getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        if(ManaHatch.getMachine(level,pos) instanceof ManaHatch hatch)return (ManaHatch) ManaHatch.getMachine(level,pos);
        return null;
    }


    @Override
    protected void write(CompoundTag data, ManaHatch hatch) {
        var BT_mana=hatch.getBTMana();
        var mana=hatch.getMana();
        var maxBTMana=hatch.getmaxBTMana();
        var maxmana=hatch.getMaxMana();
        var lp=0;
        var orb_max_lp=0;
        if(hatch.SoulNet!=null&&hatch.HAVE_ORB)
        {
            lp=hatch.SoulNet.getCurrentEssence();
            orb_max_lp=hatch.getOrb().getCapacity();
        }
        data.putInt("lp",lp);
        data.putInt("orb_max_lp",orb_max_lp);
        data.putInt("bt_mana",BT_mana);
        data.putInt("max_bt_mana",maxBTMana);
        data.putLong("mana",mana);
        data.putLong("max_mana",maxmana);

    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        if(!capData.contains("bt_mana")||!capData.contains("mana"))return;
        var helper = tooltip.getElementHelper();
        var bt_mana= capData.getInt("bt_mana");
        var maxBTMana=capData.getInt("max_bt_mana");
        var mana=capData.getLong("mana");
        var maxmana=capData.getLong("max_mana");
        var lp=capData.getInt("lp");
        var max_lp=capData.getInt("orb_max_lp");
        var progress_mana=getProgress(mana,maxmana);

        tooltip.add(
                helper.progress(
                        progress_mana,
                        Component.translatable("ctnhmana.jade.manahatch.manaprogress", DisplayHelper.dfCommas.format(mana), DisplayHelper.dfCommas.format(maxmana)),
                        helper.progressStyle().color(0x00008B, 0x00008B).textColor(-1),
                        Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555),
                        true));
        if(maxBTMana>=1) {
            var progress_bt_mana = getProgress(bt_mana, maxBTMana);
            tooltip.add(
                    helper.progress(
                            progress_bt_mana,
                            Component.translatable("ctnhmana.jade.manahatch.btmanaprogress", DisplayHelper.dfCommas.format(bt_mana), DisplayHelper.dfCommas.format(maxBTMana)),
                            helper.progressStyle().color(0XADD8E6, 0XADD8E6).textColor(-1),
                            Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555),
                            true));
        }
        if(max_lp>0)
        {
            var progress_bm_mana = getProgress(lp, Math.max(lp,max_lp));
            tooltip.add(
                    helper.progress(
                            progress_bm_mana,
                            Component.translatable("ctnhmana.jade.manahatch.bmmanaprogress", DisplayHelper.dfCommas.format(lp), DisplayHelper.dfCommas.format(max_lp)),
                            helper.progressStyle().color(0x8B0000, 0x8B0000).textColor(-1),
                            Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555),
                            true));
        }
    }
}
