package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.ICustomDescriptionId;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.moguang.ctnhmana.item.bosssummon.BossSummonerBehavior;
import com.moguang.ctnhmana.item.manamachineupdate.BMUpgradeItemT1;
import com.moguang.ctnhmana.item.manamachineupdate.BTUpgradeItemT1;
import com.moguang.ctnhmana.item.manamachineupdate.*;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

import static com.gregtechceu.gtceu.common.data.GTItems.attach;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.*;

import com.moguang.ctnhmana.item.bosssummon.ThrowItem;
public class CMItems {
    static {
        REGISTRATE.creativeModeTab(() -> CMCreativeModeTabs.ITEM);
    }

    public static void registerItem()
    {
        BT_UPDATE_T1=REGISTRATE
                .item("botania_update_t1", BTUpgradeItemT1::new)
                .cnlang("§9繁花与星空之祝福")
                .register();
        GT_UPDATE_T1=REGISTRATE
                .item("gt_update_t1", GTUpgradeItemT1::new)
                .cnlang("§9流水线之视野")
                .register();
        GT_UPDATE_T2=REGISTRATE
                .item("gt_update_t2", GTUpgradeItemT2::new)
                .cnlang("§9流水线之远视")
                .register();
        BM_UPDATE_T1=REGISTRATE
                .item("bm_update_t1", BMUpgradeItemT1::new)
                .cnlang("§c扭曲之铸造")
                .register();
        BM_UPDATE_T2=REGISTRATE
                .item("bm_update_t2", BMUpgradeItemT2::new)
                .cnlang("§c畸变之铸造")
                .register();
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
                .item("horizen_rune",ComponentItem::create)
                .cnlang("§5视域§r符文")
                .lang("§5Horizen§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.horizen_rune").withStyle(ChatFormatting.DARK_PURPLE));
                })))
                .register();
        STARLIGHT_RUNE = REGISTRATE
                .item("starlight_rune",ComponentItem::create)
                .cnlang("§9星光§r符文")
                .lang("§9Starlight§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.starlight_rune").withStyle(ChatFormatting.BLUE));
                })))
                .register();
        TWIST_RUNE = REGISTRATE
                .item("twist_rune",ComponentItem::create)
                .cnlang("§c扭曲§r符文")
                .lang("§cTwist§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.twist_rune").withStyle(ChatFormatting.RED));
                })))
                .register();
        QUASAR_RUNE = REGISTRATE
                .item("quasar_rune",ComponentItem::create)
                .cnlang("§k类星体§r符文")
                .lang("§kQuasar§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.quasar_rune").withStyle(ChatFormatting.LIGHT_PURPLE));
                })))
                .register();
        PROLIFERATION_RUNE = REGISTRATE
                .item("proliferation_rune",ComponentItem::create)
                .cnlang("§a增殖§r符文")
                .lang("§aProliferation§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.proliferation_rune").withStyle(ChatFormatting.GREEN));
                })))
                .register();
        UMLHPIC_WAFER = REGISTRATE
                .item("umlhpic_wafer", ComponentItem::create)
                .cnlang("UMLHPIC晶圆")
                .lang("Umlhpic Wafer")
                .onRegister(attach(new TooltipBehavior(text ->
                        text.add(umlhpic.translate())
                )))
                .register();

        UMLHPIC_CHIP = REGISTRATE
                .item("umlhpic_chip", ComponentItem::create)
                .cnlang("UMLHPIC芯片")
                .lang("Umlhpic Chip")
                .onRegister(attach(new TooltipBehavior(text ->
                        text.add(umlhpic.translate())
                )))
                .register();

        MAGIC_QUANTUM_PROCESSOR_MAINFRAME = REGISTRATE
                .item("magic_quantum_processor_mainframe", ComponentItem::create)
                .cnlang("量子纠缠究极魔力主机")
                .lang("Magic Quantum Processor Mainframe")
                .tag(CustomTags.UV_CIRCUITS)
                .onRegister(attach(new TooltipBehavior(text ->
                        text.add(magic_quantum_processor_mainframe.translate())
                )))
                .register();

    }
    public static void init() {
        registerItem();
    }

    public static ItemEntry<ThrowItem> BOSS_SUMMONER;
    public static ItemEntry<ThrowItem> ADVANCED_BOSS_SUMMONER;
    public static ItemEntry<BTUpgradeItemT1>BT_UPDATE_T1;
    public static ItemEntry<GTUpgradeItemT1>GT_UPDATE_T1;
    public static ItemEntry<BMUpgradeItemT1>BM_UPDATE_T1;
    public static ItemEntry<BTUpgradeItemT1>BT_UPDATE_T2;
    public static ItemEntry<GTUpgradeItemT2>GT_UPDATE_T2;
    public static ItemEntry<BMUpgradeItemT2>BM_UPDATE_T2;
    public static ItemEntry<ComponentItem> HORIZEN_RUNE;
    public static ItemEntry<ComponentItem> STARLIGHT_RUNE;
    public static ItemEntry<ComponentItem> TWIST_RUNE;
    public static ItemEntry<ComponentItem> QUASAR_RUNE;
    public static ItemEntry<ComponentItem> PROLIFERATION_RUNE;
