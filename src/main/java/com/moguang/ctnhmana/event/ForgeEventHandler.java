package com.moguang.ctnhmana.event;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.Mutiblock.HellForgeMachine;
import com.moguang.ctnhmana.client.gui.radial.CaduceusRadialMenu;
import com.moguang.ctnhmana.client.gui.radial.RadialMenuScreen;
import com.moguang.ctnhmana.common.blockentity.machine.FlowerCakeBlockEntity;
import com.moguang.ctnhmana.common.blockentity.machine.IManaMachineBlockEntity;
import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;
import com.moguang.ctnhmana.networking.packets.IndexFortunaPacket;
import com.moguang.ctnhmana.registry.CMTags;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.item.equipment.bauble.ThirdEyeItem;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.forge.CapabilityUtil;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;

import java.util.List;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.*;
import static com.moguang.ctnhmana.event.CMKeyBindings.FORTUNA;
import static com.moguang.ctnhmana.event.CMKeyBindings.OPEN_CADUCEUS;
import static mythicbotany.register.ModBlocks.petrunia;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void attachBlockEntityCaps(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof IManaMachineBlockEntity be) {
            event.addCapability(CTNHMana.id("mana_receiver"),
                    CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
        }
        if (event.getObject() instanceof FlowerCakeBlockEntity be) {
            event.addCapability(CTNHMana.id("mana_receiver_flowercake"),
                    CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof ThirdEyeItem) {
            var tooltips = event.getToolTip();
            tooltips = itemTooltipsAdd(satori_thirdeye_tooltip, tooltips);
        } else if (stack.getItem().equals(petrunia.asItem())) {
            var tooltips = event.getToolTip();
            tooltips = itemTooltipsAdd(runeAltarFowerLang, tooltips);
        }
        if (stack.is(BotaniaTags.Items.RUNES)) {
            var tooltips = event.getToolTip();

            int tier = 0;
            if (stack.is(CMTags.TIER1_RUNES)) {
                tier = 1;
            } else if (stack.is(CMTags.TIER2_RUNES)) {
                tier = 2;
            } else if (stack.is(CMTags.TIER3_RUNES)) {
                tier = 3;
            } else if (stack.is(CMTags.TIER4_RUNES)) {
                tier = 4;
            } else if (stack.is(CMTags.TIER5_RUNES)) {
                tier = 5;
            }

            if (tier > 0) {
                ChatFormatting tierColor = ChatFormatting.WHITE;
                if (tier == 2) {
                    tierColor = ChatFormatting.YELLOW;
                } else if (tier == 3) {
                    tierColor = ChatFormatting.AQUA;
                } else if (tier == 4) {
                    tierColor = ChatFormatting.LIGHT_PURPLE;
                } else if (tier == 5) {
                    tierColor = ChatFormatting.DARK_PURPLE;
                }
                tooltips.add(runeTierTags.translate(tier).withStyle(tierColor));
            }

            boolean isFire = stack.is(CMTags.ELEMENT_FIRE);
            boolean isWater = stack.is(CMTags.ELEMENT_WATER);
            boolean isWind = stack.is(CMTags.ELEMENT_WIND);
            boolean isEarth = stack.is(CMTags.ELEMENT_EARTH);
            boolean isSin = stack.is(CMTags.ELEMENT_SIN);

            boolean hasElement = isFire || isWater || isWind || isEarth || isSin;

            if (hasElement) {
                tooltips.add(runeElementTags[0].translate());
                if (isFire) {
                    tooltips.add(runeElementTags[1].translate().withStyle(ChatFormatting.RED));
                }
                if (isWater) {
                    tooltips.add(runeElementTags[2].translate().withStyle(ChatFormatting.AQUA));
                }
                if (isWind) {
                    tooltips.add(runeElementTags[3].translate().withStyle(ChatFormatting.GREEN));
                }
                if (isEarth) {
                    tooltips.add(runeElementTags[4].translate().withStyle(ChatFormatting.GOLD));
                }
                if (isSin) {
                    tooltips.add(runeElementTags[5].translate().withStyle(ChatFormatting.DARK_PURPLE));
                }
            }
        }
    }

    public static java.util.List<net.minecraft.network.chat.Component> itemTooltipsAdd(Lang[] langs,
                                                                                       List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }

    @SubscribeEvent
    public static void LivingDeathEvent(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (entity instanceof Player) {
            return;
        }
        BlockPos entityPos = entity.blockPosition();
        BlockState blockState = level.getBlockState(entityPos);
        var chunk = level.getChunk(entityPos.getX() >> 4, entityPos.getZ() >> 4);
        if (!chunk.getBlockEntities().isEmpty()) {
            for (BlockEntity machine : chunk.getBlockEntities().values()) {
                if (machine instanceof MetaMachineBlockEntity mme &&
                        mme.getMetaMachine() instanceof HellForgeMachine hmachine && hmachine.isFormed() &&
                        hmachine.hatch != null) {
                    hmachine.hatch.rawWill = Math.min(hmachine.hatch.maxDemonWill,
                            hmachine.hatch.rawWill + entity.getMaxHealth() / 20);
                }
            }
        }
        if (blockState.is(BloodMagicFluids.LIFE_ESSENCE_BLOCK.get())) {
            level.setBlockAndUpdate(entityPos, BloodMagicFluids.DOUBT_BLOCK.get().defaultBlockState());
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void keyEvent(final InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || event.getAction() != 1) {
            return;
        }

        boolean isMPressed = event.getKey() == OPEN_CADUCEUS.getKey().getValue(); // N
        if (player.getMainHandItem().getItem() instanceof CaduceusItem && isMPressed) {
            mc.setScreen(new RadialMenuScreen<>(CaduceusRadialMenu.create()));
        }
        if (player.getMainHandItem().getItem() instanceof CaduceusItem &&
                event.getKey() == FORTUNA.getKey().getValue()) {
            com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK.sendToServer(
                    new IndexFortunaPacket(1));
        }
    }
}
