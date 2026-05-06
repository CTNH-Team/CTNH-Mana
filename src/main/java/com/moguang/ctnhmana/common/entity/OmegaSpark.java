package com.moguang.ctnhmana.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import com.moguang.ctnhmana.Mutiblock.SpireMath;
import com.moguang.ctnhmana.Mutiblock.ZenithSpire;
import com.moguang.ctnhmana.api.networks.BotaniaEffectPacketExtend;
import com.moguang.ctnhmana.api.networks.BotaniaExtendEffectType;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class OmegaSpark extends DeltaSpark {

    public OmegaSpark(EntityType<?> type, Level world) {
        super(type, world);
    }

    public void sendEnergyContainerParticles(List<BlockPos> containerPosList) {
        if (!isAnimationActive || containerPosList == null || containerPosList.isEmpty()) return;
        int color = ColorHelper.getColorValue(getNetwork());
        for (BlockPos pos : containerPosList) {
            XplatAbstractions.INSTANCE.sendToTracking(this,
                    new BotaniaEffectPacketExtend(BotaniaExtendEffectType.SPARK_MANA_FLOW,
                            pos.getX(), pos.getY(), pos.getZ(),
                            getId(), getId(), color));
        }
    }

    public void sendEnergyContainerParticlesReverse(List<BlockPos> containerPosList) {
        if (!isAnimationActive || containerPosList == null || containerPosList.isEmpty()) return;
        int color = ColorHelper.getColorValue(getNetwork());
        for (BlockPos pos : containerPosList) {
            XplatAbstractions.INSTANCE.sendToTracking(this,
                    new BotaniaEffectPacketExtend(BotaniaExtendEffectType.SPARK_MANA_FLOW_REVERSE,
                            pos.getX(), pos.getY(), pos.getZ(),
                            getId(), getId(), color));
        }
    }

    @Override
    public void sendManaToDeltaNet() {
        super.sendManaToDeltaNet();
        if (SpireMachine instanceof ZenithSpire zspire && connectedDeltaSpark instanceof OmegaSpark ospark &&
                ospark.SpireMachine instanceof ZenithSpire targetspire) {
            var eus = zspire.euSpeed;
            long room = SpireMath.nonNegative(targetspire.euCapacity - targetspire.zEU);
            long srcAvail = SpireMath.nonNegative(zspire.zEU);
            long consume = Math.min(room, Math.min(eus, srcAvail));
            zspire.zEU = SpireMath.nonNegative(zspire.zEU - consume);
            targetspire.zEU = SpireMath.addCapToMax(targetspire.zEU, consume, targetspire.euCapacity);
        }
    }
}
