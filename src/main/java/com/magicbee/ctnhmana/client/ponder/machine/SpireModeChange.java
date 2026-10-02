// SPDX-License-Identifier: GPL-3.0
// Copyright (C) 2026 mmyddd
package com.magicbee.ctnhmana.client.ponder.machine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.createmod.ponder.api.scene.SceneBuilder;
import net.minecraft.core.BlockPos;

import com.magicbee.ctnhmana.common.multiblock.MysticSpire;
import tech.vixhentx.mcmod.ctnhlib.client.ponder.machine.MachineEdit;
import tech.vixhentx.mcmod.ctnhlib.client.ponder.machine.MachineEdits;

/**
 * 改尖塔的工作模式（聚焦 / 火花扩散 / 凝聚扩散 / 中转）：只写机器上的 {@code MODE}，
 * 主方块界面底部那排模式按钮读的就是它，所以按钮的「按下」状态会跟着一起变。
 *
 * <p>
 * {@code MODE} 是 public 字段，直接写即可，不用反射；回退时把改动前的值写回去。
 */
public final class SpireModeChange implements MachineEdit {

    private final int mode;
    private int previous;

    private SpireModeChange(int mode) {
        this.mode = mode;
    }

    /** 把 {@code spirePos} 那台尖塔切到 {@code mode}。 */
    public static void apply(SceneBuilder scene, BlockPos spirePos, int mode) {
        MachineEdits.add(scene, spirePos, new SpireModeChange(mode), 0);
    }

    @Override
    public void apply(MetaMachine machine, BlockPos machinePos) {
        if (machine instanceof MysticSpire spire) {
            previous = spire.MODE;
            spire.MODE = mode;
        }
    }

    @Override
    public void revert(MetaMachine machine, BlockPos machinePos) {
        if (machine instanceof MysticSpire spire) {
            spire.MODE = previous;
        }
    }
}
