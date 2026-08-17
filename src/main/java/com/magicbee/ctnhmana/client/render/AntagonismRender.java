package com.magicbee.ctnhmana.client.render;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 在携带“物理拮抗/奥法拮抗/苦难护盾”等效果的怪物头顶渲染对应 buff 的图标（始终面向玩家，多个图标并排）。
 * 1.20.1 原版不会把怪物的效果同步给客户端，因此效果数据来自服务端周期性推送的
 * {@link com.magicbee.ctnhmana.networking.packets.AntagonismPacket}。
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class AntagonismRender {

    /** 客户端缓存：实体 id -> 各效果的到期时刻（客户端世界 tick）。 */
    private static final Map<Integer, Map<MobEffect, Long>> EXPIRY_TICKS = new HashMap<>();

    /** 图标半宽（方块单位），2x2 四边形缩放后约 0.6 方块宽。 */
    private static final float ICON_HALF_WIDTH = 0.3F;
    /** 图标底部离头顶的高度。 */
    private static final float ICON_HEAD_OFFSET = 0.35F;
    /** 多个图标并排时相邻中心间距（缩放前单位）。 */
    private static final float ICON_SPACING = 2.4F;

    /** 由服务端的同步包更新效果到期时刻。 */
    public static void updateExpiry(int entityId, MobEffect effect, long expireGameTime) {
        if (expireGameTime <= 0) {
            Map<MobEffect, Long> active = EXPIRY_TICKS.get(entityId);
            if (active != null) {
                active.remove(effect);
            }
        } else {
            EXPIRY_TICKS.computeIfAbsent(entityId, key -> new HashMap<>()).put(effect, expireGameTime);
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            return; // 只给怪物显示
        }
        Map<MobEffect, Long> active = EXPIRY_TICKS.get(entity.getId());
        if (active == null || active.isEmpty()) {
            return;
        }
        long gameTime = entity.level().getGameTime();
        List<TextureAtlasSprite> sprites = new ArrayList<>();
        Iterator<Map.Entry<MobEffect, Long>> iterator = active.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<MobEffect, Long> entry = iterator.next();
            if (gameTime > entry.getValue()) {
                iterator.remove(); // 效果已结束，清理缓存
                continue;
            }
            TextureAtlasSprite sprite = Minecraft.getInstance().getMobEffectTextures().get(entry.getKey());
            if (sprite != null) {
                sprites.add(sprite);
            }
        }
        if (sprites.isEmpty()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 pos = entity.getPosition(event.getPartialTick()).subtract(camera.getPosition());

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y + entity.getBbHeight() + ICON_HEAD_OFFSET, pos.z);
        // 广告牌：旋转到始终面向相机
        poseStack.mulPose(camera.rotation());
        poseStack.scale(ICON_HALF_WIDTH, ICON_HALF_WIDTH, ICON_HALF_WIDTH);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        // 效果图标共用同一个 atlas
        RenderSystem.setShaderTexture(0, sprites.get(0).atlasLocation());

        // 多个图标并排居中
        float startX = -((sprites.size() - 1) * ICON_SPACING) / 2.0F;
        for (int i = 0; i < sprites.size(); i++) {
            drawIcon(poseStack.last().pose(), sprites.get(i), startX + i * ICON_SPACING);
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private static void drawIcon(Matrix4f matrix, TextureAtlasSprite sprite, float centerX) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, centerX - 1.0F, 1.0F, 0.0F).uv(sprite.getU0(), sprite.getV1()).endVertex();
        buffer.vertex(matrix, centerX + 1.0F, 1.0F, 0.0F).uv(sprite.getU1(), sprite.getV1()).endVertex();
        buffer.vertex(matrix, centerX + 1.0F, -1.0F, 0.0F).uv(sprite.getU1(), sprite.getV0()).endVertex();
        buffer.vertex(matrix, centerX - 1.0F, -1.0F, 0.0F).uv(sprite.getU0(), sprite.getV0()).endVertex();
        tesselator.end();
    }
}
