package com.moguang.ctnhmana.client.render.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IconParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet; // 精灵集，用于获取纹理精灵

    protected IconParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int time) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;

        // 粒子基础属性
        this.lifetime = time;       // 短生命周期，持续生成实现常驻
        this.gravity = 0.0F;     // 无重力
        this.friction = 1.0F;    // 无摩擦力
        this.xd = 0.0D;          // 无移动速度
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.quadSize = 0.5F;    // 粒子大小（可调整）
        this.hasPhysics = false; // 不受物理影响

        // 关键：从精灵集获取精灵（单精灵直接取第一个，动画则按年龄取）
        this.setSpriteFromAge(spriteSet);
    }

    // 原版精灵渲染类型（自动使用精灵表纹理）
    @Override
    public ParticleRenderType getRenderType() {
        // 半透明渲染（适合带透明通道的图标）
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        // 不透明渲染（无透明通道时用）：return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    // 按粒子年龄更新精灵（动画粒子关键）
    @Override
    public void tick() {
        super.tick();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        // 每tick更新精灵（动画粒子需要，单精灵可省略）
        this.setSpriteFromAge(this.spriteSet);
    }

    // 精灵工厂（接收SpriteSet参数）
    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            if (dx > 0) return new IconParticle(level, x, y, z, this.spriteSet, (int) dx);
            return new IconParticle(level, x, y, z, this.spriteSet, 2);
        }
    }
}
