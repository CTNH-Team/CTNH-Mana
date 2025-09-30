package com.moguang.ctnhmana.data.materials;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.moguang.ctnhmana.CTNHMana;


import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.METALLIC;
import static com.moguang.ctnhmana.registry.CMMaterials.*;

public class BotaniaMaterials {
    public static void init() {
        ManaSteel = new Material.Builder(CTNHMana.id("mana_steel"))
                .ingot()
                .fluid()
                .color(0x438FFE)
                .secondaryColor(0x3962D7)
                .iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE,
                        GENERATE_ROD,
                        GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING
                        )
                .cableProperties(GTValues.V[GTValues.LV], 6, 1)
                .buildAndRegister();
        TerraSteel = new Material.Builder(CTNHMana.id("terra_steel"))
                .ingot()
                .fluid()
                .color(0x63D12F)
                .secondaryColor(0x2AB73A)
                .iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE,
                        GENERATE_ROD,
                        GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
                .cableProperties(GTValues.V[GTValues.HV], 6, 1)
                .buildAndRegister();
        Elementium = new Material.Builder(CTNHMana.id("elementium"))
                .ingot()
                .fluid()
                .color(0xf762a3)
                .secondaryColor(0xf768d1)
                .iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE,
                        GENERATE_ROD,
                        GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
                .cableProperties(GTValues.V[GTValues.MV], 6, 1)
                .buildAndRegister();
        AlfSteel = new Material.Builder(CTNHMana.id("alfsteel"))
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
                .ingot()
                .color(0xFD9D31)
                .iconSet(METALLIC)
                .cableProperties(GTValues.V[GTValues.EV], 6, 1, false)
                .buildAndRegister();
    }
}
