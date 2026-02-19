package com.moguang.ctnhmana.event;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.DimensionMarker;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.MaterialProperties;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.data.tags.BiomeTagsLoader;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import com.hollingsworth.arsnouveau.client.gui.radial_menu.GuiRadialMenu;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketGenericClientMessage;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.moguang.ctnhmana.CTNHMana;

import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;
import com.moguang.ctnhmana.item.equipment.SaberWandItem;
import com.moguang.ctnhmana.networking.packets.CMNetworking;
import com.moguang.ctnhmana.registry.*;
import com.moguang.ctnhmana.registry.sounds.CMSoundDefinitionsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.checkerframework.checker.signature.qual.Identifier;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

import java.util.Set;

import static com.hollingsworth.arsnouveau.client.keybindings.KeyHandler.checkKeysPressed;

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


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                   CMItems.SABER_WAND.get(), // 目标物品
                    new ResourceLocation(CTNHMana.MODID, "wand_status"), // 属性标识符
                    (stack, level, entity, seed) -> {
                       if(!SaberWandItem.getBindMode(stack))return 1.0F;
                       return 0F;
                    }
            );
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
        }
    }
}