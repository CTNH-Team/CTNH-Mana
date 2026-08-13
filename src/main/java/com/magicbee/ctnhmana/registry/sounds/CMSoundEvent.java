package com.magicbee.ctnhmana.registry.sounds;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import com.magicbee.ctnhmana.CTNHMana;

public class CMSoundEvent {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT,
            CTNHMana.MODID);

    public static final RegistryObject<SoundEvent> SHROUD_WHISPER_EFFECT = SOUNDS.register(
            "shroud_whisper",
            () -> SoundEvent.createVariableRangeEvent(CTNHMana.id("shroud_whisper")));
    public static final RegistryObject<SoundEvent> INDEX_BEEP_EFFECT = SOUNDS.register(
            "index_beep",
            () -> SoundEvent.createVariableRangeEvent(CTNHMana.id("index_beep")));
}
