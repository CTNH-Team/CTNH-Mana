package com.moguang.ctnhmana.Mutiblock.parts;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.Mutiblock.ICentralStorageMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.openjdk.nashorn.internal.objects.annotations.Getter;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.List;

import static net.minecraft.core.Direction.*;

public class CentralControlBus extends ItemBusPartMachine {
    private ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            RedstoneSignalBroadcastHatch.class, TieredPartMachine.MANAGED_FIELD_HOLDER);
    protected @Nullable ISubscription metasubs;
    protected TickableSubscription tickSubs;
    public CentralControlBus(IMachineBlockEntity holder, int tier) {
        super(holder, tier, IO.IN);
    }
    @Override
    protected int getInventorySize() {
        return 1;
    }
    @Persisted
    public int meta=0;
    @Persisted // 如果你希望存档/掉线后也保持状态；不需要可去掉
    private boolean lastHadRedstone = false;

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateIOTickSubscription));
        }
        if (this.isHasCircuitSlot() && this.circuitInventory != null) {
            metasubs = circuitInventory.addChangedListener(this::updateMetaSubscription);
        }
    }
    public void updateMetaSubscription()
    {
        meta= IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.getStackInSlot(0));
    }

    @Override
    public void onUnload() {
        if(metasubs!=null)
        {
            metasubs.unsubscribe();;
            metasubs=null;
        }
        if(tickSubs!=null)
        {
            tickSubs.unsubscribe();;
            tickSubs=null;
        }
        super.onUnload();
    }
    public void updateIOTickSubscription()
    {
        tickSubs = subscribeServerTick(tickSubs, this::controlTick);
    }
    public void controlTick()
    {
        if(this.getOffsetTimer()%5==0)
        {
            var controller = getControllers().isEmpty() ? null : getControllers().first();
            if(controller instanceof ICentralStorageMachine imachine)
            {
                if(this.meta>-1&&this.meta<imachine.getInventory().getSize())
                {
                    if(getInputSignal()>0&&!lastHadRedstone&&!imachine.getInventory().getStackInSlot(meta).isEmpty())imachine.popItem(meta);
                    if(this.getInventory().getStackInSlot(0).isEmpty()) {
                        var item = this.getInventory().getStackInSlot(0);
                        item = imachine.getInventory().insertItem(meta, item, false);
                        if (item.isEmpty()) this.getInventory().setStackInSlot(0, ItemStack.EMPTY);
                    }
                }
                this.lastHadRedstone= getInputSignal() > 0;
            }
        }

    }
    private int getInputSignal() {
        Level level = this.getLevel();
        BlockPos sourcePos = this.getPos().relative(this.getFrontFacing());
        return level.getSignal(sourcePos, this.getFrontFacing());
    }
    @Override
    protected void autoIO() {
        if (this.getOffsetTimer() % 5L == 0L) {
            if (this.isWorkingEnabled()) {
                if (this.io == IO.OUT) {
                    this.getInventory().exportToNearby(new Direction[]{this.getFrontFacing()});
                } else if (this.io == IO.IN) {
                    this.getInventory().importFromNearby(new Direction[]{DOWN,UP,NORTH,SOUTH,WEST,EAST});
                } else if (this.io == IO.BOTH) {
                    this.getInventory().importFromNearby(new Direction[]{this.getFrontFacing()});
                    this.getInventory().exportToNearby(new Direction[]{this.getFrontFacing().getOpposite()});
                }
            }
            this.updateInventorySubscription();
        }
    }
    @Override
    public Widget createUIWidget() {
        int rowSize = (int)Math.sqrt((double)this.getInventorySize());
        int colSize = rowSize;
        if (this.getInventorySize() == 8) {
            rowSize = 4;
            colSize = 2;
        }

        var group = new DraggableScrollableWidgetGroup(0, 0, 176, 124);
        var container = new WidgetGroup(176/2-13, 124/2-26, 26, 26);
        int index = 0;

        for(int y = 0; y < colSize; ++y) {
            for(int x = 0; x < rowSize; ++x) {
                container.addWidget((new SlotWidget(this.getInventory().storage, index++, 4 + x * 18, 4 + y * 18, true, this.io.support(IO.IN))).setBackgroundTexture(GuiTextures.SLOT).setIngredientIO(this.io == IO.IN ? IngredientIO.INPUT : IngredientIO.OUTPUT));
            }
        }

        container.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
        group.addWidget(container);
        return group;
    }
    @Override
    public boolean canShared() {
        return false;
    }
    @Override
    public boolean hasPlayerInventory() {
        return true;
    }
    @Override
    public boolean canConnectRedstone(Direction side) {
        return true;
    }
    @CN(
            {
                    "§6完美§r地控制每一个中央槽位",
                    "该机器用于精确控制§n多方块结构主机器UI§r的存储槽位",
                    "机器的电路编号用于指定该机器控制的槽位编号(从0开始)",
                    "支持从机器的任意面输入物品，并且会将此物品插入指定槽位(如果存在)",
                    "§n机器的主面§r接收§c红石信号脉冲§r时，将该槽位的物品弹出到§n具有输出功能的§r舱室中",
                    "§o准备好成为自动化大师了吗？§r"
            }
    )
    @EN(
            {
                    "§6完美§r地控制每一个中央槽位",
                    "该机器用于精确控制§n多方块结构主机器UI§r的存储槽位",
                    "机器的电路编号用于指定该机器控制的槽位编号(从0)开始",
                    "支持从机器的任意面输入物品，并且会将此物品插入指定槽位(如果存在)",
                    "§n机器的主面§r接收§c红石信号脉冲§r时，将该槽位的物品弹出到§n具有输出功能的§r舱室中",
                    "§o准备好成为自动化大师了吗？§r"
            }
    )
    public static Lang[] centralControlBusLang;
}
