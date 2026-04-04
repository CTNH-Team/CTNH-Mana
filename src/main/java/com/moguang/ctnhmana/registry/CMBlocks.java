package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.block.property.GTBlockStateProperties;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.common.data.models.GTModels;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.blocks.CoilType;
import com.moguang.ctnhmana.common.blocks.FrameBlock;
import com.moguang.ctnhmana.common.blocks.RuneBlock;
import com.moguang.ctnhmana.item.TooltipsBlockItem;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import vazkii.botania.forge.block.ForgeSpecialFlowerBlock;

import java.util.function.Supplier;

import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.*;

public class CMBlocks {

    public static BlockEntry<RuneBlock> createRuneBlock(String name, String cnName, ResourceLocation texture) {
        return createRuneBlock(name, cnName, RuneBlock::new, texture, () -> Blocks.IRON_BLOCK,
                () -> RenderType::cutoutMipped);
    }

    @SuppressWarnings("all")
    public static BlockEntry<RuneBlock> createRuneBlock(String name,
                                                        String cnName,
                                                        NonNullFunction<BlockBehaviour.Properties, RuneBlock> blockSupplier,
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
                .tag(TagKey.create(BuiltInRegistries.BLOCK.key(),
                        ResourceLocation.tryBuild("forge", "mineable/wrench")), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    public static BlockEntry<ActiveBlock> createActiveCasing(String name, String cnName, String baseModelPath) {
        return REGISTRATE.block(name, ActiveBlock::new)
                .cnlang(cnName)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GTModels.createActiveModel(CTNHMana.id(baseModelPath)))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), CTNHMana.id(baseModelPath)))
                .build()
                .register();
    }

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
                .tag(TagKey.create(BuiltInRegistries.BLOCK.key(),
                        ResourceLocation.tryBuild("forge", "mineable/wrench")), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    public static BlockEntry<Block> createCasingBlock(String name,
                                                      String cnName,
                                                      NonNullFunction<BlockBehaviour.Properties, Block> blockSupplier,
                                                      ResourceLocation texture,
                                                      NonNullSupplier<? extends Block> properties,
                                                      Supplier<Supplier<RenderType>> type,
                                                      boolean noOcclusion) {
        return REGISTRATE.block(name, blockSupplier)
                .cnlang(cnName)
                .initialProperties(properties)
                .properties(p -> {
                    BlockBehaviour.Properties props = p.isValidSpawn((state, level, pos, ent) -> false);
                    if (noOcclusion) {
                        props = props.noOcclusion();
                    }
                    return props;
                })
                .addLayer(type)
                .blockstate((ctx, prov) -> {
                    prov.simpleBlock(ctx.getEntry(), prov.models().cubeAll(name, texture));
                })
                .tag(TagKey.create(BuiltInRegistries.BLOCK.key(),
                        ResourceLocation.tryBuild("forge", "mineable/wrench")), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    public static NonNullBiConsumer<DataGenContext<Block, CoilBlock>, RegistrateBlockstateProvider> createCoilModel(String name,
                                                                                                                    ICoilType coilType) {
        return (ctx, prov) -> {
            ActiveBlock block = ctx.getEntry();
            ModelFile inactive = prov.models().cubeAll(name, coilType.getTexture());
            ModelFile active = prov.models().withExistingParent(name + "_active", CTNHMana.id("block/cube_2_layer/all"))
                    .texture("bot_all", coilType.getTexture())
                    .texture("top_all", coilType.getTexture().withSuffix("_bloom"));
            prov.getVariantBuilder(block)
                    .partialState().with(GTBlockStateProperties.ACTIVE, false).modelForState().modelFile(inactive)
                    .addModel()
                    .partialState().with(GTBlockStateProperties.ACTIVE, true).modelForState().modelFile(active)
                    .addModel();
        };
    }

    @SuppressWarnings("all")
    private static BlockEntry<CoilBlock> createCoilBlock(String cnName, ICoilType coilType) {
        BlockEntry<CoilBlock> coilBlock = REGISTRATE
                .block("%s_coil_block".formatted(coilType.getName()), p -> new CoilBlock(p, coilType))
                .cnlang(cnName)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(createCoilModel("%s_coil_block".formatted(coilType.getName()), coilType))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
        GTCEuAPI.HEATING_COILS.put(coilType, coilBlock);
        return coilBlock;
    }

    public static BlockEntry<Block> createCasingBlock(String name, String cnName, ResourceLocation texture) {
        return createCasingBlock(name, cnName, Block::new, texture, () -> Blocks.IRON_BLOCK,
                () -> RenderType::cutoutMipped);
    }

    public static BlockEntry<Block> createFrameBlock(String name, String cnName, ResourceLocation texture,
                                                     Supplier<Supplier<RenderType>> type) {
        return createCasingBlock(name, cnName, FrameBlock::new, texture, () -> Blocks.IRON_BLOCK,
                type, true);
    }

    private static BlockEntry<Block> createGlassCasingBlock(String name, String cnName, ResourceLocation texture,
                                                            Supplier<Supplier<RenderType>> type) {
        return createCasingBlock(name, cnName, GlassBlock::new, texture, () -> Blocks.GLASS, type);
    }

    public static void init() {}

    public static final BlockEntry<Block> LIVING_ROCK_CASING = createCasingBlock("living_rock_casing", "纯净机械方块",
            CTNHMana.id("block/casings/living_rock_casing"));
    public static final BlockEntry<Block> ZENITH_CASING_BLOCK = createCasingBlock(
            "zenith_casing", "天顶强化机械方块", CTNHMana.id("block/casings/zenith_casing"));
    public static final BlockEntry<Block> ELEMENTIUM_CASING = createCasingBlock(
            "elementium_casing", "源质钢机械外壳", CTNHMana.id("block/casings/elementium_casing"));
    public static final BlockEntry<Block> MANA_STEEL_CASING = createCasingBlock(
            "mana_steel_casing", "魔力钢机械外壳", CTNHMana.id("block/casings/mana_steel_casing"));
    public static final BlockEntry<Block> TERRA_STEEL_CASING = createCasingBlock(
            "terra_steel_casing", "泰拉钢机械外壳", CTNHMana.id("block/casings/terra_steel_casing"));
    public static final BlockEntry<Block> SOUL_LOCKING_CASING = createCasingBlock("soul_lock_blackcasing", "黑石锢魂外壳",
            CTNHMana.id("block/casings/soul_lock_blackcasing"));
    public static final BlockEntry<CoilBlock> SHROUD_MANA_COIL = createCoilBlock("虚境魔力线圈", CoilType.SHROUD_MANA);
    public static final BlockEntry<Block> ZENITH_EYE = REGISTRATE
            .block("zenith_eye", Block::new)
            .cnlang("§5天顶之眼")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> {
                prov.simpleBlock(
                        ctx.getEntry(),
                        prov.models().getExistingFile(CTNHMana.id("block/zenith_eye")));
            })
            .tag(TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.tryBuild("forge", "mineable/wrench")),
                    BlockTags.MINEABLE_WITH_PICKAXE)
            .item(net.minecraft.world.item.BlockItem::new)

            .build()
            .register();
    public static final BlockEntry<Block> FIELD_RESTRICTION_CASING = createCasingBlock(
            "field_restriction_casing", "虚境力场约束机械方块",
            CTNHMana.id("block/casings/depth_force_field_stabilizing_casing"));
    public static final BlockEntry<Block> AURA_CONVERGENCE_CASING = createCasingBlock("aura_convergence_casing",
            "立场汇聚机械方块", CTNHMana.id("block/casings/aura_convergence_block"));
    public static final BlockEntry<Block> ALF_STEEL_CASING = createCasingBlock(
            "alfsteel_casing", "精灵钢机械外壳", CTNHMana.id("block/casings/alfsteel_casing"));
    public static final BlockEntry<Block> ZENITH_CASING_GEARBOX = createCasingBlock(
            "zenith_casing_gearbox", "天顶强化魔力齿轮箱机械方块", CTNHMana.id("block/casings/gearbox/zenith_casing_gearbox"));
    public static final BlockEntry<Block> UNFADING_GARDEN_CASING = createCasingBlock("unfading_garden_casing", "不凋花园方块",
            CTNHMana.id("block/casings/unfading_garden_casing"));
    public static final BlockEntry<Block> FABRIC = createCasingBlock(
            "fabric", "魔力丝绸方块", CTNHMana.id("block/fabric"));
    public static final BlockEntry<Block> ELEMENTIUM_PIPE_CASING = createCasingBlock("elementium_pipe_casing",
            "源质钢管道机械方块",
            CTNHMana.id("block/casings/pipe/elementium_pipe_casing"));
    public static final BlockEntry<Block> ELEMENTIUM_NORMAL_FLUID_PIPE = createCasingBlock(
            "elementium_normal_fluid_pipe", "源质管道方块",
            CTNHMana.id("block/casings/pipe/elementium_normal_fluid_pipe"));
    public static final BlockEntry<Block> CASING_MANASTEEL_GEARBOX = createCasingBlock("mana_steel_gearbox_casing",
            "魔力钢齿轮箱方块",
            CTNHMana.id("block/casings/gearbox/mana_steel_gearbox_casing"));
    public static final BlockEntry<Block> QUASAR_ENERGY_STABILIZATION_CASING = createCasingBlock(
            "quasar_energy_stabilization_casing", "类星体能量稳定机械外壳",
            CTNHMana.id("block/casings/quasar_energy_stabilization_casing"));
    public static final BlockEntry<Block> TWISTED_FUSION_CASING = createCasingBlock("twisted_fusion_casing", "扭曲聚变外壳",
            CTNHMana.id("block/casings/twisted_fusion_casing"));
    public static final BlockEntry<Block> MANA_FUSION_CASING = createCasingBlock("mana_fusion_casing", "魔力聚变外壳",
            CTNHMana.id("block/casings/mana_fusion_casing"));
    public static final BlockEntry<Block> CASING_BLOOD = createCasingBlock(
            "blood_casing", "血染机械方块", CTNHMana.id("block/casings/blood_casing"));
    public static final BlockEntry<Block> DUSK_MECHANICAL_BLOCK = createCasingBlock(
            "dusk_mechanical_block", "薄暮机械方块", CTNHMana.id("block/altar/dusk_mechanical_block"));
    public static final BlockEntry<Block> ZENITH_WILL_MECHANICAL_BLOCK = createCasingBlock(
            "zenith_will_mechanical_block", "穹顶意志机械方块",
            CTNHMana.id("block/altar/zenith_will_mechanical_block"));
    public static final BlockEntry<Block> BLOOD_RITUAL_MECHANICAL_BLOCK = createCasingBlock(
            "blood_ritual_mechanical_block", "血祭机械方块",
            CTNHMana.id("block/altar/blood_ritual_mechanical_block"));
    public static final BlockEntry<Block> RITUAL_MECHANICAL_BLOCK = createCasingBlock(
            "ritual_mechanical_block", "仪祭机械方块", CTNHMana.id("block/altar/ritual_mechanical_block"));
    public static final BlockEntry<Block> CASING_FORCE_FILED = createCasingBlock(
            "force_field_casing", "力场领域机械方块", CTNHMana.id("block/casings/force_field_casing"));
    public static final BlockEntry<Block> CASING_BLOODLOGIC = createCasingBlock(
            "bloodlogic_casing", "生命逻辑传感机械方块", CTNHMana.id("block/casings/bloodlogic_casing"));
    public static final BlockEntry<Block> ARCANE_CONSTRAINT_COATED_GLASS = createGlassCasingBlock(
            "arcane_resistance_coated_glass", "奥能约束覆层玻璃", CTNHMana.id("block/casings/arcane_resistance_coated_glass"),
            () -> RenderType::translucent);
    public static final BlockEntry<Block> ARCANE_SHIELDING_COATED_GLASS = createGlassCasingBlock(
            "arcane_shielding_coated_glass", "奥能屏蔽覆层玻璃", CTNHMana.id("block/casings/arcane_resistance_coated_glass"),
            () -> RenderType::cutoutMipped);
    public static final BlockEntry<Block> ENHANCED_MANA_GLASS = createGlassCasingBlock(
            "enhanced_mana_glass", "强化魔力玻璃", CTNHMana.id("block/casings/enhanced_mana_glass"),
            () -> RenderType::translucent);
    public static final BlockEntry<Block> ARCANE_REACTOR_BLOCK = createCasingBlock(
            "arcane_reactor_block", "奥能反应堆覆层方块", CTNHMana.id("block/casings/arcane_reactor_block"));
    public static final BlockEntry<Block> ELEMENTAL_RADIATION_SUPPRESSION_BLOCK = createCasingBlock(
            "elemental_radiation_suppression_block", "元素辐射抑制方块",
            CTNHMana.id("block/casings/elemental_radiation_suppression_block"));
    public static final BlockEntry<Block> ARCANE_FLOW_ACCELERATED_CONDUIT_BLOCK = createCasingBlock(
            "arcane_flow_accelerated_conduit_block", "魔流束加速管道方块",
            CTNHMana.id("block/casings/arcane_flow_accelerated_conduit_block"));
    public static final BlockEntry<Block> ASTRAL_TELEPORTER_FRAME = createCasingBlock(
            "astral_teleporter_frame", "星辉传送门框架",
            CTNHMana.id("block/casings/astral_teleporter_frame"));
    public static final BlockEntry<Block> ASTRAL_BEACON_BLOCK = createCasingBlock(
            "astral_beacon_block", "星辉信标方块", CTNHMana.id("block/casings/astral_beacon_block"));
    public static final BlockEntry<Block> ELEMENTAL_CASING_GEARBOX = createCasingBlock(
            "elemental_casing_gearbox", "源质钢齿轮箱机械方块",
            CTNHMana.id("block/casings/gearbox/elemental_steel_gearbox_casing"));
    public static final BlockEntry<Block> ELF_STEEL_CASING_GEARBOX = createCasingBlock(
            "elf_steel_casing_gearbox", "精灵钢齿轮箱机械方块", CTNHMana.id("block/casings/gearbox/elf_steel_gearbox_casing"));
    public static final BlockEntry<Block> ORICHALCOS_STEEL_CASING_GEARBOX = createCasingBlock(
            "orichalcos_steel_casing_gearbox", "奥利哈钢齿轮箱机械方块",
            CTNHMana.id("block/casings/gearbox/orichalcos_gearbox_casing"));
    public static final BlockEntry<Block> ORICHALCOS_FRAME = createFrameBlock(
            "orichalcos_frame", "奥利哈框架", CTNHMana.id("block/casings/frames/orichalcos_frame"),
            () -> RenderType::translucent);
    public static final BlockEntry<Block> ELEMENTIUM_FRAME = createFrameBlock(
            "elementium_frame", "源质钢框架", CTNHMana.id("block/casings/frames/elementium_frame"),
            () -> RenderType::translucent);
    public static final BlockEntry<Block> ALFSTEEL_FRAME = createFrameBlock(
            "alfsteel_frame", "精灵钢框架", CTNHMana.id("block/casings/frames/alfsteel_frame"),
            () -> RenderType::translucent);
    public static final BlockEntry<Block> MANA_STEEL_FRAME = createFrameBlock(
            "mana_steel_frame", "魔力钢框架", CTNHMana.id("block/casings/frames/mana_steel_frame"),
            () -> RenderType::translucent);
    public static final BlockEntry<Block> TERRA_STEEL_FRAME = createFrameBlock(
            "terra_steel_frame", "泰拉钢框架", CTNHMana.id("block/casings/frames/terra_steel_frame"),
            () -> RenderType::translucent);
    public static final BlockEntry<Block> SUPERNORMAL_MAGIC_CALCULATE_CORE = createCasingBlock(
            "supernormal_magic_calculate_core", "超因果奥术运算核心",
            CTNHMana.id("block/casings/supernormal_magic_calculate_casing"));
    public static final BlockEntry<Block> MANA_SHATTER_CORE = createCasingBlock(
            "mana_shatter_core", "魔力粉碎核心", CTNHMana.id("block/casings/core/mana_shatter_core"));
    public static final BlockEntry<Block> MANA_REFINEMENT_CORE = createCasingBlock(
            "mana_refinement_core", "魔力精细核心", CTNHMana.id("block/casings/core/mana_refinement_core"));
    public static final BlockEntry<Block> MANA_FORGE_CORE = createCasingBlock(
            "mana_forge_core", "魔力锻压核心", CTNHMana.id("block/casings/core/mana_forge_core"));
    public static final BlockEntry<Block> PURE_MAGIC_CALCULATE_CORE = createCasingBlock(
            "pure_magic_calculate_core", "纯净魔力计算核心", CTNHMana.id("block/casings/pure_magic_calculate_casing"));
    public static final BlockEntry<ForgeSpecialFlowerBlock> DEMON_FLYTRAP = REGISTRATE
            .block("demon_flytrap",
                    properties -> new ForgeSpecialFlowerBlock(MobEffects.HARM, 20,
                            BlockBehaviour.Properties.copy(Blocks.POPPY), () -> CMBlockEntities.DEMON_FLYTRAP.get()))
            .cnlang("恶魔捕蝇草")
            .initialProperties(() -> Blocks.POPPY)
            .lang("Demon Flytrap")
            .blockstate(GTModels::createCrossBlockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item((block, props) -> (BlockItem) new TooltipsBlockItem(block, props, flyTrapLang))
            .model(GTModels::rubberTreeSaplingModel)
            .build()
            .register();
    public static final BlockEntry<ForgeSpecialFlowerBlock> BLOOD_ANTIARIS = REGISTRATE
            .block("blood_antiaris",
                    properties -> new ForgeSpecialFlowerBlock(MobEffects.HARM, 20,
                            BlockBehaviour.Properties.copy(Blocks.POPPY), () -> CMBlockEntities.BLOOD_ANTIARIS.get()))
            .cnlang("见血封喉")
            .initialProperties(() -> Blocks.POPPY)
            .lang("Blood Antiaris")
            .blockstate(GTModels::createCrossBlockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()

            .model(GTModels::rubberTreeSaplingModel)
            .build()
            .register();
    public static final BlockEntry<ForgeSpecialFlowerBlock> BLACKVEIN_MARIGOLD = REGISTRATE
            .block("blackvein_marigold", properties -> new ForgeSpecialFlowerBlock(MobEffects.DIG_SPEED, 20,
                    BlockBehaviour.Properties.copy(Blocks.POPPY), () -> CMBlockEntities.BLACKVEIN_MARIGOLD.get()))
            .cnlang("黑脉金盏花")
            .initialProperties(() -> Blocks.POPPY)
            .lang("blackvein_marigold")
            .blockstate(GTModels::createCrossBlockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item((block, props) -> (BlockItem) new TooltipsBlockItem(block, props, blackVeinFlowerLang))
            .model(GTModels::rubberTreeSaplingModel)
            .build()
            .register();
    public static final BlockEntry<ForgeSpecialFlowerBlock> SEMPER_AUGUSTUS = REGISTRATE
            .block("semper_augustus",
                    properties -> new ForgeSpecialFlowerBlock(MobEffects.DIG_SPEED, 20,
                            BlockBehaviour.Properties.copy(Blocks.POPPY), () -> CMBlockEntities.SEMPER_AUGUSTUS.get()))
            .cnlang("§6永远的奥古斯都")
            .initialProperties(() -> Blocks.POPPY)
            .lang("blackvein_marigold")
            .blockstate(GTModels::createCrossBlockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item((block, props) -> (BlockItem) new TooltipsBlockItem(block, props, tulpenmanieFlowerLang))
            .model(GTModels::rubberTreeSaplingModel)
            .build()
            .register();
    public static final BlockEntry<ForgeSpecialFlowerBlock> PARAROSIA = REGISTRATE
            .block("pararosia",
                    properties -> new ForgeSpecialFlowerBlock(MobEffects.DIG_SPEED, 20,
                            BlockBehaviour.Properties.copy(Blocks.POPPY), () -> CMBlockEntities.PARAROSIA.get()))
            .cnlang("迷心蔷薇")
            .initialProperties(() -> Blocks.POPPY)
            .lang("ParaRosia")
            .blockstate(GTModels::createCrossBlockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item((block, props) -> (BlockItem) new TooltipsBlockItem(block, props, ParaRosiaLang))
            .model(GTModels::rubberTreeSaplingModel)
            .build()
            .register();
    public static final BlockEntry<ForgeSpecialFlowerBlock> ANATTA_LOTUS = REGISTRATE
            .block("anatta_lotus",
                    properties -> new ForgeSpecialFlowerBlock(MobEffects.DIG_SPEED, 20,
                            BlockBehaviour.Properties.copy(Blocks.POPPY), () -> CMBlockEntities.ANATTA_LOTUS.get()))
            .cnlang("焚我莲")
            .initialProperties(() -> Blocks.POPPY)
            .lang("Anatta Lotus")
            .blockstate(GTModels::createCrossBlockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item((block, props) -> (BlockItem) new TooltipsBlockItem(block, props, burnMeLotusLang))
            .model(GTModels::rubberTreeSaplingModel)
            .build()
            .register();
    public static final BlockEntry<ForgeSpecialFlowerBlock> GENETHISTLE = REGISTRATE
            .block("genethistle",
                    properties -> new ForgeSpecialFlowerBlock(MobEffects.DIG_SPEED, 20,
                            BlockBehaviour.Properties.copy(Blocks.POPPY), () -> CMBlockEntities.GENETHISTLE.get()))
            .cnlang("创世蓟")
            .initialProperties(() -> Blocks.POPPY)
            .lang("Genethistle")
            .blockstate(GTModels::createCrossBlockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item((block, props) -> (BlockItem) new TooltipsBlockItem(block, props, genethistleLang))
            .model(GTModels::rubberTreeSaplingModel)
            .build()
            .register();
    public static final BlockEntry<Block> Tulpenmanie = REGISTRATE
            .block("tulpenmanie", Block::new)
            .cnlang("§8泡沫郁金香")
            .initialProperties(() -> Blocks.POPPY)
            .lang("tulpenmanie")
            .blockstate(GTModels::createCrossBlockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item((block, props) -> (BlockItem) new TooltipsBlockItem(block, props, bubleFlowerLang))
            .model(GTModels::rubberTreeSaplingModel)
            .build()
            .register();

    public static final BlockEntry<Block> PURE_LOGIC_CASING = createCasingBlock("pure_logic_casing", "纯净魔力逻辑传输方块",
            CTNHMana.id("block/casings/pure_logic_casing"));
    public static final BlockEntry<Block> RUNE_CARRIER_BLOCK = createCasingBlock(
            "rune_carrier_block", "符文载体方块", CTNHMana.id("block/casings/runictexture/rune_stone_0"));
    public static final BlockEntry<RuneBlock> WINTER_RUNE_STONE = createRuneBlock(
            "winter_rune_stone", "冬之符文石", CTNHMana.id("block/casings/runictexture/rune_stone_1"));
    public static final BlockEntry<RuneBlock> MANA_RUNE_STONE = createRuneBlock(
            "mana_rune_stone", "魔力符文石", CTNHMana.id("block/casings/runictexture/rune_stone_2"));
    public static final BlockEntry<RuneBlock> SIN_GLUTTONY_RUNE_STONE = createRuneBlock(
            "sin_gluttony_rune_stone", "暴食符文石", CTNHMana.id("block/casings/runictexture/rune_stone_3"));
    public static final BlockEntry<RuneBlock> SIN_PRIDE_RUNE_STONE = createRuneBlock(
            "sin_pride_rune_stone", "傲慢符文石", CTNHMana.id("block/casings/runictexture/rune_stone_4"));
    public static final BlockEntry<RuneBlock> SIN_WRATH_RUNE_STONE = createRuneBlock(
            "sin_wrath_rune_stone", "暴怒符文石", CTNHMana.id("block/casings/runictexture/rune_stone_5"));
    public static final BlockEntry<RuneBlock> FIRE_RUNE_STONE = createRuneBlock(
            "fire_rune_stone", "火之符文石", CTNHMana.id("block/casings/runictexture/rune_stone_6"));
    public static final BlockEntry<RuneBlock> AUTUMN_RUNE_STONE = createRuneBlock(
            "autumn_rune_stone", "秋之符文石", CTNHMana.id("block/casings/runictexture/rune_stone_7"));
    public static final BlockEntry<RuneBlock> EARTH_RUNE_STONE = createRuneBlock(
            "earth_rune_stone", "地之符文石", CTNHMana.id("block/casings/runictexture/rune_stone_8"));
    public static final BlockEntry<RuneBlock> SIN_GREED_RUNE_STONE = createRuneBlock(
            "sin_greed_rune_stone", "贪婪符文石", CTNHMana.id("block/casings/runictexture/rune_stone_9"));
    public static final BlockEntry<RuneBlock> SUMMER_RUNE_STONE = createRuneBlock(
            "summer_rune_stone", "夏之符文石", CTNHMana.id("block/casings/runictexture/rune_stone_10"));
    public static final BlockEntry<RuneBlock> WIND_RUNE_STONE = createRuneBlock(
            "wind_rune_stone", "风之符文石", CTNHMana.id("block/casings/runictexture/rune_stone_11"));
    public static final BlockEntry<RuneBlock> WATER_RUNE_STONE = createRuneBlock(
            "water_rune_stone", "水之符文石", CTNHMana.id("block/casings/runictexture/rune_stone_12"));
    public static final BlockEntry<RuneBlock> SIN_ENVY_RUNE_STONE = createRuneBlock(
            "sin_envy_rune_stone", "嫉妒符文石", CTNHMana.id("block/casings/runictexture/rune_stone_13"));
    public static final BlockEntry<RuneBlock> SIN_LUST_RUNE_STONE = createRuneBlock(
            "sin_lust_rune_stone", "欲望符文石", CTNHMana.id("block/casings/runictexture/rune_stone_14"));
    public static final BlockEntry<RuneBlock> SPRING_RUNE_STONE = createRuneBlock(
            "spring_rune_stone", "春之符文石", CTNHMana.id("block/casings/runictexture/rune_stone_15"));
    public static final BlockEntry<RuneBlock> SIN_SLOTH_RUNE_STONE = createRuneBlock(
            "sin_sloth_rune_stone", "懒惰符文石", CTNHMana.id("block/casings/runictexture/rune_stone_16"));
    public static final BlockEntry<Block> RUNE_STONE_PERFECT = createCasingBlock(
            "rune_stone_perfect", "完美的符文石", CTNHMana.id("block/casings/runictexture/rune_stone_perfect"));

    // 扭曲线圈方块
    public static final BlockEntry<ActiveBlock> REALITY_TWISTED_COIL = createActiveCasing(
            "reality_twisted_coil", "现实扭曲线圈", "block/coil/reality_twisted_coil");
    public static final BlockEntry<ActiveBlock> DIMENSION_TWISTED_COIL = createActiveCasing(
            "dimension_twisted_coil", "维度扭曲线圈", "block/coil/dimension_twisted_coil");
    public static final BlockEntry<ActiveBlock> MATERIAL_TWISTED_COIL = createActiveCasing(
            "material_twisted_coil", "物质扭曲线圈", "block/coil/material_twisted_coil");
    public static final BlockEntry<ActiveBlock> TERMINAL_TWISTED_COIL = createActiveCasing(
            "terminal_twisted_coil", "终末扭曲线圈", "block/coil/terminal_twisted_coil");

    // 2.17 方块（材质路径 block/casings/）
    public static final BlockEntry<Block> ARCANE_LASER_CONDUIT_BLOCK = createCasingBlock(
            "arcane_laser_conduit_block", "奥能激光传导方块", CTNHMana.id("block/casings/arcane_laser_conduit"));
    public static final BlockEntry<ActiveBlock> MANA_COMPRESSED_CORE = createActiveCasing(
            "mana_compressed_core", "魔力压缩核心", "block/coil/mana_compressed_core");
    public static final BlockEntry<Block> ARCANE_ENERGY_LASER_TOWER = createCasingBlock(
            "arcane_energy_laser_tower", "奥能能量激光塔", CTNHMana.id("block/casings/arcane_energy_tower"));
}
