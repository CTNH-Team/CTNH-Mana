package com.moguang.ctnhmana.event;

import com.moguang.ctnhmana.CTNHMana;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CTNHMana.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class CMKeyBindings {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    public static final String CATEGORY = "key.category.ctnhmana.general";

    public static final KeyMapping OPEN_CADUCEUS = new KeyMapping("key.ctnhmana.open_caduceus", GLFW.GLFW_KEY_N, CATEGORY);

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_CADUCEUS);
    }
}