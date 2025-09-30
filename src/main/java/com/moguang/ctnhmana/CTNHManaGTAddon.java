package com.moguang.ctnhmana;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMElements;
import com.moguang.ctnhmana.registry.CMItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

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
    }

    @Override
    public String addonModId() {
        return CTNHMana.MODID;
    }

    @Override
    public void registerTagPrefixes() {

       // CMTagPrefixes.init();
    }

    @Override
    public void registerElements() {
        CMElements.init();
    }



    @Override
    public void registerSounds() {
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {

    }

    @Override
    public void removeRecipes(Consumer<ResourceLocation> consumer) {

    }
}
