package com.moguang.ctnhmana.data.lang;

import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMachines;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import tech.vixhentx.mcmod.ctnhlib.registrate.lang.RegistrateCNLangProvider;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.GTValues.*;


public class ChineseLangHandler {
    //机器tooltips
    @CN(
            {
                    "用于魔力机器的魔力能量输入，支持输入：植物魔法魔力，灵魂网络的生命源质与液态魔力",
                    "植物魔法的魔力可以通过在物品槽位放入魔力戒指或使用魔力发射器进行传输",
                    "通过在物品槽位放入宝珠来完成与宝珠所有者的灵魂网络的绑定并自动从灵魂网络抽取生命源质",
                    "直接从外部输入（不支持手动输入）来往舱室输入液态魔力",
                    "最大的魔力能量存储：%d",
                    "最大的生命植物魔法魔力存储：%d",
                    "最大的液态魔力存储：%d mb"
            }
    )
    @EN(
            {
                    "用于魔力机器的魔力能量输入，支持输入：植物魔法魔力，灵魂网络的生命源质与液态魔力",
                    "植物魔法的魔力可以通过在物品槽位放入魔力戒指或使用魔力发射器进行传输",
                    "通过在物品槽位放入宝珠来完成与宝珠所有者的灵魂网络的绑定并自动从灵魂网络抽取生命源质",
                    "直接从外部输入（不支持手动输入）来往舱室注入液态魔力",
                    "最大的魔力能量存储：%d",
                    "最大的生命植物魔法魔力存储：%d",
                    "最大的液态魔力存储：%d mb"
            }
    )
    public static  Lang[]  manahatchtootip_base;

    @CN(
            {
                    "用于血法师的魔力机器的魔力能量输入，支持输入：灵魂网络的生命源质量，液态生命源质",
                    "不支持液态魔力，植物魔法魔力",
                    "通过在物品槽位放入宝珠来完成与宝珠所有者的灵魂网络的绑定并自动从灵魂网络抽取生命源质",
                    "直接从外部输入（不支持手动输入）来往舱室注入液态生命源质",
                    "每秒自动从当前区块尽可能地吸收各种恶魔意志",
                    "最大的恶魔意志存储量：%d",
                    "最大的魔力能量存储量：%d",
                    "最大的液态魔力生命源质：%d mb"
            }
    )
    @EN(
            {
                    "用于血法师的魔力机器的魔力能量输入，支持输入：灵魂网络的生命源质量，液态生命源质",
                    "不支持液态魔力，植物魔法魔力",
                    "通过在物品槽位放入宝珠来完成与宝珠所有者的灵魂网络的绑定并自动从灵魂网络抽取生命源质",
                    "直接从外部输入（不支持手动输入）来往舱室注入液态生命源质",
                    "每秒自动从当前区块尽可能地吸收各种恶魔意志",
                    "最大的恶魔意志存储量：%d",
                    "最大的魔力能量存储：%d",
                    "最大的液态魔力生命源质：%d mb"
            }
    )
    public static  Lang[]  bloodmanahatchtootip_base;
    @CN(
            {
                    "用于魔力机器的魔力能量输入，支持输入：植物魔法魔力与液态魔力",
                    "不支持输入生命源质",
                    "植物魔法的魔力可以通过在物品槽位放入魔力戒指或使用魔力发射器进行传输",
                    "每秒从半径为8的火花绑定的魔力存储中抽取15000植物魔法魔力，每10秒重新检测一次周围的火花",
                    "直接从外部输入（不支持手动输入）来往舱室输入液态魔力",
                    "最大的魔力能量存储：%d",
                    "最大的生命植物魔法魔力存储：%d",
                    "最大的液态魔力存储：%d mb"
            }
    )
    @EN(
            {
                    "用于魔力机器的魔力能量输入，支持输入：植物魔法魔力与液态魔力",
                    "不支持输入生命源质",
                    "植物魔法的魔力可以通过在物品槽位放入魔力戒指或使用魔力发射器进行传输",
                    "每秒从半径为8范围内的每一个火花绑定的魔力存储中抽取%d植物魔法魔力，每10秒重新检测一次周围的火花",
                    "直接从外部输入（不支持手动输入）来往舱室注入液态魔力",
                    "最大的魔力能量存储：%d",
                    "最大的生命植物魔法魔力存储：%d",
                    "最大的液态魔力存储：%d mb"
            }
    )
    public static  Lang[]  sparkmanahatchtootip_base;
    @CN("基础魔力凝聚者")

