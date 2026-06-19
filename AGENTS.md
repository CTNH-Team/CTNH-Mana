# CTNH-Mana KNOWLEDGE BASE

## OVERVIEW
CTNH-Mana adds magic-themed CTNH content, Botania/Blood Magic style integrations, mana multiblocks, rituals, custom items, client radial UI, and generated resources under mod id `ctnhmana`.

## WHERE TO LOOK
- Mod entry: `src/main/java/com/moguang/ctnhmana/CTNHMana.java`. Forge mod initialization.
- GT addon: `src/main/java/com/moguang/ctnhmana/CTNHManaGTAddon.java`. GTCEu integration.
- Config: `src/main/java/com/moguang/ctnhmana/CMConfig.java`. Module config.
- Pattern helpers: `src/main/java/com/moguang/ctnhmana/api/pattern/`. Magic multiblock predicates/maps.
- Multiblocks: `src/main/java/com/moguang/ctnhmana/Mutiblock/`. Existing directory name is `Mutiblock`; keep references exact.
- Registries: `src/main/java/com/moguang/ctnhmana/registry/`. Items, machines, recipes, multiblock registrations.
- Rituals/items: `src/main/java/com/moguang/ctnhmana/common/ritualTypes/`, `item/`. Ritual and magic item behavior.
- Networking: `src/main/java/com/moguang/ctnhmana/networking/packets/`. Caduceus and Index Fortuna/target packets; initialized from `event/EventHandler.commonSetup()`.
- Client UI/Ponder: `src/main/java/com/moguang/ctnhmana/client/`. Caduceus radial menu plus Mana-owned Ponder plugin, tags, scenes, and adapter builder.
- Datagen/lang/Ponder: `src/main/java/com/moguang/ctnhmana/data/`. Source for generated resources, lang, and Ponder scene language extraction.
- Mixins/integrations: `src/main/java/com/moguang/ctnhmana/mixin/`, `integration/emi/`, `integration/jade/`. Ars Nouveau, Blood Magic, Botania, EMI, and Jade compatibility hooks.

## REGISTRATION ENTRYPOINTS
- Registrate/root: `registry/CMRegistrate.java`; mod/addon entrypoints are `CTNHMana.java` and `CTNHManaGTAddon.java`.
- Mod entry hooks: `CTNHMana.java` registers particle types, entity renderers, load-complete handling, machine/recipe-condition listeners, mob effects, and sound events.
- Load-complete hook: `CTNHMana.onFMLoadComplete()` registers the Blood Magic altar component for `CASING_BLOODLOGIC`.
- GT addon hooks: `CTNHManaGTAddon.initializeAddon()` initializes items, blocks, and block entities; `registerTagPrefixes()` and `registerElements()` initialize Mana tag/material data; `removeRecipes()` delegates to `RecipeRemoval`.
- Event handler: `event/EventHandler.java` registers machines, multiblocks, recipe types, recipe conditions, materials, tag-prefix ignores, networking, client item properties, datagen, and Mana Ponder lang.
- Items/blocks/block entities/entities: `registry/CMItems.java`, `registry/items/CMFuelItems.java`, `registry/CMBlocks.java`, `registry/CMBlockEntities.java`, `registry/CMEntities.java`.
- Machines/multiblocks: `registry/CMMachines.java`, `registry/CMMultiblockMachines.java`, plus grouped files under `registry/multiblock/`.
- Materials/elements/tag prefixes: `registry/CMMaterials.java`, `CMElements.java`, `CMTagPrefixes.java`, `GTMaterialAddon.java`.
- Recipe types/conditions: `registry/CMRecipeTypes.java`, `CMRecipeConditions.java`.
- Effects/sounds/particles/client registries: `CMMobEffects.java`, `CMParticleTypes.java`, `registry/sounds/`, `CMModelLayers.java`, `CMGuiTextures.java`.
- Recipe generation: `CTNHManaGTAddon.addRecipes()` dispatches `data/recipe/**` for Mana Reactor, Hell Forge, Wishing Will, Elven Trade, Blood Altar, Meteor Capturer, Demon Will Generator, Mana Condenser, Botania, Rune Altar/Ritual, Terra Plate, Gaia Reactor, Mana hatches/circuits/upgrades, Zenith, and Twist Collapse; keep magic-only recipes here, broad cross-module recipes in Core.
- Recipe builders: `common/recipe/builder/bloodmagic/` and `common/recipe/builder/botania/` wrap Blood Magic and Botania recipe JSON generation.
- Client model predicates: `EventHandler.clientSetup()` registers item properties for `SABER_WAND` `wand_status` and `CADUCEUS` `tool_type`.
- Datagen/lang: `data/CMDatagen.java`, `data/lang/`; `event/EventHandler.gatherData()` uses CTNH-Lib's `CTNHPonderLang.init(new CTNHManaPonderPlugin())` to extract Mana Ponder scene text during client datagen.
- Ponder: `client/ponder/CTNHManaPonderPlugin.java` registers `CTNHManaPonderScenes` and `CTNHManaPonderTags`; Mystic Spire scenes live in `client/ponder/Mana/` and use `scene.title(..., en, cn)` / `scene.showText(..., en, cn)` with text embedded directly in scene files.

## CONVENTIONS
- Namespace is `com.moguang.ctnhmana`; registry prefixes generally use `CM`.
- Generated resources are large; use `:modules:CTNH-Mana:runData` after datagen or Ponder text changes.
- Ponder `CTNHManaPonderSceneBuilder` is a thin adapter around CTNH-Lib's shared builder; keep Mana-specific scenes/tags/plugins in CTNH-Mana, not Core or Lib.
- Blood Magic/Botania/Ars/EMI compatibility is spread across recipe builders, mixins, integrations, and client packets; check all four before changing a magic integration surface.

## COMMANDS
```bash
./gradlew :modules:CTNH-Mana:build
./gradlew :modules:CTNH-Mana:runData
./gradlew :modules:CTNH-Mana:spotlessCheck
```

## ANTI-PATTERNS
- Do not rename `Mutiblock` casually; existing imports and paths rely on the current spelling.
- Do not assume magic integrations are isolated from GTCEu; machine/recipe registration still flows through GT addon patterns.
- Do not change Caduceus/Saber client behavior without checking both networking packets and item property model predicates.
