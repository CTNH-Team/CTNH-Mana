package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeCapabilities;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import dev.shadowsoffire.hostilenetworks.Hostile;
import dev.shadowsoffire.hostilenetworks.item.DataModelItem;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class EternalWosMachine extends RecipeElectricMultiblockMachine {

    public double multiplier = 0;

    public EternalWosMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    public String mode = "lp";
    @Persisted
    public double will_adder = 0;
    @Persisted
    public boolean soul_infusion_mode = false;
    public static int max_will = 200;

    /**
     * Fluid-output-equivalent will amount for the active modified recipe (soul infusion).
     * Kept across continuous runs when {@code alwaysTryModifyRecipe} is false.
     */
    @Persisted
    private long soulWillPerRecipe = 0;

    /** Resolve LP multiplier from the current data-model data count. */
    public double resolveMultiplier() {
        double[] resolved = { 0 };
        MachineUtils.applyContents(this, (content) -> {
            if (content instanceof ItemStack stack && stack.is(Hostile.Items.DATA_MODEL.get())) {
                var count = DataModelItem.getData(stack);
                if (count < 6) resolved[0] = 0;
                else if (count < 48) resolved[0] = 2;
                else if (count < 300) resolved[0] = 3;
                else if (count < 900) resolved[0] = 5;
                else resolved[0] = 3;
            }
        }, ItemRecipeCapability.CAP, IO.IN);
        return resolved[0];
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        multiplier = resolveMultiplier();
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        MachineUtils.applyContents(this, (content) -> {
            if (content instanceof ItemStack stack && stack.is(Hostile.Items.DATA_MODEL.get())) {
                var count = DataModelItem.getData(stack);
                if (count < 54) {
                    DataModelItem.setData(stack, count + 1);
                }
            }
        }, GTRecipeCapabilities.ITEM, IO.IN);
        // Only inject on normal finish (progress reached duration); interruptRecipe also calls afterWorking
        if (soulWillPerRecipe > 0 && isSoulInfusionActive() &&
                getRecipeLogic().getProgress() >= getRecipeLogic().getDuration()) {
            AddWill(soulWillPerRecipe);
        }
        super.afterWorking();
    }

    @Override
    public Widget createUIWidget() {
        var widget = super.createUIWidget();
        var button_soul = (new SwitchWidget(80, 100, 20, 20, (clickData, ispressed) -> {
            if (isConnected()) {
                soul_infusion_mode = ispressed;
            } else {
                soul_infusion_mode = false;
            }
            getRecipeLogic().markLastRecipeDirty();
        })
                .setPressed(soul_infusion_mode)
                .setTexture(
                        new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.ETERNAL_WOS_SOUL_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.ETERNAL_WOS_SOUL_ON))
                .setHoverTooltips(soul_infusion_mode_lang.translate()));
        if (widget instanceof WidgetGroup) {
            ((WidgetGroup) widget).addWidget(button_soul);
        }
        return widget;
    }

    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof EternalWosMachine dmachine) {
            // modifyRecipe runs BEFORE beforeWorking — resolve multiplier here
            dmachine.multiplier = dmachine.resolveMultiplier();
            int mult = (int) dmachine.multiplier;

            boolean soul = dmachine.isSoulInfusionActive();
            int maxParallel = soul ?
                    CTNHManaUtils.getParallelAmountWithFakeOutputCapacity(group, recipe, Integer.MAX_VALUE) :
                    CTNHManaUtils.getParallelAmount(group, recipe, Integer.MAX_VALUE);
            recipe.multiplyOutputs(maxParallel);
            recipe.multiplyTickOutputs(maxParallel);
            recipe.multiplyEUt(maxParallel);
            recipe.parallels = maxParallel;

            // Model-tier LP bonus (same scale as Digital WoS); apply after parallel
            recipe.outputs.multiply(mult);
            recipe.tickOutputs.multiply(mult);

            if (soul) {
                long will = 0;
                List<FluidIngredient> outputContents = recipe.getOutputContents(FluidRecipeCapability.CAP);
                if (!outputContents.isEmpty()) {
                    for (FluidIngredient content : outputContents) {
                        if (content == null) continue;
                        will += Arrays.stream(content.getFluids())
                                .mapToLong(fluidStack -> fluidStack.getAmount())
                                .sum();
                    }
                }
                dmachine.soulWillPerRecipe = will;
                // Strip fluid outputs so matching/IO ignore tank capacity; will is injected in afterWorking
                recipe.outputs.remove(FluidRecipeCapability.CAP);
                recipe.tickOutputs.remove(FluidRecipeCapability.CAP);
            } else {
                dmachine.soulWillPerRecipe = 0;
            }
            return null;
        }
        return null;
    }

    public boolean isConnected() {
        var level = this.getLevel();
        var pos = this.getPos().offset(0, -11, 0);
        return getMachine(level, pos) instanceof HellForgeMachine hmachine && hmachine.isFormed() &&
                hmachine.hatch != null;
    }

    public boolean isSoulInfusionActive() {
        return soul_infusion_mode && isConnected();
    }

    /** Raw LP-equivalent mB stored for the current soul-infusion recipe (before /1e6). */
    public long getSoulWillPerRecipe() {
        return soulWillPerRecipe;
    }

    /** Actual demon will injected into the Hellforge when the current recipe finishes. */
    public double getSoulWillOutput() {
        return soulWillPerRecipe / 666_666.0;
    }

    public void AddWill(Long count) {
        var willcount = count / 666666;
        var level = this.getLevel();
        var pos = this.getPos().offset(0, -11, 0);
        if (getMachine(level, pos) instanceof HellForgeMachine hmachine && hmachine.hatch != null) {
            hmachine.hatch.rawWill = Math.min((double) max_will, hmachine.hatch.rawWill + willcount);
        }
    }

    @CN("注魂模式")
    @EN("Soul Infusion Mode")
    public static Lang soul_infusion_mode_lang;
    @CN({
            "§c折磨,折磨,永恒的苦难在齿轮之中,此处即是阿鼻地狱",
            "具有无限并行,不支持低级模型,使用模型获得额外的LP加成，注意：请配备足够大的输出仓来确保完全并行输出",
            "如果自身和工业地狱锻炉以共享岩浆池的方式(该结构主方块恰好比工业地狱锻炉高11格)连接,可通过按钮开启注魂模式：不再产生LP，而是改为给地狱锻炉供应普通意志",
            "注魂模式下并行计算无视输出仓容量，产出的生命源质将全部转化为意志注入锻炉",
            "这个傻逼结构真让我破防了，我真日了，谁在跟我提这个傻逼机器的bug我就把它删了——ANGRY BEEEEEEEEEEEEEEEEEEEEE"
    })
    @EN({
            "Torment, torment, eternal torment among the gears—this place is hell itself",
            "Has infinite parallel; low-tier models unsupported. Higher model data grants bonus LP. Equip large enough output hatches for full parallel output",
            "When linked to an Industrial Hellforge via a shared lava pool (controller exactly 11 blocks above), use the button to enable Soul Infusion Mode: no LP is produced; Will is supplied to the Hellforge instead",
            "In Soul Infusion Mode, parallel ignores output capacity; all Vital Essence is converted into Will for the Hellforge",
            "这个傻逼结构真让我破防了，我真日了，谁在跟我提这个傻逼机器的bug我就把它删了——ANGRY BEEEEEEEEEEEEEEEEEEEEE"
    })
    public static Lang[] eternalWosLang;
}
