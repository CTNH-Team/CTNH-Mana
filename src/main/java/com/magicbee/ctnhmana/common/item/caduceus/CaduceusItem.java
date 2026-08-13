package com.magicbee.ctnhmana.common.item.caduceus;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.tool.GTToolItem;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMMobEffects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.*;

import static com.magicbee.ctnhmana.registry.sounds.CMSoundEvent.INDEX_BEEP_EFFECT;

/**
 * 继承 {@link GTToolItem} 的多形态工具：根据 NBT {@link #NBT_TOOL_TYPE} 选择当前类型，
 * 在物品内直接调用该类型的 {@link GTToolType#toolDefinition}#getBehaviors()。
 */

public class CaduceusItem extends GTToolItem {

    /** NBT 键：当前工具类型，值为 {@link GTToolType#name}，如 "sword", "pickaxe", "wire_cutter", "wrench" */
    @DescSynced
    public static final String NBT_TOOL_TYPE = "GTMultiTool.Type";
    public static final List<GTToolType> CYCLE_TYPES = List.of(
            GTToolType.SWORD,
            GTToolType.PICKAXE,
            GTToolType.SHOVEL,
            GTToolType.AXE,
            GTToolType.HOE,
            GTToolType.SCYTHE,
            GTToolType.SAW,
            GTToolType.WRENCH,
            GTToolType.FILE,
            GTToolType.SCREWDRIVER,
            GTToolType.WIRE_CUTTER,
            GTToolType.KNIFE,
            GTToolType.PLUNGER);

    private static final GTToolType DEFAULT_TYPE = GTToolType.SWORD;

    public CaduceusItem(Properties properties) {
        super(
                DEFAULT_TYPE,
                GTMaterials.Neutronium.getToolTier(),
                GTMaterials.Neutronium,
                new MultiToolDefinition(CaduceusItem::getCurrentType, DEFAULT_TYPE),
                properties);
    }

    @Override
    public String getDescriptionId() {
        return "item." + CTNHMana.MODID + ".caduceus";
    }

    @Override
    public Component getDescription() {
        return Component.translatable(getDescriptionId());
    }

    public static void switchFortuna(ItemStack stack, Player player) {
        if (stack.getOrCreateTag().contains("karma") || !stack.getOrCreateTag().getBoolean("karma")) {
            MobEffectInstance karmaFortunaEffect = new MobEffectInstance(
                    CMMobEffects.KarmaFortuna.get(),
                    20 * 60 * 60,
                    0,
                    false,
                    true);
            player.addEffect(karmaFortunaEffect);
            player.sendSystemMessage(CaduceusFortunaLang.translate());
            stack.getOrCreateTag().putBoolean("karma", true);
        } else if (stack.getOrCreateTag().getBoolean("karma")) {
            stack.getOrCreateTag().putBoolean("karma", false);
        }
    }

