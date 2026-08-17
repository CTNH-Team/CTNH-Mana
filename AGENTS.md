# CTNH-Mana Module Guide

**Last verified against source:** 2026-08-16
**Module version:** 0.7.7 (gradle.properties)
**Java files (src/main/java):** 294 · **Generated resources:** 718 · **Main resources:** 692
**Repository:** https://github.com/CTNH-Team/CTNH-Mana (git submodule of CTNH-Modules)

## OVERVIEW

CTNH-Mana is the magic-content module of the CTNH pack: Botania / Blood Magic / Ars Nouveau / Apotheosis integrations, mana-fueled GTCEu multiblocks, BloodMagic-style rituals, custom items and mob effects, a client radial UI, and the "Zenith" (虚境) invasion event system. Mod id `ctnhmana`, namespace **`com.magicbee.ctnhmana`** (renamed from `com.moguang.ctnhmana` in commit `eb28b8b`; older docs and guides may still show the stale package).

The module is a full GTCEu addon (`@GTAddon`): machines, recipe types, materials, elements, and tag prefixes are registered through the GTCEu addon pipeline; GT/GMT recipes are injected at runtime through the dynamic pack, exactly like the other CTNH modules.

## PACKAGE STRUCTURE

Base path: `src/main/java/com/magicbee/ctnhmana/` (294 Java files)

| Package | Files | Contents |
|---------|------:|----------|
| `api/` | 25 | effects (9), recipe conditions (4), recipe custom logic (6), pattern maps/predicates (2), Botania effect packet extension (2), `IBloodAltarLogic` mixin interface (1), `GemSublimatorRules` (1) |
| `client/` | 34 | `ClientProxy`, `ZenithInvadeClient`, `ZenithMatrixEffect`, radial menu (4), Ponder plugin/scenes/tags (7), models (6), renders (12 + 1 particle), `RenderUtils` |
| `common/` | 108 | `CommonProxy`, multiblock machines (31), machine blocks/entities, items (8 subpackages), gui (7), blockentities (12), parts (8), rituals (2), ritual types (5), blocks (4), entities (2), zenith invasion event (5) |
| `data/` | 55 | `CMDatagen`, `ManaData` (SavedData), recipes (35 + 11 builders), lang (3), tags (2), materials (1), ingredients util |
| `event/` | 9 | `EventHandler` (MOD bus), `ForgeEventHandler`, `CMKeyBindings`, `IndexEventHandler`, `PhysicalAntagonismEventHandler`, `SoulLeechEventHandler`, `TaintedBloodWeepingEyeEventHandler`, `ThirdEyeEventHandler`, `YurikoRingEventHandler` |
| `integration/` | 8 | Jade providers (7), `CTNHManaEmiPlugin` (1) |
| `mixin/` | 16 | botania (6), ars (4), bloodmagic (3), ae2 (2), emi (1) |
| `networking/` | 6 | `CMNetworking` + 5 packets |
| `registry/` | 27 | `CMRegistrate` and all `CM*` registries (19), multiblock definition holders (5), sounds (2), `CMFuelItems` (1) |
| `utils/` | 3 | `CTNHManaUtils`, `EnvUtils`, `ModUtils` |

### Vendored upstream sources (reference only, NOT built)

- `Apotheosis-1.20/` (309 Java files), `Ars-NouveauArs-Nouveau/`, `BloodMagic/` — local copies of upstream mod sources kept for reference. They are **not** wired into any Gradle source set (verified: no `srcDir` references in `build.gradle` / `dependencies.gradle` / `settings.gradle`). Actual dependencies come from the `ctnh.*` version catalog (`dependencies.gradle`). Do not edit or build them.
- `bin/` (Eclipse output) and `run/` (dev runtime) are local tooling artifacts — never source.

## REGISTRATION & LIFECYCLE FLOW

