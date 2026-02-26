package com.moguang.ctnhmana.registry.multiblock;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
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
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.basemanamutiblockLang;
import static com.moguang.ctnhmana.registry.CMBlocks.LIVING_ROCK_CASING;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.addManaMachineTooltips;

public class ZenithMachine {
    public static void init() {}
    public final static MultiblockMachineDefinition ZENITH_CUTTER = REGISTRATE.multiblock("zenith_cutter", holder-> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder,24,32))
            .cnLangValue("§b天顶思维切削者")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang,1))
            .appearanceBlock(() ->LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.MACERATOR_RECIPES,GTRecipeTypes.FORGE_HAMMER_RECIPES, CMRecipeTypes.MANA_FORGE_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("ABBACCA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "ACCAEEA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "AEEAFFA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "AFFABBA")
                    .aisle("BBBGCCC", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "CCCIEEE", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "EEEIFFF", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "FFFKBBB")
                    .aisle("BBBGCCC", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "CCCIEEE", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "EEEIFFF", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "FFFKBBB")
                    .aisle("AGGAGGA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AIIAIIA", "AKKLKKA")
                    .aisle("FFFGEEE", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "BBBIFFF", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "CCCIBBB", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "EEEKCCC")
                    .aisle("FFFGEEE", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "BBBIFFF", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "CCCIBBB", "DHHIHHD", "DJJIJJD", "DJJIJJD", "DJJIJJD", "DHHIHHD", "EEEKCCC")
                    .aisle("AFF@EEA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "ABBAFFA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "ACCABBA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "ADDADDA", "AEEACCA")
                    .where("A", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get()))
                    .where("B", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("C", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("D", Predicates.blocks(CMBlocks.ENHANCED_MANA_GLASS.get()))
                    .where("E", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("F", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("G", Predicates.blocks(CMBlocks.SOUL_LOCKING_CASING.get()))
                    .where("H", Predicates.blocks(CMBlocks.ZENITH_CASING_GEARBOX.get()))
                    .where("I", Predicates.blocks(CMBlocks.ORICHALCOS_FRAME.get()))
                    .where("J", Predicates.blocks(CMBlocks.FIELD_RESTRICTION_CASING.get()))
                    .where("K", Predicates.blocks(GTBlocks.FILTER_CASING.get()))
                    .where("L", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"), CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition ZENITH_ELECTROLYZER = REGISTRATE.multiblock("zenith_electrolyzer", holder-> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder,24,32))
            .cnLangValue("§b天顶现实解离者")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang,1))
            .appearanceBlock(() ->LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.MACERATOR_RECIPES,GTRecipeTypes.FORGE_HAMMER_RECIPES, CMRecipeTypes.MANA_FORGE_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAAAAAAAAAAA", "ABBBBBBBBBBA", "ACCCCCCCCCCA", "ABBBBBBBBBBA", "AAAAAAAAAAAA")
                    .aisle("ADDDDDDDDDDA", "EFFFFFFFFFFG", "EHHHHHHHHHHC", "EFFFFFFFFFFG", "AIIIIIIIIIIA")
                    .aisle("ADJJJJJJJJDA", "EFKKKKKKKKFG", "EHLLLLLLLLHC", "EFLLLLLLLLFG", "AILLLLLLLMIA")
                    .aisle("ADDDDDDDDJDA", "EFFFFFFFFKFG", "EHHHHHHHHLHC", "EFFFFFFFFLFG", "AIIIIIIIILIA")
                    .aisle("AAAAAAAADJDA", "ANNNNNNAFKFG", "ACCCCCCAHLHC", "ANNNNNNAFLFG", "AAAAAAAAILIA")
                    .aisle("#######ADJDA", "#######NFKFG", "#######CHLHC", "#######NFLFG", "#######AILIA")
                    .aisle("#######ADDDA", "#######NFFFG", "#######CHHHC", "#######NFFFG", "#######AIIIA")
                    .aisle("#######AAAAA", "#######AHHHA", "#######AH@HA", "#######AHHHA", "#######AAAAA")
                    .where("A", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get()))
                    .where("B", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("C", Predicates.blocks(GTBlocks.FUSION_GLASS.get()))
                    .where("D", Predicates.blocks(GTBlocks.CASING_STAINLESS_TURBINE.get()))
                    .where("E", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("F", Predicates.blocks(GTBlocks.CASING_ASSEMBLY_CONTROL.get()))
                    .where("G", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("H", Predicates.blocks(CMBlocks.ARCANE_CONSTRAINT_COATED_GLASS.get()))
                    .where("I", Predicates.blocks(GTBlocks.FILTER_CASING_STERILE.get()))
                    .where("J", Predicates.blocks(CMBlocks.ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK.get()))
                    .where("K", Predicates.blocks(GTBlocks.MACHINE_CASING_LuV.get()))
                    .where("L", Predicates.blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
                    .where("M", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
                    .where("N", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("#", Predicates.any())
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"), CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition BLOODLUST_ASCENDANT = REGISTRATE.multiblock("bloodlust_ascendant", holder-> new com.moguang.ctnhmana.Mutiblock.ZenithMachine(holder,24,32))
            .cnLangValue("§b血域升华者")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang,1))
            .appearanceBlock(() ->LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTRecipeTypes.MACERATOR_RECIPES,GTRecipeTypes.FORGE_HAMMER_RECIPES, CMRecipeTypes.MANA_FORGE_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAAAAAAAAAAA", "ABBBBBBBBBBA", "ACCCCCCCCCCA", "ABBBBBBBBBBA", "AAAAAAAAAAAA")
                    .aisle("ADDDDDDDDDDA", "EFFFFFFFFFFG", "EHHHHHHHHHHC", "EFFFFFFFFFFG", "AIIIIIIIIIIA")
                    .aisle("ADJJJJJJJJDA", "EFKKKKKKKKFG", "EHLLLLLLLLHC", "EFLLLLLLLLFG", "AILLLLLLLMIA")
                    .aisle("ADDDDDDDDJDA", "EFFFFFFFFKFG", "EHHHHHHHHLHC", "EFFFFFFFFLFG", "AIIIIIIIILIA")
                    .aisle("AAAAAAAADJDA", "ANNNNNNAFKFG", "ACCCCCCAHLHC", "ANNNNNNAFLFG", "AAAAAAAAILIA")
                    .aisle("#######ADJDA", "#######NFKFG", "#######CHLHC", "#######NFLFG", "#######AILIA")
                    .aisle("#######ADDDA", "#######NFFFG", "#######CHHHC", "#######NFFFG", "#######AIIIA")
                    .aisle("#######AAAAA", "#######AHHHA", "#######AH@HA", "#######AHHHA", "#######AAAAA")
                    .where("A", Predicates.blocks(CMBlocks.ZENITH_CASING_BLOCK.get()))
                    .where("B", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("C", Predicates.blocks(GTBlocks.FUSION_GLASS.get()))
                    .where("D", Predicates.blocks(GTBlocks.CASING_STAINLESS_TURBINE.get()))
                    .where("E", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("F", Predicates.blocks(GTBlocks.CASING_ASSEMBLY_CONTROL.get()))
                    .where("G", Predicates.blocks(CMBlocks.ALF_STEEL_CASING.get()))
                    .where("H", Predicates.blocks(CMBlocks.ARCANE_CONSTRAINT_COATED_GLASS.get()))
                    .where("I", Predicates.blocks(GTBlocks.FILTER_CASING_STERILE.get()))
                    .where("J", Predicates.blocks(CMBlocks.ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK.get()))
                    .where("K", Predicates.blocks(GTBlocks.MACHINE_CASING_LuV.get()))
                    .where("L", Predicates.blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
                    .where("M", Predicates.blocks(CMBlocks.ZENITH_EYE.get()))
                    .where("N", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("#", Predicates.any())
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .build())
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"), CTNHMana.id("block/overlay/manamachine"))
            .register();
}