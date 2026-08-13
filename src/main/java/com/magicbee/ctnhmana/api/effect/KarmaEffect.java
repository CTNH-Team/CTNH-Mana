package com.magicbee.ctnhmana.api.effect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.UUID;

public class KarmaEffect extends MobEffect {

    public static final UUID KARMA_HEATH_UUID = UUID.fromString("80802345-5566-7788-9900-AABBCCDDEEFF");
    public static final UUID KARMA_ARMOR_UUID = UUID.fromString("80802345-BBAA-0099-8877-665544332211");

    public KarmaEffect() {
        super(MobEffectCategory.HARMFUL, 0x9900FF);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            CompoundTag playerTag = player.getPersistentData();
            if (livingEntity.getEffect(this).getDuration() <= 150) {
                playerTag.remove("karma");
            } else if (!playerTag.contains("karma")) {
                playerTag.putInt("karma", amplifier);
            }

        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 100 == 0;
    }

    // @Override
    // public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity
    // livingEntity, int amplifier, double health) {
    // if(livingEntity instanceof Player player)
    // {
    // CompoundTag playerTag = player.getPersistentData();
    // playerTag.putInt("karma",amplifier);
    //
    // }
    // }
    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int amplifier) {
        super.addAttributeModifiers(entity, map, amplifier);
        if (entity instanceof Player player) {
            CompoundTag playerTag = player.getPersistentData();
            playerTag.putInt("karma", amplifier);

        }
        AttributeModifier armorModifier = new AttributeModifier(
                KARMA_ARMOR_UUID,
                "Karma of armor",
                -(2 * (amplifier + 1) + (amplifier) * (amplifier + 1) / 2),
                AttributeModifier.Operation.ADDITION);

        Multimap<Attribute, AttributeModifier> attributeMultimap = HashMultimap.create();
        attributeMultimap.put(Attributes.ARMOR, armorModifier);

        AttributeModifier lifeModifier = new AttributeModifier(
                KARMA_HEATH_UUID,
                "karma of health",
                ((int) ((amplifier + 1) / 5)) * -5,
                AttributeModifier.Operation.ADDITION);
        attributeMultimap.put(Attributes.MAX_HEALTH, lifeModifier);
        map.addTransientAttributeModifiers(attributeMultimap);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map,
                                         int amplifier) {
        super.removeAttributeModifiers(entity, map, amplifier);
        if (!(entity instanceof Player player) || player.level().isClientSide()) {
            return;
        }
    }
}
