package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import com.ctnhlang.*;
import com.moguang.ctnhmana.registry.CMItems;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

@Domain("multiblock.quasar_eye")
public class QuasarEye extends RecipeElectricMultiblockMachine implements ITieredMachine {

    private double rune_energy = 0;
    private int energy_tier = 0;
    private int active = 0;
    public static final String RUNE_ENERGY = "rune_energy";
    public static final String ET = "energy_tier";
    public static final String ACTIVE = "active";
    public long power = 0;
    private static int[] based_output = { 0, 67108864, 134217728, 268435456 };

    public QuasarEye(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        rune_energy = 0;
        energy_tier = 0;
        active = 0;
    }

    public double energy_caculate(double rune, int energy_tier) {
        if (rune <= 50) {
            return 0.5;
        }
        return Math.min((Math.log(((rune) / 50))) + 1, 1 + energy_tier * 1);
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        if (!MachineUtils.inputFluid(CMMaterials.Mana.getFluid(recipe.data.getInt("consumption")), this)) {
            if (recipe.data.getInt("active") > active) return Component.empty();
        }

        if (MachineUtils.inputItem(CMItems.TWIST_RUNE.asStack(1), this)) {
            rune_energy += 32;
        }
        if (MachineUtils.inputItem(CMItems.HORIZEN_RUNE.asStack(1), this)) {
            rune_energy += 32;
        }
        if (MachineUtils.inputItem(CMItems.STARLIGHT_RUNE.asStack(1), this)) {
            rune_energy += 32;
        }
        if (MachineUtils.inputItem(CMItems.PROLIFERATION_RUNE.asStack(1), this)) {
            rune_energy += 16;
        }
        if (MachineUtils.inputItem(CMItems.QUASAR_RUNE.asStack(1), this)) {
            rune_energy += 512;
        }
        if (active < recipe.data.getInt("active")) {
            active = recipe.data.getInt("active");
        }
        energy_tier = recipe.data.getInt("tier");
        return super.beforeWorking(recipe);
    }

