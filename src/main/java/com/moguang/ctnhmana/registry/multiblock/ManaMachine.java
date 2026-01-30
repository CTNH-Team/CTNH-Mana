package com.moguang.ctnhmana.registry.multiblock;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.Mutiblock.BaseManaMachine;
import com.moguang.ctnhmana.Mutiblock.parts.CMPartsAbility;
import com.moguang.ctnhmana.registry.CMBlocks;
import com.moguang.ctnhmana.registry.CMMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import vazkii.botania.common.block.BotaniaBlocks;

import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.basemanamutiblockLang;
import static com.moguang.ctnhmana.registry.CMBlocks.CASING_MANASTEEL_GEARBOX;
import static com.moguang.ctnhmana.registry.CMBlocks.LIVING_ROCK_CASING;
import static com.moguang.ctnhmana.registry.CMMultiblockMachines.addManaMachineTooltips;
import static com.moguang.ctnhmana.utils.ModUtils.BotaniaRL;

public class ManaMachine {
    public static void init() {}
    public final static MultiblockMachineDefinition MANA_MACERATOR = REGISTRATE.multiblock("mana_macerator", holder-> new BaseManaMachine(holder,1))
            .cnLangValue("§b魔力粉碎机")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang,1))
            .appearanceBlock(() ->LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("ABBA", "AAAA", "ABBA")
                    .aisle("ABBA", "ACCA", "ABBA")
                    .aisle("ABBA", "A@AA", "ABBA")
                    .where("A", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1))
                    )
                    .where("B", Predicates.blocks(LIVING_ROCK_CASING.get()))
                    .where("C", Predicates.blocks(CASING_STEEL_GEARBOX.get()))
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .build()
            )
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"), GTCEu.id("block/multiblock/generator/large_steam_turbine"))
            .register();
    public final static MultiblockMachineDefinition MANA_BENDER = REGISTRATE.multiblock("mana_bender", holder -> new BaseManaMachine(holder, 1))
            .cnLangValue("§b魔力卷板机")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang,1))
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.BENDER_RECIPES)
            .appearanceBlock(() -> LIVING_ROCK_CASING.get())
            .recipeModifiers(BaseManaMachine::recipeModifier, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("EEEEE", "ABBBA", "ABBBA", "ACCCA")
                    .aisle("EDDDE", "B###B", "B###B", "CDDDC")
                    .aisle("EDDDE", "B#F#B", "B#F#B", "CDDDC")
                    .aisle("EDDDE", "B###B", "B###B", "CDDDC")
                    .aisle("EEEEE", "AE@EA", "AEEEA", "ACCCA")
                    .where("A", Predicates.blocks(LIVING_ROCK_CASING.get()))
                    .where("B", Predicates.frames(CMMaterials.ManaSteel))
                    .where("C", Predicates.blocks(Blocks.SMOOTH_QUARTZ_STAIRS))
                    .where("D", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("#", Predicates.any())
                    .where("E", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1))
                    )
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("F", Predicates.blocks(CASING_MANASTEEL_GEARBOX.get()))
                    .build()
            )
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"), GTCEu.id("block/multiblock/generator/large_steam_turbine"))
            .register();
    public final static MultiblockMachineDefinition MANA_WIREMILL = REGISTRATE.multiblock("mana_wiremill", holder -> new BaseManaMachine(holder, 1))
            .cnLangValue("§b魔力线材扎机")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang,1))
            .appearanceBlock(() ->LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.WIREMILL_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAA", "BBB", "CCC")
                    .aisle("AAA", "BBB", "CCC")
                    .aisle("AAA", "B@B", "CCC")
                    .where("A", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1))
                    )
                    .where("B", Predicates.frames(CMMaterials.Elementium))
                    .where("C", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .build()
            )
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"), CTNHMana.id("block/overlay/manamachine"))
            .register();
    public final static MultiblockMachineDefinition MANA_LATHE = REGISTRATE.multiblock("mana_lathe", holder -> new BaseManaMachine(holder, 1))
            .cnLangValue("§b魔力车床")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang,1))
            .appearanceBlock(() ->LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.LATHE_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("ABA", "AAA", "AAA", "CAC")
                    .aisle("ABA", "D#D", "D#D", "CAC")
                    .aisle("ABA", "D#D", "D#D", "CAC")
                    .aisle("ABA", "D#D", "D#D", "CAC")
                    .aisle("AAA", "A@A", "AAA", "CAC")
                    .where("A", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1)))
                    .where("B", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("C", Predicates.blocks(Blocks.SMOOTH_QUARTZ_STAIRS))
                    .where("D", Predicates.frames(CMMaterials.ManaSteel))
                    .where("#", Predicates.any())
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .build()
            )

            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"), GTCEu.id("block/multiblock/generator/large_steam_turbine"))
            .register();
    public final static MultiblockMachineDefinition MANA_ASSEMBLER = REGISTRATE.multiblock("mana_assembler", holder -> new BaseManaMachine(holder, 8))
            .cnLangValue("§b魔力组装机")
            .tooltips(addManaMachineTooltips(basemanamutiblockLang,8))
            .appearanceBlock(() ->LIVING_ROCK_CASING.get())
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.ASSEMBLER_RECIPES)
            .recipeModifiers(BaseManaMachine::recipeModifier, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("ABBBBBA", "ABBBBBA", "ABBBBBA", "ACCCCCA", "AAAAAAA")
                    .aisle("BDEEEDB", "B#####B", "B#####B", "C#####C", "ABBBBBA")
                    .aisle("BEDFDEB", "B#####B", "B##G##B", "C#####C", "ABBBBBA")
                    .aisle("BEFDFEB", "B##H##B", "B#GHG#B", "C##H##C", "ABBBBBA")
                    .aisle("BEDFDEB", "B#####B", "B##G##B", "C#####C", "ABBBBBA")
                    .aisle("BDEEEDB", "B#####B", "B#####B", "C#####C", "ABBBBBA")
                    .aisle("ABBBBBA", "ABB@BBA", "ABBBBBA", "ACCCCCA", "AAAAAAA")
                    .where("A", Predicates.frames(CMMaterials.AlfSteel))
                    .where("B", Predicates.blocks(LIVING_ROCK_CASING.get())
                            .or(abilities(PartAbility.INPUT_ENERGY).setExactLimit(1))
                            .or(abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(abilities(PartAbility.IMPORT_FLUIDS))
                            .or(abilities(PartAbility.IMPORT_ITEMS))
                            .or(abilities(PartAbility.EXPORT_ITEMS))
                            .or(abilities(PartAbility.EXPORT_FLUIDS))
                            .or(abilities(CMPartsAbility.MANAHATCH).setExactLimit(1))
                    )
                    .where("C", Predicates.blocks(BotaniaBlocks.manaGlass))
                    .where("D", Predicates.blocks(CMBlocks.ELEMENTIUM_CASING.get()))
                    .where("E", Predicates.blocks(CMBlocks.MANA_STEEL_CASING.get()))
                    .where("#", Predicates.any())
                    .where("F", Predicates.blocks(CMBlocks.TERRA_STEEL_CASING.get()))
                    .where("G", Predicates.blocks(CMBlocks.CASING_MANASTEEL_GEARBOX.get()))
                    .where("H", Predicates.blocks(CASING_TITANIUM_GEARBOX.get()))

                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .build()
            )
            .workableCasingModel(CTNHMana.id("block/casings/living_rock_casing"), GTCEu.id("block/multiblock/generator/large_steam_turbine"))
            .register();
}
