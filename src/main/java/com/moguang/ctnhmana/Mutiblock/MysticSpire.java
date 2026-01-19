package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import com.moguang.ctnhmana.common.entity.DeltaSpark;
import com.moguang.ctnhmana.registry.CMEntities;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.AABB;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.List;
import java.util.Map;

public class MysticSpire extends WorkableMultiblockMachine implements IFancyUIMachine,
        IDisplayUIMachine {
    public MysticSpire(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MysticSpire.class,
            WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    @Persisted
    public BlockPos connectedSparkPos;
    public DeltaSpark Spark;

    @Persisted
    public int MODE=2;

    public DeltaSpark ConnectedSpark;
    @Persisted
    public int range=20;
    @Persisted
    public int speed=5000;
    @Persisted
    public int TargetNum=3;
    @Persisted
    public int maxMana=10000;
    @Persisted
    public BlockPos sparkpos;
    @Persisted
    public boolean isAnimationActive=true;
    @Persisted
    public int receive_rate=5000;
    @Persisted
    public DyeColor network=DyeColor.WHITE;
    public final Map<Integer, Lang> mode_MAP = Map.of(
            0,spireModeLang[0],
            1,spireModeLang[1],
            2,spireModeLang[2],
            3,spireModeLang[3]
    );


    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.sparkpos=new BlockPos(this.getPos().getX(),this.getPos().getY()+7,this.getPos().getZ());
        ((MysticSpireBlockEntity) this.holder).setMaxMana(1000000);
        ((MysticSpireBlockEntity) this.holder).receiveMana(100000);
        if(connectedSparkPos!=null&&!this.getLevel().getEntitiesOfClass(DeltaSpark.class,new AABB(connectedSparkPos)).isEmpty())
        {
            this.ConnectedSpark=this.getLevel().getEntitiesOfClass(DeltaSpark.class,new AABB(connectedSparkPos)).get(0);
        }

        getOrCreatedSpark();
    }
    @Override
    public void onStructureInvalid()
    {
        super.onStructureInvalid();
        //虽然德尔塔火花在检测到结构失效后会自行消灭，但考虑到持久化的因素，双向记录数据和摧毁是保险的，这绝对不是什么PTSD
        if(Spark!=null)
        {
            this.Spark.kill();
            this.Spark=null; //这个过程只消灭绑定的火花本身，而保留其他数据
        }

    }
    public void getOrCreatedSpark()
    {
        if(this.getLevel().isClientSide)return;
        AABB locate=new AABB(sparkpos);
        if(!this.getLevel().getEntitiesOfClass(DeltaSpark.class,locate).isEmpty())
        {
            this.Spark=this.getLevel().getEntitiesOfClass(DeltaSpark.class,locate).get(0);
            updateSpark();
        }
        else
        {
            var entity= CMEntities.DELTA_SPARK.create(getLevel());
            entity.AttachPos=this.getPos();
            entity.range=this.range;
            entity.speed=this.speed;
            entity.TargetNum=this.TargetNum;
            entity.mode=MODE;
            entity.setNetwork(network);
            entity.setPos(sparkpos.getX(),sparkpos.getY(),sparkpos.getZ());
            if(connectedSparkPos!=null&&!this.getLevel().getEntitiesOfClass(DeltaSpark.class,new AABB(connectedSparkPos)).isEmpty())
            {
                entity.connectedDeltaSpark =ConnectedSpark;
                entity.connectedDeltaSparkPos =new AABB(ConnectedSpark.getOnPos());
            }
            getLevel().addFreshEntity(entity);
        }
    }
    @Override
    public void addDisplayText(List<Component> textList) {

        if(isFormed())
        {
            var pool=((MysticSpireBlockEntity) this.holder);
            textList.add(spireDataLang[0].translate(pool.BTMana,pool.maxBTMana));
            textList.add(spireDataLang[1].translate(MODE));
            textList.add(spireDataLang[2].translate(speed));
            textList.add(spireDataLang[3].translate(receive_rate));
            textList.add(spireDataLang[4].translate(range));
            if(this.connectedSparkPos!=null)
            {
                textList.add(spireDataLang[5].translate(connectedSparkPos.getX(),connectedSparkPos.getY(),connectedSparkPos.getZ()));
            }
        }
    }
    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8+20, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182+20, 117).setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                        .textSupplier(this.getLevel().isClientSide ? null : this::addDisplayText)
                        .setMaxWidthLimit(200)
                        .clickHandler(this::handleDisplayClick)));
        var focus_button=new SwitchWidget(15,100,20,20,(clickData, aBoolean) ->
        {
            MODE=0;
            updateSpark();
        }).setSupplier(()->{return MODE==0;})

                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_FOCUS_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_FOCUS_ON))
                .setHoverTooltips(spireModeLang[4].translate(spireModeLang[0].translate()));

        var spark_button=new SwitchWidget(55,100,20,20,(clickData, aBoolean) ->
        {
            MODE=1;
            updateSpark();
        }).setSupplier(()->{return MODE==1;})
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_SPARK_SEND_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_SPARK_SEND_ON))
                .setHoverTooltips(spireModeLang[4].translate(spireModeLang[1].translate()));
        var global_button=new SwitchWidget(95,100,20,20,(clickData, aBoolean) ->
        {
            MODE=2;
            updateSpark();
        }).setSupplier(()->{return MODE==2;})
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_GLOBAL_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_GLOBAL_ON))
                .setHoverTooltips(spireModeLang[4].translate(spireModeLang[2].translate()));
        var connect_button=new SwitchWidget(135,100,20,20,(clickData, aBoolean) ->
        {
            MODE=3;
            updateSpark();
        }).setSupplier(()->{return MODE==3;})
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_CONNECT_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_CONNECT_ON))
                .setHoverTooltips(spireModeLang[4].translate(spireModeLang[3].translate()));
        var action_button=new SwitchWidget(175,100,20,20,(clickData, aBoolean) ->
        {
            isAnimationActive=true;
            updateSpark();
        })
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.SPIRE_ANIMATION_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.SPIRE_ANIMATION_ON))
                .setPressed(isAnimationActive)
                .setHoverTooltips(spireModeLang[5].translate());

        group.addWidgets(focus_button,global_button,connect_button,spark_button,action_button);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(198, 208, this, entityPlayer).widget(new FancyMachineUIWidget(this, 198, 208));
    }
    public void updateSpark()
    {
        //刷新火花数据
        if(this.Spark==null)return;
        this.Spark.range=range;
        this.Spark.speed=speed;
        this.Spark.mode=MODE;
        this.Spark.isAnimationActive=isAnimationActive;

        if(connectedSparkPos!=null) this.Spark.connectedDeltaSparkPos =new AABB(connectedSparkPos);
        this.Spark.setNetwork(network);
        this.Spark.initSpark();
    }

    public void updateSelf()
    {

    }
    @CN(
            {
                    "§4聚焦",
                    "§c火花扩散",
                    "§b广域扩散",
                    "§6中转",
                    "%d模式",
                    "关闭火花传输动画"
            }
    )
    @EN(
            {
                    "§4聚焦",
                    "§c火花扩散",
                    "§b广域扩散",
                    "§6中转",
                    "%d模式",
                    "关闭火花传输动画"
            }
    )
    public static Lang[] spireModeLang;
    @CN(
            {
                    "当前尖塔存储:%d/%d mana",
                    "当前模式: %s",
                    "传输速率:%d mana/t",
                    "接受速率:%d mana/t",
                    "覆盖范围:%d ",
                    "已与尖塔网络链接,链接的火花坐标：(%d %d %d)"
            }
    )
    @EN(
            {
                    "当前尖塔存储:%d/%d",
                    "当前模式: %s",
                    "传输速率:%d mana/t",
                    "接受速率:%d mana/t",
                    "覆盖范围:%d ",
                    "已与尖塔网络链接,链接的火花坐标：(%d %d %d)"
            }
    )
    public static Lang[] spireDataLang;

    @CN(
            {
                    "§o这将会是§b火花§r应用的一场革命",
                    "奥法尖塔通过其生成的§b德尔塔火花§r来进行魔力传输，该火花自动绑定奥法尖塔本身",
                    "奥法尖塔初始具有100W的魔力容量，五倍于普通火花的传输速度，5000/t的魔力吸取速度，吸取产魔花速度独立于吸收速度并且只有吸取速度的1/10",
                    "该结构具有多种模式，请查阅植物魔法辞典来获取更多详细说明",
            }
    )
    @EN(
            {
                    "§o这将会是§b火花§r应用的一场革命",
                    "奥法尖塔通过其生成的§b德尔塔火花§r来进行魔力传输，该火花自动绑定奥法尖塔本身",
                    "奥法尖塔初始具有100W的魔力容量，五倍于普通火花的传输速度，5000/t的魔力吸取速度，吸取产魔花速度独立于吸收速度并且只有吸取速度的1/10",
                    "该结构具有多种模式，请查阅植物魔法辞典来获取更多详细说明",
            }
    )
    public static Lang[] spireTooltipsLang;

}