public static ItemEntry<ComponentItem> UMLHPIC_WAFER;
public static ItemEntry<ComponentItem> UMLHPIC_CHIP;
public static ItemEntry<ComponentItem> MAGIC_QUANTUM_PROCESSOR_MAINFRAME;
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
    //为不具有format的列表lang提供列表
    public static List<Component> itemTooltipsAdd(Lang[] langs, List<Component> list)
    {
        for(Lang lang:langs)
        {
            list.add(lang.translate());
        }
        return list;
    }
    public static ItemEntry<ComponentItem> MANA_ELECTRONIC_CIRCUIT = REGISTRATE
            .item("mana_electronic_circuit", ComponentItem::create)
            .cnlang("注魔的电子电路")
            .tag(CustomTags.HV_CIRCUITS)
            .lang("Mana Electronic Circuit")
            .register();

    public static ItemEntry<ComponentItem> MANA_INTEGRATED_CIRCUIT = REGISTRATE
            .item("mana_integrated_circuit", ComponentItem::create)
            .cnlang("注魔的集成电路")
            .tag(CustomTags.EV_CIRCUITS)
            .lang("Mana Integrated Circuit")
            .register();

    public static ItemEntry<ComponentItem> BLOODED_MICRO_PROCESSOR_MAINFRAME = REGISTRATE
            .item("blooded_micro_processor_mainframe", ComponentItem::create)
            .cnlang("血染微型处理器主机")
            .tag(CustomTags.IV_CIRCUITS)
            .lang("Blooded Micro Processor Mainframe")
            .register();

    public static ItemEntry<ComponentItem> WILL_NANO_PROCESSOR_MAINFRAME = REGISTRATE
            .item("will_nano_processor_mainframe", ComponentItem::create)
            .cnlang("恶魔纳米处理器主机")
            .tag(CustomTags.LuV_CIRCUITS)
            .lang("Will Nano Processor Mainframe")
            .register();

    public static ItemEntry<ComponentItem> ELF_CATALYST = REGISTRATE
            .item("elf_catalyst", ComponentItem::create)
            .cnlang("精灵催化剂")
            .lang("Elf Catalyst")
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
            .cnlang("注魔电阻")
            .lang("Mana Resistor")
            .register();

    public static ItemEntry<ComponentItem> MANA_CAPACITOR = REGISTRATE
            .item("mana_capacitor", ComponentItem::create)
            .cnlang("注魔电容")
            .lang("Mana Capacitor")
            .register();

    public static ItemEntry<ComponentItem> MANA_TRANSISTOR = REGISTRATE
            .item("mana_transistor", ComponentItem::create)
            .cnlang("注魔晶体管")
            .lang("Mana Transistor")
            .register();

    public static ItemEntry<ComponentItem> MANA_DIODE = REGISTRATE
            .item("mana_diode", ComponentItem::create)
            .cnlang("注魔二极管")
            .lang("Mana Diode")
            .register();

    public static ItemEntry<ComponentItem> MANA_INDUCTOR = REGISTRATE
            .item("mana_inductor", ComponentItem::create)
            .cnlang("注魔电感")
            .lang("Mana Inductor")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_RESISTOR = REGISTRATE
            .item("advanced_mana_resistor", ComponentItem::create)
            .cnlang("高级注魔电阻")
            .lang("Advanced Mana Resistor")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_CAPACITOR = REGISTRATE
            .item("advanced_mana_capacitor", ComponentItem::create)
            .cnlang("高级注魔电容")
            .lang("Advanced Mana Capacitor")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_TRANSISTOR = REGISTRATE
            .item("advanced_mana_transistor", ComponentItem::create)
            .cnlang("高级注魔晶体管")
            .lang("Advanced Mana Transistor")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_DIODE = REGISTRATE
            .item("advanced_mana_diode", ComponentItem::create)
            .cnlang("高级注魔二极管")
            .lang("Advanced Mana Diode")
            .register();

    public static ItemEntry<ComponentItem> ADVANCED_MANA_INDUCTOR = REGISTRATE
            .item("advanced_mana_inductor", ComponentItem::create)
            .cnlang("高级注魔电感")
            .lang("Advanced Mana Inductor")
            .register();

    public static ItemEntry<ComponentItem> MANA_WAFER = REGISTRATE
            .item("mana_wafer", ComponentItem::create)
            .cnlang("§b注魔的SOC晶圆")
            .lang("§bMana Wafer")
            .register();

    public static ItemEntry<ComponentItem> ZENITH_WAFER = REGISTRATE
            .item("zenith_wafer", ComponentItem::create)
            .cnlang("§5天顶SOC晶圆")
            .lang("§5§bMana Wafer")
            .register();

    public static ItemEntry<ComponentItem> ENDSLATE = REGISTRATE
            .item("endslate", ComponentItem::create)
            .cnlang("终焉石板")
            .lang("Endslate")
            .register();



}
