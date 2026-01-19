package com.moguang.ctnhmana.item.ManaFuelStick;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.EN;

import java.util.List;

public class IManaFuelStick extends ComponentItem {
    public long heat=0;
    public int stability=0;
    public int durability=0;
    public IManaFuelStick(Properties properties, long heat, int stability, int durability) {
        super(properties.durability(durability));
        this.heat=heat;
        this.stability=stability;
        this.durability=durability;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        tooltipComponents.add(FuelStickLang[0].translate(heat));
        tooltipComponents.add(FuelStickLang[1].translate(stability));
        tooltipComponents.add(FuelStickLang[2].translate(durability));
    }
    @CN(
            {
                    "提供热量: %d/s",
                    "占用稳定度: %d/s",
                    "存储魔力上限: %d"

            }
    )
    @EN(
            {
                    "提供热量: %d/s",
                    "占用稳定度: %d/s",
                    "存储魔力上限: %d"

            }
    )
    public static Lang[] FuelStickLang;
}
