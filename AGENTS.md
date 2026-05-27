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
- Client UI: `src/main/java/com/moguang/ctnhmana/client/gui/radial/`. Caduceus radial menu.
- Datagen/lang: `src/main/java/com/moguang/ctnhmana/data/`. Source for generated resources and lang.

## REGISTRATION ENTRYPOINTS
- Registrate/root: `registry/CMRegistrate.java`; mod/addon entrypoints are `CTNHMana.java` and `CTNHManaGTAddon.java`.
- Items/blocks/block entities/entities: `registry/CMItems.java`, `registry/items/CMFuelItems.java`, `registry/CMBlocks.java`, `registry/CMBlockEntities.java`, `registry/CMEntities.java`.
- Machines/multiblocks: `registry/CMMachines.java`, `registry/CMMultiblockMachines.java`, plus grouped files under `registry/multiblock/`.
- Materials/elements/tag prefixes: `registry/CMMaterials.java`, `CMElements.java`, `CMTagPrefixes.java`, `GTMaterialAddon.java`.
- Recipe types/conditions: `registry/CMRecipeTypes.java`, `CMRecipeConditions.java`.
- Effects/sounds/particles/client registries: `CMMobEffects.java`, `CMParticleTypes.java`, `registry/sounds/`, `CMModelLayers.java`, `CMGuiTextures.java`.
- Recipe generation: `CTNHManaGTAddon.addRecipes()` dispatches `data/recipe/**`; keep magic-only recipes here, broad cross-module recipes in Core.
- Datagen/lang: `data/CMDatagen.java`, `data/lang/`.

## CONVENTIONS
- Namespace is `com.moguang.ctnhmana`; registry prefixes generally use `CM`.
- Generated resources are large; use `:modules:CTNH-Mana:runData` after datagen changes.
- Some resource docs such as `Custom-Material-Textures-Guide.md` are hand-authored and should not be treated as generated output.

## COMMANDS
```bash
./gradlew :modules:CTNH-Mana:build
./gradlew :modules:CTNH-Mana:runData
./gradlew :modules:CTNH-Mana:spotlessCheck
```

## ANTI-PATTERNS
- Do not rename `Mutiblock` casually; existing imports and paths rely on the current spelling.
- Do not assume magic integrations are isolated from GTCEu; machine/recipe registration still flows through GT addon patterns.
