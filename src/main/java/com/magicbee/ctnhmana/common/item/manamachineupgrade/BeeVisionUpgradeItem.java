package com.magicbee.ctnhmana.common.item.manamachineupgrade;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.magicbee.ctnhmana.common.entity.GiantBee;
import com.magicbee.ctnhmana.common.entity.RoyalServantBee;
import com.magicbee.ctnhmana.common.multiblock.BaseManaMachine;
import com.magicbee.ctnhmana.utils.CTNHManaUtils;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

/**
 * 蜜蜂之视野：以机器为中心 8 格半径内的每一只蜜蜂提供 +4 并行与 +10% 运行速度。
 * 若范围内存在究极无敌魔力大悲 Bee（{@link GiantBee}），则效果翻倍。
 * 运行速度加成最高提供 +1000%。
 */
public class BeeVisionUpgradeItem extends ManaMachineUpgradeItem {

    /** 侦查半径（格） */
    private static final double RANGE = 8.0D;
    /** 每只蜜蜂提供的并行 */
    private static final int PARALLEL_PER_BEE = 4;
    /** 每只蜜蜂提供的速度加成（倍率） */
    private static final double SPEED_PER_BEE = 0.10D;
    /** 究极无敌魔力大悲 Bee 存在时的翻倍系数 */
    private static final int GIANT_MULTIPLIER = 2;
    /** 最高速度加成上限（+1000%） */
    private static final double MAX_SPEED_BONUS = 10.0D;

    public BeeVisionUpgradeItem(Properties properties) {
        super(properties, "BeeVision", BEE_VISION_UPDATE_NAME);
    }

    @Override
    public BaseManaMachine.MachineMetric calculateNormalUpgrade(BaseManaMachine.MachineMetric metric,
                                                                BaseManaMachine machine) {
        Level level = machine.getLevel();
        BlockPos pos = machine.getPos();
        if (level instanceof ServerLevel serverLevel) {
            AABB range = new AABB(pos).inflate(RANGE);
            List<GiantBee> giantBees = serverLevel.getEntitiesOfClass(GiantBee.class, range);
            int beeCount = serverLevel.getEntitiesOfClass(Bee.class, range).size() +
                    serverLevel.getEntitiesOfClass(RoyalServantBee.class, range).size() + giantBees.size();
            int multiplier = giantBees.isEmpty() ? 1 : GIANT_MULTIPLIER;

            metric.parallel += beeCount * PARALLEL_PER_BEE * multiplier;
            metric.speed += Math.min(MAX_SPEED_BONUS, beeCount * SPEED_PER_BEE * multiplier);
        }
        return metric;
    }

    @Override
    public BaseManaMachine.MachineMetric calculateUpgrade(BaseManaMachine.MachineMetric metric, GTRecipe recipe,
                                                          BaseManaMachine machine, RecipeHandlerGroup group) {
        metric.true_parallel = CTNHManaUtils.getParallelAmount(group, recipe, Math.max(1, metric.parallel));
        return metric;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, itemTooltipsAdd(beeVisionLang, tooltipComponents), isAdvanced);
    }

    @CN("§e蜜蜂之视野")
    @EN("§eVantabeel Vision")
    public static Lang BEE_VISION_UPDATE_NAME;
    @CN({
            "聚焦于BEEEEEEE的升级",
            "机器获得：",
            "以机器为中心§b8格§r半径内，每一只蜜蜂提供§a+4§r并行与§a+10%§r运行速度",
            "若范围内存在§4究极无敌魔力大悲Bee§r，则以上效果§a翻倍§r",
            "运行速度加成最高提供§a+1000%§r",
            "§o§eBEEEEE终将统治格雷科技！§r"
    })
    @EN({
            "An upgrade driven by the surrounding bee swarm",
            "The machine gains:",
            "Every bee within a §b8-block§r radius grants §a+4§r parallel and §a+10%§r speed",
            "If a §4Superultra Gaint Mana Bee§r is present, all effects are §adoubled§r",
            "Speed bonus is capped at §a+1000%§r",
            "§o§eLet the buzzing of bees become the machine's pulse; the swarm's eye watches every crafted piece§r"
    })
    public static Lang[] beeVisionLang;
}
