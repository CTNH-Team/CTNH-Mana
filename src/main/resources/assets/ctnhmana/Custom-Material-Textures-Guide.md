---
title: 自定义材质纹理指南
---

# GTCEu 自定义材质纹理注册指南

本指南将详细介绍如何在 GTCEu Modern 中注册自定义的锭和各种其他部件的材质纹理。

## 目录

- [GTCEu 材质注册机制](#gtceu-材质注册机制)
- [核心组件](#核心组件)
- [材质纹理路径规则](#材质纹理路径规则)
- [如何注册自定义材质](#如何注册自定义材质)
- [只对特定部件使用自定义材质](#只对特定部件使用自定义材质)
- [实际示例](#实际示例)
- [材质查找机制](#材质查找机制)

## GTCEu 材质注册机制

GTCEu 使用一套完整的材质系统来自动生成和管理各种材料部件（锭、粉、板、齿轮等）的纹理。系统通过以下三个核心组件协同工作：

### 核心组件

#### 1. TagPrefix（标签前缀）

`TagPrefix` 定义了各种部件类型，例如：

- `TagPrefix.ingot` - 锭
- `TagPrefix.dust` - 粉
- `TagPrefix.plate` - 板
- `TagPrefix.gear` - 齿轮
- `TagPrefix.rod` - 棒
- `TagPrefix.nugget` - 粒
- 等等...

每个 `TagPrefix` 都关联一个 `MaterialIconType`，用于确定该部件使用的纹理类型。

#### 2. MaterialIconType（材质图标类型）

`MaterialIconType` 定义了材质图标的类型，对应不同的 `TagPrefix`：

- `MaterialIconType.ingot` - 锭图标
- `MaterialIconType.dust` - 粉图标
- `MaterialIconType.plate` - 板图标
- `MaterialIconType.gear` - 齿轮图标
- 等等...

#### 3. MaterialIconSet（材质图标集）

`MaterialIconSet` 定义了材质的外观风格，例如：

- `MaterialIconSet.METALLIC` - 金属风格
- `MaterialIconSet.SHINY` - 闪亮风格
- `MaterialIconSet.DULL` - 暗淡风格
- `MaterialIconSet.DIAMOND` - 钻石风格
- `MaterialIconSet.EMERALD` - 绿宝石风格
- 等等...

### 材质纹理路径规则

GTCEu 按照以下路径规则查找材质纹理：

**物品纹理路径：**
```
assets/gtceu/textures/item/material_sets/{iconSet}/{iconType}.png
```

**方块纹理路径：**
```
assets/gtceu/textures/block/material_sets/{iconSet}/{iconType}.png
```

**示例：**
- 锭（ingot）使用 `metallic` 图标集：`assets/gtceu/textures/item/material_sets/metallic/ingot.png`
- 粉（dust）使用 `dull` 图标集：`assets/gtceu/textures/item/material_sets/dull/dust.png`
- 板（plate）使用 `metallic` 图标集：`assets/gtceu/textures/item/material_sets/metallic/plate.png`

## 如何注册自定义材质

### 方法一：创建自定义 MaterialIconSet（推荐）

这是最灵活的方法，允许你创建完全自定义的材质图标集。

#### Java 代码示例

```java
// 创建自定义图标集，继承自 METALLIC
MaterialIconSet customIconSet = new MaterialIconSet("custom_metallic", MaterialIconSet.METALLIC);

// 在材质创建时指定
Material customMaterial = new Material.Builder(GTCEu.id("custom_metal"))
    .ingot()
    .iconSet(customIconSet)  // 设置自定义图标集
    .color(0xFF5733)
    .build();
```

#### KubeJS 示例

```js
GTCEuStartupEvents.registry('gtceu:material_icon_set', event => {
    event.create('custom_metallic')
        .parent('metallic')  // 继承自 metallic 图标集
})

GTCEuStartupEvents.registry('gtceu:material', event => {
    event.create('custom_metal')
        .ingot()
        .iconSet('custom_metallic')  // 使用自定义图标集
        .color(0xFF5733)
})
```

#### 资源包文件结构

创建资源包，在以下路径放置纹理文件：

```
assets/gtceu/textures/item/material_sets/custom_metallic/
  ├── ingot.png
  ├── dust.png
  ├── plate.png
  ├── gear.png
  └── ... (其他需要的部件纹理)
```

### 方法二：使用资源包直接覆盖纹理

如果你只想覆盖现有材质的部分纹理，可以直接在资源包中提供纹理文件。

**示例：**

假设你的材质使用 `MaterialIconSet.METALLIC`，你可以在资源包中覆盖特定纹理：

```
assets/gtceu/textures/item/material_sets/metallic/
  ├── ingot.png          (自定义锭纹理，覆盖原版)
  └── (其他部件使用原版，不提供文件即可)
```

### 方法三：使用 setIgnored() 指定自定义物品

如果你已经有现成的自定义物品，可以让 GTCEu 使用它而不是生成新物品。

#### Java 代码示例

```java
// 在 materialModification 事件中
GTCEuStartupEvents.materialModification(event -> {
    TagPrefix.ingot.setIgnored(myMaterial, myCustomIngotItem);
});
```

#### KubeJS 示例

```js
GTCEuStartupEvents.materialModification(event => {
    TagPrefix.ingot.setIgnored(
        GTMaterialRegistry.getMaterial("my_material"),
        Item.of("mymod:custom_ingot")  // 注意：需要使用实际的 ItemLike 对象
    )
})
```

!!! caution "注意事项"
    在 KubeJS 中，`Item.of()` 可能不适用于 `setIgnored()`。你需要直接传递 Java 的 `ItemLike` 对象。

## 只对特定部件使用自定义材质

如果你只想为某些部件（如锭）提供自定义纹理，而其他部件（如粉、板）使用原版纹理，有以下几种方案：

### 方案 A：创建专门的 MaterialIconSet

为特定部件创建专门的图标集，但材质仍使用默认图标集。

#### 实现步骤

1. **创建材质时使用默认图标集：**

```java
Material myMaterial = new Material.Builder(GTCEu.id("my_metal"))
    .ingot()
    .iconSet(MaterialIconSet.METALLIC)  // 使用默认图标集
    .build();
```

2. **在资源包中提供自定义纹理：**

```
assets/gtceu/textures/item/material_sets/metallic/
  ├── ingot.png          (自定义锭纹理)
  └── (不提供其他文件，让系统使用原版)
```

3. **系统会自动查找：**
   - 锭：使用你提供的 `metallic/ingot.png`
   - 粉：查找 `metallic/dust.png`，如果不存在则使用父图标集
   - 板：查找 `metallic/plate.png`，如果不存在则使用父图标集

### 方案 B：使用资源包优先级覆盖

在资源包中只覆盖需要的纹理，其他部件会自动使用原版。

**资源包结构：**

```
assets/gtceu/textures/item/material_sets/metallic/
  ├── ingot.png          (只提供自定义锭纹理)
  └── (其他部件不提供，使用原版)
```

### 方案 C：通过代码动态注册（高级）

如果你需要更精细的控制，可以通过代码动态注册纹理。

```java
// 在客户端初始化时
if (GTCEu.isClientSide()) {
    Material myMaterial = ...;
    TagPrefix ingot = TagPrefix.ingot;
    
    // 使用 GTDynamicResourcePack 添加自定义纹理
    ResourceLocation customTexture = new ResourceLocation("yourmod", "textures/item/custom_ingot.png");
    // 需要读取纹理文件并添加到动态资源包
    // GTDynamicResourcePack.addItemTexture(customTexture, textureData);
}
```

## 实际示例

### 示例 1：完全自定义材质

假设你要创建一个名为 `MyCustomMetal` 的材质，所有部件都使用自定义纹理。

#### 步骤 1：创建自定义图标集

```js
// KubeJS
GTCEuStartupEvents.registry('gtceu:material_icon_set', event => {
    event.create('my_custom_set')
        .parent('metallic')
})
```

#### 步骤 2：创建材质

```js
GTCEuStartupEvents.registry('gtceu:material', event => {
    event.create('my_custom_metal')
        .ingot()
        .iconSet('my_custom_set')
        .color(0xFF5733)
        .flags(GTMaterialFlags.GENERATE_PLATE, GTMaterialFlags.GENERATE_GEAR)
})
```

#### 步骤 3：提供纹理文件

在资源包中创建以下文件：

```
assets/gtceu/textures/item/material_sets/my_custom_set/
  ├── ingot.png
  ├── dust.png
  ├── plate.png
  ├── gear.png
  └── nugget.png
```

### 示例 2：只自定义锭纹理

假设你只想为材质自定义锭的纹理，其他部件使用原版。

#### 步骤 1：创建材质（使用默认图标集）

```js
GTCEuStartupEvents.registry('gtceu:material', event => {
    event.create('my_metal')
        .ingot()
        .iconSet('metallic')  // 使用默认 metallic 图标集
        .color(0xFF5733)
        .flags(GTMaterialFlags.GENERATE_PLATE, GTMaterialFlags.GENERATE_GEAR)
})
```

#### 步骤 2：在资源包中只提供锭纹理

```
assets/gtceu/textures/item/material_sets/metallic/
  └── ingot.png          (只提供自定义锭纹理)
```

#### 结果

- ✅ 锭：使用你提供的 `metallic/ingot.png`
- ✅ 粉：使用原版 `metallic/dust.png`（如果存在）或父图标集
- ✅ 板：使用原版 `metallic/plate.png`（如果存在）或父图标集
- ✅ 其他部件：使用原版纹理

### 示例 3：使用 setIgnored() 关联已有物品

假设你有一个来自其他模组的物品，想让它作为 GTCEu 材质的锭。

```js
GTCEuStartupEvents.materialModification(event => {
    // 假设你有一个来自其他模组的锭物品
    const customIngot = Item.of("othermod:custom_ingot")
    const myMaterial = GTMaterialRegistry.getMaterial("my_metal")
    
    // 让 GTCEu 使用这个已有物品作为锭
    TagPrefix.ingot.setIgnored(myMaterial, customIngot)
})
```

## 材质查找机制

根据 GTCEu 的源码实现，材质纹理的查找遵循以下规则：

### 查找顺序

1. **首先查找当前图标集的纹理**
   - 路径：`textures/item/material_sets/{currentIconSet}/{iconType}.png`

2. **如果不存在，向上查找父图标集**
   - 如果当前图标集有父图标集，会继续向上查找
   - 例如：`custom_metallic` → `metallic` → `dull`

3. **如果都不存在，使用默认纹理**
   - 系统会使用 `GTModels.BLANK_TEXTURE` 或默认纹理

### 图标集继承关系

图标集可以继承自其他图标集，形成继承链：

```
SHINY → METALLIC → DULL (根图标集)
BRIGHT → SHINY → METALLIC → DULL
DIAMOND → SHINY → METALLIC → DULL
```

当你创建一个继承自 `METALLIC` 的图标集时：
- 如果该图标集中没有某个部件的纹理，系统会自动查找 `METALLIC` 中的纹理
- 如果 `METALLIC` 中也没有，会继续向上查找

### 缓存机制

GTCEu 会缓存已查找的纹理路径，以提高性能：
- 第一次查找时会缓存结果
- 后续查找直接使用缓存

## 常见问题

### Q: 如何知道我的材质使用了哪个图标集？

A: 在材质创建时，如果没有指定 `iconSet()`，系统会根据材质属性自动选择：
- 有 `GEM` 属性 → `GEM_VERTICAL`
- 有 `DUST`、`INGOT` 或 `POLYMER` 属性 → `DULL`
- 有 `FLUID` 属性 → `FLUID`
- 其他情况 → `DULL`

### Q: 我可以为同一个材质的不同部件使用不同的图标集吗？

A: 不可以。一个材质只能使用一个 `MaterialIconSet`。但你可以：
- 在资源包中为不同部件提供不同的纹理（只要它们在同一图标集路径下）
- 使用 `setIgnored()` 为特定部件指定完全自定义的物品

### Q: 如何查看 GTCEu 使用的默认纹理路径？

A: 你可以查看 GTCEu 的资源文件，路径通常在：
```
assets/gtceu/textures/item/material_sets/{iconSet}/{iconType}.png
```

### Q: 纹理文件需要什么格式？

A: 纹理文件必须是 PNG 格式，建议使用 16x16 或 32x32 像素的纹理。

### Q: 如何调试材质纹理问题？

A: 
1. 检查纹理文件路径是否正确
2. 确认材质使用的图标集名称
3. 检查资源包的加载顺序
4. 查看游戏日志中的资源加载错误

## 总结

GTCEu 的材质系统非常灵活，提供了多种方式来注册自定义材质纹理：

1. **创建自定义 MaterialIconSet** - 最灵活，适合完全自定义
2. **使用资源包覆盖** - 简单直接，适合部分覆盖
3. **使用 setIgnored()** - 适合关联已有物品

记住关键点：
- 材质纹理路径：`assets/gtceu/textures/item/material_sets/{iconSet}/{iconType}.png`
- 只需提供需要的纹理文件，其他部件会自动使用原版或父图标集的纹理
- 图标集支持继承，可以复用父图标集的纹理

通过合理使用这些方法，你可以轻松地为你的材质创建自定义纹理，同时保持与其他部件的兼容性。

