package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CMTags {
    public static TagKey<Block> ALFHEIM_STONES = TagUtil.createBlockTag("alfheim_stones");
    public static TagKey<Item> TIER5_RUNES = TagUtil.createItemTag("zenith_runes");
    public static TagKey<Item> MANA_UPDATE_TIER1=TagUtil.createItemTag("mana_update_t1");
}
