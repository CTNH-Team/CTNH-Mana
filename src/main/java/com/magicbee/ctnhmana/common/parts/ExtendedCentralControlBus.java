package com.magicbee.ctnhmana.common.parts;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.ProgrammableCircuitSlotTrait;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.magicbee.ctnhmana.common.gui.ExtendedCentralControlBusCircuitUi;
import com.magicbee.ctnhmana.common.multiblock.ICentralStorageMachine;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import static net.minecraft.core.Direction.*;

/**
 * 拓展中央存储控制总线：32 路编程电路 + 32 个物品输入槽（按槽位 0→1→… 顺序依次消耗，供所有通道共用）。
 * 机器逻辑参考 {@link CentralControlBus}；UI 见 {@link ExtendedCentralControlBusCircuitUi}。
 */
public class ExtendedCentralControlBus extends ItemBusPartMachine {

    @CN({
            "§6批量§r控制中央存储的多个槽位",
            "具有§n32 路§r物品输入槽与 §n32 路§r可选编程电路",
            "点击电路槽位以配置多个输入槽位，机器运行时，每5tick按顺序将物品输入进配置的输入槽位中",
            "每 5 tick 按顺序向已配置电路所映射的主机槽位尝试放入物品",
            "支持从机器任意面输入物品",
            "§n机器主面§r接收§c红石信号脉冲§r时，一次性弹出§n所有§r已配置且非空的目标槽物品",
            "§o我已经是自动化大师了！§r"
    })
    @EN({
            "§6Batch§r control across central storage slots",
            "§n32§r item input slots (consumed in order 0→1→…) and §n32 optional circuit slots§r",
            "Circuit setup matches GT ghost circuits: left-click a slot to open the 0~32 picker (no physical circuit when ghost mode is on)",
            "Each number maps to a controller storage slot, same as the basic control bus",
            "Empty circuit slots are skipped",
            "Every 5 ticks, inserts from input slots in order 0→1→… into mapped controller slots",
            "Accepts items from any side",
            "A §credstone pulse§r on the §nfront face§r pops §nall§r configured non-empty targets at once",
            "§oOne lane per stream.§r"
    })
    public static Lang[] extendedCentralControlBusLang;

    public static final int CIRCUIT_SLOT_COUNT = 32;

    /** Fancy UI 窗口尺寸，供 {@link ExtendedCentralControlBusCircuitUi} 引用 */
    public static final int UI_WIDTH = 260;
    public static final int UI_HEIGHT = 400;

    @Nullable
    protected TickableSubscription tickSubs;
    protected final ProgrammableCircuitSlotTrait circuitSlot;
    @Persisted
    private boolean lastHadRedstone = false;

    /** 当前编辑的通道（0~31），由 {@link ExtendedCentralControlBusCircuitUi} 维护 */
    @Persisted
    @DescSynced
    public int selectedCircuitSlot = 0;

    public ExtendedCentralControlBus(IMachineBlockEntity holder, int tier) {
        super(holder, tier, IO.IN);
        this.circuitSlot = attachPersistentTrait("extended_circuit_slot",
                new ProgrammableCircuitSlotTrait(this, CIRCUIT_SLOT_COUNT));
        circuitSlot.shouldSearchContent(false);
    }

    @Override
    protected int getInventorySize() {
        return CIRCUIT_SLOT_COUNT;
    }

