package com.magicbee.ctnhmana.common.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import com.magicbee.ctnhmana.api.machine.trait.BTManaContainerTrait;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeCapabilities;

public class FlowerCakeMachine extends MetaMachine {

    @Getter
    private final BTManaContainerTrait manaTrait;

    @Nullable
    protected TickableSubscription ManaSubs;
    @Persisted
    public boolean is_eating = true;

    public FlowerCakeMachine(IMachineBlockEntity holder) {
        super(holder);
        manaTrait = attachPersistentTrait("mana", new BTManaContainerTrait(this, 1_000_000));
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
        if (this.is_eating) {
            manaTrait.setMaxBTMana(900000);
            if (manaTrait.isFull()) {
                this.is_eating = false;
                manaTrait.setBTMana(0);
            }
        } else {
            manaTrait.setMaxBTMana(0);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == BotaniaForgeCapabilities.MANA_RECEIVER) {
            return LazyOptional.of(() -> manaTrait).cast();
        }
        return super.getCapability(cap, side);
    }
}
