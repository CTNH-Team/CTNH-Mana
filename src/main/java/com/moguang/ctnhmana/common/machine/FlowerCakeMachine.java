package com.moguang.ctnhmana.common.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.RecipeAmperageEnergyContainer;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import com.moguang.ctnhmana.common.blockentity.machine.FlowerCakeBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FlowerCakeMachine extends SimpleTieredMachine {

    @Nullable
    protected TickableSubscription ManaSubs;
    @Persisted
    public boolean is_eating = true;

    public FlowerCakeMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, 0, (tiers) -> 32000, args);
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        long tierVoltage = GTValues.V[getTier()];
        tierVoltage = 0;
        if (isEnergyEmitter()) {
            return RecipeAmperageEnergyContainer.makeEmitterContainer(this, tierVoltage * 64L,
                    tierVoltage, getMaxInputOutputAmperage());
        } else {
            return RecipeAmperageEnergyContainer.makeReceiverContainer(this, tierVoltage * 64L,
                    tierVoltage, getMaxInputOutputAmperage());
        }
    }

    @Override
    public InteractionResult tryToOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::registersubs));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (ManaSubs != null) {
            ManaSubs.unsubscribe();;
            ManaSubs = null;
        }
    }

    public void registersubs() {
        ManaSubs = subscribeServerTick(ManaSubs, this::check_eaten);
    }

    public void check_eaten() {
        var receiver = ((FlowerCakeBlockEntity) this.getHolder());
        if (this.is_eating) {

            receiver.max_mana = 900000;
            if (receiver.max_mana <= receiver.mana) {
                this.is_eating = false;
                receiver.mana = 0;
            }
        } else {
            receiver.max_mana = 0;
        }
    }
}
