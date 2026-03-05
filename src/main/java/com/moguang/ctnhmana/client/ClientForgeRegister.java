package com.moguang.ctnhmana.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.render.ShroudGazingRender;
import com.moguang.ctnhmana.registry.CMMobEffects;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeRegister {

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiOverlayEvent.Post event) {
        ShroudGazingRender.renderPurpleTint(event.getGuiGraphics().pose(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player != null && player.hasEffect(CMMobEffects.ShroudGazing.get())) {
            event.setNewFovModifier((float) (event.getNewFovModifier() * 0.8));
        }
    }
}