    public ProgrammableCircuitSlotTrait getCircuitSlot() {
        return circuitSlot;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateControlTickSubscription));
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

    protected void updateControlTickSubscription() {
        tickSubs = subscribeServerTick(tickSubs, this::controlTick);
    }

    public int getTargetSlot(int circuitSlot) {
        if (circuitSlot < 0 || circuitSlot >= CIRCUIT_SLOT_COUNT) {
            return -1;
        }
        ItemStack circuit = this.circuitSlot.getStorage().getStackInSlot(circuitSlot);
        if (circuit.isEmpty() || !IntCircuitBehaviour.isIntegratedCircuit(circuit)) {
            return -1;
        }
        return IntCircuitBehaviour.getCircuitConfiguration(circuit);
    }

    public void controlTick() {
        if (this.getOffsetTimer() % 5 != 0) {
            return;
        }
        var controller = getControllers().isEmpty() ? null : getControllers().first();
        if (!(controller instanceof ICentralStorageMachine imachine)) {
            return;
        }

        int inventorySize = imachine.getInventory().getSlots();
        boolean hasRedstone = getInputSignal() > 0;
        boolean risingEdge = hasRedstone && !lastHadRedstone;

        for (int circuitSlot = 0; circuitSlot < CIRCUIT_SLOT_COUNT; circuitSlot++) {
            int target = getTargetSlot(circuitSlot);
            if (target < 0 || target >= inventorySize) {
                continue;
            }
            if (risingEdge && !imachine.getInventory().getStackInSlot(target).isEmpty()) {
                imachine.popItem(target);
            }
        }

        // 输入槽按 0→1→… 顺序供所有已配置通道共用，不与电路通道一一对应
        int inputSlot = 0;
        for (int circuitSlot = 0; circuitSlot < CIRCUIT_SLOT_COUNT && inputSlot < CIRCUIT_SLOT_COUNT; circuitSlot++) {
            int target = getTargetSlot(circuitSlot);
            if (target < 0 || target >= inventorySize) {
                continue;
            }
            while (inputSlot < CIRCUIT_SLOT_COUNT) {
                ItemStack buffer = getInventory().getStackInSlot(inputSlot);
                if (buffer.isEmpty()) {
                    inputSlot++;
                    continue;
                }
                buffer = imachine.getInventory().insertItem(target, buffer, false);
                getInventory().setStackInSlot(inputSlot, buffer);
                if (!buffer.isEmpty()) {
                    // 目标槽已满，尝试下一个已配置通道
                    break;
                }
                // 当前输入槽已放完，继续用下一输入槽填充同一目标
                inputSlot++;
            }
        }

        lastHadRedstone = hasRedstone;
    }

    private int getInputSignal() {
        Level level = this.getLevel();
        BlockPos sourcePos = this.getPos().relative(this.getFrontFacing());
        return level.getSignal(sourcePos, this.getFrontFacing());
    }

    @Override
    protected void autoIO() {
        if (this.getOffsetTimer() % 5L != 0L) {
            return;
        }
        if (this.isWorkingEnabled() && this.io == IO.IN) {
            this.getInventory().importFromNearby(new Direction[] { DOWN, UP, NORTH, SOUTH, WEST, EAST });
        }
        this.updateInventorySubscription();
    }

    @Override

    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(UI_WIDTH, UI_HEIGHT, this, entityPlayer)
                .widget(new FancyMachineUIWidget(this, UI_WIDTH, UI_HEIGHT));
    }

    @Override
    public Widget createUIWidget() {
        int cols = 8;
        int slot = 18;
        int step = slot + 4;
        int gridW = cols * step + 8;
        int gridH = (CIRCUIT_SLOT_COUNT / cols) * step + 8;
        var group = new DraggableScrollableWidgetGroup(0, 0, UI_WIDTH, gridH + 16);
        var container = new WidgetGroup((UI_WIDTH - gridW) / 2, 8, gridW, gridH);
        for (int i = 0; i < CIRCUIT_SLOT_COUNT; i++) {
            int x = 4 + (i % cols) * step;
            int y = 4 + (i / cols) * step;
            container.addWidget(new SlotWidget(getInventory().storage, i, x, y, true, this.io.support(IO.IN))
                    .setBackgroundTexture(GuiTextures.SLOT)
                    .setIngredientIO(IngredientIO.INPUT));
        }
        container.setBackground(new IGuiTexture[] { GuiTextures.BACKGROUND_INVERSE });
        group.addWidget(container);
        return group;
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);
        sideTabs.attachSubTab(new ExtendedCentralControlBusCircuitUi(this));
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
}
