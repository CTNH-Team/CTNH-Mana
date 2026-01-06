package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatch;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import com.moguang.ctnhmana.common.entity.DeltaSpark;
import net.minecraft.core.BlockPos;
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

    public DeltaSpark Spark;

    public DeltaSpark ConnectedSpark;

    public int range=5;
    public int speed=5000;
    public int TargetNum=3;

    public BlockPos sparkpos;
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.sparkpos=new BlockPos(this.getPos().getX(),this.getPos().getY()+7,this.getPos().getZ());
    }
    public void getOrCreatedSpark()
    {
        if(this.getLevel().isClientSide)return;
        AABB locate=new AABB(sparkpos);
        if(!this.getLevel().getEntitiesOfClass(DeltaSpark.class,locate).isEmpty())
        {
            this.Spark=Spark;
        }

    }

}
