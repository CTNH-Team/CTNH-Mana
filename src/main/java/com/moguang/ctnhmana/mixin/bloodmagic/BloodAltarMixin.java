package com.moguang.ctnhmana.mixin.bloodmagic;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.api.mixin.IBloodAltarLogic;
import com.moguang.ctnhmana.common.Mutiblock.IndustrialAltarMachine;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wayoftime.bloodmagic.altar.AltarUpgrade;
import wayoftime.bloodmagic.altar.BloodAltar;
import wayoftime.bloodmagic.block.enums.BloodRuneType;
import wayoftime.bloodmagic.common.tile.TileAltar;

import static com.gregtechceu.gtceu.api.machine.MetaMachine.getMachine;

@Mixin(BloodAltar.class)
public abstract class BloodAltarMixin implements IBloodAltarLogic {

    @Shadow(remap = false)
    private float capacityMultiplier;

    @Shadow(remap = false)
    private int capacity;
    @Shadow(remap = false)
    private TileAltar tileAltar;
    @Shadow (remap = false)
    private float consumptionMultiplier;
    @Shadow (remap = false)
    private AltarUpgrade upgrade;
    @Shadow (remap = false)
    private float dislocationMultiplier;
    @Shadow (remap = false)
    private float efficiencyMultiplier;
    @Shadow (remap = false)
    private FluidStack fluid;

    @Shadow public abstract FluidStack getFluid();

    @Shadow public abstract int getFluidAmount();

    @Unique
    @Persisted
    private BlockPos CM$IndustrialPos;
    @Override
    public void CM$BroadcastPos(BlockPos pos)
    {
        this.CM$IndustrialPos=pos;
    }

    @Override
    @Unique
    public void CM$resetCapacity(int Capacity)
    {
        capacity=Capacity;
    }
    @Override
    @Unique
    public void CM$setCapacityMultiplier(float Multiplier)
    {
        capacityMultiplier=Multiplier;
        this.capacity = (int) (FluidType.BUCKET_VOLUME * 10 * capacityMultiplier);
    }
    @Inject(
            method = "checkTier", // 目标方法名（原方法是private void checkTier()）
            at = @At("TAIL"),
            remap = false
    )
    private void injectCheckTier(CallbackInfo ci) {
        if (CM$IndustrialPos!=null&&getMachine(tileAltar.getLevel(),CM$IndustrialPos) instanceof IndustrialAltarMachine machine&&machine.isFormed())
        {
            this.consumptionMultiplier=(float) ((0.1+machine.altar_tier *0.05)*this.upgrade.getLevel(BloodRuneType.SPEED));
            this.consumptionMultiplier*=machine.SpeedModifier;
            int cap = upgrade.getLevel(BloodRuneType.CAPACITY);
            int cap_aug = upgrade.getLevel(BloodRuneType.AUGMENTED_CAPACITY);
            this.capacityMultiplier = (float) ((1 + 0.20*Math.pow(1.5,machine.altar_tier -1) * cap) * Math.pow(1.075+0.025*machine.altar_tier -1, cap_aug));
            this.capacityMultiplier= (float) (this.capacityMultiplier*(machine.CapacityModifier-1)*0.5);
            this.capacity= (int) (this.capacity*capacityMultiplier);
            this.dislocationMultiplier = (float) (Math.pow(1.2+0.05*machine.altar_tier, upgrade.getLevel(BloodRuneType.DISPLACEMENT)));
            this.efficiencyMultiplier = (float) Math.pow(0.85, upgrade.getLevel(BloodRuneType.EFFICIENCY)+machine.altar_tier -1);
        }
    }
    @Override
    public boolean CM$ConsumeLPIfEnough(int LP)
    {

        if(this.getFluidAmount()>=LP)
        {
            this.fluid.setAmount(this.getFluidAmount()-LP);
            return true;
        }
            this.fluid.setAmount(0);
            return false;

    }


}
