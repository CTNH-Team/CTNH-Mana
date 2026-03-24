package com.moguang.ctnhmana.data.materials;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.registry.CMElements;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.METALLIC;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.registry.CMMaterials.*;

public class BotaniaMaterials {

    public static void init() {
        ManaSteel = REGISTRATE.material(CTNHMana.id("mana_steel"))
                .cnlang("魔力钢")
                .ingot()
                                .liquid()
                .color(0x438FFE)
                .secondaryColor(0x3962D7)
                .iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE,
                        GENERATE_ROD,
                        GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_RING)
                .cableProperties(GTValues.V[GTValues.LV], 6, 1)

                .buildAndRegister();
        TerraSteel = REGISTRATE.material(CTNHMana.id("terra_steel"))
                .cnlang("泰拉钢")
                .ingot()
                                .liquid()
                .color(0x63D12F)
                .secondaryColor(0x2AB73A)
                .iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE,
                        GENERATE_ROD,
                        GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_RING)
                .cableProperties(GTValues.V[GTValues.HV], 8, 1)
                .buildAndRegister();
        Elementium = REGISTRATE.material(CTNHMana.id("elementium"))
                .cnlang("源质钢")
                .ingot()

                .color(0xf762a3)
                .secondaryColor(0xf768d1)
                                .liquid()
                .iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE,
                        GENERATE_ROD,
                        GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW, GENERATE_FOIL, GENERATE_RING)
                .cableProperties(GTValues.V[GTValues.MV], 10, 1)
                .buildAndRegister();
        AlfSteel = REGISTRATE.material(CTNHMana.id("alfsteel"))
                .cnlang("精灵钢")
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW,
                        GENERATE_FOIL, GENERATE_RING)
                .ingot()
                .color(0xFD9D31)
                .iconSet(METALLIC)
                .cableProperties(GTValues.V[GTValues.EV], 12, 1, false)
                .liquid()
                .buildAndRegister();
        Orichalcos = REGISTRATE.material(CTNHMana.id("orichalcos"))
                .cnlang("奥利哈钢")
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW,
                        GENERATE_FOIL, GENERATE_RING)
                .ingot()
                .color(0xE6E6FA)
                .liquid()
                .iconSet(METALLIC)
                .cableProperties(GTValues.V[GTValues.IV], 8, 0, true)
                .buildAndRegister();
        Aerialite = REGISTRATE.material(CTNHMana.id("aerialite"))
                .cnlang("天空")
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW,
                        GENERATE_FOIL, GENERATE_RING)
                .ingot()
                .color(0x1E90FF)
                .iconSet(METALLIC)
                .cableProperties(GTValues.V[GTValues.HV], 2, 0, true)
                .buildAndRegister();
        Photonium = REGISTRATE.material(CTNHMana.id("photonium"))
                .cnlang("光子")
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW,
                        GENERATE_FOIL, GENERATE_RING)
                .ingot()
                .color(0xF0F0F0)
                .iconSet(METALLIC)
                .cableProperties(GTValues.V[GTValues.EV], 4, 4, false)
                .buildAndRegister();
        DEMON = REGISTRATE.material(CTNHMana.id("hellforged"))
                .cnlang("恶魔钢")
                .liquid()
                .ingot()
                .color(0X00008B)
                .element(CMElements.DEMON)
                .blastTemp(5400, BlastProperty.GasTier.HIGHEST, 4444, 444)
                .cableProperties(GTValues.V[GTValues.IV], 4, 0, false)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW,
                        GENERATE_FOIL, GENERATE_FRAME, GENERATE_RING)
                .buildAndRegister();
        SHADOWIUM = REGISTRATE.material(CTNHMana.id("shaowium"))
                .cnlang("暗影")
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW,
                        GENERATE_FOIL, GENERATE_RING)
                .ingot()
                .element(CMElements.SHADOWIUM)
                .color(0X444444)
                .iconSet(METALLIC)
                .cableProperties(GTValues.V[GTValues.EV], 4, 4, false)
                .buildAndRegister();

        Livingrock = REGISTRATE.material(GTCEu.id("livingrock"))
                .cnlang("活石")
                .dust()
                .color(0xfafafa)
                .buildAndRegister();
    }
}