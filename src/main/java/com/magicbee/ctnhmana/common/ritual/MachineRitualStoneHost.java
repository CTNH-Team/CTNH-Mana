package com.magicbee.ctnhmana.common.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.magicbee.ctnhmana.common.multiblock.RitualMechanicalMachine;
import com.magicbee.ctnhmana.common.parts.ManaHatches.BloodManaHatch;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumReaderBoundaries;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 将 {@link RitualMechanicalMachine} 伪装成血魔法 {@link IMasterRitualStone} 的适配器。
 * <p>
 * 原版仪式通过 {@link Ritual#performRitual(IMasterRitualStone)} 获取上下文（世界坐标、作用范围、
 * 灵魂网络等），并不关心调用方是主仪式石方块还是 GT 机器。本类负责把机器的坐标、凝聚仓绑定的
 * {@link MachineRitualSoulNetwork} 以及以控制器为中心、半径 {@link #RITUAL_RADIUS} 格的范围注入到仪式逻辑中。
 * <p>
 * <b>设计要点：</b>
 * <ul>
 * <li>不经过 {@link wayoftime.bloodmagic.common.tile.TileMasterRitualStone}，因此跳过仪式石布局校验</li>
 * <li>LP 从凝聚仓储罐经虚拟灵魂网络扣除（{@link #getOwnerNetwork()}），不扣玩家全局 LP</li>
 * <li>所有仪式的 range key（如 harvest 的 {@code harvestRange}、折磨之井的 {@code damage}）
 * 统一映射到 {@link #FIXED_RANGE}，简化初版机器逻辑</li>
 * <li>恶魔意志配置暂返回空列表；需要增强效果时可改为读取区块意志或凝聚仓存储</li>
 * </ul>
 *
 * @see RitualMechanicalMachine#afterWorking() 配方完成后创建本类并调用一次 performRitual
 */
public class MachineRitualStoneHost implements IMasterRitualStone {

    /** 以控制器为中心的作用半径（含 X/Y/Z 三个方向）。 */
    public static final int RITUAL_RADIUS = 21;

    private static final int RITUAL_DIAMETER = RITUAL_RADIUS * 2 + 1;

    /**
     * 固定作用范围：以控制器方块为中心，各轴 ±{@link #RITUAL_RADIUS} 格的立方体区域。
     * <p>
     * {@link AreaDescriptor.Rectangle} 的偏移相对于 {@link #getMasterBlockPos()}，
     * 世界坐标 = 控制器坐标 + 偏移。
     */
    public static final AreaDescriptor FIXED_RANGE = new AreaDescriptor.Rectangle(
            new BlockPos(-RITUAL_RADIUS, -RITUAL_RADIUS, -RITUAL_RADIUS),
            RITUAL_DIAMETER, RITUAL_DIAMETER, RITUAL_DIAMETER);

    /** 提供坐标与世界信息的 GT 多方块控制器 */
    private final RitualMechanicalMachine machine;
    /** 提供 LP 储量的血魔法凝聚仓 */
    private final BloodManaHatch hatch;
    /** 工业机虚拟灵魂网络（仪式前 sync 缓存、仪式后扣减缓存） */
    private final MachineRitualSoulNetwork soulNetwork;
    /** 当前要执行的一次性仪式实例（应为 {@link Ritual#getNewCopy()} 的副本） */
    private Ritual currentRitual;

    public MachineRitualStoneHost(RitualMechanicalMachine machine, BloodManaHatch hatch,
                                  MachineRitualSoulNetwork soulNetwork, Ritual ritual) {
        this.machine = machine;
        this.hatch = hatch;
        this.soulNetwork = soulNetwork;
        this.currentRitual = ritual;
    }

    public void setCurrentRitual(Ritual ritual) {
        this.currentRitual = ritual;
    }

    // ── 灵魂网络 / 所有者 ──────────────────────────────────────────────

    /**
     * 仪式主人 UUID，取自凝聚仓血 Orb 绑定的玩家。
     * 部分仪式（如羽刃、Boss 召唤）会调用此方法；无 Orb 时返回 null。
     */
    @Override
    public UUID getOwner() {
        return soulNetwork != null ? soulNetwork.getOwnerId() : null;
    }

    /**
     * LP 来源：{@link MachineRitualSoulNetwork} 在仪式前从控制器 LP 缓存同步的虚拟网络。
     * 各仪式子类在 performRitual 内通过 {@code getOwnerNetwork().syphon(ticket(...))} 扣 LP。
     */
    @Override
    public SoulNetwork getOwnerNetwork() {
        return soulNetwork != null ? soulNetwork.getDelegate() : null;
    }

    // ── 原版 MRS 生命周期（工业机不走路径，仅占位） ───────────────────

    /** 工业机由 GT 配方驱动，不使用激活水晶激活；始终返回 false */
    @Override
    public boolean activateRitual(ItemStack activationCrystal, Player activator, Ritual ritual) {
        return false;
    }

    /**
     * 若通过 {@link IMasterRitualStone#performRitual(Level, BlockPos)} 调用则转交到仪式逻辑。
     * 实际由 {@link RitualMechanicalMachine} 直接调用 {@link Ritual#performRitual(IMasterRitualStone)}。
     */
    @Override
    public void performRitual(Level world, BlockPos pos) {
        if (currentRitual != null) {
            currentRitual.performRitual(this);
        }
    }

    /** 工业机单次执行，无需 stop 钩子 */
    @Override
    public void stopRitual(Ritual.BreakType breakType) {}

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void setCooldown(int cooldown) {}

    /** 单次 performRitual 调用期间视为“已激活” */
    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setActive(boolean active) {}

    /**
     * 仪式石朝向；工业机无物理仪式石，固定北向。
     * 多数 performRitual 实现不依赖朝向。
     */
    @Override
    public Direction getDirection() {
        return Direction.NORTH;
    }

    @Override
    public boolean areTanksEmpty() {
        return true;
    }

    @Override
    public int getRunningTime() {
        return 0;
    }

    // ── 坐标与世界 ────────────────────────────────────────────────────

    @Override
    public Level getWorldObj() {
        return machine.getLevel();
    }

    /** 范围原点：多方块控制器方块坐标 */
    @Override
    public BlockPos getMasterBlockPos() {
        return machine.getPos();
    }

    // ── 范围与意志（初版固定 / 空实现） ───────────────────────────────

    @Override
    public String getNextBlockRange(String range) {
        return currentRitual != null ? currentRitual.getNextBlockRange(range) : "";
    }

    @Override
    public void provideInformationOfRitualToPlayer(Player player) {}

    @Override
    public void provideInformationOfRangeToPlayer(Player player, String range) {}

    @Override
    public void provideInformationOfWillConfigToPlayer(Player player, List<EnumDemonWillType> typeList) {}

    @Override
    public void setActiveWillConfig(Player player, List<EnumDemonWillType> typeList) {}

    @Override
    public EnumReaderBoundaries setBlockRangeByBounds(Player player, String range, BlockPos offset1,
                                                      BlockPos offset2) {
        return EnumReaderBoundaries.SUCCESS;
    }

    /** 初版不启用恶魔意志增强；返回空表示仪式探知器式的意志配置未开启 */
    @Override
    public List<EnumDemonWillType> getActiveWillConfig() {
        return Collections.emptyList();
    }

    /**
     * 无论仪式定义了多少个 range key，工业机均使用同一以控制器为中心、半径 {@link #RITUAL_RADIUS} 的区域。
     * 多 range 仪式（如折磨之井的 altar + damage）在此版本中共享该范围。
     */
    @Override
    public AreaDescriptor getBlockRange(String range) {
        return FIXED_RANGE;
    }

    @Override
    public void addBlockRanges(Map<String, AreaDescriptor> blockRanges) {}

    @Override
    public void addBlockRange(String range, AreaDescriptor defaultRange) {}

    @Override
    public void setBlockRanges(Map<String, AreaDescriptor> blockRanges) {}

    @Override
    public void setBlockRange(String range, AreaDescriptor defaultRange) {}

    @Override
    public Ritual getCurrentRitual() {
        return currentRitual;
    }
}
