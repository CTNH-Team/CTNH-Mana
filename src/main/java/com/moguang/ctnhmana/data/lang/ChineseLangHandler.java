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
                    "直接从外部输入液态魔力（不支持手动输入）来往舱室输入液态魔力",
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
                    "直接从外部输入液态魔力（不支持手动输入）来往舱室输入液态魔力",
                    "最大的魔力能量存储：%d",
                    "最大的生命植物魔法魔力存储：%d",
                    "最大的液态魔力存储：%d mb"
            }
    )
    public static  Lang[]  manahatchtootip_base;
    @CN("基础魔力凝聚者")
    public static Lang manahatchtooltip_1;



    public static void init(RegistrateCNLangProvider provider){
        provider.addItem(CMItems.HORIZEN_RUNE,"§5视域§r符文");
        provider.addItem(CMItems.STARLIGHT_RUNE,"§9星光§r符文");
        provider.addItem(CMItems.TWIST_RUNE,"§c扭曲§r符文");
        provider.addItem(CMItems.QUASAR_RUNE,"§k类星体§r符文");
        provider.addItem(CMItems.PROLIFERATION_RUNE,"§a增殖§r符文");
        provider.addItem(CMItems.FLOWER_UPDATE,"§9繁花与星空之祝福");
        provider.addItem(CMItems.GT_UPDATE,"§o流水线之视野");
        provider.add("ritual.ctnhmana.ritualextractor","生灵萃取仪式");
        provider.add("ritual.ctnhmana.chargerRitual","充能仪式");
        provider.add("ritual.ctnhmana.ritualbosssummon","战争呼唤仪式");
        provider.add("ritual.ctnhmana.dragon_cloudritual","龙吟仪式");
        provider.add("ritual.ctnhmana.dragon_shroudsight","虚境之视");
        provider.add("effect.ctnhmana.shroud_gaze","虚境的凝视");
        provider.add("ctnh.boss_summoner.use", "右键长按蓄力掷出，在落点处召唤一只神化boss，每次使用有五分之一的概率消耗");
    }
    //物品tooltips
    @CN(
            {
                    "偏向于植物魔法与魔力的升级",
                    "机器获得：",
                    "魔力凝聚仓每存有5W魔力，每存有20W魔力存储上限，就获得一点并行（最高16)",
                    "运行时的每一并行提供§a+1%§r机器工作速度（最高10%）",
                    "魔力凝聚仓存有的每10W魔力提供§a+1%§r机器工作速度（最高25%）",
                    "§o§9与花朵共舞吧，即使是机器也无法遮蔽群星的美丽§r"
            }
    )
    @EN(
            {
                    "偏向于植物魔法与魔力的升级",
                    "机器获得：",
                    "魔力凝聚仓每存有5W魔力，每存有20W魔力存储上限，就获得一点并行（最高16)",
                    "运行时的每一并行提供§a+1%§r机器工作速度（最高10%）",
                    "魔力凝聚仓存有的每10W魔力提供§a+1%§r机器工作速度（最高25%）",
                    "§o§9与花朵共舞吧，即使是机器也无法遮蔽群星的美丽§r"
            }
    )
    public static Lang[] botaniacoreLang;
    @CN(
            {
                    "偏向于大规模工业流水线的升级",
                    "机器获得：",
                    "任何时候都具有64并行",
                    "运行时的每一并行提供§a+1%§r机器工作速度（最高40%）",
                    "魔力凝聚仓存有§n物品§r时，§c-10%§r机器工作速度",
                    "魔力凝聚仓存有超过10W魔力时，§c-10%§r机器工作速度",
                    "§o§5真正至臻完美的流水线不应该容许任何非常态输入，只有gt流体和超级并行才是格雷员工的标配§r"
            }
    )
    @EN(
            {
                    "偏向于大规模工业流水线的升级",
                    "机器获得：",
                    "任何时候都具有64并行",
                    "运行时的每一并行提供§a+1%§r机器工作速度（最高40%）",
                    "魔力凝聚仓存有§n物品§r时，§c-10%§r机器工作速度",
                    "魔力凝聚仓存有超过10W魔力时，§c-10%§r机器工作速度",
                    "§o§5真正至臻完美的流水线不应该容许任何非常态输入，只有gt流体和超级并行才是格雷员工的标配§r"
            }
    )
    public static Lang[] gtcoreLang;


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
