package com.moguang.ctnhmana.mixin.bloodmagic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import wayoftime.bloodmagic.altar.BloodAltar;
import wayoftime.bloodmagic.common.tile.TileAltar;

@Mixin(TileAltar.class)
public interface TileAltarAccessor {

    @Accessor(value = "bloodAltar", remap = false)
    BloodAltar getBloodAltar();
}
