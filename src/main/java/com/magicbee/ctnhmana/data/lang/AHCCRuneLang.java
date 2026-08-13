package com.magicbee.ctnhmana.data.lang;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.ctnhlang.Prefix;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.registrate.lang.RegistrateCNLangProvider;

@Prefix("ahcc_rune")
public class AHCCRuneLang {

    @CN({ "[AHCC] 水符文", "十字相邻燃料: 产热-1, 占用-2" })
    @EN({ "[AHCC] Water Rune", "Cross-adjacent fuels: Heat -1, Stability use -2" })
    public static Lang[] ahccRuneWater;

    @CN({ "[AHCC] 火符文", "十字相邻燃料: 产热x1.2", "自身占用+1稳定度" })
    @EN({ "[AHCC] Fire Rune", "Cross-adjacent fuels: Heat x1.2", "Self stability use +1" })
    public static Lang[] ahccRuneFire;

    @CN({ "[AHCC] 地符文", "每有1个地元素, 则热量上限+5", "自身占用+1稳定度" })
    @EN({ "[AHCC] Earth Rune", "Max heat +5xEarth elements", "Self stability use +1" })
    public static Lang[] ahccRuneEarth;

    @CN({ "[AHCC] 风符文", "冷却轮: 每个风符文-20tick, 最低20tick", "自身占用+1稳定度" })
    @EN({ "[AHCC] Air Rune", "Cooldown phase: -20 ticks per air rune, minimum 20", "Self stability use +1" })
    public static Lang[] ahccRuneAir;

    @CN({ "[AHCC] 春符文", ">50%稳定: 全燃料+1热量, 自身占用+2稳定度", "<=50%稳定: 自身占用-1稳定度" })
    @EN({ "[AHCC] Spring Rune", ">50% stability: all fuel heat +1, self use +2", "<=50% stability: self use -1" })
    public static Lang[] ahccRuneSpring;

    @CN({ "[AHCC] 夏符文", "十字相邻燃料: 每有1个火元素, 则产热额外+5%", "自身占用: 基础+1稳定度, 且每有1个火元素再+1稳定度" })
    @EN({ "[AHCC] Summer Rune", "Cross fuels: Heat x(1+0.05xFire elements)", "Self stability use: 1+Fire elements" })
    public static Lang[] ahccRuneSummer;

    @CN({ "[AHCC] 秋符文", "每有1个地元素且每有1个相邻燃料, 则热量上限+1", "自身占用+2稳定度" })
    @EN({ "[AHCC] Autumn Rune", "Max heat +(Earth elements x adjacent fuel count)", "Self stability use +2" })
    public static Lang[] ahccRuneAutumn;

    @CN({ "[AHCC] 冬符文", "冷却剂会按冬符文数量改变热量乘区(最多5个)" })
    @EN({ "[AHCC] Winter Rune", "Coolant modifies heat multiplier by winter rune count (up to 5)" })
    public static Lang[] ahccRuneWinter;

    @CN({ "[AHCC] 魔力符文", "自身占用+2稳定度", "额外提供所有种类元素各一个" })
    @EN({ "[AHCC] Mana Rune", "Self stability use +2", "Element map: +1 to every element per rune" })
    public static Lang[] ahccRuneMana;

    @CN({ "[AHCC] 色欲符文", "全燃料: 产热x1.1", "周围1格(含对角): 产热x0.75", "自身占用等同于罪孽元素数量的稳定度" })
    @EN({ "[AHCC] Lust Rune", "All fuels: Heat x1.1", "Radius-1 including diagonals: Heat x0.75",
            "Self use = Sin elements" })
    public static Lang[] ahccRuneLust;

    @CN({ "[AHCC] 暴食符文", "每有1个地元素且每有1个罪孽元素, 则热量上限+2", "自身占用等同于罪孽元素数量的稳定度" })
    @EN({ "[AHCC] Gluttony Rune", "Max heat +(Earth x Sin x2)", "Self use = Sin elements" })
    public static Lang[] ahccRuneGluttony;

    @CN({ "[AHCC] 贪婪符文", "左侧燃料: 占用x2", "左侧燃料：产热x1.5 如果其带有罪孽元素，产热*2", "自身占用等同于罪孽元素数量的稳定度" })
    @EN({ "[AHCC] Greed Rune", "Left fuel: Stability use x2", "Left fuel with sin tag: Heat x2, else x1.5",
            "Self use = Sin elements" })
    public static Lang[] ahccRuneGreed;

    @CN({ "[AHCC] 怠惰符文", "轮时长: 发热轮+20tick, 冷却轮+20tick", "若水符文>=10: 两轮再各+20tick", "自身占用触发条件:", "5+罪孽元素: +2稳定度",
            "10+罪孽元素: +4稳定度", "15+罪孽元素: +6稳定度" })
    @EN({ "[AHCC] Sloth Rune", "Duration: +20 ticks for recipe/cooldown phases",
            "If water runes >=10: both phases +20 ticks again", "Self use = +2 per 5 Sin elements" })
    public static Lang[] ahccRuneSloth;

