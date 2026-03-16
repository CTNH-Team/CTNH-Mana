package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.ICustomDescriptionId;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import com.moguang.ctnhmana.item.BloodMagicJade.JadeItem;
import com.moguang.ctnhmana.item.Caduceus.CaduceusItem;
import com.moguang.ctnhmana.item.ManaFuelStick.IManaFuelStick;
import com.moguang.ctnhmana.item.ManaMachineUpgrade.*;
import com.moguang.ctnhmana.item.ManaMachineUpgrade.BMUpgradeItemT1;
import com.moguang.ctnhmana.item.ManaMachineUpgrade.BTUpgradeItemT1;
import com.moguang.ctnhmana.item.Rune.IRuneItem;
import com.moguang.ctnhmana.item.bossSummoner.BossSummonerBehavior;
import com.moguang.ctnhmana.item.bossSummoner.ThrowItem;
import com.moguang.ctnhmana.item.equipment.KoishiEyeItem;
import com.moguang.ctnhmana.item.equipment.SaberWandItem;
import com.moguang.ctnhmana.item.equipment.TaintedBloodWeepingEye;
import com.moguang.ctnhmana.item.equipment.YurikoRingItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.*;
import static com.moguang.ctnhmana.item.Rune.RuneElementType.*;
import static com.moguang.ctnhmana.item.Rune.RuneElementType.WATER;

@SuppressWarnings("removal")
public class CMItems {

    static {
        REGISTRATE.creativeModeTab(() -> CMCreativeModeTabs.ITEM);
    }

