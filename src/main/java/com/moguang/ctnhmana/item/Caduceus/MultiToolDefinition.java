package com.moguang.ctnhmana.item.Caduceus;

import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.IGTToolDefinition;
import com.gregtechceu.gtceu.api.item.tool.aoe.AoESymmetrical;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 多形态工具的 Definition：{@link #getBehaviors()} 恒为空，由物品自身根据 NBT 调用对应类型的 behaviors；
 * 其余方法按 stack 的当前类型委托给该类型的 {@link GTToolType#toolDefinition}。
 */
public final class MultiToolDefinition implements IGTToolDefinition {

    private final Function<ItemStack, GTToolType> currentTypeGetter;
    private final GTToolType defaultType;

    public MultiToolDefinition(Function<ItemStack, GTToolType> currentTypeGetter, GTToolType defaultType) {
        this.currentTypeGetter = currentTypeGetter;
        this.defaultType = defaultType;
    }

    private IGTToolDefinition def(ItemStack stack) {
        GTToolType type = currentTypeGetter.apply(stack);
        return (type != null ? type : defaultType).toolDefinition;
    }

    @Override
    public List<IToolBehavior> getBehaviors() {
        return Collections.emptyList();
    }

    @Override
    public boolean isToolEffective(BlockState state) {
        return defaultType.toolDefinition.isToolEffective(state);
    }

    @Override
    public int getDamagePerAction(ItemStack stack) {
        return def(stack).getDamagePerAction(stack);
    }

    @Override
    public int getDamagePerCraftingAction(ItemStack stack) {
        return def(stack).getDamagePerCraftingAction(stack);
    }

    @Override
    public boolean isSuitableForBlockBreak(ItemStack stack) {
        return def(stack).isSuitableForBlockBreak(stack);
    }

    @Override
    public boolean isSuitableForAttacking(ItemStack stack) {
        return def(stack).isSuitableForAttacking(stack);
    }

    @Override
    public boolean isSuitableForCrafting(ItemStack stack) {
        return def(stack).isSuitableForCrafting(stack);
    }

    @Override
    public int getBaseDurability(ItemStack stack) {
        return def(stack).getBaseDurability(stack);
    }

    @Override
    public float getDurabilityMultiplier(ItemStack stack) {
        return def(stack).getDurabilityMultiplier(stack);
    }

    @Override
    public int getBaseQuality(ItemStack stack) {
        return def(stack).getBaseQuality(stack);
    }

    @Override
    public float getBaseDamage(ItemStack stack) {
        return def(stack).getBaseDamage(stack);
    }

    @Override
    public float getBaseEfficiency(ItemStack stack) {
        return def(stack).getBaseEfficiency(stack);
    }

    @Override
    public float getEfficiencyMultiplier(ItemStack stack) {
        return def(stack).getEfficiencyMultiplier(stack);
    }

    @Override
    public float getAttackSpeed(ItemStack stack) {
        return def(stack).getAttackSpeed(stack);
    }

    @Override
    public AoESymmetrical getAoEDefinition(ItemStack stack) {
        return def(stack).getAoEDefinition(stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return def(stack).isEnchantable(stack);
    }

    @Override
    public boolean canApplyEnchantment(ItemStack stack, Enchantment enchantment) {
        return def(stack).canApplyEnchantment(stack, enchantment);
    }

    @Override
    public Object2IntMap<Enchantment> getDefaultEnchantments(ItemStack stack) {
        return def(stack).getDefaultEnchantments(stack);
    }

    @Override
    public boolean doesSneakBypassUse() {
        return defaultType.toolDefinition.doesSneakBypassUse();
    }

    @Override
    public ItemStack getBrokenStack() {
        return defaultType.toolDefinition.getBrokenStack();
    }
}
