package com.moguang.ctnhmana.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.moguang.ctnhmana.CTNHMana;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CMKeyBindings {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    public static final String CATEGORY = "key.category.ctnhmana.general";

    public static final KeyMapping OPEN_CADUCEUS = new KeyMapping("key.ctnhmana.open_caduceus", GLFW.GLFW_KEY_N,
            CATEGORY);
    public static final KeyMapping FORTUNA = new KeyMapping("key.ctnhmana.fortuna", GLFW.GLFW_KEY_C, CATEGORY);

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(FORTUNA);
        event.register(OPEN_CADUCEUS);
    }
}
