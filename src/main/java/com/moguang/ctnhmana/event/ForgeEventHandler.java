package com.moguang.ctnhmana.event;

import com.moguang.ctnhmana.CTNHMana;

import com.moguang.ctnhmana.api.blockentity.IManaMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.forge.CapabilityUtil;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void attachBlockEntityCaps(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof IManaMachineBlockEntity be) {
            event.addCapability(CTNHMana.id("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
        }
    }

}
