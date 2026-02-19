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
import static com.moguang.ctnhmana.registry.CMMachines.DIGITAL_WELL_OF_SUFFER;
import static com.moguang.ctnhmana.registry.multiblock.misc.*;


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
    @CN("神谕终端[赫尔墨斯]")
    public static Lang CaduceusLang;
    @CN({
            "应该用大剑将躯干劈开时:",
            "应该用矿镐将骨骼凿碎时:",
            "应该用铁铲将皮肉铲削时:",
            "应该用战斧将肢体斩断时:",
            "应该用耕锄将筋脉割裂时:",
            "应该用镰刀将脖颈勾断时:",
            "应该用锯子将骨肉锯开时:",
            "应该用扳手将关节拧折时:",
            "应该用锉刀将皮肉磨烂时:",
            "应该用螺丝刀将颅骨撬开时:",
            "应该用剪线钳将筋腱剪断时:",
            "应该用小刀将动脉划开时:",
            "应该用马桶搋子将面门砸瘪时:"
    })
    @EN({
            "应该用大剑将躯干劈开时",
            "应该用矿镐将骨骼凿碎时",
            "应该用铁铲将皮肉铲削时",
            "应该用战斧将肢体斩断时",
            "应该用耕锄将筋脉割裂时",
            "应该用镰刀将脖颈勾断时",
            "应该用锯子将骨肉锯开时",
            "应该用扳手将关节拧折时",
            "应该用锉刀将皮肉磨烂时",
            "应该用螺丝刀将颅骨撬开时",
            "应该用剪线钳将筋腱剪断时",
            "应该用小刀将动脉划开时",
            "应该用马桶搋子将面门砸瘪时"
    })
