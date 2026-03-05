package com.moguang.ctnhmana.common.entity;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

import com.moguang.ctnhmana.Mutiblock.MysticSpire;
import com.moguang.ctnhmana.api.networks.BotaniaEffectPacketExtend;
import com.moguang.ctnhmana.api.networks.BotaniaExtendEffectType;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import com.moguang.ctnhmana.item.equipment.SaberWandItem;
import lombok.Getter;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.item.SparkEntity;
import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.entity.SparkBaseEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.moguang.ctnhmana.item.equipment.SaberWandItem.saberWandBindingLang;
import static com.moguang.ctnhmana.item.equipment.SaberWandItem.updateSpireLang;

public class DeltaSpark extends SparkBaseEntity implements SparkEntity, ManaCollisionGhost {

    public int TRANSFER_RATE = 1000;
    private static final String TAG_UPGRADE = "upgrade";
    private static final EntityDataAccessor<Integer> MODE = SynchedEntityData.defineId(DeltaSpark.class,
            EntityDataSerializers.INT);

    @Persisted
    @Getter
    public int mode = 2;
    @Persisted
    public BlockPos AttachPos;
    public MysticSpire SpireMachine;
    public DeltaSpark connectedDeltaSpark;
    public boolean isInitChecked = false;

    @Persisted
    public AABB connectedDeltaSparkPos;

    public boolean isAnimationActive = true;
    public int range = 20;
    public int speed = 5000;
    public int receive_rate = 5000;
    public int TargetNum = 3;
    public int timer = 0;

    public List<ManaSpark> sparks;
    public List<ManaReceiver> receivers;
    public List<GeneratingFlowerBlockEntity> flowers = new ArrayList<>();
    public List<ManaPoolBlockEntity> pools;

