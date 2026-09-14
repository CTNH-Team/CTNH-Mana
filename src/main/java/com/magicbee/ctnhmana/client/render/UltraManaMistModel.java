package com.magicbee.ctnhmana.client.render;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import committee.nova.mods.avaritia.api.client.model.CachedFormat;
import committee.nova.mods.avaritia.api.client.model.Quad;
import committee.nova.mods.avaritia.client.model.loader.base.HaloUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 究极魔力锭的“紫色迷雾”模型包装。
 *
 * <p>把已经烘焙好的物品模型原样委托出去，只额外挂一个迷雾 pass：
 * 该 pass 每帧用 {@code avaritia:misc/halo} 与 {@code avaritia:misc/halo_noise}
 * 现场生成一组加法混合的 quad，围绕物品缓慢环绕、缩放、呼吸，
 * 叠出连续流动的紫色雾气。因为走的是原版 {@code ItemRenderer} 的多 pass 循环，
 * 物品栏、JEI/EMI、掉落物、展示框、手持等所有显示上下文都会生效。
 *
 * <p>贴图全部复用已有资源，不改动任何材质文件。
 */
public class UltraManaMistModel implements BakedModel {

    /** 光晕贴图：32×32 纯白径向 alpha 渐变，用作雾团本体。 */
    private static final ResourceLocation HALO_TEXTURE = ResourceLocation.tryParse("avaritia:misc/halo");
    /** 噪声贴图：32×256（8 帧，原版逐帧动画），用作翻涌的雾气流。 */
    private static final ResourceLocation NOISE_TEXTURE = ResourceLocation.tryParse("avaritia:misc/halo_noise");

    /** 物品贴图所在的平面为 z=7.5/16~8.5/16，迷雾分别铺在它的正面与背面。 */
    private static final float Z_FRONT = 0.545F;
    private static final float Z_BACK = 0.400F;

    /** 取自材质 ultra_mana 自身颜色 0x7D26CD，两端各取一档深浅。 */
    private static final float[] COLOR_DEEP = rgb(0x6A1FB0);
    private static final float[] COLOR_BASE = rgb(0x7D26CD);
    private static final float[] COLOR_LIGHT = rgb(0xB06CFF);

    /** 雾团环绕中心的数量与轨道相位。 */
    private static final int PUFF_COUNT = 3;
    private static final float TAU = (float) (Math.PI * 2.0);

    private final BakedModel base;
    private final MistPass mistPass;

    public UltraManaMistModel(BakedModel base) {
        this.base = base;
        this.mistPass = new MistPass(base);
    }

    @Override
    public @NotNull List<BakedModel> getRenderPasses(ItemStack stack, boolean fabulous) {
        // 先画物品本体，再叠加迷雾；两个 pass 各自声明渲染层，互不干扰。
        return List.of(this.base, this.mistPass);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction,
                                             @NotNull RandomSource random) {
        return this.base.getQuads(state, direction, random);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.base.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.base.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.base.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.base.isCustomRenderer();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return this.base.getParticleIcon();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return this.base.getParticleIcon(data);
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return this.base.getOverrides();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return this.base.getTransforms();
    }

    @Override
    public @NotNull List<RenderType> getRenderTypes(ItemStack stack, boolean fabulous) {
        return this.base.getRenderTypes(stack, fabulous);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos,
                                           @NotNull BlockState state, @NotNull ModelData data) {
        return this.base.getModelData(level, pos, state, data);
    }

    /**
     * 纯附加的迷雾层：本体不产出任何 quad，只提供一组每帧重新生成的加法混合雾团。
     */
    private static class MistPass implements BakedModel {

        private final BakedModel base;

        private MistPass(BakedModel base) {
            this.base = base;
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction,
                                                 @NotNull RandomSource random) {
            // 只在无朝向（不剔除）的那一批里出 quad，避免同一个雾团被画多次。
            if (direction != null) {
                return List.of();
            }
            TextureAtlasSprite halo = sprite(HALO_TEXTURE);
            if (halo == null) {
                return List.of();
            }
            TextureAtlasSprite noise = sprite(NOISE_TEXTURE);

            float time = (Util.getMillis() % 1_000_000L) / 1000.0F;
            List<BakedQuad> quads = new ArrayList<>(5 + PUFF_COUNT);

            // 1) 背后的大光晕：垫在物品后面，保证从背面看也有紫雾。
            quads.add(quad(halo, 0.5F, 0.5F, 0.62F, Z_BACK, 0.0F,
                    0.0F, 1.0F, 0.0F, 1.0F, argb(COLOR_BASE, 0.30F + 0.06F * Mth.sin(time * 0.9F))));

            // 2) 正面的柔雾：让物品整体蒙上一层紫色。
            quads.add(quad(halo, 0.5F, 0.5F, 0.58F, Z_FRONT, time * 0.06F,
                    0.0F, 1.0F, 0.0F, 1.0F, argb(COLOR_LIGHT, 0.16F + 0.04F * Mth.sin(time * 1.3F + 1.1F))));

            // 3) 环绕的雾团：每团按自己的速度和相位绕着物品公转，同时自转与呼吸。
            for (int i = 0; i < PUFF_COUNT; i++) {
                float seed = i * (TAU / PUFF_COUNT);
                float orbit = time * (0.55F + 0.16F * i) + seed;
                float radius = 0.11F + 0.05F * Mth.sin(time * 0.7F + seed);
                float half = 0.19F + 0.05F * Mth.sin(time * 0.95F + seed * 1.7F);
                float spin = time * (0.8F + 0.35F * i) * ((i & 1) == 0 ? 1.0F : -1.0F);
                float alpha = 0.17F + 0.07F * Mth.sin(time * 1.15F + seed * 2.3F);
                float[] color = mix(COLOR_DEEP, COLOR_LIGHT, 0.5F + 0.5F * Mth.sin(time * 0.6F + seed));
                quads.add(quad(halo,
                        0.5F + Mth.cos(orbit) * radius, 0.5F + Mth.sin(orbit) * radius,
                        half, Z_FRONT, spin,
                        // 只取贴图中心部分，得到边缘柔和的雾团而不是硬边方块。
                        0.18F, 0.82F, 0.18F, 0.82F, argb(color, alpha)));
            }

            // 4) 噪声气流：两层反向漂移的逐帧噪声，制造持续翻涌的观感。
            if (noise != null) {
                quads.add(quad(noise, 0.5F, 0.5F, 0.55F, Z_FRONT, -time * 0.25F,
                        0.03F, 0.97F, 0.03F, 0.97F, argb(COLOR_BASE, 0.055F)));
                quads.add(quad(noise, 0.5F, 0.5F, 0.63F, Z_BACK, time * 0.18F,
                        0.03F, 0.97F, 0.03F, 0.97F, argb(COLOR_LIGHT, 0.045F)));
            }
            return quads;
        }

