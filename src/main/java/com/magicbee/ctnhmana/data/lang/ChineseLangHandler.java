package com.magicbee.ctnhmana.data.lang;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.registrate.lang.RegistrateCNLangProvider;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.magicbee.ctnhmana.registry.CMMachines.DIGITAL_WELL_OF_SUFFER;
import static com.magicbee.ctnhmana.registry.multiblock.Misc.*;

public class ChineseLangHandler {

    // 机器tooltips
    @CN({
            "用于魔力机器的魔力能量输入，支持输入：植物魔法魔力，灵魂网络的生命源质与液态魔力",
            "植物魔法的魔力可以通过在物品槽位放入魔力戒指或使用魔力发射器进行传输",
            "通过在物品槽位放入宝珠来完成与宝珠所有者的灵魂网络的绑定并自动从灵魂网络抽取生命源质",
            "直接从外部输入（不支持手动输入）来往舱室输入液态魔力",
            "最大的魔力能量存储：%d",
            "最大的生命植物魔法魔力存储：%d",
            "最大的液态魔力存储：%d mB"
    })
    @EN({
            "用于魔力机器的魔力能量输入，支持输入：植物魔法魔力，灵魂网络的生命源质与液态魔力",
            "植物魔法的魔力可以通过在物品槽位放入魔力戒指或使用魔力发射器进行传输",
            "通过在物品槽位放入宝珠来完成与宝珠所有者的灵魂网络的绑定并自动从灵魂网络抽取生命源质",
            "直接从外部输入（不支持手动输入）来往舱室注入液态魔力",
            "最大的魔力能量存储：%d",
            "最大的生命植物魔法魔力存储：%d",
            "最大的液态魔力存储：%d mB"
    })
    public static Lang[] manahatchtootip_base;

    @CN({
            "用于血法师的魔力机器的魔力能量输入，支持输入：灵魂网络的生命源质量，液态生命源质",
            "不支持凝聚魔力能量，植物魔法魔力",
            "通过在物品槽位放入宝珠来完成与宝珠所有者的灵魂网络的绑定并自动从灵魂网络抽取生命源质",
            "直接从外部输入（不支持手动输入）来往舱室注入液态生命源质",
            "每秒自动从当前区块尽可能地吸收各种恶魔意志",
            "最大的恶魔意志存储量：%d",
            "最大的魔力能量存储量：%d",
            "最大的液态魔力生命源质：%d mB"
    })
    @EN({
            "用于血法师的魔力机器的魔力能量输入，支持输入：灵魂网络的生命源质量，液态生命源质",
            "不支持凝聚魔力能量，植物魔法魔力",
            "通过在物品槽位放入宝珠来完成与宝珠所有者的灵魂网络的绑定并自动从灵魂网络抽取生命源质",
            "直接从外部输入（不支持手动输入）来往舱室注入液态生命源质",
            "每秒自动从当前区块尽可能地吸收各种恶魔意志",
            "最大的恶魔意志存储量：%d",
            "最大的魔力能量存储：%d",
            "最大的液态魔力生命源质：%d mB"
    })
    public static Lang[] bloodmanahatchtootip_base;
    @CN({
            "用于魔力机器的魔力能量输入，支持输入：植物魔法魔力与液态魔力",
            "不支持输入生命源质",
            "植物魔法的魔力可以通过在物品槽位放入魔力戒指或使用魔力发射器进行传输",
            "每秒从半径为8的火花绑定的魔力存储中抽取15000植物魔法魔力，每10秒重新检测一次周围的火花",
            "直接从外部输入（不支持手动输入）来往舱室输入液态魔力",
            "最大的魔力能量存储：%d",
            "最大的生命植物魔法魔力存储：%d",
            "最大的液态魔力存储：%d mB"
    })
    @EN({
            "用于魔力机器的魔力能量输入，支持输入：植物魔法魔力与液态魔力",
            "不支持输入生命源质",
            "植物魔法的魔力可以通过在物品槽位放入魔力戒指或使用魔力发射器进行传输",
            "每秒从半径为8范围内的每一个火花绑定的魔力存储中抽取%d植物魔法魔力，每10秒重新检测一次周围的火花",
            "直接从外部输入（不支持手动输入）来往舱室注入液态魔力",
            "最大的魔力能量存储：%d",
            "最大的生命植物魔法魔力存储：%d",
            "最大的液态魔力存储：%d mB"
    })
    public static Lang[] sparkmanahatchtootip_base;
    @CN("基础魔力凝聚者")
    @EN("Basic Mana Condenser")

    public static Lang manahatchtooltip_1;
    @CN("按住CTRL来显示具体数据")
    @EN("Hold CTRL to show detailed data")
    public static Lang manahatchctrltooltip_1;
    @CN({
            "详细转化数据:",
            "凝聚魔力能量转化比例:%s:1",
            "植物魔法魔力转化比率:%s:1",
            "戒指魔力转移效率:%s",
            "网路LP转化比率:%s:1",
            "液态LP转化比率:%s:1",
            "液态LP转化效率:每tick之多%s 当前容量",
            "§c不支持%s"
    })
    @EN({
            "详细转化数据:",
            "凝聚魔力能量转化比例:%s:1",
            "植物魔法魔力转化比率:%s:1",
            "戒指魔力转移效率:%s",
            "网路LP转化比率:%s:1",
            "液态LP转化比率:%s:1",
            "液态LP转化效率:每tick之多%s 当前容量",
            "§c不支持%s"
    })
    public static Lang[] manaHatchDataLang;
    @CN("§c凝血魔力凝聚者")
    @EN("§cCoagulated-Blood Mana Condenser")

    public static Lang bloodmanahatchtooltip_1;

    @CN({
            "§aCTNH产魔花",
            "§6工业时代的炼金术师"
    })
    @EN({
            "§aCTNH产魔花",
            "§6工业时代的炼金术师"
    })
    public static Lang[] blackVeinFlowerLang;
    @CN({
            "§aCTNH产魔花",
            "§c永远高贵，永远不衰"
    })
    @EN({
            "§aCTNH产魔花",
            "§c永远高贵，永远不衰"
    })
    public static Lang[] tulpenmanieFlowerLang;
    @CN({
            "§aCTNH产魔花",
            "§c大爆炸"
    })
    @EN({
            "§aCTNH产魔花",
            "§c大爆炸"
    })
    public static Lang[] genethistleLang;
    @CN("§7泡沫过后，一无所有")
    @EN("§7After the foam, nothing remains")

