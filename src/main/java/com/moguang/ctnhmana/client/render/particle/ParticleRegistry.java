package com.moguang.ctnhmana.client.render.particle;

import com.moguang.ctnhmana.CTNHMana;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CTNHMana.MODID);

    // 注册粒子类型
    public static final RegistryObject<SimpleParticleType> INDEX_TARGET = PARTICLE_TYPES.register("index_target",
            () -> new SimpleParticleType(false));

    // 注册精灵渲染提供者（关键：用registerSpriteSet）
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(INDEX_TARGET.get(), IconParticle.Provider::new);
    }
}