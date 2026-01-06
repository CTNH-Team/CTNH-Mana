package com.moguang.ctnhmana.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class EnglishLangHandler {
    public static void init(RegistrateLangProvider provider){

        provider.add("ctnh.recipe.quasar_eye.info.0", "Activation Cost: %.1f");
        provider.add("ctnh.recipe.quasar_eye.info.1", "Energy Tier: %d");
        provider.add("ctnh.recipe.quasar_eye.info.2", "Activation Tier: %d");
        provider.add("ctnh.common_tooltip.mana_machine.0", "Magic, isn't it?");
        provider.add("ctnh.common_tooltip.mana_machine.1", "§cMana Machines no longer have any parallelism");
        provider.add("ctnh.common_tooltip.mana_machine.2", "Each operational parallel provides 1% time and energy reduction, up to 75% maximum reduction");
        provider.add("ctnh.common_tooltip.mana_machine.3", "§4When voltage is below LUV and recipe voltage tier matches current voltage, increases processing time by 50% (Mana Assembly only increases by 1%)");
        provider.add("ctnh.common_tooltip.mana_machine.4", "Insert §5Quasar Rune§r to activate §5Eye of Astral Mode§r for 100 recipes: Parallelism becomes unlimited, but no longer provides additional time or voltage reduction. Activating this mode doesn't consume the Quasar Rune");
        provider.add("ctnh.common_tooltip.mana_generator.0", "Max power output = (Recipe base output) * (Rune multiplier) * (Rotor max RPM) * (Rotor efficiency/100) * (Machine bonus multiplier)");
        provider.add("ctnh.common_tooltip.mana_generator.1", "Actual output = (Current rotor RPM / Max rotor RPM)^2 * Max power output");
        provider.add("ctnh.common_tooltip.mana_generator.2", "§cWarning: Requires continuous Mana fluid consumption. Insufficient Mana will reduce output to 1/5 of normal. Check UI for consumption rate.");
        provider.add("ctnh.common_tooltip.mana_generator.3", "Inserting Runes into the machine boosts generation efficiency:\n" +
                " Tier I Rune: Output×1.5, Mana cost×0.8, 20% decay chance per 5s\n" +
                " Tier II Rune: Output×2.4, Mana cost×1.2, 10% decay chance per 5s\n" +
                " Tier III Rune: Output×3, Mana cost×0.8, 5% decay chance per 5s\n" +
                " Tier IV Rune: Output×4, Mana cost×0.6, 2.5% decay chance per 5s\n" +
                " Tier V Rune: Output×5, Mana cost×0.3, 2% decay chance per 5s\n" +
                " §5Quasar Rune§r: Output×999, Cost×999, §cBursts with final brilliance amidst devoured stars§r");
        provider.add("ctnh.common_tooltip.basic_mana_consume", "Base consumption is 4mB of Liquid Mana per second. For each voltage tier above §7LV§r, the consumption doubles.");
        provider.add("ctnh.common_tooltip.advanced_mana_consume", "Base consumption is 10mB of Liquid Mana per second. For each voltage tier above §7LV§r, the consumption doubles.");
        provider.add("ctnh.common_tooltip.super_mana_consume", "Base consumption is 12mB of Liquid Mana per second. For each voltage tier above §7LV§r, the consumption doubles.");
        provider.add("ctnh.common_tooltip.zenith_machine.0", "§5Transcendent Magic");
        provider.add("ctnh.multiblock.mana_turbine.info.efficiency", "Generating Efficiency：%d%%");
        provider.add("ctnh.multiblock.mana_turbine.info.consumption_rate", "Consumption Rate：%d");
        provider.add("ctnh.multiblock.quasar_eye.info.rune_energy", "Rune energy: %.2f");
        provider.add("ctnh.multiblock.quasar_eye.info.rune_consumption", "Current rune energy consumption rate: %.2f /100ticks");
        provider.add("ctnh.multiblock.quasar_eye.info.mana_model", "Current mana fuel level: %d");
        provider.add("ctnh.multiblock.quasar_eye.info.mana_production", "Current power generation efficiency: %.2f");
        provider.add("ctnh.multiblock.quasar_eye.info.quasar_parallel", "Time Parallelism: %.2f");
        provider.add("ctnh.multiblock.quasar_eye.info.consumption_parallel","Energy Consumption Rate: %.2f");
        provider.add("ctnh.multiblock.quasar_eye.info.0","Accumulated Energy: %s");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.mana", "Current mana: %.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.twist_consumption", "Twisted rune consumption probability: %.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.starlight_consumption", "Starlight rune consumption probability: %.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.max_mana", "Maximum mana: %.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.mana_required", "Mana required: %.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.mana_consumption", "Mana consumption: %.2f");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.info.stable", "Mana stability value: %.2f"+
        "The rune blocks inside the machine can be replaced to provide different enhancements:\n" +
                "§4Sacrifice Runes and Self-Sacrifice Runes§r----Increase the power generation multiplier for the Life Essence Fortified Mode§r\n" +
                "§3Speed Runes§r----Increase the duration of a single recipe operation (saving demonic will consumption)§r\n" +
                "§eAugment Runes§r----Each rune increases the demonic will concentration difference by 1§r\n" +
                "§cSupercharge Runes§r----Each rune increases the demonic will concentration difference by 5% (multiplied)§r\n");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.0", "Basic Mana Converter");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.1", "Rotor frame tier cannot exceed §bMV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.0", "Advanced Mana Converter");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.1", "Rotor frame tier cannot exceed §5EV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.2", "Consumes 2.25× fuel but generates 4× power output when operating");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.0", "Precision Mana Converter");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.1", "Rotor frame tier cannot exceed §dLuV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.2", "Consumes 3× fuel but generates 16× power output when operating");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.0", "Magical Energy Conservation");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.1", "Rotor frame tier cannot exceed §3UV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.2", "Consumes 4× fuel but generates 24× power output when operating");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.3", "Can only use Laser Cores");
        provider.add("ctnh.multiblock.zenith_circuit_assember.tooltip.0", "Allows the use of §5Magical Resonance Circuit Assembly§r to assemble resonant circuits at lower voltages and with special materials");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.0", "§9Mana's§r §cUltimate Mystery§r, a device capable of creating §5quasars§r now rests in §6your§r hands");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.1", "Machine activation requires §rinitial mana fuel consumption§R, consult JEI for specific values");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.2", "Activating lower-tier recipes at high energy tiers §bwaives activation costs§r");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.3", "§5Rune Energy§r governs output potency. Input §bTier V Runes§r to amplify rune energy and enhance outputs. Use §5Quasar Runes§r to generate massive rune energy");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.4", "Rune energy acquisition logic: §5Before each recipe cycle§r, consumes §cup to one§r of each consumable rune type");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.5", "§cWarning§r: Higher rune energy accelerates §cdepletion rate§r. Efficiency §chalves§r when rune energy falls below 50!");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.6", "Energy efficiency formula: log((rune_energy)/50)+1. Max efficiency: (1 + energy tier)");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.7", "Features time parallelism. Both consumption and duration multiply by parallel factor (efficiency*5)");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.8", "Fuel consumption formula: 1-0.05*Math.max((rune_energy-50)/50,0.75)");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.9", "In generation mode, accumulates 1% of EU output into the Quasar Eye. Gains +1% accumulation per 25 rune energy");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.10", "In creation mode, releases all stored EU. Advanced fuels multiply output. Every 1000E EU generates bonus gas byproduct. Creation mode disabled when stored EU <1E");
        provider.add("ctnh.multiblock.quasar_eye.tooltip.11", "§bGood news§r: This machine won't explode. §cBut no guarantees for future versions!§r");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.0", "§9Arcane Pivot Colossus - Reshaping the Fabric of Scale§r");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.1", "Supports parallel control pods, §cwhich don't provide recipe parallelism§r, only modifying mana input per second");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.2", "Inserted §9Tier V Runes§r determine various machine capabilities");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.3", "§9Starlight Rune§r energy reduces power consumption and enhances machine stability");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.4", "§cDistortion Rune§r energy decreases processing time and increases mana injection frequency, §cat the cost of reduced stability§r");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.5", "§dHorizon Rune§r energy significantly increases mana capacity and utilization efficiency");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.6", "§5Quasar Rune§r energy forces the machine into §coverload state§r while decupling recipe requirements, output, and voltage");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.8", "Stability formula: -((twist_power/3)+((mana/100000)*(Math.max(twist_power/9,1))))+starlight_power*4+5+tier. Machine overloads when stability <0!");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.11", "§cDistortion Rune§r consumption probability: Math.max((twist_power-3)/3,1)*0.01+(Math.max(starlight_power-twist_power,0)*0.01)+(Math.max((100-mana/100000)*0.0005,0)) per operation");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.12", "§9Starlight Rune§r consumption probability: Math.max((starlight_power-3)/3,1)*0.01+(Math.max(twist_power-starlight_power,0)*0.01)+(mana/100000*0.005) per operation");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.13", "§dHorizon Rune§r consumption probability: 0.0025*(horizen_power) per operation");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.15", "Consumes 100*parallelism Kmb(B) liquid mana/sec for beam energization. Non-mana coolant recipes can power the machine");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.16", "Warning: Excess mana beyond capacity won't be refunded. Surplus mana above capacity won't be consumed during operation");
        provider.add("ctnh.multiblock.mana_reactor.tooltip.0","工业魔力奠基者");
        provider.add("ctnh.multiblock.mana_reactor.tooltip.1","允许使用并行控制仓");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.0", "Entropy-reversal matter conversion!");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.1", "Converts mana into liquid mana or vice versa - the latter requiring significantly more energy");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.2", "All mana I/O operations are processed through the central mana pool in the structure");

        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.0", "§8Endless Twisted Power§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.1", "Can use laser warehouse.");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.2", "Applies §8incomprehensible§r parallel to all recipes, reducing energy consumption and operation time by 75%");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.3", "§5You must be crazy to make this machine, and indeed this machine is equally crazy§r.");


        provider.add("ctnh.multiblock.hellforge.info.will", "Will: %s");

        provider.add("ctnh.multiblock.demon_will_generator.tooltip.0", "Harnessing demonic power");
        provider.add("ctnh.multiblock.demon_will_generator.tooltip.1", "Generates power by utilizing the difference in demonic will concentration between the chunks on either side of the machine. The power generation increases exponentially with the concentration difference.");
        provider.add("ctnh.multiblock.demon_will_generator.tooltip.2", "Calculations are based on the will concentration at the demonic alloy blocks on either side of the machine.");
        provider.add("ctnh.multiblock.demon_will_generator.tooltip.3", "The diversity of various demonic wills in the chunks on both sides affects power generation efficiency.");
        provider.add("ctnh.multiblock.demon_will_generator.tooltip.4", "Will cores can be placed inside the machine, transforming it into a specialized mode focused on a specific type of will.");
        provider.add("ctnh.multiblock.demon_will_generator.tooltip.5",
                "The rune blocks inside the machine can be replaced to provide different enhancements:\n" +
                        "§4Sacrifice Runes and Self-Sacrifice Runes§r----Increase the power generation multiplier for the Life Essence Fortified Mode§r\n" +
                        "§3Speed Runes§r----Increase the duration of a single recipe operation (saving demonic will consumption)§r\n" +
                        "§eAugment Runes§r----Each rune increases the demonic will concentration difference by 1§r\n" +
                        "§cSupercharge Runes§r----Each rune increases the demonic will concentration difference by 5% (multiplied)§r\n" +
                        "=============================="
        );
        provider.add("ctnh.multiblock.demon_will_generator.tooltip.6",
                "Insert §4Life Essence§r to activate the Fortified Mode, doubling power output while consuming §a100mb§r of Life Essence per second."
        );

        provider.add("ctnh.multiblock.industrial_altar.tooltip.0", "§4Blood Magic, right at your doorstep!");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.1", "Like the Blood Altar, this structure has an LP input limit. You §4must§r use specific recipes to increase its LP§r\nSee JEI for the recipes that increase LP.");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.2", "Every time the voltage exceeds HV, the LP storage limit increases by 10,000. When reaching LUV, each level increases by 30,000.");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.3", "Each capacity rune increases the LP storage limit by 2500, and the enhanced capacity rune increases it by 5000. After reaching LUV, each level adds an extra 10,000/20,000 LP limit.");

        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.0", "§8Satan woke up to find himself demoted to second place.§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.1", "Enjoy the anguished screams of the suffering souls.§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.2", "Recipe time is always fixed at 1s. Increasing the voltage tier will boost the production of Vital Essence, equivalent to lossless overclocking.§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.3", "Using an incomplete Data Model yields no output. Higher model levels result in greater production.");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.4", "§bSoul Mode:§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.5", "In Soul Mode, the machine does not produce Vital Essence but provides Will to the §bIndustrial Hellforge§r below.");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.6", "Both machines must share the same lava pool, and the controller must be positioned directly above the Hellforge. Please consult JEI for more details.");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.7", "Outputs Will equal to the Vital Essence production (mB)/100,000.");

        provider.add("ctnh.multiblock.hellforge.tooltip.0", "§8Do machines have souls too?§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.1", "Running Hellforge recipes requires meeting the minimum Will requirements.§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.2", "How to fill the machine with Will:§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.3", "1. Kill a mob soaked in §cVital Essence§r near the controller using the §bSword of Perception§r. Will gained is based on the mob's max health.");
        provider.add("ctnh.multiblock.hellforge.tooltip.4", "§8The Manhattan distance to the controller must be less than 8; it doesn’t necessarily have to be the central blood chalice.§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.5", "2. Throw a Soulstone near the controller. The machine will absorb the Will automatically.");
        provider.add("ctnh.multiblock.hellforge.tooltip.6", "3. Use the §4Eternal Well of Suffering§r. Please refer to the tooltip of the respective machine.§r");

        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.0", "§8With unbelievable power.§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.1", "§eNuclear Fusion Reactor Mode:§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.2", "No start-up energy required, no room level restrictions, perform 4/2 overclocking. Provides parallel depending on recipe start-up energy:");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.3", "Less than 160MEU: 16+16*reactor level parallel");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.4", "Greater than 160MEU, less than 320MEU: 4+4*fusion reactor level parallel");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.5", "Greater than 320MEU, less than 480MEU: 1+fusion reactor level parallel");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.6", "§5Twisted Fusion Reactor Mode:§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.7", "Follows the law of conservation of letters.");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.8", "Perhaps this can be used to produce some §9strange things§r...");

    }
}
