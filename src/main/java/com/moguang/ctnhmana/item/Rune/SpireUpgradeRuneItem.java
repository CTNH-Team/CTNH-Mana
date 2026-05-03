package com.moguang.ctnhmana.item.Rune;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import com.ctnhlang.CN;
import com.ctnhlang.EN;

import java.util.List;

public class SpireUpgradeRuneItem extends ComponentItem {

    @Getter
    private final double speed;
    @Getter
    private final double range;
    @Getter
    private final double capacity;
    @Getter
    private final double conversionEfficiency;

    public SpireUpgradeRuneItem(Properties properties,
                                double speed,
                                double range,
                                double capacity,
                                double conversionEfficiency) {
        super(properties);
        this.speed = speed;
        this.range = range;
        this.capacity = capacity;
        this.conversionEfficiency = conversionEfficiency;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        tooltipComponents.add(spireUpgradeLang[4].translate());
        if (this.speed > 1.0) tooltipComponents.add(spireUpgradeLang[0].translate(speed - 1));
        if (this.range > 1.0) tooltipComponents.add(spireUpgradeLang[1].translate(range - 1));
        if (this.conversionEfficiency > 0.1) tooltipComponents.add(spireUpgradeLang[2].translate(conversionEfficiency));
        if (this.capacity > 1.0) tooltipComponents.add(spireUpgradeLang[3].translate(capacity));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @CN({
            "传输速度提升%.2f倍",
            "范围提升%.2f格",
            "将吸收魔力花效率提升至%.2f",
            "容量扩大%.2f倍",
            "奥法尖塔获得提升:"
    })
    @EN({
            "传输速度提升%.2f倍",
            "范围提升%.2f格",
            "将吸收魔力花效率提升至%.2f",
            "容量扩大%.2f倍",
            "奥法尖塔获得提升:"

    })
    public static Lang[] spireUpgradeLang;
}
