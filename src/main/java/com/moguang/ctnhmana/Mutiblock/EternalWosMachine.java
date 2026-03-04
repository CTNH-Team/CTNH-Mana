package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeCapabilities;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.moguang.ctnhmana.registry.CMGuiTextures;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import static wayoftime.bloodmagic.demonaura.WorldDemonWillHandler.getDimensionResourceLocation;
import static wayoftime.bloodmagic.demonaura.WorldDemonWillHandler.getWillChunk;

public class EternalWosMachine extends WorkableElectricMultiblockMachine {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            EternalWosMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
    public double multiplier = 1;
    public EternalWosMachine(IMachineBlockEntity holder){
        super(holder);
    }
    public String mode="lp";
    @Persisted
    public double will_adder=0;
    @Persisted
    public boolean diffusion_model=false; //NOT DDIM
    // 扩散系数（可调：0<D≤1，值越大扩散越快）
    private static final double DIFFUSION_COEFFICIENT = 0.25;
    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        MachineUtils.applyContents(this, (content) -> {
            var item=(ItemStack) content;
//            if(item.hasTag()&&item.getTag().getCompound("data_model").contains("data"))
//
            var count = ((ItemStack) content).getTag().getCompound("data_model").getInt("data");
            if (count < 6) multiplier = 0;
            else if (count < 48) multiplier = 1;
            else if (count < 300) multiplier = 1.5;
            else if (count < 900) multiplier = 2;
            else multiplier = 3;
        }, ItemRecipeCapability.CAP, IO.IN);
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        MachineUtils.applyContents(this, (content)->{
            var count = ((ItemStack)content).getTag().getCompound("data_model").getInt("data");
            if(count < 54) ((ItemStack)content).getTag().getCompound("data_model").putInt("data",count + 1);
        }, GTRecipeCapabilities.ITEM, IO.IN);
        super.afterWorking();
    }
    @Override
    public Widget createUIWidget() {
        var widget=super.createUIWidget();
        var button_diffusion=(new SwitchWidget(80, 100, 20, 20, (clickData,ispressed)->
        {
            if(getMachine(this.getLevel(),this.getPos()) instanceof HellForgeMachine hmachine)diffusion_model=true;

        })
                .setPressed(diffusion_model)
                .setTexture(new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), CMGuiTextures.ETERNAL_WOS_DIFFUSION_OFF),
                        new GuiTextureGroup(ColorPattern.T_CYAN.rectTexture(),  CMGuiTextures.ETERNAL_WOS_DIFFUSION_ON))
                .setHoverTooltips(demon_diffusion_model.translate())
        );
        if(widget instanceof WidgetGroup group)
        {
            ((WidgetGroup) widget).addWidget(button_diffusion);
        }
        return widget;
    }

    //孩子们recipelogic太好用了，什么叫unsafe听不懂喵
    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object... args) {
        return new EternalWosLogic(this);
    }
    //todo
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe){
        if(machine instanceof EternalWosMachine dmachine) {
            var level = dmachine.self().getLevel();
            var pos= dmachine.self().getPos();
            pos = pos.offset(0,-11,0);
            var maxParallel = ParallelLogic.getParallelAmount(dmachine,recipe,2147483647);
           // ModifierFunction parallel = CTNHRecipeModifiers.accurateParallel(machine,recipe,maxParallel);
            return  ModifierFunction.builder()
                    .parallels(maxParallel)
                    .eutMultiplier(maxParallel)
                    .build();
        }
        return ModifierFunction.IDENTITY;
    }
    public boolean isConnected()
    {
        var level = this.getLevel();
        var pos= this.getPos();
        pos = pos.offset(0,-11,0);
        return (getMachine(level,pos) instanceof HellForgeMachine hmachine&&hmachine.isFormed()&&hmachine.hatch!=null);
    }

    public void AddWill(Long count)
    {
        var willcount=count/1000000;
        var level = this.getLevel();
        var pos= this.getPos();
        pos = pos.offset(0,-11,0);
        if(getMachine(level,pos) instanceof HellForgeMachine hmachine)
        {
            if(!diffusion_model)
            {
                hmachine.hatch.rawWill+=willcount;
            }
            else {
                var willchunk = getWillChunk(level, pos);
                willchunk.getCurrentWill().addWill(EnumDemonWillType.DEFAULT, willcount);
                diffuseWillFromCenter(pos.getX(), pos.getZ(), 1);
            }
        }
    }

    class EternalWosLogic extends RecipeLogic
    {
        public EternalWosLogic(IRecipeLogicMachine machine) {
            super(machine);
        }
        @Override
        protected ActionResult handleRecipeIO(GTRecipe recipe, IO io) {
            if (io == IO.IN) {
                // 输入处理：正常处理
                return super.handleRecipeIO(recipe, io);
            }
            if (io == IO.OUT) {
                if(!isConnected())return super.handleRecipeIO(recipe,io);
                var safe_recipe=lastOriginRecipe;
                List<Content> outputContents = safe_recipe.getOutputContents(FluidRecipeCapability.CAP);
                if (!outputContents.isEmpty()) {
                    for (Content content : outputContents) {
                        var safe_content=content.copy(FluidRecipeCapability.CAP);
                        // 从Content中获取ItemStack
                        FluidIngredient ingredient = FluidRecipeCapability.CAP.of(safe_content.getContent());
                        if (ingredient != null) {
                            long count = Arrays.stream(ingredient.getStacks())
                                    .mapToLong(fluidStack -> fluidStack.getAmount()) // 取每个流体栈的实际数量（mb）
                                    .sum();
                            if(count<1)return ActionResult.PASS_NO_CONTENTS;
                            AddWill(count);
                        }
                    }
                }
                return ActionResult.SUCCESS;
            }

            return super.handleRecipeIO(recipe, io);
        }
    }
    public double[][][]getChunkWill(int x,int y,int range)
    {
        int chunk_x=x;
        int chunk_y=y;
        double[][][] willchunk=new double[100][100][5];
        for(int range_x=(0);range_x<=range*2;range_x++)
        {
            for(int range_y=(0);range_y<=range*2;range_y++)
            {
                var willchunker=getWillChunk(getDimensionResourceLocation(getLevel()),chunk_x+range_x-range,chunk_y+range_y-range).getCurrentWill();
                willchunk[range_x][range_y][0]=willchunker.getWill(EnumDemonWillType.DEFAULT);
                willchunk[range_x][range_y][1]=willchunker.getWill(EnumDemonWillType.CORROSIVE);
                willchunk[range_x][range_y][2]=willchunker.getWill(EnumDemonWillType.DESTRUCTIVE);
                willchunk[range_x][range_y][3]=willchunker.getWill(EnumDemonWillType.STEADFAST);
                willchunk[range_x][range_y][4]=willchunker.getWill(EnumDemonWillType.VENGEFUL);
            }
        }
        return willchunk;
    }
    public void diffuseWillFromCenter(int centerX, int centerY, int range) {
        // 1. 获取扩散前的原始意志数据（基于你最终版的getChunkWill）
        centerX = centerX >> 4;
        centerY = centerY >> 4;
        double[][][] originalWill = getChunkWill(centerX, centerY, range);

        double[] sum_will = new double[5];
        double[]transfer_will=new double[5];
        double[][][] transfer_will_weight=new double[100][100][5];
        double num_transfer_will=0;
        int tranfer_chunk_num = 0;

        for (int type = 0; type <= 4; type++) {
            for (int i = 0; i <= 2*range; i++) {
                for (int j = 0; j <= 2*range; j++) {
                    if (originalWill[i][j][type] < originalWill[range][range][type]) {
                        sum_will[type] += originalWill[i][j][type];
                        tranfer_chunk_num++;
                    }
                }
            }
            sum_will[type]=Math.max(1,sum_will[type]);
            for (int i = 0; i <= 2*range; i++) {
                for (int j = 0; j <=2* range; j++) {
                    if (originalWill[i][j][type] < originalWill[range][range][type]) {

                        transfer_will_weight[i][j][type]=originalWill[i][j][type]/sum_will[type];
                    }
                }
            }
            tranfer_chunk_num=Math.max(1,tranfer_chunk_num);
            transfer_will[type] = originalWill[range][range][type]-(sum_will[type] / tranfer_chunk_num);
            tranfer_chunk_num=0;
        }
        for (int type = 0; type <= 4; type++) {
            for (int i = 0; i <= 2*range; i++) {
                for (int j = 0; j <= 2*range; j++) {
                    if (originalWill[i][j][type] < originalWill[range][range][type]) {
                        double transfer_will_x=(transfer_will_weight[i][j][type])* transfer_will[type]*DIFFUSION_COEFFICIENT;
                        num_transfer_will+=transfer_will_x;
                        originalWill[i][j][type]+=transfer_will_x;
                    }
                }
            }
            originalWill[range][range][type]-=num_transfer_will;
            num_transfer_will=0;
        }
        changeChunkWill(centerX,centerY,range,originalWill);


    }
    public void changeChunkWill(int x,int y,int range,double[][][] willchunk)
    {
        int chunk_x=x;
        int chunk_y=y;
        //double[][][] willchunk=new double[100][100][5];
        for(int range_x=(0);range_x<=range*2;range_x++)
        {
            for(int range_y=(0);range_y<=range*2;range_y++)
            {
                var willchunker=getWillChunk(getDimensionResourceLocation(getLevel()),chunk_x+range_x-range,chunk_y+range_y-range).getCurrentWill();
                willchunker.clearWill();
                willchunker.addWill(EnumDemonWillType.DEFAULT,willchunk[range_x][range_y][0],100);
                willchunker.addWill(EnumDemonWillType.CORROSIVE,willchunk[range_x][range_y][1],100);
                willchunker.addWill(EnumDemonWillType.DESTRUCTIVE,willchunk[range_x][range_y][2],100);
                willchunker.addWill(EnumDemonWillType.STEADFAST,willchunk[range_x][range_y][3],100);
                willchunker.addWill(EnumDemonWillType.VENGEFUL,willchunk[range_x][range_y][4],100);
            }
        }
    }
    @CN("逸散模式")
    public static Lang demon_diffusion_model;

}