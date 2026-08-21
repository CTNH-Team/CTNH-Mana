package com.magicbee.ctnhmana.common.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 存放"护甲前原始伤害"的玩家实体 capability。
 * <p>
 * 用于中央非护甲减伤钳制器（{@code DamageClampHandler}）：在
 * {@code LivingHurtEvent.HIGHEST} 记下护甲前的原始 damage，让各个 mod 的减伤
 * 照常叠加后，低优先级再据此还原原始值、把累计减伤收缩到上限内。
 * <p>
 * Forge 1.20.1 的 {@code LivingHurtEvent}/{@code LivingDamageEvent} 没有
 * {@code getOriginalAmount()}（那是 1.20.2+/NeoForge 的 API），因此必须自己
 * 在事件内保存原始量，而非依赖 Forge 回填。
 */
public class DamageClampCapability implements ICapabilityProvider {

    /** 每个玩家一次事件只会命中一次，不需要实例状态；pre 存为实体能力内的可变字段。 */
    public static final Capability<DamageClampCapability> CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private float preDamage;

    public DamageClampCapability() {
        this.preDamage = -1.0F;
    }

    /** 护甲前原始伤害；未记录时为 -1。 */
    public float getPreDamage() {
        return preDamage;
    }

    public void setPreDamage(float preDamage) {
        this.preDamage = preDamage;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability,
                                                      @Nullable Direction direction) {
        return capability == CAP ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }
}
