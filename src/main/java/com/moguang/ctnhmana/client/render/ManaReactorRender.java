package com.moguang.ctnhmana.client.render;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.AABB;

import com.moguang.ctnhmana.Mutiblock.MachineUtils;
import com.moguang.ctnhmana.Mutiblock.ManaReactor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManaReactorRender extends DynamicRender<ManaReactor, ManaReactorRender> {

    public static final ManaReactorRender INSTANCE = new ManaReactorRender();
    public static final DynamicRenderType<ManaReactor, ManaReactorRender> TYPE = new DynamicRenderType<>(
            Codec.unit(INSTANCE));

    private static final double ORBIT_RADIUS = 8.0;     // 光晕小球旋转的半径
    private static final float ORBIT_SPEED = 0.03F;     // 旋转速度
    private static final float ORBIT_WAVE_AMP = 0.8F;   // 小球上下波浪起伏的高度幅度
    private static final int TRAIL_LENGTH = 15;          // 拖尾的球体级数

    private static class RenderState {

        float alpha = 0.0F;
        long lastTimeMillis = 0L;
        float continuousTime = -1.0F;
    }

    private final Map<BlockPos, RenderState> stateMap = new HashMap<>();

    private ManaReactorRender() {}

    @Override
    public DynamicRenderType<ManaReactor, ManaReactorRender> getType() {
        return TYPE;
    }

    @Override
    public void render(ManaReactor machine, float partialTick, PoseStack poseStack, MultiBufferSource buffer,
                       int packedLight, int packedOverlay) {
        ClientLevel level = (ClientLevel) machine.self().getLevel();
        float gameTime = machine.self().getOffsetTimer() + partialTick;
        BlockPos controllerPos = machine.self().getPos();

        RenderState state = stateMap.computeIfAbsent(controllerPos, k -> new RenderState());

        long now = System.currentTimeMillis();
        float deltaTime = state.lastTimeMillis == 0L ? 0.0F : (now - state.lastTimeMillis) / 50.0F;
        state.lastTimeMillis = now;

        if (deltaTime < 0.0F || deltaTime > 5.0F) deltaTime = 0.05F;

        boolean isCurrentlyWorking = machine.isFormed() && machine.isActive();
        if (isCurrentlyWorking) {
            state.alpha += deltaTime * 0.15F;
            if (state.alpha > 1.0F) state.alpha = 1.0F;
            state.continuousTime = gameTime;
        } else {
            state.alpha -= deltaTime * 0.05F;
            if (state.alpha <= 0.0F) {
                state.alpha = 0.0F;
                stateMap.remove(controllerPos);
                return;
            }
            if (state.continuousTime < 0.0F) {
                state.continuousTime = gameTime;
            } else {
                state.continuousTime += deltaTime;
            }
        }

        BlockPos targetPos = MachineUtils.getOffset(machine, 0, 7, 7);// hi，整体特效坐标改这里就行，这里是cu6（这算私货吗
        double offsetX = targetPos.getX() - controllerPos.getX() + 0.5;
        double offsetY = targetPos.getY() - controllerPos.getY() + 0.5;
        double offsetZ = targetPos.getZ() - controllerPos.getZ() + 0.5;
        double bobbing = Math.sin(state.continuousTime * 0.1F) * 0.15F;

        if (level != null && !Minecraft.getInstance().isPaused()) {
            renderInwardEnergyParticles(poseStack, buffer, offsetX, offsetY, offsetZ, bobbing, state.continuousTime,
                    state.alpha);
        }

        VertexConsumer lightningConsumer = buffer.getBuffer(RenderType.lightning());

        for (int i = 0; i < 2; i++) {

            double angle = (state.continuousTime * ORBIT_SPEED) + (i * Math.PI);
            double orbX = Math.cos(angle) * ORBIT_RADIUS;
            double orbZ = Math.sin(angle) * ORBIT_RADIUS;
            double orbY = Math.sin(angle * 2.0F) * ORBIT_WAVE_AMP;

            poseStack.pushPose();
            poseStack.translate(offsetX + orbX, offsetY + bobbing + orbY, offsetZ + orbZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.continuousTime * 5F));

            int mainAlpha = (int) (255 * state.alpha);
            renderSphere(poseStack, lightningConsumer, 0.35F, 8, 16, 0, 220, 255, mainAlpha);
            poseStack.popPose();

            for (int t = 1; t <= TRAIL_LENGTH; t++) {
                float trailDelay = t * 0.6F;
                double trailAngle = ((state.continuousTime - trailDelay) * ORBIT_SPEED) + (i * Math.PI);

                double tOrbX = Math.cos(trailAngle) * ORBIT_RADIUS;
                double tOrbZ = Math.sin(trailAngle) * ORBIT_RADIUS;
                double tOrbY = Math.sin(trailAngle * 2.0F) * ORBIT_WAVE_AMP;

                float trailFactor = 1.0F - ((float) t / (TRAIL_LENGTH + 1));
                float tSize = 0.35F * trailFactor;

                int tAlpha = (int) (255 * trailFactor * state.alpha);

                if (tAlpha > 0) {
                    poseStack.pushPose();
                    poseStack.translate(offsetX + tOrbX, offsetY + bobbing + tOrbY, offsetZ + tOrbZ);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-state.continuousTime * 3F));
                    renderSphere(poseStack, lightningConsumer, tSize, 6, 12, 0, 180, 255, tAlpha);
                    poseStack.popPose();
                }
            }
        }

        if (!isCurrentlyWorking) return;

        var recipeLogic = machine.getRecipeLogic();
        GTRecipe lastRecipe = recipeLogic.getLastRecipe();
        if (lastRecipe == null) return;

        List<Content> contents = lastRecipe.getInputContents(ItemRecipeCapability.CAP);
        if (contents.size() != 1) return;

        Content content = contents.get(0);
        ItemStack stackToRender = ItemStack.EMPTY;

        if (content.content instanceof Ingredient ingredient) {
            ItemStack[] items = ingredient.getItems();
            if (items.length > 0) {
                int index = (int) ((machine.self().getOffsetTimer() / 20) % items.length);
                stackToRender = items[index];
            }
        } else if (content.content instanceof ItemStack stack) {
            stackToRender = stack;
        }

        if (stackToRender.isEmpty()) return;

        double progress = recipeLogic.getProgress();
        double maxProgress = recipeLogic.getMaxProgress();
        if (maxProgress <= 0) return;

        float baseScale = 2.0F;
        float targetScale = baseScale;
        int finalOverlay = packedOverlay;
        double animTicks = Math.min(20.0, maxProgress * 0.2);

        if (progress < animTicks) {
            float alpha = (float) (progress / animTicks);
            targetScale = baseScale * alpha;
            float whiteIntensity = 1.0F - alpha;
            finalOverlay = OverlayTexture.pack(OverlayTexture.u(whiteIntensity), true);
        } else if ((maxProgress - progress) < animTicks) {
            float alpha = (float) ((maxProgress - progress) / animTicks);
            targetScale = baseScale * alpha;
            float whiteIntensity = 1.0F - alpha;
            finalOverlay = OverlayTexture.pack(OverlayTexture.u(whiteIntensity), true);
        }

        targetScale = Math.max(targetScale, 0.001F);

        poseStack.pushPose();
        poseStack.translate(offsetX, offsetY + bobbing, offsetZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(gameTime * 2.5F));
        poseStack.scale(targetScale, targetScale, targetScale);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                stackToRender,
                ItemDisplayContext.FIXED,
                0xF000F0,
                finalOverlay,
                poseStack,
                buffer,
                level,
                0);
        poseStack.popPose();
    }

    private void renderSphere(PoseStack poseStack, VertexConsumer consumer, float radius, int stacks, int slices, int r,
                              int g, int b, int a) {
        Matrix4f matrix = poseStack.last().pose();
        for (int i = 0; i < stacks; i++) {
            float lat0 = (float) Math.PI * (-0.5f + (float) i / stacks);
            float z0 = (float) Math.sin(lat0) * radius;
            float r0 = (float) Math.cos(lat0) * radius;

            float lat1 = (float) Math.PI * (-0.5f + (float) (i + 1) / stacks);
            float z1 = (float) Math.sin(lat1) * radius;
            float r1 = (float) Math.cos(lat1) * radius;

            for (int j = 0; j <= slices; j++) {
                float lng0 = (float) (2.0 * Math.PI * (float) j / slices);
                float x0 = (float) Math.cos(lng0);
                float y0 = (float) Math.sin(lng0);

                float lng1 = (float) (2.0 * Math.PI * (float) (j + 1) / slices);
                float x1 = (float) Math.cos(lng1);
                float y1 = (float) Math.sin(lng1);

                consumer.vertex(matrix, x0 * r0, y0 * r0, z0).color(r, g, b, a).endVertex();
                consumer.vertex(matrix, x1 * r0, y1 * r0, z0).color(r, g, b, a).endVertex();
                consumer.vertex(matrix, x1 * r1, y1 * r1, z1).color(r, g, b, a).endVertex();
                consumer.vertex(matrix, x0 * r1, y0 * r1, z1).color(r, g, b, a).endVertex();
            }
        }
    }

    private void renderInwardEnergyParticles(PoseStack poseStack, MultiBufferSource buffer, double offsetX,
                                             double offsetY, double offsetZ, double bobbing, float gameTime,
                                             float globalAlpha) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.lightning());
        int particleCount = 16;

        for (int p = 0; p < particleCount; p++) {
            float pProgress = ((gameTime + p * 8.7F) % 60F) / 60F;
            float currentRadius = (1.0F - pProgress) * 7.5F + 0.1F;

            double theta = p * 2.399963229728653;
            double phi = Math.acos(1.0 - 2.0 * ((p + 0.5) / particleCount));

            double px = currentRadius * Math.sin(phi) * Math.cos(theta);
            double pz = currentRadius * Math.sin(phi) * Math.sin(theta);
            double py = currentRadius * Math.cos(phi);

            int alpha = 255;
            if (pProgress < 0.15F) alpha = (int) (255 * (pProgress / 0.15F));
            if (pProgress > 0.85F) alpha = (int) (255 * ((1.0F - pProgress) / 0.15F));

            alpha = (int) (alpha * globalAlpha);

            if (alpha > 0) {
                float pSize = 0.06F * (1.2F - pProgress);
                poseStack.pushPose();
                poseStack.translate(offsetX + px, offsetY + bobbing + py, offsetZ + pz);
                renderSphere(poseStack, consumer, pSize, 3, 6, 200, 245, 255, alpha);
                poseStack.popPose();
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen(ManaReactor machine) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(ManaReactor machine) {
        return new AABB(machine.self().getPos()).inflate(ORBIT_RADIUS + 2.0);
    }
}
