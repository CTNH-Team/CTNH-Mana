package com.magicbee.ctnhmana.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import com.magicbee.ctnhmana.networking.packets.AntagonismPacket;

import static com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK;

/**
 * 苦难护盾：百分比减伤、移动加速、固定 90% 击退抗性、每 tick 生命恢复；
 * 被持有意志魂石的玩家攻击时会消耗意志降低等级（见
 * {@link com.magicbee.ctnhmana.event.PainShieldEventHandler}）。
 * 1.20.1 原版不会把怪物的效果同步给客户端，因此这里周期性推送数据包，
 * 由客户端 {@link com.magicbee.ctnhmana.client.render.AntagonismRender} 在头顶渲染图标。
 */
public class PainShieldEffect extends MobEffect {

    /** 移动速度修饰符 UUID。 */
    private static final String SPEED_UUID = "e9b3c1a2-4d6e-4a2b-9a2b-7f8e1c2d3a4b";
    /** 1 级速度 +50%，每级 +5%，至多 +100%。 */
    private static final double BASE_SPEED_BONUS = 0.5D;
    private static final double SPEED_BONUS_PER_LEVEL = 0.05D;
    private static final double MAX_SPEED_BONUS = 1.0D;
    /** 击退抗性修饰符 UUID 与固定 90% 抗性（不随等级变化）。 */
    private static final String KNOCKBACK_UUID = "f4c2b1a9-7d5e-4a3b-8c1d-2e9f0a1b3c4d";
    private static final double KNOCKBACK_RESISTANCE = 0.9D;

    public PainShieldEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x8B2E2E);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_UUID,
                BASE_SPEED_BONUS, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_UUID,
                KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        if (modifier.getId().toString().equals(KNOCKBACK_UUID)) {
            return KNOCKBACK_RESISTANCE; // 击退抗性固定 90%
        }
        return Math.min(BASE_SPEED_BONUS + SPEED_BONUS_PER_LEVEL * amplifier, MAX_SPEED_BONUS);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // 每 tick 恢复生命
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return; // 只在服务端执行
        }
        // 每 tick 恢复 1 点生命，每多 5 级额外 +1
        entity.heal(1.0F + (amplifier + 1) / 5);
        MobEffectInstance instance = entity.getEffect(this);
        if (instance != null && instance.getDuration() % 2 == 0) {
            NETWORK.sendToTrackingChunk(new AntagonismPacket(entity.getId(), this, instance.getDuration()),
                    new LevelChunk(entity.level(), new ChunkPos(entity.getOnPos())));
        }
    }
}
