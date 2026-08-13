package com.magicbee.ctnhmana.registry;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import vazkii.botania.common.block.BotaniaBlocks;

import static com.magicbee.ctnhmana.CTNHMana.REGISTRATE;

public class CMTagPrefixes {

    public static TagPrefix oreLivingrock;

    public static void init() {
        oreLivingrock = REGISTRATE.oreTagPrefix("livingrock", BlockTags.MINEABLE_WITH_PICKAXE)
                .cnlang("活石%s矿石")
                .lang("Livingrock %s Ore")
                .registerOre(() -> BotaniaBlocks.livingrock.defaultBlockState(),
                        () -> CMMaterials.Livingrock,
                        () -> BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE)
                                .strength(2.0F, 10.0F).sound(SoundType.STONE)
                                .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops(),
                        ResourceLocation.tryParse("botania:block/polished_livingrock"), false, false, true);

        oreLivingrock.addSecondaryMaterial(new MaterialStack(CMMaterials.Livingrock, TagPrefix.dust.materialAmount()));
    }
}
