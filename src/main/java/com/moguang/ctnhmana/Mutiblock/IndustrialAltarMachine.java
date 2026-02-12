package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.moguang.ctnhmana.api.mixin.IBloodAltarLogic;
import com.moguang.ctnhmana.api.pattern.CMPredicates;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.item.BloodMagicJade.JadeItem;
import com.moguang.ctnhmana.mixin.bloodmagic.TileAltarAccessor;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import wayoftime.bloodmagic.altar.BloodAltar;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.tile.TileAltar;

import java.util.List;

import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.failureManaLang_NoEnoughLP;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.failureManaLang_NoEnoughMana;

@SuppressWarnings("removal")
public class IndustrialAltarMachine extends MultiPatternMultiblockMachine implements ITieredMachine {
    public IndustrialAltarMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Persisted
    public int altar_tier =0;
    @Persisted
    public int dislocation_rate=100;
    @Persisted
    public float dislocation_modifier=1.0F;
    @Persisted
    public float speed= 1.0F;
    @Persisted
    public int consumption_lp=0;
    @Nullable
    protected TickableSubscription TickSubs;
    public TileAltar tileAltar;
    public BloodAltar altar;
    @Persisted
    public BlockPos altar_pos;
    @Persisted
    public  float CapacityModifier=1.0F;
    @Persisted
    public int chargingFrequency=20;
    @Getter
    @Persisted
    public String Upgrade="None";
    public String Upgrade_name="无";

