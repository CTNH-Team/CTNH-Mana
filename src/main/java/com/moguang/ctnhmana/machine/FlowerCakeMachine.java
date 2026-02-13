package com.moguang.ctnhmana.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.RecipeAmperageEnergyContainer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.common.blockentity.machine.FlowerCakeBlockEntity;
import com.moguang.ctnhmana.common.blockentity.machine.IManaMachineBlockEntity;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

public class FlowerCakeMachine extends SimpleTieredMachine {
    @Nullable
    protected TickableSubscription ManaSubs;
    @Persisted
    public boolean is_eating=true;
    public FlowerCakeMachine(IMachineBlockEntity holder,Object... args) {
        super(holder, 0, (tiers)->32000, args);
    }
    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        long tierVoltage = GTValues.V[getTier()];
        tierVoltage=0;
        if (isEnergyEmitter()) {
            return RecipeAmperageEnergyContainer.makeEmitterContainer(this, tierVoltage * 64L,
                    tierVoltage, getMaxInputOutputAmperage());
        } else {
            return RecipeAmperageEnergyContainer.makeReceiverContainer(this, tierVoltage * 64L,
                    tierVoltage, getMaxInputOutputAmperage());
        }
    }
    @Override
    public void onLoad()
    {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::registersubs));
        }
    }
    @Override
    public void onUnload()
    {
        super.onUnload();
        if(ManaSubs!=null)
        {
            ManaSubs.unsubscribe();;
            ManaSubs=null;
        }
    }
    public void registersubs()
    {
        ManaSubs=subscribeServerTick(ManaSubs, this::check_eaten);
    }
    public void check_eaten()
    {            var reciever=((FlowerCakeBlockEntity)this.getHolder());
        if(this.is_eating)
        {

            reciever.max_mana=900000;
            if(reciever.max_mana<=reciever.mana) {
                this.is_eating = false;
                reciever.mana=0;
            }
        }
        else
        {
            reciever.max_mana=0;
        }
    }
}