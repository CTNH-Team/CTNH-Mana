package com.moguang.ctnhmana.common.multi;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.common.parts.ManaHatches.BloodManaHatch;
import com.moguang.ctnhmana.common.ritual.MachineRitualSoulNetwork;
import com.moguang.ctnhmana.common.ritual.MachineRitualStoneHost;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.ItemBloodOrb;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.ritual.Ritual;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 工业血祭仪式阵控制器。
 * <p>
 * LP 通过 {@link MachineRitualSoulNetwork} 从凝聚仓储罐扣除，<b>不消耗</b>玩家全局灵魂网络。
 * Orb 仅用于绑定仪式主人 UUID（{@link #getOrbOwnerId}）。
 */
public class RitualMechanicalMachine extends ManaMachine {

    /** 与 {@link BaseManaMachine} 主面板相同的缩放系数 */
    private static final double BASE_MANA_UI_SCALE = 1.2;

    private static int sc(int v) {
        return (int) Math.round(v * BASE_MANA_UI_SCALE);
    }

    public static final String RECIPE_DATA_RITUAL_ID = "ritual_id";
    /** 可选：datagen 中覆盖 {@link #getRitualLpCost} 的显示值（如坠星触媒 LP） */
    public static final String RECIPE_DATA_RITUAL_LP = "ritual_lp";

    /** 需要主人在线（{@code ServerPlayer}）的仪式 ID */
    private static final Set<String> ONLINE_OWNER_RITUALS = Set.of("bosssummon", "shroudsight");

    /** 成型后每 5 秒检查凝聚仓血 Orb 并刷新虚拟灵魂网络 */
    private static final int ORB_CHECK_INTERVAL = 100;

    /** 虚拟灵魂网络 LP 缓存上限（10000 万 LP） */
    public static final int RITUAL_LP_CACHE_CAPACITY = 100_000_000;

    /** 配方最短时长（1 秒 = 20 tick） */
    private static final int MIN_RECIPE_DURATION = 20;

    @Nullable
    protected TickableSubscription ritualTickSubs;

    public BloodManaHatch hatch;

    @Persisted
    @Nullable
    public UUID ritualOwnerId;

    /**
     * 假灵魂网络 LP，持久化存储。
     * {@link MachineRitualSoulNetwork} 无法存档，成型/加载后需 {@link #syncPersistedLpToSoulNetwork()} 写回。
     * 仪式消耗从此扣除，配方运行前从凝聚仓尽可能补满至 {@link #RITUAL_LP_CACHE_CAPACITY}。
     */
    @Persisted
    public int ritualNetworkLp;

    /** 非持久化；Orb 变更或成型时重建 */
    @Nullable
    public MachineRitualSoulNetwork ritualSoulNetwork;

    public RitualMechanicalMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    //////////////////////////////////////
    // ******** HatchVision ********//
    //////////////////////////////////////
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch();
        if (this.hatch == null) {
            onStructureInvalid();
            return;
        }
        refreshRitualSoulNetwork();
        syncPersistedLpToSoulNetwork();
    }

    @Override
    public void onStructureInvalid() {
        this.hatch = null;
        this.ritualSoulNetwork = null;
        this.ritualOwnerId = null;
        super.onStructureInvalid();
    }

    @Override
    @Nullable
    public BloodManaHatch getHatch() {
        for (IMultiPart part : getParts()) {
            if (part instanceof BloodManaHatch bloodHatch) {
                hatchPos = bloodHatch.getPos();
                return bloodHatch;
            }
        }
        return null;
    }

    //////////////////////////////////////
    // ******** Recipe ********//
    //////////////////////////////////////
    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) {
            return false;
        }
        refreshRitualSoulNetwork();
        if (hatch == null || !hatch.HAVE_ORB || ritualSoulNetwork == null || ritualOwnerId == null) {
            RecipeLogic.putFailureReason(this, recipe, failureNoBloodOrb.translate());
            return false;
        }
        String ritualId = recipe.data.getString(RECIPE_DATA_RITUAL_ID);
        if (ritualId.isEmpty() || BloodMagic.RITUAL_MANAGER.getRitual(ritualId) == null) {
            RecipeLogic.putFailureReason(this, recipe, failureUnknownRitual.translate(ritualId));
            return false;
        }
        if (ONLINE_OWNER_RITUALS.contains(ritualId)) {
            var server = getLevel() != null ? getLevel().getServer() : null;
            if (server == null || server.getPlayerList().getPlayer(ritualOwnerId) == null) {
                RecipeLogic.putFailureReason(this, recipe, failureOwnerOffline.translate());
                return false;
            }
        }
        fillLpCacheFromHatch();
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        GTRecipe recipe = getRecipeLogic().getLastRecipe();
        if (recipe != null && hatch != null && ritualSoulNetwork != null && getLevel() != null &&
                !getLevel().isClientSide) {
            String ritualId = recipe.data.getString(RECIPE_DATA_RITUAL_ID);
            Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(ritualId);
            if (ritual != null) {
                syncPersistedLpToSoulNetwork();
                int essenceBefore = ritualSoulNetwork.getCurrentEssence();
                Ritual copy = ritual.getNewCopy();
                var host = new MachineRitualStoneHost(this, hatch, ritualSoulNetwork, copy);
                copy.performRitual(host);
                ritualNetworkLp = Math.max(0, ritualNetworkLp - ritualSoulNetwork.getDrainedAmount(essenceBefore));
            }
        }
        if (getLevel() != null && !getLevel().isClientSide) {
            collectDroppedItemsAbove();
        }
        super.afterWorking();
    }

    /**
     * 将仪式作用范围内的掉落物收入输出仓。
     * 仅当每个物品堆叠都能完整放入输出仓时才拾取（输出空间不足则留在原地）。
     */
    private void collectDroppedItemsAbove() {
        if (getLevel() == null || getLevel().isClientSide) {
            return;
        }
        List<IRecipeHandler<?>> outputHandlers = getCapabilitiesFlat(IO.OUT, ItemRecipeCapability.CAP);
        if (outputHandlers.isEmpty()) {
            return;
        }

        AABB collectArea = MachineRitualStoneHost.FIXED_RANGE.getAABB(getPos());
        List<ItemEntity> droppedItems = new ArrayList<>(
                getLevel().getEntitiesOfClass(ItemEntity.class, collectArea));

        for (ItemEntity entity : droppedItems) {
            ItemStack stack = entity.getItem();
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack simulated = insertIntoOutputs(outputHandlers, stack.copy(), true);
            if (!simulated.isEmpty()) {
                continue;
            }
            insertIntoOutputs(outputHandlers, stack.copy(), false);
            entity.discard();
        }
    }

    private static ItemStack insertIntoOutputs(List<IRecipeHandler<?>> outputHandlers, ItemStack stack,
                                               boolean simulate) {
        ItemStack remain = stack;
        for (IRecipeHandler<?> outputHandler : outputHandlers) {
            if (remain.isEmpty()) {
                break;
            }
            if (outputHandler instanceof NotifiableItemStackHandler outHandler) {
                remain = CTNHManaUtils.insertItemToOutput(outHandler, remain, simulate);
            }
        }
        return remain;
    }

    //////////////////////////////////////
    // ******** LP & SoulNetwork ********//
    //////////////////////////////////////
    /** Orb 放入/更换后由凝聚仓回调、每配方前或定时 tick 调用，重建虚拟灵魂网络。 */
    public void refreshRitualSoulNetwork() {
        if (hatch == null) {
            ritualSoulNetwork = null;
            ritualOwnerId = null;
            return;
        }
        UUID owner = getOrbOwnerId(hatch);
        ritualOwnerId = owner;
        if (owner == null) {
            ritualSoulNetwork = null;
            return;
        }
        if (ritualSoulNetwork == null || !owner.equals(ritualSoulNetwork.getOwnerId())) {
            ritualNetworkLp = 0;
            ritualSoulNetwork = new MachineRitualSoulNetwork(owner);
        }
        syncPersistedLpToSoulNetwork();
    }

    /** 将持久化的 {@link #ritualNetworkLp} 写回非持久化的假灵魂网络。 */
    private void syncPersistedLpToSoulNetwork() {
        if (ritualSoulNetwork != null) {
            ritualSoulNetwork.syncFromCache(ritualNetworkLp);
        }
    }

    /** 在 LP 未满时，从凝聚仓尽可能抽取 LP 填入 {@link #ritualNetworkLp}。 */
    public void fillLpCacheFromHatch() {
        if (hatch == null) {
            return;
        }
        int space = RITUAL_LP_CACHE_CAPACITY - ritualNetworkLp;
        if (space <= 0) {
            return;
        }
        ritualNetworkLp += drainLp(hatch, space);
        syncPersistedLpToSoulNetwork();
    }

    /** 保证配方时长不低于 {@link #MIN_RECIPE_DURATION} tick（1 秒）。 */
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof RitualMechanicalMachine)) {
            return ModifierFunction.NULL;
        }
        if (recipe.duration >= MIN_RECIPE_DURATION) {
            return ModifierFunction.IDENTITY;
        }
        return ModifierFunction.builder()
                .durationMultiplier((double) MIN_RECIPE_DURATION / recipe.duration)
                .build();
    }

    @Nullable
    public MachineRitualSoulNetwork getRitualSoulNetwork() {
        return ritualSoulNetwork;
    }

    /** 凝聚仓血 Orb 绑定主人的 UUID；未绑定则 null。 */
    @Nullable
    public static UUID getOrbOwnerId(@Nullable BloodManaHatch hatch) {
        if (hatch == null || hatch.getBlood_inventory().isEmpty()) {
            return null;
        }
        var stack = hatch.getBlood_inventory().getStackInSlot(0);
        if (!(stack.getItem() instanceof ItemBloodOrb orb)) {
            return null;
        }
        Binding binding = orb.getBinding(stack);
        return binding != null ? binding.getOwnerId() : null;
    }

    /** 凝聚仓内可用于仪式的 LP 总量（内部 Mana 折算 + 生命源质流体 mB）。 */
    public static long getAvailableLp(@Nullable BloodManaHatch hatch) {
        if (hatch == null) {
            return 0;
        }
        long lp = hatch.Mana * (long) hatch.LP_CONVERT_RATE;
        if (!hatch.getFluidTank().isEmpty()) {
            FluidStack fluid = hatch.getFluidTank().getFluidInTank(0);
            if (fluid.containsFluid(new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1))) {
                lp += fluid.getAmount();
            }
        }
        return lp;
    }

    /**
     * 从凝聚仓扣除 LP（先扣 Mana 折算，再扣生命源质流体）。
     *
     * @return 实际扣除的 LP 量
     */
    public static int drainLp(@Nullable BloodManaHatch hatch, int amount) {
        if (hatch == null || amount <= 0) {
            return 0;
        }
        int remaining = amount;
        long lpInMana = hatch.Mana * (long) hatch.LP_CONVERT_RATE;
        if (remaining > 0 && lpInMana > 0) {
            int fromMana = (int) Math.min(remaining, lpInMana);
            int manaDrain = (fromMana + hatch.LP_CONVERT_RATE - 1) / hatch.LP_CONVERT_RATE;
            hatch.Mana = Math.max(0, hatch.Mana - manaDrain);
            remaining -= fromMana;
        }
        if (remaining > 0 && !hatch.getFluidTank().isEmpty()) {
            FluidStack fluid = hatch.getFluidTank().getFluidInTank(0);
            if (fluid.containsFluid(new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 1))) {
                int drain = Math.min(remaining, fluid.getAmount());
                fluid.shrink(drain);
                remaining -= drain;
            }
        }
        return amount - remaining;
    }

    //////////////////////////////////////
    // ******** Subscriptions&Ticks ********//
    //////////////////////////////////////
    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, () -> {
                subscribeRitualTick();
                if (isFormed()) {
                    if (hatch == null) {
                        hatch = getHatch();
                    }
                    refreshRitualSoulNetwork();
                }
            }));
        }
    }

    @Override
    public void onUnload() {
        if (ritualTickSubs != null) {
            ritualTickSubs.unsubscribe();
            ritualTickSubs = null;
        }
        super.onUnload();
    }

    private void subscribeRitualTick() {
        ritualTickSubs = subscribeServerTick(ritualTickSubs, this::ritualServerTick);
    }

    // 每 5 秒检查凝聚仓血 Orb 是否就绪并刷新绑定 / 虚拟灵魂网络
    private void ritualServerTick() {
        if (!isFormed() || getLevel() == null || getLevel().isClientSide) {
            return;
        }
        if (getOffsetTimer() % ORB_CHECK_INTERVAL != 0) {
            return;
        }
        if (hatch == null) {
            hatch = getHatch();
        }
        refreshRitualSoulNetwork();
    }

    //////////////////////////////////////
    // ******** UI ********//
    //////////////////////////////////////
    @Override
    public ModularUI createUI(Player entityPlayer) {
        int w = sc(198), h = sc(208);
        return new ModularUI(w, h, this, entityPlayer).widget(new FancyMachineUIWidget(this, w, h));
    }

    @Override
    public @NotNull Widget createUIWidget() {
        int gw = sc(190), gh = sc(125);
        WidgetGroup widget = new WidgetGroup(0, 0, gw, gh);
        int m = sc(4);
        var group = (new DraggableScrollableWidgetGroup(m, m, sc(182), sc(117)))
                .setBackground(this.getScreenTexture())
                .addWidget(new LabelWidget(m, sc(5), this.self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(m, sc(17), this::addDisplayText)
                        .textSupplier(this.getLevel().isClientSide ? null : this::addDisplayText)
                        .setMaxWidthLimit(sc(200))
                        .clickHandler(this::handleDisplayClick));
        widget.setBackground(new IGuiTexture[] { GuiTextures.BACKGROUND_INVERSE });
        widget.addWidget(group);
        return widget;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (!isFormed()) {
            return;
        }
        if (ritualSoulNetwork != null && ritualOwnerId != null && hatch != null && hatch.HAVE_ORB) {
            textList.add(bloodOrbStatusLang[0].translate());
            textList.add(ritualLpCacheLang.translate(
                    FormattingUtil.formatNumbers(ritualNetworkLp),
                    FormattingUtil.formatNumbers(RITUAL_LP_CACHE_CAPACITY)));
        } else {
            textList.add(bloodOrbStatusLang[1].translate());
        }
        if (isActive()) {
            GTRecipe recipe = getRecipeLogic().getLastRecipe();
            if (recipe == null) {
                recipe = getRecipeLogic().getLastOriginRecipe();
            }
            Component ritualName = getRitualDisplayName(recipe);
            if (ritualName != null) {
                textList.add(runningRitualLang.translate(ritualName));
            }
        }
    }

    @Nullable
    private static Component getRitualDisplayName(@Nullable GTRecipe recipe) {
        if (recipe == null) {
            return null;
        }
        return getRitualDisplayComponent(recipe.data.getString(RECIPE_DATA_RITUAL_ID));
    }

    @Nullable
    public static Component getRitualDisplayComponent(String ritualId) {
        if (ritualId.isEmpty()) {
            return null;
        }
        Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(ritualId);
        if (ritual != null) {
            return Component.translatable(ritual.getTranslationKey());
        }
        return Component.literal(ritualId);
    }

    /** JEI / 配方 UI 中「仪式：xxx」一行文案 */
    public static String formatRitualRecipeTip(CompoundTag data) {
        String ritualId = data.getString(RECIPE_DATA_RITUAL_ID);
        Component name = getRitualDisplayComponent(ritualId);
        return LocalizationUtils.format("ctnhmana.recipe.blood_ritual.ritual_id",
                name != null ? name.getString() : ritualId);
    }

    public static int getRitualLpCost(String ritualId) {
        if (ritualId.isEmpty()) {
            return 0;
        }
        Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(ritualId);
        if (ritual == null) {
            return 0;
        }
        return ritual.getRefreshCost();
    }

    /** JEI / 配方 UI 中「消耗 LP：xxx」一行文案 */
    public static String formatRitualLpTip(CompoundTag data) {
        String ritualId = data.getString(RECIPE_DATA_RITUAL_ID);
        int lp = data.contains(RECIPE_DATA_RITUAL_LP) ? data.getInt(RECIPE_DATA_RITUAL_LP) : getRitualLpCost(ritualId);
        return LocalizationUtils.format("ctnhmana.recipe.blood_ritual.lp_cost",
                FormattingUtil.formatNumbers(lp));
    }

    //////////////////////////////////////
    // ******** Lang ********//
    //////////////////////////////////////
    @CN({
            "§4如果我们能把所有仪式收录在脑子里，为什么不能把它们收录在机器里？§r",
            "§c必须§r安装 §4血魔法凝聚仓§r，并在凝聚仓中放入 §4已绑定的宝珠§r 以指定仪式主人",
            "每完成一次配方，在整个机器占地范围内（半径21格）执行一次仪式",
            "配方持续时间即为仪式冷却；LP 从凝聚仓内魔力/液态生命源质扣除，§c不消耗§r玩家灵魂网络",
            "虚拟灵魂网络拥有 1亿 LP 缓存，每次运行前都尝试向内充能尽可能多的LP",
            "战争呼唤、虚境之视仪式需要主人在线",
            "每完成一次配方，将仪式作用范围内的掉落物收入输出舱室",
            "警告：执行坠星位标仪式，后果自负"
    })
    @EN({
            "§4Industrial Blood Ritual Array§r",
            "§cRequires§r a §4Blood Mana Condenser§r with a §4bound blood orb§r to designate the ritual owner",
            "Each completed recipe runs one Blood Magic ritual within a §n21§r-block radius (including height) of the controller",
            "Recipe duration is the ritual cooldown; LP is drained from condenser storage, §cnot§r the player's soul network",
            "Virtual soul network holds up to 100 million LP cache; refilled from the condenser before each recipe run",
            "Each recipe consumes a unique catalyst item",
            "War Call and Shroud Sight rituals require the owner to be online",
            "After each recipe, dropped items within the ritual area are moved to output buses if space allows",
            "警告：执行坠星位标仪式，后果自负"
    })
    public static Lang[] ritualMechanicalLang;

    @CN("凝聚仓未放入已绑定的血Orb")
    @EN("Blood condenser has no bound blood orb")
    public static Lang failureNoBloodOrb;

    @CN("未知仪式：%s")
    @EN("Unknown ritual: %s")
    public static Lang failureUnknownRitual;

    @CN("仪式主人必须在线")
    @EN("Ritual owner must be online")
    public static Lang failureOwnerOffline;
    @CN({
            "§4已检测到灵魂网络",
            "未检测到灵魂网络：请确保宝珠已经放入凝聚仓中"
    })
    @EN({
            "§4Soul network detected",
            "Soul network not detected: ensure a bound blood orb is placed in the condenser"
    })
    public static Lang[] bloodOrbStatusLang;

    @CN("LP 缓存：%s / %s")
    @EN("LP Cache: %s / %s")
    public static Lang ritualLpCacheLang;

    @CN("当前运行的仪式：%s")
    @EN("Currently running ritual: %s")
    public static Lang runningRitualLang;
}
