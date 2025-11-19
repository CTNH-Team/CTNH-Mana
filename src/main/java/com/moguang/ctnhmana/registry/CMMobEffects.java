package com.moguang.ctnhmana.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.moguang.ctnhmana.api.effect.*;
import static com.moguang.ctnhmana.CTNHMana.MODID;

public class CMMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final RegistryObject<ShroudGazeEffect> ShroudGazing = MOB_EFFECTS.register("shroud_gaze",
            () -> new ShroudGazeEffect(MobEffectCategory.HARMFUL, 0x6D7917));
    // 2. 注册你的自定义状态效果（示例：假设你有一个PurpleHazeEffect类）
}
