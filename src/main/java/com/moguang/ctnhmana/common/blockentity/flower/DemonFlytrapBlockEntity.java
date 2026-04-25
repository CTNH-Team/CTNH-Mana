package com.moguang.ctnhmana.common.blockentity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import com.moguang.ctnhmana.registry.CMMobEffects;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;

import java.util.List;

public class DemonFlytrapBlockEntity extends FunctionalFlowerBlockEntity {

    public DemonFlytrapBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int MAX_WILL = 110;

    @Override
    public void tickFlower() {
        super.tickFlower();
        if (!this.getLevel().isClientSide && this.redstoneSignal <= 0) {
            if (ticksExisted % 10 == 0) {
                var chunk = WorldDemonWillHandler.getWillChunk(getLevel(), getEffectivePos());
                if (getMana() < getCost()||getMonsters()==null) return;

                addMana(-getCost());
                for (var monster : getMonsters()) {
                    if (monster.getHealth() <= 6 && !monster.getPersistentData().getBoolean("isDead")) {
                        monster.kill();
                        monster.getPersistentData().putBoolean("isDead", true);
                        if(chunk.getCurrentWill().getWill(EnumDemonWillType.DEFAULT)<110)
                        chunk.getCurrentWill().addWill(EnumDemonWillType.DEFAULT, monster.getMaxHealth() / 20,
                                MAX_WILL);
                    } else {
                        monster.hurt(getLevel().damageSources().magic(), 6);
                        monster.addEffect(new MobEffectInstance(CMMobEffects.SOUL_LEECH.get(), 88, 0));
                    }

                }
            }
        }
    }

    public List<Monster> getMonsters() {
        var bound = new AABB(getEffectivePos()).inflate(getRange());
        return getLevel().getEntitiesOfClass(Monster.class, bound);
    }

    public int getRange() {
        return 6;
    }

    public int getCost() {
        return 666;
    }

    @Override
    public int getMaxMana() {
        return this.getCost();
    }

    @Override
    public int getColor() {
        return 0x2af5e1;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(bindingPos, getRange());
    }
}