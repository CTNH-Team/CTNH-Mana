package com.magicbee.ctnhmana.mixin.bloodmagic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

import com.magicbee.ctnhmana.event.MinerEliteHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;
import wayoftime.bloodmagic.structures.rooms.DungeonRoomPlacement;

import java.util.List;

/**
 * 血魔法地牢合成器 mixin：在矿工房间放置成功后生成神话精英怪。
 * <p>
 * {@link DungeonSynthesizer#getRandomPlacement} 返回非空即代表该房间确定会被
 * 放置（{@code attemptPlacementOfRandomRoom} 拿到放置对象后必定落房），且此时
 * 携带房间的世界坐标描述符，是触发精英怪生成的唯一完整时机。
 */
@Mixin(value = DungeonSynthesizer.class, remap = false)
public abstract class DungeonSynthesizerMixin {

    @Inject(method = "getRandomPlacement", at = @At("RETURN"))
    private void ctnhmana$spawnMinerElite(ServerLevel world, BlockPos controllerPos, ResourceLocation roomType,
                                          RandomSource rand, BlockPos activatedDoorPos, Direction doorFacing,
                                          String activatedDoorType,
                                          int previousRoomDepth, int previousMaxDepth,
                                          List<ResourceLocation> potentialRooms,
                                          boolean extendCorriDoors, CallbackInfoReturnable<DungeonRoomPlacement> cir) {
        DungeonRoomPlacement placement = cir.getReturnValue();
        if (placement == null) {
            return;
        }
        if (!roomType.getPath().contains("mines")) {
            return;
        }
        MinerEliteHandler.trySpawnMinerElite(world, placement);
    }
}
