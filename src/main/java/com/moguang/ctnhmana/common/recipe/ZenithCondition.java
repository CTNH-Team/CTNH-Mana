package com.moguang.ctnhmana.common.recipe;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.network.chat.Component;

import com.moguang.ctnhmana.Mutiblock.ManaReactor;
import com.moguang.ctnhmana.registry.CMRecipeConditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.Prefix;

import java.util.Map;

@Prefix("recipe.condition.zenith_condition")
public class ZenithCondition extends RecipeCondition {

    public static final Codec<ZenithCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("isReverse", false).forGetter(RecipeCondition::isReverse),
            Codec.BOOL.fieldOf("isZenith").forGetter(cond -> cond.isZenith),
            Codec.STRING.fieldOf("ZenithType").forGetter(cond -> cond.ZenithType),
            Codec.INT.fieldOf("tier").forGetter(cond -> cond.tier)).apply(instance, ZenithCondition::new));
    @Getter
    private boolean isZenith = false;
    @Getter
    private String ZenithType = "Blank";
    @Getter
    private int tier = 0;

    public ZenithCondition() {}

    public ZenithCondition(boolean isZenith) {
        super();
        this.ZenithType = "Blank";
        this.tier = 0;
        this.isZenith = isZenith;
    }

    public ZenithCondition(boolean isZenith, String zenithType, int tier) {
        this.isZenith = isZenith;
        if (!isZenith) {
            this.ZenithType = "Blank";
            this.tier = 0;
        } else {
            this.ZenithType = zenithType;
            this.tier = tier;
        }
    }

    public ZenithCondition(boolean isReverse, boolean isZenith, String zenithType, int tier) {
        super(isReverse);
        this.isZenith = isZenith;
        if (!isZenith) {
            this.ZenithType = "Blank";
            this.tier = 0;
        } else {
            this.ZenithType = zenithType;
            this.tier = tier;
        }
    }

    @Override
    public RecipeConditionType<ZenithCondition> getType() {
        return CMRecipeConditions.MANA_REACTOR_CONDITION;
    }

    @CN("§5需踏足虚境的幽隐之阈§r")
    @EN("§5需踏足虚境的幽隐之阈§r")
    static Lang is_zenith_tooltip;
    @CN("§d需眷于工业与远见之眼§r")
    @EN("§d需眷于工业与远见之眼§r")
    static Lang is_gt_tooltip;
    @CN("§d需眷于幻梦与繁星之耀§r")
    @EN("§d需眷于幻梦与繁星之耀§r")
    static Lang is_bt_tooltip;
    @CN("§d需眷于扭曲与歧路之影§r")
    @EN("§d需眷于扭曲与歧路之影§r")
    static Lang is_bm_tooltip;
    @CN("§d需眷于增长与繁衍之花§r")
    @EN("§d需眷于增长与繁衍之花§r")

    static Lang is_ars_tooltip;
    @CN("需要的接触等级：%s")
    @EN("需要的接触等级：%s")
    static Lang zenith_level_tooltip;
    @CN("无知(0)")
    @EN("无知")
    static Lang zenith_level_0;
    @CN("§a认知(1)")
    @EN("认知")
    static Lang zenith_level_1;
    @CN("§b接纳(2)")
    @EN("接纳")
    static Lang zenith_level_2;
    @CN("§c转化(3)")
    @EN("转化")
    static Lang zenith_level_3;
    @CN("§d贯通(4)")
    @EN("贯通")
    static Lang zenith_level_4;
    @CN("§5同调（5)")
    @EN("同调")
    static Lang zenith_level_5;
    @CN("%s\n%s\n%s\n")
    @EN("%s\n%s\n%s\n")
    static Lang Zenith_tooltip_all;
    Map<Integer, Lang> ZENITH_TIERS = Map.of(
            -1, zenith_level_0,
            0, zenith_level_0,
            1, zenith_level_1,
            2, zenith_level_2,
            3, zenith_level_3,
            4, zenith_level_4,
            5, zenith_level_5);
    Map<String, Lang> ZENITH_TYPES = Map.of(
            "GT", is_gt_tooltip,
            "BT", is_bt_tooltip,
            "BM", is_bm_tooltip,
            "ARS", is_ars_tooltip);

    @Override
    public Component getTooltips() {
        if (isZenith) {
            if (ZenithType.equals("Blank") || tier <= 0) return is_zenith_tooltip.translate();
            return Zenith_tooltip_all.translate(is_zenith_tooltip.translate(), ZENITH_TYPES.get(ZenithType).translate(),
                    zenith_level_tooltip.translate(ZENITH_TIERS.get(tier).translate()));
        }
        return null;
    }

    @Override
    protected boolean testCondition(@NotNull GTRecipe gtRecipe, @NotNull RecipeLogic recipeLogic) {
        var machine = recipeLogic.machine;
        if (machine instanceof ManaReactor mmachine) {
            if (isZenith) {
                if (mmachine.Zenith_Enhanced == null) return false;
                if (ZenithType.equals("Blank")) {
                    return true;
                }
                // 由于世界逻辑还没写，就先放在这
            }
            return true;
        }

        return false;
    }

    @Override
    public RecipeCondition createTemplate() {
        return new ZenithCondition();
    }
}
