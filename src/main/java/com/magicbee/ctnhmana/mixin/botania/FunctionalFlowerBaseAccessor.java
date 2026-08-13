package com.magicbee.ctnhmana.mixin.botania;

import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FunctionalFlowerBase.class, remap = false)
public interface FunctionalFlowerBaseAccessor {

    @Accessor("maxMana")
    @Mutable
    void ctnhmana$setMaxMana(int maxMana);

    @Accessor("maxTransfer")
    @Mutable
    void ctnhmana$setMaxTransfer(int maxTransfer);
}
