package com.moguang.ctnhmana.machine;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.item.IGTTool;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.machine.owner.MachineOwner;
import com.moguang.ctnhmana.registry.CMMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Set;

public class FlowerCakeBlock extends MetaMachineBlock {
    public FlowerCakeBlock(Properties properties, MachineDefinition definition) {
        super(properties, definition);
    }
    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1.0f;
    }
    @Override
    public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return 0;
    }
    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        var machine = getMachine(world, pos);
        ItemStack itemStack = player.getItemInHand(hand);
        boolean shouldOpenUi = true;

        if (machine != null && machine.getOwnerUUID() == null && player instanceof ServerPlayer sPlayer) {
            machine.setOwnerUUID(sPlayer.getUUID());
            machine.markDirty();
        }

        Set<GTToolType> types = ToolHelper.getToolTypes(itemStack);
        if (machine != null &&
                (!types.isEmpty() && ToolHelper.canUse(itemStack) || types.isEmpty() && player.isShiftKeyDown())) {
            var result = machine.onToolClick(types, itemStack, new UseOnContext(player, hand, hit));
            if (result.getSecond() == InteractionResult.CONSUME && player instanceof ServerPlayer serverPlayer) {
                ToolHelper.playToolSound(result.getFirst(), serverPlayer);

                if (!serverPlayer.isCreative()) {
                    ToolHelper.damageItem(itemStack, serverPlayer, 1);
                }
            }
            if (result.getSecond() != InteractionResult.PASS) return result.getSecond();
        }

        if (itemStack.is(GTItems.PORTABLE_SCANNER.get())) {
            return itemStack.getItem().use(world, player, hand).getResult();
        }

        if (itemStack.getItem() instanceof IGTTool gtToolItem) {
            shouldOpenUi = gtToolItem.definition$shouldOpenUIAfterUse(new UseOnContext(player, hand, hit));
        }

        if (machine instanceof IInteractedMachine interactedMachine) {
            var result = interactedMachine.onUse(state, world, pos, player, hand, hit);
            if (result != InteractionResult.PASS) return result;
        }
        if(itemStack.isEmpty()&&this.getMachine(world,pos)instanceof FlowerCakeMachine fmachine&&!fmachine.is_eating)
        {
            fmachine.is_eating=true;
            player.addEffect(new MobEffectInstance(CMMobEffects.WishingFlying.get(), 20*60*30));
            return InteractionResult.SUCCESS;
        }
        if (shouldOpenUi && machine instanceof IUIMachine uiMachine &&
                MachineOwner.canOpenOwnerMachine(player, machine)) {
            return uiMachine.tryToOpenUI(player, hand, hit);
        }
        return shouldOpenUi ? InteractionResult.PASS : InteractionResult.CONSUME;
    }
}