    @Override
    protected void initializePatterns() {
        super.initializePatterns();
        addPattern(createLevel3Pattern());
        addPattern(createLevel4Pattern());
        // 默认从定义中获取模式（向后兼容）
    }
    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe)
    {
        updateAltarData();
        if(consumeLPIfEnough(consumption_lp))
        {
            return super.beforeWorking(recipe);
        }
        RecipeLogic.putFailureReason(this,recipe,failureManaLang_NoEnoughLP.translate());
        return false;
    }
    @Override
    public boolean onWorking(){
        if(!consumeLPIfEnough(Upgrade.equals("suppression")? (int) (consumption_lp * 0.5) :consumption_lp))
        {
            getRecipeLogic().setProgress(this.getProgress()-1);
            RecipeLogic.putFailureReason(this,this.getRecipeLogic().getLastOriginRecipe().copy(),failureManaLang_NoEnoughLP.translate());
        }
        return super.onWorking();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        var index=getMatchedPatternIndex();
        var world=this.getLevel();
        altar_pos=MachineUtils.getOffset(this,0,1,2);
        if(world.getBlockEntity(altar_pos) instanceof TileAltar new_altar)
        {
            var tier=new_altar.getTier();
            if(tier<2)onStructureInvalid();
            altar_tier =Math.max(tier,index+2);
            this.tileAltar =new_altar;

            if(this.tileAltar instanceof TileAltarAccessor accessor)
            {
                this.altar=accessor.getBloodAltar();

            }
            if(this.altar instanceof IBloodAltarLogic altaraccessor)
            {
                altaraccessor.CM$BroadcastPos(this.getPos());
            }
            if(this.altar==null)
            {
                onStructureInvalid();
            }
            this.altar.checkTier();
            updateAltarData();

        }
        else
        {
            onStructureInvalid();
        }
    }

    public boolean consumeLPIfEnough(int lp)
    {
        if(this.altar==null)return false;
        if(this.altar instanceof IBloodAltarLogic altaraccessor)
        {
            return altaraccessor.CM$ConsumeLPIfEnough(lp);
        }
        return false;
    }

    public void updateAltarData()
    {
        this.speed=1+altar.getConsumptionMultiplier();
        this.dislocation_modifier=tileAltar.getDislocationMultiplier();
        this.dislocation_rate= (int) (20*dislocation_modifier*Math.pow(2, altar_tier));
        this.chargingFrequency=altar.getChargingFrequency();
    }
    @Override
    public void onStructureInvalid()
    {
        super.onStructureInvalid();
        CapacityModifier=1.0F;
        speed=0;
        altar_tier =0;
        dislocation_rate=100;
        dislocation_modifier=1.0F;
    }
    public GTRecipe getBloodRecipe() {
        return GTRecipeBuilder.ofRaw().inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 100)).buildRawRecipe();
    }
    public int calculateSpeed(int tier)
    {
        return this.Upgrade.equals("ephemeral")?(int) (Math.pow(4,tier)):(int) Math.pow(2,tier);
    }
    //////////////////////////////////////
    // ********  Modifier  ********//
    //////////////////////////////////////
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if(machine instanceof IndustrialAltarMachine altarMachine)
        {
            var condition=recipe.conditions.get(0);
            int consume=0;
            int altar_tier=0;
            var true_time=recipe.duration;
            int batch=1;
            double speed=altarMachine.speed;
            if(condition instanceof BloodAltarCondition altarCondition)
            {
                altar_tier=altarCondition.altar_tier;
                consume=altarCondition.consumption_rate;
                var parallel= ParallelLogic.getParallelAmountWithoutEU(machine,recipe,1024);
                var overclock=altarMachine.altar_tier-altar_tier;
                if(overclock>0)
                {
                    consume=(int) (consume*Math.pow(4,overclock));
                    speed*=altarMachine.calculateSpeed(overclock);
                }
                if(recipe.duration/speed<20)
                {
                    batch=Math.min((int)(speed/recipe.duration),parallel);
                    true_time= (int) Math.max(20,recipe.duration/speed*batch);
                }
                altarMachine.consumption_lp=consume;
                return  ModifierFunction.builder()
                        .parallels(batch)
                        .inputModifier(ContentModifier.multiplier(batch))
                        .outputModifier(ContentModifier.multiplier(batch))
                        .durationMultiplier(true_time/ recipe.duration)
                        .build();
            }
            return ModifierFunction.NULL;
        }
        return ModifierFunction.NULL;
    }
    //////////////////////////////////////
    // ********   Subscriptions&Ticks  ********//
    //////////////////////////////////////
    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTick));
        }
    }
    @Override
    public void onUnload() {
        super.onUnload();
        if(TickSubs!=null)
        {
            TickSubs.unsubscribe();
            TickSubs = null;
        }
    }
    public void updateTick()
    {
        TickSubs=subscribeServerTick(TickSubs, this::updateStates);
    }
    //todo
    public void updateStates()
    {
        if(this.isFormed()&&this.altar!=null)
        {
            if(this.getOffsetTimer()%chargingFrequency==0)
            {
//                if(MachineUtils.inputFluid(CMMaterials.Mana.getFluid(10),this ))
//                {
//                    altar.fillMainTank(10);
//                }
                var boosterRecipe = getBloodRecipe();
                var isBoosted = RecipeHelper.matchRecipe(this, boosterRecipe).isSuccess() &&
                        RecipeHelper.handleRecipeIO(this, boosterRecipe, IO.IN, this.recipeLogic.getChanceCaches()).isSuccess();
                if(isBoosted)
                {
                    altar.fillMainTank(dislocation_rate);
                }
//                if(MachineUtils.inputFluid(new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(),dislocation_rate),this)) {
//                    altar.fillMainTank(dislocation_rate);
//                }
            }
            if(this.getOffsetTimer()%100==0)
            {
                var item=tileAltar.getItem(0);
                if(item.getItem() instanceof JadeItem jades)
                {
                    this.Upgrade=jades.getType();
                    this.Upgrade_name=jades.getUpgradeName().translate().getString();
                }
            }
        }
    }
    //////////////////////////////////////
    // ********   Lang  ********//
    //////////////////////////////////////
    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if(this.isFormed&& tileAltar !=null&&altar!=null) {
            textList.add(level_lang.translate(altar_tier));
            textList.add(altar_lang[0].translate(tileAltar.getCapacity()));
            textList.add(altar_lang[1].translate(tileAltar.getCurrentBlood()));
            textList.add(altar_lang[2].translate(speed));
            textList.add(altar_lang[3].translate(dislocation_rate,chargingFrequency*0.05F));
            textList.add(altar_lang[4].translate(Upgrade_name));
//            textList.add(altar_lang[2].translate(tileAltar.getCapacity()));
        }
    }
    @CN("当前等级:%d")
    public static Lang level_lang;
    @CN({
            "祭坛最大容量:%d",
            "当前祭坛LP容量:%d",
            "速度增幅倍率:%d",
            "充能速率：%d lp/ %.2f s",
            "当前升级:%s"
    })
    @EN({
            "祭坛最大容量:%d",
            "当前祭坛LP容量:%d",
            "LP消耗倍率:%d",
            "充能速率：%d lp/ %.2f s",
            "当前升级:%s"
    })
    public static Lang[] altar_lang;
    //////////////////////////////////////
    // ********   PatternBuilder  ********//
    //////////////////////////////////////
    @Override
    public String getMatchedPatternName() {
        switch (getMatchedPatternIndex()) {
            case 0:
                return "Level 2 Structure";
            case 1:
                return "Level 3 Structure";
            case 2:
                return "Level 4 Structure";
            case 3:
                return "Level 5 Structure";
            default:
                return "No Pattern Matched";
        }
    }

    private BlockPattern createLevel3Pattern() {
        return FactoryBlockPattern.start()
                .aisle("ABBBCBBBA", "DE#####ED", "D#######D", "F#######F", "#########", "#########", "#########", "#########", "#########", "#########")
                .aisle("B#######B", "EBGGGGGBE", "#H#####H#", "#H#####H#", "#I#####I#", "#########", "#########", "#########", "#########", "#########")
                .aisle("B#######B", "#GJJKJJG#", "##LMNML##", "##E###E##", "##E###E##", "##E###E##", "##EEEEE##", "####E####", "#########", "#########")
                .aisle("B#######B", "#GJ###JG#", "##MGGGM##", "#########", "#########", "#########", "##E###E##", "####E####", "####E####", "#########")
                .aisle("C#######C", "#GA###KG#", "##NGGGO##", "####P####", "#########", "#########", "##E###E##", "##EELEE##", "###EEE###", "####Q####")
                .aisle("B#######B", "#GJ###JG#", "##MGGGM##", "#########", "#########", "#########", "##E###E##", "####E####", "####E####", "#########")
                .aisle("B#######B", "#GJJKJJG#", "##LMNML##", "##E###E##", "##E###E##", "##E###E##", "##EEEEE##", "####E####", "#########", "#########")
                .aisle("B#######B", "EBGGGGGBE", "#H#####H#", "#H#####H#", "#I#####I#", "#########", "#########", "#########", "#########", "#########")
                .aisle("ABBBCBBBA", "DE#####ED", "D#######D", "F#######F", "#########", "#########", "#########", "#########", "#########", "#########")
                .where("B", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick1"))))
                .where("L", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:sea_lantern"))))
                .where("J", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:obsidiantilepath"))))
                .where("G", CMPredicates.BMRuneBlocks)
                .where("C", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:duskritualstone"))))
                .where("D", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("M", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:blankrune")))
                        .or(Predicates.autoAbilities(this.getRecipeTypes())))

                .where("O", Predicates.controller(Predicates.blocks(this.getDefinition().get())))
                .where("Q", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:beacon"))))
                .where("#", Predicates.any())
                .where("A", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:purple_lamp"))))
                .where("I", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:glowstone"))))
                .where("P", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:altar"))))
                .where("E", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick_wall"))))
                .where("N", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:prismarine_bricks"))))
                .where("K", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:red_lamp"))))
                .where("F", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:crying_obsidian"))))
                .where("H", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:stone_bricks"))))
                .build();
    }

    private BlockPattern createLevel4Pattern() {
        return FactoryBlockPattern.start()
                .aisle("ABCCCCDCCCCBA", "E###########E", "E###########E", "E###########E", "E###########E", "F###########F", "#############", "#############", "#############", "#############", "#############")
                .aisle("B###########B", "#GHIIIIIIIHG#", "#J#########J#", "#J#########J#", "#J#########J#", "#J#########J#", "#K#########K#", "#############", "#############", "#############", "#############")
                .aisle("C###########C", "#HLMMMNMMMLH#", "##OP#####PO##", "##O#######O##", "##Q#######Q##", "#############", "#############", "#############", "#############", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "##PMIIIIIMP##", "###J#####J###", "###J#####J###", "###R#####R###", "#############", "#############", "#############", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "###ISSGSSI###", "####TUVUT####", "####P###P####", "####P###P####", "####P###P####", "####PPPPP####", "######P######", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "###IS###SI###", "####UIIIU####", "#############", "#############", "#############", "####P###P####", "######P######", "######P######", "#############")
                .aisle("D###########D", "#IN#######NI#", "###IG###GI###", "####VIIIW####", "######X######", "#############", "######Y######", "####P#Z#P####", "####PPTPP####", "#####PPP#####", "######a######")
                .aisle("C###########C", "#IM#######MI#", "###IS###SI###", "####UIIIU####", "#############", "#############", "#############", "####P###P####", "######P######", "######P######", "#############")
                .aisle("C###########C", "#IM#######MI#", "###ISSLSSI###", "####TUVUT####", "####P###P####", "####P###P####", "####P###P####", "####PPPPP####", "######P######", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "##PMIIIIIMP##", "###J#####J###", "###J#####J###", "###R#####R###", "#############", "#############", "#############", "#############", "#############")
                .aisle("C###########C", "#HLMMMNMMMLH#", "##OP#####PO##", "##O#######O##", "##Q#######Q##", "#############", "#############", "#############", "#############", "#############", "#############")
                .aisle("B###########B", "#GHIIIIIIIHG#", "#J#########J#", "#J#########J#", "#J#########J#", "#J#########J#", "#K#########K#", "#############", "#############", "#############", "#############")
                .aisle("ABCCCCDCCCCBA", "E###########E", "E###########E", "E###########E", "E###########E", "F###########F", "#############", "#############", "#############", "#############", "#############")
                .where("K", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:largebloodstonebrick"))))
                .where("S", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:obsidiantilepath"))))
                .where("I", CMPredicates.BMRuneBlocks)
                .where("O", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("H", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:airritualstone"))))
                .where("U", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:blankrune")))
                        .or(Predicates.autoAbilities(this.getRecipeTypes())))
                .where("a", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:beacon"))))
                .where("#", Predicates.any())
                .where("D", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:teleposer"))))
                .where("R", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:glowstone"))))
                .where("P", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick_wall"))))
                .where("A", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:chiseled_end_stone_bricks"))))
                .where("J", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:stone_bricks"))))
                .where("M", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick1"))))
                .where("T", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:sea_lantern"))))
                .where("B", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:waterritualstone"))))
                .where("N", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:duskritualstone"))))
                .where("Y", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:soul_lantern"))))
                .where("F", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:void_lantern_block"))))
                .where("W", Predicates.controller(Predicates.blocks(this.getDefinition().get())))
                .where("Z", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:chain"))))
                .where("L", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:purple_lamp"))))
                .where("X", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:altar"))))
                .where("V", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:prismarine_bricks"))))
                .where("G", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:red_lamp"))))
                .where("Q", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:crying_obsidian"))))
                .where("C", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:bloodstonebrick"))))
                .where("E", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:end_stone_pillar"))))
                .build();
    }
    private BlockPattern createLevel5Pattern() {
        return FactoryBlockPattern.start()
                .aisle("ABCDDDDDEDDDDDCBA", "AF#############FA", "A###############A", "A###############A", "A###############A", "A###############A", "A###############A", "#################", "GGG###########GGG", "#################", "#################", "#################")
                .aisle("B###############B", "FBHIIIIIIIIIIIHBF", "#G#############G#", "#G#############G#", "#G#############G#", "#G#############G#", "#G#############G#", "#G#############G#", "GJG###########GJG", "#################", "#################", "#################")
                .aisle("C###############C", "#HKLMMMMNMMMMLKH#", "##O###########O##", "##O###########O##", "##O###########O##", "##O###########O##", "##P###########P##", "#################", "GGG###########GGG", "#################", "#################", "#################")
                .aisle("D###############D", "#IL###########LI#", "###QRIIIIIIIRQ###", "###S#########S###", "###S#########S###", "###S#########S###", "###S#########S###", "###T#########T###", "#################", "#################", "#################", "#################")
                .aisle("D###############D", "#IM###########MI#", "###RUVVVWVVVUR###", "####XY#####YX####", "####X#######X####", "####Z#######Z####", "#################", "#################", "#################", "#################", "#################", "#################")
                .aisle("D###############D", "#IM###########MI#", "###IV#######VI###", "####YVIIIIIVY####", "#####S#####S#####", "#####S#####S#####", "#####a#####a#####", "#################", "#################", "#################", "#################", "#################")
                .aisle("D###############D", "#IM###########MI#", "###IV#######VI###", "#####IbbQbbI#####", "######cdedc######", "######Y###Y######", "######Y###Y######", "######Y###Y######", "######YYYYY######", "########Y########", "#################", "#################")
                .aisle("D###############D", "#IM###########MI#", "###IV#######VI###", "#####Ib###bI#####", "######dIIId######", "#################", "#################", "#################", "######Y###Y######", "########Y########", "########Y########", "#################")
                .aisle("E###############E", "#IN###########NI#", "###IW#######WI###", "#####IQ###QI#####", "######fIIIf######", "########g########", "#################", "########h########", "######Y#i#Y######", "######YYcYY######", "#######YYY#######", "########j########")
                .aisle("D###############D", "#IM###########MI#", "###IV#######VI###", "#####Ib###bI#####", "######dIIId######", "#################", "#################", "#################", "######Y###Y######", "########Y########", "########Y########", "#################")
                .aisle("D###############D", "#IM###########MI#", "###IV#######VI###", "#####IbbUbbI#####", "######cdfdc######", "######Y###Y######", "######Y###Y######", "######Y###Y######", "######YYYYY######", "########Y########", "#################", "#################")
                .aisle("D###############D", "#IM###########MI#", "###IV#######VI###", "####YVIIIIIVY####", "#####S#####S#####", "#####S#####S#####", "#####a#####a#####", "#################", "#################", "#################", "#################", "#################")
                .aisle("D###############D", "#IM###########MI#", "###RUVVVWVVVUR###", "####XY#####YX####", "####X#######X####", "####Z#######Z####", "#################", "#################", "#################", "#################", "#################", "#################")
                .aisle("D###############D", "#IL###########LI#", "###QRIIIIIIIRQ###", "###S#########S###", "###S#########S###", "###S#########S###", "###S#########S###", "###T#########T###", "#################", "#################", "#################", "#################")
                .aisle("C###############C", "#HKLMMMMNMMMMLKH#", "##O###########O##", "##O###########O##", "##O###########O##", "##O###########O##", "##P###########P##", "#################", "GGG###########GGG", "#################", "#################", "#################")
                .aisle("B###############B", "FBHIIIIIIIIIIIHBF", "#G#############G#", "#G#############G#", "#G#############G#", "#G#############G#", "#G#############G#", "#G#############G#", "GJG###########GJG", "#################", "#################", "#################")
                .aisle("ABCDDDDDEDDDDDCBA", "AF#############FA", "A###############A", "A###############A", "A###############A", "A###############A", "A###############A", "#################", "GGG###########GGG", "#################", "#################", "#################")
                .aisle("#################", "#################", "#################", "#################", "#################", "#################", "#################", "#################", "#################", "#################", "#################", "#################")
                .where("T", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:largebloodstonebrick"))))
                .where("b", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:obsidiantilepath"))))
                .where("I", CMPredicates.BMRuneBlocks)
                .where("C", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:sculk_catalyst"))))
                .where("A", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:reinforced_deepslate"))))
                .where("B", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_metal"))))
                .where("R", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:airritualstone"))))
                .where("d", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:blankrune")))
                        .or(Predicates.autoAbilities(this.getRecipeTypes())))
                .where("j", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:beacon"))))
                .where("F", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:mechanical_fusion_anvil"))))
                .where("#", Predicates.any())
                .where("X", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("N", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:teleposer"))))
                .where("a", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:glowstone"))))
                .where("Y", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick_wall"))))
                .where("J", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:obsidian"))))
                .where("K", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:chiseled_end_stone_bricks"))))
                .where("S", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:stone_bricks"))))
                .where("E", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:witherite_block"))))
                .where("V", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick1"))))
                .where("c", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:sea_lantern"))))
                .where("L", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:waterritualstone"))))
                .where("W", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:duskritualstone"))))
                .where("h", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:soul_lantern"))))
                .where("P", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:void_lantern_block"))))
                .where("G", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ctnhcore:blood_casing"))))
                .where("e", Predicates.controller(Predicates.blocks(this.getDefinition().get())))
                .where("D", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:sculk"))))
                .where("i", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:chain"))))
                .where("U", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:purple_lamp"))))
                .where("g", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:tileAltar"))))
                .where("H", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:fireritualstone"))))
                .where("f", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:prismarine_bricks"))))
                .where("Q", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:red_lamp"))))
                .where("Z", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:crying_obsidian"))))
                .where("M", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:bloodstonebrick"))))
                .where("O", Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:end_stone_pillar"))))
                .build();
    }
//    public MultiblockShapeInfo tier2_build = MultiblockShapeInfo.builder()
//            .aisle("ABBBCBBBA", "DE#####ED", "D#######D", "F#######F", "#########", "#########", "#########", "#########", "#########", "#########")
//            .aisle("B#######B", "EBGGGGGBE", "#H#####H#", "#H#####H#", "#I#####I#", "#########", "#########", "#########", "#########", "#########")
//            .aisle("B#######B", "#GJJKJJG#", "##LMNML##", "##E###E##", "##E###E##", "##E###E##", "##EEEEE##", "####E####", "#########", "#########")
//            .aisle("B#######B", "#GJ###JG#", "##MGGGM##", "#########", "#########", "#########", "##E###E##", "####E####", "####E####", "#########")
//            .aisle("C#######C", "#GA###KG#", "##NGGGO##", "####P####", "#########", "#########", "##E###E##", "##EELEE##", "###EEE###", "####Q####")
//            .aisle("B#######B", "#GJ###JG#", "##MGGGM##", "#########", "#########", "#########", "##E###E##", "####E####", "####E####", "#########")
//            .aisle("B#######B", "#GJJKJJG#", "##LMNML##", "##E###E##", "##E###E##", "##E###E##", "##EEEEE##", "####E####", "#########", "#########")
//            .aisle("B#######B", "EBGGGGGBE", "#H#####H#", "#H#####H#", "#I#####I#", "#########", "#########", "#########", "#########")
//            .aisle("ABBBCBBBA", "DE#####ED", "D#######D", "F#######F", "#########", "#########", "#########", "#########", "#########", "#########")
//            // 完全参照B的写法：移除blocks()，直接传入Block实例
//            .where('B', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick1")))
//            .where('L', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:sea_lantern")))
//            .where('J', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:obsidiantilepath")))
//            .where('G', BLANK_RUNE.get().defaultBlockState())
//            .where('C', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:duskritualstone")))
//            .where('D', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks")))
//            .where('M', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:blankrune")))
//            .where('O', this.getDefinition(),Direction.EAST) // controller需保留，内部直接传Block
//            .where('Q', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:beacon")))
//            .where('#',  Blocks.AIR.defaultBlockState()) // 非方块类型，保留原逻辑
//            .where('A', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:purple_lamp")))
//            .where('I', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:glowstone")))
//            .where('P', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:tileAltar")))
//            .where('E', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick_wall")))
//            .where('N', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:prismarine_bricks")))
//            .where('K', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:red_lamp")))
//            .where('F', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:crying_obsidian")))
//            .where('H', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:stone_bricks"))).build();
//    MultiblockShapeInfo tier3_build = MultiblockShapeInfo.builder()
//            .aisle("ABBBCBBBA", "DE#####ED", "D#######D", "F#######F", "#########", "#########", "#########", "#########", "#########", "#########")
//            .aisle("B#######B", "EBGGGGGBE", "#H#####H#", "#H#####H#", "#I#####I#", "#########", "#########", "#########", "#########", "#########")
//            .aisle("B#######B", "#GJJKJJG#", "##LMNML##", "##E###E##", "##E###E##", "##E###E##", "##EEEEE##", "####E####", "#########", "#########")
//            .aisle("B#######B", "#GJ###JG#", "##MGGGM##", "#########", "#########", "#########", "##E###E##", "####E####", "####E####", "#########")
//            .aisle("C#######C", "#GA###KG#", "##NGGGO##", "####P####", "#########", "#########", "##E###E##", "##EELEE##", "###EEE###", "####Q####")
//            .aisle("B#######B", "#GJ###JG#", "##MGGGM##", "#########", "#########", "#########", "##E###E##", "####E####", "####E####", "#########")
//            .aisle("B#######B", "#GJJKJJG#", "##LMNML##", "##E###E##", "##E###E##", "##E###E##", "##EEEEE##", "####E####", "#########", "#########")
//            .aisle("B#######B", "EBGGGGGBE", "#H#####H#", "#H#####H#", "#I#####I#", "#########", "#########", "#########", "#########", "#########")
//            .aisle("ABBBCBBBA", "DE#####ED", "D#######D", "F#######F", "#########", "#########", "#########", "#########", "#########", "#########")
//            // 统一格式：单引号 + 直接传Block实例 + 特殊节点按指定写法调整
//            .where('B', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick1")))
//            .where('L', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:sea_lantern")))
//            .where('J', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:obsidiantilepath")))
//            .where('G', BLANK_RUNE.get().defaultBlockState()) // 替换原CMPredicates.BMRuneBlocks
//            .where('C', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:duskritualstone")))
//            .where('D', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks")))
//            .where('M', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:blankrune")))
//            .where('O', this.getDefinition(), Direction.EAST) // 简化controller写法
//            .where('Q', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:beacon")))
//            .where('#', Blocks.AIR.defaultBlockState()) // 替换原Predicates.any()
//            .where('A', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:purple_lamp")))
//            .where('I', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:glowstone")))
//            .where('P', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:tileAltar")))
//            .where('E', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick_wall")))
//            .where('N', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:prismarine_bricks")))
//            .where('K', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:red_lamp")))
//            .where('F', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:crying_obsidian")))
//            .where('H', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:stone_bricks")))
//            .build(); // 补充build()方法完成构建
//    MultiblockShapeInfo tier4_build = MultiblockShapeInfo.builder()
//            .aisle("ABCCCCDCCCCBA", "E###########E", "E###########E", "E###########E", "E###########E", "F###########F", "#############", "#############", "#############", "#############", "#############")
//            .aisle("B###########B", "#GHIIIIIIIHG#", "#J#########J#", "#J#########J#", "#J#########J#", "#J#########J#", "#K#########K#", "#############", "#############", "#############", "#############")
//            .aisle("C###########C", "#HLMMMNMMMLH#", "##OP#####PO##", "##O#######O##", "##Q#######Q##", "#############", "#############", "#############", "#############", "#############", "#############")
//            .aisle("C###########C", "#IM#######MI#", "##PMIIIIIMP##", "###J#####J###", "###J#####J###", "###R#####R###", "#############", "#############", "#############", "#############", "#############")
//            .aisle("C###########C", "#IM#######MI#", "###ISSGSSI###", "####TUVUT####", "####P###P####", "####P###P####", "####P###P####", "####PPPPP####", "######P######", "#############", "#############")
//            .aisle("C###########C", "#IM#######MI#", "###IS###SI###", "####UIIIU####", "#############", "#############", "#############", "####P###P####", "######P######", "######P######", "#############")
//            .aisle("D###########D", "#IN#######NI#", "###IG###GI###", "####VIIIW####", "######X######", "#############", "######Y######", "####P#Z#P####", "####PPTPP####", "#####PPP#####", "######a######")
//            .aisle("C###########C", "#IM#######MI#", "###IS###SI###", "####UIIIU####", "#############", "#############", "#############", "####P###P####", "######P######", "######P######", "#############")
//            .aisle("C###########C", "#IM#######MI#", "###ISSLSSI###", "####TUVUT####", "####P###P####", "####P###P####", "####P###P####", "####PPPPP####", "######P######", "#############", "#############")
//            .aisle("C###########C", "#IM#######MI#", "##PMIIIIIMP##", "###J#####J###", "###J#####J###", "###R#####R###", "#############", "#############", "#############", "#############", "#############")
//            .aisle("C###########C", "#HLMMMNMMMLH#", "##OP#####PO##", "##O#######O##", "##Q#######Q##", "#############", "#############", "#############", "#############", "#############", "#############")
//            .aisle("B###########B", "#GHIIIIIIIHG#", "#J#########J#", "#J#########J#", "#J#########J#", "#J#########J#", "#K#########K#", "#############", "#############", "#############", "#############")
//            .aisle("ABCCCCDCCCCBA", "E###########E", "E###########E", "E###########E", "E###########E", "F###########F", "#############", "#############", "#############", "#############", "#############")
//            .where('K', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:largebloodstonebrick")))
//            .where('S', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:obsidiantilepath")))
//            .where('I', BLANK_RUNE.get().defaultBlockState()) // 替换原CMPredicates.BMRuneBlocks
//            .where('O', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks")))
//            .where('H', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:airritualstone")))
//            .where('U', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:blankrune")))
//            .where('a', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:beacon")))
//            .where('#', Blocks.AIR.defaultBlockState()) // 替换原Predicates.any()
//            .where('D', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:teleposer")))
//            .where('R', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:glowstone")))
//            .where('P', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick_wall")))
//            .where('A', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:chiseled_end_stone_bricks")))
//            .where('J', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:stone_bricks")))
//            .where('M', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_brick1")))
//            .where('T', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:sea_lantern")))
//            .where('B', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:waterritualstone")))
//            .where('N', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:duskritualstone")))
//            .where('Y', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:soul_lantern")))
//            .where('F', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:void_lantern_block")))
//            .where('W', this.getDefinition(), Direction.EAST) // 简化controller写法
//            .where('Z', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:chain")))
//            .where('L', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:purple_lamp")))
//            .where('X', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:tileAltar")))
//            .where('V', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:prismarine_bricks")))
//            .where('G', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("gtceu:red_lamp")))
//            .where('Q', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:crying_obsidian")))
//            .where('C', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:bloodstonebrick")))
//            .where('E', ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:end_stone_pillar")))
//            .build();




}