package com.magicbee.ctnhmana.api.machine.gem;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemInstance;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.cutting.GemCuttingMenu;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 宝石刻格机的静态规则表。
 * <p>
 * 机器主流程不走 GT 配方秒表，而是用自定义进度点；本类集中存放：
 * <ul>
 * <li>各稀有度升级所需进度上限</li>
 * <li>通电/无电时的每 tick 增速与硬顶</li>
 * <li>珍宝材料瞬间加速（含上限）</li>
 * <li>GT 精致宝石 → Apotheosis 有瑕疵(rare) 宝石的颜色映射</li>
 * <li>完美(ancient) 升级占位钩子</li>
 * </ul>
 * 神话稀有度中文对照：碎裂(common)→开裂(uncommon)→有瑕疵(rare)→普通(epic)→无瑕(mythic)→完美(ancient)。
 */
public final class GemSublimatorRules {

    // —— 升级档所需进度（进度点，不是秒）——
    /** 碎裂 → 开裂 */
    public static final int PROGRESS_COMMON_TO_UNCOMMON = 500;
    /** 开裂 → 有瑕疵 */
    public static final int PROGRESS_UNCOMMON_TO_RARE = 2500;
    /** 有瑕疵 → 普通(epic) */
    public static final int PROGRESS_RARE_TO_EPIC = 10000;
    /** 普通 → 无瑕 */
    public static final int PROGRESS_EPIC_TO_MYTHIC = 25000;
    /** 无瑕 → 完美（首版不可启动，仅配置占位） */
    public static final int PROGRESS_MYTHIC_TO_ANCIENT = 100000;
    /** GT 精致宝石 → Apoth 有瑕疵宝石 */
    public static final int PROGRESS_EXQUISITE_TO_RARE = 10000;

    /** 无电时、进度未达阻遏前：每隔多少 tick +1 */
    public static final int UNPOWERED_INTERVAL = 5;
    /**
     * 无电自然涨的阻遏点：到达后不再按 {@link #UNPOWERED_INTERVAL} 涨，
     * 改走按档位区分的保底爬升（不回拉已有进度）。
     */
    public static final int UNPOWERED_HARD_CAP = 1000;
    /** 碎裂→开裂：保底每 20 tick（1 秒）+1 */
    public static final int CRAWL_COMMON_TO_UNCOMMON = 20;
    /** 开裂→有瑕疵：保底每 40 tick（2 秒）+1 */
    public static final int CRAWL_UNCOMMON_TO_RARE = 40;
    /** 有瑕疵→普通：保底每 100 tick（5 秒）+1 */
    public static final int CRAWL_RARE_TO_EPIC = 100;
    /** 普通→无瑕及之后：保底每 1200 tick（60 秒）+1 */
    public static final int CRAWL_LATER = 1200;
    /** GT 精致→有瑕疵：保底每 40 tick（2 秒）+1 */
    public static final int CRAWL_EXQUISITE_TO_RARE = 40;

    /**
     * 精致材料 → Apotheosis 宝石 id。
     * 用 IdentityHashMap：GT Material 按引用比较即可。
     */
    private static final Map<Material, ResourceLocation> EXQUISITE_GEM_MAP = new IdentityHashMap<>();

    static {
        // 按颜色相近做首版固定映射；未列出的材料回落 core/splendor
        // 红色系
        map(GTMaterials.Ruby, Apotheosis.loc("the_nether/blood_lord"));
        map(GTMaterials.Redstone, Apotheosis.loc("core/warlord"));
        map(GTMaterials.Cinnabar, Apotheosis.loc("the_nether/blood_lord"));
        // 蓝色系
        map(GTMaterials.Sapphire, Apotheosis.loc("core/lunar"));
        map(GTMaterials.Lapis, Apotheosis.loc("core/slipstream"));
        map(GTMaterials.Sodalite, Apotheosis.loc("core/lunar"));
        map(GTMaterials.Lazurite, Apotheosis.loc("core/slipstream"));
        map(GTMaterials.BlueTopaz, Apotheosis.loc("core/lunar"));
        // 绿色系
        map(GTMaterials.Emerald, Apotheosis.loc("overworld/earth"));
        map(GTMaterials.GreenSapphire, Apotheosis.loc("overworld/earth"));
        // 透明 / 白色系
        map(GTMaterials.Diamond, Apotheosis.loc("core/ballast"));
        map(GTMaterials.NetherQuartz, Apotheosis.loc("core/guardian"));
        map(GTMaterials.CertusQuartz, Apotheosis.loc("core/splendor"));
        map(GTMaterials.Quartzite, Apotheosis.loc("core/guardian"));
        map(GTMaterials.Coal, Apotheosis.loc("core/ballast"));
        // 紫色 / 彩系
        map(GTMaterials.Amethyst, Apotheosis.loc("core/splendor"));
        map(GTMaterials.Opal, Apotheosis.loc("overworld/royalty"));
        map(GTMaterials.Topaz, Apotheosis.loc("overworld/royalty"));
        // 暗色系
        map(GTMaterials.Netherite, Apotheosis.loc("the_end/endersurge"));
    }

