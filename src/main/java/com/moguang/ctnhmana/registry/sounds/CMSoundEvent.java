package com.moguang.ctnhmana.registry.sounds;

import com.moguang.ctnhmana.CTNHMana;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.moguang.ctnhmana.CTNHMana.MODID;

public class CMSoundEvent {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT,CTNHMana.MODID);

    public static final RegistryObject<SoundEvent> SHROUD_WHISPER_EFFECT = SOUNDS.register(
            "shroud_whisper",
            () -> SoundEvent.createVariableRangeEvent(CTNHMana.id("shroud_whisper"))
    );
}
