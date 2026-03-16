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
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.api.mixin.IBloodAltarLogic;
import com.moguang.ctnhmana.api.pattern.CMPredicates;
import com.moguang.ctnhmana.common.recipe.BloodAltarCondition;
import com.moguang.ctnhmana.item.BloodMagicJade.JadeItem;
import com.moguang.ctnhmana.mixin.bloodmagic.TileAltarAccessor;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;
import wayoftime.bloodmagic.altar.BloodAltar;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.tile.TileAltar;

import java.util.List;

import static com.gregtechceu.gtceu.api.pattern.Predicates.autoAbilities;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.failureManaLang_NoEnoughLP;

@SuppressWarnings("removal")
public class IndustrialAltarMachine extends MultiPatternMultiblockMachine implements ITieredMachine {

    public IndustrialAltarMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            IndustrialAltarMachine.class, MultiPatternMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    public int altar_tier = 0;
    @Persisted
    public int dislocation_rate = 100;
    @Persisted
    public float dislocation_modifier = 1.0F;
    @Persisted
    public float speed = 1.0F;
    @Persisted
    public int consumption_lp = 0;
    @Nullable
    protected TickableSubscription TickSubs;
    public TileAltar tileAltar;
    public BloodAltar altar;
    @Persisted
    public BlockPos altar_pos;
    @Persisted
    public float CapacityModifier = 1.0F;
    @Persisted
    public int chargingFrequency = 20;
    @Getter
    @Persisted
    public String Upgrade = "None";
    public String Upgrade_name = "无";

    @Override
    protected void initializePatterns() {
        super.initializePatterns();
        addPattern(createLevel3Pattern());
        addPattern(createLevel4Pattern());
        addPattern(createLevel5Pattern());
        addPattern(createLevel6Pattern());
        // 默认从定义中获取模式（向后兼容）
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        updateAltarData();
        if (consumeLPIfEnough(consumption_lp)) {
            return super.beforeWorking(recipe);
        }
        RecipeLogic.putFailureReason(this, recipe, failureManaLang_NoEnoughLP.translate());
        return false;
    }

    @Override
    public boolean onWorking() {
        if (!consumeLPIfEnough(Upgrade.equals("suppression") ? (int) (consumption_lp * 0.5) : consumption_lp)) {
            getRecipeLogic().setProgress(this.getProgress() - 1);
            RecipeLogic.putFailureReason(this, this.getRecipeLogic().getLastOriginRecipe().copy(),
                    failureManaLang_NoEnoughLP.translate());
        }
        return super.onWorking();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        var index = getMatchedPatternIndex();
        var world = this.getLevel();
        altar_pos = MachineUtils.getOffset(this, 0, 1, 2);
        if (world.getBlockEntity(altar_pos) instanceof TileAltar new_altar) {
            var tier = new_altar.getTier();
            if (tier < 2) onStructureInvalid();
            altar_tier = Math.max(tier, index + 2);
            this.tileAltar = new_altar;

            if (this.tileAltar instanceof TileAltarAccessor accessor) {
                this.altar = accessor.getBloodAltar();

            }
            if (this.altar instanceof IBloodAltarLogic altaraccessor) {
                altaraccessor.CM$BroadcastPos(this.getPos());
            }
            if (this.altar == null) {
                onStructureInvalid();
            }
            this.altar.checkTier();
            updateAltarData();

        } else {
            onStructureInvalid();
        }
    }

    public boolean consumeLPIfEnough(int lp) {
        if (this.altar == null) return false;
        if (this.altar instanceof IBloodAltarLogic altaraccessor) {
            return altaraccessor.CM$ConsumeLPIfEnough(lp);
        }
        return false;
    }

    public void updateAltarData() {
        this.speed = 1 + altar.getConsumptionMultiplier();
        this.dislocation_modifier = tileAltar.getDislocationMultiplier();
        this.dislocation_rate = (int) (20 * dislocation_modifier * Math.pow(2, altar_tier));
        this.chargingFrequency = altar.getChargingFrequency();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        CapacityModifier = 1.0F;
        speed = 0;
        altar_tier = 0;
        dislocation_rate = 100;
        dislocation_modifier = 1.0F;
    }

