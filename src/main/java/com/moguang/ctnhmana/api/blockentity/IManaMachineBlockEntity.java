package com.moguang.ctnhmana.api.blockentity;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.model.machine.MachineRenderState;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.syncdata.managed.MultiManagedStorage;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;

public class IManaMachineBlockEntity extends MetaMachineBlockEntity implements IMachineBlockEntity, IManaged, ManaReceiver {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(IManaMachineBlockEntity.class);
    @Persisted
    @DescSynced
    @RequireRerender
    private MachineRenderState renderState;
    private final long offset;
    @Persisted
    @Getter
    public long MAX_BT_MANA=10000L;
    @Persisted
    @Getter
    public long BT_MANA=0;
    public IManaMachineBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.renderState = this.getDefinition().defaultRenderState();
        this.offset = (long) GTValues.RNG.nextInt(20);
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
        return (int)BT_MANA;
    }

    @Override
    public boolean isFull() {
        return BT_MANA>=MAX_BT_MANA;
    }


    @Override
    public void receiveMana(int i) {
        BT_MANA+=i;
        BT_MANA=Math.min(BT_MANA,MAX_BT_MANA);
        setChanged();
    }
    public void setMaxMana(long i)
    {
        MAX_BT_MANA=i;
    }
    public long ChangeMana(long mana)
    {
        if(BT_MANA>mana)
        {
            BT_MANA-=mana;
            return mana;
        }
        else
        {
            mana=BT_MANA;
            BT_MANA=0;
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
