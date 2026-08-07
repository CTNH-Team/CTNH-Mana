package com.moguang.ctnhmana.registry.multiblock;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.world.level.block.Blocks;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.multiblock.BaseManaMachine;
import com.moguang.ctnhmana.common.multiblock.IndustrialSalvagingMachine;
import com.moguang.ctnhmana.common.multiblock.ManaFuelInfuserMachine;
import com.moguang.ctnhmana.common.parts.CMPartsAbility;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import com.simibubi.create.AllBlocks;
import vazkii.botania.common.block.BotaniaBlocks;

import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.registry.CMBlocks.CASING_MANASTEEL_GEARBOX;
import static com.moguang.ctnhmana.registry.CMBlocks.LIVING_ROCK_CASING;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.addManaMachineTooltips;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.basemanamutiblockLang;

public class ManaMachine {

    public static void init() {}

    public final static MultiblockMachineDefinition MANA_MACERATOR = REGISTRATE
            .multiblock("mana_macerator", holder -> new BaseManaMachine(holder, 1))
            .cnLangValue("§b魔力粉碎机")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang, 1))
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.MACERATOR_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK),
                    GTRecipeModifiers.BATCH_MODE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("A##########", "###########", "#####B#####", "###BBBBB###", "###BBCBB###", "###BCCCB###",
                            "###BBCBB###", "###BBBBB###", "#####B#####", "###########", "###########")
                    .aisle("###########", "####BBB####", "##BBB#BBB##", "##B#####B##", "#BB#B#B#BB#", "#CC#####CC#",
                            "#BB#B#B#BB#", "##B#####B##", "##BBB#BBB##", "####BBB####", "###########")
                    .aisle("#####B#####", "##BBB#BBB##", "#B#######B#", "#B#######B#", "#B##B#B##B#", "#C#######C#",
                            "#B##B#B##B#", "#B#######B#", "#B#######B#", "##BBB#BBB##", "#####B#####")
                    .aisle("###BBBBB###", "##B#####B##", "#B#######B#", "B#########B", "B##BBDBB##B", "B###EFE###B",
                            "B##BBDBB##B", "B#########B", "#B#######B#", "##B#####B##", "###BBBBB###")
                    .aisle("###BGHGB###", "#BB#IHI#BB#", "#B##IHI##B#", "B###IHI###B", "BBBBJ#JBBBB", "C##E#G#E##C",
                            "BBBBJ#JBBBB", "B###IHI###B", "#B##IHI##B#", "#BB#IHI#BB#", "###BHGHB###")
                    .aisle("##BBHGHBB##", "#B##HGH##B#", "B###HGH###B", "B###HGH###B", "C##D#G#D##C", "C##FGEGF##C",
                            "C##D#G#D##C", "B###HGH###B", "B###HGH###B", "#B##HGH##B#", "##BBGIGBB##")
                    .aisle("###BGHGB###", "#BB#IHI#BB#", "#B##IHI##B#", "B###IHI###B", "BBBBJ#JBBBB", "C##E#G#E##C",
                            "BBBBJ#JBBBB", "B###IHI###B", "#B##IHI##B#", "#BB#IHI#BB#", "###BHGHB###")
                    .aisle("###BBBBB###", "##B#####B##", "#B#######B#", "B#########B", "B##BBDBB##B", "B###EFE###B",
                            "B##BBDBB##B", "B#########B", "#B#######B#", "##B#####B##", "###BBBBB###")
                    .aisle("#####B#####", "##BBB#BBB##", "#B#######B#", "#B#######B#", "#B##B#B##B#", "#C#######C#",
                            "#B##B#B##B#", "#B#######B#", "#B#######B#", "##BBB#BBB##", "#####B#####")
                    .aisle("###########", "#####B#####", "##BBB#BBB##", "##B#####B##", "#BB#B#B#BB#", "#CC#####CC#",
                            "#BB#B#B#BB#", "##B#####B##", "##BBB#BBB##", "####BBB####", "###########")
                    .aisle("###########", "###########", "#####B#####", "###BBBBB###", "###B#B#B###", "###BBKBB###",
                            "###B#B#B###", "###BBBBB###", "#####B#####", "###########", "##########A")
                    .where("B", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("D", Predicates.blocks(CMBlocks.PURE_MAGIC_CALCULATE_CORE.get()))
                    .where("J", Predicates.blocks(CMBlocks.MANA_FORGE_CORE.get()))
                    .where("C", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .where("F", Predicates.blocks(CMBlocks.MANA_SHATTER_CORE.get()))
                    .where("#", Predicates.any())
                    .where("E", Predicates.blocks(CMBlocks.MANA_REFINEMENT_CORE.get()))
                    .where("H", Predicates.blocks(CMBlocks.ALFSTEEL_FRAME.get()))
                    .where("K", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("I", Predicates.blocks(CMBlocks.PURE_LOGIC_CASING.get()))
                    .where("A", Predicates.any())
                    .where("G", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition MANA_BENDER = REGISTRATE
            .multiblock("mana_bender", holder -> new BaseManaMachine(holder, 1))
            .cnLangValue("§b魔力卷板机")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang, 1))
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.BENDER_RECIPES, GTRecipeTypes.FORGE_HAMMER_RECIPES,
                    CMRecipeTypes.MANA_FORGE_RECIPES)
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK),
                    GTRecipeModifiers.BATCH_MODE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("A#BBB##", "#######", "#######", "#######", "#######", "#######", "#######")
                    .aisle("#BCBCB#", "###B###", "###B###", "###B###", "###B###", "###B###", "#BBBBB#")
                    .aisle("BCBDBCB", "##EFE##", "##E#E##", "##EFE##", "##E#E##", "##EFE##", "#BCFCB#")
                    .aisle("BBDBDBB", "#BFGFB#", "#B#G#B#", "#BFGFB#", "#B#G#B#", "#BFGFB#", "#BF#FB#")
                    .aisle("BCBDBCB", "##EFE##", "##E#E##", "##EFE##", "##E#E##", "##EFE##", "#BCFCB#")
                    .aisle("#BCBCB#", "###B###", "###B###", "###B###", "###B###", "###B###", "#BBHBB#")
                    .aisle("##BIB##", "#######", "#######", "#######", "#######", "#######", "######A")
                    .where("B", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("G", Predicates.blocks(CMBlocks.MANA_FORGE_CORE.get()))
                    .where("C", Predicates.blocks(CMBlocks.ELEMENTAL_CASING_GEARBOX.get()))
                    .where("D", Predicates.blocks(CASING_MANASTEEL_GEARBOX.get()))
                    .where("I", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("E", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .where("A", Predicates.any())
                    .where("#", Predicates.any())
                    .where("F", Predicates.blocks(CMBlocks.MANA_REFINEMENT_CORE.get()))
                    .where("H", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition MANA_WIREMILL = REGISTRATE
            .multiblock("mana_wiremill", holder -> new BaseManaMachine(holder, 1))
            .cnLangValue("§b魔力线材扎机")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang, 1))
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.WIREMILL_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK),
                    GTRecipeModifiers.BATCH_MODE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("A#BBB##", "###B###", "###B###", "###B###", "###B###")
                    .aisle("#BCDCB#", "##E#E##", "##E#E##", "##E#E##", "##CBC##")
                    .aisle("BCBBBCB", "#EBEBE#", "#E#E#E#", "#EBEBE#", "#CBBBC#")
                    .aisle("BDBBBDB", "B#EDE#B", "B#E#E#B", "B#EDE#B", "BBBBBBB")
                    .aisle("BCB#BCB", "##BEB##", "##BEB##", "##BEB##", "##BBB##")
                    .aisle("#BCFCB#", "#######", "#######", "#######", "#######")
                    .aisle("##BGB##", "#######", "#######", "#######", "######A")
                    .where("B", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("D", Predicates.blocks(CMBlocks.MANA_FORGE_CORE.get()))
                    .where("C", Predicates.blocks(CASING_MANASTEEL_GEARBOX.get()))
                    .where("E", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .where("#", Predicates.any())
                    .where("F", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("A", Predicates.any())
                    .where("G", Predicates.controller(Predicates.blocks(definition.get())))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition MANA_LATHE = REGISTRATE
            .multiblock("mana_lathe", holder -> new BaseManaMachine(holder, 1))
            .cnLangValue("§b魔力车床")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang, 1))
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.LATHE_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK),
                    GTRecipeModifiers.BATCH_MODE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("A#BBB##", "#######", "#######", "#######", "#######")
                    .aisle("#BCBCB#", "###B###", "###B###", "###B###", "###B###")
                    .aisle("BCBBBCB", "##DED##", "##D#D##", "##DED##", "##BEB##")
                    .aisle("BBBBBBB", "#BEFEB#", "#B###B#", "#BEFEB#", "#BEGEB#")
                    .aisle("BBBBBBB", "#DF#FD#", "#D###D#", "#DF#FD#", "#BGHGB#")
                    .aisle("BBBBBBB", "#BEFEB#", "#B###B#", "#BEFEB#", "#BEGEB#")
                    .aisle("BCBBBCB", "##IEI##", "##I#I##", "##IEI##", "##BEB##")
                    .aisle("#BCBCB#", "###I###", "###I###", "###I###", "###B###")
                    .aisle("##BJB##", "#######", "#######", "#######", "######A")
                    .where("B", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("F", Predicates.blocks(CMBlocks.MANA_FORGE_CORE.get()))
                    .where("G", Predicates.blocks(CMBlocks.PURE_MAGIC_CALCULATE_CORE.get()))
                    .where("C", Predicates.blocks(CMBlocks.CASING_MANASTEEL_GEARBOX.get()))
                    .where("J", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("I", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .where("A", Predicates.any())
                    .where("#", Predicates.any())
                    .where("E", Predicates.blocks(CMBlocks.MANA_REFINEMENT_CORE.get()))
                    .where("D", Predicates.blocks(CMBlocks.MANA_STEEL_FRAME.get()))
                    .where("H", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .build())

            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition MANA_ASSEMBLER = REGISTRATE
            .multiblock("mana_assembler", holder -> new BaseManaMachine(holder, 8))
            .cnLangValue("§b魔力组装机")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang, 8))
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.ASSEMBLER_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK),
                    GTRecipeModifiers.BATCH_MODE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("A####BBB#####", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#####BBB#####")
                    .aisle("###BBCDCBB###", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "###BBCDCBB###")
                    .aisle("##BECBBBCEB##", "##C#BBBBB#C##", "##C#BFFFB#C##", "##C#BBBBB#C##", "##CC#####CC##",
                            "##C#BBBBB#C##", "##C#BFFFB#C##", "##C#BBBBB#C##", "##BECBBBCEB##")
                    .aisle("#BEGBHBHBGEB#", "#############", "#############", "#############", "##C#######C##",
                            "#############", "#############", "#############", "#BEGBHBHBGEB#")
                    .aisle("#BCBHBEBHBCB#", "##B#######B##", "##B#CCCCC#B##", "##B#CEBEC#B##", "####CBFBC####",
                            "##B#CEBEC#B##", "##B#CCCCC#B##", "##B#######B##", "#BCBHBEBHBCB#")
                    .aisle("BCBHBE#EBHBCB", "##B#######B##", "##F#CEBEC#F##", "##B#EG#GE#B##", "####B###B####",
                            "##B#EGIGE#B##", "##F#CEBEC#F##", "##B#######B##", "BCBHBE#EBHBCB")
                    .aisle("BDBBE#D#EBBDB", "##B#######B##", "##F#CBFBC#F##", "##B#B#H#B#B##", "####F#D#F####",
                            "##B#B#HIB#B##", "##F#CBFBC#F##", "##B#######B##", "BDBBE#D#EBBDB")
                    .aisle("BCBHBE#EBHBCB", "##B#######B##", "##F#CEBEC#F##", "##B#EG#GE#B##", "####B###B####",
                            "##B#EGIGE#B##", "##F#CEBEC#F##", "##B#######B##", "BCBHBE#EBHBCB")
                    .aisle("#BCBHBEBHBCB#", "##B#######B##", "##B#CCCCC#B##", "##B#CEBEC#B##", "####CBFBC####",
                            "##B#CEBEC#B##", "##B#CCCCC#B##", "##B#######B##", "#BCBHBEBHBCB#")
                    .aisle("#BEGBHBHBGEB#", "#############", "#############", "#############", "##C#######C##",
                            "#############", "#############", "#############", "#BEGBHBHBGEB#")
                    .aisle("##BECBBBCEB##", "##C#BBBBB#C##", "##C#BFFFB#C##", "##C#BBBBB#C##", "##CC#####CC##",
                            "##C#BBBBB#C##", "##C#BFFFB#C##", "##C#BBBBB#C##", "##BECBBBCEB##")
                    .aisle("###BBCDCBB###", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "###BBCDCBB###")
                    .aisle("#####BJB#####", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#####BBB####A")
                    .where("B", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                            .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(abilities(PartAbility.IMPORT_FLUIDS))
                            .or(abilities(PartAbility.IMPORT_ITEMS))
                            .or(abilities(PartAbility.EXPORT_ITEMS))
                            .or(abilities(PartAbility.EXPORT_FLUIDS))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("D", Predicates.blocks(CMBlocks.PURE_MAGIC_CALCULATE_CORE.get()))
                    .where("G", Predicates.blocks(CMBlocks.MANA_FORGE_CORE.get()))
                    .where("F", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .where("J", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("#", Predicates.any())
                    .where("H", Predicates.blocks(CMBlocks.MANA_REFINEMENT_CORE.get()))
                    .where("I", Predicates.any())
                    .where("C", Predicates.blocks(CMBlocks.ORICHALCOS_FRAME.get()))
                    .where("A", Predicates.any())
                    .where("E", Predicates.blocks(CMBlocks.ORICHALCOS_STEEL_CASING_GEARBOX.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition MANA_MIXER = REGISTRATE
            .multiblock("mana_mixer", holder -> new BaseManaMachine(holder, 8))
            .cnLangValue("§b魔力搅拌机")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang, 8))
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.MIXER_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK),
                    GTRecipeModifiers.BATCH_MODE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("A####BBB#####", "#############", "#############", "#############", "#############",
                            "#############", "#####BBB#####")
                    .aisle("###BBCCCBB###", "#####BBB#####", "####BDDDB####", "####BDDDB####", "####BDDDB####",
                            "#####BBB#####", "###BBEEEBB###")
                    .aisle("##BFFCECFFB##", "###BB###BB###", "###DB###BD###", "###BG###GB###", "###DB###BD###",
                            "###BB###BB###", "##BEECCCEEB##")
                    .aisle("#BFCCCECCCFB#", "##B#######B##", "##D#######D##", "##B#######B##", "##D#######D##",
                            "##B#######B##", "#BECCCECCCEB#")
                    .aisle("#BFCGCECGCFB#", "##B###E###B##", "#BB###E###BB#", "#BG#GEEEG#GB#", "#BB###E###BB#",
                            "##B###E###B##", "#BECGCECGCEB#")
                    .aisle("BCCCCFEFCCCCB", "#B#########B#", "#D####F####D#", "#D##EFGFE##D#", "#D####F####D#",
                            "#B####E####B#", "BECCCFEFCCCEB")
                    .aisle("BCEEEEEEEEECB", "#B##E###E##B#", "#D##EFGFE##D#", "#D##EGHGE##D#", "#D##EFGFE##D#",
                            "#B##EEEEE##B#", "BEEEEEEEEECEB")
                    .aisle("BCCCCFEFCCCCB", "#B#########B#", "#D####F####D#", "#D##EFGFE##D#", "#D####F####D#",
                            "#B####E####B#", "BECCCFEFCCCEB")
                    .aisle("#BFCGCECGCFB#", "##B###E###B##", "#BB###E###BB#", "#BG#GEEEG#GB#", "#BB###E###BB#",
                            "##B###E###B##", "#BECGCECGCEB#")
                    .aisle("#BFCCCECCCFB#", "##B#######B##", "##D#######D##", "##B#######B##", "##D#######D##",
                            "##B#######B##", "#BECCCECCCEB#")
                    .aisle("##BFFCECFFB##", "###BB###BB###", "###DB###BD###", "###BG###GB###", "###DB###BD###",
                            "###BB###BB###", "##BEECCCEEB##")
                    .aisle("###BBCCCBB###", "#####BBB#####", "####BBDBB####", "####BDDDB####", "####BBDBB####",
                            "#####BBB#####", "###BBEEEBB###")
                    .aisle("#####B@B#####", "#############", "#############", "#############", "#############",
                            "#############", "#####BBB####A")
                    .where("B", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("G", Predicates.blocks(CMBlocks.PURE_MAGIC_CALCULATE_CORE.get()))
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("H", Predicates.blocks(CMBlocks.PURE_MAGIC_CALCULATE_CORE.get()))
                    .where("#", Predicates.any())
                    .where("E", Predicates.blocks(CMBlocks.ORICHALCOS_FRAME.get()))
                    .where("C", Predicates.blocks(CMBlocks.PURE_LOGIC_CASING.get()))
                    .where("D", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .where("A", Predicates.any())
                    .where("F", Predicates.blocks(CMBlocks.ORICHALCOS_STEEL_CASING_GEARBOX.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();

    public final static MultiblockMachineDefinition MANA_FUEL_INFUSER = REGISTRATE
            .multiblock("mana_fuel_infuser", ManaFuelInfuserMachine::new)
            .cnLangValue("§b注魔单元灌注器")
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(CMRecipeTypes.MANA_FUEL_INFUSER_RECIPES)
            .recipeModifiers(
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .tooltips(ManaFuelInfuserMachine.ManaFuelerLang)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#####AAAAA###", "#####ABBBA###", "#####CBBBC###", "#####CBBBC###", "#####CCCCC###",
                            "######CDC####", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#############", "#######E#####", "######AFA####", "######AFA####", "######CCC####",
                            "#############")
                    .aisle("####AAAAAAA##", "####AC###CA##", "####F#####F##", "####F#####F##", "####CCGGGCC##",
                            "#####CBBBC###", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#######C#####", "#####CECEC###", "#####CHIHC###", "#####C#I#C###", "#####JCCCJ###",
                            "#######A#####")
                    .aisle("###AAAAAAAAA#", "###ACH###HCA#", "###F#######F#", "###F#H###H#F#", "###CKHCCCHKC#",
                            "####DBDDDB###", "####D########", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#####AAAA####",
                            "####AACCC####", "####CLLLLLC##", "####ML###LM##", "####ML###LM##", "####JJCCCJJ##",
                            "######JAJ####")
                    .aisle("#AAAAAAAAAAAA", "#AACH#####HCE", "#CC########FE", "#FF#H#####HFE", "#CCCH#####HCE",
                            "#EECBAAAAABCE", "##ECDAFFFA##A", "##END#######A", "##NN########A", "##N##########",
                            "##N##########", "##N###OOO####", "##N##CPPPC###", "##N##CPPPC###", "##N###OOO####",
                            "##N##########", "##N##########", "##NN#########", "##ENN########", "###EAAFFFA###",
                            "###EECCBCC###", "##ECHLC#CLLC#", "##CCL#####LC#", "##CCL#####LC#", "###JJCC#CCJJ#",
                            "#####JJAJJ###")
                    .aisle("AAAAAAAAAAAAA", "C#####QQQ###A", "CA##########A", "CAAA########A", "CA##C#QQQ#CGA",
                            "CAAAAAPPPADBA", "CCAAJFPPPF##A", "OCCJJ#DBD###A", "#OCJ###F####A", "##CJ###F####A",
                            "##CJ###F####A", "##CJ#OOFOOOOA", "##CJ#P#F#PCCA", "##CJ#P#F#PCCA", "##CJ#OOFOOOOA",
                            "##CJ###F#####", "##CJ###F#####", "##JJ###F#####", "##JJJ#DBD####", "##CCAFPPPFA##",
                            "##CBBBPPPCC##", "#ELBCC###CLE#", "ACC########HA", "AK##########A", "CCCCCC###CCCC",
                            "####JJAAAJJ##")
                    .aisle("AAAAAAAAAAAAA", "C#####QKQRRRR", "DJ#####D####R", "DJAA###D####R", "DJ##C#QKQ#CGR",
                            "DAAAAAPPPADBR", "ODDF#FPIPF##R", "#ODF##BIB###R", "##DF##FIF###R", "##DF##FIF###R",
                            "##DF##FIF###R", "##DF#OFIFODDR", "##DF#PFIFPCCK", "##DF#PFIFPCCK", "##DF#OFIFODDD",
                            "##DF##FIF####", "##DF##FIF####", "##DF##FIF####", "##DDF#BIB####", "##KKAFPIPFA##",
                            "##CBBBPPPBCC#", "EAA#######LCE", "AA#########IF", "AC#########IF", "AC#########CC",
                            "AAAAAAASAAAA#")
                    .aisle("AAAAAAAAAAAAA", "C#####QQQ###A", "CA##########A", "CAAA########A", "CA##C#QQQ#CGA",
                            "CAAAAAPPPADBA", "CCAAJFPPPF##A", "OCCJJ#DBD###A", "#OCJ###F####A", "##CJ###F####A",
                            "##CJ###F####A", "##CJ#OOFOOOOA", "##CJ#P#F#PCCA", "##CJ#P#F#PCCA", "##CJ#OOFOOOOA",
                            "##CJ###F#####", "##CJ###F#####", "##JJ###F#####", "##JJJ#DBD####", "##CCAFPPPFA##",
                            "##CBBBPPPCC##", "#ELBCC###CLE#", "ACC########HA", "AK##########A", "CCCCCC###CCCC",
                            "####JJAAAJJ##")
                    .aisle("#AAAAAAAAAAAA", "#AACH#####HCE", "#CC########FE", "#FF#H#####HFE", "#CCCH#####HCE",
                            "#EECBAAAAABCE", "##ECDAFFFA##A", "##END#######A", "##NN########A", "##N##########",
                            "##N##########", "##N###OOO####", "##N##CPPPC###", "##N##CPPPC###", "##N###OOO####",
                            "##N##########", "##N##########", "##NN#########", "##ENN########", "###EAAFFFA###",
                            "###EECCBCC###", "##ECLLC#CLLC#", "##CCL#####LC#", "##CCL#####LC#", "###JJCC#CCJJ#",
                            "#####JJAJJ###")
                    .aisle("###AAAAAAAAA#", "###ACH###HCA#", "###F#######F#", "###F#H###H#F#", "###CKHCCCHKC#",
                            "####DBDDDB###", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#####AAAA####",
                            "####AACCC####", "####CLLLLLC##", "####ML###LM##", "####ML###LM##", "####JJC#CJJ##",
                            "######JAJ####")
                    .aisle("####AAAAAAA##", "####AC###CA##", "####F#####F##", "####F#####F##", "####CCGGGCC##",
                            "#####CBBBC###", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#######C#####", "#####CECEC###", "#####CHIHC###", "#####C#I#C###", "#####JCCCJ###",
                            "#######A#####")
                    .aisle("#####AAAAA###", "#####ABBBA###", "#####CBTBC###", "#####CBBBC###", "#####CCCCC###",
                            "######CDC####", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#############", "#############", "#############", "#############", "#############",
                            "#############", "#######E#####", "######AFA####", "######AFA####", "######CCC####",
                            "#############")
                    .where("A", Predicates.blocks(CMBlocks.ELEMENTAL_RADIATION_SUPPRESSION_BLOCK.get()))
                    .where("C", Predicates.blocks(LIVING_ROCK_CASING.get()))
                    .where("H", Predicates.blocks(CMBlocks.PURE_MAGIC_CALCULATE_CORE.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes())))
                    .where("Q", Predicates.blocks(CMBlocks.MANA_FORGE_CORE.get()))
                    .where("P", Predicates.blocks(GCYMBlocks.MOLYBDENUM_DISILICIDE_COIL_BLOCK.get()))
                    .where("S", Predicates.abilities(CMPartsAbility.MANAHATCH))
                    .where("D", Predicates.blocks(CMBlocks.ORICHALCOS_FRAME.get()))
                    .where("B", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get()))
                    .where("G", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("M", Predicates.blocks(CMBlocks.ARCANE_SHIELDING_COATED_GLASS.get()))
                    .where("E", Predicates.blocks(CMBlocks.MANA_STEEL_FRAME.get()))
                    .where("O", Predicates.blocks(BotaniaBlocks.corporeaSlab))
                    .where("I", Predicates.blocks(CMBlocks.ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK.get()))
                    .where("J", Predicates.blocks(CMBlocks.ARCANE_REACTOR_BLOCK.get()))
                    .where("R", Predicates.blocks(CMBlocks.ELEMENTIUM_PIPE_CASING.get()))
                    .where("T", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("L", Predicates.blocks(CMBlocks.PURE_LOGIC_CASING.get()))
                    .where("N", Predicates.blocks(BotaniaBlocks.corporeaBrickWall))
                    .where("F", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .where("K", Predicates.blocks(CMBlocks.ORICHALCOS_STEEL_CASING_GEARBOX.get()))
                    .where("#", Predicates.any())
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();

    public final static MultiblockMachineDefinition INDUSTRIAL_SALVAGING = REGISTRATE
            .multiblock("industrial_salvaging", IndustrialSalvagingMachine::new)
            .cnLangValue("§b工业拆解台")
            .tooltips(IndustrialSalvagingMachine.industrialSalvagingLang)
            .appearanceBlock(GCYMBlocks.CASING_INDUSTRIAL_STEAM)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(CMRecipeTypes.INDUSTRIAL_SALVAGING_RECIPES)
            .recipeModifiers(
                    IndustrialSalvagingMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK),
                    GTRecipeModifiers.BATCH_MODE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("###############", "###############", "###############", "###############",
                            "###############",
                            "######AAA######", "#####AAAAA#####", "#####AAAAA#####", "#####AAAAA#####",
                            "######AAA######", "###############", "###############", "###############",
                            "###############", "###############")
                    .aisle("###############", "###############", "###############", "#####AAAAA#####",
                            "####AAAAAAA####",
                            "###AAA###AAA###", "###AA#####AA###", "###AA#####AA###", "###AA#####AA###",
                            "###AAA###AAA###", "####AAAAAAA####", "#####AAAAA#####", "###############",
                            "###############", "###############")
                    .aisle("###############", "###############", "#####AAAAA#####", "####A#####A####",
                            "###A#######A###",
                            "##A#########A##", "##A#########A##", "##A#########A##", "##A#########A##",
                            "##A#########A##", "###B#######B###", "####A#####A####", "#####AAAAA#####",
                            "###############", "###############")
                    .aisle("###############", "#####AAAAA#####", "####A#####A####", "###A#######A###",
                            "##A#########A##",
                            "#A###########A#", "#A###########A#", "#A###########A#", "#A###########A#",
                            "#A###########A#", "##B#########B##", "###B#######B###", "####A#####A####",
                            "#####AAAAA#####", "###############")
                    .aisle("###############", "####AAAAAAA####", "###A#######A###", "##A#########A##",
                            "#A###########A#",
                            "#A###########A#", "#A###########A#", "#A###########A#", "#A###########A#",
                            "#A####CCC####A#", "#A###CCCCC###A#", "##ACCCCCCCCCA##", "###ACCCCCCCA###",
                            "####AACCCAA####", "######AAA######")
                    .aisle("######AAA######", "###AAA###AAA###", "##A#########A##", "#A###########A#",
                            "#A###########A#",
                            "#A###########A#", "A#############A", "A#############A", "A####CCCCC####A",
                            "#A#CCC###CCC#A#", "#ACCC#####CCCA#", "#AC#########CA#", "###############",
                            "###############", "###############")
                    .aisle("#####AAAAA#####", "###AA#####AA###", "##A#########A##", "#A###########A#",
                            "#A###########A#",
                            "A#############A", "A#############A", "A###CCCCCCC###A", "ACCCC#####CCCCA",
                            "ACC#########CCA", "###############", "###############", "###############",
                            "###############", "###############")
                    .aisle("#####AAAAA#####", "###AA#####AA###", "##A#########A##", "#A###########A#",
                            "#A###########A#",
                            "A#############A", "A###CCCDCCC###A", "ECCC#######CCCE", "###############",
                            "###############", "###############", "###############", "###############",
                            "###############", "###############")
                    .aisle("#####AAAAA#####", "###AA#####AA###", "##A#########A##", "#A###########A#",
                            "#A###########A#",
                            "A#############A", "A#############A", "A###CCCCCCC###A", "ACCCC#####CCCCA",
                            "ACC#########CCA", "###############", "###############", "###############",
                            "###############", "###############")
                    .aisle("######AAA######", "###AAA###AAA###", "##A#########A##", "#A###########A#",
                            "#A###########A#",
                            "#A###########A#", "A#############A", "A#############A", "A####CCCCC####A",
                            "#A#CCC###CCC#A#", "#ACCC#####CCCA#", "#AC#########CA#", "###############",
                            "###############", "###############")
                    .aisle("###############", "####AAAAAAA####", "###A#######A###", "##A#########A##",
                            "#A###########A#",
                            "#A###########A#", "#A###########A#", "#A###########A#", "#A###########A#",
                            "#A####CCC####A#", "#A###CCCCC###A#", "##ACCCCCCCCCA##", "###ACCCCCCCA###",
                            "####AACCCAA####", "######AAA######")
                    .aisle("###############", "#####AAAAA#####", "####A#####A####", "###A#######A###",
                            "##A#########A##",
                            "#A###########A#", "#A###########A#", "#A###########A#", "#A###########A#",
                            "#A###########A#", "##A#########A##", "###A#######A###", "####A#####A####",
                            "#####AAAAA#####", "###############")
                    .aisle("###############", "###############", "#####AAAAA#####", "####A#####A####",
                            "###A#######A###",
                            "##A#########A##", "##A#########A##", "##A#########A##", "##A#########A##",
                            "##A#########A##", "###A#######A###", "####A#####A####", "#####AAAAA#####",
                            "###############", "###############")
                    .aisle("###############", "###############", "###############", "#####AAAAA#####",
                            "####AAAAAAA####",
                            "###AAA###AAA###", "###AA#####AA###", "###AA#####AA###", "###AA#####AA###",
                            "###AAA###AAA###", "####AAAAAAA####", "#####AAAAA#####", "###############",
                            "###############", "###############")
                    .aisle("###############", "###############", "###############", "###############",
                            "###############",
                            "######AAA######", "#####AAAAA#####", "#####AA@AA#####", "#####AAAAA#####",
                            "######AAA######", "###############", "###############", "###############",
                            "###############", "###############")
                    .where("#", Predicates.any())
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("A", Predicates.blocks(GCYMBlocks.CASING_INDUSTRIAL_STEAM.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("B", Predicates.blocks(Blocks.BLACK_WOOL))
                    .where("C", Predicates.blocks(Blocks.BLACK_CONCRETE))
                    .where("D", Predicates.blocks(AllBlocks.ROSE_QUARTZ_LAMP.get()))
                    .where("E", Predicates.blocks(GTBlocks.CASING_BRONZE_GEARBOX.get()))
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/gcym/industrial_steam_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
}
