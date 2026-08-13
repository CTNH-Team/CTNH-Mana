package com.magicbee.ctnhmana.common.blockentity.machine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import lombok.Getter;
import vazkii.botania.api.mana.ManaReceiver;

public class ManaMachineBlockEntity extends MetaMachineBlockEntity
                                    implements IMachineBlockEntity, IManaged, ManaReceiver {

    @Persisted
    @Getter
    public int maxBTMana;
    @Persisted
    public int BTMana = 0;

    public ManaMachineBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.maxBTMana = 10000;
    }

    // 魔力接受单位
    @Override
    public Level getManaReceiverLevel() {
        return this.getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return this.getBlockPos();
    }

    @Override
    public int getCurrentMana() {
        return (int) BTMana;
    }

    @Override
    public boolean isFull() {
        return BTMana >= maxBTMana;
    }

    @Override
    public void receiveMana(int i) {
        BTMana += i;
        BTMana = Math.min(BTMana, maxBTMana);
        setChanged();
    }

    public void setMaxMana(int i) {
        maxBTMana = i;
        setChanged();
    }

    public long sendMana(long mana) {
        if (BTMana > mana) {
            BTMana -= mana;
            return mana;
        } else {
            mana = BTMana;
            BTMana = 0;
            return mana;
        }
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    // private final LazyOptional<ManaReceiver> manaReceiverCap = LazyOptional.of(() -> this);
    //
    // @Override
    // public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
    // if(cap == BotaniaForgeCapabilities.MANA_RECEIVER)
    // return manaReceiverCap.cast();
    // return super.getCapability(cap);
    // }
}
