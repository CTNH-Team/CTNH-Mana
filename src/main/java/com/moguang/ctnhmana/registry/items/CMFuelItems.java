package com.moguang.ctnhmana.registry.items;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.world.item.Item;

import com.moguang.ctnhmana.item.ManaFuelStick.IManaFuelStick;
import com.moguang.ctnhmana.registry.CMCreativeModeTabs;
import com.moguang.ctnhmana.registry.CMTags;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

/**
 * 注术单元类物品集中注册，风格与 {@link com.moguang.ctnhmana.registry.CMItems} 一致。
 */
public final class CMFuelItems {

    static {
        REGISTRATE.creativeModeTab(() -> CMCreativeModeTabs.ITEM);
    }

    /** 崩解态：仅 {@link ComponentItem}，无注术单元燃料机制，作普通材料；供 {@link IManaFuelStick#disintegration} 引用。 */
    public static ItemEntry<ComponentItem> SPARK_STICK_DISINTEGRATED;
    public static ItemEntry<ComponentItem> ADVANCED_SPARK_STICK_DISINTEGRATED;
    public static ItemEntry<ComponentItem> TERRA_STICK_DISINTEGRATED;
    public static ItemEntry<ComponentItem> MIXED_WILL_STICK_DISINTEGRATED;
    public static ItemEntry<ComponentItem> URANIUM_NUCLEON_FLUCTUATION_INFUSION_CELL_CLEAVED;

    public static ItemEntry<IManaFuelStick> SPARK_STICK;
    public static ItemEntry<IManaFuelStick> ADVANCED_SPARK_STICK;
    public static ItemEntry<IManaFuelStick> TERRA_STICK;
    public static ItemEntry<IManaFuelStick> MIXED_WILL_STICK;
    public static ItemEntry<IManaFuelStick> URANIUM_NUCLEON_FLUCTUATION_INFUSION_CELL;

    public static ItemEntry<ComponentItem> SPARK_SUBSTRATE;
    public static ItemEntry<ComponentItem> TERRA_SUBSTRATE;
    public static ItemEntry<ComponentItem> PHASE_RETRIEVAL_SUBSTRATE;

    private CMFuelItems() {}

    /**
     * 物品模型使用 {@code assets/&lt;modid&gt;/textures/item/manaspark/&lt;注册名&gt;.png}（与 {@code item/generated} 父模型）。
     */
    public static <
            T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> manasparkModel() {
        return (ctx, prov) -> prov.generated(ctx, prov.modLoc("item/manaspark/" + ctx.getName()));
    }

    /**
     * 基质贴图目录：{@code assets/&lt;modid&gt;/textures/item/substrate/&lt;注册名&gt;.png}。
     */
    public static <
            T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> substrateModel() {
        return (ctx, prov) -> prov.generated(ctx, prov.modLoc("item/substrate/" + ctx.getName()));
    }

