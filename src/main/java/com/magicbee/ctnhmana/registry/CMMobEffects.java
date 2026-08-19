package com.magicbee.ctnhmana.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.magicbee.ctnhmana.api.effect.*;

import static com.magicbee.ctnhmana.CTNHMana.MODID;

public class CMMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            MODID);
    public static final RegistryObject<ShroudGazeEffect> ShroudGazing = MOB_EFFECTS.register("shroud_gaze",
            () -> new ShroudGazeEffect(MobEffectCategory.HARMFUL, 0x6D7917));
    public static final RegistryObject<WishingFlyEffect> WishingFlying = MOB_EFFECTS.register("helian_blessing",
            WishingFlyEffect::new);
    public static final RegistryObject<KarmaEffect> Karma = MOB_EFFECTS.register("karma",
            KarmaEffect::new);
    public static final RegistryObject<KarmaFortunaEffect> KarmaFortuna = MOB_EFFECTS.register("karma_fortuna",
            KarmaFortunaEffect::new);
    public static final RegistryObject<BladeUnleashedEffect> Bladeunleashed = MOB_EFFECTS.register("blade_unleashed",
            BladeUnleashedEffect::new);
    public static final RegistryObject<IndexTargetEffect> indextarget = MOB_EFFECTS.register("index_target",
            IndexTargetEffect::new);
    public static final RegistryObject<SoulLeechEffect> SOUL_LEECH = MOB_EFFECTS.register("soul_leech",
            SoulLeechEffect::new);
    public static final RegistryObject<TaintedBloodEffect> TAINTED_BLOOD = MOB_EFFECTS.register("tainted_blood",
            TaintedBloodEffect::new);
    public static final RegistryObject<PhysicalAntagonismEffect> PHYSICAL_ANTAGONISM = MOB_EFFECTS.register(
            "physical_antagonism", PhysicalAntagonismEffect::new);
    public static final RegistryObject<MagicalAntagonismEffect> MAGICAL_ANTAGONISM = MOB_EFFECTS.register(
            "magical_antagonism", MagicalAntagonismEffect::new);
    public static final RegistryObject<PainShieldEffect> PAIN_SHIELD = MOB_EFFECTS.register(
            "pain_shield", PainShieldEffect::new);
    public static final RegistryObject<RootedEffect> ROOTED = MOB_EFFECTS.register(
            "rooted", RootedEffect::new);
    public static final RegistryObject<ArmorBreakEffect> ARMOR_BREAK = MOB_EFFECTS.register(
            "armor_break", ArmorBreakEffect::new);
    public static final RegistryObject<RageEffect> RAGE = MOB_EFFECTS.register(
            "rage", RageEffect::new);
    public static final RegistryObject<WitherCloudEffect> WITHER_CLOUD = MOB_EFFECTS.register(
            "wither_cloud", WitherCloudEffect::new);
}
