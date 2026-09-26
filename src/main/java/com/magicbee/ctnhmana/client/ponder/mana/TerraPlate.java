package com.magicbee.ctnhmana.client.ponder.mana;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.client.ponder.CTNHManaPonderSceneBuilder;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.item.BotaniaItems;

public final class TerraPlate {

    private TerraPlate() {}

    public static void common(SceneBuilder builder, SceneBuildingUtil util) {
        CTNHManaPonderSceneBuilder scene = new CTNHManaPonderSceneBuilder(builder);
        scene.title("terra_plate", "How to use a Terra Plate", "如何使用泰拉凝聚板");
        scene.init5x5(util);
        scene.idle(20);

        var poolPos = util.grid().at(1, 1, 1);
        var platePos = util.grid().at(3, 1, 3);
        var pool = util.select().position(poolPos);
        var plate = util.select().position(platePos);
        Vec3 poolTop = util.vector().blockSurface(poolPos, Direction.UP);
        Vec3 plateTop = util.vector().blockSurface(platePos, Direction.UP);
        Vec3 poolSpark = util.vector().of(1.5, 2.5, 1.5);
        Vec3 plateSpark = util.vector().of(3.5, 2.5, 3.5);

        scene.world().showSection(pool, Direction.DOWN);
        scene.world().modifyBlockEntityNBT(pool, ManaPoolBlockEntity.class, tag -> {
            tag.putInt("mana", ManaPoolBlockEntity.MAX_MANA / 2);
            tag.putInt("manaCap", ManaPoolBlockEntity.MAX_MANA);
        }, true);
        scene.showText(80,
                "Place a mana pool as the mana source, then place the Terra Plate nearby.",
                "先放置魔力池作为魔力来源，再在附近放置泰拉凝聚板。")
                .pointAt(poolTop)
                .attachKeyFrame();
        scene.overlay().showControls(poolTop, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(BotaniaBlocks.manaPool.asItem()));
        scene.idle(90);

        scene.world().showSection(plate, Direction.DOWN);
        scene.showText(80,
                "The Terra Plate is the platform where the terrestrial agglomeration recipe runs.",
                "泰拉凝聚板是进行泰拉凝聚配方的合成平台。")
                .pointAt(plateTop)
                .attachKeyFrame();
        scene.overlay().showControls(plateTop, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(BotaniaBlocks.terraPlate.asItem()));
        scene.idle(90);

        scene.showText(100,
                "Place a Botania spark on both blocks and keep the sparks on the same network.",
                "在两个方块上各放置一枚植物魔法火花，并让两枚火花处于同一网络。")
                .pointAt(plateSpark)
                .attachKeyFrame();
        scene.overlay().showControls(poolTop, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(BotaniaItems.spark));
        scene.idle(20);
        scene.overlay().showControls(plateTop, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(BotaniaItems.spark));
        scene.idle(20);
        scene.idle(70);

        scene.world().createEntity(world -> {
            var spark = BotaniaEntities.SPARK.create(world);
            spark.setPos(poolSpark.x, poolSpark.y, poolSpark.z);
            spark.setNetwork(DyeColor.LIGHT_BLUE);
            spark.setYRot(225F);
            return spark;
        });
        scene.world().createEntity(world -> {
            var spark = BotaniaEntities.SPARK.create(world);
            spark.setPos(plateSpark.x, plateSpark.y, plateSpark.z);
            spark.setNetwork(DyeColor.LIGHT_BLUE);
            spark.setYRot(225F);
            return spark;
        });
        scene.showText(110,
                "Keep both sparks on the same network. Mana transfer begins after a complete recipe is supplied.",
                "让两枚火花处于同一网络。只有提供完整配方材料后，魔力传输才会开始。")
                .pointAt(plateSpark)
                .attachKeyFrame();
        scene.idle(140);

        scene.showText(130,
                "Drop every ingredient required by the selected recipe onto the plate.",
                "将选定配方所需的全部材料丢到凝聚板上。")
                .pointAt(plateTop)
                .attachKeyFrame();
        ElementLink<EntityElement> manaDiamond = scene.world().createItemEntity(
                plateTop.add(-0.22, 0.12, -0.12), Vec3.ZERO, new ItemStack(BotaniaItems.manaDiamond));
        scene.idle(20);
        ElementLink<EntityElement> manaPearl = scene.world().createItemEntity(
                plateTop.add(0.18, 0.12, -0.16), Vec3.ZERO, new ItemStack(BotaniaItems.manaPearl));
        scene.idle(20);
        ElementLink<EntityElement> manaSteel = scene.world().createItemEntity(
                plateTop.add(-0.16, 0.12, 0.18), Vec3.ZERO, new ItemStack(BotaniaItems.manaSteel));
        scene.idle(20);
        ElementLink<EntityElement> manaRune = scene.world().createItemEntity(
                plateTop.add(0.20, 0.12, 0.16), Vec3.ZERO, new ItemStack(BotaniaItems.runeMana));
        scene.idle(80);

        scene.showText(150,
                "This example is the Terra Steel recipe: mana diamond, mana pearl, mana steel, and a mana rune.",
                "这里演示泰拉钢配方：魔力钻石、魔力珍珠、魔力钢和魔力符文。")
                .pointAt(plateTop)
                .attachKeyFrame();
        scene.idle(160);
        PonderParticleUtil.sparkManaFlow(scene.effects(), poolSpark, plateSpark, 0x66E6FF, 1F, 180);
        scene.showText(70,
                "With every ingredient supplied, the recipe starts and the spark network transfers mana.",
                "材料齐全后，配方开始运行，火花网络会传输魔力。")
                .pointAt(plateSpark)
                .attachKeyFrame();
        scene.idle(15);
        scene.world().modifyBlockEntityNBT(pool, ManaPoolBlockEntity.class,
                tag -> tag.putInt("mana", 400_000), true);
        scene.idle(15);
        scene.world().modifyBlockEntityNBT(pool, ManaPoolBlockEntity.class,
                tag -> tag.putInt("mana", 300_000), true);
        scene.idle(15);
        scene.world().modifyBlockEntityNBT(pool, ManaPoolBlockEntity.class,
                tag -> tag.putInt("mana", 200_000), true);
        scene.idle(15);
        scene.world().modifyBlockEntityNBT(pool, ManaPoolBlockEntity.class,
                tag -> tag.putInt("mana", 100_000), true);
        scene.idle(15);
        scene.world().modifyBlockEntityNBT(pool, ManaPoolBlockEntity.class,
                tag -> tag.putInt("mana", 0), true);
        scene.idle(15);

        scene.world().modifyEntity(manaDiamond, Entity::kill);
        scene.world().modifyEntity(manaPearl, Entity::kill);
        scene.world().modifyEntity(manaSteel, Entity::kill);
        scene.world().modifyEntity(manaRune, Entity::kill);
        scene.world().createItemEntity(plateTop.add(0, 0.16, 0), Vec3.ZERO,
                new ItemStack(BotaniaItems.terrasteel));
        scene.showText(120,
                "When the recipe finishes, the ingredients are consumed and the output appears on the plate.",
                "配方完成后，材料会被消耗，产物会出现在凝聚板上。")
                .pointAt(plateTop)
                .attachKeyFrame();
        scene.idle(150);
        scene.markAsFinished();
    }
}