    public GTRecipe getBloodRecipe() {
        return GTRecipeBuilder.ofRaw().inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 100))
                .buildRawRecipe();
    }

    public int calculateSpeed(int tier) {
        return this.Upgrade.equals("ephemeral") ? (int) (Math.pow(4, tier)) : (int) Math.pow(2, tier);
    }

    //////////////////////////////////////
    // ******** Modifier ********//
    //////////////////////////////////////
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (machine instanceof IndustrialAltarMachine altarMachine) {
            var condition = recipe.conditions.get(0);
            int consume = 0;
            int altar_tier = 0;
            var true_time = recipe.duration;
            int batch = 1;
            double speed = altarMachine.speed;
            if (condition instanceof BloodAltarCondition altarCondition) {
                altar_tier = altarCondition.altar_tier;
                consume = altarCondition.consumption_rate;
                var parallel = ParallelLogic.getParallelAmountWithoutEU(machine, recipe, 1024);
                var overclock = altarMachine.altar_tier - altar_tier;
                if (overclock > 0) {
                    consume = (int) (consume * Math.pow(4, overclock));
                    speed *= altarMachine.calculateSpeed(overclock);
                }
                if (recipe.duration / speed < 20) {
                    int minBatch = (int) Math.ceil(20.0 * speed / recipe.duration);
                    batch = Math.min(Math.max(1, minBatch), parallel);
                    true_time = (int) Math.max(20, Math.ceil(recipe.duration / speed * batch));
                } else {
                    true_time = (int) Math.max(20, recipe.duration / speed);
                }
                altarMachine.consumption_lp = consume;
                return ModifierFunction.builder()
                        .parallels(batch)
                        .inputModifier(ContentModifier.multiplier(batch))
                        .outputModifier(ContentModifier.multiplier(batch))
                        .durationMultiplier((double) true_time / recipe.duration)
                        .build();
            }
            return ModifierFunction.NULL;
        }
        return ModifierFunction.NULL;
    }

    //////////////////////////////////////
    // ******** Subscriptions&Ticks ********//
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
        if (TickSubs != null) {
            TickSubs.unsubscribe();
            TickSubs = null;
        }
    }

    public void updateTick() {
        TickSubs = subscribeServerTick(TickSubs, this::updateStates);
    }

    // todo
    public void updateStates() {
        if (this.isFormed() && this.altar != null) {
            if (this.getOffsetTimer() % chargingFrequency == 0) {
                // if(MachineUtils.inputFluid(CMMaterials.Mana.getFluid(10),this ))
                // {
                // altar.fillMainTank(10);
                // }
                var boosterRecipe = getBloodRecipe();
                var isBoosted = RecipeHelper.matchRecipe(this, boosterRecipe).isSuccess() &&
                        RecipeHelper.handleRecipeIO(this, boosterRecipe, IO.IN, this.recipeLogic.getChanceCaches())
                                .isSuccess();
                if (isBoosted) {
                    altar.fillMainTank(dislocation_rate);
                }
                // if(MachineUtils.inputFluid(new
                // FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(),dislocation_rate),this)) {
                // altar.fillMainTank(dislocation_rate);
                // }
            }
            if (this.getOffsetTimer() % 100 == 0) {
                var item = tileAltar.getItem(0);
                if (item.getItem() instanceof JadeItem jades) {
                    this.Upgrade = jades.getType();
                    this.Upgrade_name = jades.getUpgradeName().translate().getString();
                }
            }
        }
    }

    //////////////////////////////////////
    // ******** Lang ********//
    //////////////////////////////////////
    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (this.isFormed && tileAltar != null && altar != null) {
            textList.add(level_lang.translate(altar_tier));
            textList.add(altar_lang[0].translate(tileAltar.getCapacity()));
            textList.add(altar_lang[1].translate(tileAltar.getCurrentBlood()));
            textList.add(altar_lang[2].translate(speed));
            textList.add(altar_lang[3].translate(dislocation_rate, chargingFrequency * 0.05F));
            textList.add(altar_lang[4].translate(Upgrade_name));
            // textList.add(altar_lang[2].translate(tileAltar.getCapacity()));
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
    // ******** PatternBuilder ********//
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
            case 4:
                return "Level 6 Structure";
            default:
                return "No Pattern Matched";
        }
    }

    private BlockPattern createLevel3Pattern() {
        return FactoryBlockPattern.start()
                .aisle("ABBBCBBBA", "D#######D", "D#######D", "E#######E", "#########", "#########", "#########",
                        "#########", "#########")
                .aisle("B#######B", "#BFFFFFB#", "#G#####G#", "#G#####G#", "#H#####H#", "#I#####I#", "#########",
                        "#########", "#########")
                .aisle("B#######B", "#FJJAJJF#", "##KLMLK##", "##N###N##", "##N###N##", "##N###N##", "##ONNNO##",
                        "#########", "#########")
                .aisle("B#######B", "#FJ###JF#", "##LLFLL##", "#########", "#########", "#########", "##N###N##",
                        "###OOO###", "#########")
                .aisle("C#######C", "#FP###PF#", "##MFFFM##", "####Q####", "#########", "####R####", "##N#S#N##",
                        "###OTO###", "####U####")
                .aisle("B#######B", "#FJ###JF#", "##LLFLL##", "#########", "#########", "#########", "##N###N##",
                        "###OOO###", "#########")
                .aisle("B#######B", "#FJJPJJF#", "##KLVLK##", "##N###N##", "##N###N##", "##N###N##", "##ONNNO##",
                        "#########", "#########")
                .aisle("B#######B", "#BFFFFFB#", "#G#####G#", "#G#####G#", "#H#####H#", "#I#####I#", "#########",
                        "#########", "#########")
                .aisle("ABBBCBBBA", "D#######D", "D#######D", "E#######E", "#########", "#########", "#########",
                        "#########", "########W")
                .where("B", Predicates.blocks(BloodMagicBlocks.DUNGEON_BRICK_1.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("I",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("twilightforest:wither_skeleton_skull_candle"))))
                .where("T",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:quartz_brick_wall"))))
                .where("K", Predicates.blocks(Blocks.SEA_LANTERN))
                .where("V", Predicates.controller(Predicates.blocks(getDefinition().get())))
                .where("J", Predicates.blocks(BloodMagicBlocks.OBSIDIAN_TILE_PATH.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("F", CMPredicates.BMRuneBlocks)
                .where("C", Predicates.blocks(BloodMagicBlocks.DUSK_RITUAL_STONE.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("R", Predicates.blocks(Blocks.SOUL_LANTERN))
                .where("D",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("N", Predicates.blocks(Blocks.QUARTZ_PILLAR))
                .where("W", Predicates.any())
                .where("L", Predicates.blocks(BloodMagicBlocks.BLANK_RUNE.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("U", Predicates.blocks(Blocks.BEACON))
                .where("#", Predicates.any())
                .where("S", Predicates.blocks(Blocks.CHAIN))
                .where("A", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.PURPLE).get()))
                .where("O", Predicates.blocks(Blocks.CHISELED_QUARTZ_BLOCK))
                .where("H", Predicates.blocks(Blocks.GLOWSTONE))
                .where("Q", Predicates.blocks(BloodMagicBlocks.BLOOD_ALTAR.get()))
                .where("M", Predicates.blocks(Blocks.PRISMARINE_BRICKS))
                .where("P", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.RED).get()))
                .where("E", Predicates.blocks(Blocks.CRYING_OBSIDIAN))
                .where("G", Predicates.blocks(Blocks.STONE_BRICKS))
                .build();
    }

    private BlockPattern createLevel4Pattern() {
        return FactoryBlockPattern.start()
                .aisle("ABCCCCDCCCCBA", "E###########E", "E###########E", "E###########E", "E###########E",
                        "E###########E", "F###########F", "#############", "#############", "#############")
                .aisle("B###########B", "#GHIIIIIIIHG#", "#J#########J#", "#J#########J#", "#J#########J#",
                        "#J#########J#", "#K#########K#", "#############", "#############", "#############")
                .aisle("C###########C", "#HLMMMNMMMLH#", "##########O##", "##########O##", "##P#######P##",
                        "#############", "#############", "#############", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "###MIIIIIM###", "###J#####J###", "###J#####J###",
                        "###Q#####Q###", "###R#####R###", "#############", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "###ISSGSSI###", "####TVUUT####", "####W###W####",
                        "####W###W####", "####W###W####", "####XWWWX####", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "###IS###SI###", "####UUIUU####", "#############",
                        "#############", "#############", "####W###W####", "#####XXX#####", "#############")
                .aisle("D###########D", "#IN#######NI#", "###IG###GI###", "####YIIIY####", "######Z######",
                        "#############", "######a######", "####W#b#W####", "#####XcX#####", "######d######")
                .aisle("C###########C", "#IM#######MI#", "###IS###SI###", "####UUIUU####", "#############",
                        "#############", "#############", "####W###W####", "#####XXX#####", "#############")
                .aisle("C###########C", "#IM#######MI#", "###ISSLSSI###", "####TUYUT####", "####W###W####",
                        "####W###W####", "####W###W####", "####XWWWX####", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "###MIIIIIM###", "###J#####J###", "###J#####J###",
                        "###Q#####Q###", "###R#####R###", "#############", "#############", "#############")
                .aisle("C###########C", "#HLMMMNMMMLH#", "##O#######O##", "##O#######O##", "##P#######P##",
                        "#############", "#############", "#############", "#############", "#############")
                .aisle("B###########B", "#GHIIIIIIIHG#", "#J#########J#", "#J#########J#", "#J#########J#",
                        "#J#########J#", "#K#########K#", "#############", "#############", "#############")
                .aisle("ABCCCCDCCCCBA", "E###########E", "E###########E", "E###########E", "E###########E",
                        "E###########E", "F###########F", "#############", "#############", "############e")
                .where("R",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("twilightforest:wither_skeleton_skull_candle"))))
                .where("c",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:quartz_brick_wall"))))
                .where("V", Predicates.controller(Predicates.blocks(getDefinition().get())))
                .where("S", Predicates.blocks(BloodMagicBlocks.OBSIDIAN_TILE_PATH.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("I", CMPredicates.BMRuneBlocks)
                .where("O",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("W", Predicates.blocks(Blocks.QUARTZ_PILLAR))
                .where("H", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:airritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("U", Predicates.blocks(BloodMagicBlocks.BLANK_RUNE.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("d", Predicates.blocks(Blocks.BEACON))
                .where("#", Predicates.any())
                .where("D",
                        Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:teleposer")))
                                .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("Q", Predicates.blocks(Blocks.GLOWSTONE))
                .where("A",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("cataclysm:chiseled_end_stone_bricks"))))
                .where("J", Predicates.blocks(Blocks.STONE_BRICKS))
                .where("M", Predicates.blocks(BloodMagicBlocks.DUNGEON_BRICK_1.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("T", Predicates.blocks(Blocks.SEA_LANTERN))
                .where("B", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:waterritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("N", Predicates.blocks(BloodMagicBlocks.DUSK_RITUAL_STONE.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("a", Predicates.blocks(Blocks.SOUL_LANTERN))
                .where("F",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:void_lantern_block"))))
                .where("e", Predicates.any())
                .where("C", Predicates.blocks(CMBlocks.CASING_BLOODLOGIC.get()))
                .where("b", Predicates.blocks(Blocks.CHAIN))
                .where("L", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.PURPLE).get()))
                .where("X", Predicates.blocks(Blocks.CHISELED_QUARTZ_BLOCK))
                .where("Z", Predicates.blocks(BloodMagicBlocks.BLOOD_ALTAR.get()))
                .where("Y", Predicates.blocks(Blocks.PRISMARINE_BRICKS))
                .where("G", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.RED).get()))
                .where("P", Predicates.blocks(Blocks.CRYING_OBSIDIAN))
                .where("E",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:end_stone_pillar"))))
                .where("K", Predicates.blocks(BloodMagicBlocks.BLOODSTONE_BRICK.get()))
                .build();
    }

    private BlockPattern createLevel5Pattern() {
        return FactoryBlockPattern.start()
                .aisle("AABCCCCCCACCCCCCBAA", "###################", "###################", "###################",
                        "###################", "###################", "###################", "###################",
                        "###################", "###################", "###################")
                .aisle("AAD#############DAA", "#DEFFFFFFFFFFFFFED#", "#G###############G#", "#G###############G#",
                        "#G###############G#", "#G###############G#", "#G###############G#", "#G###############G#",
                        "#HG#############GH#", "###################", "###################")
                .aisle("BD###############DB", "#EDEHIIIIIIIIIHEDE#", "##J#############J##", "##J#############J##",
                        "##J#############J##", "##J#############J##", "##J#############J##", "##JG###########GJ##",
                        "#GK#############KG#", "###################", "###################")
                .aisle("C#################C", "#FELMHHHHNHHHHMLEF#", "###O###########O###", "###O###########O###",
                        "###O###########O###", "###O###########O###", "###OG#########GO###", "##GP###########PG##",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FHM###########MHF#", "####QRFFFFFFFFQ####", "####S#########S####",
                        "####S#########S####", "####S#########S####", "###GS#########SG###", "####I#########I####",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####RTUUUVUUUTR####", "#####W#######W#####",
                        "#####W#######W#####", "#####X#######X#####", "###################", "###################",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######UFFFFFU######",
                        "######S#####S######", "######S#####S######", "######Y#####Y######", "######Z#####Z######",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######FaaQaaF######",
                        "#######bcdcb#######", "#######G###G#######", "#######G###G#######", "#######G###G#######",
                        "#######GGGGG#######", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######Fa###aF######",
                        "#######ccccc#######", "###################", "###################", "###################",
                        "#######G###G#######", "########GGG########", "###################")
                .aisle("A#################A", "#FIN###########NIF#", "####FV#######VF####", "######FQ###QF######",
                        "#######ecFFe#######", "#########f#########", "###################", "#########g#########",
                        "#######G#h#G#######", "########GiG########", "#########j#########")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######Fa###aF######",
                        "#######ccFcc#######", "###################", "###################", "###################",
                        "#######G###G#######", "########GGG########", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######FaaTaaF######",
                        "#######bcecb#######", "#######G###G#######", "#######G###G#######", "#######G###G#######",
                        "#######GGGGG#######", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######UFFFFF#######",
                        "######S#####S######", "######S#####S######", "######Y#####Y######", "######Z#####Z######",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####RTUUUVUUUTR####", "#####W#############",
                        "#####W#############", "#####X#######X#####", "###################", "###################",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FHM###########MHF#", "####QRFFFFFFFRQ####", "####S#########S####",
                        "####S#########S####", "####S#########S####", "###GS#########SG###", "####I#########I####",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FELMHHHHNHHHHMLEF#", "###O###########O###", "###O###########O###",
                        "###O###########O###", "###O###########O###", "###OG#########GO###", "##GP###########PG##",
                        "###################", "###################", "###################")
                .aisle("BD###############DB", "#EDEHIIIIIIIIIHEDE#", "##J#############J##", "##J#############J##",
                        "##J#############J##", "##J#############J##", "##J#############J##", "##JG###########GJ##",
                        "#GK#############KG#", "###################", "###################")
                .aisle("AAD#############DAA", "#DEFFFFFFFFFFFFFED#", "#G###############G#", "#G###############G#",
                        "#G###############G#", "#G###############G#", "#G###############G#", "#G###############G#",
                        "#HG#############GH#", "###################", "##################k")
                .where("Z",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("twilightforest:wither_skeleton_skull_candle"))))
                .where("d", Predicates.controller(Predicates.blocks(getDefinition().get())))
                .where("a", Predicates.blocks(BloodMagicBlocks.OBSIDIAN_TILE_PATH.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("F", CMPredicates.BMRuneBlocks)
                .where("B", Predicates.blocks(Blocks.SCULK_CATALYST))
                .where("W",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("i", Predicates.blocks(Blocks.REINFORCED_DEEPSLATE))
                .where("D", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_metal")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("R", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:airritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("c", Predicates.blocks(BloodMagicBlocks.BLANK_RUNE.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("j", Predicates.blocks(Blocks.BEACON))
                .where("#", Predicates.any())
                .where("J",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("bloodmagic:dungeon_pillar_special"))))
                .where("G", Predicates.blocks(CMBlocks.SOUL_LOCKING_CASING.get()))
                .where("N",
                        Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:teleposer")))
                                .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("Y", Predicates.blocks(Blocks.GLOWSTONE))
                .where("K", Predicates.blocks(Blocks.OBSIDIAN))
                .where("L",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("cataclysm:chiseled_end_stone_bricks"))))
                .where("S", Predicates.blocks(Blocks.STONE_BRICKS))
                .where("A",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:witherite_block"))))
                .where("U", Predicates.blocks(BloodMagicBlocks.DUNGEON_BRICK_1.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("b", Predicates.blocks(Blocks.SEA_LANTERN))
                .where("M", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:waterritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("V", Predicates.blocks(BloodMagicBlocks.DUSK_RITUAL_STONE.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("g", Predicates.blocks(Blocks.SOUL_LANTERN))
                .where("P",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:void_lantern_block"))))
                .where("k", Predicates.any())
                .where("H", Predicates.blocks(CMBlocks.CASING_BLOODLOGIC.get()))
                .where("C", Predicates.blocks(Blocks.SCULK))
                .where("h", Predicates.blocks(Blocks.CHAIN))
                .where("T", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.PURPLE).get()))
                .where("f", Predicates.blocks(BloodMagicBlocks.BLOOD_ALTAR.get()))
                .where("E", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:fireritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("e", Predicates.blocks(Blocks.PRISMARINE_BRICKS))
                .where("Q", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.RED).get()))
                .where("X", Predicates.blocks(Blocks.CRYING_OBSIDIAN))
                .where("I", Predicates.blocks(BloodMagicBlocks.BLOODSTONE_BRICK.get()))
                .where("O",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:end_stone_pillar"))))
                .build();
    }

    private BlockPattern createLevel6Pattern() {
        return FactoryBlockPattern.start()
                .aisle("ABBBBBBBBBBBBBBBBBBBBBA", "AC###################CA", "A#####################A",
                        "A#####################A", "A#####################A", "A#####################A",
                        "A#####################A", "A#####################A", "DE###################ED",
                        "#######################", "#######################")
                .aisle("BFEEGGHFIJKLKJIFHGGEEFB", "CMC#################CMC", "#M###################M#",
                        "#M###################M#", "#M###################M#", "#M###################M#",
                        "#M###################M#", "#M###################M#", "ENE#################ENE",
                        "#######################", "#######################")
                .aisle("BEOOPQQQQQQOQQQQQQPOOEB", "#CMC###############CMC#", "##M#################M##",
                        "##M#################M##", "##M#################M##", "##M#################M##",
                        "##M#################M##", "##M#################M##", "#ENE###############ENE#",
                        "#######################", "#######################")
                .aisle("BEOOR#############ROOEB", "##CRIBBBBBBBBBBBBBIRC##", "###E###############E###",
                        "###E###############E###", "###E###############E###", "###E###############E###",
                        "###E###############E###", "###E###############E###", "##ENE#############ENE##",
                        "#######################", "#######################")
                .aisle("BGPR###############RPGB", "###IRINSSSSSSSSSNIRI###", "####T#############T####",
                        "####T#############T####", "####T#############T####", "####T#############T####",
                        "####T#############T####", "####TE###########ET####", "###EU#############UE###",
                        "#######################", "#######################")
                .aisle("BGQ#################QGB", "###BIVFNNNNWNNNNFVIB###", "#####X###########X#####",
                        "#####X###########X#####", "#####X###########X#####", "#####X###########X#####",
                        "#####XE#########EX#####", "####EY###########YE####", "#######################",
                        "#######################", "#######################")
                .aisle("BHQ#################QHB", "###BNF###########FNB###", "######ZHBBBBBBBHZ######",
                        "######A#########A######", "######A#########A######", "######A#########A######",
                        "#####EA#########AE#####", "######S#########S######", "#######################",
                        "#######################", "#######################")
                .aisle("BFQ#################QFB", "###BSN###########NSB###", "######HabbbKbbbaH######",
                        "#######c#######c#######", "#######c#######c#######", "#######d#######d#######",
                        "#######################", "#######################", "#######################",
                        "#######################", "#######################")
                .aisle("BIQ#################QIB", "###BSN###########NSB###", "######Bb#######bB######",
                        "########bBBBBBb########", "########A#####A########", "########A#####A########",
                        "########e#####e########", "########f#####f########", "#######################",
                        "#######################", "#######################")
                .aisle("BJQ#################QJB", "###BSN###########NSB###", "######Bb#######bB######",
                        "########BggZggB########", "#########hijih#########", "#########E###E#########",
                        "#########E###E#########", "#########E###E#########", "#########EEEEE#########",
                        "#######################", "#######################")
                .aisle("BKQ#################QKB", "###BSN###########NSB###", "######Bb#######bB######",
                        "########Bg###gB########", "#########iiBii#########", "#######################",
                        "#######################", "#######################", "#########E###E#########",
                        "##########EEE##########", "#######################")
                .aisle("BLO#################OLB", "###BSW###########WSB###", "######BK#######KB######",
                        "########BZ###ZB########", "#########kBBBk#########", "###########l###########",
                        "#######################", "###########m###########", "#########E#n#E#########",
                        "##########EME##########", "###########o###########")
                .aisle("BKQ#################QKB", "###BSN###########NSB###", "######Bb#######bB######",
                        "########Bg###gB########", "#########iiBii#########", "#######################",
                        "#######################", "#######################", "#########E###E#########",
                        "##########EEE##########", "#######################")
                .aisle("BJQ#################QJB", "###BSN###########NSB###", "######Bb#######bB######",
                        "########BggaggB########", "#########hikih#########", "#########E###E#########",
                        "#########E###E#########", "#########E###E#########", "#########EEEEE#########",
                        "#######################", "#######################")
                .aisle("BIQ#################QIB", "###BSN###########NSB###", "######Bb#######bB######",
                        "########bBBBBBb########", "########A#####A########", "########A#####A########",
                        "########e#####e########", "########f#####f########", "#######################",
                        "#######################", "#######################")
                .aisle("BFQ#################QFB", "###BSN###########NSB###", "######HabbbKbbbaH######",
                        "#######c#######c#######", "#######c#######c#######", "#######d#######d#######",
                        "#######################", "#######################", "#######################",
                        "#######################", "#######################")
                .aisle("BHQ#################QHB", "###BNF###########FNB###", "######ZHBBBBBBBHZ######",
                        "######A#########A######", "######A#########A######", "######A#########A######",
                        "#####EA#########AE#####", "######S#########S######", "#######################",
                        "#######################", "#######################")
                .aisle("BGQ#################QGB", "###BIVFNNNNWNNNNFVIB###", "#####X###########X#####",
                        "#####X###########X#####", "#####X###########X#####", "#####X###########X#####",
                        "#####XE#########EX#####", "####EY###########YE####", "#######################",
                        "#######################", "#######################")
                .aisle("BGPR###############RPGB", "###IRINSSSSSSSSSNIRI###", "####T#############T####",
                        "####T#############T####", "####T#############T####", "####T#############T####",
                        "####T#############T####", "####TE###########ET####", "###EU#############UE###",
                        "#######################", "#######################")
                .aisle("BEOOR#############ROOEB", "##CRIBBBBBBBBBBBBBIRC##", "###E###############E###",
                        "###E###############E###", "###E###############E###", "###E###############E###",
                        "###E###############E###", "###E###############E###", "##ENE#############ENE##",
                        "#######################", "#######################")
                .aisle("BEOOPQQQQQQOQQQQQQPOOEB", "#CMC###############CMC#", "##M#################M##",
                        "##M#################M##", "##M#################M##", "##M#################M##",
                        "##M#################M##", "##M#################M##", "#ENE###############ENE#",
                        "#######################", "#######################")
                .aisle("BFEEGGHFIJKLKJIFHGGEEFB", "CMC#################CMC", "#M###################M#",
                        "#M###################M#", "#M###################M#", "#M###################M#",
                        "#M###################M#", "#M###################M#", "ENE#################ENE",
                        "#######################", "#######################")
                .aisle("ABBBBBBBBBBBBBBBBBBBBBA", "AC###################CA", "A#####################A",
                        "A#####################A", "A#####################A", "A#####################A",
                        "A#####################A", "A#####################A", "DE###################ED",
                        "p######################", "######################q")
                .where("p", Predicates.blocks(CMBlocks.RUNE_STONE_PERFECT.get()))
                .where("f",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("twilightforest:wither_skeleton_skull_candle"))))
                .where("j", Predicates.controller(Predicates.blocks(getDefinition().get())))
                .where("D", Predicates.blocks(CMBlocks.SUPERNORMAL_MAGIC_CALCULATE_CORE.get()))
                .where("g", Predicates.blocks(BloodMagicBlocks.OBSIDIAN_TILE_PATH.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("B", CMPredicates.BMRuneBlocks)
                .where("P", Predicates.blocks(Blocks.SCULK_CATALYST))
                .where("L", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:lightritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("c",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("M", Predicates.blocks(Blocks.REINFORCED_DEEPSLATE))
                .where("J", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:earthritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("R", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_metal")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("H", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:airritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("i", Predicates.blocks(BloodMagicBlocks.BLANK_RUNE.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("o", Predicates.blocks(Blocks.BEACON))
                .where("#", Predicates.any())
                .where("T",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("bloodmagic:dungeon_pillar_special"))))
                .where("E", Predicates.blocks(CMBlocks.SOUL_LOCKING_CASING.get()))
                .where("W",
                        Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:teleposer")))
                                .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("e", Predicates.blocks(Blocks.GLOWSTONE))
                .where("U", Predicates.blocks(Blocks.OBSIDIAN))
                .where("V",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("cataclysm:chiseled_end_stone_bricks"))))
                .where("A", Predicates.blocks(Blocks.STONE_BRICKS))
                .where("C",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("apotheosis:draconic_endshelf"))))
                .where("O",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:witherite_block"))))
                .where("b", Predicates.blocks(BloodMagicBlocks.DUNGEON_BRICK_1.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("h", Predicates.blocks(Blocks.SEA_LANTERN))
                .where("F", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:waterritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("K", Predicates.blocks(BloodMagicBlocks.DUSK_RITUAL_STONE.get())
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("m", Predicates.blocks(Blocks.SOUL_LANTERN))
                .where("Y",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:void_lantern_block"))))
                .where("q", Predicates.any())
                .where("G",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:cursium_block"))))
                .where("N", Predicates.blocks(CMBlocks.CASING_BLOODLOGIC.get()))
                .where("Q", Predicates.blocks(Blocks.SCULK))
                .where("n", Predicates.blocks(Blocks.CHAIN))
                .where("a", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.PURPLE).get()))
                .where("l", Predicates.blocks(BloodMagicBlocks.BLOOD_ALTAR.get()))
                .where("I", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:fireritualstone")))
                        .or(autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES)))
                .where("k", Predicates.blocks(Blocks.PRISMARINE_BRICKS))
                .where("Z", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.RED).get()))
                .where("d", Predicates.blocks(Blocks.CRYING_OBSIDIAN))
                .where("S", Predicates.blocks(BloodMagicBlocks.BLOODSTONE_BRICK.get()))
                .where("X",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:end_stone_pillar"))))
                .build();
    }
}
