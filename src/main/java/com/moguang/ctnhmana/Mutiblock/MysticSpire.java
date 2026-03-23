package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import com.moguang.ctnhmana.item.Rune.SpireUpgradeRuneItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.AABB;

import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import com.moguang.ctnhmana.common.entity.DeltaSpark;
import com.moguang.ctnhmana.registry.CMEntities;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.List;
import java.util.Map;

public class MysticSpire extends WorkableMultiblockMachine implements IFancyUIMachine,
                         IDisplayUIMachine {

    private static final double UI_SCALE = 1.5;



    @Nullable
    protected TickableSubscription TickSubs;
    @Persisted
    public final NotifiableItemStackHandler machineStorage;

    private static int sc(int v) {
        return (int) Math.round(v * UI_SCALE);
    }

    public MysticSpire(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.sparkpos = MachineUtils.getOffset(this, 0, 10, 1);
        // 4 槽位：用于与尼克尔戴森光束一致的槽位承载逻辑（仅 UI 展示/存放）。
        this.machineStorage = new NotifiableItemStackHandler(
                this, 4, IO.NONE, IO.BOTH, slots -> new CustomItemStackHandler(4) {

        });
        this.machineStorage.setFilter(itemStack -> itemStack.getItem() instanceof SpireUpgradeRuneItem);

    }

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MysticSpire.class,
            WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    public BlockPos connectedSparkPos;
    public DeltaSpark Spark;

    @Persisted
    public int MODE = 2;

    public DeltaSpark ConnectedSpark;
    @Persisted
    public int range = 20;
    @Persisted
    public int speed = 10000;
    @Persisted
    public int TargetNum = 3;
    @Persisted
    public int maxMana = 1000;
    @Persisted
    public BlockPos sparkpos;
    @Persisted
    public boolean isAnimationActive = true;
    @Persisted
    public int receive_rate = 10000;
    @Persisted
    public DyeColor network = DyeColor.WHITE;
    @Persisted
    public double effencicy=0.1;
    public int base_speed=10000;
    public int base_maxmana=10000000;
    public double base_effencicy=0.1;
    public int base_range=15;
    public final Map<Integer, Lang> mode_MAP = Map.of(
            0, spireModeLang[0],
            1, spireModeLang[1],
            2, spireModeLang[2],
            3, spireModeLang[3]);

    //////////////////////////////////////
    // ******** Subscriptions&Ticks ********//
    //////////////////////////////////////
    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTick));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (TickSubs != null) {
            TickSubs.unsubscribe();
            TickSubs = null;
        }
    }
    public void updateTick() {
        TickSubs = subscribeServerTick(TickSubs, this::metircTick);
    }

    public void metircTick()
    {
        if(this.getOffsetTimer()%100==0)
        {
            getOrCreatedSpark();
        }
    }
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();


        // ((MysticSpireBlockEntity) this.holder).receiveMana(100000);
        if (connectedSparkPos != null &&
                !this.getLevel().getEntitiesOfClass(DeltaSpark.class, new AABB(connectedSparkPos)).isEmpty()) {
            this.ConnectedSpark = this.getLevel().getEntitiesOfClass(DeltaSpark.class, new AABB(connectedSparkPos))
                    .get(0);
        }

        getOrCreatedSpark();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        // 虽然德尔塔火花在检测到结构失效后会自行消灭，但考虑到持久化的因素，双向记录数据和摧毁是保险的，这绝对不是什么PTSD
        if (Spark != null) {
            this.Spark.kill();
            this.Spark = null; // 这个过程只消灭绑定的火花本身，而保留其他数据
        }
    }

    public void getOrCreatedSpark() {
        updateSelf();
        if (this.getLevel().isClientSide) return;
        AABB locate = new AABB(sparkpos);
        if (!this.getLevel().getEntitiesOfClass(DeltaSpark.class, locate).isEmpty()) {
            this.Spark = this.getLevel().getEntitiesOfClass(DeltaSpark.class, locate).get(0);
            updateSpark();
        } else {
            var entity = CMEntities.DELTA_SPARK.create(getLevel());
            entity.AttachPos = this.getPos();
            entity.range = this.range;
            entity.speed = this.speed;
            entity.TargetNum = this.TargetNum;
            entity.effencicy=this.effencicy;
            entity.mode = MODE;
            entity.setNetwork(network);
            entity.setPos(sparkpos.getX(), sparkpos.getY(), sparkpos.getZ());
            if (connectedSparkPos != null &&
                    !this.getLevel().getEntitiesOfClass(DeltaSpark.class, new AABB(connectedSparkPos)).isEmpty()) {
                entity.connectedDeltaSpark = ConnectedSpark;
                entity.connectedDeltaSparkPos = new AABB(ConnectedSpark.getOnPos());
            }
            getLevel().addFreshEntity(entity);
        }
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        if (isFormed()) {
            var pool = ((MysticSpireBlockEntity) this.holder);
            textList.add(spireDataLang[0].translate(pool.BTMana, pool.maxBTMana));
            textList.add(spireDataLang[1].translate(mode_MAP.get(MODE).translate()));
            textList.add(spireDataLang[2].translate(speed));
            textList.add(spireDataLang[4].translate(range));
            if (this.connectedSparkPos != null) {
                textList.add(spireDataLang[5].translate(connectedSparkPos.getX(), connectedSparkPos.getY(),
                        connectedSparkPos.getZ()));
            }
        }
    }

    @Override
    public Widget createUIWidget() {
        int groupW = sc(182 + 8 + 20);
        int groupH = sc(117 + 8);
        var group = new WidgetGroup(0, 0, groupW, groupH);
        
        // 背景/信息面板区域：整体按比例缩放
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, sc(182 + 20)+8, sc(117)+8)
                .setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                        .textSupplier(this.getLevel().isClientSide ? null : this::addDisplayText)
                        .setMaxWidthLimit(sc(200))
                        .clickHandler(this::handleDisplayClick)));

        // 中央 4 槽位（2x2），位置参考尼克尔戴森光束的 SlotWidget 写法，仅改坐标到中心区域
        int tile = sc(18); // 槽位间距/尺寸的近似步长（与现有 UI 代码保持一致）
        int startX = (groupW - tile * 2) / 2;
        int slotY = (groupH - sc(18) * 2) / 2;

        for (int i = 0; i < 4; i++) {
            int col = i % 2;
            int row = i / 2;
            int x = startX + col * tile;
            int y = slotY + row * sc(18);
            group.addWidget(
                    new SlotWidget(machineStorage.storage, i, x, y, true, true)
                            .setBackground(GuiTextures.SLOT));
        }

        // 按钮不放大：固定 20x20，放到底部，并在左右/按钮间保持等分对齐
        int buttonW = 20;
        int buttonH = 20;
        int buttonCount = 5;
        int bottomY = groupH - buttonH - sc(5);
        double space = (groupW - buttonW * buttonCount) / (double) (buttonCount + 1);
        int x0 = (int) Math.round(space * 1 + buttonW * 0);
        int x1 = (int) Math.round(space * 2 + buttonW * 1);
        int x2 = (int) Math.round(space * 3 + buttonW * 2);
        int x3 = (int) Math.round(space * 4 + buttonW * 3);
        int x4 = (int) Math.round(space * 5 + buttonW * 4);

        var focus_button = new SwitchWidget(x0, bottomY, buttonW, buttonH, (clickData, aBoolean) -> {
            MODE = 0;
            updateSpark();
        }).setSupplier(() -> { return MODE == 0; })
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_FOCUS_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_FOCUS_ON))
                .setHoverTooltips(spireModeLang[4].translate(spireModeLang[0].translate()));

        var spark_button = new SwitchWidget(x1, bottomY, buttonW, buttonH, (clickData, aBoolean) -> {
            MODE = 1;
            updateSpark();
        }).setSupplier(() -> { return MODE == 1; })
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_SPARK_SEND_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_SPARK_SEND_ON))
                .setHoverTooltips(spireModeLang[4].translate(spireModeLang[1].translate()));

        var global_button = new SwitchWidget(x2, bottomY, buttonW, buttonH, (clickData, aBoolean) -> {
            MODE = 2;
            updateSpark();
        }).setSupplier(() -> { return MODE == 2; })
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_GLOBAL_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_GLOBAL_ON))
                .setHoverTooltips(spireModeLang[4].translate(spireModeLang[2].translate()));

        var connect_button = new SwitchWidget(x3, bottomY, buttonW, buttonH, (clickData, aBoolean) -> {
            MODE = 3;
            updateSpark();
        }).setSupplier(() -> { return MODE == 3; })
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_CONNECT_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_CONNECT_ON))
                .setHoverTooltips(spireModeLang[4].translate(spireModeLang[3].translate()));

        var action_button = new SwitchWidget(x4, bottomY, buttonW, buttonH, (clickData, aBoolean) -> {
            isAnimationActive = aBoolean;
            updateSpark();
        })
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_ANIMATION_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_ANIMATION_ON))
                .setPressed(isAnimationActive)
                .setHoverTooltips(spireModeLang[5].translate());

        group.addWidgets(focus_button, global_button, connect_button, spark_button, action_button);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        // 等比 1.5x：与 createUIWidget 的 group 尺寸保持一致
        // 外层 UI 贴图/背景（FancyMachineUIWidget）应以原始 ModularUI 尺寸为基准
        int w = sc(198);
        int h = sc(208);
        return new ModularUI(w, h, this, entityPlayer).widget(new FancyMachineUIWidget(this, w, h));
    }

    public void updateSpark() {
        // 刷新火花数据
        if (this.Spark == null) return;
        this.Spark.range = range;
        this.Spark.speed = speed;
        this.Spark.mode = MODE;
        this.Spark.isAnimationActive = isAnimationActive;

        if (connectedSparkPos != null) this.Spark.connectedDeltaSparkPos = new AABB(connectedSparkPos);
        this.Spark.setNetwork(network);
        this.Spark.initSpark();
    }

    public void updateSelf() {
        this.range=base_range;
        this.maxMana=base_maxmana;
        this.speed=base_speed;
        this.effencicy=base_effencicy;
        for(int i=0;i<=3;i++)
        {
            if(!machineStorage.getStackInSlot(i).isEmpty() &&machineStorage.getStackInSlot(i).getItem() instanceof SpireUpgradeRuneItem ritem)
            {
                if(ritem.getRange()>1)this.range+=ritem.getRange()-1;
                if(ritem.getSpeed()>1)this.speed*=ritem.getSpeed();
                if(ritem.getCapacity()>1)this.maxMana*=ritem.getCapacity();
                if(ritem.getConversionEfficiency()>0.1)this.effencicy=Math.max(this.effencicy,ritem.getCapacity());

            }
        }
        this.range=Math.min(50,this.range);
        ((MysticSpireBlockEntity) this.holder).setMaxMana(this.maxMana);

    }

    @CN({
            "§4聚焦",
            "§c火花扩散",
            "§b广域扩散",
            "§6中转",
            "%d模式",
            "关闭火花传输动画"
    })
    @EN({
            "§4聚焦",
            "§c火花扩散",
            "§b广域扩散",
            "§6中转",
            "%d模式",
            "关闭火花传输动画"
    })
    public static Lang[] spireModeLang;
    @CN({
            "当前尖塔存储:%d/%d mana",
            "当前模式: %s",
            "传输速率:%d mana/t",
            "接受速率:%d mana/t",
            "覆盖范围:%d ",
            "已与尖塔网络链接,链接的火花坐标：(%d %d %d)"
    })
    @EN({
            "当前尖塔存储:%d/%d",
            "当前模式: %s",
            "传输速率:%d mana/t",
            "接受速率:%d mana/t",
            "覆盖范围:%d ",
            "已与尖塔网络链接,链接的火花坐标：(%d %d %d)"
    })
    public static Lang[] spireDataLang;

    @CN({
            "§o这将会是§b§o火花§r§o应用的一场革命",
            "奥法尖塔通过其生成的§b德尔塔火花§r来进行魔力传输，该火花自动绑定奥法尖塔本身",
            "奥法尖塔初始具有100W的魔力容量，五倍于普通火花的传输速度，5000/t的魔力吸取速度，吸取产魔花速度独立于吸收速度并且只有吸取速度的1/10",
            "该结构具有多种模式，请查阅植物魔法辞典来获取更多详细说明",
    })
    @EN({
            "§o这将会是§b火花§r应用的一场革命",
            "奥法尖塔通过其生成的§b德尔塔火花§r来进行魔力传输，该火花自动绑定奥法尖塔本身",
            "奥法尖塔初始具有100W的魔力容量，五倍于普通火花的传输速度，5000/t的魔力吸取速度，吸取产魔花速度独立于吸收速度并且只有吸取速度的1/10",
            "该结构具有多种模式，请查阅植物魔法辞典来获取更多详细说明",
    })
    public static Lang[] spireTooltipsLang;
}