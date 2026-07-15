package com.moguang.ctnhmana.common.ritual;

import com.moguang.ctnhmana.common.multiblock.RitualMechanicalMachine;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;

import java.util.UUID;

/**
 * 工业仪式阵专用的「虚拟」灵魂网络：仅存在于内存，不注册进 {@code BMWorldSavedData}。
 * <p>
 * {@link #playerId} 与凝聚仓血 Orb 绑定主人一致，供需要 {@code getOwner()} 的仪式使用；
 * LP 数值来自控制器上持久化的 {@link RitualMechanicalMachine#ritualNetworkLp}，
 * 成型/加载后由 {@link RitualMechanicalMachine#syncPersistedLpToSoulNetwork()} 写回本对象；
 * {@link #syphon} 只修改 {@code currentEssence}，仪式结束后由控制器写回 {@code ritualNetworkLp}。
 */
public class MachineRitualSoulNetwork {

    private final SoulNetwork delegate;

    public MachineRitualSoulNetwork(UUID ownerId) {
        this.delegate = SoulNetwork.newEmpty(ownerId);
    }

    public SoulNetwork getDelegate() {
        return delegate;
    }

    public UUID getOwnerId() {
        return delegate.getPlayerId();
    }

    /** 将控制器 LP 缓存同步到虚拟网络的 currentEssence。 */
    public void syncFromCache(int cachedLp) {
        delegate.setCurrentEssence((int) Math.min(Integer.MAX_VALUE, Math.max(0, cachedLp)));
    }

    /**
     * 计算仪式执行期间 syphon 的 LP 量。
     *
     * @param essenceBefore 仪式执行前 {@link #syncFromCache(int)} 后的 essence
     */
    public int getDrainedAmount(int essenceBefore) {
        return Math.max(0, essenceBefore - delegate.getCurrentEssence());
    }

    /** 工业机 LP 不足时不恶心绑定玩家（尤其主人离线时）。 */
    public void causeNausea() {
        // no-op
    }

    public int getCurrentEssence() {
        return delegate.getCurrentEssence();
    }

    public int syphon(SoulTicket ticket) {
        return delegate.syphon(ticket);
    }
}