    public void setKarma(Player player) {
        if (player.hasEffect(CMMobEffects.Karma.get())) {
            var karma = player.getEffect(CMMobEffects.Karma.get()).getAmplifier();
            MobEffectInstance karmaEffect = new MobEffectInstance(
                    CMMobEffects.Karma.get(),
                    20 * 10 * 60,
                    karma + 1,
                    false,
                    true);
            MobEffectInstance darkEffect = new MobEffectInstance(
                    MobEffects.DARKNESS,
                    20 * 7,
                    1,
                    false,
                    true);
            player.addEffect(karmaEffect);
            player.addEffect(darkEffect);
        } else {
            MobEffectInstance karmaEffect = new MobEffectInstance(
                    CMMobEffects.Karma.get(),
                    20 * 10 * 60,
                    0,
                    false,
                    true);
            MobEffectInstance darkEffect = new MobEffectInstance(
                    MobEffects.DARKNESS,
                    20 * 7,
                    1,
                    false,
                    true);
            player.addEffect(karmaEffect);
            player.addEffect(darkEffect);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.getGameTime() % (20 * 30) == 0 && entity instanceof Player player) {
            if (player.level().isClientSide()) {
                playIndexMusic(player);
                return;
            }
            var tag = player.getPersistentData();
            if (stack.getTag().contains("karma") && stack.getTag().getBoolean("karma")) {
                MobEffectInstance karmaFortunaEffect = new MobEffectInstance(
                        CMMobEffects.KarmaFortuna.get(),
                        20 * 60 * 60,
                        0,
                        false,
                        true);
                ((Player) entity).addEffect(karmaFortunaEffect);
                player.sendSystemMessage(CaduceusFortunaLang.translate());
                return;
            }
            // 先找tag有没有方块
            if (tag.contains("index_target_block")) {
                var posArray = tag.getIntArray("index_target_block");
                var targetBlockPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
                player.getPersistentData().remove("index_target_block");
                tag.remove("index_target");
                if (player.level().getBlockEntity(targetBlockPos) == null) {
                    // 完成了
                    if (!player.hasEffect(CMMobEffects.Bladeunleashed.get())) {
                        player.addEffect(new MobEffectInstance(
                                CMMobEffects.Bladeunleashed.get(),
                                20 * 30 * 60,
                                0,
                                true,
                                true));
                    } else {
                        var amplifier = player.getEffect(CMMobEffects.Bladeunleashed.get()).getAmplifier();
                        player.addEffect(new MobEffectInstance(
                                CMMobEffects.Bladeunleashed.get(),
                                20 * 30 * 60,
                                Math.min(3, amplifier + 1),
                                true,
                                true));
                    }
                } else setKarma(player); // 没完成，上业

                return;
            }
            if (tag.contains("index_target")) {
                // 没完成，上业
                setKarma(player);
                tag.remove("index_target");
                tag.remove("index_target_block");
            } else {
                getIndex(entity, stack);
            }
        }
    }

    public void getIndexOfBlock(Player player, ItemStack stack, Map<BlockPos, BlockEntity> bes) {
        int i = bes.size();
        for (BlockEntity be : bes.values()) {
            i--;
            if (be instanceof MetaMachineBlockEntity machine) {
                var chance = 0.2;
                if (i == 0) chance = 1;
                if (machine.getMetaMachine() instanceof WorkableMultiblockMachine wmachine && wmachine.isActive() &&
                        player.hasEffect(CMMobEffects.Karma.get())) {
                    chance += player.getEffect(CMMobEffects.Karma.get()).getAmplifier() * 0.1;
                }
                if (Math.random() <= chance) {
                    var tag = player.getPersistentData();
                    List<Integer> posList = new ArrayList<>();
                    posList.add(machine.getBlockPos().getX()); // 添加X坐标
                    posList.add(machine.getBlockPos().getY()); // 添加Y坐标
                    posList.add(machine.getBlockPos().getZ()); // 添加Z坐标
                    tag.putIntArray("index_target_block", posList);
                    player.sendSystemMessage(IndexLang[3].translate());
                    return;
                }
            }
        }
    }

    public void getIndex(Entity entity, ItemStack stack) {
        if (entity.level().isClientSide()) return;
        if (entity instanceof Player player) {
            var tag = player.getPersistentData();
            entity.sendSystemMessage(IndexLang[0].translate(entity.getName().getString()));
            if (player.hasEffect(CMMobEffects.Bladeunleashed.get()) &&
                    player.getEffect(CMMobEffects.Bladeunleashed.get()).getAmplifier() >= 3) {
                // 当剑刃解放达到IV时，不再下发新的指令，也不再切换武器
                entity.sendSystemMessage(IndexLang[1].translate());
                return;
            }
            // changeType(stack);
            LevelChunk chunk = player.level().getChunkAt(player.getOnPos());
            var blockTarget = chunk.getBlockEntities();
            Map<BlockPos, BlockEntity> TblockTarget = new HashMap<>();
            for (BlockPos be : blockTarget.keySet()) {
                if ((blockTarget.get(be) instanceof MetaMachineBlockEntity)) {
                    TblockTarget.put(be, blockTarget.get(be));
                }
            }
            if (Math.random() > 0.7 && !TblockTarget.isEmpty()) {
                getIndexOfBlock(player, stack, TblockTarget);
                return;
            }
            // 检查是否有可以破坏的指令目标方块 如果有，则有30%几率选择的是机器
            double r = 16.0D;
            AABB aabb = new AABB(
                    player.getX() - r, player.getY() - r, player.getZ() - r,
                    player.getX() + r, player.getY() + r, player.getZ() + r);
            var targets = player.level().getEntitiesOfClass(LivingEntity.class, aabb, b -> !b.equals(player)); // 选择除自己以外的目标

            if (targets.isEmpty()) // 指令找不到，高呼牛逼
            {
                if (!TblockTarget.isEmpty()) {
                    getIndexOfBlock(player, stack, TblockTarget);
                    return;
                }
                entity.sendSystemMessage(IndexLang[1].translate());
                return;
            } else // 标记其中一个
            {
                entity.sendSystemMessage(IndexLang[2].translate());
                var rand = targets.size() * Math.random();
                var target = targets.get((int) rand);
                tag.putString("index_target", target.getStringUUID());
                target.addEffect(new MobEffectInstance(
                        CMMobEffects.indextarget.get(),
                        20 * 30,
                        0,
                        true,
                        true));
            }
        }
    }

