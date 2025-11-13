package com.moguang.ctnhmana.registry;


import com.moguang.ctnhmana.CTNHMana;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;


public class CMBlocks {




    @SuppressWarnings("all")
    public static BlockEntry<Block> createCasingBlock(String name,
                                                      String cnName,
                                                      NonNullFunction<BlockBehaviour.Properties, Block> blockSupplier,
                                                      ResourceLocation texture,
                                                      NonNullSupplier<? extends Block> properties,
                                                      Supplier<Supplier<RenderType>> type) {
        return REGISTRATE.block(name, blockSupplier)
                .cnlang(cnName)
                .initialProperties(properties)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(type)
                .blockstate((ctx, prov) -> {
                    prov.simpleBlock(ctx.getEntry(), prov.models().cubeAll(name, texture));
                })
                .tag(TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.tryBuild("forge", "mineable/wrench")), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }
    public static BlockEntry<Block> createCasingBlock(String name, String cnName, ResourceLocation texture) {
        return createCasingBlock(name, cnName, Block::new, texture, () -> Blocks.IRON_BLOCK,
                () -> RenderType::cutoutMipped);
    }


    public static void init() {}
    public static final BlockEntry<Block> ZENITH_CASING_BLOCK = createCasingBlock(
            "zenith_casing", "天顶强化机械方块", CTNHMana.id("block/casings/zenith_casing"));
    public static final BlockEntry<Block> ELEMENTIUM_CASING = createCasingBlock(
            "elementium_casing","源质钢机械外壳", CTNHMana.id("block/casings/elementium_casing"));
    public static final BlockEntry<Block> MANA_STEEL_CASING = createCasingBlock(
            "mana_steel_casing","魔力钢机械外壳", CTNHMana.id("block/casings/mana_steel_casing"));
    public static final BlockEntry<Block> TERRA_STEEL_CASING = createCasingBlock(
            "terra_steel_casing","泰拉钢机械外壳", CTNHMana.id("block/casings/terra_steel_casing"));
    public static final BlockEntry<Block> ZENITH_EYE = createCasingBlock(
            "zenith_eye","§5天顶之眼", CTNHMana.id("block/zenith_eye"));
    public static final BlockEntry<Block> FIELD_RESTRICTION_CASING = createCasingBlock(
            "field_restriction_casing", "虚境立场约束机械方块",CTNHMana.id("block/casings/depth_force_field_stabilizing_casing"));
    public static final BlockEntry<Block> ALF_STEEL_CASING = createCasingBlock(
            "alfsteel_casing","精灵钢机械外壳", CTNHMana.id("block/casings/alfsteel_casing"));
    public static final BlockEntry<Block> ZENITH_CASING_GEARBOX = createCasingBlock(
            "zenith_casing_gearbox","天顶强化魔力齿轮箱机械方块", CTNHMana.id("block/zenith_casing_gearbox"));
}
