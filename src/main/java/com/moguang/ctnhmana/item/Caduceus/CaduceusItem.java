package com.moguang.ctnhmana.item.Caduceus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

import com.gregtechceu.gtceu.api.item.tool.GTToolItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.List;
import java.util.Set;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.*;

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
            GTToolType.PLUNGER
    );

    private static final GTToolType DEFAULT_TYPE = GTToolType.SWORD;

    public CaduceusItem(Properties properties) {
        super(
                DEFAULT_TYPE,
                GTMaterials.Neutronium.getToolTier(),
                GTMaterials.Neutronium,
                new MultiToolDefinition(CaduceusItem::getCurrentType, DEFAULT_TYPE),
                properties
        );

    }
    public void changeType(ItemStack stack)
    {
        var rand=(int)(Math.random()*(CYCLE_TYPES.size()-1));
        CaduceusItem.setCurrentType(stack,CYCLE_TYPES.get(rand));
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
//        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(CaduceusTooltipNormalLang[0].translate());
        tooltipComponents.add(getWeaponName(stack));
        tooltipComponents.add(CaduceusStatusLang[0].translate());
        tooltipComponents.add(CaduceusStatusLang[1].translate());
        tooltipComponents.add(CaduceusStatusLang[2].translate());
        tooltipComponents.add(CaduceusTooltipNormalLang[1].translate());
        tooltipComponents.add(CaduceusTooltipNormalLang[2].translate());
        tooltipComponents.add(CaduceusTooltipNormalLang[3].translate());
        tooltipComponents.add(CaduceusDamageLang[0].translate(getAttack(stack)));
        tooltipComponents.add(CaduceusDamageLang[1].translate(getAttackSpeed(stack)));
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
    public Multimap<Attribute, AttributeModifier> definition$getDefaultAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        multimap.remove(Attributes.ATTACK_DAMAGE,new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)getAttack(stack), AttributeModifier.Operation.ADDITION));
        new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", Math.max(-3.9, (double)getAttackSpeed(stack)), AttributeModifier.Operation.ADDITION);
        if (equipmentSlot == EquipmentSlot.MAINHAND) {

            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)getAttack(stack), AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", Math.max(-3.9, (double)getAttackSpeed(stack)), AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }
    public double getAttack(ItemStack stack)
    {
        GTToolType type = getCurrentType(stack);
        if(type==null)return 12.0;
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
    public double getAttackSpeed(ItemStack stack)
    {
        GTToolType type = getCurrentType(stack);
        if(type==null)return 12.0;
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
    public Component getWeaponName(ItemStack stack)
    {
        GTToolType type = getCurrentType(stack);
        if(type==null)return CaduceusWeaponLang[0].translate();
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

}