    public void changeType(ItemStack stack) {
        var rand = (int) (Math.random() * (CYCLE_TYPES.size() - 1));
        CaduceusItem.setCurrentType(stack, CYCLE_TYPES.get(rand));
    }

    @Nullable
    public static GTToolType getCurrentType(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(NBT_TOOL_TYPE, CompoundTag.TAG_STRING)) return null;
        return GTToolType.getTypes().get(tag.getString(NBT_TOOL_TYPE));
    }

    public static void setCurrentType(ItemStack stack, GTToolType type) {
        if (stack == null || stack.isEmpty() || type == null) return;
        stack.getOrCreateTag().putString(NBT_TOOL_TYPE, type.name);
        stack.getOrCreateTag().putFloat("caduceus_type_index", CYCLE_TYPES.indexOf(type));
    }

    @Override
    public Set<GTToolType> getToolClasses(ItemStack stack) {
        GTToolType type = getCurrentType(stack);
        return type != null ? Set.of(type) : Set.of(DEFAULT_TYPE);
    }

    @Override
    public Component getName(ItemStack stack) {
        GTToolType type = getCurrentType(stack);
        if (type != null) {
            return CaduceusLang.translate();
        }
        return super.getName(stack);
    }

    private static List<IToolBehavior> behaviorsFor(ItemStack stack) {
        GTToolType type = getCurrentType(stack);
        if (type == null) return List.of();
        return type.toolDefinition.getBehaviors();
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        for (IToolBehavior b : behaviorsFor(stack)) {
            InteractionResult r = b.onItemUseFirst(stack, context);
            if (r != InteractionResult.PASS) return r;
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        for (IToolBehavior b : behaviorsFor(stack)) {
            InteractionResult r = b.onItemUse(context);
            if (r != InteractionResult.PASS) return r;
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        for (IToolBehavior b : behaviorsFor(stack)) {
            InteractionResultHolder<ItemStack> r = b.onItemRightClick(level, player, usedHand);
            if (r.getResult() == InteractionResult.SUCCESS) return r;
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        for (IToolBehavior b : behaviorsFor(stack)) {
            b.hitEntity(stack, target, attacker);
        }
        ToolHelper.damageItem(stack, attacker, getToolStats().getToolDamagePerAttack(stack));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!level.isClientSide) {
            for (IToolBehavior b : behaviorsFor(stack)) {
                b.onBlockDestroyed(stack, level, state, pos, entityLiving);
            }
        }
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        for (IToolBehavior b : behaviorsFor(stack)) {
            b.onBlockStartBreak(stack, pos, player);
        }
        return super.onBlockStartBreak(stack, pos, player);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction action) {
        for (IToolBehavior b : behaviorsFor(stack)) {
            if (b.canPerformAction(stack, action)) return true;
        }
        return super.canPerformAction(stack, action);
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        for (IToolBehavior b : behaviorsFor(stack)) {
            if (b.canDisableShield(stack, shield, entity, attacker)) return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        // super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(CaduceusTooltipNormalLang[0].translate());
        tooltipComponents.add(getWeaponName(stack));
        tooltipComponents.add(CaduceusStatusLang[0].translate());
        tooltipComponents.add(CaduceusStatusLang[1].translate());
        tooltipComponents.add(CaduceusStatusLang[2].translate());
        tooltipComponents.add(CaduceusTooltipNormalLang[1].translate());
        tooltipComponents.add(CaduceusTooltipNormalLang[2].translate());
        tooltipComponents.add(CaduceusTooltipNormalLang[3].translate());
        tooltipComponents.add(CaduceusTooltipNormalLang[4].translate());
        tooltipComponents.add(CaduceusTooltipNormalLang[5].translate());
        tooltipComponents.add(CaduceusDamageLang[0].translate(getAttack(stack)));
        tooltipComponents.add(CaduceusDamageLang[1].translate(getAttackSpeed(stack)));
        if (stack.getOrCreateTag().contains("karma") && stack.getOrCreateTag().getBoolean("karma"))
            tooltipComponents.add(CaduceusFortunaLang.translate());
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return 100;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return true;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return definition$getDefaultAttributeModifiers(slot, stack);
    }

    public Multimap<Attribute, AttributeModifier> definition$getDefaultAttributeModifiers(EquipmentSlot equipmentSlot,
                                                                                          ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        multimap.remove(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                (double) getAttack(stack), AttributeModifier.Operation.ADDITION));
        new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier",
                Math.max(-3.9, (double) getAttackSpeed(stack)), AttributeModifier.Operation.ADDITION);
        if (equipmentSlot == EquipmentSlot.MAINHAND) {

            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID,
                    "Weapon modifier", (double) getAttack(stack), AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier",
                    Math.max(-3.9, (double) getAttackSpeed(stack)), AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }

    public double getAttack(ItemStack stack) {
        GTToolType type = getCurrentType(stack);
        if (type == null) return 12.0;
        if (type.equals(GTToolType.SWORD)) {
            return 20.0; // 剑
        } else if (type.equals(GTToolType.PICKAXE)) {
            return 7.0;  // 镐
        } else if (type.equals(GTToolType.SHOVEL)) {
            return 22.0;  // 铲
        } else if (type.equals(GTToolType.AXE)) {
            return 40; // 斧
        } else if (type.equals(GTToolType.HOE)) {
            return 4.0;  // 锄
        } else if (type.equals(GTToolType.SCYTHE)) {
            return 24;  // 镰刀
        } else if (type.equals(GTToolType.SAW)) {
            return 5.0;  // 锯子
        } else if (type.equals(GTToolType.WRENCH)) {
            return 10.0;  // 扳手
        } else if (type.equals(GTToolType.FILE)) {
            return 10.0;  // 锉刀
        } else if (type.equals(GTToolType.SCREWDRIVER)) {
            return 12;  // 螺丝刀
        } else if (type.equals(GTToolType.WIRE_CUTTER)) {
            return 22;  // 剪线钳
        } else if (type.equals(GTToolType.KNIFE)) {
            return 18.0;  // 刀
        } else if (type.equals(GTToolType.PLUNGER)) {
            return 1.0;  // 马桶搋子
        } else {
            return 12.0; // 兜底：默认返回剑的伤害
        }
    }

    public double getAttackSpeed(ItemStack stack) {
        GTToolType type = getCurrentType(stack);
        if (type == null) return 12.0;
        if (type.equals(GTToolType.SWORD)) {
            return 2.0; // 剑
        } else if (type.equals(GTToolType.PICKAXE)) {
            return 1.6;  // 镐
        } else if (type.equals(GTToolType.SHOVEL)) {
            return 1.2;  // 铲
        } else if (type.equals(GTToolType.AXE)) {
            return 1.0; // 斧
        } else if (type.equals(GTToolType.HOE)) {
            return 10.0;  // 锄
        } else if (type.equals(GTToolType.SCYTHE)) {
            return 1.2;  // 镰刀
        } else if (type.equals(GTToolType.SAW)) {
            return 2.0;  // 锯子
        } else if (type.equals(GTToolType.WRENCH)) {
            return 2.0;  // 扳手
        } else if (type.equals(GTToolType.FILE)) {
            return 2.0;  // 锉刀
        } else if (type.equals(GTToolType.SCREWDRIVER)) {
            return 0.8;  // 螺丝刀
        } else if (type.equals(GTToolType.WIRE_CUTTER)) {
            return 1.2;  // 剪线钳
        } else if (type.equals(GTToolType.KNIFE)) {
            return 4.0;  // 刀
        } else if (type.equals(GTToolType.PLUNGER)) {
            return 114514.0;  // 马桶搋子
        } else {
            return 2.0; // 兜底：默认返回剑的伤害
        }
    }

    public Component getWeaponName(ItemStack stack) {
        GTToolType type = getCurrentType(stack);
        if (type == null) return CaduceusWeaponLang[0].translate();
        if (type.equals(GTToolType.SWORD)) {
            return CaduceusWeaponLang[0].translate(); // 剑 - 下标0
        } else if (type.equals(GTToolType.PICKAXE)) {
            return CaduceusWeaponLang[1].translate();  // 镐 - 下标1
        } else if (type.equals(GTToolType.SHOVEL)) {
            return CaduceusWeaponLang[2].translate();  // 铲 - 下标2
        } else if (type.equals(GTToolType.AXE)) {
            return CaduceusWeaponLang[3].translate(); // 斧 - 下标3
        } else if (type.equals(GTToolType.HOE)) {
            return CaduceusWeaponLang[4].translate();  // 锄 - 下标4
        } else if (type.equals(GTToolType.SCYTHE)) {
            return CaduceusWeaponLang[5].translate();  // 镰刀 - 下标5
        } else if (type.equals(GTToolType.SAW)) {
            return CaduceusWeaponLang[6].translate();  // 锯子 - 下标6
        } else if (type.equals(GTToolType.WRENCH)) {
            return CaduceusWeaponLang[7].translate();  // 扳手 - 下标7
        } else if (type.equals(GTToolType.FILE)) {
            return CaduceusWeaponLang[8].translate();  // 锉刀 - 下标8
        } else if (type.equals(GTToolType.SCREWDRIVER)) {
            return CaduceusWeaponLang[9].translate();  // 螺丝刀 - 下标9
        } else if (type.equals(GTToolType.WIRE_CUTTER)) {
            return CaduceusWeaponLang[10].translate();  // 剪线钳 - 下标10
        } else if (type.equals(GTToolType.KNIFE)) {
            return CaduceusWeaponLang[11].translate();  // 刀 - 下标11
        } else if (type.equals(GTToolType.PLUNGER)) {
            return CaduceusWeaponLang[12].translate();  // 马桶搋子 - 下标12
        } else {
            return CaduceusWeaponLang[0].translate(); // 兜底：默认返回剑的名称
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void playIndexMusic(Player player) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != player) return;
        SoundInstance Gazing = new INDEXMUSIC(player);

        SoundManager soundManager = mc.getSoundManager();
        // 检查音乐是否正在播放
        boolean isPlaying = soundManager.isActive(Gazing);
        if (!isPlaying) {
            mc.getSoundManager().play(Gazing);
        }
    }

    private static class INDEXMUSIC extends AbstractTickableSoundInstance {

        Player player = null;

        private INDEXMUSIC(Player player) {
            super(INDEX_BEEP_EFFECT.get(), SoundSource.MUSIC, SoundInstance.createUnseededRandom());
            this.player = player;
            this.looping = false;
        }

        public void tick() {}
    }

    @CN("神谕终端[赫尔墨斯]")
    @EN("Oracle Terminal [Hermes]")
    public static Lang CaduceusLang;
    @CN({
            "应该用大剑将躯干劈开时:",
            "应该用矿镐将骨骼凿碎时:",
            "应该用铁铲将皮肉铲削时:",
            "应该用战斧将肢体斩断时:",
            "应该用耕锄将筋脉割裂时:",
            "应该用镰刀将脖颈勾断时:",
            "应该用锯子将骨肉锯开时:",
            "应该用扳手将关节拧折时:",
            "应该用锉刀将皮肉磨烂时:",
            "应该用螺丝刀将颅骨撬开时:",
            "应该用剪线钳将筋腱剪断时:",
            "应该用小刀将动脉划开时:",
            "应该用马桶搋子将面门砸瘪时:"
    })
    @EN({
            "应该用大剑将躯干劈开时",
            "应该用矿镐将骨骼凿碎时",
            "应该用铁铲将皮肉铲削时",
            "应该用战斧将肢体斩断时",
            "应该用耕锄将筋脉割裂时",
            "应该用镰刀将脖颈勾断时",
            "应该用锯子将骨肉锯开时",
            "应该用扳手将关节拧折时",
            "应该用锉刀将皮肉磨烂时",
            "应该用螺丝刀将颅骨撬开时",
            "应该用剪线钳将筋腱剪断时",
            "应该用小刀将动脉划开时",
            "应该用马桶搋子将面门砸瘪时"
    })
    public static Lang[] CaduceusWeaponLang;
    @CN({
            "由通过电波传达指令的都市之神赫尔墨斯所赐的最强的武器，同时也是诅咒",
            "终端内部有着一种类似金属的液体，使用与奇点相同的技术，可以改变其形状与质量。",
            "这种液体会随着终端所吸收到的指令改变其外观，根据对象的情况与限制而变换成合适的武器。",
            "参与合成时被视为§6所有§r工具，可以破坏§6所有§r常规方块",
            "按N键来打开形态滚轮，切换神谕之杖的模式，按C激活福尔图娜模式：停止发放指令，获得负面效果业：福尔图娜",
            "这个物品还在测试状态！如果有任何bug，请反馈给魔力蜜蜂！"
    })
    @EN({
            "由通过电波传达指令的都市之神赫尔墨斯所赐的最强的武器，同时也是诅咒",
            "终端内部有着一种类似金属的液体，使用与奇点相同的技术，可以改变其形状与质量。",
            "这种液体会随着终端所吸收到的指令改变其外观，根据暗杀对象的情况与限制而变换成合适的武器。",
            "参与合成时被视为§6所有§r工具，可以破坏§6所有§r常规方块",
            "按V键来打开形态滚轮，切换神谕之杖的模式，按C激活福尔图娜模式：停止发放指令，获得负面效果业：福尔图娜",
            "这个物品还在测试状态！如果有任何bug，请反馈给魔力蜜蜂！"
    })
    public static Lang[] CaduceusTooltipNormalLang;
    @CN({
            "+§8%d§r攻击力",
            "+§8%d§r攻击速度"
    })
    @EN({
            "+%d攻击力",
            "+%d攻击速度"
    })
    public static Lang[] CaduceusDamageLang;
    @CN({
            "根据自身§1§n剑刃解放§r层数造成更多伤害",
            "对§1§n指令目标§r造成更多伤害",
            "命中时获得§4力量§r，根据自身§1§n剑刃解放§r层数或者命中§1§n指令目标§1§r获得额外§4力量§r",
            ""
    })
    @EN({
            "根据自身剑刃解放层数造成更多伤害",
            "对指令目标造成更多伤害",
            "命中时获得力量，根据自身剑刃解放层数或者命中指令目标获得额外力量"
    })
    public static Lang[] CaduceusStatusLang;

    @CN({
            "致%s：",
            "等待直至收到下一条指令",
            "在限定时间内消灭指令对象",
            "在限定时间内，确保指令对象位置没有机器"
    })
    @EN({
            "致%s：",
            "等待直至收到下一条指令",
            "在限定时间内前消灭指令对象",
            "在限定时间内，确保指令对象位置没有机器"
    })
    public static Lang[] IndexLang;
    @CN("§1§o哦，命运，如月亮般变化无常")
    @EN("§1§oO Fortune, inconstant as the moon")
    public static Lang CaduceusFortunaLang;
}
