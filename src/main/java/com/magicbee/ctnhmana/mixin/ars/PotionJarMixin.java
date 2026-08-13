package com.magicbee.ctnhmana.mixin.ars;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;

import com.hollingsworth.arsnouveau.common.block.PotionJar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 手持流体容器对药水罐灌入/抽出。
 * 走 Ars Creo 挂载、并由 {@link PotionTankMixin} 改为只认 GT 药水的 cap；
 * 原版药水瓶 / 玻璃瓶 / 箭仍走 Ars 原逻辑。
 */
@Mixin(PotionJar.class)
public class PotionJarMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void ctnh$fluidInteract(BlockState state, Level worldIn, BlockPos pos, Player player,
                                    InteractionHand handIn, BlockHitResult hit,
                                    CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(handIn);
        // 原版瓶/箭留给 Ars 自己处理
        if (stack.is(Items.POTION) || stack.is(Items.GLASS_BOTTLE) || stack.is(Items.ARROW)) {
            return;
        }
        // 通过 Creo/GT 改造后的 IFluidHandler 交互
        if (FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, hit.getDirection())) {
            cir.setReturnValue(InteractionResult.sidedSuccess(worldIn.isClientSide));
        }
    }
}