    // public DeltaSpark(EntityType<?> type, Level world, BlockPos AttachPos) {
    // super(type, world);
    // this.AttachPos=AttachPos;
    // }
    public DeltaSpark(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public BlockPos getAttachPos() {
        return AttachPos;
    }

    @Override
    public void tick() {
        if (level().isClientSide && firstTick) {
            return;
        }

        if (!isInitChecked) {
            initSpark();
        } else {
            if (timer % 200 == 0) {
                initSpark();
                timer = 1;
            }
            timer++;
            if (AttachPos != null && level().getBlockEntity(AttachPos) != null &&
                    level().getBlockEntity(AttachPos) instanceof MysticSpireBlockEntity entity &&
                    entity.getMetaMachine() instanceof MysticSpire machine && machine.isFormed()) {
                this.SpireMachine = machine;
            } else {
                this.kill();
            }
            if (mode == 1 && !sparks.isEmpty()) sendManaToSpark();
            if (mode == 2 && !receivers.isEmpty()) sendManaToReceiver();
            if (mode == 0 && !receivers.isEmpty()) receiveManaFromSpark();
            if (mode == 0 && !flowers.isEmpty()) receiveManaFromFlower();
            if (connectedDeltaSpark != null) sendManaToDeltaNet();
        }

        // When loaded, initialize transfers
    }

    public void initSpark() {
        if (AttachPos != null && level().getBlockEntity(AttachPos) != null &&
                level().getBlockEntity(AttachPos) instanceof MysticSpireBlockEntity entity &&
                entity.getMetaMachine() instanceof MysticSpire machine && machine.isFormed()) {
            this.SpireMachine = machine;
            machine.Spark = this;
        } else {
            this.kill();
        }
        if (connectedDeltaSparkPos != null) {
            if (!level().getEntitiesOfClass(DeltaSpark.class, connectedDeltaSparkPos).isEmpty()) {
                connectedDeltaSpark = level().getEntitiesOfClass(DeltaSpark.class, connectedDeltaSparkPos).get(0);
            } else {
                connectedDeltaSparkPos = null;
            }
        }
        isInitChecked = true;
        receivers = scanChunkReceiver(level(), this.getOnPos(), range);
        sparks = getSparksAround(level(), this.getX(), this.getY(), this.getZ(), this.getNetwork());
    }

    public void sendManaToSpark() {
        var pool = (MysticSpireBlockEntity) SpireMachine.getHolder();
        int consume = 0;
        var num = 0;
        for (ManaSpark spark : sparks) {
            if (!spark.entity().isAlive() || spark.getAttachedManaReceiver() == null) continue;
            if (!spark.getAttachedManaReceiver().isFull()) num++;
        }
        if (num < 1 || pool.getCurrentMana() <= 0) return;
        if (pool.getCurrentMana() <= speed) {
            consume = pool.getCurrentMana();
            pool.BTMana = 0;
        } else {
            pool.BTMana -= speed;
            consume = speed;
        }

        consume = consume / num;
        for (ManaSpark spark : sparks) {
            if (!spark.entity().isAlive() || spark.getAttachedManaReceiver() == null) continue;
            if (!spark.getAttachedManaReceiver().isFull()) spark.getAttachedManaReceiver().receiveMana(consume);
            if (isAnimationActive) particlesTowards(spark.entity());

        }
    }

    public void sendManaToReceiver() {
        var pool = (MysticSpireBlockEntity) SpireMachine.getHolder();
        int consume = 0;
        var num = 0;
        for (ManaReceiver receiver : receivers) {
            if (!receiver.isFull() && !((BlockEntity) receiver).isRemoved()) num++;
        }
        if (num < 1 || pool.getCurrentMana() <= 0) return;
        if (pool.getCurrentMana() <= speed) {
            consume = pool.getCurrentMana();
            pool.BTMana = 0;
        } else {
            pool.BTMana -= speed;
            consume = speed;
        }
        consume = consume / num;
        for (ManaReceiver receiver : receivers) {
            if (!receiver.isFull() && !((BlockEntity) receiver).isRemoved()) {
                receiver.receiveMana(consume);
                if (isAnimationActive) particlesTowards((BlockEntity) receiver);
            }
        }
    }

    public void receiveManaFromSpark() {
        var pool = (MysticSpireBlockEntity) SpireMachine.getHolder();
        int consume = 0;
        var num = 0;
        consume = Math.min(pool.maxBTMana - pool.BTMana, receive_rate);
        for (ManaSpark spark : sparks) {
            if (!spark.entity().isAlive() || spark.getAttachedManaReceiver() == null) {
                continue;
            }
            if (spark.getAttachedManaReceiver().getCurrentMana() > 0) num++;
        }
        if (num < 1 || pool.isFull()) return;
        consume = consume / num;
        for (ManaSpark spark : sparks) {
            if (!spark.entity().isAlive() || spark.getAttachedManaReceiver() == null ||
                    spark.getAttachedManaReceiver().getCurrentMana() <= 0)
                continue;
            var mana = spark.getAttachedManaReceiver().getCurrentMana();
            if (mana < consume) {
                spark.getAttachedManaReceiver().receiveMana(-mana);
                pool.receiveMana(mana);
            } else {
                spark.getAttachedManaReceiver().receiveMana(-consume);
                pool.receiveMana(consume);
            }
            if (isAnimationActive) particlesTowardsReverse(spark.entity());
        }
    }

    public void receiveManaFromFlower() {
        var pool = (MysticSpireBlockEntity) SpireMachine.getHolder();
        int consume = 0;
        var num = 0;
        consume = Math.min(pool.maxBTMana - pool.BTMana, receive_rate);
        for (GeneratingFlowerBlockEntity flower : flowers) {
            if (!flower.isRemoved() && flower.getMana() > 0) num++;
        }
        if (num < 1 || pool.isFull()) return;
        consume = consume / num;
        for (GeneratingFlowerBlockEntity flower : flowers) {
            if (flower.isRemoved() || flower.getMana() <= 0) continue;
            var mana = flower.getMana();
            if (mana < consume) {
                flower.addMana(-mana);
                pool.receiveMana(mana);
            } else {
                flower.addMana(-consume);
                pool.receiveMana(consume);
            }
            if (isAnimationActive) particlesTowardsReverse((BlockEntity) flower);
        }
    }

    public void sendManaToDeltaNet() {
        if (connectedDeltaSpark == null) return;
        var pool = (MysticSpireBlockEntity) SpireMachine.getHolder();
        var target_pool = (MysticSpireBlockEntity) connectedDeltaSpark.SpireMachine.getHolder();
        int consume = 0;
        var num = 0;
        consume = Math.min(pool.BTMana, speed);
        consume = Math.min(consume, target_pool.maxBTMana - target_pool.BTMana);
        pool.receiveMana(-consume);
        target_pool.receiveMana(consume);
        if (isAnimationActive) particlesTowards((connectedDeltaSpark));
    }

    public List<ManaSpark> getSparksAround(Level world, double x, double y, double z, DyeColor color) {
        // 不管DeltaSpark，它有自己的一套逻辑，当然实际上也不是一个东西
        int r = range;
        Predicate<Entity> predicate = e -> e instanceof ManaSpark spark && spark.getNetwork() == color &&
                !(e instanceof DeltaSpark);
        @SuppressWarnings("unchecked")
        List<ManaSpark> entities = (List<ManaSpark>) (List<?>) world.getEntitiesOfClass(Entity.class,
                new AABB(x - r, y - r, z - r, x + r, y + r, z + r), predicate);
        return entities;
    }

    public List<ManaReceiver> scanChunkReceiver(Level level, BlockPos centerPos, int radius) {
        // 扫区块的Receiver
        List<ManaReceiver> result = new ArrayList<>();
        List<GeneratingFlowerBlockEntity> receive_result = new ArrayList<>();
        if (level == null || radius < 0) {
            return result;
        }

        int minX = centerPos.getX() - radius;
        int maxX = centerPos.getX() + radius;
        int minZ = centerPos.getZ() - radius;
        int maxZ = centerPos.getZ() + radius;
        for (int chunkX = minX >> 4; chunkX <= maxX >> 4; chunkX++) {
            for (int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; chunkZ++) {
                LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                if (chunk == null) {
                    continue;
                }
                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (be instanceof ManaReceiver && !(be instanceof MysticSpireBlockEntity)) {
                        BlockPos bePos = be.getBlockPos();
                        if (bePos.getY() >= centerPos.getY() - radius * 2 &&
                                bePos.getY() <= centerPos.getY() + radius * 2 &&
                                Math.abs(bePos.getX() - centerPos.getX()) <= radius &&
                                Math.abs(bePos.getZ() - centerPos.getZ()) <= radius) {
                            result.add((ManaReceiver) be);
                        }
                    }
                    if (be instanceof GeneratingFlowerBlockEntity) {
                        BlockPos bePos = be.getBlockPos();
                        if (bePos.getY() >= centerPos.getY() - radius * 2 &&
                                bePos.getY() <= centerPos.getY() + radius * 2 &&
                                Math.abs(bePos.getX() - centerPos.getX()) <= radius &&
                                Math.abs(bePos.getZ() - centerPos.getZ()) <= radius) {
                            receive_result.add((GeneratingFlowerBlockEntity) be);
                        }
                    }
                }
            }
        }
        flowers = receive_result;
        return result;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(MODE, mode);
    }

