package com.moguang.ctnhmana.client;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.render.ShroudGazingRender;
import com.moguang.ctnhmana.registry.CMMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.fml.loading.FMLEnvironment.dist;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
public class ClientForgeRegister {

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiOverlayEvent.Post event) {
        ShroudGazingRender.renderPurpleTint(event.getGuiGraphics().pose(), event.getPartialTick());
    }
    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        // 仅当玩家有 ShroudGazing 效果时修改 FOV
        if (player != null && player.hasEffect(CMMobEffects.ShroudGazing.get())) {
            // event.getNewFovModifier() 是当前 FOV 修正系数（默认1.0）
            // 乘以缩放系数实现视野缩小
            event.setNewFovModifier((float) (event.getNewFovModifier() * 0.8));
        }
    }
}
