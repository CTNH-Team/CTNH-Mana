package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import com.moguang.ctnhmana.common.entity.DeltaSpark;
import com.moguang.ctnhmana.registry.CMEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.AABB;

public class MysticSpire extends WorkableMultiblockMachine {
    public MysticSpire(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MysticSpire.class,
            WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    @Persisted
    public BlockPos connectedSparkPos;
    public DeltaSpark Spark;

    @Persisted
    public int MODE=1;

    public DeltaSpark ConnectedSpark;
    @Persisted
    public int range=20;
    @Persisted
    public int speed=5000;
    @Persisted
    public int TargetNum=3;
    @Persisted
    public int maxMana=10000;
    @Persisted
    public BlockPos sparkpos;
    @Persisted
    public boolean isAnimationActive=true;
    @Persisted
    public int receive_rate=5000;
    @Persisted
    public DyeColor network=DyeColor.WHITE;


    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.sparkpos=new BlockPos(this.getPos().getX(),this.getPos().getY()+7,this.getPos().getZ());
        ((MysticSpireBlockEntity) this.holder).setMaxMana(100000000);
        ((MysticSpireBlockEntity) this.holder).receiveMana(10000000);
        getOrCreatedSpark();
    }
    @Override
    public void onStructureInvalid()
    {
        super.onStructureInvalid();
        //虽然德尔塔火花在检测到结构失效后会自行消灭，但考虑到持久化的因素，双向记录数据和摧毁是保险的，这绝对不是什么PTSD
        if(Spark!=null)
        {
            this.Spark.kill();
            this.Spark=null; //这个过程只消灭绑定的火花本身，而保留其他数据
        }
    }
    public void getOrCreatedSpark()
    {
        if(this.getLevel().isClientSide)return;
        AABB locate=new AABB(sparkpos);
        if(!this.getLevel().getEntitiesOfClass(DeltaSpark.class,locate).isEmpty())
        {
            this.Spark=this.getLevel().getEntitiesOfClass(DeltaSpark.class,locate).get(0);
            updateSpark();
        }
        else
        {
            var entity= CMEntities.DELTA_SPARK.create(getLevel());
            entity.AttachPos=this.getPos();
            entity.range=this.range;
            entity.speed=this.speed;
            entity.TargetNum=this.TargetNum;
            entity.mode=MODE;
            entity.setNetwork(network);
            entity.setPos(sparkpos.getX(),sparkpos.getY(),sparkpos.getZ());
            if(connectedSparkPos!=null)
            {
                entity.BindingDeltaSpark=ConnectedSpark;
                entity.BindingSparkPos=new AABB(ConnectedSpark.getOnPos());
            }
            getLevel().addFreshEntity(entity);
        }
    }

    public void updateSpark()
    {
        //刷新火花数据
        if(this.Spark==null)return;
        this.Spark.range=range;
        this.Spark.speed=speed;
        this.Spark.mode=MODE;
        if(connectedSparkPos!=null) this.Spark.BindingSparkPos=new AABB(connectedSparkPos);
        this.Spark.setNetwork(network);

    }
    public void updateSelf()
    {

    }

}
