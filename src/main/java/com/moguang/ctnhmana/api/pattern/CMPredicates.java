package com.moguang.ctnhmana.api.pattern;

import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;

import static wayoftime.bloodmagic.common.block.BloodMagicBlocks.*;

public class CMPredicates {
   public static TraceabilityPredicate BMRuneBlocks = Predicates.blocks(
           // 等级1符文（按顺序逐个放入）
           BLANK_RUNE.get(),
           SPEED_RUNE.get(),
           SACRIFICE_RUNE.get(),
           SELF_SACRIFICE_RUNE.get(),
           DISPLACEMENT_RUNE.get(),
           CAPACITY_RUNE.get(),
           AUGMENTED_CAPACITY_RUNE.get(),
           ORB_RUNE.get(),
           ACCELERATION_RUNE.get(),
           CHARGING_RUNE.get(),
           SELF_SACRIFICE_RUNE_2.get(),
           SPEED_RUNE_2.get(),
           SACRIFICE_RUNE_2.get(),
           SELF_SACRIFICE_RUNE_2.get(),
           DISPLACEMENT_RUNE_2.get(),
           CAPACITY_RUNE_2.get(),
           AUGMENTED_CAPACITY_RUNE_2.get(),
           ORB_RUNE_2.get(),
           ACCELERATION_RUNE_2.get(),
           CHARGING_RUNE_2.get());

}
