package com.moguang.ctnhmana.common.ritual;

import com.moguang.ctnhmana.Mutiblock.RitualMechanicalMachine;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatches.BloodManaHatch;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;

import java.util.UUID;

/**
 * 工业仪式阵专用的「虚拟」灵魂网络：仅存在于内存，不注册进 {@code BMWorldSavedData}。
 * <p>
 * {@link #playerId} 与凝聚仓血 Orb 绑定主人一致，供需要 {@code getOwner()} 的仪式使用；
 * LP 数值在每次仪式前从凝聚仓同步，{@link #syphon} 只修改本对象上的 {@code currentEssence}，
 * 由 {@link com.moguang.ctnhmana.Mutiblock.RitualMechanicalMachine} 在仪式后写回凝聚仓储罐。
 */
public class MachineRitualSoulNetwork {

    private final SoulNetwork delegate;
    private final BloodManaHatch hatch;

    public MachineRitualSoulNetwork(UUID ownerId, BloodManaHatch hatch) {
        this.delegate = SoulNetwork.newEmpty(ownerId);
        this.hatch = hatch;
    }

    public SoulNetwork getDelegate() {
        return delegate;
    }

    public UUID getOwnerId() {
        return delegate.getPlayerId();
    }

    /** 将凝聚仓当前 LP 储量同步到虚拟网络的 currentEssence。 */
    public void syncFromHatch() {
        long available = RitualMechanicalMachine.getAvailableLp(hatch);
        delegate.setCurrentEssence((int) Math.min(Integer.MAX_VALUE, available));
    }

    /**
     * 将虚拟网络上已 syphon 的 LP 差额写回凝聚仓。
     *
     * @param essenceBefore 仪式执行前 {@link #syncFromHatch()} 后的 essence
     */
    public void applyDrainToHatch(int essenceBefore) {
        int drained = Math.max(0, essenceBefore - delegate.getCurrentEssence());
        if (drained > 0) {
            RitualMechanicalMachine.drainLp(hatch, drained);
        }
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
