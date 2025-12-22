package com.moguang.ctnhmana.item.itemregister;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.tterrag.registrate.util.entry.ItemEntry;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.magic_quantum_processor_mainframe;
import static com.moguang.ctnhmana.registry.CMItems.attach;

public class registercircuit {
    public static void init() {

        registeritem();

    }
    @CN("§4单向涌血控制")
    public static Lang blooddiodelang;
    @CN("§4调整源质流动")
    public static Lang bloodresistorlang;
    @CN("§4存储命结电荷")
    public static Lang bloodcapacitorlang;
    @CN("§4放大源质信号")
    public static Lang bloodtransistorlang;
    @CN("§4提供血涌缓冲")
    public static Lang bloodinductorlang;
    @CN("§4否决原电路逻辑，执行自意志运算")
    public static Lang willsoclang;
    public static ItemEntry<ComponentItem> BLOOD_DIODE=REGISTRATE
            .item("blood_diode", ComponentItem::create)
            .cnlang("血染逻辑二极管")
            .onRegister(attach(new TooltipBehavior(text ->
                    text.add(blooddiodelang.translate())
            )))
            .register();
    public static ItemEntry<ComponentItem> BLOOD_RESISTOR=REGISTRATE
            .item("blood_resistor", ComponentItem::create)
            .cnlang("血染逻辑电阻")
            .onRegister(attach(new TooltipBehavior(text ->
                    text.add(bloodresistorlang.translate())
            )))
            .register();
    public static ItemEntry<ComponentItem> BLOOD_CAPACITOR=REGISTRATE
            .item("blood_capacitor", ComponentItem::create)
            .cnlang("血染逻辑电容")
            .onRegister(attach(new TooltipBehavior(text ->
                    text.add(bloodcapacitorlang.translate())
            )))
            .register();
    public static ItemEntry<ComponentItem> BLOOD_TRANSISTOR=REGISTRATE
            .item("blood_transistor", ComponentItem::create)
            .cnlang("鲜血结晶管")
            .onRegister(attach(new TooltipBehavior(text ->
                    text.add(bloodtransistorlang.translate())
            )))
            .register();
    public static ItemEntry<ComponentItem> BLOOD_INDUCTOR=REGISTRATE
            .item("blood_inductor", ComponentItem::create)
            .cnlang("血级电感")
            .onRegister(attach(new TooltipBehavior(text ->
                    text.add(bloodinductorlang.translate())
            )))
            .register();
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
    public static ItemEntry<ComponentItem> WILL_WAFER = REGISTRATE
            .item("will_wafer", ComponentItem::create)
            .cnlang("§b意志运算晶圆")
            .onRegister(attach(new TooltipBehavior(text ->
                    text.add(willsoclang.translate())
            )))
            .lang("§bWill Wafer")
            .register();
    public static ItemEntry<ComponentItem> WILL_SOC = REGISTRATE
            .item("will_soc", ComponentItem::create)
            .cnlang("§b意志运算SOC")
            .onRegister(attach(new TooltipBehavior(text ->
                    text.add(willsoclang.translate())
            )))
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

    private static void registeritem() {


    }




}