    public static Lang bubleFlowerLang;
    @CN({
            "§aCTNH产魔花?",
            "§7アナタが夢を忘れて ワタシは此処にいる"
    })
    @EN({
            "§aCTNH产魔花?",
            "§7アナタが夢を忘れて ワタシは此処にいる"
    })
    public static Lang[] ParaRosiaLang;
    @CN({
            "§cCTNH产魔花",
            "§c焚我执相，三千大世界"
    })
    @EN({
            "§cCTNH产魔花",
            "§7アナタが夢を忘れて ワタシは此処にいる"
    })
    public static Lang[] burnMeLotusLang;
    @CN({
            "§1CTNH功能花",
            "§1你将收获永世的折磨"
    })
    @EN({
            "§1CTNH功能花",
            "§1你将收获永世的折磨"
    })
    public static Lang[] flyTrapLang;
    @CN({
            "§6完美§r地控制每一个中央槽位",
            "该机器用于精确控制§n多方块结构主机器UI§r的存储槽位",
            "机器的电路编号用于指定该机器控制的槽位编号(从0开始)",
            "支持从机器的任意面输入物品，并且会将此物品插入指定槽位(如果存在)",
            "§n机器的主面§r接收§c红石信号脉冲§r时，将该槽位的物品弹出到§n具有输出功能的§r舱室中",
            "§o准备好成为自动化大师了吗？§r"
    })
    @EN({
            "§6完美§r地控制每一个中央槽位",
            "该机器用于精确控制§n多方块结构主机器UI§r的存储槽位",
            "机器的电路编号用于指定该机器控制的槽位编号(从0)开始",
            "支持从机器的任意面输入物品，并且会将此物品插入指定槽位(如果存在)",
            "§n机器的主面§r接收§c红石信号脉冲§r时，将该槽位的物品弹出到§n具有输出功能的§r舱室中",
            "§o准备好成为自动化大师了吗？§r"
    })
    public static Lang[] centralControlBusLang;
    @CN({
            "从魔力池处吸收魔力",
            "当符文仪式材料准备就绪后，每秒消耗自己所有的魔力缓存并且为符文仪式累计进度",
            "当需要的魔力累计足够时，自动执行符文仪式"
    })
    @EN({
            "从魔力池处吸收魔力",
            "当符文仪式材料准备就绪后，每秒消耗自己所有的魔力缓存并且为符文仪式累计进度",
            "当需要的魔力累计足够时，自动执行符文仪式"
    })
    public static Lang[] runeAltarFowerLang;

    @CN({
            "该符文具有以下元素类型:",
            "火元素",
            "水元素",
            "风元素",
            "地元素",
            "罪孽元素"
    })
    @EN({
            "该符文具有以下元素类型:",
            "火元素",
            "水元素",
            "风元素",
            "地元素",
            "罪孽元素"
    })
    public static Lang[] runeElementTags;
    @CN("%d级符文")
    @EN("Tier %d Rune")
    public static Lang runeTierTags;

