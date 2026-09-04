package com.magicbee.ctnhmana.api.machine.trait;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import com.magicbee.ctnhmana.common.multiblock.SpireBigMath;
import vazkii.botania.api.mana.ManaReceiver;

import java.math.BigInteger;

/** Owns Mystic Spire's persistent mana state; the block entity is only a rendering shell. */
public class MysticSpireManaTrait extends MachineTrait implements ManaReceiver {

    @Persisted
    private int maxBTMana = 10_000;
    @Persisted
    private int BTMana;
    @Persisted
    private String trueMana = "0";
    @Persisted
    private String trueManaCapacity = "0";

    public MysticSpireManaTrait(MetaMachine machine) {
        super(machine);
    }

    public BigInteger getTrueManaBig() {
        return SpireBigMath.parsePersisted(trueMana);
    }

    public BigInteger getTrueManaCapBig() {
        BigInteger cap = SpireBigMath.parsePersisted(trueManaCapacity);
        return cap.signum() > 0 ? cap : BigInteger.valueOf(Math.max(0, maxBTMana));
    }

    private void setTrueMana(BigInteger value) {
        trueMana = SpireBigMath.toPersistString(SpireBigMath.nonNegative(value));
    }

    private void setTrueManaCapacity(BigInteger value) {
        trueManaCapacity = SpireBigMath.toPersistString(SpireBigMath.nonNegative(value));
    }

    public void setTrueManaCapacityBig(BigInteger value) {
        setTrueManaCapacity(value);
        if (getTrueManaBig().compareTo(getTrueManaCapBig()) > 0) setTrueMana(getTrueManaCapBig());
        syncManaCache();
    }

    public void setMaxMana(int value) {
        maxBTMana = Math.max(0, value);
        syncManaCache();
    }

    public void syncManaCache() {
        BTMana = SpireBigMath.clampToIntNonNegative(SpireBigMath.min(getTrueManaBig(), BigInteger.valueOf(maxBTMana)));
    }

    public BigInteger getTrueManaRoomBig() {
        return SpireBigMath.subtractNonNegative(getTrueManaCapBig(), getTrueManaBig());
    }

    public int mysticOutboundTickCap(int limit) {
        return bounded(getTrueManaBig(), limit);
    }

    public int mysticInboundTickBudget(int limit) {
        return bounded(getTrueManaRoomBig(), limit);
    }

    private static int bounded(BigInteger amount, int limit) {
        return limit <= 0 ? 0 : SpireBigMath
                .clampToIntNonNegative(SpireBigMath.min(SpireBigMath.nonNegative(amount), BigInteger.valueOf(limit)));
    }

    public void mysticDrainMana(int amount) {
        receiveMana(-amount);
    }

    public long sendMana(long amount) {
        long sent = Math.min(Math.max(0L, amount), SpireBigMath.clampToLong(getTrueManaBig()));
        receiveMana(-(int) Math.min(sent, Integer.MAX_VALUE));
        return sent;
    }

    public int getMaxBTMana() {
        return maxBTMana;
    }

    @Override
    public Level getManaReceiverLevel() {
        return machine.getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return machine.getPos();
    }

    @Override
    public int getCurrentMana() {
        return BTMana;
    }

    @Override
    public boolean isFull() {
        return getTrueManaBig().compareTo(getTrueManaCapBig()) >= 0;
    }

    @Override
    public void receiveMana(int amount) {
        if (amount == 0) return;
        BigInteger current = getTrueManaBig();
        setTrueMana(amount > 0 ? SpireBigMath.addCapToMax(current, BigInteger.valueOf(amount), getTrueManaCapBig()) :
                SpireBigMath.subtractNonNegative(current, BigInteger.valueOf(-(long) amount)));
        syncManaCache();
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }
}
