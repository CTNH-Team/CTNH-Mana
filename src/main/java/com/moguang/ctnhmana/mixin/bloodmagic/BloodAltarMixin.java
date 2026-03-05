package com.moguang.ctnhmana.mixin.bloodmagic;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import com.moguang.ctnhmana.Mutiblock.IndustrialAltarMachine;
import com.moguang.ctnhmana.api.mixin.IBloodAltarLogic;
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

@Mixin(value = BloodAltar.class, remap = false)
public abstract class BloodAltarMixin implements IBloodAltarLogic {

    @Shadow(remap = false)
    private float capacityMultiplier;

    @Shadow(remap = false)
    private int capacity;
    @Shadow(remap = false)
    private int bufferCapacity;
    @Shadow(remap = false)
    private TileAltar tileAltar;
    @Shadow(remap = false)
    private float consumptionMultiplier;
    @Shadow(remap = false)
    private AltarUpgrade upgrade;
    @Shadow(remap = false)
    private float dislocationMultiplier;
    @Shadow(remap = false)
    private float efficiencyMultiplier;
    @Shadow(remap = false)
    private FluidStack fluid;

    @Shadow(remap = false)
    public abstract FluidStack getFluid();

    @Shadow(remap = false)
    public abstract int getFluidAmount();

    @Unique
    @Persisted
    private BlockPos CM$IndustrialPos;

    @Override
    public void CM$BroadcastPos(BlockPos pos) {
        this.CM$IndustrialPos = pos;
    }

    @Override
    @Unique
    public void CM$resetCapacity(int Capacity) {
        capacity = Capacity;
    }

    @Override
    @Unique
    public void CM$setCapacityMultiplier(float Multiplier) {
        capacityMultiplier = Multiplier;
        this.capacity = (int) (FluidType.BUCKET_VOLUME * 10 * capacityMultiplier);
    }

    @Inject(
            method = "checkTier", // 目标方法名（原方法是private void checkTier()）
            at = @At("TAIL"),
            remap = false)
    private void injectCheckTier(CallbackInfo ci) {
        if (CM$IndustrialPos != null &&
                getMachine(tileAltar.getLevel(), CM$IndustrialPos) instanceof IndustrialAltarMachine machine &&
                machine.isFormed() && machine.altar_tier >= 2) {
            this.consumptionMultiplier = (float) ((0.1 + (machine.altar_tier - 1) * 0.05) *
                    this.upgrade.getLevel(BloodRuneType.SPEED));
            int cap = upgrade.getLevel(BloodRuneType.CAPACITY);
            int cap_aug = upgrade.getLevel(BloodRuneType.AUGMENTED_CAPACITY);
            this.capacityMultiplier = (float) ((1 + 0.20 * Math.pow(1.5, machine.altar_tier - 1) * cap) *
                    Math.pow(1.075 + 0.025 * (machine.altar_tier - 1), cap_aug)) * (machine.altar_tier - 1);
            this.capacity = (int) (FluidType.BUCKET_VOLUME * 10 * this.capacityMultiplier);
            this.dislocationMultiplier = (float) (Math.pow(1.2 + 0.05 * machine.altar_tier,
                    upgrade.getLevel(BloodRuneType.DISPLACEMENT)));
            this.efficiencyMultiplier = (float) Math.pow(0.85,
                    upgrade.getLevel(BloodRuneType.EFFICIENCY) + machine.altar_tier - 1);
            this.bufferCapacity = 0;
            tileAltar.getLevel().sendBlockUpdated(tileAltar.getBlockPos(),
                    tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()),
                    tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), 3);
        }
    }

    @Override
    public boolean CM$ConsumeLPIfEnough(int LP) {
        if (this.getFluidAmount() >= LP) {
            this.fluid.setAmount(this.getFluidAmount() - LP);
            return true;
        }
        this.fluid.setAmount(0);
        return false;
    }
}
