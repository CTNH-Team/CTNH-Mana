package com.moguang.ctnhmana.common.blockentity.flower;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.storage.DrumMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;

import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

import java.util.HashMap;
import java.util.Map;

public class BlackVeinMarigoldBlockEntity extends GeneratingFlowerBlockEntity {

    public BlackVeinMarigoldBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int burn_time = 0;
    public int gold = 100000;
    public int status = 0;
    public int mana_per_tick = 0;

    @Override
    public void writeToPacketNBT(CompoundTag tag) {
        super.writeToPacketNBT(tag);
        tag.putInt("BurnTime", burn_time);
        tag.putInt("Gold", gold);
        tag.putInt("Status", status);
        tag.putInt("ManaPerTick", mana_per_tick);
    }

    @Override
    public void readFromPacketNBT(CompoundTag tag) {
        super.readFromPacketNBT(tag);
        burn_time = tag.getInt("BurnTime");
        gold = tag.getInt("Gold");
        status = tag.getInt("Status");
        mana_per_tick = tag.getInt("ManaPerTick");
    }

    @Override
    public int getMaxMana() {
        return 23333;
    }

    public static final Map<Fluid, Integer> FUEL_HEAT_MAP = new HashMap<>();

    static {
        FUEL_HEAT_MAP.put(GTMaterials.Oil.getFluid(), 2000);
        FUEL_HEAT_MAP.put(GTMaterials.OilLight.getFluid(), 3000);                // 轻油（基准锚点）
        FUEL_HEAT_MAP.put(GTMaterials.RawOil.getFluid(), 375);                   // 石油
        FUEL_HEAT_MAP.put(GTMaterials.Naphtha.getFluid(), 16000);                // 石脑油
        FUEL_HEAT_MAP.put(GTMaterials.SulfuricLightFuel.getFluid(), 1000);       // 含硫轻燃料
        FUEL_HEAT_MAP.put(GTMaterials.Methanol.getFluid(), 4800);                // 甲醇
        FUEL_HEAT_MAP.put(GTMaterials.Ethanol.getFluid(), 6900);                 // 乙醇
        FUEL_HEAT_MAP.put(GTMaterials.Octane.getFluid(), 4000);                  // 辛烷
        FUEL_HEAT_MAP.put(GTMaterials.BioDiesel.getFluid(), 12800);              // 生物柴油
        FUEL_HEAT_MAP.put(GTMaterials.LightFuel.getFluid(), 16000);              // 轻燃料
        FUEL_HEAT_MAP.put(GTMaterials.Diesel.getFluid(), 36000);                 // 柴油
        FUEL_HEAT_MAP.put(GTMaterials.CetaneBoostedDiesel.getFluid(), 64000);    // 十六烷增强柴油
        FUEL_HEAT_MAP.put(GTMaterials.RocketFuel.getFluid(), 125000);             // 火箭燃料
        FUEL_HEAT_MAP.put(GTMaterials.Gasoline.getFluid(), 80000);               // 汽油
        FUEL_HEAT_MAP.put(GTMaterials.HighOctaneGasoline.getFluid(), 160000);    // 高辛烷汽油（热值最高）
        FUEL_HEAT_MAP.put(GTMaterials.Toluene.getFluid(), 16000);                // 甲苯

    }

    @Override
    public int getColor() {
        return 0XA120B;
    }

    public int getRange() {
        return 3;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
    }

    @Override
    public void tickFlower() {
        super.tickFlower();
        if (this.level.isClientSide()) return;
        if (burn_time <= 0) {
            if (this.mana_per_tick > 0) {
                this.gold -= this.mana_per_tick * 1000;
                if (this.gold <= 0) {
                    Direction[] horizontalDirections = {
                            Direction.SOUTH,  // 前（南）
                            Direction.NORTH,  // 后（北）
                            Direction.WEST,   // 左（西）
                            Direction.EAST    // 右（东）
                    };
                    for (Direction dir : horizontalDirections) {
                        BlockPos adjacentPos = this.getBlockPos().relative(dir);
                        if (!level.isInWorldBounds(adjacentPos)) {
                            continue;
                        }
                        if (level.getBlockState(adjacentPos).is(Tags.Blocks.STONE)) {
                            level.setBlock(adjacentPos, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
                            gold = 100000;
                            break;
                        }
                    }
                }
                this.mana_per_tick = 0;
            }
            if (this.level.getGameTime() % 20 == 0) {
                searchValidBlock(this.getLevel(), this.getBlockPos(), 2);
            }
        }
        burn_time--;
        this.addMana(mana_per_tick);
    }

    public void searchValidBlock(Level level, BlockPos centerPos, int range) {
        int radius = range;
        int minX = centerPos.getX() - radius;
        int maxX = centerPos.getX() + radius;
        int minY = centerPos.getY() - radius;
        int maxY = centerPos.getY() + radius;
        int minZ = centerPos.getZ() - radius;
        int maxZ = centerPos.getZ() + radius;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    // 检查坐标是否在世界范围内（避免越界）
                    if (level.isInWorldBounds(currentPos)) {
                        BlockState blockState = level.getBlockState(currentPos);
                        Block block = blockState.getBlock();
                        if ((level.getBlockEntity(currentPos) instanceof MetaMachineBlockEntity me &&
                                me.getMetaMachine() instanceof DrumMachine dmachine)) {
                            var fluid = dmachine.getStored();
                            if (FUEL_HEAT_MAP.containsKey(fluid.copy().getFluid()) && fluid.getAmount() >= 1000) {
                                mana_per_tick = FUEL_HEAT_MAP.get(fluid.copy().getFluid()) / 1000;
                                burn_time = 1000;
                                fluid.setAmount(fluid.getAmount() - 1000);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
