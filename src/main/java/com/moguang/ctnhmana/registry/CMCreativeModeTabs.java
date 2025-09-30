package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.moguang.ctnhmana.CTNHMana;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTab;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;


public class CMCreativeModeTabs {
    public static RegistryEntry<CreativeModeTab> MACHINE = REGISTRATE.defaultCreativeTab("machine",
                builder -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("machine", REGISTRATE))
                        //.icon(MultiblocksA.ASTRONOMICAL_OBSERVATORY::asStack)
                        .title(REGISTRATE.addLang("itemGroup", CTNHMana.id("machine"), "CTNH ManaMachines"))
                        .build())
        .register();
    public static RegistryEntry<CreativeModeTab> ITEM = REGISTRATE.defaultCreativeTab("item",
                    builder -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("item", REGISTRATE))
                            //.icon(CTNHItems.ASTRONOMY_CIRCUIT_1::asStack)
                            .title(REGISTRATE.addLang("itemGroup", CTNHMana.id("item"), "CTNH ManaItems"))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab> BLOCK = REGISTRATE.defaultCreativeTab("block",
                    builder -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("block", REGISTRATE))
                            //.icon(CTNHBlocks.CASING_REFLECT_LIGHT::asStack)
                            .title(REGISTRATE.addLang("itemGroup", CTNHMana.id("block"), "CTNH ManaBlocks"))
                            .build())
            .register();

    public static void init() {

    }
}
