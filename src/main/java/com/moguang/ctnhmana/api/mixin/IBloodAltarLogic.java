package com.moguang.ctnhmana.api.mixin;

import net.minecraft.core.BlockPos;

public interface IBloodAltarLogic {

    void CM$resetCapacity(int Capacity);

    void CM$setCapacityMultiplier(float Multiplier);

    void CM$BroadcastPos(BlockPos pos);

    boolean CM$ConsumeLPIfEnough(int lp);
}
