package com.moguang.ctnhmana.client.render;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;

import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.moguang.ctnhmana.common.multiblock.DemonWillMachine;
import com.moguang.ctnhmana.common.multiblock.MachineUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import org.joml.Matrix4f;

public class DemonWillRender extends DynamicRender<IMachineFeature, DemonWillRender> {

    public static Codec<DemonWillRender> CODEC = Codec.unit(DemonWillRender::new);
    public static final DynamicRenderType<IMachineFeature, DemonWillRender> TYPE = new DynamicRenderType<>(
            DemonWillRender.CODEC);

    // 五种恶魔意志的代表颜色 (ARGB)
    private static final int[] WILL_COLORS = {
            0xFF88FFFF, 0xFFFF3333, 0xFF33FF33, 0xFF3333FF, 0xFFFF8800
    };

    // 动画周期常量
    private static final float ANIMATION_DURATION_TICKS = 80.0f; // 动画一个周期的 Tick 数

    // 方块坐标（即整个特效位置）
    private static final int MAIN_TOWER_OFFSET_X = 0;
    private static final int MAIN_TOWER_OFFSET_Y = 12;
    private static final int MAIN_TOWER_OFFSET_Z = 1;

    // 方块坐标的相对高度 (在方块坐标上的高度偏移)
    private static final float MAIN_TOWER_CENTER_Y = 16.0f;  // 塔中间的Y坐标
    private static final float MAIN_TOWER_TOP_TIP = 30.0f;   // 塔尖的Y坐标
    private static final float MAIN_TOWER_BOT_TIP = 5.0f;    // 塔底尖的Y坐标
    private static final float MAIN_TOWER_MAX_RADIUS = 9.0f; // 塔中间发射时的光环最大半径

    // 四个小塔坐标
    private static final int[][] SMALL_TOWER_OFFSETS = {
            { 9, 30, 9 }, { -9, 30, 9 }, { 9, 30, -9 }, { -9, 30, -9 }
    };

    private static final float SMALL_TOWER_BASE_Y = 5.0f;
    private static final float SMALL_TOWER_TIP_Y = 25.0f;
    private static final float SMALL_TOWER_MAX_RADIUS = 1.5f;

    public DemonWillRender() {}

    @Override
    public DynamicRenderType<IMachineFeature, DemonWillRender> getType() {
        return TYPE;
    }

