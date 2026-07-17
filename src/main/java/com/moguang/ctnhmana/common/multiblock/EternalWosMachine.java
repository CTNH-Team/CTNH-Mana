package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeCapabilities;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import dev.shadowsoffire.hostilenetworks.Hostile;
import dev.shadowsoffire.hostilenetworks.item.DataModelItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import static wayoftime.bloodmagic.demonaura.WorldDemonWillHandler.getDimensionResourceLocation;
import static wayoftime.bloodmagic.demonaura.WorldDemonWillHandler.getWillChunk;

public class EternalWosMachine extends RecipeElectricMultiblockMachine {

    public double multiplier = 1;

    public EternalWosMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    public String mode = "lp";
    @Persisted
    public double will_adder = 0;
    @Persisted
    public boolean diffusion_model = false; // NOT DDIM
    // æ‰©æ•£ç³»æ•°ï¼ˆå¯è°ƒï¼š0<Dâ‰?ï¼Œå€¼è¶Šå¤§æ‰©æ•£è¶Šå¿«ï¼‰
    private static final double DIFFUSION_COEFFICIENT = 0.25;
    public static int max_will = 200;

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        MachineUtils.applyContents(this, (content) -> {
            if(content instanceof ItemStack stack && stack.is(Hostile.Items.DATA_MODEL.get())) {
                var count = DataModelItem.getData(stack);
                if (count < 6) multiplier = 0;
                else if (count < 48) multiplier = 1;
                else if (count < 300) multiplier = 1.5;
                else if (count < 900) multiplier = 2;
                else multiplier = 3;
            }
        }, ItemRecipeCapability.CAP, IO.IN);
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        MachineUtils.applyContents(this, (content) -> {
            if(content instanceof ItemStack stack && stack.is(Hostile.Items.DATA_MODEL.get())) {
                var count = DataModelItem.getData(stack);
                if (count < 54) {
                    DataModelItem.setData(stack, count + 1);
                }
            }
        }, GTRecipeCapabilities.ITEM, IO.IN);
        super.afterWorking();
    }

    @Override
    public Widget createUIWidget() {
        var widget = super.createUIWidget();
        var button_diffusion = (new SwitchWidget(80, 100, 20, 20, (clickData, ispressed) -> {
            if (getMachine(this.getLevel(), this.getPos()) instanceof HellForgeMachine hmachine)
                diffusion_model = ispressed;

        })
                .setPressed(diffusion_model)
                .setTexture(
                        new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.ETERNAL_WOS_DIFFUSION_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(), CMGuiTextures.ETERNAL_WOS_DIFFUSION_ON))
                .setHoverTooltips(demon_diffusion_model.translate()));
        if (widget instanceof WidgetGroup group) {
            ((WidgetGroup) widget).addWidget(button_diffusion);
        }
        return widget;
    }

    // å­©å­ä»¬recipelogicå¤ªå¥½ç”¨äº†ï¼Œä»€ä¹ˆå«unsafeå¬ä¸æ‡‚å–µ
    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object... args) {
        return new EternalWosLogic(this);
    }

    // todo
    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof EternalWosMachine dmachine) {
            var level = dmachine.self().getLevel();
            var pos = dmachine.self().getPos();
            pos = pos.offset(0, -11, 0);
            var maxParallel = ParallelLogic.getParallelAmount(group, recipe, 2147483647);
            // ModifierFunction parallel = CTNHRecipeModifiers.accurateParallel(machine,recipe,maxParallel);
            recipe.multiplyOutputs(maxParallel);
            recipe.multiplyTickOutputs(maxParallel);
            recipe.multiplyEUt(maxParallel);
            recipe.parallels = maxParallel;
            return null;
        }
        return null;
    }

    public boolean isConnected() {
        var level = this.getLevel();
        var pos = this.getPos();
        pos = pos.offset(0, -11, 0);
        return (getMachine(level, pos) instanceof HellForgeMachine hmachine && hmachine.isFormed() &&
                hmachine.hatch != null);
    }

    public void AddWill(Long count) {
        var willcount = count / 1000000;
        var level = this.getLevel();
        var pos = this.getPos();
        pos = pos.offset(0, -11, 0);
        if (getMachine(level, pos) instanceof HellForgeMachine hmachine) {
            if (!diffusion_model) {
                hmachine.hatch.rawWill = Math.min((double) max_will, hmachine.hatch.rawWill + willcount);
            } else {
                var willchunk = getWillChunk(level, pos);
                willchunk.getCurrentWill().addWill(EnumDemonWillType.DEFAULT, willcount, max_will);
                diffuseWillFromCenter(pos.getX(), pos.getZ(), 1);
            }
        }
    }

    class EternalWosLogic extends RecipeLogic {

        public EternalWosLogic(IRecipeLogicMachine machine) {
            super(machine);
        }

        @Override
        protected ActionResult handleRecipeIO(GTRecipe recipe, IO io) {
            if (io == IO.IN) {
                // è¾“å…¥å¤„ç†ï¼šæ­£å¸¸å¤„ç?
                return super.handleRecipeIO(recipe, io);
            }
            if (io == IO.OUT) {
                if (!isConnected()) return super.handleRecipeIO(recipe, io);
                var safe_recipe = lastOriginRecipe;
                List<FluidIngredient> outputContents = safe_recipe.getOutputContents(FluidRecipeCapability.CAP);
                if (!outputContents.isEmpty()) {
                    for (FluidIngredient content : outputContents) {
                        var safe_content = content.copy();
                        // ä»ŽContentä¸­èŽ·å–ItemStack
                        if (safe_content != null) {
                            long count = Arrays.stream(safe_content.getFluids())
                                    .mapToLong(fluidStack -> fluidStack.getAmount()) // å–æ¯ä¸ªæµä½“æ ˆçš„å®žé™…æ•°é‡ï¼ˆmbï¼?
                                    .sum();
                            if (count < 1) return ActionResult.PASS_NO_CONTENTS;
                            AddWill(count);
                        }
                    }
                }
                return ActionResult.SUCCESS;
            }

            return super.handleRecipeIO(recipe, io);
        }
    }

    public double[][][] getChunkWill(int x, int y, int range) {
        int chunk_x = x;
        int chunk_y = y;
        double[][][] willchunk = new double[100][100][5];
        for (int range_x = (0); range_x <= range * 2; range_x++) {
            for (int range_y = (0); range_y <= range * 2; range_y++) {
                var willchunker = getWillChunk(getDimensionResourceLocation(getLevel()), chunk_x + range_x - range,
                        chunk_y + range_y - range).getCurrentWill();
                willchunk[range_x][range_y][0] = willchunker.getWill(EnumDemonWillType.DEFAULT);
                willchunk[range_x][range_y][1] = willchunker.getWill(EnumDemonWillType.CORROSIVE);
                willchunk[range_x][range_y][2] = willchunker.getWill(EnumDemonWillType.DESTRUCTIVE);
                willchunk[range_x][range_y][3] = willchunker.getWill(EnumDemonWillType.STEADFAST);
                willchunk[range_x][range_y][4] = willchunker.getWill(EnumDemonWillType.VENGEFUL);
            }
        }
        return willchunk;
    }

    public void diffuseWillFromCenter(int centerX, int centerY, int range) {
        // 1. èŽ·å–æ‰©æ•£å‰çš„åŽŸå§‹æ„å¿—æ•°æ®ï¼ˆåŸºäºŽä½ æœ€ç»ˆç‰ˆçš„getChunkWillï¼?
        centerX = centerX >> 4;
        centerY = centerY >> 4;
        double[][][] originalWill = getChunkWill(centerX, centerY, range);

        double[] sum_will = new double[5];
        double[] transfer_will = new double[5];
        double[][][] transfer_will_weight = new double[100][100][5];
        double num_transfer_will = 0;
        int tranfer_chunk_num = 0;

        for (int type = 0; type <= 4; type++) {
            for (int i = 0; i <= 2 * range; i++) {
                for (int j = 0; j <= 2 * range; j++) {
                    if (originalWill[i][j][type] < originalWill[range][range][type]) {
                        sum_will[type] += originalWill[i][j][type];
                        tranfer_chunk_num++;
                    }
                }
            }
            sum_will[type] = Math.max(1, sum_will[type]);
            for (int i = 0; i <= 2 * range; i++) {
                for (int j = 0; j <= 2 * range; j++) {
                    if (originalWill[i][j][type] < originalWill[range][range][type]) {

                        transfer_will_weight[i][j][type] = originalWill[i][j][type] / sum_will[type];
                    }
                }
            }
            tranfer_chunk_num = Math.max(1, tranfer_chunk_num);
            transfer_will[type] = originalWill[range][range][type] - (sum_will[type] / tranfer_chunk_num);
            tranfer_chunk_num = 0;
        }
        for (int type = 0; type <= 4; type++) {
            for (int i = 0; i <= 2 * range; i++) {
                for (int j = 0; j <= 2 * range; j++) {
                    if (originalWill[i][j][type] < originalWill[range][range][type]) {
                        double transfer_will_x = (transfer_will_weight[i][j][type]) * transfer_will[type] *
                                DIFFUSION_COEFFICIENT;
                        num_transfer_will += transfer_will_x;
                        originalWill[i][j][type] += transfer_will_x;
                    }
                }
            }
            originalWill[range][range][type] -= num_transfer_will;
            num_transfer_will = 0;
        }
        for (int type = 0; type <= 4; type++) {
            for (int i = 0; i <= 2 * range; i++) {
                for (int j = 0; j <= 2 * range; j++) {
                    originalWill[i][j][type] = Math.min((double) max_will, Math.max(0.0, originalWill[i][j][type]));
                }
            }
        }
        changeChunkWill(centerX, centerY, range, originalWill);
    }

    public void changeChunkWill(int x, int y, int range, double[][][] willchunk) {
        int chunk_x = x;
        int chunk_y = y;
        // double[][][] willchunk=new double[100][100][5];
        for (int range_x = (0); range_x <= range * 2; range_x++) {
            for (int range_y = (0); range_y <= range * 2; range_y++) {
                var willchunker = getWillChunk(getDimensionResourceLocation(getLevel()), chunk_x + range_x - range,
                        chunk_y + range_y - range).getCurrentWill();
                willchunker.clearWill();
                willchunker.addWill(EnumDemonWillType.DEFAULT, willchunk[range_x][range_y][0], max_will);
                willchunker.addWill(EnumDemonWillType.CORROSIVE, willchunk[range_x][range_y][1], max_will);
                willchunker.addWill(EnumDemonWillType.DESTRUCTIVE, willchunk[range_x][range_y][2], max_will);
                willchunker.addWill(EnumDemonWillType.STEADFAST, willchunk[range_x][range_y][3], max_will);
                willchunker.addWill(EnumDemonWillType.VENGEFUL, willchunk[range_x][range_y][4], max_will);
            }
        }
    }

    @CN("é€¸æ•£æ¨¡å¼")
    @EN("Diffusion mode")
    public static Lang demon_diffusion_model;
    @CN({
            "æŠ˜ç£¨,æŠ˜ç£¨,æ°¸æ’çš„æŠ˜ç£¨åœ¨é½¿è½®ä¹‹ä¸­,æ­¤åœ°å³æ˜¯é˜¿é¼»åœ°ç‹±",
            "å…·æœ‰æ— é™å¹¶è¡Œ,ä¸æ”¯æŒä½Žçº§æ¨¡åž?ä½¿ç”¨æ¨¡åž‹èŽ·å¾—é¢å¤–çš„LPåŠ æˆï¼Œæ³¨æ„ï¼šè¯·é…å¤‡è¶³å¤Ÿå¤§çš„è¾“å‡ºä»“æ¥ç¡®ä¿å®Œå…¨å¹¶è¡Œè¾“å‡?",
            "å¦‚æžœè‡ªèº«å’Œå·¥ä¸šåœ°ç‹±é”»ç‚‰ä»¥å…±äº«å²©æµ†æ± çš„æ–¹å¼(è¯¥ç»“æž„ä¸»æ–¹å—æ°å¥½æ¯”å·¥ä¸šåœ°ç‹±é”»ç‚‰é«˜11æ ?è¿žæŽ¥,åˆ™è½¬ä¸ºçµé­‚æ¨¡å¼ï¼šä¸å†äº§ç”ŸLPï¼Œè€Œæ˜¯æ”¹ä¸ºç»™åœ°ç‹±é”»ç‚‰ä¾›åº”æ™®é€šæ„å¿?",
            "åœ¨è¿žæŽ¥æ—¶å¼€å¯é€¸æ•£æ¨¡å¼ï¼Œå°†ä¼šç›´æŽ¥æŠŠç”Ÿäº§çš„æ„å¿—é€¸æ•£ï¼Œå¹¶ä¸”æ‰©æ•£å½“å‰åŒºå—çš„æ„å¿—ï¼Œé€šè¿‡æ­¤äº§ç”Ÿçš„åŒºå—æ„å¿—å¯ä»¥è¶…è¶Š100ï¼Œè¾¾åˆ?00åŒºåŸŸåŒºå—ä¸Šé™"
    })
    @EN({
            "æŠ˜ç£¨,æŠ˜ç£¨,æ°¸æ’çš„æŠ˜ç£¨åœ¨é½¿è½®ä¹‹ä¸­,æ­¤åœ°å³æ˜¯åœ°ç‹±",
            "å…·æœ‰æ— é™å¹¶è¡Œ,ä¸æ”¯æŒä½Žçº§æ¨¡åž?ä½¿ç”¨æ¨¡åž‹èŽ·å¾—é¢å¤–çš„LPåŠ æˆï¼Œæ³¨æ„ï¼šè¯·é…å¤‡è¶³å¤Ÿå¤§çš„è¾“å‡ºä»“æ¥ç¡®ä¿å®Œå…¨å¹¶è¡Œè¾“å‡?",
            "å¦‚æžœè‡ªèº«å’Œå·¥ä¸šåœ°ç‹±é”»ç‚‰ä»¥å…±äº«å²©æµ†æ± çš„æ–¹å¼(è¯¥ç»“æž„ä¸»æ–¹å—æ°å¥½æ¯”å·¥ä¸šåœ°ç‹±é”»ç‚‰é«˜11æ ?è¿žæŽ¥,åˆ™è½¬ä¸ºçµé­‚æ¨¡å¼ï¼šä¸å†äº§ç”ŸLPï¼Œè€Œæ˜¯æ”¹ä¸ºç»™åœ°ç‹±é”»ç‚‰ä¾›åº”æ™®é€šæ„å¿?",
            "åœ¨è¿žæŽ¥æ—¶å¼€å¯é€¸æ•£æ¨¡å¼ï¼Œå°†ä¼šç›´æŽ¥æŠŠç”Ÿäº§çš„æ„å¿—é€¸æ•£ï¼Œå¹¶ä¸”æ‰©æ•£å½“å‰åŒºå—çš„æ„å¿—ï¼Œé€šè¿‡æ­¤äº§ç”Ÿçš„åŒºå—æ„å¿—å¯ä»¥è¶…è¶Š100ï¼Œè¾¾åˆ?00åŒºåŸŸåŒºå—ä¸Šé™"
    })
    public static Lang[] eternalWosLang;
}
