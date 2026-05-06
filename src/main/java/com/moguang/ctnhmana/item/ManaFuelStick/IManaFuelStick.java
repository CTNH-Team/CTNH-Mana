package com.moguang.ctnhmana.item.ManaFuelStick;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.util.List;

public class IManaFuelStick extends ComponentItem {

    public long heat = 0;
    public int stability = 0;
    public int durability = 0;
    public Item disintegration = null;

    public IManaFuelStick(Properties properties, long heat, int stability, int durability) {
        super(properties.durability(durability));
        this.heat = heat;
        this.stability = stability;
        this.durability = durability;
    }

    public IManaFuelStick(Properties properties, long heat, int stability, int durability, Item disintegration) {
        super(properties.durability(durability));
        this.heat = heat;
        this.stability = stability;
        this.durability = durability;
        this.disintegration = disintegration;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        tooltipComponents.add(FuelStickLang[0].translate(heat));
        tooltipComponents.add(FuelStickLang[1].translate(stability));
        tooltipComponents.add(FuelStickLang[2].translate(durability - stack.getDamageValue(), durability));
    }

    @CN({
            "提供热量: %d/s",
            "占用稳定度: %d/s",
            "存储的魔力热：%d/%d"

    })
    @EN({
            "提供热量: %d/s",
            "占用稳定度: %d/s",
            "存储的魔力热：%d/%d"

    })
    public static Lang[] FuelStickLang;
}
