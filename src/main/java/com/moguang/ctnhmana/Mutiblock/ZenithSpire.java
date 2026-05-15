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
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import com.moguang.ctnhmana.common.entity.DeltaSpark;
import com.moguang.ctnhmana.common.entity.OmegaSpark;
import com.moguang.ctnhmana.registry.CMEntities;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZenithSpire extends MysticSpire {

    public ZenithSpire(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.sparkpos = MachineUtils.getOffset(this, 0, 53, 9);
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

    /**
     * 内部 EU 储量（十进制字符串持久化，兼容 LDL/GT 同步；{@link BigInteger} 运算请用 {@link #getStoredEuBig()}）。
     */
    @Persisted
    @DescSynced
    public String zenithStoredEu = "0";

    /**
     * 内部 EU 上限（十进制字符串）。
     */
    @Persisted
    @DescSynced
    public String zenithCapacityEu = "0";

    /**
     * 旧存档：曾为 {@code long zEU}，加载后迁移至 {@link #zenithStoredEu} 并清空。
     */
    @Persisted(key = "zEU")
    @Nullable
    Long legacyZenithZeu = null;

    /**
     * 旧存档：曾为 {@code long euCapacity}。
     */
    @Persisted(key = "euCapacity")
    @Nullable
    Long legacyZenithEuCapacity = null;

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

    /** 运行时读取 EU 储量（BigInteger）。 */
    public BigInteger getStoredEuBig() {
        return SpireBigMath.parsePersisted(zenithStoredEu);
    }

    /** 写入储量并触发同步（已非负化）。 */
    public void setStoredEuBig(BigInteger v) {
        zenithStoredEu = SpireBigMath.toPersistString(SpireBigMath.nonNegative(v));
    }

    public BigInteger getEuCapacityBig() {
        return SpireBigMath.parsePersisted(zenithCapacityEu);
    }

    private void setEuCapacityBig(BigInteger v) {
        zenithCapacityEu = SpireBigMath.toPersistString(SpireBigMath.nonNegative(v));
    }

    /**
     * 旧存档 long 字段迁移为字符串 BigInteger（仅一次）。
     */
    private void migrateLegacyZenithEuPersistence() {
        boolean touched = false;
        if (legacyZenithZeu != null) {
            zenithStoredEu = SpireBigMath.toPersistString(BigInteger.valueOf(legacyZenithZeu));
            legacyZenithZeu = null;
            touched = true;
        }
        if (legacyZenithEuCapacity != null) {
            zenithCapacityEu = SpireBigMath.toPersistString(BigInteger.valueOf(legacyZenithEuCapacity));
            legacyZenithEuCapacity = null;
            touched = true;
        }
        if (touched) {
            onChanged();
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        migrateLegacyZenithEuPersistence();
    }

    @Override
    public void metircTick() {
        if (this.holder instanceof MysticSpireBlockEntity mbe) {
            mbe.syncMysticManaCacheFromTrue();
        }
        if (this.getOffsetTimer() % 100 == 0) {
            getOrCreatedSpark();
            getEUContainer();
        }
        BigInteger cap = getEuCapacityBig();
        BigInteger z = SpireBigMath.nonNegative(getStoredEuBig());
        if (z.compareTo(cap) > 0) {
            z = cap;
            setStoredEuBig(z);
        }
        if (z.compareTo(cap) < 0 && this.MODE == 0) sendEUToSpire();
        if (z.signum() > 0 && this.MODE != 3) sendEUToContainer();
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
        // EU 倍率独立于魔力侧天顶 ×4：速度用奥法档 int speed；容量因子用真实魔力上限 BigInteger（避免 maxMana 钳 int 导致 EU 上限偏小）
        final int euSpeedFactor = this.speed;
        final BigInteger euMaxManaCapBig = this.holder instanceof MysticSpireBlockEntity poolEu ?
                poolEu.getTrueManaCapBig() : BigInteger.valueOf(Math.max(0, this.maxMana));
        final BigInteger euBaseMaxManaBig = BigInteger.valueOf(base_maxmana);

        selfEnergyContainer = getEnergyContainer();
        var voltage = selfEnergyContainer.getHighestInputVoltage();
        this.eutier = GTUtil.getFloorTierByVoltage(voltage);

        long rangeL = (long) this.range * 2L + 200L;
        this.range = rangeL > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) rangeL;

        // 天顶魔力：与奥法池同源 BigInteger 真实上限再 ×4；公开 int maxMana 仅作柱条/火花窗口（与 pool.setMaxMana 一致）
        if (this.holder instanceof MysticSpireBlockEntity pool) {
            BigInteger zenithTrueCap = pool.getTrueManaCapBig()
                    .multiply(BigInteger.valueOf(4L))
                    .min(SpireBigMath.TRUE_MANA_ABS_CEILING);
            pool.setTrueManaCapacityBig(zenithTrueCap);
            int barCap = SpireBigMath.interactionManaBarCap(zenithTrueCap);
            pool.setMaxMana(barCap);
            this.maxMana = barCap;
        } else {
            this.maxMana = SpireMath.multiplyIntPositiveCap(this.maxMana, 4);
            this.maxMana = Math.min(this.maxMana, Integer.MAX_VALUE);
        }

        // 天顶传输速率 ×4（与 EU 倍率无关）
        this.speed = SpireMath.multiplyIntPositiveCap(this.speed, 4);
        this.speed = Math.min(this.speed, Integer.MAX_VALUE);
        // 与 MysticSpire.updateSelf 相同规则：传输速率不超过魔力容量的 1/10
        this.speed = (int) Math.min((long) this.maxMana / 10L, (long) this.speed);
        this.speed = Math.max(1, this.speed);

        // EU：(电压×奥法speed/base)×4、(舱室×真实魔力上限/base_maxmana)×4 — BigInteger，不在此叠加魔力侧天顶 ×4
        this.euSpeed = SpireMath.euSpeedScaled(selfEnergyContainer.getInputVoltage(), euSpeedFactor, base_speed);
        setEuCapacityBig(SpireBigMath.euCapacityScaled(selfEnergyContainer.getEnergyCapacity(), euMaxManaCapBig,
                euBaseMaxManaBig));
        getEUContainer();
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
                    } else if (machine instanceof LaserHatchPartMachine laserpart) {
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
                            tieredMachine.getTier() <= this.eutier) {
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
                                ((ITieredMachine) machine).getTier() <= this.eutier) {
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
            long capRem = energyInputContainer.getEnergyCapacity() - energyInputContainer.getEnergyStored();
            capRem = SpireMath.nonNegative(capRem);
            BigInteger stored = getStoredEuBig();
            long takeFromSpire = SpireBigMath.clampToLong(
                    SpireBigMath.min(BigInteger.valueOf(capRem), BigInteger.valueOf(EUs), stored));
            long consume = energyInputContainer.addEnergy(takeFromSpire);
            setStoredEuBig(SpireBigMath.subtractNonNegative(stored, BigInteger.valueOf(consume)));
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
        if (this.energyoutputContainer != null) {
            BigInteger stored = getStoredEuBig();
            BigInteger cap = getEuCapacityBig();
            if (stored.compareTo(cap) >= 0) return;

            BigInteger room = cap.subtract(stored);
            BigInteger pullLimitBd;
            try {
                pullLimitBd = BigInteger.valueOf(Math.multiplyExact(this.euSpeed, 2L));
            } catch (ArithmeticException e) {
                pullLimitBd = BigInteger.valueOf(Long.MAX_VALUE / 4);
            }
            BigInteger avail = BigInteger.valueOf(this.energyoutputContainer.getEnergyStored());
            BigInteger take = SpireBigMath.min(room, pullLimitBd, avail);
            long consume = SpireBigMath.clampToLong(take);
            if (consume <= 0) return;
            setStoredEuBig(SpireBigMath.addCapToMax(stored, BigInteger.valueOf(consume), cap));
            this.energyoutputContainer.removeEnergy(consume);
            if (this.Spark instanceof OmegaSpark omegaSpark) {
                omegaSpark.sendEnergyContainerParticlesReverse(this.euOutputContainerPos);
            }
        }
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (this.isFormed && this.selfEnergyContainer != null) {
            int ti = Math.max(0, Math.min(this.eutier, GTValues.V.length - 1));
            long denom = Math.max(1L, GTValues.V[ti]);
            long displayAmp = this.euSpeed / denom;
            textList.add(omegaSpireStateLang[0].translate(GTValues.VNF[ti])
                    .withStyle(Style.EMPTY.withColor(GTValues.VC[ti])));
            textList.add(omegaSpireStateLang[1].translate(
                    FormattingUtil.formatNumbers(getStoredEuBig()),
                    FormattingUtil.formatNumbers(getEuCapacityBig())));
            textList.add(omegaSpireStateLang[2].translate(this.euSpeed, displayAmp, GTValues.VNF[ti]));
        }
    }

    @CN({
            "当前允许传输的最高电压等级:%s",
            "当前存储量:%s / %s EU",
            "当前最高输入速度:%d EU(%dA %s)"
    })
    @EN({
            "当前存储的电压等级:%.2f",
            "当前存储量:%s / %s EU",
            "当前最高输入速度:%d EU(%dA/%d)"
    })
    public static Lang[] omegaSpireStateLang;

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
