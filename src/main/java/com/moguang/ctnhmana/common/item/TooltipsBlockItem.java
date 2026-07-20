package com.moguang.ctnhmana.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import com.moguang.ctnhmana.utils.CTNHManaUtils;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class TooltipsBlockItem extends BlockItem {

    public List<Component> langs = new ArrayList<Component>();
    // 就连这玩意都要单独造一个轮子......

    public TooltipsBlockItem(Block block, Properties properties, Lang lang) {
        super(block, properties);
        this.langs.add(lang.translate());
    }

    public TooltipsBlockItem(Block block, Properties properties, Lang[] langs) {
        super(block, properties);

        CTNHManaUtils.itemTooltipsAdd(langs, this.langs);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.addAll(langs);
    }
}
