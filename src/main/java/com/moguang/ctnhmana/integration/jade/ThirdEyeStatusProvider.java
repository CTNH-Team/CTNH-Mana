package com.moguang.ctnhmana.integration.jade;

import com.moguang.ctnhmana.CTNHMana;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import mythicbotany.infuser.TileManaInfuser;
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
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.bauble.ThirdEyeItem;

public class ThirdEyeStatusProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        var player=blockAccessor.getPlayer();

        ItemStack item=EquipmentHandler.findOrEmpty(BotaniaItems.thirdEye,player);
        if(item.getItem() instanceof ThirdEyeItem)
        {
            BlockEntity be = blockAccessor.getBlockEntity();
            var capData = blockAccessor.getServerData().getCompound(getUid().toString());
            var helper = tooltip.getElementHelper();
            if(be instanceof ManaPoolBlockEntity||be instanceof BindableSpecialFlowerBlockEntity||be instanceof FunctionalFlowerBase) {
                var mana = capData.getLong("mana_x");
                var maxmana = capData.getLong("max_mana_x");
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
            if(be instanceof TerrestrialAgglomerationPlateBlockEntity plate||be instanceof TileManaInfuser)
            {
                var plate_mana=capData.getLong("plate_mana");
                var required_mana=capData.getLong("required_mana");
                if(required_mana>0)
                {
                    var progress_tetra_mana = getProgress(plate_mana, required_mana);
                    tooltip.add(
                            helper.progress(
                                    progress_tetra_mana,
                                    Component.translatable("ctnhmana.jade.terra_plate.manaprogress", DisplayHelper.dfCommas.format(plate_mana), DisplayHelper.dfCommas.format(required_mana),DisplayHelper.dfCommas.format(progress_tetra_mana*100)),
                                    helper.progressStyle().color(0XADD8E6, 0XADD8E6).textColor(-1),
                                    Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555),
                                    true));
                }
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
        int plate_mana=0;
        int required_mana=0;
        if(be instanceof ManaPoolBlockEntity pool)
        {
            mana= pool.getCurrentMana();
            max_mana=pool.getMaxMana();
        }
        if(be instanceof TileManaInfuser infuser)
        {
            plate_mana=infuser.getCurrentMana();
            required_mana=infuser.getAvailableSpaceForMana()+plate_mana;
        }
        if(be instanceof TerrestrialAgglomerationPlateBlockEntity plate)
        {
            plate_mana=plate.getCurrentMana();
            required_mana=plate.getAvailableSpaceForMana()+plate_mana;
        }
        if(be instanceof BindableSpecialFlowerBlockEntity flower)
        {
            mana=flower.getMana();
            max_mana=flower.getMaxMana();
        }
        if(be instanceof FunctionalFlowerBase flower)
        {
            mana=flower.getCurrentMana();
            max_mana=flower.maxMana;
        }
        data.putInt("plate_mana",plate_mana);
        data.putInt("required_mana",required_mana);
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
