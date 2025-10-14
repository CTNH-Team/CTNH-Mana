package com.moguang.ctnhmana.registry;


import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.moguang.ctnhmana.common.Mutiblock.BaseManaMachine;
import com.moguang.ctnhmana.common.Mutiblock.parts.CMPartsAbility;
import vazkii.botania.common.block.BotaniaBlocks;

import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_STAINLESS_CLEAN;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_STEEL_GEARBOX;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.basemanamutiblockLang;
import static com.moguang.ctnhmana.utils.ModUtils.BotaniaRL;
import com.moguang.ctnhmana.common.*;
public class CMMultiblockMachines {
    static {
        REGISTRATE.creativeModeTab(() -> CMCreativeModeTabs.MACHINE);
    }

    public static void init() {
//I LOVE U
    }
    public final static MultiblockMachineDefinition MANA_MACERATOR = REGISTRATE.multiblock("mana_macerator",holder-> new BaseManaMachine(holder,1))
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .appearanceBlock(CASING_STAINLESS_CLEAN)
            .tooltips()
            .recipeModifiers(BaseManaMachine::recipeModifier, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("ABBA", "AAAA", "ABBA")
                    .aisle("ABBA", "ACCA", "ABBA")
                    .aisle("ABBA", "A@DA", "ABBA")
                    .where("A", Predicates.blocks(BotaniaBlocks.livingrockPolished)
                            .or(Predicates.autoAbilities(definition.getRecipeTypes())))
                    .where("B", Predicates.blocks(BotaniaBlocks.livingrockPolished))
                    .where("C", Predicates.blocks(CASING_STEEL_GEARBOX.get()))
                    .where("D", abilities(CMPartsAbility.MANAHATCH))
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .build()
            )
            .workableCasingModel(BotaniaRL("block/polished_livingrock"), GTCEu.id("block/multiblock/generator/large_steam_turbine"))
            .register();
}