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
        provider.addItem(CMItems.FLOWER_UPDATE,"§9繁花之核心");
    }
    //物品tooltips
    @CN(
            {
                    "偏向于植物魔法与液态魔力的升级",
                    "机器增益：",
                    "魔力凝聚仓每存有5W魔力，每存有20W魔力存储上限，就获得一点并行（最高16)",
                    "运行时的每一并行提供1%机器增速（最高10%）",
                    "魔力凝聚仓存有的每10W魔力提供1%机器增速（最高25%）",
            }
    )
    @EN(
            {
                    "偏向于植物魔法与液态魔力的升级",
                    "机器增益：",
                    "魔力凝聚仓每存有5W魔力，每存有20W魔力存储上限，就获得一点并行（最高16)",
                    "运行时的每一并行提供1%机器增速（最高10%）",
                    "魔力凝聚仓存有的每10W魔力提供1%机器增速（最高25%）",
            }
    )
    public static Lang[] botaniacoreLang;






}
