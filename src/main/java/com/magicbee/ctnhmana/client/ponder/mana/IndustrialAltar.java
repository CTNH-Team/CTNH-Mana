package com.magicbee.ctnhmana.client.ponder.mana;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.data.GTMachines;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import com.magicbee.ctnhmana.client.ponder.CTNHManaPonderSceneBuilder;

/**
 * 工业血祭坛的思索：演示 2 级 → 3 级 → 4 级 → 回到 3 级的结构升级。
 * <p>
 * storyboard 存的是 <b>4 级超集</b>（15x15x15，结构占 x1..13 / y1..10 / z1..13），
 * 开场只显示 2 级核心，再逐层“长出”外壳。
 * <p>
 * 之所以能这样演：把 5 套 pattern 都还原到同一坐标系后，它们是严格包含关系
 * （base ⊂ L3 ⊂ L4 ⊂ L5 ⊂ L6），重叠位置上的方块 id 完全一致——所以同一个
 * storyboard 可以按 box 露出/隐藏，同一帧内升降级都不会有方块冲突。
 * <p>
 * 坐标推导：{@code world = controller + (-(c-ctlC), r-ctlR, -(a-ctlA))}，主方块（L4 符号 V）
 * 落在 {@code grid(7, 4, 5)}，结构内的血祭坛（符号 Z）落在 {@code grid(7, 5, 7)}，
 * 正是主方块的 {@code (0, +1, +2)}，与 {@code MachineUtils.getOffset(this, 0, 1, 2)} 一致。
 */
public class IndustrialAltar {

    // ---------------------------------------------------------------- 结构分解
    // 下面三组 box 由 pattern 还原后求“精确覆盖”得到，恰好等于各级的方块集合，
    // 彼此之间零重叠：L2_CORE = 2 级全部；L3_SHELL = 3 级新增；L4_SHELL = 4 级新增。

    /** L2 核心（含主方块）：2 个 box。 */
    private static final int[][] L2_CORE = {
            { 5, 4, 5, 9, 4, 9 },
            { 7, 5, 7, 7, 5, 7 },
    };

    /** 2 -> 3 级新增的外壳：27 个 box（与 L2 无重叠）。 */
    private static final int[][] L3_SHELL = {
            { 3, 2, 3, 11, 2, 3 },
            { 3, 2, 4, 3, 2, 11 },
            { 3, 3, 3, 3, 5, 3 },
            { 3, 3, 11, 3, 5, 11 },
            { 4, 2, 11, 11, 2, 11 },
            { 4, 3, 4, 10, 3, 5 },
            { 4, 3, 6, 5, 3, 10 },
            { 4, 4, 4, 4, 7, 4 },
            { 4, 4, 10, 4, 7, 10 },
            { 5, 5, 5, 5, 8, 5 },
            { 5, 5, 9, 5, 8, 9 },
            { 5, 8, 6, 5, 8, 8 },
            { 6, 3, 9, 10, 3, 10 },
            { 6, 8, 5, 9, 8, 5 },
            { 6, 8, 9, 9, 8, 9 },
            { 6, 9, 6, 8, 9, 8 },
            { 7, 7, 7, 7, 8, 7 },
            { 7, 10, 7, 7, 10, 7 },
            { 9, 3, 6, 10, 3, 8 },
            { 9, 5, 5, 9, 7, 5 },
            { 9, 5, 9, 9, 7, 9 },
            { 9, 8, 6, 9, 8, 8 },
            { 10, 4, 4, 10, 7, 4 },
            { 10, 4, 10, 10, 7, 10 },
            { 11, 2, 4, 11, 2, 10 },
            { 11, 3, 3, 11, 5, 3 },
            { 11, 3, 11, 11, 5, 11 },
    };

    /** 3 -> 4 级新增的外壳：16 个 box（与 L3 无重叠）。 */
    private static final int[][] L4_SHELL = {
            { 1, 1, 1, 13, 1, 1 },
            { 1, 1, 2, 1, 1, 13 },
            { 1, 2, 1, 1, 7, 1 },
            { 1, 2, 13, 1, 7, 13 },
            { 2, 1, 13, 13, 1, 13 },
            { 2, 2, 2, 12, 2, 2 },
            { 2, 2, 3, 2, 2, 12 },
            { 2, 3, 2, 2, 7, 2 },
            { 2, 3, 12, 2, 7, 12 },
            { 3, 2, 12, 12, 2, 12 },
            { 12, 2, 3, 12, 2, 11 },
            { 12, 3, 2, 12, 7, 2 },
            { 12, 3, 12, 12, 7, 12 },
            { 13, 1, 2, 13, 1, 12 },
            { 13, 2, 1, 13, 7, 1 },
            { 13, 2, 13, 13, 7, 13 },
    };

    /** 主方块（L4 符号 V）。 */
    private static final int CONTROLLER_X = 7;
    private static final int CONTROLLER_Y = 4;
    private static final int CONTROLLER_Z = 5;

    /** 结构内的血祭坛（符号 Z），正好在主方块的 (0, +1, +2)。 */
    private static final int ALTAR_X = 7;
    private static final int ALTAR_Y = 5;
    private static final int ALTAR_Z = 7;

    /** L4 外壳上、同时也在 L3 里合法的仓室位（符号 B，visible 面）。 */
    private static final int HATCH_X = 4;
    private static final int HATCH_Y = 2;
    private static final int HATCH_Z = 3;

