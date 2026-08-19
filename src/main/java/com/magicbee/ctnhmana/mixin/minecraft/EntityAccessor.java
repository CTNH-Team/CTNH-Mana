package com.magicbee.ctnhmana.mixin.minecraft;

import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * 暴露 {@link Entity#setSharedFlag(int, boolean)}（protected），
 * 供缚地效果清除鞘翅滑翔状态（Entity.FLAG_FALL_FLYING = 7）使用。
 */
@Mixin(Entity.class)
public interface EntityAccessor {

    @Invoker("setSharedFlag")
    void ctnhmana$setSharedFlag(int flag, boolean set);
}