        @Override
        public @NotNull List<RenderType> getRenderTypes(ItemStack stack, boolean fabulous) {
            return List.of(UltraManaMistRenderType.mist());
        }

        @Override
        public boolean useAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean usesBlockLight() {
            return false;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public @NotNull TextureAtlasSprite getParticleIcon() {
            return this.base.getParticleIcon();
        }

        @Override
        public @NotNull ItemOverrides getOverrides() {
            return ItemOverrides.EMPTY;
        }

        @Override
        public @NotNull ItemTransforms getTransforms() {
            return this.base.getTransforms();
        }
    }

    /**
     * 生成一片绕 (cx, cy) 旋转、位于 z 平面上的雾团 quad。
     *
     * <p>顶点顺序与 UV 配对沿用 Avaritia halo 的写法，保证 quad 法线朝 +z、朝向物品正面。
     * 顶点数据用 Avaritia 的 {@link HaloUtils#putVertex} 写入 BLOCK 格式布局，
     * 渲染时可直接交给 {@code ItemRenderer#renderQuadList}。
     *
     * @param uMin/uMax/vMin/vMax 相对贴图自身的采样窗口（0~1），用来裁出柔和雾团或避开贴图边缘渗色
     */
    private static BakedQuad quad(TextureAtlasSprite sprite, float cx, float cy, float half, float z, float rotation,
                                  float uMin, float uMax, float vMin, float vMax, int argb) {
        Quad quad = new Quad();
        quad.reset(CachedFormat.BLOCK);
        quad.setTexture(sprite);

        float cos = Mth.cos(rotation);
        float sin = Mth.sin(rotation);
        float su0 = sprite.getU0();
        float su1 = sprite.getU1();
        float sv0 = sprite.getV0();
        float sv1 = sprite.getV1();

        // 四个角依次为 (+,+) (-,+) (-,-) (+,-)，与 Avaritia HaloUtils 一致
        float[] dxs = { half, -half, -half, half };
        float[] dys = { half, half, -half, -half };
        float[] us = { uMax, uMin, uMin, uMax };
        float[] vs = { vMin, vMin, vMax, vMax };

        float r = (argb >> 16 & 0xFF) / 255.0F;
        float g = (argb >> 8 & 0xFF) / 255.0F;
        float b = (argb & 0xFF) / 255.0F;
        float a = (argb >>> 24 & 0xFF) / 255.0F;

        for (int i = 0; i < 4; i++) {
            float x = cx + dxs[i] * cos - dys[i] * sin;
            float y = cy + dxs[i] * sin + dys[i] * cos;
            HaloUtils.putVertex(quad.vertices[i], x, y, z,
                    su0 + (su1 - su0) * us[i], sv0 + (sv1 - sv0) * vs[i]);
            float[] color = quad.vertices[i].color;
            color[0] = r;
            color[1] = g;
            color[2] = b;
            color[3] = a;
        }
        quad.calculateOrientation(true);
        return quad.bake();
    }

    private static @Nullable TextureAtlasSprite sprite(net.minecraft.resources.ResourceLocation texture) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
    }

    private static float[] rgb(int rgb) {
        return new float[] { (rgb >> 16 & 0xFF) / 255.0F, (rgb >> 8 & 0xFF) / 255.0F, (rgb & 0xFF) / 255.0F };
    }

    private static float[] mix(float[] from, float[] to, float t) {
        return new float[] { Mth.lerp(t, from[0], to[0]), Mth.lerp(t, from[1], to[1]), Mth.lerp(t, from[2], to[2]) };
    }

    private static int argb(float[] color, float alpha) {
        int a = (int) (Mth.clamp(alpha, 0.0F, 1.0F) * 255.0F) & 0xFF;
        int r = (int) (Mth.clamp(color[0], 0.0F, 1.0F) * 255.0F) & 0xFF;
        int g = (int) (Mth.clamp(color[1], 0.0F, 1.0F) * 255.0F) & 0xFF;
        int b = (int) (Mth.clamp(color[2], 0.0F, 1.0F) * 255.0F) & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }
}
