package com.moguang.ctnhmana.common.blockentity.machine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import vazkii.botania.api.mana.ManaReceiver;

public class FlowerCakeBlockEntity extends MetaMachineBlockEntity
                                   implements IMachineBlockEntity, IManaged, GeoBlockEntity, ManaReceiver {

    @Persisted
    public int mana = 1;
    @Persisted
    public int max_mana = 1000000;

    public FlowerCakeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

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
        return this.mana;
    }

    @Override
    public boolean isFull() {
        return this.mana >= this.max_mana;
    }

    @Override
    public void receiveMana(int i) {
        this.mana += i;
        this.mana = Math.min(this.mana, max_mana);
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }
}
