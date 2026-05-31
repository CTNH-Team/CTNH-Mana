package com.moguang.ctnhmana.event;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.ponder.CTNHManaPonderPlugin;
import com.moguang.ctnhmana.item.equipment.SaberWandItem;
import com.moguang.ctnhmana.networking.packets.CMNetworking;
import com.moguang.ctnhmana.registry.*;
import com.moguang.ctnhmana.registry.sounds.CMSoundDefinitionsProvider;
import tech.vixhentx.mcmod.ctnhlib.client.ponder.CTNHPonderLang;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {

    public static void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        CMMachines.init();
        CMMultiblockMachines.init();
    }

    public static void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        CMRecipeTypes.init();
    }

    @SubscribeEvent
    public static void registerRecipeConditions(GTCEuAPI.RegisterEvent<ResourceLocation, RecipeConditionType> event) {
        CMRecipeConditions.init();
    }

    @SubscribeEvent
    public static void registerMaterials(MaterialEvent event) {
        CMMaterials.init();
        CMMaterials.tagPrefixIgnore();
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CMNetworking.init();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    CMItems.SABER_WAND.get(), // 目标物品
                    new ResourceLocation(CTNHMana.MODID, "wand_status"),
                    (stack, level, entity, seed) -> {
                        if (!SaberWandItem.getBindMode(stack)) return 1.0f;
                        return 0f;
                    });
            ItemProperties.register(
                    CMItems.CADUCEUS.get(),
                    new ResourceLocation(CTNHMana.MODID, "tool_type"),
                    (stack, level, entity, seed) -> {
                        if (stack.getTag().contains("caduceus_type_index")) {
                            float num = stack.getTag().getFloat("caduceus_type_index") / 12f;
                            return num;
                        }
                        return 0f;
                    });
        });
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
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