    public static Lang manahatchtooltip_1;
    @CN("§c凝血魔力凝聚者")
    public static Lang bloodmanahatchtooltip_1;



    public static void init(RegistrateCNLangProvider provider){
        provider.add("ctnh.item.runes.starlight_rune","Per Aspera Ad Astra");
        provider.add("ctnh.item.runes.twist_rune","速度与人性的扭曲");
        provider.add("ctnh.item.runes.proliferation_rune","金融与生物的本能");
        provider.add("ctnh.item.runes.quasar_rune","毁灭与创造交替");
        provider.add("ctnh.item.runes.horizen_rune","视野所向之处");
//        provider.addItem(CMItems.FLOWER_UPDATE,"§9繁花与星空之祝福");
//        provider.addItem(CMItems.GT_UPDATE,"§o流水线之视野");
        provider.add("ritual.ctnhmana.ritualextractor","生灵萃取仪式");
        provider.add("ritual.ctnhmana.chargerRitual","充能仪式");
        provider.add("ritual.ctnhmana.ritualbosssummon","战争呼唤仪式");
        provider.add("ritual.ctnhmana.dragon_cloudritual","龙吟仪式");
        provider.add("ritual.ctnhmana.dragon_shroudsight","虚境之视");
        provider.add("effect.ctnhmana.shroud_gaze","虚境的凝视");
        provider.add("ctnh.boss_summoner.use", "右键长按蓄力掷出，在落点处召唤一只神化boss，每次使用有五分之一的概率消耗");
        provider.add("ctnhmana.jade.manahatch.manaprogress","魔力能量：%s / %s");
        provider.add("ctnhmana.jade.manahatch.btmanaprogress","植物魔法魔力量：%s / %s");
        provider.add("ctnhmana.jade.manahatch.bmmanaprogress","灵魂网络LP量：%s / %s");
        provider.add("ctnhmana.jade.manahatch.rawwillprogress", "§9普通意志：%s / %s");
        provider.add("ctnhmana.jade.manahatch.steadfastwillprogress", "§5坚韧意志：%s / %s");
        provider.add("ctnhmana.jade.manahatch.corrosivewillprogress", "§a侵蚀意志：%s / %s");
        provider.add("ctnhmana.jade.manahatch.destructivewillprogress", "§6破坏意志：%s / %s");
        provider.add("ctnhmana.jade.manahatch.vengefulwillprogress", "§c复仇意志：%s / %s");
        provider.add("ctnhmana.jade.terra_plate.manaprogress","魔力输入进度：%s / %s (%.2f%%)");
        provider.add("config.jade.plugin_gtceu.manahatch_status_provider","魔力舱室属性");
        provider.add("config.jade.plugin_gtceu.manamachine_status_provider","魔力机器属性");
        provider.add("config.jade.plugin_ctnhmana.mana_pool_status","魔力池属性");
        //provider.add("ctnh.recipe_type.info", "配方类型：%s");
        provider.add("ctnh.recipe_type.list", "%s, %s");
        provider.add("ctnhmana.copyright.info","由CTNHMana增加");
    }
    //物品tooltips



    @CN("§b同时蕴含信息和能量§r")
    @EN("§bSimultaneously containing information and energy§r")
    public static Lang umlhpic;
    @CN("§b魔力逻辑UV电路板§r")
    @EN("§bMagic Logic UV Circuit Board§r")
    public static Lang magic_quantum_processor_mainframe;

    @CN(
            {
                    "§4血染逻辑LuV电路§r",
                    "§4残酷工业的哲学§r"
            }
    )
    @EN(
            {
                    "§4血染逻辑LuV电路§r",
                    "§4残酷工业的哲学§r"
            }
    )
    public static Lang[] bloody_nano_circuit_lang;

