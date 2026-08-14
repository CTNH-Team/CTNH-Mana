package com.magicbee.ctnhmana.registry;

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

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.magicbee.ctnhmana.common.item.ZenithDebugToolItem;
import com.magicbee.ctnhmana.common.item.bloodmagicjade.JadeItem;
import com.magicbee.ctnhmana.common.item.bosssummoner.BossSummonerBehavior;
import com.magicbee.ctnhmana.common.item.bosssummoner.ThrowItem;
import com.magicbee.ctnhmana.common.item.caduceus.CaduceusItem;
import com.magicbee.ctnhmana.common.item.dungeon.PerfectMineKeyItem;
import com.magicbee.ctnhmana.common.item.equipment.KoishiEyeItem;
import com.magicbee.ctnhmana.common.item.equipment.SaberWandItem;
import com.magicbee.ctnhmana.common.item.equipment.TaintedBloodWeepingEye;
import com.magicbee.ctnhmana.common.item.equipment.YurikoRingItem;
import com.magicbee.ctnhmana.common.item.manamachineupgrade.*;
import com.magicbee.ctnhmana.common.item.rune.IRuneItem;
import com.magicbee.ctnhmana.common.item.rune.SpireUpgradeRuneItem;
import com.magicbee.ctnhmana.registry.items.CMFuelItems;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

