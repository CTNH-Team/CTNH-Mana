package com.magicbee.ctnhmana.integration.emi;

import com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory;

import com.magicbee.ctnhmana.registry.CMRecipeTypes;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;

import static com.magicbee.ctnhmana.registry.CMMultiblockMachines.AHCC;
import static com.magicbee.ctnhmana.registry.CMMultiblockMachines.RITUAL_MECHANICAL_ARRAY;

@EmiEntrypoint
public class CTNHManaEmiPlugin implements EmiPlugin {

    @Override
    public void initialize(EmiInitRegistry registry) {}

    @Override
    public void register(EmiRegistry registry) {
        registry.addWorkstation(
                GTRecipeEMICategory.CATEGORIES.apply(CMRecipeTypes.TwistCollapse.getCategory()),
                EmiStack.of(AHCC.asStack()));
        registry.addWorkstation(
                GTRecipeEMICategory.CATEGORIES.apply(CMRecipeTypes.METEOR_RITUAL_GUIDE.getCategory()),
                EmiStack.of(RITUAL_MECHANICAL_ARRAY.asStack()));
    }
}
