package com.moguang.ctnhmana.common.item.equipment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.proxy.Proxy;

import java.util.List;
import java.util.Objects;

public class KoishiEyeItem extends BaubleItem {

    private static final int COST = 0;

    public KoishiEyeItem(Properties props) {
        super(props);
        Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this,
                new KoishiEyeItem.Renderer()));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        super.appendHoverText(stack, world, tooltip, flags);
        tooltip = (CTNHManaUtils.itemTooltipsAdd(koishi_thirdeye_tooltips, tooltip));
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity living) {
        if (!(living instanceof Player eplayer)) {
            return;
        }
        if (living instanceof Player player) {
            var time = Objects.requireNonNull(player.level()).getDayTime() % 20;
            if (time != 0) return;
            List<LivingEntity> entityList = player.level().getNearbyEntities(
                    LivingEntity.class,
                    TargetingConditions.forCombat().range(8),
                    player,
                    player.getBoundingBox().inflate(8));
            for (LivingEntity entity : entityList) {
                if (entity instanceof Mob mob) {
                    mob.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(1);
                    if (Objects.equals(mob.getTarget(), player)) mob.setTarget(null);

                }
            }
        }
    }

    public static class Renderer implements AccessoryRenderer {

        @Override
        public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms,
                             MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount,
                             float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            boolean armor = !living.getItemBySlot(EquipmentSlot.CHEST).isEmpty();

            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);

            double time = ClientTickHandler.total() * 0.12;
            double dist = 0.05;
            ms.translate(Math.sin(time) * dist, Math.cos(time * 0.5) * dist, 0);
            ms.scale(0.75F, 0.75F, 1F);
            ms.translate(0, 0.1, -0.025);

            ms.translate(-0.3, 0.6, armor ? 0.10 : 0.15);
            ms.scale(0.6F, -0.6F, -0.6F);
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.NONE,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    ms,
                    buffers,
                    living.level(),
                    living.getId());
            ms.popPose();
        }
    }

    @CN({
            "佩戴时获得:",
            "§4无意识蒙蔽了视野§r",
            "所有人都难以产察觉到地底微不足道的小石！",
            "消除与所有生物只有一面之缘的§m你§r（§7§oKOISHI§r）的存在",
            "§7§o以自己的意识，换来虚伪的幸福，这就是身为§l§d【恋】§r§7§o之少女的被厌恶者的哲学§r"
    })
    @EN({
            "佩戴时获得:",
            "§4无意识蒙蔽了视野§r",
            "所有人都难以产察觉到地底微不足道的小石!",
            "消除所有与所有生物对只有一面之缘的§m你§r（§7§oKOISHI§r）的认知",
            "§7§o以自己的意识，换来虚伪的幸福，这就是身为§l§d【恋】§r§7§o之少女的被厌恶者的哲学§r"
    })
    public static Lang[] koishi_thirdeye_tooltips;
}
