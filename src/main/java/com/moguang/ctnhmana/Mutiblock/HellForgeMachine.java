package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatches.BloodManaHatch;
import com.moguang.ctnhmana.item.ManaMachineUpgrade.BMUpgradeItemT1;
import net.minecraft.core.BlockPos;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

public class HellForgeMachine extends BaseManaMachine {
    public double will = 0;
    public static final String WILL = "will";
    @Persisted
    public boolean consumeLock=false;
    @Persisted
    public BlockPos hatchPos;
    public BloodManaHatch hatch;
    public HellForgeMachine(IMachineBlockEntity holder){
        super(holder,4);
        machineStorage.setFilter(itemStack -> itemStack.getItem() instanceof BMUpgradeItemT1);
    }
    @Override
    public void afterWorking() {
        super.afterWorking();
        consumeLock=false;
    }
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch(); //获取舱室
        if (this.hatch == null) onStructureInvalid(); //获取不到就别成型
        var tier = getTier();//获取等级
    }
    public BloodManaHatch getHatch() {
        for (IMultiPart part : this.getParts()) {
            if (part instanceof BloodManaHatch hatchs) {
                hatchPos = (hatchs).getPos();
                return hatchs;
            }
        }
        return null;
    }
    @CN(
            {
                    "§4以灵魂为薪，齿轮为模，铸造扭曲之形§r",
                    "§c只能使用§4血魔法凝聚仓§r和§4血魔法升级§r",
                    "运行时每秒消耗4魔力能量，电压每提升一级，消耗量便翻倍",
                    "直接从§4血魔法凝聚仓§r吸收恶魔意志以执行配方"
            }
    )
    @EN(
            {
                    "§4以灵魂为薪，齿轮为模，铸造扭曲之形§r",
                    "§c只能使用§4血魔法凝聚仓§r和§4血魔法升级§r",
                    "运行时每秒消耗4魔力能量，电压每提升一级，消耗量便翻倍",
                    "直接从血§4魔法凝聚仓§r吸收恶魔意志以执行配方"
            }
    )
    public static Lang[] hellforgeLang;

}