    private GemSublimatorRules() {}

    private static void map(Material material, ResourceLocation gemId) {
        EXQUISITE_GEM_MAP.put(material, gemId);
    }

    /**
     * 完美(ancient) 升级是否允许启动。
     * 首版恒为 false，仅留钩子供后续解锁条件接入。
     */
    public static boolean canUpgradeToAncient() {
        // TODO: 实现完美升级真实解锁条件后再返回 true
        return false;
    }

    /** 取稀有度注册路径名（common/uncommon/...），用于 switch 与档位判断。 */
    public static String rarityPath(LootRarity rarity) {
        ResourceLocation id = RarityRegistry.INSTANCE.getKey(rarity);
        return id != null ? id.getPath() : "";
    }

    /**
     * 按「当前稀有度」查升到下一档需要的 maxProgress。
     * 未知路径返回 -1，表示不可升华。
     */
    public static int maxProgressForUpgrade(LootRarity current) {
        return switch (rarityPath(current)) {
            case "common" -> PROGRESS_COMMON_TO_UNCOMMON;
            case "uncommon" -> PROGRESS_UNCOMMON_TO_RARE;
            case "rare" -> PROGRESS_RARE_TO_EPIC;
            case "epic" -> PROGRESS_EPIC_TO_MYTHIC;
            case "mythic" -> PROGRESS_MYTHIC_TO_ANCIENT;
            default -> -1;
        };
    }

    /**
     * 宝石粉消耗：与切割台一致，{@code 1 + ordinal * 2}。
     * 按「当前稀有度」计费（开跑时一次性扣）。
     */
    public static int dustCost(LootRarity current) {
        return GemCuttingMenu.getDustCost(current);
    }

    /**
     * 精致转化模式的粉消耗：按目标有瑕疵(rare) 档的切割台费用计。
     * rare 尚未加载时回落 5（ordinal=2 时的理论值）。
     */
    public static int dustCostExquisite() {
        LootRarity rare = rarityByPath("rare");
        return rare != null ? GemCuttingMenu.getDustCost(rare) : 5;
    }

    /** 按路径名查找已注册的 {@link LootRarity}；找不到返回 null。 */
    @Nullable
    public static LootRarity rarityByPath(String path) {
        for (var holder : RarityRegistry.INSTANCE.getOrderedRarities()) {
            if (path.equals(rarityPath(holder.get()))) {
                return holder.get();
            }
        }
        return null;
    }

    /**
     * 机器电压允许产出的最高目标稀有度（不含完美特殊判断）。
     * <ul>
     * <li>ULV：最高普通(epic)——计划里「扑通」按普通=epic 理解</li>
     * <li>LV 及以上：最高无瑕(mythic)</li>
     * </ul>
     * 完美(ancient) 一律再走 {@link #canUpgradeToAncient()}。
     */
    public static LootRarity maxTargetRarity(int machineTier) {
        if (machineTier <= GTValues.ULV) {
            return rarityByPath("epic");
        }
        return rarityByPath("mythic");
    }

    /**
     * 目标稀有度是否允许被本档机器升华到。
     * 完美目标强制检查占位钩子；其余按 ordinal 与电压上限比较。
     */
    public static boolean canTargetRarity(LootRarity target, int machineTier) {
        if (target == null) {
            return false;
        }
        if ("ancient".equals(rarityPath(target))) {
            return canUpgradeToAncient();
        }
        LootRarity max = maxTargetRarity(machineTier);
        return max != null && target.ordinal() <= max.ordinal();
    }

