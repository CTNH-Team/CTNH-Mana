package com.moguang.ctnhmana.event;

import com.moguang.ctnhmana.CTNHMana;

import com.moguang.ctnhmana.api.blockentity.IManaMachineBlockEntity;
import com.moguang.ctnhmana.client.render.ShroudGazingRender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.bauble.ThirdEyeItem;
import vazkii.botania.forge.CapabilityUtil;

import java.awt.*;
import java.util.List;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.satori_thirdeye_tooltip;
import static net.minecraftforge.fml.loading.FMLEnvironment.dist;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void attachBlockEntityCaps(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof IManaMachineBlockEntity be) {
            event.addCapability(CTNHMana.id("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
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

    public static java.util.List<net.minecraft.network.chat.Component> itemTooltipsAdd(Lang[] langs, List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }
}
