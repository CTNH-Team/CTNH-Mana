package com.magicbee.ctnhmana.common.item.equipment;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.magicbee.ctnhmana.registry.CMMobEffects;
import com.magicbee.ctnhmana.utils.CTNHManaUtils;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.proxy.Proxy;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import java.util.List;
import java.util.Objects;

public class TaintedBloodWeepingEye extends BaubleItem {

    public static final String TAG_TIMER = "tbe_timer";
    public static final String TAG_COOLDOWN = "tbe_cooldown";
    public static final String TAG_INJECTED = "tbe_injected";

    /** 每 11 秒注入一次 */
    public static final int INJECT_INTERVAL = 11 * 20;
    public static final int INJECT_LP = 5140;
    public static final int INJECT_DAMAGE = 5;
    public static final float MIN_HEALTH_FOR_INJECT = 5.0F;
    /** 灵魂网络注入上限：1000W */
    public static final int NETWORK_CAP = 10_000_000;
    /** 致死触发最多消耗：5.14W */
    public static final int MAX_CONSUME_LP = 51_400;
    public static final int LP_PER_UNIT = 5140;
    public static final float HEAL_PER_UNIT = 10.0F;
    public static final float AOE_DAMAGE = 11.0F;
    public static final double AOE_RADIUS = 5.0D;
    public static final int SLOW_DURATION = 5 * 20;
    public static final int SLOW_AMPLIFIER = 2; // 缓慢 III
    public static final int TAINTED_BLOOD_DURATION = 10 * 20;
    /** 冷却所需累计注入：5.14W */
    public static final int COOLDOWN_INJECT_NEED = 51_400;

    public TaintedBloodWeepingEye(Properties props) {
        super(props);
        Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this,
                new vazkii.botania.common.item.equipment.bauble.ThirdEyeItem.Renderer()));
    }

    public static boolean isOnCooldown(ItemStack stack) {
        return ItemNBTHelper.getBoolean(stack, TAG_COOLDOWN, false);
    }

    public static void startCooldown(ItemStack stack) {
        ItemNBTHelper.setBoolean(stack, TAG_COOLDOWN, true);
        ItemNBTHelper.setInt(stack, TAG_INJECTED, 0);
    }

    public static int getInjectedForCooldown(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_INJECTED, 0);
    }

    private static void addInjected(ItemStack stack, int amount) {
        int next = getInjectedForCooldown(stack) + amount;
        ItemNBTHelper.setInt(stack, TAG_INJECTED, next);
        if (next >= COOLDOWN_INJECT_NEED) {
            ItemNBTHelper.setBoolean(stack, TAG_COOLDOWN, false);
            ItemNBTHelper.setInt(stack, TAG_INJECTED, 0);
        }
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity living) {
        if (!(living instanceof Player player) || living.level().isClientSide()) {
            return;
        }

        int timer = ItemNBTHelper.getInt(stack, TAG_TIMER, 0) + 1;
        if (timer < INJECT_INTERVAL) {
            ItemNBTHelper.setInt(stack, TAG_TIMER, timer);
            return;
        }
        ItemNBTHelper.setInt(stack, TAG_TIMER, 0);

        if (player.getHealth() <= MIN_HEALTH_FOR_INJECT) {
            return;
        }

        SoulNetwork network = NetworkHelper.getSoulNetwork(player);
        int current = network.getCurrentEssence();
        int room = Math.max(0, NETWORK_CAP - current);
        int inject = Math.min(INJECT_LP, room);
        if (inject <= 0) {
            return;
        }

        player.setHealth(player.getHealth() - INJECT_DAMAGE);
        network.add(new SoulTicket(inject), NETWORK_CAP);

        if (isOnCooldown(stack)) {
            addInjected(stack, inject);
        }
        if (player.hasEffect(CMMobEffects.TAINTED_BLOOD.get())) {
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

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        super.appendHoverText(stack, world, tooltip, flags);
        tooltip = CTNHManaUtils.itemTooltipsAdd(taintedBloodEyeLang, tooltip);
        if (isOnCooldown(stack)) {
            tooltip.add(taintedBloodCooldownLang.translate(
                    getInjectedForCooldown(stack), COOLDOWN_INJECT_NEED));
        } else {
            tooltip.add(taintedBloodReadyLang.translate());
        }
    }

    @CN({
            "§4§o此般污秽和崩坏的心灵已无可供升入之修罗之天……所有肮脏与污秽的心灵,与我一同堕入永恒的阿鼻吧！",
            "§c穿戴时：所受伤害增加25%",
            "§c生命值高于5点时，每11秒受到5点伤害，并向灵魂网络注入5140 LP（至多注入到1000W）",
            "§d受到致死伤害时：消耗灵魂网络至多5.14W LP",
            "§d每消耗5140 LP：恢复10生命值，并对5格内敌人造成11点伤害与缓慢III（5秒）",
            "§4随后获得「污血」10秒：造成伤害+514%，激活紧闭的第三只眼效果；期间受到致死伤害将生命保留至1",
            "§8冷却：直至本饰品累计向灵魂网络注入5.14W LP",
            "§8§o这样是否就能抹去我与她的泪迹呢?",
    })
    @EN({
            "§4§oHearts so defiled and shattered have no place left among the asura heavens... All souls stained by filth and misunderstanding—fall with me into eternal Avīci!",
            "§cWhile worn: damage taken +25%",
            "§cWhen HP > 5, every 11s take 5 damage and inject 5140 LP (cap 10,000,000)",
            "§dOn lethal damage: consume up to 51,400 LP from the soul network",
            "§dPer 5140 LP: heal 10 HP, deal 11 damage and Slow III (5s) to enemies in radius 5",
            "§4Then gain Tainted Blood for 10s: damage dealt +514%, activate Closed Third Eye; lethal hits leave you at 1 HP",
            "§8Cooldown until this bauble has injected 51,400 LP into the soul network",
            "§8§oCould this wipe away our tears?",
    })
    public static Lang[] taintedBloodEyeLang;

    @CN("§4冷却中：已注入 %d / %d LP")
    @EN("§4Cooldown: injected %d / %d LP")
    public static Lang taintedBloodCooldownLang;

    @CN("§a污血泣泪 终：就绪")
    @EN("§aLethal save: ready")
    public static Lang taintedBloodReadyLang;

    @CN({
            "§4消失了，一切都消失了",
            "§4好痛，好痛，好痛，好痛......",
            "§4对不起对不起对不起对不起对不起",
            "§4我是坏孩子，请饶恕我......姐姐",
            "§4什么都不剩下了，什么都不剩下了"
    })
    @EN({
            "Gone, everything is gone",
            "It hurts, it hurts, it hurts, it hurts......",
            "I'm sorry I'm sorry I'm sorry I'm sorry I'm sorry",
            "I'm a bad child, please forgive me...... sister",
            "Nothing left, nothing left at all"
    })
    public static Lang[] taintedBloodHoverLang;
}
