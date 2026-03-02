package com.moguang.ctnhmana.event;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.moguang.ctnhmana.CTNHMana;

import com.moguang.ctnhmana.Mutiblock.HellForgeMachine;
import com.moguang.ctnhmana.common.blockentity.machine.FlowerCakeBlockEntity;
import com.moguang.ctnhmana.common.blockentity.machine.IManaMachineBlockEntity;
import com.moguang.ctnhmana.client.gui.radial.CaduceusRadialMenu;
import com.moguang.ctnhmana.client.gui.radial.RadialMenuScreen;
import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;
import com.moguang.ctnhmana.networking.packets.CMNetworking;
import com.moguang.ctnhmana.networking.packets.CaduceusPacket;
import com.moguang.ctnhmana.networking.packets.IndexFortunaPacket;
import com.moguang.ctnhmana.networking.packets.IndexTargetParticlePacket;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMTags;
import com.moguang.ctnhmana.registry.multiblock.BloodMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.item.equipment.bauble.ThirdEyeItem;
import vazkii.botania.forge.CapabilityUtil;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

import java.util.List;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.satori_thirdeye_tooltip;
import static com.moguang.ctnhmana.event.CMKeyBindings.FORTUNA;
import static com.moguang.ctnhmana.event.CMKeyBindings.OPEN_CADUCEUS;
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



    public static java.util.List<net.minecraft.network.chat.Component> itemTooltipsAdd(Lang[] langs, List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }
    @SubscribeEvent
    public static void LivingDeathEvent(LivingDeathEvent event)
    {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (entity instanceof Player) {
            return;
        }
        BlockPos entityPos = entity.blockPosition();
        BlockState blockState = level.getBlockState(entityPos);
        var chunk=level.getChunk(entityPos.getX()>>4,entityPos.getZ()>>4);
        if(!chunk.getBlockEntities().isEmpty())
        {
            for(BlockEntity machine:chunk.getBlockEntities().values())
            {
                if(machine instanceof MetaMachineBlockEntity mme&&mme.getMetaMachine() instanceof HellForgeMachine hmachine&&hmachine.isFormed())
                {
                    hmachine.hatch.rawWill=Math.max(hmachine.hatch.maxDemonWill,hmachine.hatch.rawWill+entity.getMaxHealth()/20);
                }
            }
        }
        if(blockState.is(BloodMagicFluids.LIFE_ESSENCE_BLOCK.get()))
        {
            level.setBlockAndUpdate(entityPos,BloodMagicFluids.DOUBT_BLOCK.get().defaultBlockState());
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
        if(player.getMainHandItem().getItem() instanceof CaduceusItem&&event.getKey()==FORTUNA.getKey().getValue())
        {
            com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK.sendToServer(
                    new IndexFortunaPacket()
            );
        }

    }
}