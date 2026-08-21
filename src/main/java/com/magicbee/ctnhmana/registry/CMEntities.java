package com.magicbee.ctnhmana.registry;

import net.minecraft.world.entity.MobCategory;

import com.magicbee.ctnhmana.client.render.BeeNukeProjectileRenderer;
import com.magicbee.ctnhmana.client.render.DeltaSparkRenderer;
import com.magicbee.ctnhmana.client.render.GiantBeeRenderer;
import com.magicbee.ctnhmana.client.render.MaliciousThermalilyProjectileRenderer;
import com.magicbee.ctnhmana.client.render.OmegaSparkRenderer;
import com.magicbee.ctnhmana.client.render.RoyalServantBeeRenderer;
import com.magicbee.ctnhmana.client.render.WitherAconiteProjectileRenderer;
import com.magicbee.ctnhmana.common.entity.DeltaSpark;
import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.magicbee.ctnhmana.common.entity.OmegaSpark;
import com.magicbee.ctnhmana.common.entity.RoyalServantBee;
import com.magicbee.ctnhmana.common.entity.projectile.BeeNukeProjectile;
import com.magicbee.ctnhmana.common.entity.projectile.MaliciousThermalilyProjectile;
import com.magicbee.ctnhmana.common.entity.projectile.WitherAconiteProjectile;
import com.tterrag.registrate.util.entry.EntityEntry;

import static com.magicbee.ctnhmana.CTNHMana.REGISTRATE;

public class CMEntities {

    public static EntityEntry<DeltaSpark> DELTA_SPARK = REGISTRATE
            .entity("delta_spark", DeltaSpark::new, MobCategory.MISC)
            .cnlang("德尔塔火花")
            .lang("Delta Spark")
            .properties(props -> props.sized(0.2F, 0.2F))
            .renderer(() -> DeltaSparkRenderer::new)
            .register();

    public static EntityEntry<OmegaSpark> OMEGA_SPARK = REGISTRATE
            .entity("omega_spark", OmegaSpark::new, MobCategory.MISC)
            .cnlang("欧米茄火花")
            .lang("Omega Spark")
            .properties(props -> props.sized(0.2F, 0.2F))
            .renderer(() -> OmegaSparkRenderer::new)
            .register();

    public static EntityEntry<GiantBee> GIANT_BEE = REGISTRATE
            .entity("giant_bee", GiantBee::new, MobCategory.MISC)
            .cnlang("超级无敌魔力大悲Bee")
            .lang("SuperUltraGaintManaBee")
            .properties(props -> props.sized(0.7F * GiantBee.SCALE, 0.91F * GiantBee.SCALE)
                    .setTrackingRange(16)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(GiantBee::createAttributes)
            .renderer(() -> GiantBeeRenderer::new)
            .register();

    /** 皇家侍从Bee：模型与普通蜜蜂一致（原版 BeeRenderer），只能由巨蜂召唤 */
    public static EntityEntry<RoyalServantBee> ROYAL_SERVANT_BEE = REGISTRATE
            .entity("royal_servant_bee", RoyalServantBee::new, MobCategory.MISC)
            .cnlang("超级无敌皇家侍从Bee")
            .lang("RoyalServantBee")
            .properties(props -> props.sized(0.7F, 1.2F)
                    .setTrackingRange(16)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(RoyalServantBee::createAttributes)
            .renderer(() -> RoyalServantBeeRenderer::new)
            .register();

    /** 凋灵兔葵投掷物（巨蜂巡空时丢下，落地生成凋灵云） */
    public static EntityEntry<WitherAconiteProjectile> WITHER_ACONITE_PROJECTILE = REGISTRATE
            .entity("wither_aconite_projectile", WitherAconiteProjectile::new, MobCategory.MISC)
            .cnlang("凋灵兔葵")
            .lang("Wither Aconite")
            .properties(props -> props.sized(0.25F, 0.25F)
                    .setTrackingRange(16)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> WitherAconiteProjectileRenderer::new)
            .register();

    /** 恶意热爆花投掷物（巨蜂二阶段八方向抛射，落地爆炸） */
    public static EntityEntry<MaliciousThermalilyProjectile> MALICIOUS_THERMALILY_PROJECTILE = REGISTRATE
            .entity("malicious_thermalily_projectile", MaliciousThermalilyProjectile::new, MobCategory.MISC)
            .cnlang("恶意热爆花")
            .lang("Malicious Thermalily")
            .properties(props -> props.sized(0.25F, 0.25F)
                    .setTrackingRange(16)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> MaliciousThermalilyProjectileRenderer::new)
            .register();

    /** 蜜蜂核弹投掷物（巨蜂第三阶段朝玩家抛射，命中 25 半径凋零伤害 + 蜜蜡实心块） */
    public static EntityEntry<BeeNukeProjectile> BEE_NUKE_PROJECTILE = REGISTRATE
            .entity("bee_nuke_projectile", BeeNukeProjectile::new, MobCategory.MISC)
            .cnlang("蜜蜂核弹")
            .lang("Bee Nuke")
            .properties(props -> props.sized(0.25F, 0.25F)
                    .setTrackingRange(16)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> BeeNukeProjectileRenderer::new)
            .register();

    public static void init() {}
}
