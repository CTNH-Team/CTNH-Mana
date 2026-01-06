package com.moguang.ctnhmana.integration.jade;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatches.BloodManaHatch;
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
        double rawWill = -1;
        double steadfastWill = -1;
        double corrosiveWill=-1;
        double destructiveWill = 0;
        double vengefulWill = 0;
        double maxwill=0;
        if(hatch.SoulNet!=null&&hatch.HAVE_ORB)
        {
            lp=hatch.SoulNet.getCurrentEssence();
            orb_max_lp=hatch.getOrb().getCapacity();
        }
        if(hatch instanceof BloodManaHatch bmhatch)
        {
            rawWill=bmhatch.rawWill;
            steadfastWill=bmhatch.steadfastWill;
            corrosiveWill=bmhatch.corrosiveWill;
            destructiveWill=bmhatch.destructiveWill;
            vengefulWill=bmhatch.vengefulWill;
            maxwill=bmhatch.maxDemonWill;
        }
        data.putInt("lp",lp);
        data.putInt("orb_max_lp",orb_max_lp);
        data.putInt("bt_mana",BT_mana);
        data.putInt("max_bt_mana",maxBTMana);
        data.putLong("mana",mana);
        data.putLong("max_mana",maxmana);
        data.putDouble("raw_will", rawWill);
        data.putDouble("steadfast_will", steadfastWill);
        data.putDouble("corrosive_will", corrosiveWill);
        data.putDouble("destructive_will", destructiveWill);
        data.putDouble("vengeful_will", vengefulWill);
        data.putDouble("max_will", maxwill);

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
        var rawWill = capData.getDouble("raw_will");
        var steadfastWill = capData.getDouble("steadfast_will");
        var corrosiveWill = capData.getDouble("corrosive_will");
        var destructiveWill = capData.getDouble("destructive_will");
        var vengefulWill = capData.getDouble("vengeful_will");
        var maxwill = capData.getDouble("max_will");

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
        int rawWillBorderColor = 0xFF0099FF;    // §9 普通 -> 蓝色（0099FF）
        int steadfastWillBorderColor = 0xFF9900CC;// §5 坚韧 -> 品红色（9900CC）
        int corrosiveWillBorderColor = 0xFF00CC66; // §a 侵蚀 -> 绿色（00CC66）
        int destructiveWillBorderColor = 0xFFFFCC00;// §6 破坏 -> 金色（FFCC00）
        int vengefulWillBorderColor = 0xFFFF3333;   // §c 复仇 -> 红色（FF3333）

// 1. 普通 Will（rawWill）进度槽：大于0时显示
        if (maxwill > 0 && rawWill > 0) {
            var progress_rawWill = getProgress((long) rawWill, (long) maxwill);
            tooltip.add(
                    helper.progress(
                            progress_rawWill,
                            Component.translatable("ctnhmana.jade.manahatch.rawwillprogress",
                                    DisplayHelper.dfCommas.format(rawWill),
                                    DisplayHelper.dfCommas.format(maxwill)),
                            helper.progressStyle().color(0x8B0000, 0x8B0000).textColor(-1),
                            Util.make(BoxStyle.DEFAULT, style -> style.borderColor = rawWillBorderColor),
                            true
                    )
            );
        }

// 2. 坚韧 Will（steadfastWill）进度槽：大于0时显示
        if (maxwill > 0 && steadfastWill > 0) {
            var progress_steadfastWill = getProgress((long) steadfastWill, (long) maxwill);
            tooltip.add(
                    helper.progress(
                            progress_steadfastWill,
                            Component.translatable("ctnhmana.jade.manahatch.steadfastwillprogress",
                                    DisplayHelper.dfCommas.format(steadfastWill),
                                    DisplayHelper.dfCommas.format(maxwill)),
                            helper.progressStyle().color(0x8B0000, 0x8B0000).textColor(-1),
                            Util.make(BoxStyle.DEFAULT, style -> style.borderColor = steadfastWillBorderColor),
                            true
                    )
            );
        }

// 3. 侵蚀 Will（corrosiveWill）进度槽：大于0时显示
        if (maxwill > 0 && corrosiveWill > 0) {
            var progress_corrosiveWill = getProgress((long) corrosiveWill, (long) maxwill);
            tooltip.add(
                    helper.progress(
                            progress_corrosiveWill,
                            Component.translatable("ctnhmana.jade.manahatch.corrosivewillprogress",
                                    DisplayHelper.dfCommas.format(corrosiveWill),
                                    DisplayHelper.dfCommas.format(maxwill)),
                            helper.progressStyle().color(0x8B0000, 0x8B0000).textColor(-1),
                            Util.make(BoxStyle.DEFAULT, style -> style.borderColor = corrosiveWillBorderColor),
                            true
                    )
            );
        }

// 4. 破坏 Will（destructiveWill）进度槽：大于0时显示
        if (maxwill > 0 && destructiveWill > 0) {
            var progress_destructiveWill = getProgress((long) destructiveWill, (long) maxwill);
            tooltip.add(
                    helper.progress(
                            progress_destructiveWill,
                            Component.translatable("ctnhmana.jade.manahatch.destructivewillprogress",
                                    DisplayHelper.dfCommas.format(destructiveWill),
                                    DisplayHelper.dfCommas.format(maxwill)),
                            helper.progressStyle().color(0x8B0000, 0x8B0000).textColor(-1),
                            Util.make(BoxStyle.DEFAULT, style -> style.borderColor = destructiveWillBorderColor),
                            true
                    )
            );
        }

// 5. 复仇 Will（vengefulWill）进度槽：大于0时显示
        if (maxwill > 0 && vengefulWill > 0) {
            var progress_vengefulWill = getProgress((long) vengefulWill, (long) maxwill);
            tooltip.add(
                    helper.progress(
                            progress_vengefulWill,
                            Component.translatable("ctnhmana.jade.manahatch.vengefulwillprogress",
                                    DisplayHelper.dfCommas.format(vengefulWill),
                                    DisplayHelper.dfCommas.format(maxwill)),
                            helper.progressStyle().color(0x8B0000, 0x8B0000).textColor(-1),
                            Util.make(BoxStyle.DEFAULT, style -> style.borderColor = vengefulWillBorderColor),
                            true
                    )
            );
        }
    }
}
