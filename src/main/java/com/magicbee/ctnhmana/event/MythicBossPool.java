package com.magicbee.ctnhmana.event;

import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;

import dev.shadowsoffire.apotheosis.adventure.boss.ApothBoss;
import dev.shadowsoffire.apotheosis.adventure.boss.BossStats;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.placebo.json.ChancedEffectInstance;
import dev.shadowsoffire.placebo.json.GearSet.SetPredicate;
import dev.shadowsoffire.placebo.json.RandomAttributeModifier;
import dev.shadowsoffire.placebo.util.StepFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 无尽领域矿工房间精英怪的 mythic 品质自建池。
 * <p>
 * Apotheosis 原版 Boss 数据中主世界最高 rare、下界最高 epic，无法以 mythic
 * 生成；且末地的 endermite/evoker/shulker 会因寻路/飞行/瞬移机制脱离矿工房间。
 * 因此在代码中直接构造一组固定 mythic 品质的 Boss（实体覆盖主世界/下界/末地，
 * 剔除上述三个问题实体），由 {@link MinerEliteHandler} 按权重随机抽取。
 * <p>
 * 数值（体型/权重/词缀属性）参考 Apotheosis 原 Boss 数据，品质固定 mythic；
 * 实体按原版体型给出碰撞盒，便于 {@code createBoss} 的占位检查。
 */
public final class MythicBossPool {

    private MythicBossPool() {}

    /** 池条目：实体 + 体型 + 权重 + 可用装备组。 */
    private record Entry(EntityType<?> entity, float width, float height, int weight, List<SetPredicate> gearSets) {

        /** 以原点为基准的碰撞盒（{@code ApothBoss.AABB_CODEC} 同语义）。 */
        AABB box() {
            return new AABB(0, 0, 0, width, height, width);
        }
    }

    /** mythic 档通用属性模板（参考末地系 Boss 的 mythic stats）。 */
    private static final BossStats MYTHIC_STATS = new BossStats(0.85F,
            new int[] { 32, 25, 50, 50 },
            List.of(new ChancedEffectInstance(1.0F, MobEffects.FIRE_RESISTANCE,
                    new StepFunction(1.0F, 1, 0.0F), false, true)),
            List.of(
                    new RandomAttributeModifier(Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION,
                            new StepFunction(100.0F, 25, 2.0F)),
                    new RandomAttributeModifier(Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE,
                            new StepFunction(0.1F, 10, 0.01F)),
                    new RandomAttributeModifier(Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE,
                            new StepFunction(0.5F, 5, 0.01F)),
                    new RandomAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADDITION,
                            new StepFunction(0.05F, 1, 0.0F)),
                    new RandomAttributeModifier(Attributes.ARMOR, AttributeModifier.Operation.ADDITION,
                            new StepFunction(0.0F, 1, 0.0F)),
                    new RandomAttributeModifier(Attributes.ARMOR_TOUGHNESS, AttributeModifier.Operation.ADDITION,
                            new StepFunction(0.0F, 1, 0.0F))));

    /** 池内实体：主世界 6 + 下界 6 + 末地 2（剔除 endermite/evoker/shulker）。 */
    private static final List<Entry> ENTRIES = List.of(
            // 主世界
            entry(EntityType.ZOMBIE, 0.6F, 1.95F, 100, "#overworld"),
            entry(EntityType.SKELETON, 0.6F, 1.99F, 100, "#overworld_bow"),
            entry(EntityType.HUSK, 0.6F, 1.95F, 75, "#overworld"),
            entry(EntityType.STRAY, 0.6F, 1.99F, 75, "#overworld_bow"),
            entry(EntityType.VINDICATOR, 0.6F, 1.95F, 45, "#overworld"),
            entry(EntityType.WITCH, 0.6F, 1.95F, 45, "#overworld"),
            // 下界
            entry(EntityType.ZOMBIFIED_PIGLIN, 0.6F, 1.95F, 100, "#the_nether"),
            entry(EntityType.WITHER_SKELETON, 0.7F, 2.4F, 100, "#the_nether", "#the_nether_bow"),
            entry(EntityType.PIGLIN, 0.6F, 1.95F, 70, "#the_nether"),
            entry(EntityType.PIGLIN_BRUTE, 0.6F, 1.95F, 50, "#the_nether"),
            entry(EntityType.BLAZE, 0.6F, 1.8F, 30, "#the_nether"),
            entry(EntityType.ZOGLIN, 1.3965F, 1.4F, 30, "#the_nether"),
            // 末地
            entry(EntityType.ENDERMAN, 0.6F, 2.9F, 80, "#the_end"),
            entry(EntityType.PHANTOM, 0.9F, 0.5F, 50, "#the_end"));

    private static Entry entry(EntityType<?> entity, float width, float height, int weight, String... gearTags) {
        List<SetPredicate> gearSets = new ArrayList<>();
        for (String tag : gearTags) {
            gearSets.add(new SetPredicate(tag));
        }
        return new Entry(entity, width, height, weight, gearSets);
    }

    /**
     * 按权重随机抽取一个 mythic Boss。
     *
     * @param mythic 已解析的 mythic 品质（由调用方传入，避免重复查注册表）
     */
    public static ApothBoss getRandom(RandomSource rand, LootRarity mythic) {
        int total = 0;
        for (Entry entry : ENTRIES) {
            total += entry.weight();
        }
        int roll = rand.nextInt(total);
        for (Entry entry : ENTRIES) {
            roll -= entry.weight();
            if (roll < 0) {
                return buildBoss(entry, mythic);
            }
        }
        return buildBoss(ENTRIES.get(ENTRIES.size() - 1), mythic);
    }

    private static ApothBoss buildBoss(Entry entry, LootRarity mythic) {
        return new ApothBoss(entry.weight(), 0.0F, entry.entity(), entry.box(),
                Map.of(mythic, MYTHIC_STATS),
                Optional.empty(), entry.gearSets(), Optional.empty(),
                Set.of(), mythic, mythic, Optional.empty());
    }
}