    @CN(
            {
                    "§1意志逻辑ZPM电路§r",
                    "§1扭曲主导的运算法则§r"
            }
    )
    @EN(
            {
                    "§1意志逻辑ZPM电路§r",
                    "§1扭曲主导的运算法则§r"
            }
    )
    public static Lang[] mixin_will_circuit_lang;
    @CN("将逻辑映射在符文之上")
    public static Lang rune_circuit_board_lang;

    @CN(
            {
                    "§b魔力逻辑HV电路",
                    "§b魔力逻辑电路的第一步"
            }
    )
    @EN(
            {
                    "§b魔力逻辑HV电路",
                    "§b魔力电路化的第一步"
            }
    )
    public static Lang[] mana_circuit_lang;

    @CN(
            {
                    "§a精灵逻辑EV电路",
                    "§a超越精灵的逻辑运算因式"
            }
    )
    @EN(
            {
                    "§a精灵逻辑EV电路",
                    "§a超越精灵的逻辑运算因式"
            }
    )
    public static Lang[] advanced_mana_circuit_lang;
    @CN(
            {
                    "§1意志逻辑IV电路",
                    "§1意志结晶的逻辑拓展"
            }
    )
    @EN(
            {
                    "§1意志逻辑IV电路",
                    "§1意志结晶的逻辑拓展"
            }
    )
    public static Lang[] will_crystal_circuit_lang;
    @CN("§4单向涌血控制")
    public static Lang blooddiodelang;
    @CN("§1非静态意志限制器")
    public static Lang willdiodelang;
    @CN("§4调整源质流动")
    public static Lang bloodresistorlang;
    @CN("§1阻遏过度思维")
    public static Lang willresistorlang;
    @CN("§4存储命结电荷")
    public static Lang bloodcapacitorlang;
    @CN("§1增生困惑思维")
    public static Lang willcapacitorlang;
    @CN("§4放大源质信号")
    public static Lang bloodtransistorlang;
    @CN("§1实体化意志扭曲")
    public static Lang willtransistorlang;
    @CN("§4提供血涌缓冲")
    public static Lang bloodinductorlang;
    @CN("§4提供矛盾缓冲")
    public static Lang willinductorlang;
    @CN("§4否决原电路逻辑，执行自意志运算")
    public static Lang willsoclang;
    @CN(
            {
                    "佩戴时获得:",
                    "显示§b魔力池§r，§b魔力花§r和§b魔力凝聚板§r的详细信息",
                    "看穿所有敌人的伪装",
                    "攻击时必定抓住敌人的破绽",
                    "被所有生物所憎恶，§4§l这就是看穿真实的代价",
                    "§5§o以自己的幸福，换来可以看见真实的眼睛，这就是身为§r§4§n觉§r§5§o之妖怪的被厌恶者的觉悟"
            }
    )
    @EN(
            {
                    "佩戴时获得:",
                    "显示魔力池，魔力花和魔力凝聚板的详细信息",
                    "看穿所有敌人的伪装",
                    "攻击时必定抓住敌人的破绽",
                    "被所有生物所憎恶，§4§l这就是看穿真实的代价",
                    "§5§o以自己的幸福，换来可以看见真实的眼睛，这就是身为§r§n觉§r§5§o之妖怪的被厌恶者的觉悟"
            }
    )
    public static Lang[] satori_thirdeye_tooltip;
    @CN(
            {
                    "魔法之力！",
                    "要求结构中必须且仅有1个魔力凝聚仓",
                    "通过机器内升级槽放入升级",
                    "每秒消耗%d魔力能量，电压每有1级，消耗的魔力能量就翻倍",
                    "无损超频（暂时）！"
            }
    )
    @EN(
            {
                    "§b魔法之力！",
                    "要求结构中必须且仅有1个魔力凝聚仓",
                    "通过机器内升级槽放入升级",
                    "每秒消耗%d魔力能量，电压每有1级，消耗的魔力能量就翻倍",
                    "无损超频（暂时）！"
            }
    )
    public static Lang[] basemanamutiblockLang;
    @CN(
            {
                    "§4然而，轰鸣机器的齿轮中流淌的鲜血，也只是另一场名为'工业化'的血祭",
                    "§4工业血祭坛§r是特殊的祭坛结构，其拥有多个等级，其等级决定了可以运行的配方，在结构成型时，将自动绑定结构中的血祭坛",
                    "根据自身的等级，增强§c所有§r血祭坛符文的效果，并直接扩大血祭容量",
                    "§c清除血祭坛的缓存容量，也不会使用血祭坛的缓存LP",
                    "具有与血祭坛类似的供血机制，每秒从输入总线抽取一定数量的LP注入血祭坛，该注入受到等级，符文的影响",
                    "运行时，直接消耗血祭坛中的LP来进行工作。LP不足时，§c进度会缓慢倒退而不是直接归零",
                    "§c不可进行任何电力超频§r，自身工作速度受到血祭坛工作速度影响，同时拥有特殊的血超频机制",
                    "运行时，祭坛血等级每高配方等级一级，速度翻倍，LP消耗速率翻四倍",
                    "具有批处理功能：当配方时间<1s时，机器会尝试批处理直到运行时间≥1s",
                    "§c注意：即使批处理不足，实际运行时间不会低于1s§r",
                    "按住CTRL来显示具体数据"
            }
    )
    @EN(
            {
                    "§4然而，轰鸣机器的齿轮中流淌的鲜血，也只是另一场名为'工业化'的血祭",
                    "工业血祭坛是特殊的祭坛结构，其拥有多个等级，其等级决定了可以运行的配方，在结构成型时，将自动绑定结构中的血祭坛",
                    "根据自身的等级，增强所有血祭坛的符文的效果，并直接扩大血祭容量",
                    "清除血祭坛的缓存容量，也不会使用血祭坛的缓存LP",
                    "具有与血祭坛类似的供血机制，每秒从输入总线抽取一定数量的LP注入血祭坛，该注入受到等级，符文的影响",
                    "运行时，直接消耗血祭坛中的LP来进行工作。LP不足时，§c进度会缓慢倒退而不是直接归零",
                    "§c不可进行任何电力超频§r，自身工作速度受到血祭坛工作速度影响，同时拥有特殊的血超频机制",
                    "运行时，祭坛血等级每高配方等级一级，速度翻倍，LP消耗速率翻四倍",
                    "具有批处理功能：当配方时间<1s时，机器会尝试批处理直到运行时间≥1s",
                    "§c注意：即使批处理不足，实际运行时间不会低于1s§r",
                    "按住CTRL来显示具体数据"
            }
    )
    public static Lang[] industrialAltarLang;
    @CN(
            {
                    "工业血祭坛数据详细：",
                    "增强符文效果：",
                    "血祭坛扩大(工业血祭坛等级-1)倍容量",
                    "§b速度符文§r：额外提升(0.05)*(工业血祭坛等级-2)的速度",
                    "§e增容符文§r：提供的容量乘以1.5^(工业血祭坛等级-1)",
                    "§4超容符文§r：提供的指数容量基数提升0.025*(工业血祭坛等级-1）",
                    "§1转位符文§r：指数基数提升(0.05*工业血祭坛等级)",
                    "效率符文：视作额外计算(工业血祭坛等级-1)个效率符文",
                    "基础供血速率：20lp*(2^工业血祭坛等级)/s 该速率受到转位符文，促速符文的影响",

            }
    )
    @EN(
            {
                    "工业血祭坛数据详细：",
                    "增强符文效果：",
                    "血祭坛扩大(工业血祭坛等级-1)倍容量",
                    "§b速度符文§r：额外提升(0.05)*(工业血祭坛等级-2)的速度",
                    "§e增容符文§r：提供的容量乘以1.5^(工业血祭坛等级-1)",
                    "§4超容符文§r：提供的指数容量基数提升0.025*(工业血祭坛等级-1）",
                    "§1转位符文§r：指数基数提升(0.05*工业血祭坛等级)",
                    "效率符文：视作额外计算(工业血祭坛等级-1)个效率符文",
                    "基础供血速率：20lp*(2^工业血祭坛等级)/s 该速率受到转位符文，促速符文的影响"
            }
    )
    public static Lang[] industrialAltarctrlLang;





}
