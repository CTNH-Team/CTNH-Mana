package com.moguang.ctnhmana.item.equipment;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.common.effects.TemperatureImmunityEffect;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.proxy.Proxy;
import java.util.List;
import java.util.Objects;

import static sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry.TEMPERATURE_IMMUNITY;

public class YurikoRingItem extends BaubleItem {
    Long timer=0L;
    @Persisted
    public boolean is_armor_boost=false;
    public YurikoRingItem(Properties props) {
        super(props);
        Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new vazkii.botania.common.item.equipment.bauble.ThirdEyeItem.Renderer()));
    }
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags)
    {
        super.appendHoverText(stack,world,tooltip,flags);
        tooltip=(CTNHManaUtils.itemTooltipsAdd(yuriko_ring_lang,tooltip));
        if(timer>0L)tooltip.add(yuriko_heal_cooldown.translate(timer/20));
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity living) {

        if (!(living instanceof Player eplayer)) {
            return;
        }
        if(living instanceof Player player)
        {
            if(player.level() instanceof ClientLevel)return;
            var time = Objects.requireNonNull(player.level()).getDayTime() % 20;
            if(time!=0)return;
            var armor=player.getAttribute(Attributes.ARMOR);
            AttributeModifier armorBoost = new AttributeModifier(
                    "YurikoArmorBoost",
                    10,// ）
                    AttributeModifier.Operation.ADDITION
            );
            if(armor.getValue()==0)
            {
                armor.addPermanentModifier(armorBoost);
                is_armor_boost=true;
            }
            else if(is_armor_boost&&armor.getValue()>10)
            {
                armor.removeModifier(armorBoost);
                is_armor_boost=false;
            }
            CompoundTag playerTag = player.getPersistentData();
            long currentTick = player.level().getGameTime();
            if((playerTag.get("Yuriko_revival_cooldown")==null||playerTag.getInt(("Yuriko_revival_cooldown"))<=currentTick)&&player.getHealth()<=5)
            {
                playerTag.putLong("Yuriko_revival_cooldown",currentTick+20*1000);
                timer=currentTick+20*1000-player.level().getGameTime();
                YurikoHealing(player);
            }
            else
            {
                timer=playerTag.getLong("Yuriko_revival_cooldown")-currentTick;
            }
        }
    }
    public void YurikoHealing(Player player)
    {
        var time=200;
        player.heal(10);
        MobEffectInstance resistance = new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE,    // 效果类型：抗性提升
                time,          // 时长：200tick=10秒
                4,                        // 等级：5（注意：MC中等级从0开始，V对应4）
                false,                    // 是否显示粒子效果（true=显示，false=隐藏）
                true                     // 是否显示图标（true=显示在屏幕右侧）
        );
        // 2. 生命恢复II（等级2，10秒）
        MobEffectInstance regeneration = new MobEffectInstance(
                MobEffects.REGENERATION,
                time,
                1,  // 等级2（II对应1）
                false,
                true
        );
        // 3. 迅捷V（等级5，10秒）
        MobEffectInstance speed = new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED,
                time,
                4,  // 等级5（V对应4）
                false,
                true
        );
        MobEffectInstance CT=new MobEffectInstance(
                TEMPERATURE_IMMUNITY.get(),
                time,
                4,
                false,
                true
        );
        player.addEffect(regeneration);
        player.addEffect(resistance);
        player.addEffect(speed);
        player.addEffect(CT);
    }
    @CN(
            {
                    "§6精致的做工，散发着些许百合气息，尽管黯淡，但总感觉充满§m不幸§r§6希望！",
                    "§4祂一直在§o渴求你§r，§4只是还不够......§r",
                    "§d佩戴时获得：",
                    "§d抗寒+2",
                    "§d抗暑+2",
                    "§d攻击时有20%几率触发§kYuriko§r§d的连击，对目标的伤害提升至150%，为目标周围2格内的玩家恢复伤害量10%的生命并对非玩家造成25%的溅射伤害",
                    "§d如果你没有护甲值，为你提供10点护甲",
                    "§d在你的生命低于5时，§kYuriko§r§d会立即为你恢复5点生命值，获得持续10秒的恒温X，抗性提升V和速度V，这个效果每10分钟只能触发一次",
                    "§c你能听到祂的呼唤，接受祂，你将走向那充满辉煌的道路，但是铭记一切§m都有§r§c代价都是值得的",
                    "§7还不是现在"
            }
    )
    @EN(
            {
                    "§6精致的做工，散发着些许百合气息，尽管黯淡，但总感觉充满§m不幸§r§6希望！",
                    "§4祂一直在§o渴求你§r，§4只是还不够......§r",
                    "§d佩戴时获得：",
                    "§d抗寒+2",
                    "§d",
                    "§d攻击时有20%几率触发§kYuriko§r§c的连击，对目标的伤害提升至150%，为目标周围2格内的玩家恢复伤害量10%的生命并对非玩家造成25%的溅射伤害",
                    "§d如果你没有护甲值，为你提供10点护甲",
                    "§d在你的生命低于5时，§kYuriko§r§c会立即为你恢复5点生命值，获得持续10秒的恒温X，抗性提升V和速度V，这个效果每10分钟只能触发一次",
                    "§c你能听到祂的呼唤，接受祂，你将走向那充满辉煌的道路，但是铭记一切§m都有§r§c代价都是值得的",
                    "§7还不是现在"
            }
    )
    public static Lang[] yuriko_ring_lang;
    @CN("剩余冷却时间：%s s")
    public static Lang yuriko_heal_cooldown;
}
