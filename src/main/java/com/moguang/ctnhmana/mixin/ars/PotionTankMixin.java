package com.moguang.ctnhmana.mixin.ars;

import com.gregtechceu.gtceu.common.data.GTFluids;
import com.gregtechceu.gtceu.common.fluid.potion.PotionFluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import com.hollingsworth.ars_creo.common.PotionTank;
import com.hollingsworth.arsnouveau.api.potion.PotionData;
import com.hollingsworth.arsnouveau.common.block.tile.PotionJarTile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 改写 Ars Creo 挂在药水罐上的 {@link PotionTank}：
 * 只接受 / 产出 GT 药水流体（{@code gtceu:potion}），拒绝 Create 的 {@code create:potion}。
 * <p>
 * 换算逻辑必须写在本 mixin 的 {@code @Unique} 方法中，不能调用本 mod 其它类：
 * Mixin 合并进 {@code ars_creo} 目标类后，从目标模块解析 {@code ctnhmana} 辅助类会
 * {@link ClassNotFoundException}。
 */
@Mixin(value = PotionTank.class, remap = false)
public abstract class PotionTankMixin {

    @Shadow
    @Final
    private PotionJarTile jar;

    @Shadow
    public abstract int getFluidAmount();

    /** 仅允许 GT 药水流体，并走 Ars 原有 canAccept */
    @Inject(method = "isFluidValid", at = @At("HEAD"), cancellable = true)
    private void ctnh$gtPotionOnly(FluidStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isEmpty() || !stack.getFluid().isSame(GTFluids.POTION.get().getSource())) {
            cir.setReturnValue(false);
            return;
        }
        cir.setReturnValue(jar.canAccept(ctnh$toPotionData(stack), 1));
    }

    /** 对外展示为 GT 药水流体，避免 Create 管道按 create:potion 匹配 */
    @Inject(method = "getFluid", at = @At("HEAD"), cancellable = true)
    private void ctnh$gtGetFluid(CallbackInfoReturnable<FluidStack> cir) {
        cir.setReturnValue(ctnh$toFluidStack(jar.getData(), getFluidAmount()));
    }

    /** 抽出时同样返回 GT 药水流体 */
    @Inject(
            method = "drain(ILnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/minecraftforge/fluids/FluidStack;",
            at = @At("HEAD"),
            cancellable = true)
    private void ctnh$gtDrain(int maxDrain, IFluidHandler.FluidAction action,
                              CallbackInfoReturnable<FluidStack> cir) {
        int drained = Math.min(maxDrain, getFluidAmount());
        if (drained <= 0) {
            cir.setReturnValue(FluidStack.EMPTY);
            return;
        }
        // 与 Creo 一致：mB → 点数用 ceil(mB * 0.4)
        int points = (int) Math.ceil(drained * PotionTank.MB_TO_POTION);
        FluidStack stack = ctnh$toFluidStack(jar.getData(), drained);
        if (action.execute() && points > 0) {
            jar.remove(points);
        }
        cir.setReturnValue(stack);
    }

    /** 从 GT 药水流体 NBT 构造 Ars {@link PotionData} */
    @Unique
    private static PotionData ctnh$toPotionData(FluidStack stack) {
        if (stack.isEmpty() || !stack.getFluid().isSame(GTFluids.POTION.get().getSource())) {
            return new PotionData();
        }
        CompoundTag tag = stack.getOrCreateTag();
        Potion potion = PotionUtils.getPotion(tag);
        if (potion == Potions.EMPTY) {
            return new PotionData();
        }
        List<MobEffectInstance> customs = PotionUtils.getCustomEffects(tag);
        return new PotionData(potion, customs, new HashSet<>(Collections.singletonList(potion)));
    }

    /** 将罐内 {@link PotionData} 转为带效果 NBT 的 GT 药水流体 */
    @Unique
    private static FluidStack ctnh$toFluidStack(PotionData data, int mb) {
        if (mb <= 0 || data == null || data.getPotion() == Potions.EMPTY) {
            return FluidStack.EMPTY;
        }
        return PotionFluid.withEffects(mb, data.getPotion(), data.getCustomEffects());
    }
}
