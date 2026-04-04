package com.moguang.ctnhmana.common.blockentity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import com.moguang.ctnhmana.registry.CMBlocks;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

import java.util.ArrayList;
import java.util.List;

public class GenethistleBlockEntity extends GeneratingFlowerBlockEntity {

    public GenethistleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getMaxMana() {
        return 7777777;
    }

    private static final int BASE_MANA = 7 * 4;
    private static final int BONUS_MANA = 7 * 4;
    private static final int FLUID_SCAN_HALF_HEIGHT = 2;
    private static final int RELOCATE_INTERVAL_TICKS = 666;
    private static final int MIN_HORIZONTAL_DIST_SQR = 4;

    @Override
    public void tickFlower() {
        super.tickFlower();
        if (level.isClientSide()) {
            return;
        }
        if (this.ticksExisted % 7 == 0) {
            int mana = BASE_MANA;
            BlockPos pos = getEffectivePos();
            if (level.isRaining()) {
                Biome.Precipitation precip = level.getBiome(pos).value().getPrecipitationAt(pos);
                if (precip == Biome.Precipitation.RAIN) {
                    mana += BONUS_MANA;
                } else if (precip == Biome.Precipitation.SNOW) {
                    mana += BONUS_MANA;
                }
            }
            if (level.isThundering()) {
                mana += BONUS_MANA;
            }
            if (pos.getY() > 77) {
                mana += BONUS_MANA;
            }
            if (hasNearbyWaterOrLava(pos)) {
                mana += BONUS_MANA;
            }
            this.addMana(mana);
        }

        if (ticksExisted > 0 && ticksExisted % RELOCATE_INTERVAL_TICKS == 0) {
            tryBigBang();
        }
    }

    private void tryBigBang() {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        BlockPos origin = getBlockPos();
        int ox = origin.getX();
        int oy = origin.getY();
        int oz = origin.getZ();
        int r = getRange();
        int rSqr = r * r;
        List<BlockPos> candidates = new ArrayList<>();
        for (int x = ox - r; x <= ox + r; x++) {
            for (int z = oz - r; z <= oz + r; z++) {
                int rx = x - ox;
                int rz = z - oz;
                int horizontalDistSqr = rx * rx + rz * rz;
                if (horizontalDistSqr > rSqr || horizontalDistSqr < MIN_HORIZONTAL_DIST_SQR) {
                    continue;
                }
                BlockPos target = new BlockPos(x, oy, z);
                if (!level.hasChunkAt(target)) {
                    continue;
                }
                BlockState place = CMBlocks.GENETHISTLE.getDefaultState();
                if (level.getBlockState(target).isAir() && place.canSurvive(level, target)) {
                    candidates.add(target);
                }
            }
        }
        if (candidates.isEmpty()) {
            level.setBlock(origin, Blocks.AIR.defaultBlockState(), 3);
            return;
        }
        BlockPos chosen = candidates.get(serverLevel.random.nextInt(candidates.size()));
        Vec3 center = Vec3.atCenterOf(origin);
        level.explode(null, center.x, center.y, center.z, 6.0F, Level.ExplosionInteraction.NONE);
        level.setBlock(chosen, CMBlocks.GENETHISTLE.getDefaultState(), 3);
        level.setBlock(origin, Blocks.AIR.defaultBlockState(), 3);
    }

    private boolean hasNearbyWaterOrLava(BlockPos center) {
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();
        int r = getRange();
        int rSqr = r * r;
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                int rx = x - cx;
                int rz = z - cz;
                if (rx * rx + rz * rz > rSqr) {
                    continue;
                }
                for (int y = cy - FLUID_SCAN_HALF_HEIGHT; y <= cy + FLUID_SCAN_HALF_HEIGHT; y++) {
                    BlockPos p = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(p);
                    if (state.getFluidState().is(FluidTags.WATER) || state.getFluidState().is(FluidTags.LAVA)) {
                        return true;
                    }
                    if (state.is(Blocks.WATER) || state.is(Blocks.LAVA)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getColor() {
        return 0XF9F6EF;
    }

    public int getRange() {
        return 7;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
    }
}
