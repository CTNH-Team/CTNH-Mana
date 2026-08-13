package com.magicbee.ctnhmana.common.blocks;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import com.magicbee.ctnhmana.CTNHMana;
import com.magicbee.ctnhmana.registry.CMMaterials;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum CoilType implements StringRepresentable, ICoilType {

    SHROUD_MANA("shroud_mana", 10801, 77, 7, 6, CMMaterials.Ultra_Mana, CTNHMana.id("block/casings/coils/shroud_coil"));
    // ULTRA_MANA("ulta_mana", 7201, 16, 5, 6, CTNHMaterials.QUASER_MANA,
    // CTNHCore.id("block/casings/coils/ultra_mana_coil_block"));

    @NotNull
    private final String name;
    // electric blast furnace properties
    private final int coilTemperature;
    // multi smelter properties
    private final int level;
    @Getter
    private final int tier;
    private final int energyDiscount;
    @NotNull
    private final Material material;
    @NotNull
    @Getter
    private final ResourceLocation texture;

    CoilType(String name, int coilTemperature, int level, int tier, int energyDiscount, Material material,
             ResourceLocation texture) {
        this.name = name;
        this.coilTemperature = coilTemperature;
        this.level = level;
        this.tier = tier;
        this.energyDiscount = energyDiscount;
        this.material = material;
        this.texture = texture;
    }

    @NotNull
    @Override
    public String toString() {
        return getName();
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return name;
    }
}
