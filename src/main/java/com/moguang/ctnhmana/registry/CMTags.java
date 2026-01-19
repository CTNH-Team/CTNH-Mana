package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CMTags {
    public static TagKey<Block> ALFHEIM_STONES = TagUtil.createBlockTag("alfheim_stones");
    public static TagKey<Item> TIER5_RUNES = TagUtil.createItemTag("zenith_runes");
    public static TagKey<Item> MANA_UPDATE_TIER1=TagUtil.createItemTag("mana_update_t1");
    public static TagKey<Item> ELEMENT_FIRE=TagUtil.createItemTag("element_fire");
    public static TagKey<Item> ELEMENT_WATER=TagUtil.createItemTag("element_water");
    public static TagKey<Item> ELEMENT_WIND=TagUtil.createItemTag("element_wind");
    public static TagKey<Item> ELEMENT_EARTH=TagUtil.createItemTag("element_earth");
    public static TagKey<Item> ELEMENT_SIN=TagUtil.createItemTag("element_sin");
    public static TagKey<Item> MANA_FUEL_STACK=TagUtil.createItemTag("mana_fuel_stack");

}
