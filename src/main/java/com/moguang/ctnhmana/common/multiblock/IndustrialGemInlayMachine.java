package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

/**
 * Gem inlay machine controller. Automates Apotheosis Gem Cutting Table rarity upgrades.
 */
public class IndustrialGemInlayMachine extends ManaMachine {

    @CN({
            "自动化执行神话宝石切割台配方",
            "消耗两颗同种同稀有度宝石、宝石粉与珍宝材料，升级稀有度",
            "结构中需包含原版宝石切割台与1个魔力凝聚仓",
            "材料可用同级/高一级/低一级，消耗分别为3/1/9"
    })
    @EN({
            "Automates Apotheosis Gem Cutting Table recipes",
            "Consumes two identical gems, gem dust, and rarity materials to upgrade rarity",
            "Structure requires the vanilla Gem Cutting Table and exactly one Mana Condenser hatch",
            "Materials: same/next/prev tier cost 3/1/9"
    })
    public static Lang[] industrialGemInlayLang;

    public IndustrialGemInlayMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch();
        if (this.hatch == null) {
            onStructureInvalid();
        }
    }
}
