package com.magicbee.ctnhmana.common.blockentity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import com.magicbee.ctnhmana.common.item.equipment.KoishiEyeItem;
import com.magicbee.ctnhmana.registry.CMItems;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.bauble.ThirdEyeItem;

import java.util.List;

public class ParaRosiaBlockEntity extends GeneratingFlowerBlockEntity {

    public ParaRosiaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getMaxMana() {
        return 82400;
    }

    @Override
    public int getColor() {
        return 0;
    }

    public int getRange() {
        return 10;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(bindingPos, getRange());
    }

    @Override
    public void tickFlower() {
        super.tickFlower();
        if (ticksExisted % 51 == 4 && !this.getLevel().isClientSide()) {
            Player satori = null;
            Player koishi = null;
            for (LivingEntity entity : getMonsters()) {

                if (entity instanceof Player player) {
                    double playerToFlowerDistance = getDistanceToFlowerSqr(player);

                    ItemStack item = EquipmentHandler.findOrEmpty(BotaniaItems.thirdEye, player);
                    if (item.getItem() instanceof ThirdEyeItem) {
                        satori = player;
                    }
                    item = EquipmentHandler.findOrEmpty(CMItems.KOISHI_EYE.get(), player);
                    if (item.getItem() instanceof KoishiEyeItem) {
                        koishi = player;
                    }
                }
                if (satori != null && koishi != null) {
                    if (getDistanceToFlowerSqr(satori) <= 10 && getDistanceToFlowerSqr(koishi) >= 51.4) {
                        this.addMana((int) (((10 - getDistanceToFlowerSqr(satori)) / 10 +
                                51.4 / (getDistanceToFlowerSqr(koishi))) * 82400));
                        if (satori.getName().getString().contains("satori") &&
                                koishi.getName().getString().contains("koishi")) {
                            this.addMana(81400);
                        } else {
                            applyPullOrPush(satori, 0.514D, false);
                            applyPullOrPush(koishi, 0.11D, true);
                        }
                    }
                    break;
                }
            }
        }
    }

    public double getDistanceToFlowerSqr(Player player) {
        BlockPos flowerPos = getEffectivePos();
        double flowerCenterX = flowerPos.getX() + 0.5D;
        double flowerCenterY = flowerPos.getY() + 0.5D;
        double flowerCenterZ = flowerPos.getZ() + 0.5D;
        return player.distanceToSqr(flowerCenterX, flowerCenterY, flowerCenterZ);
    }

    public void applyPullOrPush(Player player, double strength, boolean pull) {
        if (level == null || level.isClientSide) return; // 只在服务端施加动量

        BlockPos flowerPos = getEffectivePos();
        double centerX = flowerPos.getX() + 0.5D;
        double centerY = flowerPos.getY() + 0.5D;
        double centerZ = flowerPos.getZ() + 0.5D;

        double dx = centerX - player.getX();
        double dy = centerY - player.getY();
        double dz = centerZ - player.getZ();
        double lenSqr = dx * dx + dy * dy + dz * dz;
        if (lenSqr < 1.0e-6D) return;

        double len = Math.sqrt(lenSqr);
        double nx = dx / len;
        double ny = dy / len;
        double nz = dz / len;
        double dir = pull ? 1.0D : -1.0D;

        player.push(nx * strength * dir, ny * strength * dir, nz * strength * dir);
        player.hurtMarked = true; // 标记速度变化，确保及时同步客户端
    }

    public List<LivingEntity> getMonsters() {
        var bound = new AABB(getEffectivePos()).inflate(getRange());
        return getLevel().getEntitiesOfClass(LivingEntity.class, bound);
    }
}
