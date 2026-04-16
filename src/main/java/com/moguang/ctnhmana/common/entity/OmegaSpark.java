package com.moguang.ctnhmana.common.entity;

import com.moguang.ctnhmana.Mutiblock.ZenithSpire;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

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
        if(SpireMachine instanceof ZenithSpire zspire&&connectedDeltaSpark instanceof OmegaSpark ospark&&ospark.SpireMachine instanceof ZenithSpire targetspire)
        {
            var eus=zspire.euSpeed;
            var consume=Math.min(targetspire.euCapacity-targetspire.zEU,Math.min(eus,zspire.zEU));
            zspire.zEU-=consume;
            targetspire.zEU+=consume;
        }
    }
}