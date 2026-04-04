package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;

import com.moguang.ctnhmana.common.entity.DeltaSpark;
import com.moguang.ctnhmana.common.entity.OmegaSpark;
import com.moguang.ctnhmana.registry.CMEntities;

import java.util.ArrayList;
import java.util.List;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.omegaSpireStateLang;

public class ZenithSpire extends MysticSpire {

    public ZenithSpire(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ZenithSpire.class,
            MysticSpire.MANAGED_FIELD_HOLDER);

    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public EnergyContainerList energyContainer;
    @Persisted
    public Long sEU;
    @Persisted
    public Long maxEU;
    @Persisted
    public Long maxEULimit;
    @Persisted
    public int eutier = 0;

    @Override
    public void getOrCreatedSpark() {
        updateSelf();
        if (this.getLevel().isClientSide) return;

        AABB locate = new AABB(sparkpos);

        // Prefer OmegaSpark for ZenithSpire.
        var omegaSparks = this.getLevel().getEntitiesOfClass(OmegaSpark.class, locate);
        if (!omegaSparks.isEmpty()) {
            this.Spark = omegaSparks.get(0);
            updateSpark();
            return;
        }

        // Replace any legacy DeltaSpark.
        var deltaSparks = this.getLevel().getEntitiesOfClass(DeltaSpark.class, locate);
        if (!deltaSparks.isEmpty()) {
            deltaSparks.get(0).kill();
        }

        var entity = CMEntities.OMEGA_SPARK.create(getLevel());
        entity.AttachPos = this.getPos();
        entity.range = this.range;
        entity.speed = this.speed;
        entity.TargetNum = this.TargetNum;
        entity.effencicy = this.effencicy;
        entity.mode = MODE;
        entity.setNetwork(network);
        entity.setPos(sparkpos.getX(), sparkpos.getY(), sparkpos.getZ());

        // Connect the other side spark: any DeltaSpark is acceptable.
        if (connectedSparkPos != null) {
            BlockPos otherPos = connectedSparkPos;

            var connectedDelta = this.getLevel().getEntitiesOfClass(DeltaSpark.class, new AABB(otherPos));
            if (!connectedDelta.isEmpty()) {
                this.ConnectedSpark = connectedDelta.get(0);
            }

            if (this.ConnectedSpark != null) {
                entity.connectedDeltaSpark = this.ConnectedSpark;
                entity.connectedDeltaSparkPos = new AABB(this.ConnectedSpark.getOnPos());
            }
        }

        getLevel().addFreshEntity(entity);
    }

    public EnergyContainerList getEnergyContainer() {
        List<IEnergyContainer> containers = new ArrayList<>();
        var handlers = getCapabilitiesFlat(IO.IN, EURecipeCapability.CAP);
        if (handlers.isEmpty()) handlers = getCapabilitiesFlat(IO.OUT, EURecipeCapability.CAP);
        for (IRecipeHandler<?> handler : handlers) {
            if (handler instanceof IEnergyContainer container) {
                containers.add(container);
            }
        }
        return new EnergyContainerList(containers);
    }

    @Override
    public void updateSelf() {
        super.updateSelf();
        energyContainer = getEnergyContainer();
        var voltage = energyContainer.getInputVoltage();
        this.eutier = GTUtil.getFloorTierByVoltage(voltage);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        if (this.isFormed && this.energyContainer != null) {
            textList.add(omegaSpireStateLang[0].translate(GTValues.VN[eutier]));
        }
    }
}
