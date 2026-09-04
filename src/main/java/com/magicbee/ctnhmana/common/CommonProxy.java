package com.magicbee.ctnhmana.common;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.common.unification.material.MaterialRegistryManager;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.magicbee.ctnhmana.CMConfig;
import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.client.ponder.CTNHManaPonderPlugin;
import com.magicbee.ctnhmana.data.CMDatagen;
import com.magicbee.ctnhmana.networking.packets.CMNetworking;
import com.magicbee.ctnhmana.registry.*;
import com.magicbee.ctnhmana.registry.sounds.CMSoundDefinitionsProvider;
import com.magicbee.ctnhmana.registry.sounds.CMSoundEvent;
import tech.vixhentx.mcmod.ctnhlib.client.ponder.CTNHPonderLang;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

@SuppressWarnings("removal")
public class CommonProxy {

    public CommonProxy() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
        init();
    }

    @SuppressWarnings("removal")
    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(MachineDefinition.class, CommonProxy::registerMachines);
        modEventBus.addGenericListener(GTRecipeType.class, CommonProxy::registerRecipeTypes);
        modEventBus.addGenericListener(RecipeConditionType.class, CommonProxy::registerRecipeConditions);

        CMParticleTypes.PARTICLE_TYPES.register(modEventBus);
        CMMobEffects.MOB_EFFECTS.register(modEventBus);
        CMSoundEvent.SOUNDS.register(modEventBus);

        CMEntities.init();
        CMCreativeModeTabs.init();
        CTNHMana.REGISTRATE.registerRegistrate();
        CMDatagen.init();
        // CMRecipes.init(modEventBus);
        CMConfig.init();
    }

    @SubscribeEvent
    public void addMaterialFlag(MaterialEvent event) {
        GTMaterialAddon.init();
    }

    public static void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        CMMachines.init();
        CMMultiblockMachines.init();
    }

    public static void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        CMRecipeTypes.init();
    }

    public static void registerRecipeConditions(GTCEuAPI.RegisterEvent<ResourceLocation, RecipeConditionType> event) {
        CMRecipeConditions.init();
    }

    @SubscribeEvent
    public void registerMaterial(MaterialRegistryEvent event) {
        MaterialRegistryManager.getInstance().createRegistry(CTNHMana.MODID);
    }

    @SubscribeEvent
    public void registerMaterials(MaterialEvent event) {
        CMMaterials.init();
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        CMNetworking.init();
    }

    @SubscribeEvent
    public void onFMLoadComplete(FMLLoadCompleteEvent event) {
        BloodMagicAPI.INSTANCE.registerAltarComponent(
                CMBlocks.CASING_BLOODLOGIC.getDefaultState(),
                "CRYSTAL");
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        var registries = event.getLookupProvider();
        if (event.includeClient()) {
            generator.addProvider(true, new CMSoundDefinitionsProvider(packOutput, CTNHMana.MODID, existingFileHelper));
            CTNHPonderLang.init(new CTNHManaPonderPlugin());
        }
    }
}
