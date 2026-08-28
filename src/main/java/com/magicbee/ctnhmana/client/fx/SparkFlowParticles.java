package com.magicbee.ctnhmana.client.fx;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.CMConfig;

/**
 * 本地生成火花「魔力流动」粒子。
 *
 * <p>
 * 颜色、抖动、尺寸与 Botania 的 {@code SPARK_MANA_FLOW} 完全一致；区别是用 {@link SparkFlowParticle}
 * 匀速飞行并在终点消失；Botania 的粒子带阻力、寿命随机，落点是距离的 0.86~1.11 倍，在尖塔火花
 * 15~50 格的连线上会明显冲过火花中心。
 * </p>
 */
public final class SparkFlowParticles {

    private SparkFlowParticles() {}

    public static void spawn(Level level, Vec3 origin, Vec3 target, int color) {
        if (!(level instanceof ClientLevel client)) {
            return;
        }
        double rc = 0.45D;
        Vec3 from = origin.add((Math.random() - 0.5D) * rc, (Math.random() - 0.5D) * rc, (Math.random() - 0.5D) * rc);
        Vec3 to = target.add((Math.random() - 0.5D) * rc, (Math.random() - 0.5D) * rc, (Math.random() - 0.5D) * rc);

        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        if (client.random.nextFloat() < 0.25F) {
            r += 0.2F * (float) client.random.nextGaussian();
            g += 0.2F * (float) client.random.nextGaussian();
            b += 0.2F * (float) client.random.nextGaussian();
        }
        float size = 0.125F + 0.125F * (float) Math.random();

        SparkFlowParticle.spawn(client, from, to, size, r, g, b, CMConfig.spark().flightTicks());
    }
}
