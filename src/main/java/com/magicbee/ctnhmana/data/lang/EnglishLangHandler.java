package com.magicbee.ctnhmana.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class EnglishLangHandler {

    public static void init(RegistrateLangProvider provider) {
        provider.add("ctnh.common_tooltip.mana_machine.0", "Magic, isn't it?");
        provider.add("ctnh.common_tooltip.mana_machine.1", "§cMana Machines no longer have any parallelism");
        provider.add("ctnh.common_tooltip.mana_machine.2",
                "Each operational parallel provides 1% time and energy reduction, up to 75% maximum reduction");
        provider.add("ctnh.common_tooltip.mana_machine.3",
                "§4When voltage is below LuV and recipe voltage tier matches current voltage, increases processing time by 50% (Mana Assembly only increases by 1%)");
        provider.add("ctnh.common_tooltip.mana_machine.4",
                "Insert §5Quasar Rune§r to activate §5Eye of Astral Mode§r for 100 recipes: Parallelism becomes unlimited, but no longer provides additional time or voltage reduction. Activating this mode doesn't consume the Quasar Rune");
        provider.add("ctnh.common_tooltip.mana_generator.0",
                "Max power output = (Recipe base output) * (Rune multiplier) * (Rotor max RPM) * (Rotor efficiency/100) * (Machine bonus multiplier)");
        provider.add("ctnh.common_tooltip.mana_generator.1",
                "Actual output = (Current rotor RPM / Max rotor RPM)^2 * Max power output");
        provider.add("ctnh.common_tooltip.mana_generator.2",
                "§cWarning: Requires continuous Mana fluid consumption. Insufficient Mana will reduce output to 1/5 of normal. Check UI for consumption rate.");
        provider.add("ctnh.common_tooltip.mana_generator.3",
                "Inserting Runes into the machine boosts generation efficiency:\n" +
                        " Tier I Rune: Output×1.5, Mana cost×0.8, 20% decay chance per 5s\n" +
                        " Tier II Rune: Output×2.4, Mana cost×1.2, 10% decay chance per 5s\n" +
                        " Tier III Rune: Output×3, Mana cost×0.8, 5% decay chance per 5s\n" +
                        " Tier IV Rune: Output×4, Mana cost×0.6, 2.5% decay chance per 5s\n" +
                        " Tier V Rune: Output×5, Mana cost×0.3, 2% decay chance per 5s\n" +
                        " §5Quasar Rune§r: Output×999, Cost×999, §cBursts with final brilliance amidst devoured stars§r");
        provider.add("ctnh.common_tooltip.basic_mana_consume",
                "Base consumption is 4mB of Liquid Mana per second. For each voltage tier above §7LV§r, the consumption doubles.");
        provider.add("ctnh.common_tooltip.advanced_mana_consume",
                "Base consumption is 10mB of Liquid Mana per second. For each voltage tier above §7LV§r, the consumption doubles.");
        provider.add("ctnh.common_tooltip.super_mana_consume",
                "Base consumption is 12mB of Liquid Mana per second. For each voltage tier above §7LV§r, the consumption doubles.");
        provider.add("ctnh.common_tooltip.zenith_machine.0", "§5Transcendent Magic");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.0", "Basic Mana Converter");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier1.tooltip.1", "Rotor frame tier cannot exceed §bMV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.0", "Advanced Mana Converter");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.1", "Rotor frame tier cannot exceed §5EV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier2.tooltip.2",
                "Consumes 2.25× fuel but generates 4× power output when operating");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.0", "Precision Mana Converter");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.1",
                "Rotor frame tier cannot exceed §dLuV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier3.tooltip.2",
                "Consumes 3× fuel but generates 16× power output when operating");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.0", "Magical Energy Conservation");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.1", "Rotor frame tier cannot exceed §3UV§r");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.2",
                "Consumes 4× fuel but generates 24× power output when operating");
        provider.add("ctnh.multiblock.mana_generator_turbine_tier4.tooltip.3", "Can only use Laser Cores");
        provider.add("ctnh.multiblock.zenith_circuit_assember.tooltip.0",
                "Allows the use of §5Magical Resonance Circuit Assembly§r to assemble resonant circuits at lower voltages and with special materials");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.0",
                "§9Arcane Pivot Colossus - Reshaping the Fabric of Scale§r");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.1",
                "Supports parallel control pods, §cwhich don't provide recipe parallelism§r, only modifying mana input per second");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.2",
                "Inserted §9Tier V Runes§r determine various machine capabilities");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.3",
                "§9Starlight Rune§r energy reduces power consumption and enhances machine stability");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.4",
                "§cDistortion Rune§r energy decreases processing time and increases mana injection frequency, §cat the cost of reduced stability§r");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.5",
                "§dHorizon Rune§r energy significantly increases mana capacity and utilization efficiency");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.6",
                "§5Quasar Rune§r energy forces the machine into §coverload state§r while decupling recipe requirements, output, and voltage");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.8",
                "Stability formula: -((twist_power/3)+((mana/100000)*(Math.max(twist_power/9,1))))+starlight_power*4+5+tier. Machine overloads when stability <0!");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.11",
                "§cDistortion Rune§r consumption probability: Math.max((twist_power-3)/3,1)*0.01+(Math.max(starlight_power-twist_power,0)*0.01)+(Math.max((100-mana/100000)*0.0005,0)) per operation");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.12",
                "§9Starlight Rune§r consumption probability: Math.max((starlight_power-3)/3,1)*0.01+(Math.max(twist_power-starlight_power,0)*0.01)+(mana/100000*0.005) per operation");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.13",
                "§dHorizon Rune§r consumption probability: 0.0025*(horizen_power) per operation");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.15",
                "Consumes 100*parallelism Kmb(B) liquid mana/sec for beam energization. Non-mana coolant recipes can power the machine");
        provider.add("ctnh.multiblock.nicoll_dyson_beams.tooltip.16",
                "Warning: Excess mana beyond capacity won't be refunded. Surplus mana above capacity won't be consumed during operation");
        provider.add("ctnh.multiblock.mana_reactor.tooltip.0", "工业魔力奠基者");
        provider.add("ctnh.multiblock.mana_reactor.tooltip.1", "允许使用并行控制仓");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.0", "Entropy-reversal matter conversion!");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.1",
                "Converts mana into liquid mana or vice versa - the latter requiring significantly more energy");
        provider.add("ctnh.multiblock.mana_condenser.tooltips.2",
                "All mana I/O operations are processed through the central mana pool in the structure");
        provider.add("effect.ctnhmana.soul_leech", "Soul Leech");
        provider.add("effect.ctnhmana.tainted_blood", "Tainted Blood");
        provider.add("effect.ctnhmana.physical_antagonism", "Physical Antagonism");
        provider.add("effect.ctnhmana.magical_antagonism", "Arcane Antagonism");
        provider.add("effect.ctnhmana.pain_shield", "Pain Shield");

        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.0", "§8Endless Twisted Power§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.1", "Can use laser warehouse.");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.2",
                "Applies §8incomprehensible§r parallel to all recipes, reducing energy consumption and operation time by 75%");
        provider.add("ctnh.multiblock.twisted_fusion_mk_infinity.tooltip.3",
                "§5You must be crazy to make this machine, and indeed this machine is equally crazy§r.");

        provider.add("ctnh.multiblock.hellforge.info.will", "Will: %s");

        provider.add("ctnh.multiblock.industrial_altar.tooltip.0", "§4Blood Magic, right at your doorstep!");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.1",
                "Like the Blood Altar, this structure has an LP input limit. You §4must§r use specific recipes to increase its LP§r\nSee JEI for the recipes that increase LP.");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.2",
                "Every time the voltage exceeds HV, the LP storage limit increases by 10,000. When reaching LuV, each level increases by 30,000.");
        provider.add("ctnh.multiblock.industrial_altar.tooltip.3",
                "Each capacity rune increases the LP storage limit by 2500, and the enhanced capacity rune increases it by 5000. After reaching LuV, each level adds an extra 10,000/20,000 LP limit.");

        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.0",
                "§8Satan woke up to find himself demoted to second place.§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.1",
                "Enjoy the anguished screams of the suffering souls.§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.2",
                "Recipe time is always fixed at 1s. Increasing the voltage tier will boost the production of Vital Essence, equivalent to lossless overclocking.§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.3",
                "Using an incomplete Data Model yields no output. Higher model levels result in greater production.");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.4", "§bSoul Infusion Mode:§r");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.5",
                "When linked to the §bIndustrial Hellforge§r below, use the button to enable Soul Infusion Mode: no Vital Essence is produced; Will is supplied to the Hellforge instead.");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.6",
                "Both machines must share the same lava pool, and the controller must be positioned directly above the Hellforge. Please consult JEI for more details.");
        provider.add("ctnh.multiblock.eternal_well_of_suffer.tooltip.7",
                "Outputs Will equal to the Vital Essence production (mB)/1,000,000. In Soul Infusion Mode, parallel ignores output capacity.");

        provider.add("ctnh.multiblock.hellforge.tooltip.0", "§8Do machines have souls too?§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.1",
                "Running Hellforge recipes requires meeting the minimum Will requirements.§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.2", "How to fill the machine with Will:§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.3",
                "1. Kill a mob soaked in §cVital Essence§r near the controller using the §bSword of Perception§r. Will gained is based on the mob's max health.");
        provider.add("ctnh.multiblock.hellforge.tooltip.4",
                "§8The Manhattan distance to the controller must be less than 8; it doesn’t necessarily have to be the central blood chalice.§r");
        provider.add("ctnh.multiblock.hellforge.tooltip.5",
                "2. Throw a Soulstone near the controller. The machine will absorb the Will automatically.");
        provider.add("ctnh.multiblock.hellforge.tooltip.6",
                "3. Use the §4Eternal Well of Suffering§r. Please refer to the tooltip of the respective machine.§r");

        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.0", "§8With unbelievable power.§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.1", "§eNuclear Fusion Reactor Mode:§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.2",
                "No start-up energy required, no room level restrictions, perform 4/2 overclocking. Provides parallel depending on recipe start-up energy:");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.3", "Less than 160MEU: 16+16*reactor level parallel");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.4",
                "Greater than 160MEU, less than 320MEU: 4+4*fusion reactor level parallel");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.5",
                "Greater than 320MEU, less than 480MEU: 1+fusion reactor level parallel");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.6", "§5Twisted Fusion Reactor Mode:§r");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.7", "Follows the law of conservation of letters.");
        provider.add("ctnh.multiblock.twisted_fusion_mk1.tooltip.8",
                "Perhaps this can be used to produce some §9strange things§r...");

        provider.add("ctnhmana.jade.eternal_wos.will_output", "§9Soul Infusion Output: %s Raw Will");
        provider.add("config.jade.plugin_gtceu.manahatch_status_provider", "Manahatch Status");
        provider.add("config.jade.plugin_gtceu.manamachine_status_provider", "Manamachine Status");
        provider.add("config.jade.plugin_gtceu.bloodaltar_status_provider", "Bloodalter Status");
        provider.add("config.jade.plugin_gtceu.manamachine_mana_status_provider", "Manamachine Mana Status");
        provider.add("config.jade.plugin_gtceu.eternal_wos_status_provider", "Eternal Well Soul Infusion");
        provider.add("config.jade.plugin_gtceu.gem_sublimator_status_provider", "Gem Engraver Progress");
        provider.add("config.jade.plugin_ctnhmana.mana_pool_status", "Manapool Status");

        provider.add("ctnhmana.entry.anatta_lotus", "Anatta Lotus");
        provider.add("ctnhmana.anatta_lotus.lexicon.1", "");
        provider.add("ctnhmana.anatta_lotus.lexicon.2", "");
        provider.add("ctnhmana.entry.genethistle", "Genethistle");
        provider.add("ctnhmana.genethistle.lexicon.1", "");
        provider.add("ctnhmana.genethistle.lexicon.2", "");
        provider.add("ctnhmana.entry.pararosia", "ParaRosia");
        provider.add("ctnhmana.pararosia.lexicon.1", "");
        provider.add("ctnhmana.pararosia.lexicon.2", "");
        provider.add("ctnhmana.entry.demon_flytrap", "Demon Flytrap");
        provider.add("ctnhmana.demon_flytrap.lexicon.1", "");
        provider.add("ctnhmana.demon_flytrap.lexicon.2", "");
        provider.add("ctnhmana.entry.blood_antiaris", "Blood Antiaris");
        provider.add("ctnhmana.blood_antiaris.lexicon.1", "");
        provider.add("ctnhmana.blood_antiaris.lexicon.2", "");

        provider.add("ctnhmana.recipe.blood_ritual.ritual_id", "Ritual: %s");
        provider.add("ctnhmana.recipe.blood_ritual.lp_cost", "LP Cost: %s");
        provider.add("ctnhmana.recipe.meteor_ritual.lp_cost", "LP Cost: %s");
        provider.add("ctnhmana.recipe.meteor_ritual.marker_tip",
                "Marker item must be a dropped item within 21 blocks of the controller");
    }
}
