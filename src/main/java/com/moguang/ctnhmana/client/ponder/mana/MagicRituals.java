package com.moguang.ctnhmana.client.ponder.mana;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.client.ponder.CTNHManaPonderSceneBuilder;

public class MagicRituals {

    private MagicRituals() {}

    public static void Blaze(SceneBuilder builder, SceneBuildingUtil util) {
        CTNHManaPonderSceneBuilder scene = new CTNHManaPonderSceneBuilder(builder);
        scene.title("blaze", "How to spawn blaze without entering nether", "如何不进入下界生成烈焰人");
        scene.configureBasePlate(0, 0, 2);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(0, 0, 0, 2, 4, 2), Direction.SOUTH);
        scene.world().replaceBlocks(util.select().fromTo(1, 1, 1, 1, 3, 1), Blocks.AIR.defaultBlockState(), false);
        scene.idle(10);

        var blaze = scene.world().createEntity(world -> {
            var entity = EntityType.BLAZE.create(world);
            entity.setPos(1, 1, 1);
            return entity;
        });
        scene.showText(60, "Normally, blaze cannot be found before entering the Nether.",
                "通常情况下，进入下界前无法遇到烈焰人。")
                .pointAt(util.vector().of(1, 1.5, 1))
                .attachKeyFrame();
        scene.idle(60);
        scene.world().modifyEntity(blaze, entity -> entity.kill());
        scene.idle(10);

        scene.world().setBlocks(util.select().fromTo(1, 1, 1, 1, 2, 1), Blocks.SNOW_BLOCK.defaultBlockState(), true);
        scene.world().setBlock(util.grid().at(1, 3, 1), Blocks.CARVED_PUMPKIN.defaultBlockState(), true);
        scene.idle(10);
        scene.showText(60, "This is the normal way to place a snow golem.",
                "这是通常摆放雪傀儡的方式。")
                .pointAt(util.vector().of(1, 1.5, 1))
                .attachKeyFrame();
        scene.idle(60);

