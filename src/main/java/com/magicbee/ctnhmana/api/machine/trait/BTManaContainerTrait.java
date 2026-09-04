package com.magicbee.ctnhmana.api.machine.trait;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import lombok.Getter;
import lombok.Setter;
import vazkii.botania.api.mana.ManaReceiver;

/**
 * Botania 植物魔法魔力容器 trait，作为 {@link ManaReceiver} 对外暴露，
 * 替代旧版在 {@code ManaMachineBlockEntity} 上承载 BTMana 的字段。
 */
public class BTManaContainerTrait extends MachineTrait implements ManaReceiver {

    @Getter
    @Setter
    @Persisted
    protected int maxBTMana;

    @Getter
    @Setter
    @Persisted
    protected int BTMana = 0;

    public BTManaContainerTrait(MetaMachine machine) {
        this(machine, 10_000);
    }

    public BTManaContainerTrait(MetaMachine machine, int maxBTMana) {
        super(machine);
        this.maxBTMana = maxBTMana;
    }

    @Override
    public Level getManaReceiverLevel() {
        return getMachine().getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return getMachine().getPos();
    }

    @Override
    public int getCurrentMana() {
        return BTMana;
    }

    @Override
    public boolean isFull() {
        return BTMana >= maxBTMana;
    }

    @Override
    public void receiveMana(int mana) {
        changeMana(mana);
    }

    public int changeMana(int toChange) {
        int changed;
        if (toChange > 0) {
            changed = Math.min(toChange, maxBTMana - BTMana);
        } else {
            changed = -Math.min(-toChange, BTMana);
        }
        BTMana += changed;
        return changed;
    }

    /** 抽取至多 {@code mana}，返回实际抽出的量。 */
    public long sendMana(long mana) {
        if (mana <= 0) return 0;
        long drained = Math.min(mana, BTMana);
        BTMana -= (int) drained;
        return drained;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }
}
