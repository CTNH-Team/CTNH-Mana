package com.moguang.ctnhmana.registry.sounds;

import com.moguang.ctnhmana.CTNHMana;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;


public class CMSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public CMSoundDefinitionsProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }

    @Override
    public void registerSounds() {
        this.add(CMSoundEvent.SHROUD_WHISPER_EFFECT.get(),definition()
                .subtitle("subtitle.ctnhmana.bgm.shroud_whisper")
                .with(sound(CTNHMana.id("shroud_whisper"))
                        .weight(3)
                        .volume(0.6)
                        .stream())
        );
    }
}
