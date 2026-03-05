package com.moguang.ctnhmana.Mutiblock;

import com.moguang.ctnhmana.Mutiblock.parts.RedstoneSignalBroadcastHatch;

import java.util.List;

public interface IChannelMachine {

    // 用于表示自己是有红石信号频道的机器
    public int getChannelSignal(int channel);

    public List<RedstoneSignalBroadcastHatch> broadcastSelf();
}
