package com.magicbee.ctnhmana.api.recipe.condition;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.network.chat.Component;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.magicbee.ctnhmana.common.multiblock.ManaMultiBlockMachine;
import com.magicbee.ctnhmana.registry.CMRecipeConditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

/**
 * 注术单元铸造条件：检测魔力凝聚仓当前储量是否不少于配方要求的魔力消耗。
 */
public class InfusionCellCastingCondition extends RecipeCondition<InfusionCellCastingCondition> {

    public static final Codec<InfusionCellCastingCondition> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.BOOL.optionalFieldOf("isReverse", false).forGetter(RecipeCondition::isReverse),
                    Codec.LONG.fieldOf("mana_cost").forGetter(c -> c.manaCost))
            .apply(instance, InfusionCellCastingCondition::new));

    @Getter
    private long manaCost;

    public InfusionCellCastingCondition() {
        super();
        this.manaCost = 0L;
    }

    public InfusionCellCastingCondition(long manaCost) {
        super();
        this.manaCost = manaCost;
    }

    public InfusionCellCastingCondition(boolean isReverse, long manaCost) {
        super(isReverse);
        this.manaCost = manaCost;
    }

    @Override
    public RecipeConditionType<InfusionCellCastingCondition> getType() {
        return CMRecipeConditions.INFUSION_CELL_CASTING_CONDITION;
    }

    @CN("需要消耗的魔力能量:%d")
    @EN("Infusion cell casting: condenser mana ≥ %s")
    static Lang tooltipLang;

    @Override
    public Component getTooltips() {
        return tooltipLang.translate(formatMana(manaCost));
    }

    private static String formatMana(long v) {
        return String.format("%,d", v);
    }

    @Override
    protected boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        var machine = recipeLogic.getMachine();
        if (!(machine instanceof ManaMultiBlockMachine manaMultiBlockMachine)) {
            return false;
        }
        var hatch = manaMultiBlockMachine.hatch != null ? manaMultiBlockMachine.hatch :
                manaMultiBlockMachine.getHatch();
        return hatch != null;
    }

    @Override
    public InfusionCellCastingCondition createTemplate() {
        return new InfusionCellCastingCondition();
    }
}