    public static void registerItem() {
        BOSS_SUMMONER = REGISTRATE
                .item("boss_summoner", ThrowItem::new)
                .cnlang("boss召唤器")
                .lang("Boss Summoner")
                .onRegister(attach(new BossSummonerBehavior(1)))
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.boss_summoner.use").withStyle(ChatFormatting.RED));
                })))
                .register();
        ADVANCED_BOSS_SUMMONER = REGISTRATE
                .item("advanced_boss_summoner", ThrowItem::new)
                .cnlang("进阶boss召唤器")
                .lang("Advanced Boss Summoner")
                .onRegister(attach(new BossSummonerBehavior(2)))
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.boss_summoner.use").withStyle(ChatFormatting.DARK_RED));
                })))
                .register();
        HORIZEN_RUNE = REGISTRATE
                .item("horizen_rune", holder -> new IRuneItem(holder, List.of(EARTH, WIND, FIRE), 5))
                .cnlang("§5视域§r符文")
                .lang("§5Horizen§r Rune")
                .tag(BotaniaTags.Items.RUNES, CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.horizen_rune")
                            .withStyle(ChatFormatting.DARK_PURPLE));
                })))
                .register();
        STARLIGHT_RUNE = REGISTRATE
                .item("starlight_rune", holder -> new IRuneItem(holder, List.of(EARTH, WATER, FIRE), 5))
                .cnlang("§9星光§r符文")
                .lang("§9Starlight§r Rune")
                .tag(BotaniaTags.Items.RUNES, CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.starlight_rune").withStyle(ChatFormatting.BLUE));
                })))
                .register();
        TWIST_RUNE = REGISTRATE
                .item("twist_rune", holder -> new IRuneItem(holder, List.of(EARTH, SIN, FIRE), 5))
                .cnlang("§c扭曲§r符文")
                .lang("§cTwist§r Rune")
                .tag(BotaniaTags.Items.RUNES, CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.twist_rune").withStyle(ChatFormatting.RED));
                })))
                .register();
        QUASAR_RUNE = REGISTRATE
                .item("quasar_rune", holder -> new IRuneItem(holder, List.of(EARTH, WATER, WIND, SIN, FIRE), 5))
                .cnlang("§k类星体§r符文")
                .lang("§kQuasar§r Rune")
                .tag(BotaniaTags.Items.RUNES, CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.quasar_rune")
                            .withStyle(ChatFormatting.LIGHT_PURPLE));
                })))
                .register();
        PROLIFERATION_RUNE = REGISTRATE
                .item("proliferation_rune", holder -> new IRuneItem(holder, List.of(EARTH, WATER, WIND), 5))
                .cnlang("§a增殖§r符文")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    properties.stacksTo(1);
                    return properties;
                })
                .lang("§aProliferation§r Rune")
                .tag(BotaniaTags.Items.RUNES, CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.proliferation_rune")
                            .withStyle(ChatFormatting.GREEN));
                })))
                .register();

        KOISHI_EYE = REGISTRATE
                .item("koishi_eye", KoishiEyeItem::new)
                .cnlang("§9紧闭的第三只眼")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    properties.stacksTo(1);
                    return properties;
                })
                .lang("§9Koishi_eye")
                .tag(accessory("body"))
                .register();
        YURIKO_RING = REGISTRATE
                .item("yuriko_ring", YurikoRingItem::new)
                .cnlang("§7黯淡的绮璃之戒")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    properties.stacksTo(1);
                    return properties;
                })
                .lang("§§7黯淡的绮璃之戒")
                .tag(accessory("ring"))
                .register();
        BLOODY_NANO_PROCESSOR_MAINFRAME = REGISTRATE
                .item("bloody_nano_processor_mainframe", ComponentItem::create)
                .cnlang("§4血染微处理主机§r")
                .tag(CustomTags.LuV_CIRCUITS)
                .onRegister(attach(new TooltipBehavior(text -> itemTooltipsChange(bloody_nano_circuit_lang, text))))
                .register();
        MIXIN_WILL_PROCESSOR_MAINFRAME = REGISTRATE
                .item("mixin_will_processor_mainframe", ComponentItem::create)
                .cnlang("§1注元意志处理器主机§r")
                .tag(CustomTags.ZPM_CIRCUITS)
                .onRegister(attach(new TooltipBehavior(text -> itemTooltipsChange(mixin_will_circuit_lang, text))))
                .register();
        RUNE_CIRCUIT_BOARD = REGISTRATE
                .item("rune_circuit_board", ComponentItem::create)
                .cnlang("符石电路基板")
                .onRegister(attach(new TooltipBehavior(text -> text.add(rune_circuit_board_lang.translate()))))
                .register();
        UMLHPIC_WAFER = REGISTRATE
                .item("umlhpic_wafer", ComponentItem::create)
                .cnlang("UMLHPIC晶圆")
                .lang("Umlhpic Wafer")
                .onRegister(attach(new TooltipBehavior(text -> text.add(umlhpic.translate()))))
                .register();

        UMLHPIC_CHIP = REGISTRATE
                .item("umlhpic_chip", ComponentItem::create)
                .cnlang("UMLHPIC芯片")
                .lang("Umlhpic Chip")
                .onRegister(attach(new TooltipBehavior(text -> text.add(umlhpic.translate()))))
                .register();

        MAGIC_QUANTUM_PROCESSOR_MAINFRAME = REGISTRATE
                .item("magic_quantum_processor_mainframe", ComponentItem::create)
                .cnlang("超因果逻辑UHV电路")
                .lang("Supercausal Logic UHV Circuit")
                .tag(CustomTags.UHV_CIRCUITS)
                .onRegister(
                        attach(new TooltipBehavior(text -> itemTooltipsAdd(magic_quantum_processor_mainframe, text))))
                .register();
        ETCHING_JADE = REGISTRATE
                .item("etching_jade", p -> new JadeItem(p, "etching", etching_jade_upgrade))
                .cnlang("蚀刻之玉")
                .lang("Etching Jade")
                .onRegister(attach(new TooltipBehavior(list -> itemTooltipsAdd(etchingJadeLang, list))))
                .register();
        SUPPRESSION_JADE = REGISTRATE
                .item("suppression_jade", p -> new JadeItem(p, "suppression", suppression_jade_upgrade))
                .cnlang("抑液之玉")
                .lang("Suppression Jade")
                .onRegister(attach(new TooltipBehavior(list -> itemTooltipsAdd(suppressionJadeLang, list))))
                .register();
        EPHEMERAL_JADE = REGISTRATE
                .item("ephemeral_jade", p -> new JadeItem(p, "ephemeral", ephemeral_jade_upgrade))
                .cnlang("须臾之玉")
                .lang("Ephemeral Jade")
                .onRegister(attach(new TooltipBehavior(list -> itemTooltipsAdd(ephemeralJadeLang, list))))
                .register();
        WILL_CRYSTAL_PROCESSOR = REGISTRATE
                .item("will_crystal_processor", ComponentItem::create)
                .cnlang("晶化意志处理器")
                .tag(CustomTags.IV_CIRCUITS)
                .onRegister(attach(new TooltipBehavior(text -> itemTooltipsChange(will_crystal_circuit_lang, text))))
                .register();
        SABER_WAND = REGISTRATE
                .item("saber_wand", SaberWandItem::new)
                .cnlang("§9电子精灵法杖")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    properties.stacksTo(1);
                    return properties;
                })
                .register();
        CADUCEUS = REGISTRATE
                .item("caduceus", CaduceusItem::new)
                .cnlang("神谕终端[赫尔墨斯]")
                .model((ctx, prov) -> {
                    String[] sub = { "sword", "pickaxe", "shovel", "axe", "hoe", "scythe", "saw",
                            "wrench", "file", "screwdriver", "wire_cutter", "knife", "plunger" };
                    var baseModel = prov.withExistingParent(ctx.getName(), prov.mcLoc("item/handheld"))
                            .texture("layer0", prov.modLoc("item/caduceus/caduceus_sword"));

                    for (String s : sub) {
                        String name = "caduceus_" + s;
                        prov.getBuilder(name)
                                .parent(new ModelFile.UncheckedModelFile(prov.mcLoc("item/handheld")))
                                .texture("layer0", prov.modLoc("item/caduceus/caduceus_" + s));
                    }

                    for (int i = sub.length - 1; i >= 0; i--) {
                        float v = i / (float) (sub.length - 1); // 0.0 ~ 1.0
                        baseModel.override()
                                .predicate(prov.modLoc("tool_type"), v)
                                .model(new ModelFile.UncheckedModelFile(
                                        prov.modLoc("item/caduceus_" + sub[i])))
                                .end();
                    }

                })
                .lang("Caduceus")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    properties.stacksTo(1);
                    return properties;
                })
                // d等EMI问题解决了再说这个
                // .tag(
                // ItemTags.SWORDS,
                // ItemTags.PICKAXES,
                // ItemTags.SHOVELS,
                // ItemTags.AXES,
                // ItemTags.HOES,
                // CustomTags.SCYTHES,
                // CustomTags.SAWS,
                // CustomTags.WRENCHES,
                // CustomTags.WRENCH,
                // CustomTags.FILES,
                // CustomTags.SCREWDRIVERS,
                // CustomTags.WIRE_CUTTERS,
                // CustomTags.KNIVES,
                // CustomTags.PLUNGERS,
                // CustomTags.CRAFTING_CROWBARS,
                // CustomTags.CRAFTING_FILES,
                // CustomTags.CRAFTING_HAMMERS,
                // CustomTags.CRAFTING_KNIVES,
                // CustomTags.CRAFTING_MALLETS,
                // CustomTags.CRAFTING_MORTARS,
                // CustomTags.CRAFTING_SAWS,
                // CustomTags.CRAFTING_SCREWDRIVERS,
                // CustomTags.CRAFTING_WIRE_CUTTERS,
                // CustomTags.CRAFTING_WRENCHES
                // )
                .register();
        SPARK_STICK = REGISTRATE
                .item("spark_stick", properties -> new IManaFuelStick(properties, 5, 1, 1000))
                .cnlang("火花级魔力燃料棒")
                .tag(CMTags.MANA_FUEL_STACK)
                .register();
        BROKEN_RUNE = REGISTRATE
                .item("broken_rune", ComponentItem::create)
                .cnlang("破碎的符文")
                .onRegister(attach(new TooltipBehavior(text -> text.add(brokenRuneLang.translate()))))
                .register();
        EMPTY_RUNE = REGISTRATE
                .item("blank_rune", ComponentItem::create)
                .cnlang("空无符文")
                .onRegister(attach(new TooltipBehavior(text -> text.add(blankRuneLang.translate()))))
                .lang("Blank Rune")
                .register();
        UNIMBUED_SPIRIT = REGISTRATE
                .item("unimbued_spirit", ComponentItem::create)
                .cnlang("未注魔的精魄")
                .onRegister(attach(new TooltipBehavior(text -> text.add(unimbudSpritLang.translate()))))
                .lang("Unimbued Spirit")
                .register();
        ORICHALCOS_SPIRIT = REGISTRATE
                .item("orichalcos_spirit", ComponentItem::create)
                .cnlang("§d奥利哈精魄")
                .onRegister(attach(new TooltipBehavior(text -> text.add(orichalcosSpritLang.translate()))))
                .lang("Orichalcum Spirit")
                .register();
        TAINTED_BLOOD_EYE = REGISTRATE
                .item("tainted_blood_third_eye", TaintedBloodWeepingEye::new)
                .cnlang("污血泣眼")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    properties.stacksTo(1);
                    return properties;
                })
                .tag(accessory("body"))
                .register();
        TERRA_CATALYST = REGISTRATE
                .item("terra_catalyst", ComponentItem::create)
                .cnlang("§a泰拉催化剂")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    return properties;
                })
                .onRegister(attach(new TooltipBehavior(text -> text.add(terra_catalyst.translate()))))
                .register();
        ZENITH_STAR = REGISTRATE
                .item("zenith_star", ComponentItem::create)
                .cnlang("§5天穹之星")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    return properties;
                })
                .onRegister(attach(new TooltipBehavior(text -> text.add(zenith_star.translate()))))
                .register();
        ENCAPSULATED_TWIST_MANA = REGISTRATE
                .item("encapsulated_twist_mana", ComponentItem::create)
                .cnlang("§5封装扭曲魔力")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    return properties;
                })
                .onRegister(attach(new TooltipBehavior(text -> text.add(encapsulated_twist_mana.translate()))))
                .register();
        MANA_CIRCUIT_BOARD = REGISTRATE
                .item("mana_circuit_board", ComponentItem::create)
                .cnlang("§5究极魔力电路基板")
                .properties(properties -> {
                    properties.rarity(Rarity.EPIC);
                    return properties;
                })
                .onRegister(attach(new TooltipBehavior(text -> text.add(mana_circuit_board.translate()))))
                .register();
        SKY_FLOWER_SPEECH = REGISTRATE
                .item("sky_flower_speech", BTUpgradeItemT1::new)
                .cnlang("天空之花语")
                .register();
        ZENITH_FLOWER_THOUGHT = REGISTRATE
                .item("zenith_flower_thought", ComponentItem::create)
                .cnlang("天顶之花思")
                .register();
        ZENITH_VISION = REGISTRATE
                .item("zenith_vision", ComponentItem::create)
                .cnlang("天顶之视线")
                .register();
        TWISTED_BLOOD_FORGING = REGISTRATE
                .item("twisted_blood_forging", BMUpgradeItemT1::new)
                .cnlang("曲血之锻造")
                .register();
        TWISTED_SOUL_FORGING = REGISTRATE
                .item("twisted_soul_forging", BMUpgradeItemT2::new)
                .cnlang("曲魂之锻造")
                .register();
        PIPELINE_VISION = REGISTRATE
                .item("pipeline_vision", GTUpgradeItemT1::new)
                .cnlang("流水线的视野")
                .register();
        PIPELINE_FARSIGHT = REGISTRATE
                .item("pipeline_farsight", GTUpgradeItemT2::new)
                .cnlang("流水线的远视")
                .register();
        CLEAR_SKY_FLOWER_WISH = REGISTRATE
                .item("clear_sky_flower_wish", BTUpgradeItemT2::new)
                .cnlang("澄空之花愿")
                .register();
        // WARPED_FORGING = REGISTRATE
        // .item("warped_forging", BMUpgradeItemT1::new)
        // .cnlang("翘曲之锻造")
        // .register();
        AZURE_SKY_FLOWER_DANCE = REGISTRATE
                .item("azure_sky_flower_dance", BTUpgradeItemT3::new)
                .cnlang("苍穹之花舞")
                .register();
        MAGIC_CORE = REGISTRATE
                .item("magic_core", ComponentItem::create)
                .cnlang("魔式转化核心")
                .lang("Magic Core")
                .onRegister(attach(new TooltipBehavior(text -> text.add(magicCoreLang.translate()))))
                .register();
        HEART_OF_FLOWER = REGISTRATE
                .item("heart_of_flower", ComponentItem::create)
                .cnlang("繁花之心")
                .lang("Heart of Flower")
                .onRegister(attach(new TooltipBehavior(text -> text.add(flowerHeartLang.translate()))))
                .register();
    }

    public static void init() {
        registerItem();
        // registercircuit.init();
        // registerJade.init();
    }

    public static ItemEntry<ThrowItem> BOSS_SUMMONER;
    public static ItemEntry<ThrowItem> ADVANCED_BOSS_SUMMONER;
    public static ItemEntry<ComponentItem> MANA_CIRCUIT_BOARD;
    public static ItemEntry<ComponentItem> ZENITH_STAR;
    public static ItemEntry<IRuneItem> HORIZEN_RUNE;
    public static ItemEntry<IRuneItem> STARLIGHT_RUNE;
    public static ItemEntry<IRuneItem> TWIST_RUNE;
    public static ItemEntry<IRuneItem> QUASAR_RUNE;
    public static ItemEntry<IRuneItem> PROLIFERATION_RUNE;
    public static ItemEntry<KoishiEyeItem> KOISHI_EYE;
    public static ItemEntry<YurikoRingItem> YURIKO_RING;
    public static ItemEntry<ComponentItem> BLOODY_NANO_PROCESSOR_MAINFRAME;
    public static ItemEntry<ComponentItem> MIXIN_WILL_PROCESSOR_MAINFRAME;
    public static ItemEntry<ComponentItem> WILL_CRYSTAL_PROCESSOR;
    public static ItemEntry<ComponentItem> RUNE_CIRCUIT_BOARD;
    public static ItemEntry<ComponentItem> UMLHPIC_WAFER;
    public static ItemEntry<ComponentItem> UMLHPIC_CHIP;
    public static ItemEntry<ComponentItem> MAGIC_QUANTUM_PROCESSOR_MAINFRAME;
    public static ItemEntry<JadeItem> ETCHING_JADE;
    public static ItemEntry<JadeItem> SUPPRESSION_JADE;
    public static ItemEntry<JadeItem> EPHEMERAL_JADE;
    public static ItemEntry<SaberWandItem> SABER_WAND;
    public static ItemEntry<CaduceusItem> CADUCEUS;
    public static ItemEntry<IManaFuelStick> SPARK_STICK;
    public static ItemEntry<ComponentItem> BROKEN_RUNE;
    public static ItemEntry<ComponentItem> EMPTY_RUNE;
    public static ItemEntry<TaintedBloodWeepingEye> TAINTED_BLOOD_EYE;
    public static ItemEntry<ComponentItem> TERRA_CATALYST;
    public static ItemEntry<ComponentItem> ENCAPSULATED_TWIST_MANA;
    public static ItemEntry<ComponentItem> UNIMBUED_SPIRIT;
    public static ItemEntry<ComponentItem> ORICHALCOS_SPIRIT;

    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent components) {
        return item -> item.attachComponents(components);
    }

    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent... components) {
        return item -> item.attachComponents(components);
    }

    public static ICustomDescriptionId cellName() {
        return new ICustomDescriptionId() {

            @Override
            public Component getItemName(ItemStack stack) {
                Component prefix = FluidUtil.getFluidContained(stack).map(FluidStack::getDisplayName)
                        .orElse(Component.translatable("gtceu.fluid.empty"));
                return Component.translatable(stack.getDescriptionId(), prefix);
            }
        };
    }

    // 为不具有format的列表lang提供列表
    public static List<Component> itemTooltipsAdd(Lang[] langs, List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
        return list;
    }

    public static void itemTooltipsChange(Lang[] langs, List<Component> list) {
        for (Lang lang : langs) {
            list.add(lang.translate());
        }
    }

    private static TagKey<Item> accessory(String name) {
        return ItemTags.create(new ResourceLocation("curios", name));
    }

    public static ItemEntry<ComponentItem> BLOOD_DIODE = REGISTRATE
            .item("blood_diode", ComponentItem::create)
            .cnlang("血染逻辑二极管")
            .onRegister(attach(new TooltipBehavior(text -> text.add(blooddiodelang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> WILL_DIODE = REGISTRATE
            .item("will_diode", ComponentItem::create)
            .cnlang("意志注入二极管")
            .onRegister(attach(new TooltipBehavior(text -> text.add(willdiodelang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> BLOOD_RESISTOR = REGISTRATE
            .item("blood_resistor", ComponentItem::create)
            .cnlang("血染逻辑电阻")
            .onRegister(attach(new TooltipBehavior(text -> text.add(bloodresistorlang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> WILL_RESISTOR = REGISTRATE
            .item("will_resistor", ComponentItem::create)
            .cnlang("意志阻遏电阻")
            .onRegister(attach(new TooltipBehavior(text -> text.add(willresistorlang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> BLOOD_CAPACITOR = REGISTRATE
            .item("blood_capacitor", ComponentItem::create)
            .cnlang("血染逻辑电容")
            .onRegister(attach(new TooltipBehavior(text -> text.add(bloodcapacitorlang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> WILL_CAPACITOR = REGISTRATE
            .item("will_capacitor", ComponentItem::create)
            .cnlang("疑虑增生电容")
            .onRegister(attach(new TooltipBehavior(text -> text.add(willcapacitorlang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> WILL_TRANSISTOR = REGISTRATE
            .item("will_transistor", ComponentItem::create)
            .cnlang("晶化意志管")
            .onRegister(attach(new TooltipBehavior(text -> text.add(willtransistorlang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> BLOOD_TRANSISTOR = REGISTRATE
            .item("blood_transistor", ComponentItem::create)
            .cnlang("鲜血结晶管")
            .onRegister(attach(new TooltipBehavior(text -> text.add(bloodtransistorlang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> BLOOD_INDUCTOR = REGISTRATE
            .item("blood_inductor", ComponentItem::create)
            .cnlang("血级电感")
            .onRegister(attach(new TooltipBehavior(text -> text.add(bloodinductorlang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> WILL_INDUCTOR = REGISTRATE
            .item("will_inductor", ComponentItem::create)
            .cnlang("意志悖论电感")
            .onRegister(attach(new TooltipBehavior(text -> text.add(willinductorlang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> MANA_ELECTRONIC_CIRCUIT = REGISTRATE
            .item("mana_electronic_circuit", ComponentItem::create)
            .cnlang("§b注魔的电子电路")
            .tag(CustomTags.HV_CIRCUITS)
            .onRegister(attach(new TooltipBehavior(text -> itemTooltipsChange(mana_circuit_lang, text))))
            .lang("Mana Electronic Circuit")
            .register();

    public static ItemEntry<ComponentItem> MANA_INTEGRATED_CIRCUIT = REGISTRATE
            .item("mana_integrated_circuit", ComponentItem::create)
            .cnlang("§a注魔的集成电路")
            .tag(CustomTags.EV_CIRCUITS)
            .onRegister(attach(new TooltipBehavior(text ->

            itemTooltipsChange(advanced_mana_circuit_lang, text))))
            .lang("Mana Integrated Circuit")
            .register();
    public static ItemEntry<ComponentItem> MANA_SOC = REGISTRATE
            .item("mana_soc", ComponentItem::create)
            .cnlang("§b注魔的SOC")
            .lang("§bMana Soc")
            .register();

    public static ItemEntry<ComponentItem> ZENITH_SOC = REGISTRATE
            .item("zenith_soc", ComponentItem::create)
            .cnlang("§5天顶SOC")
            .lang("§5Zenith Soc")
            .register();

    public static ItemEntry<ComponentItem> MANA_RESISTOR = REGISTRATE
            .item("mana_resistor", ComponentItem::create)
            .cnlang("魔力电阻")
            .lang("Mana Resistor")
            .register();

    public static ItemEntry<ComponentItem> MANA_CAPACITOR = REGISTRATE
            .item("mana_capacitor", ComponentItem::create)
            .cnlang("魔力电容")
            .lang("Mana Capacitor")
            .register();

    public static ItemEntry<ComponentItem> MANA_TRANSISTOR = REGISTRATE
            .item("mana_transistor", ComponentItem::create)
            .cnlang("魔力晶体管")
            .lang("Mana Transistor")
            .register();

    public static ItemEntry<ComponentItem> MANA_DIODE = REGISTRATE
            .item("mana_diode", ComponentItem::create)
            .cnlang("魔力二极管")
            .lang("Mana Diode")
            .register();

    public static ItemEntry<ComponentItem> MANA_INDUCTOR = REGISTRATE
            .item("mana_inductor", ComponentItem::create)
            .cnlang("魔力电感")
            .lang("Mana Inductor")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_RESISTOR = REGISTRATE
            .item("advanced_mana_resistor", ComponentItem::create)
            .cnlang("精灵电阻")
            .lang("Advanced Mana Resistor")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_CAPACITOR = REGISTRATE
            .item("advanced_mana_capacitor", ComponentItem::create)
            .cnlang("精灵电容")
            .lang("Advanced Mana Capacitor")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_TRANSISTOR = REGISTRATE
            .item("advanced_mana_transistor", ComponentItem::create)
            .cnlang("精灵晶体管")
            .lang("Advanced Mana Transistor")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_DIODE = REGISTRATE
            .item("advanced_mana_diode", ComponentItem::create)
            .cnlang("精灵二极管")
            .lang("Advanced Mana Diode")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_INDUCTOR = REGISTRATE
            .item("advanced_mana_inductor", ComponentItem::create)
            .cnlang("精灵电感")
            .lang("Advanced Mana Inductor")
            .register();

    public static ItemEntry<ComponentItem> MANA_WAFER = REGISTRATE
            .item("mana_wafer", ComponentItem::create)
            .cnlang("§b注魔的SOC晶圆")
            .lang("§bMana Wafer")
            .register();
    public static ItemEntry<ComponentItem> WILL_WAFER = REGISTRATE
            .item("will_wafer", ComponentItem::create)
            .cnlang("§b意志运算晶圆")
            .onRegister(attach(new TooltipBehavior(text -> text.add(willsoclang.translate()))))
            .lang("§bWill Wafer")
            .register();
    public static ItemEntry<ComponentItem> WILL_SOC = REGISTRATE
            .item("will_soc", ComponentItem::create)
            .cnlang("§b意志运算SOC")
            .onRegister(attach(new TooltipBehavior(text -> text.add(willsoclang.translate()))))
            .lang("§bWill SOC")
            .register();
    public static ItemEntry<ComponentItem> BLOODY_WAFER = REGISTRATE
            .item("bloody_wafer", ComponentItem::create)
            .cnlang("§4血染逻辑晶圆")
            .register();
    public static ItemEntry<ComponentItem> BLOODY_CHIP = REGISTRATE
            .item("bloody_chip", ComponentItem::create)
            .cnlang("§4血染逻辑芯片")
            .register();

    public static ItemEntry<ComponentItem> ZENITH_WAFER = REGISTRATE
            .item("zenith_wafer", ComponentItem::create)
            .cnlang("§5天顶SOC晶圆")
            .lang("§5§bMana Wafer")
            .register();
    public static ItemEntry<ComponentItem> ELF_CATALYST = REGISTRATE
            .item("elf_catalyst", ComponentItem::create)
            .cnlang("精灵催化剂")
            .lang("Elf Catalyst")
            .register();

    public static ItemEntry<ComponentItem> ENDSLATE = REGISTRATE
            .item("endslate", ComponentItem::create)
            .cnlang("终焉石板")
            .lang("Endslate")
            .register();

    public static ItemEntry<Item> CORROSIVE_CORE = REGISTRATE
            .item("corrosive_core", Item::new)
            .cnlang("腐蚀核心")
            .lang("Corrosive Core")
            .register();
    public static ItemEntry<Item> VENGEFUL_CORE = REGISTRATE
            .item("vengeful_core", Item::new)
            .cnlang("复仇核心")
            .lang("Vengeful Core")
            .register();
    public static ItemEntry<Item> DESTRUCTIVE_CORE = REGISTRATE
            .item("destructive_core", Item::new)
            .cnlang("破坏核心")
            .lang("Destructive Core")
            .register();
    public static ItemEntry<Item> STEADFAST_CORE = REGISTRATE
            .item("steadfast_core", Item::new)
            .cnlang("坚毅核心")
            .lang("Steadfast Core")
            .register();

    public static ItemEntry<BTUpgradeItemT1> SKY_FLOWER_SPEECH;
    public static ItemEntry<ComponentItem> ZENITH_FLOWER_THOUGHT;
    public static ItemEntry<ComponentItem> ZENITH_VISION;
    public static ItemEntry<BMUpgradeItemT1> TWISTED_BLOOD_FORGING;
    public static ItemEntry<BMUpgradeItemT2> TWISTED_SOUL_FORGING;
    public static ItemEntry<GTUpgradeItemT1> PIPELINE_VISION;
    public static ItemEntry<GTUpgradeItemT2> PIPELINE_FARSIGHT;
    public static ItemEntry<BTUpgradeItemT2> CLEAR_SKY_FLOWER_WISH;
    public static ItemEntry<ComponentItem> WARPED_FORGING;
    public static ItemEntry<BTUpgradeItemT3> AZURE_SKY_FLOWER_DANCE;
    public static ItemEntry<ComponentItem> MAGIC_CORE;
    public static ItemEntry<ComponentItem> HEART_OF_FLOWER;
}
