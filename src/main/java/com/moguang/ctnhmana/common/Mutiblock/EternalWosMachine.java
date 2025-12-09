package com.moguang.ctnhmana.common.Mutiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeCapabilities;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

import javax.annotation.Nullable;

import static wayoftime.bloodmagic.demonaura.WorldDemonWillHandler.getDimensionResourceLocation;
import static wayoftime.bloodmagic.demonaura.WorldDemonWillHandler.getWillChunk;

public class EternalWosMachine extends WorkableElectricMultiblockMachine {
    public double multiplier = 1;
    public EternalWosMachine(IMachineBlockEntity holder){
        super(holder);
    }
    public String mode="lp";
    // 扩散系数（可调：0<D≤1，值越大扩散越快）
    private static final double DIFFUSION_COEFFICIENT = 0.15;
    // 单次最大扩散量（防止浓度突变）
    private static final double MAX_DIFF_PER_STEP = 5;
    // 新增常量（建议定义在类中，可根据需求调整）
    // 中心每次逸散的意志比例（比如10%）
    private static final double EMIT_RATIO = 0.1;
    // 距离衰减系数（距离每增加1，接收量衰减比例）
    private static final double DISTANCE_ATTENUATION = 0.2;
    // 最小接收量（避免远距离区块接收为0）
    private static final double MIN_EMIT_PER_BLOCK = 0.01;
    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        MachineUtils.applyContents(this, (content) -> {
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
    //todo
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe){
        if(machine instanceof EternalWosMachine dmachine) {
            var level = dmachine.self().getLevel();
            var pos= dmachine.self().getPos();
            pos = pos.offset(0,-11,0);
            var maxParallel = ParallelLogic.getParallelAmount(dmachine,recipe,2147483647);
           // ModifierFunction parallel = CTNHRecipeModifiers.accurateParallel(machine,recipe,maxParallel);
            if(getMachine(level,pos) instanceof HellForgeMachine hmachine){
                var outputAmount=((FluidIngredient)(recipe.getOutputContents(GTRecipeCapabilities.FLUID).get(0).content)).getAmount();
                hmachine.will = hmachine.will + maxParallel * outputAmount * dmachine.multiplier/100000;
                dmachine.multiplier=0;
            }

            return ModifierFunction.builder().build();
        }
        return ModifierFunction.IDENTITY;
    }
    public void SpreadingWill()
    {
        var pos=this.getPos();
        var level=this.getLevel();
        var x=pos.getX();
        var y=pos.getY();
        LevelChunk chunk = level.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
    }
    public double[][][]getChunkWill(int x,int y,int range)
    {
        int chunk_x=x>>4;
        int chunk_y=y>>4;
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
    public double[][][] diffuseWillFromCenter(int centerX, int centerY, int range) {
        // 1. 获取扩散前的原始意志数据（基于你最终版的getChunkWill）
        centerX=centerX>>4;
        centerY=centerY>>4;
        double[][][] originalWill = getChunkWill(centerX, centerY, range);
        // 初始化扩散后的数组（与原始数组尺寸一致）
        double[][][] diffusedWill = new double[originalWill.length][originalWill[0].length][5];
        // 存储每个周边区块到中心的距离（曼哈顿距离，适合区块级逸散）
        int[][] distanceToCenter = new int[range * 2 + 1][range * 2 + 1];
        int centerRangeX = range; // 扩散范围数组的中心索引（0~2r的中间值）
        int centerRangeY = range;

        for (int rangeX = 0; rangeX <= range * 2; rangeX++) {
            for (int rangeY = 0; rangeY <= range * 2; rangeY++) {
                // 曼哈顿距离：|x差| + |y差|（中心索引是range，所以差值为rangeX - range）
                distanceToCenter[rangeX][rangeY] =
                        Math.abs(rangeX - centerRangeX) + Math.abs(rangeY - centerRangeY);
            }
        }

        // 2. 四邻域方向（上下左右，区块级扩散）
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // 3. 遍历每个区块的意志数据（按扩散范围遍历）
        for (int rangeX = 0; rangeX <= range * 2; rangeX++) {
            for (int rangeY = 0; rangeY <= range * 2; rangeY++) {
                // 3.1 获取当前区块的5种意志浓度
                double[] currentWill = originalWill[rangeX][rangeY];
                // 存储邻域意志浓度差总和（按意志类型分别计算）
                double[] neighborDiffSum = new double[5];
                // 有效邻域数量（避免边缘区块扩散过度）
                int validNeighborCount = 0;

                // 3.2 遍历四邻域，计算浓度差
                for (int[] dir : directions) {
                    int neighborX = rangeX + dir[0];
                    int neighborY = rangeY + dir[1];
                    // 检查邻域是否在扩散范围内（避免数组越界）
                    if (neighborX >= 0 && neighborX <= range * 2
                            && neighborY >= 0 && neighborY <= range * 2) {
                        // 获取邻域的意志数据
                        double[] neighborWill = originalWill[neighborX][neighborY];
                        // 按类型计算浓度差（邻域 - 当前 → 高浓度向低浓度扩散）
                        for (int type = 0; type < 5; type++) {
                            neighborDiffSum[type] += (neighborWill[type] - currentWill[type]);
                        }
                        validNeighborCount++;
                    }
                }

                // 3.3 计算扩散后的意志浓度（核心公式）
                for (int type = 0; type < 5; type++) {
                    if (validNeighborCount > 0) {
                        // 菲克第一定律适配：ΔC = D × (Σ浓度差 / 有效邻域数)
                        double delta = DIFFUSION_COEFFICIENT * neighborDiffSum[type] / validNeighborCount;
                        // 限制单次最大扩散量，避免数值突变
                        delta = Math.max(-MAX_DIFF_PER_STEP, Math.min(delta, MAX_DIFF_PER_STEP));
                        // 扩散后浓度 = 原始浓度 + 变化量（保证非负）
                        diffusedWill[rangeX][rangeY][type] = Math.max(0, currentWill[type] + delta);
                    } else {
                        // 孤立区块（无邻域），浓度不变
                        diffusedWill[rangeX][rangeY][type] = currentWill[type];
                    }
                }
            }
        }
        for(int range_x=(0);range_x<=range*2;range_x++)
        {
            for(int range_y=(0);range_y<=range*2;range_y++)
            {
                var willchunker=getWillChunk(getDimensionResourceLocation(getLevel()),centerX+range_x-range,centerY+range_y-range).getCurrentWill();

                willchunker.addWill(EnumDemonWillType.DEFAULT,diffusedWill[range_x][range_y][0]);
                willchunker.addWill(EnumDemonWillType.CORROSIVE, diffusedWill[range_x][range_y][1]);
                willchunker.addWill(EnumDemonWillType.DESTRUCTIVE, diffusedWill[range_x][range_y][2]);
                willchunker.addWill(EnumDemonWillType.STEADFAST, diffusedWill[range_x][range_y][3]);
                willchunker.addWill(EnumDemonWillType.VENGEFUL, diffusedWill[range_x][range_y][4]);
            }
        }


        return diffusedWill;
    }
}