1. `CTNHMana` (`@Mod`, mod id `ctnhmana`) → `DistExecutor` → `ClientProxy` / `CommonProxy`.
2. `CommonProxy` wires mod-bus generic listeners: `MachineDefinition` → `CMMachines.init()` + `CMMultiblockMachines.init()`; `GTRecipeType` → `CMRecipeTypes.init()`; `RecipeConditionType` → `CMRecipeConditions.init()`. Also registers particle types, mob effects, sounds, entities, creative tabs, `CMRegistrate`, `CMDatagen`, `CMConfig` (dev.toma yaml), all 7 Jade providers via CTNH-Lib `JadePriorityManager` (priority 900), networking on `FMLCommonSetup`, and on `FMLLoadComplete` registers `CASING_BLOODLOGIC` as a BloodMagic altar `"CRYSTAL"` component.
3. `CTNHManaGTAddon` (`@GTAddon`): `initializeAddon()` → `CMItems`/`CMBlocks`/`CMBlockEntities`; `registerTagPrefixes()` → `CMTagPrefixes`; `registerElements()` → `CMElements`; `addRecipes()` wires **33 recipe classes** (see data/recipe below); `removeRecipes()` → `RecipeRemoval`. The `changeId(...)` wrapper re-registers other mods' deleted recipes under the `ctnhmana` namespace so they survive removal.
4. `ClientProxy` registers 5 `DynamicRenderManager` render types (zenith_laser, eternal_garden, mana_condenser, mana_reactor, demon_will_generator), the two `zenith` shaders (`zenithShader`, `zenithBeamShader`), item property predicates (e.g. `SaberWandItem`), keybindings, particles, and model layers.

## KEY SYSTEMS

### Machine hierarchy (`common/multiblock/`)
- `ManaMachine extends RecipeElectricMultiblockMachine` — base for all mana multiblocks: holds the bound `ManaHatch` (`hatch`, `hatchPos`), syncs `ManaLevel` + `isZenithOpen` from `ManaData`, optional `Zenith_Enhanced` link to a `ZenithMatrixMachine`.
- `BaseManaMachine extends ManaMachine` — mana consumption model: `baseConsumption * 2^tier`, instant vs. per-second consumption, `ManaMachineUpgradeItem` support, `MachineMetric`/`recipeMetric`/`globalMetric` for UI metrics, custom `ManaStatusGui`/`ShroudUi` tabs.
- `MultiPatternMultiblockMachine` — multi-structure-pattern machines (e.g. `MeteorCaptureMachine`).
- 31 machine classes total, including: `ManaReactor`, `HellForgeMachine`, `WishingWill` (genshin-style gacha), `EternalGarden`, `EternalWosMachine`, `DemonWillMachine`, `IndustrialAltarMachine`, `IndustrialSalvagingMachine`, `IndustrialGemInlayMachine`, `ManaCondenserMachine`, `ManaForceTransformer`, `ManaMaceratorMachine`, `ManaFuelInfuserMachine`, `MeteorCaptureMachine`, `MysticSpire` (+`SpireMath`/`SpireBigMath`), `NicollDysonBeams`, `QuasarEye`, `TwistedFusionMachine`, `ArcaneHighEnergyCompressionReactorCore`, `RitualMechanicalMachine`, `ZenithMachine`, `ZenithMatrixMachine`, `ZenithSpire`, `ICentralStorageMachine`/`IChannelMachine` (interfaces), `MachineUtils`.
- Definition holders live in `registry/multiblock/`: `BloodMagic`, `Botania`, `ManaMachine`, `Misc`, `ZenithMachine` — all built with `REGISTRATE.multiblock(...)`, `cnLangValue(...)` Chinese names, `addManaMachineTooltips(...)` tooltips, and recipe-type/modifier wiring. `registry/CMMultiblockMachines` holds shared tooltip/lang helpers.
- Single-block machines (`common/machine/`): `GemSublimatorMachine` (+ `api/machine/gem/GemSublimatorRules`), `FlowerCakeMachine`/`FlowerCakeBlock`, plus `ManaMachineBlockEntity`/`MysticSpireBlockEntity`/`ZenithEyeBlockEntity`.

### Parts (`common/parts/`)
`CMPartsAbility` defines custom part abilities: `MANAHATCH`, `SIGNALHATCH`, `CentralControlBus`, `ExtendedCentralControlBus`. Parts: `ManaHatch` (BT mana storage/consumption), `ManaHatches/BloodManaHatch`, `CreativeManaHatch`, `SparkManaHatch`, `CentralControlBus`, `ExtendedCentralControlBus` (circuit UI via `ExtendedCentralControlBusCircuitUi`), `RedstoneSignalBroadcastHatch`.

