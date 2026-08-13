package com.magicbee.ctnhmana.api.recipe.condition;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.network.chat.Component;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.magicbee.ctnhmana.common.multiblock.IndustrialAltarMachine;
import com.magicbee.ctnhmana.registry.CMItems;
import com.magicbee.ctnhmana.registry.CMRecipeConditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

public class BloodAltarCondition extends RecipeCondition<BloodAltarCondition> {

    public int altar_tier;
    public int consumption_rate;
    public int min_consumption;
    public String upgrade;

    public static final Codec<BloodAltarCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tier").forGetter(cond -> cond.altar_tier),
            Codec.INT.optionalFieldOf("consumption_rate", 1).forGetter(cond -> cond.consumption_rate),
            Codec.INT.optionalFieldOf("min_consumption", 1).forGetter(cond -> cond.min_consumption),
            Codec.STRING.optionalFieldOf("upgrade", "None").forGetter(cond -> cond.upgrade))
            .apply(instance, BloodAltarCondition::new));

    @Override
    public RecipeConditionType<BloodAltarCondition> getType() {
        return CMRecipeConditions.BLOOD_ALTAR_CONDITION;
    }

    public BloodAltarCondition() {}

    public BloodAltarCondition(int tier, int consumption_rate) {
        this.altar_tier = tier;
        this.consumption_rate = consumption_rate;
        this.min_consumption = consumption_rate * 200;// default:10s
        this.upgrade = "None";
    }

    public BloodAltarCondition(int tier, int consumption_rate, int min_consumption) {
        this.altar_tier = tier;
        this.consumption_rate = consumption_rate;
        this.min_consumption = min_consumption;// default:10s
        this.upgrade = "None";
    }

    public BloodAltarCondition(int tier, int consumption, int min_consumption, String upgrade) {
        this.altar_tier = tier;
        this.consumption_rate = consumption;
        this.min_consumption = min_consumption;
        this.upgrade = upgrade;
    }

    @Override
    public Component getTooltips() {
        if (upgrade == null || upgrade.equals("None"))
            return altar_lang_2.translate(altar_tier_lang.translate(altar_tier),
                    altar_consumption_lang[1].translate(min_consumption),
                    altar_consumption_lang[0].translate(consumption_rate));
        else if (upgrade.equals("etching")) return altar_lang_1.translate(altar_tier_lang.translate(altar_tier),
                altar_consumption_lang[1].translate(min_consumption),
                altar_consumption_lang[0].translate(consumption_rate),
                altar_upgrade_lang.translate(CMItems.etching_jade_upgrade.translate()));
        else if (upgrade.equals("suppression")) return altar_lang_1.translate(altar_tier_lang.translate(altar_tier),
                altar_consumption_lang[1].translate(min_consumption),
                altar_consumption_lang[0].translate(consumption_rate),
                altar_upgrade_lang.translate(CMItems.suppression_jade_upgrade.translate()));
        else if (upgrade.equals("ephemeral")) return altar_lang_1.translate(altar_tier_lang.translate(altar_tier),
                altar_consumption_lang[1].translate(min_consumption),
                altar_consumption_lang[0].translate(consumption_rate),
                altar_upgrade_lang.translate(CMItems.ephemeral_jade_upgrade.translate()));
        return null;
    }

    @Override
    protected boolean testCondition(@NotNull GTRecipe gtRecipe, @NotNull RecipeLogic recipeLogic) {
        var machine = recipeLogic.getMachine();
        if (machine instanceof IndustrialAltarMachine altarMachine) {
            if (altarMachine.altar_tier < this.altar_tier) return false;
            if (!altarMachine.getUpgrade().equals(this.upgrade) && this.upgrade != "None") return false;
            return true;
        }
        return false;
    }

    @Override
    public BloodAltarCondition createTemplate() {
        return new BloodAltarCondition();
    }

    @CN("需要血祭坛等级:%d")
    @EN("Blood Altar tier required: %d")
    public static Lang altar_tier_lang;
    @CN({
            "消耗LP速率：%d/tick",
            "至少消耗的LP总量: %d",
    })
    @EN({
            "LP consumption rate: %d/tick",
            "Minimum total LP consumed: %d",
    })
    public static Lang[] altar_consumption_lang;
    @CN("需要的升级: %s")
    @EN("Required upgrade: %s")
    public static Lang altar_upgrade_lang;
    @CN("%s\n%s\n%s\n%s")
    @EN("%s\n%s\n%s\n%s")
    public static Lang altar_lang_1;
    @CN("%s\n%s\n%s\n")
    @EN("%s\n%s\n%s\n")
    public static Lang altar_lang_2;
}
