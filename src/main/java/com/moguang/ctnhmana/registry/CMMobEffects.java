package com.moguang.ctnhmana.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.moguang.ctnhmana.api.effect.*;

import static com.moguang.ctnhmana.CTNHMana.MODID;

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
}