    public void setMode(int mode) {
        this.mode = mode;
        entityData.set(MODE, mode);
    }

    private void particlesTowards(Entity e) {
        XplatAbstractions.INSTANCE.sendToTracking(this,
                new BotaniaEffectPacket(EffectType.SPARK_MANA_FLOW, getX(), getY(), getZ(),
                        getId(), e.getId(), ColorHelper.getColorValue(getNetwork())));
    }

    private void particlesTowardsReverse(Entity e) {
        XplatAbstractions.INSTANCE.sendToTracking(e,
                new BotaniaEffectPacket(EffectType.SPARK_MANA_FLOW, getX(), getY(), getZ(),
                        e.getId(), getId(), ColorHelper.getColorValue(getNetwork())));
    }

    private void particlesTowardsReverse(BlockEntity e) {
        XplatAbstractions.INSTANCE.sendToTracking(this,
                new BotaniaEffectPacketExtend(BotaniaExtendEffectType.SPARK_MANA_FLOW_REVERSE, e.getBlockPos().getX(),
                        e.getBlockPos().getY(), e.getBlockPos().getZ(),
                        getId(), getId(), ColorHelper.getColorValue(getNetwork())));
    }

    private void particlesTowards(BlockEntity e) {
        XplatAbstractions.INSTANCE.sendToTracking(this,
                new BotaniaEffectPacketExtend(BotaniaExtendEffectType.SPARK_MANA_FLOW, e.getBlockPos().getX(),
                        e.getBlockPos().getY(), e.getBlockPos().getZ(),
                        getId(), getId(), ColorHelper.getColorValue(getNetwork())));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        var world = player.level();
        if (world.isClientSide) return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof SaberWandItem item && SaberWandItem.getBindMode(stack)) {
            if (player.isSecondaryUseActive() && this.connectedDeltaSpark != null) {
                if (!world.isClientSide) {
                    this.connectedDeltaSpark = null;
                }
                if (world.isClientSide) {
                    player.playSound(BotaniaSounds.ding, 0.11F, 1F);
                    player.displayClientMessage(saberWandBindingLang[0].translate(), true);
                }
                updateMachine();
                return InteractionResult.SUCCESS;
            } else {
                if (item.recordedSpark == null) {
                    if (!world.isClientSide) {
                        item.recordedSpark = this;
                        player.displayClientMessage(saberWandBindingLang[1].translate(), true);
                    }
                    updateMachine();
                    return InteractionResult.SUCCESS;
                } else {
                    if (item.recordedSpark != null && item.recordedSpark.isAlive() && !item.recordedSpark.isRemoved()) {
                        if (!world.isClientSide) {
                            this.connectedDeltaSpark = item.recordedSpark;
                            this.connectedDeltaSparkPos = item.recordedSpark.getBoundingBox();

                            item.recordedSpark = null;
                        }

                        if (world.isClientSide) {
                            player.playSound(BotaniaSounds.ding, 0.11F, 1F);
                            player.displayClientMessage(saberWandBindingLang[2].translate(), true);
                        }
                        updateMachine();
                        return InteractionResult.SUCCESS;
                    } else {
                        player.displayClientMessage(saberWandBindingLang[3].translate(), true);
                        return InteractionResult.FAIL;
                    }

                }
            }
        }
        if (stack.getItem() instanceof SaberWandItem item && !SaberWandItem.getBindMode(stack)) {
            if (!world.isClientSide) {
                this.connectedDeltaSpark = null;
            }
            if (this.mode < 3) {
                this.mode++;
                SpireMachine.MODE = this.mode;
            } else {
                this.mode = 0;
                SpireMachine.MODE = this.mode;
            }
            SpireMachine.updateSpark();
            player.displayClientMessage(updateSpireLang.translate(SpireMachine.mode_MAP.get(this.mode).translate()),
                    true);
            return InteractionResult.SUCCESS;
        } else if (stack.getItem() instanceof DyeItem dye) {
            DyeColor color = dye.getDyeColor();
            if (color != getNetwork()) {
                if (!level().isClientSide) {
                    setNetwork(color);
                    this.SpireMachine.network = this.getNetwork();
                }
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public void updateMachine() {
        // 刷新火花数据
        if (this.SpireMachine == null) return;
        this.SpireMachine.range = range;
        this.SpireMachine.speed = speed;
        this.SpireMachine.MODE = mode;
        if (connectedDeltaSpark != null)
            this.SpireMachine.connectedSparkPos = new BlockPos((int) connectedDeltaSpark.getX(),
                    (int) connectedDeltaSpark.getY(), (int) connectedDeltaSpark.getZ());
    }

    public record WandHud(DeltaSpark entity) implements WandHUD {

        @Override
        public void renderHUD(GuiGraphics gui, Minecraft mc) {
            ItemStack sparkStack = new ItemStack(BotaniaItems.spark);
            DyeColor networkColor = entity.getNetwork();
            Component networkColorName = Component.translatable("color.minecraft." + networkColor.getName())
                    .withStyle(ChatFormatting.ITALIC);
            Component mode_name = MysticSpire.spireModeLang[entity.getMode()].translate();
            int textColor = ColorHelper.getColorLegibleOnGrayBackground(networkColor);

            int width = 4 + Collections.max(Arrays.asList(
                    mc.font.width(networkColorName),
                    RenderHelper.itemWithNameWidth(mc, sparkStack)));
            int height = 50;
            int networkColorTextStart = mc.font.width(networkColorName) / 2;
            int modeTextStart = mc.font.width(mode_name) / 2;
            int centerX = mc.getWindow().getGuiScaledWidth() / 2;
            int centerY = mc.getWindow().getGuiScaledHeight() / 2;

            RenderHelper.renderHUDBox(gui, centerX - width / 2, centerY + 8, centerX + width / 2, centerY + 8 + height);

            RenderHelper.renderItemWithNameCentered(gui, mc, sparkStack, centerY + 10, textColor);
            // RenderHelper.renderItemWithNameCentered(gui, mc, augmentStack, centerY + 28, textColor);
            gui.drawString(mc.font, mode_name, centerX - modeTextStart, centerY + (28), textColor);
            gui.drawString(mc.font, networkColorName, centerX - networkColorTextStart, centerY + (46), textColor);
        }
    }
}
