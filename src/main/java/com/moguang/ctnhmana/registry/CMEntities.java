package com.moguang.ctnhmana.registry;

import net.minecraft.world.entity.MobCategory;

import com.moguang.ctnhmana.client.render.DeltaSparkRenderer;
import com.moguang.ctnhmana.client.render.OmegaSparkRenderer;
import com.moguang.ctnhmana.common.entity.DeltaSpark;
import com.moguang.ctnhmana.common.entity.OmegaSpark;
import com.tterrag.registrate.util.entry.EntityEntry;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

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

    public static void init() {}
}