        scene.showText(80,
                "Replace snow blocks with iron bars and the carved pumpkin with a fel pumpkin to spawn a blaze.",
                "将雪块替换为铁栏杆，将雕刻南瓜替换为魔力南瓜，就能生成烈焰人。")
                .pointAt(util.vector().of(1, 1.5, 1))
                .attachKeyFrame();
        scene.world().replaceBlocks(util.select().fromTo(1, 1, 1, 1, 2, 1), Blocks.IRON_BARS.defaultBlockState(),
                true);
        scene.world().setBlock(util.grid().at(1, 3, 1), block("botania", "fel_pumpkin").defaultBlockState(), true);
        scene.idle(40);
        scene.world().replaceBlocks(util.select().fromTo(1, 1, 1, 1, 3, 1), Blocks.AIR.defaultBlockState(), true);
        scene.world().createEntity(world -> {
            var entity = EntityType.BLAZE.create(world);
            entity.setPos(1, 1, 1);
            return entity;
        });
        scene.markAsFinished();
    }

    public static void RuneRitual(SceneBuilder builder, SceneBuildingUtil util) {
        CTNHManaPonderSceneBuilder scene = new CTNHManaPonderSceneBuilder(builder);
        scene.title("rune_ritual", "How to finish the rune ritual", "如何完成符文仪式");
        scene.configureBasePlate(0, 0, 8);
        scene.scaleSceneView(0.65f);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(0, 1, 0, 8, 1, 8), Direction.DOWN);
        scene.idle(20);

        scene.overlay().showOutline(PonderPalette.GREEN, "central_holder", util.select().position(4, 1, 4), 60);
        scene.showText(60, "First, place the central rune holder.", "首先放置中央符文座。")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 1, 4), Direction.UP))
                .attachKeyFrame();
        scene.idle(70);

        scene.showText(60, "Then place other rune holders at the positions shown in JEI.",
                "然后按 JEI 显示的位置放置其他符文座。")
                .attachKeyFrame();
        int[][] holders = { { 1, 1, 4 }, { 3, 1, 2 }, { 3, 1, 6 }, { 5, 1, 2 }, { 5, 1, 6 }, { 7, 1, 4 } };
        for (int[] holder : holders) {
            scene.world().showSection(util.select().position(holder[0], holder[1], holder[2]), Direction.DOWN);
            scene.idle(5);
        }
        scene.idle(30);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(4, 1, 4), Direction.UP),
                Pointing.DOWN, 40)
                .rightClick()
                .withItem(item("gtceu", "chipped_glass_gem"));
        scene.showText(80, "Put the right item on the right position.", "将正确物品放在正确位置。")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 1, 4), Direction.UP))
                .attachKeyFrame();
        scene.idle(60);

        scene.showText(60, "Then drop the ritual catalysts in the area.", "然后把仪式催化物丢入区域内。")
                .attachKeyFrame();
        scene.world().createItemEntity(util.vector().of(3, 2, 4), util.vector().of(0.1, 0, 0.2),
                item("botania", "rune_wrath"));
        scene.idle(10);
        scene.world().createItemEntity(util.vector().of(3, 2, 4), util.vector().of(0.2, 0, 0.2),
                item("botania", "rune_greed"));
        scene.idle(10);
        scene.world().createItemEntity(util.vector().of(3, 2, 4), util.vector().of(0.1, 0, 0.15),
                item("alexscaves", "shadow_silk"));
        scene.idle(50);

        var trader = scene.world().createEntity(world -> {
            var entity = EntityType.WANDERING_TRADER.create(world);
            entity.setPos(5, 1, 4);
            return entity;
        });
        scene.showText(60, "Also prepare a wandering trader here for sacrifice.",
                "同时在这里准备一名流浪商人作为献祭目标。")
                .attachKeyFrame();
        scene.idle(60);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(4, 1, 4), Direction.UP),
                Pointing.DOWN, 20)
                .rightClick()
                .withItem(item("botania", "twig_wand"));
        scene.showText(60,
                "Finally, make sure your mana ring has enough mana, then right-click the central rune holder.",
                "最后确认魔力戒指中有足够魔力，然后右键中央符文座。")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 1, 4), Direction.UP))
                .attachKeyFrame();
        scene.idle(60);
        scene.world().modifyEntity(trader, entity -> entity.kill());
        scene.showText(40, "If the dropped items disappear, the ritual has started successfully.",
                "如果地上的物品被清除，说明仪式已成功启动。")
                .attachKeyFrame();
        scene.markAsFinished();
    }

    public static void RitualDiviner(SceneBuilder builder, SceneBuildingUtil util) {
        CTNHManaPonderSceneBuilder scene = new CTNHManaPonderSceneBuilder(builder);
        scene.title("ritual_diviner", "How to use ritual diviner", "如何使用仪式推测杖");
        scene.scaleSceneView(0.6f);
        scene.showBasePlate();
        scene.idle(10);
        scene.world().showSection(util.select().position(8, 1, 8), Direction.DOWN);
        scene.showText(60, "First, place a master ritual stone.", "首先放置主仪式石。")
                .pointAt(util.vector().blockSurface(util.grid().at(8, 1, 8), Direction.UP))
                .attachKeyFrame();
        scene.idle(60);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(8, 1, 8), Direction.UP),
                Pointing.DOWN, 40)
                .rightClick()
                .withItem(item("bloodmagic", "ritualdivinerdusk"));
        scene.showText(60, "Right-click it with the dusk ritual diviner to place the selected ritual.",
                "使用黄昏仪式推测杖右键主仪式石，即可放置选中的仪式。")
                .attachKeyFrame();
        scene.markAsFinished();
    }

    private static Block block(String namespace, String path) {
        return ForgeRegistries.BLOCKS.getValue(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    private static ItemStack item(String namespace, String path) {
        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(namespace, path));
        return item == null ? GTItems.TERMINAL.asStack() : new ItemStack(item);
    }
}
