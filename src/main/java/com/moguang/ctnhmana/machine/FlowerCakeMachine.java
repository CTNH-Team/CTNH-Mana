package com.moguang.ctnhmana.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.factory.MachineUIFactory;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

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
    public InteractionResult tryToOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
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
    {            var receiver=((FlowerCakeBlockEntity)this.getHolder());
        if(this.is_eating)
        {

            receiver.max_mana=900000;
            if(receiver.max_mana<=receiver.mana) {
                this.is_eating = false;
                receiver.mana=0;
            }
        }
        else
        {
            receiver.max_mana=0;
        }
    }
    @CN(
            {
                    "§b制作组物品§r",
                    "§b我永恒的灵魂，注视着你的心。纵使黑夜孤寂，白昼如焚。",
                    "需要输入900000魔力来激活，食用后获得半个小时的创造飞行",
                    "可连续食用！"
            }
    )
    @EN(
            {
                    "§b制作组物品§r",
                    "§b我永恒的灵魂，注视着你的心。纵使黑夜孤寂，白昼如焚。",
                    "需要输入900000魔力来激活，食用后获得半个小时的创造飞行",
                    "可连续食用！"
            }
    )
    public static Lang[] FlowerCakeLang;
}