    private IndustrialAltar() {}

    public static void Common(SceneBuilder builder, SceneBuildingUtil util) {
        CTNHManaPonderSceneBuilder scene = new CTNHManaPonderSceneBuilder(builder);
        scene.title("industrial_altar", "How to build the Industrial Blood Altar",
                "如何搭建工业血之祭坛", "Industrial Blood Altar", "工业血之祭坛");
        scene.configureBasePlate(0, 0, 15);
        scene.scaleSceneView(0.42f);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);

        // ---- 1. 先看 2 级核心：主方块 + 血祭坛 ----
        scene.world().showSection(select(util, L2_CORE), Direction.DOWN);
        scene.showText(60,
                "It is a multi-pattern multiblock. It starts as the Level 2 core: a platform with the Blood Altar.",
                "它是多结构多方块。起点是 2 级核心：一层平台，中间嵌着血祭坛。")
                .pointAt(surface(util, CONTROLLER_X, CONTROLLER_Y, CONTROLLER_Z))
                .attachKeyFrame();
        scene.idle(70);

        // ---- 2. 核心中的血祭坛被自动绑定 ----
        scene.overlay().showOutline(PonderPalette.RED, "inner_altar",
                util.select().position(ALTAR_X, ALTAR_Y, ALTAR_Z), 70);
        scene.showText(70,
                "This Blood Altar is the core: the structure binds to it automatically.",
                "这个血祭坛是核心：结构会自动与它绑定。")
                .pointAt(surface(util, ALTAR_X, ALTAR_Y, ALTAR_Z))
                .attachKeyFrame();
        scene.idle(80);

        scene.showText(70,
                "The altar itself must already be tier 2 or higher, or the structure will not form.",
                "血祭坛自身必须已经达到 2 级或以上，否则结构无法成型。")
                .pointAt(surface(util, ALTAR_X, ALTAR_Y, ALTAR_Z))
                .attachKeyFrame();
        scene.idle(80);

        // ---- 3. 升级：2 -> 3 级 ----
        scene.showText(60, "Add the second ring of runes and pillars.", "加上第二圈符文与立柱。")
                .attachKeyFrame();
        scene.idle(15);
        scene.world().showSection(select(util, L3_SHELL), Direction.DOWN);
        scene.idle(60);
        scene.showText(70,
                "The controller re-matches a larger pattern: this is the Level 3 structure.",
                "主方块会重新匹配更大的结构：这就是 3 级结构。")
                .pointAt(surface(util, CONTROLLER_X, CONTROLLER_Y, CONTROLLER_Z))
                .attachKeyFrame();
        scene.idle(80);

        // ---- 4. 升级：3 -> 4 级 ----
        scene.showText(60, "Keep growing it outward.", "继续向外扩建。")
                .attachKeyFrame();
        scene.idle(15);
        scene.world().showSection(select(util, L4_SHELL), Direction.DOWN);
        scene.idle(60);
        scene.showText(70,
                "Now it is the Level 4 structure. Build it bigger still and it reaches Level 6.",
                "现在是 4 级结构。继续扩大还能到 6 级。")
                .attachKeyFrame();
        scene.idle(80);

        scene.showText(70,
                "The machine level is the higher of the altar tier and the matched pattern level.",
                "机器等级取两者较大值：血祭坛自身等级，与匹配到的结构等级。")
                .attachKeyFrame();
        scene.idle(80);

        // ---- 5. 降级：4 -> 3 级 ----
        scene.showText(60, "Remove the outer ring and it drops back down.", "拆掉最外一圈，等级就会回落。")
                .attachKeyFrame();
        scene.idle(15);
        scene.world().hideSection(select(util, L4_SHELL), Direction.UP);
        scene.idle(60);
        scene.showText(70,
                "Back to Level 3. The structure downgrades to whatever still matches.",
                "回到 3 级。结构会降级到仍然满足的那个等级。")
                .pointAt(surface(util, CONTROLLER_X, CONTROLLER_Y, CONTROLLER_Z))
                .attachKeyFrame();
        scene.idle(80);

        // ---- 6. 仓室接入 ----
        scene.overlay().showControls(surface(util, HATCH_X, HATCH_Y, HATCH_Z), Pointing.DOWN, 40)
                .rightClick()
                .withItem(GTMachines.FLUID_IMPORT_HATCH[GTValues.EV].asStack());
        scene.world().setBlock(util.grid().at(HATCH_X, HATCH_Y, HATCH_Z),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.EV].defaultBlockState(), true);
        scene.showText(80,
                "Some ring blocks, such as this brick, can be replaced by hatches for Life Essence and power.",
                "部分外壳方块（例如这个砖块）可以替换成仓室，用来输入生命源质和电力。")
                .pointAt(surface(util, HATCH_X, HATCH_Y, HATCH_Z))
                .attachKeyFrame();
        scene.idle(80);

        scene.markAsFinished();
    }

    /** 把 box 列表并成一个 Selection。 */
    private static Selection select(SceneBuildingUtil util, int[][] boxes) {
        Selection result = null;
        for (int[] b : boxes) {
            Selection one = util.select().fromTo(b[0], b[1], b[2], b[3], b[4], b[5]);
            result = result == null ? one : result.add(one);
        }
        return result;
    }

    private static Vec3 surface(SceneBuildingUtil util, int x, int y, int z) {
        return util.vector().blockSurface(util.grid().at(x, y, z), Direction.UP);
    }
}
