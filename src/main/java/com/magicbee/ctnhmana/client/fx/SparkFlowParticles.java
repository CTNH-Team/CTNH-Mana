package com.magicbee.ctnhmana.client.fx;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.WispParticleData;

/**
 * 本地生成火花「魔力流动」粒子。
 *
 * <p>
 * 观感与 Botania 的 {@code SPARK_MANA_FLOW} 一致（同样的抖动、速度、色偏与尺寸），但改用
 * {@link Level#addParticle} 而不是 {@code addAlwaysVisibleParticle}，让客户端自己的粒子等级设置与
 * 32 格距离裁剪重新生效——弱机玩家可以自行调低。
 * </p>
 */
public final class SparkFlowParticles {

    private SparkFlowParticles() {}

    public static void spawn(Level level, Vec3 origin, Vec3 target, int color) {
        double rc = 0.45D;
        Vec3 from = origin.add((Math.random() - 0.5D) * rc, (Math.random() - 0.5D) * rc, (Math.random() - 0.5D) * rc);
        Vec3 to = target.add((Math.random() - 0.5D) * rc, (Math.random() - 0.5D) * rc, (Math.random() - 0.5D) * rc);
        Vec3 motion = to.subtract(from).scale(0.04D);

        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        if (level.random.nextFloat() < 0.25F) {
            r += 0.2F * (float) level.random.nextGaussian();
            g += 0.2F * (float) level.random.nextGaussian();
            b += 0.2F * (float) level.random.nextGaussian();
        }
        float size = 0.125F + 0.125F * (float) Math.random();

        WispParticleData data = WispParticleData.wisp(size, r, g, b).withNoClip(true);
        level.addParticle(data, from.x, from.y, from.z, motion.x, motion.y, motion.z);
    }
}
