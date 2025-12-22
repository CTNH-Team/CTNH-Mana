package com.moguang.ctnhmana.common.recipe;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.moguang.ctnhmana.registry.CMRecipeConditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

public class BloodAltarCondition extends RecipeCondition {
    public int altar_tier;
    public int consumption;
    public String upgrade;
    public static final Codec<BloodAltarCondition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("consumption", 1).forGetter(cond-> cond.consumption),
                    Codec.INT.fieldOf("tier").forGetter(cond-> cond.altar_tier),
                    Codec.STRING.optionalFieldOf("upgrade","None").forGetter(cond-> cond.upgrade)
            ).apply(instance,BloodAltarCondition::new)
    );

    @Override
    public RecipeConditionType<?> getType() {
        return CMRecipeConditions.BLOOD_ALTAR_CONDITION;
    }
    public BloodAltarCondition() {}
    public BloodAltarCondition(int tier,int consumption) {
        this.altar_tier=tier;
        this.consumption=consumption;
    }
    public BloodAltarCondition(int tier,int consumption,String upgrade) {
        this.altar_tier=tier;
        this.consumption=consumption;
        this.upgrade=upgrade;
    }
    @Override
    public Component getTooltips() {
        if(upgrade==null||upgrade.equals("None")) return altar_lang.translate(altar_tier_lang.translate(altar_tier),altar_consumption_lang.translate(consumption));
        return altar_lang.translate(altar_tier_lang.translate(altar_tier),altar_consumption_lang.translate(consumption),altar_upgrade_lang.translate(upgrade));
    }

    @Override
    protected boolean testCondition(@NotNull GTRecipe gtRecipe, @NotNull RecipeLogic recipeLogic) {
        return false;
    }

    @Override
    public RecipeCondition createTemplate() {
        return new BloodAltarCondition();
    }
    @CN(
            "需要血祭坛等级:%d"
    )
    public static Lang altar_tier_lang;
    @CN(
            "每tick消耗的生命源质数量 %d"
    )
    public static Lang altar_consumption_lang;
    @CN(
            "需要的升级: %s"
    )
    public static Lang altar_upgrade_lang;
    @CN("%s\n%s\n%s")
    public static Lang altar_lang;
}
