package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import com.moguang.ctnhmana.common.ritual.MachineRitualSoulNetwork;
import com.moguang.ctnhmana.common.ritual.MachineRitualStoneHost;
import com.moguang.ctnhmana.Mutiblock.parts.ManaHatches.BloodManaHatch;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.ItemBloodOrb;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.ritual.Ritual;

import java.util.Set;
import java.util.UUID;

/**
 * 工业血祭仪式阵控制器。
 * <p>
 * LP 通过 {@link MachineRitualSoulNetwork} 从凝聚仓储罐扣除，<b>不消耗</b>玩家全局灵魂网络。
 * Orb 仅用于绑定仪式主人 UUID（{@link #getOrbOwnerId}）。
 */
public class RitualMechanicalMachine extends ManaMachine {

    public static final String RECIPE_DATA_RITUAL_ID = "ritual_id";

    /** 需要主人在线（{@code ServerPlayer}）的仪式 ID */
    private static final Set<String> ONLINE_OWNER_RITUALS = Set.of("bosssummon", "shroudsight");

    public BloodManaHatch hatch;

    @Persisted
    @Nullable
    public UUID ritualOwnerId;

    /** 非持久化；Orb 变更或成型时重建 */
    @Nullable
    public MachineRitualSoulNetwork ritualSoulNetwork;

    public RitualMechanicalMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.hatch = getHatch();
        if (this.hatch == null) {
            onStructureInvalid();
            return;
        }
        refreshRitualSoulNetwork();
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

    /** Orb 放入/更换后由凝聚仓回调或每配方前调用，重建虚拟灵魂网络。 */
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
            ritualSoulNetwork = new MachineRitualSoulNetwork(owner, hatch);
        }
    }

    @Nullable
    public MachineRitualSoulNetwork getRitualSoulNetwork() {
        return ritualSoulNetwork;
    }

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
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        GTRecipe recipe = getRecipeLogic().getLastRecipe();
        if (recipe != null && hatch != null && ritualSoulNetwork != null && getLevel() != null
                && !getLevel().isClientSide) {
            String ritualId = recipe.data.getString(RECIPE_DATA_RITUAL_ID);
            Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(ritualId);
            if (ritual != null) {
                ritualSoulNetwork.syncFromHatch();
                int essenceBefore = ritualSoulNetwork.getCurrentEssence();
                Ritual copy = ritual.getNewCopy();
                var host = new MachineRitualStoneHost(this, hatch, ritualSoulNetwork, copy);
                copy.performRitual(host);
                ritualSoulNetwork.applyDrainToHatch(essenceBefore);
            }
        }
        super.afterWorking();
    }

    @CN({
            "§4工业血祭仪式阵§r",
            "§c必须§r安装 §4血魔法凝聚仓§r，并在凝聚仓中放入 §4已绑定的血Orb§r 以指定仪式主人",
            "每完成一次配方，在控制器周围 §n5×5§r 范围内执行一次血魔法仪式",
            "配方持续时间即为仪式冷却；LP 从凝聚仓内魔力/液态生命源质扣除，§c不消耗§r玩家灵魂网络",
            "配方需消耗红石粉作为仪式触媒",
            "战争呼唤、虚境之视仪式需要主人在线"
    })
    @EN({
            "§4Industrial Blood Ritual Array§r",
            "§cRequires§r a §4Blood Mana Condenser§r with a §4bound blood orb§r to designate the ritual owner",
            "Each completed recipe runs one Blood Magic ritual in a fixed §n5×5§r area centered on the controller",
            "Recipe duration is the ritual cooldown; LP is drained from condenser storage, §cnot§r the player's soul network",
            "Recipes consume redstone dust as a ritual catalyst",
            "War Call and Shroud Sight rituals require the owner to be online"
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

    // ── 凝聚仓 LP 换算（虚拟灵魂网络用，不扣玩家全局 LP）────────────────

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
}