### Rituals (`common/ritual/` + `common/ritualtypes/`)
`MachineRitualStoneHost` is an adapter that disguises `RitualMechanicalMachine` as a BloodMagic `IMasterRitualStone` (injects machine coords, `MachineRitualSoulNetwork` bound to the machine's blood hatch, and a controller-centered radius), letting vanilla BloodMagic `Ritual.performRitual(...)` run inside GT machines. Five custom ritual types: `RitualBossSummon`, `RitualCharger`, `RitualDragonCloud`, `RitualLifeExtractor`, `RitualShroudSight`.

### Zenith invasion (虚境) system
`ManaData` (SavedData `ctnhmana_manadata`) persists the `isZenithOpen` flag and the per-mod magic level map (`LevelName`: BT/BM/ARS/GT). Server flow: `common/event/zenith/` (`ZenithInvadeManager`, `ZenithInvadeEvent`, `ZenithInvadeEffects`, `ZenithInvadeMessages`, `ZenithGlitchText`); client flow: `client/ZenithInvadeClient`, `ZenithMatrixEffect`, zenith shaders; networking: `ZenithInvadePacket`. Machines: `ZenithMachine`, `ZenithMatrixMachine`, `ZenithSpire`; recipes: `ZenithRecipes` + `ZENITH_CIRCUIT`/`ANTIPHASE_ETCHING` recipe types + `ZenithCondition`.

### Custom recipe types & conditions (`registry/CMRecipeTypes`, `api/recipe/`)
~28 custom `GTRecipeType`s (mana_reactor, hell_forge, blood_altar, genshin_wishing, demon_will_generator, eternal_garden, mana_condenser, beams, quasar_create/eye, twisted_fusion, digital_well_of_suffer, gaia_reactor, industrial_petal_apothecary, industrial_salvaging, gem_inlay, gem_sublimator(+generic), mana_forge, mana_fuel_infuser, meteor_capturer, zenith_circuit, antiphase_etching, twist_collapse, meteor_ritual_guide, ritual, door_of_shroud, mana_transformer). Conditions: `BloodAltarCondition`, `HellForgeCondition`, `InfusionCellCastingCondition`, `ZenithCondition`. Custom recipe logic (`api/recipe/customlogic/`): `DigitalWellOfSufferLogic`, `EternalGardenLogic`, `IndustrialGemCuttingLogic`, `IndustrialGemSublimatorLogic`, `IndustrialGemSublimatorGenericLogic`, `IndustrialSalvagingLogic`.

### Data generation (`data/`)
`CMDatagen` wires: `LANG` → `EnglishLangHandler`, `CNLANG` → `ChineseLangHandler`, `FLUID_TAGS` → `FluidTypeTags`, `ITEM_TAGS` → `ItemTags`. `GatherDataEvent` adds `CMSoundDefinitionsProvider` and initializes Ponder lang via `CTNHPonderLang` + `CTNHManaPonderPlugin`. Materials: `data/materials/BotaniaMaterials` (plus `registry/CMMaterials` and `GTMaterialAddon` hazard helpers). All text goes through the CTNH-Lib lang-provider `com.ctnhlang.CN` / `com.ctnhlang.EN` annotations or builder `.cnLangValue(...)`/`.translate()`.

### Recipes (`data/recipe/`)
33 recipe classes are wired from `CTNHManaGTAddon.addRecipes()`, including `BloodAltarRecipes`, `BotaniaRecipes`, `ElvenTradeRecipes`, `RuneAltarRecipes` + `runeRitualRecipes`, `TerraPlateRecipes`, `ManaPoolRecipes`, `HellForgeRecipes`, `WishingWillRecipes`, `EternalGardenRecipes`(+Special), `EternalWosRecipes`, `DemonWillGeneratorRecipes`, `ManaCondenserRecipes`, `ManaReactorRecipes`, `ManaMachineRecipes`, `ManaMachineUpgradeRecipes`, `ManaHatchRecipes`, `ManaCircuitRecipes`, `ZenithRecipes`, `TwistCollapseRecipes`, `RitualMechanicalRecipes`, `MeteorCapturerRecipes`(+Guide), `SalvagingRecipes`, `GemCuttingRecipes`, `PerfectMineKeyRecipes`, `ManaTransformerRecipes`, `BeamsRecipes`, `GaiaReactorRecipes`, `MachineRecipes`, `ManaRecipes`, `ManaMachineBlockRecipes`. Removal: `RecipeRemoval`. Builders in `data/recipe/builder/`: botania (7: petal, rune altar, rune ritual, terra plate, mana infusion, elven trade, elf plate), bloodmagic (2: altar, tartaric forge), apotheosis (2: gem cutting, salvaging). Ingredient helpers: `data/recipe/utils/BotaniaIngredients`.

### Items (`common/item/`)
- `equipment/` — `SaberWandItem` (multi-tool, client predicates), `KoishiEyeItem`, `TaintedBloodWeepingEye`, `YurikoRingItem`
- `caduceus/` — `CaduceusItem` + `MultiToolDefinition` (radial-menu tool, `CaduceusPacket`)
- `bosssummoner/` — `ThrowableSummoner`, `ThrowItem`, `BossSummonerBehavior`, `IThrowableItem`
- `manamachineupgrade/` — `ManaMachineUpgradeItem` + BM/BT/GT tiers T1-T3 (`BMUpgradeItemT1/T2`, `BTUpgradeItemT1/T2/T3`, `GTUpgradeItemT1/T2`)
- `rune/` — `IRuneItem`, `RuneElementType`, `SpireUpgradeRuneItem`; `manafuelstick/` — `IManaFuelStick`; `bloodmagicjade/` — `JadeItem`; `dungeon/` — `PerfectMineKeyItem`; plus `FlowerCakeItem`, `TooltipsBlockItem`, `ZenithDebugToolItem`

### Effects (`api/effect/` + `registry/CMMobEffects`)
9 mob effects: `ShroudGazeEffect` (shroud_gaze), `WishingFlyEffect` (helian_blessing), `KarmaEffect`, `KarmaFortunaEffect`, `BladeUnleashedEffect` (blade_unleashed), `IndexTargetEffect` (index_target), `SoulLeechEffect`, `TaintedBloodEffect`, `PhysicalAntagonismEffect` (physical_antagonism, WIP). Effect handlers in `event/` (e.g. `PhysicalAntagonismEventHandler`, `SoulLeechEventHandler`, `ThirdEyeEventHandler`, `TaintedBloodWeepingEyeEventHandler`, `YurikoRingEventHandler`, `IndexEventHandler`).

### Networking (`networking/packets/`)
`CMNetworking` (init on `FMLCommonSetup`) + `CaduceusPacket`, `IndexFortunaPacket`, `IndexTargetBlockPacket`, `IndexTargetParticlePacket`, `ZenithInvadePacket`.

### Mixins (`mixin/`, declared in `src/main/resources/ctnhmana.mixins.json`)
- botania (6): `BotaniaEntitiesMixin`, `FunctionalFlowerBaseAccessor`, `ManaPoolBlockEntityMixin`, `MixinForgePacketHandler`, `PetruniaMixin`, `WitherAconiteMixin`
- ars (4): `MixinEmiLecternRecipeHandler`, `PotionJarMixin`, `PotionTankMixin`, `StoredItemStackMixin`
- bloodmagic (3): `BloodAltarMixin`, `DemonWillHolderMixin`, `TileAltarAccessor` (used through `api/mixin/IBloodAltarLogic`)
- ae2 (2): `WirelessTerminalItemMixin`, `WirelessTerminalMenuHostMixin`
- emi (1): `TagEmiIngredientMixin`
- Note: the json lists 15 mixins; `ars.StoredItemStackMixin` exists on disk but is **not** listed in the json. Refmap is `mixins.magicbee.refmap.json` (differs from mod id — keep it that way).

### Client (`client/`)
Radial menu (`gui/radial/`: `RadialMenu`, `RadialMenuScreen`, `RadialMenuSlot`, `CaduceusRadialMenu`), Ponder (`ponder/`: plugin, scene builder adapter over CTNH-Lib `CTNHPonderSceneBuilder`, scenes, tags; `ponder/mana/`: `MagicRituals`, `MysticSpire`, `PonderParticleUtil`), renders (`render/`: `ManaReactorRender`, `EternalGardenRender`, `ManaCondenserRender`, `DemonWillRender`, `ZenithMatrixRender`, `StarCake*`, `DeltaSparkRenderer`, `OmegaSparkRenderer`, `ShroudGazingRender`, `PhysicalAntagonismRender`; `IconParticle`), models (`model/`: `MagicCubeModel`, `StarCakeBlockModel`/`StarCakeItemModel`, `ModelBase`/`ModelDefinition`, `CMModels`), common gui helpers (`common/gui/`: `BaseManaMachineGui`, `ManaStatusGui`, `ShroudUi`, `SelectableCircuitSlotWidget`, `ArcButtonWidget`, `AnimationTextureY`, `ExtendedCentralControlBusCircuitUi`).

### Flowers (`common/blockentity/flower/`)
7 custom Botania functional flowers with block entities: `AnattaLotus`, `BlackVeinMarigold`, `BloodAntiaris`, `DemonFlytrap`, `Genethistle`, `ParaRosia`, `Tulpenmanie`.

## WHERE TO LOOK

| Task | Location |
|------|----------|
| Mod entry / mod id | `CTNHMana.java` |
| GT addon hook (recipes, removal, elements, tag prefixes) | `CTNHManaGTAddon.java` |
| Lifecycle wiring, Jade providers, datagen, networking init | `common/CommonProxy.java` |
| Client renders, shaders, item predicates, keybindings | `client/ClientProxy.java` |
| Config | `CMConfig.java` (dev.toma YAML) |
| SavedData (mana levels, zenith flag) | `data/ManaData.java` |
| Mana multiblock definitions | `registry/multiblock/*.java` |
| Machine implementations | `common/multiblock/` (31) |
| Parts / hatches | `common/parts/` |
| Ritual adapter + ritual types | `common/ritual/`, `common/ritualtypes/` |
| Zenith invasion | `common/event/zenith/`, `client/ZenithInvadeClient.java`, `ZenithInvadePacket` |
| Recipe types / conditions | `registry/CMRecipeTypes.java`, `api/recipe/condition/` |
| Recipe JSON writers | `data/recipe/` (33 classes) + `data/recipe/builder/` |
| Lang (CN/EN) | `data/lang/`, `com.ctnhlang.CN`/`EN` annotations |
| Mixins | `mixin/` + `ctnhmana.mixins.json` |
| Jade / EMI integration | `integration/jade/`, `integration/emi/` |
| Ponder | `client/ponder/` (plugin/scenes/tags) |
| Registry objects (direct references) | `registry/CMItems` `CMBlocks` `CMMaterials` `CMBlocks` `CMRecipeTypes` `CMMobEffects` ... |

## DOMAIN GUIDE ROUTING

Read the matching CTNH-Docs domain guide before editing that source area:

| Source area | Guide |
|-------------|-------|
| `api` | `docs/CTNH-Mana/api/AGENTS.md` |
| `client` | `docs/CTNH-Mana/client/AGENTS.md` |
| `common` | `docs/CTNH-Mana/common/AGENTS.md` |
| `data` | `docs/CTNH-Mana/data/AGENTS.md` |
| `event` | `docs/CTNH-Mana/event/AGENTS.md` |
| `integration` | `docs/CTNH-Mana/integration/AGENTS.md` |
| `mixin` | `docs/CTNH-Mana/mixin/AGENTS.md` |
| `networking` | `docs/CTNH-Mana/networking/AGENTS.md` |
| `registry` | `docs/CTNH-Mana/registry/AGENTS.md` |
| `utils` | `docs/CTNH-Mana/utils/AGENTS.md` |

## CONVENTIONS

- Namespace is **`com.magicbee.ctnhmana`** (not the historical `com.moguang.ctnhmana`); registry objects are `CM*`-prefixed (`CMItems.X`, `CMBlocks.X`, `CMMaterials.X`, `CMRecipeTypes.X`). Mixin package is `com.magicbee.ctnhmana.mixin` with refmap `mixins.magicbee.refmap.json`.
- **GT/GMT recipes are runtime dynamic-pack data**: everything wired through `CTNHManaGTAddon.addRecipes(Consumer<FinishedRecipe>)` is serialized into the GTCEu dynamic pack at runtime. `runData` produces **no JSON** for them; a clean `src/generated/resources` tree does not indicate missing recipes. Verify GT recipes in-game or via `ConfigHolder.dev.dumpRecipes`. Static generated data covers lang, tags, models, sounds only.
- Item/block/fluid references MUST use direct registration objects (`CMItems.X`, `CMBlocks.X`, `CMMaterials.X`, `GTMaterials.X`, `TagPrefix.ingot`, `BotaniaBlocks.X`, ...), never `ResourceLocation` string parsing + `ForgeRegistries` lookups.
- All text is bilingual: use `com.ctnhlang.CN`/`com.ctnhlang.EN` lang-provider annotations or builder `.cnLangValue(...)`/`.translate()`; never hardcode Chinese into tooltips without the lang system.
- All registration flows through `CMRegistrate` (extends CTNH-Lib `CNRegistrate`); single-block machines via `CMMachines`, multiblocks via `CMMultiblockMachines` + `registry/multiblock/*`.
- `src/main/resources` is authored input; `src/generated/resources` is generated output — regenerate with `:modules:CTNH-Mana:runData`, never hand-edit.
- Re-registering a recipe with an unchanged id after removing it will still be filtered out; use `CTNHManaGTAddon.changeId(...)` to remap ids under the `ctnhmana` namespace.
- Magic integration surfaces span four places at once — recipe builders (`data/recipe/builder/<mod>/`), mixins (`mixin/<mod>/`), integrations (`integration/`), and client packets — check all four before changing a Blood Magic / Botania / Ars / Apotheosis surface.
- Ponder: keep reusable scene builder/text helpers in CTNH-Lib; CTNH-Mana keeps only its plugin, scenes, tags, and module-specific helpers (`CTNHManaPonderSceneBuilder` is a thin adapter).

## ANTI-PATTERNS

- Do not treat `Apotheosis-1.20/`, `Ars-NouveauArs-Nouveau/`, `BloodMagic/` as buildable sources — they are reference copies of upstream maven deps and are not in any source set.
- Do not rename the mixin refmap (`mixins.magicbee.refmap.json`) to match the mod id; it intentionally differs.
- Do not change Caduceus/Saber client behavior without checking both the networking packets (`CaduceusPacket`, `CMNetworking`) and the item property predicates in `ClientProxy`.
- Do not hand-edit `build/`, `bin/`, `run/`, `.gradle/`, or `src/generated/resources`.
- Do not introduce dependencies from CTNH-Mana back to CTNH-Core (dependency direction is Core -> modules).
- Do not commit `modules/*` submodule pointer updates from the root repository.
- Do not treat the stale `com.moguang.ctnhmana` package name in old docs/guides as current.

## COMMANDS

```text
./gradlew :modules:CTNH-Mana:build
./gradlew :modules:CTNH-Mana:runData
./gradlew :modules:CTNH-Mana:spotlessCheck
./gradlew :modules:CTNH-Mana:spotlessApply
```

Use Java 17. This module must be built from the CTNH-Modules workspace, not standalone.

## SCOPE, SOURCE OF TRUTH, WORKFLOW

**Scope:** Applies to `modules/CTNH-Mana` and its submodule repository. This guide is loaded through the root routing table; it is a reference, not an additional instruction file.

**Source of truth:**
- Registration/lifecycle: `CTNHMana.java`, `CTNHManaGTAddon.java`, `common/CommonProxy.java`
- Forge metadata and mixins: `src/main/resources/META-INF/mods.toml`, `src/main/resources/ctnhmana.mixins.json`
- Static generated data: providers plus `src/generated/resources`
- Upstream magic-mod APIs: the vendored reference copies (`Apotheosis-1.20/`, `Ars-NouveauArs-Nouveau/`, `BloodMagic/`)

**Workflow (4 steps):**
1. Map the changed symbol to its domain and read that domain guide (webfetch from CTNH-Docs).
2. Check GT addon hook order (`addRecipes` wiring), `CommonProxy` lifecycle, and networking packets before touching a machine/item/event.
3. Run the narrowest Gradle task: `runData` for datagen/lang/Ponder changes, `build` for compilation, `spotlessCheck` after Java edits.
4. For GT/GMT recipe changes, verify in-game (or via dev recipe dump), never by inspecting `src/generated/resources`.

## WORKTREE NOTES (as of 2026-08-16)

- Uncommitted WIP: `PhysicalAntagonismEffect` (+ `PhysicalAntagonismRender`, `PhysicalAntagonismEventHandler`, texture) — untracked; modified: `ChineseLangHandler`, `EnglishLangHandler`, `RuneAltarRecipes`, `CMMobEffects`, `CMMultiblockMachines`.
- Recent history highlights: mana-machine overclock fix (all abnormal lossless overclock removed), DemonWill generator nerf + BloodMagic `addWill` limit-fix mixin, `PerfectMineKeyItem` (破碎钥匙/完美钥匙), package rename `com.moguang` → `com.magicbee`, gem cutting/sublimator machines.
- The previous CTNH-Docs guide was written against the old `com.moguang.ctnhmana` package (284 files); this guide reflects the current `com.magicbee.ctnhmana` tree (294 files) and should be published to CTNH-Docs when the domain guides are next updated.
