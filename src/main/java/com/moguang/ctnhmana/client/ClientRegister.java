package com.moguang.ctnhmana.client;


import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.render.ShroudGazingRender;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegister {

    @SubscribeEvent
    public static void onLayerRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

}
