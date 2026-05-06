package com.moguang.ctnhmana;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.moguang.ctnhmana.client.ClientProxy;
import com.moguang.ctnhmana.common.CommonProxy;
import com.moguang.ctnhmana.event.EventHandler;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMMobEffects;
import com.moguang.ctnhmana.registry.CMParticleTypes;
import com.moguang.ctnhmana.registry.CMRegistrate;
import com.moguang.ctnhmana.registry.sounds.CMSoundEvent;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

@Mod(CTNHMana.MODID)
public class CTNHMana {

    public static final String MODID = "ctnhmana";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String CUSTOM_TAG_SOURCE = "CTNH Custom Tags";
    public static final CMRegistrate REGISTRATE = CMRegistrate.create();

    @SuppressWarnings("removal")
    public CTNHMana() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CMParticleTypes.PARTICLE_TYPES.register(modEventBus);

        modEventBus.addListener(this::onRegisterEntityRenderers);
        modEventBus.addListener(this::onFMLoadComplete);
        modEventBus.addGenericListener(MachineDefinition.class, EventHandler::registerMachines);
        modEventBus.addGenericListener(GTRecipeType.class, EventHandler::registerRecipeTypes);
        modEventBus.addGenericListener(RecipeConditionType.class, EventHandler::registerRecipeConditions);
        CMMobEffects.MOB_EFFECTS.register(modEventBus);
        CMSoundEvent.SOUNDS.register(modEventBus);

        // modEventBus.addGenericListener(GTRecipeCategory.class, EventHandler::onRecipeCategoryRegister);
        // modEventBus.addGenericListener(ChanceLogic.class,EventHandler::registerChanceLogic);

        DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.tryParse(MODID + ":" + name);
    }

    public void onFMLoadComplete(FMLLoadCompleteEvent event) {
        BloodMagicAPI.INSTANCE.registerAltarComponent(
                CMBlocks.CASING_BLOODLOGIC.getDefaultState(),
                "CRYSTAL");
    }

    private void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {}
}
