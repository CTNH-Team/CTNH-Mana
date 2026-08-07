package com.moguang.ctnhmana.common.parts.ManaHatches;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

/**
 * 创造模式魔力凝聚仓：无限魔力，并始终额外持有全部恶魔意志各 {@link #CREATIVE_WILL}。
 */
public class CreativeManaHatch extends BloodManaHatch {

    private static final long CREATIVE_MANA = 1_000_000L;
    /** 各类型意志恒定储量 */
    public static final double CREATIVE_WILL = 666_666_666D;

    public CreativeManaHatch(IMachineBlockEntity holder) {
        // maxDemonWill 设为意志恒定值，避免从世界/宝石继续吸取
        super(holder, CREATIVE_MANA, Long.MAX_VALUE / 4, 1, Integer.MAX_VALUE / 4,
                (int) CREATIVE_WILL, 1.0D);
        refillMana();
        refillWills();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        refillMana();
        refillWills();
    }

    @Override
    public void ConvertMana() {
        // 创造仓不走常规转化，每 tick 回满魔力与意志
        refillMana();
        refillWills();
    }

    @Override
    public void consumeMana(long consume) {}

    @Override
    public boolean consumeManaIfEnough(long consume) {
        return true;
    }

    /**
     * 创造模式下意志消耗始终成功，并在调用后回满。
     */
    @Override
    public boolean ConsumeWillIfEnough(String type, double num) {
        refillWills();
        return true;
    }

    private void refillMana() {
        Mana = maxMana;
    }

    /** 始终额外持有全部类型意志各 CREATIVE_WILL */
    private void refillWills() {
        maxDemonWill = (int) CREATIVE_WILL;
        rawWill = CREATIVE_WILL;
        steadfastWill = CREATIVE_WILL;
        corrosiveWill = CREATIVE_WILL;
        destructiveWill = CREATIVE_WILL;
        vengefulWill = CREATIVE_WILL;
    }
}