    public static void init(RegistrateCNLangProvider provider) {
        AHCCRuneLang.init(provider);
        provider.add("ctnhmana.extended_centralcontrol_bus.channel_toggle.enabled", "启用该通道");
        provider.add("ctnhmana.extended_centralcontrol_bus.channel_toggle.disabled", "禁用该通道");
        provider.add("ctnh.item.runes.starlight_rune", "Per Aspera Ad Astra");
        provider.add("ctnh.item.runes.twist_rune", "速度与人性的扭曲");
        provider.add("ctnh.item.runes.proliferation_rune", "金融与生物的本能");
        provider.add("ctnh.item.runes.quasar_rune", "毁灭与创造交替");
        provider.add("ctnh.item.runes.horizen_rune", "视野所向之处");
        provider.add("ritual.ctnhmana.ritualextractor", "生灵萃取仪式");
        provider.add("ritual.ctnhmana.chargerRitual", "充能仪式");
        provider.add("ritual.ctnhmana.ritualbosssummon", "战争呼唤仪式");
        provider.add("ritual.ctnhmana.dragon_cloudritual", "龙吟仪式");
        provider.add("ritual.ctnhmana.dragon_shroudsight", "虚境之视");
        provider.add("ctnhmana.recipe.blood_ritual.ritual_id", "仪式：%s");
        provider.add("ctnhmana.recipe.blood_ritual.lp_cost", "消耗 LP：%s");
        provider.add("ctnhmana.recipe.meteor_ritual.lp_cost", "消耗 LP：%s");
        provider.add("ctnhmana.recipe.meteor_ritual.marker_tip", "标定物需以掉落物形式落在控制器 21 格范围内");
        // Blood Magic 仪式中文名（配方 tip / JEI）
        provider.add("ritual.bloodmagic.waterRitual", "满春之仪");
        provider.add("ritual.bloodmagic.speedRitual", "速度之仪");
        provider.add("ritual.bloodmagic.animalGrowthRitual", "牧者之仪");
        provider.add("ritual.bloodmagic.armourEvolveRitual", "活性进化之仪");
        provider.add("ritual.bloodmagic.condorRitual", "神鹰敬拜");
        provider.add("ritual.bloodmagic.craftingRitual", "锻砧律动");
        provider.add("ritual.bloodmagic.crushingRitual", "粉碎之仪");
        provider.add("ritual.bloodmagic.crystalHarvestRitual", "裂晶仪式");
        provider.add("ritual.bloodmagic.crystalSplitRitual", "晶体共鸣仪式");
        provider.add("ritual.bloodmagic.ellipseRitual", "椭球聚焦");
        provider.add("ritual.bloodmagic.featheredKnifeRitual", "羽刃之仪");
        provider.add("ritual.bloodmagic.fellingRitual", "砍伐仪式");
        provider.add("ritual.bloodmagic.forsakenSoulRitual", "弃魂聚集");
        provider.add("ritual.bloodmagic.fullStomachRitual", "饱足之仪");
        provider.add("ritual.bloodmagic.geode", "晶洞丰饶之仪");
        provider.add("ritual.bloodmagic.greenGroveRitual", "翠谷之仪");
        provider.add("ritual.bloodmagic.groundingRitual", "罪人之负");
        provider.add("ritual.bloodmagic.jumpRitual", "跳跃之仪");
        provider.add("ritual.bloodmagic.lavaRitual", "下界小夜曲");
        provider.add("ritual.bloodmagic.downgradeRitual", "铅魂忏悔");
        provider.add("ritual.bloodmagic.magneticRitual", "磁力之仪");
        provider.add("ritual.bloodmagic.meteorRitual", "坠星位标");
        provider.add("ritual.bloodmagic.placerRitual", "填充领域");
        provider.add("ritual.bloodmagic.regenerationRitual", "再生之仪");
        provider.add("ritual.bloodmagic.sphereRitual", "新月破晓");
        provider.add("ritual.bloodmagic.upgradeRemoveRitual", "净化之魂");
        provider.add("ritual.bloodmagic.wellOfSufferingRitual", "苦难之井");
        provider.add("ritual.bloodmagic.yawningVoidRitual", "虚空哈欠");
        provider.add("ritual.bloodmagic.zephyrRitual", "西风召唤");
        provider.add("effect.ctnhmana.shroud_gaze", "虚境的凝视");
        provider.add("effect.ctnhmana.helian_blessing", "赫利安的祝福");
        provider.add("effect.ctnhmana.karma", "§1业");
        provider.add("effect.ctnhmana.karma_fortuna", "§1业[福尔图娜]");
        provider.add("effect.ctnhmana.blade_unleashed", "§1剑刃解放");
        provider.add("effect.ctnhmana.index_target", "§1指令对象");
        provider.add("effect.ctnhmana.soul_leech", "灵魂汲取");
        provider.add("effect.ctnhmana.tainted_blood", "污血");
        provider.add("effect.ctnhmana.physical_antagonism", "物理拮抗");
        provider.add("effect.ctnhmana.magical_antagonism", "奥法拮抗");
        provider.add("effect.ctnhmana.pain_shield", "苦难护盾");
        provider.add("ctnh.boss_summoner.use", "右键长按蓄力掷出，在落点处召唤一只神化boss，每次使用有五分之一的概率消耗");
        provider.add("ctnhmana.jade.manahatch.manaprogress", "魔力能量：%s / %s");
        provider.add("ctnhmana.jade.manahatch.btmanaprogress", "植物魔法魔力量：%s / %s");
        provider.add("ctnhmana.jade.manahatch.bmmanaprogress", "灵魂网络LP量：%s / %s");
        provider.add("ctnhmana.jade.manahatch.rawwillprogress", "§9普通意志：%s / %s");
        provider.add("ctnhmana.jade.manahatch.steadfastwillprogress", "§5坚韧意志：%s / %s");
        provider.add("ctnhmana.jade.manahatch.corrosivewillprogress", "§a侵蚀意志：%s / %s");
        provider.add("ctnhmana.jade.manahatch.destructivewillprogress", "§6破坏意志：%s / %s");
        provider.add("ctnhmana.jade.manahatch.vengefulwillprogress", "§c复仇意志：%s / %s");
        provider.add("ctnhmana.jade.terra_plate.manaprogress", "魔力输入进度：%s / %s (%.2f%%)");
        provider.add("ctnhmana.jade.bloodaltar.lp", "LP存储：%d / %d");
        provider.add("ctnhmana.jade.eternal_wos.will_output", "§9本轮注魂产出：%s 普通意志");
        provider.add("config.jade.plugin_gtceu.manahatch_status_provider", "魔力舱室属性");
        provider.add("config.jade.plugin_gtceu.manamachine_status_provider", "魔力机器属性");
        provider.add("config.jade.plugin_gtceu.bloodaltar_status_provider", "工业血祭坛属性");
        provider.add("config.jade.plugin_gtceu.manamachine_mana_status_provider", "魔力机器魔力属性");
        provider.add("config.jade.plugin_gtceu.eternal_wos_status_provider", "永恒苦难之井注魂");
        provider.add("config.jade.plugin_gtceu.gem_sublimator_status_provider", "宝石刻格机进度");
        provider.add("config.jade.plugin_ctnhmana.mana_pool_status", "魔力池属性");
        // provider.add("ctnh.recipe_type.info", "配方类型：%s");
        provider.add("ctnh.recipe_type.list", "%s, %s");

        provider.add(TWISTED_FUSION_MK1.getBlock(), "扭曲聚变反应堆mk1");
        provider.add(TWISTED_FUSION_MK2.getBlock(), "扭曲聚变反应堆mk2");
        provider.add(TWISTED_FUSION_MK3.getBlock(), "扭曲聚变反应堆mk3");
        provider.add(TWISTED_FUSION_MKINFINITY.getBlock(), "扭曲聚变反应堆mk∞");
        provider.add(TWISTED_FUSION_MKALEPHNULL.getBlock(), "扭曲聚变反应恒星ℵ₀");

        provider.add(DIGITAL_WELL_OF_SUFFER[LV].getBlock(), "基础数字化苦难之井");
        provider.add(DIGITAL_WELL_OF_SUFFER[MV].getBlock(), "§b进阶数字化苦难之井§r");
        provider.add(DIGITAL_WELL_OF_SUFFER[HV].getBlock(), "§6进阶数字化苦难之井 II§r");
        provider.add(DIGITAL_WELL_OF_SUFFER[EV].getBlock(), "§5进阶数字化苦难之井 III§r");
        provider.add(DIGITAL_WELL_OF_SUFFER[IV].getBlock(), "§9精英数字化苦难之井§r");
        provider.add(DIGITAL_WELL_OF_SUFFER[LuV].getBlock(), "§d精英数字化苦难之井 II§r");
        provider.add(DIGITAL_WELL_OF_SUFFER[ZPM].getBlock(), "§c数字化猩红深渊§r");
        provider.add(DIGITAL_WELL_OF_SUFFER[UV].getBlock(), "§3数字化猩红深渊 II§r");

        provider.add("ctnh.common_tooltip.mana_machine.0", "魔法，神奇吧");
        provider.add("ctnh.common_tooltip.mana_machine.2", "运行中的每一并行提供1%时间和耗能减免，至多减少75%");
        provider.add("ctnh.common_tooltip.mana_machine.3", "§4运行同电压等级配方时，使配方时间增加50%（魔力组装只增加1%）");
        provider.add("ctnh.common_tooltip.mana_machine.4",
                "放入§5类星体符文§r以在100次配方内启用§5星体之眼模式§r：并行变为无限，并行不再提供额外的时间与电压减少。启动此模式不消耗类星体符文");

        provider.add("ctnh.common_tooltip.mana_generator.0", "机器的基础最大发电量为(配方发电量)*符文提供倍率*转子最大转速*转子发电效率/100*机器自身提供发电量倍率");
        provider.add("ctnh.common_tooltip.mana_generator.1", "机器的实际发电量为(机器当前转子转速/转子最大转子)^2*机器最大发电量");
        provider.add("ctnh.common_tooltip.mana_generator.2",
                "§c注意：运行时需要额外消耗液态魔力，如果消耗液态魔力不足，则本次发电配方发电量将会除以5，通过机器UI可以获得魔力消耗量");
        provider.add("ctnh.common_tooltip.mana_generator.3", "在机器内放入符文可以提升发电效率：\n" +
                "  一级符文：发电量x1.5，魔力消耗量x0.8，每5秒符文消耗概率：0.2\n" +
                "  二级符文：发电量x2.4，魔力消耗量x1.2，每5秒符文消耗概率：0.1\n" +
                "  三级符文：发电量x3，魔力消耗量x0.8，每5秒符文消耗概率：0.05\n" +
                "  四级符文：发电量x4，魔力消耗量x0.6，每5秒符文消耗概率：0.025\n" +
                "  五级符文：发电量x5，魔力消耗量x0.3，每5秒符文消耗概率：0.02\n" +
                "  §5类星体符文§r: 发电量*999, 消耗量*999，§c在被吞噬的星辰中绽放最终的光芒§r");
        provider.add("ctnh.common_tooltip.basic_mana_consume", "每秒基础消耗4mB液态魔力，电压每超过§7LV§r一级，消耗量变为原来的两倍");
        provider.add("ctnh.common_tooltip.advanced_mana_consume", "每秒基础消耗10mB液态魔力，电压每超过§7LV§r一级，消耗量变为原来的两倍");
        provider.add("ctnh.common_tooltip.super_mana_consume", "每秒基础消耗12mB液态魔力，电压每超过§7LV§r一级，消耗量变为原来的两倍");

        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.overload", "§c警告：机器过载！！！");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.overload_1", "§c机器过载度：%d/%d");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.crash", "§c机器已损坏");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.mana", "当前魔力量：%.4fM");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.twist_consumption", "扭曲符文消耗概率：%.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.starlight_consumption", "星光符文消耗概率：%.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.overload_2", "§c！！！警告：能量溢出！！！");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.max_mana", "魔力上限：%.4fM");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.mana_required", "魔力需求：%.2fM");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.mana_consumption", "消耗魔力：%.2fM");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.time", "运行时间倍率：%.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.eut_consumption", "消耗能源倍率：%.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.stable", "魔力稳定值：%.2f");

        provider.add("ctnh.eternalgarden.info.fire", "当前烧煤花温度：%.1f");
        provider.add("ctnh.eternalgarden.info.eat", "当前彼方兰营养值：%d");

        provider.add("ctnh.multiblock.hellforge.info.will", "意志：%s");

        provider.add("ctnh.common_tooltip.zenith_machine.0", "§5超越魔法");
        provider.add("ctnh.common_tooltip.zenith_machine.1",
                "在达到LuV电压后，如果§5天顶源质§r足够，每次运行会消耗(60*(当前电压等级-6))的天顶源质，获得2^(当前电压等级-6)的并行数，最大并行数取决于当前电压。但是不输入天顶源质会损失4并行数。");
        provider.add("ctnh.common_tooltip.zenith_machine.2",
                "注意，源质的消耗与当前你输入的物品数无关，即使没有并行，我也会克扣你的天顶源质，当并行达到上限后仍然会消耗天顶源质，但是消耗量固定为60");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.0", "简易魔力转换器");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.1", "转子支架等级不能超过§bMV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.2", "运行基础产能小于64的配方时，产生双倍发电");
        provider.add("ctnh.mutiblock.mana_generator_turbine.base", "运行时会根据能量产出额外消耗液态魔力，请确保液态魔力供应充足，在液态魔力供应不足时最终产能/5");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.0", "进阶魔力转换器");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.1", "转子支架等级不能超过§5EV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.2", "运行时消耗2.25倍燃料，获得4倍的发电量");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.3", "运行基础产能小于64的配方时，产生双倍发电");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.0", "精密魔力转换器");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.1", "转子支架等级不能超过§dLuV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.2", "运行时消耗4倍燃料，获得16倍的发电量");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.3", "最终发电量额外乘以 (1.2^(基础产能/32))");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.0", "神奇的能量守恒");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.1", "转子支架等级不能超过§cUV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.2", "运行时消耗4倍燃料，获得64倍的发电量");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.3", "最终发电量额外乘以 (1.5^(基础产能/32))");

        provider.add("ctnh.multiblock.industrial_altar.tooltip.0", "§4血魔法，就在你家门口！");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.1",
                "与血祭坛相同，该结构有输入LP上限，同时你§4必须通过特定配方来增加其lp§r，详见JEI以查询增加的配方");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.2", "电压每超过HV一级，就增加10000可存储LP上限，达到LuV后每级额外增加30000");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.3",
                "每一个增容符文增加2500LP上限，强化增容符文增加5000，达到LuV后每级额外增加1000000/2000000LP上限");

        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.0", "§8撒旦一觉醒来发现自己掉到榜二了§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.1", "享受生灵痛苦的嘶吼吧。§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.2", "配方时间始终固定在1s。提高电压等级会提高产出生命源质的产出，等效于无损超频。§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.3", "使用残缺的数据模型不会产出任何东西，模型等级越高，产出越多");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.4", "§b注魂模式：§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.5",
                "与下方§b工业狱火锻炉§r连接后，可通过按钮开启注魂模式：机器不生产生命源质，而是为锻炉提供意志。");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.6", "两台机器需要共用岩浆池，且控制器必须位于狱火锻炉的正上方。请查阅JEI以获得更多信息。");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.7", "产出生命源质量（mB）/1000000的意志。注魂模式下并行无视输出仓容量。");

        provider.add("ctnh.multiblock.hellforge.tooltip.0", "§8机器也会有灵魂吗？§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.1", "运行狱火锻炉的配方，需要满足配方的最小意志条件。§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.2", "如何向机器内填充意志：§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.3", "1. 在控制器附近用§b感知之剑§r杀死一只浸泡于§c生命源质§r的怪物。获得基于怪物最大生命值的意志。");
        provider.add("ctnh.multiblock.hellforge.tooltip.4", "§8到控制器的曼哈顿距离小于8即可，不用非得是中间的血杯§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.5", "2. 在控制器附近丢出一块魂石。机器会自动吸取其中的意志。");
        provider.add("ctnh.multiblock.hellforge.tooltip.6", "3. 使用§4永恒苦难之井§r。请查阅对应机器的tooltip§r");

        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.0", "§8以不可思议的伟力。§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.1", "§e核聚变反应堆模式：§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.2", "不需要启动电量，不限制仓室等级，进行4/4超频。提供取决于配方启动电量的并行：");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.3", "小于160MEU：16+16*反应堆等级并行");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.4", "大于160MEU，小于320MEU：4+4*聚变反应堆等级并行");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.5", "大于320MEU，小于480MEU：1+聚变反应堆等级并行");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.6", "§5扭曲聚变反应堆模式：§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.7", "遵循字母守恒定律的反应。");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.8", "或许可以用来生产一些§9奇怪的东西§r...");

        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.0", "§9魔枢巨星，重构万物尺度");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.1", "允许使用并行控制仓，§c其不会为配方提供并行§r，只修改每秒输入魔力量");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.2", "插入机器的几种§9五级符文§r决定了该机器的各种能力");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.3", "§9星光符文§r的能量降低了能源消耗并增强了机器稳定性");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.4", "§c扭曲符文§r的能量降低了所用时间并增大了机器魔力注入频率，§c但会让机器更加不稳定");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.5", "§d视域符文§r的能量极大增大了魔力上限和魔力使用效率");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.6",
                "§5类星体符文§r的能量太过强大，它会直接让机器进入§c不稳定状态，但是同样使配方的消耗，产出，电压需求翻10倍");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.7", "§c扭曲§r与§9星空§r的对抗决定了机器的稳定性");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.8",
                "稳定性公式:-((twist_power /3)+((mana/100000)*(Math.max(twist_power/9,1))))+starlight_power*4+5+tier,当稳定性低于0时机器会开始过载！");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.9", "§c过载度§r每秒提升1且在机器拥有过载度时下§c配方时间会翻1倍§r");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.10", "在不处于过载状态下每3秒减少1过载度，§c过载度积累满时机器将会爆炸§r");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.11",
                "§c扭曲符文§r消耗概率公式:每次运行有Math.max((twist_power-3)/3,1)*0.01+(Math.max(starlight_power-twist_power,0)*0.01)+(Math.max((100-mana/100000)*0.0005,0))概率消耗");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.12",
                "§9星光符文§r消耗概率公式:每次运行有Math.max((starlight_power-3)/3,1)*0.01+(Math.max(twist_power-starlight_power,0)*0.01)+(mana/100000*0.005)概率消耗");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.13", "§d视域符文§r消耗概率公式：每次运行有0.0025*(horizen_power)概率消耗");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.14", "该机器无法超频");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.15",
                "运行时每运行1秒，固定消耗100*并行Kmb(B)液态魔力来为光束注能，可以使用只消耗冷却液的非需求魔力配方来为机器注能");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.16",
                "注意：如果你在输入魔力时超过了魔力上限，则超过上限的魔力不会被返还。如果你的魔力量超过了上限，则运行时不会减少多出于魔力上限的魔力");

        provider.add("ctnh.gtceu.tooltip.tfmkalephzero.1", "§4不，这不可能，你到底是怎么做出来这个东西的？§r");
        provider.add("ctnh.gtceu.tooltip.tfmkalephzero.2", "§c你到底是无聊到什么地步能凑到这么多材料和算力，只为了这点并行和速度？§r");
        provider.add("ctnh.gtceu.tooltip.tfmkalephzero.3", "§8好吧，我不管你是开创还是怎么回事的，你确实和你的AE征服我了§r");
        provider.add("ctnh.gtceu.tooltip.tfmkalephzero.4", "§7并行数为2147483637，配方时间乘以0.0001，无损超频，满意了吧？§r");

        provider.add("ctnh.multiblock.eternalgarden.tooltip.1", "§b万物在这世间绽放的永恒的一瞥§r");
        provider.add("ctnh.multiblock.eternalgarden.tooltip.broadcast", "允许使用§c§l红石信号广播仓§r，自动化，耶！");
        provider.add("ctnh.multiblock.eternalgarden.tooltip.2", "无法超频，但是机器自身电压每比配方电压高一级就使最终产出乘以1.1");
        provider.add("ctnh.multiblock.eternalgarden.tooltip.3", "每朵花都有自己独特的机制，机制太复杂了！请参阅§5魔力飞升§r章节来获取各种花的机制");
        provider.add("ctnh.multiblock.eternalgarden.tooltip.unknown", "§5......等待着永恒的紫罗兰如今在何方？");

        provider.add("zenith_extruder", "配方类型：压膜机/§5天顶灵压塑形");
        provider.add("zenith_extruder.1", "允许使用§5天顶灵压塑形§r，其以每个形态1mB§5天顶源质§5r的代价来一次性塑造大部分锭的各种形态");
        provider.add("zenith_extruder.2", "允许塑形的形态包括：§7板，杆，小型齿轮，齿轮，转子，环，螺栓，§4不允许塑形南瓜派！");

        provider.add("ctnh.multiblock.mana_condenser.tooltips.0", "这个机器在本版本已经暂时移除！！未来将会重做");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.1", "可以将魔力转化为液态魔力，或者将液态魔力转化为魔力，后者所需的能量更多");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.2", "所有魔力输入输出均通过结构中心的魔力池进行");

        provider.add("ritual.ctnh.chargerRitual", "充能仪式");
        provider.add("ctnh.terminal.multiblockhelper.tips",
                "第一次右键选择第一个方块，第二次右键选择第二个方块，使用shift+右键启用多方块构建\nshift+右键后将清除原坐标\n选择的方块请按照：底部西北角出发，前往顶部东南角来选择不然无法输出完整结构");

        provider.add("ctnh.anti_inf_matter.1", "-∞");
        provider.add("ctnh.anti_inf_matter.2", "它到底是怎么在现实世界存在的......");

        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.0", "§8无穷无尽的扭曲之力§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.1", "可以使用激光仓");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.2", "对所有配方都有§8无法理喻§r的并行数，所有配方能耗和运行时间减少75%");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.3", "§5想制作这台机器的你疯的不轻，当然这台机器也同样疯狂至极§r");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.4", "只能使用激光仓");

        provider.add("ctnh.multiblock.zenith_laser.tooltip.0", "允许使用§5反相蚀刻§r，消耗§5天顶源质§r来将芯片制成晶圆");

        provider.add("ctnh.multiblock.zenith_circuit_assember.tooltip.0", "允许使用§5魔力共振电路组装§r，以更低电压和特殊材料组装共振电路");

        provider.add("ctnh.multiblock.super_ebf.tooltip.0", "所有配方耗时减半");

        provider.add("botania.tagline.thirdEye", "看清所有的一切");

        provider.add("ctnhmana.botania.page.satori_eye1",
                "§9第三只眼§r同样允许你看清所有的§b魔力接受者（魔力池等）§r等的具体魔力信息$(p)§9第三只眼§r也同样能看穿敌人的破绽，在时§9必定§r造成1.5倍的暴击伤害息$(p)§c作为代价§r，所有的中立生物都将§c主动与你为敌§r");
        provider.add("ctnhmana.botania.page.satori_eye2", "§9第三只眼§r也同样能看穿敌人的破绽，在时§9必定§r造成1.5倍的暴击伤害");
        provider.add("ctnhmana.botania.page.satori_eye3", "§c作为代价§r，所有的中立生物都将§c主动与你为敌§r");
        provider.add("ctnhmana.botania.page.satori_eye4", "§7哇，你已经找到姐姐了，你很快也会找到§f恋恋§r§7了吧!§r");
        provider.add("ctnhmana.botania.page.satori_eye5", "§o§5终将一切看穿，终被一切厌恶§r");
        provider.add("ctnhmana.entry.satori_eye", "第三只眼");
        provider.add("ctnhmana.entry.koishi", "§m小恋§r紧闭的第三只眼");
        provider.add("ctnhmana.botania.page.koishi_eye1",
                "哇，你真的找到§m小恋§r了，§m小恋§r要把她的秘密分享给你!$(p) §m小恋§r的眼睛有很多作用!首先，佩戴时所有生物只有在§b与你贴贴§r的时候才会发现你!并且完~全不用担心尴尬，因为§b过一段时间§r所有人就会§b完全忘记你§r了！当然，小恋§4可没有姐姐的读心能力哦§r");
        provider.add("ctnhmana.botania.page.koishi_eye3", "§o§5终被一切厌弃，终将一切遗忘§r");
        provider.add("botania.tagline.koishi", "不用看清一切也行哦？");
        provider.add("ctnhmana.category.horizon", "植物魔法：新视野");
        provider.add("ctnhmana.category.horizon.description",
                "作为一名优秀的格雷员工，你当然知晓如何合理地将魔法与科技结合获得最大化效益，本条目介绍了§m植物魔法§r机械动力：新视野的一些魔力新增内容");

        provider.add("ctnhmana.entry.mystic_spire", "奥法尖塔");
        provider.add("ctnhmana.spire.title.binding", "绑定");
        provider.add("ctnhmana.spire.title.mode", "模式");
        provider.add("ctnhmana.spire.title.update", "升级");

        provider.add("ctnhmana.spire.page.1",
                "奥法尖塔是你在击败盖亚之后就可以制作的高级结构,它会生成§b德尔塔火花§r,火花的高级特殊变种$(p) 奥法尖塔可以代替传统的发射器和火花,为各种魔力容器(包括魔力凝聚仓)供应mana$(br)对于具体的工作逻辑,请参考条目：德尔塔火花");
        provider.add("ctnhmana.spire.page.2",
                "奥法尖塔在结构成型时会生成一个§b德尔塔火花§r,同时与其完成绑定,奥法尖塔结构失效时§b德尔塔火花§r随之消失,但数据会保留在尖塔主方块中$(p),通过切换尖塔/火花的模式来决定魔力尖塔的工作模式,§b德尔塔火花§r可以绑定另一个§b德尔塔火花§r,无视模式向该火花传递魔力,该效果独立于传递速度运算且无视模式限制");
        provider.add("ctnhmana.spire.page.3",
                "奥法尖塔拥有多种模式,你可以在机器主方块UI进行切换,或者对奥法尖塔/德尔塔火花使用处于工作模式下的§b电子火花之杖§r来切换模式$(p)奥法尖塔目前具有四个模式：§4聚焦,§c火花扩散,§b凝聚扩散,§6中转");
        provider.add("ctnhmana.spire.page.4",
                "奥法尖塔的各项属性决定了德尔塔火花的性能：$(p)速度：决定了火花传递魔力的速度,初始值10000/tick$(p) 接收速度：决定了火花主动吸取魔力的速度,注意火花吸收产能花的初始吸收速率只有接收速度的1/10,初始值：10000/tick$(p)范围：决定了火花的影响范围,初始值：15");
        provider.add("ctnhmana.spire.page.5",
                "奥法尖塔的模式决定了其工作逻辑：$(p)§4聚焦§r：奥法尖塔不输出魔力,而自动接受周围同色火花或者产魔花的魔力$(p)§c火花扩散§r：奥法尖塔将魔力输出到周围同色火花和火花升级魔力凝聚仓中$(p)§b凝聚扩散§r：尖塔仅向范围内的魔力凝聚仓广播魔力$(p)§6中转§r：奥法尖塔不执行任何操作,只实现奥法尖塔间的魔力传递");

        provider.add("ctnhmana.spire.page.6",
                "奥法尖塔间可以使用§4电子火花魔杖§r来进行绑定$(p)在§4电子火花魔杖§r的绑定模式下,第一次右键delta火花将选定要绑定的,目标火花,第二次将完成绑定,让奥法尖塔往目标火花传递mana。$(p)使用shift+右键点击delta火花将清除绑定,请注意:允许奥法尖塔间成环 传递");

        provider.add("ctnhmana.spire.page.7",
                "奥法尖塔自带四个升级槽位,可以自由安装升级来增加奥法尖塔的各项属性,不过需要注意以下:$(p)转位升级对吸收产魔法效率的提升只取最大值$(p)奥法尖塔的最大范围为50,最大容量为INT,最大传输速度不能超过最大容量的1/10");

        provider.add("ctnhmana.entry.mana_upgrades", "魔力升级");
        provider.add("ctnhmana.mana_upgrades.bt",
                "§b植物魔法系升级§r：使用植物魔法魔力来强化机器,较为基础的升级");
        provider.add("ctnhmana.mana_upgrades.bm",
                "§4血域系升级§r：使用LP与意志来获得强力强化的升级。");
        provider.add("ctnhmana.mana_upgrades.gt",
                "§9工业系升级§r：适用于巨额材料处理的大规模流线升级。");
        provider.add("ctnhmana.mana_upgrades.index.title", "升级检索");
        provider.add("ctnhmana.mana_upgrades.index",
                "以下页面按图标逐个列出全部魔力升级,便于在手册中快速检索与对照。");
        provider.add("ctnhmana.mana_upgrades.icon.clear_sky_flower_wish", "澄空之花愿");
        provider.add("ctnhmana.mana_upgrades.icon.azure_sky_flower_dance", "苍穹之花舞");
        provider.add("ctnhmana.mana_upgrades.icon.twisted_blood_forging", "曲血之锻造");
        provider.add("ctnhmana.mana_upgrades.icon.twisted_soul_forging", "曲魂之锻造");
        provider.add("ctnhmana.mana_upgrades.icon.pipeline_vision", "流水线的视野");
        provider.add("ctnhmana.mana_upgrades.icon.pipeline_farsight", "流水线的远视");

        provider.add("ctnhmana.entry.tulpenmanie", "永远的奥古斯都");
        provider.add("ctnhmana.tulpenmanie.1",
                "魔力beeeee疯了！它竟然做出来了这么一朵超模的花！$(p)永远的奥古斯都每tick会自动吸收周围魔力池100mana，而它每存储1000mana,每tick就可以自然产生1mana！$(p)最恐怖的是,这朵花有着100W的魔力缓存,魔生魔,太疯狂了!");
        provider.add("ctnh.copyright.magic.info", "§bCTNH：工业魔力学");

        provider.add("ctnhmana.entry.blackveinmarigold", "黑脉金盏花");
        provider.add("ctnhmana.blackveinmarigold.1",
                "  黑脉金盏花可以将周围 格雷桶 中的石油资源转化为魔力。每次会在5格范围内消耗1000mB流体，然后在20秒内持续产出魔力。在产出时不会再吸收流体");
        provider.add("ctnhmana.blackveinmarigold.2",
                "  可吸收的燃料类型与内燃发电机一致，燃料热值越高，产魔越多。累计产出到一定程度后，还会把相邻石头转化为金块。");

        provider.add("ctnhmana.entry.anatta_lotus", "焚我莲");
        provider.add("ctnhmana.anatta_lotus.lexicon.1",
                "作为火红莲的进阶版,焚我莲会一次性燃烧3格半径内所有的燃料并按照(总燃烧tick/30)来计算总产魔量,随后§c点燃§r所有燃料所处的方块");
        provider.add("ctnhmana.anatta_lotus.lexicon.2",
                "非常重要的是,焚我莲有着生命的循环周期,在它被种下时,在3000tick后将开始点燃所有燃料,然后休息300tick$(p)300tick后,焚我莲将会死亡,在原地浴火重生并且点燃周围地块$(p)请务必确保你的产魔环境足够安全$(p)另外注意,SL(save and load)行为会收到业报的惩罚,破坏它的轮回周期,务必注意");
        provider.add("ctnhmana.entry.genethistle", "创世蓟");
        provider.add("ctnhmana.genethistle.lexicon.1",
                "创世蓟有着非常强大的力量,它会在白天,黑夜,雨天,晴天,雷暴天,雪天,高空,周围有水方块,周围有岩浆方块时自动产魔,这些产魔条件会相互叠加$(p)需要注意:在666秒后,创世蓟将死亡并引发一场不会破坏方块的大爆炸,随后在周围生成一朵新的创世蓟,它不会生成在自身1个半径范围内,如果没有任何点位可以生成它,那么它就会直接死亡");
        provider.add("ctnhmana.entry.pararosia", "迷心蔷薇");
        provider.add("ctnhmana.pararosia.lexicon.1",
                "多少次试着理解$(p)却总是捉摸不透$(p)你的心去了何方$(p)§c如果能与你的距离更近一点的话§r$(p)我想要触碰你的那颗心$(p)在平方绽放的(11)心中");
        provider.add("ctnhmana.pararosia.lexicon.2",
                "用那双眼睛你看到了什么？$(p)无论是谁，我也不想交出来$(p)如果相信你的话$(p)那么将你的欲望暴露给我看吧$(p)在平方隔绝的石(514)中");
        provider.add("ctnhmana.pararosia.lexicon.3",
                "那双眼睛歌颂的市羁绊之物,§c成双成对§r的才是背德的模拟$(p)就让那恋心随之绽放吧，尽管这只是一场仿造的舞台$(p)如果与之相关，那么会不会绽放出更美丽的花呢？");

        provider.add("ctnhmana.entry.demon_flytrap", "恶魔捕蝇草");
        provider.add("ctnhmana.demon_flytrap.lexicon.1",
                "恶魔捕蝇草可以捕捉怪物,每4tick消耗666mana对周围怪物造成4点伤害,并施加灵魂汲取效果,灵魂汲取将会持续对怪物造成伤害,并且怪物死亡时将就地释放最大生命值/20的普通意志.$(p)恶魔捕蝇草击杀怪物时,同样会释放最大生命值/20的普通意志,两者可以相互叠加");
        provider.add("ctnhmana.entry.blood_antiaris", "见血封喉");
        provider.add("ctnhmana.blood_antiaris.lexicon.1", "");
        provider.add("ctnhmana.blood_antiaris.lexicon.2", "");

        provider.add("ctnhmana.copyright.info", "由CTNHMana增加");

        provider.add("key.category.ctnhmana.general", "CTNHMana");
        provider.add("key.ctnhmana.open_caduceus", "打开神谕终端菜单");
    }
    // 物品tooltips

    @CN({
            "佩戴时获得：",
            "显示§b魔力池§r，§b魔力花§r和§b魔力凝聚板§r的详细信息",
            "看穿所有敌人的伪装",
            "攻击时必定抓住敌人的破绽",
            "被所有生物所憎恶，§4§l这就是看穿真实的代价",
            "§5§o以自己的幸福，换来可以看见真实的眼睛，这就是身为§r§4§n觉§r§5§o之妖怪的被厌恶者的觉悟"
    })
    @EN({
            "佩戴时获得：",
            "显示魔力池，魔力花和魔力凝聚板的详细信息",
            "看穿所有敌人的伪装",
            "攻击时必定抓住敌人的破绽",
            "被所有生物所憎恶，§4§l这就是看穿真实的代价",
            "§5§o以自己的幸福，换来可以看见真实的眼睛，这就是身为§r§n觉§r§5§o之妖怪的被厌恶者的觉悟"
    })
    public static Lang[] satori_thirdeye_tooltip;
    @CN({
            "§5于超越现实的视线之中",
            "要求结构中必须且仅有一个魔力凝聚仓",
            "通过机器内升级槽放入升级",
            "每秒消耗%d 魔力能量，电压每有一级，消耗的魔力能量就翻7倍",
            "最终运行速度+77%,任何时候都有32并行"
    })
    @EN({
            "§5于超越现实的视线之中",
            "要求结构中必须且仅有一个魔力凝聚仓",
            "通过机器内升级槽放入升级",
            "每秒消耗%d 魔力能量，电压每比IV高一级，消耗的魔力能量就翻7倍",
            "最终运行速度+77%,任何时候都有32并行"
    })
    public static Lang[] basezenithmutiblockLang;
    @CN("§5灵能之力使得该机器无视蒸馏塔的舱室规则")
    @EN("§5Psychic power lets this machine ignore Distillery casing rules")

    public static Lang zenithDistillationLang;
    @CN("§5具有特殊的天域组合配方类型,允许制作天顶SOC和价格极为低廉的魔力电路")
    @EN("§5Special zenith assembly recipes: Zenith SOC and very cheap mana circuits")

    public static Lang zenithAssemblerLang;
    @CN("§5具有特殊的反相蚀刻配方类型,允许你将芯片蚀刻成晶圆")
    @EN("§5Special inverse-etching recipes: turn chips into wafers")

    public static Lang zenithLaserLang;
    @CN({
            "§4然而，轰鸣机器的齿轮中流淌的鲜血，也只是另一场名为'工业化'的血祭",
            "§4工业血祭坛§r是特殊的祭坛结构，其拥有多个等级，其等级决定了可以运行的配方，在结构成型时，将自动绑定结构中的血祭坛",
            "根据自身的等级，增强§c所有§r血祭坛符文的效果，并直接扩大血祭容量",
            "§c清除血祭坛的缓存容量，也不会使用血祭坛的缓存LP",
            "具有与血祭坛类似的供血机制，每秒从输入总线抽取一定数量的LP注入血祭坛，该注入受到等级，符文的影响",
            "运行时，直接消耗血祭坛中的LP来进行工作。LP不足时，§c进度会停滞直到LP足够",
            "§c不可进行任何电力超频§r，自身工作速度受到血祭坛工作速度影响，同时拥有特殊的血超频机制",
            "运行时，祭坛血等级每高配方等级一级，速度翻倍，LP消耗速率翻四倍",
            "具有批处理功能：当配方时间<1s时，机器会尝试批处理直到运行时间≥1s",
            "§c注意：即使批处理不足，实际运行时间不会低于1s§r",
            "按住CTRL来显示具体数据"
    })
    @EN({
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
    })
    public static Lang[] industrialAltarLang;
    @CN({
            "工业血祭坛数据详细：",
            "增强符文效果：",
            "血祭坛扩大(工业血祭坛等级-1)倍容量",
            "§b速度符文§r：额外提升(0.05)*(工业血祭坛等级-2)的速度",
            "§e增容符文§r：提供的容量乘以1.5^(工业血祭坛等级-1)",
            "§4超容符文§r：提供的指数容量基数提升0.025*(工业血祭坛等级-1）",
            "§1转位符文§r：指数基数提升(0.05*工业血祭坛等级)",
            "效率符文：视作额外计算(工业血祭坛等级-1)个效率符文",
            "基础供血速率：20lp*(2^工业血祭坛等级)/s 该速率受到转位符文，促速符文的影响",

    })
    @EN({
            "工业血祭坛数据详细：",
            "增强符文效果：",
            "血祭坛扩大(工业血祭坛等级-1)倍容量",
            "§b速度符文§r：额外提升(0.05)*(工业血祭坛等级-2)的速度",
            "§e增容符文§r：提供的容量乘以1.5^(工业血祭坛等级-1)",
            "§4超容符文§r：提供的指数容量基数提升0.025*(工业血祭坛等级-1）",
            "§1转位符文§r：指数基数提升(0.05*工业血祭坛等级)",
            "效率符文：视作额外计算(工业血祭坛等级-1)个效率符文",
            "基础供血速率：20lp*(2^工业血祭坛等级)/s 该速率受到转位符文，促速符文的影响"
    })
    public static Lang[] industrialAltarctrlLang;
}
