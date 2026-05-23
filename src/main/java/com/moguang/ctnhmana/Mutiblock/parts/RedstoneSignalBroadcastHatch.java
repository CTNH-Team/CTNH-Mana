package com.moguang.ctnhmana.Mutiblock.parts;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;

import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;

import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.Mutiblock.IChannelMachine;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

public class RedstoneSignalBroadcastHatch extends TieredPartMachine {

    public RedstoneSignalBroadcastHatch(IMachineBlockEntity holder) {
        super(holder, GTValues.EV);
    }

    public int redstoneSignalOutput = 0;
    @Persisted
    public int channel = 0;
    protected TickableSubscription tickSubs;

    public IChannelMachine machine;

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTickSubscription));
        }
    }

    @Override
    public void onUnload() {
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
        super.onUnload();
    }

    protected void updateTickSubscription() {
        tickSubs = subscribeServerTick(tickSubs, this::tick);
    }

    public void tick() {
        if (this.getOffsetTimer() % 100 == 0 && this.machine != null) {
            setRedstoneSignalOutput(machine.getChannelSignal(channel));
        }
    }

    public void setRedstoneSignalOutput(int redstoneSignalOutput) {
        this.redstoneSignalOutput = redstoneSignalOutput;
        updateSignal();
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        if (side == getFrontFacing().getOpposite()) {
            return redstoneSignalOutput;
        }
        return 0;
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(Position.ORIGIN, new Size(176, 112));
        var text_filed = new TextFieldWidget(12, 40, 152, 30, () -> String.valueOf(channel), this::update)
                .setNumbersOnly(0, 12)
                .setHoverTooltips(broadcasthatchLang[0].translate());
        group.addWidget(text_filed);
        return group;
    }

    public void update(String value) {
        this.channel = Integer.parseInt(value);
        setRedstoneSignalOutput(channel);
    }

    @Override
    public boolean canShared() {
        return false;
    }

    @Override
    public boolean hasPlayerInventory() {
        return false;
    }

    @Override
    public boolean canConnectRedstone(Direction side) {
        return false;
    }

    @CN({
            "调整链接的机器信号频道，如果机器没有这个频道则默认输出0红石强度",
            "用于输出多方块机器提供地特定频道的红石信号",
            "多个红石信号广播仓可以输出同一频道的信号，通过UI来调整对应的频道"
    })
    @EN({
            "Adjust the linked machine's channel; outputs §70§r if it has no such channel",
            "Outputs the multiblock's redstone strength for the §eselected channel§r",
            "Multiple hatches can share one channel — pick it in the UI"
    })
    public static Lang[] broadcasthatchLang;
}
