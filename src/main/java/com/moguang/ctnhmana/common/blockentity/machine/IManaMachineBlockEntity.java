package com.moguang.ctnhmana.common.blockentity.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.model.machine.MachineRenderState;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.api.mana.ManaReceiver;

public class IManaMachineBlockEntity extends MetaMachineBlockEntity implements IMachineBlockEntity, IManaged, ManaReceiver {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(IManaMachineBlockEntity.class,MetaMachineBlockEntity.MANAGED_FIELD_HOLDER);
    @Persisted
    @DescSynced
    @RequireRerender
    private MachineRenderState renderState;
    private final long offset;
    @Persisted
    @Getter
    public int maxBTMana;
    @Persisted
    public int BTMana =0;
    public IManaMachineBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.renderState = this.getDefinition().defaultRenderState();
        this.offset = (long) GTValues.RNG.nextInt(20);
        this.maxBTMana =10000;
    }


    @Override
    public MachineRenderState getRenderState() {
        return this.renderState;
    }

    @Override
    public void setRenderState(MachineRenderState state) {
        this.renderState = state;
        this.scheduleRenderUpdate();
    }

    @Override
    public MetaMachine getMetaMachine() {
        return this.metaMachine;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }
    @Override
    public void onChanged() {
        super.onChanged(); // 调用父类逻辑
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }


    //魔力接受单位
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
        BTMana +=i;
        BTMana =Math.min(BTMana, maxBTMana);
        setChanged();
    }
    public void setMaxMana(int i)
    {
        maxBTMana =i;
        setChanged();
    }
    public long sendMana(long mana)
    {
        if(BTMana >mana)
        {
            BTMana -=mana;
            return mana;
        }
        else
        {
            mana= BTMana;
            BTMana =0;
            return mana;
        }
    }
    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

//    private final LazyOptional<ManaReceiver> manaReceiverCap = LazyOptional.of(() -> this);
//
//    @Override
//    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
//        if(cap == BotaniaForgeCapabilities.MANA_RECEIVER)
//            return manaReceiverCap.cast();
//        return super.getCapability(cap);
//    }
}
