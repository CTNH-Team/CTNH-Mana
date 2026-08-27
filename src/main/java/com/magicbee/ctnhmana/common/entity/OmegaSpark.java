package com.magicbee.ctnhmana.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import com.magicbee.ctnhmana.common.multiblock.SpireBigMath;
import com.magicbee.ctnhmana.common.multiblock.ZenithSpire;

import java.math.BigInteger;
import java.util.List;

public class OmegaSpark extends DeltaSpark {

    public OmegaSpark(EntityType<?> type, Level world) {
        super(type, world);
    }

    /** EU 容器连线：只记录进活跃连线表，由客户端本地生成粒子（不再每 tick 逐个容器发包）。 */
    public void sendEnergyContainerParticles(List<BlockPos> containerPosList) {
        if (!isAnimationActive || containerPosList == null || containerPosList.isEmpty()) return;
        for (BlockPos pos : containerPosList) {
            addFlowTarget(pos, false);
        }
    }

    /** EU 容器反向连线（容器 → 火花），同样只记录进活跃连线表。 */
    public void sendEnergyContainerParticlesReverse(List<BlockPos> containerPosList) {
        if (!isAnimationActive || containerPosList == null || containerPosList.isEmpty()) return;
        for (BlockPos pos : containerPosList) {
            addFlowTarget(pos, true);
        }
    }

    @Override
    public void sendManaToDeltaNet() {
        super.sendManaToDeltaNet();
        if (SpireMachine instanceof ZenithSpire zspire && connectedDeltaSpark instanceof OmegaSpark ospark &&
                ospark.SpireMachine instanceof ZenithSpire targetspire) {
            BigInteger speedBd = BigInteger.valueOf(zspire.euSpeed);
            BigInteger room = SpireBigMath.subtractNonNegative(
                    targetspire.getEuCapacityBig(), targetspire.getStoredEuBig());
            BigInteger srcAvail = SpireBigMath.nonNegative(zspire.getStoredEuBig());
            BigInteger consume = SpireBigMath.min(room, speedBd, srcAvail);
            zspire.setStoredEuBig(SpireBigMath.subtractNonNegative(zspire.getStoredEuBig(), consume));
            targetspire.setStoredEuBig(SpireBigMath.addCapToMax(
                    targetspire.getStoredEuBig(), consume, targetspire.getEuCapacityBig()));
        }
    }
}
