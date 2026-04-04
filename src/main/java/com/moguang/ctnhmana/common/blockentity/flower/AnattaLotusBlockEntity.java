package com.moguang.ctnhmana.common.blockentity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;

import com.moguang.ctnhmana.registry.CMBlocks;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.List;

public class AnattaLotusBlockEntity extends GeneratingFlowerBlockEntity {

    public AnattaLotusBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int MaxMana = 3000;
    public boolean rebirth = false;
    public int waiting_time = 3000;

    @Override
    public int getColor() {
        return 0x785000;
    }

    @Override
    public void tickFlower() {
        super.tickFlower();
        if (!this.level.isClientSide() && ticksExisted % waiting_time == 0) {
            if (this.rebirth) {
                var serverLevel = level;
                var pos = this.getBlockPos();
                int respawnAt = level.getServer().getTickCount() + 2000;
                level.getServer().tell(new TickTask(respawnAt, () -> {
                    if (!level.isLoaded(pos)) return;
                    BlockState current = level.getBlockState(pos);
                    if (current.isAir() || current.getBlock() instanceof BaseFireBlock) {
                        level.setBlock(pos, CMBlocks.ANATTA_LOTUS.getDefaultState(), 3);
                    }
                    burnAround((ServerLevel) level, pos, 3);
                }));
                level.removeBlockEntity(this.getBlockPos()); // 从世界中移除旧BlockEntity
                this.setRemoved(); // 终止BlockEntity的所有逻辑

            }
            getLevel().playSound(null, getEffectivePos(), BotaniaSounds.endoflame, SoundSource.BLOCKS, 1F, 1F);
            int totalBurnTime = processGroundItemsAndIgnite();
            if (!this.rebirth) this.MaxMana = 3000000;
            this.rebirth = true;
            this.waiting_time = 300;
            if (totalBurnTime > 0) {
                this.addMana(Math.min(getMaxMana() - getMana(), totalBurnTime / 30));
            }
        }
    }

    @Override
    public int getMaxMana() {
        return this.MaxMana;
    }

    public void burn(ServerLevel serverLevel, BlockPos firePos) {
        if (!BaseFireBlock.canBePlacedAt(serverLevel, firePos, Direction.UP)) {
            firePos = firePos.above();
        }
        if (serverLevel.getBlockState(firePos).isAir() &&
                BaseFireBlock.canBePlacedAt(serverLevel, firePos, Direction.UP)) {
            BlockState fireState = BaseFireBlock.getState(serverLevel, firePos);
            serverLevel.setBlock(firePos, fireState, 3);
        }
    }

    public void burnAround(ServerLevel serverLevel, BlockPos center, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue;
                burn(serverLevel, center.offset(dx, 0, dz));
            }
        }
    }

    /**
     * 扫描同高度平面内掉落物，统计可燃总时间并点燃它们所在位置/实体。
     */
    private int processGroundItemsAndIgnite() {
        BlockPos center = getEffectivePos();
        int radius = getRange();
        double y = center.getY();

        AABB scanBox = new AABB(
                center.getX() - radius, y - 0.5D, center.getZ() - radius,
                center.getX() + radius + 1, y + 0.5D, center.getZ() + radius + 1);
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, scanBox);
        int totalBurnTime = 0;
        for (ItemEntity itemEntity : items) {
            if (!itemEntity.onGround()) continue;
            double dx = itemEntity.getX() - (center.getX() + 0.5D);
            double dz = itemEntity.getZ() - (center.getZ() + 0.5D);
            if (dx * dx + dz * dz > radius * radius) continue;
            ItemStack stack = itemEntity.getItem();
            int burnPerItem = ForgeHooks.getBurnTime(stack, null);
            if (burnPerItem <= 0) continue;
            totalBurnTime += burnPerItem * stack.getCount();
            igniteItemPosition(itemEntity);
        }
        return totalBurnTime;
    }

    private void igniteItemPosition(ItemEntity itemEntity) {
        BlockPos itemPos = itemEntity.blockPosition();
        BlockPos firePos = itemPos;
        if (!BaseFireBlock.canBePlacedAt(level, firePos, Direction.UP)) {
            firePos = firePos.above();
        }
        if (level.getBlockState(firePos).isAir() && BaseFireBlock.canBePlacedAt(level, firePos, Direction.UP)) {
            BlockState fire = BaseFireBlock.getState(level, firePos);
            level.setBlock(firePos, fire, 3);
        }
        itemEntity.setSecondsOnFire(10);
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
    }

    public int getRange() {
        return 3;
    }
}
