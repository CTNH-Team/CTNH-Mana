package com.magicbee.ctnhmana.client.render;

import net.minecraft.client.renderer.RenderType;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

/**
 * 究极魔力锭紫色迷雾的渲染层。
 *
 * <p>刻意沿用原版半透明层（{@code RenderType.TRANSLUCENT}）的 BLOCK 顶点格式与方块图集采样，
 * 只把混合模式换成加法、关掉面剔除并禁止写深度：
 * <ul>
 * <li>加法混合让迷雾只“发光”而不压暗物品；</li>
 * <li>不写深度保证迷雾永远不会挡住自己的物品；</li>
 * <li>BLOCK 格式与原版物品渲染 {@code ItemRenderer#renderModelLists} 写入的 quad 布局完全一致。</li>
 * </ul>
 */
public final class UltraManaMistRenderType extends RenderType {

    private static final RenderType MIST = RenderType.create("ctnhmana_mana_mist",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, SMALL_BUFFER_SIZE, false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(ADDITIVE_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setWriteMaskState(COLOR_WRITE)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(NO_OVERLAY)
                    .createCompositeState(false));

    private UltraManaMistRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize,
                                    boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState,
                                    Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType mist() {
        return MIST;
    }
}