    @CN({ "[AHCC] 愤怒符文", "稳定<50%时: 罪孽元素和火元素之和每有1, 则对角燃料产热+5%", "罪孽元素和火元素之和每有1, 则对角燃料占用+1", "自身占用等同于罪孽元素数量的稳定度" })
    @EN({ "[AHCC] Wrath Rune", "When stability <50%: diagonal fuels heat x(1+0.05x(Sin+Fire))",
            "Diagonal fuels use +(Sin+Fire)", "Self use = Sin elements" })
    public static Lang[] ahccRuneWrath;

    @CN({ "[AHCC] 嫉妒符文", "自身占用等同于罪孽元素数量的稳定度", "仅在无第二嫉妒且目标为全局高热时生效", "每有1个罪孽元素, 则正四格燃料产热额外+10%" })
    @EN({ "[AHCC] Envy Rune", "Self use = Sin elements", "Works only with no second Envy and target being top-heat",
            "Cardinal fuels heat x(1+0.1xSin)" })
    public static Lang[] ahccRuneEnvy;

    @CN({ "[AHCC] 傲慢符文", "每有1个符文, 则正四格燃料产热倍率-0.2(基础2.0, 最低0.1)", "自身占用等同于罪孽元素数量的稳定度" })
    @EN({ "[AHCC] Pride Rune", "Cardinal fuels: Heat x max(0.1, 2.0-0.2xTotalRunes)", "Self use = Sin elements" })
    public static Lang[] ahccRunePride;

    @CN({ "[AHCC] 阿斯加德符文", "神域符文: 预留效果" })
    @EN({ "[AHCC] Asgard Rune", "Reserved effect" })
    public static Lang[] ahccRuneAsgard;

    @CN({ "[AHCC] 华纳海姆符文", "获得(水元素*地元素*2)的热量上限", "水元素和地元素之和达到以下数量时:", "10+: 额外x2", "20+: 额外x2", "35+: 额外x2" })
    @EN({ "[AHCC] Vanaheim Rune", "Max heat +(Water x Earth x2)",
            "If (Water+Earth)>=10/20/35: extra x2/x2/x2 stacked" })
    public static Lang[] ahccRuneVanaheim;

    @CN({ "[AHCC] 亚尔夫海姆符文", "精灵国度符文: 预留效果" })
    @EN({ "[AHCC] Alfheim Rune", "Reserved effect" })
    public static Lang[] ahccRuneAlfheim;

    @CN({ "[AHCC] 米德加德符文", "人类世界符文: 预留效果" })
    @EN({ "[AHCC] Midgard Rune", "Reserved effect" })
    public static Lang[] ahccRuneMidgard;

    @CN({ "[AHCC] 约顿海姆符文", "巨人国度符文: 预留效果" })
    @EN({ "[AHCC] Jotunheim Rune", "Reserved effect" })
    public static Lang[] ahccRuneJotunheim;

    @CN({ "[AHCC] 穆斯贝尔海姆符文", "1) 自身长十字范围（整行整列）所有注术单元产热+10%", "2) 若火元素占比最多：", " - 所有带火元素的注术单元产热+10%",
            " - 自身长十字范围注术单元获得额外档位加成（取最高档）：", "10+火元素: +10%", "20+火元素: +30%", "25+火元素: +50%",
            "3) 触发占比效果时自身占用5稳定度，否则占用2稳定度", "4) 额外档位（福袋）本轮最多触发一次" })
    @EN({ "[AHCC] Muspelheim Rune", "Long-cross infusion cells: Heat x1.1",
            "All fire-element infusion cells: extra stability use +1",
            "If fire is dominant: terminal (fire infusion cells x1.1 + long-cross extra tier once per cycle)",
            "Self use: +5 on terminal, else +2" })
    public static Lang[] ahccRuneMuspelheim;

    @CN({ "[AHCC] 尼福尔海姆符文", "雾与寒冰符文: 预留效果" })
    @EN({ "[AHCC] Niflheim Rune", "Reserved effect" })
    public static Lang[] ahccRuneNiflheim;

    @CN({ "[AHCC] 尼达维勒符文", "元素: 每有1个尼达维勒符文, 则额外+5地元素", "地元素占比最多且热量<50%上限时触发终端(本轮一次)", "地符文和水符文之和达到以下数量时:",
            "0+: 产热x1.1, 占用-1", "20+: 产热x1.2, 占用-1", "50+: 产热x1.3, 占用-1", "75+: 产热x1.5, 占用-2",
            "自身占用: 默认+2稳定度, 触发终端+5稳定度" })
    @EN({ "[AHCC] Nidavellir Rune", "Element: +5 Earth per rune",
            "Terminal triggers once per cycle when Earth is dominant and heat<50% max",
            "All fuels: heat x1.1/1.2/1.3/1.5, use -1/-1/-1/-2 (thresholds by Earth+Water runes)",
            "Self use: +2 default, +5 on terminal" })
    public static Lang[] ahccRuneNidavellir;

    @CN({ "[AHCC] 赫尔海姆符文", "冥界终端符文: 预留效果" })
    @EN({ "[AHCC] Helheim Rune", "Reserved effect" })
    public static Lang[] ahccRuneHelheim;

    public static void init(RegistrateCNLangProvider provider) {
        // 预留: 若后续需要手动 provider.add 键值，可在此扩展。
    }
}
