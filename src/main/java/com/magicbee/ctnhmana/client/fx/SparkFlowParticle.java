package com.magicbee.ctnhmana.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.client.fx.FXWisp;

/**
 * 火花「魔力流动」光点：匀速直线飞行，寿命等于飞行时间，到达终点即消失。
 *
 * <p>
 * 观感沿用 Botania 的 wisp（同一张 {@code botania:wisp} 贴图、同样的加色渲染、同样的先胀后缩尺寸曲线），
 * 只把物理换成确定性的：{@link FXWisp} 每 tick 施加 0.98 阻力、寿命又在 28~40 tick 之间随机，落点因此是
 * 距离的 0.86~1.11 倍——短距离看不出来，而尖塔火花的连线长达 15~50 格时就会明显冲过火花中心。本类
 * 匀速飞行，速度取 {@code (终点-起点)/飞行tick}，落点误差为零。
 * </p>
 */
@OnlyIn(Dist.CLIENT)
public class SparkFlowParticle extends TextureSheetParticle {

    /** 由 {@link Provider} 在粒子提供者注册时捕获，用于直接构造粒子（addParticle 只能传 6 个 double，装不下颜色与寿命）。 */
    private static SpriteSet sprites;

    private final float baseQuadSize;
    private final int halfLife;

    private SparkFlowParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz,
                              float size, float r, float g, float b, int lifeTicks) {
        super(level, x, y, z);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.alpha = 0.375F;
        this.gravity = 0.0F;
        this.friction = 1.0F;
        this.hasPhysics = false;
        this.lifetime = Math.max(1, lifeTicks);
        this.quadSize = (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F * size;
        this.baseQuadSize = this.quadSize;
        this.halfLife = Math.max(1, this.lifetime / 2);
        if (sprites != null) {
            setSpriteFromAge(sprites);
        }
    }

    /**
     * 在 from → to 之间生成一个光点：匀速飞行 {@code flightTicks} tick 后正好到达终点并消失。
     *
     * @param flightTicks 飞行时间；Botania 原版的 {@code 速度 = 距离 × 0.04} 对应 25 tick
     */
    public static void spawn(ClientLevel level, Vec3 from, Vec3 to, float size, float r, float g, float b,
                             int flightTicks) {
        if (sprites == null) {
            return;
        }
        int ticks = Math.max(1, flightTicks);
        Vec3 step = to.subtract(from).scale(1.0D / ticks);
        Minecraft.getInstance().particleEngine.add(
                new SparkFlowParticle(level, from.x, from.y, from.z, step.x, step.y, step.z, size, r, g, b, ticks));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return FXWisp.NORMAL_RENDER;
    }

    @Override
    public float getQuadSize(float partialTick) {
        float scale = (float) age / (float) halfLife;
        if (scale > 1.0F) {
            scale = 2.0F - scale;
        }
        quadSize = baseQuadSize * scale * 0.5F;
        return quadSize;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        // 匀速：无阻力、无重力，落点因此可预测
        this.move(this.xd, this.yd, this.zd);
    }

    /** 只为注册粒子精灵集而存在；本模组通过 {@link #spawn} 直接构造粒子。 */
    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
            SparkFlowParticle.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            SparkFlowParticle particle = new SparkFlowParticle(level, x, y, z, dx, dy, dz, 0.15F, 1.0F, 1.0F, 1.0F, 25);
            particle.setSpriteFromAge(this.spriteSet);
            return particle;
        }
    }
}
