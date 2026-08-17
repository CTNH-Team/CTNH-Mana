package com.magicbee.ctnhmana.event;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.api.machine.gem.GemSublimatorRules;
import com.magicbee.ctnhmana.registry.CMMobEffects;
import dev.shadowsoffire.apotheosis.adventure.boss.ApothBoss;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.structures.rooms.DungeonRoomPlacement;

import java.util.List;

/**
 * 无尽领域矿工房间的精英怪（神话品质随机 Boss）与死亡宝箱。
 * <p>
 * 由 {@code DungeonSynthesizerMixin} 在矿工房间放置成功后调用
 * {@link #trySpawnMinerElite}：在房间内选取一个有效位置，生成随机的
 * mythic 品质神话 Boss，并附加无限时长的苦难护盾效果。
 * 该怪死亡后会在原地留下一个装有 {@code ctnhmana:chests/miner_elite}
 * 战利品的宝箱。
 */
@Mod.EventBusSubscriber(modid = CTNHMana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MinerEliteHandler {

    /** 精英怪 NBT 标记，用于死亡落箱判定。 */
    public static final String TAG_MINER_ELITE = "ctnhmana_miner_elite";

    /** 精英宝箱战利品表。 */
    public static final ResourceLocation ELITE_LOOT = ResourceLocation.fromNamespaceAndPath(CTNHMana.MODID,
            "chests/miner_elite");

    /** 矿工房间池路径特征（与 {@code PerfectMineKeyItem} 的判定一致）。 */
    private static final String MINE_POOL_PATH = "mines";

    /**
     * 房间放置成功后尝试生成精英怪。
     * 仅矿工房间池（{@code room_pools/mines/*}）触发；连廊兜底等其余池忽略。
     * <p>
     * 生成位置取开房玩家（房间中心附近最近玩家）前方 3 格、高 1 格处，
     * 清空阻碍方块后以雷击特效落场。
     *
     * @param placement 已放置房间的几何信息（世界坐标描述符 + 随机）
     */
    public static void trySpawnMinerElite(ServerLevel world, DungeonRoomPlacement placement) {
        ServerPlayer player = nearestPlayer(world, placement.getAreaDescriptors());
        if (player == null) {
            return;
        }
        // 玩家前方 3 格、高 1 格
        BlockPos spawnPos = player.blockPosition().relative(player.getDirection(), 3).above(1);
        // 兜底：脚下无立足点时向下找最近的实心方块（最多下探 8 格）
        BlockPos ground = spawnPos;
        int fallback = 0;
        while (fallback++ < 8 && ground.getY() > world.getMinBuildHeight() &&
                !world.getBlockState(ground.below()).isSolid()) {
            ground = ground.below();
        }
        if (fallback <= 8) {
            spawnPos = ground;
        }
        // 清空可能阻碍生成的方块（水平 3x3、高度 3）
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 2; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    world.setBlockAndUpdate(spawnPos.offset(dx, dy, dz), Blocks.AIR.defaultBlockState());
                }
            }
        }
        // 雷击特效
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(world);
        if (bolt != null) {
            bolt.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
            bolt.setVisualOnly(true);
            world.addFreshEntity(bolt);
        }
        LootRarity mythic = GemSublimatorRules.rarityByPath("mythic");
        if (mythic == null) {
            return;
        }
        // 从自建 mythic 池随机抽取（主世界/下界/末地实体，已剔除会脱离房间的问题怪）
        ApothBoss boss = MythicBossPool.getRandom(world.getRandom(), mythic);
        if (boss == null) {
            return;
        }
        Mob elite = boss.createBoss(world, spawnPos, world.getRandom(), 0F, mythic);
        // 无限时长的苦难护盾：百分比减伤、移动加速、每 tick 生命恢复
        elite.addEffect(
                new MobEffectInstance(CMMobEffects.PAIN_SHIELD.get(), Integer.MAX_VALUE, 2, false, false));
        elite.getPersistentData().putBoolean(TAG_MINER_ELITE, true);
        elite.setPersistenceRequired();
        world.addFreshEntityWithPassengers(elite);
    }

    /** 取房间描述符合并包围盒中心附近最近的玩家（开房者）。 */
    private static ServerPlayer nearestPlayer(ServerLevel world, List<AreaDescriptor> descriptors) {
        BlockPos center = roomCenter(descriptors);
        if (center == null) {
            return null;
        }
        return (ServerPlayer) world.getNearestPlayer(center.getX(), center.getY(), center.getZ(), -1.0, false);
    }

    /** 合并所有描述符得到房间的世界坐标包围盒中心。 */
    private static BlockPos roomCenter(List<AreaDescriptor> descriptors) {
        if (descriptors.isEmpty()) {
            return null;
        }
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (AreaDescriptor descriptor : descriptors) {
            if (!(descriptor instanceof AreaDescriptor.Rectangle rect)) {
                continue;
            }
            BlockPos min = rect.getMinimumOffset();
            BlockPos max = rect.getMaximumOffset(); // 非包含
            minX = Math.min(minX, min.getX());
            minY = Math.min(minY, min.getY());
            minZ = Math.min(minZ, min.getZ());
            maxX = Math.max(maxX, max.getX());
            maxY = Math.max(maxY, max.getY());
            maxZ = Math.max(maxZ, max.getZ());
        }
        if (minX == Integer.MAX_VALUE) {
            return null;
        }
        return new BlockPos((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2);
    }

    /** 精英怪死亡后在原地留下宝箱，战利品指向 {@link #ELITE_LOOT}。 */
    @SubscribeEvent
    public static void onMinerEliteDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide() || !entity.getPersistentData().getBoolean(TAG_MINER_ELITE)) {
            return;
        }
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }
        BlockPos pos = entity.blockPosition();
        level.setBlockAndUpdate(pos, Blocks.CHEST.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
            chest.setLootTable(ELITE_LOOT, level.getRandom().nextLong());
        }
    }
}