    @Override
    public int getViewDistance() {
        return 64;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(IMachineFeature feature, float gameTime, PoseStack poseStack, MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {
        var metaMachine = feature.self();
        if (metaMachine instanceof DemonWillMachine machine && machine.isFormed() && (machine.isActive() ||
                machine.getLevel() instanceof TrackedDummyWorld)) {

            float time = machine.getLevel().getGameTime() + gameTime;
            VertexConsumer builder = buffer.getBuffer(RenderType.lightning());

            // 获取控制器坐标系起点
            BlockPos controllerPos = machine.self().getPos();

            poseStack.pushPose();

            renderMainTowerAnimation(machine, controllerPos, time, poseStack, builder);
            renderSmallTowersAnimation(machine, controllerPos, time, poseStack, builder);

            poseStack.popPose();
        }
    }

    private void renderMainTowerAnimation(DemonWillMachine machine, BlockPos controllerPos, float time,
                                          PoseStack poseStack, VertexConsumer builder) {
        BlockPos targetPos = MachineUtils.getOffset(machine, MAIN_TOWER_OFFSET_X, MAIN_TOWER_OFFSET_Y,
                MAIN_TOWER_OFFSET_Z);

        double offsetX = targetPos.getX() - controllerPos.getX() + 0.5;
        double offsetY = targetPos.getY() - controllerPos.getY() + 0.5;
        double offsetZ = targetPos.getZ() - controllerPos.getZ() + 0.5;

        float distanceTop = MAIN_TOWER_TOP_TIP - MAIN_TOWER_CENTER_Y;
        float distanceBot = MAIN_TOWER_CENTER_Y - MAIN_TOWER_BOT_TIP;

        for (int i = 0; i < 5; i++) {
            float offsetTime = time + (i * (ANIMATION_DURATION_TICKS / 5.0f));
            float progress = (offsetTime % ANIMATION_DURATION_TICKS) / ANIMATION_DURATION_TICKS;

            int color = WILL_COLORS[i];
            float r = ((color >> 16) & 0xFF) / 255.0f;
            float g = ((color >> 8) & 0xFF) / 255.0f;
            float b = (color & 0xFF) / 255.0f;

            float currentRadius = MAIN_TOWER_MAX_RADIUS * (1.0f - (float) Math.pow(progress, 1.5));
            float alpha = 1.0f - progress;
            float ringWidth = 0.2f * (1.0f - progress);

            // 1. 上侧光环
            float currentYTop = (float) offsetY + MAIN_TOWER_CENTER_Y + (distanceTop * progress);
            poseStack.pushPose();
            poseStack.translate(offsetX, currentYTop, offsetZ);
            renderRing(poseStack.last().pose(), builder, currentRadius, ringWidth, r, g, b, alpha);
            poseStack.popPose();

            // 2. 下侧光环
            float currentYBot = (float) offsetY + MAIN_TOWER_CENTER_Y - (distanceBot * progress);
            poseStack.pushPose();
            poseStack.translate(offsetX, currentYBot, offsetZ);
            renderRing(poseStack.last().pose(), builder, currentRadius, ringWidth, r, g, b, alpha);
            poseStack.popPose();
        }
    }

    private void renderSmallTowersAnimation(DemonWillMachine machine, BlockPos controllerPos, float time,
                                            PoseStack poseStack, VertexConsumer builder) {
        float distance = SMALL_TOWER_TIP_Y - SMALL_TOWER_BASE_Y;

        for (int[] offset : SMALL_TOWER_OFFSETS) {

            BlockPos targetPos = MachineUtils.getOffset(machine, offset[0], offset[1], offset[2]);

            double offsetX = targetPos.getX() - controllerPos.getX() + 0.5;
            double offsetY = targetPos.getY() - controllerPos.getY() + 0.5;
            double offsetZ = targetPos.getZ() - controllerPos.getZ() + 0.5;

            for (int j = 0; j < 3; j++) {
                float offsetTime = time + (j * (ANIMATION_DURATION_TICKS / 3.0f));
                float progress = (offsetTime % ANIMATION_DURATION_TICKS) / ANIMATION_DURATION_TICKS;

                float currentRadius = SMALL_TOWER_MAX_RADIUS * (1.0f - progress);
                float alpha = (1.0f - progress) * 0.8f;
                float ringWidth = 0.1f;
                float currentY = (float) offsetY + SMALL_TOWER_BASE_Y + (distance * progress);

                poseStack.pushPose();
                poseStack.translate(offsetX, currentY, offsetZ);
                renderRing(poseStack.last().pose(), builder, currentRadius, ringWidth, 0.8f, 1.0f, 1.0f, alpha);
                poseStack.popPose();
            }
        }
    }

    private void renderRing(Matrix4f matrix, VertexConsumer builder, float radius, float width, float r, float g,
                            float b, float a) {
        int segments = 32;
        float innerRadius = Math.max(0.01f, radius - width);
        float outerRadius = radius + width;

        for (int i = 0; i <= segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) * 2 * Math.PI / segments);

            float sin1 = Mth.sin(angle);
            float cos1 = Mth.cos(angle);
            float sin2 = Mth.sin(nextAngle);
            float cos2 = Mth.cos(nextAngle);

            builder.vertex(matrix, innerRadius * cos1, 0, innerRadius * sin1).color(r, g, b, a).endVertex();
            builder.vertex(matrix, outerRadius * cos1, 0, outerRadius * sin1).color(r, g, b, 0.0f).endVertex();
            builder.vertex(matrix, outerRadius * cos2, 0, outerRadius * sin2).color(r, g, b, 0.0f).endVertex();
            builder.vertex(matrix, innerRadius * cos2, 0, innerRadius * sin2).color(r, g, b, a).endVertex();
        }
    }

    @Override
    public AABB getRenderBoundingBox(IMachineFeature machine) {
        BlockPos pos = machine.self().getPos();

        return new AABB(pos).inflate(64.0, 64.0, 64.0);
    }
}