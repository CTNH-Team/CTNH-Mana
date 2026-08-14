package com.magicbee.ctnhmana.common.item.dungeon;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.resources.ResourceLocation;

import wayoftime.bloodmagic.common.item.dungeon.IDungeonKey;
import wayoftime.bloodmagic.structures.ModRoomPools;

import java.util.List;

/**
 * 完善的工头钥匙。
 * <p>
 * 地牢封印的开启只检查物品是否实现了 {@link IDungeonKey}，以及返回的房间池是否为空；
 * 封印自身的房间池限制不会在控制器处被校验。但放置房间时 BM 会以封印的门类型去匹配
 * 新房间的门：普通锁的门类型是 "default"，矿区房间（{@code room_pools/mines/*}）的门
 * 类型是 "mine"，直接请求矿区池会全部匹配失败并落入连廊兜底。
 * <p>
 * 因此按封印的房间池自适应：
 * <ul>
 * <li>普通封印（房间池不含 {@code mines}，门类型 "default"）：请求矿区入口池
 * {@link ModRoomPools#MINE_ENTRANCES}——其中的矿区入口房间自带 "default" 门，
 * 可挂在普通锁上，其内部另有 "mine" 门通向矿区；</li>
 * <li>矿区门（房间池含 {@code mines}，门类型 "mine"）：请求矿区房间池
 * {@link ModRoomPools#MINE_ROOMS}，直接进入矿区。</li>
 * </ul>
 */
public class PerfectMineKeyItem extends ComponentItem implements IDungeonKey {

    public PerfectMineKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public ResourceLocation getValidResourceLocation(List<ResourceLocation> list) {
        if (list.isEmpty()) {
            return null;
        }
        boolean mineDoor = list.stream().anyMatch(rl -> rl.getPath().contains("mines"));
        return mineDoor ? ModRoomPools.MINE_ROOMS : ModRoomPools.MINE_ENTRANCES;
    }
}
