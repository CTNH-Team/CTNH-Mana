package com.magicbee.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

public interface ICentralStorageMachine {

    // 用来标识自己是一个中央有存储的机器
    public NotifiableItemStackHandler getInventory();

    public void popItem(int slot); // 声明自己可以弹出物品到输出总线
}
