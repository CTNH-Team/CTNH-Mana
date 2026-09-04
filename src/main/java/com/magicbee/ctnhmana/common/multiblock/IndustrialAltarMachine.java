package com.magicbee.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.ingredient.fluid.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.magicbee.ctnhmana.api.mixin.IBloodAltarLogic;
import com.magicbee.ctnhmana.api.pattern.CMPredicates;
import com.magicbee.ctnhmana.api.recipe.condition.BloodAltarCondition;
import com.magicbee.ctnhmana.common.item.bloodmagicjade.JadeItem;
import com.magicbee.ctnhmana.mixin.bloodmagic.TileAltarAccessor;
import com.magicbee.ctnhmana.registry.CMBlocks;
import com.magicbee.ctnhmana.registry.CMRecipeTypes;
import com.magicbee.ctnhmana.utils.CTNHManaUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.overlay.DisplayHelper;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import wayoftime.bloodmagic.altar.BloodAltar;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.tile.TileAltar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.pattern.Predicates.autoAbilities;

@SuppressWarnings("removal")
public class IndustrialAltarMachine extends MultiPatternMultiblockMachine implements ITieredMachine {

    @CN("§4LP消耗速度:%d/t")
    @EN("§4LP consumption: %d/t")
    public static Lang Lpconsumelang;

    @Override
    protected void writeMachineJadeData(CompoundTag data, BlockAccessor accessor) {
        super.writeMachineJadeData(data, accessor);
        int lp = 0;
        int maxLp = 0;
        if (isFormed() && altar != null) {
            lp = altar.getCurrentBlood();
            maxLp = altar.getCapacity();
        }
        data.putInt("altar_lp", lp);
        data.putInt("altar_max_lp", maxLp);
        int consume = isFormed() && isActive() ? consumption_lp : 0;
        if ("suppression".equals(Upgrade)) consume = (int) (consume * 0.5);
        data.putInt("altar_consume", consume);
    }