import static com.magicbee.ctnhmana.CTNHMana.REGISTRATE;
import static com.magicbee.ctnhmana.common.item.rune.RuneElementType.*;

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
                    return properties;
                })
                .lang("§aProliferation§r Rune")
                .tag(BotaniaTags.Items.RUNES, CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.proliferation_rune")
                            .withStyle(ChatFormatting.GREEN));
                })))
                .register();

        UPGRADE_RUNE_SPEED_1 = REGISTRATE
                .item("upgrade_rune_speed_1", props -> new SpireUpgradeRuneItem(props, 2.0, 1.0, 1.0, 0.1))
                .cnlang("§9尖塔速度符文I§r")
                .lang("Spire Speed Rune (x2)")
                .register();
        UPGRADE_RUNE_SPEED_2 = REGISTRATE
                .item("upgrade_rune_speed_2", props -> new SpireUpgradeRuneItem(props, 4.0, 1.0, 1.0, 0.1))
                .cnlang("§9尖塔速度符文II§r")
                .lang("Spire Speed Rune (x4)")
                .register();
        UPGRADE_RUNE_SPEED_3 = REGISTRATE
                .item("upgrade_rune_speed_3", props -> new SpireUpgradeRuneItem(props, 16.0, 1.0, 1.0, 0.1))
                .cnlang("§9尖塔速度符文III§r")
                .lang("Spire Speed Rune (x16)")
                .register();

        UPGRADE_RUNE_RANGE_1 = REGISTRATE
                .item("upgrade_rune_range_1", props -> new SpireUpgradeRuneItem(props, 1.0, 6.0, 1.0, 0.1))
                .cnlang("§9尖塔范围符文I§r")
                .lang("Spire Range Rune I (+5 Range)")
                .register();
        UPGRADE_RUNE_RANGE_2 = REGISTRATE
                .item("upgrade_rune_range_2", props -> new SpireUpgradeRuneItem(props, 1.0, 11.0, 1.0, 0.1))
                .cnlang("§9尖塔范围符文II§r")
                .lang("Spire Range Rune II (+10 Range)")
                .register();
        UPGRADE_RUNE_RANGE_3 = REGISTRATE
                .item("upgrade_rune_range_3", props -> new SpireUpgradeRuneItem(props, 1.0, 16.0, 1.0, 0.1))
                .cnlang("§9尖塔范围符文III§r")
                .lang("Spire Range Rune III (+15 Range)")
                .register();

        UPGRADE_RUNE_ALPHA = REGISTRATE
                .item("upgrade_rune_alpha", props -> new SpireUpgradeRuneItem(props, 16.0, 16.0, 16.0, 0.75))
                .cnlang("§9尖塔α全能符文§r")
                .lang("Spire Conversion Efficiency Rune (Alpha)")
                .register();
        UPGRADE_RUNE_TRANSLOCATION_1 = REGISTRATE
                .item("upgrade_rune_translocation_1", props -> new SpireUpgradeRuneItem(props, 1.0, 1.0, 4.0, 0.25))
                .cnlang("§9尖塔转位符文I§r")
                .lang("Spire Translocation Rune I")
                .register();
        UPGRADE_RUNE_TRANSLOCATION_2 = REGISTRATE
                .item("upgrade_rune_translocation_2", props -> new SpireUpgradeRuneItem(props, 1.0, 1.0, 32.0, 0.5))
                .cnlang("§9尖塔转位符文II§r")
                .lang("Spire Translocation Rune II (0.5 Conversion)")
                .register();
        UPGRADE_RUNE_TRANSLOCATION_3 = REGISTRATE
                .item("upgrade_rune_translocation_3", props -> new SpireUpgradeRuneItem(props, 1.0, 1.0, 256.0, 1.0))
                .cnlang("§9尖塔转位符文III§r")
                .lang("Spire Translocation Rune III")
                .register();
        UPGRADE_RUNE_OMEGA = REGISTRATE
                .item("upgrade_rune_omega", props -> new SpireUpgradeRuneItem(props, 777.0, 77.0, 777.0, 1.0))
                .cnlang("§5尖塔Ω全能符文§r")
                .lang("Spire Conversion Efficiency Rune (Omega)")
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
                .cnlang("§4血染微处理主机")
                .tag(CustomTags.LuV_CIRCUITS)
                .onRegister(attach(new TooltipBehavior(text -> itemTooltipsChange(bloody_nano_circuit_lang, text))))
                .register();
        MIXIN_WILL_PROCESSOR_MAINFRAME = REGISTRATE
                .item("mixin_will_processor_mainframe", ComponentItem::create)
                .cnlang("§1注元意志处理器主机")
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
                .cnlang("超因果逻辑集成电路")
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

                    for (int i = 0; i <= sub.length - 1; i++) {
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
                .tag(
                        ItemTags.SWORDS,
                        ItemTags.PICKAXES,
                        ItemTags.SHOVELS,
                        ItemTags.AXES,
                        ItemTags.HOES,
                        CustomTags.SCYTHES,
                        CustomTags.SAWS,
                        CustomTags.WRENCHES,
                        CustomTags.WRENCH,
                        CustomTags.FILES,
                        CustomTags.SCREWDRIVERS,
                        CustomTags.WIRE_CUTTERS,
                        CustomTags.KNIVES,
                        CustomTags.PLUNGERS,
                        CustomTags.CRAFTING_CROWBARS,
                        CustomTags.CRAFTING_FILES,
                        CustomTags.CRAFTING_HAMMERS,
                        CustomTags.CRAFTING_KNIVES,
                        CustomTags.CRAFTING_MALLETS,
                        CustomTags.CRAFTING_MORTARS,
                        CustomTags.CRAFTING_SAWS,
                        CustomTags.CRAFTING_SCREWDRIVERS,
                        CustomTags.CRAFTING_WIRE_CUTTERS,
                        CustomTags.CRAFTING_WRENCHES)
                .register();

        BROKEN_RUNE = REGISTRATE
                .item("broken_rune", ComponentItem::create)
                .cnlang("破碎符文")
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
                .cnlang("污血泪眼")
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
        INDEX_CLOTH = REGISTRATE
                .item("index_cloth", ComponentItem::create)
                .cnlang("指令所织之布")
                .onRegister(attach(new TooltipBehavior(text -> text.add(indexPaperLang.translate()))))
                .register();
        ZENITH_DEBUG_TOOL = REGISTRATE
                .item("zenith_debug_tool", ZenithDebugToolItem::new)
                .cnlang("虚境debug工具")
                .lang("Zenith Debug Tool")
                .properties(properties -> {
                    properties.stacksTo(1);
                    properties.rarity(Rarity.EPIC);
                    return properties;
                })
                .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/zenith_star")))
                .register();
    }

    public static void init() {
        registerItem();
        CMFuelItems.registerItem();
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
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_SPEED_1;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_SPEED_2;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_SPEED_3;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_RANGE_1;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_RANGE_2;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_RANGE_3;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_ALPHA;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_TRANSLOCATION_1;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_TRANSLOCATION_2;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_TRANSLOCATION_3;
    public static ItemEntry<SpireUpgradeRuneItem> UPGRADE_RUNE_OMEGA;
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
    public static ItemEntry<ComponentItem> BROKEN_RUNE;
    public static ItemEntry<ComponentItem> EMPTY_RUNE;
    public static ItemEntry<TaintedBloodWeepingEye> TAINTED_BLOOD_EYE;
    public static ItemEntry<ComponentItem> TERRA_CATALYST;
    public static ItemEntry<ComponentItem> ENCAPSULATED_TWIST_MANA;
    public static ItemEntry<ComponentItem> UNIMBUED_SPIRIT;
    public static ItemEntry<ComponentItem> ORICHALCOS_SPIRIT;
    public static ItemEntry<ComponentItem> INDEX_CLOTH;
    public static ItemEntry<ZenithDebugToolItem> ZENITH_DEBUG_TOOL;

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

    @CN({
            "§4血染逻辑LuV电路§r",
            "§4残酷工业的哲学§r"
    })
    @EN({
            "§4血染逻辑LuV电路§r",
            "§4残酷工业的哲学§r"
    })
    public static Lang[] bloody_nano_circuit_lang;
    @CN({
            "§1意志逻辑ZPM电路§r",
            "§1扭曲主导的运算法则§r"
    })
    @EN({
            "§1意志逻辑ZPM电路§r",
            "§1扭曲主导的运算法则§r"
    })
    public static Lang[] mixin_will_circuit_lang;
    @CN("将逻辑映射在符文之上")
    @EN("Map logic onto runes")
    public static Lang rune_circuit_board_lang;
    @CN("§b将信息本身作为能量的非物质运算§r")
    @EN("§bNon-physical computation that takes information itself as energy§r")
    public static Lang umlhpic;
    @CN({
            "§b魔力逻辑UHV电路板§r",
            "§b超越因果，在两世之间纠缠的有限运算§r"
    })
    @EN({
            "§bMagic Logic UHV Circuit Board§r",
            "§bTranscending Karma, a Finite Computation Entwined Between Two Existences§r"
    })
    public static Lang[] magic_quantum_processor_mainframe;
    @CN({
            "放置在血祭坛中时：",
            "使工业血祭坛可以执行额外的配方",
    })
    @EN({
            "放置在血祭坛中时：",
            "使工业血祭坛可以执行额外的配方",
    })
    public static Lang[] etchingJadeLang;
    @CN({
            "放置在血祭坛中时：",
            "使工业血祭坛可以执行额外的配方",
            "减少一半的LP消耗"
    })
    @EN({
            "放置在血祭坛中时：",
            "使工业血祭坛可以执行额外的配方",
            "减少一半的LP消耗"
    })
    public static Lang[] suppressionJadeLang;
    @CN({
            "放置在血祭坛中时：",
            "使工业血祭坛可以执行额外的配方",
            "将工业血祭坛的超频改为无损超频"
    })
    @EN({
            "放置在血祭坛中时：",
            "使工业血祭坛可以执行额外的配方",
            "将工业血祭坛的超频改为无损超频"
    })
    public static Lang[] ephemeralJadeLang;
    @CN({
            "§1意志逻辑IV电路",
            "§1意志结晶的逻辑拓展"
    })
    @EN({
            "§1Will Logic IV Circuit",
            "§1Logical Expansion of the Will Crystal"
    })
    public static Lang[] will_crystal_circuit_lang;
    @CN({
            "§b魔力逻辑HV电路",
            "§b魔力逻辑电路的第一步"
    })
    @EN({
            "§bMagic Logic HV Circuit",
            "§bThe First Step of Magic Logic Circuits"
    })
    public static Lang[] mana_circuit_lang;
    @CN({
            "§a精灵逻辑EV电路",
            "§a超越精灵的逻辑运算因式"
    })
    @EN({
            "§aAdvanced Magic Logic EV Circuit",
            "§aTranscending Elf, a Factorial Operation of Logic"
    })
    public static Lang[] advanced_mana_circuit_lang;
    @CN("§4单向涌血控制")
    @EN("§4One-way blood surge control")
    public static Lang blooddiodelang;
    @CN("§1非静态意志限制器")
    @EN("§1Non-static will limiter")
    public static Lang willdiodelang;
    @CN("§4调整源质流动")
    @EN("§4Tune vital essence flow")
    public static Lang bloodresistorlang;
    @CN("§1阻遏过度思维")
    @EN("§1Restrain runaway thought")
    public static Lang willresistorlang;
    @CN("§4存储命结电荷")
    @EN("§4Store lifeforce charge")
    public static Lang bloodcapacitorlang;
    @CN("§1增生困惑思维")
    @EN("§1Proliferate perplexity")
    public static Lang willcapacitorlang;
    @CN("§4放大源质信号")
    @EN("§4Amplify vital signal")
    public static Lang bloodtransistorlang;
    @CN("§1实体化意志扭曲")
    @EN("§1Embody will distortion")
    public static Lang willtransistorlang;
    @CN("§4提供血涌缓冲")
    @EN("§4Blood-surge buffering")
    public static Lang bloodinductorlang;
    @CN("§4提供矛盾缓冲")
    @EN("§4Paradox buffering")
    public static Lang willinductorlang;
    @CN("§4否决原电路逻辑，执行自意志运算")
    @EN("§4Overrides base circuit logic; runs self-will computation")
    public static Lang willsoclang;
    @CN("§5世界的逻辑本质凝聚于此,§l凝视§r于此")
    @EN("§5The world's logical essence gathers here—§llook§r upon it")
    public static Lang zenithsoclang;
    @CN("被高能魔力击碎的符文，其残余显现出了空间扭曲的性质")
    @EN("A rune shattered by intense mana; its residue warps space")
    public static Lang brokenRuneLang;
    @CN("§a大地的力量凝聚于此")
    @EN("§aEarth's power condensed here")
    public static Lang terra_catalyst;
    @CN("§5宛如天上的繁星")
    @EN("§5Like stars in the sky")
    public static Lang zenith_star;
    @CN("高能魔力蚀刻基板")
    @EN("High-energy mana etched substrate")
    public static Lang mana_circuit_board;
    @CN("调谐电力与魔力的核心")
    @EN("Core that tunes electricity and magic")
    public static Lang magicCoreLang;
    @CN("永恒的十二面体")
    @EN("The eternal dodecahedron")
    public static Lang flowerHeartLang;
    @CN("封藏着魔力的奥义")
    @EN("Arcane secrets of magic sealed within")
    public static Lang encapsulated_twist_mana;
    @CN("放置在魔力凝聚仓内，每个消耗10000魔力能量来转化为奥利哈精魄")
    @EN("Place in Mana Condenser: each consumes 10000 Mana Energy to convert into Orichalcum spirit")
    public static Lang unimbudSpritLang;
    @CN("通过未注魂的精魄在魔力凝聚仓中消耗魔力能量转化而来")
    @EN("Made from unimbued spirit in the Mana Condenser using Mana Energy")
    public static Lang orichalcosSpritLang;
    @CN("§b它会变成什么?")
    @EN("§bWhat will it become?")
    public static Lang blankRuneLang;
    @CN("指令所编织之布")
    @EN("Cloth woven from commands")
    public static Lang indexPaperLang;

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
            .onRegister(attach(new TooltipBehavior(text -> text.add(zenithsoclang.translate()))))
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
            .onRegister(attach(new TooltipBehavior(text -> text.add(zenithsoclang.translate()))))
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
    @CN("蕴含火焰之力")
    public static Lang spiritFireLang;
    public static ItemEntry<ComponentItem> SPIRIT_FIRE = REGISTRATE
            .item("spirit_fire", ComponentItem::create)
            .cnlang("§c火之灵魄")
            .lang("§cSpirit of Fire")
            .onRegister(attach(
                    new TooltipBehavior(text -> text.add(spiritFireLang.translate().withStyle(ChatFormatting.RED)))))
            .register();
    @CN("蕴含水流之力")
    public static Lang spiritWaterLang;
    public static ItemEntry<ComponentItem> SPIRIT_WATER = REGISTRATE
            .item("spirit_water", ComponentItem::create)
            .cnlang("§b水之灵魄")
            .lang("§bSpirit of Water")
            .onRegister(attach(
                    new TooltipBehavior(text -> text.add(spiritWaterLang.translate().withStyle(ChatFormatting.AQUA)))))
            .register();
    @CN("蕴含疾风之力")
    public static Lang spiritWindLang;
    public static ItemEntry<ComponentItem> SPIRIT_WIND = REGISTRATE
            .item("spirit_wind", ComponentItem::create)
            .cnlang("§a风之灵魄")
            .lang("§aSpirit of Wind")
            .onRegister(attach(
                    new TooltipBehavior(text -> text.add(spiritWindLang.translate().withStyle(ChatFormatting.GREEN)))))
            .register();
    @CN("蕴含大地之力")
    public static Lang spiritEarthLang;
    public static ItemEntry<ComponentItem> SPIRIT_EARTH = REGISTRATE
            .item("spirit_earth", ComponentItem::create)
            .cnlang("§6地之灵魄")
            .lang("§6Spirit of Earth")
            .onRegister(attach(
                    new TooltipBehavior(text -> text.add(spiritEarthLang.translate().withStyle(ChatFormatting.GOLD)))))
            .register();
    @CN("寄托我深深的罪孽")
    public static Lang spiritSinLang;
    public static ItemEntry<ComponentItem> SPIRIT_SIN = REGISTRATE
            .item("spirit_sin", ComponentItem::create)
            .cnlang("§5罪之灵魄")
            .lang("§5Spirit of Sin")
            .onRegister(attach(new TooltipBehavior(
                    text -> text.add(spiritSinLang.translate().withStyle(ChatFormatting.DARK_PURPLE)))))
            .register();
    @CN("振幅转化元件")
    public static Lang gemOsLang;
    public static ItemEntry<ComponentItem> GEM_OSCILLATOR = REGISTRATE
            .item("gem_oscillator", ComponentItem::create)
            .cnlang("宝石振子")
            .lang("spirit_sin")
            .onRegister(attach(new TooltipBehavior(text -> text.add(gemOsLang.translate()))))
            .register();
    public static ItemEntry<ComponentItem> WEAK_MANA_SPIRIT = REGISTRATE
            .item("weak_mana_spirit", ComponentItem::create)
            .cnlang("§7稀疏蕴魔精魄")
            .lang("§7Sparse Mana Spirit")
            .register();
    public static ItemEntry<ComponentItem> ASCENDING_MANA_SPIRIT = REGISTRATE
            .item("ascending_mana_spirit", ComponentItem::create)
            .cnlang("§b升腾蕴魔精魄")
            .lang("§bAscending Mana Spirit")
            .register();
    public static ItemEntry<ComponentItem> GIGA_MANA_SPIRIT = REGISTRATE
            .item("giga_mana_spirit", ComponentItem::create)
            .cnlang("§a盖亚蕴魔精魄")
            .lang("§aGaia Mana Spirit")
            .register();
    public static ItemEntry<ComponentItem> RAINBOW_MANA_SPIRIT = REGISTRATE
            .item("rainbow_mana_spirit", ComponentItem::create)
            .cnlang("§d虹彩蕴魔精魄")
            .lang("§dRainbow Mana Spirit")
            .register();
    public static ItemEntry<ComponentItem> BROKEN_MINE_KEY = REGISTRATE
            .item("broken_mine_key", ComponentItem::create)
            .cnlang("§7破碎的工头钥匙")
            .lang("§7Broken Foreman's Key")
            .register();
    @CN(
            {
                    "§6可打开任何地牢封印，将普通封印当作矿区入口的封印使用",
                    "§4我讨厌你们,讨厌所有的钥匙,讨厌所有的锁,讨厌你们整个领域！"
            })
    @EN(
            {
                    "§6Can open any dungeon seal, treating normal seals as mine-entrance seals",
                    "§4I hate you all, hate every key, hate every lock, hate your entire realm!"
            })
    public static Lang perfectMineKeyLang[];
    public static ItemEntry<PerfectMineKeyItem> PERFECT_MINE_KEY = REGISTRATE
            .item("perfect_mine_key", PerfectMineKeyItem::new)
            .cnlang("§6完善的工头钥匙")
            .lang("§6Perfect Foreman's Key")
            .properties(properties -> {
                properties.stacksTo(1);
                properties.rarity(Rarity.EPIC);
                return properties;
            })
            .onRegister(attach(new TooltipBehavior(text -> itemTooltipsChange(perfectMineKeyLang,text))))
            .register();
    @CN({
            "§7来自过去的崩坏的残留",
            "在崩解的水晶之中，你能否看见那道潜藏的泪痕？"
    })
    @EN({
            "§7来自过去的崩坏的残留",
            "在崩解的水晶之中，你能否看见那道潜藏的泪痕？"
    })
    public static Lang[] cryshCatalystLang;
    public static ItemEntry<ComponentItem> CRYSH_CATALYST = REGISTRATE
            .item("crysh_catalyst", ComponentItem::create)
            .cnlang("§7崩坏的催化剂")
            .lang("§7crysh_catalyst")
            .onRegister(attach(new TooltipBehavior(text -> itemTooltipsChange(cryshCatalystLang, text))))
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

    @CN("蚀刻强化")
    @EN("Etching Upgrade")
    public static Lang etching_jade_upgrade;
    @CN("抑液强化")
    @EN("Suppression Upgrade")
    public static Lang suppression_jade_upgrade;
    @CN("须臾强化")
    @EN("Ephemeral Upgrade")
    public static Lang ephemeral_jade_upgrade;
}