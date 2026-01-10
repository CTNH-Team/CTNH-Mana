package com.moguang.ctnhmana.registry;

import com.moguang.ctnhmana.client.render.DeltaSparkRenderer;
import com.moguang.ctnhmana.common.entity.DeltaSpark;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

public class CMEntities {
    public static EntityEntry<DeltaSpark> DELTA_SPARK = REGISTRATE
            .entity("delta_spark", DeltaSpark::new, MobCategory.MISC)
            .cnlang("德尔塔火花")
            .lang("Delta Spark")
            .properties(props -> props.sized(0.2F, 0.2F))
            .renderer(()-> DeltaSparkRenderer::new)
            .register();
    public static void init() {}
}