//
    public static Lang[] CaduceusWeaponLang;
    @CN(
            {
                    "由通过电波传达指令的都市之神赫尔墨斯所赐的最强的武器，同时也是诅咒",
                    "终端内部有着一种类似金属的液体，使用与奇点相同的技术，可以改变其形状与质量。",
                    "这种液体会随着终端所吸收到的指令改变其外观，根据对象的情况与限制而变换成合适的武器。",
                    "参与合成时被视为§6所有§r工具，可以破坏§6所有§r常规方块"
            }
    )
    @EN(
            {
                    "由通过电波传达指令的都市之神赫尔墨斯所赐的最强的武器，同时也是诅咒",
                    "终端内部有着一种类似金属的液体，使用与奇点相同的技术，可以改变其形状与质量。",
                    "这种液体会随着终端所吸收到的指令改变其外观，根据暗杀对象的情况与限制而变换成合适的武器。",
                    "参与合成时被视为§6所有§r工具，可以破坏§6所有§r常规方块"
            }
    )
    public static Lang[] CaduceusTooltipNormalLang;
    @CN(
            {
                    "+§8%d§r攻击力",
                    "+§8%d§r攻击速度"
            }
    )
    @EN(
            {
                    "+%d攻击力",
                    "+%d攻击速度"
            }
    )
    public static Lang[] CaduceusDamageLang;
    @CN(
            {
                    "根据自身§1§n剑刃解放§r层数造成更多伤害",
                    "对§1§n指令目标§r造成更多伤害",
                    "命中时获得§4力量§r，根据自身§1§n剑刃解放§r层数或者命中指令目标获得额外§4力量§r"
            }
    )
    @EN(
            {
                    "根据自身剑刃解放层数造成更多伤害",
                    "对指令目标造成更多伤害",
                    "命中时获得力量，根据自身剑刃解放层数或者命中指令目标获得额外力量"
            }
    )
    public static Lang[] CaduceusStatusLang;
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

    @CN("蚀刻强化")
    @EN("Etching Upgrade")
    public static Lang etching_jade_upgrade;
    @CN(
            {
                    "放置在血祭坛中时：",
                    "使工业血祭坛可以执行额外的配方",
            }
    )
    @EN(  {
            "放置在血祭坛中时：",
            "使工业血祭坛可以执行额外的配方",
    })
    public static Lang[] etchingJadeLang;
    @CN(
            {
                    "放置在血祭坛中时：",
                    "使工业血祭坛可以执行额外的配方",
                    "减少一半的LP消耗"
            }
    )
    @EN(  {
            "放置在血祭坛中时：",
            "使工业血祭坛可以执行额外的配方",
            "减少一半的LP消耗"
    })
    public static Lang[] suppressionJadeLang;
    @CN(
            {
                    "放置在血祭坛中时：",
                    "使工业血祭坛可以执行额外的配方",
                    "将工业血祭坛的超频改为无损超频"
            }
    )
    @EN(  {
            "放置在血祭坛中时：",
            "使工业血祭坛可以执行额外的配方",
            "将工业血祭坛的超频改为无损超频"
    })
    public static Lang[] ephemeralJadeLang;
    @CN("抑液强化")
    @EN("Suppression Upgrade")
    public static Lang suppression_jade_upgrade;
    @CN("须臾强化")
    @EN("Ephemeral Upgrade")
    public static Lang ephemeral_jade_upgrade;

    public static void init(RegistrateCNLangProvider provider){
        provider.add("ctnh.item.runes.starlight_rune","Per Aspera Ad Astra");
        provider.add("ctnh.item.runes.twist_rune","速度与人性的扭曲");
        provider.add("ctnh.item.runes.proliferation_rune","金融与生物的本能");
        provider.add("ctnh.item.runes.quasar_rune","毁灭与创造交替");
        provider.add("ctnh.item.runes.horizen_rune","视野所向之处");
        provider.add("ritual.ctnhmana.ritualextractor","生灵萃取仪式");
        provider.add("ritual.ctnhmana.chargerRitual","充能仪式");
        provider.add("ritual.ctnhmana.ritualbosssummon","战争呼唤仪式");
        provider.add("ritual.ctnhmana.dragon_cloudritual","龙吟仪式");
        provider.add("ritual.ctnhmana.dragon_shroudsight","虚境之视");
        provider.add("effect.ctnhmana.shroud_gaze","虚境的凝视");
        provider.add("effect.ctnhmana.helian_blessing","赫利安的祝福");
        provider.add("effect.ctnhmana.karma","§1业");
        provider.add("effect.ctnhmana.karma_fortuna","§1业[福尔图娜]");
        provider.add("effect.ctnhmana.blade_unleashed","§1剑刃解放");
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
        provider.add("ctnhmana.jade.bloodaltar.lp","LP存储:%d / %d");
        provider.add("config.jade.plugin_gtceu.manahatch_status_provider","魔力舱室属性");
        provider.add("config.jade.plugin_gtceu.manamachine_status_provider","魔力机器属性");
        provider.add("config.jade.plugin_gtceu.bloodaltar_status_provider","工业血祭坛属性");
        provider.add("config.jade.plugin_ctnhmana.mana_pool_status","魔力池属性");
        //provider.add("ctnh.recipe_type.info", "配方类型：%s");
        provider.add("ctnh.recipe_type.list", "%s, %s");

        provider.add(TWISTED_FUSION_MK1.getBlock(),"扭曲聚变反应堆mk1");
        provider.add(TWISTED_FUSION_MK2.getBlock(),"扭曲聚变反应堆mk2");
        provider.add(TWISTED_FUSION_MK3.getBlock(),"扭曲聚变反应堆mk3");
        provider.add(TWISTED_FUSION_MKINFINITY.getBlock(),"扭曲聚变反应堆mk∞");
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
        provider.add("ctnh.common_tooltip.mana_machine.2","运行中的每一并行提供1%时间和耗能减免，至多减少75%");
        provider.add("ctnh.common_tooltip.mana_machine.3","§4运行同电压等级配方时，使配方时间增加50%（魔力组装只增加1%)");
        provider.add("ctnh.common_tooltip.mana_machine.4","放入§5类星体符文§r以在100次配方内启用§5星体之眼模式§r：并行变为无限，并行不再提供额外的时间与电压减少。启动此模式不消耗类星体符文");

        provider.add("ctnh.common_tooltip.mana_generator.0","机器的基础最大发电量为(配方发电量)*符文提供倍率*转子最大转速*转子发电效率/100*机器自身提供发电量倍率");
        provider.add("ctnh.common_tooltip.mana_generator.1","机器的实际发电量为(机器当前转子转速/转子最大转子)^2*机器最大发电量");
        provider.add("ctnh.common_tooltip.mana_generator.2","§c注意：运行时需要额外消耗液态魔力，如果消耗液态魔力不足，则本次发电配方发电量将会除以5，通过机器UI可以获得魔力消耗量");
        provider.add("ctnh.common_tooltip.mana_generator.3", "在机器内放入符文可以提升发电效率：\n" +
                "  一级符文：发电量x1.5，魔力消耗量x0.8，每5秒符文消耗概率：0.2\n" +
                "  二级符文：发电量x2。4，魔力消耗量x1.2，每5秒符文消耗概率：0.1\n" +
                "  三级符文：发电量x3，魔力消耗量x0.8，每5秒符文消耗概率：0.05\n" +
                "   四级符文：发电量x4， 魔力消耗量x0.6，每5秒符文消耗概率：0.025\n" +
                "  五级符文： 发电量x5, 魔力消耗量x0.3， 每5秒符文消耗概率：0.02\n" +
                "  §5类星体符文§r: 发电量*999, 消耗量*999，§c在被吞噬的星辰中绽放最终的光芒§r");
        provider.add("ctnh.common_tooltip.basic_mana_consume", "每秒基础消耗4mB液态魔力，电压每超过§7LV§r一级，消耗量变为原来的两倍");
        provider.add("ctnh.common_tooltip.advanced_mana_consume", "每秒基础消耗10mB液态魔力，电压每超过§7LV§r一级，消耗量变为原来的两倍");
        provider.add("ctnh.common_tooltip.super_mana_consume", "每秒基础消耗12mB液态魔力，电压每超过§7LV§r一级，消耗量变为原来的两倍");

        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.overload","§c警告：机器过载！！！");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.overload_1","§c机器过载度:%d/%d");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.crash","§c机器已损坏");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.mana","当前魔力量:%.4fM");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.twist_consumption","扭曲符文消耗概率:%.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.starlight_consumption","星光符文消耗概率:%.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.overload_2","§c！！！警告：能量溢出！！！");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.max_mana","魔力上限:%.4fM");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.mana_required","魔力需求:%.2fM");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.mana_consumption","消耗魔力:%.2fM");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.time","运行时间倍率:%.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.eut_consumption","消耗能源倍率:%.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.stable","魔力稳定值:%.2f");

        provider.add("ctnh.eternalgarden.info.fire","当前烧煤花温度:%.1f");
        provider.add("ctnh.eternalgarden.info.eat","当前彼方兰营养值:%d");

        provider.add("ctnh.multiblock.hellforge.info.will", "意志：%s");

        provider.add("ctnh.common_tooltip.zenith_machine.0","§5超越魔法");
        provider.add("ctnh.common_tooltip.zenith_machine.1","在达到LUV电压后，如果§5天顶源质§r足够，每次运行会消耗(60*(当前电压等级-6))的天顶源质，获得2^(当前电压等级-6)的并行数，最大并行数取决于当前电压。但是不输入天顶源质会损失4并行数。");
        provider.add("ctnh.common_tooltip.zenith_machine.2","注意，源质的消耗与当前你输入的物品数无关，即使没有并行，我也会克扣你的天顶源质，当并行达到上限后仍然会消耗天顶源质，但是消耗量固定为60");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.0", "简易魔力转换器");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.1", "转子支架等级不能超过§bMV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.2", "运行基础产能小于64的配方时，产生双倍发电");
        provider.add("ctnh.mutiblock.mana_generator_turbine.base","运行时会根据能量产出额外消耗液态魔力，请确保液态魔力供应充足，在液态魔力供应不足时最终产能/5");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.0", "进阶魔力转换器");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.1", "转子支架等级不能超过§5EV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.2","运行时消耗2.25倍燃料，获得4倍的发电量");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.3", "运行基础产能小于64的配方时，产生双倍发电");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.0", "精密魔力转换器");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.1", "转子支架等级不能超过§dLuV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.2","运行时消耗4倍燃料，获得16倍的发电量");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.3", "最终发电量额外乘以（1.2^(基础产能/32))");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.0", "神奇的能量守恒");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.1", "转子支架等级不能超过§cUV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.2","运行时消耗4倍燃料，获得64倍的发电量");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.3", "最终发电量额外乘以（1.5^(基础产能/32))");

        provider.add("ctnh.multiblock.industrial_altar.tooltip.0","§4血魔法，就在你家门口！");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.1","与血祭坛相同，该结构有输入LP上限，同时你§4必须通过特定配方来增加其lp§r\n详见JEI以查询增加的配方");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.2","电压每超过HV一级，就增加10000可存储LP上限，达到LUV后每级额外增加30000");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.3","每一个增容符文增加2500LP上限，强化增容符文增加5000,达到LUV后每级额外增加1000000/2000000LP上限");

        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.0", "§8撒旦一觉醒来发现自己掉到榜二了§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.1", "享受生灵痛苦的嘶吼吧。§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.2", "配方时间始终固定在1s。提高电压等级会提高产出生命源质的产出，等效于无损超频。§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.3", "使用残缺的数据模型不会产出任何东西，模型等级越高，产出越多");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.4", "§b灵魂模式：§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.5", "灵魂模式下，机器不生产生命源质，而是为下方的§b工业狱火锻炉§r提供意志。");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.6", "两台机器需要共用岩浆池，且控制器必须位于狱火锻炉的正上方。请查阅JEI以获得更多信息。");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.7", "产出生命源质量（mB）/100000的意志。");

        provider.add("ctnh.multiblock.hellforge.tooltip.0", "§8机器也会有灵魂吗？§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.1", "运行狱火锻炉的配方，需要满足配方的最小意志条件。§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.2", "如何向机器内填充意志：§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.3", "1.在控制器附近用§b感知之剑§r杀死一只浸泡于§c生命源质§r的怪物。获得基于怪物最大生命值的意志。");
        provider.add("ctnh.multiblock.hellforge.tooltip.4", "§8到控制器的曼哈顿距离小于8即可，不用非得是中间的血杯§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.5", "2.在控制器附近丢出一块魂石。机器会自动吸取其中的意志。");
        provider.add("ctnh.multiblock.hellforge.tooltip.6", "3.使用§4永恒苦难之井§r。请查阅对应机器的tooltip§r");

        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.0", "§8以不可思议的伟力。§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.1", "§e核聚变反应堆模式：§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.2", "不需要启动电量，不限制仓室等级，进行4/2超频。提供取决于配方启动电量的并行：");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.3", "小于160MEU：16+16*反应堆等级并行");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.4", "大于160MEU，小于320MEU：4+4*聚变反应堆等级并行");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.5", "大于320MEU，小于480MEU：1+聚变反应堆等级并行");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.6", "§5扭曲聚变反应堆模式：§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.7", "遵循字母守恒定律的反应。");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.8", "或许可以用来生产一些§9奇怪的东西§r...");

        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.0","§9魔枢巨星，重构万物尺度");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.1","允许使用并行控制仓，§c其不会为配方提供并行§r，只修改每秒输入魔力量");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.2","插入机器的几种§9五级符文§r决定了该机器的各种能力");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.3","§9星空符文§r的能量降低了能源消耗并增强了机器稳定性");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.4","§c扭曲符文§r的能量降低了所用时间并增大了机器魔力注入频率，§c但会让机器更加不稳定");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.5","§d视域符文§r的能量极大增大了魔力上限和魔力使用效率");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.6","§5类星体符文§r的能量太过强大，它会直接让机器进入§c不稳定状态，但是同样使配方的消耗，产出，电压需求翻10倍");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.7","§c扭曲§r与§9星空§r的对抗决定了机器的稳定性");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.8","稳定性公式:-((twist_power /3)+((mana/100000)*(Math.max(twist_power/9,1))))+starlight_power*4+5+tier,当稳定性低于0时机器会开始过载！");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.9","§c过载度§r每秒提升1且在机器拥有过载度时下§c配方时间会翻1倍§r");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.10","在不处于过载状态下每3秒减少1过载度，§c过载度积累满时机器将会爆炸§r");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.11","§c扭曲符文§r消耗概率公式:每次运行有Math.max((twist_power-3)/3,1)*0.01+(Math.max(starlight_power-twist_power,0)*0.01)+(Math.max((100-mana/100000)*0.0005,0))概率消耗");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.12","§9星空符文§r消耗概率公式:每次运行有Math.max((starlight_power-3)/3,1)*0.01+(Math.max(twist_power-starlight_power,0)*0.01)+(mana/100000*0.005)概率消耗");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.13","§d视域符文§r消耗概率公式：每次运行有0.0025*(horizen_power)概率消耗");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.14","该机器无法超频");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.15","运行时每运行1秒，固定消耗100*并行Kmb(B)液态魔力来为光束注能，可以使用只消耗冷却液的非需求魔力配方来为机器注能");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.16","注意：如果你在输入魔力时超过了魔力上限，则超过上限的魔力不会被返还。如果你的魔力量超过了上限，则运行时不会减少多出于魔力上限的魔力");

        provider.add("ctnh.gtceu.tooltip.tfmkalephzero.1","§4不，这不可能，你到底是怎么做出来这个东西的？§r");
        provider.add("ctnh.gtceu.tooltip.tfmkalephzero.2","§c你到底是无聊到什么地步能凑到这么多材料和算力，只为了这点并行和速度？§r");
        provider.add("ctnh.gtceu.tooltip.tfmkalephzero.3","§8好吧，我不管你是开创还是怎么回事的，你确实和你的AE征服我了§r");
        provider.add("ctnh.gtceu.tooltip.tfmkalephzero.4","§7并行数为2147483637，配方时间乘以0.0001，无损超频，满意了吧？§r");

        provider.add("ctnh.multiblock.eternalgarden.tooltip.1","§b万物在这世间绽放的永恒的一瞥§r");
        provider.add("ctnh.multiblock.eternalgarden.tooltip.broadcast","允许使用§c§l红石信号广播仓§r，自动化，耶！");
        provider.add("ctnh.multiblock.eternalgarden.tooltip.2","无法超频，但是机器自身电压每比配方电压高一级就使最终产出乘以1.1");
        provider.add("ctnh.multiblock.eternalgarden.tooltip.3","每朵花都有自己独特的机制，机制太复杂了！请参阅§5魔力飞升§r章节来获取各种花的机制");
        provider.add("ctnh.multiblock.eternalgarden.tooltip.unknown","§5......等待着永恒的紫罗兰如今在何方？");

        provider.add("zenith_extruder","配方类型：压膜机/§5天顶灵压塑形");
        provider.add("zenith_extruder.1","允许使用§5天顶灵压塑形§r，其以每个形态1mb§5天顶源质§5r的代价来一次性塑造大部分锭的各种形态");
        provider.add("zenith_extruder.2","允许塑形的形态包括：§7板，杆，小型齿轮，齿轮，转子，环，螺栓，§4不允许塑形南瓜派！");

        provider.add("ctnh.multiblock.mana_condenser.tooltips.0", "反熵物质转化！");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.1", "可以将魔力转化为液态魔力，或者将液态魔力转化为魔力，后者所需的能量更多");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.2", "所有魔力输入输出均通过结构中心的魔力池进行");

        provider.add("ritual.ctnh.chargerRitual", "充能仪式");
        provider.add("ctnh.terminal.multiblockhelper.tips","第一次右键选择第一个方块，第二次右键选择第二个方块，使用shift+右键启用多方块构建\nshift+右键后将清除原坐标\n选择的方块请按照：底部西北角出发，前往顶部东南角来选择不然无法输出完整结构");


        provider.add("ctnh.anti_inf_matter.1","-∞");
        provider.add("ctnh.anti_inf_matter.2","它到底是怎么在现实世界存在的......");

        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.0","§8无穷无尽的扭曲之力§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.1","可以使用激光仓");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.2","对所有配方都有§8无法理喻§r的并行数,所有配方能耗和运行时间减少75%");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.3","§5想制作这台机器的你疯的不轻，当然这台机器也同样疯狂至极§r");

        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.4","只能使用激光仓");

        provider.add("ctnh.multiblock.zenith_laser.tooltip.0","允许使用§5反相蚀刻§r，消耗§5天顶源质§r来将芯片制成晶圆");

        provider.add("ctnh.multiblock.zenith_circuit_assember.tooltip.0","允许使用§5魔力共振电路组装§r，以更低电压和特殊材料组装共振电路");

        provider.add("ctnh.multiblock.super_ebf.tooltip.0", "所有配方耗时减半");

        provider.add("botania.tagline.thirdEye","看清所有的一切");

        provider.add("ctnhmana.botania.page.satori_eye1","§9第三只眼§r同样允许你看清所有的§b魔力接受者（魔力池等）§r等的具体魔力信息$(p)§9第三只眼§r也同样能看穿敌人的破绽，在时§9必定§r造成1.5倍的暴击伤害息$(p)§c作为代价§r，所有的中立生物都将§c主动与你为敌§r");
        provider.add("ctnhmana.botania.page.satori_eye2","§9第三只眼§r也同样能看穿敌人的破绽，在时§9必定§r造成1.5倍的暴击伤害");
        provider.add("ctnhmana.botania.page.satori_eye3","§c作为代价§r，所有的中立生物都将§c主动与你为敌§r");
        provider.add("ctnhmana.botania.page.satori_eye4","§7哇，你已经找到姐姐了，你很快也会找到§f恋恋§r§7了吧!§r");
        provider.add("ctnhmana.botania.page.satori_eye5","§o§5终将一切看穿，终被一切厌恶§r");
        provider.add("ctnhmana.entry.satori_eye","第三只眼");
        provider.add("ctnhmana.entry.koishi","§m小恋§r紧闭的第三只眼");
        provider.add("ctnhmana.botania.page.koishi_eye1","哇，你真的找到§m小恋§r了，§m小恋§r要把她的秘密分享给你!$(p) §m小恋§r的眼睛有很多作用!首先，佩戴时所有生物只有在§b与你贴贴§r的时候才会发现你!并且完~全不用担心尴尬，因为§b过一段时间§r所有人就会§b完全忘记你§r了！当然，小恋§4可没有姐姐的读心能力哦§r");
        provider.add("ctnhmana.botania.page.koishi_eye3","§o§5终被一切厌弃，终将一切遗忘§r");
        provider.add("botania.tagline.koishi","不用看清一切也行哦？");
        provider.add("ctnhmana.category.horizon","植物魔法：新视野");
        provider.add("ctnhmana.category.horizon.description","作为一名优秀的格雷员工，你当然知晓如何合理地将魔法与科技结合获得最大化效益，本条目介绍了§m植物魔法§r机械动力:新视野的一些魔力新增内容");

        provider.add("ctnhmana.entry.mystic_spire","奥法尖塔");

        provider.add("ctnhmana.spire.page.1","奥法尖塔是你在击败盖亚之后就可以制作的高级结构，它会生成§b德尔塔火花§r，火花的高级特殊变种$(p) 奥法尖塔可以代替传统的发射器和火花，为各种魔力容器(包括魔力凝聚仓)供应mana$(br)对于具体的工作逻辑，请参考条目：德尔塔火花");
        provider.add("ctnhmana.spire.page.2","奥法尖塔在结构成型时会生成一个§b德尔塔火花§r，同时与其完成绑定，奥法尖塔结构失效时§b德尔塔火花§r随之消失，但数据会保留在尖塔主方块中$(p)，通过切换尖塔/火花的模式来决定魔力尖塔的工作模式，§b德尔塔火花§r可以绑定另一个§b德尔塔火花§r，无视模式向该火花传递魔力，该效果独立于传递速度运算且无视模式限制");
        provider.add("ctnhmana.spire.page.3","奥法尖塔拥有多种模式，你可以在机器主方块UI进行切换，或者对奥法尖塔/德尔塔火花使用处于工作模式下的§b电子火花之杖§r来切换模式.$(p)奥法尖塔目前具有四个模式：§4聚焦,§c火花扩散,§b广域扩散,§6中转");
        provider.add("ctnhmana.spire.page.4","奥法尖塔的各项属性决定了德尔塔火花的性能：$(p)速度：决定了火花传递魔力的速度 初始值50000/tick$(p) 接收速度：决定了火花主动吸取魔力的速度，注意火花吸收产能花的速度只有接收速度的1/10，初始值:5000/tick$(p)范围：决定了火花的影响范围，初始值:10");
        provider.add("ctnhmana.spire.page.5","奥法尖塔的模式决定了其工作逻辑：$(p)§4聚焦§r：奥法尖塔不输出魔力，而自动接受周围同色火花或者产魔花的魔力$(p)§c火花扩散§r：奥法尖塔将魔力输出到周围同色火花和火花升级魔力凝聚仓中$(p)§b广域扩散§r：火花无视一切限制规则，向周围所有的非德尔塔火花的魔力容器传递魔力$(p)§6中转§r:奥法尖塔不执行任何操作，只实现奥法尖塔间的魔力传递");

        provider.add("ctnh.copyright.magic.info","§bCTNH：工业魔力学");

        provider.add("ctnhmana.copyright.info","由CTNHMana增加");

        provider.add("key.category.ctnhmana.general","CTNHMana");
        provider.add("key.ctnhmana.open_caduceus","打开神谕终端菜单");


    }
    //物品tooltips



    @CN("§b将信息本身作为能量的非物质运算§r")
    @EN("§bNon-physical computation that takes information itself as energy§r")
    public static Lang umlhpic;
    @CN(
            {
                    "§b魔力逻辑UHV电路板§r",
                    "§b超越因果，在两世之间纠缠的有限运算§r"

            }
    )
    @EN({
            "§bMagic Logic UHV Circuit Board§r",
            "§bTranscending Karma, a Finite Computation Entwined Between Two Existences§r"
    })
    public static Lang[] magic_quantum_processor_mainframe;

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
    @CN("被高能魔力击碎的符文，其残余显现出了空间扭曲的性质")
    public static Lang brokenRuneLang;
    @CN("§a大地的力量凝聚于此")
    public static Lang terra_catalyst;
    @CN("宛如天上的繁星")
    public static Lang zenith_star;
    @CN("高能魔力蚀刻基板")
    public static Lang mana_circuit_board;
    @CN("调谐电力与魔力的核心")
    public static Lang magicCoreLang;
    @CN("永恒的十二面体")
    public static Lang flowerHeartLang;
    @CN("封藏着魔力的奥义")
    public static Lang encapsulated_twist_mana;
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
    @CN("魔力不足")
    public static Lang failureManaLang_NoEnoughMana;
    @CN("LP不足")
    public static Lang failureManaLang_NoEnoughLP;
    @CN("等级不足，至少需要扭曲等级:%d")
    public static Lang failureManaLang_NoEnoughTwistLevel;
    @CN("END COLOR :XD")
    public static Lang failureManaLang_BeamCrash;





}