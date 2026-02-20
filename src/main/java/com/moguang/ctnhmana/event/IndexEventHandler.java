package com.moguang.ctnhmana.event;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.client.render.particle.ParticleRegistry;
import com.moguang.ctnhmana.networking.packets.IndexTargetBlockPacket;
import com.moguang.ctnhmana.networking.packets.IndexTargetParticlePacket;
import com.moguang.ctnhmana.registry.CMMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK;

@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class IndexEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide) {

            return; // 只服务端逻辑时可加
        }

        long tick = player.level().getGameTime();
        if (tick % 100 == 0) {
            var tags = player.getPersistentData();
            if (tags.contains("index_target_block") && player.level().getGameTime() % 100 == 0) {
                var posArray = tags.getIntArray("index_target_block");
                NETWORK.sendToTrackingChunk(new IndexTargetBlockPacket(posArray), new LevelChunk(player.level(), new ChunkPos(player.getOnPos())));

                if (tags.contains("karma") && player.getEffect(CMMobEffects.Karma.get()) == null) {
                    MobEffectInstance karmaEffect = new MobEffectInstance(
                            CMMobEffects.Karma.get(),
                            20 * 10 * 60,
                            tags.getInt("karma") + 1,
                            false,
                            true
                    );
                    MobEffectInstance darkEffect = new MobEffectInstance(
                            MobEffects.DARKNESS,
                            20 * 7,
                            1,
                            false,
                            true
                    );
                    player.addEffect(karmaEffect);
                    player.addEffect(darkEffect);

                }
                if (tags.contains("karma_fortuna") && player.getEffect(CMMobEffects.KarmaFortuna.get()) == null) {
                    MobEffectInstance karmaEffectFortuna = new MobEffectInstance(
                            CMMobEffects.KarmaFortuna.get(),
                            20 * 10 * 60,
                            tags.getInt("karma_fortuna"),
                            false,
                            true
                    );
                    MobEffectInstance darkEffect = new MobEffectInstance(
                            MobEffects.DARKNESS,
                            20 * 7,
                            1,
                            false,
                            true
                    );
                    player.addEffect(karmaEffectFortuna);
                    player.addEffect(darkEffect);
                }
            }

        }
    }
    @SubscribeEvent
    public static void LivingDeathEvent(LivingDeathEvent event) {
        var killer=event.getSource().getEntity();
        var entity=event.getEntity();
        if(!entity.level().isClientSide()&&entity.hasEffect(CMMobEffects.indextarget.get())&&(killer instanceof Player player)&&killer.getPersistentData().contains("index_target")&&player.getPersistentData().getString("index_target").equals(entity.getStringUUID()))
        {
            player.getPersistentData().remove("index_target");
            if(!player.hasEffect(CMMobEffects.Bladeunleashed.get()))
            {
                player.addEffect(new MobEffectInstance(
                        CMMobEffects.Bladeunleashed.get(),
                        20*30*60,
                        0,
                        true,
                        true));
            }
            else
            {
                var amplifier=player.getEffect(CMMobEffects.Bladeunleashed.get()).getAmplifier();
                player.addEffect(new MobEffectInstance(
                        CMMobEffects.Bladeunleashed.get(),
                        20*30*60,
                        Math.min(3,amplifier+1),
                        true,
                        true));
            }

        }
    }

//    @SubscribeEvent
//    public static void onRenderLivingCenterIcon(
//            RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
//        LivingEntity entity = event.getEntity();
//        PoseStack poseStack = event.getPoseStack();
//        MultiBufferSource bufferSource = event.getMultiBufferSource();
//        int packedLight = event.getPackedLight();
//        var p=entity.hasEffect(MobEffects.DARKNESS);
////        if(!entity.getPersistentData().contains("index_targeted"))return;
//
//
//        Vec3 centerPos = entity.getBoundingBox().getCenter();
//        // 将渲染坐标转换为相对于实体的本地坐标（避免随实体移动偏移）
//        poseStack.pushPose();
//
//        // 3. 定位到实体正中心
//        Vec3 localCenter = centerPos.subtract(entity.position());
//        poseStack.translate(localCenter.x(), localCenter.y(), localCenter.z());
//
//
//
//        // ========== 图标缩放+防遮挡 ==========
//        poseStack.scale(0.03F, 0.03F, 0.6F); // 去掉负号，避免翻转
//        poseStack.translate(0.0F, 0.0F, 1.0F); // 前移0.1格，避免被实体遮挡
//        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(CMGuiTextures.INDEX_TARGET.imageLocation));
//        int iconSize = 64;
//        int halfSize = iconSize / 2;
//        blitCenteredIcon(poseStack, consumer, -halfSize, -halfSize, iconSize, iconSize, packedLight);
//        poseStack.popPose();
//    }
//    private static void blitCenteredIcon(PoseStack poseStack, VertexConsumer consumer, int x, int y, int width, int height, int packedLight) {
//        Minecraft minecraft = Minecraft.getInstance();
//        // 绑定图标纹理
//        minecraft.getTextureManager().bindForSetup(CMGuiTextures.INDEX_TARGET.imageLocation);
//
//        // 纹理UV坐标（16x16图标）
//        float u0 = 0.0F;
//        float u1 = 1.0F;
//        float v0 = 0.0F;
//        float v1 = 1.0F;
//
//        // 绘制四边形（中心对齐）
//        poseStack.pushPose();
//        // 左下顶点
//        consumer.vertex(poseStack.last().pose(), (float) x, (float) (y + height), 0.0F)
//                .color(1.0F, 1.0F, 1.0F, 1.0F)
//                .uv(u0, v1)
//                .overlayCoords(OverlayTexture.NO_OVERLAY)
//                .uv2(packedLight)
//                .normal(0.0F, 0.0F, 1.0F)
//                .endVertex();
//        // 右下顶点
//        consumer.vertex(poseStack.last().pose(), (float) (x + width), (float) (y + height), 0.0F)
//                .color(1.0F, 1.0F, 1.0F, 1.0F)
//                .uv(u1, v1)
//                .overlayCoords(OverlayTexture.NO_OVERLAY)
//                .uv2(packedLight)
//                .normal(0.0F, 0.0F, 1.0F)
//                .endVertex();
//        // 右上顶点
//        consumer.vertex(poseStack.last().pose(), (float) (x + width), (float) y, 0.0F)
//                .color(1.0F, 1.0F, 1.0F, 1.0F)
//                .uv(u1, v0)
//                .overlayCoords(OverlayTexture.NO_OVERLAY)
//                .uv2(packedLight)
//                .normal(0.0F, 0.0F, 1.0F)
//                .endVertex();
//        // 左上顶点
//        consumer.vertex(poseStack.last().pose(), (float) x, (float) y, 0.0F)
//                .color(1.0F, 1.0F, 1.0F, 1.0F)
//                .uv(u0, v0)
//                .overlayCoords(OverlayTexture.NO_OVERLAY)
//                .uv2(packedLight)
//                .normal(0.0F, 0.0F, 1.0F)
//                .endVertex();
//        poseStack.popPose();
//    }
    }