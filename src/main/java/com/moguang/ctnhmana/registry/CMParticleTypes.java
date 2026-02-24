package com.moguang.ctnhmana.registry;

import com.moguang.ctnhmana.CTNHMana;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 粒子类型注册（common，服务端与客户端均需）
 * 粒子 Provider 注册在 ClientProxy 中。
 */
public class CMParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CTNHMana.MODID);

    public static final RegistryObject<SimpleParticleType> INDEX_TARGET =
            PARTICLE_TYPES.register("index_target", () -> new SimpleParticleType(false));
}