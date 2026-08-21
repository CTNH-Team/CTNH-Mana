package com.magicbee.ctnhmana.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CMConfig;
import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.common.capability.DamageClampCapability;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

/**
 * 中央"非护甲减伤钳制器"。
 * <p>
 * 神话/匠魂等模块通过各自 {@code LivingHurtEvent}/{@code LivingDamageEvent}
 * 监听做百分比减伤，乘法链叠加后可超过 100%，使伤害失去意义且难以逐个修改。
 * 本处理器在护甲计算之前(<b>LivingHurtEvent</b>)把「各 mod 累计造成的非护甲
 * 减伤倍率」用温和 sigmoid 收缩到 config 规定上限(默认 50%)，全程<b>只对玩家
 * 生效</b>，其它生物不受到影响。
 * <ul>
 * <li><b>HIGHEST</b>：记录护甲前原始伤害到玩家 capability。</li>
 * <li><b>LOWEST</b>：各 mod 减伤叠加完毕后，按 pre-&gt;current 计算累计减免
 * s，再 <code>capped = cap * sigmoid(k*(s-s0))</code> 重写伤害。</li>
 * <li>放大/无效分支(ratio&gt;=1)直接放行；被取消的事件不重写。
 * 直接 {@code setHealth} 的计数器自伤(如污血泣泪注入扣血)绕过此管线，
 * 天然不受影响。</li>
 * </ul>
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageClampHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    /** 记下护甲前原始伤害。必须在最前，避免被其它减伤污染。 */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void recordPre(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        event.getEntity().getCapability(DamageClampCapability.CAP).ifPresent(cap -> {
            cap.setPreDamage(event.getAmount());
        });
    }

    /** 各 mod 减伤叠加后，把累计非护甲减伤收缩到上限内。 */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void clamp(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.isCanceled()) {
            return; // 前置已取消（拮抗等彻底免疫），保持原语义，不重写
        }
        var cap = event.getEntity().getCapability(DamageClampCapability.CAP)
                .orElse(null);
        if (cap == null) {
            return;
        }
        float pre = cap.getPreDamage();
        if (pre <= 0.0F) {
            return; // 未记录到有效原始伤害
        }
        float current = event.getAmount();
        float ratio = current / pre;
        if (ratio >= 1.0F) {
            return; // 放大/无效分支，不钳
        }
        float reduced = 1.0F - ratio; // 累计非护甲减速度 s
        // 某些过分的 mod 把伤害削到 1 或更低（接近免疫）：绕过 sigmoid 曲线，
        // 直接锁定最大减伤（cap），保证伤害仍保留 cap 对应的下限，而非趋近于零。
        // 仅在钳制启用时生效；关闭（enabled=false）时保持原样，不被强制削减。
        float capped = CMConfig.INSTANCE.damageClamp.enabled && current <= 1.0F ? CMConfig.INSTANCE.damageClamp.cap :
                CMConfig.INSTANCE.damageClamp.applyCap(reduced);
        float newAmount = pre * (1.0F - capped);
        event.setAmount(newAmount);
        if (CMConfig.INSTANCE.damageClamp.debugLog && newAmount < current) {
            LOGGER.debug("[DamageClamp] {}: pre={}, ratio={}, s={}, capped={}, {} -> {}",
                    event.getEntity().getScoreboardName(), pre, ratio, reduced, capped, current, newAmount);
        }
    }
}
