package com.moguang.ctnhmana.common.ritualTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.ritual.*;

import java.util.List;
import java.util.function.Consumer;
@RitualRegister("dragoncloud")
public class RitualDragonCloud extends Ritual {
    private final int costs=1;
    public RitualDragonCloud() {
        super("ritualdragoncloud", 0,1, "ritual.ctnhmana.dragon_cloudritual");
        this.addBlockRange("dragon_cloud", new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 7));
        this.setMaximumVolumeAndDistanceOfRange("dragon_cloud", 1, 30, 30);
    }

    @Override
    public void performRitual(IMasterRitualStone MasterRitualStone) {
        var time_muti=1;
        var areas=3.0F;
        Level world = MasterRitualStone.getWorldObj();
        AreaDescriptor growingRange = MasterRitualStone.getBlockRange("dragon_cloud");
        List<EnumDemonWillType> willConfig = MasterRitualStone.getActiveWillConfig();

        BlockPos pos = MasterRitualStone.getMasterBlockPos();
        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        if(rawWill>=2)
        {
            time_muti=2;
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, 2, true);
        }
        if(steadfastWill>=2)
        {
            areas+=2.5F;
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, 2, true);
        }
        AreaEffectCloud cloud = new AreaEffectCloud(EntityType.AREA_EFFECT_CLOUD, world);
        cloud.setPos(pos.getX(),pos.getY()+1, pos.getZ());
        cloud.setRadius(areas); // 初始半径
        cloud.setRadiusOnUse(-0.25F); // 每次对实体造成效果后缩小的半径
        cloud.setWaitTime(2); // 生成后等待多久开始生效（tick）
        cloud.setDuration(100*time_muti);
        cloud.setRadiusPerTick(-0.025F/time_muti);
        cloud.addEffect(new MobEffectInstance(
                MobEffects.HARM, // 瞬间伤害效果
                1,
                1,
                false,
                false
        ));

        cloud.setParticle(ParticleTypes.DRAGON_BREATH);
        world.addFreshEntity(cloud);
    }

    @Override
    public int getRefreshCost() {
        return 1;
    }
    @Override
    public int getRefreshTime() {
        return 200;
    }
    @Override
    public void gatherComponents(Consumer<RitualComponent> consumer) {
        this.addParallelRunes(consumer,1,0,EnumRuneType.EARTH);
        this.addParallelRunes(consumer,2,0,EnumRuneType.FIRE);
        this.addParallelRunes(consumer,3,0,EnumRuneType.WATER);
        this.addParallelRunes(consumer,4,0,EnumRuneType.BLANK);
        this.addParallelRunes(consumer,4,1,EnumRuneType.EARTH);
        this.addParallelRunes(consumer,4,2,EnumRuneType.EARTH);
        this.addParallelRunes(consumer,4,3,EnumRuneType.EARTH);
        this.addParallelRunes(consumer,4,4,EnumRuneType.BLANK);
        this.addCornerRunes(consumer,1,0,EnumRuneType.FIRE);
        this.addCornerRunes(consumer,2,0,EnumRuneType.WATER);
        this.addCornerRunes(consumer,3,0,EnumRuneType.BLANK);
        this.addCornerRunes(consumer,3,1,EnumRuneType.DUSK);
        this.addCornerRunes(consumer,3,2,EnumRuneType.DUSK);
        this.addCornerRunes(consumer,3,3,EnumRuneType.DUSK);
        this.addCornerRunes(consumer,3,4,EnumRuneType.BLANK);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualDragonCloud();
    }
}
