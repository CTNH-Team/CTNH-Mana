package com.magicbee.ctnhmana.common.entity;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

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

import com.magicbee.ctnhmana.api.machine.trait.BTManaContainerTrait;
import com.magicbee.ctnhmana.api.networks.BotaniaEffectPacketExtend;
import com.magicbee.ctnhmana.api.networks.BotaniaExtendEffectType;
import com.magicbee.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import com.magicbee.ctnhmana.common.item.equipment.SaberWandItem;
import com.magicbee.ctnhmana.common.multiblock.MysticSpire;
import com.magicbee.ctnhmana.common.multiblock.SpireBigMath;
import lombok.Getter;
import mythicbotany.functionalflora.WitherAconite;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.item.SparkEntity;
import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.SparkBaseEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;

import static com.magicbee.ctnhmana.common.item.equipment.SaberWandItem.saberWandBindingLang;
import static com.magicbee.ctnhmana.common.item.equipment.SaberWandItem.updateSpireLang;

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
    public double effencicy = 0.1;
    public int range = 20;
    public int speed = 5000;
    public int receive_rate = 5000;
    public int TargetNum = 3;
    public int timer = 0;

    public List<ManaSpark> sparks;
    public List<ManaReceiver> receivers;
    public List<GeneratingFlowerBlockEntity> flowers = new ArrayList<>();

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
                entity.syncMysticManaCacheFromTrue();
            } else {
                this.kill();
            }
            if (mode == 1) {
                if (!sparks.isEmpty()) sendManaToSpark();
                if (!receivers.isEmpty()) sendManaToHatchReceiver();
            }
            // 凝聚扩散：仅向魔力凝聚仓广播
            if (mode == 2 && !receivers.isEmpty()) sendManaToHatchReceiver();
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
        int remaining = pool.mysticOutboundTickCap(speed);
        distributeOutboundToSparksEvenly(pool, remaining);
    }

    public void sendManaToHatchReceiver() {
        var pool = (MysticSpireBlockEntity) SpireMachine.getHolder();
        int remaining = pool.mysticOutboundTickCap(speed);
        distributeOutboundToReceiversEvenly(pool, remaining, receiver -> !receiver.isFull() &&
                !((BlockEntity) receiver).isRemoved() && receiver instanceof BTManaContainerTrait);
    }

    /** 本 tick 预算在多个火花目标间均分；单目标满则跳过，余量下一轮再分 */
    private void distributeOutboundToSparksEvenly(MysticSpireBlockEntity pool, int remaining) {
        if (remaining <= 0) return;
        while (remaining > 0) {
            List<ManaSpark> active = new ArrayList<>();
            for (ManaSpark spark : sparks) {
                if (!spark.entity().isAlive()) continue;
                var receiver = spark.getAttachedManaReceiver();
                if (receiver == null || receiver.isFull()) continue;
                active.add(spark);
            }
            if (active.isEmpty()) break;

            int share = remaining / active.size();
            boolean progress = false;
            for (ManaSpark spark : active) {
                if (remaining <= 0) break;
                var receiver = spark.getAttachedManaReceiver();
                if (receiver == null) continue;
                int attempt = share == 0 ? 1 : Math.min(share, remaining);
                int sent = tryTransferMana(receiver, attempt);
                if (sent <= 0) continue;
                pool.mysticDrainMana(sent);
                remaining -= sent;
                if (isAnimationActive) particlesTowards(spark.entity());
                progress = true;
            }
            if (!progress) break;
        }
    }

    /** 本 tick 预算在多个 ManaReceiver 间均分；单目标满则跳过，余量下一轮再分 */
    private void distributeOutboundToReceiversEvenly(MysticSpireBlockEntity pool, int remaining,
                                                     Predicate<ManaReceiver> eligible) {
        if (remaining <= 0) return;
        while (remaining > 0) {
            List<ManaReceiver> active = new ArrayList<>();
            for (ManaReceiver receiver : receivers) {
                if (eligible.test(receiver)) active.add(receiver);
            }
            if (active.isEmpty()) break;

            int share = remaining / active.size();
            boolean progress = false;
            for (ManaReceiver receiver : active) {
                if (remaining <= 0) break;
                int attempt = share == 0 ? 1 : Math.min(share, remaining);
                int sent = tryTransferMana(receiver, attempt);
                if (sent <= 0) continue;
                pool.mysticDrainMana(sent);
                remaining -= sent;
                if (isAnimationActive) particlesTowards(receiver.getManaReceiverPos());
                progress = true;
            }
            if (!progress) break;
        }
    }

    public void receiveManaFromSpark() {
        var pool = (MysticSpireBlockEntity) SpireMachine.getHolder();
        if (pool.isFull()) return;
        int remaining = pool.mysticInboundTickBudget(speed);
        distributeInboundFromSparksEvenly(pool, remaining);
    }

    public void receiveManaFromFlower() {
        var pool = (MysticSpireBlockEntity) SpireMachine.getHolder();
        if (pool.isFull()) return;
        long flowerLim = (long) (speed * effencicy);
        if (flowerLim > Integer.MAX_VALUE) flowerLim = Integer.MAX_VALUE;
        if (flowerLim < 1) flowerLim = 1;
        int remaining = pool.mysticInboundTickBudget((int) flowerLim);
        distributeInboundFromFlowersEvenly(pool, remaining);
    }

    /** 本 tick 吸收预算在多个火花来源间均分；尖塔满或来源空则跳过，余量下一轮再分 */
    private void distributeInboundFromSparksEvenly(MysticSpireBlockEntity pool, int remaining) {
        if (remaining <= 0) return;
        while (remaining > 0 && !pool.isFull()) {
            List<ManaSpark> active = new ArrayList<>();
            for (ManaSpark spark : sparks) {
                if (!spark.entity().isAlive()) continue;
                var src = spark.getAttachedManaReceiver();
                if (src == null || src.getCurrentMana() <= 0) continue;
                active.add(spark);
            }
            if (active.isEmpty()) break;

            int share = remaining / active.size();
            boolean progress = false;
            for (ManaSpark spark : active) {
                if (remaining <= 0 || pool.isFull()) break;
                var src = spark.getAttachedManaReceiver();
                if (src == null) continue;
                int avail = src.getCurrentMana();
                if (avail <= 0) continue;
                int attempt = share == 0 ? 1 : Math.min(share, Math.min(remaining, avail));
                int accepted = tryReceiveMana(pool, attempt);
                if (accepted <= 0) continue;
                src.receiveMana(-accepted);
                remaining -= accepted;
                if (isAnimationActive) particlesTowardsReverse(spark.entity());
                progress = true;
            }
            if (!progress) break;
        }
    }

    /** 本 tick 吸收预算在多个产魔花间均分；尖塔满或花空则跳过，余量下一轮再分 */
    private void distributeInboundFromFlowersEvenly(MysticSpireBlockEntity pool, int remaining) {
        if (remaining <= 0) return;
        while (remaining > 0 && !pool.isFull()) {
            List<GeneratingFlowerBlockEntity> active = new ArrayList<>();
            for (GeneratingFlowerBlockEntity flower : flowers) {
                if (flower.isRemoved() || flower.getMana() <= 0) continue;
                active.add(flower);
            }
            if (active.isEmpty()) break;

            int share = remaining / active.size();
            boolean progress = false;
            for (GeneratingFlowerBlockEntity flower : active) {
                if (remaining <= 0 || pool.isFull()) break;
                int avail = flower.getMana();
                if (avail <= 0) continue;
                int attempt = share == 0 ? 1 : Math.min(share, Math.min(remaining, avail));
                int accepted = tryReceiveMana(pool, attempt);
                if (accepted <= 0) continue;
                flower.addMana(-accepted);
                remaining -= accepted;
                if (isAnimationActive) particlesTowardsReverse((BlockEntity) flower);
                progress = true;
            }
            if (!progress) break;
        }
    }

    public void sendManaToDeltaNet() {
        if (connectedDeltaSpark == null || connectedDeltaSpark.SpireMachine == null) return;
        var pool = (MysticSpireBlockEntity) SpireMachine.getHolder();
        var target_pool = (MysticSpireBlockEntity) connectedDeltaSpark.SpireMachine.getHolder();
        int consume = Math.min(pool.mysticOutboundTickCap(speed),
                target_pool.mysticInboundTickBudget(Integer.MAX_VALUE));
        if (consume <= 0) return;
        pool.receiveMana(-consume);
        target_pool.receiveMana(consume);
        if (isAnimationActive) particlesTowards((connectedDeltaSpark));
    }

    private static int tryTransferMana(ManaReceiver receiver, int attempt) {
        if (attempt <= 0 || receiver.isFull()) {
            return 0;
        }
        int before = receiver.getCurrentMana();
        receiver.receiveMana(attempt);
        return receiver.getCurrentMana() - before;
    }

    /** 先尝试注入尖塔，返回真实进入量（基于 BigInteger 储量，避免 int 窗口计量失真） */
    private static int tryReceiveMana(MysticSpireBlockEntity pool, int attempt) {
        if (attempt <= 0 || pool.isFull()) {
            return 0;
        }
        BigInteger before = pool.getTrueManaBig();
        pool.receiveMana(attempt);
        BigInteger accepted = pool.getTrueManaBig().subtract(before);
        if (accepted.signum() <= 0) {
            return 0;
        }
        return SpireBigMath.clampToIntNonNegative(accepted);
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
                    if (be instanceof IMachineBlockEntity machineHolder) {
                        var trait = machineHolder.getMetaMachine().getTrait(BTManaContainerTrait.class);
                        if (trait != null) {
                            BlockPos bePos = be.getBlockPos();
                            if (bePos.getY() >= centerPos.getY() - radius * 2 &&
                                    bePos.getY() <= centerPos.getY() + radius * 2 &&
                                    Math.abs(bePos.getX() - centerPos.getX()) <= radius &&
                                    Math.abs(bePos.getZ() - centerPos.getZ()) <= radius) {
                                result.add(trait);
                            }
                        }
                    }
                    if (be instanceof GeneratingFlowerBlockEntity || be instanceof WitherAconite) {
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
        particlesTowards(e.getBlockPos());
    }

    private void particlesTowards(BlockPos pos) {
        XplatAbstractions.INSTANCE.sendToTracking(this,
                new BotaniaEffectPacketExtend(BotaniaExtendEffectType.SPARK_MANA_FLOW, pos.getX(),
                        pos.getY(), pos.getZ(),
                        getId(), getId(), ColorHelper.getColorValue(getNetwork())));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        var world = player.level();
        if (world.isClientSide) return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof SaberWandItem item && SaberWandItem.getBindMode(stack)) {
            if (this.connectedDeltaSpark != null && player.isShiftKeyDown()) {
                if (!world.isClientSide) {
                    this.connectedDeltaSpark = null;
                    this.connectedDeltaSparkPos = null;
                    this.SpireMachine.connectedSparkPos = null;
                    this.SpireMachine.ConnectedSpark = null;
                    updateMachine();
                    player.displayClientMessage(saberWandBindingLang[0].translate(), true);
                }
                if (world.isClientSide) {
                    player.playSound(BotaniaSounds.ding, 0.11F, 1F);

                }
                updateMachine();
                return InteractionResult.SUCCESS;
            } else {
                BlockPos bindingSparkPos = SaberWandItem.hasBindingSpire(stack) ? SaberWandItem.getBindingSpire(stack) :
                        null;
                DeltaSpark spark = null;
                if (bindingSparkPos != null) {
                    BlockPos finalBinding = bindingSparkPos;
                    // 绑定记录的是火花实体位置，因此按实体位置反查，不再依赖 AttachPos（控制器坐标）
                    AABB search = new AABB(finalBinding).inflate(2);
                    List<DeltaSpark> sparks = level().getEntitiesOfClass(DeltaSpark.class, search,
                            e -> e != this && e.isAlive() && !e.isRemoved());
                    if (!sparks.isEmpty()) {
                        spark = sparks.stream()
                                .min(Comparator.comparingDouble(
                                        a -> a.distanceToSqr(finalBinding.getX() + 0.5, finalBinding.getY() + 0.5,
                                                finalBinding.getZ() + 0.5)))
                                .orElse(null);
                    }
                }
                if (spark == null) {
                    if (!world.isClientSide) {
                        BlockPos record = this.blockPosition();
                        SaberWandItem.setBindingSpire(stack, record);
                        player.displayClientMessage(saberWandBindingLang[1].translate(), true);
                    }
                    updateMachine();
                    return InteractionResult.SUCCESS;
                } else {
                    if (spark != null && spark.isAlive() && !spark.isRemoved()) {
                        if (!world.isClientSide) {
                            this.connectedDeltaSpark = spark;
                            this.connectedDeltaSparkPos = spark.getBoundingBox();
                            SaberWandItem.clearBinding(stack);
                            player.displayClientMessage(saberWandBindingLang[2].translate(), true);
                        }

                        if (world.isClientSide) {
                            player.playSound(BotaniaSounds.ding, 0.11F, 1F);

                        }
                        updateMachine();
                        return InteractionResult.SUCCESS;
                    } else {
                        player.displayClientMessage(saberWandBindingLang[3].translate(), true);
                        SaberWandItem.clearBinding(stack);
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
        this.SpireMachine.getOrCreatedSpark();
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
