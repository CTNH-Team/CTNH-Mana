package com.moguang.ctnhmana.registry;


import com.gregtechceu.gtceu.api.data.chemical.material.properties.*;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMedicalConditions;


import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;


public class GTMaterialAddon {
    public static HazardProperty radioactive(float multiplier) {
        return new HazardProperty(HazardProperty.HazardTrigger.ANY,
                GTMedicalConditions.CARCINOGEN, multiplier, true);
    }
    public static void init() {

    }
}
