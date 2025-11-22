package com.moguang.ctnhmana.client;


import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.api.blockentity.IZenithMartixBlockEntity;
import com.moguang.ctnhmana.client.render.ShroudGazingRender;
import com.moguang.ctnhmana.client.render.ZenithMatrixBlockEntityRender;
import com.moguang.ctnhmana.common.CommonProxy;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
        init();
    }

    public static void init() {
        DynamicRenderManager.register(CTNHMana.id("zenith_laser"), ZenithMatrixBlockEntityRender.TYPE);
    }

    @SubscribeEvent
    public void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        event.registerEntityRenderer(CBEntities.BASIC_MOB.get(),
//                BasicLivingMachineEntityRenderer::new
//        );

    }

}
