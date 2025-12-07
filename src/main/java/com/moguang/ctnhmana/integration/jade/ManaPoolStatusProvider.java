package com.moguang.ctnhmana.integration.jade;

import com.moguang.ctnhmana.CTNHMana;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.overlay.DisplayHelper;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.bauble.ThirdEyeItem;

import java.util.List;

public class ManaPoolStatusProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        var player=blockAccessor.getPlayer();

        ItemStack item=EquipmentHandler.findOrEmpty(BotaniaItems.thirdEye,player);
        if(item.getItem() instanceof ThirdEyeItem)
        {
            BlockEntity be = blockAccessor.getBlockEntity();
            var capData = blockAccessor.getServerData().getCompound(getUid().toString());
            var mana=capData.getLong("mana_x");
            var maxmana=capData.getLong("max_mana_x");
            var helper = tooltip.getElementHelper();
            if(maxmana>0)
            {
                var progress_bt_mana = getProgress(mana, maxmana);
                tooltip.add(
                        helper.progress(
                                progress_bt_mana,
                                Component.translatable("ctnhmana.jade.manahatch.btmanaprogress", DisplayHelper.dfCommas.format(mana), DisplayHelper.dfCommas.format(maxmana)),
                                helper.progressStyle().color(0XADD8E6, 0XADD8E6).textColor(-1),
                                Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555),
                                true));
            }
        }

    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
        BlockEntity be = blockAccessor.getBlockEntity();
        CompoundTag capData = tag.getCompound(getUid().toString());
        CompoundTag data = new CompoundTag();
        int mana=0;
        int max_mana=0;
        if(be instanceof ManaPoolBlockEntity pool)
        {
            mana= pool.getCurrentMana();
            max_mana=pool.getMaxMana();
        }
        data.putInt("mana_x",mana);
        data.putInt("max_mana_x",max_mana);
        tag.put(getUid().toString(),data);
    }

    @Override
    public ResourceLocation getUid() {
        return CTNHMana.id("mana_pool_status");
    }
    protected float getProgress(long progress, long maxProgress) {
        return maxProgress == 0L ? 0.0F : (float)((double)progress / (double)maxProgress);
    }
}
