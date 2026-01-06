package com.moguang.ctnhmana.common.entity;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.Mutiblock.MysticSpire;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import vazkii.botania.api.item.SparkEntity;
import vazkii.botania.common.entity.ManaSparkEntity;
import vazkii.botania.common.entity.SparkBaseEntity;

public class DeltaSpark extends SparkBaseEntity implements SparkEntity {
    public int TRANSFER_RATE = 1000;
    private static final String TAG_UPGRADE = "upgrade";
    private static final EntityDataAccessor<Integer> MODE = SynchedEntityData.defineId(DeltaSpark.class, EntityDataSerializers.INT);
    @Persisted
    public BlockPos AttachPos;
    public MysticSpire SpireMachine;

    public DeltaSpark BindingDeltaSpark;
    public boolean isInitChecked=false;

    @Persisted
    public AABB BindingSparkPos;

    public DeltaSpark(EntityType<?> type, Level world, BlockPos AttachPos) {
        super(type, world);
        this.AttachPos=AttachPos;
    }
    public DeltaSpark(EntityType<?> type, Level world) {
        super(type, world);
    }
    @Override
    public void tick() {
        if (level().isClientSide) {
            return;
        }
        if(!isInitChecked)
        {
            initSpark();
        }
        // When loaded, initialize transfers
    }
    public void initSpark()
    {
        if(level().getBlockEntity(AttachPos) !=null&&level().getBlockEntity(AttachPos) instanceof MysticSpireBlockEntity entity&&entity.getMetaMachine() instanceof MysticSpire machine&&machine.isFormed())
        {
            this.SpireMachine= machine;
        }
        else
        {
            this.kill();
        }
        if(BindingSparkPos!=null)
        {
            if(!level().getEntitiesOfClass(DeltaSpark.class,BindingSparkPos).isEmpty())
            {
                BindingDeltaSpark=level().getEntitiesOfClass(DeltaSpark.class,BindingSparkPos).get(0);
            }
            else
            {
                BindingSparkPos=null;
            }
        }

    }

    public void getAllManaReceiver()
    {

    }
    @Override
    public BlockPos getAttachPos() {
        return AttachPos;
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(MODE, 0);
    }

}
