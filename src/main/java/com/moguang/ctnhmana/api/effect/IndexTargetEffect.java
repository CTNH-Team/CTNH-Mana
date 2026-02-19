package com.moguang.ctnhmana.api.effect;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class IndexTargetEffect extends MobEffect {
    public IndexTargetEffect() {
        super(MobEffectCategory.NEUTRAL, 0x9900FF);
    }
    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int amplifier) {
        super.addAttributeModifiers(entity, map, amplifier);
        var tag=entity.getPersistentData();
        tag.putBoolean("index_targeted",true);
    }
    @Override
    public void removeAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap map, int amplifier) {

        super.removeAttributeModifiers(entity, map, amplifier);
        if (!(entity instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        entity.getPersistentData().remove("index_targeted");

    }
}