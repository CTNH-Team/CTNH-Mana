package com.magicbee.ctnhmana.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.magicbee.ctnhmana.data.ManaData;

/** 虚境 debug 工具：右键切换世界级 isZenithOpen 状态。 */
public class ZenithDebugToolItem extends Item {

    public ZenithDebugToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            ManaData data = ManaData.getOrCreate(serverLevel);
            boolean next = !data.isZenithOpen();
            data.setZenithOpen(next);
            player.sendSystemMessage(Component.literal("isZenithOpen = " + next));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
