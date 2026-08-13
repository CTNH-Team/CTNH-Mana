package com.magicbee.ctnhmana.client.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import com.magicbee.ctnhmana.CTNHMana;

public class CTNHManaPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return CTNHMana.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        CTNHManaPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        CTNHManaPonderTags.register(helper);
    }
}
