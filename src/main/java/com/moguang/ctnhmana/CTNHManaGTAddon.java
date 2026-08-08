package com.moguang.ctnhmana;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import com.google.gson.JsonObject;
import com.moguang.ctnhmana.data.recipe.*;
import com.moguang.ctnhmana.registry.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@GTAddon
public class CTNHManaGTAddon implements IGTAddon {

    @Override
    public GTRegistrate getRegistrate() {
        return CTNHMana.REGISTRATE;
    }

    @Override
    public void initializeAddon() {
        CMItems.init();
        CMBlocks.init();
        CMBlockEntities.init();
    }

    @Override
    public String addonModId() {
        return CTNHMana.MODID;
    }

    // @Override
    // public void registerMultiblockPreviewHighlighters(MultiblockPreviewHighlightRegistry registry) {
    // registry.registerAbilityHighlight(MultiblockPreviewHighlightRegistry.POWER_COLOR, CMPartsAbility.MANAHATCH);
    // }

    @Override
    public void registerTagPrefixes() {
        CMTagPrefixes.init();
    }

    @Override
    public void registerElements() {
        CMElements.init();
    }

    @Override
    public void registerSounds() {}

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        ManaReactorRecipes.init(provider);
        HellForgeRecipes.init(provider);
        WishingWillRecipes.init(provider);
        ElvenTradeRecipes.init(provider);
        BloodAltarRecipes.init(provider);
        MeteorCapturerRecipes.init(provider);
        DemonWillGeneratorRecipes.init(provider);
        ManaCondenserRecipes.init(provider);
        BotaniaRecipes.init(provider);
        MachineRecipes.init(provider);
        ManaRecipes.init(provider);
        BeamsRecipes.init(provider);
        RuneAltarRecipes.init(provider);
        runeRitualRecipes.init(provider);
        TerraPlateRecipes.init(provider);
        ManaPoolRecipes.init(provider);
        ManaMachineBlockRecipes.init(provider);
        GaiaReactorRecipes.init(provider);
        ManaMachineRecipes.init(provider);
        ManaHatchRecipes.init(provider);
        EternalGardenRecipes.init(provider);
        EternalGardenSpecialRecipes.init(provider);
        ManaCircuitRecipes.init(provider);
        ManaMachineUpgradeRecipes.init(provider);
        ZenithRecipes.init(provider);
        TwistCollapseRecipes.init(provider);
        RitualMechanicalRecipes.init(provider);
        MeteorRitualGuideRecipes.init(provider);
        SalvagingRecipes.init(provider);
        GemCuttingRecipes.init(provider);
        EternalWosRecipes.init(provider);
        ManaTransformerRecipes.init(provider);
    }

    // 这个函数用于重新注册其他模组被删除的配方，因为被删除的配方如果id不变即使重新注册也会被移除，故通过这个函数将配方的命名空间变为ctnhmana
    Consumer<FinishedRecipe> changeId(Consumer<FinishedRecipe> provider) {
        return r -> {

            provider.accept(new FinishedRecipe() {

                @Override
                public void serializeRecipeData(JsonObject jsonObject) {
                    r.serializeRecipeData(jsonObject);
                }

                @Override
                public ResourceLocation getId() {
                    return CTNHMana.id(r.getId().getNamespace() + '/' + r.getId().getPath());
                }

                @Override
                public RecipeSerializer<?> getType() {
                    return r.getType();
                }

                @Override
                public @Nullable JsonObject serializeAdvancement() {
                    return r.serializeAdvancement();
                }

                @Override
                public @Nullable ResourceLocation getAdvancementId() {
                    return r.getAdvancementId();
                }
            });
        };
    }

    @Override
    public void removeRecipes(Consumer<ResourceLocation> consumer) {
        RecipeRemoval.init(consumer);
        // DataFilterPack.removeRecipeType("bloodmagic", "altar");
        // DataFilterPack.removeRecipeType("botania","petal_apothecary");
        // DataFilterPack.removeRecipeType("botania","runic_altar");
        // DataFilterPack.removeRecipeType("botania","terra_plate");
        // DataFilterPack.removeRecipeType("extrabotany","petal_apothecary");
        // DataFilterPack.removeRecipeType("mythicbotany:.*_runic_altar");
        // DataFilterPack.removeRecipeType("bloodmagic", "soulforge");
        // DataFilterPack.removeRecipeType("bloodmagic:.*_from_dungeon_raw_stonecutting");
        //
        // DataFilterPack.removeRecipe("bloodmagic:soulforge/demon_crystallizer");
    }
}