package com.moguang.ctnhmana.common.entity;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.Mutiblock.MysticSpire;
import com.moguang.ctnhmana.api.networks.BotaniaEffectPacketExtend;
import com.moguang.ctnhmana.api.networks.BotaniaExtendEffectType;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.item.SparkEntity;
import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.entity.SparkBaseEntity;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DeltaSpark extends SparkBaseEntity implements SparkEntity, ManaCollisionGhost {
    public int TRANSFER_RATE = 1000;
    private static final String TAG_UPGRADE = "upgrade";
    private static final EntityDataAccessor<Integer> MODE = SynchedEntityData.defineId(DeltaSpark.class, EntityDataSerializers.INT);

    @Persisted
    public int mode=2;
    @Persisted
    public BlockPos AttachPos;
    public MysticSpire SpireMachine;
    public DeltaSpark connectedDeltaSpark;
    public boolean isInitChecked=false;

    @Persisted
    public AABB connectedDeltaSparkPos;

    public boolean isAnimationActive=true;
    public int range=20;
    public int speed=5000;
    public int receive_rate=5000;
    public int TargetNum=3;
    public int timer=0;

    public List<ManaSpark>sparks;
    public List<ManaReceiver>receivers;
    public List<GeneratingFlowerBlockEntity>flowers=new ArrayList<>();
    public List<ManaPoolBlockEntity>pools;

//    public DeltaSpark(EntityType<?> type, Level world, BlockPos AttachPos) {
//        super(type, world);
//        this.AttachPos=AttachPos;
//    }
    public DeltaSpark(EntityType<?> type, Level world) {
        super(type, world);
    }
    @Override
    public BlockPos getAttachPos() {
        return AttachPos;
    }
    @Override
    public void tick() {
        if (level().isClientSide&&firstTick) {
            return;
        }

        if(!isInitChecked)
        {
            initSpark();
        }
        else
        {
            if(timer%200==0)
            {
                initSpark();
                receivers=scanChunkReceiver(level(),this.getOnPos(),range);
                sparks=getSparksAround(level(),this.getX(),this.getY(),this.getZ(),this.getNetwork());
            }
            timer++;
            if(AttachPos!=null&&level().getBlockEntity(AttachPos) !=null&&level().getBlockEntity(AttachPos) instanceof MysticSpireBlockEntity entity&&entity.getMetaMachine() instanceof MysticSpire machine&&machine.isFormed())
            {
                this.SpireMachine= machine;
            }
            else
            {
                this.kill();
            }
            if(mode==0&&!sparks.isEmpty()) sendManaToSpark();
            if(mode==1&&!receivers.isEmpty()) sendManaToReceiver();
            if(mode==2&&!receivers.isEmpty()) receiveManaFromSpark();
            if(mode==2&&!flowers.isEmpty())receiveManaFromFlower();
        }

        // When loaded, initialize transfers

    }
    public void initSpark()
    {
        if(AttachPos!=null&&level().getBlockEntity(AttachPos) !=null&&level().getBlockEntity(AttachPos) instanceof MysticSpireBlockEntity entity&&entity.getMetaMachine() instanceof MysticSpire machine&&machine.isFormed())
        {
            this.SpireMachine= machine;
        }
        else
        {
            this.kill();
        }
        if(connectedDeltaSparkPos !=null)
        {
            if(!level().getEntitiesOfClass(DeltaSpark.class, connectedDeltaSparkPos).isEmpty())
            {
                connectedDeltaSpark =level().getEntitiesOfClass(DeltaSpark.class, connectedDeltaSparkPos).get(0);
            }
            else
            {
                connectedDeltaSparkPos =null;
            }
        }
        isInitChecked=true;
    }
    public void sendManaToSpark()
    {
        var pool=(MysticSpireBlockEntity)SpireMachine.getHolder();
        int consume=0;
        var num=0;
        for(ManaSpark spark:sparks)
        {
            if(!spark.entity().isAlive()||spark.getAttachedManaReceiver()==null)continue;
            if(!spark.getAttachedManaReceiver().isFull())num++;
        }
        if(num<1||pool.getCurrentMana()<=0)return;
        if(pool.getCurrentMana()<=speed) {
            consume = pool.getCurrentMana();
            pool.BTMana=0;
        }
        else
        {
            pool.BTMana-=speed;
            consume=speed;
        }

        consume=consume/num;
        for(ManaSpark spark:sparks)
        {
            if(!spark.entity().isAlive()||spark.getAttachedManaReceiver()==null)continue;
            if(!spark.getAttachedManaReceiver().isFull())spark.getAttachedManaReceiver().receiveMana(consume);
            if(isAnimationActive)particlesTowards(spark.entity());

        }
    }
    public void sendManaToReceiver()
    {
        var pool=(MysticSpireBlockEntity)SpireMachine.getHolder();
        int consume=0;
        var num=0;
        for(ManaReceiver receiver:receivers)
        {
            if(!receiver.isFull()&&!((BlockEntity) receiver).isRemoved())num++;
        }
        if(num<1||pool.getCurrentMana()<=0)return;
        if(pool.getCurrentMana()<=speed) {
            consume = pool.getCurrentMana();
            pool.BTMana=0;
        }
        else
        {
            pool.BTMana-=speed;
            consume=speed;
        }
        consume=consume/num;
        for(ManaReceiver receiver:receivers)
        {
            if(!receiver.isFull()&&!((BlockEntity) receiver).isRemoved()) {
                receiver.receiveMana(consume);
                if(isAnimationActive)particlesTowards((BlockEntity) receiver);
            }
        }
    }
    public void receiveManaFromSpark()
    {
        var pool=(MysticSpireBlockEntity)SpireMachine.getHolder();
        int consume=0;
        var num=0;
        consume=Math.min(pool.maxBTMana-pool.BTMana,receive_rate);
        for(ManaSpark spark:sparks)
        {
            if(!spark.entity().isAlive()||spark.getAttachedManaReceiver()==null) {
                continue;
            }
            if(spark.getAttachedManaReceiver().getCurrentMana()>0)num++;
        }
        if(num<1||pool.isFull())return;
        consume=consume/num;
        for(ManaSpark spark:sparks)
        {
            if(!spark.entity().isAlive()||spark.getAttachedManaReceiver()==null||spark.getAttachedManaReceiver().getCurrentMana()<=0)continue;
            var mana=spark.getAttachedManaReceiver().getCurrentMana();
            if(mana<consume)
            {
                spark.getAttachedManaReceiver().receiveMana(-mana);
                pool.receiveMana(mana);
            }
            else
            {
                spark.getAttachedManaReceiver().receiveMana(-consume);
                pool.receiveMana(consume);
            }
            if(isAnimationActive)particlesTowardsReverse(spark.entity());
        }
    }
    public void receiveManaFromFlower()
    {
        var pool=(MysticSpireBlockEntity)SpireMachine.getHolder();
        int consume=0;
        var num=0;
        consume=Math.min(pool.maxBTMana-pool.BTMana,receive_rate);
        for(GeneratingFlowerBlockEntity flower:flowers)
        {
            if(!flower.isRemoved()&&flower.getMana()>0)num++;
        }
        if(num<1||pool.isFull())return;
        consume=consume/num;
        for(GeneratingFlowerBlockEntity flower:flowers)
        {
            if(flower.isRemoved()||flower.getMana()<=0)continue;
            var mana=flower.getMana();
            if(mana<consume)
            {
                flower.addMana(-mana);
                pool.receiveMana(mana);
            }
            else
            {
                flower.addMana(-consume);
                pool.receiveMana(consume);
            }
            if(isAnimationActive)particlesTowardsReverse((BlockEntity)flower);
        }
    }
    public void sendManaToDeltaNet()
    {
        if(connectedDeltaSpark==null)return;
        var pool=(MysticSpireBlockEntity)SpireMachine.getHolder();
        var target_pool=(MysticSpireBlockEntity)connectedDeltaSpark.SpireMachine.getHolder();
        int consume=0;
        var num=0;
        consume=Math.min(pool.BTMana,speed);
        consume=Math.min(consume,target_pool.maxBTMana-target_pool.BTMana);
        pool.receiveMana(-consume);
        target_pool.receiveMana(consume);
        if(isAnimationActive)particlesTowards((connectedDeltaSpark));
    }
    public void refreshSpark()
    {

    }

    public List<ManaSpark> getSparksAround(Level world, double x, double y, double z, DyeColor color) {
        //不管DeltaSpark，它有自己的一套逻辑，当然实际上也不是一个东西
        int r = range;
        Predicate<Entity> predicate = e -> e instanceof ManaSpark spark && spark.getNetwork() == color &&!(e instanceof DeltaSpark);
        @SuppressWarnings("unchecked")
        List<ManaSpark> entities = (List<ManaSpark>) (List<?>) world.getEntitiesOfClass(Entity.class, new AABB(x - r, y - r, z - r, x + r, y + r, z + r), predicate);
        return entities;
    }

    public List<ManaReceiver> scanChunkReceiver(Level level, BlockPos centerPos, int radius) {
        //扫区块的Receiver
        List<ManaReceiver> result = new ArrayList<>();
        List<GeneratingFlowerBlockEntity> receive_result = new ArrayList<>();
        if (level == null || radius < 0) {
            return result;
        }

        int minX = centerPos.getX() - radius;
        int maxX = centerPos.getX() + radius;
        int minZ = centerPos.getZ() - radius;
        int maxZ = centerPos.getZ() + radius;
        for (int chunkX = minX >> 4; chunkX <= maxX >> 4; chunkX++) {
            for (int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; chunkZ++) {
                LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                if (chunk == null) {
                    continue;
                }
                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if(be instanceof ManaReceiver&&!(be instanceof MysticSpireBlockEntity))
                    {
                        BlockPos bePos = be.getBlockPos();
                        if (bePos.getY() >= centerPos.getY() - radius*2
                                && bePos.getY() <= centerPos.getY() + radius*2
                                && Math.abs(bePos.getX() - centerPos.getX()) <= radius
                                && Math.abs(bePos.getZ() - centerPos.getZ()) <= radius) {
                            result.add((ManaReceiver) be);
                        }
                    }
                    if(be instanceof GeneratingFlowerBlockEntity)
                    {
                        BlockPos bePos = be.getBlockPos();
                        if (bePos.getY() >= centerPos.getY() - radius*2
                                && bePos.getY() <= centerPos.getY() + radius*2
                                && Math.abs(bePos.getX() - centerPos.getX()) <= radius
                                && Math.abs(bePos.getZ() - centerPos.getZ()) <= radius) {
                             receive_result.add((GeneratingFlowerBlockEntity) be);
                        }
                    }
                }
            }
        }
        flowers=receive_result;
        return result;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(MODE, mode);
    }
    public void setMode(int mode)
    {
        this.mode=mode;
        entityData.set(MODE,mode);
    }

    private void particlesTowards(Entity e) {
        XplatAbstractions.INSTANCE.sendToTracking(this, new BotaniaEffectPacket(EffectType.SPARK_MANA_FLOW, getX(), getY(), getZ(),
                getId(), e.getId(), ColorHelper.getColorValue(getNetwork())));
    }
    private void particlesTowardsReverse(Entity e) {
        XplatAbstractions.INSTANCE.sendToTracking(e, new BotaniaEffectPacket(EffectType.SPARK_MANA_FLOW, getX(), getY(), getZ(),
                e.getId(), getId(), ColorHelper.getColorValue(getNetwork())));
    }
    private void particlesTowardsReverse(BlockEntity e) {
        XplatAbstractions.INSTANCE.sendToTracking(this, new BotaniaEffectPacketExtend(BotaniaExtendEffectType.SPARK_MANA_FLOW_REVERSE, e.getBlockPos().getX(), e.getBlockPos().getY(), e.getBlockPos().getZ(),
                getId(),getId(), ColorHelper.getColorValue(getNetwork())));
    }
    private void particlesTowards(BlockEntity e) {
        XplatAbstractions.INSTANCE.sendToTracking(this, new BotaniaEffectPacketExtend(BotaniaExtendEffectType.SPARK_MANA_FLOW, e.getBlockPos().getX(), e.getBlockPos().getY(), e.getBlockPos().getZ(),
                getId(),getId(), ColorHelper.getColorValue(getNetwork())));
    }

}
