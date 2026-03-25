package com.moguang.ctnhmana.mixin.bloodmagic;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import wayoftime.bloodmagic.altar.AltarTier;
import wayoftime.bloodmagic.altar.AltarUpgrade;
import wayoftime.bloodmagic.altar.AltarUtil;
import wayoftime.bloodmagic.altar.BloodAltar;
import wayoftime.bloodmagic.block.enums.BloodRuneType;
import wayoftime.bloodmagic.common.tile.TileAltar;

import static com.gregtechceu.gtceu.api.machine.MetaMachine.getMachine;

@Mixin(value = BloodAltar.class, remap = false)
public abstract class BloodAltarMixin implements IBloodAltarLogic {

    @Shadow(remap = false)
    private AltarTier altarTier;
    @Shadow(remap = false)
    private AltarTier currentTierDisplayed;
    @Shadow(remap = false)
    private AltarUpgrade upgrade;
    @Shadow(remap = false)
    private boolean isUpgraded;
    @Shadow(remap = false)
    private float consumptionMultiplier;
    @Shadow(remap = false)
    private float efficiencyMultiplier;
    @Shadow(remap = false)
    private float sacrificeEfficiencyMultiplier;
    @Shadow(remap = false)
    private float selfSacrificeEfficiencyMultiplier;
    @Shadow(remap = false)
    private float capacityMultiplier;
    @Shadow(remap = false)
    private float orbCapacityMultiplier;
    @Shadow(remap = false)
    private float dislocationMultiplier;
    @Shadow(remap = false)
    private int accelerationUpgrades;
    @Shadow(remap = false)
    private int chargingFrequency;
    @Shadow(remap = false)
    private int chargingRate;
    @Shadow(remap = false)
    private int maxCharge;
    @Shadow(remap = false)
    private int totalCharge;
    @Shadow(remap = false)
    private int capacity;
    @Shadow(remap = false)
    private int bufferCapacity;
    @Shadow(remap = false)
    private FluidStack fluid;
    @Shadow(remap = false)
    private FluidStack fluidOutput;
    @Shadow(remap = false)
    private FluidStack fluidInput;
    @Shadow(remap = false)
    private TileAltar tileAltar;

    @Shadow(remap = false)
    public abstract FluidStack getFluid();

    @Shadow(remap = false)
    public abstract int getFluidAmount();

    @Unique
    private BlockPos CM$IndustrialPos;

    private static final String CTNH_INDUSTRIAL_X = "ctnhmana_industrial_x";
    private static final String CTNH_INDUSTRIAL_Y = "ctnhmana_industrial_y";
    private static final String CTNH_INDUSTRIAL_Z = "ctnhmana_industrial_z";

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