    @Override
    protected void appendMachineJadeTooltip(CompoundTag data, ITooltip tooltip, BlockAccessor accessor,
                                            IPluginConfig config) {
        super.appendMachineJadeTooltip(data, tooltip, accessor, config);
        int max = data.getInt("altar_max_lp");
        if (max > 0) {
            int lp = data.getInt("altar_lp");
            var helper = tooltip.getElementHelper();
            tooltip.add(helper.progress(Math.min(1F, lp / (float) max),
                    Component.translatable("ctnhmana.jade.manahatch.manaprogress",
                            DisplayHelper.dfCommas.format(lp), DisplayHelper.dfCommas.format(max)),
                    helper.progressStyle().color(0x8B0000, 0x8B0000).textColor(-1),
                    Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555), true));
        }
        int consume = data.getInt("altar_consume");
        if (consume > 0) tooltip.add(tooltip.getElementHelper().text(Lpconsumelang.translate(consume)));
    }

    private record PreviewCell(BlockPos pos, TraceabilityPredicate predicate, int sliceIndex) {}

    public IndustrialAltarMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @DescSynced
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
        addPattern(createLevel3Pattern(getDefinition()));
        addPattern(createLevel4Pattern(getDefinition()));
        addPattern(createLevel5Pattern(getDefinition()));
        addPattern(createLevel6Pattern(getDefinition()));
    }

    @Override
    public Component beforeWorking(@Nullable GTRecipe recipe) {
        updateAltarData();
        if (consumeLPIfEnough(consumption_lp)) {
            return super.beforeWorking(recipe);
        }
        return failureManaLang_NoEnoughLP.translate();
    }

    @Override
    public boolean onWorking() {
        if (!consumeLPIfEnough(Upgrade.equals("suppression") ? (int) (consumption_lp * 0.5) : consumption_lp)) {
            getRecipeLogic().setProgress(this.getProgress() - 1);
            getRecipeLogic().setWaiting(failureManaLang_NoEnoughLP.translate());
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
        updateTick();
    }

    public boolean consumeLPIfEnough(int lp) {
        if (this.altar == null) return false;
        if (this.altar instanceof IBloodAltarLogic altaraccessor) {
            return altaraccessor.CM$ConsumeLPIfEnough(lp);
        }
        return false;
    }

    public void updateAltarData() {
        var world = this.getLevel();
        if (this.getLevel().isClientSide()) return;
        var index = getMatchedPatternIndex();
        if (!isStructureOperational()) return;
        altar_pos = MachineUtils.getOffset(this, 0, 1, 2);
        if (world.getBlockEntity(altar_pos) instanceof TileAltar new_altar) {
            var tier = new_altar.getTier();
            if (tier < 2) onStructureInvalid();
            altar_tier = Math.max(tier, index + 2);
            this.tileAltar = new_altar;
            if (this.tileAltar instanceof TileAltarAccessor accessor) {
                this.altar = accessor.getBloodAltar();
            }
        }
        if (this.altar == null) return;
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
        updateTick();
    }

    public GTRecipe getBloodRecipe() {
        return GTRecipeBuilder.ofRaw().inputFluids(FluidIngredient.of(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 100))
                .buildRawRecipe().toRuntime();
    }

    public int calculateSpeed(int tier) {
        return this.Upgrade.equals("ephemeral") ? (int) (Math.pow(4, tier)) : (int) Math.pow(2, tier);
    }

    //////////////////////////////////////
    // ******** Modifier ********//
    //////////////////////////////////////
    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
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
                var parallel = CTNHManaUtils.getParallelAmount(group, recipe, 1024, false);
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
                CTNHManaUtils.multiplyInputs(recipe, batch);
                recipe.multiplyOutputs(batch);
                CTNHManaUtils.multiplyTickInputs(recipe, batch);
                recipe.multiplyTickOutputs(batch);
                recipe.multiplyDuration((double) true_time / recipe.duration);
                recipe.parallels = batch;
                return null;
            }
            return null;
        }
        return null;
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
        if (isStructureOperational()) {
            TickSubs = subscribeServerTick(TickSubs, this::updateStates);
        } else if (TickSubs != null) {
            TickSubs.unsubscribe();
            TickSubs = null;
        }
    }

    @Override
    protected void onStructureRevalidationChanged(boolean pending) {
        super.onStructureRevalidationChanged(pending);
        updateTick();
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
                var isBoosted = RecipeHelper.matchRecipe(getRecipeLogic().getLastGroup(), boosterRecipe).isSuccess() &&
                        RecipeHelper.handleRecipeIO(getRecipeLogic().getLastGroup(), boosterRecipe, IO.IN)
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
        if (isStructureOperational() && tileAltar != null && altar != null) {
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
    @EN("Current tier: %d")
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

    public static BlockPattern createLevel3Pattern(MultiblockMachineDefinition definition) {
        var abilities = autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES);
        return FactoryBlockPattern.start()
                .aisle("ABBBCBBBA", "D#######D", "D#######D", "E#######E", "#########", "#########", "#########",
                        "#########", "#########")
                .aisle("B#######B", "#BFFFFFB#", "#G#####G#", "#G#####G#", "#H#####H#", "#I#####I#", "#########",
                        "#########", "#########")
                .aisle("B#######B", "#FJJPJJF#", "##KLMLK##", "##N###N##", "##N###N##", "##N###N##", "##ONNNO##",
                        "#########", "#########")
                .aisle("B#######B", "#FJ###JF#", "##LFFFL##", "#########", "#########", "#########", "##N###N##",
                        "###OOO###", "#########")
                .aisle("C#######C", "#FP###PF#", "##MFFFM##", "####Q####", "#########", "####R####", "##N#S#N##",
                        "###OTO###", "####U####")
                .aisle("B#######B", "#FJ###JF#", "##LFFFL##", "#########", "#########", "#########", "##N###N##",
                        "###OOO###", "#########")
                .aisle("B#######B", "#FJJPJJF#", "##KLVLK##", "##N###N##", "##N###N##", "##N###N##", "##ONNNO##",
                        "#########", "#########")
                .aisle("B#######B", "#BFFFFFB#", "#G#####G#", "#G#####G#", "#H#####H#", "#I#####I#", "#########",
                        "#########", "#########")
                .aisle("ABBBCBBBA", "D#######D", "D#######D", "E#######E", "#########", "#########", "#########",
                        "#########", "########W")
                .where("B", Predicates.blocks(BloodMagicBlocks.DUNGEON_BRICK_1.get())
                        .or(abilities))
                .where("I",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("twilightforest:wither_skeleton_skull_candle"))))
                .where("T",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:quartz_brick_wall"))))
                .where("K", Predicates.blocks(Blocks.SEA_LANTERN))
                .where("V", Predicates.controller(Predicates.blocks(definition.get())))
                .where("J", Predicates.blocks(BloodMagicBlocks.OBSIDIAN_TILE_PATH.get())
                        .or(abilities))
                .where("F", CMPredicates.BMRuneBlocks)
                .where("C", Predicates.blocks(BloodMagicBlocks.DUSK_RITUAL_STONE.get())
                        .or(abilities))
                .where("R", Predicates.blocks(Blocks.SOUL_LANTERN))
                .where("D",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("N", Predicates.blocks(Blocks.QUARTZ_PILLAR))
                .where("W", Predicates.any())
                .where("L", Predicates.blocks(BloodMagicBlocks.BLANK_RUNE.get())
                        .or(abilities))
                .where("U", Predicates.blocks(Blocks.BEACON))
                .where("#", Predicates.any())
                .where("S", Predicates.blocks(Blocks.CHAIN))
                .where("A", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.PURPLE).get()))
                .where("O", Predicates.blocks(Blocks.CHISELED_QUARTZ_BLOCK))
                .where("H", Predicates.blocks(Blocks.GLOWSTONE)
                        .or(Predicates.blocks(Blocks.SEA_LANTERN))
                        .or(Predicates.blocks(Blocks.SHROOMLIGHT)))
                .where("Q", Predicates.blocks(BloodMagicBlocks.BLOOD_ALTAR.get()))
                .where("M", Predicates.blocks(Blocks.PRISMARINE_BRICKS))
                .where("P", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.RED).get()))
                .where("E", Predicates.blocks(Blocks.CRYING_OBSIDIAN))
                .where("G", Predicates.blocks(Blocks.STONE_BRICKS))
                .build();
    }

    public static BlockPattern createLevel4Pattern(MultiblockMachineDefinition definition) {
        var abilities = autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES);
        return FactoryBlockPattern.start()
                .aisle("ABCCCCDCCCCBA", "E###########E", "E###########E", "E###########E", "E###########E",
                        "E###########E", "F###########F", "#############", "#############", "#############")
                .aisle("B###########B", "#GHIIIIIIIHG#", "#J#########J#", "#J#########J#", "#J#########J#",
                        "#J#########J#", "#K#########K#", "#############", "#############", "#############")
                .aisle("C###########C", "#HLMMMNMMMLH#", "##O#######O##", "##O#######O##", "##P#######P##",
                        "#############", "#############", "#############", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "###MIIIIIM###", "###J#####J###", "###J#####J###",
                        "###Q#####Q###", "###R#####R###", "#############", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "###ISSGSSI###", "####TUYUT####", "####W###W####",
                        "####W###W####", "####W###W####", "####XWWWX####", "#############", "#############")
                .aisle("C###########C", "#IM#######MI#", "###IS###SI###", "####UIIIU####", "#############",
                        "#############", "#############", "####W###W####", "#####XXX#####", "#############")
                .aisle("D###########D", "#IN#######NI#", "###IG###GI###", "####YIIIY####", "######Z######",
                        "#############", "######a######", "####W#b#W####", "#####XcX#####", "######d######")
                .aisle("C###########C", "#IM#######MI#", "###IS###SI###", "####UIIIU####", "#############",
                        "#############", "#############", "####W###W####", "#####XXX#####", "#############")
                .aisle("C###########C", "#IM#######MI#", "###ISSGSSI###", "####TUVUT####", "####W###W####",
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
                .where("V", Predicates.controller(Predicates.blocks(definition.get())))
                .where("S", Predicates.blocks(BloodMagicBlocks.OBSIDIAN_TILE_PATH.get())
                        .or(abilities))
                .where("I", CMPredicates.BMRuneBlocks)
                .where("O",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("W", Predicates.blocks(Blocks.QUARTZ_PILLAR))
                .where("H", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:airritualstone")))
                        .or(abilities))
                .where("U", Predicates.blocks(BloodMagicBlocks.BLANK_RUNE.get())
                        .or(abilities))
                .where("d", Predicates.blocks(Blocks.BEACON))
                .where("#", Predicates.any())
                .where("D",
                        Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:teleposer")))
                                .or(abilities))
                .where("Q", Predicates.blocks(Blocks.GLOWSTONE)
                        .or(Predicates.blocks(Blocks.SEA_LANTERN))
                        .or(Predicates.blocks(Blocks.SHROOMLIGHT)))
                .where("A",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("cataclysm:chiseled_end_stone_bricks"))))
                .where("J", Predicates.blocks(Blocks.STONE_BRICKS))
                .where("M", Predicates.blocks(BloodMagicBlocks.DUNGEON_BRICK_1.get())
                        .or(abilities))
                .where("T", Predicates.blocks(Blocks.SEA_LANTERN))
                .where("B", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:waterritualstone")))
                        .or(abilities))
                .where("N", Predicates.blocks(BloodMagicBlocks.DUSK_RITUAL_STONE.get())
                        .or(abilities))
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
                .where("K", Predicates.blocks(BloodMagicBlocks.BLOODSTONE_BRICK.get())
                        .or(Predicates.blocks(BloodMagicBlocks.BLOODSTONE.get())))
                .build();
    }

    public static BlockPattern createLevel5Pattern(MultiblockMachineDefinition definition) {
        var abilities = autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES);
        return FactoryBlockPattern.start()
                .aisle("AABCCCCCCACCCCCCBAA", "###################", "###################", "###################",
                        "###################", "###################", "###################", "###################",
                        "###################", "###################", "###################")
                .aisle("AAD#############DAA", "#zEFFFFFFFFFFFFFEz#", "#G###############G#", "#G###############G#",
                        "#G###############G#", "#G###############G#", "#G###############G#", "#G###############G#",
                        "#HG#############GH#", "###################", "###################")
                .aisle("BD###############DB", "#EDEHIIIIIIIIIHEDE#", "##J#############J##", "##J#############J##",
                        "##J#############J##", "##J#############J##", "##J#############J##", "##JG###########GJ##",
                        "#GK#############KG#", "###################", "###################")
                .aisle("C#################C", "#FELMHHHHNHHHHMLEF#", "###O###########O###", "###O###########O###",
                        "###O###########O###", "###O###########O###", "###OG#########GO###", "##GP###########PG##",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FHM###########MHF#", "####QRFFFFFFFRQ####", "####S#########S####",
                        "####S#########S####", "####S#########S####", "###GS#########SG###", "####I#########I####",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####RTUUUVUUUTR####", "#####W#######W#####",
                        "#####W#######W#####", "#####X#######X#####", "###################", "###################",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######UFFFFFU######",
                        "######S#####S######", "######S#####S######", "######Y#####Y######", "######Z#####Z######",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######FaaQaaF######",
                        "#######bcecb#######", "#######G###G#######", "#######G###G#######", "#######G###G#######",
                        "#######GGGGG#######", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######Fa###aF######",
                        "#######cFFFc#######", "###################", "###################", "###################",
                        "#######G###G#######", "########GGG########", "###################")
                .aisle("A#################A", "#FIN###########NIF#", "####FV#######VF####", "######FQ###QF######",
                        "#######eFFFe#######", "#########f#########", "###################", "#########g#########",
                        "#######G#h#G#######", "########GiG########", "#########j#########")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######Fa###aF######",
                        "#######cFFFc#######", "###################", "###################", "###################",
                        "#######G###G#######", "########GGG########", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######FaaQaaF######",
                        "#######bcdcb#######", "#######G###G#######", "#######G###G#######", "#######G###G#######",
                        "#######GGGGG#######", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####FU#######UF####", "######UFFFFFU######",
                        "######S#####S######", "######S#####S######", "######Y#####Y######", "######Z#####Z######",
                        "###################", "###################", "###################")
                .aisle("C#################C", "#FIH###########HIF#", "####RTUUUVUUUTR####", "#####W#######W#####",
                        "#####W#######W#####", "#####X#######X#####", "###################", "###################",
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
                .aisle("AAD#############DAA", "#zEFFFFFFFFFFFFFEz#", "#G###############G#", "#G###############G#",
                        "#G###############G#", "#G###############G#", "#G###############G#", "#G###############G#",
                        "#HG#############GH#", "###################", "###################")
                .aisle("AABCCCCCCACCCCCCBAA", "###################", "###################", "###################",
                        "###################", "###################", "###################", "###################",
                        "###################", "###################", "###################")
                .where("Z",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("twilightforest:wither_skeleton_skull_candle"))))
                .where("d", Predicates.controller(Predicates.blocks(definition.get())))
                .where("a", Predicates.blocks(BloodMagicBlocks.OBSIDIAN_TILE_PATH.get())
                        .or(abilities))
                .where("F", CMPredicates.BMRuneBlocks)
                .where("B", Predicates.blocks(Blocks.SCULK_CATALYST))
                .where("W",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("i", Predicates.blocks(Blocks.REINFORCED_DEEPSLATE))
                .where("D", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_metal")))
                        .or(abilities))
                .where("z",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_metal"))))
                .where("R", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:airritualstone")))
                        .or(abilities))
                .where("c", Predicates.blocks(BloodMagicBlocks.BLANK_RUNE.get())
                        .or(abilities))
                .where("j", Predicates.blocks(Blocks.BEACON))
                .where("#", Predicates.any())
                .where("J",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("bloodmagic:dungeon_pillar_special"))))
                .where("G", Predicates.blocks(CMBlocks.SOUL_LOCKING_CASING.get()))
                .where("N",
                        Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:teleposer")))
                                .or(abilities))
                .where("Y", Predicates.blocks(Blocks.GLOWSTONE)
                        .or(Predicates.blocks(Blocks.SEA_LANTERN))
                        .or(Predicates.blocks(Blocks.SHROOMLIGHT)))
                .where("K", Predicates.blocks(Blocks.OBSIDIAN))
                .where("L",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("cataclysm:chiseled_end_stone_bricks"))))
                .where("S", Predicates.blocks(Blocks.STONE_BRICKS))
                .where("A",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:witherite_block"))))
                .where("U", Predicates.blocks(BloodMagicBlocks.DUNGEON_BRICK_1.get())
                        .or(abilities))
                .where("b", Predicates.blocks(Blocks.SEA_LANTERN))
                .where("M", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:waterritualstone")))
                        .or(abilities))
                .where("V", Predicates.blocks(BloodMagicBlocks.DUSK_RITUAL_STONE.get())
                        .or(abilities))
                .where("g", Predicates.blocks(Blocks.SOUL_LANTERN))
                .where("P",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:void_lantern_block"))))
                .where("H", Predicates.blocks(CMBlocks.CASING_BLOODLOGIC.get()))
                .where("C", Predicates.blocks(Blocks.SCULK))
                .where("h", Predicates.blocks(Blocks.CHAIN))
                .where("T", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.PURPLE).get()))
                .where("f", Predicates.blocks(BloodMagicBlocks.BLOOD_ALTAR.get()))
                .where("E", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:fireritualstone")))
                        .or(abilities))
                .where("e", Predicates.blocks(Blocks.PRISMARINE_BRICKS))
                .where("Q", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.RED).get()))
                .where("X", Predicates.blocks(Blocks.CRYING_OBSIDIAN))
                .where("I", Predicates.blocks(BloodMagicBlocks.BLOODSTONE_BRICK.get())
                        .or(Predicates.blocks(BloodMagicBlocks.BLOODSTONE.get())))
                .where("O",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:end_stone_pillar"))))
                .build();
    }

    public static BlockPattern createLevel6Pattern(MultiblockMachineDefinition definition) {
        var abilities = autoAbilities(CMRecipeTypes.BLOOD_ALTAR_RECIPES);
        return FactoryBlockPattern.start()
                .aisle("ABBBBBBBBBBBBBBBBBBBBBA", "AC###################CA", "A#####################A",
                        "A#####################A", "A#####################A", "A#####################A",
                        "A#####################A", "A#####################A", "NE###################EN",
                        "#######################", "#######################")
                .aisle("BFEEGGHFIJKLKJIFHGGEEFB", "CMC#################CMC", "#M###################M#",
                        "#M###################M#", "#M###################M#", "#M###################M#",
                        "#M###################M#", "#M###################M#", "ENE#################ENE",
                        "#######################", "#######################")
                .aisle("BEOOPQQQQQQOQQQQQQPOOEB", "#CMC###############CMC#", "##M#################M##",
                        "##M#################M##", "##M#################M##", "##M#################M##",
                        "##M#################M##", "##M#################M##", "#ENE###############ENE#",
                        "#######################", "#######################")
                .aisle("BEOOR#############ROOEB", "##CzIBBBBBBBBBBBBBIzC##", "###E###############E###",
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
                        "########BggZggB########", "#########hikih#########", "#########E###E#########",
                        "#########E###E#########", "#########E###E#########", "#########EEEEE#########",
                        "#######################", "#######################")
                .aisle("BKQ#################QKB", "###BSN###########NSB###", "######Bb#######bB######",
                        "########Bg###gB########", "#########iBBBi#########", "#######################",
                        "#######################", "#######################", "#########E###E#########",
                        "##########EEE##########", "#######################")
                .aisle("BLO#################OLB", "###BSW###########WSB###", "######BK#######KB######",
                        "########BZ###ZB########", "#########kBBBk#########", "###########l###########",
                        "#######################", "###########m###########", "#########E#n#E#########",
                        "##########EME##########", "###########o###########")
                .aisle("BKQ#################QKB", "###BSN###########NSB###", "######Bb#######bB######",
                        "########Bg###gB########", "#########iBBBi#########", "#######################",
                        "#######################", "#######################", "#########E###E#########",
                        "##########EEE##########", "#######################")
                .aisle("BJQ#################QJB", "###BSN###########NSB###", "######Bb#######bB######",
                        "########BggZggB########", "#########hijih#########", "#########E###E#########",
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
                .aisle("BEOOR#############ROOEB", "##CzIBBBBBBBBBBBBBIzC##", "###E###############E###",
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
                        "A#####################A", "A#####################A", "NE###################EN",
                        "#######################", "######################q")
                .where("f",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("twilightforest:wither_skeleton_skull_candle"))))
                .where("j", Predicates.controller(Predicates.blocks(definition.get())))
                .where("D", Predicates.blocks(CMBlocks.SUPERNORMAL_MAGIC_CALCULATE_CORE.get()))
                .where("g", Predicates.blocks(BloodMagicBlocks.OBSIDIAN_TILE_PATH.get())
                        .or(abilities))
                .where("B", CMPredicates.BMRuneBlocks)
                .where("P", Predicates.blocks(Blocks.SCULK_CATALYST))
                .where("L", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:lightritualstone")))
                        .or(abilities))
                .where("c",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:obsidian_bricks"))))
                .where("M", Predicates.blocks(Blocks.REINFORCED_DEEPSLATE))
                .where("J", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:earthritualstone")))
                        .or(abilities))
                .where("R", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_metal")))
                        .or(abilities))
                .where("z",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:dungeon_metal"))))
                .where("H", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:airritualstone")))
                        .or(abilities))
                .where("i", Predicates.blocks(BloodMagicBlocks.BLANK_RUNE.get())
                        .or(abilities))
                .where("o", Predicates.blocks(Blocks.BEACON))
                .where("#", Predicates.any())
                .where("T",
                        Predicates.blocks(ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation("bloodmagic:dungeon_pillar_special"))))
                .where("E", Predicates.blocks(CMBlocks.SOUL_LOCKING_CASING.get()))
                .where("W",
                        Predicates.blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:teleposer")))
                                .or(abilities))
                .where("e", Predicates.blocks(Blocks.GLOWSTONE)
                        .or(Predicates.blocks(Blocks.SEA_LANTERN))
                        .or(Predicates.blocks(Blocks.SHROOMLIGHT)))
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
                        .or(abilities))
                .where("h", Predicates.blocks(Blocks.SEA_LANTERN))
                .where("F", Predicates
                        .blocks(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:waterritualstone")))
                        .or(abilities))
                .where("K", Predicates.blocks(BloodMagicBlocks.DUSK_RITUAL_STONE.get())
                        .or(abilities))
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
                        .or(abilities))
                .where("k", Predicates.blocks(Blocks.PRISMARINE_BRICKS))
                .where("Z", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.RED).get()))
                .where("d", Predicates.blocks(Blocks.CRYING_OBSIDIAN))
                .where("S", Predicates.blocks(BloodMagicBlocks.BLOODSTONE_BRICK.get())
                        .or(Predicates.blocks(BloodMagicBlocks.BLOODSTONE.get())))
                .where("X",
                        Predicates.blocks(
                                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("cataclysm:end_stone_pillar"))))
                .build();
    }

    public static BlockInfo[][][] createPreview(BlockPattern pattern) {
        int[][] repetitions = pattern.aisleRepetitions;
        int[] rep = new int[repetitions.length];
        for (int i = 0; i < repetitions.length; i++) {
            rep[i] = repetitions[i][0];
        }
        return createTopFirstPreview(pattern, rep);
    }

    private static BlockInfo[][][] createTopFirstPreview(BlockPattern pattern, int[] repetition) {
        try {
            var blockMatchesField = BlockPattern.class.getDeclaredField("blockMatches");
            blockMatchesField.setAccessible(true);
            TraceabilityPredicate[][][] blockMatches = (TraceabilityPredicate[][][]) blockMatchesField.get(pattern);

            var setActualRelativeOffset = BlockPattern.class.getDeclaredMethod("setActualRelativeOffset", int.class,
                    int.class, int.class, Direction.class, Direction.class, boolean.class);
            setActualRelativeOffset.setAccessible(true);

            var resetFacing = BlockPattern.class.getDeclaredMethod("resetFacing", BlockPos.class, BlockState.class,
                    Direction.class, BiPredicate.class, Consumer.class);
            resetFacing.setAccessible(true);

            int[] dimensions = pattern.getDimensions();
            int fingerLength = dimensions[0];
            int thumbLength = dimensions[1];
            int palmLength = dimensions[2];

            Object2IntOpenHashMap<SimplePredicate> cacheGlobal = new Object2IntOpenHashMap<>();
            Map<Integer, Object2IntOpenHashMap<SimplePredicate>> cacheLayers = new HashMap<>();
            Map<BlockPos, BlockInfo> blocks = new HashMap<>();
            List<PreviewCell> cells = new ArrayList<>();
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int minZ = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int maxZ = Integer.MIN_VALUE;

            for (int l = 0, x = 0; l < fingerLength; l++) {
                for (int r = 0; r < repetition[l]; r++) {
                    for (int y = thumbLength - 1; y >= 0; y--) {
                        for (int z = 0; z < palmLength; z++) {
                            BlockPos pos = (BlockPos) setActualRelativeOffset.invoke(pattern, z, y, x,
                                    Direction.NORTH, Direction.UP, false);
                            cells.add(new PreviewCell(pos, blockMatches[l][y][z], x));
                            minX = Math.min(pos.getX(), minX);
                            minY = Math.min(pos.getY(), minY);
                            minZ = Math.min(pos.getZ(), minZ);
                            maxX = Math.max(pos.getX(), maxX);
                            maxY = Math.max(pos.getY(), maxY);
                            maxZ = Math.max(pos.getZ(), maxZ);
                        }
                    }
                    x++;
                }
            }

            cells.sort(Comparator.comparingInt((PreviewCell cell) -> cell.pos().getY()).reversed()
                    .thenComparingInt(cell -> cell.pos().getX())
                    .thenComparingInt(cell -> cell.pos().getZ()));

            for (PreviewCell cell : cells) {
                Object2IntOpenHashMap<SimplePredicate> cacheLayer = cacheLayers.computeIfAbsent(cell.sliceIndex(),
                        ignored -> new Object2IntOpenHashMap<>());
                TraceabilityPredicate predicate = cell.predicate();
                boolean find = false;
                BlockInfo[] infos = null;
                for (SimplePredicate limit : predicate.limited) {
                    if (limit.minLayerCount > 0) {
                        if (cacheLayer.getInt(limit) < limit.minLayerCount) {
                            cacheLayer.addTo(limit, 1);
                        } else {
                            continue;
                        }
                        if (cacheGlobal.getInt(limit) < limit.previewCount) {
                            cacheGlobal.addTo(limit, 1);
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                    infos = limit.candidates == null ? null : limit.candidates.get();
                    find = true;
                    break;
                }
                if (!find) {
                    for (SimplePredicate limit : predicate.limited) {
                        if (limit.minCount == -1 && limit.previewCount == -1) continue;
                        if (cacheGlobal.getInt(limit) < limit.previewCount) {
                            cacheGlobal.addTo(limit, 1);
                        } else if (limit.minCount > 0) {
                            if (cacheGlobal.getInt(limit) < limit.minCount) {
                                cacheGlobal.addTo(limit, 1);
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                        infos = limit.candidates == null ? null : limit.candidates.get();
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    for (SimplePredicate common : predicate.common) {
                        if (common.previewCount > 0) {
                            if (cacheGlobal.getInt(common) < common.previewCount) {
                                cacheGlobal.addTo(common, 1);
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                        infos = common.candidates == null ? null : common.candidates.get();
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    for (SimplePredicate common : predicate.common) {
                        if (common.previewCount == -1) {
                            infos = common.candidates == null ? null : common.candidates.get();
                            find = true;
                            break;
                        }
                    }
                }
                if (!find) {
                    for (SimplePredicate limit : predicate.limited) {
                        if (limit.previewCount != -1) continue;
                        if (limit.maxCount != -1 || limit.maxLayerCount != -1) {
                            if (cacheGlobal.getOrDefault(limit, 0) < limit.maxCount) {
                                cacheGlobal.addTo(limit, 1);
                            } else if (cacheLayer.getOrDefault(limit, 0) < limit.maxLayerCount) {
                                cacheLayer.addTo(limit, 1);
                            } else {
                                continue;
                            }
                        }

                        infos = limit.candidates == null ? null : limit.candidates.get();
                        break;
                    }
                }
                blocks.put(cell.pos(), infos == null || infos.length == 0 ? BlockInfo.EMPTY : infos[0]);
            }

            BlockInfo[][][] result = new BlockInfo[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];
            int finalMinX = minX;
            int finalMinY = minY;
            int finalMinZ = minZ;
            blocks.forEach((pos, info) -> {
                try {
                    BiPredicate<BlockPos, Direction> checker = (p, facing) -> {
                        BlockInfo blockInfo = blocks.get(p.relative(facing));
                        if (blockInfo == null || blockInfo.getBlockState().getBlock() == Blocks.AIR) {
                            if (blocks.get(pos).getBlockState().getBlock() instanceof MetaMachineBlock machineBlock) {
                                if (machineBlock.newBlockEntity(BlockPos.ZERO,
                                        machineBlock
                                                .defaultBlockState()) instanceof IMachineBlockEntity machineBlockEntity) {
                                    var machine = machineBlockEntity.getMetaMachine();
                                    if (machine instanceof IMultiController) {
                                        return false;
                                    }
                                    return machine.isFacingValid(facing);
                                }
                            }
                            return true;
                        }
                        return false;
                    };
                    resetFacing.invoke(pattern, pos, info.getBlockState(), null, checker,
                            (Consumer<BlockState>) info::setBlockState);
                    result[pos.getX() - finalMinX][pos.getY() - finalMinY][pos.getZ() - finalMinZ] = info;
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            return result;
        } catch (ReflectiveOperationException e) {
            return pattern.getPreview(repetition);
        }
    }

    @CN("LP不足")
    @EN("Insufficient LP")
    public static Lang failureManaLang_NoEnoughLP;
}
