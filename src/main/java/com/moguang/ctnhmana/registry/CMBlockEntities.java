package com.moguang.ctnhmana.registry;

import com.moguang.ctnhmana.common.blockentity.flower.BlackVeinMarigoldBlockEntity;
import com.moguang.ctnhmana.common.blockentity.flower.BloodAntiarisBlockEntity;
import com.moguang.ctnhmana.common.blockentity.flower.DemonFlytrapBlockEntity;
import com.moguang.ctnhmana.common.blockentity.flower.TulpenmanieBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

public class CMBlockEntities {
    public static void init() {

    }

    public static BlockEntityEntry<DemonFlytrapBlockEntity> DEMON_FLYTRAP = REGISTRATE
            .blockEntity("demon_flytrap", DemonFlytrapBlockEntity::new)
            .validBlocks(CMBlocks.DEMON_FLYTRAP)
            .register();

    public static BlockEntityEntry<BloodAntiarisBlockEntity> BLOOD_ANTIARIS = REGISTRATE
            .blockEntity("blood_antiaris", BloodAntiarisBlockEntity::new)
            .validBlocks(CMBlocks.BLOOD_ANTIARIS)
            .register();
    public static BlockEntityEntry<BlackVeinMarigoldBlockEntity> BLACKVEIN_MARIGOLD = REGISTRATE
            .blockEntity("blackvein_marigold", BlackVeinMarigoldBlockEntity::new)
            .validBlocks(CMBlocks.BLACKVEIN_MARIGOLD)
            .register();
    public static BlockEntityEntry<TulpenmanieBlockEntity> SEMPER_AUGUSTUS = REGISTRATE
            .blockEntity("semper_augustus", TulpenmanieBlockEntity::new)
            .validBlocks(CMBlocks.SEMPER_AUGUSTUS)
            .register();
}