    /**
     * 与 Blood Magic 反编译 {@code BloodAltar#checkTier} 等价，末尾接工业祭坛逻辑，并 cancel 跳过原版方法。
     */
    @Inject(method = "checkTier", at = @At("HEAD"), cancellable = true, remap = false)
    private void ctnhmana$replaceCheckTier(CallbackInfo ci) {
        AltarTier tier = AltarUtil.getTier(this.tileAltar.getLevel(), this.tileAltar.getBlockPos());
        this.altarTier = tier;
        this.upgrade = AltarUtil.getUpgrades(this.tileAltar.getLevel(), this.tileAltar.getBlockPos(), tier);
        if (tier.equals(this.currentTierDisplayed)) {
            this.currentTierDisplayed = AltarTier.ONE;
        }

        if (tier.equals(AltarTier.ONE)) {
            this.upgrade = null;
            this.isUpgraded = false;
            this.consumptionMultiplier = 0.0F;
            this.efficiencyMultiplier = 1.0F;
            this.sacrificeEfficiencyMultiplier = 0.0F;
            this.selfSacrificeEfficiencyMultiplier = 0.0F;
            this.capacityMultiplier = 1.0F;
            this.orbCapacityMultiplier = 1.0F;
            this.dislocationMultiplier = 1.0F;
            this.accelerationUpgrades = 0;
            this.chargingFrequency = 20;
            this.chargingRate = 0;
            this.maxCharge = 0;
            this.totalCharge = 0;
        } else {
            if (!tier.equals(AltarTier.ONE)) {
                this.isUpgraded = true;
                this.accelerationUpgrades = this.upgrade.getLevel(BloodRuneType.ACCELERATION);
                this.consumptionMultiplier = (float) (0.2 * (double) this.upgrade.getLevel(BloodRuneType.SPEED));
                this.efficiencyMultiplier = (float) Math.pow(0.85,
                        (double) this.upgrade.getLevel(BloodRuneType.EFFICIENCY));
                this.sacrificeEfficiencyMultiplier = (float) (0.1 *
                        (double) this.upgrade.getLevel(BloodRuneType.SACRIFICE));
                this.selfSacrificeEfficiencyMultiplier = (float) (0.1 *
                        (double) this.upgrade.getLevel(BloodRuneType.SELF_SACRIFICE));
                int cap = this.upgrade.getLevel(BloodRuneType.CAPACITY);
                int cap_aug = this.upgrade.getLevel(BloodRuneType.AUGMENTED_CAPACITY);
                this.capacityMultiplier = (float) (((double) 1.0F + 0.2 * (double) cap) *
                        Math.pow(1.075, (double) cap_aug));
                this.dislocationMultiplier = (float) Math.pow(1.2,
                        (double) this.upgrade.getLevel(BloodRuneType.DISPLACEMENT));
                this.orbCapacityMultiplier = (float) ((double) 1.0F +
                        0.02 * (double) this.upgrade.getLevel(BloodRuneType.ORB));
                this.chargingFrequency = Math.max(20 - this.accelerationUpgrades, 1);
                this.chargingRate = (int) ((float) (10 * this.upgrade.getLevel(BloodRuneType.CHARGING)) *
                        (1.0F + this.consumptionMultiplier / 2.0F));
                this.maxCharge = (int) ((double) 1000.0F *
                        Math.max((double) 0.5F * (double) this.capacityMultiplier, (double) 1.0F) *
                        (double) this.upgrade.getLevel(BloodRuneType.CHARGING));
            }

            this.capacity = (int) (10000.0F * this.capacityMultiplier);
            this.bufferCapacity = (int) (1000.0F * this.capacityMultiplier);
            if (CM$IndustrialPos != null && this.upgrade != null &&
                    getMachine(tileAltar.getLevel(), CM$IndustrialPos) instanceof IndustrialAltarMachine machine &&
                    machine.isFormed() && machine.altar_tier >= 2) {
                machine.updateAltarData();
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
            if (this.fluid.getAmount() > this.capacity) {
                this.fluid.setAmount(this.capacity);
            }
            if (this.fluidOutput.getAmount() > this.bufferCapacity) {
                this.fluidOutput.setAmount(this.bufferCapacity);
            }
            if (this.fluidInput.getAmount() > this.bufferCapacity) {
                this.fluidInput.setAmount(this.bufferCapacity);
            }
            if (this.totalCharge > this.maxCharge) {
                this.totalCharge = this.maxCharge;
            }
            this.tileAltar.getLevel().sendBlockUpdated(this.tileAltar.getBlockPos(),
                    this.tileAltar.getLevel().getBlockState(this.tileAltar.getBlockPos()),
                    this.tileAltar.getLevel().getBlockState(this.tileAltar.getBlockPos()), 3);
        }

        ci.cancel();
    }

    @Inject(method = "readFromNBT", at = @At("TAIL"), remap = false)
    private void ctnhmana$readIndustrialPos(CompoundTag tagCompound, CallbackInfo ci) {
        if (tagCompound.contains(CTNH_INDUSTRIAL_X) && tagCompound.contains(CTNH_INDUSTRIAL_Y) &&
                tagCompound.contains(CTNH_INDUSTRIAL_Z)) {
            this.CM$IndustrialPos = new BlockPos(
                    tagCompound.getInt(CTNH_INDUSTRIAL_X),
                    tagCompound.getInt(CTNH_INDUSTRIAL_Y),
                    tagCompound.getInt(CTNH_INDUSTRIAL_Z));
        } else {
            this.CM$IndustrialPos = null;
        }
    }

    @Inject(method = "writeToNBT", at = @At("TAIL"), remap = false)
    private void ctnhmana$writeIndustrialPos(CompoundTag tagCompound, CallbackInfo ci) {
        if (this.CM$IndustrialPos != null) {
            tagCompound.putInt(CTNH_INDUSTRIAL_X, this.CM$IndustrialPos.getX());
            tagCompound.putInt(CTNH_INDUSTRIAL_Y, this.CM$IndustrialPos.getY());
            tagCompound.putInt(CTNH_INDUSTRIAL_Z, this.CM$IndustrialPos.getZ());
        } else {
            tagCompound.remove(CTNH_INDUSTRIAL_X);
            tagCompound.remove(CTNH_INDUSTRIAL_Y);
            tagCompound.remove(CTNH_INDUSTRIAL_Z);
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
