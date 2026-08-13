package com.magicbee.ctnhmana.registry;

import com.magicbee.ctnhmana.common.blockentity.flower.AnattaLotusBlockEntity;
import com.magicbee.ctnhmana.common.blockentity.flower.BlackVeinMarigoldBlockEntity;
import com.magicbee.ctnhmana.common.blockentity.flower.BloodAntiarisBlockEntity;
import com.magicbee.ctnhmana.common.blockentity.flower.DemonFlytrapBlockEntity;
import com.magicbee.ctnhmana.common.blockentity.flower.GenethistleBlockEntity;
import com.magicbee.ctnhmana.common.blockentity.flower.ParaRosiaBlockEntity;
import com.magicbee.ctnhmana.common.blockentity.flower.TulpenmanieBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.magicbee.ctnhmana.CTNHMana.REGISTRATE;

public class CMBlockEntities {

    public static void init() {}

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
    public static BlockEntityEntry<ParaRosiaBlockEntity> PARAROSIA = REGISTRATE
            .blockEntity("pararosia", ParaRosiaBlockEntity::new)
            .validBlocks(CMBlocks.PARAROSIA)
            .register();
    public static BlockEntityEntry<AnattaLotusBlockEntity> ANATTA_LOTUS = REGISTRATE
            .blockEntity("anatta_lotus", AnattaLotusBlockEntity::new)
            .validBlocks(CMBlocks.ANATTA_LOTUS)
            .register();
    public static BlockEntityEntry<GenethistleBlockEntity> GENETHISTLE = REGISTRATE
            .blockEntity("genethistle", GenethistleBlockEntity::new)
            .validBlocks(CMBlocks.GENETHISTLE)
            .register();
}