    @Override
    public boolean onWorking() {
        if (getOffsetTimer() % 100 == 0) {
            if (rune_energy > 0) rune_energy -= Math.max((rune_energy / 50) * Math.log((rune_energy / 50) + 1), 0);
        }
        return super.onWorking();
    }

    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        textList.add(textList.size(), INFO_MANA_MODEL.translate(String.format("%d", energy_tier)));
        textList.add(textList.size(), INFO_RUNE_ENERGY.translate(String.format("%.2f", rune_energy)));
        textList.add(textList.size(),
                INFO_MANA_PRODUCTION.translate(String.format("%.2f", energy_caculate(rune_energy, energy_tier))));
        textList.add(textList.size(), INFO_RUNE_CONSUMPTION
                .translate(String.format("%.2f", (rune_energy / 50) * Math.log(rune_energy / 50 + 1))));
        textList.add(textList.size(),
                INFO_QUASAR_PARALLEL.translate(String.format("%.2f", energy_caculate(rune_energy, energy_tier) * 5)));
        textList.add(textList.size(), INFO_CONSUMPTION_PARALLEL
                .translate(String.format("%.2f", (1 - 0.05 * Math.max((rune_energy - 50) / 50, 10)))));
        textList.add(textList.size(), INFO_ACCUMULATED.translate((double) power / 100000000 + "E EU"));
    }

    // lang: info (display in UI)
    @Key("info.mana_model")
    @CN("å½“å‰é­”åŠ›ç‡ƒæ–™ç­‰çº§:%d")
    @EN("Current mana fuel tier: %d")
    public static Lang INFO_MANA_MODEL;
    @Key("info.rune_energy")
    @CN("ç¬¦æ–‡èƒ½é‡ï¼?.2f")
    @EN("Rune energy: %.2f")
    public static Lang INFO_RUNE_ENERGY;
    @Key("info.mana_production")
    @CN("å½“å‰å‘ç”µæ•ˆçŽ‡:%.2f")
    @EN("Current generation efficiency: %.2f")
    public static Lang INFO_MANA_PRODUCTION;
    @Key("info.rune_consumption")
    @CN("å½“å‰æ¶ˆè€—ç¬¦æ–‡èƒ½é‡é€ŸçŽ‡:%.2f /100tick")
    @EN("Rune energy consumption rate: %.2f / 100 ticks")
    public static Lang INFO_RUNE_CONSUMPTION;
    @Key("info.quasar_parallel")
    @CN("æ—¶é—´å¹¶è¡Œ:%.2f")
    @EN("Time parallelism: %.2f")
    public static Lang INFO_QUASAR_PARALLEL;
    @Key("info.consumption_parallel")
    @CN("èƒ½æºæ¶ˆè€—çŽ‡:%.2f")
    @EN("Energy consumption factor: %.2f")
    public static Lang INFO_CONSUMPTION_PARALLEL;
    @Key("info.0")
    @CN("ç§¯ç´¯çš„èƒ½é‡?%s")
    @EN("Accumulated energy: %s")
    public static Lang INFO_ACCUMULATED;

    // lang: tooltips (multiblock description)
    @Key("ctnh.multiblock.quasar_eye.tooltip")
    @CN({
            "Â§9é­”åŠ›Â§rçš„Â§cç»ˆæžå¥¥ç§˜Â§rï¼Œèƒ½å¤Ÿåˆ›é€ Â?ç±»æ˜Ÿä½“Â§rçš„è£…ç½®å¦‚ä»Šæ¡åœ¨Â?ä½ Â§ræ‰‹ä¸­",
            "æœºå™¨æ¿€æ´»éœ€è¦Â§ræ¶ˆè€—åˆå§‹é­”åŠ›ç‡ƒæ–™Â§rï¼Œå…·ä½“æ•°å€¼è¯·æŸ¥é˜…JEI",
            "åœ¨é«˜èƒ½é‡ç­‰çº§ä¸‹æ¿€æ´»ä½Žç­‰çº§é…æ–¹æ—¶Â§bå¯å…é™¤æ¿€æ´»æ¶ˆè€—Â§r",
            "Â§5ç¬¦æ–‡èƒ½é‡Â§rå†³å®šäº§å‡ºå¼ºåº¦ã€‚æŠ•å…¥Â§bäº”çº§ç¬¦æ–‡Â§rå¯æ”¾å¤§ç¬¦æ–‡èƒ½é‡å¹¶æå‡äº§å‡ºã€‚ä½¿ç”¨Â?ç±»æ˜Ÿä½“ç¬¦æ–‡Â§rå¯å¤§é‡ç”Ÿæˆç¬¦æ–‡èƒ½é‡?",
            "ç¬¦æ–‡èƒ½é‡èŽ·å–é€»è¾‘ï¼šÂ?æ¯æ¬¡é…æ–¹å‘¨æœŸå‰Â§rï¼Œæ¯ç§å¯æ¶ˆè€—ç¬¦æ–‡ç±»åž‹Â§cæœ€å¤šæ¶ˆè€—ä¸€ä¸ªÂ§r",
            "Â§cè­¦å‘ŠÂ§rï¼šç¬¦æ–‡èƒ½é‡è¶Šé«˜ï¼ŒÂ§cæ¶ˆè€—é€ŸçŽ‡Â§rè¶Šå¿«ã€‚ç¬¦æ–‡èƒ½é‡ä½Žäº?0æ—¶æ•ˆçŽ‡Â§cå‡åŠÂ§rï¼?",
            "èƒ½é‡æ•ˆçŽ‡å…¬å¼ï¼šlog((ç¬¦æ–‡èƒ½é‡)/50)+1ã€‚æœ€å¤§æ•ˆçŽ‡ï¼š(1+èƒ½é‡ç­‰çº§)",
            "å…·æœ‰æ—¶é—´å¹¶è¡Œã€‚æ¶ˆè€—ä¸Žæ—¶é•¿å‡ä¹˜ä»¥å¹¶è¡Œç³»æ•?æ•ˆçŽ‡*5)",
            "ç‡ƒæ–™æ¶ˆè€—å…¬å¼ï¼š1-0.05*Math.max((ç¬¦æ–‡èƒ½é‡-50)/50,0.75)",
            "å‘ç”µæ¨¡å¼ä¸‹ï¼Œå°?%çš„EUäº§å‡ºç§¯ç´¯è¿›ç±»æ˜Ÿä½“ä¹‹çœ¼ã€‚æ¯25ç‚¹ç¬¦æ–‡èƒ½é‡é¢å¤?1%ç§¯ç´¯",
            "åˆ›é€ æ¨¡å¼ä¸‹ï¼Œé‡Šæ”¾å…¨éƒ¨å‚¨å­˜EUã€‚é«˜çº§ç‡ƒæ–™å¯æ”¾å¤§äº§å‡ºã€‚æ¯1000E EUäº§ç”Ÿé¢å¤–æ°”ä½“å‰¯äº§ç‰©ã€‚å‚¨å­˜EU<1Eæ—¶åˆ›é€ æ¨¡å¼ç¦ç”?",
            "Â§bå¥½æ¶ˆæ¯Â§rï¼šè¿™å°æœºå™¨ä¸ä¼šçˆ†ç‚¸ã€‚Â§cä½†æ— æ³•ä¿è¯ä»¥åŽç‰ˆæœ¬ï¼Â§r"
    })
    @EN({
            "Â§9é­”åŠ›Â§rçš„Â§cç»ˆæžå¥¥ç§˜Â§rï¼Œèƒ½å¤Ÿåˆ›é€ Â?ç±»æ˜Ÿä½“Â§rçš„è£…ç½®å¦‚ä»Šæ¡åœ¨Â?ä½ Â§ræ‰‹ä¸­",
            "æœºå™¨æ¿€æ´»éœ€è¦Â§ræ¶ˆè€—åˆå§‹é­”åŠ›ç‡ƒæ–™Â§rï¼Œå…·ä½“æ•°å€¼è¯·æŸ¥é˜…JEI",
            "åœ¨é«˜èƒ½é‡ç­‰çº§ä¸‹æ¿€æ´»ä½Žç­‰çº§é…æ–¹æ—¶Â§bå¯å…é™¤æ¿€æ´»æ¶ˆè€—Â§r",
            "Â§5ç¬¦æ–‡èƒ½é‡Â§rå†³å®šäº§å‡ºå¼ºåº¦ã€‚æŠ•å…¥Â§bäº”çº§ç¬¦æ–‡Â§rå¯æ”¾å¤§ç¬¦æ–‡èƒ½é‡å¹¶æå‡äº§å‡ºã€‚ä½¿ç”¨Â?ç±»æ˜Ÿä½“ç¬¦æ–‡Â§rå¯å¤§é‡ç”Ÿæˆç¬¦æ–‡èƒ½é‡?",
            "ç¬¦æ–‡èƒ½é‡èŽ·å–é€»è¾‘ï¼šÂ?æ¯æ¬¡é…æ–¹å‘¨æœŸå‰Â§rï¼Œæ¯ç§å¯æ¶ˆè€—ç¬¦æ–‡ç±»åž‹Â§cæœ€å¤šæ¶ˆè€—ä¸€ä¸ªÂ§r",
            "Â§cè­¦å‘ŠÂ§rï¼šç¬¦æ–‡èƒ½é‡è¶Šé«˜ï¼ŒÂ§cæ¶ˆè€—é€ŸçŽ‡Â§rè¶Šå¿«ã€‚ç¬¦æ–‡èƒ½é‡ä½Žäº?0æ—¶æ•ˆçŽ‡Â§cå‡åŠÂ§rï¼?",
            "èƒ½é‡æ•ˆçŽ‡å…¬å¼ï¼šlog((ç¬¦æ–‡èƒ½é‡)/50)+1ã€‚æœ€å¤§æ•ˆçŽ‡ï¼š(1+èƒ½é‡ç­‰çº§)",
            "å…·æœ‰æ—¶é—´å¹¶è¡Œã€‚æ¶ˆè€—ä¸Žæ—¶é•¿å‡ä¹˜ä»¥å¹¶è¡Œç³»æ•?æ•ˆçŽ‡*5)",
            "ç‡ƒæ–™æ¶ˆè€—å…¬å¼ï¼š1-0.05*Math.max((ç¬¦æ–‡èƒ½é‡-50)/50,0.75)",
            "å‘ç”µæ¨¡å¼ä¸‹ï¼Œå°?%çš„EUäº§å‡ºç§¯ç´¯è¿›ç±»æ˜Ÿä½“ä¹‹çœ¼ã€‚æ¯25ç‚¹ç¬¦æ–‡èƒ½é‡é¢å¤?1%ç§¯ç´¯",
            "åˆ›é€ æ¨¡å¼ä¸‹ï¼Œé‡Šæ”¾å…¨éƒ¨å‚¨å­˜EUã€‚é«˜çº§ç‡ƒæ–™å¯æ”¾å¤§äº§å‡ºã€‚æ¯1000E EUäº§ç”Ÿé¢å¤–æ°”ä½“å‰¯äº§ç‰©ã€‚å‚¨å­˜EU<1Eæ—¶åˆ›é€ æ¨¡å¼ç¦ç”?",
            "Â§bå¥½æ¶ˆæ¯Â§rï¼šè¿™å°æœºå™¨ä¸ä¼šçˆ†ç‚¸ã€‚Â§cä½†æ— æ³•ä¿è¯ä»¥åŽç‰ˆæœ¬ï¼Â§r"
    })
    public static Lang[] TOOLTIPS;

    /**
     * ç±»æ˜Ÿä½“ä¹‹çœ¼é…æ–¹åœ¨ JEI ä¸­æ˜¾ç¤ºçš„ dataInfoï¼Œç”± LangProcessor æ³¨å†Œ ctnh.recipe.quasar_eye.info.0/1/2
     */
    @Category("recipe")
    public static class RecipeLang {

        @CN("æ¿€æ´»æ¶ˆè€—ï¼š%.1f")
        @EN("Activation cost: %.1f")
        public static Lang RECIPE_INFO_0;

        @CN("èƒ½é‡ç­‰çº§ï¼?d")
        @EN("Energy tier: %d")
        public static Lang RECIPE_INFO_1;

        @CN("æ¿€æ´»ç­‰çº§ï¼š%d")
        @EN("Activation tier: %d")
        public static Lang RECIPE_INFO_2;
    }

    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, @NotNull GTRecipe recipe) {
        if (machine instanceof QuasarEye qmachine) {
            if (recipe.recipeType.equals(CMRecipeTypes.QUASAR_CREATE)) {

                var true_eut = Math.min(qmachine.power / 200, Long.MAX_VALUE);
                if (true_eut < 10000000) return null;
                var outputmuti = true_eut / 50000000L;
                qmachine.power = 0;
                recipe.multiplyEUt(true_eut);
                recipe.multiplyOutputs((int) outputmuti);
                recipe.multiplyTickOutputs((int) outputmuti);
                return null;
            }
            var EUt = RecipeHelper.getRealEUtWithIO(recipe);
            var tier = recipe.data.getInt("tier");
            var power = (long) (qmachine.energy_caculate(qmachine.rune_energy, tier) *
                    RecipeHelper.getRealEUtWithIO(recipe) *
                    (qmachine.energy_caculate(qmachine.rune_energy, tier) * 5) * recipe.duration * 0.2 *
                    (qmachine.rune_energy / 25));
            qmachine.power += power / 200;
            var efficiency = qmachine.energy_caculate(qmachine.rune_energy, tier);
            var contentMultiplier = efficiency * 5 *
                    (1 - 0.05 * Math.max((qmachine.rune_energy - 50) / 50, 10));
            recipe.multiplyEUt(efficiency);
            recipe.multiplyDuration(efficiency * 5);
            recipe.multiplyInputs((int) contentMultiplier);
            recipe.multiplyOutputs((int) contentMultiplier);
            recipe.multiplyTickInputs((int) contentMultiplier);
            recipe.multiplyTickOutputs((int) contentMultiplier);
            return null;
        }
        return null;
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        if (!forDrop) {
            tag.putInt(ACTIVE, active);
            tag.putInt(ET, energy_tier);
            tag.putDouble(RUNE_ENERGY, rune_energy);
        }
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        active = tag.contains(ACTIVE) ? tag.getInt(ACTIVE) : 0;
        energy_tier = tag.contains(ET) ? tag.getInt(ET) : 0;
        rune_energy = tag.contains(RUNE_ENERGY) ? tag.getDouble(RUNE_ENERGY) : 0;
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }
}
