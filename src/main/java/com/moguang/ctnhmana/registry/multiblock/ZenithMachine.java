package com.moguang.ctnhmana.registry.multiblock;

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

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.Mutiblock.BaseManaMachine;
import com.moguang.ctnhmana.Mutiblock.parts.CMPartsAbility;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMRecipeTypes;

import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.api.pattern.Predicates.autoAbilities;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.*;
import static com.moguang.ctnhmana.registry.CMBlocks.LIVING_ROCK_CASING;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.addManaMachineTooltips;

public class ZenithMachine {

    public static void init() {}

    public final static MultiblockMachineDefinition ZENITH_CUTTER = REGISTRATE
            .multiblock("zenith_cutter", holder -> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder, 24, 32))
            .cnLangValue("§5天顶思维切削者")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang, 1))
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.CUTTER_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("ABBACCA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "ACCAEEA", "ADDADDA", "ADDADDA",
                            "ADDADDA", "ADDADDA", "ADDADDA", "AEEAFFA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA",
                            "ADDADDA", "AFFABBA")
                    .aisle("BBBGCCC", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "CCCIEEE", "DHHIHHD", "DKKIKKD",
                            "DLLILLD", "DKKIKKD", "DHHIHHD", "EEEIFFF", "DHHIHHD", "DKKIKKD", "DLLILLD", "DKKIKKD",
                            "DHHIHHD", "FFFMBBB")
                    .aisle("BBBGCCC", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "CCCIEEE", "DHHIHHD", "DKKIKKD",
                            "DLLILLD", "DKKIKKD", "DHHIHHD", "EEEIFFF", "DHHIHHD", "DKKIKKD", "DLLILLD", "DKKIKKD",
                            "DHHIHHD", "FFFMBBB")
                    .aisle("AGGAGGA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA",
                            "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA",
                            "AIIAIIA", "AMMNMMA")
                    .aisle("FFFGEEE", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "BBBIFFF", "DHHIHHD", "DKKIKKD",
                            "DLLILLD", "DKKIKKD", "DHHIHHD", "CCCIBBB", "DHHIHHD", "DKKIKKD", "DLLILLD", "DKKIKKD",
                            "DHHIHHD", "EEEMCCC")
                    .aisle("FFFGEEE", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "BBBIFFF", "DHHIHHD", "DKKIKKD",
                            "DLLILLD", "DKKIKKD", "DHHIHHD", "CCCIBBB", "DHHIHHD", "DKKIKKD", "DLLILLD", "DKKIKKD",
                            "DHHIHHD", "EEEMCCC")
                    .aisle("AFF@EEA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "ABBAFFA", "ADDADDA", "ADDADDA",
                            "ADDADDA", "ADDADDA", "ADDADDA", "ACCABBA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA",
                            "ADDADDA", "AEEACCA")
                    .where("K", Predicates.blocks(CMBlocks.MANA_FORGE_CORE.get()))
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("L", Predicates.blocks(CMBlocks.MANA_SHATTER_CORE.get()))
                    .where("I", Predicates.blocks(CMBlocks.ORICHALCOS_FRAME.get()))
                    .where("B", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("A", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("F", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("H", Predicates.blocks(CMBlocks.ZENITH_CASING_GEARBOX.get()))
                    .where("M", Predicates.blocks(GTBlocks.FILTER_CASING.get()))
                    .where("E", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("C", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("G", Predicates.blocks(CMBlocks.SOUL_LOCKING_CASING.get()))
                    .where("J", Predicates.blocks(CMBlocks.MANA_REFINEMENT_CORE.get()))
                    .where("N", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
                    .where("D", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/zenith_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition ZENITH_ELECTROLYZER = REGISTRATE
            .multiblock("zenith_electrolyzer",
                    holder -> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder, 7, 32))
            .cnLangValue("§5天顶概念解离者")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang, 7))
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.ELECTROLYZER_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("A################", "######BBBBB######", "###BBBCCDEEBBB###", "##BCCCFCDEFEEEB##",
                            "##BCFBBCDEBBFEB##", "##BCBCCCDEEEBEB##", "#BCFBCDDDDDEBFEB#", "BBCCCCDDDDDEEEEBB",
                            "BDDDDDDDDDDDDDDDB", "BBGGGGDDDDDHHHHBB", "#BGFBGDDDDDHBFHB#", "##BGBGGGDHHHBHB##",
                            "##BGFBBGDHBBFHB##", "##BGGGFGDHFHHHB##", "###BBBGGDHHBBB###", "######BBDBB######",
                            "#######BBB#######")
                    .aisle("######BBBBB######", "###BBB##I##BBB###", "##B#####I#####B##", "#B######I######B#",
                            "#B######I######B#", "#B######I######B#", "B######I#I######B", "B#####I###I#####B",
                            "DIIIII#####IIIIID", "B#####I###I#####B", "B######I#I######B", "#B######I######B#",
                            "#B######I######B#", "#B######I######B#", "##B#####I#####B##", "###BBB##I##BBB###",
                            "######BJJJB######")
                    .aisle("######BBBBB######", "###DDD#IFI#DDD###", "##D####IFI####D##", "#D#####IFI#####D#",
                            "#D#####IFI#####D#", "#D#####IFI#####D#", "D#####IFKFI#####D", "DIIIIIF#K#FIIIIID",
                            "#FFFFFKKLKKFFFFF#", "DIIIIIF#K#FIIIIID", "D#####IFKFI#####D", "#D#####IFI#####D#",
                            "#D#####IFI#####D#", "#D#####IFI#####D#", "##D####IFI####D##", "###DDD#IFI#DDD###",
                            "######BJMJB######")
                    .aisle("######BBBBB######", "###BBB##I##BBB###", "##B#####I#####B##", "#B######I######B#",
                            "#B######I######B#", "#B######I######B#", "B######I#I######B", "B#####I###I#####B",
                            "DIIIII#####IIIIID", "B#####I###I#####B", "B######I#I######B", "#B######I######B#",
                            "#B######I######B#", "#B######I######B#", "##B#####I#####B##", "###BBB##I##BBB###",
                            "######BJJJB######")
                    .aisle("#################", "######BB#BB######", "###BBBHHDGGBBB###", "##BHHHFHDGFGGGB##",
                            "##BHFBBHDGBBFGB##", "##BHBHHHDGGGBGB##", "#BHFBHDDDDDGBFGB#", "BBHHHHDDDDDGGGGB#",
                            "BDDDDDDDDDDDDDDD#", "BBEEEEDD@DDCCCCB#", "#BEFBEDDDDDCBFCB#", "##BEBEEEDCCCBCB##",
                            "##BEFBBEDCBBFCB##", "##BEEEFEDCFCCCB##", "###BBBEEDCCBBB###", "######BBDBB######",
                            "#######BBB######A")
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("A", Predicates.any())
                    .where("K", Predicates.blocks(CMBlocks.ORICHALCOS_FRAME.get()))
                    .where("H", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("B", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get())
                            .or(autoAbilities(definition.getRecipeTypes())))
                    .where("E", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("F", Predicates.blocks(CMBlocks.ZENITH_CASING_GEARBOX.get()))
                    .where("J", Predicates.blocks(GTBlocks.FILTER_CASING_STERILE.get()))
                    .where("D", Predicates.blocks(CMBlocks.ARCANE_CONSTRAINT_COATED_GLASS.get())
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("G", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("I", Predicates.blocks(CMBlocks.ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK.get()))
                    .where("#", Predicates.any())
                    .where("C", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("M", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
                    .where("L", Predicates.blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/zenith_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
    // public final static MultiblockMachineDefinition BLOODLUST_ASCENDANT = REGISTRATE
    // .multiblock("bloodlust_ascendant",
    // holder -> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder, 24, 32))
    // .cnLangValue("§b血域升华者")
    // .tooltips(addManaMachineTooltips(basemanamutiblockLang, 1))
    // .appearanceBlock(() -> LIVING_ROCK_CASING.get())
    // .rotationState(RotationState.NON_Y_AXIS)
    // .recipeTypes(GTRecipeTypes.MACERATOR_RECIPES, GTRecipeTypes.FORGE_HAMMER_RECIPES,
    // CMRecipeTypes.MANA_FORGE_RECIPES)
    // .recipeModifiers(BaseManaMachine::recipeModifier,
    // GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
    // .pattern(definition -> FactoryBlockPattern.start()
    // .aisle("AAAAAAAAAAAA", "ABBBBBBBBBBA", "ACCCCCCCCCCA", "ABBBBBBBBBBA", "AAAAAAAAAAAA")
    // .aisle("ADDDDDDDDDDA", "EFFFFFFFFFFG", "EHHHHHHHHHHC", "EFFFFFFFFFFG", "AIIIIIIIIIIA")
    // .aisle("ADJJJJJJJJDA", "EFKKKKKKKKFG", "EHLLLLLLLLHC", "EFLLLLLLLLFG", "AILLLLLLLMIA")
    // .aisle("ADDDDDDDDJDA", "EFFFFFFFFKFG", "EHHHHHHHHLHC", "EFFFFFFFFLFG", "AIIIIIIIILIA")
    // .aisle("AAAAAAAADJDA", "ANNNNNNAFKFG", "ACCCCCCAHLHC", "ANNNNNNAFLFG", "AAAAAAAAILIA")
    // .aisle("#######ADJDA", "#######NFKFG", "#######CHLHC", "#######NFLFG", "#######AILIA")
    // .aisle("#######ADDDA", "#######NFFFG", "#######CHHHC", "#######NFFFG", "#######AIIIA")
    // .aisle("#######AAAAA", "#######AHHHA", "#######AH@HA", "#######AHHHA", "#######AAAAA")
    // .where("A", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get())
    // .or(autoAbilities(definition.getRecipeTypes()))
    // )
    // .where("B", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
    // .where("C", Predicates.blocks(GTBlocks.FUSION_GLASS.get()))
    // .where("D", Predicates.blocks(GTBlocks.CASING_STAINLESS_TURBINE.get()))
    // .where("E", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
    // .where("F", Predicates.blocks(GTBlocks.CASING_ASSEMBLY_CONTROL.get()))
    // .where("G", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
    // .where("H", Predicates.blocks(CMBlocks.ARCANE_CONSTRAINT_COATED_GLASS.get()))
    // .where("I", Predicates.blocks(GTBlocks.FILTER_CASING_STERILE.get()))
    // .where("J", Predicates.blocks(CMBlocks.ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK.get()))
    // .where("K", Predicates.blocks(GTBlocks.MACHINE_CASING_LuV.get()))
    // .where("L", Predicates.blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
    // .where("M", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
    // .where("N", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
    // .where("#", Predicates.any())
    // .where("@", Predicates.controller(Predicates.blocks(definition.get())))
    // .build())
    // .workableCasingModel(CTNHMana.id("block/casings/zenith_casing"),
    // CTNHMana.id("block/overlay/manamachine"))
    // .register();
    public final static MultiblockMachineDefinition ZENITH_CIRCUIT_ASSEMBLER = REGISTRATE
            .multiblock("zenith_assembler",
                    holder -> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder, 7, 32))
            .cnLangValue("§5天顶逻辑组合者")
            .tooltips(addManaMachineTooltips(basezenithmutiblockLang, 7))
            .tooltips(zenithAssemblerLang.translate())
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES, CMRecipeTypes.ZENITH_CIRCUIT)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#########", "#########", "#########", "##BBBBB##", "##BBBBB##", "##BB@BB##", "##BBBBB##",
                            "##BBBBB##", "#########", "#########", "########A")
                    .aisle("#########", "#########", "#IIFFFHH#", "#I#####H#", "#F#####F#", "#F#####F#", "#F#####F#",
                            "#G#####E#", "#GGFFFEE#", "#########", "#########")
                    .aisle("#########", "##BBBBB##", "#I#####H#", "B#######B", "B###H###B", "B##JDJ##B", "B###G###B",
                            "B#######B", "#G#####E#", "##BBBBB##", "#########")
                    .aisle("###BBB###", "##BBBBB##", "#F#####F#", "B###H###B", "C##KHK##C", "C#JJDJJ#C", "C##KGK##C",
                            "B###G###B", "#F#####F#", "##B###B##", "###LLL###")
                    .aisle("###BMB###", "##BBNBB##", "#F#####F#", "B##IDE##B", "C#IIDEE#C", "D#DDDDD#D", "C#EEDII#C",
                            "B##EDI##B", "#F#####F#", "##B#D#B##", "###LOL###")
                    .aisle("###BBB###", "##BBBBB##", "#F#####F#", "B###G###B", "C##KGK##C", "C#JJDJJ#C", "C##KHK##C",
                            "B###H###B", "#F#####F#", "##B###B##", "###LLL###")
                    .aisle("#########", "##BBBBB##", "#E#####G#", "B#######B", "B###G###B", "B##JDJ##B", "B###H###B",
                            "B#######B", "#H#####I#", "##BBBBB##", "#########")
                    .aisle("#########", "#########", "#EEFFFGG#", "#E#####G#", "#F#####F#", "#F#####F#", "#F#####F#",
                            "#H#####I#", "#HHFFFII#", "#########", "#########")
                    .aisle("A########", "#########", "#########", "##BBBBB##", "##BCCCB##", "##BCDCB##", "##BCCCB##",
                            "##BBBBB##", "#########", "#########", "#########")
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("A", Predicates.any())
                    .where("D", Predicates.blocks(CMBlocks.SUPERNORMAL_MAGIC_CALCULATE_CORE.get()))
                    .where("I", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("B", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get())
                            .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                            .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(abilities(PartAbility.IMPORT_FLUIDS))
                            .or(abilities(PartAbility.IMPORT_ITEMS))
                            .or(abilities(PartAbility.EXPORT_ITEMS))
                            .or(abilities(PartAbility.EXPORT_FLUIDS))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))

                    .where("G", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("J", Predicates.blocks(CMBlocks.ZENITH_CASING_GEARBOX.get()))
                    .where("L", Predicates.blocks(GTBlocks.FILTER_CASING_STERILE.get()))
                    .where("E", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("N", Predicates.blocks(GTBlocks.MACHINE_CASING_ZPM.get()))
                    .where("F", Predicates.blocks(CMBlocks.ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK.get()))
                    .where("#", Predicates.any())
                    .where("H", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("K", Predicates.blocks(CMBlocks.MANA_REFINEMENT_CORE.get()))
                    .where("M", Predicates.blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where("O", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
                    .where("C", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/zenith_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition ZENITH_LASER = REGISTRATE
            .multiblock("zenith_laser",
                    holder -> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder, 7, 32))
            .cnLangValue("§5天顶空间映射者")
            .tooltips(addManaMachineTooltips(basezenithmutiblockLang, 7))
            .tooltips(zenithLaserLang.translate())
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES, CMRecipeTypes.ANTIPHASE_ETCHING)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("###AA@AA###", "##AABCBAA##", "#AADDDDDAA#", "AADDDDDDDAA", "#AADDDDDAA#", "##AABCBAA##",
                            "###AABAA###")
                    .aisle("##AABEBAA##", "#A#######A#", "AF#######FA", "GE#######EG", "AF#######FA", "#A#######A#",
                            "##AAHIJAA##")
                    .aisle("#AABECEBAA#", "A#########A", "G#########G", "D#########D", "G#########G", "A#########A",
                            "#AAHHKJJAA#")
                    .aisle("#AAEBEBEAA#", "A###EFE###A", "D###LLL###D", "D###LCL###D", "D###LLL###D", "A###EFE###A",
                            "#AAHBIBJAA#")
                    .aisle("#AABEIEBAA#", "A##ECICE##A", "D##L###L##D", "D##LCICL##D", "D##L###L##D", "A##ECICE##A",
                            "#AABMMMBAA#")
                    .aisle("#ABEIIIEBA#", "A#ECIIICE#A", "D#L##I##L#D", "D#LCICICL#D", "D#L##I##L#D", "A#ECIIICE#A",
                            "#ABEMKMEBA#")
                    .aisle("#AABEIEBAA#", "A##ECICE##A", "D##L###L##D", "D##LCICL##D", "D##L###L##D", "A##ECICE##A",
                            "#AABMMMBAA#")
                    .aisle("#AAEBEBEAA#", "A###EFE###A", "D###LLL###D", "D###LCL###D", "D###LLL###D", "A###EFE###A",
                            "#AANBIBOAA#")
                    .aisle("#AABECEBAA#", "A#########A", "G#########G", "D#########D", "G#########G", "A#########A",
                            "#AANNKOOAA#")
                    .aisle("##AABEBAA##", "#A#######A#", "AF#######FA", "GE#######EG", "AF#######FA", "#A#######A#",
                            "##AANIOAA##")
                    .aisle("###AAAAA###", "##AJJDHHA##", "#AGDDDDDGA#", "AGDDDDDDDGA", "#AGDDDDDGA#", "##ANNDOOA##",
                            "###AABAA###")
                    .where("F", Predicates.blocks(CMBlocks.MANA_FORGE_CORE.get()))
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("C", Predicates.blocks(CMBlocks.SUPERNORMAL_MAGIC_CALCULATE_CORE.get()))
                    .where("G", Predicates.blocks(CMBlocks.ORICHALCOS_FRAME.get()))
                    .where("O", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("A", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get()))
                    .where("J", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("B", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))

                    .where("M", Predicates.blocks(GTBlocks.FILTER_CASING_STERILE.get()))
                    .where("#", Predicates.any())
                    .where("L", Predicates.blocks(CMBlocks.ARCANE_CONSTRAINT_COATED_GLASS.get()))
                    .where("N", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("I", Predicates.blocks(CMBlocks.ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK.get()))
                    .where("#", Predicates.any())
                    .where("H", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("E", Predicates.blocks(CMBlocks.MANA_REFINEMENT_CORE.get()))
                    .where("K", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
                    .where("D", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/zenith_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition ZENITH_EXTRUDER = REGISTRATE
            .multiblock("zenith_extruder",
                    holder -> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder, 32, 32))
            .cnLangValue("§5天顶现实塑造者")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang, 1))
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.EXTRUDER_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("A#######", "########", "########", "########", "########", "########", "########",
                            "########", "########", "########", "########", "########", "########", "########",
                            "########", "########", "########")
                    .aisle("##BBBBB#", "########", "########", "########", "########", "########", "########",
                            "########", "########", "########", "########", "########", "########", "########",
                            "########", "########", "########")
                    .aisle("#BBBBBBB", "##CBDBE#", "###DFD##", "###BDB##", "########", "########", "########",
                            "########", "########", "########", "########", "########", "########", "########",
                            "########", "########", "###BBB##")
                    .aisle("BBBBBBBB", "#CC#G#EE", "##C#F#E#", "##C###E#", "##CBDBE#", "##CCGEE#", "########",
                            "########", "########", "########", "########", "########", "########", "########",
                            "########", "###BBB##", "##BDDDB#")
                    .aisle("BBBBBBBB", "#B##G##B", "#D##F##D", "#B#####B", "##B###B#", "##C###E#", "###B#B##",
                            "###B#B##", "###BHB##", "###BHB##", "###BHB##", "###BHB##", "###BHB##", "###BHB##",
                            "###BHB##", "##BDDDB#", "#BDIIIDB")
                    .aisle("BBBBBBBB", "#DGGGGGD", "#FFFGFFF", "#D##G##D", "##D#G#D#", "##G#G#G#", "###HGH##",
                            "###HGH##", "###HGH##", "###HGH##", "###HGH##", "###HGH##", "###HGH##", "###HGH##",
                            "###HGH##", "##BDGDB#", "#BDIJIDB")
                    .aisle("BBBBBBBB", "#B##G##B", "#D##F##D", "#B#####B", "##B###B#", "##K###L#", "###BHB##",
                            "###BHB##", "###BHB##", "###BHB##", "###BHB##", "###BHB##", "###BHB##", "###BHB##",
                            "###BHB##", "##BDDDB#", "#BDIIIDB")
                    .aisle("BBBBBBBB", "#KK#G#LL", "##K#F#L#", "##K###L#", "##KBDBL#", "##KKGLL#", "########",
                            "########", "########", "########", "########", "########", "########", "########",
                            "########", "###BBB##", "##BDDDB#")
                    .aisle("#BBBBBBB", "##KBDBL#", "###DFD##", "###BDB##", "########", "########", "########",
                            "########", "########", "########", "########", "########", "########", "########",
                            "########", "########", "###BBB##")
                    .aisle("##BB@BB#", "########", "########", "########", "########", "########", "########",
                            "########", "########", "########", "########", "########", "########", "########",
                            "########", "########", "#######A")
                    .where("D", Predicates.blocks(CMBlocks.MANA_FORGE_CORE.get()))
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("A", Predicates.any())
                    .where("F", Predicates.blocks(CMBlocks.ORICHALCOS_FRAME.get()))
                    .where("C", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("B", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("E", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("G", Predicates.blocks(CMBlocks.ZENITH_CASING_GEARBOX.get()))
                    .where("I", Predicates.blocks(GTBlocks.FILTER_CASING_STERILE.get()))
                    .where("K", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("#", Predicates.any())
                    .where("L", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("J", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
                    .where("H", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/zenith_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();

    public final static MultiblockMachineDefinition ZENITH_DISTILLATION = REGISTRATE
            .multiblock("zenith_distillation",
                    holder -> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder, 7, 32))
            .cnLangValue("§5天顶灵能升华者")
            .tooltips(addManaMachineTooltips(basezenithmutiblockLang, 7))
            .tooltips(zenithDistillationLang.translate())
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.DISTILLATION_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("A##BBB###", "#########", "#########", "#########", "#########", "#########", "#########",
                            "#########", "#########", "#########", "#########", "#########", "#########", "#########",
                            "#########", "#########", "###BBB###")
                    .aisle("##BBBBB##", "##BCDCB##", "##CDDDC##", "#########", "#########", "#########", "#########",
                            "#########", "#########", "#########", "#########", "#########", "#########", "#########",
                            "##CEEEC##", "##BCECB##", "##BBBBB##")
                    .aisle("#BBBBBBB#", "#BCBDBCB#", "#CBBBBBC#", "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##",
                            "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##",
                            "#CBBBBBC#", "#BCBEBCB#", "#BBBBBBB#")
                    .aisle("BBBBBBBBB", "#CBBDBBC#", "#GBHHHBI#", "##F###F##", "##F###F##", "##F###F##", "##F###F##",
                            "##F###F##", "##F###F##", "##F###F##", "##F###F##", "##F###F##", "##F###F##", "##F###F##",
                            "#IBHHHBG#", "#CBBEBBC#", "BBBJJJBBB")
                    .aisle("BBBBBBBBB", "#GGGKIII#", "#GBHHHBI#", "##F#H#F##", "##F#H#F##", "##F#H#F##", "##F#H#F##",
                            "##F#H#F##", "##F#H#F##", "##F#H#F##", "##F#H#F##", "##F#H#F##", "##F#H#F##", "##F#H#F##",
                            "#IBHHHBG#", "#IIIKGGG#", "BBBJLJBBB")
                    .aisle("BBBBBBBBB", "#CBBEBBC#", "#GBHHHBI#", "##F###F##", "##F###F##", "##F###F##", "##F###F##",
                            "##F###F##", "##F###F##", "##F###F##", "##F###F##", "##F###F##", "##F###F##", "##F###F##",
                            "#IBHHHBG#", "#CBBDBBC#", "BBBJJJBBB")
                    .aisle("#BBBBBBB#", "#BCBEBCB#", "#CBBBBBC#", "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##",
                            "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##", "##BFFFB##",
                            "#CBBBBBC#", "#BCBDBCB#", "#BBBBBBB#")
                    .aisle("##BBBBB##", "##BCECB##", "##CEEEC##", "#########", "#########", "#########", "#########",
                            "#########", "#########", "#########", "#########", "#########", "#########", "#########",
                            "##CDDDC##", "##BCDCB##", "##BBBBB##")
                    .aisle("###B@B###", "#########", "#########", "#########", "#########", "#########", "#########",
                            "#########", "#########", "#########", "#########", "#########", "#########", "#########",
                            "#########", "#########", "###BBB##A")
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("E", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("B", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("G", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("C", Predicates.blocks(CMBlocks.ZENITH_CASING_GEARBOX.get()))
                    .where("J", Predicates.blocks(GTBlocks.FILTER_CASING_STERILE.get()))
                    .where("I", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("H", Predicates.blocks(CMBlocks.ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK.get()))
                    .where("#", Predicates.any())
                    .where("D", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("K", Predicates.blocks(CMBlocks.MANA_REFINEMENT_CORE.get()))
                    .where("A", Predicates.any())
                    .where("L", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
                    .where("F", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/zenith_casing"),
                    CTNHMana.id("block/overlay/manamachine"))
            .register();
}
