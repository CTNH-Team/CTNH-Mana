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
        provider.add("config.jade.plugin_gtceu.manahatch_status_provider","魔力舱室属性");
    }
    //物品tooltips




    @CN(
            {
                    "魔法之力！",
                    "要求结构中必须且仅有1个魔力凝聚仓",
                    "通过机器内升级槽放入升级",
                    "每秒消耗%d魔力能量，电压每有1级，消耗的魔力能量就翻倍",
                    "运行电压与当前电压的相同配方具有§c-25%§r运行速度",
                    "无损超频（暂时）！"
            }
    )
    @EN(
            {
                    "魔法之力！",
                    "要求结构中必须且仅有1个魔力凝聚仓",
                    "通过机器内升级槽放入升级",
                    "每秒消耗%d魔力能量，电压每有1级，消耗的魔力能量就翻倍",
                    "运行电压与当前电压的相同配方具有§c-25%§r运行速度",
                    "无损超频（暂时）！"
            }
    )
    public static Lang[] basemanamutiblockLang;






}
