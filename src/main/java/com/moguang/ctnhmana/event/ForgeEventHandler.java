package com.moguang.ctnhmana.event;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.moguang.ctnhmana.CTNHMana;

import com.moguang.ctnhmana.common.blockentity.machine.FlowerCakeBlockEntity;
import com.moguang.ctnhmana.common.blockentity.machine.IManaMachineBlockEntity;
import com.moguang.ctnhmana.registry.CMTags;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.item.equipment.bauble.ThirdEyeItem;
import vazkii.botania.forge.CapabilityUtil;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

import java.util.List;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.satori_thirdeye_tooltip;
import static com.moguang.ctnhmana.registry.CMBlocks.RUNE_STONE_PERFECT;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
    @SubscribeEvent
    public static void attachBlockEntityCaps(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof IManaMachineBlockEntity be) {
            event.addCapability(CTNHMana.id("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
        }
        if (event.getObject() instanceof FlowerCakeBlockEntity be) {
            event.addCapability(CTNHMana.id("mana_receiver_flowercake"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
        }
    }
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof ThirdEyeItem) {
            var tooltips = event.getToolTip();
            tooltips = itemTooltipsAdd(satori_thirdeye_tooltip, tooltips);
        }
    }
    @SubscribeEvent
    public void onFMLoadComplete(FMLLoadCompleteEvent event) {
        BloodMagicAPI.INSTANCE.registerAltarComponent(
                RUNE_STONE_PERFECT.getDefaultState(),
                "CRYSTAL"
        );
    }

    public static java.util.List<net.minecraft.network.chat.Component> itemTooltipsAdd(Lang[] langs, List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }
}
