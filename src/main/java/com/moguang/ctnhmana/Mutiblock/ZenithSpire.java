package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.LaserHatchPartMachine;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

import com.moguang.ctnhmana.common.entity.DeltaSpark;
import com.moguang.ctnhmana.common.entity.OmegaSpark;
import com.moguang.ctnhmana.registry.CMEntities;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.omegaSpireStateLang;

public class ZenithSpire extends MysticSpire {

    public ZenithSpire(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.sparkpos = MachineUtils.getOffset(this, 0, 53, 9);
    }

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ZenithSpire.class,
            MysticSpire.MANAGED_FIELD_HOLDER);

    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    // Zenith spire controller's own energy container.
    public EnergyContainerList selfEnergyContainer;
    public EnergyContainerList energyInputContainer;
    public EnergyContainerList smallMachineContainer;
    public EnergyContainerList energyHatchContainer;
    public EnergyContainerList energyoutputContainer;

    @Persisted
    public int eutier = 0;
    @Nullable
    public List<BlockPos> euContainerPos = new ArrayList<>();
    @Nullable
    public List<BlockPos> euOutputContainerPos = new ArrayList<>();
    @Persisted
    public Double zenithEfficiency = 0.25;
    @Persisted
    public Long euSpeed = 0L;
    @Persisted
    public Long zEU = 0L;
    @Persisted
    public Long euCapacity = 0L;

    /**
     * 结构成型时记录：属于本天顶尖塔多方块的能源仓 / 激光仓方块坐标。
     * {@link #getEUContainer()} 扫描时跳过这些位置，避免向自身舱室回灌 EU。
     */
    @Persisted
    public List<BlockPos> selfStructuralEnergyChamberPos = new ArrayList<>();

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        refreshSelfStructuralEnergyChambers();
    }

    @Override
    public void onStructureInvalid() {
        if (selfStructuralEnergyChamberPos != null) {
            selfStructuralEnergyChamberPos.clear();
        }
        super.onStructureInvalid();
    }

    private void refreshSelfStructuralEnergyChambers() {
        if (selfStructuralEnergyChamberPos == null) {
            selfStructuralEnergyChamberPos = new ArrayList<>();
        } else {
            selfStructuralEnergyChamberPos.clear();
        }
        for (IMultiPart part : getParts()) {
            MetaMachine meta = part.self();
            if (meta instanceof EnergyHatchPartMachine || meta instanceof LaserHatchPartMachine) {
                selfStructuralEnergyChamberPos.add(meta.getPos().immutable());
            }
        }
    }

    @Override
    public void metircTick() {
        if (this.getOffsetTimer() % 100 == 0) {
            getOrCreatedSpark();
            getEUContainer();
        }
        // this.zEU= (long) (euCapacity*0.1);
        this.zEU = Math.max(0, this.zEU);
        this.zEU = Math.min(this.euCapacity, this.zEU);
        if (this.zEU < this.euCapacity && this.MODE == 0) sendEUToSpire();
        if (this.zEU > 0 && this.MODE != 3) sendEUToContainer();
    }

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
        selfEnergyContainer = getEnergyContainer();
        getEUContainer();
        var voltage = selfEnergyContainer.getHighestInputVoltage();
        this.eutier = GTUtil.getFloorTierByVoltage(voltage) * 4;
        this.euSpeed = selfEnergyContainer.getInputVoltage() * this.speed / base_speed * 4;
        this.euCapacity = selfEnergyContainer.getEnergyCapacity() * this.maxMana / base_maxmana * 4;
        this.range *= 4;
        this.speed *= 4;
        this.maxMana *= 4;
    }

    public void getEUContainer() {
        List<IEnergyContainer> smallMachines = new ArrayList<>();
        List<IEnergyContainer> energyHatches = new ArrayList<>();
        List<IEnergyContainer> energyOutputs = new ArrayList<>();
        Set<BlockPos> scannedPos = new HashSet<>();
        Set<BlockPos> scannedOutputPos = new HashSet<>();
        var level = this.getLevel();
        if (level == null) {
            this.euContainerPos = new ArrayList<>();
            this.euOutputContainerPos = new ArrayList<>();
            this.energyInputContainer = new EnergyContainerList(new ArrayList<>());
            this.smallMachineContainer = new EnergyContainerList(new ArrayList<>());
            this.energyHatchContainer = new EnergyContainerList(new ArrayList<>());
            this.energyoutputContainer = new EnergyContainerList(new ArrayList<>());
            return;
        }
        int scanRange = (int) Math.max(1, Math.floor(this.range * this.zenithEfficiency));
        BlockPos controllerPos = this.getPos();
        int minX = controllerPos.getX() - scanRange;
        int maxX = controllerPos.getX() + scanRange;
        int minY = controllerPos.getY() - scanRange;
        int maxY = controllerPos.getY() + scanRange;
        int minZ = controllerPos.getZ() - scanRange;
        int maxZ = controllerPos.getZ() + scanRange;

        Set<BlockPos> selfChamberSet = new HashSet<>();
        if (selfStructuralEnergyChamberPos != null) {
            for (BlockPos p : selfStructuralEnergyChamberPos) {
                if (p != null) selfChamberSet.add(p.immutable());
            }
        }

        for (int chunkX = minX >> 4; chunkX <= maxX >> 4; chunkX++) {
            for (int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; chunkZ++) {
                if (!level.hasChunk(chunkX, chunkZ)) continue;
                LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                for (var blockEntity : chunk.getBlockEntities().values()) {
                    if (!(blockEntity instanceof IMachineBlockEntity machineHolder)) continue;

                    BlockPos candidatePos = blockEntity.getBlockPos();
                    if (candidatePos.equals(controllerPos)) continue;
                    if (selfChamberSet.contains(candidatePos)) continue;
                    if (candidatePos.getY() < minY || candidatePos.getY() > maxY) continue;
                    if (Math.abs(candidatePos.getX() - controllerPos.getX()) > scanRange ||
                            Math.abs(candidatePos.getZ() - controllerPos.getZ()) > scanRange)
                        continue;

                    var machine = machineHolder.getMetaMachine();
                    // Never include zenith spire controller itself to avoid loops.
                    if (machine instanceof ZenithSpire) continue;

                    IEnergyContainer found = null;
                    if (machine instanceof TieredEnergyMachine tieredMachine) {
                        found = tieredMachine.energyContainer;
                    } else if (machine instanceof SimpleGeneratorMachine generatorMachine) {
                        found = generatorMachine.energyContainer;
                    } else if (machine instanceof EnergyHatchPartMachine hatchPart) {
                        found = hatchPart.energyContainer;
                    } else if (machine instanceof LaserHatchPartMachine) {
                        found = blockEntity.getCapability(GTCapability.CAPABILITY_LASER).orElse(null);
                    }
                    if (found == null) continue;
                    scannedPos.add(candidatePos.immutable());

                    boolean canInput = false;
                    boolean canOutput = false;
                    for (Direction direction : Direction.values()) {
                        if (found.inputsEnergy(direction)) canInput = true;
                        if (found.outputsEnergy(direction)) canOutput = true;
                        if (canInput && canOutput) break;
                    }

                    if ((machine instanceof TieredEnergyMachine || machine instanceof SimpleGeneratorMachine) &&
                            machine instanceof ITieredMachine tieredMachine &&
                            tieredMachine.getTier() <= eutier) {
                        // Small machines that can receive EU are treated as broadcast targets.
                        if (canInput) {
                            smallMachines.add(found);
                        }
                        // Generator-like small machines (output only) are treated as EU providers for spire charging.
                        if (canOutput && !canInput) {
                            energyOutputs.add(found);
                            scannedOutputPos.add(candidatePos.immutable());
                        }
                    } else
                        if ((machine instanceof EnergyHatchPartMachine || machine instanceof LaserHatchPartMachine) &&
                                ((ITieredMachine) machine).getTier() <= eutier) {
                                    if (canInput) {
                                        energyHatches.add(found);
                                    } else if (canOutput) {
                                        energyOutputs.add(found);
                                        scannedOutputPos.add(candidatePos.immutable());
                                    }
                                }
                }
            }
        }
        this.euContainerPos = new ArrayList<>(scannedPos);
        this.euOutputContainerPos = new ArrayList<>(scannedOutputPos);
        List<IEnergyContainer> mergedInputs = new ArrayList<>(smallMachines);
        mergedInputs.addAll(energyHatches);
        this.energyInputContainer = new EnergyContainerList(mergedInputs);
        this.smallMachineContainer = new EnergyContainerList(smallMachines);
        this.energyHatchContainer = new EnergyContainerList(energyHatches);
        this.energyoutputContainer = new EnergyContainerList(energyOutputs);
    }

    public void sendEUToContainer() {
        var EUs = this.euSpeed;
        if (energyInputContainer != null) {
            var consume = Math.min(energyInputContainer.getEnergyCapacity() - energyInputContainer.getEnergyStored(),
                    Math.min(EUs, this.zEU));
            energyInputContainer.addEnergy(consume);
            EUs -= consume;
            this.zEU -= consume;
            if (consume > 0 && this.Spark instanceof OmegaSpark omegaSpark) {
                omegaSpark.sendEnergyContainerParticles(this.euContainerPos);
            }
        }
    }

    public void sendEUToSpire() {
        // if(this.selfEnergyContainer!=null&&this.zEU<this.euCapacity) {
        // var consume=Math.min(this.euCapacity-this.zEU,this.selfEnergyContainer.getEnergyStored());
        // this.zEU+=consume;
        // this.selfEnergyContainer.removeEnergy(consume);
        // this.zEU=Math.min(this.euCapacity,this.zEU);
        // }
        if (this.energyoutputContainer != null && this.zEU < this.euCapacity) {
            var consume = Math.min(this.euCapacity - this.zEU,
                    Math.min(this.euSpeed * 2, this.energyoutputContainer.getEnergyStored()));
            this.zEU += consume;
            this.energyoutputContainer.removeEnergy(consume);
            if (consume > 0 && this.Spark instanceof OmegaSpark omegaSpark) {
                omegaSpark.sendEnergyContainerParticlesReverse(this.euOutputContainerPos);
            }
        }
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (this.isFormed && this.selfEnergyContainer != null) {
            textList.add(omegaSpireStateLang[0].translate(GTValues.VNF[eutier])
                    .withStyle(Style.EMPTY.withColor(GTValues.VC[eutier])));
            textList.add(omegaSpireStateLang[1].translate(this.zEU, this.euCapacity));
            textList.add(omegaSpireStateLang[2].translate(this.euSpeed, this.euSpeed / GTValues.V[eutier],
                    GTValues.VNF[eutier]));
        }
    }

    @CN({
            "§b星空§r是魔法,§4血欲§r是魔法,§a大地§r是魔法,§5科技§r自然也是魔法.在超越现实的思维洪流之中,现实正逐渐塑造成我们心中所想",
            "天顶尖塔包含奥法尖塔的所有功能，请查阅奥法尖塔来获知这些功能,天顶尖塔的所有数据额外翻四倍",
            "允许使用能源仓,变电仓,激光仓",
            "天顶尖塔将电力(EU)当作魔力束流进行传输,其初始电压等级,容量,速度与能源舱室相同,尖塔的强化同样强化这些数据",
            "在聚焦模式下,天顶尖塔将自动吸收范围内的动力仓来为其供能，天顶尖塔同样会将电力传输至其绑定的其他尖塔",
            "在非中转和聚焦模式下,天顶尖塔将自动广播到范围内的所有能源存储者(小机器,能源仓,激光仓,变电仓)",
            "在中转模式下，天顶尖塔将只做中转"
    })

    @EN({
            "§b星空§r是魔法,§4血欲§r是魔法,§a大地§r是魔法,§5科技§r自然也是魔法.在超越现实的思维洪流之中,现实正逐渐塑造成我们心中所想",
            "天顶尖塔包含奥法尖塔的所有功能，请查阅奥法尖塔来获知这些功能",
            "允许使用能源仓,变电仓,激光仓",
            "天顶尖塔将电力(EU)当作魔力束流进行传输,其初始电压等级,容量,速度与能源舱室相同,尖塔的强化同样强化这些数据",
            "在聚焦模式下,天顶尖塔将自动吸收范围内的动力仓来为其供能，天顶尖塔同样会将电力传输至其绑定的其他尖塔",
            "在非中转和聚焦模式下,天顶尖塔将自动广播到范围内的所有能源存储者(小机器,能源仓,激光仓,变电仓)",
            "在中转模式下，天顶尖塔将只做中转"
    })
    public static Lang[] omegaSpireLang;
}
