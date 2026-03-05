package com.moguang.ctnhmana.common.recipe;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import com.moguang.ctnhmana.Mutiblock.HellForgeMachine;
import com.moguang.ctnhmana.registry.CMRecipeConditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.Map;

public class HellForgeCondition extends RecipeCondition {

    public String Willtype;
    public double consume;
    public static final Codec<HellForgeCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("isReverse", false).forGetter(RecipeCondition::isReverse),
            Codec.STRING.optionalFieldOf("willtype", "default").forGetter(cond -> cond.Willtype),
            Codec.DOUBLE.fieldOf("consume").forGetter(cond -> cond.consume)).apply(instance, HellForgeCondition::new));

    public HellForgeCondition() {}

    public HellForgeCondition(double consume) {
        super();
        this.Willtype = "default";
        this.consume = consume;
    }

    public HellForgeCondition(String typer, double consume) {
        super();
        this.Willtype = typer;
        this.consume = consume;
    }

    public HellForgeCondition(boolean reverse, String typer, double consume) {
        super(reverse);
        this.Willtype = typer;
        this.consume = consume;
    }

    @CN({
            "§9普通",
            "§5坚韧",
            "§a侵蚀",
            "§6破坏",
            "§c复仇",
    })
    @EN({
            "§9普通",
            "§5坚韧",
            "§a侵蚀",
            "§6破坏",
            "§c复仇",
    })
    public static Lang[] WillLang;
    @CN("需求的恶魔意志类型:%s")
    public static Lang WillWilltypeLang;
    @CN("需求的数量:%d")
    public static Lang WillNumLang;
    @CN("%s\n%s")
    public static Lang WillAllLang;

    @Override
    public RecipeConditionType<HellForgeCondition> getType() {
        return CMRecipeConditions.HELL_FORGE_CONDITION;
    }

    @Override
    public Component getTooltips() {
        Map<@NotNull String, @NotNull MutableComponent> DEMON_TYPE = Map.of(
                "default", WillLang[0].translate(),
                "steadfast", WillLang[1].translate(),
                "corrosive", WillLang[2].translate(),
                "destructive", WillLang[3].translate(),
                "vengeful", WillLang[4].translate());
        return WillAllLang.translate(WillWilltypeLang.translate(DEMON_TYPE.get(Willtype)),
                WillNumLang.translate(consume));
    }

    @Override
    protected boolean testCondition(@NotNull GTRecipe gtRecipe, @NotNull RecipeLogic recipeLogic) {
        var machine = recipeLogic.machine;
        if (machine instanceof HellForgeMachine hmachine) {
            if (hmachine.hatch == null) return false;
            if (hmachine.consumeLock == false) {
                if (hmachine.hatch.ConsumeWillIfEnough(Willtype, consume)) {
                    hmachine.consumeLock = true;
                    return true;
                }
                return false;
            }
            return hmachine.consumeLock;

        }
        return false;
    }

    @Override
    public RecipeCondition createTemplate() {
        return new HellForgeCondition();
    }
}