    public static void registerItem() {
        SPARK_STICK_DISINTEGRATED = REGISTRATE
                .item("spark_stick_disintegrated", ComponentItem::create)
                .cnlang("火花级注术单元（崩解态）")
                .model(manasparkModel())

                .lang("Spark-Tier Infusion Cell (Disintegrated)")
                .tag(CMTags.ELEMENT_FIRE)
                .register();
        ADVANCED_SPARK_STICK_DISINTEGRATED = REGISTRATE
                .item("advanced_spark_stick_disintegrated", ComponentItem::create)
                .cnlang("进阶注术单元（崩解态）")
                .model(manasparkModel())

                .lang("Advanced Infusion Cell (Disintegrated)")
                .tag(CMTags.ELEMENT_FIRE)
                .register();
        TERRA_STICK_DISINTEGRATED = REGISTRATE
                .item("terra_stick_disintegrated", ComponentItem::create)
                .cnlang("大地注术单元（崩解态）")
                .model(manasparkModel())

                .lang("Terra Infusion Cell (Disintegrated)")
                .tag(CMTags.ELEMENT_EARTH)
                .register();
        MIXED_WILL_STICK_DISINTEGRATED = REGISTRATE
                .item("mixed_will_stick_disintegrated", ComponentItem::create)
                .cnlang("聚合意志注术单元（崩解态）")
                .model(manasparkModel())

                .lang("Aggregated Will Infusion Cell (Disintegrated)")
                .tag(CMTags.ELEMENT_SIN)
                .register();

        SPARK_STICK = REGISTRATE
                .item("spark_stick",
                        p -> new IManaFuelStick(p, 5, 1, 250, SPARK_STICK_DISINTEGRATED.get()))
                .cnlang("火花级注术单元")
                .model(manasparkModel())

                .lang("Spark-Tier Infusion Cell")
                .tag(CMTags.MANA_FUEL_STACK, CMTags.ELEMENT_FIRE)
                .register();
        ADVANCED_SPARK_STICK = REGISTRATE
                .item("advanced_spark_stick",
                        p -> new IManaFuelStick(p, 8, 2, 500, ADVANCED_SPARK_STICK_DISINTEGRATED.get()))
                .cnlang("进阶注术单元")
                .model(manasparkModel())

                .lang("Advanced Infusion Cell")
                .tag(CMTags.MANA_FUEL_STACK, CMTags.ELEMENT_FIRE)
                .register();
        TERRA_STICK = REGISTRATE
                .item("terra_stick",
                        p -> new IManaFuelStick(p, 8, 1, 1000, TERRA_STICK_DISINTEGRATED.get()))
                .cnlang("大地注术单元")
                .model(manasparkModel())

                .lang("Terra Infusion Cell")
                .tag(CMTags.MANA_FUEL_STACK, CMTags.ELEMENT_EARTH)
                .register();
        MIXED_WILL_STICK = REGISTRATE
                .item("mixed_will_stick",
                        p -> new IManaFuelStick(p, 12, 5, 4444, MIXED_WILL_STICK_DISINTEGRATED.get()))
                .cnlang("聚合意志注术单元")
                .model(manasparkModel())

                .lang("Aggregated Will Infusion Cell")
                .tag(CMTags.MANA_FUEL_STACK, CMTags.ELEMENT_SIN)
                .register();

        URANIUM_NUCLEON_FLUCTUATION_INFUSION_CELL_CLEAVED = REGISTRATE
                .item("uranium_nucleon_fluctuation_infusion_cell_cleaved", ComponentItem::create)
                .cnlang("铀-核子涨落注术单元（裂解态）")
                .model(manasparkModel())
                .lang("Uranium Nucleon-Fluctuation Infusion Cell (Cleaved)")
                .register();
        URANIUM_NUCLEON_FLUCTUATION_INFUSION_CELL = REGISTRATE
                .item("uranium_nucleon_fluctuation_infusion_cell",
                        p -> new IManaFuelStick(p, 12, 8, 720,
                                URANIUM_NUCLEON_FLUCTUATION_INFUSION_CELL_CLEAVED.get()))
                .cnlang("铀-核子涨落注术单元")
                .model(manasparkModel())
                .lang("Uranium Nucleon-Fluctuation Infusion Cell")
                .tag(CMTags.MANA_FUEL_STACK)
                .register();

        SPARK_SUBSTRATE = REGISTRATE
                .item("spark_substrate", ComponentItem::create)
                .cnlang("火花基质")
                .model(substrateModel())
                .lang("Spark Substrate")
                .tag(CMTags.ELEMENT_FIRE)
                .register();
        TERRA_SUBSTRATE = REGISTRATE
                .item("terra_substrate", ComponentItem::create)
                .cnlang("大地催化基质")
                .model(substrateModel())
                .lang("Terra Substrate")
                .tag(CMTags.ELEMENT_EARTH)
                .register();
        PHASE_RETRIEVAL_SUBSTRATE = REGISTRATE
                .item("phase_retrieval_substrate", ComponentItem::create)
                .cnlang("相位回溯基质")
                .model(substrateModel())
                .lang("Phase Retrieval Substrate")
                .register();
    }
}