    /** 是否为有效的、未镶嵌的 Apotheosis 宝石物品。 */
    public static boolean isApothGem(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != Adventure.Items.GEM.get()) {
            return false;
        }
        return GemInstance.unsocketed(stack).isValidUnsocketed();
    }

    /** 是否为 GT {@link TagPrefix#gemExquisite} 精致宝石。 */
    public static boolean isExquisiteGem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        MaterialEntry entry = ChemicalHelper.getMaterialEntry(stack.getItem());
        return !entry.isEmpty() && entry.tagPrefix() == TagPrefix.gemExquisite;
    }

    /** 取出精致宝石对应的 GT 材料；非精致则 null。 */
    @Nullable
    public static Material getExquisiteMaterial(ItemStack stack) {
        if (!isExquisiteGem(stack)) {
            return null;
        }
        return ChemicalHelper.getMaterialEntry(stack.getItem()).material();
    }

    /** 材料 → 目标 Apoth 宝石 id；未映射则回落 splendor。 */
    public static ResourceLocation mapExquisiteToGemId(Material material) {
        return EXQUISITE_GEM_MAP.getOrDefault(material, Apotheosis.loc("core/splendor"));
    }

    /**
     * 按 id 在动态注册表中查找宝石。
     * 不直接依赖 registry.getValue，避免不同 Placebo 版本 API 差异。
     */
    @Nullable
    public static Gem resolveGem(ResourceLocation id) {
        for (Gem gem : GemRegistry.INSTANCE.getValues()) {
            if (id.equals(gem.getId())) {
                return gem;
            }
        }
        return null;
    }

    @Nullable
    public static Gem mapExquisiteToGem(Material material) {
        return resolveGem(mapExquisiteToGemId(material));
    }

    /**
     * 生成精致转化的产出栈：真实 {@link GemRegistry#createGemStack}，稀有度为有瑕疵(rare)。
     * 注册表未就绪时返回 EMPTY。
     */
    public static ItemStack createExquisiteResult(Material material) {
        Gem gem = mapExquisiteToGem(material);
        LootRarity rare = rarityByPath("rare");
        if (gem == null || rare == null) {
            return ItemStack.EMPTY;
        }
        return GemRegistry.createGemStack(gem, rare);
    }

    /**
     * 通电加速时每 tick 扣除的 EU：{@code VA[tier] × 1A}。
     * ULV 不通电加速，恒返回 0。
     */
    public static long energyPerTick(int tier) {
        if (tier <= GTValues.ULV) {
            return 0;
        }
        return GTValues.VA[tier];
    }

    /**
     * 计算本 tick 通电应得的进度增量（不含材料加速）。
     * <p>
     * 「不能超过 XX」只约束<strong>本渠道</strong>的继续增长，绝不能把材料等其它来源推上去的进度往回钳。
     * LV 到顶后返回 0；MV+ 过阈值后减速但仍可继续涨；LuV–UV 始终 +64。
     */
    public static int tickProgressGain(int tier, boolean powered, int currentProgress) {
        if (!powered || tier <= GTValues.ULV) {
            return 1;
        }
        return switch (tier) {
            case GTValues.LV -> currentProgress < 2500 ? 1 : 0;
            case GTValues.MV -> currentProgress < 5000 ? 4 : 1;
            case GTValues.HV -> currentProgress < 10000 ? 16 : 4;
            case GTValues.EV -> currentProgress < 25000 ? 64 : 8;
            case GTValues.IV -> currentProgress < 50000 ? 64 : 16;
            default -> {
                // LuV–UV（及更高若注册）：满速 +64
                if (tier >= GTValues.LuV) {
                    yield 64;
                }
                yield 1;
            }
        };
    }

    /**
     * 通电渠道的硬顶（仅限制通电加成本身）。
     * LV = 2500；其余档位用减速阈值而非硬顶，返回 -1。
     */
    public static int poweredHardCap(int tier) {
        if (tier == GTValues.LV) {
            return 2500;
        }
        return -1;
    }

    /** 当前无电进度是否已进入阻遏区（改走极慢爬升）。 */
    public static boolean isNaturalThrottled(int progress) {
        return progress >= UNPOWERED_HARD_CAP;
    }

    /**
     * 无电过阻遏后的保底爬升间隔（tick / +1），按本轮升级档位区分。
     *
     * @param mode          1=Apoth 稀有度升级，2=精致转化
     * @param currentRarity Apoth 升级时的「当前」稀有度；精致模式可传 null
     */
    public static int unpoweredCrawlInterval(int mode, @Nullable LootRarity currentRarity) {
        if (mode == 2) {
            return CRAWL_EXQUISITE_TO_RARE;
        }
        if (currentRarity == null) {
            return CRAWL_LATER;
        }
        return switch (rarityPath(currentRarity)) {
            case "common" -> CRAWL_COMMON_TO_UNCOMMON;
            case "uncommon" -> CRAWL_UNCOMMON_TO_RARE;
            case "rare" -> CRAWL_RARE_TO_EPIC;
            default -> CRAWL_LATER; // epic→mythic、mythic→ancient 等
        };
    }

    /**
     * 无电阻遏前的正常自然增长一步（+1，且本步不跨过阻遏点）。
     * <b>绝不会</b>把已超过阻遏的进度往回钳。
     */
    public static int applyNaturalGain(int progress, int maxProgress) {
        if (progress >= UNPOWERED_HARD_CAP) {
            return progress;
        }
        return Math.min(progress + 1, Math.min(UNPOWERED_HARD_CAP, maxProgress));
    }

    /**
     * 无电过阻遏后的极慢爬升：+1，可超过 1000，只受本轮 {@code maxProgress} 限制。
     */
    public static int applyNaturalCrawlGain(int progress, int maxProgress) {
        if (progress >= maxProgress) {
            return progress;
        }
        return Math.min(progress + 1, maxProgress);
    }

    /**
     * 将「本渠道」新增后的进度钳到渠道硬顶；已超过硬顶的进度保持不变（不回拉）。
     *
     * @param hardCap 渠道硬顶；{@code < 0} 表示本渠道无硬顶
     */
    public static int clampChannelGain(int before, int after, int hardCap, int maxProgress) {
        int next = after;
        if (hardCap >= 0 && before < hardCap) {
            // 只阻止本渠道跨过硬顶；before 已在硬顶之上时由调用方不应再走本渠道
            next = Math.min(next, hardCap);
        }
        return Math.min(next, maxProgress);
    }

    /**
     * 单个珍宝材料槽的加速定义。
     *
     * @param item  对应 Apotheosis 珍宝材料注册对象（延迟 get，避免类加载过早）
     * @param boost 消耗 1 个立刻增加的进度
     * @param cap   该材料能把进度推到的上限；{@code -1} 表示无上限
     */
    public record MaterialBoost(RegistryObject<Item> item, int boost, int cap) {

        public Item resolveItem() {
            return item.get();
        }

        public boolean hasCap() {
            return cap >= 0;
        }

        /**
         * 在当前进度上应用一次加速。
         * 已达上限则进度不变（调用方据此决定是否扣物品）。
         */
        public int apply(int progress) {
            int next = progress + boost;
            if (hasCap()) {
                if (progress >= cap) {
                    return progress;
                }
                return Math.min(next, cap);
            }
            return next;
        }
    }

    /**
     * 与 UI 下方 5 个材料槽一一对应（index 0→uncommon … 4→ancient）：
     * <ul>
     * <li>陈旧布匹 +50，顶 1000</li>
     * <li>发光水晶碎片 +250，顶 5000</li>
     * <li>玄奥沙 +2000，顶 10000</li>
     * <li>神铸珍珠 +5000，无上限</li>
     * <li>无限之体现 +100000，无上限</li>
     * </ul>
     */
    public static final MaterialBoost[] MATERIAL_SLOTS = {
            new MaterialBoost(Adventure.Items.UNCOMMON_MATERIAL, 50, 1000),
            new MaterialBoost(Adventure.Items.RARE_MATERIAL, 250, 5000),
            new MaterialBoost(Adventure.Items.EPIC_MATERIAL, 2000, 10000),
            new MaterialBoost(Adventure.Items.MYTHIC_MATERIAL, 5000, -1),
            new MaterialBoost(Adventure.Items.ANCIENT_MATERIAL, 100000, -1),
    };

    /** 判断物品是否为指定材料槽应放的那一种珍宝材料。 */
    public static boolean isMaterialForSlot(ItemStack stack, int slot) {
        return slot >= 0 && slot < MATERIAL_SLOTS.length && !stack.isEmpty() &&
                stack.getItem() == MATERIAL_SLOTS[slot].resolveItem();
    }
}
