package com.magicbee.ctnhmana.common.blockentity.machine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.magicbee.ctnhmana.api.machine.trait.BTManaContainerTrait;
import com.magicbee.ctnhmana.common.machine.FlowerCakeMachine;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FlowerCakeBlockEntity extends MetaMachineBlockEntity implements GeoBlockEntity {

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

    public BTManaContainerTrait getManaTrait() {
        return ((FlowerCakeMachine) getMetaMachine()).getManaTrait();
    }
}
